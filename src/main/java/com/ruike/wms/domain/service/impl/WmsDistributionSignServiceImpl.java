package com.ruike.wms.domain.service.impl;

import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import com.ruike.hme.domain.repository.HmeObjectRecordLockRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.app.service.WmsEventService;
import com.ruike.wms.domain.entity.WmsObjectTransaction;
import com.ruike.wms.domain.repository.*;
import com.ruike.wms.domain.service.WmsDistributionSignService;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.util.StringCommonUtils;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.exception.CommonException;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlVO10;
import tarzan.inventory.domain.vo.MtContLoadDtlVO4;
import tarzan.inventory.domain.vo.MtContainerVO7;
import tarzan.inventory.domain.vo.MtMaterialLotVO9;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO2;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.InstructionStatus.*;
import static io.tarzan.common.domain.util.MtBaseConstants.ORGANIZATION_TYPE.SITE;

/**
 * 配送签收 服务实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 17:31
 */
@Service
public class WmsDistributionSignServiceImpl implements WmsDistributionSignService {
    private final WmsInstructionDocRepository instructionDocRepository;
    private final WmsDistributionSignRepository wmsDistributionSignRepository;
    private final WmsPrepareExecuteRepository wmsPrepareExecuteRepository;
    private final MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtContainerRepository mtContainerRepository;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final WmsEventService wmsEventService;
    private final MtExtendSettingsRepository mtExtendSettingsRepository;
    private final WmsObjectTransactionRepository wmsObjectTransactionRepository;
    private final WmsTransactionTypeRepository wmsTransactionTypeRepository;
    private final MtInstructionRepository mtInstructionRepository;
    private final MtInstructionDocRepository mtInstructionDocRepository;
    private final WmsInstructionRepository wmsInstructionRepository;
    private final LovAdapter lovAdapter;
    private final MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;
    private final ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;
    private final HmeObjectRecordLockService hmeObjectRecordLockService;
    private final HmeObjectRecordLockRepository hmeObjectRecordLockRepository;

    public WmsDistributionSignServiceImpl(WmsInstructionDocRepository instructionDocRepository, WmsDistributionSignRepository wmsDistributionSignRepository, WmsPrepareExecuteRepository wmsPrepareExecuteRepository, MtContainerLoadDetailRepository mtContainerLoadDetailRepository, MtErrorMessageRepository mtErrorMessageRepository, MtContainerRepository mtContainerRepository, MtMaterialLotRepository mtMaterialLotRepository, WmsEventService wmsEventService, MtExtendSettingsRepository mtExtendSettingsRepository, WmsObjectTransactionRepository wmsObjectTransactionRepository, WmsTransactionTypeRepository wmsTransactionTypeRepository, MtInstructionRepository mtInstructionRepository, MtInstructionDocRepository mtInstructionDocRepository, WmsInstructionRepository wmsInstructionRepository, LovAdapter lovAdapter, MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository, ItfObjectTransactionIfaceService itfObjectTransactionIfaceService, HmeObjectRecordLockService hmeObjectRecordLockService, HmeObjectRecordLockRepository hmeObjectRecordLockRepository) {
        this.instructionDocRepository = instructionDocRepository;
        this.wmsDistributionSignRepository = wmsDistributionSignRepository;
        this.wmsPrepareExecuteRepository = wmsPrepareExecuteRepository;
        this.mtContainerLoadDetailRepository = mtContainerLoadDetailRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.mtContainerRepository = mtContainerRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.wmsEventService = wmsEventService;
        this.mtExtendSettingsRepository = mtExtendSettingsRepository;
        this.wmsObjectTransactionRepository = wmsObjectTransactionRepository;
        this.wmsTransactionTypeRepository = wmsTransactionTypeRepository;
        this.mtInstructionRepository = mtInstructionRepository;
        this.mtInstructionDocRepository = mtInstructionDocRepository;
        this.wmsInstructionRepository = wmsInstructionRepository;
        this.lovAdapter = lovAdapter;
        this.mtModLocatorOrgRelRepository = mtModLocatorOrgRelRepository;
        this.itfObjectTransactionIfaceService = itfObjectTransactionIfaceService;
        this.hmeObjectRecordLockService = hmeObjectRecordLockService;
        this.hmeObjectRecordLockRepository = hmeObjectRecordLockRepository;
    }

