package com.ruike.wms.domain.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.domain.vo.HmeStockInDetailsVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.api.dto.WmsBarcodeDTO;
import com.ruike.wms.api.dto.WmsProductPrepareDocQueryDTO;
import com.ruike.wms.api.dto.WmsProductionRequisitionMaterialExecutionDetailDTO;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.app.service.WmsEventService;
import com.ruike.wms.app.service.WmsMaterialLotService;
import com.ruike.wms.domain.repository.*;
import com.ruike.wms.domain.service.WmsProductPrepareService;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsInstructionSnRelMapper;
import com.ruike.wms.infra.mapper.WmsSoDeliveryDetailMapper;
import com.ruike.wms.infra.util.StringCommonUtils;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.exception.CommonException;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.MtIdHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ruike.hme.infra.constant.HmeConstants.StatusCode.CANRELEASE;
import static com.ruike.wms.infra.constant.WmsConstant.CONTAINER;
import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.*;
import static com.ruike.wms.infra.constant.WmsConstant.EventType.*;
import static com.ruike.wms.infra.constant.WmsConstant.InspectionDocType.SO_DELIVERY;
import static com.ruike.wms.infra.constant.WmsConstant.InstructionStatus.*;
import static com.ruike.wms.infra.constant.WmsConstant.InstructionType.SHIP_TO_CUSTOMER;
import static com.ruike.wms.infra.constant.WmsConstant.MATERIAL_LOT;
import static com.ruike.wms.infra.constant.WmsConstant.MaterialLotStatus.SCANNED;
import static com.ruike.wms.infra.constant.WmsConstant.MaterialLotStatus.TO_SHIP;
import static com.ruike.wms.infra.constant.WmsConstant.TransactionReasonCode.PRODUCT_PREPARE;
import static com.ruike.wms.infra.constant.WmsConstant.TransactionTypeCode.WMS_LOCATOR_TRAN;
import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 成品备料 服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/12 16:20
 */
@Service
public class WmsProductPrepareServiceImpl implements WmsProductPrepareService, AopProxy<WmsProductPrepareServiceImpl> {
    private final WmsSoDeliveryDocRepository wmsSoDeliveryDocRepository;
    private final WmsSoDeliveryLineRepository wmsSoDeliveryLineRepository;
    private final WmsContainerRepository wmsContainerRepository;
    private final WmsMaterialLotRepository wmsMaterialLotRepository;
    private final MtInstructionActualRepository instructionActualRepository;
    private final WmsEventService wmsEventService;
    private final WmsMaterialLotService wmsMaterialLotService;
    private final MtInstructionActualDetailRepository instructionActualDetailRepository;
    private final WmsInstructionRepository wmsInstructionRepository;
    private final MtInstructionRepository mtInstructionRepository;
    private final WmsSoDeliveryDetailRepository wmsSoDeliveryDetailRepository;
    private final MtContainerRepository mtContainerRepository;
    private final MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    private final MtMaterialLotRepository materialLotRepository;
    private final MtInstructionDocRepository mtInstructionDocRepository;
    private final WmsObjectTransactionRepository wmsObjectTransactionRepository;
    private final WmsTransactionTypeRepository wmsTransactionTypeRepository;
    private final MtModLocatorRepository mtModLocatorRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final WmsSoDeliveryDetailMapper wmsSoDeliveryDetailMapper;
    private final WmsInstructionSnRelMapper wmsInstructionSnRelMapper;
    private final LovAdapter lovAdapter;

    public WmsProductPrepareServiceImpl(WmsSoDeliveryDocRepository wmsSoDeliveryDocRepository, WmsSoDeliveryLineRepository wmsSoDeliveryLineRepository, WmsContainerRepository wmsContainerRepository, MtContainerLoadDetailRepository mtContainerLoadDetailRepository, MtMaterialLotRepository materialLotRepository, WmsMaterialLotRepository wmsMaterialLotRepository, MtInstructionActualRepository instructionActualRepository, WmsEventService wmsEventService, WmsMaterialLotService wmsMaterialLotService, MtInstructionActualDetailRepository instructionActualDetailRepository, WmsInstructionRepository wmsInstructionRepository, MtInstructionRepository mtInstructionRepository, WmsSoDeliveryDetailRepository wmsSoDeliveryDetailRepository, MtContainerRepository mtContainerRepository, MtInstructionDocRepository mtInstructionDocRepository, WmsObjectTransactionRepository wmsObjectTransactionRepository, WmsTransactionTypeRepository wmsTransactionTypeRepository, MtModLocatorRepository mtModLocatorRepository, MtErrorMessageRepository mtErrorMessageRepository, WmsSoDeliveryDetailMapper wmsSoDeliveryDetailMapper, WmsInstructionSnRelMapper wmsInstructionSnRelMapper, LovAdapter lovAdapter) {
        this.wmsSoDeliveryDocRepository = wmsSoDeliveryDocRepository;
        this.wmsSoDeliveryLineRepository = wmsSoDeliveryLineRepository;
        this.wmsContainerRepository = wmsContainerRepository;
        this.mtContainerLoadDetailRepository = mtContainerLoadDetailRepository;
        this.materialLotRepository = materialLotRepository;
        this.wmsMaterialLotRepository = wmsMaterialLotRepository;
        this.instructionActualRepository = instructionActualRepository;
        this.wmsEventService = wmsEventService;
        this.wmsMaterialLotService = wmsMaterialLotService;
        this.instructionActualDetailRepository = instructionActualDetailRepository;
        this.wmsInstructionRepository = wmsInstructionRepository;
        this.mtInstructionRepository = mtInstructionRepository;
        this.wmsSoDeliveryDetailRepository = wmsSoDeliveryDetailRepository;
        this.mtContainerRepository = mtContainerRepository;
        this.mtInstructionDocRepository = mtInstructionDocRepository;
        this.wmsObjectTransactionRepository = wmsObjectTransactionRepository;
        this.wmsTransactionTypeRepository = wmsTransactionTypeRepository;
        this.mtModLocatorRepository = mtModLocatorRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.wmsSoDeliveryDetailMapper = wmsSoDeliveryDetailMapper;
        this.wmsInstructionSnRelMapper = wmsInstructionSnRelMapper;
        this.lovAdapter = lovAdapter;
    }

    @Override
    @ProcessLovValue
    public List<WmsProductPrepareDocVO> deliveryDocLovGet(Long tenantId, WmsProductPrepareDocQueryDTO dto) {
        return wmsSoDeliveryDocRepository.selectPrepareListByCondition(tenantId, dto);
    }

    @Override
    public WmsProductPrepareDocVO deliveryDocScan(Long tenantId, String instructionDocNum) {
        if(instructionDocNum.contains("@")){
            instructionDocNum = instructionDocNum.replaceAll("@00","");
        }
        WmsProductPrepareDocVO doc = wmsSoDeliveryDocRepository.selectPrepareDocByNum(tenantId, instructionDocNum);
        WmsCommonUtils.processValidateMessage(tenantId, Objects.isNull(doc), "WMS_MATERIAL_ON_SHELF_0001", WMS, instructionDocNum);
        WmsCommonUtils.processValidateMessage(tenantId, !StringCommonUtils.contains(doc.getInstructionDocStatus(), RELEASED, PREPARE_EXECUTE, PREPARE_COMPLETE, DELIVERY_EXECUTE), "WMS_STOCKTAKE_001", WMS, "出货单", instructionDocNum, doc.getInstructionDocStatusMeaning());
        return doc;
    }

    @Override
    @ProcessLovValue
    public List<WmsProductPrepareLineVO> prepareListGet(Long tenantId, String instructionDocId) {
        return wmsSoDeliveryLineRepository.prepareListGet(tenantId, instructionDocId);
    }

