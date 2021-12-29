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
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.*;
import tarzan.iface.domain.entity.MtPoHeader;
import tarzan.iface.domain.entity.MtPoLine;
import tarzan.iface.domain.entity.MtSapPoIface;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtSapPoIfaceRepository;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.iface.domain.vo.*;
import tarzan.iface.infra.mapper.MtPoHeaderMapper;
import tarzan.iface.infra.mapper.MtPoLineMapper;
import tarzan.iface.infra.mapper.MtSapPoIfaceMapper;
import tarzan.iface.infra.mapper.MtSitePlantReleationMapper;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.infra.mapper.MtUomMapper;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.entity.MtSupplierSite;
import tarzan.modeling.domain.repository.MtSupplierRepository;
import tarzan.modeling.infra.mapper.MtSupplierSiteMapper;

/**
 * SAP采购订单接口表 资源库实现
 *
 * @author peng.yuan@hand-china.com 2019-10-08 19:40:53
 */
@Component
public class MtSapPoIfaceRepositoryImpl extends BaseRepositoryImpl<MtSapPoIface> implements MtSapPoIfaceRepository {

    private static final int SQL_ITEM_COUNT_LIMIT = 1000;

    private static final String MT_PO_HEADER_ATTR = "mt_po_header_attr";
    private static final String MT_PO_LINE_ATTR = "mt_po_line_attr";


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtSapPoIfaceMapper mtSapPoIfaceMapper;

    @Autowired
    private MtSitePlantReleationMapper mtSitePlantReleationMapper;

    @Autowired
    private MtPoHeaderMapper mtPoHeaderMapper;

    @Autowired
    private MtPoLineMapper mtPoLineMapper;

    @Autowired
    private MtSupplierSiteMapper mtSupplierSiteMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepository;

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    @Autowired
    private MtUomMapper mtUomMapper;

    @Autowired
    private MtSupplierRepository mtSupplierRepository;

