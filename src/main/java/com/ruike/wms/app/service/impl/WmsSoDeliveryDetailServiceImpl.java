package com.ruike.wms.app.service.impl;

import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.wms.app.service.WmsSoDeliveryDetailService;
import com.ruike.wms.domain.vo.WmsSoDeliveryDetailVO;
import com.ruike.wms.domain.vo.WmsSoDeliveryLineVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsSoDeliveryDetailMapper;
import com.ruike.wms.infra.mapper.WmsSoDeliveryLineMapper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtCommonExtendVO5;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualVO;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static io.tarzan.common.domain.util.MtBaseConstants.NO;
import static io.tarzan.common.domain.util.StringHelper.getWhereInValuesSql;

/**
 * 出货单明细 ServiceImpl
 *
 * @author faming.yang@hand-china.com 2021-07-15 18:32
 */
@Service
public class WmsSoDeliveryDetailServiceImpl implements WmsSoDeliveryDetailService {

    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private WmsSoDeliveryDetailMapper wmsSoDeliveryDetailMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private WmsSoDeliveryLineMapper wmsSoDeliveryLineMapper;

    @Override
    @Transactional
    public void delete(Long tenantId, List<WmsSoDeliveryDetailVO> voList, Double lineActualQty, Double lineDemandQty, String instructionDocId) {
        if (CollectionUtils.isEmpty(voList)) {
            return;
        }

        validationPreDelete(tenantId, voList);

        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventTypeCode("MATERIALLOT_SCAN_CANCEL");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);

        List<WmsSoDeliveryLineVO> lineVOList = wmsSoDeliveryLineMapper.selectListByDocId(tenantId, voList.get(0).getInstructionDocId());
        WmsSoDeliveryLineVO wmsSoDeliveryLineVO = lineVOList.stream().filter(item -> voList.get(0).getInstructionId().equals(item.getInstructionId())).collect(Collectors.toList()).get(0);

        BigDecimal lineActualQty2 = wmsSoDeliveryLineVO.getActualQty() == null ? BigDecimal.ZERO : wmsSoDeliveryLineVO.getActualQty();
        BigDecimal lineDemandQty2 = wmsSoDeliveryLineVO.getDemandQty() == null ? BigDecimal.ZERO : wmsSoDeliveryLineVO.getDemandQty();

        //指令取消执行
        //实绩更新
        MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
        mtInstructionActualVO.setInstructionId(voList.get(0).getInstructionId());
        double actualQty = voList.stream().mapToDouble(WmsSoDeliveryDetailVO::getActualQty).sum();
        mtInstructionActualVO.setActualQty(-actualQty);
        mtInstructionActualVO.setEventId(eventId);
        if (lineActualQty2.compareTo(BigDecimal.valueOf(actualQty)) == 0) {
            mtInstructionActualVO.setFromLocatorId("");
        }
        mtInstructionActualVO.setActualId(voList.get(0).getActualId());
        mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);
        //实绩明细更新
        mtCustomDbRepository.batchDeleteTarzan(voList.stream().map(WmsSoDeliveryDetailVO::getActualDetailId).collect(Collectors.toList())
                , MtInstructionActualDetail.class);

        //条码更新
        List<MtMaterialLotVO20> materialLotList = voList.stream().map(t -> new MtMaterialLotVO20() {{
            setMaterialLotId(t.getMaterialLotId());
        }}).collect(Collectors.toList());
        mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotList, eventId, NO);

        List<MtCommonExtendVO6> commonExtendVO6List = new LinkedList<>();
        voList.forEach(item -> {
            MtCommonExtendVO6 commonExtendVO6 = new MtCommonExtendVO6();
            commonExtendVO6.setKeyId(item.getMaterialLotId());
            MtCommonExtendVO5 crsAttr = new MtCommonExtendVO5();
            crsAttr.setAttrName("STATUS");
            crsAttr.setAttrValue("INSTOCK");
            List<MtCommonExtendVO5> attrs = new ArrayList<>();
            attrs.add(crsAttr);
            commonExtendVO6.setAttrs(attrs);
            commonExtendVO6List.add(commonExtendVO6);
        });
        mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, commonExtendVO6List);

        //行状态更新
        MtInstructionVO instructionUpdate = new MtInstructionVO();
        instructionUpdate.setInstructionId(voList.get(0).getInstructionId());

        BigDecimal subtract = lineActualQty2.subtract(BigDecimal.valueOf(actualQty));
        if (subtract.compareTo(BigDecimal.ZERO) == 0) {
            instructionUpdate.setInstructionStatus("RELEASED");
        } else if (subtract.compareTo(BigDecimal.ZERO) > 0
                && subtract.compareTo(lineDemandQty2) < 0) {
            instructionUpdate.setInstructionStatus("PREPARE_EXECUTE");
        } else if (subtract.compareTo(lineDemandQty2) >= 0) {
            instructionUpdate.setInstructionStatus("PREPARE_COMPLETE");
        }
        instructionUpdate.setEventId(eventId);
        mtInstructionRepository.instructionUpdate(tenantId, instructionUpdate, WmsConstant.CONSTANT_N);

        //单据状态更新
        MtInstructionDocDTO2 mtInstructionDocDTO2 = new MtInstructionDocDTO2();
        mtInstructionDocDTO2.setEventId(eventId);
        mtInstructionDocDTO2.setInstructionDocId(instructionDocId);

        List<MtInstruction> mtInstructionList = mtInstructionRepository.selectByCondition(Condition.builder(MtInstruction.class)
                .andWhere(Sqls.custom().andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID, instructionDocId)).build());

        boolean result = mtInstructionList.stream().filter(item -> !"CANCEL".equals(item.getInstructionStatus())).allMatch(item -> "RELEASED".equals(item.getInstructionStatus()));
        boolean result3 = mtInstructionList.stream().filter(item -> !"CANCEL".equals(item.getInstructionStatus())).allMatch(item -> "PREPARE_COMPLETE".equals(item.getInstructionStatus()));
        if (result) {
            mtInstructionDocDTO2.setInstructionDocStatus("RELEASED");
        } else if (result3) {
            mtInstructionDocDTO2.setInstructionDocStatus("PREPARE_COMPLETE");
        } else {
            mtInstructionDocDTO2.setInstructionDocStatus("PREPARE_EXECUTE");
        }
        MtInstructionDocVO3 mtInstructionDocVO3 = mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDocDTO2, QmsConstants.ConstantValue.NO);
    }


    /**
     * 删除前前验证
     *
     * @param tenantId 租户
     * @param voList   明细ID集合
     */
    private void validationPreDelete(Long tenantId, List<WmsSoDeliveryDetailVO> voList) {

        List<String> collect = voList.stream().map(WmsSoDeliveryDetailVO::getActualDetailId).collect(Collectors.toList());
        String whereInValuesSql = getWhereInValuesSql("miad.actual_detail_id", collect, 1000);

        List<MtInstructionActualDetail> detailList = wmsSoDeliveryDetailMapper.batchSelectById(tenantId, whereInValuesSql);

        List<MtInstructionActualDetail> nullList = detailList.stream().filter(detail -> StringUtils.isNotBlank(detail.getContainerId())).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(nullList)) {
            throw new MtException("WX_WMS_SO_DELIVERY_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId
                    , "WX_WMS_SO_DELIVERY_0008"
                    , "WMS", voList.get(0).getMaterialLotCode()));
        }

    }
}
