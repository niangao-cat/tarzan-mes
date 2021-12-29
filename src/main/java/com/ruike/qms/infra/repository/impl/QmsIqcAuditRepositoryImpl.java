package com.ruike.qms.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeInspectorItemGroupRel;
import com.ruike.hme.domain.repository.HmeInspectorItemGroupRelRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.qms.api.dto.QmsIqcAuditDTO;
import com.ruike.qms.api.dto.QmsIqcAuditQueryDTO;
import com.ruike.qms.domain.entity.*;
import com.ruike.qms.domain.repository.QmsInspectionLevelsRecordRepository;
import com.ruike.qms.domain.repository.QmsIqcAuditRepository;
import com.ruike.qms.domain.repository.QmsIqcHeaderHisRepository;
import com.ruike.qms.domain.repository.QmsTransitionRuleRepository;
import com.ruike.qms.domain.vo.QmsIqcAuditQueryLineVO;
import com.ruike.qms.domain.vo.QmsIqcAuditQueryVO;
import com.ruike.qms.domain.vo.QmsIqcAuditQueryVO2;
import com.ruike.qms.domain.vo.QmsIqcSelectLimitVO;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsInspectionLevelsRecordMapper;
import com.ruike.qms.infra.mapper.QmsIqcAuditMapper;
import com.ruike.qms.infra.mapper.QmsIqcCheckPlatformMapper;
import com.ruike.qms.infra.mapper.QmsIqcHeaderMapper;
import com.ruike.wms.domain.entity.WmsPutInStorageTask;
import com.ruike.wms.domain.repository.WmsDocLotRelRepository;
import com.ruike.wms.domain.repository.WmsPutInStorageTaskRepository;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.inventory.domain.entity.MtInvOnhandQuantity;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: tarzan-mes
 * @description: iqc审核repo实现层
 * @author: han.zhang
 * @create: 2020/05/19 11:42
 */
@Component
public class QmsIqcAuditRepositoryImpl implements QmsIqcAuditRepository {
    /**
     * QMS模块报错的服务包
     */
    private static final String MODULE_CODE = "QMS";
    @Autowired
    private QmsIqcAuditMapper qmsIqcAuditMapper;
    @Autowired
    private MtUserClient mtUserClient;
    @Autowired
    private QmsIqcHeaderMapper qmsIqcHeaderMapper;
    @Autowired
    private WmsPutInStorageTaskRepository wmsPutInStorageTaskRepository;
    @Autowired
    private QmsInspectionLevelsRecordRepository qmsInspectionLevelsRecordRepository;
    @Autowired
    private QmsInspectionLevelsRecordMapper qmsInspectionLevelsRecordMapper;
    @Autowired
    private QmsTransitionRuleRepository qmsTransitionRuleRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private QmsIqcHeaderHisRepository qmsIqcHeaderHisRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private WmsDocLotRelRepository wmsDocLotRelRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private HmeInspectorItemGroupRelRepository hmeInspectorItemGroupRelRepository;

    @Autowired
    private QmsIqcCheckPlatformMapper qmsIqcCheckPlatformMapper;

    @Autowired
    private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;

    @Override
    @ProcessLovValue
    public Page<QmsIqcAuditQueryVO> selectIqcHeader(Long tenantId, QmsIqcAuditQueryDTO dto, PageRequest pageRequest) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //2021-04-02 09:47 edit by chaonan.hu for kang.wang 当登录用户在检验员与物料组关系表中存在有效数据时，增加此限制条件
        int count = hmeInspectorItemGroupRelRepository.selectCount(new HmeInspectorItemGroupRel() {{
            setTenantId(tenantId);
            setUserId(userId);
            setPrivilegeType("CHECKER");
            setEnableFlag("Y");
        }});
        if(count > 0){
            dto.setRelFlag("REL");
        }
        Page<QmsIqcAuditQueryVO> iqcHeaders = PageHelper.doPage(pageRequest,
                () -> qmsIqcAuditMapper.selectIqcHeader(tenantId, dto, userId));

