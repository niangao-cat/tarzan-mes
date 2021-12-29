package com.ruike.qms.infra.repository.impl;

import com.ruike.hme.domain.repository.HmeMaterialTransferRepository;
import com.ruike.hme.domain.vo.HmeChipTransferVO4;
import com.ruike.qms.domain.entity.QmsIqcHeader;
import com.ruike.qms.domain.repository.QmsIqcHeaderRepository;
import com.ruike.qms.domain.vo.QmsDocMaterialLotVO;
import com.ruike.qms.domain.vo.QmsDocMaterialLotVO2;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsIqcCheckPlatformMapper;
import com.ruike.qms.infra.mapper.QmsIqcHeaderMapper;
import com.ruike.qms.infra.mapper.QmsQcDocMaterialLotRelMapper;
import com.ruike.wms.domain.entity.WmsDocLotRel;
import com.ruike.wms.domain.repository.WmsDocLotRelRepository;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtCommonExtendVO5;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.qms.domain.entity.QmsQcDocMaterialLotRel;
import com.ruike.qms.domain.repository.QmsQcDocMaterialLotRelRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.infra.mapper.MtInstructionActualDetailMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO1;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 二次送检条码 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-24 17:28:04
 */
@Component
@Slf4j
public class QmsQcDocMaterialLotRelRepositoryImpl extends BaseRepositoryImpl<QmsQcDocMaterialLotRel> implements QmsQcDocMaterialLotRelRepository {


    @Autowired
    private QmsQcDocMaterialLotRelMapper qmsQcDocMaterialLotRelMapper;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingMapper;

    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private QmsIqcHeaderRepository qmsIqcHeaderRepository;

    @Autowired
    private QmsIqcHeaderMapper qmsIqcHeaderMapper;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;

    @Autowired
    private WmsDocLotRelRepository wmsDocLotRelRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private QmsIqcCheckPlatformMapper qmsIqcCheckPlatformMapper;