    @Override
    @ProcessLovValue
    public WmsDistributionSignDocVO docScan(Long tenantId, String instructionDocNum) {
        // 查询配送单头
        WmsInstructionDocAttrVO doc = instructionDocRepository.selectByDocNum(tenantId, instructionDocNum, WmsConstant.DocType.DISTRIBUTION_DOC);
        WmsCommonUtils.processValidateMessage(tenantId, Objects.isNull(doc), "WMS_MATERIAL_ON_SHELF_0001", "WMS", instructionDocNum);
        WmsCommonUtils.processValidateMessage(tenantId, !StringUtils.containsAny(doc.getInstructionDocStatus(), PREPARE_EXECUTE, PREPARE_COMPLETE, SIGN_EXECUTE),
                "WMS_STOCKTAKE_001", "WMS", "配送单", instructionDocNum, lovAdapter.queryLovMeaning("WMS.DISTRIBUTION_DOC_STATUS", tenantId, doc.getInstructionDocStatus()));
        WmsCommonUtils.processValidateMessage(tenantId, WmsConstant.CONSTANT_N.equals(doc.getSignFlag()), "WMS_DISTRIBUTION_0001", "WMS", instructionDocNum);
        // 查询行信息
        List<WmsDistributionSignLineVO> signList = wmsDistributionSignRepository.selectSignListByDocId(tenantId, doc.getInstructionDocId());
        // 组合返回值
        WmsDistributionSignDocVO signDoc = new WmsDistributionSignDocVO();
        signDoc.setTenantId(tenantId);
        signDoc.setInstructionDocId(doc.getInstructionDocId());
        signDoc.setInstructionDocNum(doc.getInstructionDocNum());
        signDoc.setDemandTime(doc.getDemandTime());
        signDoc.setProdLineCode(doc.getProdLineCode());
        signDoc.setRemark(doc.getRemark());
        signDoc.setWorkcellCode(doc.getWorkcellCode());
        signDoc.setLocatorCode(doc.getToLocatorCode());
        signDoc.setInstructionDocStatus(doc.getInstructionDocStatus());
        signDoc.setLineList(signList);
        return signDoc;
    }

    @Override
    @ProcessLovValue
    public List<WmsInstructionActualDetailVO> detailQuery(Long tenantId, String instructionId) {
        List<WmsInstructionActualDetailVO> list = wmsPrepareExecuteRepository.selectActualDetailByInstId(tenantId, instructionId);
        list = list.stream().sorted((ins1, ins2) -> {
            if (ins1.getInstructionStatus().equals(ins2.getInstructionStatus())) {
                return 0;
            } else if (PREPARE_COMPLETE.equals(ins1.getInstructionStatus())) {
                return -1;
            } else {
                return 1;
            }
        }).collect(Collectors.toList());
        return list;
    }

