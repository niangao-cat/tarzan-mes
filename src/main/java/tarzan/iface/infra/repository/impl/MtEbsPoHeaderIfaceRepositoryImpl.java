package tarzan.iface.infra.repository.impl;

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
import tarzan.iface.domain.entity.MtEbsPoHeaderIface;
import tarzan.iface.domain.entity.MtPoHeader;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtEbsPoHeaderIfaceRepository;
import tarzan.iface.domain.vo.MtPoHeaderVO;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;
import tarzan.iface.infra.mapper.MtEbsPoHeaderIfaceMapper;
import tarzan.iface.infra.mapper.MtPoHeaderMapper;
import tarzan.iface.infra.mapper.MtSitePlantReleationMapper;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.entity.MtSupplierSite;
import tarzan.modeling.infra.mapper.MtSupplierMapper;
import tarzan.modeling.infra.mapper.MtSupplierSiteMapper;

/**
 * EBS采购订单接口表 资源库实现
 *
 * @author guichuan.li@hand-china.com 2019-10-08 15:19:56
 */
@Component
public class MtEbsPoHeaderIfaceRepositoryImpl extends BaseRepositoryImpl<MtEbsPoHeaderIface>
                implements MtEbsPoHeaderIfaceRepository {
    private static final int SQL_ITEM_COUNT_LIMIT = 1000;

    private static final String MT_PO_HEADER_ATTR = "mt_po_header_attr";
    private static final List<String> CLOSED_FLAG = Arrays.asList("CLOSED", "FINALLY CLOSED");

    @Autowired
    private MtEbsPoHeaderIfaceMapper mtEbsPoHeaderIfaceMapper;

    @Autowired
    private MtPoHeaderMapper mtPoHeaderMapper;

    @Autowired
    private MtSupplierMapper mtSupplierMapper;

    @Autowired
    private MtSupplierSiteMapper mtSupplierSiteMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtSitePlantReleationMapper mtSitePlantReleationMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Override
    public void ebsPoHeaderInterfaceImport(Long tenantId) {
        Long start = System.currentTimeMillis();
        // change data status
        List<MtEbsPoHeaderIface> mtEbsPoHeaderIfaceList = self().updateIfaceStatus(tenantId, "P");
        Map<Double, List<MtEbsPoHeaderIface>> ifacePerBatch = mtEbsPoHeaderIfaceList.stream().collect(
                        Collectors.groupingBy(MtEbsPoHeaderIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtEbsPoHeaderIface>> entry : ifacePerBatch.entrySet()) {
            try {
                List<AuditDomain> ifaceList = saveEbsPoHeaderIface(tenantId, entry.getValue());
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
        System.out.println("EBS采购订单头数据导入" + mtEbsPoHeaderIfaceList.size() + "条数据，总耗时："
                        + (System.currentTimeMillis() - start) + "毫秒");

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AuditDomain> saveEbsPoHeaderIface(Long tenantId, List<MtEbsPoHeaderIface> list) {
        Long userId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        Date now = new Date(System.currentTimeMillis());
        List<String> sqlList = new ArrayList<>();
        List<AuditDomain> result = new ArrayList<>(list.size());

        // po release id
        List<MtPoHeaderVO> mtPoHeaderVOS =
                        list.stream().map((t -> new MtPoHeaderVO(t.getPoHeaderId(), t.getPoReleaseId())))
                                        .collect(Collectors.toList());
        // plant code
        List<String> plantCodes =
                        list.stream().map(MtEbsPoHeaderIface::getPlantCode).distinct().collect(Collectors.toList());

        // supplier_id

        List<String> supplierIds =
                        list.stream().map(MtEbsPoHeaderIface::getSupplierId).distinct().collect(Collectors.toList());
        // supplier_site_id

        List<String> supplierSiteIds = list.stream().map(MtEbsPoHeaderIface::getSupplierSiteId).distinct()
                        .collect(Collectors.toList());

        // mt_po_header list for po header id and po_release_id
        List<MtPoHeader> mtPoHeaders = mtPoHeaderMapper.selectOnlyByPoHeaderId(tenantId, mtPoHeaderVOS);
        Map<MtPoHeaderVO, List<MtPoHeader>> mtPoHeaderMap = mtPoHeaders.stream().collect(
                        Collectors.groupingBy(t -> new MtPoHeaderVO(t.getErpPoHeadId(), t.getErpPoReleaseId())));

        // mt_supplier for supplierIds
        String whereInValuesSql = StringHelper.getWhereInValuesSql("s.SOURCE_IDENTIFICATION_ID", supplierIds, 1000);
        List<MtSupplier> mtSuppliers = mtSupplierMapper.querySupplierIds(tenantId, whereInValuesSql);
        Map<Double, List<MtSupplier>> mtSuppliersMap =
                        mtSuppliers.stream().collect(Collectors.groupingBy(MtSupplier::getSourceIdentificationId));

        // mt_supplier_site for supplierSiteIds
        String whereInValuesSql2 = StringHelper.getWhereInValuesSql("SOURCE_IDENTIFICATION_ID", supplierSiteIds, 1000);
        List<MtSupplierSite> mtSupplierSites = mtSupplierSiteMapper.querySupplierSiteIds(tenantId, whereInValuesSql2);
        Map<Double, List<MtSupplierSite>> mtSupplierSitesMap = mtSupplierSites.stream()
                        .collect(Collectors.groupingBy(MtSupplierSite::getSourceIdentificationId));

        // 根据工厂获取站点
        MtSitePlantReleationVO3 mtSitePlantReleationVO3 = new MtSitePlantReleationVO3();
        mtSitePlantReleationVO3.setSiteType("MANUFACTURING");
        mtSitePlantReleationVO3.setPlantCodes(plantCodes);
        List<MtSitePlantReleation> sitePlantRelationList =
                        mtSitePlantReleationMapper.getsiteIdList(tenantId, mtSitePlantReleationVO3);
        Map<String, List<MtSitePlantReleation>> sitePlantRelationMap = sitePlantRelationList.stream()
                        .collect(Collectors.groupingBy(MtSitePlantReleation::getPlantCode));
        // 获取Cid、Id
        List<String> customerIds = this.customDbRepository.getNextKeys("mt_po_header_s",
                        list.size() - mtPoHeaderMap.keySet().size());
        List<String> customerCids = this.customDbRepository.getNextKeys("mt_po_header_cid_s", list.size());

        List<AuditDomain> changeList = new ArrayList<>(list.size());
        MtPoHeader mtPoHeader;
        MtSitePlantReleation mtSitePlantReleation;
        MtSupplier mtSupplier;
        MtSupplierSite mtSupplierSite;
        MtPoHeaderVO mtPoHeaderVO;
        Map<MtEbsPoHeaderIface, String> attrMap = new HashMap<>(list.size());
        for (MtEbsPoHeaderIface iface : list) {
            try {
                // 参数校验
                if (MapUtils.isEmpty(sitePlantRelationMap) || sitePlantRelationMap.get(iface.getPlantCode()) == null) {
                    result.add(constructIfaceMessage(tenantId, iface, "E",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0012",
                                                    "INTERFACE", iface.getIfaceId(),
                                                    "【API:ebsPoHeaderInterfaceImport】"),
                                    now, userId));
                    continue;
                }
                mtSitePlantReleation = sitePlantRelationMap.get(iface.getPlantCode()).get(0);
                if (mtSitePlantReleation == null || StringUtils.isEmpty(mtSitePlantReleation.getSiteId())) {
                    result.add(constructIfaceMessage(tenantId, iface, "E",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0012",
                                                    "INTERFACE", iface.getIfaceId(),
                                                    "【API:ebsPoHeaderInterfaceImport】"),
                                    now, userId));
                    continue;
                }
                if (MapUtils.isEmpty(mtSuppliersMap)
                                || mtSuppliersMap.get(Double.valueOf(iface.getSupplierId())) == null) {
                    result.add(constructIfaceMessage(tenantId, iface, "E",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0031",
                                                    "INTERFACE", iface.getIfaceId(),
                                                    "【API:ebsPoHeaderInterfaceImport】"),
                                    now, userId));
                    continue;
                }

                mtSupplier = mtSuppliersMap.get(Double.valueOf(iface.getSupplierId())).get(0);
                if (mtSupplier == null || StringUtils.isEmpty(mtSupplier.getSupplierId())) {
                    result.add(constructIfaceMessage(tenantId, iface, "E",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0031",
                                                    "INTERFACE", iface.getIfaceId(),
                                                    "【API:ebsPoHeaderInterfaceImport】"),
                                    now, userId));
                    continue;
                }
                if (MapUtils.isEmpty(mtSupplierSitesMap)
                                || mtSupplierSitesMap.get(Double.valueOf(iface.getSupplierSiteId())) == null) {
                    result.add(constructIfaceMessage(tenantId, iface, "E",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0032",
                                                    "INTERFACE", iface.getIfaceId(),
                                                    "【API:ebsPoHeaderInterfaceImport】"),
                                    now, userId));
                    continue;
                }

                mtSupplierSite = mtSupplierSitesMap.get(Double.valueOf(iface.getSupplierSiteId())).get(0);
                if (mtSupplierSite == null || StringUtils.isEmpty(mtSupplierSite.getSupplierSiteId())) {
                    result.add(constructIfaceMessage(tenantId, iface, "E",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0032",
                                                    "INTERFACE", iface.getIfaceId(),
                                                    "【API:ebsPoHeaderInterfaceImport】"),
                                    now, userId));
                    continue;
                }
                mtPoHeaderVO = new MtPoHeaderVO(iface.getPoHeaderId(), iface.getPoReleaseId());
                Integer isNewType;
                if (MapUtils.isEmpty(mtPoHeaderMap) || CollectionUtils.isEmpty(mtPoHeaderMap.get(mtPoHeaderVO))) {
                    // insert
                    isNewType = 0;
                } else {
                    isNewType = 1;
                }

                switch (isNewType) {
                    case 0:
                        mtPoHeader = new MtPoHeader();
                        mtPoHeader = constructMtPoHeader(tenantId, mtPoHeader, iface, now, userId,
                                        mtSitePlantReleation.getSiteId(), mtSupplier.getSupplierId(),
                                        mtSupplierSite.getSupplierSiteId(), true, customerIds.remove(0),
                                        customerCids.remove(0));
                        break;
                    case 1:
                        // update
                        mtPoHeader = mtPoHeaderMap.get(mtPoHeaderVO).get(0);
                        mtPoHeader = constructMtPoHeader(tenantId, mtPoHeader, iface, now, userId,
                                        mtSitePlantReleation.getSiteId(), mtSupplier.getSupplierId(),
                                        mtSupplierSite.getSupplierSiteId(), false, null, customerCids.remove(0));
                        break;
                    default:
                        mtPoHeader = new MtPoHeader();
                        break;
                }
                result.add(constructIfaceMessage(tenantId, iface, "S", "成功.", now, userId));
                attrMap.put(iface, mtPoHeader.getPoHeaderId());
                changeList.add(mtPoHeader);
            } catch (Exception e) {
                throw new MtException("MT_INTERFACE_0011",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0011", "INTERFACE",
                                                iface.getIfaceId(), e.getMessage(),
                                                "【API:ebsPoHeaderInterfaceImport】"));
            }
        }
        if (CollectionUtils.isNotEmpty(changeList)) {
            sqlList.addAll(constructSql(changeList));
            changeList = null;
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
            sqlList.addAll(saveAttrColumn(tenantId, attrMap));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<MtEbsPoHeaderIface> updateIfaceStatus(Long tenantId, String status) {
        List<MtEbsPoHeaderIface> list = mtEbsPoHeaderIfaceMapper.getUnprocessedList(tenantId);
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
     * 保存扩展属性
     * 
     * @param tenantId
     */
    private List<String> saveAttrColumn(Long tenantId, Map<MtEbsPoHeaderIface, String> attrMap) {

        // 获取系统支持的所有语言环境
        List<Language> languages = LanguageHelper.languages();
        List<String> result = new ArrayList<>();

        // 扩展表批量查询
        List<String> kidList = attrMap.entrySet().stream().map(t -> t.getValue()).collect(Collectors.toList());
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(MT_PO_HEADER_ATTR);
        mtExtendVO1.setKeyIdList(kidList);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        Map<String, List<MtExtendAttrVO1>> originMap =
                        extendAttrList.stream().collect(Collectors.groupingBy(t -> t.getKeyId()));
        Map<String, List<MtExtendVO5>> attrTableMap = new HashMap<>(attrMap.size());
        attrMap.entrySet().stream().forEach(t -> {
            Map<String, String> fieldMap = ObjectFieldsHelper.getAttributeFields(t.getKey(), "poAttribute");
            fieldMap.putAll(ObjectFieldsHelper.getAttributeFields(t.getKey(), "releaseAttribute"));
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
            result.addAll(mtExtendSettingsRepository.attrPropertyUpdateForIface(tenantId, MT_PO_HEADER_ATTR,
                            attrTableMap));
        }
        return result;
    }

    private MtPoHeader constructMtPoHeader(Long tenantId, MtPoHeader mtPoHeader, MtEbsPoHeaderIface iface, Date now,
                    Long userId, String siteId, String supplierId, String supplierSiteId, Boolean isNew, String keyId,
                    String cid) {
        mtPoHeader.setErpPoHeadId(iface.getPoHeaderId());
        mtPoHeader.setErpPoReleaseId(iface.getPoReleaseId());
        mtPoHeader.setPoNumber(iface.getPoNumber());
        mtPoHeader.setPoReleaseNum(iface.getPoReleaseNum());
        mtPoHeader.setBuyerCode(iface.getBuyerCode());
        mtPoHeader.setPoOrderType(iface.getPoOrderType());
        if ("APPROVED".equalsIgnoreCase(iface.getApprovedStatus())) {
            mtPoHeader.setApprovedFlag("Y");
        } else {
            mtPoHeader.setApprovedFlag("N");
        }
        mtPoHeader.setCanceledFlag(iface.getCanceledFlag());
        if (CLOSED_FLAG.contains(iface.getClosedCode())) {
            mtPoHeader.setClosedFlag("Y");
        } else {
            mtPoHeader.setClosedFlag("N");
        }
        mtPoHeader.setDescription(iface.getDescription());
        mtPoHeader.setCurrencyCode(iface.getCurrencyCode());
        mtPoHeader.setSiteId(siteId);
        mtPoHeader.setSupplierId(supplierId);
        mtPoHeader.setSupplierSiteId(supplierSiteId);
        mtPoHeader.setLastUpdateDate(now);
        mtPoHeader.setLastUpdatedBy(userId);
        mtPoHeader.setTenantId(tenantId);
        if (isNew) {
            mtPoHeader.setPoHeaderId(keyId);
            mtPoHeader.setCreatedBy(userId);
            mtPoHeader.setCreationDate(now);
            mtPoHeader.setObjectVersionNumber(1L);
        } else {
            mtPoHeader.setObjectVersionNumber(mtPoHeader.getObjectVersionNumber() + 1L);
        }
        mtPoHeader.setCid(Long.valueOf(cid));
        return mtPoHeader;
    }

    /**
     * 构建返回消息
     *
     * @author benjamin
     * @date 2019-06-27 17:02
     * @param mtEbsPoHeaderIface MtCostcenterIface
     * @param message 错误消息
     * @return MtEbsPoHeaderIface
     */
    private MtEbsPoHeaderIface constructIfaceMessage(Long tenantId, MtEbsPoHeaderIface mtEbsPoHeaderIface,
                    String status, String message, Date date, Long userId) {
        mtEbsPoHeaderIface.setTenantId(tenantId);
        mtEbsPoHeaderIface.setStatus(status);
        mtEbsPoHeaderIface.setMessage(message);
        mtEbsPoHeaderIface.setLastUpdateDate(date);
        mtEbsPoHeaderIface.setLastUpdatedBy(userId);

        return mtEbsPoHeaderIface;
    }


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
