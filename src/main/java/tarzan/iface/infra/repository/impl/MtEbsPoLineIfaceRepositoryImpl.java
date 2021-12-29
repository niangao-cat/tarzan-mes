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
import org.springframework.util.LinkedMultiValueMap;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.helper.LanguageHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.*;
import tarzan.iface.domain.entity.MtEbsPoLineIface;
import tarzan.iface.domain.entity.MtPoHeader;
import tarzan.iface.domain.entity.MtPoLine;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtEbsPoLineIfaceRepository;
import tarzan.iface.domain.vo.MtPoHeaderVO;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;
import tarzan.iface.infra.mapper.MtEbsPoLineIfaceMapper;
import tarzan.iface.infra.mapper.MtPoHeaderMapper;
import tarzan.iface.infra.mapper.MtPoLineMapper;
import tarzan.iface.infra.mapper.MtSitePlantReleationMapper;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.infra.mapper.MtMaterialSiteMapper;
import tarzan.material.infra.mapper.MtUomMapper;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.infra.mapper.MtWorkOrderMapper;

/**
 * EBS采购订单行接口表 资源库实现
 *
 * @author guichuan.li@hand-china.com 2019-10-08 15:19:56
 */
@Component
public class MtEbsPoLineIfaceRepositoryImpl extends BaseRepositoryImpl<MtEbsPoLineIface>
                implements MtEbsPoLineIfaceRepository {

    private static final int SQL_ITEM_COUNT_LIMIT = 1000;

    private static final String MT_PO_LINE_ATTR = "mt_po_line_attr";
    private static final List<String> CLOSED_FLAG = Arrays.asList("CLOSED", "FINALLY CLOSED", "CLOSED FOR RECEIVING");

    @Autowired
    private MtEbsPoLineIfaceMapper mtEbsPoLineIfaceMapper;

    @Autowired
    private MtPoLineMapper mtPoLineMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtMaterialSiteMapper mtMaterialSiteMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtPoHeaderMapper mtPoHeaderMapper;

    @Autowired
    private MtWorkOrderMapper mtWorkOrderMapper;

    @Autowired
    private MtSitePlantReleationMapper mtSitePlantReleationMapper;

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    @Autowired
    private MtUomMapper mtUomMapper;

    @Override
    public void ebsPoLineInterfaceImport(Long tenantId) {

        Long start = System.currentTimeMillis();
        // change data status
        List<MtEbsPoLineIface> mtEbsPoLineIfaceList = self().updateIfaceStatus(tenantId, "P");
        Map<Double, List<MtEbsPoLineIface>> ifacePerBatch = mtEbsPoLineIfaceList.stream().collect(
                        Collectors.groupingBy(MtEbsPoLineIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtEbsPoLineIface>> entry : ifacePerBatch.entrySet()) {
            try {
                List<AuditDomain> ifaceList = saveEbsPoLineIface(tenantId, entry.getValue());
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
        System.out.println("EBS采购订单行数据导入" + mtEbsPoLineIfaceList.size() + "条数据，总耗时："
                        + (System.currentTimeMillis() - start) + "毫秒");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AuditDomain> saveEbsPoLineIface(Long tenantId, List<MtEbsPoLineIface> list) {
        Long userId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        Date now = new Date(System.currentTimeMillis());
        List<String> sqlList = new ArrayList<>();
        List<AuditDomain> result = new ArrayList<>(list.size());

        // po_distribution_id
        List<String> poReleaseIds = list.stream().filter(t -> StringUtils.isNotEmpty(t.getPoDistributionId()))
                        .map(MtEbsPoLineIface::getPoDistributionId).distinct().collect(Collectors.toList());
        List<MtPoHeaderVO> mtPoHeaderVOS =
                        list.stream().map((t -> new MtPoHeaderVO(t.getPoHeaderId(), t.getPoReleaseId())))
                                        .collect(Collectors.toList());

        // po_line_id
        List<String> poLineIds =
                        list.stream().map(MtEbsPoLineIface::getPoLineId).distinct().collect(Collectors.toList());

        // plant code
        List<String> plantCodes =
                        list.stream().map(MtEbsPoLineIface::getPlantCode).distinct().collect(Collectors.toList());
        // uom code
        List<String> uomCodes = list.stream().map(MtEbsPoLineIface::getUom).distinct().collect(Collectors.toList());

        // item id
        List<String> itemIds = list.stream().map(MtEbsPoLineIface::getItemId).distinct().collect(Collectors.toList());

        // wip entity id
        List<String> wipEntityIds =
                        list.stream().map(MtEbsPoLineIface::getWipEntityId).distinct().collect(Collectors.toList());

        // mt_po_line list for erp_po_distribution_id
        String whereInValuesSql = StringHelper.getWhereInValuesSql("t.ERP_PO_DISTRIBUTION_ID", poReleaseIds, 1000);
        List<MtPoLine> mtPoLinesDistribution = mtPoLineMapper.selectByPoDistributionId(tenantId, whereInValuesSql);
        Map<String, List<MtPoLine>> mtPoLinesDistributionMap =
                        mtPoLinesDistribution.stream().collect(Collectors.groupingBy(MtPoLine::getErpPoDistributionId));

        // mt_po_line list for erp_po_line_id
        whereInValuesSql = StringHelper.getWhereInValuesSql("t.ERP_PO_LINE_ID", poLineIds, 1000);
        List<MtPoLine> mtPoLinesPoLine = mtPoLineMapper.selectByPoLineId(tenantId, whereInValuesSql);
        Map<String, List<MtPoLine>> mtPoLinesPoLineMap =
                        mtPoLinesPoLine.stream().collect(Collectors.groupingBy(MtPoLine::getErpPoLineId));



        // mt_po_header list for po header id and po_release_id
        List<MtPoHeader> mtPoHeaders = mtPoHeaderMapper.selectOnlyByPoHeaderId(tenantId, mtPoHeaderVOS);
        Map<MtPoHeaderVO, List<MtPoHeader>> mtPoHeaderMap = mtPoHeaders.stream().collect(
                        Collectors.groupingBy(t -> new MtPoHeaderVO(t.getErpPoHeadId(), t.getErpPoReleaseId())));

        // uomId from uomCodes
        whereInValuesSql = StringHelper.getWhereInValuesSql("tb.UOM_CODE", uomCodes, 1000);
        List<MtUom> mtUoms = mtUomMapper.selectByUomCode(tenantId, whereInValuesSql);
        Map<String, List<MtUom>> mtUomsMap = mtUoms.stream().collect(Collectors.groupingBy(MtUom::getUomCode));

        // material_site for item id
        whereInValuesSql = StringHelper.getWhereInValuesSql("SOURCE_IDENTIFICATION_ID", itemIds, 1000);
        List<MtMaterialSite> mtMaterialSites =
                        mtMaterialSiteMapper.queryMaterialSiteByItemId(tenantId, whereInValuesSql);
        Map<String, List<MtMaterialSite>> mtMaterialSiteMap = mtMaterialSites.stream()
                        .collect(Collectors.groupingBy(MtMaterialSite::getSourceIdentificationId));

        // work_order for wip_entity_id
        whereInValuesSql = StringHelper.getWhereInValuesSql("SOURCE_IDENTIFICATION_ID", wipEntityIds, 1000);
        List<MtWorkOrder> mtWorkOrders = mtWorkOrderMapper.selectByWipEntityId(tenantId, whereInValuesSql);
        Map<Double, List<MtWorkOrder>> mtWorkOrdersMap =
                        mtWorkOrders.stream().collect(Collectors.groupingBy(MtWorkOrder::getSourceIdentificationId));

        // 根据工厂获取站点
        MtSitePlantReleationVO3 mtSitePlantReleationVO3 = new MtSitePlantReleationVO3();
        mtSitePlantReleationVO3.setSiteType("MANUFACTURING");
        mtSitePlantReleationVO3.setPlantCodes(plantCodes);
        List<MtSitePlantReleation> sitePlantRelationList =
                        mtSitePlantReleationMapper.getsiteIdList(tenantId, mtSitePlantReleationVO3);
        Map<String, List<MtSitePlantReleation>> sitePlantRelationMap = sitePlantRelationList.stream()
                        .collect(Collectors.groupingBy(MtSitePlantReleation::getPlantCode));

        // 获取Cid、Id
        List<String> newoLineIds = this.customDbRepository.getNextKeys("mt_po_line_s", list.size());
        List<String> newoLineCids = this.customDbRepository.getNextKeys("mt_po_line_cid_s", list.size());

        MtPoHeader mtPoHeader;
        MtSitePlantReleation mtSitePlantReleation;
        MtPoLine mtPoLine;
        MtMaterialSite mtMaterialSite;
        MtWorkOrder mtWorkOrder;

        MtNumrangeVO10 mtNumrangeVO10;
        List<MtNumrangeVO10> mtNumrangeVO10s = new ArrayList<>();
        Map<MtEbsPoLineIface, String> attrMap = new HashMap<>(list.size());
        List<MtPoLine> changeList = new ArrayList<>(list.size());
        Long sequence = 0L;
        MtUom mtUom;
        for (MtEbsPoLineIface iface : list) {
            try {

                MtPoHeaderVO mtPoHeaderVO = new MtPoHeaderVO(iface.getPoHeaderId(), iface.getPoReleaseId());
                if (MapUtils.isEmpty(mtPoHeaderMap) || CollectionUtils.isEmpty(mtPoHeaderMap.get(mtPoHeaderVO))) {
                    result.add(constructIfaceMessage(tenantId, iface, "E",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0033",
                                                    "INTERFACE", iface.getIfaceId(), "【API:ebsPoLineInterfaceImport】"),
                                    now, userId));
                    continue;
                }
                mtPoHeader = mtPoHeaderMap.get(mtPoHeaderVO).get(0);

                if (mtPoHeader == null) {
                    result.add(constructIfaceMessage(tenantId, iface, "E",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0033",
                                                    "INTERFACE", iface.getIfaceId(), "【API:ebsPoLineInterfaceImport】"),
                                    now, userId));
                    continue;
                }

                if (MapUtils.isEmpty(sitePlantRelationMap) || sitePlantRelationMap.get(iface.getPlantCode()) == null) {
                    result.add(constructIfaceMessage(tenantId, iface, "E",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0012",
                                                    "INTERFACE", iface.getIfaceId(), "【API:ebsPoLineInterfaceImport】"),
                                    now, userId));
                    continue;
                }
                mtSitePlantReleation = sitePlantRelationMap.get(iface.getPlantCode()).get(0);
                if (mtSitePlantReleation == null || StringUtils.isEmpty(mtSitePlantReleation.getSiteId())) {
                    result.add(constructIfaceMessage(tenantId, iface, "E",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0012",
                                                    "INTERFACE", iface.getIfaceId(), "【API:ebsPoLineInterfaceImport】"),
                                    now, userId));
                    continue;
                }

                if (MapUtils.isEmpty(mtMaterialSiteMap) || StringUtils.isEmpty(iface.getItemId())
                                || CollectionUtils.isEmpty(mtMaterialSiteMap.get(Double.valueOf(iface.getItemId())))) {
                    result.add(constructIfaceMessage(tenantId, iface, "E",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0023",
                                                    "INTERFACE", iface.getIfaceId(), "【API:ebsPoLineInterfaceImport】"),
                                    now, userId));
                    continue;
                }
                mtMaterialSite = mtMaterialSiteMap.get(Double.valueOf(iface.getItemId())).get(0);

                if (mtMaterialSite == null
                                || !mtMaterialSite.getSiteId().equalsIgnoreCase(mtSitePlantReleation.getSiteId())) {
                    result.add(constructIfaceMessage(tenantId, iface, "E",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0023",
                                                    "INTERFACE", iface.getIfaceId(), "【API:ebsPoLineInterfaceImport】"),
                                    now, userId));
                    continue;
                }
                if (StringUtils.isNotEmpty(iface.getWipEntityId())) {
                    if (MapUtils.isEmpty(mtWorkOrdersMap) || CollectionUtils
                                    .isEmpty(mtWorkOrdersMap.get(Double.valueOf(iface.getWipEntityId())))) {
                        result.add(constructIfaceMessage(tenantId, iface, "E",
                                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0022",
                                                        "INTERFACE", iface.getIfaceId(),
                                                        "【API:ebsPoLineInterfaceImport】"),
                                        now, userId));
                        continue;
                    } else {
                        mtWorkOrder = mtWorkOrdersMap.get(Double.valueOf(iface.getWipEntityId())).get(0);
                        if (mtWorkOrder == null) {
                            result.add(constructIfaceMessage(tenantId, iface, "E",
                                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0022",
                                                            "INTERFACE", iface.getIfaceId(),
                                                            "【API:ebsPoLineInterfaceImport】"),
                                            now, userId));
                            continue;
                        }
                    }
                } else {
                    mtWorkOrder = null;
                }
                // uomId
                if (MapUtils.isEmpty(mtUomsMap) || CollectionUtils.isEmpty(mtUomsMap.get(iface.getUom()))) {
                    result.add(constructIfaceMessage(tenantId, iface, "E",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0019",
                                                    "INTERFACE", iface.getIfaceId(), "【API:ebsPoLineInterfaceImport】"),
                                    now, userId));
                    continue;
                }
                mtUom = mtUomsMap.get(iface.getUom()).get(0);
                Integer isNewType;
                if (MapUtils.isEmpty(mtPoLinesDistributionMap)) {
                    if (MapUtils.isEmpty(mtPoLinesPoLineMap)
                                    || CollectionUtils.isEmpty(mtPoLinesPoLineMap.get(iface.getPoLineId()))) {
                        // insert
                        isNewType = 0;
                    } else {
                        // update
                        isNewType = 1;
                    }
                } else {
                    if (CollectionUtils.isEmpty(mtPoLinesDistributionMap.get(iface.getPoDistributionId()))) {
                        if (MapUtils.isEmpty(mtPoLinesPoLineMap)
                                        || CollectionUtils.isEmpty(mtPoLinesPoLineMap.get(iface.getPoLineId()))) {
                            // insert
                            isNewType = 0;
                        } else {
                            // update
                            isNewType = 1;
                        }
                    } else {
                        // update
                        isNewType = 2;
                    }

                }
                if (isNewType == 0) {
                    mtNumrangeVO10 = new MtNumrangeVO10();
                    Map<String, String> objectCodeMap = new HashMap<>();
                    objectCodeMap.put("poHeadId", mtPoHeader.getPoHeaderId());
                    mtNumrangeVO10.setCallObjectCode(objectCodeMap);
                    mtNumrangeVO10.setSequence(sequence);
                    sequence += 1;
                    mtNumrangeVO10s.add(mtNumrangeVO10);
                }
                switch (isNewType) {
                    case 0:
                        mtPoLine = new MtPoLine();
                        mtPoLine = constructMtPoLine(tenantId, mtPoLine, iface, mtPoHeader.getPoHeaderId(),
                                        mtSitePlantReleation.getSiteId(), mtMaterialSite.getMaterialId(),
                                        mtUom.getUomId(), mtWorkOrder == null ? "" : mtWorkOrder.getWorkOrderId(), now,
                                        userId, true, newoLineIds.remove(0), newoLineCids.remove(0));
                        break;
                    case 1:
                        // update
                        mtPoLine = mtPoLinesPoLineMap.get(iface.getPoLineId()).get(0);
                        mtPoLine = constructMtPoLine(tenantId, mtPoLine, iface, mtPoHeader.getPoHeaderId(),
                                        mtSitePlantReleation.getSiteId(), mtMaterialSite.getMaterialId(),
                                        mtUom.getUomId(), mtWorkOrder == null ? "" : mtWorkOrder.getWorkOrderId(), now,
                                        userId, false, null, newoLineCids.remove(0));

                        break;
                    case 2:
                        // update
                        mtPoLine = mtPoLinesDistributionMap.get(iface.getPoDistributionId()).get(0);
                        mtPoLine = constructMtPoLine(tenantId, mtPoLine, iface, mtPoHeader.getPoHeaderId(),
                                        mtSitePlantReleation.getSiteId(), mtMaterialSite.getMaterialId(),
                                        mtUom.getUomId(), mtWorkOrder == null ? "" : mtWorkOrder.getWorkOrderId(), now,
                                        userId, false, null, newoLineCids.remove(0));
                        break;
                    default:
                        mtPoLine = new MtPoLine();
                        break;
                }
                result.add(constructIfaceMessage(tenantId, iface, "S", "成功.", now, userId));
                attrMap.put(iface, mtPoLine.getPoLineId());
                changeList.add(mtPoLine);

            } catch (Exception e) {
                throw new MtException("MT_INTERFACE_0011",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0011", "INTERFACE",
                                                iface.getIfaceId(), e.getMessage(), "【API:ebsPoLineInterfaceImport】"));
            }

        }


        /**
         * 获取LineNum
         */
        if (CollectionUtils.isNotEmpty(mtNumrangeVO10s)) {
            MtNumrangeVO9 mtNumrange = new MtNumrangeVO9();
            mtNumrange.setCallObjectCodeList(mtNumrangeVO10s);
            mtNumrange.setObjectCode("PO_LINE_NUM");
            mtNumrange.setNumQty(sequence);
            mtNumrange.setObjectNumFlag("N");
            MtNumrangeVO8 mtNumrangeVO8 = mtNumrangeRepository.numrangeBatchGenerate(tenantId, mtNumrange);
            if (mtNumrangeVO8 != null && CollectionUtils.isNotEmpty(mtNumrangeVO8.getNumberList())) {
                LinkedMultiValueMap<String, String> lineNumMap =
                                new LinkedMultiValueMap<>(mtNumrangeVO8.getNumberList().size());
                mtNumrangeVO8.getNumberList().stream().forEach(c -> {
                    String[] temp = c.split("-");
                    lineNumMap.add(temp.length > 1 ? temp[0] : c, temp.length > 1 ? temp[1] : c);
                });
                changeList.stream().forEach(c -> {
                    if (CollectionUtils.isNotEmpty(lineNumMap.get(c.getPoHeaderId()))
                                    && StringUtils.isEmpty(c.getLineNum())) {
                        c.setLineNum(lineNumMap.get(c.getPoHeaderId()).remove(0));
                    }
                });
            }
        }

        if (CollectionUtils.isNotEmpty(changeList)) {
            List<AuditDomain> tempList = new ArrayList<>(changeList.size());
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
    public List<MtEbsPoLineIface> updateIfaceStatus(Long tenantId, String status) {
        List<MtEbsPoLineIface> list = mtEbsPoLineIfaceMapper.getUnprocessedList(tenantId);
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

    private MtPoLine constructMtPoLine(Long tenantId, MtPoLine mtPoLine, MtEbsPoLineIface iface, String headerId,
                    String siteId, String materialId, String uomId, String workOrderId, Date now, Long userId,
                    Boolean isNew, String id, String cid) {
        mtPoLine.setTenantId(tenantId);
        mtPoLine.setErpPoDistributionId(iface.getPoDistributionId());
        mtPoLine.setErpPoLineId(iface.getPoLineId());
        mtPoLine.setErpPoDistributionNum(iface.getPoDistributionNum());
        mtPoLine.setErpPoLocationId(iface.getPoLocationId());
        mtPoLine.setErpPoShipmentNum(iface.getPoShipmentNum());
        mtPoLine.setErpPoReleaseId(iface.getPoReleaseId());
        mtPoLine.setErpPoHeaderId(iface.getPoHeaderId());
        mtPoLine.setPoHeaderId(headerId);
        mtPoLine.setErpPoLineNum(iface.getPoLineNum());
        mtPoLine.setSiteId(siteId);
        mtPoLine.setMaterialId(materialId);
        mtPoLine.setUomId(uomId);
        mtPoLine.setUnitPrice(iface.getUnitPrice());
        mtPoLine.setLineDescription(iface.getLineDescription());
        mtPoLine.setExpirationDate(iface.getExpirationDate());
        mtPoLine.setQuantityAccepted(iface.getQuantityAccepted());
        mtPoLine.setQuantityCancelled(iface.getQuantityCancelled());
        mtPoLine.setQuantityDelivered(iface.getQuantityDelivered());
        mtPoLine.setQuantityOrdered(iface.getQuantityOrdered());
        mtPoLine.setQuantityReceived(iface.getQuantityReceived());
        if (MtBaseConstants.YES.equalsIgnoreCase(iface.getLineCancelFlag())
                        || MtBaseConstants.YES.equalsIgnoreCase(iface.getLocationCancelFlag())) {
            mtPoLine.setCancelFlag("Y");
        } else {
            mtPoLine.setCancelFlag("N");
        }
        if (CLOSED_FLAG.contains(iface.getLineClosedCode()) || CLOSED_FLAG.contains(iface.getLocationClosedCode())) {
            mtPoLine.setClosedFlag("Y");
        } else {
            mtPoLine.setClosedFlag("N");
        }
        mtPoLine.setNeedByDate(iface.getNeedByDate());
        mtPoLine.setConsignedFlag(iface.getConsignedFlag());
        mtPoLine.setLineType(iface.getLineTpye());
        mtPoLine.setErpReceivingRoutingId(iface.getReceivingRoutingId());
        mtPoLine.setWorkOrderId(workOrderId);

        mtPoLine.setLastUpdateDate(now);
        mtPoLine.setLastUpdatedBy(userId);
        if (isNew) {
            mtPoLine.setPoLineId(id);
            mtPoLine.setCreatedBy(userId);
            mtPoLine.setCreationDate(now);
            mtPoLine.setObjectVersionNumber(1L);
        } else {
            mtPoLine.setObjectVersionNumber(mtPoLine.getObjectVersionNumber() + 1L);
        }
        mtPoLine.setCid(Long.valueOf(cid));
        return mtPoLine;
    }

    /**
     * 保存扩展属性
     */
    private List<String> saveAttrColumn(Long tenantId, Map<MtEbsPoLineIface, String> attrMap) {

        // 获取系统支持的所有语言环境
        List<Language> languages = LanguageHelper.languages();
        List<String> result = new ArrayList<>();

        // 扩展表批量查询
        List<String> kidList = attrMap.entrySet().stream().map(t -> t.getValue()).collect(Collectors.toList());
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(MT_PO_LINE_ATTR);
        mtExtendVO1.setKeyIdList(kidList);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        Map<String, List<MtExtendAttrVO1>> originMap =
                        extendAttrList.stream().collect(Collectors.groupingBy(t -> t.getKeyId()));
        Map<String, List<MtExtendVO5>> attrTableMap = new HashMap<>(attrMap.size());
        attrMap.entrySet().stream().forEach(t -> {
            Map<String, String> fieldMap = ObjectFieldsHelper.getAttributeFields(t.getKey(), "lineAttribute");
            fieldMap.putAll(ObjectFieldsHelper.getAttributeFields(t.getKey(), "locationAttribute"));
            fieldMap.putAll(ObjectFieldsHelper.getAttributeFields(t.getKey(), "distributionAttribute"));
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
            result.addAll(mtExtendSettingsRepository.attrPropertyUpdateForIface(tenantId, MT_PO_LINE_ATTR,
                            attrTableMap));
        }
        return result;

    }

    /**
     * 构建返回消息
     *
     * @author benjamin
     * @date 2019-06-27 17:02
     * @param mtEbsPoLineIface MtCostcenterIface
     * @param message 错误消息
     * @return MtEbsPoLineIface
     */
    private MtEbsPoLineIface constructIfaceMessage(Long tenantId, MtEbsPoLineIface mtEbsPoLineIface, String status,
                    String message, Date date, Long userId) {
        mtEbsPoLineIface.setTenantId(tenantId);
        mtEbsPoLineIface.setStatus(status);
        mtEbsPoLineIface.setMessage(message);
        mtEbsPoLineIface.setLastUpdateDate(date);
        mtEbsPoLineIface.setLastUpdatedBy(userId);

        return mtEbsPoLineIface;
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
}
