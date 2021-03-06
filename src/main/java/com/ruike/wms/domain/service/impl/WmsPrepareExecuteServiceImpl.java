package com.ruike.wms.domain.service.impl;

import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import com.ruike.hme.domain.repository.HmeObjectRecordLockRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsEventService;
import com.ruike.wms.domain.repository.*;
import com.ruike.wms.domain.service.WmsPrepareExecuteService;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.util.StringCommonUtils;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.exception.CommonException;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO;
import tarzan.actual.domain.vo.MtInstructionActualVO;
import tarzan.actual.domain.vo.MtInstructionActualVO1;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.vo.MtMaterialVO3;
import tarzan.modeling.domain.entity.MtModLocator;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.LoadTypeCode.CONTAINER;
import static com.ruike.hme.infra.constant.HmeConstants.LoadTypeCode.MATERIAL_LOT;
import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.WMS;
import static com.ruike.wms.infra.constant.WmsConstant.EventType.DISTRIBUTION_EXECUTE;
import static com.ruike.wms.infra.constant.WmsConstant.InstructionStatus.*;
import static com.ruike.wms.infra.constant.WmsConstant.PrepareExecUpdateMode.CANCEL;
import static com.ruike.wms.infra.constant.WmsConstant.PrepareExecUpdateMode.SUBMIT;

/**
 * ???????????? ????????????
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 17:02
 */
@Service
public class WmsPrepareExecuteServiceImpl implements WmsPrepareExecuteService {
    private final WmsPrepareExecuteRepository wmsPrepareExecuteRepository;
    private final MtContainerRepository mtContainerRepository;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final LovAdapter lovAdapter;
    private final WmsMaterialLotRepository wmsMaterialLotRepository;
    private final MtInstructionActualRepository mtInstructionActualRepository;
    private final WmsInstructionRepository wmsInstructionRepository;
    private final MtInstructionActualDetailRepository mtInstructionActualDetailRepository;
    private final MtEventRepository mtEventRepository;
    private final MtInstructionRepository mtInstructionRepository;
    private final MtEventRequestRepository mtEventRequestRepository;
    private final MtExtendSettingsRepository mtExtendSettingsRepository;
    private final MtInstructionDocRepository mtInstructionDocRepository;
    private final WmsObjectTransactionRepository wmsObjectTransactionRepository;
    private final WmsTransactionTypeRepository wmsTransactionTypeRepository;
    private final ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;
    private final WmsEventService wmsEventService;
    private final WmsInstructionDocRepository wmsInstructionDocRepository;
    private final HmeObjectRecordLockService hmeObjectRecordLockService;
    private final HmeObjectRecordLockRepository hmeObjectRecordLockRepository;

    public WmsPrepareExecuteServiceImpl(WmsPrepareExecuteRepository wmsPrepareExecuteRepository, MtContainerRepository mtContainerRepository, MtMaterialLotRepository mtMaterialLotRepository, MtContainerLoadDetailRepository mtContainerLoadDetailRepository, MtErrorMessageRepository mtErrorMessageRepository, LovAdapter lovAdapter, WmsMaterialLotRepository wmsMaterialLotRepository, MtInstructionActualRepository mtInstructionActualRepository, WmsInstructionRepository wmsInstructionRepository, MtInstructionActualDetailRepository mtInstructionActualDetailRepository, MtEventRepository mtEventRepository, MtInstructionRepository mtInstructionRepository, MtEventRequestRepository mtEventRequestRepository, MtExtendSettingsRepository mtExtendSettingsRepository, MtInstructionDocRepository mtInstructionDocRepository, WmsObjectTransactionRepository wmsObjectTransactionRepository, WmsTransactionTypeRepository wmsTransactionTypeRepository, ItfObjectTransactionIfaceService itfObjectTransactionIfaceService, WmsEventService wmsEventService, WmsInstructionDocRepository wmsInstructionDocRepository, HmeObjectRecordLockService hmeObjectRecordLockService, HmeObjectRecordLockRepository hmeObjectRecordLockRepository) {
        this.wmsPrepareExecuteRepository = wmsPrepareExecuteRepository;
        this.mtContainerRepository = mtContainerRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.mtContainerLoadDetailRepository = mtContainerLoadDetailRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.lovAdapter = lovAdapter;
        this.wmsMaterialLotRepository = wmsMaterialLotRepository;
        this.mtInstructionActualRepository = mtInstructionActualRepository;
        this.wmsInstructionRepository = wmsInstructionRepository;
        this.mtInstructionActualDetailRepository = mtInstructionActualDetailRepository;
        this.mtEventRepository = mtEventRepository;
        this.mtInstructionRepository = mtInstructionRepository;
        this.mtEventRequestRepository = mtEventRequestRepository;
        this.mtExtendSettingsRepository = mtExtendSettingsRepository;
        this.mtInstructionDocRepository = mtInstructionDocRepository;
        this.wmsObjectTransactionRepository = wmsObjectTransactionRepository;
        this.wmsTransactionTypeRepository = wmsTransactionTypeRepository;
        this.itfObjectTransactionIfaceService = itfObjectTransactionIfaceService;
        this.wmsEventService = wmsEventService;
        this.wmsInstructionDocRepository = wmsInstructionDocRepository;
        this.hmeObjectRecordLockService = hmeObjectRecordLockService;
        this.hmeObjectRecordLockRepository = hmeObjectRecordLockRepository;
    }

    @Override
    @ProcessLovValue
    public WmsPrepareExecInsDocVO instructionDocQuery(Long tenantId, String instructionDocNum) {
        return wmsPrepareExecuteRepository.selectDistDocByNum(tenantId, instructionDocNum);
    }

    @Override
    @ProcessLovValue
    public List<WmsPrepareExecInsVO> instructionQuery(Long tenantId, String instructionDocId) {
        return wmsPrepareExecuteRepository.selectInsListByDocId(tenantId, instructionDocId);
    }

    @Override
    @ProcessLovValue
    public List<WmsInstructionActualDetailVO> actualDetailQuery(Long tenantId, String instructionId) {
        return wmsPrepareExecuteRepository.selectActualDetailByInstId(tenantId, instructionId);
    }

