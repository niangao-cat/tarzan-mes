package com.ruike.wms.app.service.impl;

import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import com.ruike.hme.domain.repository.HmeObjectRecordLockRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsInvTransferReceiptService;
import com.ruike.wms.domain.entity.WmsObjectTransaction;
import com.ruike.wms.domain.entity.WmsPfepInertiaLocator;
import com.ruike.wms.domain.entity.WmsStockAllocateSetting;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.*;
import com.ruike.wms.domain.vo.WmsInvTransferObjectTrxVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsInvTransferIssueMapper;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionVO3;
import tarzan.instruction.infra.mapper.MtInstructionDocMapper;
import tarzan.instruction.infra.mapper.MtInstructionMapper;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtPfepInventory;
import tarzan.material.domain.repository.MtPfepInventoryRepository;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import io.choerodon.mybatis.helper.OptionalHelper;

/**
 * 库存调拨接收执行应用服务实现
 *
 * @author jiangling.zheng@hand-china.com 2020-04-27 09:50:00
 */
@Service
public class WmsInvTransferReceiptServiceImpl implements WmsInvTransferReceiptService {

    @Autowired
    private WmsInvTransferIssueMapper wmsInvTransferIssueMapper;

    @Autowired
    MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;

    @Autowired
    private MtInstructionRepository mtLogisticInstructionService;

    @Autowired
    private MtInstructionMapper mtInstructionMapper;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtInstructionDocMapper mtInstructionDocMapper;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private WmsInvTransferIssueRepository wmsInvTransferIssueRepository;

    @Autowired
    private MtMaterialLotRepository materialLotRepository;

    @Autowired
    private WmsStockAllocateSettingRepository wmsStockAllocateSettingRepository;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private WmsDocPrivilegeRepository docPrivilegeRepository;

    @Autowired
    private MtPfepInventoryRepository mtPfepInventoryRepository;

    @Autowired
    private WmsPfepInertiaLocatorRepository wmsPfepInertiaLocatorRepository;

    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;

    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;

