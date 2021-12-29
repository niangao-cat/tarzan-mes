package tarzan.iface.infra.repository.impl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.util.StringHelper;
import tarzan.iface.domain.entity.MtItemCategoryAssignIface;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtItemCategoryAssignIfaceRepository;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;
import tarzan.iface.infra.mapper.MtItemCategoryAssignIfaceMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.entity.MtMaterialCategoryAssign;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.MtMaterialCategoryAssignRepository;
import tarzan.material.domain.repository.MtMaterialCategoryRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;

/**
 * 物料与类别关系数据接口 资源库实现
 *
 * @author mingjie.chen@hand-china.com 2019-09-09 17:05:24
 */
@Component
public class MtItemCategoryAssignIfaceRepositoryImpl extends BaseRepositoryImpl<MtItemCategoryAssignIface>
                implements MtItemCategoryAssignIfaceRepository {

    private static final int SQL_ITEM_COUNT_LIMIT = 10000;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtMaterialRepository mtMaterialService;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteService;

    @Autowired
    private MtMaterialCategoryRepository mtMaterialCategoryService;

    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationService;

    @Autowired
    private MtMaterialCategoryAssignRepository mtMaterialCategoryAssignService;

    @Autowired
    private MtItemCategoryAssignIfaceMapper mtItemCategoryAssignIfaceMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Override
    public void itemCategoryAssignInterfaceImport(Long tenantId) {
        Long start = System.currentTimeMillis();
        // change data status
        List<MtItemCategoryAssignIface> mtSubinventoryIfaceList = self().updateIfaceStatus(tenantId, "P");

        // get data list
        Map<Double, List<MtItemCategoryAssignIface>> ifacePerBatch = mtSubinventoryIfaceList.stream().collect(Collectors
                        .groupingBy(MtItemCategoryAssignIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtItemCategoryAssignIface>> entry : ifacePerBatch.entrySet()) {
            try {
                List<AuditDomain> ifaceList = self().saveItemCategoryAssignIface(tenantId, entry.getValue());
                self().log(tenantId, ifaceList);
            } catch (Exception e) {
                List<AuditDomain> ifaceList = new ArrayList<>(entry.getValue().size());
                String errorMsg = StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage().replace("'", "\"") : "数据异常.";
                if (errorMsg.length() > 1000) {
                    errorMsg = errorMsg.substring(errorMsg.length() - 1000);
                }
                final String msg = errorMsg;
                entry.getValue().forEach(ifs -> {
                    ifs.setStatus("E");
                    ifs.setMessage(msg);
                });
                ifaceList.addAll(entry.getValue());
                self().log(tenantId, ifaceList);
            }
        }
        System.out.println("物料分配类别接口导入" + mtSubinventoryIfaceList.size() + "条数据，总耗时："
                        + (System.currentTimeMillis() - start) + "毫秒");

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AuditDomain> saveItemCategoryAssignIface(Long tenantId,
                    List<MtItemCategoryAssignIface> mtItemCategoryAssignIfaceList) {
        Long userId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        Date now = new Date(System.currentTimeMillis());

        List<AuditDomain> ifaceList = new ArrayList<>(mtItemCategoryAssignIfaceList.size());

        // 获取plant_site_relation
        Map<String, List<MtSitePlantReleation>> sitePlantMap =
                        getSitePlantRelation(tenantId, mtItemCategoryAssignIfaceList);
        // 获取material_site
        Map<String, List<MtMaterialSite>> materialSiteMap = getMaterialSite(tenantId, mtItemCategoryAssignIfaceList);

        // 获取MtMaterialCategory
        List<MtMaterialCategory> materialCategoryList = mtMaterialCategoryService.queryMaterialCategoryByCode(tenantId,
                        mtItemCategoryAssignIfaceList.stream().map(MtItemCategoryAssignIface::getCategoryCode)
                                        .distinct().collect(Collectors.toList()));
        Map<String, MtMaterialCategory> materialCategoryMap = null;
        Map<CategoryAssign, MtMaterialCategoryAssign> materialCategoryAssignMap = new HashMap<>();
        Map<String, List<String>> assignMap = new HashMap<>();

        // 物料类别集
        List<String> materialSiteIds = new ArrayList<>();
        if (MapUtils.isNotEmpty(materialSiteMap)) {
            for (Map.Entry<String, List<MtMaterialSite>> entry : materialSiteMap.entrySet()) {
                if (CollectionUtils.isNotEmpty(entry.getValue())) {
                    materialSiteIds.addAll(entry.getValue().stream().map(MtMaterialSite::getMaterialSiteId).distinct()
                                    .collect(Collectors.toList()));
                }
            }
        }
        Map<String, List<String>> setMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(materialCategoryList)) {
            List<MtMaterialCategoryAssign> mtMaterialCategoryAssigns = mtMaterialCategoryAssignService
                            .materialCategoryAssignByMaterilSiteIds(tenantId, materialSiteIds);
            if (CollectionUtils.isNotEmpty(mtMaterialCategoryAssigns)) {
                materialCategoryAssignMap.putAll(mtMaterialCategoryAssigns.stream().collect(Collectors.toMap(
                                c -> new CategoryAssign(c.getMaterialSiteId(), c.getMaterialCategoryId()), c -> c)));
                assignMap.putAll(mtMaterialCategoryAssigns.stream()
                                .collect(Collectors.groupingBy(MtMaterialCategoryAssign::getMaterialSiteId,
                                                Collectors.mapping(MtMaterialCategoryAssign::getMaterialCategoryId,
                                                                Collectors.toList()))));
            }

            List<MtMaterialCategory> mtMaterialCategories = mtMaterialCategoryService.queryMaterialCategoryBySetId(
                            tenantId, materialCategoryList.stream().map(MtMaterialCategory::getMaterialCategorySetId)
                                            .collect(Collectors.toList()));

            materialCategoryMap = materialCategoryList.stream()
                            .collect(Collectors.toMap(MtMaterialCategory::getCategoryCode, c -> c));

            setMap.putAll(mtMaterialCategories.stream().collect(Collectors.groupingBy(
                            MtMaterialCategory::getMaterialCategorySetId,
                            Collectors.mapping(MtMaterialCategory::getMaterialCategoryId, Collectors.toList()))));
        }

        // 循环处理
        MtMaterialCategory materialCategory;
        MtMaterialCategoryAssign categoryAssign;
        CategoryAssign categoryAssignTuple;
        List<MtMaterialCategoryAssign> changeList = new ArrayList<>();
        for (MtItemCategoryAssignIface iface : mtItemCategoryAssignIfaceList) {
            // check site plant
            if (CollectionUtils.isEmpty(sitePlantMap.get(iface.getPlantCode()))
                            || sitePlantMap.get(iface.getPlantCode()).stream()
                                            .noneMatch(s -> "MANUFACTURING".equals(s.getSiteType()))) {
                ifaceList.add(constructIfaceMessage(iface, "E",
                                mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0012",
                                                "INTERFACE", iface.getIfaceId(),
                                                "【API：itemCategoryAssignInterfaceImport】"),
                                now, userId));
                continue;
            }

            // check material site
            StringBuilder errMsgBuilder = new StringBuilder();
            if (materialSiteMap == null || CollectionUtils.isEmpty(materialSiteMap.get(iface.getItemCode()))) {
                errMsgBuilder.append(mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0026",
                                "INTERFACE", iface.getIfaceId(), "【API：itemCategoryAssignInterfaceImport】"));
                errMsgBuilder.append(mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0028",
                                "INTERFACE", iface.getIfaceId(), "【API：itemCategoryAssignInterfaceImport】"));
                errMsgBuilder.append(mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0029",
                                "INTERFACE", iface.getIfaceId(), "【API：itemCategoryAssignInterfaceImport】"));
                ifaceList.add(constructIfaceMessage(iface, "E", errMsgBuilder.toString(), now, userId));
                continue;
            }

            for (MtSitePlantReleation relation : sitePlantMap.get(iface.getPlantCode())) {
                MtMaterialSite mtMaterialSite = materialSiteMap.get(iface.getItemCode()).stream()
                                .filter(s -> s.getSiteId().equals(relation.getSiteId())).findAny().orElse(null);
                if (mtMaterialSite == null) {
                    switch (relation.getSiteType()) {
                        case "MANUFACTURING":
                            errMsgBuilder.append(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                            "MT_INTERFACE_0026", "INTERFACE", iface.getIfaceId(),
                                            "【API：itemCategoryAssignInterfaceImport】"));
                            break;
                        case "SCHEDULE":
                            errMsgBuilder.append(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                            "MT_INTERFACE_0028", "INTERFACE", iface.getIfaceId(),
                                            "【API：itemCategoryAssignInterfaceImport】"));
                            break;
                        case "PURCHASE":
                            errMsgBuilder.append(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                            "MT_INTERFACE_0029", "INTERFACE", iface.getIfaceId(),
                                            "【API：itemCategoryAssignInterfaceImport】"));
                            break;
                        default:
                            break;
                    }
                }
            }
            if (StringUtils.isNotEmpty(errMsgBuilder)) {
                ifaceList.add(constructIfaceMessage(iface, "E", errMsgBuilder.toString(), now, userId));
                continue;
            }

            // check material category
            if (MapUtils.isEmpty(materialCategoryMap) || materialCategoryMap.get(iface.getCategoryCode()) == null) {
                ifaceList.add(constructIfaceMessage(iface, "E",
                                mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0017",
                                                "INTERFACE", iface.getIfaceId(),
                                                "【API：itemCategoryAssignInterfaceImport】"),
                                now, userId));
                continue;
            }
            materialCategory = materialCategoryMap.get(iface.getCategoryCode());
            for (MtMaterialSite mtMaterialSite : materialSiteMap.get(iface.getItemCode())) {
                if (sitePlantMap.get(iface.getPlantCode()).stream().map(MtSitePlantReleation::getSiteId)
                                .noneMatch(p -> p.equals(mtMaterialSite.getSiteId()))) {
                    continue;
                }
                categoryAssignTuple = new CategoryAssign(mtMaterialSite.getMaterialSiteId(),
                                materialCategory.getMaterialCategoryId());

                List<String> setList = setMap.get(materialCategory.getMaterialCategorySetId());
                List<String> assignList = assignMap.get(mtMaterialSite.getMaterialSiteId());
                if (materialCategoryAssignMap.get(categoryAssignTuple) == null) {
                    if (CollectionUtils.isNotEmpty(setList) && CollectionUtils.isNotEmpty(assignList)) {
                        setList.retainAll(assignList);
                        if (CollectionUtils.isNotEmpty(setList)) {
                            categoryAssignTuple =
                                            new CategoryAssign(mtMaterialSite.getMaterialSiteId(), setList.get(0));
                        }
                    }

                }
                categoryAssign = materialCategoryAssignMap.get(categoryAssignTuple);
                if (categoryAssign == null) {
                    // 新增
                    categoryAssign = new MtMaterialCategoryAssign();
                    categoryAssign.setCreatedBy(userId);
                    categoryAssign.setCreationDate(now);
                    categoryAssign.setObjectVersionNumber(1L);
                } else {
                    categoryAssign.setObjectVersionNumber(categoryAssign.getObjectVersionNumber() + 1L);
                }
                categoryAssign.setTenantId(tenantId);
                categoryAssign.setMaterialSiteId(mtMaterialSite.getMaterialSiteId());
                categoryAssign.setMaterialCategoryId(materialCategory.getMaterialCategoryId());
                categoryAssign.setLastUpdatedBy(userId);
                categoryAssign.setLastUpdateDate(now);
                changeList.add(categoryAssign);
            }
            ifaceList.add(constructIfaceMessage(iface, "S", "成功.", now, userId));
        }
        /**
         * 写入Id、CID
         */
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(changeList)) {
            List<String> cids = this.customDbRepository.getNextKeys("mt_material_category_assign_cid_s", changeList.size());
            List<MtMaterialCategoryAssign> insertList =
                            changeList.stream().filter(t -> StringUtils.isEmpty(t.getMaterialCategoryAssignId()))
                                            .collect(Collectors.toList());
            List<String> ids = this.customDbRepository.getNextKeys("mt_material_category_assign_s", insertList.size());
            List<AuditDomain> tempList = new ArrayList<>(changeList.size());
            changeList.stream().forEach(c -> {
                if (StringUtils.isEmpty(c.getMaterialCategoryAssignId())) {
                    c.setMaterialCategoryAssignId(ids.remove(0));
                }
                c.setCid(Long.valueOf(cids.remove(0)));
            });
            tempList.addAll(changeList);
            sqlList.addAll(constructSql(tempList));
            changeList = null;
            tempList = null;
        }
        /**
         * 保存数据
         */
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
            sqlList.clear();
        }
        return ifaceList;
    }

    /**
     * 根据工厂编码获取工厂与站点关系
     *
     * @author benjamin
     * @date 2019-06-27 10:21
     * @param tenantId IRequest
     * @param mtItemCategoryAssignIfaceList List<MtItemCategoryAssignIface>
     * @return Map<String, MtSitePlantReleation>
     */
    private Map<String, List<MtSitePlantReleation>> getSitePlantRelation(Long tenantId,
                    List<MtItemCategoryAssignIface> mtItemCategoryAssignIfaceList) {
        List<String> plantCodeList = mtItemCategoryAssignIfaceList.stream()
                        .filter(p -> StringUtils.isNotEmpty(p.getPlantCode()))
                        .map(MtItemCategoryAssignIface::getPlantCode).distinct().collect(Collectors.toList());
        Map<String, List<MtSitePlantReleation>> sitePlantMap = null;
        if (CollectionUtils.isNotEmpty(plantCodeList)) {
            MtSitePlantReleationVO3 mtSitePlantReleationVO3 = new MtSitePlantReleationVO3();
            mtSitePlantReleationVO3.setPlantCodes(plantCodeList);
            List<MtSitePlantReleation> relationByPlantAndSiteType = mtSitePlantReleationService
                            .getRelationByPlantAndSiteType(tenantId, mtSitePlantReleationVO3);
            if (CollectionUtils.isNotEmpty(relationByPlantAndSiteType)) {
                sitePlantMap = relationByPlantAndSiteType.stream()
                                .collect(Collectors.groupingBy(MtSitePlantReleation::getPlantCode));
            }
        }
        return sitePlantMap;
    }

    /**
     * 根据物料获取物料站点关系
     *
     * @author benjamin
     * @date
     * @date 2019-09-02 15:05
     * @param tenantId 租户Id
     * @param mtItemCategoryAssignIfaceList List<MtItemCategoryAssignIface>
     * @return Map<ItemPlant, MtInterFaceVO1>
     */
    private Map<String, List<MtMaterialSite>> getMaterialSite(Long tenantId,
                    List<MtItemCategoryAssignIface> mtItemCategoryAssignIfaceList) {
        List<MtMaterial> materialList = mtMaterialService.queryMaterialByCode(tenantId, mtItemCategoryAssignIfaceList
                        .stream().map(MtItemCategoryAssignIface::getItemCode).distinct().collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(materialList)) {
            return null;
        }

        List<MtMaterialSite> materialSiteList = mtMaterialSiteService.queryMaterialSiteByMaterialId(tenantId,
                        materialList.stream().map(MtMaterial::getMaterialId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(materialSiteList)) {
            return null;
        }

        Map<String, List<MtMaterialSite>> materialSiteMap = new HashMap<>(materialList.size());
        for (MtMaterial material : materialList) {
            List<MtMaterialSite> currentMaterialSiteList =
                            materialSiteList.stream().filter(s -> material.getMaterialId().equals(s.getMaterialId()))
                                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(currentMaterialSiteList)) {
                materialSiteMap.put(material.getMaterialCode(), currentMaterialSiteList);
            }
        }
        return materialSiteMap;
    }

    /**
     * 构建返回消息
     *
     * @param mtItemCategoryAssignIface MtItemCategoryAssignIface
     * @param message 错误消息
     * @return MtItemCategoryAssignIface
     * @author benjamin
     * @date 2019-06-27 17:02
     */
    private MtItemCategoryAssignIface constructIfaceMessage(MtItemCategoryAssignIface mtItemCategoryAssignIface,
                    String status, String message, Date date, Long userId) {
        mtItemCategoryAssignIface.setStatus(status);
        mtItemCategoryAssignIface.setMessage(message);
        mtItemCategoryAssignIface.setLastUpdateDate(date);
        mtItemCategoryAssignIface.setLastUpdatedBy(userId);

        return mtItemCategoryAssignIface;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<MtItemCategoryAssignIface> updateIfaceStatus(Long tenantId, String status) {
        List<MtItemCategoryAssignIface> list = mtItemCategoryAssignIfaceMapper.getUnprocessedList(tenantId);
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> sqlList = new ArrayList<>(list.size());
            List<AuditDomain> auditDomains = new ArrayList<>(list.size());
            list.stream().forEach(ifs -> {
                ifs.setStatus(status);
                ifs.setTenantId(tenantId);
            });
            auditDomains.addAll(list);
            sqlList.addAll(constructSql(auditDomains));
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void log(Long tenantId, List<AuditDomain> ifaceSqlList) {
        if (CollectionUtils.isNotEmpty(ifaceSqlList)) {
            List<String> sqlList = constructSql(ifaceSqlList);
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
    }

    /**
     * 分割数据集合 限制数量每项不多于SQL_ITEM_COUNT_LIMIT
     *
     * @author benjamin
     * @date 2019-06-25 18:40
     * @return List<List>
     */
    private List<String> constructSql(List<AuditDomain> ifaceSqlList) {
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ifaceSqlList)) {
            List<List<AuditDomain>> splitSqlList = StringHelper.splitSqlList(ifaceSqlList, SQL_ITEM_COUNT_LIMIT);
            for (List<AuditDomain> domains : splitSqlList) {
                sqlList.addAll(customDbRepository.getReplaceSql(domains));
            }
        }
        return sqlList;
    }

    private List<List<String>> commitSqlList(List<String> sqlList, int splitNum) {

        List<List<String>> returnList = new ArrayList<>();
        if (sqlList.size() <= splitNum) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / splitNum;
            int splitRest = sqlList.size() % splitNum;

            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * splitNum, (i + 1) * splitNum));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * splitNum, sqlList.size()));
            }
        }
        return returnList;
    }

    private static class CategoryAssign implements Serializable {
        private static final long serialVersionUID = -7208142176025646546L;
        private String materialSiteId;
        private String materialCategoryId;

        public CategoryAssign(String materialSiteId, String materialCategoryId) {
            this.materialSiteId = materialSiteId;
            this.materialCategoryId = materialCategoryId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CategoryAssign that = (CategoryAssign) o;
            return Objects.equals(materialSiteId, that.materialSiteId)
                            && Objects.equals(materialCategoryId, that.materialCategoryId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(materialSiteId, materialCategoryId);
        }
    }
}