    @Autowired
    private MtInstructionActualDetailMapper instructionActualDetailMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    public QmsDocMaterialLotVO scanMaterialLot(Long tenantId, String materialLotCode, String instructionDocNum) {
        if (StringUtils.isBlank(materialLotCode)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "materialLotCode"));
        }

        MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, materialLotCode);

        //条码检验
        QmsDocMaterialLotVO2 materialLotVO2 = checkMaterialLot(tenantId, materialLot);

        QmsDocMaterialLotVO materialLotVO = new QmsDocMaterialLotVO();
        QmsDocMaterialLotVO qmsDocMaterialLotVO = qmsQcDocMaterialLotRelMapper.queryInstructionHeaderInfo(tenantId, materialLotVO2.getInstructionDocId());
        BeanUtils.copyProperties(qmsDocMaterialLotVO, materialLotVO);

        materialLotVO.setIqcHeaderId(materialLotVO2.getIqcHeaderId());
        materialLotVO.setInstructionLineDocId(materialLotVO2.getInstructionLineDocId());
        materialLotVO.setInstructionDocId(materialLotVO2.getInstructionDocId());
        materialLotVO.setMaterialLotId(materialLot.getMaterialLotId());
        materialLotVO.setMaterialLotCode(materialLot.getMaterialLotCode());
        materialLotVO.setLot(materialLot.getLot());

        if(StringUtils.isNotBlank(instructionDocNum) && !StringUtils.equals(materialLotVO.getInstructionDocNum(), instructionDocNum)){
            throw new MtException("QMS_QC_DOC_MATERIAL_LOT_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_QC_DOC_MATERIAL_LOT_008", "HME", materialLotVO.getInstructionDocNum(), instructionDocNum));
        }

        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialLot.getMaterialId());
        if (mtMaterial != null) {
            materialLotVO.setMaterialName(mtMaterial.getMaterialName());
            materialLotVO.setMaterialCode(mtMaterial.getMaterialCode());
        }
        if (materialLot.getPrimaryUomQty() != null) {
            materialLotVO.setPrimaryUomQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty()).longValue());
            materialLotVO.setMaxPrimaryUomQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty()).longValue());
        }

        MtUom mtUom = mtUomRepository.selectByPrimaryKey(materialLot.getPrimaryUomId());
        materialLotVO.setUomCode(mtUom != null ? mtUom.getUomCode() : "");
        materialLotVO.setQualityStatus(materialLot.getQualityStatus());
        //质量状态
        if(StringUtils.isNotBlank(materialLot.getQualityStatus())){
            List<LovValueDTO> qualityList = lovAdapter.queryLovValue("WMS.MTLOT.QUALITY_STATUS", tenantId);
            List<LovValueDTO> qualityStatusList = qualityList.stream().filter(f -> StringUtils.equals(f.getValue(), materialLot.getQualityStatus())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(qualityStatusList)){
            materialLotVO.setQualityStatusMeaning(qualityStatusList.get(0).getMeaning());
            }
        }
        return materialLotVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMaterialLotCode(Long tenantId, HmeChipTransferVO4 vo4) {
        if (CollectionUtils.isNotEmpty(vo4.getMaterialLotCodeList())) {
            List<QmsQcDocMaterialLotRel> materialLotRelList = qmsQcDocMaterialLotRelMapper.selectByCondition(Condition.builder(QmsQcDocMaterialLotRel.class)
                    .andWhere(Sqls.custom().andEqualTo(QmsQcDocMaterialLotRel.FIELD_TENANT_ID, tenantId)
                            .andIn(QmsQcDocMaterialLotRel.FIELD_MATERIAL_LOT_ID, vo4.getMaterialLotCodeList())).build());

            for (QmsQcDocMaterialLotRel qmsQcDocMaterialLotRel : materialLotRelList) {
                qmsQcDocMaterialLotRelMapper.deleteByPrimaryKey(qmsQcDocMaterialLotRel);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmMaterialLotCode(Long tenantId, QmsDocMaterialLotVO2 vo2) {
        QmsQcDocMaterialLotRel lotRel = new QmsQcDocMaterialLotRel();
        lotRel.setInstructionDocId(vo2.getInstructionDocId());
        lotRel.setInstructionId(vo2.getInstructionLineDocId());
        lotRel.setDocStatus("NEW");
        lotRel.setMaterialLotId(vo2.getMaterialLotId());
        lotRel.setQuantity(BigDecimal.valueOf(vo2.getPrimaryUomQty()));
        //质检单头
        lotRel.setIqcHeaderId(vo2.getIqcHeaderId());
        self().insertSelective(lotRel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QmsDocMaterialLotVO2 submitMaterialLotCode(Long tenantId, QmsDocMaterialLotVO2 vo2) {
        if (CollectionUtils.isNotEmpty(vo2.getMaterialLotVOList())) {
            //更新质检单状态
            QmsIqcHeader qmsIqcHeader = qmsIqcHeaderRepository.selectByPrimaryKey(vo2.getMaterialLotVOList().get(0).getIqcHeaderId());
            qmsIqcHeader.setInspectionStatus("TBSQ");
            qmsIqcHeaderMapper.updateByPrimaryKeySelective(qmsIqcHeader);

            MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(vo2.getMaterialLotVOList().get(0).getInstructionDocId());

            List<WmsDocLotRel> lotRelList = wmsDocLotRelRepository.selectByCondition(Condition.builder(WmsDocLotRel.class)
                    .andWhere(Sqls.custom().andEqualTo(WmsDocLotRel.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(WmsDocLotRel.FIELD_DOC_ID, mtInstructionDoc.getInstructionDocId())
                            .andEqualTo(WmsDocLotRel.FIELD_DOC_TYPE, "DELIVERY_DOC")).build());

            vo2.getMaterialLotVOList().forEach(material -> {
                MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, material.getMaterialLotCode());

                //插入数据
                QmsDocMaterialLotVO2 qmsDocMaterialLotVO2 = new QmsDocMaterialLotVO2();
                qmsDocMaterialLotVO2.setInstructionDocId(mtInstructionDoc.getInstructionDocId());
                qmsDocMaterialLotVO2.setInstructionLineDocId(material.getInstructionLineDocId());
                qmsDocMaterialLotVO2.setMaterialLotId(materialLot.getMaterialLotId());
                qmsDocMaterialLotVO2.setMaterialLotCode(materialLot.getMaterialLotCode());
                qmsDocMaterialLotVO2.setPrimaryUomQty(material.getPrimaryUomQty());
                qmsDocMaterialLotVO2.setIqcHeaderId(material.getIqcHeaderId());
                this.confirmMaterialLotCode(tenantId, qmsDocMaterialLotVO2);

                // 创建事件
                MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                eventCreateVO.setEventTypeCode("MATERIAL_LOT_PECKED");
                // 创建事件
                String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                //调用API {materialLotConsume} 条码更新
                MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                mtMaterialLotVO1.setMaterialLotId(materialLot.getMaterialLotId());
                double trxPrimaryUomQty = BigDecimal.valueOf(materialLot.getPrimaryUomQty()).subtract(BigDecimal.valueOf(material.getPrimaryUomQty())).doubleValue();
                mtMaterialLotVO1.setTrxPrimaryUomQty(trxPrimaryUomQty);
                mtMaterialLotVO1.setParentEventId(eventId);
                mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);

                //更新状态
                List<QmsQcDocMaterialLotRel> materialLotRelList = qmsQcDocMaterialLotRelMapper.selectByCondition(Condition.builder(QmsQcDocMaterialLotRel.class)
                                .andWhere(Sqls.custom().andEqualTo(QmsQcDocMaterialLotRel.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(QmsQcDocMaterialLotRel.FIELD_MATERIAL_LOT_ID, materialLot.getMaterialLotId())).build());
                for (QmsQcDocMaterialLotRel lotRel : materialLotRelList) {
                    lotRel.setDocStatus("COMPLETED");
                    qmsQcDocMaterialLotRelMapper.updateByPrimaryKeySelective(lotRel);
                }
            });

            QmsIqcHeader iqcHeader = new QmsIqcHeader();
            iqcHeader.setSiteId(mtInstructionDoc.getSiteId());
            if (CollectionUtils.isNotEmpty(lotRelList)) {
                iqcHeader.setReceiptLot(lotRelList.get(0).getLot());
            }
            iqcHeader.setReceiptBy(String.valueOf(DetailsHelper.getUserDetails().getUserId()));
            iqcHeader.setDocType("IQC_DOC");
            if (WmsConstant.CONSTANT_Y.equals(qmsIqcHeader.getUaiFlag())) {
                iqcHeader.setUaiFlag(WmsConstant.CONSTANT_Y);
            }
            iqcHeader.setDocHeaderId(qmsIqcHeader.getIqcHeaderId());
            iqcHeader.setDocLineId("");
            iqcHeader.setMaterialId(qmsIqcHeader.getMaterialId());
            iqcHeader.setMaterialVersion(qmsIqcHeader.getMaterialVersion());

            List<String> materialList = vo2.getMaterialLotVOList().stream().map(QmsDocMaterialLotVO::getMaterialLotId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(materialList)){
                List<QmsQcDocMaterialLotRel> docMaterialLotRelList = qmsQcDocMaterialLotRelMapper.selectByCondition(Condition.builder(QmsQcDocMaterialLotRel.class)
                        .andWhere(Sqls.custom().andEqualTo(QmsQcDocMaterialLotRel.FIELD_TENANT_ID, tenantId)
                                .andIn(QmsQcDocMaterialLotRel.FIELD_MATERIAL_LOT_ID, materialList)
                                .andEqualTo(QmsQcDocMaterialLotRel.FIELD_IQC_HEADER_ID, qmsIqcHeader.getIqcHeaderId())).build());
                double quantity = docMaterialLotRelList.stream().map(QmsQcDocMaterialLotRel::getQuantity).filter(Objects::nonNull)
                        .collect(Collectors.toList()).stream().mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                iqcHeader.setQuantity(BigDecimal.valueOf(quantity));
                iqcHeader.setUomId(qmsIqcHeader.getUomId());
                iqcHeader.setSupplierId(qmsIqcHeader.getSupplierId());
                iqcHeader.setLocatorId(qmsIqcHeader.getLocatorId());
                iqcHeader.setCreatedDate(new Date());
                if (WmsConstant.CONSTANT_Y.equals(qmsIqcHeader.getIdentification())) {
                    iqcHeader.setIdentification(WmsConstant.CONSTANT_Y);
                }

                try {
                    qmsIqcHeaderRepository.createIqcBill(tenantId, iqcHeader);
                } catch (Exception e) {
                    log.info("生成质检单报错!");
                }
            }
            //清空对应条码的明细中ngqty的值
            List<String> materialLotIdList = vo2.getMaterialLotVOList().stream().map(QmsDocMaterialLotVO::getMaterialLotId).collect(Collectors.toList());
            String actualId = qmsIqcCheckPlatformMapper.getActualIdByInstruction(tenantId, qmsIqcHeader.getDocLineId());
            if(StringUtils.isNotBlank(actualId)){
                List<MtInstructionActualDetail> mtInstructionActualDetailList = instructionActualDetailMapper.getActualDetailId(tenantId,actualId);
                if(CollectionUtils.isNotEmpty(mtInstructionActualDetailList)){
                    MtEventCreateVO eventCreate = new MtEventCreateVO();
                    eventCreate.setEventTypeCode("MATERIAL_LOT_PECKED");
                    String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
                    List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
                    List<MtCommonExtendVO5> attrs = new ArrayList<>();
                    MtCommonExtendVO5 mtCommonExtendVO5 = new MtCommonExtendVO5();
                    mtCommonExtendVO5.setAttrName("NG_QTY");
                    mtCommonExtendVO5.setAttrValue("");
                    attrs.add(mtCommonExtendVO5);
                    for(MtInstructionActualDetail mtInstructionActualDetail: mtInstructionActualDetailList){
                        MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
                        mtCommonExtendVO6.setKeyId(mtInstructionActualDetail.getActualDetailId());
                        mtCommonExtendVO6.setAttrs(attrs);
                        attrPropertyList.add(mtCommonExtendVO6);
                    }
                    mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId,"mt_instruct_act_detail_attr",eventId,attrPropertyList);
                }
            }
        }
        return vo2;
    }

    private QmsDocMaterialLotVO2 checkMaterialLot(Long tenantId, MtMaterialLot mtMaterialLot) {
        QmsDocMaterialLotVO2 materialLotVO2 = new QmsDocMaterialLotVO2();
        //存在性
        if (mtMaterialLot == null) {
            throw new MtException("MT_ASSEMBLE_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0017", "ASSEMBLE", "api【checkMaterialLot】"));
        }

        //条码状态
        List<MtExtendSettings> attrList = new ArrayList<>();
        MtExtendSettings mtExtendSettings = new MtExtendSettings();
        mtExtendSettings.setAttrName("STATUS");
        attrList.add(mtExtendSettings);
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                "mt_material_lot_attr", "MATERIAL_LOT_ID", mtMaterialLot.getMaterialLotId(), attrList);
        if (CollectionUtils.isEmpty(mtExtendAttrVOList) || !(StringUtils.equals("TO_ACCEPT", mtExtendAttrVOList.get(0).getAttrValue()) || StringUtils.equals("MINSTOCK", mtExtendAttrVOList.get(0).getAttrValue()))) {
            throw new MtException("QMS_QC_DOC_MATERIAL_LOT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_QC_DOC_MATERIAL_LOT_001", "HME"));
        }

        //是否有效
        if (!StringUtils.equals(QmsConstants.ConstantValue.YES, mtMaterialLot.getEnableFlag())) {
            throw new MtException("QMS_QC_DOC_MATERIAL_LOT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_QC_DOC_MATERIAL_LOT_002", "HME"));
        }

        //质量状态
        if (!StringUtils.equals("PENDING", mtMaterialLot.getQualityStatus())) {
            throw new MtException("QMS_QC_DOC_MATERIAL_LOT_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_QC_DOC_MATERIAL_LOT_003", "HME", mtMaterialLot.getMaterialLotCode()));
        }

        //是否关联送货单
        MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
        mtInstructionActualDetail.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        List<MtInstructionActualDetail> detailList = mtInstructionActualDetailRepository.select(mtInstructionActualDetail);
        if (CollectionUtils.isEmpty(detailList)) {
            throw new MtException("QMS_QC_DOC_MATERIAL_LOT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_QC_DOC_MATERIAL_LOT_004", "HME", mtMaterialLot.getMaterialLotCode()));
        }

        //多个送货单 报错
        List<MtInstructionDoc> mtInstructionDocs = qmsQcDocMaterialLotRelMapper.queryInstructionDocInfo(tenantId, detailList.get(0).getActualId());
        if (CollectionUtils.isEmpty(mtInstructionDocs) || mtInstructionDocs.size() > 1) {
            throw new MtException("QMS_QC_DOC_MATERIAL_LOT_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_QC_DOC_MATERIAL_LOT_005", "HME", mtMaterialLot.getMaterialLotCode()));
        }

        //所关联送货单的状态
        MtInstructionDoc mtInstructionDoc = mtInstructionDocs.get(0);
        if (!StringUtils.equals("RECEIVE_COMPLETE", mtInstructionDoc.getInstructionDocStatus())) {
            throw new MtException("QMS_QC_DOC_MATERIAL_LOT_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_QC_DOC_MATERIAL_LOT_006", "HME", mtMaterialLot.getMaterialLotCode()));
        }

        //质检单状态
        MtInstruction mtInstruction = new MtInstruction();
        mtInstruction.setSourceDocId(mtInstructionDoc.getInstructionDocId());
        mtInstruction.setMaterialId(mtMaterialLot.getMaterialId());
        List<MtInstruction> mtInstructionList = mtInstructionRepository.select(mtInstruction);

        for (MtInstruction instruction : mtInstructionList) {
            QmsIqcHeader qmsIqcHeader = new QmsIqcHeader();
            qmsIqcHeader.setDocLineId(instruction.getInstructionId());
            qmsIqcHeader.setDocType("DELIVERY_DOC");
            List<QmsIqcHeader> qmsIqcHeaderList = qmsIqcHeaderRepository.select(qmsIqcHeader);
            materialLotVO2.setInstructionLineDocId(CollectionUtils.isNotEmpty(qmsIqcHeaderList) ? qmsIqcHeaderList.get(0).getIqcHeaderId() : "");
            for (QmsIqcHeader iqcHeader : qmsIqcHeaderList) {
                if (!StringUtils.equals(iqcHeader.getInspectionStatus(), "TBP")) {
                    throw new MtException("QMS_QC_DOC_MATERIAL_LOT_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "QMS_QC_DOC_MATERIAL_LOT_007", "HME", mtMaterialLot.getMaterialLotCode()));
                }
                materialLotVO2.setIqcHeaderId(iqcHeader.getIqcHeaderId());
            }
        }
        materialLotVO2.setInstructionLineDocId(CollectionUtils.isNotEmpty(mtInstructionList) ? mtInstructionList.get(0).getInstructionId() : "");
        materialLotVO2.setInstructionDocId(mtInstructionDoc.getInstructionDocId());
        return materialLotVO2;
    }
}