    @Override
    @ProcessLovValue(targetField = {"", "docLineList", ""})
    public WmsInvTransferDTO docQuery(Long tenantId, String docBarCode) {
        if (StringUtils.isEmpty(docBarCode)) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "docBarCode", ""));
        }
        // 扫码单据查询
        WmsInvTransferDTO doc = wmsInvTransferIssueRepository.docHeaderQuery(tenantId, docBarCode);
        // 单据存在性校验
        if (doc == null) {
            throw new MtException("WMS_COST_CENTER_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0002", WmsConstant.ConstantValue.WMS, docBarCode, ""));
        }
        // 检验类型必须为  RECEIVE_EXECUTE 或 SEND_RECEIVE_EXECUTE
        if (!WmsConstant.DocType.RECEIVE_EXECUTE.equals(doc.getInstructionDocType()) &&
                !WmsConstant.DocType.SEND_RECEIVE_EXECUTE.equals(doc.getInstructionDocType())) {
            throw new MtException("WMS_INV_TRANSFER_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0003", WmsConstant.ConstantValue.WMS, docBarCode, ""));
        }
        // 单据状态校验
        if (WmsConstant.DocType.SEND_RECEIVE_EXECUTE.equals(doc.getInstructionDocType())) {
            if (!WmsConstant.DocStatus.SEND_OUT_COMPLETE.equals(doc.getInstructionDocStatus()) &&
                    !WmsConstant.DocStatus.RECEIVE_EXECUTE.equals(doc.getInstructionDocStatus())) {
                throw new MtException("WMS_INV_TRANSFER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0005", WmsConstant.ConstantValue.WMS, docBarCode, ""));
            }
        }
        // 单据行查询
        List<WmsInvTransferDTO2> docLineList =
                wmsInvTransferIssueRepository.docLineQuery(tenantId, doc.getInstructionDocId(),
                        null, WmsConstant.TransferType.RECEIPT);
        if (CollectionUtils.isEmpty(docLineList)) {
            throw new MtException("WMS_INV_TRANSFER_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0029", WmsConstant.ConstantValue.WMS, docBarCode));
        }
        //增加推荐货位逻辑
        for(WmsInvTransferDTO2 wmsInvTransferDTO2:docLineList){
            String locatorRecomMode = mtInstructionMapper.getMode(tenantId,
                    doc.getSiteId(),
                    wmsInvTransferDTO2.getMaterialId(),
                    wmsInvTransferDTO2.getToWarehouseId());
            if(StringUtils.isBlank(locatorRecomMode)){
                wmsInvTransferDTO2.setRecommendLocatorCode("");
            }else if(locatorRecomMode.equals("POSITION")){
                MtPfepInventoryVO mtPfepInventoryVO = new MtPfepInventoryVO();
                mtPfepInventoryVO.setMaterialId(wmsInvTransferDTO2.getMaterialId());
                mtPfepInventoryVO.setSiteId(doc.getSiteId());
                mtPfepInventoryVO.setOrganizationType("LOCATOR");
                mtPfepInventoryVO.setOrganizationId(wmsInvTransferDTO2.getToWarehouseId());
                MtPfepInventory mtPfepInventory = mtPfepInventoryRepository.pfepInventoryGet(tenantId,mtPfepInventoryVO);
                if(StringUtils.isBlank(mtPfepInventory.getStockLocatorId())){
                    wmsInvTransferDTO2.setRecommendLocatorCode("");
                }else{
                    wmsInvTransferDTO2.setRecommendLocatorCode(mtModLocatorRepository.selectByPrimaryKey(mtPfepInventory.getStockLocatorId()).getLocatorCode());
                }
            }else if(locatorRecomMode.equals("INERTIA")){
                WmsPfepInertiaLocator wmsPfepInertiaLocator = new WmsPfepInertiaLocator();
                wmsPfepInertiaLocator.setTenantId(tenantId);
                wmsPfepInertiaLocator.setMaterialId(wmsInvTransferDTO2.getMaterialId());
                wmsPfepInertiaLocator.setSiteId(doc.getSiteId());
                wmsPfepInertiaLocator.setWarehouseId(wmsInvTransferDTO2.getToWarehouseId());
                List<WmsPfepInertiaLocator> wmsPfepInertiaLocatorList = wmsPfepInertiaLocatorRepository.select(wmsPfepInertiaLocator);
                if(wmsPfepInertiaLocatorList.size() !=0){
                    wmsInvTransferDTO2.setRecommendLocatorCode(mtModLocatorRepository.selectByPrimaryKey(wmsPfepInertiaLocatorList.get(0).getLocatorId()).getLocatorCode());
                }else{
                    wmsInvTransferDTO2.setRecommendLocatorCode("");
                }
            }
        }
        Long userId = DetailsHelper.getUserDetails().getUserId();
        docPrivilegeRepository.isWarehousePrivileged(tenantId, WmsWarehousePrivilegeQueryDTO.builder()
                .userId(userId)
                .locatorId(docLineList.get(0).getFromWarehouseId())
                .docType(doc.getInstructionDocType())
                .operationType(WmsConstant.InstructionStatus.EXECUTE)
                .locationType(WmsConstant.LocatorType.FROM_LOCATOR)
                .build());
        docPrivilegeRepository.isWarehousePrivileged(tenantId, WmsWarehousePrivilegeQueryDTO.builder()
                .userId(userId)
                .locatorId(docLineList.get(0).getToWarehouseId())
                .docType(doc.getInstructionDocType())
                .operationType(WmsConstant.InstructionStatus.EXECUTE)
                .locationType(WmsConstant.LocatorType.TO_LOCATOR)
                .build());

        if (WmsConstant.DocType.RECEIVE_EXECUTE.equals(doc.getInstructionDocType())) {
            WmsInvTransferDTO2 dto = docLineList.get(0);
            // 校验来源仓库与目标仓库
            // 2020-10-05 王康说取值FromWarehouseId 为仓库字段，修改人刘克金
            WmsStockAllocateSetting setting = wmsStockAllocateSettingRepository.selectOne(new WmsStockAllocateSetting() {{
                setSiteId(dto.getSiteId());
                setFromLocatorId(dto.getFromWarehouseId());
                setToLocatorId(dto.getToWarehouseId());
                setApproveSetting("Y");
                setTenantId(tenantId);
            }});
            if (Objects.isNull(setting)) {
                // 单据状态校验(新建、执行中)
                if (!WmsConstant.DocStatus.NEW.equals(doc.getInstructionDocStatus()) && !WmsConstant.DocStatus.RECEIVE_EXECUTE.equals(doc.getInstructionDocStatus())) {
                    throw new MtException("WMS_INV_TRANSFER_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_INV_TRANSFER_0002", WmsConstant.ConstantValue.WMS, docBarCode, ""));
                }
            } else {
                if (!StringUtils.equalsAny(doc.getInstructionDocStatus(), WmsConstant.DocStatus.APPROVED, WmsConstant.DocStatus.RECEIVE_EXECUTE)) {
                    throw new MtException("WMS_INV_TRANSFER_0031", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_INV_TRANSFER_0031", WmsConstant.ConstantValue.WMS, docBarCode, ""));
                }
            }
        }
        doc.setDocLineList(docLineList);
        return doc;
    }

    @Override
    public MtModLocator locatorQuery(Long tenantId, WmsInvTransferDTO8 dto) {
        if (StringUtils.isEmpty(dto.getLocatorCode())) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "locatorCode", ""));
        }
        if (StringUtils.isEmpty(dto.getToWarehouseId())) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "toWarehouseId", ""));
        }
        // 扫码货位查询
        MtModLocator locator = mtModLocatorRepository.selectOne(new MtModLocator() {{
            setLocatorCode(dto.getLocatorCode());
            setTenantId(tenantId);
        }});
        if (ObjectUtils.isEmpty(locator)) {
            throw new MtException("WMS_INV_TRANSFER_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0019", WmsConstant.ConstantValue.WMS, dto.getLocatorCode()));
        }
        String parentLocatorId = locator.getParentLocatorId();
        // 单据存在性校验
        if (StringUtils.isBlank(parentLocatorId) || !StringUtils.equals(parentLocatorId, dto.getToWarehouseId())) {
            throw new MtException("WMS_INV_TRANSFER_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0015", WmsConstant.ConstantValue.WMS));
        }
        return locator;
    }

    @Override
    @ProcessLovValue(targetField = {"", "", ""})
    public List<WmsCostCtrMaterialDTO3> containerOrMaterialLotQuery(Long tenantId, WmsInvTransferDTO3 dto) {
        return wmsInvTransferIssueRepository.containerOrMaterialLotQuery(tenantId, WmsConstant.TransferType.RECEIPT, dto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WmsInvTransferDTO execute(Long tenantId, WmsInvTransferDTO5 docDto) {
        //20211202 add by sanfeng.zhang for wenxin.zhang 对单据进行加锁
        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
        hmeObjectRecordLockDTO.setFunctionName("库存调拨接收");
        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PDA);
        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.DOCUMENT);
        hmeObjectRecordLockDTO.setObjectRecordId(docDto.getInstructionDocId());
        hmeObjectRecordLockDTO.setObjectRecordCode(docDto.getInstructionDocNum());
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
        //加锁
        hmeObjectRecordLockRepository.commonLockWo(hmeObjectRecordLock);

        WmsInvTransferDTO wmsInvTransferDTO = null;
        try {
            if (CollectionUtils.isEmpty(docDto.getMaterialLotList())) {
                throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "MaterialLotList"));
            }
            if (CollectionUtils.isEmpty(docDto.getDocLineList())) {
                throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "docLineList"));
            }
            List<WmsInvTransferDTO2> docLineList = docDto.getDocLineList();
            List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
            // 获取所有扫描过的条码信息
            List<WmsCostCtrMaterialDTO3> barDtoList = docDto.getMaterialLotList();
            // 创建库存调拨请求事件
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "STOCK_ALLOCATION_REQUISTION_EXECUTE");
            for (WmsInvTransferDTO2 docLine : docLineList) {
                // 获取执行物料行下本次扫描的条码
                List<WmsCostCtrMaterialDTO3> lotDtoList = barDtoList.stream().filter(item -> StringUtils.equals(docLine.getMaterialId(), item.getMaterialId()) &&
                        ((StringUtils.equals(Optional.ofNullable(docLine.getMaterialVersion()).orElse(""),
                                Optional.ofNullable(item.getMaterialVersion()).orElse(""))) || StringUtils.isEmpty(docLine.getMaterialVersion()))).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(lotDtoList)) {
                    continue;
                }
                // 创建库存调拨发出事件
                MtEventCreateVO eventCreate = new MtEventCreateVO();
            /*eventCreate.setEventRequestId(eventRequestId);
            eventCreate.setLocatorId(docLine.getFromLocatorId());
            eventCreate.setEventTypeCode("STOCK_ALLOCATION_OUT");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);*/
                // 创建库存调拨发出接收事件
                eventCreate.setEventRequestId(eventRequestId);