    /**
     * ?????????????????????
     *
     * @param tenantId     ??????
     * @param loadTypeCode ????????????
     * @param loadTypeId   ??????ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialLotAttrVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/11 09:30:25
     */
    private List<WmsMaterialLotAttrVO> getMaterialLotList(Long tenantId, String loadTypeCode, String loadTypeId) {
        List<String> materialLotIdList;
        // ?????????????????????????????????
        MtContLoadDtlVO10 lotSearch = new MtContLoadDtlVO10();
        if (CONTAINER.equals(loadTypeCode)) {
            lotSearch.setContainerId(loadTypeId);
            lotSearch.setAllLevelFlag(WmsConstant.CONSTANT_Y);
            List<MtContLoadDtlVO4> containerMaterialLotList = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, lotSearch);
            materialLotIdList = containerMaterialLotList.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList());
        } else {
            materialLotIdList = Collections.singletonList(loadTypeId);
        }
        return wmsMaterialLotRepository.selectListWithAttrByIds(tenantId, materialLotIdList);

    }

    /**
     * ????????????????????????
     *
     * @param tenantId        ??????
     * @param instruction     ????????????
     * @param materialLotList ????????????
     * @param scan            ????????????
     * @return java.math.BigDecimal
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/11 09:26:17
     */
    private BigDecimal processLotValidation(Long tenantId, WmsInstructionAttrVO instruction, List<WmsMaterialLotAttrVO> materialLotList, WmsPrepareExecScanDTO scan) {
        List<WmsInstructionActualDetailVO> actualList = wmsPrepareExecuteRepository.selectActualDetailByInstId(tenantId, instruction.getInstructionId());
        for (WmsMaterialLotAttrVO materialLot : materialLotList) {
            if (!WmsConstant.CONSTANT_Y.equals(materialLot.getEnableFlag())) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_DISTRIBUTION_0005", WMS, materialLot.getMaterialLotCode());
            }
            if (WmsConstant.CONSTANT_Y.equals(materialLot.getFreezeFlag())) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_COST_CENTER_0025", WMS, materialLot.getMaterialLotCode());
            }
            if (WmsConstant.CONSTANT_Y.equals(materialLot.getStocktakeFlag())) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_COST_CENTER_0034", WMS, materialLot.getMaterialLotCode());
            }
            if (WmsConstant.CONSTANT_Y.equals(materialLot.getMfFlag())) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_DISTRIBUTION_0003", WMS, materialLot.getMaterialLotCode());
            }
            if (actualList.stream().anyMatch(rec -> rec.getMaterialLotId().equals(materialLot.getMaterialLotId()))) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_DISTRIBUTION_0006", WMS, materialLot.getMaterialLotCode());
            }

            if (!StringCommonUtils.equalsIgnoreBlank(instruction.getMaterialId(), materialLot.getMaterialId())) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_DISTRIBUTION_0007", WMS, materialLot.getMaterialLotCode(), instruction.getInstructionLineNum());
            }
            if (StringUtils.isNotBlank(instruction.getMaterialVersion()) && !StringCommonUtils.equalsIgnoreBlank(instruction.getMaterialVersion(), materialLot.getMaterialVersion())) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_DISTRIBUTION_0008", WMS, materialLot.getMaterialLotCode(), instruction.getInstructionLineNum());
            }
            if (!StringCommonUtils.equalsIgnoreBlank(instruction.getUomId(), materialLot.getPrimaryUomId())) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_DISTRIBUTION_0004", WMS, materialLot.getMaterialLotCode());
            }
            boolean statusScanFlag = WmsPrepareExecScanDTO.SCAN_TYPE_MAIN.equals(scan.getScanType()) && WmsConstant.MaterialLotStatus.INSTOCK.equals(materialLot.getStatus())
                    || WmsPrepareExecScanDTO.SCAN_TYPE_SPLIT.equals(scan.getScanType()) && StringUtils.containsAny(materialLot.getStatus(), WmsConstant.MaterialLotStatus.INSTOCK, WmsConstant.MaterialLotStatus.SCANNED);
            if (!statusScanFlag) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_DISTRIBUTION_0010", WMS, materialLot.getMaterialLotCode(), lovAdapter.queryLovMeaning("WMS.MTLOT.STATUS", tenantId, materialLot.getStatus()));
            }
            if (!WmsConstant.ConstantValue.OK.equals(materialLot.getQualityStatus())) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_DISTRIBUTION_0011", WMS, materialLot.getMaterialLotCode(), lovAdapter.queryLovMeaning("WMS.MTLOT.QUALITY_STATUS", tenantId, materialLot.getQualityStatus()));
            }
            if (!StringCommonUtils.equalsIgnoreBlank(instruction.getSoNum(), materialLot.getSoNum()) || !StringCommonUtils.equalsIgnoreBlank(instruction.getSoLineNum(), materialLot.getSoLineNum())) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_DISTRIBUTION_0009", WMS, materialLot.getMaterialLotCode(), instruction.getInstructionLineNum());
            }
            WmsCommonUtils.processValidateMessage(tenantId, materialLot.getPrimaryUomQty().compareTo(BigDecimal.ZERO) == 0,
                    "WMS_PUT_IN_STOCK_013", WMS, scan.getBarcode());
        }

        // ?????????????????????????????????????????????????????????
        return actualList.stream().map(WmsInstructionActualDetailVO::getPrimaryUomQty).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * ????????????
     *
     * @param tenantId    ??????
     * @param containerId ??????
     * @param instruction ?????????
     * @param scan        ????????????
     * @return com.ruike.wms.domain.vo.WmsDistributeExecuteBarcodeVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 03:29:44
     */
    @ProcessLovValue
    public WmsPrepareExecuteBarcodeVO validateContainer(Long tenantId, WmsInstructionAttrVO instruction, String containerId, WmsPrepareExecScanDTO scan) {
        // ??????????????????????????????
        MtContainer container = mtContainerRepository.containerPropertyGet(tenantId, containerId);
        MtModLocator warehouse = wmsPrepareExecuteRepository.selectLocatorOnWarehouse(tenantId, container.getLocatorId());
        if (Objects.nonNull(warehouse)) {
            WmsCommonUtils.processValidateMessage(tenantId, "WMS_DISTRIBUTION_0012", WMS, scan.getBarcode());
        }

        // ???????????????????????????
        MtContLoadDtlVO5 condition = new MtContLoadDtlVO5();
        condition.setLoadObjectType(CONTAINER);
        condition.setLoadObjectId(containerId);
        List<String> parentIdList = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, condition);
        WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isNotEmpty(parentIdList),
                "WMS_COST_CENTER_0029", WMS, scan.getBarcode());
        // ?????????????????????????????????
        MtContLoadDtlVO10 lotSearch = new MtContLoadDtlVO10();
        lotSearch.setContainerId(containerId);
        lotSearch.setAllLevelFlag(WmsConstant.CONSTANT_Y);
        List<MtContLoadDtlVO4> containerMaterialLotList = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, lotSearch);
        WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(containerMaterialLotList), "WMS_DISTRIBUTION_0018", WMS, scan.getBarcode());
        List<String> materialLotIdList = containerMaterialLotList.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList());
        List<WmsMaterialLotAttrVO> materialLotList = wmsMaterialLotRepository.selectListWithAttrByIds(tenantId, materialLotIdList);
        // ???????????????
        BigDecimal actualQty = processLotValidation(tenantId, instruction, materialLotList, scan);
        // ??????
        BigDecimal materialLotQty = materialLotList.stream().map(WmsMaterialLotAttrVO::getPrimaryUomQty).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal newActualQty = actualQty.add(materialLotQty);
        boolean overloadFlag = newActualQty.compareTo(instruction.getQuantity()) <= 0;
        if (overloadFlag) {
            WmsPrepareExecScannedDTO scanned = new WmsPrepareExecScannedDTO();
            scanned.setInstructionId(instruction.getInstructionId());
            scanned.setLoadObjectType(CONTAINER);
            scanned.setLoadObjectId(containerId);
            scanned.setActualQty(materialLotQty);
            scanned.setUomId(materialLotList.get(0).getPrimaryUomId());
            this.submitScanned(tenantId, scanned);
        }

        // ???????????????
        MtInstruction mtInstruction = mtInstructionRepository.instructionPropertyGet(tenantId, instruction.getInstructionId());
        MtModLocator locator = wmsPrepareExecuteRepository.selectDistLocatorBySiteId(tenantId, instruction.getMaterialId(), instruction.getMaterialVersion(), instruction.getSiteId(), instruction.getSoNum(), instruction.getSoLineNum());
        WmsPrepareExecuteBarcodeVO barcodeInfo = new WmsPrepareExecuteBarcodeVO();
        barcodeInfo.setLoadObjectId(containerId);
        barcodeInfo.setLoadObjectType(CONTAINER);
        barcodeInfo.setMaterialLotList(materialLotList);
        barcodeInfo.setUomId(instruction.getUomId());
        barcodeInfo.setPrimaryUomQty(materialLotQty);
        barcodeInfo.setOverloadFlag(overloadFlag);
        if (locator != null) {
            barcodeInfo.setLocatorId(locator.getLocatorId());
            barcodeInfo.setLocatorCode(locator.getLocatorCode());
        } else {
            barcodeInfo.setLocatorId("");
            barcodeInfo.setLocatorCode("");
        }
        barcodeInfo.setInstructionStatus(mtInstruction.getInstructionStatus());
        barcodeInfo.setInstructionStatusMeaning(lovAdapter.queryLovMeaning("WMS.DISTRIBUTION_LINE_STATUS", tenantId, mtInstruction.getInstructionStatus()));
        return barcodeInfo;
    }

    /**
     * ???????????????
     *
     * @param tenantId      ??????
     * @param materialLotId ?????????ID
     * @return com.ruike.wms.domain.vo.WmsPrepareExecuteBarcodeVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 03:30:54
     */
    @ProcessLovValue
    public WmsPrepareExecuteBarcodeVO validateMaterialLot(Long tenantId, WmsInstructionAttrVO instruction, String materialLotId, WmsPrepareExecScanDTO scan) {
        // ???????????????????????????????????????????????????
        MtContLoadDtlVO5 condition = new MtContLoadDtlVO5();
        condition.setLoadObjectType(MATERIAL_LOT);
        condition.setLoadObjectId(materialLotId);
        List<String> parentIdList = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, condition);
        WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isNotEmpty(parentIdList),
                "WMS_COST_CENTER_0030", WMS, scan.getBarcode());
        // ??????????????????????????????
        MtMaterialLot lot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotId);
        MtModLocator warehouse = wmsPrepareExecuteRepository.selectLocatorOnWarehouse(tenantId, lot.getLocatorId());
        if (Objects.nonNull(warehouse)) {
            WmsCommonUtils.processValidateMessage(tenantId, "WMS_DISTRIBUTION_0012", WMS, scan.getBarcode());
        }

        // ???????????????
        List<WmsMaterialLotAttrVO> materialLotList = wmsMaterialLotRepository.selectListWithAttrByIds(tenantId, Collections.singletonList(materialLotId));
        BigDecimal actualQty = processLotValidation(tenantId, instruction, materialLotList, scan);
        // ??????
        BigDecimal materialLotQty = materialLotList.stream().map(WmsMaterialLotAttrVO::getPrimaryUomQty).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal newActualQty = actualQty.add(materialLotQty);
        boolean overloadFlag = newActualQty.compareTo(instruction.getQuantity()) <= 0;
        if (newActualQty.compareTo(instruction.getQuantity()) <= 0) {
            WmsPrepareExecScannedDTO scanned = new WmsPrepareExecScannedDTO();
            scanned.setInstructionId(instruction.getInstructionId());
            scanned.setLoadObjectType(MATERIAL_LOT);
            scanned.setLoadObjectId(materialLotId);
            scanned.setActualQty(materialLotQty);
            scanned.setUomId(materialLotList.get(0).getPrimaryUomId());
            this.submitScanned(tenantId, scanned);
        }

        MtInstruction mtInstruction = mtInstructionRepository.instructionPropertyGet(tenantId, instruction.getInstructionId());
        MtModLocator locator = wmsPrepareExecuteRepository.selectDistLocatorBySiteId(tenantId, instruction.getMaterialId(), instruction.getMaterialVersion(), instruction.getSiteId(), instruction.getSoNum(), instruction.getSoLineNum());
        WmsPrepareExecuteBarcodeVO barcodeInfo = new WmsPrepareExecuteBarcodeVO();
        barcodeInfo.setLoadObjectId(materialLotId);
        barcodeInfo.setLoadObjectType(MATERIAL_LOT);
        barcodeInfo.setUomId(instruction.getUomId());
        barcodeInfo.setPrimaryUomQty(materialLotQty);
        barcodeInfo.setOverloadFlag(overloadFlag);
        if (locator != null) {
            barcodeInfo.setLocatorId(locator.getLocatorId());
            barcodeInfo.setLocatorCode(locator.getLocatorCode());
        } else {
            barcodeInfo.setLocatorId("");
            barcodeInfo.setLocatorCode("");
        }
        barcodeInfo.setInstructionStatus(mtInstruction.getInstructionStatus());
        barcodeInfo.setInstructionStatusMeaning(lovAdapter.queryLovMeaning("WMS.DISTRIBUTION_LINE_STATUS", tenantId, mtInstruction.getInstructionStatus()));
        return barcodeInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public WmsPrepareExecuteBarcodeVO barcodeScan(Long tenantId, WmsPrepareExecScanDTO scan) {
        if (!StringUtils.containsAny(scan.getScanType(), WmsPrepareExecScanDTO.SCAN_TYPE_MAIN, WmsPrepareExecScanDTO.SCAN_TYPE_SPLIT)) {
            throw new CommonException("???????????????????????????");
        }
        // ?????????????????????
        WmsInstructionAttrVO instruction = wmsInstructionRepository.selectDetailById(tenantId, scan.getInstructionId());
        if (!StringCommonUtils.contains(instruction.getInstructionStatus(), PREPARE_EXECUTE, RELEASED)) {
            String status = lovAdapter.queryLovMeaning("WMS.DISTRIBUTION_LINE_STATUS", tenantId, instruction.getInstructionStatus());
            throw new MtException("WMS_STOCKTAKE_001", mtErrorMessageRepository.getErrorMessageWithModule
                    (tenantId, "WMS_STOCKTAKE_001", WMS, "????????????", instruction.getInstructionNum(), status));
        }
        MtContainerVO13 conSelect = new MtContainerVO13();
        conSelect.setContainerCode(scan.getBarcode());
        List<String> containerIdList = mtContainerRepository.propertyLimitContainerQuery(tenantId, conSelect);
        if (CollectionUtils.isNotEmpty(containerIdList)) {
            // ??????????????????
            return validateContainer(tenantId, instruction, containerIdList.get(0), scan);
        } else {
            // ?????????????????????
            MtMaterialLotVO3 lotSelect = new MtMaterialLotVO3();
            lotSelect.setMaterialLotCode(scan.getBarcode());
            List<String> materialLotIdList = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, lotSelect);
            WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(materialLotIdList),
                    "WMS_COST_CENTER_0006", WMS, scan.getBarcode());
            return validateMaterialLot(tenantId, instruction, materialLotIdList.get(0), scan);
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param tenantId          ??????
     * @param eventId           ??????
     * @param materialLotIdList ?????????
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 12:37:32
     */
    private void updateBatchMaterialLot(Long tenantId, String eventId, List<String> materialLotIdList, String updateMode) {
        // ??????????????????
        List<MtMaterialLotVO20> materialLotList = materialLotIdList.stream().map(t -> new MtMaterialLotVO20() {{
            setMaterialLotId(t);
        }}).collect(Collectors.toList());
        MtMaterialLotVO2 materialLotUpdate = new MtMaterialLotVO2();
        mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotList, eventId, WmsConstant.CONSTANT_N);

        //????????????????????????
        List<MtCommonExtendVO7> attrList = materialLotIdList.stream().map(t -> new MtCommonExtendVO7(t, Collections.singletonList(new MtCommonExtendVO4("STATUS", SUBMIT.equals(updateMode) ? WmsConstant.MaterialLotStatus.SCANNED : WmsConstant.MaterialLotStatus.INSTOCK)))).collect(Collectors.toList());
        mtExtendSettingsRepository.attrPropertyBatchUpdateNew(tenantId, "mt_material_lot_attr", eventId, attrList);
    }

    /**
     * ??????????????????
     *
     * @param tenantId ??????
     * @param dto      ????????????
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 12:36:17
     */
    private void instructionActualUpdate(Long tenantId, String eventId, WmsPrepareExecScannedDTO dto) {
        List<MtInstructionActual> actualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, dto.getInstructionId());
        if (CollectionUtils.isEmpty(actualList)) {
            throw new CommonException("????????????????????????");
        }
        MtInstructionActual actual = actualList.get(0);
        // ????????????
        MtInstructionActualVO actualVo = new MtInstructionActualVO();
        actualVo.setActualId(actual.getActualId());
        actualVo.setActualQty(dto.getActualQty().doubleValue());
        actualVo.setEventId(eventId);
        MtInstructionActualVO1 instructionActual = mtInstructionActualRepository.instructionActualUpdate(tenantId, actualVo);
        // ??????????????????
        switch (dto.getLoadObjectType()) {
            case CONTAINER:
                // ?????????????????????????????????
                MtContLoadDtlVO10 lotSearch = new MtContLoadDtlVO10();
                lotSearch.setContainerId(dto.getLoadObjectId());
                lotSearch.setAllLevelFlag(WmsConstant.CONSTANT_Y);
                List<MtContLoadDtlVO4> containerMaterialLotList = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, lotSearch);
                List<String> materialLotIdList = containerMaterialLotList.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList());
                List<WmsMaterialLotAttrVO> materialLotList = wmsMaterialLotRepository.selectListWithAttrByIds(tenantId, materialLotIdList);
                materialLotList.forEach(rec -> {
                    MtInstructionActualDetail actualDetail = new MtInstructionActualDetail();
                    actualDetail.setActualId(instructionActual.getActualId());
                    actualDetail.setContainerId(dto.getLoadObjectId());
                    actualDetail.setMaterialLotId(rec.getMaterialLotId());
                    actualDetail.setUomId(rec.getPrimaryUomId());
                    actualDetail.setActualQty(rec.getPrimaryUomQty().doubleValue());
                    actualDetail.setFromLocatorId(rec.getLocatorId());
                    mtInstructionActualDetailRepository.instructionActualDetailCreate(tenantId, actualDetail);
                });
                break;
            case MATERIAL_LOT:
                MtMaterialLot materialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getLoadObjectId());
                MtInstructionActualDetail actualDetail = new MtInstructionActualDetail();
                actualDetail.setActualId(instructionActual.getActualId());
                actualDetail.setMaterialLotId(dto.getLoadObjectId());
                actualDetail.setUomId(materialLot.getPrimaryUomId());
                actualDetail.setActualQty(materialLot.getPrimaryUomQty());
                actualDetail.setFromLocatorId(materialLot.getLocatorId());
                mtInstructionActualDetailRepository.instructionActualDetailCreate(tenantId, actualDetail);
                break;
            default:
                break;
        }
    }

    private String createEvent(Long tenantId, String updateMode) {
        // ????????????
        MtEventCreateVO event = new MtEventCreateVO();
        switch (updateMode) {
            case SUBMIT:
                event.setEventTypeCode("MATERIALLOT_SCAN");
                break;
            case CANCEL:
                event.setEventTypeCode("MATERIALLOT_SCAN_CANCEL");
                break;
            default:
                break;
        }
        return mtEventRepository.eventCreate(tenantId, event);
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param tenantId       ??????
     * @param eventId        ??????ID
     * @param loadObjectType ????????????
     * @param loadObjectId   ??????ID
     * @param updateMode     ????????????
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/10 10:31:20
     */
    private List<String> materialLotStatusUpdate(Long tenantId, String eventId, String loadObjectType, String loadObjectId, String updateMode) {
        List<String> materialLotIdList = new ArrayList<>();
        switch (loadObjectType) {
            case CONTAINER:
                // ?????????????????????????????????
                MtContLoadDtlVO10 lotSearch = new MtContLoadDtlVO10();
                lotSearch.setContainerId(loadObjectId);
                lotSearch.setAllLevelFlag(WmsConstant.CONSTANT_Y);
                List<MtContLoadDtlVO4> containerMaterialLotList = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, lotSearch);
                materialLotIdList = containerMaterialLotList.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList());
                break;
            case MATERIAL_LOT:
                materialLotIdList = Collections.singletonList(loadObjectId);
                break;
            default:
                break;
        }
        return materialLotIdList;
    }

    /**
     * ??????????????? ??????????????????????????????
     *
     * @param tenantId      ??????
     * @param eventId       ??????
     * @param instructionId ????????????
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 12:36:17
     */
    private String instructionUpdate(Long tenantId, String eventId, String instructionId) {
        WmsInstructionAttrVO instruction = wmsInstructionRepository.selectDetailById(tenantId, instructionId);
        BigDecimal actualQty = mtInstructionActualDetailRepository.instructionLimitActualDetailQuery(tenantId, instructionId)
                .stream().map(rec -> BigDecimal.valueOf(rec.getActualQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        String instructionStatus = RELEASED;
        if (actualQty.compareTo(BigDecimal.ZERO) == 0) {
            instructionStatus = RELEASED;
        } else if (actualQty.compareTo(BigDecimal.ZERO) > 0 && actualQty.compareTo(instruction.getQuantity()) < 0) {
            instructionStatus = PREPARE_EXECUTE;
        } else if (actualQty.compareTo(instruction.getQuantity()) >= 0) {
            BigDecimal signedQty = Optional.ofNullable(instruction.getSignedQty()).orElse(BigDecimal.ZERO);
            if (signedQty.compareTo(BigDecimal.ZERO) == 0) {
                instructionStatus = PREPARE_COMPLETE;
            }
        } else {
            throw new CommonException("????????????");
        }
        MtInstructionVO instructionUpdate = new MtInstructionVO();
        instructionUpdate.setInstructionId(instructionId);
        instructionUpdate.setInstructionStatus(instructionStatus);
        instructionUpdate.setEventId(eventId);
        mtInstructionRepository.instructionUpdate(tenantId, instructionUpdate, WmsConstant.CONSTANT_N);
        return instructionStatus;
    }

    @Override
    @ProcessLovValue
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public WmsPrepareExecuteBarcodeVO submitScanned(Long tenantId, WmsPrepareExecScannedDTO dto) {
        // ????????????
        String eventId = createEvent(tenantId, SUBMIT);

        // i.	????????????
        this.instructionActualUpdate(tenantId, eventId, dto);

        // ii.	??????????????????
        List<String> materialLotIdList = this.materialLotStatusUpdate(tenantId, eventId, dto.getLoadObjectType(), dto.getLoadObjectId(), SUBMIT);
        updateBatchMaterialLot(tenantId, eventId, materialLotIdList, SUBMIT);

        // iii.	??????????????????
        String instructionStatus = this.instructionUpdate(tenantId, eventId, dto.getInstructionId());

        // ??????????????????
        WmsPrepareExecuteBarcodeVO instruction = new WmsPrepareExecuteBarcodeVO();
        instruction.setInstructionStatus(instructionStatus);
        return instruction;
    }

    /**
     * ??????????????????
     *
     * @param tenantId      ??????
     * @param instructionId ????????????ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 12:36:17
     */
    private void instructionActualCancel(Long tenantId, String instructionId, List<WmsBarcodeDTO> barcodeList, String eventId) {
        // ????????????actual??????
        List<MtInstructionActual> actualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, instructionId);
        if (CollectionUtils.isEmpty(actualList)) {
            throw new CommonException("???????????????actual??????");
        }
        BigDecimal cancelledQty = BigDecimal.ZERO;
        MtInstructionActual instructionActual = actualList.get(0);
        for (WmsBarcodeDTO barcode : barcodeList) {
            List<WmsMaterialLotAttrVO> materialLotList = this.getMaterialLotList(tenantId, barcode.getLoadObjectType(), barcode.getLoadObjectId());
            BigDecimal materialLotQty = materialLotList.stream().map(WmsMaterialLotAttrVO::getPrimaryUomQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            cancelledQty = cancelledQty.add(materialLotQty);
            // ???????????????????????????
            MtInstructionActualDetail query = new MtInstructionActualDetail();
            query.setActualId(instructionActual.getActualId());
            if (MATERIAL_LOT.equals(barcode.getLoadObjectType())) {
                query.setMaterialLotId(barcode.getLoadObjectId());
            } else {
                query.setContainerId(barcode.getLoadObjectId());
            }
            List<MtInstructionActualDetailVO> detailList = mtInstructionActualDetailRepository.propertyLimitInstructionActualDetailQuery(tenantId, query);
            List<String> detailIdList = detailList.stream().map(MtInstructionActualDetailVO::getActualDetailId).collect(Collectors.toList());
            mtInstructionActualDetailRepository.instructionActualDetailBatchDelete(tenantId, detailIdList);
        }

        // ????????????
        MtInstructionActualVO actualVo = new MtInstructionActualVO();
        actualVo.setActualId(instructionActual.getActualId());
        actualVo.setActualQty(cancelledQty.negate().doubleValue());
        actualVo.setEventId(eventId);
        mtInstructionActualRepository.instructionActualUpdate(tenantId, actualVo);
    }

    @Override
    @ProcessLovValue
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public WmsPrepareExecInsVO removeScanned(Long tenantId, String instructionId, List<WmsBarcodeDTO> barcodeList) {
        // ????????????
        String eventId = createEvent(tenantId, CANCEL);

        // i.	????????????
        this.instructionActualCancel(tenantId, instructionId, barcodeList, eventId);

        // ii.	??????????????????
        List<String> materialLotIdList = barcodeList.stream().map(rec -> this.materialLotStatusUpdate(tenantId, eventId, rec.getLoadObjectType(), rec.getLoadObjectId(), CANCEL)).flatMap(Collection::stream).collect(Collectors.toList());
        updateBatchMaterialLot(tenantId, eventId, materialLotIdList, CANCEL);

        // iii.	??????????????????
        String instructionStatus = this.instructionUpdate(tenantId, eventId, instructionId);
        WmsInstructionAttrVO instruction = wmsInstructionRepository.selectDetailById(tenantId, instructionId);
        MtModLocator locator = wmsPrepareExecuteRepository.selectDistLocatorBySiteId(tenantId, instruction.getMaterialId(), instruction.getMaterialVersion(), instruction.getSiteId(), instruction.getSoNum(), instruction.getSoLineNum());
        WmsPrepareExecInsVO execIns = new WmsPrepareExecInsVO();
        execIns.setInstructionStatus(instructionStatus);
        if (locator != null) {
            execIns.setLocatorCode(locator.getLocatorCode());
            execIns.setLocatorId(locator.getLocatorId());
        } else {
            execIns.setLocatorCode("");
            execIns.setLocatorId("");
        }
        return execIns;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String split(Long tenantId, WmsPrepareExecSplitDTO dto) {
        // ??????????????????
        if (StringUtils.isNotBlank(dto.getCustomMaterialLotCode())) {
            MtMaterialLotVO3 materialLot = new MtMaterialLotVO3();
            materialLot.setMaterialLotCode(dto.getCustomMaterialLotCode());
            List<String> materialLotIdList = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLot);
            WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isNotEmpty(materialLotIdList), "WMS_MTLOT_SPLIT_0003", WMS, dto.getCustomMaterialLotCode());
        }

        // ????????????
        final String requestTypeCode = "MATERIAL_LOT_SPLIT";
        final String attrTable = "mt_material_lot_attr";
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, requestTypeCode);
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventRequestId(eventRequestId);
        eventCreate.setEventTypeCode(requestTypeCode);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);

        // ???????????????
        if (StringUtils.isNotBlank(dto.getInstructionId())) {
            WmsBarcodeDTO barcode = new WmsBarcodeDTO();
            barcode.setLoadObjectId(dto.getMaterialLotId());
            barcode.setLoadObjectType(MATERIAL_LOT);
            this.removeScanned(tenantId, dto.getInstructionId(), Collections.singletonList(barcode));
        }

        // ???????????????
        MtMaterialVO3 splitCondition = new MtMaterialVO3();
        splitCondition.setEventRequestId(eventRequestId);
        splitCondition.setSourceMaterialLotId(dto.getMaterialLotId());
        splitCondition.setSplitPrimaryQty(dto.getSplitQty().doubleValue());
        if (StringUtils.isNotBlank(dto.getCustomMaterialLotCode())) {
            splitCondition.setSplitMaterialLotCode(dto.getCustomMaterialLotCode());
        }
        String targetMaterialLotId = mtMaterialLotRepository.materialLotSplit(tenantId, splitCondition);

        // ??????????????????
        MtExtendVO extendQuery = new MtExtendVO();
        extendQuery.setTableName(attrTable);
        extendQuery.setKeyId(dto.getMaterialLotId());
        List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendQuery);
        List<MtExtendVO5> targetExtendAttrList = new ArrayList<>(extendAttrList.size());

        // ???????????????
        for (MtExtendAttrVO attr : extendAttrList) {
            MtExtendVO5 newAttr = new MtExtendVO5();
            newAttr.setAttrName(attr.getAttrName());
            newAttr.setAttrValue(attr.getAttrValue());
            if ("ORIGINAL_ID".equals(attr.getAttrName())) {
                newAttr.setAttrValue(dto.getMaterialLotId());
            } else {
                newAttr.setAttrValue(attr.getAttrValue());
            }
            targetExtendAttrList.add(newAttr);
        }
        // ????????????????????????ID????????????
        if (extendAttrList.stream().noneMatch(rec -> "ORIGINAL_ID".equals(rec.getAttrName()))) {
            MtExtendVO5 newAttr = new MtExtendVO5();
            newAttr.setAttrName("ORIGINAL_ID");
            newAttr.setAttrValue(dto.getMaterialLotId());
            targetExtendAttrList.add(newAttr);
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, attrTable, targetMaterialLotId, eventId, targetExtendAttrList);

        MtMaterialLot targetMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, targetMaterialLotId);

        return targetMaterialLot.getMaterialLotCode();
    }

    private void materialLotExtendAttrUpdate(Long tenantId, String eventId, String materialLotId, String signFlag) {
        final String tableName = "mt_material_lot_attr";
        List<MtExtendVO5> attrList = new ArrayList<>();
        MtExtendVO5 attr = new MtExtendVO5();
        attr.setAttrName("STATUS");
        attr.setAttrValue(WmsConstant.CONSTANT_Y.equals(signFlag) ? WmsConstant.MaterialLotStatus.PREPARED : WmsConstant.MaterialLotStatus.SHIPPED);
        attrList.add(attr);
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, tableName, materialLotId, eventId, attrList);
    }

    /**
     * ??????????????????
     *
     * @param tenantId          ??????
     * @param materialLotIdList ???????????????
     * @param trxReq            ??????????????????
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/28 10:00:26
     */
    private List<WmsObjectTransactionRequestVO> addInventoryTransaction(Long tenantId, List<String> materialLotIdList, WmsPrepareExecTrxReqVO trxReq) {
        String transactionTypeCode = WmsConstant.CONSTANT_Y.equals(trxReq.getSignFlag()) ? WmsConstant.TransactionTypeCode.WMS_LOCATOR_TRAN : WmsConstant.TransactionTypeCode.WMS_WAREHOUSE_TRAN;
        WmsTransactionTypeDTO transactionType = wmsTransactionTypeRepository.getTransactionType(tenantId, transactionTypeCode);
        if (Objects.isNull(transactionType)) {
            throw new CommonException("???????????????????????????{}", transactionTypeCode);
        }
        List<WmsMaterialLotAttrVO> materialLotList = wmsMaterialLotRepository.selectListWithAttrByIds(tenantId, materialLotIdList);
        List<WmsObjectTransactionRequestVO> requestList = new ArrayList<>();
        Date now = new Date();
        for (WmsMaterialLotAttrVO materialLot : materialLotList) {
            WmsObjectTransactionRequestVO request = new WmsObjectTransactionRequestVO();
            request.setTransactionTypeCode(transactionTypeCode);
            request.setEventId(trxReq.getEventId());
            request.setMaterialLotId(materialLot.getMaterialLotId());
            request.setMaterialId(materialLot.getMaterialId());
            request.setTransactionQty(materialLot.getPrimaryUomQty());
            request.setTransactionUom(materialLot.getPrimaryUomCode());
            request.setLotNumber(materialLot.getLot());
            request.setTransactionTime(now);
            request.setTransactionReasonCode(WmsConstant.TransactionReasonCode.PREPARE_EXECUTE);
            request.setPlantId(materialLot.getPlantId());
            request.setWarehouseId(materialLot.getWarehouseId());
            request.setLocatorId(materialLot.getLocatorId());
            request.setTransferLotNumber(materialLot.getLot());
            request.setTransferPlantId(trxReq.getTargetSiteId());
            request.setTransferWarehouseId(trxReq.getTargetWarehouseId());
            request.setTransferLocatorId(trxReq.getTargetLocatorId());
            request.setSourceDocType(WmsConstant.DocType.DISTRIBUTION_DOC);
            request.setSourceDocId(trxReq.getInstructionDocId());
            request.setSourceDocLineId(trxReq.getInstructionId());
            request.setContainerId(trxReq.getContainerId());
            request.setMoveType(transactionType.getMoveType());
            request.setAttribute40(transactionType.getProcessErpFlag());
            request.setSoNum(materialLot.getSoNum());
            request.setSoLineNum(materialLot.getSoLineNum());
            requestList.add(request);
        }
        return requestList;
    }

    private List<WmsObjectTransactionRequestVO> executeTransaction(Long tenantId, String instructionDocId, WmsEventVO event, String signFlag) {
        // ??????????????????????????????????????????????????????????????????
        List<WmsInstructionActualDetailVO> detailList = wmsPrepareExecuteRepository.selectActualDetailByDocId(tenantId, instructionDocId);
        WmsInstructionDocAttrVO instructionDoc = wmsInstructionDocRepository.selectByDocId(tenantId, instructionDocId);
        detailList = detailList.stream().filter(rec -> WmsConstant.MaterialLotStatus.SCANNED.equals(rec.getMaterialLotStatus())).collect(Collectors.toList());
        Set<String> trfContainerSet = new HashSet<>();
        List<WmsObjectTransactionRequestVO> requestList = new ArrayList<>();
        // ??????????????????
        for (WmsInstructionActualDetailVO detail : detailList) {
            // ?????????????????????????????????
            if (StringUtils.isNotBlank(detail.getContainerId())) {
                // ????????????
                if (!trfContainerSet.contains(detail.getContainerId())) {
                    // ???????????????????????????
                    WmsLocatorSiteVO targetLocator = wmsPrepareExecuteRepository.selectTargetLocator(tenantId, signFlag, instructionDoc, detail);
                    // ??????????????????????????? ?????????????????????
                    MtContLoadDtlVO10 lotSearch = new MtContLoadDtlVO10();
                    lotSearch.setContainerId(detail.getContainerId());
                    lotSearch.setAllLevelFlag(WmsConstant.CONSTANT_Y);
                    List<MtContLoadDtlVO4> containerMaterialLotList = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, lotSearch);
                    List<String> materialLotIdList = containerMaterialLotList.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList());
                    // ??????????????????
                    WmsPrepareExecTrxReqVO trxReq = new WmsPrepareExecTrxReqVO();
                    trxReq = trxReq.setEventId(event.getEventId()).setSignFlag(signFlag).setInstructionDocId(instructionDocId).setInstructionId(detail.getInstructionId()).setTargetSiteId(targetLocator.getSiteId()).setTargetWarehouseId(targetLocator.getWarehouseId()).setTargetLocatorId(targetLocator.getLocatorId()).setContainerId(detail.getContainerId());
                    requestList.addAll(this.addInventoryTransaction(tenantId, materialLotIdList, trxReq));

                    // ????????????
                    MtContainerVO7 container = new MtContainerVO7();
                    container.setContainerId(detail.getContainerId());
                    container.setTargetLocatorId(targetLocator.getLocatorId());
                    container.setTargetSiteId(targetLocator.getSiteId());
                    container.setEventRequestId(event.getEventRequestId());
                    mtContainerRepository.containerTransfer(tenantId, container);
                    trfContainerSet.add(detail.getContainerId());
                    // ?????????????????????
                    materialLotIdList.forEach(rec -> this.materialLotExtendAttrUpdate(tenantId, event.getEventId(), rec, signFlag));
                }
            } else {
                // ???????????????????????????
                WmsLocatorSiteVO targetLocator = wmsPrepareExecuteRepository.selectTargetLocator(tenantId, signFlag, instructionDoc, detail);
                // ??????????????????
                WmsPrepareExecTrxReqVO trxReq = new WmsPrepareExecTrxReqVO();
                trxReq = trxReq.setEventId(event.getEventId()).setSignFlag(signFlag).setInstructionDocId(instructionDocId).setInstructionId(detail.getInstructionId()).setTargetSiteId(targetLocator.getSiteId()).setTargetWarehouseId(targetLocator.getWarehouseId()).setTargetLocatorId(targetLocator.getLocatorId());
                requestList.addAll(this.addInventoryTransaction(tenantId, Collections.singletonList(detail.getMaterialLotId()), trxReq));
                // ????????????
                MtMaterialLotVO9 lot = new MtMaterialLotVO9();
                lot.setMaterialLotId(detail.getMaterialLotId());
                lot.setTargetLocatorId(targetLocator.getLocatorId());
                lot.setTargetSiteId(targetLocator.getSiteId());
                lot.setEventRequestId(event.getEventRequestId());
                mtMaterialLotRepository.materialLotTransfer(tenantId, lot);
                // ?????????????????????
                this.materialLotExtendAttrUpdate(tenantId, event.getEventId(), detail.getMaterialLotId(), signFlag);
            }
        }
        return requestList;
    }

    @Override
    public Boolean validateBeforeExecute(Long tenantId, String instructionDocId) {
        List<WmsInstructionAttrVO> lineList = wmsInstructionRepository.selectScannedByDocId(tenantId, instructionDocId);
        if (CollectionUtils.isEmpty(lineList)) {
            WmsCommonUtils.processValidateMessage(tenantId, "WMS_DISTRIBUTION_0013", WMS);
        }
        return lineList.stream().anyMatch(rec -> WmsConstant.InstructionStatus.PREPARE_EXECUTE.equals(rec.getInstructionStatus()));
    }

    /**
     * ????????????????????????
     *
     * @param tenantId    ??????
     * @param event       ??????
     * @param instruction ????????????
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/11 09:31:08
     */
    private void processUpdateInstruction(Long tenantId, WmsEventVO event, WmsInstructionAttrVO instruction) {
        MtInstructionVO instructionUpd = new MtInstructionVO();
        instructionUpd.setEventId(event.getEventId());
        instructionUpd.setInstructionId(instruction.getInstructionId());
        instructionUpd.setInstructionStatus(WmsConstant.CONSTANT_Y.equals(instruction.getSignFlag()) ? WmsConstant.InstructionStatus.PREPARE_COMPLETE : WmsConstant.InstructionStatus.SIGN_COMPLETE);
        mtInstructionRepository.instructionUpdate(tenantId, instructionUpd, WmsConstant.CONSTANT_N);

        if (WmsConstant.CONSTANT_N.equals(instruction.getSignFlag())) {
            // ??????????????????
            List<MtExtendVO5> attrs = new ArrayList<>();
            MtExtendVO5 attr = new MtExtendVO5();
            attr.setAttrName("SIGNED_QTY");
            attr.setAttrValue(String.valueOf(instruction.getActualQty()));
            attrs.add(attr);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", instruction.getInstructionId(), event.getEventId(), attrs);
        }
    }

    /**
     * ?????????????????????
     *
     * @param tenantId         ??????
     * @param instructionDocId ?????????
     * @param eventId          ??????
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/11 09:33:07
     */
    private void instructionDocUpdate(Long tenantId, String instructionDocId, String eventId) {
        List<WmsInstructionAttrVO> list = wmsInstructionRepository.selectListByDocId(tenantId, instructionDocId);
        String instructionDocStatus = PREPARE_EXECUTE;
        int releaseCount = 0, completeCount = 0, signCount = 0;
        for (WmsInstructionAttrVO ins : list) {
            if (RELEASED.equals(ins.getInstructionStatus()) || PREPARE_EXECUTE.equals(ins.getInstructionStatus())) {
                releaseCount += 1;
            } else if (PREPARE_COMPLETE.equals(ins.getInstructionStatus())) {
                completeCount += 1;
            } else if (SIGN_COMPLETE.equals(ins.getInstructionStatus())) {
                signCount += 1;
            }
        }
        if (releaseCount > 0) {
            instructionDocStatus = PREPARE_EXECUTE;
        } else if (completeCount == list.size()) {
            instructionDocStatus = PREPARE_COMPLETE;
        } else if (releaseCount == 0 && signCount > 0 && signCount < list.size()) {
            instructionDocStatus = SIGN_EXECUTE;
        } else if (signCount == list.size()) {
            instructionDocStatus = SIGN_COMPLETE;
        }
        MtInstructionDocDTO2 docUpdate = new MtInstructionDocDTO2();
        docUpdate.setInstructionDocId(instructionDocId);
        docUpdate.setEventId(eventId);
        docUpdate.setInstructionDocStatus(instructionDocStatus);
        mtInstructionDocRepository.instructionDocUpdate(tenantId, docUpdate, WmsConstant.CONSTANT_N);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Boolean execute(Long tenantId, String instructionDocId) {
        // 20211202 add by sanfeng.zhang for peng.zhao ????????????
        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
        hmeObjectRecordLockDTO.setFunctionName("????????????");
        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PDA);
        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.DOCUMENT);
        hmeObjectRecordLockDTO.setObjectRecordId(instructionDocId);
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(instructionDocId);
        hmeObjectRecordLockDTO.setObjectRecordCode(mtInstructionDoc != null ? mtInstructionDoc.getInstructionDocNum() : "");
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
        //??????
        hmeObjectRecordLockRepository.commonLockWo(hmeObjectRecordLock);
        try {
            // ??????????????????
            this.validateBeforeExecute(tenantId, instructionDocId);
            List<WmsInstructionAttrVO> lineList = wmsInstructionRepository.selectScannedByDocId(tenantId, instructionDocId);
            // ????????????flag
            MtExtendVO extendQuery = new MtExtendVO();
            extendQuery.setTableName("mt_instruction_doc_attr");
            extendQuery.setKeyId(instructionDocId);
            List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendQuery);
            MtExtendAttrVO extend = extendAttrList.stream().filter(rec -> "SIGN_FLAG".equals(rec.getAttrName())).collect(Collectors.toList()).get(0);
            if (Objects.isNull(extend) || StringUtils.isBlank(extend.getAttrName())) {
                throw new CommonException("???????????????SIGN_FLAG");
            }
            String signFlag = extend.getAttrValue();

            // ????????????
            WmsEventVO event = wmsEventService.createEventWithRequest(tenantId, DISTRIBUTION_EXECUTE);

            // ???????????????????????????
            List<WmsObjectTransactionRequestVO> wmsObjectTransactionRequestList = this.executeTransaction(tenantId, instructionDocId, event, signFlag);

            //???????????????
            lineList.forEach(rec -> processUpdateInstruction(tenantId, event, rec));

            // ????????????
            this.instructionDocUpdate(tenantId, instructionDocId, event.getEventId());
            // ????????????
            List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseList = wmsObjectTransactionRepository.objectTransactionSync(tenantId, wmsObjectTransactionRequestList);
            itfObjectTransactionIfaceService.sendSapMaterialMove(tenantId, wmsObjectTransactionResponseList);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        } finally {
            hmeObjectRecordLockRepository.batchReleaseLock(tenantId , Collections.singletonList(hmeObjectRecordLock) , HmeConstants.ConstantValue.YES);
        }
        return true;
    }

    @Override
    public MtModLocator getRecommendLocator(Long tenantId, String instructionId) {
        return wmsPrepareExecuteRepository.getRecommendLocator(tenantId, instructionId);
    }

}
