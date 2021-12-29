package com.ruike.wms.app.service.impl;

import com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO;
import com.ruike.itf.domain.repository.ItfSoDeliveryChanOrPostIfaceRepository;
import com.ruike.wms.api.dto.WmsSoDeliveryDetailQueryDTO;
import com.ruike.wms.api.dto.WmsSoDeliveryQueryDTO;
import com.ruike.wms.api.dto.WmsSoDeliverySubmitDTO;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.app.service.WmsEventService;
import com.ruike.wms.app.service.WmsSoDeliveryDocService;
import com.ruike.wms.app.service.WmsSoDeliveryLineService;
import com.ruike.wms.domain.repository.WmsInstructionRepository;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsSoDeliveryDocRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsSoDeliveryDetailMapper;
import com.ruike.wms.infra.mapper.WmsSoDeliveryLineMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtCommonExtendVO5;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import io.tarzan.common.domain.vo.MtExtendVO5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO6;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO15;
import tarzan.inventory.domain.vo.MtMaterialLotVO16;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.NO;
import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.WMS;
import static com.ruike.wms.infra.constant.WmsConstant.DocStatus.CANCEL;
import static com.ruike.wms.infra.constant.WmsConstant.DocStatus.NEW;
import static com.ruike.wms.infra.constant.WmsConstant.EventType.*;
import static com.ruike.wms.infra.constant.WmsConstant.InspectionDocType.NO_SO_DELIVERY;
import static com.ruike.wms.infra.constant.WmsConstant.InstructionStatus.*;
import static com.ruike.wms.infra.constant.WmsConstant.Profile.WMS_SO_DELIVERY_PREPARE_FLAG;

/**
 * <p>
 * 出货单单据 服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 15:25
 */
@Service
@Slf4j
class WmsSoDeliveryDocServiceImpl implements WmsSoDeliveryDocService {
    private final WmsSoDeliveryDocRepository wmsSoDeliveryDocRepository;
    private final WmsEventService wmsEventService;
    private final MtInstructionDocRepository instructionDocRepository;
    private final MtInstructionRepository instructionRepository;
    private final MtExtendSettingsRepository mtExtendSettingsRepository;
    private final ProfileClient profileClient;
    private final WmsSoDeliveryLineService wmsSoDeliveryLineService;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final WmsInstructionRepository wmsInstructionRepository;
    private final MtInstructionRepository mtInstructionRepository;
    private final MtEventRequestRepository mtEventRequestRepository;
    private final MtEventRepository mtEventRepository;
    private final MtMaterialLotRepository materialLotRepository;
    private final WmsSoDeliveryLineMapper wmsSoDeliveryLineMapper;
    private final WmsSoDeliveryDetailMapper wmsSoDeliveryDetailMapper;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final WmsObjectTransactionRepository wmsObjectTransactionRepository;
    private final MtUomRepository mtUomRepository;
    private final MtModLocatorRepository mtModLocatorRepository;
    private final WmsTransactionTypeRepository wmsTransactionTypeRepository;
    private final ItfSoDeliveryChanOrPostIfaceRepository itfSoDeliveryChanOrPostIfaceRepository;

    private String prepareFlagProfile;

