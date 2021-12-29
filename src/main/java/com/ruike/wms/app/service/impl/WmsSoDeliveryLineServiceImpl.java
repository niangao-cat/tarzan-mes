package com.ruike.wms.app.service.impl;

import com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO;
import com.ruike.itf.domain.repository.ItfSoDeliveryChanOrPostIfaceRepository;
import com.ruike.wms.api.dto.WmsSoDeliverySubmitDTO;
import com.ruike.wms.app.service.WmsEventService;
import com.ruike.wms.app.service.WmsSoDeliveryLineService;
import com.ruike.wms.domain.repository.WmsInstructionRepository;
import com.ruike.wms.domain.repository.WmsSoDeliveryLineRepository;
import com.ruike.wms.domain.vo.WmsEventVO;
import com.ruike.wms.domain.vo.WmsInstructionAttrVO;
import com.ruike.wms.domain.vo.WmsSoDeliveryDocVO;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.domain.vo.MtExtendVO5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO6;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.*;
import static com.ruike.wms.infra.constant.WmsConstant.DocStatus.CANCEL;
import static com.ruike.wms.infra.constant.WmsConstant.EventType.SO_DELIVERY_CANCEL;
import static com.ruike.wms.infra.constant.WmsConstant.InspectionDocType.SO_DELIVERY;
import static com.ruike.wms.infra.constant.WmsConstant.InstructionStatus.RELEASED;
import static com.ruike.wms.infra.constant.WmsConstant.InstructionType.SHIP_TO_CUSTOMER;

/**
 * <p>
 * 出货单行 服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 21:22
 */
@Service
@Slf4j
public class WmsSoDeliveryLineServiceImpl implements WmsSoDeliveryLineService {
    private final MtInstructionRepository instructionRepository;
    private final MtExtendSettingsRepository extendSettingsRepository;
    private final WmsInstructionRepository wmsInstructionRepository;
    private final WmsEventService wmsEventService;
    private final WmsSoDeliveryLineRepository wmsSoDeliveryLineRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtExtendSettingsRepository mtExtendSettingsRepository;
    private final MtInstructionDocRepository instructionDocRepository;
    private final ItfSoDeliveryChanOrPostIfaceRepository itfSoDeliveryChanOrPostIfaceRepository;