        iqcHeaders.stream().forEach(item -> {
            MtUserInfo mtUserInfo = mtUserClient.userInfoGet(tenantId, Long.valueOf(item.getReceiptBy()));
            item.setReceiptRealName(mtUserInfo.getRealName());
        });

        return iqcHeaders;
    }

    @Override
    @ProcessLovValue
    public Page<QmsIqcAuditQueryLineVO> selectIqcLine(Long tenantId, String iqcHeaderId, PageRequest pageRequest) {
        Page<QmsIqcAuditQueryLineVO> iqcHeaders = PageHelper.doPage(pageRequest,
                () -> qmsIqcAuditMapper.selectIqcLine(tenantId, iqcHeaderId));

        return iqcHeaders;
    }

    @Override
    public Page<QmsIqcDetails> selectIqcDetail(Long tenantId, String iqcLineId, PageRequest pageRequest) {
        Page<QmsIqcDetails> iqcDetails = PageHelper.doPage(pageRequest,
                () -> qmsIqcAuditMapper.selectIqcDetail(tenantId, iqcLineId));
        return iqcDetails;
    }

    @Override
    public void handleMaterialLot(Long tenantId, QmsIqcHeader qmsIqcHeader, String eventId, String qualityStatus, String enableFlag, String attrValueStr) {
        //检验来源
        String sourceDocId = "";
        String instructionId = "";
        if (StringUtils.equals(qmsIqcHeader.getDocType(), QmsConstants.DocType.DELIVERY_DOC)) {
            //送货单
            sourceDocId = qmsIqcHeader.getDocHeaderId();
            instructionId = qmsIqcHeader.getDocLineId();
        } else if (StringUtils.equals(qmsIqcHeader.getDocType(), QmsConstants.DocType.IQC_DOC)) {
            //质检单 往上找到送货单
            QmsIqcHeader header = qmsIqcHeaderMapper.selectByPrimaryKey(qmsIqcHeader.getDocHeaderId());
            if(header == null){
                throw new MtException("QMS_MATERIAL_INSP_P0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0042", MODULE_CODE));
            }
            sourceDocId = header.getDocHeaderId();
            instructionId = header.getDocLineId();

            //原质检单置为完成状态
            header.setInspectionStatus(QmsConstants.DocStatus.COMPLETED);
            qmsIqcHeaderMapper.updateByPrimaryKeySelective(header);
        }

        Condition condition = new Condition(MtInstruction.class);
        condition.and().andEqualTo("sourceDocId", sourceDocId)
                .andEqualTo("instructionId", instructionId);
        List<MtInstruction> mtInstructions = mtInstructionRepository.selectByCondition(condition);
        String toLocatorId = "";
        if (CollectionUtils.isNotEmpty(mtInstructions)) {
            toLocatorId = mtInstructions.get(0).getToLocatorId();
        }

        MtExtendVO extendVO = new MtExtendVO();
        extendVO.setTableName("mt_mod_locator_attr");
        extendVO.setAttrName("RECEIVE_METHOD");
        extendVO.setKeyId(toLocatorId);
        // 找到实际存在的属性值
        List<MtExtendAttrVO> attrValueList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);

        String attrValue = CollectionUtils.isNotEmpty(attrValueList) ? attrValueList.get(0).getAttrValue() : "";

        if (StringUtils.isBlank(attrValue)) {
            attrValue = "M_STOCKIN";
        }

        if (StringUtils.equals("M_STOCKIN", attrValue)) {
            List<String> materialLotIdList = qmsIqcHeaderHisRepository.queryMaterialLotIdByLine(tenantId, instructionId);
            for (String materialLotId : materialLotIdList) {
                //调用API {materialLotUpdate} 条码更新
                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setMaterialLotId(materialLotId);
                mtMaterialLotVO2.setQualityStatus(qualityStatus);
                if(StringUtils.isNotBlank(enableFlag)){
                    mtMaterialLotVO2.setEnableFlag(enableFlag);
                }
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
            }
        } else if (StringUtils.equals("A_STOCKIN", attrValue)) {
            List<String> materialLotIdList = qmsIqcHeaderHisRepository.queryMaterialLotIdByLine(tenantId, instructionId);
            for (String materialLotId : materialLotIdList) {
                //调用API {materialLotUpdate} 条码更新
                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setMaterialLotId(materialLotId);
                mtMaterialLotVO2.setQualityStatus(qualityStatus);
                if(StringUtils.isNotBlank(enableFlag)){
                    mtMaterialLotVO2.setEnableFlag(enableFlag);
                }
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

                List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
                MtExtendVO5 deadLineDateAttr = new MtExtendVO5();
                deadLineDateAttr.setAttrName("STATUS");
                deadLineDateAttr.setAttrValue(attrValueStr);
                mtExtendVO5List.add(deadLineDateAttr);
                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {
                    {
                        setKeyId(materialLotId);
                        setEventId(eventId);
                        setAttrs(mtExtendVO5List);
                    }
                });
            }
        }
    }

    @Override
    public List<QmsIqcAuditQueryVO2> exportIqcHeader(Long tenantId, QmsIqcAuditQueryDTO dto) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        int count = hmeInspectorItemGroupRelRepository.selectCount(new HmeInspectorItemGroupRel() {{
            setTenantId(tenantId);
            setUserId(userId);
            setPrivilegeType("CHECKER");
            setEnableFlag("Y");
        }});
        if(count > 0){
            dto.setRelFlag("REL");
        }
        List<QmsIqcAuditQueryVO2> resultList = qmsIqcAuditMapper.exportIqcHeader(tenantId, dto, userId);
        if(CollectionUtils.isNotEmpty(resultList)){
            //检验类型值集
            List<LovValueDTO> inspectionTypeLov = lovAdapter.queryLovValue("QMS.DOC_INSPECTION_TYPE", tenantId);
            //处理状态值集
            List<LovValueDTO> inspectionDocStatusLov = lovAdapter.queryLovValue("QMS.INSPECTION_DOC_STATUS", tenantId);
            //审核结果值集
            List<LovValueDTO> finalDecisionLov = lovAdapter.queryLovValue("QMS.FINAL_DECISION", tenantId);
            //是否加急值集
            List<LovValueDTO> identificationLov = lovAdapter.queryLovValue("QMS.IDENTIFICATION", tenantId);
            for (QmsIqcAuditQueryVO2 result:resultList) {
                //接收人
                MtUserInfo mtUserInfo = mtUserClient.userInfoGet(tenantId, Long.valueOf(result.getReceiptBy()));
                result.setReceiptRealName(mtUserInfo.getRealName());
                //检验类型
                if(StringUtils.isNotBlank(result.getInspectionType())){
                    List<LovValueDTO> inspectionTypeList = inspectionTypeLov.stream().filter(item -> result.getInspectionType().equals(item.getValue())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(inspectionTypeList)){
                        result.setInspectionTypeMeaning(inspectionTypeList.get(0).getMeaning());
                    }
                }
                //处理状态
                if(StringUtils.isNotBlank(result.getInspectionStatus())){
                    List<LovValueDTO> inspectionStatusList = inspectionDocStatusLov.stream().filter(item -> result.getInspectionStatus().equals(item.getValue())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(inspectionStatusList)){
                        result.setInspectionStatusMeaning(inspectionStatusList.get(0).getMeaning());
                    }
                }
                //审核结果
                if(StringUtils.isNotBlank(result.getFinalDecision())){
                    List<LovValueDTO> finalDecisionList = finalDecisionLov.stream().filter(item -> result.getFinalDecision().equals(item.getValue())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(finalDecisionList)){
                        result.setFinalDecisionMeaning(finalDecisionList.get(0).getMeaning());
                    }
                }
                //是否加急
                if(StringUtils.isNotBlank(result.getIdentification())){
                    List<LovValueDTO> identificationList = identificationLov.stream().filter(item -> result.getIdentification().equals(item.getValue())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(identificationList)){
                        result.setIdentificationMeaning(identificationList.get(0).getMeaning());
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * 处理库存扣减
     *
     * @param tenantId          租户id
     * @param qmsIqcHeader      质检单头
     * @param eventId           事件id
     * @author sanfeng.zhang@hand-china.com 2020/8/7 10:54
     * @return void
     */
    private void handInventoryReduce(Long tenantId,QmsIqcHeader qmsIqcHeader,String eventId){
        MtEventCreateVO qcEvent = new MtEventCreateVO();
        //2020-10-22 10:52 edit by chaonan.hu for lu.bai 事件编码由QC_DOC_SUBMIT改为QC_RETURN
        String qcEventTypeCode = "QC_RETURN";
        qcEvent.setEventTypeCode(qcEventTypeCode);
        List<MtInstruction> mtInstructions = new ArrayList<>();
        if(WmsConstant.DocType.DELIVERY_DOC.equals(qmsIqcHeader.getDocType())){
            Condition condition = new Condition(MtInstruction.class);
            condition.and().andEqualTo("sourceDocId", qmsIqcHeader.getDocHeaderId())
                    .andEqualTo("instructionId", qmsIqcHeader.getDocLineId());
            mtInstructions = mtInstructionRepository.selectByCondition(condition);
        }else if(WmsConstant.DocType.IQC_DOC.equals(qmsIqcHeader.getDocType())){
            QmsIqcHeader qmsIqcHeader2 = qmsIqcHeaderMapper.selectByPrimaryKey(qmsIqcHeader.getDocHeaderId());
            Condition condition = new Condition(MtInstruction.class);
            condition.and().andEqualTo("sourceDocId", qmsIqcHeader2.getDocHeaderId())
                    .andEqualTo("instructionId", qmsIqcHeader2.getDocLineId());
            mtInstructions = mtInstructionRepository.selectByCondition(condition);
        }
        if(CollectionUtils.isEmpty(mtInstructions)){
            throw new MtException("QMS_MATERIAL_INSP_P0040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_P0040", MODULE_CODE));
        }
        String locatorId = qmsIqcAuditMapper.getLocatorIdByInstructionId(tenantId,mtInstructions.get(0).getInstructionId() );
        if(StringUtils.isEmpty(locatorId)){
            throw new MtException("QMS_MATERIAL_INSP_P0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_P0043", MODULE_CODE));
        }
        qcEvent.setLocatorId(locatorId);
        qcEvent.setParentEventId(eventId);
        String qcEventId = mtEventRepository.eventCreate(tenantId, qcEvent);

        // 20210525 add by sanfeng.zhang for kang.wang 带料废调换的检验单处理 有料废调换的不扣减库存 两个都有的 不良数大于现有量的  按现有量扣减 其他按原来逻辑
        MtInstruction mtInstruction = mtInstructions.get(0);
        // 执行数量
        BigDecimal qty = mtInstruction.getQuantity() != null ? BigDecimal.valueOf(mtInstruction.getQuantity()) : BigDecimal.ZERO;
        List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
            setKeyId(mtInstruction.getInstructionId());
            setAttrName("EXCHANGE_QTY");
            setTableName("mt_instruction_attr");
        }});
        // 料废调换数量
        BigDecimal exchangeQty = CollectionUtils.isNotEmpty(attrVOList) ? BigDecimal.valueOf(Double.valueOf(attrVOList.get(0).getAttrValue())) : BigDecimal.ZERO;

        String actualId = qmsIqcCheckPlatformMapper.getActualIdByInstruction(tenantId, mtInstruction.getInstructionId());
        if (StringUtils.isNotEmpty(actualId)) {
            List<MtInstructionActualDetail> mtInstructionActualDetailList = mtInstructionActualDetailRepository.select(new MtInstructionActualDetail() {{
                setTenantId(tenantId);
                setActualId(actualId);
            }});
            List<MtInvOnhandQuantityVO13> onhandList = new ArrayList();
            for (MtInstructionActualDetail instructionActualDetail : mtInstructionActualDetailList) {
                // 条码
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(instructionActualDetail.getMaterialLotId());
                if (BigDecimal.ZERO.compareTo(qty) < 0 && BigDecimal.ZERO.compareTo(exchangeQty) == 0) {
                    // 执行数量不为0 料废调换数量为0 扣减数量 为条码数量
                    // 批量扣减现有量API组装参数
                    MtInvOnhandQuantityVO13 mtInvOnhandQuantityVO13 = new MtInvOnhandQuantityVO13();
                    mtInvOnhandQuantityVO13.setSiteId(mtMaterialLot.getSiteId());
                    mtInvOnhandQuantityVO13.setMaterialId(mtMaterialLot.getMaterialId());
                    mtInvOnhandQuantityVO13.setLocatorId(mtMaterialLot.getLocatorId());
                    mtInvOnhandQuantityVO13.setLotCode(mtMaterialLot.getLot());
                    mtInvOnhandQuantityVO13.setChangeQuantity(mtMaterialLot.getPrimaryUomQty());
                    onhandList.add(mtInvOnhandQuantityVO13);
                    // 将条码数量清空
                    mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                        setTenantId(tenantId);
                        setMaterialLotId(mtMaterialLot.getMaterialLotId());
                        setPrimaryUomQty(BigDecimal.ZERO.doubleValue());
                        setEnableFlag(HmeConstants.ConstantValue.NO);
                        setEventId(qcEventId);
                    }}, "N");
                } else if (BigDecimal.ZERO.compareTo(qty) < 0 && BigDecimal.ZERO.compareTo(exchangeQty) < 0) {
                    // 执行数量不为0 料废调换数量不为0 查询现有量  按现有量扣减
                    // 基于条码上的物料+批次+货位获取库存现有量
                    List<MtInvOnhandQuantity> invOnhandQuantityList = mtInvOnhandQuantityRepository.select(new MtInvOnhandQuantity() {{
                        setTenantId(tenantId);
                        setLocatorId(mtMaterialLot.getLocatorId());
                        setMaterialId(mtMaterialLot.getMaterialId());
                        setLotCode(mtMaterialLot.getLot());
                    }});
                    Double sumOnhandQty = null;
                    if (CollectionUtils.isNotEmpty(invOnhandQuantityList)) {
                        sumOnhandQty = invOnhandQuantityList.stream().map(MtInvOnhandQuantity::getOnhandQuantity).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                    }
                    BigDecimal onhandQty = sumOnhandQty != null ? BigDecimal.valueOf(sumOnhandQty) : BigDecimal.ZERO;
                    if (onhandQty.compareTo(BigDecimal.ZERO) > 0) {
                        // 批量扣减现有量API组装参数 理论上只会扣减一次
                        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                        mtInvOnhandQuantityVO9.setSiteId(mtMaterialLot.getSiteId());
                        mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
                        mtInvOnhandQuantityVO9.setLocatorId(mtMaterialLot.getLocatorId());
                        mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
                        mtInvOnhandQuantityVO9.setChangeQuantity(onhandQty.doubleValue());
                        mtInvOnhandQuantityVO9.setEventId(qcEventId);
                        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
                    }
                    // 将条码数量清空
                    mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                        setTenantId(tenantId);
                        setMaterialLotId(mtMaterialLot.getMaterialLotId());
                        setPrimaryUomQty(BigDecimal.ZERO.doubleValue());
                        setEnableFlag(HmeConstants.ConstantValue.NO);
                        setEventId(qcEventId);
                    }}, "N");
                } else if (BigDecimal.ZERO.compareTo(qty) == 0 && BigDecimal.ZERO.compareTo(exchangeQty) < 0) {
                    // 执行数量为0 料废调换数量不为0 将条码数量清空
                    mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                        setTenantId(tenantId);
                        setMaterialLotId(instructionActualDetail.getMaterialLotId());
                        setPrimaryUomQty(BigDecimal.ZERO.doubleValue());
                        setEnableFlag(HmeConstants.ConstantValue.NO);
                        setEventId(qcEventId);
                    }}, "N");
                }
            }
            //批量扣减现有量
            if(CollectionUtils.isNotEmpty(onhandList)) {
                MtInvOnhandQuantityVO16 mtInvOnhandQuantityVO16 = new MtInvOnhandQuantityVO16();
                mtInvOnhandQuantityVO16.setEventId(qcEventId);
                mtInvOnhandQuantityVO16.setOnhandList(onhandList);
                mtInvOnhandQuantityRepository.onhandQtyUpdateBatchProcess(tenantId, mtInvOnhandQuantityVO16);
            }
        }