    public WmsSoDeliveryDocServiceImpl(WmsSoDeliveryDocRepository wmsSoDeliveryDocRepository, WmsEventService wmsEventService, MtInstructionDocRepository instructionDocRepository, MtExtendSettingsRepository mtExtendSettingsRepository, ProfileClient profileClient, WmsSoDeliveryLineService wmsSoDeliveryLineService, MtErrorMessageRepository mtErrorMessageRepository, WmsInstructionRepository wmsInstructionRepository, MtInstructionRepository mtInstructionRepository, MtEventRequestRepository mtEventRequestRepository, MtEventRepository mtEventRepository, MtMaterialLotRepository materialLotRepository, WmsSoDeliveryLineMapper wmsSoDeliveryLineMapper, WmsSoDeliveryDetailMapper wmsSoDeliveryDetailMapper, MtMaterialLotRepository mtMaterialLotRepository, WmsObjectTransactionRepository wmsObjectTransactionRepository, MtUomRepository mtUomRepository, MtModLocatorRepository mtModLocatorRepository, WmsTransactionTypeRepository wmsTransactionTypeRepository, MtInstructionRepository instructionRepository, ItfSoDeliveryChanOrPostIfaceRepository itfSoDeliveryChanOrPostIfaceRepository) {
        this.wmsSoDeliveryDocRepository = wmsSoDeliveryDocRepository;
        this.wmsEventService = wmsEventService;
        this.instructionDocRepository = instructionDocRepository;
        this.mtExtendSettingsRepository = mtExtendSettingsRepository;
        this.profileClient = profileClient;
        this.wmsSoDeliveryLineService = wmsSoDeliveryLineService;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.wmsInstructionRepository = wmsInstructionRepository;
        this.mtInstructionRepository = mtInstructionRepository;
        this.mtEventRequestRepository = mtEventRequestRepository;
        this.mtEventRepository = mtEventRepository;
        this.materialLotRepository = materialLotRepository;
        this.wmsSoDeliveryLineMapper = wmsSoDeliveryLineMapper;
        this.wmsSoDeliveryDetailMapper = wmsSoDeliveryDetailMapper;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.wmsObjectTransactionRepository = wmsObjectTransactionRepository;
        this.mtUomRepository = mtUomRepository;
        this.mtModLocatorRepository = mtModLocatorRepository;
        this.wmsTransactionTypeRepository = wmsTransactionTypeRepository;
        this.instructionRepository = instructionRepository;
        this.itfSoDeliveryChanOrPostIfaceRepository = itfSoDeliveryChanOrPostIfaceRepository;
    }

    @Override
    @ProcessLovValue
    public Page<WmsSoDeliveryDocVO> pageList(Long tenantId, PageRequest pageRequest, WmsSoDeliveryQueryDTO dto) {
        return PageHelper.doPage(pageRequest, () -> wmsSoDeliveryDocRepository.selectListByQueryCondition(tenantId, dto));
    }

    private void insertOrUpdate(Long tenantId, WmsSoDeliverySubmitDTO dto) {
        // 更新单据
        MtInstructionDocDTO2 docBuilder = new MtInstructionDocDTO2();
        docBuilder.setInstructionDocType(dto.getInstructionDocType());
        docBuilder.setSiteId(dto.getSiteId());
        docBuilder.setCustomerId(dto.getCustomerId());
        docBuilder.setCustomerSiteId(dto.getCustomerSiteId());
        docBuilder.setExpectedArrivalTime(dto.getExpectedArrivalTime());
        docBuilder.setDemandTime(dto.getDemandTime());
        docBuilder.setRemark(dto.getRemark());
        docBuilder.setEventRequestId(dto.getEventRequestId());
        if (StringUtils.isNotBlank(dto.getInstructionDocId())) {
            docBuilder.setEventId(dto.getEventId());
            docBuilder.setInstructionDocId(dto.getInstructionDocId());
        }

        List<MtInstruction> mtInstructionList = mtInstructionRepository.selectByCondition(Condition.builder(MtInstruction.class)
                .andWhere(Sqls.custom().andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID, dto.getInstructionDocId())).build());

        boolean result = mtInstructionList.stream().filter(item -> !"CANCEL".equals(item.getInstructionStatus())).allMatch(item -> "RELEASED".equals(item.getInstructionStatus()));
        boolean result3 = mtInstructionList.stream().filter(item -> !"CANCEL".equals(item.getInstructionStatus())).allMatch(item -> "PREPARE_COMPLETE".equals(item.getInstructionStatus()));
        if (result) {
            docBuilder.setInstructionDocStatus("RELEASED");
        } else if (result3) {
            docBuilder.setInstructionDocStatus("PREPARE_COMPLETE");
        } else {
            docBuilder.setInstructionDocStatus("PREPARE_EXECUTE");
        }

        MtInstructionDocVO3 doc = instructionDocRepository.instructionDocUpdate(tenantId, docBuilder, NO);
        String instructionDocId = doc.getInstructionDocId();
        dto.setInstructionDocId(instructionDocId);