    public WmsSoDeliveryLineServiceImpl(MtInstructionRepository instructionRepository, MtExtendSettingsRepository extendSettingsRepository, WmsInstructionRepository wmsInstructionRepository, WmsEventService wmsEventService, WmsSoDeliveryLineRepository wmsSoDeliveryLineRepository, MtErrorMessageRepository mtErrorMessageRepository, MtExtendSettingsRepository mtExtendSettingsRepository, MtInstructionDocRepository instructionDocRepository, ItfSoDeliveryChanOrPostIfaceRepository itfSoDeliveryChanOrPostIfaceRepository) {
        this.instructionRepository = instructionRepository;
        this.extendSettingsRepository = extendSettingsRepository;
        this.wmsInstructionRepository = wmsInstructionRepository;
        this.wmsEventService = wmsEventService;
        this.wmsSoDeliveryLineRepository = wmsSoDeliveryLineRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.mtExtendSettingsRepository = mtExtendSettingsRepository;
        this.instructionDocRepository = instructionDocRepository;
        this.itfSoDeliveryChanOrPostIfaceRepository = itfSoDeliveryChanOrPostIfaceRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void batchInsertOrUpdate(Long tenantId, WmsSoDeliverySubmitDTO dto) {
        List<WmsInstructionAttrVO> lineList = wmsInstructionRepository.selectListByDocId(tenantId, dto.getInstructionDocId());
        // 求出当前最大行号，+1后作为新行的行号
        int currentLine = lineList.stream().map(rec -> Integer.valueOf(rec.getInstructionLineNum())).max(Integer::compareTo).orElse(0);
        AtomicInteger index = new AtomicInteger(currentLine / 10 + 1);
        dto.getLineList().forEach(line -> {
            // 更新单据行
            MtInstructionVO lineBuilder = new MtInstructionVO();
            lineBuilder.setSourceDocId(dto.getInstructionDocId());
            lineBuilder.setSiteId(dto.getSiteId());
            lineBuilder.setMaterialId(line.getMaterialId());
            lineBuilder.setUomId(line.getUomId());
            lineBuilder.setSourceOrderType(StringUtils.isNotBlank(line.getSourceOrderId()) ? SO : null);
            lineBuilder.setSourceOrderId(StringUtils.isBlank(line.getSourceOrderId()) ? null : line.getSourceOrderId());
            lineBuilder.setSourceOrderLineId(StringUtils.isBlank(line.getSourceOrderLineId()) ? null : line.getSourceOrderLineId());
            lineBuilder.setFromSiteId(line.getFromSiteId());
            Optional<WmsInstructionAttrVO> vo = lineList.stream().filter(item -> item.getInstructionId().equals(line.getInstructionId())).findFirst();
            if ("SALES_RETURN".equals(dto.getInstructionDocType())) {
                vo.ifPresent(item -> {
                    if (!item.getToLocatorId().equals(line.getFromLocatorId()) && !"RELEASED".equals(item.getInstructionStatus())) {
                        throw new MtException("WX.WMS_SO_DELIVERY_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WX.WMS_SO_DELIVERY_0002", "WMS", line.getInstructionLineNum()));
                    }
                });
                lineBuilder.setToLocatorId(Optional.ofNullable(line.getFromLocatorId()).orElse("-1"));
            } else {
                vo.ifPresent(item -> {
                    if (!item.getFromLocatorId().equals(line.getFromLocatorId()) && !"RELEASED".equals(item.getInstructionStatus())) {
                        throw new MtException("WX.WMS_SO_DELIVERY_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WX.WMS_SO_DELIVERY_0002", "WMS", line.getInstructionLineNum()));
                    }
                });
                lineBuilder.setFromLocatorId(Optional.ofNullable(line.getFromLocatorId()).orElse("-1"));
            }
            BigDecimal lineQuantity = line.getQuantity() == null ? BigDecimal.ZERO : line.getQuantity();
            BigDecimal lineActualQty = line.getActualQty() == null ? BigDecimal.ZERO : line.getActualQty();

            vo.ifPresent(item -> {
                BigDecimal quantity = item.getQuantity() == null ? BigDecimal.ZERO : item.getQuantity();
                if (quantity.compareTo(lineQuantity) != 0) {
                    if (!(lineQuantity.compareTo(line.getActualQty()) >= 0
                            && lineQuantity.compareTo(quantity) <= 0)) {
                        throw new MtException("WX.WMS_SO_DELIVERY_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WX.WMS_SO_DELIVERY_0003", "WMS", line.getInstructionLineNum()));
                    }
                }
            });
            lineBuilder.setQuantity(lineQuantity.doubleValue());
            lineBuilder.setCustomerId(dto.getCustomerId());
            lineBuilder.setCustomerSiteId(dto.getCustomerSiteId());
            lineBuilder.setRemark(line.getRemark());
            lineBuilder.setEventRequestId(dto.getEventRequestId());
            if (StringUtils.isBlank(line.getInstructionId())) {
                lineBuilder.setBusinessType(SO_DELIVERY);
                lineBuilder.setInstructionType(SHIP_TO_CUSTOMER);
            }
            if (StringUtils.isNotBlank(line.getInstructionId())) {
                lineBuilder.setInstructionId(line.getInstructionId());
                lineBuilder.setEventId(dto.getEventId());
            }
            vo.ifPresent(item -> {
                if (lineActualQty.compareTo(BigDecimal.ZERO) == 0) {
                    lineBuilder.setInstructionStatus("RELEASED");
                } else if (lineActualQty.compareTo(BigDecimal.ZERO) > 0
                        && lineActualQty.compareTo(lineQuantity) < 0) {
                    lineBuilder.setInstructionStatus("PREPARE_EXECUTE");
                } else if (lineActualQty.compareTo(lineQuantity) >= 0) {
                    lineBuilder.setInstructionStatus("PREPARE_COMPLETE");
                }
            });
            MtInstructionVO6 ins = instructionRepository.instructionUpdate(tenantId, lineBuilder, NO);

            // 新增/更新拓展字段
            List<MtExtendVO5> attrList = new ArrayList<>(1);
            // 行号仅在新增时写入
            if (StringUtils.isBlank(line.getInstructionId())) {
                MtExtendVO5 lineNum = new MtExtendVO5();
                lineNum.setAttrName("INSTRUCTION_LINE_NUM");
                lineNum.setAttrValue(String.valueOf(index.getAndIncrement() * 10));
                attrList.add(lineNum);
            }
            MtExtendVO5 materialVersion = new MtExtendVO5();
            materialVersion.setAttrName("MATERIAL_VERSION");
            materialVersion.setAttrValue(line.getMaterialVersion());
            attrList.add(materialVersion);
            MtExtendVO5 toleranceLowerLimit = new MtExtendVO5();
            toleranceLowerLimit.setAttrName("TOLERANCE_LOWER_LIMIT");
            toleranceLowerLimit.setAttrValue(Optional.ofNullable(line.getToleranceLowerLimit()).orElse(BigDecimal.ZERO).toPlainString());
            attrList.add(toleranceLowerLimit);
            MtExtendVO5 toleranceUpperLimit = new MtExtendVO5();
            toleranceUpperLimit.setAttrName("TOLERANCE_UPPER_LIMIT");
            toleranceUpperLimit.setAttrValue(Optional.ofNullable(line.getToleranceUpperLimit()).orElse(BigDecimal.ZERO).toPlainString());
            attrList.add(toleranceUpperLimit);
            MtExtendVO5 soFlag = new MtExtendVO5();
            soFlag.setAttrName("SO_FLAG");
            soFlag.setAttrValue(StringUtils.isNotBlank(line.getSourceOrderId()) ? YES : NO);
            attrList.add(soFlag);
            extendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", ins.getInstructionId(), dto.getEventId(), attrList);
        });

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void batchUpdateStatus(Long tenantId, String instructionDocId, List<String> idList, String eventId, String status) {
        // 查询出所有状态不为CANCEL的行
        idList.forEach(instructionId -> {
            MtInstructionVO lineBuilder = new MtInstructionVO();
            lineBuilder.setInstructionId(instructionId);
            lineBuilder.setInstructionStatus(status);
            lineBuilder.setEventId(eventId);
            instructionRepository.instructionUpdate(tenantId, lineBuilder, NO);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void cancel(Long tenantId, String instructionId) {
        MtInstruction doc = instructionRepository.selectByPrimaryKey(instructionId);
        this.validationPreCacle(tenantId, doc);
        WmsEventVO event = wmsEventService.createEventOnly(tenantId, SO_DELIVERY_CANCEL);
        MtInstructionVO lineBuilder = new MtInstructionVO();
        lineBuilder.setInstructionId(instructionId);
        lineBuilder.setInstructionStatus(CANCEL);
        lineBuilder.setEventId(event.getEventId());
        instructionRepository.instructionUpdate(tenantId, lineBuilder, NO);


        List<MtInstruction> mtInstructionList = instructionRepository.selectByCondition(Condition.builder(MtInstruction.class)
                .andWhere(Sqls.custom().andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID, doc.getSourceDocId())).build());
        MtInstructionDocDTO2 docBuilder = new MtInstructionDocDTO2();
        docBuilder.setInstructionDocId(doc.getSourceDocId());
        docBuilder.setEventId(event.getEventId());
        boolean result = mtInstructionList.stream().allMatch(item -> "CANCEL".equals(item.getInstructionStatus()));
        if (result) {
            docBuilder.setInstructionDocStatus("CANCEL");
        }else{
            //筛选掉取消状态的行,判断剩下的是否为全部完成
            mtInstructionList = mtInstructionList.stream().filter(item-> !item.getInstructionStatus().equals("CANCEL")).collect(Collectors.toList());
            boolean result1 = mtInstructionList.stream().allMatch(item -> "PREPARE_COMPLETE".equals(item.getInstructionStatus()));
            boolean result2 = mtInstructionList.stream().allMatch(item -> "COMPLETE".equals(item.getInstructionStatus()));
            if (result1) {
                docBuilder.setInstructionDocStatus("PREPARE_COMPLETE");
            }
            if(result2){
                docBuilder.setInstructionDocStatus("COMPLETE");
            }
        }
        MtInstructionDocVO3 docUpdate = instructionDocRepository.instructionDocUpdate(tenantId, docBuilder, NO);
        //调用接口[soDeliveryChangeOrPostIface]
        List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostList = new ArrayList<>();
        ItfSoDeliveryChanOrPostDTO itfSoDeliveryChanOrPostDTO = new ItfSoDeliveryChanOrPostDTO();
        itfSoDeliveryChanOrPostDTO.setType("CHANGE");
        itfSoDeliveryChanOrPostDTO.setInstructionDocId(doc.getSourceDocId());
        itfSoDeliveryChanOrPostDTO.setInstructionId(instructionId);
        itfSoDeliveryChanOrPostDTO.setLReturnStatus("CANCEL");
        itfSoDeliveryChanOrPostList.add(itfSoDeliveryChanOrPostDTO);
        List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostDTOS = itfSoDeliveryChanOrPostIfaceRepository.soDeliveryChangeOrPostIface(tenantId,itfSoDeliveryChanOrPostList);
        if(CollectionUtils.isNotEmpty(itfSoDeliveryChanOrPostDTOS) && "E".equals(itfSoDeliveryChanOrPostDTOS.get(0).getStatus())){
            log.info("<<------------------------------"+itfSoDeliveryChanOrPostDTOS.get(0).getMessage());
            throw new MtException("MtException",itfSoDeliveryChanOrPostDTOS.get(0).getMessage());
        }
    }


    /**
     * 取消头前验证
     *
     * @param tenantId 租户
     * @param doc      单据行
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 09:35:12
     */
    private void validationPreCacle(Long tenantId, MtInstruction doc) {
        List<MtExtendAttrVO1> anotherMtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, new MtExtendVO1("mt_instruction_attr", Collections.singletonList(doc.getInstructionId()), "INSTRUCTION_LINE_NUM"));
        Map<String, String> anotherExtendAttrMap = new HashMap<>();
        anotherMtExtendAttrVO1s.forEach(item -> anotherExtendAttrMap.put(item.getAttrName(), item.getAttrValue()));
        if (Objects.isNull(doc)) {
            throw new MtException("MT_INVENTORY_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0022", "INVENTORY"));
        }
        if (!RELEASED.equals(doc.getInstructionStatus())) {
            throw new MtException("WX.WMS_SO_DELIVERY_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WX.WMS_SO_DELIVERY_0005", "WMS", anotherExtendAttrMap.get("INSTRUCTION_LINE_NUM")));
        }
    }
}
