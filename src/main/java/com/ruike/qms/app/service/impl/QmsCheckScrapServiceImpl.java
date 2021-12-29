package com.ruike.qms.app.service.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.qms.api.dto.*;
import com.ruike.qms.app.service.QmsCheckScrapService;
import com.ruike.qms.domain.entity.QmsIqcHeader;
import com.ruike.qms.domain.entity.QmsIqcHeaderHis;
import com.ruike.qms.domain.repository.QmsIqcHeaderHisRepository;
import com.ruike.qms.domain.repository.QmsIqcHeaderRepository;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsCheckScrapMapper;
import com.ruike.qms.infra.mapper.QmsIqcHeaderMapper;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtUomVO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 检验报废应用服务实现
 *
 * @author jiangling.zheng@hand-china.com 2020-08-26 09:37:14
 */
@Service
public class QmsCheckScrapServiceImpl implements QmsCheckScrapService {

    @Autowired
    MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private QmsCheckScrapMapper qmsCheckScrapMapper;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private QmsIqcHeaderRepository qmsIqcHeaderRepository;

    @Autowired
    private QmsIqcHeaderHisRepository qmsIqcHeaderHisRepository;

    @Autowired
    private QmsIqcHeaderMapper qmsIqcHeaderMapper;


    @Override
    public QmsCheckScrapBarCodeDTO barCodeQuery(Long tenantId, QmsCheckScrapParamsDTO dto) {
        if (Objects.isNull(dto)) {
            throw new MtException("QMS_CHECK_SCRAP_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_CHECK_SCRAP_0001", QmsConstants.ConstantValue.QMS));
        }
        String barCode = dto.getBarCode();
        //校验条码是否存在
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(barCode);
        }});
        if (Objects.isNull(mtMaterialLot)) {
            throw new MtException("QMS_CHECK_SCRAP_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_CHECK_SCRAP_0002", QmsConstants.ConstantValue.QMS, barCode));
        }
        MtExtendVO mtExtendVO = new MtExtendVO();
        mtExtendVO.setTableName(QmsConstants.AttrTable.MT_MATERIAL_LOT_ATTR);
        mtExtendVO.setKeyId(mtMaterialLot.getMaterialLotId());
        mtExtendVO.setAttrName(QmsConstants.ExtendAttr.STATUS);
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
        String status = CollectionUtils.isEmpty(mtExtendAttrVOList) || StringUtils.isEmpty(mtExtendAttrVOList.get(0).getAttrValue()) ?
                "" : mtExtendAttrVOList.get(0).getAttrValue();
        // 校验条码状态
        if (!StringUtils.equalsAny(status, WmsConstant.MaterialLotStatus.MINSTOCK, WmsConstant.MaterialLotStatus.TO_ACCEPT)) {
            throw new MtException("QMS_CHECK_SCRAP_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_CHECK_SCRAP_0003", QmsConstants.ConstantValue.QMS));
        }
        // 校验条码是否有效
        if (!StringUtils.equals(mtMaterialLot.getEnableFlag(), QmsConstants.ConstantValue.YES)) {
            throw new MtException("QMS_CHECK_SCRAP_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_CHECK_SCRAP_0004", QmsConstants.ConstantValue.QMS, barCode));
        }
        // 校验质量状态
        if (!StringUtils.equals(mtMaterialLot.getQualityStatus(), QmsConstants.QualityStatus.PENDING)) {
            throw new MtException("QMS_CHECK_SCRAP_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_CHECK_SCRAP_0005", QmsConstants.ConstantValue.QMS, barCode));
        }
        // 校验是否关联送货单
        List<QmsCheckScrapDocLineDTO> orderLines =
                qmsCheckScrapMapper.selectDocCondition(tenantId, mtMaterialLot.getMaterialLotId());
        // 校验单据行唯一
        if (orderLines.size() == 0) {
            throw new MtException("QMS_CHECK_SCRAP_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_CHECK_SCRAP_0006", QmsConstants.ConstantValue.QMS, barCode));
        } else if (orderLines.size() > 1) {
            throw new MtException("QMS_CHECK_SCRAP_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_CHECK_SCRAP_0007", QmsConstants.ConstantValue.QMS, barCode));
        }
        QmsCheckScrapDocLineDTO lineDto = orderLines.get(0);
        // 当前单据是否与上次条码对应单据一致
        String instructionDocNum = StringUtils.isEmpty(dto.getInstructionDocNum()) ? lineDto.getInstructionDocNum() : dto.getInstructionDocNum();
        String instructionNum = StringUtils.isEmpty(dto.getInstructionNum()) ? lineDto.getInstructionNum() : dto.getInstructionNum();
        if (!StringUtils.equals(instructionDocNum, lineDto.getInstructionDocNum()) ||
                !StringUtils.equals(instructionNum, lineDto.getInstructionNum()) ) {
            throw new MtException("QMS_CHECK_SCRAP_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_CHECK_SCRAP_0008", QmsConstants.ConstantValue.QMS, dto.getInstructionNum(), lineDto.getInstructionNum()));
        }
        // 校验单据状态
        if (!StringUtils.equals(lineDto.getInstructionDocStatus(),QmsConstants.DocStatus.RECEIVE_COMPLETE)) {
            throw new MtException("QMS_CHECK_SCRAP_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_CHECK_SCRAP_0006", QmsConstants.ConstantValue.QMS, barCode));
        }
        QmsCheckScrapBarCodeDTO barCodeDto = new QmsCheckScrapBarCodeDTO();
        BeanUtils.copyProperties(mtMaterialLot, barCodeDto);
        BeanUtils.copyProperties(lineDto, barCodeDto);
        MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, barCodeDto.getPrimaryUomId());
        barCodeDto.setPrimaryUomQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
        barCodeDto.setPrimaryUomCode(mtUomVO.getUomCode());
        barCodeDto.setPrimaryUomName(mtUomVO.getUomName());
        return barCodeDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QmsCheckScrapDocLineDTO2 scrapSubmit(Long tenantId, QmsCheckScrapDocLineDTO2 dto) {
        // 校验单据状态
        if (Objects.isNull(dto)) {
            throw new MtException("QMS_CHECK_SCRAP_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_CHECK_SCRAP_0010", QmsConstants.ConstantValue.QMS));
        }
        List<QmsCheckScrapBarCodeDTO2> barCodeList = dto.getBarCodeList();
        QmsIqcHeader qmsIqcHeader = qmsIqcHeaderRepository.selectOne(new QmsIqcHeader(){{
            setTenantId(tenantId);
            setDocHeaderId(dto.getInstructionDocId());
            setDocLineId(dto.getInstructionId());
        }});
        // 校验单据状态
        if (Objects.isNull(qmsIqcHeader)) {
            throw new MtException("QMS_CHECK_SCRAP_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_CHECK_SCRAP_0009", QmsConstants.ConstantValue.QMS));
        }
        // 创建送货单事件
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventTypeCode(WmsConstant.EVENT_MATERIAL_STOCKING);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
        List<MtExtendAttrDTO3> attrList;
        MtExtendAttrDTO3 attr;
        Long userId = DetailsHelper.getUserDetails().getUserId();
        for (QmsCheckScrapBarCodeDTO2 barCodeDto : barCodeList) {
            attrList = new ArrayList<>();
            attr = new MtExtendAttrDTO3();
            attr.setAttrName("SCRAP_QTY");
            attr.setAttrValue(String.valueOf(barCodeDto.getScrapQty()));
            attrList.add(attr);
            attr = new MtExtendAttrDTO3();
            attr.setAttrName("SCRAP_BY");
            attr.setAttrValue(String.valueOf(userId));
            attrList.add(attr);
            // 更新扩展字段
            mtExtendSettingsService.attrSave(tenantId, QmsConstants.AttrTable.MT_INSTRUCT_ACT_DETAIL_ATTR,
                    barCodeDto.getActualDetailId(), eventId, attrList);
        }
        // 获取单据行下所有条码的报废数量合计
        BigDecimal sumScrapQty = qmsCheckScrapMapper.sumScrapQtyGet(tenantId, dto.getInstructionId()) == null ?
                BigDecimal.ZERO : qmsCheckScrapMapper.sumScrapQtyGet(tenantId, dto.getInstructionId());
        qmsIqcHeader.setScrapQty(sumScrapQty);
        qmsIqcHeaderMapper.updateByPrimaryKeySelective(qmsIqcHeader);
        QmsIqcHeaderHis headerHis = new QmsIqcHeaderHis();
        BeanUtils.copyProperties(qmsIqcHeader, headerHis);
        headerHis.setEventId(eventId);
        qmsIqcHeaderHisRepository.createQmsIqcHeaderHis(headerHis);
        return dto;
    }

}