        // 更新拓展字段
        List<MtExtendVO5> attrList = new ArrayList<>(1);
//        MtExtendVO5 prepareFlag = new MtExtendVO5();
//        prepareFlag.setAttrName("PREPARE_FLAG");
//        prepareFlag.setAttrValue(this.prepareFlagProfile);
//        attrList.add(prepareFlag);
        MtExtendVO5 sourceSystem = new MtExtendVO5();
        sourceSystem.setAttrName("SOURCE_SYSTEM");
        sourceSystem.setAttrValue(WMS);
        attrList.add(sourceSystem);
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr", dto.getInstructionDocId(), dto.getEventId(), attrList);
    }

    /**
     * 提交前验证
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 09:35:12
     */
    private void validationPreSave(Long tenantId, String instructionDocId) {
        MtInstructionDoc doc = instructionDocRepository.selectByPrimaryKey(instructionDocId);
        if (Objects.isNull(doc)) {
            throw new MtException("MT_INVENTORY_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0022", "INVENTORY"));
        }
        if (!"RELEASED".equals(doc.getInstructionDocStatus()) && !"PREPARE_EXECUTE".equals(doc.getInstructionDocStatus())) {
            throw new MtException("WX.WMS_SO_DELIVERY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WX.WMS_SO_DELIVERY_0001", "WMS", doc.getInstructionDocNum()));
        }
    }

    /**
     * 下达前验证
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 09:35:12
     */
    private void validationPreRelease(Long tenantId, String instructionDocId) {
        MtInstructionDoc doc = instructionDocRepository.selectByPrimaryKey(instructionDocId);
        if (Objects.isNull(doc)) {
            throw new MtException("MT_INVENTORY_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0022", "INVENTORY"));
        }
        if (!NEW.equals(doc.getInstructionDocStatus())) {
            throw new MtException("WMS_SO_DELIVERY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_SO_DELIVERY_0001", "WMS", doc.getInstructionDocNum()));
        }
    }

    /**
     * 过账前验证
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 09:35:12
     */
    private void validationPreConfirm(Long tenantId, String instructionDocId) {
        MtInstructionDoc doc = instructionDocRepository.selectByPrimaryKey(instructionDocId);
        if (Objects.isNull(doc)) {
            throw new MtException("MT_INVENTORY_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0022", "INVENTORY"));
        }
        if (!PREPARE_COMPLETE.equals(doc.getInstructionDocStatus())) {
            throw new MtException("WX.WMS_SO_DELIVERY_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WX.WMS_SO_DELIVERY_0006", "WMS", doc.getInstructionDocNum()));
        }
    }


    /**
     * 取消头前验证
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 09:35:12
     */
    private void validationPreCacle(Long tenantId, String instructionDocId) {
        MtInstructionDoc doc = instructionDocRepository.selectByPrimaryKey(instructionDocId);
        if (Objects.isNull(doc)) {
            throw new MtException("MT_INVENTORY_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0022", "INVENTORY"));
        }
        if (!RELEASED.equals(doc.getInstructionDocStatus())) {
            throw new MtException("WX.WMS_SO_DELIVERY_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WX.WMS_SO_DELIVERY_0004", "WMS", doc.getInstructionDocNum()));
        }
    }

    /**
     * 下达取消前验证
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 09:35:12
     */
    private void validationPreReleaseCancel(Long tenantId, String instructionDocId) {
        MtInstructionDoc doc = instructionDocRepository.selectByPrimaryKey(instructionDocId);
        if (Objects.isNull(doc)) {
            throw new MtException("MT_INVENTORY_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0022", "INVENTORY"));
        }
        if (!RELEASED.equals(doc.getInstructionDocStatus())) {
            throw new MtException("WMS_SO_DELIVERY_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_SO_DELIVERY_0002", "WMS", doc.getInstructionDocNum()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @ProcessLovValue
    public WmsSoDeliveryDocVO submit(Long tenantId, WmsSoDeliverySubmitDTO dto) {
        if (StringUtils.isNotBlank(dto.getInstructionDocId())) {
            this.validationPreSave(tenantId, dto.getInstructionDocId());
        }
//        prepareFlagProfile = profileClient.getProfileValueByOptions(tenantId, null, null, WMS_SO_DELIVERY_PREPARE_FLAG);
        // 创建事件
        WmsEventVO event = wmsEventService.createEventWithRequest(tenantId, StringUtils.isBlank(dto.getInstructionDocId()) ? SO_DELIVERY_CREATE : SO_DELIVERY_UPDATE);
        dto.setEventId(event.getEventId());
        dto.setEventRequestId(event.getEventRequestId());

        // 创建或更新行以及更新拓展字段
        if (CollectionUtils.isNotEmpty(dto.getLineList())) {
            wmsSoDeliveryLineService.batchInsertOrUpdate(tenantId, dto);
        }

        // 创建或更新单据以及更新拓展字段
        this.insertOrUpdate(tenantId, dto);
        // 组建返回值
        WmsSoDeliveryQueryDTO condition = new WmsSoDeliveryQueryDTO();
        condition.setInstructionDocId(dto.getInstructionDocId());
        List<WmsSoDeliveryDocVO> list = wmsSoDeliveryDocRepository.selectListByQueryCondition(tenantId, condition);
        //调用接口[soDeliveryChangeOrPostIface]
        List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostList = new ArrayList<>();
        ItfSoDeliveryChanOrPostDTO itfSoDeliveryChanOrPostDTO = new ItfSoDeliveryChanOrPostDTO();
        itfSoDeliveryChanOrPostDTO.setType("CHANGE");
        itfSoDeliveryChanOrPostDTO.setInstructionDocId(dto.getInstructionDocId());
        itfSoDeliveryChanOrPostDTO.setInstructionId(dto.getLineList().get(0).getInstructionId());
        itfSoDeliveryChanOrPostDTO.setChangeQty(dto.getLineList().get(0).getQuantity());
        itfSoDeliveryChanOrPostDTO.setWarehouseId(dto.getLineList().get(0).getFromLocatorId());
        itfSoDeliveryChanOrPostDTO.setLReturnStatus("");
        itfSoDeliveryChanOrPostList.add(itfSoDeliveryChanOrPostDTO);
        List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostDTOS = itfSoDeliveryChanOrPostIfaceRepository.soDeliveryChangeOrPostIface(tenantId,itfSoDeliveryChanOrPostList);
        if(CollectionUtils.isNotEmpty(itfSoDeliveryChanOrPostDTOS) && "E".equals(itfSoDeliveryChanOrPostDTOS.get(0).getStatus())){
            log.info("<<------------------------------"+itfSoDeliveryChanOrPostDTOS.get(0).getMessage());
            throw new MtException("MtException",itfSoDeliveryChanOrPostDTOS.get(0).getMessage());
        }
        return list.get(0);
    }

    /**
     * 单据状态更新
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @param docStatus        更新状态
     * @param eventId          事件ID
     * @param filterStatus     筛选状态
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 02:26:50
     */
    private void docStatusUpdate(Long tenantId, String instructionDocId, String docStatus, String eventId, String filterStatus) {
        // 修改头状态
        MtInstructionDocDTO2 docBuilder = new MtInstructionDocDTO2();
        docBuilder.setInstructionDocId(instructionDocId);
        docBuilder.setEventId(eventId);
        docBuilder.setInstructionDocStatus(docStatus);
        instructionDocRepository.instructionDocUpdate(tenantId, docBuilder, NO);

        // 修改行状态
        List<WmsInstructionAttrVO> lineList = wmsInstructionRepository.selectListByDocId(tenantId, instructionDocId);
        List<String> idList = lineList.stream().filter(rec -> filterStatus.equals(rec.getInstructionStatus())).map(WmsInstructionAttrVO::getInstructionId).collect(Collectors.toList());
        wmsSoDeliveryLineService.batchUpdateStatus(tenantId, instructionDocId, idList, eventId, docStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public WmsSoDeliveryDocVO cancel(Long tenantId, String instructionDocId) {
        this.validationPreCacle(tenantId, instructionDocId);
        WmsEventVO event = wmsEventService.createEventOnly(tenantId, SO_DELIVERY_CANCEL);
        this.docStatusUpdate(tenantId, instructionDocId, CANCEL, event.getEventId(), NEW);
        WmsSoDeliveryQueryDTO condition = new WmsSoDeliveryQueryDTO();
        condition.setInstructionDocId(instructionDocId);
        List<WmsSoDeliveryDocVO> list = wmsSoDeliveryDocRepository.selectListByQueryCondition(tenantId, condition);
        //调用接口[soDeliveryChangeOrPostIface]
        List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostList = new ArrayList<>();
        ItfSoDeliveryChanOrPostDTO itfSoDeliveryChanOrPostDTO = new ItfSoDeliveryChanOrPostDTO();
        itfSoDeliveryChanOrPostDTO.setType("CHANGE");
        itfSoDeliveryChanOrPostDTO.setInstructionDocId(instructionDocId);
        itfSoDeliveryChanOrPostDTO.setHReturnStatus("CANCEL");
        itfSoDeliveryChanOrPostList.add(itfSoDeliveryChanOrPostDTO);
        List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostDTOS = itfSoDeliveryChanOrPostIfaceRepository.soDeliveryChangeOrPostIface(tenantId,itfSoDeliveryChanOrPostList);
        if(CollectionUtils.isNotEmpty(itfSoDeliveryChanOrPostDTOS) && "E".equals(itfSoDeliveryChanOrPostDTOS.get(0).getStatus())){
            log.info("<<------------------------------"+itfSoDeliveryChanOrPostDTOS.get(0).getMessage());
            throw new MtException("MtException",itfSoDeliveryChanOrPostDTOS.get(0).getMessage());
        }
        return list.get(0);
    }

    @Override
    public WmsSoDeliveryDocVO release(Long tenantId, String instructionDocId) {
        this.validationPreRelease(tenantId, instructionDocId);

        WmsSoDeliveryQueryDTO condition = new WmsSoDeliveryQueryDTO();
        condition.setInstructionDocId(instructionDocId);
        List<WmsSoDeliveryDocVO> list = wmsSoDeliveryDocRepository.selectListByQueryCondition(tenantId, condition);
        return list.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public WmsSoDeliveryDocVO confirm(Long tenantId, String instructionDocId) {
        this.validationPreConfirm(tenantId, instructionDocId);

        // 调用{eventRequestCreate}创建请求事件
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "SO_DELIVERY_POST");

        // 调用{eventCreate}创建事件
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventTypeCode("SO_DELIVERY_POST");
        eventCreate.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);

        // 调用{ sequenceLimitMaterialLotBatchConsume }进行消耗
        MtMaterialLotVO15 mtMaterialLotVO15 = new MtMaterialLotVO15();
        mtMaterialLotVO15.setAllConsume(WmsConstant.CONSTANT_Y);
        mtMaterialLotVO15.setInstructionDocId(instructionDocId);
        mtMaterialLotVO15.setEventRequestId(eventRequestId);

        List<WmsSoDeliveryLineVO> lineVOList = wmsSoDeliveryLineMapper.selectListByDocId(tenantId, instructionDocId);
        List<String> instructionIdList = lineVOList.stream().map(WmsSoDeliveryLineVO::getInstructionId).collect(Collectors.toList());
        WmsSoDeliveryDetailQueryDTO dto = new WmsSoDeliveryDetailQueryDTO();
        dto.setInstructionDocId(instructionDocId);
        List<WmsSoDeliveryDetailVO> detailVOList = wmsSoDeliveryDetailMapper.selectListByCondition(tenantId, instructionIdList, dto);

        Map<String, List<WmsSoDeliveryDetailVO>> collect = detailVOList.stream().collect(Collectors.groupingBy(WmsSoDeliveryDetailVO::getMaterialId));

        for (Map.Entry<String, List<WmsSoDeliveryDetailVO>> entry : collect.entrySet()) {
            mtMaterialLotVO15.setMtMaterialLotSequenceList(entry.getValue().stream()
                    .map(detail -> {
                        MtMaterialLotVO16 mtMaterialLotVO161 = new MtMaterialLotVO16();
                        mtMaterialLotVO161.setMaterialLotId(detail.getMaterialLotId());
                        return mtMaterialLotVO161;
                    }).collect(Collectors.toList()));
            materialLotRepository.sequenceLimitMaterialLotBatchConsume(tenantId, mtMaterialLotVO15);
        }

        //调用{ materialLotBatchUpdate }更新物料批批次
//        List<MtMaterialLotVO20> materialLotList = detailVOList.stream().map(t -> new MtMaterialLotVO20() {{
//            setMaterialLotId(t.getMaterialLotId());
//            setLot("21090101");
//        }}).collect(Collectors.toList());
//        mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotList, eventId, MtBaseConstants.NO);

        //调用{ materialLotAttrPropertyUpdate }更新物料批拓展表
        List<MtCommonExtendVO6> commonExtendVO6List = new LinkedList<>();
        detailVOList.forEach(item -> {
            MtCommonExtendVO6 commonExtendVO6 = new MtCommonExtendVO6();
            List<MtCommonExtendVO5> attrs = new ArrayList<>();
            MtCommonExtendVO5 crsAttr = new MtCommonExtendVO5();
            crsAttr.setAttrName("STATUS");
            crsAttr.setAttrValue("SHIPPED");
            attrs.add(crsAttr);
            commonExtendVO6.setKeyId(item.getMaterialLotId());
            commonExtendVO6.setAttrs(attrs);
            commonExtendVO6List.add(commonExtendVO6);
        });
        mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, commonExtendVO6List);

        //调用{objectTransactionSync}记录发货事务
//        List<String> stringList = detailVOList.stream().map(WmsSoDeliveryDetailVO::getMaterialLotCode).collect(Collectors.toList());
//        List<MtMaterialLot> mtMaterialLotList = materialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class)
//                .andWhere(Sqls.custom().andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, stringList)).build());
//        List<String> list1 = mtMaterialLotList.stream().map(MtMaterialLot::getPrimaryUomId).collect(Collectors.toList());
//        List<MtUom> mtUoms = mtUomRepository.selectByCondition(Condition.builder(MtUom.class)
//                .andWhere(Sqls.custom().andIn(MtUom.FIELD_UOM_ID, list1)
//                        .andEqualTo(MtUom.FIELD_TENANT_ID, tenantId)).build());
//        List<String> list2 = mtMaterialLotList.stream().map(MtMaterialLot::getLocatorId).collect(Collectors.toList());
//        List<MtModLocator> mtModLocators = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class)
//                .andWhere(Sqls.custom().andIn(MtModLocator.FIELD_LOCATOR_ID, list2)
//                        .andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)).build());
//        WmsTransactionTypeDTO externalTrxType = wmsTransactionTypeRepository.getTransactionType(tenantId, "WMS_SHIP_TO_CUS");
//
//
//        List<WmsObjectTransactionRequestVO> list = new LinkedList<>();
//        detailVOList.forEach(item -> {
//            WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO = new WmsObjectTransactionRequestVO();
//            wmsObjectTransactionRequestVO.setTransactionTypeCode("WMS_SHIP_TO_CUS");
//            wmsObjectTransactionRequestVO.setEventId(eventId);
//            wmsObjectTransactionRequestVO.setMaterialLotId(item.getMaterialLotId());
//            wmsObjectTransactionRequestVO.setMaterialId(item.getMaterialId());
//            wmsObjectTransactionRequestVO.setTransactionQty(BigDecimal.valueOf(item.getActualQty()));
////            wmsObjectTransactionRequestVO.setLotNumber("21090101");
////            wmsObjectTransactionRequestVO.setTransferLotNumber("21090101");
//            Optional<MtMaterialLot> first = mtMaterialLotList.stream().filter(materialLot -> materialLot.getMaterialLotId().equals(item.getMaterialLotId())).findFirst();
//            first.ifPresent(materialLot -> {
//                wmsObjectTransactionRequestVO.setPlantId(materialLot.getSiteId());
//                wmsObjectTransactionRequestVO.setLocatorId(materialLot.getLocatorId());
//
//                Optional<MtUom> first1 = mtUoms.stream().filter(mtUom -> mtUom.getUomId().equals(materialLot.getPrimaryUomId())).findFirst();
//                first1.ifPresent(mtUom -> {
//                    wmsObjectTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
//                });
//
//                Optional<MtModLocator> first2 = mtModLocators.stream().filter(mtModLocator -> mtModLocator.getLocatorId().equals(materialLot.getLocatorId())).findFirst();
//                first2.ifPresent(mtModLocator -> {
//                    wmsObjectTransactionRequestVO.setWarehouseId(mtModLocator.getParentLocatorId());
//                });
//            });
//
//            wmsObjectTransactionRequestVO.setTransactionTime(new Date());
//            wmsObjectTransactionRequestVO.setTransactionReasonCode("发货过账");
//            wmsObjectTransactionRequestVO.setSourceDocType("SO_DELIVERY");
//            wmsObjectTransactionRequestVO.setSourceDocId(instructionDocId);
//            wmsObjectTransactionRequestVO.setSourceDocLineId(item.getInstructionId());
//            wmsObjectTransactionRequestVO.setMoveType(externalTrxType != null ? externalTrxType.getMoveType() : "");
//            wmsObjectTransactionRequestVO.setContainerId(item.getContainerId());
//            list.add(wmsObjectTransactionRequestVO);
//        });
//
//        wmsObjectTransactionRepository.objectTransactionSync(tenantId, list);

        //行更新
        List<WmsSoDeliveryLineVO> lineVOList1 = lineVOList.stream().filter(line -> !"CANCEL".equals(line.getInstructionStatus())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(lineVOList1)) {
            lineVOList1.forEach(item -> {
                MtInstructionVO lineBuilder = new MtInstructionVO();
                lineBuilder.setEventId(eventId);
                lineBuilder.setInstructionStatus(COMPLETE);
                lineBuilder.setInstructionId(item.getInstructionId());
                MtInstructionVO6 ins = instructionRepository.instructionUpdate(tenantId, lineBuilder, NO);
            });
        }

        // 修改头状态
        MtInstructionDocDTO2 docBuilder = new MtInstructionDocDTO2();
        docBuilder.setInstructionDocId(instructionDocId);
        docBuilder.setEventId(eventId);
        docBuilder.setInstructionDocStatus(COMPLETE);
        instructionDocRepository.instructionDocUpdate(tenantId, docBuilder, NO);

        WmsSoDeliveryQueryDTO condition = new WmsSoDeliveryQueryDTO();
        condition.setInstructionDocId(instructionDocId);
        List<WmsSoDeliveryDocVO> result = wmsSoDeliveryDocRepository.selectListByQueryCondition(tenantId, condition);
        //调用接口[soDeliveryChangeOrPostIface]
        List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostList = new ArrayList<>();
        ItfSoDeliveryChanOrPostDTO itfSoDeliveryChanOrPostDTO = new ItfSoDeliveryChanOrPostDTO();
        itfSoDeliveryChanOrPostDTO.setType("POST");
        itfSoDeliveryChanOrPostDTO.setInstructionDocId(instructionDocId);
        itfSoDeliveryChanOrPostList.add(itfSoDeliveryChanOrPostDTO);
        List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostDTOS = itfSoDeliveryChanOrPostIfaceRepository.soDeliveryChangeOrPostIface(tenantId,itfSoDeliveryChanOrPostList);
        if(CollectionUtils.isNotEmpty(itfSoDeliveryChanOrPostDTOS) && "E".equals(itfSoDeliveryChanOrPostDTOS.get(0).getStatus())){
            log.info("<<------------------------------"+itfSoDeliveryChanOrPostDTOS.get(0).getMessage());
            throw new MtException("MtException",itfSoDeliveryChanOrPostDTOS.get(0).getMessage());
        }
        return result.get(0);
    }

    @Override
    public WmsSoDeliveryDocVO releaseCancel(Long tenantId, String instructionDocId) {
        this.validationPreReleaseCancel(tenantId, instructionDocId);
        WmsEventVO event = wmsEventService.createEventOnly(tenantId, SO_DELIVERY_RELEASE_CANCEL);
        this.docStatusUpdate(tenantId, instructionDocId, NEW, event.getEventId(), RELEASED);
        WmsSoDeliveryQueryDTO condition = new WmsSoDeliveryQueryDTO();
        condition.setInstructionDocId(instructionDocId);
        List<WmsSoDeliveryDocVO> list = wmsSoDeliveryDocRepository.selectListByQueryCondition(tenantId, condition);
        return list.get(0);
    }
}