//            eventCreate.setLocatorId(docLine.getToLocatorId());
                eventCreate.setEventTypeCode("STOCK_ALLOCATION_IN");
//            eventCreate.setParentEventId(eventId);
                String eventReceiptId = mtEventRepository.eventCreate(tenantId, eventCreate);
                String transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_WAREHOUSE_TRAN;
                WmsTransactionType type = wmsTransactionTypeRepository.selectOne(new WmsTransactionType() {{
                    setTenantId(tenantId);
                    setTransactionTypeCode(transactionTypeCode);
                }});
                for (WmsCostCtrMaterialDTO3 lotDto : lotDtoList) {
                    // 步骤1：本次执行条码记录成本中心领料事务
                    WmsInvTransferObjectTrxVO objectTrxVO = new WmsInvTransferObjectTrxVO();
                    objectTrxVO.setEventId(eventReceiptId);
                    objectTrxVO.setTransactionTypeCode(transactionTypeCode);
                    objectTrxVO.setTransactionTime(new Date());
                    objectTrxVO.setTransactionQty(lotDto.getPrimaryUomQty());
                    objectTrxVO.setSourceDocId(docDto.getInstructionDocId());
                    objectTrxVO.setSourceDocLineId(docLine.getInstructionId());
                    objectTrxVO.setSourceDocNum(docDto.getInstructionDocNum());
                    objectTrxVO.setSourceDocLineNum(docLine.getInstructionNum());
                    objectTrxVO.setSourceDocType(docDto.getInstructionDocType());
                    objectTrxVO.setTransferPlantId(docLine.getToSiteId());
                    objectTrxVO.setTransferPlantCode(docLine.getToSiteCode());
                    objectTrxVO.setTransferWarehouseId(docLine.getToWarehouseId());
                    objectTrxVO.setTransferWarehouseCode(docLine.getToWarehouseCode());
                    objectTrxVO.setContainerId(lotDto.getContainerId());
                    objectTrxVO.setContainerCode(lotDto.getContainerCode());
                    objectTrxVO.setTransferLocatorId(lotDto.getCacheLocatorId());
                    objectTrxVO.setTransferLocatorCode(lotDto.getCacheLocatorCode());
                    objectTrxVO.setMoveType(type.getMoveType());
                    objectTrxVO.setRemark("调拨单接收执行");
                    wmsInvTransferIssueRepository.addObjectTransaction(objectTrxVO, lotDto, objectTransactionRequestList);
                    // 步骤2：更新条码（发出事件）
                /*MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotVO2.setMaterialLotId(lotDto.getMaterialLotId());
                mtMaterialLotVO2.setLocatorId(lotDto.getCacheLocatorId());
                // 更新条码（接收事件）
                mtMaterialLotVO2.setEventId(eventReceiptId);
                materialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, WmsConstant.ConstantValue.NO);*/
                    // 步骤3：建立条码与执行单据行关系
                    MtModLocator locator = mtModLocatorRepository.selectByPrimaryKey(lotDto.getCacheLocatorId());
                    MtInstructionVO3 logisticInstructionVO3 = new MtInstructionVO3();
                    List<MtInstructionVO3.MaterialLotList> list = new ArrayList<>();
                    MtInstructionVO3.MaterialLotList materialLotList = new MtInstructionVO3.MaterialLotList();
                    materialLotList.setMaterialLotId(lotDto.getMaterialLotId());
                    materialLotList.setQty(lotDto.getPrimaryUomQty().doubleValue());
                    materialLotList.setContainerId(lotDto.getContainerId());
                    materialLotList.setFromLocatorId(lotDto.getWarehouseId());
                    materialLotList.setToLocatorId(locator.getParentLocatorId());
                    materialLotList.setUomId(lotDto.getPrimaryUomId());
                    list.add(materialLotList);
                    logisticInstructionVO3.setInstructionId(docLine.getInstructionId());
                    logisticInstructionVO3.setMaterialLotMessageList(list);
                    logisticInstructionVO3.setEventRequestId(eventRequestId);
                    mtLogisticInstructionService.instructionExecute(tenantId, logisticInstructionVO3);
                /*// 步骤6：扣减来源库存现有量
                //更新物料库存
                MtInvOnhandQuantityVO9 condition = new MtInvOnhandQuantityVO9();
                condition.setSiteId(docLine.getFromSiteId());
                condition.setLocatorId(lotDto.getLocatorId());
                condition.setMaterialId(lotDto.getMaterialId());
                condition.setLotCode(lotDto.getLot());
                condition.setChangeQuantity(lotDto.getPrimaryUomQty().doubleValue());
                condition.setEventId(eventId);
                condition.setOwnerId("");
                condition.setOwnerType("");
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, condition);
                // 步骤7：增加目标库存现有量
                //更新物料库存
                condition.setSiteId(docLine.getToSiteId());
                condition.setLocatorId(lotDto.getCacheLocatorId());
                condition.setEventId(eventReceiptId);
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, condition);*/
                }
                // 获取物料批条码
                List<WmsCostCtrMaterialDTO3> lotList = lotDtoList.stream().filter(item -> item.getContainerId() == null).collect(Collectors.toList());
                // 物料批条码转移
                if (CollectionUtils.isNotEmpty(lotList)) {
                    for (WmsCostCtrMaterialDTO3 lot : lotList) {
                        MtMaterialLotVO9 lotDto = new MtMaterialLotVO9();
                        lotDto.setMaterialLotId(lot.getMaterialLotId());
                        lotDto.setTargetSiteId(docLine.getToSiteId());
                        lotDto.setTargetLocatorId(lot.getCacheLocatorId());
                        lotDto.setEventRequestId(eventRequestId);
                        materialLotRepository.materialLotTransfer(tenantId, lotDto);
                    }
                }
                // 步骤4：更新成本中心领料单行数据
                MtInstruction mtInstruction = mtInstructionMapper.selectByPrimaryKey(docLine.getInstructionId());
                if (docLine.getQuantity().compareTo(Optional.ofNullable(docLine.getActualQty()).orElse(BigDecimal.ZERO)) <= 0) {
                    mtInstruction.setInstructionStatus(WmsConstant.DocStatus.RECEIVE_COMPLETE);
                } else {
                    mtInstruction.setInstructionStatus(WmsConstant.InstructionStatus.RECEIVE_EXECUTE);
                }
                OptionalHelper.optional(Collections.singletonList(MtInstruction.FIELD_INSTRUCTION_STATUS));
                mtInstructionMapper.updateOptional(mtInstruction);
            }
            // 获取容器条码
            List<String> conIds = barDtoList.stream().filter(item -> item.getContainerId() != null)
                    .map(WmsCostCtrMaterialDTO3::getContainerId).distinct().collect(Collectors.toList());
            // 容器条码转移
            if (CollectionUtils.isNotEmpty(conIds)) {
                for (String containerId : conIds) {
                    // 获取容器转移的目标货位
                    List<WmsCostCtrMaterialDTO3> ctrMaterialDTO3List = barDtoList.stream().filter(item -> containerId.equals(item.getContainerId()))
                            .collect(Collectors.toList());
                    String cacheLocatorId = ctrMaterialDTO3List.get(0).getCacheLocatorId();
                    String toSiteId = docLineList.get(0).getToSiteId();
                    MtContainerVO7 conDto = new MtContainerVO7();
                    conDto.setContainerId(containerId);
                    conDto.setTargetSiteId(toSiteId);
                    conDto.setTargetLocatorId(cacheLocatorId);
                    conDto.setEventRequestId(eventRequestId);
                    mtContainerRepository.containerTransfer(tenantId, conDto);
                }
            }
            // 步骤5：更新成本中心领料单头状态
            List<WmsInvTransferDTO2> docLineTempList =
                    wmsInvTransferIssueMapper.selectDocLineCondition(tenantId, docDto.getInstructionDocId(),
                            null, WmsConstant.TransferType.RECEIPT);
            MtInstructionDoc mtInstructionDoc = mtInstructionDocMapper.selectByPrimaryKey(docDto.getInstructionDocId());
            if (docLineTempList.stream().allMatch(item -> WmsConstant.InstructionStatus.RECEIVE_COMPLETE.equals(item.getInstructionStatus()))) {
                mtInstructionDoc.setInstructionDocStatus(WmsConstant.InstructionStatus.RECEIVE_COMPLETE);
            } else {
                mtInstructionDoc.setInstructionDocStatus(WmsConstant.InstructionStatus.RECEIVE_EXECUTE);
            }
            OptionalHelper.optional(Collections.singletonList(MtInstructionDoc.FIELD_INSTRUCTION_DOC_STATUS));
            mtInstructionDocMapper.updateOptional(mtInstructionDoc);
            // 返回单据信息
            wmsInvTransferDTO = wmsInvTransferIssueRepository.docHeaderQuery(tenantId, docDto.getInstructionDocNum());

            //批量查询销售订单号/销售订单行号 add by yuchao.wang for yiwei.zhou at 2020.10.3
            if (CollectionUtils.isNotEmpty(objectTransactionRequestList)) {
                List<String> kidList = objectTransactionRequestList.stream().map(WmsObjectTransactionRequestVO::getMaterialLotId).distinct().collect(Collectors.toList());
                MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
                mtExtendVO1.setTableName(WmsConstant.AttrTable.MT_MATERIAL_LOT_ATTR);
                mtExtendVO1.setKeyIdList(kidList);
                List<MtExtendVO5> attrs = new ArrayList<>();
                MtExtendVO5 extend1 = new MtExtendVO5();
                extend1.setAttrName(WmsConstant.MaterialLotAttr.SO_NUM);
                attrs.add(extend1);
                MtExtendVO5 extend2 = new MtExtendVO5();
                extend2.setAttrName(WmsConstant.MaterialLotAttr.SO_LINE_NUM);
                attrs.add(extend2);
                mtExtendVO1.setAttrs(attrs);
                List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
                if (CollectionUtils.isNotEmpty(extendAttrList)) {
                    Map<String, String> extendAttrMap = new HashMap<String, String>();
                    extendAttrList.forEach(item -> extendAttrMap.put(item.getKeyId() + "-" + item.getAttrName(), item.getAttrValue()));

                    //赋值到API参数
                    for (WmsObjectTransactionRequestVO transactionRequest : objectTransactionRequestList) {
                        transactionRequest.setSoNum(extendAttrMap.get(transactionRequest.getMaterialLotId() + "-" + WmsConstant.MaterialLotAttr.SO_NUM));
                        transactionRequest.setSoLineNum(extendAttrMap.get(transactionRequest.getMaterialLotId() + "-" + WmsConstant.MaterialLotAttr.SO_LINE_NUM));
                    }
                }
            }
            // 本次执行条码记录成本中心领料事务
            List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
            itfObjectTransactionIfaceService.sendSapMaterialMove(tenantId, wmsObjectTransactionResponseVOS);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        } finally {
            hmeObjectRecordLockRepository.batchReleaseLock(tenantId , Collections.singletonList(hmeObjectRecordLock) , HmeConstants.ConstantValue.YES);
        }
        return wmsInvTransferDTO;
    }

    @Override
    @ProcessLovValue(targetField = {"", "barDtoList", ""})
    public WmsInvTransferDTO7 docDetailQuery(Long tenantId, WmsInvTransferDTO6 dto) {
        return wmsInvTransferIssueRepository.docDetailQuery(tenantId, WmsConstant.TransferType.RECEIPT, dto);
    }

    @Override
    public WmsInvTransferDTO7 deleteMaterialLot(Long tenantId, WmsInvTransferDTO6 dto) {
        return wmsInvTransferIssueRepository.docDetailQuery(tenantId, WmsConstant.TransferType.RECEIPT, dto);
    }
}
