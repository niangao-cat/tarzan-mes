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
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.domain.vo.MtExtendVO5;
import tarzan.iface.domain.entity.MtAslIface;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtAslIfaceRepository;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.iface.domain.vo.MtSitePlantReleationVO;
import tarzan.iface.domain.vo.MtSitePlantReleationVO1;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;
import tarzan.iface.infra.mapper.MtAslIfaceMapper;
import tarzan.material.domain.entity.MtPfepPurchaseSupplier;
import tarzan.material.domain.repository.MtPfepPurchaseSupplierRepository;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.entity.MtSupplierSite;
import tarzan.modeling.domain.repository.MtSupplierRepository;
import tarzan.modeling.domain.repository.MtSupplierSiteRepository;

/**
 * 合格供应商（货源清单）数据接口 资源库实现
 *
 * @author mingjie.chen@hand-china.com 2019-09-09 17:05:24
 */
@Component
public class MtAslIfaceRepositoryImpl extends BaseRepositoryImpl<MtAslIface> implements MtAslIfaceRepository {


    private static final int SQL_ITEM_COUNT_LIMIT = 10000;

    private static final String PFEP_PURCHASE_SUPPLIER_ATTR_TABLE = "mt_pfep_purchase_supplier_attr";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepo;

    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepo;

    @Autowired
    private MtSupplierRepository mtSupplierRepo;

    @Autowired
    private MtSupplierSiteRepository mtSupplierSiteRepo;

    @Autowired
    private MtAslIfaceMapper mtAslIfaceMapper;

    @Autowired
    private MtPfepPurchaseSupplierRepository mtPfepPurchaseSupplierRepository;

