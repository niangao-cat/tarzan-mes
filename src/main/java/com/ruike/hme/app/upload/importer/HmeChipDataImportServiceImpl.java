package com.ruike.hme.app.upload.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO3;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.HmeCosPatchPdaVO9;
import com.ruike.hme.domain.vo.HmeCosRetestImportVO;
import com.ruike.hme.domain.vo.HmeEoJobSnVO4;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeChipImportDataMapper;
import com.ruike.hme.infra.mapper.HmeCosPatchPdaMapper;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.MtWoComponentActualVO1;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * 六型芯片导入
 *
 * @author chaonan.hu@hand-china.com 2021-01-26 09:13:45
 */
@ImportService(templateCode = "HME.CHIP_DATA")
public class HmeChipDataImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private HmeChipTransferRepository hmeChipTransferRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeChipImportDataRepository hmeChipImportDataRepository;
    @Autowired
    private HmeServiceSplitRecordRepository hmeServiceSplitRecordRepository;
    @Autowired
    private HmeChipImportDataMapper hmeChipImportDataMapper;
    @Autowired
    private HmeCosOperationRecordRepository hmeCosOperationRecordRepository;
    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;
    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private HmeContainerCapacityRepository hmeContainerCapacityRepository;
    @Autowired
    private MtOperationRepository mtOperationRepository;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;
    @Autowired
    private HmeCosPatchPdaMapper hmeCosPatchPdaMapper;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long tenantId = curUser == null ? 0L : curUser.getTenantId();
        // 获取当前用户默认工厂
        String siteId = wmsSiteRepository.userDefaultSite(tenantId);
        if (CollectionUtils.isNotEmpty(data)) {
            List<HmeChipImportData> importVOList = new ArrayList<>();
            for (String vo : data) {
                HmeChipImportData importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeChipImportData.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                importVOList.add(importVO);
            }
            //数据校验
            String operationId = dataCheck(tenantId, importVOList, siteId);
            //业务逻辑
            importData(tenantId, importVOList, operationId);
        }
        return true;
    }

    /**
     * 数据校验
     *
     * @param tenantId
     * @param importVoList
     * @return void
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/01/26 09:15:67
     */
    private String dataCheck(Long tenantId, List<HmeChipImportData> importVoList, String siteId) {
        //数据重复校验
        Map<String, List<HmeChipImportData>> map = importVoList.stream().collect(Collectors.groupingBy(t -> {
            return t.getFoxNum() + "," + t.getPosition();
        }));
        for (Map.Entry<String, List<HmeChipImportData>> entry : map.entrySet()) {
            if (entry.getValue().size() > 1) {
                throw new MtException("HME_CHIP_DATA_0011", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_CHIP_DATA_0011", "HME",
                                entry.getValue().get(0).getFoxNum(), entry.getValue().get(0).getPosition()));
            }
        }
        //模版中只能存在一个工单
        List<String> workNumList = importVoList.stream().map(HmeChipImportData::getWorkNum).distinct().collect(Collectors.toList());
        if (workNumList.size() > 1) {
            throw new MtException("HME_CHIP_DATA_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0016", "HME"));
        }
        //盒号&来料条码校验
        Map<String, List<String>> foxNumMap = importVoList.stream().collect(Collectors.groupingBy(HmeChipImportData::getFoxNum,
                Collectors.mapping(HmeChipImportData::getSourceBarcode, Collectors.toList())));
        for (Map.Entry<String, List<String>> entry : foxNumMap.entrySet()) {
            List<String> sourceBarCodeList = entry.getValue().stream().distinct().collect(Collectors.toList());
            if (sourceBarCodeList.size() > 1) {
                throw new MtException("HME_CHIP_DATA_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0015", "HME", entry.getKey()));
            }
        }
        //盒号&COS类型校验
        Map<String, List<String>> cosTypeMap = importVoList.stream().collect(Collectors.groupingBy(HmeChipImportData::getFoxNum,
                Collectors.mapping(HmeChipImportData::getCosType, Collectors.toList())));
        for (Map.Entry<String, List<String>> entry : cosTypeMap.entrySet()) {
            List<String> cosTypeList = entry.getValue().stream().distinct().collect(Collectors.toList());
            if (cosTypeList.size() > 1) {
                throw new MtException("HME_CHIP_DATA_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0018", "HME", entry.getKey(), "COS类型"));
            }
        }
        //盒号&wafer校验
        Map<String, List<String>> waferMap = importVoList.stream().collect(Collectors.groupingBy(HmeChipImportData::getFoxNum,
                Collectors.mapping(HmeChipImportData::getWafer, Collectors.toList())));
        for (Map.Entry<String, List<String>> entry : waferMap.entrySet()) {
            List<String> cosTypeList = entry.getValue().stream().distinct().collect(Collectors.toList());
            if (cosTypeList.size() > 1) {
                throw new MtException("HME_CHIP_DATA_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0018", "HME", entry.getKey(), "WAFER"));
            }
        }
        //盒号&Avg(nm)校验
        Map<String, List<String>> avgMap = importVoList.stream().collect(Collectors.groupingBy(HmeChipImportData::getFoxNum,
                Collectors.mapping(HmeChipImportData::getAvgWavelenght, Collectors.toList())));
        for (Map.Entry<String, List<String>> entry : avgMap.entrySet()) {
            List<String> cosTypeList = entry.getValue().stream().distinct().collect(Collectors.toList());
            if (cosTypeList.size() > 1) {
                throw new MtException("HME_CHIP_DATA_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0018", "HME", entry.getKey(), "Avg(nm)"));
            }
        }
        //盒号&Type校验
        Map<String, List<String>> typeMap = importVoList.stream().collect(Collectors.groupingBy(HmeChipImportData::getFoxNum,
                Collectors.mapping(HmeChipImportData::getType, Collectors.toList())));
        for (Map.Entry<String, List<String>> entry : typeMap.entrySet()) {
            List<String> cosTypeList = entry.getValue().stream().distinct().collect(Collectors.toList());
            if (cosTypeList.size() > 1) {
                throw new MtException("HME_CHIP_DATA_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0018", "HME", entry.getKey(), "Type"));
            }
        }
        //盒号&LotNo校验
        Map<String, List<String>> lotnoMap = importVoList.stream().collect(Collectors.groupingBy(HmeChipImportData::getFoxNum,
                Collectors.mapping(HmeChipImportData::getLotno, Collectors.toList())));
        for (Map.Entry<String, List<String>> entry : lotnoMap.entrySet()) {
            List<String> cosTypeList = entry.getValue().stream().distinct().collect(Collectors.toList());
            if (cosTypeList.size() > 1) {
                throw new MtException("HME_CHIP_DATA_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0018", "HME", entry.getKey(), "LOTNO"));
            }
        }
        //盒号&备注校验
        Map<String, List<String>> remarkMap = importVoList.stream().collect(Collectors.groupingBy(HmeChipImportData::getFoxNum,
                Collectors.mapping(HmeChipImportData::getRemark, Collectors.toList())));
        for (Map.Entry<String, List<String>> entry : remarkMap.entrySet()) {
            List<String> cosTypeList = entry.getValue().stream().distinct().collect(Collectors.toList());
            if (cosTypeList.size() > 1) {
                throw new MtException("HME_CHIP_DATA_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0018", "HME", entry.getKey(), "备注"));
            }
        }
        //盒号&录入批次校验
        Map<String, List<String>> importLotMap = importVoList.stream().collect(Collectors.groupingBy(HmeChipImportData::getFoxNum,
                Collectors.mapping(HmeChipImportData::getImportLot, Collectors.toList())));
        for (Map.Entry<String, List<String>> entry : importLotMap.entrySet()) {
            List<String> cosTypeList = entry.getValue().stream().distinct().collect(Collectors.toList());
            if (cosTypeList.size() > 1) {
                throw new MtException("HME_CHIP_DATA_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0018", "HME", entry.getKey(), "录入批次"));
            }
        }
        //盒号&位置校验
        Map<String, List<String>> positionMap = importVoList.stream().collect(Collectors.groupingBy(HmeChipImportData::getFoxNum,
                Collectors.mapping(HmeChipImportData::getPosition, Collectors.toList())));
        for (Map.Entry<String, List<String>> entry : positionMap.entrySet()) {
            List<String> positionList = entry.getValue();
            List<String> distonctPositionList = entry.getValue().stream().distinct().collect(Collectors.toList());
            if (positionList.size() != distonctPositionList.size()) {
                throw new MtException("HME_CHIP_DATA_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0018", "HME", entry.getKey(), "位置"));
            }
        }

        //工位校验,并记录工位及其对应产线关系
        List<String> workcellList = importVoList.stream().map(HmeChipImportData::getWorkcell).distinct().collect(Collectors.toList());
        //模版中只能存在一个工位
        if (workcellList.size() > 1) {
            throw new MtException("HME_CHIP_DATA_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0017", "HME"));
        }
        Map<String, String> workcellProdLineMap = new HashMap<>();
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectOne(new MtModWorkcell() {{
            setTenantId(tenantId);
            setWorkcellCode(workcellList.get(0));
        }});
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeChipTransferRepository.workcellScan(tenantId, new HmeEoJobSnDTO() {{
            setSiteId(siteId);
            setWorkcellCode(workcellList.get(0));
        }});
        String operationId = hmeEoJobSnVO4.getOperationIdList().get(0);
        //查询工位对应的产线
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setOrganizationId(mtModWorkcell.getWorkcellId());
        mtModOrganizationVO2.setOrganizationType("WORKCELL");
        mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
        mtModOrganizationVO2.setTopSiteId(siteId);
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        if (CollectionUtils.isEmpty(mtModOrganizationItemVOS)) {
            throw new MtException("HME_CHIP_DATA_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0012", "HME", mtModWorkcell.getWorkcellCode()));
        }
        workcellProdLineMap.put(mtModWorkcell.getWorkcellCode(), mtModOrganizationItemVOS.get(0).getOrganizationId());
        //工单产线与工位产线校验
        String workcellProdLinne = workcellProdLineMap.get(importVoList.get(0).getWorkcell());
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectOne(new MtWorkOrder() {{
            setTenantId(tenantId);
            setWorkOrderNum(importVoList.get(0).getWorkNum());
        }});
        if (!workcellProdLinne.equals(mtWorkOrder.getProductionLineId())) {
            throw new MtException("HME_CHIP_DATA_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0013", "HME", importVoList.get(0).getWorkNum(), importVoList.get(0).getWorkcell()));
        }
        //来料条码数量校验
        Map<String, Long> sourceBarcodeMap = importVoList.stream().collect(Collectors.groupingBy(HmeChipImportData::getSourceBarcode,
                Collectors.summingLong(item -> item.getQty())));
        for (Map.Entry<String, Long> entry : sourceBarcodeMap.entrySet()) {
            Long value = entry.getValue();
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(entry.getKey());
            }});
            if (BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).compareTo(BigDecimal.valueOf(value)) == -1) {
                throw new MtException("HME_CHIP_DATA_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0014", "HME", entry.getKey()));
            }
        }
        //累计投料数量不可超过总需求数量
        String routerOperationId = hmeChipImportDataMapper.getRouterOperationId(tenantId, mtWorkOrder.getRouterId(), operationId);
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(importVoList.get(0).getSourceBarcode());
        }});
        String bomComponentId = hmeChipImportDataMapper.getBomComponentId(tenantId, mtWorkOrder.getBomId(), mtMaterialLot.getMaterialId(), routerOperationId);
        if(StringUtils.isBlank(bomComponentId)){
            throw new MtException("HME_CHIP_DATA_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0019", "HME"));
        }
        BigDecimal lineAttribute5 = BigDecimal.ZERO;
        MtExtendVO mtExtendVO = new MtExtendVO();
        mtExtendVO.setKeyId(bomComponentId);
        mtExtendVO.setTableName("mt_bom_component_attr");
        mtExtendVO.setAttrName("lineAttribute5");
        List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
            lineAttribute5 = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
        }
        BigDecimal assembleQty = hmeChipImportDataMapper.getAssembleQtySum(tenantId, mtWorkOrder.getWorkOrderId(), bomComponentId);
        BigDecimal qtySum = importVoList.stream().collect(CollectorsUtil.summingBigDecimal(t -> BigDecimal.valueOf(t.getQty())));
        if(lineAttribute5.compareTo(assembleQty.add(qtySum)) == -1){
            throw new MtException("HME_CHIP_DATA_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0020", "HME"));
        }
        return operationId;
    }

    /**
     * 导入数据具体业务逻辑
     *
     * @param tenantId
     * @param importVoList
     * @return void
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/26 10:00:20
     */
    void importData(Long tenantId, List<HmeChipImportData> importVoList, String operationId) {
        //生成事件请求、事件
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "COS_IO_SPLIT");
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventRequestId(eventRequestId);
            setEventTypeCode("COS_INCOMING");
        }});
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectOne(new MtWorkOrder() {{
            setTenantId(tenantId);
            setWorkOrderNum(importVoList.get(0).getWorkNum());
        }});
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectOne(new MtModWorkcell() {{
            setTenantId(tenantId);
            setWorkcellCode(importVoList.get(0).getWorkcell());
        }});
        String routerStepId = hmeChipImportDataMapper.getRouterStepId(tenantId, mtWorkOrder.getRouterId(), operationId);
        String routerOperationId = hmeChipImportDataMapper.getRouterOperationId(tenantId, mtWorkOrder.getRouterId(), operationId);
        MtMaterialLot mtMaterialLot2 = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(importVoList.get(0).getSourceBarcode());
        }});
        String bomComponentId = hmeChipImportDataMapper.getBomComponentId(tenantId, mtWorkOrder.getBomId(), mtMaterialLot2.getMaterialId(), routerOperationId);
        MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
        MtExtendVO mtExtendVO = new MtExtendVO();
        mtExtendVO.setKeyId(mtBomComponent.getBomComponentId());
        mtExtendVO.setTableName("mt_bom_component_attr");
        mtExtendVO.setAttrName("lineAttribute10");
        List<MtExtendAttrVO> lineAttribute10Attr = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
        Map<String, Long> sourceBarcodeMap = importVoList.stream().collect(Collectors.groupingBy(HmeChipImportData::getSourceBarcode,
                Collectors.summingLong(item -> item.getQty())));

        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();

        for (Map.Entry<String, Long> entry : sourceBarcodeMap.entrySet()) {
            //更新来料条码
            MtMaterialLot mtMaterialLot = new MtMaterialLot();
            mtMaterialLot.setTenantId(tenantId);
            mtMaterialLot.setMaterialLotCode(entry.getKey());
            MtMaterialLot mtMaterialLotDb = mtMaterialLotRepository.selectOne(mtMaterialLot);

            //2021-04-22 11:22 edit by chaonan.hu for kang.wang 物料批更新改为批量
            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(mtMaterialLotDb.getMaterialLotId());
            mtMaterialLotVO20.setPrimaryUomQty(mtMaterialLotDb.getPrimaryUomQty() - entry.getValue());
            if (mtMaterialLotDb.getPrimaryUomQty() - entry.getValue() == 0) {
                mtMaterialLotVO20.setEnableFlag(HmeConstants.ConstantValue.NO);
            }
            mtMaterialLotVO20List.add(mtMaterialLotVO20);

//            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
//            mtMaterialLotVO2.setEventId(eventId);
//            mtMaterialLotVO2.setMaterialLotId(mtMaterialLotDb.getMaterialLotId());
//            mtMaterialLotVO2.setPrimaryUomQty(mtMaterialLotDb.getPrimaryUomQty() - entry.getValue());
//            if (mtMaterialLotDb.getPrimaryUomQty() - entry.getValue() == 0) {
//                mtMaterialLotVO2.setEnableFlag(HmeConstants.ConstantValue.NO);
//            }
//            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
            //记录一笔计划内投料事务
            WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO = new WmsObjectTransactionRequestVO();
            wmsObjectTransactionRequestVO.setTransactionTypeCode("HME_WO_ISSUE");
            wmsObjectTransactionRequestVO.setEventId(eventId);
            wmsObjectTransactionRequestVO.setMaterialLotId(mtMaterialLotDb.getMaterialLotId());
            wmsObjectTransactionRequestVO.setMaterialId(mtMaterialLotDb.getMaterialId());
            wmsObjectTransactionRequestVO.setTransactionQty(BigDecimal.valueOf(entry.getValue().longValue()));
            wmsObjectTransactionRequestVO.setLotNumber(mtMaterialLotDb.getLot());
            MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLotDb.getPrimaryUomId());
            wmsObjectTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
            wmsObjectTransactionRequestVO.setTransactionTime(new Date());
            wmsObjectTransactionRequestVO.setPlantId(mtMaterialLotDb.getSiteId());
            wmsObjectTransactionRequestVO.setLocatorId(mtMaterialLotDb.getLocatorId());
            List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, mtMaterialLotDb.getLocatorId(), "TOP");
            if (CollectionUtils.isNotEmpty(pLocatorIds)) {
                MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
                if (ploc != null) {
                    wmsObjectTransactionRequestVO.setWarehouseId(ploc.getLocatorId());
                }
            }
            wmsObjectTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
            MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(mtWorkOrder.getProductionLineId());
            wmsObjectTransactionRequestVO.setProdLineCode(mtModProductionLine.getProdLineCode());
            wmsObjectTransactionRequestVO.setMoveType("261");
            if(CollectionUtils.isNotEmpty(lineAttribute10Attr) && StringUtils.isNotBlank(lineAttribute10Attr.get(0).getAttrValue())){
                wmsObjectTransactionRequestVO.setBomReserveNum(lineAttribute10Attr.get(0).getAttrValue());
            }
            wmsObjectTransactionRequestVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
            wmsObjectTransactionRequestVO.setRemark("COS来料录入");
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(wmsObjectTransactionRequestVO));
            //扣减组件线边库存
            MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
            mtInvOnhandQuantityVO9.setEventId(eventId);
            mtInvOnhandQuantityVO9.setSiteId(mtMaterialLotDb.getSiteId());
            mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLotDb.getMaterialId());
            mtInvOnhandQuantityVO9.setLocatorId(mtMaterialLotDb.getLocatorId());
            mtInvOnhandQuantityVO9.setLotCode(mtMaterialLotDb.getLot());
            mtInvOnhandQuantityVO9.setChangeQuantity(entry.getValue().doubleValue());
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
        }

        //批量更新物料批
        if(CollectionUtils.isNotEmpty(mtMaterialLotVO20List)){
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, HmeConstants.ConstantValue.NO);
        }
        //按照来料条码+COS类型+WAFER+Avg(nm)+TYPE+LOTNO+备注+录入批次维度分组生成来料记录
        Map<String, List<HmeChipImportData>> map = importVoList.stream().collect(Collectors.groupingBy(t -> {
            return t.getSourceBarcode() + "," + t.getCosType() + "," + t.getWafer() + ","
                    + (StringUtils.isEmpty(t.getAvgWavelenght()) ? "" : t.getAvgWavelenght()) + ","
                    + (StringUtils.isEmpty(t.getType()) ? "" : t.getType()) + ","
                    + (StringUtils.isEmpty(t.getLotno()) ? "" : t.getLotno()) + ","
                    + (StringUtils.isEmpty(t.getRemark()) ? "" : t.getRemark()) + ","
                    + (StringUtils.isEmpty(t.getImportLot()) ? "" : t.getPrintFlag());
        }));
        //记录下盒号与来料记录ID之间的关系
        Map<String, String> foxNumOperationRecordMap = new HashMap<>();
        for (Map.Entry<String, List<HmeChipImportData>> entry:map.entrySet()) {
            HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
            hmeCosOperationRecord.setTenantId(tenantId);
            hmeCosOperationRecord.setSiteId(mtWorkOrder.getSiteId());
            hmeCosOperationRecord.setWorkOrderId(mtWorkOrder.getWorkOrderId());
            hmeCosOperationRecord.setCosType(entry.getValue().get(0).getCosType());
            if(StringUtils.isNotBlank(entry.getValue().get(0).getAvgWavelenght())){
                hmeCosOperationRecord.setAverageWavelength(new BigDecimal(entry.getValue().get(0).getAvgWavelenght()));
            }
            hmeCosOperationRecord.setType(entry.getValue().get(0).getType());
            hmeCosOperationRecord.setLotNo(entry.getValue().get(0).getLotno());
            hmeCosOperationRecord.setWafer(entry.getValue().get(0).getWafer());
            hmeCosOperationRecord.setRemark(entry.getValue().get(0).getRemark());
            hmeCosOperationRecord.setJobBatch(entry.getValue().get(0).getImportLot());
            BigDecimal qtySum = entry.getValue().stream().collect(CollectorsUtil.summingBigDecimal(t -> BigDecimal.valueOf(t.getQty())));
            hmeCosOperationRecord.setCosNum(qtySum.longValue());
            hmeCosOperationRecord.setOperationId(operationId);
            hmeCosOperationRecord.setWorkcellId(mtModWorkcell.getWorkcellId());
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(entry.getValue().get(0).getSourceBarcode());
            }});
            hmeCosOperationRecord.setMaterialId(mtMaterialLot.getMaterialId());
            hmeCosOperationRecordRepository.insertSelective(hmeCosOperationRecord);
            // 保存历史记录
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);

            List<String> foxNumList = entry.getValue().stream().map(HmeChipImportData::getFoxNum).distinct().collect(Collectors.toList());
            for (String foxNum:foxNumList) {
                foxNumOperationRecordMap.put(foxNum, hmeCosOperationRecord.getOperationRecordId());
            }
        }

        Date now = new Date();
        String nowStr = DateUtil.date2String(now, "yyyy-MM-dd HH:mm:ss");
        String year = nowStr.substring(2, 4);
        String month = nowStr.substring(5, 7);
        String day = nowStr.substring(8, 10);
        String hour = nowStr.substring(11, 13);
        String minute = nowStr.substring(14, 16);
        String second = nowStr.substring(17);
        String dateStr = year + month + day + hour + minute;
        Map<String, List<HmeChipImportData>> foxNumMap = importVoList.stream().collect(Collectors.groupingBy(HmeChipImportData::getFoxNum));
        Map<String, String> foxNumTargetMaterialLotMap = new HashMap<>();
        List<String> containerTypeList = importVoList.stream().map(HmeChipImportData::getContainerType).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, String> mtContainerTypeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(containerTypeList)) {
            List<MtContainerType> mtContainerTypeList = mtContainerTypeRepository.selectByCondition(Condition.builder(MtContainerType.class).select(MtContainerType.FIELD_CONTAINER_TYPE_ID, MtContainerType.FIELD_CONTAINER_TYPE_CODE).andWhere(Sqls.custom()
                    .andEqualTo(MtContainerType.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtContainerType.FIELD_ENABLE_FLAG, HmeConstants.ConstantValue.YES)
                    .andIn(MtContainerType.FIELD_CONTAINER_TYPE_CODE, containerTypeList)).build());
            mtContainerTypeMap = mtContainerTypeList.stream().collect(Collectors.toMap(MtContainerType::getContainerTypeCode, MtContainerType::getContainerTypeId, (n1, n2) -> n1));
        }
        List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList = new ArrayList<>();
        for (Map.Entry<String, List<HmeChipImportData>> entry : foxNumMap.entrySet()) {
            String containerTypeId = mtContainerTypeMap.getOrDefault(entry.getValue().get(0).getContainerType(), "");
            // 根据容器类型和工艺及COS类型获取容器容量信息
            List<HmeContainerCapacity> containerCapacityList = hmeContainerCapacityRepository.selectByCondition(Condition.builder(HmeContainerCapacity.class).andWhere(Sqls.custom()
                    .andEqualTo(HmeContainerCapacity.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(HmeContainerCapacity.FIELD_OPERATION_ID, operationId)
                    .andEqualTo(HmeContainerCapacity.FIELD_CONTAINER_TYPE_ID, containerTypeId)
                    .andEqualTo(HmeContainerCapacity.FIELD_COS_TYPE, entry.getValue().get(0).getCosType())
                    .andEqualTo(HmeContainerCapacity.FIELD_ENABLE_FLAG, YES)).build());
            if (CollectionUtils.isEmpty(containerCapacityList)) {
                MtOperation mtOperation = mtOperationRepository.selectByPrimaryKey(operationId);
                throw new MtException("HME_COS_RETEST_IMPORT_025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_025", entry.getValue().get(0).getContainerType(), entry.getValue().get(0).getCosType(), mtOperation != null ? mtOperation.getOperationName() : ""));
            }
            HmeContainerCapacity hmeContainerCapacity = containerCapacityList.get(0);
            // 校验 位置不能超出容量
            Optional<HmeChipImportData> firstOpt = entry.getValue().stream().filter(vo -> {
                String loadRow = vo.getPosition().subSequence(0, 1).toString();
                String loadColumn = vo.getPosition().subSequence(1, vo.getPosition().length()).toString();
                return Long.valueOf(loadRow.charAt(0) - 64).compareTo(hmeContainerCapacity.getLineNum()) > 0 || Long.valueOf(loadColumn).compareTo(hmeContainerCapacity.getColumnNum()) > 0;
            }).findFirst();
            if (firstOpt.isPresent()) {
                throw new MtException("HME_COS_RETEST_IMPORT_026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_026", entry.getValue().get(0).getSourceBarcode(), entry.getValue().get(0).getPosition()));
            }
            //根据每一个盒子生成拆分条码
            String sourceBarCode = entry.getValue().get(0).getSourceBarcode();
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(sourceBarCode);
            }});
            //2021-10-28 edit by chaonan.hu for yiming.zhang 来料条码的实验代码和备注要继承到新条码上
            List<String> materialLotIdList = new ArrayList<>();
            materialLotIdList.add(mtMaterialLot.getMaterialLotId());
            List<HmeCosPatchPdaVO9> labCodeAndRemarkAttrList = hmeCosPatchPdaMapper.labCodeAndRemarkAttrQuery(tenantId, materialLotIdList);
            String labCode = labCodeAndRemarkAttrList.get(0).getLabCode();
            String remark = labCodeAndRemarkAttrList.get(0).getRemark();
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotVO2.setSiteId(mtMaterialLot.getSiteId());
            mtMaterialLotVO2.setEnableFlag(HmeConstants.ConstantValue.YES);
            mtMaterialLotVO2.setQualityStatus(mtMaterialLot.getQualityStatus());
            mtMaterialLotVO2.setMaterialId(mtMaterialLot.getMaterialId());
            mtMaterialLotVO2.setPrimaryUomId(mtMaterialLot.getPrimaryUomId());
            BigDecimal qtySum = entry.getValue().stream().collect(CollectorsUtil.summingBigDecimal(t -> BigDecimal.valueOf(t.getQty())));
            mtMaterialLotVO2.setPrimaryUomQty(qtySum.doubleValue());
            mtMaterialLotVO2.setLocatorId(mtMaterialLot.getLocatorId());
            mtMaterialLotVO2.setLot(mtMaterialLot.getLot());
            mtMaterialLotVO2.setSupplierId(mtMaterialLot.getSupplierId());
            mtMaterialLotVO2.setInLocatorTime(mtMaterialLot.getInLocatorTime());
            mtMaterialLotVO2.setLoadTime(mtMaterialLot.getLoadTime());
            mtMaterialLotVO2.setUnloadTime(mtMaterialLot.getUnloadTime());
            mtMaterialLotVO2.setCreateReason("INITIALIZE");
            MtMaterialLotVO13 mtMaterialLotVO13 = mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
            MtMaterialLot targetMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtMaterialLotVO13.getMaterialLotId());
            foxNumTargetMaterialLotMap.put(entry.getKey(), targetMaterialLot.getMaterialLotCode());
            //更新/新增条码扩展字段
            MtMaterialLotAttrVO3 mtMaterialLotAttrVO3 = new MtMaterialLotAttrVO3();
            mtMaterialLotAttrVO3.setMaterialLotId(mtMaterialLotVO13.getMaterialLotId());
            mtMaterialLotAttrVO3.setEventId(eventId);
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 workOrderAttr = new MtExtendVO5();
            workOrderAttr.setAttrName("WORK_ORDER_ID");
            workOrderAttr.setAttrValue(mtWorkOrder.getWorkOrderId());
            attrList.add(workOrderAttr);
            MtExtendVO5 workingLotAttr = new MtExtendVO5();
            workingLotAttr.setAttrName("WORKING_LOT");
            workingLotAttr.setAttrValue(entry.getValue().get(0).getImportLot());
            attrList.add(workingLotAttr);
            MtExtendVO5 waferNumAttr = new MtExtendVO5();
            waferNumAttr.setAttrName("WAFER_NUM");
            waferNumAttr.setAttrValue(entry.getValue().get(0).getWafer());
            attrList.add(waferNumAttr);
            MtExtendVO5 typeAttr = new MtExtendVO5();
            typeAttr.setAttrName("TYPE");
            typeAttr.setAttrValue(entry.getValue().get(0).getType());
            attrList.add(typeAttr);
            MtExtendVO5 statusAttr = new MtExtendVO5();
            statusAttr.setAttrName("STATUS");
            statusAttr.setAttrValue("INSTOCK");
            attrList.add(statusAttr);
            MtExtendVO5 remarkAttr = new MtExtendVO5();
            remarkAttr.setAttrName("REMARK");
            remarkAttr.setAttrValue(entry.getValue().get(0).getRemark());
            attrList.add(remarkAttr);
            MtExtendVO5 productDateAttr = new MtExtendVO5();
            productDateAttr.setAttrName("PRODUCT_DATE");
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            mtMaterialLotAttrVO2.setAttrName("PRODUCT_DATE");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if(CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotBlank(mtExtendAttrVOS.get(0).getAttrValue())){
                productDateAttr.setAttrValue(mtExtendAttrVOS.get(0).getAttrValue());
            }else{
                productDateAttr.setAttrValue(null);
            }
            attrList.add(productDateAttr);
            MtExtendVO5 originalIdAttr = new MtExtendVO5();
            originalIdAttr.setAttrName("ORIGINAL_ID");
            originalIdAttr.setAttrValue(mtMaterialLot.getMaterialLotId());
            attrList.add(originalIdAttr);
            MtExtendVO5 mfFlagAttr = new MtExtendVO5();
            mfFlagAttr.setAttrName("MF_FLAG");
            mfFlagAttr.setAttrValue("Y");
            attrList.add(mfFlagAttr);
            MtExtendVO5 lotnoAttr = new MtExtendVO5();
            lotnoAttr.setAttrName("LOTNO");
            lotnoAttr.setAttrValue(entry.getValue().get(0).getLotno());
            attrList.add(lotnoAttr);
            MtExtendVO5 routerStepAttr = new MtExtendVO5();
            routerStepAttr.setAttrName("CURRENT_ROUTER_STEP");
            routerStepAttr.setAttrValue(routerStepId);
            attrList.add(routerStepAttr);
            MtExtendVO5 cosTypeAttr = new MtExtendVO5();
            cosTypeAttr.setAttrName("COS_TYPE");
            cosTypeAttr.setAttrValue(entry.getValue().get(0).getCosType());
            attrList.add(cosTypeAttr);
            MtExtendVO5 cosRecordAttr = new MtExtendVO5();
            cosRecordAttr.setAttrName("COS_RECORD");
            String operationRecordId = foxNumOperationRecordMap.get(entry.getKey());
            cosRecordAttr.setAttrValue(operationRecordId);
            attrList.add(cosRecordAttr);
            MtExtendVO5 avgAttr = new MtExtendVO5();
            avgAttr.setAttrName("AVG_WAVE_LENGTH");
            avgAttr.setAttrValue(entry.getValue().get(0).getAvgWavelenght());
            // 20210720 add by sanfeng.zhang for peng.zhao 增加行列及容器类型信息
            MtExtendVO5 rowAttr = new MtExtendVO5();
            rowAttr.setAttrName("LOCATION_ROW");
            rowAttr.setAttrValue(String.valueOf(hmeContainerCapacity.getLineNum()));
            attrList.add(rowAttr);
            MtExtendVO5 columnAttr = new MtExtendVO5();
            columnAttr.setAttrName("LOCATION_COLUMN");
            columnAttr.setAttrValue(String.valueOf(hmeContainerCapacity.getLineNum()));
            attrList.add(columnAttr);
            MtExtendVO5 containerTypeAttr = new MtExtendVO5();
            containerTypeAttr.setAttrName("CONTAINER_TYPE");
            containerTypeAttr.setAttrValue(entry.getValue().get(0).getContainerType());
            attrList.add(containerTypeAttr);
            MtExtendVO5 chipNumAttr = new MtExtendVO5();
            chipNumAttr.setAttrName("CHIP_NUM");
            chipNumAttr.setAttrValue(String.valueOf(hmeContainerCapacity.getCapacity()));
            attrList.add(chipNumAttr);
            MtExtendVO5 labCodeAttr = new MtExtendVO5();
            labCodeAttr.setAttrName("LAB_CODE");
            labCodeAttr.setAttrValue(labCode);
            attrList.add(labCodeAttr);
            MtExtendVO5 labRemarkAttr = new MtExtendVO5();
            labRemarkAttr.setAttrName("LAB_REMARK");
            labRemarkAttr.setAttrValue(remark);
            attrList.add(labRemarkAttr);
            mtMaterialLotAttrVO3.setAttr(attrList);
            mtMaterialLotRepository.materialLotLimitAttrUpdate(tenantId, mtMaterialLotAttrVO3);
            //基于生成的条码生成装载信息
            for (HmeChipImportData hmeChipImportData:entry.getValue()) {
                HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                hmeMaterialLotLoad.setTenantId(tenantId);
                hmeMaterialLotLoad.setMaterialLotId(mtMaterialLotVO13.getMaterialLotId());
                String loadRow = hmeChipImportData.getPosition().subSequence(0, 1).toString();
                String loadColumn = hmeChipImportData.getPosition().subSequence(1, hmeChipImportData.getPosition().length()).toString();
                hmeMaterialLotLoad.setLoadRow(new Long(loadRow.charAt(0) - 64));
                hmeMaterialLotLoad.setLoadColumn(new Long(loadColumn));
                String[] split = mtMaterialLotVO13.getMaterialLotId().split("\\.");
                hmeMaterialLotLoad.setLoadSequence(split[0] + hmeMaterialLotLoad.getLoadRow()
                        + hmeMaterialLotLoad.getLoadColumn() + dateStr);
                hmeMaterialLotLoad.setCosNum(hmeChipImportData.getQty());
                hmeMaterialLotLoad.setAttribute1(hmeChipImportData.getCosType());
                hmeMaterialLotLoad.setAttribute2(hmeChipImportData.getWafer());
                hmeMaterialLotLoad.setAttribute3(mtWorkOrder.getWorkOrderId());
                if(StringUtils.isNotBlank(labCode)){
                    hmeMaterialLotLoad.setAttribute19(labCode);
                    //当实验代码不为空时，记录实验代码到表hme_material_lot_lab_code中
                    HmeMaterialLotLabCode hmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                    hmeMaterialLotLabCode.setTenantId(tenantId);
                    hmeMaterialLotLabCode.setMaterialLotId(mtMaterialLotVO13.getMaterialLotId());
                    hmeMaterialLotLabCode.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
                    hmeMaterialLotLabCode.setLabCode(labCode);
                    hmeMaterialLotLabCode.setWorkcellId(mtModWorkcell.getWorkcellId());
                    hmeMaterialLotLabCode.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                    hmeMaterialLotLabCode.setSourceObject("MA");
                    hmeMaterialLotLabCode.setRouterStepId(routerStepId);
                    hmeMaterialLotLabCode.setSourceMaterialLotId(mtMaterialLot.getMaterialLotId());
                    hmeMaterialLotLabCode.setSourceMaterialId(mtMaterialLot.getMaterialId());
                    hmeMaterialLotLabCode.setEnableFlag(YES);
                    hmeMaterialLotLabCodeList.add(hmeMaterialLotLabCode);
                }
                if(StringUtils.isNotBlank(remark)){
                    hmeMaterialLotLoad.setAttribute20(remark);
                }
                hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
            }
        }
        //调用API{woComponentAssemble}更新/新增工单组件装配实绩
        MtWoComponentActualVO1 mtWoComponentActualVO1 = new MtWoComponentActualVO1();
        mtWoComponentActualVO1.setAssembleExcessFlag("N");
        mtWoComponentActualVO1.setBomComponentId(bomComponentId);
        mtWoComponentActualVO1.setEventRequestId(eventRequestId);
        mtWoComponentActualVO1.setMaterialId(mtMaterialLot2.getMaterialId());
        mtWoComponentActualVO1.setOperationId(operationId);
        mtWoComponentActualVO1.setParentEventId(eventId);
        mtWoComponentActualVO1.setRouterStepId(routerStepId);
        BigDecimal qtySum = importVoList.stream().collect(CollectorsUtil.summingBigDecimal(t -> BigDecimal.valueOf(t.getQty())));
        mtWoComponentActualVO1.setTrxAssembleQty(qtySum.doubleValue());
        mtWoComponentActualVO1.setWorkcellId(mtModWorkcell.getWorkcellId());
        mtWoComponentActualVO1.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        mtWorkOrderComponentActualRepository.woComponentAssemble(tenantId, mtWoComponentActualVO1);
        //导入数据插入到临时表中
        for (HmeChipImportData importVO : importVoList) {
            importVO.setTenantId(tenantId);
            importVO.setPrintFlag(HmeConstants.ConstantValue.NO);
            importVO.setImportLot(dateStr + second);
            String targetMaterialLot = foxNumTargetMaterialLotMap.get(importVO.getFoxNum());
            importVO.setTargetBarcode(targetMaterialLot);
            hmeChipImportDataRepository.insertSelective(importVO);
        }
        //批量记录实验代码到表hme_material_lot_lab_code中
        if(CollectionUtils.isNotEmpty(hmeMaterialLotLabCodeList)){
            Date nowDate = new Date();
            // 获取当前用户
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            List<String> ids = mtCustomDbRepository.getNextKeys("hme_material_lot_lab_code_s", hmeMaterialLotLabCodeList.size());
            List<String> cids = mtCustomDbRepository.getNextKeys("hme_material_lot_lab_code_cid_s", hmeMaterialLotLabCodeList.size());
            int i = 0;
            List<String> sqlList = new ArrayList<>();
            for (HmeMaterialLotLabCode hmeMaterialLotLabCode:hmeMaterialLotLabCodeList) {
                hmeMaterialLotLabCode.setLabCodeId(ids.get(i));
                hmeMaterialLotLabCode.setCid(Long.valueOf(cids.get(i)));
                hmeMaterialLotLabCode.setObjectVersionNumber(1L);
                hmeMaterialLotLabCode.setCreationDate(nowDate);
                hmeMaterialLotLabCode.setCreatedBy(userId);
                hmeMaterialLotLabCode.setLastUpdateDate(nowDate);
                hmeMaterialLotLabCode.setLastUpdatedBy(userId);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeMaterialLotLabCode));
                i++;
            }
            if (CollectionUtils.isNotEmpty(sqlList)) {
                jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
        }
    }

}