//        //库存扣减
//        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
//        mtInvOnhandQuantityVO9.setSiteId(qmsIqcHeader.getSiteId());
//        mtInvOnhandQuantityVO9.setLocatorId(locatorId);
//        mtInvOnhandQuantityVO9.setMaterialId(qmsIqcHeader.getMaterialId());
//        mtInvOnhandQuantityVO9.setChangeQuantity(qmsIqcHeader.getQuantity().doubleValue());
//        mtInvOnhandQuantityVO9.setEventId(qcEventId);
//        //2020-10-22 edit by chaonan.hu for lu.bai 增加lot传入
//        if(WmsConstant.DocType.DELIVERY_DOC.equals(qmsIqcHeader.getDocType())){
//            List<WmsDocLotRel> wmsDocLotRelList = wmsDocLotRelRepository.select(new WmsDocLotRel() {{
//                setTenantId(tenantId);
//                setDocType(WmsConstant.DocType.DELIVERY_DOC);
//                setDocId(qmsIqcHeader.getDocHeaderId());
//            }});
//            if(CollectionUtils.isEmpty(wmsDocLotRelList)){
//                throw new MtException("QMS_MATERIAL_INSP_P0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "QMS_MATERIAL_INSP_P0041", MODULE_CODE));
//            }
//            mtInvOnhandQuantityVO9.setLotCode(wmsDocLotRelList.get(0).getLot());
//        }else if(WmsConstant.DocType.IQC_DOC.equals(qmsIqcHeader.getDocType())){
//            QmsIqcHeader qmsIqcHeader2 = qmsIqcHeaderMapper.selectByPrimaryKey(qmsIqcHeader.getDocHeaderId());
//            List<WmsDocLotRel> wmsDocLotRelList = wmsDocLotRelRepository.select(new WmsDocLotRel() {{
//                setTenantId(tenantId);
//                setDocType(WmsConstant.DocType.DELIVERY_DOC);
//                setDocId(qmsIqcHeader2.getDocHeaderId());
//            }});
//            if(CollectionUtils.isEmpty(wmsDocLotRelList)){
//                throw new MtException("QMS_MATERIAL_INSP_P0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "QMS_MATERIAL_INSP_P0041", MODULE_CODE));
//            }
//            mtInvOnhandQuantityVO9.setLotCode(wmsDocLotRelList.get(0).getLot());
//        }
//        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(Long tenantId, QmsIqcAuditDTO auditDTO) {
        //2020-8-7 add by sanfeng.zhang 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        String eventTypeCode = "QC_DOC_EXAMINE";
        eventCreateVO.setEventTypeCode(eventTypeCode);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        //更新质检单头数据
        QmsIqcHeader qmsIqcHeader = new QmsIqcHeader();
        qmsIqcHeader.setIqcHeaderId(auditDTO.getIqcHeaderId());
        qmsIqcHeader.setAuditOpinion(auditDTO.getAuditOpinion());
        qmsIqcHeader.setFinalDecision(auditDTO.getFinalDecision());
        qmsIqcHeader.setObjectVersionNumber(auditDTO.getObjectVersionNumber());
        qmsIqcHeader.setInspectionStatus(auditDTO.getInspectionStatus());
        qmsIqcHeaderMapper.updateByPrimaryKeySelective(qmsIqcHeader);
        //查询一遍头数据后面用
        qmsIqcHeader = qmsIqcHeaderMapper.selectByPrimaryKey(qmsIqcHeader);

        //2020-8-7 add by sanfeng.zhang 生成质检单头历史记录
        QmsIqcHeaderHis headerHis = new QmsIqcHeaderHis();
        BeanUtils.copyProperties(qmsIqcHeader, headerHis);
        headerHis.setEventId(eventId);
        qmsIqcHeaderHisRepository.createQmsIqcHeaderHis(headerHis);
        //让步 退货处理 挑选不改变
        if (StringUtils.equals("RB", auditDTO.getFinalDecision())) {
            //2020-8-7 add by sanfeng.zhang 让步-送货单下条码处理
            this.handleMaterialLot(tenantId,qmsIqcHeader,eventId,"OK","","TO_ACCEPT");

        } else if (StringUtils.equals("TH", auditDTO.getFinalDecision())) {
            //2020-8-7 add by sanfeng.zhang 退货
            this.handleMaterialLot(tenantId, qmsIqcHeader, eventId, "NG", "N", "TO_SCRAP");

            //2020-8-7 add by sanfeng.zhang 库存处理：扣减待收区库存
            this.handInventoryReduce(tenantId, qmsIqcHeader, eventId);
        } else if (QmsConstants.FinalDecision.FX.equals(auditDTO.getFinalDecision())) {
            // 现场返修-送货单下条码处理
            this.handleMaterialLot(tenantId, qmsIqcHeader, eventId, "OK", "", "TO_ACCEPT");
        }

        //让步或者现场返修的时候 插入入库上架的数据
        String RB = "RB";
        String FX = "FX";
        if (RB.equals(auditDTO.getFinalDecision()) || FX.equals(auditDTO.getFinalDecision())) {
            String taskStatus = "STOCK_PENDING";
            WmsPutInStorageTask wmsPutInStorageTask = new WmsPutInStorageTask();
            wmsPutInStorageTask.setTaskType(qmsIqcHeader.getDocType());
            wmsPutInStorageTask.setInstructionDocId(qmsIqcHeader.getIqcHeaderId());
            wmsPutInStorageTask.setInstructionId(qmsIqcHeader.getDocLineId());
            wmsPutInStorageTask.setTaskStatus(taskStatus);
            wmsPutInStorageTask.setInstructionDocType(qmsIqcHeader.getDocType());
            wmsPutInStorageTask.setMaterialId(qmsIqcHeader.getMaterialId());
            wmsPutInStorageTask.setTaskQty(qmsIqcHeader.getQuantity());
            wmsPutInStorageTask.setExecuteQty(BigDecimal.ZERO);
            wmsPutInStorageTaskRepository.insertSelective(wmsPutInStorageTask);
        }
        //供应商加严放宽记录QMS_INSPECTION_LEVELS_RECORD
        //2020/12/10 add by sanfeng.zhang for zhangjian 去掉加严放宽校验
        //updateInspectionLeveles(tenantId, qmsIqcHeader);
    }

    /**
     * @param qmsIqcHeader 检验头数据
     * @return void
     * @Description 根据情况新增或处理加严放宽记录
     * @Date 2020-05-20 10:00
     * @Author han.zhang
     */
    private void updateInspectionLeveles(Long tenantId, QmsIqcHeader qmsIqcHeader) {
        //先按照组织+物料+供应商的维度在本表进行查询
        QmsInspectionLevelsRecord qmsInspectionLevelsRecord = new QmsInspectionLevelsRecord();
        qmsInspectionLevelsRecord.setSiteId(qmsIqcHeader.getSiteId());
        qmsInspectionLevelsRecord.setMaterialId(qmsIqcHeader.getMaterialId());
        qmsInspectionLevelsRecord.setSupplierId(qmsIqcHeader.getSupplierId());
        int count = qmsInspectionLevelsRecordRepository.selectCount(qmsInspectionLevelsRecord);
        //如果没有查询到数据，则认为不进行加严放宽，并向该表插入数据
        if (QmsConstants.ConstantValue.ZERO == count) {
            qmsInspectionLevelsRecord.setInspectionLevels(String.valueOf(QmsConstants.ConstantValue.ZERO));
            qmsInspectionLevelsRecord.setInspectionMethod(QmsConstants.InspectionMethod.NORMAL);
            qmsInspectionLevelsRecordRepository.insertSelective(qmsInspectionLevelsRecord);
        }
        QmsTransitionRule qmsTransitionRule = new QmsTransitionRule();
        qmsTransitionRule.setSiteId(qmsIqcHeader.getSiteId());
        qmsTransitionRule.setMaterialId(qmsIqcHeader.getMaterialId());
        qmsTransitionRule = qmsTransitionRuleRepository.selectOne(qmsTransitionRule);
        if (Objects.isNull(qmsTransitionRule)) {
            List<QmsTransitionRule> ruleList = qmsIqcAuditMapper.queryTransitionRule(tenantId,qmsIqcHeader.getSiteId());
            if(CollectionUtils.isEmpty(ruleList)){
                //该组织物料供应商未维护检验水平转移规则
                throw new MtException("QMS_MATERIAL_INSP_P0038",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0038",
                                MODULE_CODE));
            }

            qmsTransitionRule = ruleList.get(0);
        }
        //加严不合格限
        Long K = qmsTransitionRule.getNgBatches();
        //加严连续批
        Long N = qmsTransitionRule.getTightenedBatches();
        //放宽连续批
        Long P = qmsTransitionRule.getRelaxationBatches();
        //查找为NG的数据量M
        QmsIqcSelectLimitVO qmsIqcSelectLimitVO = new QmsIqcSelectLimitVO();
        qmsIqcSelectLimitVO.setMaterialId(qmsIqcHeader.getMaterialId());
        qmsIqcSelectLimitVO.setSupplierId(qmsIqcHeader.getSupplierId());
        qmsIqcSelectLimitVO.setLimitCount(N);
        Long M = qmsIqcAuditMapper.selectIqcByLimit(tenantId, qmsIqcSelectLimitVO);
        //加严放宽记录
        qmsInspectionLevelsRecord = qmsInspectionLevelsRecordRepository.selectOne(qmsInspectionLevelsRecord);
        //判断检验水平是不是数字
        boolean numeric = CommonUtils.isNumeric(qmsInspectionLevelsRecord.getInspectionLevels());
        if (!numeric) {
            //该组织物料供应商的检验水平不是数字
            throw new MtException("QMS_MATERIAL_INSP_P0039",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0039",
                            MODULE_CODE));
        }
        long inspectionLeve = Long.parseLong(qmsInspectionLevelsRecord.getInspectionLevels());
        if (M.compareTo(K) >= 0) {
            //超过不合格限，检验等级减一
            inspectionLeve -= 1;
            qmsInspectionLevelsRecord.setInspectionLevels(String.valueOf(inspectionLeve));
        } else {
            QmsIqcSelectLimitVO vo2 = new QmsIqcSelectLimitVO();
            vo2.setMaterialId(qmsIqcHeader.getMaterialId());
            vo2.setSupplierId(qmsIqcHeader.getSupplierId());
            vo2.setLimitCount(P);
            Long c = qmsIqcAuditMapper.selectIqcByLimit(tenantId, qmsIqcSelectLimitVO);
            if (c.longValue() == 0) {
                inspectionLeve += 1;
                qmsInspectionLevelsRecord.setInspectionLevels(String.valueOf(inspectionLeve));
            }
        }
        if (inspectionLeve > 0) {
            qmsInspectionLevelsRecord.setInspectionMethod(QmsConstants.InspectionMethod.RELAXATION);
        } else if (inspectionLeve < 0) {
            qmsInspectionLevelsRecord.setInspectionMethod(QmsConstants.InspectionMethod.TIGHTENED);
        } else {
            qmsInspectionLevelsRecord.setInspectionMethod(QmsConstants.InspectionMethod.NORMAL);
        }
        qmsInspectionLevelsRecordMapper.updateByPrimaryKeySelective(qmsInspectionLevelsRecord);
    }

}