    @Override
    public void aslInterfaceImport(Long tenantId) {

        Long start = System.currentTimeMillis();
        // change data status
        List<MtAslIface> mtSubinventoryIfaceList = self().updateIfaceStatus(tenantId, "P");

        // get data list
        Map<Double, List<MtAslIface>> ifacePerBatch = mtSubinventoryIfaceList.stream()
                        .collect(Collectors.groupingBy(MtAslIface::getBatchId, TreeMap::new, Collectors.toList()));
        for (Map.Entry<Double, List<MtAslIface>> entry : ifacePerBatch.entrySet()) {
            try {
                List<AuditDomain> ifaceList = self().saveAslIface(tenantId, entry.getValue());
                self().log(ifaceList);
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
                self().log(ifaceList);
            }
        }
        System.out.println("ASL接口总耗时：" + (System.currentTimeMillis() - start) + "毫秒");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AuditDomain> saveAslIface(Long tenantId, List<MtAslIface> mtAslIfaceList) {
        List<AuditDomain> ifaceList = new ArrayList<>(mtAslIfaceList.size());
        // 公有变量
        Long userId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        Date now = new Date(System.currentTimeMillis());
        // get site plant relation
        Map<String, MtSitePlantReleation> sitePlantMap = getSitePlantRelation(tenantId, mtAslIfaceList);

        // get site material relation
        Map<ItemPlant, List<MtSitePlantReleationVO1>> materialSiteMap = getMaterialSite(tenantId, mtAslIfaceList);

        // get supplier
        List<MtSupplier> originSupplierList = mtSupplierRepo.querySupplierByCode(tenantId, mtAslIfaceList.stream()
                        .map(MtAslIface::getSupplierCode).distinct().collect(Collectors.toList()));
        Map<String, List<MtSupplier>> existSupplierPerCode =
                        originSupplierList.stream().collect(Collectors.groupingBy(MtSupplier::getSupplierCode));

        // get supplier site
        List<String> supplierIdList =
                        originSupplierList.stream().map(MtSupplier::getSupplierId).collect(Collectors.toList());
        Map<String, List<MtSupplierSite>> existSitePerSupplier =
                        mtSupplierSiteRepo.querySupplierSiteBySupplierId(tenantId, supplierIdList).stream()
                                        .collect(Collectors.groupingBy(MtSupplierSite::getSupplierId));

        // get mt_pfep_purchase_supplier by supplier id
        Map<SupplierPurchase, List<MtPfepPurchaseSupplier>> supplierPurchaseListMap = null;
        if (CollectionUtils.isNotEmpty(originSupplierList)) {
            List<MtPfepPurchaseSupplier> mtPfepPurchaseSuppliers = mtPfepPurchaseSupplierRepository
                            .queryPfepPurchaseSupplierBySupplierId(tenantId, originSupplierList.stream()
                                            .map(MtSupplier::getSupplierId).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(mtPfepPurchaseSuppliers)) {
                supplierPurchaseListMap = mtPfepPurchaseSuppliers.stream()
                                .collect(Collectors.groupingBy(t -> new SupplierPurchase(t.getMaterialSiteId(),
                                                t.getSupplierId(), t.getSupplierSiteId())));
            }
        }
        List<String> sqlList = new ArrayList<>();
        Map<MtAslIface, String> attrMap = new HashMap<>(mtAslIfaceList.size());
        MtPfepPurchaseSupplier mtPfepPurchaseSupplier;
        ItemPlant itemPlant;
        SupplierPurchase supplierPurchase;
        String materialSiteId;
        String supplierId;
        String supplierSiteId;

        // 获取Cid、Id
        List<String> pfepPurchaseSupplierIds =
                        this.customDbRepository.getNextKeys("mt_pfep_purchase_supplier_s", mtAslIfaceList.size());
        List<String> pfepPurchaseSupplierCids =
                        this.customDbRepository.getNextKeys("mt_pfep_purchase_supplier_cid_s", mtAslIfaceList.size());
        List<AuditDomain> pfepList = new ArrayList<>(mtAslIfaceList.size());
        for (MtAslIface iface : mtAslIfaceList) {
            if (sitePlantMap.get(iface.getPlantCode()) == null) {
                ifaceList.add(constructIfaceMessage(
                                iface, "E", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0030",
                                                "INTERFACE", iface.getIfaceId(), "【API：aslInterfaceImport】"),
                                now, userId));
                continue;
            }
            itemPlant = new ItemPlant(iface.getPlantCode(), iface.getItemCode());
            if (materialSiteMap == null || materialSiteMap.get(itemPlant) == null) {
                ifaceList.add(constructIfaceMessage(
                                iface, "E", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0029",
                                                "INTERFACE", iface.getIfaceId(), "【API：aslInterfaceImport】"),
                                now, userId));
                continue;
            }
            materialSiteId = materialSiteMap.get(itemPlant).get(0).getMaterialSiteId();
            if (existSupplierPerCode == null || existSupplierPerCode.get(iface.getSupplierCode()) == null) {
                ifaceList.add(constructIfaceMessage(
                                iface, "E", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0018",
                                                "INTERFACE", iface.getIfaceId(), "【API：aslInterfaceImport】"),
                                now, userId));
                continue;
            }
            supplierId = existSupplierPerCode.get(iface.getSupplierCode()).get(0).getSupplierId();
            if (existSitePerSupplier == null) {
                ifaceList.add(constructIfaceMessage(
                                iface, "E", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0027",
                                                "INTERFACE", iface.getIfaceId(), "【API：aslInterfaceImport】"),
                                now, userId));
                continue;
            }
            List<MtSupplierSite> supplierSiteList = existSitePerSupplier
                            .get(existSupplierPerCode.get(iface.getSupplierCode()).get(0).getSupplierId());
            if (CollectionUtils.isEmpty(supplierSiteList) || supplierSiteList.stream()
                            .noneMatch(s -> s.getSupplierSiteCode().equals(iface.getSupplierSiteCode()))) {
                ifaceList.add(constructIfaceMessage(
                                iface, "E", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0027",
                                                "INTERFACE", iface.getIfaceId(), "【API：aslInterfaceImport】"),
                                now, userId));
                continue;
            }
            supplierSiteId = existSitePerSupplier.get(supplierId).get(0).getSupplierSiteId();

            // save pfep purchanse supplier
            supplierPurchase = new SupplierPurchase(materialSiteId, supplierId, supplierSiteId);
            if (MapUtils.isNotEmpty(supplierPurchaseListMap)
                            && CollectionUtils.isNotEmpty(supplierPurchaseListMap.get(supplierPurchase))) {
                mtPfepPurchaseSupplier = supplierPurchaseListMap.get(supplierPurchase).get(0);
                mtPfepPurchaseSupplier.setObjectVersionNumber(mtPfepPurchaseSupplier.getObjectVersionNumber() + 1L);
            } else {
                mtPfepPurchaseSupplier = new MtPfepPurchaseSupplier();
                mtPfepPurchaseSupplier.setCreatedBy(userId);
                mtPfepPurchaseSupplier.setCreationDate(now);
                mtPfepPurchaseSupplier.setObjectVersionNumber(1L);
            }
            mtPfepPurchaseSupplier = constructPfepPurchaseSupplier(tenantId, materialSiteId, supplierId, supplierSiteId,
                            iface, mtPfepPurchaseSupplier);
            mtPfepPurchaseSupplier.setTenantId(tenantId);
            mtPfepPurchaseSupplier.setLastUpdatedBy(userId);
            mtPfepPurchaseSupplier.setLastUpdateDate(now);
            mtPfepPurchaseSupplier.setCid(Long.valueOf(pfepPurchaseSupplierCids.remove(0)));
            if (StringUtils.isEmpty(mtPfepPurchaseSupplier.getPfepPurchaseSupplierId())) {
                mtPfepPurchaseSupplier.setPfepPurchaseSupplierId(pfepPurchaseSupplierIds.remove(0));

            }
            pfepList.add(mtPfepPurchaseSupplier);

            // save attr
            attrMap.put(iface, mtPfepPurchaseSupplier.getPfepPurchaseSupplierId());
            ifaceList.add(constructIfaceMessage(iface, "S", "成功.", now, userId));
        }

        if (CollectionUtils.isNotEmpty(pfepList)) {
            sqlList.addAll(constructSql(pfepList));
            pfepList = null;
        }
        /**
         * 保存评pfep数据
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
            sqlList.addAll(saveAttrColumn(tenantId, attrMap, PFEP_PURCHASE_SUPPLIER_ATTR_TABLE, "attribute"));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
        return ifaceList;
    }

    /**
     * 保存扩展属性
     *
     * @author benjamin
     * @date 2019-08-19 21:50
     * @param tenantId IRequest
     */
    private List<String> saveAttrColumn(Long tenantId, Map<MtAslIface, String> attrMap, String tableName,
                    String attributePrefix) {

        // 获取系统支持的所有语言环境
        List<Language> languages = LanguageHelper.languages();
        List<String> result = new ArrayList<>();

        // 扩展表批量查询
        List<String> kidList = attrMap.entrySet().stream().map(t -> t.getValue()).collect(Collectors.toList());
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(tableName);
        mtExtendVO1.setKeyIdList(kidList);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepo.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        Map<String, List<MtExtendAttrVO1>> originMap =
                        extendAttrList.stream().collect(Collectors.groupingBy(t -> t.getKeyId()));
        Map<String, List<MtExtendVO5>> attrTableMap = new HashMap<>(attrMap.size());
        attrMap.entrySet().stream().forEach(t -> {
            Map<String, String> fieldMap = ObjectFieldsHelper.getAttributeFields(t.getKey(), attributePrefix);
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
            result.addAll(mtExtendSettingsRepo.attrPropertyUpdateForIface(tenantId, tableName, attrTableMap));
        }
        return result;
    }

    /**
     * 根据工厂编码获取工厂与站点关系
     *
     * 站点类型指定为manufacturing
     *
     * @author benjamin
     * @date 2019-06-27 10:21
     * @param tenantId IRequest
     * @param mtAslIfaceList List<MtAslIface>
     * @return Map<String, MtSitePlantReleation>
     */
    private Map<String, MtSitePlantReleation> getSitePlantRelation(Long tenantId, List<MtAslIface> mtAslIfaceList) {
        List<String> plantCodeList =
                        mtAslIfaceList.stream().map(MtAslIface::getPlantCode).distinct().collect(Collectors.toList());
        MtSitePlantReleationVO3 mtSitePlantReleationVO3 = new MtSitePlantReleationVO3();
        mtSitePlantReleationVO3.setPlantCodes(plantCodeList);
        mtSitePlantReleationVO3.setSiteType("PURCHASE");
        List<MtSitePlantReleation> relationByPlantAndSiteType =
                        mtSitePlantReleationRepo.getRelationByPlantAndSiteType(tenantId, mtSitePlantReleationVO3);
        Map<String, MtSitePlantReleation> sitePlantMap = null;
        if (CollectionUtils.isNotEmpty(relationByPlantAndSiteType)) {
            sitePlantMap = relationByPlantAndSiteType.stream()
                            .collect(Collectors.toMap(MtSitePlantReleation::getPlantCode, t -> t));
        }
        return sitePlantMap;
    }

    /**
     * 根据工厂物料获取物料站点关系
     *
     * 站点类型指定为PURCHASE
     *
     * @author benjamin
     * @date 2019-06-27 10:29
     * @param tenantId IRequest
     * @param mtAslIfaceList List<MtAslIface>
     * @return Map<ItemPlant, MtInterFaceVO1>
     */
    private Map<ItemPlant, List<MtSitePlantReleationVO1>> getMaterialSite(Long tenantId,
                    List<MtAslIface> mtAslIfaceList) {
        MtSitePlantReleationVO mtSitePlantReleationVO = new MtSitePlantReleationVO();
        mtSitePlantReleationVO.setPlantCodeList(
                        mtAslIfaceList.stream().map(MtAslIface::getPlantCode).distinct().collect(Collectors.toList()));
        mtSitePlantReleationVO.setItemCodeList(
                        mtAslIfaceList.stream().map(MtAslIface::getItemCode).distinct().collect(Collectors.toList()));
        mtSitePlantReleationVO.setSiteType("PURCHASE");
        List<MtSitePlantReleationVO1> mtSitePlantReleationVO1s =
                        mtSitePlantReleationRepo.itemMaterialSiteIdBatchQuery(tenantId, mtSitePlantReleationVO);
        Map<ItemPlant, List<MtSitePlantReleationVO1>> itemPlantListMap = null;
        if (CollectionUtils.isNotEmpty(mtSitePlantReleationVO1s)) {
            itemPlantListMap = mtSitePlantReleationVO1s.stream()
                            .collect(Collectors.groupingBy(t -> new ItemPlant(t.getPlantCode(), t.getItemCode())));
        }
        return itemPlantListMap;
    }

    /**
     * 构建物料供应商采购属性
     *
     * @author benjamin
     * @date 2019-09-03 13:34
     * @param tenantId IRequest
     * @param materialSiteId 物料Id
     * @param supplierId 供应商Id
     * @param supplierSiteId 供应商地点Id
     * @param iface MtAslIface
     * @return MtAslIface
     */
    private MtPfepPurchaseSupplier constructPfepPurchaseSupplier(Long tenantId, String materialSiteId,
                    String supplierId, String supplierSiteId, MtAslIface iface,
                    MtPfepPurchaseSupplier mtPfepPurchaseSupplier) {
        mtPfepPurchaseSupplier.setTenantId(tenantId);
        mtPfepPurchaseSupplier.setMaterialSiteId(materialSiteId);
        mtPfepPurchaseSupplier.setSupplierId(supplierId);
        mtPfepPurchaseSupplier.setSupplierSiteId(supplierSiteId);
        mtPfepPurchaseSupplier.setMinPackageQty(iface.getMinPackageQty());
        mtPfepPurchaseSupplier.setMinPurchaseQty(iface.getMinPurchaseQty());
        mtPfepPurchaseSupplier.setEnableFlag(iface.getEnableFlag());
        return mtPfepPurchaseSupplier;
    }

    /**
     * 构建返回消息
     *
     * @author benjamin
     * @date 2019-06-27 17:02
     * @param mtAslIface MtAslIface
     * @param message 错误消息
     * @return MtAslIface
     */
    private MtAslIface constructIfaceMessage(MtAslIface mtAslIface, String status, String message, Date date,
                    Long userId) {
        mtAslIface.setStatus(status);
        mtAslIface.setMessage(message);
        mtAslIface.setLastUpdateDate(date);
        mtAslIface.setLastUpdatedBy(userId);

        return mtAslIface;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<MtAslIface> updateIfaceStatus(Long tenantId, String status) {
        List<MtAslIface> mtAslIfaceList = mtAslIfaceMapper.getUnprocessedList(tenantId);
        if (CollectionUtils.isNotEmpty(mtAslIfaceList)) {
            List<String> sqlList = new ArrayList<>(mtAslIfaceList.size());
            List<AuditDomain> auditDomains = new ArrayList<>(mtAslIfaceList.size());
            mtAslIfaceList.stream().forEach(ifs -> {
                ifs.setStatus(status);
                ifs.setTenantId(tenantId);
            });
            auditDomains.addAll(mtAslIfaceList);
            sqlList.addAll(constructSql(auditDomains));
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return mtAslIfaceList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void log(List<AuditDomain> ifaceSqlList) {
        if (CollectionUtils.isNotEmpty(ifaceSqlList)) {
            List<String> sqlList = constructSql(ifaceSqlList);
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }

        }
    }

    private static class ItemPlant implements Serializable {
        private static final long serialVersionUID = -2958353779967068024L;
        private String plantCode;
        private String itemCode;

        ItemPlant(String plantCode, String itemCode) {
            this.plantCode = plantCode;
            this.itemCode = itemCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ItemPlant tuple = (ItemPlant) o;
            return Objects.equals(plantCode, tuple.plantCode) && Objects.equals(itemCode, tuple.itemCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(plantCode, itemCode);
        }
    }
    private static class SupplierPurchase implements Serializable {
        private static final long serialVersionUID = -8097790807817543036L;
        private String materialSiteId;
        private String supplierId;
        private String supplierSiteId;



        public SupplierPurchase(String materialSiteId, String supplierId, String supplierSiteId) {
            this.materialSiteId = materialSiteId;
            this.supplierId = supplierId;
            this.supplierSiteId = supplierSiteId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SupplierPurchase that = (SupplierPurchase) o;
            return Objects.equals(materialSiteId, that.materialSiteId) && Objects.equals(supplierId, that.supplierId)
                            && Objects.equals(supplierSiteId, that.supplierSiteId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(materialSiteId, supplierId, supplierSiteId);
        }
    }

    /**
     * 生成拆分的sql
     *
     * @param ifaceSqlList
     * @return
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
}