    @Override
    public WmsContainerVO containerScan(Long tenantId, String containerCode, String instructionDocId) {
        WmsContainerVO container = wmsContainerRepository.getInfoByCode(tenantId, containerCode);
        // 校验是否为空或者状态不为CAN RELEASE
        WmsCommonUtils.processValidateMessage(tenantId, Objects.isNull(container), "WMS_SO_DELIVERY_0003", WMS, containerCode);
        WmsCommonUtils.processValidateMessage(tenantId, !CANRELEASE.equals(container.getStatus()), "WMS_SO_DELIVERY_0003", WMS, containerCode);
        // 校验是否有上层容器
        WmsCommonUtils.processValidateMessage(tenantId, StringUtils.isNotBlank(container.getCurrentContainerId()), "WMS_COST_CENTER_0029", WMS, containerCode);
        // 获取容器下所有的条码，必须包含在当前单据的所有条码范围内
        MtContLoadDtlVO10 query = new MtContLoadDtlVO10();
        query.setContainerId(container.getContainerId());
        query.setAllLevelFlag(YES);
        List<MtContLoadDtlVO4> materialLotList = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, query);
        Set<String> idSet = wmsSoDeliveryDocRepository.selectMaterialLotIdByDocId(tenantId, instructionDocId);
        materialLotList.forEach(rec -> {
            if (!idSet.contains(rec.getMaterialLotId())) {
                MtMaterialLot materialLot = materialLotRepository.selectByPrimaryKey(rec.getMaterialLotId());
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_SO_DELIVERY_0004", WMS, containerCode, materialLot.getMaterialLotCode());
            }
        });
        return container;
    }

    @Override
    public WmsProdPrepareScanVO barcodeScan(Long tenantId, String barcode, String instructionDocId, String unBundingFlag) {
        if (StringUtils.isBlank(barcode)) {
            throw new MtException("WMS_COST_CENTER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0006", "WMS", barcode));
        }
        // 首先判断条码是否为容器，不为容器安装物料批判断
        WmsContainerVO container = wmsContainerRepository.getInfoByCode(tenantId, barcode);
        List<String> materialLotIdList;
        WmsProdPrepareScanVO result = new WmsProdPrepareScanVO();
        String warehouseId = "";
        if (Objects.nonNull(container)) {
            // 校验是否有上层容器
            if (StringUtils.isNotBlank(container.getCurrentContainerId())) {
                if (StringUtils.isBlank(unBundingFlag)) {
                    result.setUnBundingFlag(WmsConstant.CONSTANT_Y);
                    return result;
                } else if (StringUtils.equals(unBundingFlag, WmsConstant.CONSTANT_Y)) {
                    // 调用API{ containerUnload }进行卸载
                    MtContainerVO25 mtContainerVO22 = new MtContainerVO25();
                    mtContainerVO22.setContainerId(container.getCurrentContainerId());
                    mtContainerVO22.setLoadObjectType(HmeConstants.LoadTypeCode.CONTAINER);
                    mtContainerVO22.setLoadObjectId(container.getContainerId());
                    mtContainerRepository.containerUnload(tenantId, mtContainerVO22);
                }
            }
            //WmsCommonUtils.processValidateMessage(tenantId, StringUtils.isNotBlank(container.getCurrentContainerId()), "WMS_COST_CENTER_0029", WMS, barcode);
            // 获取容器下所有的条码
            List<WmsMaterialLotAttrVO> containerMaterialLotList = wmsContainerRepository.getMaterialLotInContainer(tenantId, container.getContainerId());
            WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(containerMaterialLotList), "WMS_DISTRIBUTION_0018", WMS, barcode);
            result.setLoadObjectType(CONTAINER);
            result.setLoadObjectId(container.getContainerId());
            warehouseId = container.getWarehouseId();
            // 开始校验物料批
            materialLotIdList = containerMaterialLotList.stream().map(WmsMaterialLotAttrVO::getMaterialLotId).collect(Collectors.toList());
        } else {
            // 判断是否为物料批，否则报错
            WmsMaterialLotAttrVO materialLot = wmsMaterialLotRepository.selectWithAttrByCode(tenantId, barcode);
            WmsCommonUtils.processValidateMessage(tenantId, Objects.isNull(materialLot), "WMS_COST_CENTER_0006", WMS, barcode);
            if (StringUtils.isNotBlank(materialLot.getCurrentContainerId())) {
                if (StringUtils.isBlank(unBundingFlag)) {
                    result.setUnBundingFlag(WmsConstant.CONSTANT_Y);
                    return result;
                } else if (StringUtils.equals(unBundingFlag, WmsConstant.CONSTANT_Y)) {
                    // 调用API{ containerUnload }进行卸载
                    MtContainerVO25 mtContainerVO22 = new MtContainerVO25();
                    mtContainerVO22.setContainerId(materialLot.getCurrentContainerId());
                    mtContainerVO22.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                    mtContainerVO22.setLoadObjectId(materialLot.getMaterialLotId());
                    mtContainerRepository.containerUnload(tenantId, mtContainerVO22);
                }
            }
            //WmsCommonUtils.processValidateMessage(tenantId, StringUtils.isNotBlank(materialLot.getCurrentContainerId()), "WMS_COST_CENTER_0030", WMS, barcode);
            result.setLoadObjectType(MATERIAL_LOT);
            result.setLoadObjectId(materialLot.getMaterialLotId());
            result.setMaterialId(materialLot.getMaterialId());
            result.setMaterialCode(materialLot.getMaterialCode());
            result.setMaterialVersion(materialLot.getMaterialVersion());
            result.setQuantity(materialLot.getPrimaryUomQty());
            result.setUomCode(materialLot.getPrimaryUomCode());
            materialLotIdList = Collections.singletonList(materialLot.getMaterialLotId());
        }
        // 获取到物料批和单据行信息，进行匹配
        List<WmsMaterialLotAttrVO> materialLotList = wmsMaterialLotRepository.selectListWithAttrByIds(tenantId, materialLotIdList);
        List<WmsProductPrepareLineVO> lineList = self().prepareListGet(tenantId, instructionDocId);
        // 验证并组装返回值
        return this.validationScan(tenantId, result, materialLotList, instructionDocId, lineList, warehouseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<WmsProductPrepareLineVO> barcodeMatchSubmit(Long tenantId, String instructionDocId, WmsProdPrepareScanVO vo, String containerId) {
        // 创建事件
        WmsEventVO event = wmsEventService.createEventOnly(tenantId, MATERIALLOT_SCAN);
        // 更新（或新增）指令实际
        List<WmsProdPrepareAssignVO> lineList = vo.getMaterialLotList().stream().flatMap(rec -> rec.getAssignList().stream()).collect(Collectors.toList());
//        Map<String,Double> instructionMap = lineList.stream()
//                .collect(Collectors.groupingBy(WmsProdPrepareAssignVO::getInstructionId, Collectors.summingInt(x -> x.get())));
        Set<String> lineIdSet = lineList.stream().map(WmsProdPrepareAssignVO::getInstructionId).collect(Collectors.toSet());
        Map<String, MtInstruction> lineMap = mtInstructionRepository.selectByCondition(Condition.builder(MtInstruction.class).andWhere(Sqls.custom().andIn(MtInstruction.FIELD_INSTRUCTION_ID, lineIdSet)).build()).stream().collect(Collectors.toMap(MtInstruction::getInstructionId, a -> a, (k1, k2) -> k1));
        double sum = vo.getMaterialLotList().stream().map(WmsProdPrepareMaterialLotVO::getQuantity).filter(Objects::nonNull).collect(toList()).stream().mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
        lineIdSet.forEach(s -> {
            Double qty = 0d;
            for(WmsProdPrepareMaterialLotVO wmsProdPrepareMaterialLotVO:vo.getMaterialLotList()){
                for(WmsProdPrepareAssignVO wmsProdPrepareAssignVO:wmsProdPrepareMaterialLotVO.getAssignList()){
                    if(s.equals(wmsProdPrepareAssignVO.getInstructionId())){
                        qty = qty + wmsProdPrepareMaterialLotVO.getQuantity().doubleValue();
                    }
                }
            }
            WmsProdPrepareAssignVO rec = lineList.stream().filter(item -> item.getInstructionId().equals(s)).collect(toList()).get(0);
            MtInstruction line = lineMap.get(rec.getInstructionId());
            MtInstructionActualVO actual = new MtInstructionActualVO();
            actual.setInstructionId(rec.getInstructionId());
            actual.setEventId(event.getEventId());
            actual.setActualQty(qty);
            //actual.setActualQty(rec.getAssignQty().doubleValue());
            actual.setMaterialId(rec.getMaterialId());
            actual.setTenantId(tenantId);
            actual.setBusinessType(SO_DELIVERY);
            actual.setInstructionType(SHIP_TO_CUSTOMER);
            actual.setUomId(rec.getUomId());
            actual.setFromLocatorId(rec.getFromLocatorId());
            actual.setFromSiteId(rec.getFromSiteId());
            actual.setCustomerId(line.getCustomerId());
            actual.setCustomerSiteId(line.getCustomerSiteId());
            instructionActualRepository.instructionActualUpdate(tenantId, actual);
        });
//        MtInstruction line = lineMap.get(lineList.get(0).getInstructionId());
//        MtInstructionActualVO actual = new MtInstructionActualVO();
//        actual.setInstructionId(lineList.get(0).getInstructionId());
//        actual.setEventId(event.getEventId());
//        actual.setActualQty(sum);
//        //actual.setActualQty(rec.getAssignQty().doubleValue());
//        actual.setMaterialId(lineList.get(0).getMaterialId());
//        actual.setTenantId(tenantId);
//        actual.setBusinessType(SO_DELIVERY);
//        actual.setInstructionType(SHIP_TO_CUSTOMER);
//        actual.setUomId(lineList.get(0).getUomId());
//        actual.setFromLocatorId(lineList.get(0).getFromLocatorId());
//        actual.setFromSiteId(lineList.get(0).getFromSiteId());
//        actual.setCustomerId(line.getCustomerId());
//        actual.setCustomerSiteId(line.getCustomerSiteId());
//        instructionActualRepository.instructionActualUpdate(tenantId, actual);

        // 查询指令行对应的明细
        List<MtInstructionActual> actualList = instructionActualRepository.selectByCondition(Condition.builder(MtInstructionActual.class).andWhere(Sqls.custom().andIn(MtInstructionActual.FIELD_INSTRUCTION_ID, lineIdSet)).build());
        Map<String, String> actualMap = actualList.stream().collect(Collectors.toMap(MtInstructionActual::getInstructionId, MtInstructionActual::getActualId, (k1, k2) -> k1));
        vo.getMaterialLotList().forEach(rec -> {
            rec.getAssignList().forEach(line -> {
                if (Objects.nonNull(line.getAssignQty()) || BigDecimal.ZERO.compareTo(line.getAssignQty()) < 0) {
                    String actualId = actualMap.get(line.getInstructionId());
                    MtInstructionActualDetail actualDetail = new MtInstructionActualDetail();
                    actualDetail.setActualId(actualId);
                    actualDetail.setMaterialLotId(rec.getMaterialLotId());
                    actualDetail.setUomId(rec.getUomId());
                    actualDetail.setActualQty(rec.getQuantity().doubleValue());
                    //actualDetail.setActualQty(line.getAssignQty().doubleValue());
                    MtModLocator mtModLocator = new MtModLocator();
                    mtModLocator.setTenantId(tenantId);
                    mtModLocator.setLocatorId(rec.getWarehouseId());
                    MtModLocator mtModLocator1 = mtModLocatorRepository.selectOne(mtModLocator);
                    actualDetail.setFromLocatorId(mtModLocator1.getParentLocatorId());
                    //actualDetail.setFromLocatorId(rec.getWarehouseId());
                    actualDetail.setContainerId(StringUtils.isNotBlank(containerId) ? containerId : CONTAINER.equals(vo.getLoadObjectType()) ? vo.getLoadObjectId() : null);
                    instructionActualDetailRepository.instructionActualDetailCreate(tenantId, actualDetail);
                }
            });
            // 更新条码状态
            wmsMaterialLotService.updateMaterialLotStatus(tenantId, event.getEventId(), rec.getMaterialLotId(), SCANNED);
        });

        // 出货单行状态更新
        lineIdSet.forEach(instructionId -> this.instructionUpdate(tenantId, event.getEventId(), instructionId));

        //单据状态更新
        List<WmsProductPrepareLineVO> wmsProductPrepareLineVOS = wmsSoDeliveryLineRepository.prepareListGet(tenantId, instructionDocId);
        List<WmsProductPrepareLineVO> wmsProductPrepareLineVOS1 = wmsProductPrepareLineVOS.stream().filter(line -> !line.getInstructionStatus().equals("CANCEL")).collect(toList());
        List<String> collect = wmsProductPrepareLineVOS1.stream().map(WmsProductPrepareLineVO::getInstructionStatus).distinct().collect(toList());
        List<WmsProductPrepareLineVO> collect1 = wmsProductPrepareLineVOS1.stream().filter(line -> StringCommonUtils.contains(line.getInstructionStatus(), RELEASED)).collect(toList());
        List<WmsProductPrepareLineVO> collect2 = wmsProductPrepareLineVOS1.stream().filter(line -> StringCommonUtils.contains(line.getInstructionStatus(), PREPARE_COMPLETE)).collect(toList());

        MtInstructionDocDTO2 mtInstructionDocDTO2 = new MtInstructionDocDTO2();
        mtInstructionDocDTO2.setInstructionDocId(instructionDocId);
        mtInstructionDocDTO2.setEventId(event.getEventId());
        if (wmsProductPrepareLineVOS1.size() == collect1.size()) {
            mtInstructionDocDTO2.setInstructionDocStatus(RELEASED);
        } else if (wmsProductPrepareLineVOS1.size() == collect2.size()) {
            mtInstructionDocDTO2.setInstructionDocStatus(PREPARE_COMPLETE);
        } else if (collect.contains(PREPARE_EXECUTE)) {
            mtInstructionDocDTO2.setInstructionDocStatus(PREPARE_EXECUTE);
        } else {
            mtInstructionDocDTO2.setInstructionDocStatus(PREPARE_EXECUTE);
        }
        mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDocDTO2, WmsConstant.KEY_IFACE_STATUS_NEW);
        // 返回查询结果
        return self().prepareListGet(tenantId, instructionDocId);
    }

    @Override
    @ProcessLovValue
    public List<WmsProductPrepareDetailVO> detailListQuery(Long tenantId, String instructionId) {
        return wmsSoDeliveryDetailRepository.selectPrepareListByLineId(tenantId, instructionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<WmsProductPrepareLineVO> barcodeCancel(Long tenantId, String instructionDocId, String instructionId, WmsProdPrepareScanVO vo) {
        // 创建事件
        WmsEventVO event = wmsEventService.createEventOnly(tenantId, MATERIALLOT_SCAN_CANCEL);
        //行的明细
        // List<WmsProductPrepareDetailVO> wmsProductPrepareDetailVOS = wmsSoDeliveryDetailRepository.selectPrepareListByLineId(tenantId, instructionId);
        if (CollectionUtils.isNotEmpty(vo.getMaterialLotIdList())) {
            for (String materialLotId : vo.getMaterialLotIdList()) {
                WmsProductionRequisitionMaterialExecutionDetailDTO materialLotCodeCheck = wmsInstructionSnRelMapper.materialLotCodeCheck(tenantId, "", materialLotId);
                //校验是否有上层容器API[objectLimitLoadingContainerQuery]
                if (materialLotCodeCheck != null && StringUtils.isNotBlank(materialLotCodeCheck.getTopContainerId())) {
                    //解绑
                    MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
                    mtContainerVO25.setContainerId(materialLotCodeCheck.getTopContainerId());
                    mtContainerVO25.setLoadObjectId(materialLotCodeCheck.getMaterialLotId());
                    mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                    mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
                }
            }
        }
        List<WmsBarcodeDTO> barCodeLlist = new ArrayList<>();
        vo.getMaterialLotIdList().stream().forEach(materialLotId -> {
            WmsBarcodeDTO wmsBarcodeDTO = new WmsBarcodeDTO();
            wmsBarcodeDTO.setLoadObjectId(materialLotId);
            wmsBarcodeDTO.setLoadObjectType(MATERIAL_LOT);
            barCodeLlist.add(wmsBarcodeDTO);
        });
        // 查询出需要取消的明细行
        List<WmsInstructionActualDetailVO> detailList = wmsSoDeliveryDetailRepository.selectListByDocAndBarcode(tenantId, instructionDocId, barCodeLlist);
        Map<String, BigDecimal> actualMap = detailList.stream().collect(Collectors.toMap(WmsInstructionActualDetailVO::getActualId, WmsInstructionActualDetailVO::getPrimaryUomQty, (k1, k2) -> k1));
        Map<String, BigDecimal> qtyMap = detailList.stream().collect(Collectors.groupingBy(WmsInstructionActualDetailVO::getActualId, CollectorsUtil.summingBigDecimal(WmsInstructionActualDetailVO::getActualQty)));
        // 更新指令实际
        actualMap.forEach((actualId, actualQty) -> {
            BigDecimal trxQty = qtyMap.get(actualId);
            if (trxQty.compareTo(actualQty) == 0) {
                // 当所有的数量全部清空后删除实际行
                instructionActualRepository.deleteByPrimaryKey(actualId);
            } else {
                MtInstructionActualVO actual = new MtInstructionActualVO();
                actual.setActualId(actualId);
                actual.setTenantId(tenantId);
                actual.setInstructionId(instructionId);
                actual.setEventId(event.getEventId());
                actual.setActualQty(-trxQty.doubleValue());
                //actual.setActualQty(trxQty.negate().doubleValue());
                instructionActualRepository.instructionActualUpdate(tenantId, actual);
            }
        });
        // 删除指令明细
        detailList.forEach(rec -> instructionActualDetailRepository.deleteByPrimaryKey(rec.getActualDetailId()));

        // 更新条码状态
        Set<String> materialLotIdSet = detailList.stream().map(WmsInstructionActualDetailVO::getMaterialLotId).collect(Collectors.toSet());
        materialLotIdSet.forEach(materialLotId -> wmsMaterialLotService.updateMaterialLotStatus(tenantId, event.getEventId(), materialLotId, INSTOCK));


        // 出货单行状态更新
        Set<String> instructionIdSet = detailList.stream().map(WmsInstructionActualDetailVO::getInstructionId).collect(Collectors.toSet());
        instructionIdSet.forEach(instructionIds -> this.instructionUpdate(tenantId, event.getEventId(), instructionId));

        //vi.	单据状态更新
        List<WmsProductPrepareLineVO> wmsProductPrepareLineVOS = wmsSoDeliveryLineRepository.prepareListGet(tenantId, instructionDocId);
        List<WmsProductPrepareLineVO> wmsProductPrepareLineVOS1 = wmsProductPrepareLineVOS.stream().filter(line -> !line.getInstructionStatus().equals("CANCEL")).collect(toList());
        List<String> collect = wmsProductPrepareLineVOS1.stream().map(WmsProductPrepareLineVO::getInstructionStatus).distinct().collect(toList());
        List<WmsProductPrepareLineVO> collect1 = wmsProductPrepareLineVOS1.stream().filter(line -> StringCommonUtils.contains(line.getInstructionStatus(), RELEASED)).collect(toList());
        List<WmsProductPrepareLineVO> collect2 = wmsProductPrepareLineVOS1.stream().filter(line -> StringCommonUtils.contains(line.getInstructionStatus(), PREPARE_COMPLETE)).collect(toList());

        MtInstructionDocDTO2 mtInstructionDocDTO2 = new MtInstructionDocDTO2();
        mtInstructionDocDTO2.setInstructionDocId(instructionDocId);
        mtInstructionDocDTO2.setEventId(event.getEventId());
        if (wmsProductPrepareLineVOS1.size() == collect1.size()) {
            mtInstructionDocDTO2.setInstructionDocStatus(RELEASED);
        } else if (wmsProductPrepareLineVOS1.size() == collect2.size()) {
            mtInstructionDocDTO2.setInstructionDocStatus(PREPARE_COMPLETE);
        } else if (collect.contains(PREPARE_EXECUTE)) {
            mtInstructionDocDTO2.setInstructionDocStatus(PREPARE_EXECUTE);
        } else {
            mtInstructionDocDTO2.setInstructionDocStatus(PREPARE_EXECUTE);
        }
        mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDocDTO2, WmsConstant.CONSTANT_N);


        // 返回查询结果
        return self().prepareListGet(tenantId, instructionDocId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public WmsProductPrepareDocVO execute(Long tenantId, String instructionDocId) {
        // 查询执行明细数据
        List<WmsProdPrepareExecVO> list = wmsSoDeliveryDetailRepository.selectExecuteListByDocId(tenantId, instructionDocId);
        // 对明细数据进行校验并获取目标货位
        this.validationBeforeExecute(tenantId, instructionDocId, list);

        // 创建事件
        WmsEventVO event = wmsEventService.createEventWithRequest(tenantId, PRODUCT_DELIVERY);

        // 执行转移，并更新条码状态和单据行状态
        this.executeTransfer(tenantId, event, list);

        // 记录事务
        this.createTransaction(tenantId, event.getEventId(), instructionDocId, list);

        // 单据状态更新
        this.instructionDocStatusUpdate(tenantId, event.getEventId(), instructionDocId);

        // 返回单据信息
        return wmsSoDeliveryDocRepository.selectPrepareListByCondition(tenantId, new WmsProductPrepareDocQueryDTO() {{
            setInstructionDocId(instructionDocId);
        }}).get(0);
    }

    private void createTransaction(Long tenantId, String eventId, String instructionDocId, List<WmsProdPrepareExecVO> list) {
        WmsTransactionTypeDTO transactionType = wmsTransactionTypeRepository.getTransactionType(tenantId, WMS_LOCATOR_TRAN);
        Date now = DateUtil.date();
        List<WmsObjectTransactionRequestVO> reqList = new ArrayList<>();
        list.forEach(line -> {
            WmsObjectTransactionRequestVO request = new WmsObjectTransactionRequestVO();
            request.setTransactionTypeCode(WMS_LOCATOR_TRAN);
            request.setEventId(eventId);
            request.setMaterialLotId(line.getMaterialLotId());
            request.setMaterialId(line.getMaterialId());
            request.setTransactionQty(line.getDetailActualQty());
            request.setLotNumber(line.getLot());
            request.setTransferLotNumber(line.getLot());
            request.setTransactionUom(line.getUomCode());
            request.setTransactionTime(now);
            request.setTransactionReasonCode(PRODUCT_PREPARE);
            request.setPlantId(line.getSiteId());
            request.setWarehouseId(line.getWarehouseId());
            request.setLocatorId(line.getLocatorId());
            request.setTransferPlantId(line.getTargetSiteId());
            request.setTransferWarehouseId(line.getTargetWarehouseId());
            request.setTransferLocatorId(line.getTargetLocatorId());
            request.setSourceDocType(SO_DELIVERY);
            request.setSourceDocId(instructionDocId);
            request.setSourceDocLineId(line.getInstructionId());
            request.setMoveType(transactionType.getMoveType());
            request.setContainerId(line.getContainerId());
            reqList.add(request);
        });
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, reqList);
    }

    /**
     * 单据状态更新
     *
     * @param tenantId         租户
     * @param eventId          事件
     * @param instructionDocId 单据ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/21 03:44:58
     */
    private void instructionDocStatusUpdate(Long tenantId, String eventId, String instructionDocId) {
        List<MtInstruction> list = mtInstructionRepository.select(new MtInstruction() {{
            setSourceDocId(instructionDocId);
        }});
        Map<String, List<MtInstruction>> statusMap = list.stream().collect(Collectors.groupingBy(MtInstruction::getInstructionStatus));
        Set<String> statusSet = statusMap.keySet();
        MtInstructionDoc doc = mtInstructionDocRepository.selectByPrimaryKey(instructionDocId);
        if (statusSet.contains(RELEASED) || statusSet.contains(PREPARE_EXECUTE)) {
            doc.setInstructionDocStatus(PREPARE_EXECUTE);
        } else if (statusSet.size() == 1 && statusSet.contains(PREPARE_COMPLETE)) {
            doc.setInstructionDocStatus(PREPARE_COMPLETE);
        } else if (statusSet.size() == 1 && statusSet.contains(SIGN_COMPLETE)) {
            doc.setInstructionDocStatus(SIGN_COMPLETE);
        } else {
            doc.setInstructionDocStatus(SIGN_EXECUTE);
        }
        MtInstructionDocDTO2 updateDoc = new MtInstructionDocDTO2();
        updateDoc.setInstructionDocId(instructionDocId);
        updateDoc.setEventId(eventId);
        updateDoc.setInstructionDocStatus(doc.getInstructionDocStatus());
        mtInstructionDocRepository.instructionDocUpdate(tenantId, updateDoc, NO);
    }

    /**
     * 执行转移
     *
     * @param tenantId 租户
     * @param event    事件
     * @param list     明细列表
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/21 03:42:33
     */
    private void executeTransfer(Long tenantId, WmsEventVO event, List<WmsProdPrepareExecVO> list) {
        // 1.找出所有 top_container_id 为空的条码，并判断是否在目标货位上，不在的执行条码转移
        List<WmsProdPrepareExecVO> materialLotTrfList = list.stream().filter(rec -> StringUtils.isBlank(rec.getTopContainerId()) && !rec.getLocatorId().equals(rec.getTargetLocatorId())).collect(Collectors.toList());
        materialLotTrfList.forEach(rec ->
                materialLotRepository.materialLotTransfer(tenantId, new MtMaterialLotVO9() {{
                    setEventRequestId(event.getEventRequestId());
                    setTargetSiteId(rec.getTargetSiteId());
                    setTargetLocatorId(rec.getTargetLocatorId());
                    setMaterialLotId(rec.getMaterialLotId());
                }}));

        // 2.找到所有的 container_id 和 top_container_id 是否在目标货位，不在的执行容器转移
        List<WmsProdPrepareExecVO> containerTrfList = list.stream().filter(rec -> !StringCommonUtils.equalsIgnoreBlank(rec.getContainerLocatorId(), rec.getTopContainerLocatorId())).collect(Collectors.toList());
        // 分别取得container和top_container对应的目标货位和目标站点
        Map<String, WmsProdPrepareExecVO> containerTrfMap = Stream.of(containerTrfList.stream().filter(rec -> StringUtils.isNotBlank(rec.getContainerId())).collect(Collectors.toMap(WmsProdPrepareExecVO::getContainerId, a -> a, (k1, k2) -> k1)), containerTrfList.stream().filter(rec -> StringUtils.isNotBlank(rec.getTopContainerId())).collect(Collectors.toMap(WmsProdPrepareExecVO::getTopContainerId, a -> a, (k1, k2) -> k1))).flatMap(x -> x.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> new WmsProdPrepareExecVO() {{
            setTargetSiteId(v1.getTargetSiteId());
            setTargetLocatorId(v1.getTargetLocatorId());
        }}));
        containerTrfMap.forEach((containerId, rec) -> {
            mtContainerRepository.containerTransfer(tenantId, new MtContainerVO7() {{
                setEventRequestId(event.getEventRequestId());
                setTargetSiteId(rec.getTargetSiteId());
                setTargetLocatorId(rec.getTargetLocatorId());
                setContainerId(containerId);
            }});
        });

        // 3.将条码装载到container_id，将 top_container_id 装载到 container_id,容器装载注意筛选 top_container_id != container_id
        Map<String, List<String>> materialLotLoadMap = list.stream().filter(rec -> StringUtils.isNotBlank(rec.getContainerId())).collect(Collectors.groupingBy(WmsProdPrepareExecVO::getContainerId, Collectors.mapping(WmsProdPrepareExecVO::getMaterialLotId, Collectors.toList())));
        Map<String, List<String>> containerLoadMap = list.stream().filter(rec -> StringUtils.isNotBlank(rec.getContainerId()) && StringUtils.isNotBlank(rec.getTopContainerId()) && !rec.getContainerId().equals(rec.getTopContainerId())).collect(Collectors.groupingBy(WmsProdPrepareExecVO::getContainerId, Collectors.mapping(WmsProdPrepareExecVO::getTopContainerId, Collectors.toList())));
        List<MtContainerVO31> containerLoadList = new ArrayList<>();
        materialLotLoadMap.forEach((containerId, materialLotList) -> materialLotList.forEach(materialLotId -> {
            MtContainerVO31 load = new MtContainerVO31();
            load.setContainerId(containerId);
            load.setLoadObjectType(MATERIAL_LOT);
            load.setLoadObjectId(materialLotId);
            containerLoadList.add(load);
        }));
        containerLoadMap.forEach((containerId, containerList) -> containerList.forEach(topContainerId -> {
            MtContainerVO31 load = new MtContainerVO31();
            load.setContainerId(containerId);
            load.setLoadObjectType(CONTAINER);
            load.setLoadObjectId(topContainerId);
            containerLoadList.add(load);
        }));
        // 若有需要装载的数据，则进行装载
        if (CollectionUtils.isNotEmpty(containerLoadList)) {
            MtContainerVO30 loadParam = new MtContainerVO30();
            loadParam.setEventRequestId(event.getEventRequestId());
            loadParam.setContainerLoadList(containerLoadList);
            mtContainerRepository.containerBatchLoad(tenantId, loadParam);
        }

        // 更新条码状态
        list.stream().map(WmsProdPrepareExecVO::getMaterialLotId).filter(StringUtils::isNotBlank).collect(Collectors.toList()).forEach(materialLotId -> wmsMaterialLotService.updateMaterialLotStatus(tenantId, event.getEventId(), materialLotId, TO_SHIP));

        // 更新行状态
        list.stream().map(WmsProdPrepareExecVO::getInstructionId).distinct().forEach(instructionId -> this.instructionUpdate(tenantId, event.getEventId(), instructionId));
    }

    /**
     * 执行前校验
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @param list             明细列表
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/21 03:43:02
     */
    private void validationBeforeExecute(Long tenantId, String instructionDocId, List<WmsProdPrepareExecVO> list) {
        WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(list), "WMS_DISTRIBUTION_0013", WMS);
        // 获取单据下所有条码ID
        Set<String> materialLotIdSet = wmsSoDeliveryDocRepository.selectMaterialLotIdByDocId(tenantId, instructionDocId);
        Set<String> scannedMaterialLotIdSet = wmsSoDeliveryDocRepository.selectMaterialLotIdByDocId(tenantId, instructionDocId, SCANNED);
        // 取容器/顶层容器下所有条码，若容器/顶层容器下有条码不在单据下则报错
        Set<String> containerIdSet = list.stream().map(WmsProdPrepareExecVO::getContainerId).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        Set<String> topContainerIdSet = list.stream().map(WmsProdPrepareExecVO::getTopContainerId).filter(StringUtils::isNotBlank).collect(Collectors.toSet());

        // 容器框扫描条码校验所有条码必须在
        containerIdSet.forEach(id -> {
            List<WmsMaterialLotAttrVO> materialLotList = wmsContainerRepository.getMaterialLotInContainer(tenantId, id);
            Optional<String> materialLotCodeExists = materialLotList.stream().filter(rec -> !materialLotIdSet.contains(rec.getMaterialLotId())).map(WmsMaterialLotAttrVO::getMaterialCode).findAny();
            materialLotCodeExists.ifPresent(materialLotCode -> WmsCommonUtils.processValidateMessage(tenantId, "WMS_DISTRIBUTION_0013", WMS, mtContainerRepository.selectByPrimaryKey(id).getContainerCode(), materialLotCode));
        });

        // 顶层容器校验所有条码必须在状态为已扫描的条码下
        topContainerIdSet.forEach(id -> {
            List<WmsMaterialLotAttrVO> materialLotList = wmsContainerRepository.getMaterialLotInContainer(tenantId, id);
            Optional<String> materialLotCodeExists = materialLotList.stream().filter(rec -> !scannedMaterialLotIdSet.contains(rec.getMaterialLotId())).map(WmsMaterialLotAttrVO::getMaterialCode).findAny();
            materialLotCodeExists.ifPresent(materialLotCode -> WmsCommonUtils.processValidateMessage(tenantId, "WMS_SO_DELIVERY_0014", WMS, mtContainerRepository.selectByPrimaryKey(id).getContainerCode(), materialLotCode));
        });

        // 验证仓库
        list.forEach(rec -> WmsCommonUtils.processValidateMessage(tenantId, !rec.getActualFromLocatorId().equals(rec.getWarehouseId()), "WMS_SO_DELIVERY_0015", WMS, rec.getMaterialLotCode()));
        Map<String, List<WmsProdPrepareExecVO>> containerMap = list.stream().collect(Collectors.groupingBy(WmsProdPrepareExecVO::getContainerId));
        containerMap.forEach((containerId, values) -> {
            Set<String> fromLocatorIdSet = values.stream().map(WmsProdPrepareExecVO::getActualFromLocatorId).collect(Collectors.toSet());
            if (fromLocatorIdSet.size() > 1) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_SO_DELIVERY_0017", WMS, mtContainerRepository.selectByPrimaryKey(containerId).getContainerCode());
            }
        });

        // 获取目标货位和目标工厂，并校验是否能获取到
        Set<String> targetWarehouseIdSet = list.stream().map(WmsProdPrepareExecVO::getTargetWarehouseId).collect(Collectors.toSet());
        Map<String, List<String>> targetLocatorMap = wmsSoDeliveryDocRepository.selectPrepareTargetLocators(tenantId, new ArrayList<>(targetWarehouseIdSet));
        Map<String, List<String>> targetSiteMap = wmsSoDeliveryDocRepository.selectPrepareTargetSites(tenantId, new ArrayList<>(targetWarehouseIdSet));
        list.forEach(rec -> {
            if (!targetLocatorMap.containsKey(rec.getTargetWarehouseId()) || targetLocatorMap.get(rec.getTargetWarehouseId()).size() != 1) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_SO_DELIVERY_0016", WMS, rec.getMaterialLotCode());
            }
            rec.setTargetLocatorId(targetLocatorMap.get(rec.getTargetWarehouseId()).get(0));
            rec.setTargetSiteId(targetSiteMap.get(rec.getTargetWarehouseId()).get(0));
        });
    }

    /**
     * 单据行状态更新
     *
     * @param tenantId      租户
     * @param eventId       事件ID
     * @param instructionId 单据行ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/21 03:43:33
     */
    private void instructionUpdate(Long tenantId, String eventId, String instructionId) {
        WmsInstructionAttrVO instruction = wmsInstructionRepository.selectDetailById(tenantId, instructionId);
        BigDecimal actualQty = instructionActualDetailRepository.instructionLimitActualDetailQuery(tenantId, instructionId).stream().map(rec -> BigDecimal.valueOf(rec.getActualQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        String instructionStatus;
        if (actualQty.compareTo(BigDecimal.ZERO) == 0) {
            instructionStatus = RELEASED;
        } else if (actualQty.compareTo(BigDecimal.ZERO) > 0 && actualQty.compareTo(instruction.getQuantity()) < 0) {
            instructionStatus = PREPARE_EXECUTE;
        } else if (actualQty.compareTo(instruction.getQuantity()) >= 0) {
            BigDecimal deliveredQty = Optional.ofNullable(instruction.getDeliveredQty()).orElse(BigDecimal.ZERO);
            if (deliveredQty.compareTo(BigDecimal.ZERO) == 0) {
                instructionStatus = PREPARE_COMPLETE;
            } else {
                if (deliveredQty.compareTo(actualQty) == 0) {
                    instructionStatus = DELIVERY_COMPLETE;
                } else {
                    instructionStatus = DELIVERY_EXECUTE;
                }
            }
        } else {
            throw new CommonException("数量错误");
        }

        MtInstructionVO instructionUpdate = new MtInstructionVO();
        instructionUpdate.setInstructionId(instructionId);
        instructionUpdate.setInstructionStatus(instructionStatus);
        instructionUpdate.setEventId(eventId);
        mtInstructionRepository.instructionUpdate(tenantId, instructionUpdate, WmsConstant.CONSTANT_N);
    }

    /**
     * 验证扫描条码
     *
     * @param tenantId             租户
     * @param result               返回结果
     * @param materialLotList      物料批列表
     * @param instructionDocId     单据ID
     * @param lineList             行列表
     * @param containerWarehouseId 容器仓库
     * @return com.ruike.wms.domain.vo.WmsProdPrepareScanVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/15 02:51:11
     */
    @ProcessLovValue
    private WmsProdPrepareScanVO validationScan(Long tenantId, WmsProdPrepareScanVO result, List<WmsMaterialLotAttrVO> materialLotList, String instructionDocId, List<WmsProductPrepareLineVO> lineList, String containerWarehouseId) {
        WmsProdPrepareScanVO returnDto = new WmsProdPrepareScanVO();
        List<WmsProdPrepareMaterialLotVO> prepareMaterialLots = new ArrayList<>();
        List<String> instructionIdList = lineList.stream().map(WmsProductPrepareLineVO::getInstructionId).collect(Collectors.toList());
        List<MtInstructionActual> actualList = instructionActualRepository.selectByCondition(Condition.builder(MtInstructionActual.class).andWhere(Sqls.custom().andIn(MtInstructionActual.FIELD_INSTRUCTION_ID, instructionIdList)).build());
        Map<String, List<MtInstructionActual>> actualMap = actualList.stream().collect(Collectors.groupingBy(MtInstructionActual::getInstructionId));
        Set<String> idSet = wmsSoDeliveryDocRepository.selectMaterialLotIdByDocId(tenantId, instructionDocId);
        //获取行指定信息
        List<WmsInstructionSnRelVO> wmsInstructionSnRelVOS = wmsInstructionSnRelMapper.selectInstruction(tenantId);
        materialLotList.forEach(rec -> {
            WmsCommonUtils.processValidateMessage(tenantId, idSet.contains(rec.getMaterialLotId()), "WMS_DISTRIBUTION_0006", WMS, rec.getMaterialLotCode());
            // 纯校验条码信息
            WmsCommonUtils.processValidateMessage(tenantId, !YES.equals(rec.getEnableFlag()), "WMS_DISTRIBUTION_0005", WMS, rec.getMaterialLotCode());
            WmsCommonUtils.processValidateMessage(tenantId, YES.equals(rec.getFreezeFlag()), "WMS_COST_CENTER_0025", WMS, rec.getMaterialLotCode());
            WmsCommonUtils.processValidateMessage(tenantId, YES.equals(rec.getStocktakeFlag()), "WMS_COST_CENTER_0034", WMS, rec.getMaterialLotCode());
            WmsCommonUtils.processValidateMessage(tenantId, YES.equals(rec.getMfFlag()), "WMS_DISTRIBUTION_0003", WMS, rec.getMaterialLotCode());
            WmsCommonUtils.processValidateMessage(tenantId, !INSTOCK.equals(rec.getStatus()), "WMS_DISTRIBUTION_0010", WMS, rec.getMaterialLotCode(), rec.getStatusMeaning());
            WmsCommonUtils.processValidateMessage(tenantId, !OK.equals(rec.getQualityStatus()), "WMS_DISTRIBUTION_0011", WMS, rec.getMaterialLotCode(), rec.getQualityStatusMeaning());
            WmsCommonUtils.processValidateMessage(tenantId, BigDecimal.ZERO.compareTo(rec.getPrimaryUomQty()) >= 0, "WMS_PUT_IN_STOCK_013", WMS, rec.getMaterialLotCode());
            // 条码和单据一同校验，顺便将匹配到的单据加入到预分配行
            //List<WmsProductPrepareLineVO> matchLines = lineList.stream().filter(line -> line.getFromSiteId().equals(rec.getPlantId())).collect(Collectors.toList());
            //WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(matchLines), "WMS_PUT_IN_STOCK_013", WMS, rec.getMaterialLotCode());

            List<WmsProductPrepareLineVO> matchLines = lineList.stream().filter(line -> line.getMaterialId().equals(rec.getMaterialId())).collect(Collectors.toList());
            WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(matchLines), "WMS_SO_DELIVERY_0006", WMS, rec.getMaterialLotCode(), rec.getMaterialCode());
            //版本校验
            List<WmsProductPrepareLineVO> lineMaterialVersionNotEmpty = matchLines.stream().filter(line -> StringUtils.isNotEmpty(line.getMaterialVersion())).collect(Collectors.toList());
            List<WmsProductPrepareLineVO> lineMaterialVersionEmpty = matchLines.stream().filter(line -> StringUtils.isEmpty(line.getMaterialVersion())).collect(Collectors.toList());
            //校验销售订单号和销售订单行号
            List<WmsProductPrepareLineVO> linespecStockFlag = matchLines.stream().filter(line -> StringUtils.equals(line.getSpecStockFlag(), WmsConstant.KEY_IFACE_STATUS_ERROR)).collect(Collectors.toList());
            List<WmsProductPrepareLineVO> linespecStockFlag1 = matchLines.stream().filter(line -> !StringUtils.equals(line.getSpecStockFlag(), WmsConstant.KEY_IFACE_STATUS_ERROR)).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(lineMaterialVersionNotEmpty)) {
                matchLines = matchLines.stream().filter(line -> StringUtils.isNotEmpty(line.getMaterialVersion()) && StringCommonUtils.equalsIgnoreBlank(line.getMaterialVersion(), rec.getMaterialVersion())).collect(Collectors.toList());
                WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(matchLines), "WMS_SO_DELIVERY_0007", WMS, rec.getMaterialLotCode(), rec.getMaterialVersion());
            }
            if (CollectionUtils.isNotEmpty(linespecStockFlag)) {
                //matchLines = matchLines.stream().filter(line ->WmsConstant.KEY_IFACE_STATUS_ERROR.equals(line.getSpecStockFlag()) && (!(StringCommonUtils.equalsIgnoreBlank(line.getSoNum(), rec.getSoNum()) && StringCommonUtils.equalsIgnoreBlank(line.getSoLineNum(), rec.getSoLineNum())))).collect(Collectors.toList());
//                List<WmsProductPrepareLineVO>  checkLine=matchLines;
//                matchLines = matchLines.stream().filter(line ->WmsConstant.KEY_IFACE_STATUS_ERROR.equals(line.getSpecStockFlag()) && (!(StringUtils.equals(line.getSoNum(), rec.getSoNum()) && StringUtils.equals(line.getSoLineNum(), rec.getSoLineNum())))).collect(Collectors.toList());
//                WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isNotEmpty(matchLines), "WMS_SO_DELIVERY_0011", WMS, rec.getMaterialLotCode());
//                if (matchLines.stream().allMatch(line -> )) {
//                    WmsCommonUtils.processValidateMessage(tenantId, "WMS_SO_DELIVERY_0011", WMS, rec.getMaterialLotCode());
//
//                }
                List<String> instructionIdList1 = new ArrayList<>();
                List<String> instructionIdList3 = new ArrayList<>();
                for(WmsProductPrepareLineVO wmsProductPrepareLineVO:matchLines){
                    if(WmsConstant.KEY_IFACE_STATUS_ERROR.equals(wmsProductPrepareLineVO.getSpecStockFlag()) && ((StringCommonUtils.equalsIgnoreBlank(wmsProductPrepareLineVO.getSoNum(), rec.getSoNum()) && StringCommonUtils.equalsIgnoreBlank(wmsProductPrepareLineVO.getSoLineNum(), rec.getSoLineNum())))){
                        instructionIdList1.add(wmsProductPrepareLineVO.getInstructionId());
                    }
                    if(WmsConstant.KEY_IFACE_STATUS_ERROR.equals(wmsProductPrepareLineVO.getSpecStockFlag())){
                        instructionIdList3.add(wmsProductPrepareLineVO.getInstructionId());
                    }
                }
                if(CollectionUtils.isNotEmpty(instructionIdList1)){
                    matchLines = matchLines.stream().filter(item ->instructionIdList1.contains(item.getInstructionId())).collect(toList());
                }else{
                    if(CollectionUtils.isNotEmpty(instructionIdList3)){
                        matchLines = matchLines.stream().filter(item ->!instructionIdList3.contains(item.getInstructionId())).collect(toList());
                    }
                }
                if(CollectionUtils.isEmpty(matchLines)){
                    WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(matchLines), "WMS_SO_DELIVERY_0011", WMS, rec.getMaterialLotCode());
                }
            }

            if(CollectionUtils.isNotEmpty(linespecStockFlag1)){
                List<String> instructionIdList2 = new ArrayList<>();
                for(WmsProductPrepareLineVO wmsProductPrepareLineVO:matchLines){
                    if(!WmsConstant.KEY_IFACE_STATUS_ERROR.equals(wmsProductPrepareLineVO.getSpecStockFlag())){
                        instructionIdList2.add(wmsProductPrepareLineVO.getInstructionId());
                    }
                }
                if(StringUtils.isBlank(rec.getSoNum()) && StringUtils.isBlank(rec.getSoLineNum())){
                    matchLines = matchLines.stream().filter(item ->instructionIdList2.contains(item.getInstructionId())).collect(toList());
                }else{
                    matchLines = matchLines.stream().filter(item ->!instructionIdList2.contains(item.getInstructionId())).collect(toList());
                }
                if(CollectionUtils.isEmpty(matchLines)){
                    WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(matchLines), "WMS_SO_DELIVERY_0011", WMS, rec.getMaterialLotCode());
                }
            }

            if (CollectionUtils.isNotEmpty(lineMaterialVersionEmpty) || CollectionUtils.isNotEmpty(linespecStockFlag1) || CollectionUtils.isNotEmpty(lineMaterialVersionNotEmpty) || CollectionUtils.isNotEmpty(linespecStockFlag)) {
                matchLines = matchLines.stream().filter(line -> line.getUomId().equals(rec.getPrimaryUomId())).collect(Collectors.toList());
                WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(matchLines), "WMS_SO_DELIVERY_0008", WMS, rec.getMaterialLotCode(), rec.getPrimaryUomCode());

                matchLines = matchLines.stream().filter(line -> line.getFromSiteId().equals(rec.getPlantId())).collect(Collectors.toList());
                WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(matchLines), "WMS_SO_DELIVERY_0005", WMS, rec.getMaterialLotCode());

                // 校验仓库
                String currentWarehouseId = StringUtils.isBlank(containerWarehouseId) ? rec.getWarehouseId() : containerWarehouseId;
                matchLines = matchLines.stream().filter(line -> STRING_MINUS_ONE.equals(line.getFromLocatorId()) || line.getFromLocatorId().equals(currentWarehouseId)).collect(Collectors.toList());
                WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(matchLines), "WMS_SO_DELIVERY_0018", WMS, rec.getMaterialLotCode());
      /*      if (matchLines.stream().anyMatch(m -> actualMap.containsKey(m.getInstructionId()) && actualMap.get(m.getInstructionId()).stream().noneMatch(a -> STRING_MINUS_ONE.equals(a.getFromLocatorId()) || currentWarehouseId.equals(a.getFromLocatorId())))) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_SO_DELIVERY_0009", WMS);
            }*/

                //校验销售订单号和销售订单行号
              /*  List<WmsProductPrepareLineVO> linespecStockFlag = matchLines.stream().filter(line -> StringUtils.equals(line.getSpecStockFlag(), WmsConstant.KEY_IFACE_STATUS_ERROR)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(linespecStockFlag)) {
                    if (matchLines.stream().anyMatch(line -> WmsConstant.KEY_IFACE_STATUS_ERROR.equals(line.getSpecStockFlag()) && (!(StringCommonUtils.equalsIgnoreBlank(line.getSoNum(), rec.getSoNum()) && StringCommonUtils.equalsIgnoreBlank(line.getSoLineNum(), rec.getSoLineNum()))))) {
                        WmsCommonUtils.processValidateMessage(tenantId, "WMS_SO_DELIVERY_0011", WMS, rec.getMaterialLotCode());
                    }
                }*/


           /* // 匹配SO
            if (matchLines.stream().anyMatch(line -> YES.equals(line.getSoFlag()) && (!(StringCommonUtils.equalsIgnoreBlank(line.getSoNum(), rec.getSoNum()) && StringCommonUtils.equalsIgnoreBlank(line.getSoLineNum(), rec.getSoLineNum())) || StringUtils.isBlank(rec.getSoNum())))) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_SO_DELIVERY_0011", WMS, rec.getMaterialLotCode());
            }*/
           /* if (matchLines.stream().anyMatch(line -> StringUtils.isNotBlank(rec.getSoNum()) && NO.equals(line.getSoFlag()))) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_SO_DELIVERY_0010", WMS, rec.getMaterialLotCode());
            }*/
                //matchLines = matchLines.stream().filter(line -> StringUtils.isBlank(rec.getSoNum()) && NO.equals(line.getSoFlag()) || (YES.equals(line.getSoFlag()) && StringCommonUtils.equalsIgnoreBlank(line.getSoNum(), rec.getSoNum()) && StringCommonUtils.equalsIgnoreBlank(line.getSoLineNum(), rec.getSoLineNum()))).collect(Collectors.toList());

                // 校验状态
                //matchLines.forEach(line -> WmsCommonUtils.processValidateMessage(tenantId, StringCommonUtils.contains(line.getInstructionStatus(), RELEASED, PREPARE_EXECUTE), "WMS_STOCKTAKE_001", WMS, "出货单行", line.getInstructionLineNum().toString(), line.getInstructionStatusMeaning()));
                List<WmsProductPrepareLineVO> matchLinesStatus = matchLines;
                matchLines = matchLines.stream().filter(line -> StringCommonUtils.contains(line.getInstructionStatus(), RELEASED, PREPARE_EXECUTE)).collect(Collectors.toList());
                WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(matchLines), "WMS_STOCKTAKE_001", WMS, "出货单行", matchLinesStatus.get(0).getInstructionLineNum().toString(), matchLinesStatus.get(0).getInstructionStatusMeaning());
                //matchLines.forEach(line -> WmsCommonUtils.processValidateMessage(tenantId, !(Objects.isNull(line.getToleranceUpperLimit()) && StringCommonUtils.contains(line.getInstructionStatus(), RELEASED, PREPARE_EXECUTE) || StringCommonUtils.contains(line.getInstructionStatus(), RELEASED, PREPARE_EXECUTE, PREPARE_COMPLETE, DELIVERY_EXECUTE, DELIVERY_COMPLETE)), "WMS_STOCKTAKE_001", WMS, "出货单行", line.getInstructionLineNum().toString(), line.getInstructionStatusMeaning()));
                //matchLines = matchLines.stream().filter(line -> Objects.isNull(line.getToleranceUpperLimit()) && StringCommonUtils.contains(line.getInstructionStatus(), RELEASED, PREPARE_EXECUTE) || StringCommonUtils.contains(line.getInstructionStatus(), RELEASED, PREPARE_EXECUTE, PREPARE_COMPLETE, DELIVERY_EXECUTE, DELIVERY_COMPLETE)).collect(Collectors.toList());

                //优先指定SN逻辑[若sn有值就不走指定SN的逻辑，如果sn无值就走指定sn的逻辑]
                List<WmsProductPrepareLineVO> matchLinesSn = matchLines;
                matchLines = matchLines.stream().filter(line -> StringUtils.isNotBlank(line.getSn()) && StringUtils.equals(line.getSn(),rec.getMaterialLotCode())).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(matchLines)){
//                    matchLines=matchLinesSn;
                    matchLines = matchLinesSn.stream().filter(line -> StringUtils.isBlank(line.getSn())).collect(Collectors.toList());
                    //匹配指定sn
                    List<WmsProductPrepareLineVO> lineList1 = new ArrayList<>();
                    List<WmsInstructionSnRelVO> wmsInstructionSnRelVOList1 = wmsInstructionSnRelVOS.stream().filter(item -> item.getMaterialLotId().equals(rec.getMaterialLotId())).collect(toList());
                    //条码没被指定
                    if (CollectionUtils.isEmpty(wmsInstructionSnRelVOList1)) {
                        List<String> instructionIds = wmsInstructionSnRelVOS.stream().map(WmsInstructionSnRelVO::getInstructionId).collect(toList());
                        matchLines = matchLines.stream().filter(item -> !instructionIds.contains(item.getInstructionId())).collect(toList());
                        if(CollectionUtils.isEmpty(matchLines)){
                            throw new MtException("WX_WMS_SO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WX_WMS_SO_DELIVERY_0010", "WMS"));
                        }
                    } else {
                        //条码被指定
                        List<String> instructionIds = wmsInstructionSnRelVOList1.stream().map(WmsInstructionSnRelVO::getInstructionId).collect(toList());
                        List<WmsProductPrepareLineVO> lineList2 = matchLines.stream().filter(item -> instructionIds.contains(item.getInstructionId())).collect(toList());
                        if (CollectionUtils.isEmpty(lineList2)) {
                            //条码被指定的行不在该单据下
                            //获取该条码被指定的行状态
                            List<MtInstruction> mtInstructions = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIds);
                            for (MtInstruction mtInstruction : mtInstructions) {
                                //判断行状态是否在值集中
                                List<LovValueDTO> status = lovAdapter.queryLovValue("WX.WMS.AUTO_SN_DOC_STATUS_LIMIT", tenantId);
                                List<LovValueDTO> statusList = status.stream().filter(item -> item.getValue().equals(mtInstruction.getInstructionStatus())).collect(Collectors.toList());
                                if (CollectionUtils.isEmpty(statusList)) {
                                    //不存在报错
                                    //获取单据num
                                    MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId, mtInstruction.getSourceDocId());
                                    throw new MtException("WX_WMS_SO_DELIVERY_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WX_WMS_SO_DELIVERY_0009", "WMS", rec.getMaterialLotCode(),mtInstructionDoc.getInstructionDocNum()));
                                }
                            }
                        } else {
                            //条码被指定的行在该单据下
                            matchLines = lineList2;
                        }
                    }
                }


                // 匹配完毕，组装数据，之后进行数量校验
                List<WmsProdPrepareAssignVO> assignList = new ArrayList<>();
                matchLines.forEach(ml -> {
                    BigDecimal remainedDemandQty = ml.getDemandQty().subtract(ml.getActualQty());
                    assignList.add(WmsProdPrepareAssignVO.newInstance(ml.getInstructionId(), ml.getInstructionLineNum(), ml.getMaterialId()
                            , ml.getUomId(), currentWarehouseId, ml.getFromSiteId(), remainedDemandQty, BigDecimal.ZERO, ml.getToleranceUpperLimit()));
                });
                prepareMaterialLots.add(WmsProdPrepareMaterialLotVO.newInstance(rec.getMaterialLotId(), rec.getMaterialId(), rec.getPrimaryUomId(), rec.getWarehouseId(), rec.getMaterialLotCode(), rec.getPrimaryUomQty(), String.join("#", rec.getPlantId(), rec.getMaterialId(), Optional.ofNullable(rec.getMaterialVersion()).orElse(""), rec.getPrimaryUomId(), rec.getLocatorId(), Optional.ofNullable(rec.getSoNum()).orElse(""), Optional.ofNullable(rec.getSoLineNum()).orElse("")), assignList));
            }
        });

        //一个物料匹配一行，另一个物料匹配多行
        List<WmsProdPrepareMaterialLotVO> backList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(prepareMaterialLots)) {
            prepareMaterialLots.stream().forEach(prepareMaterialLot -> {
                if (prepareMaterialLot.getAssignList().size() >= 2) {
                    backList.add(prepareMaterialLot);
                    prepareMaterialLot.setSort(0);
                }else{
                    prepareMaterialLot.setSort(1);
                }
            });
        }
        if (backList.size() >= 2) {
            throw new MtException("WX_WMS_SO_DELIVERY_0007", mtErrorMessageRepository.getErrorMessageWithModule
                    (tenantId, "WX_WMS_SO_DELIVERY_0007", WMS));
        }
        //数量筛选
        List<WmsProdPrepareAssignVO> lineList2 = prepareMaterialLots.stream().flatMap(rec -> rec.getAssignList().stream()).distinct().sorted(Comparator.comparing(WmsProdPrepareAssignVO::getInstructionLineNum)).collect(Collectors.toList());
        Map<String, WmsProdPrepareAssignVO> lineMap = lineList2.stream().collect(Collectors.toMap(WmsProdPrepareAssignVO::getInstructionId, a -> a, (k1, k2) -> k1));
        Map<String, List<WmsProdPrepareMaterialLotVO>> materialLotMap = prepareMaterialLots.stream().collect(Collectors.groupingBy(WmsProdPrepareMaterialLotVO::getMatchGroupKey));
        materialLotMap.forEach((key, lots) -> {
            List<String> idList = new ArrayList<>();
            for (WmsProdPrepareMaterialLotVO rec : lots) {
                for (WmsProdPrepareAssignVO line : rec.getAssignList()) {
                    WmsProdPrepareAssignVO current = lineMap.get(line.getInstructionId());
                            // 判断分配数量+条码数量是否小于剩余需求数量，能匹配上的将数量分配到行上
                    BigDecimal assignQty = current.getAssignQty().add(rec.getQuantity());
                    if (assignQty.compareTo(current.getRemainingDemandQty()) > 0) {
                        idList.add(current.getInstructionId());
                    }
                }
                List<WmsProdPrepareAssignVO> assignList1 = rec.getAssignList().stream().filter(item ->!idList.contains(item.getInstructionId())).collect(toList());
                rec.setAssignList(assignList1);
            }
        });
        //数量校验
        Boolean manualMatchFlag = this.quantityMatchValidation(tenantId, prepareMaterialLots);
        //新增排序,匹配到多行的放在第一个
        List<WmsProdPrepareMaterialLotVO> prepareMaterialLotList = prepareMaterialLots.stream().sorted(Comparator.comparing(WmsProdPrepareMaterialLotVO::getSort)).collect(Collectors.toList());
        result.setManualMatchFlag(manualMatchFlag);
        result.setMaterialLotList(prepareMaterialLotList);
        return result;
    }

    /**
     * 数量匹配校验
     *
     * @param tenantId            租户
     * @param prepareMaterialLots 备货条码
     * @return java.lang.Boolean
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/21 03:44:26
     */
    private Boolean quantityMatchValidation(Long tenantId, List<WmsProdPrepareMaterialLotVO> prepareMaterialLots) {
        AtomicReference<Boolean> manualMatchFlag = new AtomicReference<>(false);
        // 1.提取所有匹配上的行，去重并排序，提取ID作为主键，供物料批匹配
        List<WmsProdPrepareAssignVO> lineList = prepareMaterialLots.stream().flatMap(rec -> rec.getAssignList().stream()).distinct().sorted(Comparator.comparing(WmsProdPrepareAssignVO::getInstructionLineNum)).collect(Collectors.toList());
        Map<String, WmsProdPrepareAssignVO> lineMap = lineList.stream().collect(Collectors.toMap(WmsProdPrepareAssignVO::getInstructionId, a -> a, (k1, k2) -> k1));
        // 根据匹配键汇总物料批，
        Map<String, List<WmsProdPrepareMaterialLotVO>> materialLotMap = prepareMaterialLots.stream().collect(Collectors.groupingBy(WmsProdPrepareMaterialLotVO::getMatchGroupKey));
        // 2.遍历条码，按照行号顺序匹配数量，优先匹配大于条码数量的行，若不能，则将条码分配到所有能够匹配的行上
        materialLotMap.forEach((key, lots) -> {
            List<WmsProdPrepareAssignVO> assignLines = lots.get(0).getAssignList().stream().sorted(Comparator.comparing(WmsProdPrepareAssignVO::getInstructionLineNum)).collect(Collectors.toList());
            boolean allMatchedFlag = true;
            for (WmsProdPrepareMaterialLotVO rec : lots) {
                boolean matchedFlag = false;
                for (WmsProdPrepareAssignVO line : rec.getAssignList()) {
                    WmsProdPrepareAssignVO current = lineMap.get(line.getInstructionId());
                    // 判断分配数量+条码数量是否小于剩余需求数量，能匹配上的将数量分配到行上
                    BigDecimal assignQty = current.getAssignQty().add(rec.getQuantity());
                    if (assignQty.compareTo(current.getRemainingDemandQty()) <= 0) {
                        current.setAssignQty(assignQty);
                        matchedFlag = true;
                        break;
                    }
                }
                // 如果没有行被匹配上，直接后面不需要校验了，进行总体的数量校验
                if (!matchedFlag) {
                    allMatchedFlag = false;
                    break;
                }
            }

            // 若条码全部匹配完了就不校验数量了
            if (!allMatchedFlag) {
                // 如果条码数量大于行需求数量+允差数量，直接报错
                for(WmsProdPrepareMaterialLotVO rec : lots){
                    BigDecimal DemondQty = rec.getAssignList().stream().map(WmsProdPrepareAssignVO::getRemainingDemandQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal assignQty = rec.getAssignList().stream().map(WmsProdPrepareAssignVO::getAssignQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                    WmsCommonUtils.processValidateMessage(tenantId, rec.getQuantity().compareTo(DemondQty.subtract(assignQty)) > 0, "WMS_SO_DELIVERY_0012", WMS);
                }
//                BigDecimal materialLotQty = lots.stream().map(WmsProdPrepareMaterialLotVO::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
//                BigDecimal linesQty = assignLines.stream().map(l -> l.getRemainingDemandQty().add(Optional.ofNullable(l.getToleranceUpperLimit()).orElse(BigDecimal.ZERO))).reduce(BigDecimal.ZERO, BigDecimal::add);
//                WmsCommonUtils.processValidateMessage(tenantId, materialLotQty.compareTo(linesQty) > 0, "WMS_SO_DELIVERY_0012", WMS);
                // 通过校验，但还需人工匹配
                manualMatchFlag.set(true);
            }
        });
        return manualMatchFlag.get();
    }
}