    @Override
    public void sapPoInterfaceImport(Long tenantId) {
        Long start = System.currentTimeMillis();
        List<MtSapPoIface> mtSapPoIfaceList = self().updateIfaceStatus(tenantId, "P");
        Map<Double, List<MtSapPoIface>> ifacePerBatch = mtSapPoIfaceList.stream()
                        .collect(Collectors.groupingBy(MtSapPoIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtSapPoIface>> entry : ifacePerBatch.entrySet()) {
            try {
                // change data status
                List<AuditDomain> ifaceList = saveSapPoIface(tenantId, entry.getValue());
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
        System.out.println("SAP采购订单数据导入" + mtSapPoIfaceList.size() + "条数据，总耗时：" + (System.currentTimeMillis() - start)
                        + "毫秒");

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AuditDomain> saveSapPoIface(Long tenantId, List<MtSapPoIface> list) {
        Long userId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        Date now = new Date(System.currentTimeMillis());
        List<String> sqlList = new ArrayList<>();
        List<AuditDomain> result = new ArrayList<>();

        // plant code
        List<String> plantCodes = list.stream().map(MtSapPoIface::getPlantCode).distinct().collect(Collectors.toList());


        // transfer plant code
        List<String> transferPlantCodes =
                        list.stream().map(MtSapPoIface::getTransferPlantCode).distinct().collect(Collectors.toList());

        // supplier code
        List<String> supplierCodes =
                        list.stream().map(MtSapPoIface::getSupplierCode).distinct().collect(Collectors.toList());

        // uom code
        List<String> uomCodes = list.stream().map(MtSapPoIface::getUom).distinct().collect(Collectors.toList());

        // supplier site code
        List<String> supplierSiteCodes =
                        list.stream().map(MtSapPoIface::getSupplierSiteCode).distinct().collect(Collectors.toList());

        // po number
        List<String> poNumbers = list.stream().map(MtSapPoIface::getPoNumber).distinct().collect(Collectors.toList());

        // line num
        List<String> lineNum = list.stream().map(MtSapPoIface::getLineNum).distinct().collect(Collectors.toList());

        // material code
        List<String> itemCodes = list.stream().map(MtSapPoIface::getItemCode).distinct().collect(Collectors.toList());

        // po header list for poNum
        String whereInValuesSql = StringHelper.getWhereInValuesSql("t.PO_NUMBER", poNumbers, 1000);

        List<MtPoHeader> poNum = mtPoHeaderMapper.selectOnlyByPoNumber(tenantId, whereInValuesSql);
        Map<MtSapPoIfaceVO1, List<MtPoHeader>> poNumMap = poNum.stream()
                        .collect(Collectors.groupingBy(t -> new MtSapPoIfaceVO1(t.getPoNumber(), t.getPoReleaseNum())));

        // uomId from uomCodes
        whereInValuesSql = StringHelper.getWhereInValuesSql("tb.UOM_CODE", uomCodes, 1000);
        List<MtUom> mtUoms = mtUomMapper.selectByUomCode(tenantId, whereInValuesSql);
        Map<String, List<MtUom>> mtUomsMap = mtUoms.stream().collect(Collectors.groupingBy(MtUom::getUomCode));

        // po header list for releaseNum
        whereInValuesSql = StringHelper.getWhereInValuesSql("t.PO_RELEASE_NUM", poNumbers, 1000);
        List<MtPoHeader> releaseNum = mtPoHeaderMapper.selectOnlyByReleaseNum(tenantId, whereInValuesSql);
        Map<MtSapPoIfaceVO1, List<MtPoHeader>> releaseNumMap = releaseNum.stream()
                        .collect(Collectors.groupingBy(t -> new MtSapPoIfaceVO1(t.getPoNumber(), t.getPoReleaseNum())));

        // po line list for line num
        whereInValuesSql = StringHelper.getWhereInValuesSql("t.ERP_PO_LINE_NUM", lineNum, 1000);
        List<MtPoLine> mtPoLines = mtPoLineMapper.selectByPoNums(tenantId, whereInValuesSql);
        Map<MtSapPoIfaceVO2, List<MtPoLine>> mtPoLinesMap = mtPoLines.stream()
                        .collect(Collectors.groupingBy(t -> new MtSapPoIfaceVO2(t.getErpPoNum(), t.getErpPoLineNum())));


        // material id
        MtSitePlantReleationVO mtSitePlantReleationVO = new MtSitePlantReleationVO();
        mtSitePlantReleationVO.setItemCodeList(itemCodes);
        mtSitePlantReleationVO.setPlantCodeList(plantCodes);
        mtSitePlantReleationVO.setSiteType("MANUFACTURING");
        List<MtSitePlantReleationVO1> mtSitePlantReleationVO1s =
                        mtSitePlantReleationRepository.itemMaterialSiteIdBatchQuery(tenantId, mtSitePlantReleationVO);

        Map<Tuple, List<MtSitePlantReleationVO1>> mtSitePlantReleationVO1sMap = mtSitePlantReleationVO1s.stream()
                        .collect(Collectors.groupingBy(t -> new Tuple(t.getPlantCode(), t.getItemCode())));


        // supper id for supplier code
        List<MtSupplier> mtSuppliers = mtSupplierRepository.querySupplierByCode(tenantId, supplierCodes);
        Map<String, List<MtSupplier>> mtSuppliersMap =
                        mtSuppliers.stream().collect(Collectors.groupingBy(MtSupplier::getSupplierCode));

        // supper sitec id for supplier site code
        whereInValuesSql = StringHelper.getWhereInValuesSql("SUPPLIER_SITE_CODE", supplierSiteCodes, 1000);
        List<MtSupplierSite> mtSupplierSites = mtSupplierSiteMapper.querySupplierSiteCodes(tenantId, whereInValuesSql);
        Map<String, List<MtSupplierSite>> mtSupplierSitesMap =
                        mtSupplierSites.stream().collect(Collectors.groupingBy(MtSupplierSite::getSupplierSiteCode));

        // site id list for plant code
        MtSitePlantReleationVO3 mtSitePlantReleationVO3 = new MtSitePlantReleationVO3();
        mtSitePlantReleationVO3.setSiteType("MANUFACTURING");
        mtSitePlantReleationVO3.setPlantCodes(plantCodes);
        List<MtSitePlantReleation> sitePlantRelationList =
                        mtSitePlantReleationMapper.getsiteIdList(tenantId, mtSitePlantReleationVO3);
        Map<String, List<MtSitePlantReleation>> sitePlantRelationMap = sitePlantRelationList.stream()
                        .collect(Collectors.groupingBy(MtSitePlantReleation::getPlantCode));


        // transfer site id
        mtSitePlantReleationVO3.setPlantCodes(transferPlantCodes);
        List<MtSitePlantReleation> transForSiteId =
                        mtSitePlantReleationMapper.getsiteIdList(tenantId, mtSitePlantReleationVO3);
        Map<String, List<MtSitePlantReleation>> transForSiteIdMap =
                        transForSiteId.stream().collect(Collectors.groupingBy(MtSitePlantReleation::getPlantCode));
        // group by head
        Map<MtSapPoIfaceVO, List<MtSapPoIface>> ifaceVOListMap = list.stream()
                        .collect(Collectors.groupingBy(t -> new MtSapPoIfaceVO(t.getPlantCode(), t.getPoNumber(),
                                        t.getContractNum(), t.getSupplierCode(), t.getSupplierSiteCode(),
                                        t.getBuyerCode(), t.getPoCategory(), t.getPoOrderType(), t.getApprovedFlag(),
                                        t.getDescription(), t.getCurrencyCode(), t.getPoEnableFlag(),
                                        t.getTransferPlantCode())));

        // 获取Cid、Id
        List<String> newPoHeaderIds =
                        this.customDbRepository.getNextKeys("mt_po_header_s", ifaceVOListMap.keySet().size());
        List<String> newPoHeaderCids =
                        this.customDbRepository.getNextKeys("mt_po_header_cid_s", ifaceVOListMap.keySet().size());
        List<String> newPoLineIds = this.customDbRepository.getNextKeys("mt_po_line_s", list.size());
        List<String> newPoLineCids = this.customDbRepository.getNextKeys("mt_po_line_cid_s", list.size());

        MtSapPoIfaceVO mtSapPoIfaceVO;
        MtPoHeader mtPoHeader;
        MtSitePlantReleation mtSitePlantReleation;
        MtSitePlantReleation mtTransferSite;
        MtSupplier mtSupplier;
        MtSupplierSite mtSupplierSite;
        MtSapPoIface temp;
        Tuple tuple;
        MtSitePlantReleationVO1 mtSitePlantReleationVO1;
        MtUom mtUom;
        MtSapPoIfaceVO2 mtSapPoIfaceVO2;
        MtSapPoIfaceVO2 mtSapPoIfaceVOForC;

        MtNumrangeVO10 mtNumrangeVO10;
        List<MtNumrangeVO10> mtNumrangeVO10s = new ArrayList<>();
        Map<MtSapPoIface, String> attrMap = new HashMap<>(ifaceVOListMap.keySet().size());
        Map<MtSapPoIface, List<String>> attrLineMap = new HashMap<>(ifaceVOListMap.keySet().size());
        List<MtPoLine> changeLineList = new ArrayList<>(list.size());
        List<AuditDomain> changeHeadList = new ArrayList<>(ifaceVOListMap.keySet().size());
        Long sequence = 0L;
        for (Map.Entry<MtSapPoIfaceVO, List<MtSapPoIface>> entry : ifaceVOListMap.entrySet()) {
            try {
                mtSapPoIfaceVO = entry.getKey();
                temp = entry.getValue().get(0);

                // deal with head data
                // 参数校验
                if (MapUtils.isEmpty(sitePlantRelationMap)
                                || CollectionUtils.isEmpty(sitePlantRelationMap.get(mtSapPoIfaceVO.getPlantCode()))) {

                    result.addAll(constructIfaceMessage(tenantId, entry.getValue(), "E", "MT_INTERFACE_0012",
                                    "INTERFACE", "【API:sapPoInterfaceImport】", now, userId));
                    continue;
                }
                mtSitePlantReleation = sitePlantRelationMap.get(mtSapPoIfaceVO.getPlantCode()).get(0);
                if (StringUtils.isNotEmpty(mtSapPoIfaceVO.getTransferPlantCode())
                                && (MapUtils.isEmpty(transForSiteIdMap) || CollectionUtils.isEmpty(
                                                transForSiteIdMap.get(mtSapPoIfaceVO.getTransferPlantCode())))) {

                    result.addAll(constructIfaceMessage(tenantId, entry.getValue(), "E", "MT_INTERFACE_0012",
                                    "INTERFACE", "【API:sapPoInterfaceImport】", now, userId));
                    continue;


                }
                if (StringUtils.isNotEmpty(mtSapPoIfaceVO.getTransferPlantCode())) {
                    mtTransferSite = transForSiteIdMap.get(mtSapPoIfaceVO.getPlantCode()).get(0);
                } else {
                    mtTransferSite = null;
                }
                String transferSiteId = mtTransferSite == null ? "" : mtTransferSite.getSiteId();
                if (MapUtils.isEmpty(mtSuppliersMap)
                                || CollectionUtils.isEmpty(mtSuppliersMap.get(mtSapPoIfaceVO.getSupplierCode()))) {

                    result.addAll(constructIfaceMessage(tenantId, entry.getValue(), "E", "MT_INTERFACE_0031",
                                    "INTERFACE", "【API:sapPoInterfaceImport】", now, userId));
                    continue;
                }
                mtSupplier = mtSuppliersMap.get(mtSapPoIfaceVO.getSupplierCode()).get(0);

                if (MapUtils.isEmpty(mtSupplierSitesMap) || CollectionUtils
                                .isEmpty(mtSupplierSitesMap.get(mtSapPoIfaceVO.getSupplierSiteCode()))) {

                    result.addAll(constructIfaceMessage(tenantId, entry.getValue(), "E", "MT_INTERFACE_0032",
                                    "INTERFACE", "【API:sapPoInterfaceImport】", now, userId));
                    continue;
                }
                mtSupplierSite = mtSupplierSitesMap.get(mtSapPoIfaceVO.getSupplierSiteCode()).get(0);
                MtSapPoIfaceVO1 mtSapPoIfaceCon =
                                new MtSapPoIfaceVO1(mtSapPoIfaceVO.getPoNumber(), mtSapPoIfaceVO.getContractNum());
                MtSapPoIfaceVO1 mtSapPoIface =
                                new MtSapPoIfaceVO1(mtSapPoIfaceVO.getContractNum(), mtSapPoIfaceVO.getPoNumber());
                Integer headIsNewType;
                if (StringUtils.isEmpty(mtSapPoIfaceVO.getContractNum())) {
                    if (MapUtils.isEmpty(poNumMap) || CollectionUtils.isEmpty(poNumMap.get(mtSapPoIfaceCon))) {
                        // insert
                        headIsNewType = 0;
                    } else {
                        // update
                        headIsNewType = 1;
                    }
                } else {
                    if (MapUtils.isEmpty(releaseNumMap) || CollectionUtils.isEmpty(releaseNumMap.get(mtSapPoIface))) {

                        // insert
                        headIsNewType = 0;

                    } else {
                        // update
                        headIsNewType = 2;
                    }
                }
                switch (headIsNewType) {
                    case 0:
                        mtPoHeader = new MtPoHeader();
                        mtPoHeader = constructMtPoHeader(tenantId, mtPoHeader, mtSapPoIfaceVO, now, userId,
                                        mtSitePlantReleation.getSiteId(), mtSupplier.getSupplierId(),
                                        mtSupplierSite.getSupplierSiteId(), transferSiteId, true,
                                        newPoHeaderIds.remove(0), newPoHeaderCids.remove(0));
                        break;
                    case 1:
                        // update
                        mtPoHeader = poNumMap.get(mtSapPoIfaceCon).get(0);
                        mtPoHeader = constructMtPoHeader(tenantId, mtPoHeader, mtSapPoIfaceVO, now, userId,
                                        mtSitePlantReleation.getSiteId(), mtSupplier.getSupplierId(),
                                        mtSupplierSite.getSupplierSiteId(), transferSiteId, false, null,
                                        newPoHeaderCids.remove(0));
                        break;
                    case 2:
                        // update
                        mtPoHeader = releaseNumMap.get(mtSapPoIface).get(0);
                        mtPoHeader = constructMtPoHeader(tenantId, mtPoHeader, mtSapPoIfaceVO, now, userId,
                                        mtSitePlantReleation.getSiteId(), mtSupplier.getSupplierId(),
                                        mtSupplierSite.getSupplierSiteId(), transferSiteId, false, null,
                                        newPoHeaderCids.remove(0));
                        break;
                    default:
                        mtPoHeader = new MtPoHeader();
                        break;
                }
                attrMap.put(temp, mtPoHeader.getPoHeaderId());
                changeHeadList.add(mtPoHeader);

                // deal with po line
                List<String> lineIdList = new ArrayList<>(entry.getValue().size());
                MtPoLine mtPoLine;
                for (MtSapPoIface iface : entry.getValue()) {
                    tuple = new Tuple(iface.getPlantCode(), iface.getItemCode());
                    // 参数校验
                    if (MapUtils.isEmpty(mtSitePlantReleationVO1sMap)
                                    || CollectionUtils.isEmpty(mtSitePlantReleationVO1sMap.get(tuple))
                                    || StringUtils.isEmpty(
                                                    mtSitePlantReleationVO1sMap.get(tuple).get(0).getMaterialId())) {
                        result.addAll(constructIfaceMessage(tenantId, Arrays.asList(iface), "E", "MT_INTERFACE_0023",
                                        "INTERFACE", "【API:sapPoInterfaceImport】", now, userId));
                        continue;
                    }
                    mtSitePlantReleationVO1 = mtSitePlantReleationVO1sMap.get(tuple).get(0);


                    if (MapUtils.isEmpty(mtUomsMap) || CollectionUtils.isEmpty(mtUomsMap.get(iface.getUom()))) {
                        result.addAll(constructIfaceMessage(tenantId, Arrays.asList(iface), "E", "MT_INTERFACE_0019",
                                        "INTERFACE", "【API:sapPoInterfaceImport】", now, userId));
                        continue;
                    }
                    mtUom = mtUomsMap.get(iface.getUom()).get(0);

                    // 行数据
                    mtSapPoIfaceVO2 = new MtSapPoIfaceVO2(iface.getPoNumber(), iface.getLineNum());
                    mtSapPoIfaceVOForC = new MtSapPoIfaceVO2(iface.getContractNum(), iface.getLineNum());

                    Integer lineIsNewType = 0;
                    mtPoLine = new MtPoLine();
                    if (StringUtils.isEmpty(iface.getShipmentNum())) {
                        if (MapUtils.isNotEmpty(mtPoLinesMap)
                                        && CollectionUtils.isNotEmpty(mtPoLinesMap.get(mtSapPoIfaceVO2))) {
                            Optional<MtPoLine> first = mtPoLinesMap.get(mtSapPoIfaceVO2).stream()
                                            .filter(t -> StringUtils.isEmpty(t.getErpPoShipmentNum())).findFirst();
                            if (first.isPresent()) {
                                // update
                                lineIsNewType = 1;
                                mtPoLine = first.get();
                            }
                        }
                    } else {
                        if (StringUtils.isEmpty(iface.getContractNum())) {
                            if (MapUtils.isNotEmpty(mtPoLinesMap)
                                            && CollectionUtils.isNotEmpty(mtPoLinesMap.get(mtSapPoIfaceVO2))) {
                                Optional<MtPoLine> first = mtPoLinesMap.get(mtSapPoIfaceVO2).stream()
                                                .filter(t -> iface.getShipmentNum().equals(t.getErpPoShipmentNum())
                                                                && StringUtils.isEmpty(t.getErpPoReleaseNum()))
                                                .findFirst();
                                if (first.isPresent()) {
                                    // update
                                    lineIsNewType = 1;
                                    mtPoLine = first.get();
                                }
                            }
                        } else {
                            if (MapUtils.isNotEmpty(mtPoLinesMap)
                                            && CollectionUtils.isNotEmpty(mtPoLinesMap.get(mtSapPoIfaceVOForC))) {
                                Optional<MtPoLine> first = mtPoLinesMap.get(mtSapPoIfaceVOForC).stream()
                                                .filter(t -> iface.getShipmentNum().equals(t.getErpPoShipmentNum())
                                                                && iface.getPoNumber().equals(t.getErpPoReleaseNum()))
                                                .findFirst();
                                if (first.isPresent()) {
                                    // update
                                    lineIsNewType = 1;
                                    mtPoLine = first.get();
                                }
                            }
                        }
                    }
                    // get Line Num
                    switch (lineIsNewType) {
                        case 0:
                            mtPoLine = constructMtPoLine(tenantId, mtPoLine, iface, mtPoHeader.getPoHeaderId(),
                                            mtSitePlantReleationVO1.getMaterialId(), mtUom.getUomId(), now, userId,
                                            true, mtSitePlantReleation.getSiteId(), newPoLineIds.remove(0),
                                            newPoLineCids.remove(0));
                            mtNumrangeVO10 = new MtNumrangeVO10();
                            Map<String, String> objectCodeMap = new HashMap<>();
                            objectCodeMap.put("poHeadId", mtPoHeader.getPoHeaderId());
                            mtNumrangeVO10.setCallObjectCode(objectCodeMap);
                            mtNumrangeVO10.setSequence(sequence);
                            sequence += 1;
                            mtNumrangeVO10s.add(mtNumrangeVO10);
                            break;
                        case 1:
                            // update
                            mtPoLine = constructMtPoLine(tenantId, mtPoLine, iface, mtPoHeader.getPoHeaderId(),
                                            mtSitePlantReleationVO1.getMaterialId(), mtUom.getUomId(), now, userId,
                                            false, mtSitePlantReleation.getSiteId(), null, newPoLineCids.remove(0));
                            break;
                        default:
                            break;
                    }
                    changeLineList.add(mtPoLine);
                    lineIdList.add(mtPoLine.getPoLineId());

                    result.add(constructIfaceSuccessMessage(tenantId, iface, "S", "成功.", now, userId));
                }
                if (CollectionUtils.isNotEmpty(lineIdList)) {
                    attrLineMap.put(temp, lineIdList);
                }
            } catch (Exception e) {
                throw new MtException("MT_INTERFACE_0011",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0011", "INTERFACE",
                                                entry.getValue().get(0).getIfaceId(), e.getMessage(),
                                                "【API:sapPoInterfaceImport】"));
            }

        }
        /**
         * 获取Line_Num
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
                    String[] tempNum = c.split("-");
                    lineNumMap.add(tempNum.length > 1 ? tempNum[0] : c, tempNum.length > 1 ? tempNum[1] : c);
                });
                changeLineList.stream().forEach(c -> {
                    if (CollectionUtils.isNotEmpty(lineNumMap.get(c.getPoHeaderId()))
                                    && StringUtils.isEmpty(c.getLineNum())) {
                        c.setLineNum(lineNumMap.get(c.getPoHeaderId()).remove(0));
                    }
                });
            }
        }
        if (CollectionUtils.isNotEmpty(changeHeadList)) {
            sqlList.addAll(constructSql(changeHeadList));
            changeHeadList = null;
        }
        if (CollectionUtils.isNotEmpty(changeLineList)) {
            List<AuditDomain> tempList = new ArrayList<>(changeLineList.size());
            tempList.addAll(changeLineList);
            sqlList.addAll(constructSql(tempList));
            changeLineList = null;
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
            sqlList.addAll(saveAttrColumn(tenantId, attrMap, MT_PO_HEADER_ATTR, "poAttribute"));
        }
        if (MapUtils.isNotEmpty(attrLineMap)) {
            sqlList.addAll(saveAttrColumnAttr(tenantId, attrLineMap, MT_PO_LINE_ATTR, "lineAttribute"));
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
    public List<MtSapPoIface> updateIfaceStatus(Long tenantId, String status) {
        List<MtSapPoIface> list = mtSapPoIfaceMapper.getUnprocessedList(tenantId);
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
     * 构建返回消息
     *
     * @param mtSapPoIface MtSapPoIface
     * @return MtSapPoIface
     * @author benjamin
     * @date 2019-06-27 17:02
     */
    private List<MtSapPoIface> constructIfaceMessage(Long tenantId, List<MtSapPoIface> mtSapPoIface, String status,
                    String messageCode, String module, String api, Date date, Long userId) {
        mtSapPoIface.stream().forEach(t -> {
            t.setTenantId(tenantId);
            t.setStatus(status);
            t.setMessage(mtErrorMessageRepo.getErrorMessageWithModule(tenantId, messageCode, module, t.getIfaceId(),
                            api));
            t.setLastUpdateDate(date);
            t.setLastUpdatedBy(userId);
        });
        return mtSapPoIface;
    }

    /**
     * 分割数据集合 限制数量每项不多于SQL_ITEM_COUNT_LIMIT
     *
     * @return List<List>
     * @author benjamin
     * @date 2019-06-25 18:40
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

    /**
     * 保存扩展属性
     *
     * @param tenantId
     */
    private List<String> saveAttrColumn(Long tenantId, Map<MtSapPoIface, String> attrMap, String attrTable,
                    String attribute) {
        // 获取系统支持的所有语言环境
        List<Language> languages = LanguageHelper.languages();
        List<String> result = new ArrayList<>();

        // 扩展表批量查询
        List<String> kidList = attrMap.entrySet().stream().map(t -> t.getValue()).collect(Collectors.toList());
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(attrTable);
        mtExtendVO1.setKeyIdList(kidList);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
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
            result.addAll(mtExtendSettingsRepository.attrPropertyUpdateForIface(tenantId, attrTable, attrTableMap));
        }
        return result;
    }

    /**
     * 保存扩展属性--一对多
     *
     * @param tenantId
     * @param attrMap
     * @return
     */
    private List<String> saveAttrColumnAttr(Long tenantId, Map<MtSapPoIface, List<String>> attrMap, String tableName,
                    String attributePrefix) {

        // 获取系统支持的所有语言环境
        List<Language> languages = LanguageHelper.languages();

        // 扩展表批量查询
        List<List<String>> lists = attrMap.entrySet().stream().map(t -> t.getValue()).collect(Collectors.toList());
        List<String> kidList = new ArrayList<>();
        for (List<String> list : lists) {
            kidList.addAll(list);
        }

        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(tableName);
        mtExtendVO1.setKeyIdList(kidList);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);

        Map<String, List<MtExtendAttrVO1>> originMap =
                        extendAttrList.stream().collect(Collectors.groupingBy(t -> t.getKeyId()));

        Map<String, List<MtExtendVO5>> attrTableMap = new HashMap<>(attrMap.size());
        List<String> result = new ArrayList<>();
        attrMap.entrySet().stream().forEach(t -> {
            Map<String, String> fieldMap = ObjectFieldsHelper.getAttributeFields(t.getKey(), attributePrefix);

            // 扩展属性更新时未传入时置为空
            if (MapUtils.isNotEmpty(originMap)) {
                for (String s : t.getValue()) {
                    if (CollectionUtils.isNotEmpty(originMap.get(s))) {
                        for (MtExtendAttrVO1 attrVO1 : originMap.get(s)) {
                            fieldMap.putIfAbsent(attrVO1.getAttrName(), "");
                        }
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
                for (String s : t.getValue()) {
                    attrTableMap.put(s, saveExtendList);
                }
            }
        });
        if (MapUtils.isNotEmpty(attrTableMap)) {
            result.addAll(mtExtendSettingsRepository.attrPropertyUpdateForIface(tenantId, tableName, attrTableMap));
        }
        return result;

    }


    private MtPoHeader constructMtPoHeader(Long tenantId, MtPoHeader mtPoHeader, MtSapPoIfaceVO iface, Date now,
                    Long userId, String siteId, String supplierId, String supplierSiteId, String transferSiteId,
                    Boolean isNew, String id, String cid) {

        if (StringUtils.isNotEmpty(iface.getContractNum())) {
            mtPoHeader.setPoReleaseNum(iface.getPoNumber());
            mtPoHeader.setPoNumber(iface.getContractNum());
        } else {
            mtPoHeader.setPoNumber(iface.getPoNumber());
        }
        mtPoHeader.setBuyerCode(iface.getBuyerCode());
        mtPoHeader.setPoCategory(iface.getPoCategory());
        mtPoHeader.setPoOrderType(iface.getPoOrderType());
        mtPoHeader.setApprovedFlag(iface.getApprovedFlag());
        mtPoHeader.setDescription(iface.getDescription());
        mtPoHeader.setCurrencyCode(iface.getCurrencyCode());
        mtPoHeader.setCanceledFlag("N".equalsIgnoreCase(iface.getPoEnableFlag()) ? "Y" : "N");
        mtPoHeader.setSiteId(siteId);
        mtPoHeader.setTransferSiteId(transferSiteId);
        mtPoHeader.setSupplierId(supplierId);
        mtPoHeader.setSupplierSiteId(supplierSiteId);

        mtPoHeader.setLastUpdateDate(now);
        mtPoHeader.setLastUpdatedBy(userId);
        mtPoHeader.setTenantId(tenantId);
        if (isNew) {
            mtPoHeader.setPoHeaderId(id);
            mtPoHeader.setCreatedBy(userId);
            mtPoHeader.setCreationDate(now);
            mtPoHeader.setObjectVersionNumber(1L);
        } else {
            mtPoHeader.setObjectVersionNumber(mtPoHeader.getObjectVersionNumber() + 1L);
        }
        mtPoHeader.setCid(Long.valueOf(cid));
        return mtPoHeader;
    }

    private MtSapPoIface constructIfaceSuccessMessage(Long tenantId, MtSapPoIface mtSapPoIface, String status,
                    String message, Date date, Long userId) {

        mtSapPoIface.setTenantId(tenantId);
        mtSapPoIface.setStatus(status);
        mtSapPoIface.setMessage(message);
        mtSapPoIface.setLastUpdateDate(date);
        mtSapPoIface.setLastUpdatedBy(userId);

        return mtSapPoIface;
    }

    private MtPoLine constructMtPoLine(Long tenantId, MtPoLine mtPoLine, MtSapPoIface iface, String headerId,
                    String materialId, String uomId, Date now, Long userId, Boolean isNew, String siteId, String id,
                    String cid) {
        mtPoLine.setTenantId(tenantId);
        mtPoLine.setPoHeaderId(headerId);
        if (StringUtils.isNotEmpty(iface.getContractNum())) {
            mtPoLine.setErpPoReleaseNum(iface.getPoNumber());
            mtPoLine.setErpPoNum(iface.getContractNum());
        } else {
            mtPoLine.setErpPoNum(iface.getPoNumber());
        }
        mtPoLine.setLineType(iface.getLineType());
        mtPoLine.setSiteId(siteId);
        mtPoLine.setErpPoLineNum(iface.getLineNum());
        mtPoLine.setErpPoShipmentNum(iface.getShipmentNum());
        if ("K".equalsIgnoreCase(iface.getLineType())) {
            mtPoLine.setConsignedFlag("Y");
        } else {
            mtPoLine.setConsignedFlag("N");
        }
        mtPoLine.setMaterialId(materialId);
        mtPoLine.setUomId(uomId);
        mtPoLine.setUnitPrice(iface.getUnitPrice());
        mtPoLine.setLineDescription(iface.getLineDescription());
        mtPoLine.setQuantityDelivered(iface.getQuantityDelivered());
        mtPoLine.setQuantityOrdered(iface.getQuantityOrdered());
        mtPoLine.setNeedByDate(iface.getNeedDate());
        if ("Y".equalsIgnoreCase(iface.getCompleteFlag())) {
            mtPoLine.setClosedFlag("Y");
        } else {
            mtPoLine.setClosedFlag("N");
        }
        if ("N".equalsIgnoreCase(iface.getLineEnableFlag())) {
            mtPoLine.setCancelFlag("Y");
        } else {
            mtPoLine.setCancelFlag("N");
        }
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

    private static class Tuple {
        private String plantCode;
        private String itemCode;

        public String getItemCode() {
            return itemCode;
        }

        public Tuple(String plantCode, String itemCode) {
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

            Tuple tuple = (Tuple) o;

            if (plantCode != null ? !plantCode.equals(tuple.plantCode) : tuple.plantCode != null) {
                return false;
            }
            return getItemCode() != null ? getItemCode().equals(tuple.getItemCode()) : tuple.getItemCode() == null;
        }

        @Override
        public int hashCode() {
            int result = plantCode != null ? plantCode.hashCode() : 0;
            result = 31 * result + (getItemCode() != null ? getItemCode().hashCode() : 0);
            return result;
        }
    }
}
