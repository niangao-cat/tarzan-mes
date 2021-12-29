package tarzan.iface.infra.repository.impl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.helper.LanguageHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.domain.vo.MtExtendVO5;
import tarzan.iface.domain.entity.MtItemCategoryIface;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtItemCategoryIfaceRepository;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;
import tarzan.iface.infra.mapper.MtItemCategoryIfaceMapper;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.entity.MtMaterialCategorySet;
import tarzan.material.domain.entity.MtMaterialCategorySite;
import tarzan.material.domain.repository.MtMaterialCategoryRepository;
import tarzan.material.domain.repository.MtMaterialCategorySetRepository;
import tarzan.material.domain.repository.MtMaterialCategorySiteRepository;

/**
 * 物料类别数据接口 资源库实现
 *
 * @author mingjie.chen@hand-china.com 2019-09-09 17:05:24
 */
@Component
public class MtItemCategoryIfaceRepositoryImpl extends BaseRepositoryImpl<MtItemCategoryIface>
                implements MtItemCategoryIfaceRepository {

    private static final int SQL_ITEM_COUNT_LIMIT = 10000;

    private static final String MATERIAL_CATEGORY_ATTR_TABLE = "mt_material_category_attr";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtMaterialCategorySetRepository mtMaterialCategorySetRepo;

    @Autowired
    private MtMaterialCategoryRepository mtMaterialCategoryRepo;

    @Autowired
    private MtMaterialCategorySiteRepository mtMaterialCategorySiteRepo;

    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepo;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepo;

    @Autowired
    private MtItemCategoryIfaceMapper mtItemCategoryIfaceMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Override
    public void itemCategoryInterfaceImport(Long tenantId) {
        // change data status
        Long start = System.currentTimeMillis();
        List<MtItemCategoryIface> mtSubinventoryIfaceList = self().updateIfaceStatus(tenantId, "P");

        // get data list
        Map<Double, List<MtItemCategoryIface>> ifacePerBatch = mtSubinventoryIfaceList.stream().collect(
                        Collectors.groupingBy(MtItemCategoryIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtItemCategoryIface>> entry : ifacePerBatch.entrySet()) {
            try {
                List<AuditDomain> ifaceList = self().saveItemCategoryIface(tenantId, entry.getValue());
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
        System.out.println("category接口导入接口导入" + mtSubinventoryIfaceList.size() + "条数据，总耗时："
                        + (System.currentTimeMillis() - start) + "毫秒");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AuditDomain> saveItemCategoryIface(Long tenantId, List<MtItemCategoryIface> mtItemCategoryIfaceList) {

        Long userId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        Date now = new Date(System.currentTimeMillis());

        // group by category set
        Map<String, List<MtItemCategoryIface>> ifacePerCategorySet = mtItemCategoryIfaceList.stream()
                        .collect(Collectors.groupingBy(MtItemCategoryIface::getCategorySetCode));

        List<AuditDomain> resultList = new ArrayList<>(mtItemCategoryIfaceList.size());

        // get plant site relation
        Map<String, List<MtSitePlantReleation>> sitePlantMap = getSitePlantRelation(tenantId, mtItemCategoryIfaceList);

        // get material category set list
        List<String> categorySetCodeList = new ArrayList<>(ifacePerCategorySet.keySet());
        List<MtMaterialCategorySet> originCategorySetList =
                        mtMaterialCategorySetRepo.queryMaterialCategorySetByCode(tenantId, categorySetCodeList);
        Map<String, List<MtMaterialCategorySet>> existCategorySetPerCode = originCategorySetList.stream()
                        .collect(Collectors.groupingBy(MtMaterialCategorySet::getCategorySetCode));

        // get material category list
        List<String> categorySetIdList = originCategorySetList.stream()
                        .map(MtMaterialCategorySet::getMaterialCategorySetId).distinct().collect(Collectors.toList());
        List<MtMaterialCategory> mtMaterialCategories =
                        mtMaterialCategoryRepo.queryMaterialCategoryBySetId(tenantId, categorySetIdList);
        Map<String, MtMaterialCategory> categoryPerSet = mtMaterialCategories.stream()
                        .collect(Collectors.toMap(MtMaterialCategory::getCategoryCode, c -> c));

        // 获取 categoryId
        List<String> categoryIds = mtMaterialCategories.stream().map(MtMaterialCategory::getMaterialCategoryId)
                        .collect(Collectors.toList());

        List<String> materialCategoryDisIds = categoryIds.stream().distinct().collect(Collectors.toList());
        Map<CategorySite, MtMaterialCategorySite> materialCategorySiteMap = null;
        if (CollectionUtils.isNotEmpty(materialCategoryDisIds)) {
            List<MtMaterialCategorySite> mtMaterialCategorySites =
                            mtMaterialCategorySiteRepo.selectByMaterialCategoryIds(tenantId, materialCategoryDisIds);
            if (CollectionUtils.isNotEmpty(mtMaterialCategorySites)) {
                materialCategorySiteMap = mtMaterialCategorySites.stream().collect(Collectors
                                .toMap(c -> new CategorySite(c.getSiteId(), c.getMaterialCategoryId()), c -> c));
            }
        }
        // 批量获取Id、Cid
        List<String> materialCategorySetIds =
                        this.customDbRepository.getNextKeys("mt_material_category_set_s", ifacePerCategorySet.size());
        List<String> materialCategorySetCids =
                        this.customDbRepository.getNextKeys("mt_material_category_set_cid_s", ifacePerCategorySet.size());

        List<String> materialCategoryIds =
                        this.customDbRepository.getNextKeys("mt_material_category_s", mtItemCategoryIfaceList.size());
        List<String> materialCategoryCids =
                        this.customDbRepository.getNextKeys("mt_material_category_cid_s", mtItemCategoryIfaceList.size());

        // save material category set
        MtMaterialCategorySet categorySet;
        MtMaterialCategory category;
        MtMaterialCategorySite categorySite;
        List<AuditDomain> materialCategorySetList = new ArrayList<>(ifacePerCategorySet.size());
        List<AuditDomain> materialCategoryList = new ArrayList<>(mtItemCategoryIfaceList.size());
        List<MtMaterialCategorySite> materialCategorySiteList = new ArrayList<>();
        CategorySite categorySite1;
        Map<MtItemCategoryIface, String> attrMap = new HashMap<>(mtItemCategoryIfaceList.size());
        for (Map.Entry<String, List<MtItemCategoryIface>> entry : ifacePerCategorySet.entrySet()) {
            boolean categorySetNewFlag = false;
            MtItemCategoryIface headCategorySetIface = entry.getValue().get(0);

            if (CollectionUtils.isNotEmpty(existCategorySetPerCode.get(entry.getKey()))) {
                categorySet = existCategorySetPerCode.get(entry.getKey()).get(0);
                categorySet.setObjectVersionNumber(categorySet.getObjectVersionNumber() + 1L);
            } else {
                categorySet = new MtMaterialCategorySet();
                categorySet.setObjectVersionNumber(1L);
                categorySet.setCreatedBy(userId);
                categorySet.setCreationDate(now);
                categorySet.setMaterialCategorySetId(materialCategorySetIds.remove(0));
                categorySetNewFlag = true;
            }
            Map<String, String> categorySetTlMap = new HashMap<>(1);
            categorySetTlMap.put("description", headCategorySetIface.getCategorySetDescription());
            categorySet.set_tls(getTlsMap(categorySetTlMap));
            categorySet.setCategorySetCode(headCategorySetIface.getCategorySetCode());
            categorySet.setDescription(headCategorySetIface.getCategorySetDescription());
            categorySet.setEnableFlag("Y");
            categorySet.setTenantId(tenantId);
            categorySet.setCid(Long.valueOf(materialCategorySetCids.remove(0)));
            categorySet.setLastUpdateDate(now);
            categorySet.setLastUpdatedBy(userId);
            materialCategorySetList.add(categorySet);
            // save material category
            Map<String, List<MtItemCategoryIface>> ifacePerCategory = entry.getValue().stream()
                            .collect(Collectors.groupingBy(MtItemCategoryIface::getCategoryCode));

            for (Map.Entry<String, List<MtItemCategoryIface>> categoryEntry : ifacePerCategory.entrySet()) {
                boolean categoryNewFlag = categorySetNewFlag;
                for (MtItemCategoryIface iface : categoryEntry.getValue()) {
                    if (StringUtils.isNotEmpty(iface.getPlantCode())) {
                        if (sitePlantMap.size() == 0 || sitePlantMap.get(iface.getPlantCode()).stream()
                                        .noneMatch(p -> "MANUFACTURING".equals(p.getSiteType()))) {
                            throw new MtException("MT_INTERFACE_0012",
                                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0012",
                                                            "INTERFACE", iface.getIfaceId(),
                                                            "【API：itemCategoryInterfaceImport】"));
                        }
                    }

                    if (categorySetNewFlag || categoryPerSet.get(iface.getCategoryCode()) == null) {
                        category = new MtMaterialCategory();
                        categoryNewFlag = true;
                    } else {
                        category = categoryPerSet.get(iface.getCategoryCode());
                        categoryNewFlag = false;
                    }
                    if (categoryNewFlag) {
                        category.setObjectVersionNumber(1L);
                        category.setCreatedBy(userId);
                        category.setCreationDate(now);
                        category.setMaterialCategoryId(materialCategoryIds.remove(0));
                    } else {
                        category.setObjectVersionNumber(category.getObjectVersionNumber() + 1L);
                    }

                    Map<String, String> categorySiteTlMap = new HashMap<>(1);
                    categorySiteTlMap.put("description", iface.getCategoryDescription());
                    category.set_tls(getTlsMap(categorySiteTlMap));
                    category.setCategoryCode(iface.getCategoryCode());
                    category.setDescription(iface.getCategoryDescription());
                    category.setEnableFlag(iface.getEnableFlag());
                    category.setMaterialCategorySetId(categorySet.getMaterialCategorySetId());
                    category.setTenantId(tenantId);
                    category.setCid(Long.valueOf(materialCategoryCids.remove(0)));
                    category.setLastUpdateDate(now);
                    category.setLastUpdatedBy(userId);
                    materialCategoryList.add(category);
                    // save material category site
                    if (StringUtils.isNotEmpty(iface.getPlantCode())) {
                        for (MtSitePlantReleation sitePlantRelation : sitePlantMap.get(iface.getPlantCode())) {
                            categorySite1 = new CategorySite(sitePlantRelation.getSiteId(),
                                            category.getMaterialCategoryId());
                            if (!categoryNewFlag && MapUtils.isNotEmpty(materialCategorySiteMap)
                                            && materialCategorySiteMap.get(categorySite1) != null) {
                                categorySite = materialCategorySiteMap.get(categorySite1);
                                categorySite.setObjectVersionNumber(categorySite.getObjectVersionNumber() + 1L);
                            } else {
                                categorySite = new MtMaterialCategorySite();
                                categorySite.setMaterialCategoryId(category.getMaterialCategoryId());
                                categorySite.setSiteId(sitePlantRelation.getSiteId());
                                categorySite.setTenantId(tenantId);
                                categorySite.setObjectVersionNumber(1L);
                                categorySite.setCreatedBy(userId);
                                categorySite.setCreationDate(now);
                            }
                            categorySite.setLastUpdatedBy(userId);
                            categorySite.setLastUpdateDate(now);
                            materialCategorySiteList.add(categorySite);
                        }
                    }

                    // save attr
                    attrMap.put(iface, category.getMaterialCategoryId());
                    resultList.add(constructIfaceMessage(iface, "S", "成功.", now, userId));
                }

            }
        }
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(materialCategorySetList)) {
            sqlList.addAll(constructSql(materialCategorySetList));
            materialCategorySetList = null;
        }
        if (CollectionUtils.isNotEmpty(materialCategoryList)) {
            sqlList.addAll(constructSql(materialCategoryList));
            materialCategoryList = null;
        }
        /**
         * 写入Id、CID
         */

        if (CollectionUtils.isNotEmpty(materialCategorySiteList)) {
            List<String> cids = this.customDbRepository.getNextKeys("mt_material_category_site_cid_s",
                            materialCategorySiteList.size());
            List<MtMaterialCategorySite> insertList = materialCategorySiteList.stream()
                            .filter(t -> StringUtils.isEmpty(t.getMaterialCategorySiteId()))
                            .collect(Collectors.toList());
            List<String> ids = this.customDbRepository.getNextKeys("mt_material_category_site_s", insertList.size());
            List<AuditDomain> tempList = new ArrayList<>(materialCategorySiteList.size());
            materialCategorySiteList.stream().forEach(c -> {
                if (StringUtils.isEmpty(c.getMaterialCategorySiteId())) {
                    c.setMaterialCategorySiteId(ids.remove(0));
                }
                c.setCid(Long.valueOf(cids.remove(0)));
            });
            tempList.addAll(materialCategorySiteList);
            sqlList.addAll(constructSql(tempList));
            materialCategorySiteList = null;
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

        /**
         * 处理扩展属性
         */
        if (MapUtils.isNotEmpty(attrMap)) {
            sqlList.addAll(saveAttrColumn(tenantId, attrMap, MATERIAL_CATEGORY_ATTR_TABLE, "attribute"));
            if (CollectionUtils.isNotEmpty(sqlList)) {
                List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
                for (List<String> commitSql : commitSqlList) {
                    this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
                }
            }
        }
        return resultList;
    }

    /**
     * 保存扩展属性
     *
     * @author benjamin
     * @date 2019-08-19 21:50
     * @param tenantId IRequest
     */
    private List<String> saveAttrColumn(Long tenantId, Map<MtItemCategoryIface, String> attrMap, String attrTable,
                    String attribute) {
        // 获取系统支持的所有语言环境
        List<Language> languages = LanguageHelper.languages();
        List<String> result = new ArrayList<>();

        // 扩展表批量查询
        List<String> kidList = attrMap.entrySet().stream().map(t -> t.getValue()).collect(Collectors.toList());
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(attrTable);
        mtExtendVO1.setKeyIdList(kidList);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepo.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        Map<String, List<MtExtendAttrVO1>> originMap =
                        extendAttrList.stream().collect(Collectors.groupingBy(t -> t.getKeyId()));
        Map<String, List<MtExtendVO5>> attrTableMap = new HashMap<>(attrMap.size());
        attrMap.entrySet().stream().forEach(t -> {
            Map<String, String> fieldMap = ObjectFieldsHelper.getAttributeFields(t.getKey(), attribute);
            // 扩展属性更新时未传入时置为空
            if (MapUtils.isNotEmpty(originMap)) {
                if (CollectionUtils.isNotEmpty(originMap.get(t.getValue()))) {
                    for (MtExtendAttrVO1 attrVO1 : originMap.get(t.getValue())) {
                        fieldMap.putIfAbsent(attrVO1.getAttrName(), "");
                    }
                }
            }
            List<MtExtendVO5> saveExtendList = new ArrayList<>();
            MtExtendVO5 saveExtend;
            for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
                for (Language language : languages) {
                    saveExtend = new MtExtendVO5();
                    saveExtend.setAttrName(entry.getKey());
                    saveExtend.setAttrValue(entry.getValue());
                    saveExtend.setLang(language.getCode());
                    saveExtendList.add(saveExtend);
                }
                attrTableMap.put(t.getValue(), saveExtendList);
            }
        });
        if (MapUtils.isNotEmpty(attrTableMap)) {
            result.addAll(mtExtendSettingsRepo.attrPropertyUpdateForIface(tenantId, attrTable, attrTableMap));
        }
        return result;
    }

    /**
     * 根据工厂编码获取工厂与站点关系
     *
     * @author benjamin
     * @date 2019-06-27 10:21
     * @param tenantId IRequest
     * @param mtItemCategoryIfaceList List<MtItemCategoryIface>
     * @return Map<String, MtSitePlantReleation>
     */
    private Map<String, List<MtSitePlantReleation>> getSitePlantRelation(Long tenantId,
                    List<MtItemCategoryIface> mtItemCategoryIfaceList) {
        List<String> plantCodeList =
                        mtItemCategoryIfaceList.stream().filter(p -> StringUtils.isNotEmpty(p.getPlantCode()))
                                        .map(MtItemCategoryIface::getPlantCode).distinct().collect(Collectors.toList());
        Map<String, List<MtSitePlantReleation>> sitePlantMap = null;
        if (CollectionUtils.isNotEmpty(plantCodeList)) {
            MtSitePlantReleationVO3 mtSitePlantReleationVO3 = new MtSitePlantReleationVO3();
            mtSitePlantReleationVO3.setPlantCodes(plantCodeList);
            List<MtSitePlantReleation> relationByPlantAndSiteType =
                            mtSitePlantReleationRepo.getRelationByPlantAndSiteType(tenantId, mtSitePlantReleationVO3);
            if (CollectionUtils.isNotEmpty(relationByPlantAndSiteType)) {
                sitePlantMap = relationByPlantAndSiteType.stream()
                                .collect(Collectors.groupingBy(MtSitePlantReleation::getPlantCode));
            }
        }
        return sitePlantMap;
    }

    /**
     * 获得多语言Map
     *
     * @author benjamin
     * @date 2019-07-10 21:47
     * @param fieldValueMap 多语言描述
     * @return Map
     */
    private Map<String, Map<String, String>> getTlsMap(Map<String, String> fieldValueMap) {
        Map<String, Map<String, String>> tlMap = new HashMap<>();
        Map<String, String> map;

        for (Map.Entry<String, String> entry : fieldValueMap.entrySet()) {
            map = new HashMap<>();
            map.put("zh_CN", entry.getValue());
            tlMap.put(entry.getKey(), map);
        }

        return tlMap;
    }

    /**
     * 构建返回消息
     *
     * @author benjamin
     * @date 2019-06-27 17:02
     * @param mtItemCategoryIface MtItemCategoryIface
     * @param message 错误消息
     * @return MtItemCategoryIface
     */
    private MtItemCategoryIface constructIfaceMessage(MtItemCategoryIface mtItemCategoryIface, String status,
                    String message, Date date, Long userId) {
        mtItemCategoryIface.setStatus(status);
        mtItemCategoryIface.setMessage(message);
        mtItemCategoryIface.setLastUpdateDate(date);
        mtItemCategoryIface.setLastUpdatedBy(userId);
        return mtItemCategoryIface;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<MtItemCategoryIface> updateIfaceStatus(Long tenantId, String status) {
        List<MtItemCategoryIface> list = mtItemCategoryIfaceMapper.getUnprocessedList(tenantId);

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

    private static class CategorySite implements Serializable {
        private static final long serialVersionUID = 1523810987571228927L;
        private String siteId;
        private String materialCategoryId;

        public CategorySite(String siteId, String materialCategoryId) {
            this.siteId = siteId;
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
            CategorySite that = (CategorySite) o;
            return Objects.equals(siteId, that.siteId) && Objects.equals(materialCategoryId, that.materialCategoryId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(siteId, materialCategoryId);
        }
    }
}