    /**
     * 对容器数据校验
     *
     * @param containerId 容器ID
     * @param actualList  单据实际行
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/9 02:00:06
     */
    private void validateContainer(String containerId, List<WmsDistributionSignDetailVO> actualList) {
        Long tenantId = actualList.get(0).getTenantId();
        // 查询容器下所有的物料批
        MtContLoadDtlVO10 containerQuery = new MtContLoadDtlVO10();
        containerQuery.setContainerId(containerId);
        containerQuery.setAllLevelFlag(WmsConstant.CONSTANT_Y);
        List<MtContLoadDtlVO4> materialLotList = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, containerQuery);
        materialLotList.forEach(rec -> {
            // 若存在有物料批在勾选行明细上不存在，则报错条码不在勾选的待签收条码列表中
            if (actualList.stream().noneMatch(actual -> actual.getMaterialLotId().equals(rec.getMaterialLotId()))) {
                MtContainer container = mtContainerRepository.containerPropertyGet(tenantId, containerId);
                MtMaterialLot materialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, rec.getMaterialLotId());
                throw new MtException("WMS_DISTRIBUTION_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_DISTRIBUTION_0002"
                        , "WMS", container.getContainerCode(), materialLot.getMaterialLotCode()));
            }
        });
    }

    private void executeTransfer(Long tenantId, String instructionDocId, List<WmsDistributionSignDetailVO> actualList, String eventRequestId) {
        // 筛选出存在容器的数据，按照容器执行库存转移
        Map<String, List<WmsDistributionSignDetailVO>> containerMap = actualList.stream().filter(rec -> StringUtils.isNotBlank(rec.getContainerId())).collect(Collectors.groupingBy(WmsDistributionSignDetailVO::getContainerId));
        containerMap.forEach((containerId, list) -> {
            MtContainerVO7 containerTrf = new MtContainerVO7();
            containerTrf.setContainerId(containerId);
            containerTrf.setEventRequestId(eventRequestId);
            containerTrf.setInstructionDocId(instructionDocId);
            containerTrf.setTargetSiteId(list.get(0).getToSiteId());
            containerTrf.setTargetLocatorId(list.get(0).getToLocatorId());
            mtContainerRepository.containerTransfer(tenantId, containerTrf);
        });

        // 剩下不存在容器的数据，按照物料批执行库存转移
        List<WmsDistributionSignDetailVO> materialLotList = actualList.stream().filter(rec -> StringUtils.isBlank(rec.getContainerId())).collect(Collectors.toList());
        materialLotList.forEach(rec -> {
            MtMaterialLotVO9 materialLotTrf = new MtMaterialLotVO9();
            materialLotTrf.setMaterialLotId(rec.getMaterialLotId());
            materialLotTrf.setEventRequestId(eventRequestId);
            materialLotTrf.setInstructionDocId(instructionDocId);
            materialLotTrf.setTargetSiteId(rec.getToSiteId());
            materialLotTrf.setTargetLocatorId(rec.getToLocatorId());
            mtMaterialLotRepository.materialLotTransfer(tenantId, materialLotTrf);
        });
    }

    private WmsObjectTransactionRequestVO createTransactionRequest(String eventId, String instructionDocId, String instructionId, WmsDistributionSignDetailVO detail, String moveType) {
        WmsObjectTransactionRequestVO request = new WmsObjectTransactionRequestVO();
        request.setTransactionTypeCode(WmsConstant.TransactionTypeCode.WMS_WAREHOUSE_TRAN);
        request.setEventId(eventId);
        request.setMaterialLotId(detail.getMaterialLotId());
        request.setMaterialId(detail.getMaterialId());
        request.setTransactionQty(detail.getPrimaryUomQty());
        request.setLotNumber(detail.getLot());
        request.setTransferLotNumber(detail.getLot());
        request.setTransactionUom(detail.getPrimaryUomCode());
        request.setTransactionTime(new Date());
        request.setTransactionReasonCode(WmsConstant.TransactionReasonCode.DISTRIBUTION_SIGN);
        request.setPlantId(detail.getSiteId());
        request.setWarehouseId(detail.getWarehouseId());
        request.setLocatorId(detail.getLocatorId());
        request.setTransferPlantId(detail.getToSiteId());
        request.setTransferWarehouseId(detail.getToWarehouseId());
        request.setTransferLocatorId(detail.getToLocatorId());
        request.setSourceDocType(WmsConstant.DocType.DISTRIBUTION_DOC);
        request.setSourceDocId(instructionDocId);
        request.setSourceDocLineId(instructionId);
        request.setMoveType(moveType);
        request.setContainerId(detail.getContainerId());
        request.setSoNum(detail.getSoNum());
        request.setSoLineNum(detail.getSoLineNum());
        return request;
    }

    private List<WmsObjectTransactionRequestVO> doSignTransaction(Long tenantId, String instructionDocId, String instructionId, List<WmsDistributionSignDetailVO> actualList, WmsEventVO event) {
        // 4.2 执行库存转移
        // 执行库存转移，分容器和物料批分别执行
        this.executeTransfer(tenantId, instructionDocId, actualList, event.getEventRequestId());
        // 获取库存事务类型数据
        WmsTransactionTypeDTO transactionType = wmsTransactionTypeRepository.getTransactionType(tenantId, WmsConstant.TransactionTypeCode.WMS_WAREHOUSE_TRAN);
        List<WmsObjectTransactionRequestVO> requestList = new ArrayList<>();
        for (WmsDistributionSignDetailVO detail : actualList) {
            // 按照明细行，更新物料批拓展属性 条码状态为SHIPPED
            List<MtExtendVO5> extendList = new ArrayList<>();
            MtExtendVO5 statusAttr = new MtExtendVO5();
            statusAttr.setAttrName("STATUS");
            statusAttr.setAttrValue(WmsConstant.MaterialLotStatus.INSTOCK);
            extendList.add(statusAttr);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", detail.getMaterialLotId(), event.getEventId(), extendList);
            // 组装签收事务列表
            WmsObjectTransactionRequestVO request = this.createTransactionRequest(event.getEventId(), instructionDocId, instructionId, detail, transactionType.getMoveType());
            requestList.add(request);
        }

        // 更新配送单行状态
        MtInstructionVO instructionUpdate = new MtInstructionVO();
        instructionUpdate.setInstructionId(instructionId);
        instructionUpdate.setInstructionStatus(SIGN_COMPLETE);
        instructionUpdate.setEventId(event.getEventId());
        mtInstructionRepository.instructionUpdate(tenantId, instructionUpdate, WmsConstant.CONSTANT_N);
        // 更新配送单行拓展属性上的已签收数量
        BigDecimal signedQty = actualList.stream().map(WmsDistributionSignDetailVO::getActualQty).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<MtExtendVO5> extendList = new ArrayList<>();
        MtExtendVO5 attr = new MtExtendVO5();
        attr.setAttrName("SIGNED_QTY");
        attr.setAttrValue(signedQty.toPlainString());
        extendList.add(attr);
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", instructionId, event.getEventId(), extendList);

        return requestList;
    }

    private void instructionDocUpdate(Long tenantId, String instructionDocId, String eventId) {
        List<WmsInstructionAttrVO> list = wmsInstructionRepository.selectListByDocId(tenantId, instructionDocId);
        String instructionDocStatus;
        int prepareCount = 0, signCount = 0;
        for (WmsInstructionAttrVO ins : list) {
            if (StringCommonUtils.contains(ins.getInstructionStatus(), PREPARE_EXECUTE, RELEASED)) {
                prepareCount += 1;
                break;
            }
            if (SIGN_COMPLETE.equals(ins.getInstructionStatus())) {
                signCount += 1;
            }
        }
        if (prepareCount == 0) {
            if (signCount == list.size()) {
                instructionDocStatus = SIGN_COMPLETE;
            } else {
                instructionDocStatus = SIGN_EXECUTE;
            }
        } else {
            instructionDocStatus = PREPARE_EXECUTE;
        }
        MtInstructionDocDTO2 docUpdate = new MtInstructionDocDTO2();
        docUpdate.setInstructionDocId(instructionDocId);
        docUpdate.setEventId(eventId);
        docUpdate.setInstructionDocStatus(instructionDocStatus);
        mtInstructionDocRepository.instructionDocUpdate(tenantId, docUpdate, WmsConstant.CONSTANT_N);
    }

    /**
     * 通过货位查询站点
     *
     * @param tenantId  租户
     * @param locatorId 货位
     * @return java.lang.String
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/20 11:27:42
     */
    private String getSiteIdByLocatorId(Long tenantId, String locatorId) {
        MtModLocatorOrgRelVO2 siteQuery = new MtModLocatorOrgRelVO2();
        siteQuery.setLocatorId(locatorId);
        siteQuery.setOrganizationType(SITE);
        List<MtModLocatorOrgRelVO3> siteList = mtModLocatorOrgRelRepository.locatorLimitOrganizationQuery(tenantId, siteQuery);
        if (CollectionUtils.isEmpty(siteList)) {
            throw new CommonException("未获取到站点，请检查！");
        }
        return siteList.get(0).getOrganizationId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void execute(Long tenantId, String instructionDocId, List<String> instructionIdList) {
        // 20211202 add by sanfeng.zhang for wenxin.zhang 加锁
        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
        hmeObjectRecordLockDTO.setFunctionName("配送签收");
        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PDA);
        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.DOCUMENT);
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(instructionDocId);
        hmeObjectRecordLockDTO.setObjectRecordId(instructionDocId);
        hmeObjectRecordLockDTO.setObjectRecordCode(mtInstructionDoc != null ? mtInstructionDoc.getInstructionDocNum() : "");
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
        //加锁
        hmeObjectRecordLockRepository.commonLockWo(hmeObjectRecordLock);
        try {
            // 1) 校验码状态为PREPARED的明细数据否存在
            List<WmsDistributionSignDetailVO> detailList = wmsDistributionSignRepository.selectPrepareSignList(tenantId, instructionIdList);
            if (CollectionUtils.isEmpty(detailList)) {
                throw new CommonException("未找到待签收数据，请检查！");
            }
            // 获取目标站点ID
            String toSiteId = this.getSiteIdByLocatorId(tenantId, detailList.get(0).getToLocatorId());
            detailList.forEach(rec -> rec.setToSiteId(toSiteId));

            // 2) 校验勾选行的待签收条码所属容器中装载物料批是否全在待签收条码列表中
            Map<String, List<WmsDistributionSignDetailVO>> containerMap = detailList.stream().filter(rec -> StringUtils.isNotBlank(rec.getContainerId())).collect(Collectors.groupingBy(WmsDistributionSignDetailVO::getContainerId));
            containerMap.forEach(this::validateContainer);
            // 创建请求事件
            WmsEventVO event = wmsEventService.createEventWithRequest(tenantId, WmsConstant.EventType.DISTRIBUTION_SIGN);

            // 4) 进行账务转移及事务记录
            List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
            Map<String, List<WmsDistributionSignDetailVO>> lineMap = detailList.stream().collect(Collectors.groupingBy(WmsDistributionSignDetailVO::getInstructionId));
            lineMap.forEach((instructionId, actualList) -> objectTransactionRequestList.addAll(this.doSignTransaction(tenantId, instructionDocId, instructionId, actualList, event)));

            // 更新单据状态
            this.instructionDocUpdate(tenantId, instructionDocId, event.getEventId());

            // 执行签收事务
            List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
            itfObjectTransactionIfaceService.sendSapMaterialMove(tenantId, wmsObjectTransactionResponseVOS);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        } finally {
            hmeObjectRecordLockRepository.batchReleaseLock(tenantId , Collections.singletonList(hmeObjectRecordLock) , HmeConstants.ConstantValue.YES);
        }
    }
}
