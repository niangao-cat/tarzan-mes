package com.ruike.qms.app.service.impl;

import com.ruike.hme.domain.entity.HmeInspectorItemGroupRel;
import com.ruike.hme.domain.repository.HmeInspectorItemGroupRelRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.qms.api.dto.*;
import com.ruike.qms.app.service.QmsIqcCheckPlatformService;
import com.ruike.qms.domain.entity.*;
import com.ruike.qms.domain.repository.*;
import com.ruike.qms.domain.vo.*;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.*;
import com.ruike.wms.app.service.WmsEventService;
import com.ruike.wms.domain.entity.WmsPutInStorageTask;
import com.ruike.wms.domain.repository.WmsMaterialLotRepository;
import com.ruike.wms.domain.repository.WmsPutInStorageTaskRepository;
import com.ruike.wms.domain.vo.WmsEventVO;
import com.ruike.wms.domain.vo.WmsMaterialLotExtendAttrVO;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.*;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.util.PageUtil;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.file.FileClient;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.inventory.domain.entity.MtInvOnhandQuantity;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description IQC检验平台
 * @Author tong.li
 * @Date 2020/5/12 10:32
 * @Version 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QmsIqcCheckPlatformServiceImpl implements QmsIqcCheckPlatformService {

    @Autowired
    private QmsIqcCheckPlatformMapper qmsIqcCheckPlatformMapper;

    @Autowired
    private MtUserRepository mtUserRepository;

    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private QmsIqcHeaderRepository qmsIqcHeaderRepository;

    @Autowired
    private QmsIqcLineRepository qmsIqcLineRepository;

    @Autowired
    private QmsIqcDetailsRepository qmsIqcDetailsRepository;

    @Autowired
    private QmsIqcHeaderMapper qmsIqcHeaderMapper;

    @Autowired
    private QmsIqcLineMapper qmsIqcLineMapper;

    @Autowired
    private QmsIqcDetailsMapper qmsIqcDetailsMapper;

    @Autowired
    private WmsPutInStorageTaskRepository wmsPutInStorageTaskRepository;

    @Autowired
    private QmsInspectionLevelsRecordRepository qmsInspectionLevelsRecordRepository;

    @Autowired
    private QmsSampleTypeRepository qmsSampleTypeRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private QmsSampleSchemeRepository sampleSchemeRepository;

    @Autowired
    private QmsTransitionRuleRepository qmsTransitionRuleRepository;

    @Autowired
    private QmsInspectionLevelsRecordMapper qmsInspectionLevelsRecordMapper;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private QmsIqcHeaderHisRepository qmsIqcHeaderHisRepository;

    @Autowired
    private QmsIqcLineHisRepository qmsIqcLineHisRepository;

    @Autowired
    private QmsIqcDetailsHisRepository qmsIqcDetailsHisRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private QmsIqcAuditRepository qmsIqcAuditRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;

    @Autowired
    private MtInstructionActualDetailRepository instructionActualDetailRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private QmsIqcCheckPlatformRepository iqcCheckPlatformRepository;

    @Autowired
    private WmsMaterialLotRepository materialLotRepository;

    @Autowired
    private WmsEventService wmsEventService;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private HmeInspectorItemGroupRelRepository hmeInspectorItemGroupRelRepository;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Override
    @ProcessLovValue
    public Page<QmsIqcCheckPlatformMainReturnDTO> mainQuery(Long tenantId, PageRequest pageRequest, QmsIqcCheckPlatformDTO qmsIqcCheckPlatformDTO) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //2021-04-02 09:47 edit by chaonan.hu for kang.wang 当登录用户在检验员与物料组关系表中存在有效数据时，增加此限制条件
        int count = hmeInspectorItemGroupRelRepository.selectCount(new HmeInspectorItemGroupRel() {{
            setTenantId(tenantId);
            setUserId(userId);
            setPrivilegeType("INSPECTOR");
            setEnableFlag("Y");
        }});
        if(count > 0){
            qmsIqcCheckPlatformDTO.setRelFlag("REL");
        }
        Page<QmsIqcCheckPlatformMainReturnDTO> resultList = PageHelper.doPage(pageRequest, () -> qmsIqcCheckPlatformMapper.mainQuery(tenantId, qmsIqcCheckPlatformDTO, userId));
        //批量等级
        List<String> instructionIdList = resultList.stream().map(QmsIqcCheckPlatformMainReturnDTO::getInstructionId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<QmsIqcGradeVO> qmsIqcGradeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(instructionIdList)) {
            qmsIqcGradeList = qmsIqcCheckPlatformMapper.batchQueryGrade(tenantId, instructionIdList);
        }
        Map<String, List<QmsIqcGradeVO>> qmsIqcGradeMap = qmsIqcGradeList.stream().collect(Collectors.groupingBy(dto -> dto.getInstructionId()));
        for (QmsIqcCheckPlatformMainReturnDTO qmsIqcCheckPlatformMainReturnDTO : resultList) {
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(qmsIqcCheckPlatformMainReturnDTO.getReceiptBy()));
            if (mtUserInfo != null) {
                qmsIqcCheckPlatformMainReturnDTO.setReceiptBy(mtUserInfo.getRealName());
            }
            //检验结果
            if (StringUtils.isNotEmpty(qmsIqcCheckPlatformMainReturnDTO.getInspectionResult())) {
                String meaning = lovAdapter.queryLovMeaning("QMS.INSPECTION_RESULT", tenantId, qmsIqcCheckPlatformMainReturnDTO.getInspectionResult());
                qmsIqcCheckPlatformMainReturnDTO.setInspectionResultMeaning(meaning);
            }
            //供应商批次
            String instructionId = null;
            if(WmsConstant.DocType.DELIVERY_DOC.equals(qmsIqcCheckPlatformMainReturnDTO.getDocType())){
                //如果检验单类型DOC_TYPE 为DELIVERY_DOC,
                instructionId = qmsIqcCheckPlatformMainReturnDTO.getDocLineId();
            }else if(WmsConstant.DocType.IQC_DOC.equals(qmsIqcCheckPlatformMainReturnDTO.getDocType())){
                //如果DOC_TYPE为IQC_DOC,则根据DOC_HEADER_ID关联QMS_IQC_HEADER得到DOC_LINE_ID
                QmsIqcHeader qmsIqcHeader = qmsIqcHeaderRepository.selectByPrimaryKey(qmsIqcCheckPlatformMainReturnDTO.getDocHeaderId());
                instructionId = qmsIqcHeader.getDocLineId();
            }
            if(StringUtils.isNotBlank(instructionId)){
                List<String> supplierLot = qmsIqcCheckPlatformMapper.getSupplierLotByInstructionId(tenantId, instructionId);
                if(CollectionUtils.isNotEmpty(supplierLot)){
                    qmsIqcCheckPlatformMainReturnDTO.setSupplierLot(StringUtils.join(supplierLot, ";"));
                }
            }
            // 检验组信息 （与业务确认：测试环境数据会存在多条、正式不会）
            List<QmsIqcTagGroupVO> qmsIqcTagGroupVoList = qmsIqcCheckPlatformMapper.queryTagGroupDescriptionByMaterial2(qmsIqcCheckPlatformMainReturnDTO.getMaterialId(),
                    qmsIqcCheckPlatformMainReturnDTO.getMaterialVersion(), qmsIqcCheckPlatformMainReturnDTO.getInspectionType());
            if (CollectionUtils.isNotEmpty(qmsIqcTagGroupVoList)) {
                qmsIqcCheckPlatformMainReturnDTO.setTagGroupCode(qmsIqcTagGroupVoList.get(0).getTagGroupCode());
                qmsIqcCheckPlatformMainReturnDTO.setTagGroupDescription(qmsIqcTagGroupVoList.get(0).getTagGroupDescription());
            }
            //等级汇总
            List<QmsIqcGradeVO> qmsIqcGradeVOList = qmsIqcGradeMap.get(qmsIqcCheckPlatformMainReturnDTO.getInstructionId());
            if (CollectionUtils.isNotEmpty(qmsIqcGradeVOList)) {
                qmsIqcCheckPlatformMainReturnDTO.setGrade(qmsIqcGradeVOList.get(0).getGrade());
            }
        }
        return resultList;
    }

    @Override
    @ProcessLovValue
    public Page<QmsIqcCheckPlatformIqcReturnDTO> iqcHeadQuery(Long tenantId, PageRequest pageRequest, QmsIqcCheckPlatformIqcQueryDTO iqcCheckPlatformIqcQueryDTO) {
        Page<QmsIqcCheckPlatformIqcReturnDTO> resultList = new Page<>();
        if (StringUtils.isNotEmpty(iqcCheckPlatformIqcQueryDTO.getSampleCode())) {
            String instructionId = qmsIqcCheckPlatformMapper.queryInstructionId(tenantId, iqcCheckPlatformIqcQueryDTO.getSampleCode());
            iqcCheckPlatformIqcQueryDTO.setInstructionId(instructionId);
            if (StringUtils.isEmpty(instructionId)) {
                return resultList;
            }
            iqcCheckPlatformIqcQueryDTO.setIqcNumber("");
        }

        resultList = PageHelper.doPageAndSort(pageRequest, () -> qmsIqcCheckPlatformMapper.iqcHeadQuery(tenantId, iqcCheckPlatformIqcQueryDTO));
        for (QmsIqcCheckPlatformIqcReturnDTO qmsIqcCheckPlatformIqcReturnDTO : resultList) {
            //接收人
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(qmsIqcCheckPlatformIqcReturnDTO.getReceiptBy()));
            qmsIqcCheckPlatformIqcReturnDTO.setReceiptByName(mtUserInfo.getRealName());
            if ("DELIVERY_DOC".equals(qmsIqcCheckPlatformIqcReturnDTO.getDocType())) {
                //来源单号
                MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId, qmsIqcCheckPlatformIqcReturnDTO.getDocHeaderId());

                MtExtendVO extendVO = new MtExtendVO();
                extendVO.setTableName("mt_instruction_attr");
                extendVO.setAttrName("INSTRUCTION_LINE_NUM");
                extendVO.setKeyId(qmsIqcCheckPlatformIqcReturnDTO.getDocLineId());
                // 找到实际存在的属性值
                List<MtExtendAttrVO> attrValueList;
                attrValueList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);

                if (mtInstructionDoc != null && CollectionUtils.isNotEmpty(attrValueList)) {
                    String res = mtInstructionDoc.getInstructionDocNum() + "#" + attrValueList.get(0).getAttrValue();
                    qmsIqcCheckPlatformIqcReturnDTO.setFromDocNum(res);
                }
                //2020-09-16 add by chaonan.hu for lu.bai 增加不良项数返回前台
                //不良项数
                BigDecimal ncQty = ncQtyQuery(tenantId, qmsIqcCheckPlatformIqcReturnDTO.getDocLineId());
                qmsIqcCheckPlatformIqcReturnDTO.setNcQty(ncQty);
                qmsIqcCheckPlatformIqcReturnDTO.setInspectionId(qmsIqcCheckPlatformIqcReturnDTO.getDocLineId());
            } else if (WmsConstant.DocType.IQC_DOC.equals(qmsIqcCheckPlatformIqcReturnDTO.getDocType())) {
                //来源单号
                qmsIqcCheckPlatformIqcReturnDTO.setFromDocNum(qmsIqcCheckPlatformIqcReturnDTO.getIqcNumber());
                //不良项数
                QmsIqcHeader qmsIqcHeader = qmsIqcHeaderRepository.selectByPrimaryKey(qmsIqcCheckPlatformIqcReturnDTO.getDocHeaderId());
                BigDecimal ncQty = ncQtyQuery(tenantId, qmsIqcHeader.getDocLineId());
                qmsIqcCheckPlatformIqcReturnDTO.setNcQty(ncQty);
                qmsIqcCheckPlatformIqcReturnDTO.setInspectionId(qmsIqcHeader.getDocLineId());
            }
            //2020-10-12 add by sanfeng.zhang for wangkang 增加IQC版本显示
            if(StringUtils.isNotBlank(qmsIqcCheckPlatformIqcReturnDTO.getDocLineId())){
                MtExtendVO iqcVersionAttr = new MtExtendVO();
                iqcVersionAttr.setTableName("mt_instruction_attr");
                iqcVersionAttr.setAttrName("IQC_VERSION");
                iqcVersionAttr.setKeyId(qmsIqcCheckPlatformIqcReturnDTO.getDocLineId());
                List<MtExtendAttrVO> iqcVersionAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, iqcVersionAttr);
                if(CollectionUtils.isNotEmpty(iqcVersionAttrList)){
                    qmsIqcCheckPlatformIqcReturnDTO.setIqcVersion(iqcVersionAttrList.get(0).getAttrValue());
                }
            }
            //查找仓库
            MtInstruction mtInstruction = mtInstructionRepository.selectByPrimaryKey(qmsIqcCheckPlatformIqcReturnDTO.getInspectionId());
            if(mtInstruction != null){
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtInstruction.getToLocatorId());
                if(mtModLocator != null){
                    qmsIqcCheckPlatformIqcReturnDTO.setLocatorName(mtModLocator.getLocatorName());
                    qmsIqcCheckPlatformIqcReturnDTO.setLocatorCode(mtModLocator.getLocatorCode());
                }
            }
            //检验开始时间
            if (qmsIqcCheckPlatformIqcReturnDTO.getInspectionStartDate() == null) {
                qmsIqcCheckPlatformIqcReturnDTO.setInspectionStartDate(new Date());

                QmsIqcHeader qmsIqcHeader = new QmsIqcHeader();
                qmsIqcHeader.setTenantId(tenantId);
                qmsIqcHeader.setIqcHeaderId(qmsIqcCheckPlatformIqcReturnDTO.getIqcHeaderId());
                qmsIqcHeader = qmsIqcHeaderRepository.selectOne(qmsIqcHeader);
                qmsIqcHeader.setInspectionStartDate(new Date());
                qmsIqcHeaderMapper.updateByPrimaryKeySelective(qmsIqcHeader);

            }
            QmsInspectionLevelsRecord record = new QmsInspectionLevelsRecord();
            record.setSupplierId(qmsIqcCheckPlatformIqcReturnDTO.getSupplierId());
            record.setMaterialId(qmsIqcCheckPlatformIqcReturnDTO.getMaterialId());
            List<QmsInspectionLevelsRecord> recordList = qmsInspectionLevelsRecordRepository.select(record);
            if (CollectionUtils.isNotEmpty(recordList)) {
                qmsIqcCheckPlatformIqcReturnDTO.setInspectionLevels(recordList.get(0).getInspectionLevels());
            }
        }
        return resultList;
    }

    @Override
    @ProcessLovValue
    public List<QmsIqcCheckPlatformIqcReturnLineDTO> iqcLineQuery(Long tenantId, PageRequest pageRequest, String iqcHeaderId) {
        List<QmsIqcCheckPlatformIqcReturnLineDTO> resultList = qmsIqcCheckPlatformMapper.iqcLineQuery(tenantId, iqcHeaderId, null);
        for (QmsIqcCheckPlatformIqcReturnLineDTO qmsIqcCheckPlatformIqcReturnLineDTO : resultList) {
            if (QmsConstants.StandardType.VALUE.equals(qmsIqcCheckPlatformIqcReturnLineDTO.getStandardType())) {
                int size = qmsIqcCheckPlatformIqcReturnLineDTO.getSampleSize().intValue();
                List<QmsIqcCheckPlatformIqcReturnDetailDTO> qmsIqcCheckPlatformIqcReturnDetailDTOS = qmsIqcCheckPlatformMapper.iqcDetailQuery(tenantId, qmsIqcCheckPlatformIqcReturnLineDTO.getIqcLineId());

                for (int i = qmsIqcCheckPlatformIqcReturnDetailDTOS.size(); i < size; i++) {
                    QmsIqcCheckPlatformIqcReturnDetailDTO addEmpty = new QmsIqcCheckPlatformIqcReturnDetailDTO();
                    addEmpty.setIqcHeaderId(qmsIqcCheckPlatformIqcReturnLineDTO.getIqcHeaderId());
                    addEmpty.setIqcLineId(qmsIqcCheckPlatformIqcReturnLineDTO.getIqcLineId());
                    addEmpty.setNumber(String.valueOf(i + 1));
                    qmsIqcCheckPlatformIqcReturnDetailDTOS.add(addEmpty);
                }
                List<QmsIqcCheckPlatformIqcReturnDetailDTO> collect = qmsIqcCheckPlatformIqcReturnDetailDTOS.stream().sorted(Comparator.comparing(QmsIqcCheckPlatformIqcReturnDetailDTO::getNumber)).collect(Collectors.toList());
                qmsIqcCheckPlatformIqcReturnLineDTO.setDetailList(collect);
            } else {
                //2020/7/27 add by sanfeng.zhang 非数值的加一条记录
                List<QmsIqcCheckPlatformIqcReturnDetailDTO> collect = new ArrayList<>();
                QmsIqcCheckPlatformIqcReturnDetailDTO detailDTO = new QmsIqcCheckPlatformIqcReturnDetailDTO();
                detailDTO.setIqcHeaderId(qmsIqcCheckPlatformIqcReturnLineDTO.getIqcHeaderId());
                detailDTO.setIqcLineId(qmsIqcCheckPlatformIqcReturnLineDTO.getIqcLineId());
                List<QmsIqcCheckPlatformIqcReturnDetailDTO> qmsIqcCheckPlatformIqcReturnDetailDTOS = qmsIqcCheckPlatformMapper.iqcDetailQuery(tenantId, qmsIqcCheckPlatformIqcReturnLineDTO.getIqcLineId());
                if (CollectionUtils.isNotEmpty(qmsIqcCheckPlatformIqcReturnDetailDTOS)) {
                    detailDTO.setIqcDetailsId(qmsIqcCheckPlatformIqcReturnDetailDTOS.get(0).getIqcDetailsId());
                    detailDTO.setRemark(qmsIqcCheckPlatformIqcReturnDetailDTOS.get(0).getRemark());
                    detailDTO.setResult(qmsIqcCheckPlatformIqcReturnDetailDTOS.get(0).getResult());
                    detailDTO.setNumber(qmsIqcCheckPlatformIqcReturnDetailDTOS.get(0).getNumber());
                }
                collect.add(detailDTO);
                qmsIqcCheckPlatformIqcReturnLineDTO.setDetailList(collect);
            }

        }
        return resultList;
    }

    @Override
    public Page<QmsIqcCheckPlatformIqcReturnDetailDTO> iqcDetailQuery(Long tenantId, PageRequest pageRequest, String iqcLineId) {
        Page<QmsIqcCheckPlatformIqcReturnDetailDTO> resultList = PageHelper.doPageAndSort(pageRequest, () -> qmsIqcCheckPlatformMapper.iqcDetailQuery(tenantId, iqcLineId));
        return resultList;
    }

    @Override
    @ProcessLovValue
    public Page<QmsIqcCheckPlatformIqcReturnLineDTO> createPageLineQuery(Long tenantId, PageRequest pageRequest, String iqcHeaderId) {
        Page<QmsIqcCheckPlatformIqcReturnLineDTO> resultList = PageHelper.doPageAndSort(pageRequest, () -> qmsIqcCheckPlatformMapper.iqcLineQuery(tenantId, iqcHeaderId, HmeConstants.ConstantValue.YES));
        return resultList;
    }

    @Override
    @ProcessLovValue
    public Page<QmsIqcCheckPlatformCreateBringDTO> lovBringDataQuery(Long tenantId, PageRequest pageRequest, String tagGroupId, String iqcHeaderId) {
        Page<QmsIqcCheckPlatformCreateBringDTO> resultList = PageHelper.doPageAndSort(pageRequest, () -> qmsIqcCheckPlatformMapper.lovBringDataQuery(tenantId, tagGroupId));

        QmsIqcHeader qmsIqcHeader = new QmsIqcHeader();
        qmsIqcHeader.setIqcHeaderId(iqcHeaderId);
        List<QmsIqcHeader> iqcHeaders = qmsIqcHeaderRepository.select(qmsIqcHeader);
        QmsIqcHeader iqcHeaderOne = iqcHeaders.get(0);

        QmsInspectionLevelsRecord record = new QmsInspectionLevelsRecord();
        record.setSiteId(iqcHeaderOne.getSiteId());
        record.setSupplierId(iqcHeaderOne.getSupplierId());
        record.setMaterialId(iqcHeaderOne.getMaterialId());
        List<QmsInspectionLevelsRecord> recordList = qmsInspectionLevelsRecordRepository.select(record);


        for (QmsIqcCheckPlatformCreateBringDTO qmsIqcCheckPlatformCreateBringDTO : resultList) {
            if (CollectionUtils.isNotEmpty(recordList)) {
                qmsIqcCheckPlatformCreateBringDTO.setInspectionLevels(recordList.get(0).getInspectionLevels());
            }

            //规格类型
            Condition condition = new Condition(MtGenType.class);
            condition.and().andEqualTo("module", "GENERAL")
                    .andEqualTo("typeGroup", "TAG_VALUE_TYPE");
            List<MtGenType> mtGenTypes = mtGenTypeRepository.selectByCondition(condition);
            List<MtGenType> collect = mtGenTypes.stream().filter(f -> StringUtils.equals(f.getTypeCode(), qmsIqcCheckPlatformCreateBringDTO.getStandardType())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                qmsIqcCheckPlatformCreateBringDTO.setStandardTypeMeaning(collect.get(0).getDescription());
            }

            //检验方法
            List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.TAG_COLLECTION_METHOD", tenantId);
            List<LovValueDTO> methodCollect = lovValueDTOS.stream().filter(f -> StringUtils.equals(f.getValue(), qmsIqcCheckPlatformCreateBringDTO.getCollectionMethod())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(methodCollect)) {
                qmsIqcCheckPlatformCreateBringDTO.setCollectionMethodMeaning(methodCollect.get(0).getMeaning());
            }

        }
        return resultList;
    }

    @Override
    public QmsIqcCheckPlatformCreateBringDTO2 sampleLovBringDataQuery(Long tenantId, String sampleTypeId, String iqcHeaderId) {

        QmsIqcCheckPlatformCreateBringDTO2 result = new QmsIqcCheckPlatformCreateBringDTO2();

        QmsSampleType qmsSampleType = new QmsSampleType();
        qmsSampleType.setSampleTypeId(sampleTypeId);
        //这里不会为空
        List<QmsSampleType> qmsSampleTypes = qmsSampleTypeRepository.select(qmsSampleType);
        QmsSampleType sampleTypeOne = qmsSampleTypes.get(0);

        QmsIqcHeader qmsIqcHeader = new QmsIqcHeader();
        qmsIqcHeader.setIqcHeaderId(iqcHeaderId);
        List<QmsIqcHeader> iqcHeaders = qmsIqcHeaderRepository.select(qmsIqcHeader);
        QmsIqcHeader headerOne = iqcHeaders.get(0);
        result.setAc("0");
        result.setRe("1");
        result.setAql(String.valueOf(sampleTypeOne.getAcceptanceQuantityLimit() == null ? "" : sampleTypeOne.getAcceptanceQuantityLimit()));

        if (QmsConstants.SampleType.SAMPLE_TYPE.equalsIgnoreCase(sampleTypeOne.getSampleType())
                && QmsConstants.ConstantValue.STRING_ZERO.equalsIgnoreCase(sampleTypeOne.getSampleStandard())) {

            if (QmsConstants.ConstantValue.STRING_ZERO.equals(sampleTypeOne.getInspectionLevels())) {
                throw new MtException("QMS_MATERIAL_INSP_P0026",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0026",
                                "QMS"));
            }

            //检验水平
            String levels = sampleTypeOne.getInspectionLevels();

            //2020/5/9 新增
            QmsInspectionLevelsRecord record = new QmsInspectionLevelsRecord();
            record.setSiteId(headerOne.getSiteId());
            record.setSupplierId(headerOne.getSupplierId());
            record.setMaterialId(headerOne.getMaterialId());
            List<QmsInspectionLevelsRecord> recordList = qmsInspectionLevelsRecordRepository.select(record);
            int maxLevel = 7;
            if (CollectionUtils.isNotEmpty(recordList)) {
                int recordLevels = Integer.parseInt(recordList.get(0).getInspectionLevels());
                int sampleLevels = Integer.parseInt(sampleTypeOne.getInspectionLevels());
                int res = sampleLevels + recordLevels;
                if (res <= 0) {
                    levels = String.valueOf(1);
                } else if (res < maxLevel) {
                    levels = String.valueOf(res);
                } else {
                    levels = String.valueOf(maxLevel);
                }
            }
            result.setInspectionLevels(levels);

            List<QmsSampleSizeCodeLetter> sizeCodeLetters = qmsIqcHeaderMapper.querySampleSizeCodeLetter(headerOne.getQuantity());

            if (CollectionUtils.isEmpty(sizeCodeLetters)) {
                throw new MtException("QMS_MATERIAL_INSP_P0026",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0026",
                                "QMS"));
            }

            QmsSampleSizeCodeLetter qmsSampleSizeCodeLetter = sizeCodeLetters.get(0);

            String sampleSizeCodeLetter = null;
            switch (levels) {
                case "1":
                    sampleSizeCodeLetter = qmsSampleSizeCodeLetter.getSizeCodeLetter1();
                    break;
                case "2":
                    sampleSizeCodeLetter = qmsSampleSizeCodeLetter.getSizeCodeLetter2();
                    break;
                case "3":
                    sampleSizeCodeLetter = qmsSampleSizeCodeLetter.getSizeCodeLetter3();
                    break;
                case "4":
                    sampleSizeCodeLetter = qmsSampleSizeCodeLetter.getSizeCodeLetter4();
                    break;
                case "5":
                    sampleSizeCodeLetter = qmsSampleSizeCodeLetter.getSizeCodeLetter5();
                    break;
                case "6":
                    sampleSizeCodeLetter = qmsSampleSizeCodeLetter.getSizeCodeLetter6();
                    break;
                case "7":
                    sampleSizeCodeLetter = qmsSampleSizeCodeLetter.getSizeCodeLetter7();
                    break;
                default:
                    break;
            }

            if (qmsSampleSizeCodeLetter != null) {
                QmsSampleScheme sampleScheme = new QmsSampleScheme();
                sampleScheme.setSampleSizeCodeLetter(sampleSizeCodeLetter);
                sampleScheme.setSampleStandardType(qmsSampleSizeCodeLetter.getSampleStandardType());
                sampleScheme.setAcceptanceQuantityLimit(sampleTypeOne.getAcceptanceQuantityLimit().toString());
                List<QmsSampleScheme> sampleSchemes = sampleSchemeRepository.select(sampleScheme);

                if (CollectionUtils.isNotEmpty(sampleSchemes)) {
                    //2020-10-22 09:57 edit by chaonan.hu for lu.bai
                    //如果sampleSchemes的attribute1有值的话，根据scheme_id=attribute1查询数据，将这条数据作为赋值的来源数据
                    if(StringUtils.isNotBlank(sampleSchemes.get(0).getAttribute1())){
                        QmsSampleScheme qmsSampleScheme = sampleSchemeRepository.selectByPrimaryKey(sampleSchemes.get(0).getAttribute1());
                        if(Objects.isNull(qmsSampleScheme)){
                            throw new MtException("QMS_MATERIAL_INSP_P0026",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0026",
                                            "QMS"));
                        }
                        BigDecimal sampleSize = new BigDecimal(qmsSampleScheme.getSampleSize());
                        result.setSampleSize(String.valueOf(sampleSize));
                        //AC值
                        result.setAc(String.valueOf(qmsSampleScheme.getAc()));
                        //RE值
                        result.setRe(String.valueOf(qmsSampleScheme.getRe()));
                    }else{
                        BigDecimal sampleSize = new BigDecimal(sampleSchemes.get(0).getSampleSize());
                        result.setSampleSize(String.valueOf(sampleSize));
                        //AC值
                        result.setAc(String.valueOf(sampleSchemes.get(0).getAc()));
                        //RE值
                        result.setRe(String.valueOf(sampleSchemes.get(0).getRe()));
                    }
                } else {
                    throw new MtException("QMS_MATERIAL_INSP_P0026",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0026",
                                    "QMS"));
                }
            } else {
                throw new MtException("QMS_MATERIAL_INSP_P0026",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0026",
                                "QMS"));
            }


        } else if (QmsConstants.SampleType.SAMPLE_TYPE.equalsIgnoreCase(sampleTypeOne.getSampleType())
                && QmsConstants.ConstantValue.STRING_ONE.equalsIgnoreCase(sampleTypeOne.getSampleStandard())) {
            QmsSampleScheme sampleScheme = qmsIqcHeaderMapper.querySampleScheme(sampleTypeOne.getSampleStandard(), headerOne.getQuantity(), sampleTypeOne.getAcceptanceQuantityLimit());
            if (sampleScheme != null) {
                BigDecimal sampleSize = new BigDecimal(sampleScheme.getSampleSize());
                result.setSampleSize(String.valueOf(sampleSize));
            } else {
                result.setSampleSize(QmsConstants.ConstantValue.STRING_ONE);
            }
        } else if (QmsConstants.SampleType.SAME_NUMBER.equalsIgnoreCase(sampleTypeOne.getSampleType())) {
            result.setSampleSize(String.valueOf(sampleTypeOne.getParameters()));
        } else if (QmsConstants.SampleType.SAME_PERCENTAGE.equalsIgnoreCase(sampleTypeOne.getSampleType())) {
            double sampleSize = Math.ceil(BigDecimal.valueOf(sampleTypeOne.getParameters()).multiply(headerOne.getQuantity().multiply(BigDecimal.valueOf(0.01))).doubleValue());
            result.setSampleSize(String.valueOf(sampleSize));
        } else if (QmsConstants.SampleType.ALL_INSPECTION.equalsIgnoreCase(sampleTypeOne.getSampleType())) {
            result.setSampleSize(String.valueOf(headerOne.getQuantity()));
        } else {
            throw new MtException("QMS_MATERIAL_INSP_P0026",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0026",
                            "QMS"));
        }
        //2020-10-22 10:04 edit by chaonan.hu for lu.bai
        //将SampleSize与头上的物料数量比较，取较小值存入到SampleSize
        if(headerOne.getQuantity().doubleValue() < Double.valueOf(result.getSampleSize()).doubleValue()){
            result.setSampleSize(headerOne.getQuantity().toString());
        }
        return result;
    }

    @Override
    public List<QmsIqcCheckPlatformIqcSaveDTO> iqcSave(Long tenantId, QmsIqcCheckPlatformIqcSaveDTO dto) {
        List<QmsIqcCheckPlatformIqcSaveDTO> result = new ArrayList<>();
        //保存 质检单头
        QmsIqcHeader qmsIqcHeader = new QmsIqcHeader();
        qmsIqcHeader.setTenantId(tenantId);
        qmsIqcHeader.setIqcHeaderId(dto.getIqcHeaderId());
        qmsIqcHeader = qmsIqcHeaderRepository.selectOne(qmsIqcHeader);
        qmsIqcHeader.setInspectionResult(dto.getInspectionResult());
        qmsIqcHeader.setOkQty(dto.getOkQty());
        qmsIqcHeader.setNgQty(dto.getNgQty());
        qmsIqcHeader.setRemark(dto.getRemark());
        //2021-03-30 10:19 edit by chaonan.hu for kang.wang 保存质检单头时将QcBy更新为当前登录用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        qmsIqcHeader.setQcBy(userId);
        qmsIqcHeaderMapper.updateByPrimaryKeySelective(qmsIqcHeader);

        //2020-8-6 add by sanfeng.zhang 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        String eventTypeCode = "QC_DOC_SUBMIT";
        eventCreateVO.setEventTypeCode(eventTypeCode);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        //2020-8-6 add by sanfeng.zhang 生成质检单头历史记录
        QmsIqcHeaderHis headerHis = new QmsIqcHeaderHis();
        BeanUtils.copyProperties(qmsIqcHeader, headerHis);
        headerHis.setEventId(eventId);
        qmsIqcHeaderHisRepository.createQmsIqcHeaderHis(headerHis);

        //保存 质检单的行与明细信息
        updateLineAndDetail(tenantId, dto, eventId);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<QmsIqcCheckPlatformIqcSaveDTO> iqcSubmit(Long tenantId, QmsIqcCheckPlatformIqcSaveDTO dto) {
        List<QmsIqcCheckPlatformIqcSaveDTO> result = new ArrayList<>();
        //保存质检单头
        QmsIqcHeader qmsIqcHeader = new QmsIqcHeader();
        qmsIqcHeader.setTenantId(tenantId);
        qmsIqcHeader.setIqcHeaderId(dto.getIqcHeaderId());
        qmsIqcHeader = qmsIqcHeaderRepository.selectOne(qmsIqcHeader);
        qmsIqcHeader.setInspectionFinishDate(new Date());
        //2020-08-11 add by sanfeng.zhang 校验  新建才能进行提交
        if (!QmsConstants.ConstantValue.NEW.equals(qmsIqcHeader.getInspectionStatus())) {
            throw new MtException("QMS_IQC_HEADER_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_IQC_HEADER_0014", "QMS"));
        }

        //检验时长
        BigDecimal betweenTwoDateHours = hoursBetweenTwoDate(dto.getInspectionStartDate(), new Date());
        qmsIqcHeader.setInspectionStartDate(dto.getInspectionStartDate());
        qmsIqcHeader.setInspectionTime(betweenTwoDateHours);
        qmsIqcHeader.setInspectionResult(dto.getInspectionResult());
        qmsIqcHeader.setOkQty(dto.getOkQty());
        qmsIqcHeader.setNgQty(dto.getNgQty());
        qmsIqcHeader.setRemark(dto.getRemark());
        if (QmsConstants.ConstantValue.OK.equals(dto.getInspectionResult())) {
            qmsIqcHeader.setInspectionStatus(WmsConstant.InstructionStatus.COMPLETED);
        } else {
            qmsIqcHeader.setInspectionStatus(WmsConstant.InstructionStatus.TBD);
        }
        //2021-03-30 10:19 edit by chaonan.hu for kang.wang 保存质检单头时将QcBy更新为当前登录用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        qmsIqcHeader.setQcBy(userId);
        qmsIqcHeaderMapper.updateByPrimaryKeySelective(qmsIqcHeader);

        //2020-8-6 add by sanfeng.zhang 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        String eventTypeCode = "QC_DOC_SUBMIT";
        eventCreateVO.setEventTypeCode(eventTypeCode);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        //2020-8-6 add by sanfeng.zhang 生成质检单头历史记录
        QmsIqcHeaderHis headerHis = new QmsIqcHeaderHis();
        BeanUtils.copyProperties(qmsIqcHeader, headerHis);
        headerHis.setEventId(eventId);
        qmsIqcHeaderHisRepository.createQmsIqcHeaderHis(headerHis);

        //更新 质检单的行与明细
        updateLineAndDetail(tenantId, dto, eventId);

        if (QmsConstants.ConstantValue.OK.equals(dto.getInspectionResult())) {
            //插入 入库上架任务表
            insertPutInStorageTask(tenantId, dto);
        }

        //供应商加严放宽记录
        //2020/12/10 add by sanfeng.zhang for zhangjian 去掉加严放宽校验
        //insertInspectionLevelsRecord(tenantId, dto);

        //2020-8-6 add by sanfeng.zhang 送货单下条码的处理
        if (QmsConstants.ConstantValue.OK.equals(dto.getInspectionResult())) {
            //仓库的接受方式
            if (StringUtils.equals("DELIVERY_DOC", qmsIqcHeader.getDocType()) || StringUtils.equals("IQC_DOC", qmsIqcHeader.getDocType())) {
                //处理送货单下条码
                qmsIqcAuditRepository.handleMaterialLot(tenantId, qmsIqcHeader, eventId, "OK", "", "TO_ACCEPT");
            }
        }

        //2020-09-16 add by chaona.hu for lu.bai 库存扣减、将送货单行下的NG_QTY扩展字段更新
        this.handleNcQty(tenantId, dto.getInstructionId(), eventId);
        return result;
    }

    @Override
    public void createPageSave(Long tenantId, QmsIqcCheckPlatformCreatePageSaveDTO2 saveData) {
        List<QmsIqcCheckPlatformCreatePageSaveDTO> saveDataList = saveData.getSaveData();
        for (QmsIqcCheckPlatformCreatePageSaveDTO qmsIqcCheckPlatformCreatePageSaveDTO : saveDataList) {
            QmsIqcLine line = new QmsIqcLine();
            line.setTenantId(tenantId);
            line.setIqcHeaderId(qmsIqcCheckPlatformCreatePageSaveDTO.getIqcHeaderId());
            line.setInspectionType(qmsIqcCheckPlatformCreatePageSaveDTO.getInspectionType());
            line.setInspection(qmsIqcCheckPlatformCreatePageSaveDTO.getInspection());
            line.setInspectionDesc(qmsIqcCheckPlatformCreatePageSaveDTO.getInspectionDesc());
            line.setSampleType(qmsIqcCheckPlatformCreatePageSaveDTO.getSampleTypeId());
            line.setInspectionLevels(qmsIqcCheckPlatformCreatePageSaveDTO.getInspectionLevels());
            line.setDefectLevels(qmsIqcCheckPlatformCreatePageSaveDTO.getDefectLevels());
            line.setAcceptanceQuantityLimit(qmsIqcCheckPlatformCreatePageSaveDTO.getAcceptanceQuantityLimit());
            line.setSampleSize(qmsIqcCheckPlatformCreatePageSaveDTO.getSampleSize());
            line.setAc(qmsIqcCheckPlatformCreatePageSaveDTO.getAC());
            line.setRe(qmsIqcCheckPlatformCreatePageSaveDTO.getRE());
            line.setStandardText(qmsIqcCheckPlatformCreatePageSaveDTO.getStandardText());
            line.setStandardFrom(qmsIqcCheckPlatformCreatePageSaveDTO.getStandardFrom());
            line.setStandardTo(qmsIqcCheckPlatformCreatePageSaveDTO.getStandardTo());
            if (StringUtils.isNotEmpty(qmsIqcCheckPlatformCreatePageSaveDTO.getUomId())) {
                MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, qmsIqcCheckPlatformCreatePageSaveDTO.getUomId());
                line.setStandardUom(mtUomVO.getUomId());
            }

            line.setInspectionTool(qmsIqcCheckPlatformCreatePageSaveDTO.getInspectionTool());
            line.setEnableFlag(HmeConstants.ConstantValue.YES);
            line.setAddedFlag(HmeConstants.ConstantValue.YES);
            line.setStandardType(qmsIqcCheckPlatformCreatePageSaveDTO.getStandardType());
            Long maxNum = Optional.ofNullable(qmsIqcCheckPlatformMapper.queryLineMaxNum(tenantId, qmsIqcCheckPlatformCreatePageSaveDTO.getIqcHeaderId())).orElse(0L);

            line.setNumber(maxNum + 1);
            if (StringUtils.isNotEmpty(qmsIqcCheckPlatformCreatePageSaveDTO.getIqcLineId())) {
                //行主键不为空  走更新逻辑
                QmsIqcLine check = new QmsIqcLine();
                check.setTenantId(tenantId);
                check.setIqcLineId(qmsIqcCheckPlatformCreatePageSaveDTO.getIqcLineId());
                check = qmsIqcLineRepository.selectOne(check);
                line.setObjectVersionNumber(check.getObjectVersionNumber());
                line.setIqcLineId(qmsIqcCheckPlatformCreatePageSaveDTO.getIqcLineId());
                qmsIqcLineMapper.updateByPrimaryKeySelective(line);
            } else {
                //走新增逻辑
                qmsIqcLineRepository.insertSelective(line);
            }
        }
    }

    @Override
    public Boolean createPageDelete(Long tenantId, QmsIqcCheckPlatformIqcDeleteDTO dto) {
        List<String> iqcLineIdList = dto.getIqcLineIdList();
        for (String iqcLineId : iqcLineIdList) {
            QmsIqcLine line = new QmsIqcLine();
            line.setTenantId(tenantId);
            line.setIqcLineId(iqcLineId);
            line = qmsIqcLineRepository.selectOne(line);
            line.setAddedFlag(QmsConstants.ConstantValue.NO);
            line.setEnableFlag(QmsConstants.ConstantValue.NO);
            qmsIqcLineMapper.updateByPrimaryKeySelective(line);
        }
        return true;
    }

    @Override
    public QmsIqcLine uploadAttachment(Long tenantId, String iqcLineId, String uuid) {
        QmsIqcLine line = new QmsIqcLine();
        line.setTenantId(tenantId);
        line.setIqcLineId(iqcLineId);
        line = qmsIqcLineMapper.selectOne(line);
        line.setAttachmentUuid(uuid);
        qmsIqcLineMapper.updateByPrimaryKeySelective(line);

        return line;
    }

    @Override
    public Page<QmsIqcCheckPlatformDTO2> listForUi(Long tenantId, QmsIqcCheckPlatformDTO2 dto, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> qmsIqcCheckPlatformMapper.queryLov(tenantId, dto));
    }

    @Override
    public Page<QmsIqcCheckPlatformVO> ncDataQuery(Long tenantId, String inspectionId, PageRequest pageRequest) {
        List<QmsIqcCheckPlatformVO> resultList = new ArrayList<>();
        if (StringUtils.isNotEmpty(inspectionId)) {
            String actualId = qmsIqcCheckPlatformMapper.getActualIdByInstruction(tenantId, inspectionId);
            if (StringUtils.isNotEmpty(actualId)) {
                List<MtInstructionActualDetail> mtInstructionActualDetailList = instructionActualDetailRepository.select(new MtInstructionActualDetail() {{
                    setTenantId(tenantId);
                    setActualId(actualId);
                }});
                for (MtInstructionActualDetail instructionActualDetail : mtInstructionActualDetailList) {
                    MtExtendVO mtExtendVO = new MtExtendVO();
                    mtExtendVO.setTableName("mt_instruct_act_detail_attr");
                    mtExtendVO.setKeyId(instructionActualDetail.getActualDetailId());
                    mtExtendVO.setAttrName("NG_QTY");
                    List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                        QmsIqcCheckPlatformVO qmsIqcCheckPlatformVO = new QmsIqcCheckPlatformVO();
                        qmsIqcCheckPlatformVO.setActualDetailId(instructionActualDetail.getActualDetailId());
                        qmsIqcCheckPlatformVO.setMaterialLotId(instructionActualDetail.getMaterialLotId());
                        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(instructionActualDetail.getMaterialLotId());
                        qmsIqcCheckPlatformVO.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                        qmsIqcCheckPlatformVO.setNcQty(new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue()));
                        resultList.add(qmsIqcCheckPlatformVO);
                    }
                }
            }
        }
        List<QmsIqcCheckPlatformVO> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
        return new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), resultList.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<QmsIqcCheckPlatformVO> ncDataDelete(Long tenantId, List<QmsIqcCheckPlatformVO> dtoList) {
        for (QmsIqcCheckPlatformVO dto : dtoList) {
            if (StringUtils.isNotEmpty(dto.getActualDetailId())) {
                qmsIqcCheckPlatformMapper.updateAttr(tenantId, dto.getActualDetailId());
            }
        }
        return dtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QmsIqcCheckPlatformDTO3 ncDataUpdate(Long tenantId, QmsIqcCheckPlatformDTO3 dto) {
        String inspectionId = dto.getInspectionId();
        if (StringUtils.isEmpty(dto.getInspectionId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "inspectionId"));
        }
        for (QmsIqcCheckPlatformVO qmsIqcCheckPlatformVO : dto.getMaterialLotData()) {
            if (StringUtils.isEmpty(qmsIqcCheckPlatformVO.getActualDetailId())) {
                //新增
                String materialLotCode = qmsIqcCheckPlatformVO.getMaterialLotCode();
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                    setTenantId(tenantId);
                    setMaterialLotCode(materialLotCode);
                }});
                if (mtMaterialLot == null) {
                    throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0003", "HME", materialLotCode));
                }
                if ("N".equals(mtMaterialLot.getEnableFlag())) {
                    throw new MtException("QMS_IQC_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "QMS_IQC_0002", "QMS", materialLotCode));
                }
                if (!"PENDING".equals(mtMaterialLot.getQualityStatus())) {
                    throw new MtException("QMS_IQC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "QMS_IQC_0003", "QMS", materialLotCode));
                }
                //校验不良数量不得大于条码数量
                if(mtMaterialLot.getPrimaryUomQty() < qmsIqcCheckPlatformVO.getNcQty().doubleValue()){
                    throw new MtException("HME_NC_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0014", "HME", mtMaterialLot.getMaterialLotCode()));
                }
                String actualId = qmsIqcCheckPlatformMapper.getActualIdByInstruction(tenantId, inspectionId);
                if (StringUtils.isEmpty(actualId)) {
                    throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0006", "HME", "actualId"));
                }
                MtInstructionActualDetail mtInstructionActualDetail = instructionActualDetailRepository.selectOne(new MtInstructionActualDetail() {{
                    setTenantId(tenantId);
                    setActualId(actualId);
                    setMaterialLotId(mtMaterialLot.getMaterialLotId());
                }});
                if (mtInstructionActualDetail == null) {
                    throw new MtException("QMS_IQC_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "QMS_IQC_0001", "QMS", materialLotCode));
                }
                int attrCount = qmsIqcCheckPlatformMapper.getAttrCount(tenantId, mtInstructionActualDetail.getActualDetailId());
                if (attrCount == 1) {
                    //更新
                    qmsIqcCheckPlatformMapper.updateAttr2(tenantId, mtInstructionActualDetail.getActualDetailId(), qmsIqcCheckPlatformVO.getNcQty().toString());
                } else {
                    //新增
                    String attrId = customDbRepository.getNextKey("mt_instruct_act_detail_attr_s");
                    String cid = customDbRepository.getNextKey("mt_instruct_act_detail_attr_cid_s");
                    CustomUserDetails curUser = DetailsHelper.getUserDetails();
                    Long userId = curUser == null ? -1L : curUser.getUserId();
                    qmsIqcCheckPlatformMapper.insertAttr(tenantId, attrId, mtInstructionActualDetail.getActualDetailId(),
                            qmsIqcCheckPlatformVO.getNcQty().toString(), cid, userId.toString());
                }
            } else {
                //更新
                qmsIqcCheckPlatformMapper.updateAttr2(tenantId, qmsIqcCheckPlatformVO.getActualDetailId(), qmsIqcCheckPlatformVO.getNcQty().toString());
            }
        }
        return dto;
    }

    private void updateLineAndDetail(Long tenantId, QmsIqcCheckPlatformIqcSaveDTO dto, String eventId) {
        List<QmsIqcCheckPlatformIqcSaveLineDTO> lineList = dto.getLineList();
        for (QmsIqcCheckPlatformIqcSaveLineDTO qmsIqcCheckPlatformIqcSaveLineDTO : lineList) {
            //保存 质检单行
            QmsIqcLine qmsIqcLine = new QmsIqcLine();
            qmsIqcLine.setTenantId(tenantId);
            qmsIqcLine.setIqcLineId(qmsIqcCheckPlatformIqcSaveLineDTO.getIqcLineId());
            qmsIqcLine = qmsIqcLineRepository.selectOne(qmsIqcLine);

            long ngQty = 0L;
            long okQty = 0L;
            List<QmsIqcCheckPlatformIqcSaveDetailDTO> detailList = qmsIqcCheckPlatformIqcSaveLineDTO.getDetailList();
            if (CollectionUtils.isNotEmpty(detailList)) {
                for (QmsIqcCheckPlatformIqcSaveDetailDTO qmsIqcCheckPlatformIqcSaveDetailDTO : detailList) {
                    if (QmsConstants.StandardType.VALUE.equals(qmsIqcLine.getStandardType())) {
                        if (qmsIqcLine.getStandardFrom() != null && qmsIqcLine.getStandardTo() != null) {
                            if (StringUtils.isNotEmpty(qmsIqcCheckPlatformIqcSaveDetailDTO.getResult())) {
                                Boolean checkOkOrNgFlag = checkOkOrNg(qmsIqcLine.getStandardFrom(), qmsIqcLine.getStandardTo(), new BigDecimal(qmsIqcCheckPlatformIqcSaveDetailDTO.getResult()));
                                if (!checkOkOrNgFlag) {
                                    ngQty++;
                                } else {
                                    okQty++;
                                }
                            }
                        }
                    }
                    if (StringUtils.isEmpty(qmsIqcCheckPlatformIqcSaveDetailDTO.getIqcDetailsId())) {
//                        if (StringUtils.isNotEmpty(qmsIqcCheckPlatformIqcSaveDetailDTO.getResult())) {
                        //查询该行上明细num最大值
                        QmsIqcDetails checkNum = new QmsIqcDetails();
                        checkNum.setTenantId(tenantId);
                        checkNum.setIqcHeaderId(dto.getIqcHeaderId());
                        checkNum.setIqcLineId(qmsIqcCheckPlatformIqcSaveLineDTO.getIqcLineId());
                        List<QmsIqcDetails> select = qmsIqcDetailsRepository.select(checkNum);
                        long maxNum = 0;
                        if (CollectionUtils.isNotEmpty(select)) {
                            maxNum = select.get(select.size() - 1).getNumber();
                        }
                        //新增 质检单明细
                        QmsIqcDetails qmsIqcDetails = new QmsIqcDetails();
                        qmsIqcDetails.setTenantId(tenantId);
                        qmsIqcDetails.setIqcHeaderId(dto.getIqcHeaderId());
                        qmsIqcDetails.setIqcLineId(qmsIqcCheckPlatformIqcSaveLineDTO.getIqcLineId());
                        long num = maxNum + 1;
                        qmsIqcDetails.setNumber(num);
                        qmsIqcDetails.setResult(qmsIqcCheckPlatformIqcSaveDetailDTO.getResult());
                        qmsIqcDetails.setRemark(qmsIqcCheckPlatformIqcSaveDetailDTO.getRemark());
                        qmsIqcDetailsRepository.insertSelective(qmsIqcDetails);

                        //2020-8-6 add by sanfeng.zhang 质检单明细历史
                        QmsIqcDetailsHis qmsIqcDetailsHis = new QmsIqcDetailsHis();
                        BeanUtils.copyProperties(qmsIqcDetails, qmsIqcDetailsHis);
                        qmsIqcDetailsHis.setEventId(eventId);
                        qmsIqcDetailsHisRepository.createQmsIqcDetailsHis(qmsIqcDetailsHis);
//                        }

                    } else {
                        //更新  质检单明细
                        QmsIqcDetails qmsIqcDetails = new QmsIqcDetails();
                        qmsIqcDetails.setTenantId(tenantId);
                        qmsIqcDetails.setIqcDetailsId(qmsIqcCheckPlatformIqcSaveDetailDTO.getIqcDetailsId());
                        qmsIqcDetails = qmsIqcDetailsRepository.selectOne(qmsIqcDetails);
                        qmsIqcDetails.setIqcHeaderId(dto.getIqcHeaderId());
                        qmsIqcDetails.setIqcLineId(qmsIqcCheckPlatformIqcSaveLineDTO.getIqcLineId());
                        qmsIqcDetails.setNumber(qmsIqcCheckPlatformIqcSaveDetailDTO.getNumber());
                        qmsIqcDetails.setResult(qmsIqcCheckPlatformIqcSaveDetailDTO.getResult());
                        qmsIqcDetails.setRemark(qmsIqcCheckPlatformIqcSaveDetailDTO.getRemark());
                        qmsIqcDetailsMapper.updateByPrimaryKeySelective(qmsIqcDetails);

                        //2020-8-6 add by sanfeng.zhang 质检单明细历史
                        QmsIqcDetailsHis qmsIqcDetailsHis = new QmsIqcDetailsHis();
                        BeanUtils.copyProperties(qmsIqcDetails, qmsIqcDetailsHis);
                        qmsIqcDetailsHis.setEventId(eventId);
                        qmsIqcDetailsHisRepository.createQmsIqcDetailsHis(qmsIqcDetailsHis);
                    }
                }
            }


            if (QmsConstants.StandardType.VALUE.equals(qmsIqcLine.getStandardType())) {
                qmsIqcLine.setOkQty(okQty);
                qmsIqcLine.setNgQty(ngQty);
                if (ngQty > 0) {
                    qmsIqcLine.setInspectionResult(HmeConstants.ConstantValue.NG);
                } else {
                    qmsIqcLine.setInspectionResult(HmeConstants.ConstantValue.OK);
                }

            } else {
                qmsIqcLine.setOkQty(qmsIqcCheckPlatformIqcSaveLineDTO.getOkQty());
                qmsIqcLine.setNgQty(qmsIqcCheckPlatformIqcSaveLineDTO.getNgQty());
                qmsIqcLine.setInspectionResult(qmsIqcCheckPlatformIqcSaveLineDTO.getInspectionResult());
            }
            qmsIqcLine.setAttachmentUuid(qmsIqcCheckPlatformIqcSaveLineDTO.getAttachmentUuid());
            if (qmsIqcCheckPlatformIqcSaveLineDTO.getSampleSize() != null) {
                qmsIqcLine.setSampleSize(qmsIqcCheckPlatformIqcSaveLineDTO.getSampleSize());
            }
            qmsIqcLineMapper.updateByPrimaryKeySelective(qmsIqcLine);

            //2020-8-6 add by sanfeng.zhang 生成质检行的历史记录
            QmsIqcLineHis qmsIqcLineHis = new QmsIqcLineHis();
            BeanUtils.copyProperties(qmsIqcLine, qmsIqcLineHis);
            qmsIqcLineHis.setEventId(eventId);
            qmsIqcLineHisRepository.createQmsIqcLineHis(qmsIqcLineHis);
        }
    }

    /**
     * @param tenantId 1
     * @param dto      2
     * @return : void
     * @Description: 如果质检结果为OK，则向入库上架任务表插入数据
     * @author: tong.li
     * @date 2020/5/14 10:09
     * @version 1.0
     */
    private void insertPutInStorageTask(Long tenantId, QmsIqcCheckPlatformIqcSaveDTO dto) {
        QmsIqcHeader qmsIqcHeader = new QmsIqcHeader();
        qmsIqcHeader.setIqcHeaderId(dto.getIqcHeaderId());
        List<QmsIqcHeader> iqcHeaders = qmsIqcHeaderRepository.select(qmsIqcHeader);
        QmsIqcHeader headerOne = iqcHeaders.get(0);

        WmsPutInStorageTask putInStorageTask = new WmsPutInStorageTask();
        putInStorageTask.setTenantId(tenantId);
        putInStorageTask.setTaskType(headerOne.getDocType());
        putInStorageTask.setInstructionDocId(headerOne.getDocHeaderId());
        putInStorageTask.setInstructionId(headerOne.getDocLineId());
        putInStorageTask.setTaskStatus("STOCK_PENDING");
        putInStorageTask.setInstructionDocType(headerOne.getDocType());
        putInStorageTask.setMaterialId(headerOne.getMaterialId());
        putInStorageTask.setTaskQty(headerOne.getQuantity());
        putInStorageTask.setExecuteQty(BigDecimal.valueOf(0));
        wmsPutInStorageTaskRepository.insertSelective(putInStorageTask);
    }

    /**
     * @param tenantId 1
     * @param dto      2
     * @return : void
     * @Description: 供应商加严放宽记录
     * @author: tong.li
     * @date 2020/5/14 10:23
     * @version 1.0
     */
    private void insertInspectionLevelsRecord(Long tenantId, QmsIqcCheckPlatformIqcSaveDTO dto) {
        QmsIqcHeader qmsIqcHeader = new QmsIqcHeader();
        qmsIqcHeader.setIqcHeaderId(dto.getIqcHeaderId());
        List<QmsIqcHeader> iqcHeaders = qmsIqcHeaderRepository.select(qmsIqcHeader);
        QmsIqcHeader headerOne = iqcHeaders.get(0);

        //先去QMS_INSPECTION_LEVELS_RECORD 按组织+物料+供应商的维度在本表进行查询
        QmsInspectionLevelsRecord record = new QmsInspectionLevelsRecord();
        record.setSiteId(headerOne.getSiteId());
        record.setSupplierId(headerOne.getSupplierId());
        record.setMaterialId(headerOne.getMaterialId());
        List<QmsInspectionLevelsRecord> recordList = qmsInspectionLevelsRecordRepository.select(record);

        if (CollectionUtils.isEmpty(recordList)) {
            //如果没有查到数据，则认为不进行加严放宽
            QmsInspectionLevelsRecord recordInsert = new QmsInspectionLevelsRecord();
            recordInsert.setTenantId(tenantId);
            recordInsert.setSiteId(headerOne.getSiteId());
            recordInsert.setSupplierId(headerOne.getSupplierId());
            recordInsert.setMaterialId(headerOne.getMaterialId());
            recordInsert.setInspectionLevels("0");
            recordInsert.setInspectionMethod("NORMAL");
            qmsInspectionLevelsRecordRepository.insertSelective(recordInsert);
        } else {

            QmsTransitionRule qmsTransitionRule = new QmsTransitionRule();
            qmsTransitionRule.setSiteId(headerOne.getSiteId());
            qmsTransitionRule.setMaterialId(headerOne.getMaterialId());
            List<QmsTransitionRule> ruleList = qmsTransitionRuleRepository.select(qmsTransitionRule);
            if (CollectionUtils.isNotEmpty(ruleList)) {
                QmsTransitionRule rule = ruleList.get(0);

                long n = rule.getTightenedBatches();
                long k = rule.getNgBatches();
                long p = rule.getRelaxationBatches();

                //按照组织+物料+供应商的维度，且INSPECTION_STATUS = COMPLETED  在表QMS_IQC_HEADER中查找
                QmsIqcHeader header = new QmsIqcHeader();
                header.setSiteId(headerOne.getSiteId());
                header.setMaterialId(headerOne.getMaterialId());
                header.setSupplierId(headerOne.getSupplierId());
                header.setInspectionStatus("COMPLETED");
                List<QmsIqcHeader> headerList = qmsIqcHeaderRepository.select(qmsIqcHeader);
                int m = 0;
                int z = 0;
                List<QmsIqcHeader> sortedHeaderList = headerList.stream().sorted(Comparator.comparing(QmsIqcHeader::getInspectionFinishDate).reversed()).collect(Collectors.toList());

                if (sortedHeaderList.size() < n) {
                    n = sortedHeaderList.size();
                }
                if (sortedHeaderList.size() < p) {
                    p = sortedHeaderList.size();
                }

                for (int i = 0; i < n; i++) {
                    if (HmeConstants.ConstantValue.NG.equals(sortedHeaderList.get(i).getInspectionResult())) {
                        m++;
                    }
                }
                for (int i = 0; i < p; i++) {
                    if (HmeConstants.ConstantValue.NG.equals(sortedHeaderList.get(i).getInspectionResult())) {
                        z++;
                    }
                }

                QmsInspectionLevelsRecord recordUpdate = recordList.get(0);

                if (m >= k) {
                    recordUpdate.setInspectionLevels(String.valueOf(Integer.parseInt(recordUpdate.getInspectionLevels()) - 1));
                } else {
                    if (z == 0) {
                        recordUpdate.setInspectionLevels(String.valueOf(Integer.parseInt(recordUpdate.getInspectionLevels()) + 1));
                    }
                }

                if (Integer.parseInt(recordUpdate.getInspectionLevels()) > 0) {
                    recordUpdate.setInspectionMethod("RELAXATION");
                } else if (Integer.parseInt(recordUpdate.getInspectionLevels()) == 0) {
                    recordUpdate.setInspectionMethod("NORMAL");
                } else {
                    recordUpdate.setInspectionMethod("TIGHTENED");
                }
                qmsInspectionLevelsRecordMapper.updateByPrimaryKeySelective(recordUpdate);
            }
        }

    }


    /**
     * 获取两个日期之间相差小时数, date2-date1
     *
     * @param date1 比较日期
     * @param date2 被比较日期
     * @return BigDecimal
     */
    private BigDecimal hoursBetweenTwoDate(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        double test = ((double) time2 - (double) time1) / (1000 * 3600);
        String aaa = String.format("%.2f", test);
        return new BigDecimal(aaa);
    }


    /**
     * 校验明细上的结果值是否在行上的规范范围内
     *
     * @param from   规格范围从
     * @param to     规格范围至
     * @param resQty 明细中的结果值
     * @return true:在范围内/false:不在范围内
     */
    private Boolean checkOkOrNg(BigDecimal from, BigDecimal to, BigDecimal resQty) {
        return from.compareTo(resQty) <= 0 && to.compareTo(resQty) >= 0;
    }

    private BigDecimal ncQtyQuery(Long tenantId, String docLineId) {
        BigDecimal ncQty = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(docLineId)) {
            String actualId = qmsIqcCheckPlatformMapper.getActualIdByInstruction(tenantId, docLineId);
            if (StringUtils.isNotEmpty(actualId)) {
                List<MtInstructionActualDetail> mtInstructionActualDetailList = instructionActualDetailRepository.select(new MtInstructionActualDetail() {{
                    setTenantId(tenantId);
                    setActualId(actualId);
                }});
                for (MtInstructionActualDetail instructionActualDetail : mtInstructionActualDetailList) {
                    MtExtendVO mtExtendVO = new MtExtendVO();
                    mtExtendVO.setTableName("mt_instruct_act_detail_attr");
                    mtExtendVO.setKeyId(instructionActualDetail.getActualDetailId());
                    mtExtendVO.setAttrName("NG_QTY");
                    List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                        ncQty = ncQty.add(new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue()));
                    }
                }
            }
        }
        return ncQty;
    }

    @Override
    public void handleNcQty(Long tenantId, String instructionId, String eventId) {
        // 20210407 add by sanfeng.zhang for kang.wang 带料废调换的检验单处理 又有料废调换的不扣减库存 两个都有的 不良数大于现有量的  按现有量扣减 其他按原来逻辑
        MtInstruction mtInstruction = mtInstructionRepository.selectByPrimaryKey(instructionId);
        // 执行数量
        BigDecimal qty = mtInstruction.getQuantity() != null ? BigDecimal.valueOf(mtInstruction.getQuantity()) : BigDecimal.ZERO;
        List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
            setKeyId(instructionId);
            setAttrName("EXCHANGE_QTY");
            setTableName("mt_instruction_attr");
        }});
        // 料废调换数量
        BigDecimal exchangeQty = CollectionUtils.isNotEmpty(attrVOList) ? BigDecimal.valueOf(Double.valueOf(attrVOList.get(0).getAttrValue())) : BigDecimal.ZERO;

        String actualId = qmsIqcCheckPlatformMapper.getActualIdByInstruction(tenantId, instructionId);
        BigDecimal ncQty = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(actualId)) {
            List<MtInstructionActualDetail> mtInstructionActualDetailList = instructionActualDetailRepository.select(new MtInstructionActualDetail() {{
                setTenantId(tenantId);
                setActualId(actualId);
            }});
            // 更新条码和库存事件
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            String eventTypeCode = "QC_DOC_SUBMIT";
            eventCreateVO.setEventTypeCode(eventTypeCode);
            String qcEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            List<MtInvOnhandQuantityVO13> onhandList = new ArrayList<>();

            for (MtInstructionActualDetail instructionActualDetail : mtInstructionActualDetailList) {
                MtExtendVO mtExtendVO = new MtExtendVO();
                mtExtendVO.setTableName("mt_instruct_act_detail_attr");
                mtExtendVO.setKeyId(instructionActualDetail.getActualDetailId());
                mtExtendVO.setAttrName("NG_QTY");
                List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                    BigDecimal ngQty = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
                    ncQty = ncQty.add(ngQty);
                    if (BigDecimal.ZERO.compareTo(qty) < 0 && BigDecimal.ZERO.compareTo(exchangeQty) == 0) {
                        // 执行数量不为0 料废调换数量为0 按原逻辑扣减库存
                        //调用API{materialLotConsume} 进行库存扣减
                        MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                        mtMaterialLotVO1.setMaterialLotId(instructionActualDetail.getMaterialLotId());
                        mtMaterialLotVO1.setTrxPrimaryUomQty(new Double(mtExtendAttrVOS.get(0).getAttrValue()));
                        mtMaterialLotVO1.setParentEventId(eventId);
                        mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);
                    } else if (BigDecimal.ZERO.compareTo(qty) < 0 && BigDecimal.ZERO.compareTo(exchangeQty) < 0) {
                        // 执行数量不为0 料废调换数量不为0 判断现有量和不良数
                        // 基于条码上的物料+批次+货位获取库存现有量
                        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(instructionActualDetail.getMaterialLotId());
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
                        if (ngQty.compareTo(onhandQty) > 0) {
                            // 大于现有量  按现有量扣减
                            if (onhandQty.compareTo(BigDecimal.ZERO) > 0) {
//                                MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
//                                updateOnhandVO.setSiteId(mtMaterialLot.getSiteId());
//                                updateOnhandVO.setMaterialId(mtMaterialLot.getMaterialId());
//                                updateOnhandVO.setLocatorId(mtMaterialLot.getLocatorId());
//                                updateOnhandVO.setLotCode(mtMaterialLot.getLot());
//                                updateOnhandVO.setChangeQuantity(onhandQty.doubleValue());
//                                updateOnhandVO.setEventId(qcEventId);
//                                mtInvOnhandQuantityRepository.onhandQtyUpdate(tenantId, updateOnhandVO);

                                //V20210421 modify by penglin.sui for 产品 ， 批量扣减现有量API组装参数
                                MtInvOnhandQuantityVO13 mtInvOnhandQuantityVO13 = new MtInvOnhandQuantityVO13();
                                mtInvOnhandQuantityVO13.setSiteId(mtMaterialLot.getSiteId());
                                mtInvOnhandQuantityVO13.setMaterialId(mtMaterialLot.getMaterialId());
                                mtInvOnhandQuantityVO13.setLocatorId(mtMaterialLot.getLocatorId());
                                mtInvOnhandQuantityVO13.setLotCode(mtMaterialLot.getLot());
                                mtInvOnhandQuantityVO13.setChangeQuantity(onhandQty.doubleValue());
                                onhandList.add(mtInvOnhandQuantityVO13);
                            }
                            // 扣减条码数量 为0将条码失效
                            BigDecimal primaryUomQty = BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).subtract(ngQty);
                            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                                setTenantId(tenantId);
                                setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                setPrimaryUomQty(primaryUomQty.doubleValue());
                                setEnableFlag(BigDecimal.ZERO.compareTo(primaryUomQty) == 0 ? HmeConstants.ConstantValue.NO : HmeConstants.ConstantValue.YES);
                                setEventId(qcEventId);
                            }}, "N");
                        } else {
                            // 小于按之前逻辑扣减
                            //调用API{materialLotConsume} 进行库存扣减
                            MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                            mtMaterialLotVO1.setMaterialLotId(instructionActualDetail.getMaterialLotId());
                            mtMaterialLotVO1.setTrxPrimaryUomQty(new Double(mtExtendAttrVOS.get(0).getAttrValue()));
                            mtMaterialLotVO1.setParentEventId(eventId);
                            mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);
                        }
                    } else if (BigDecimal.ZERO.compareTo(qty) == 0 && BigDecimal.ZERO.compareTo(exchangeQty) < 0) {
                        // 执行数量为0 料废调换数量不为0 不扣减库存 扣减条码数量
                        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(instructionActualDetail.getMaterialLotId());
                        BigDecimal subQty = BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).subtract(ngQty);
                        mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                            setTenantId(tenantId);
                            setMaterialLotId(mtMaterialLot.getMaterialLotId());
                            setPrimaryUomQty(subQty.doubleValue());
                            setEnableFlag(BigDecimal.ZERO.compareTo(subQty) == 0 ? HmeConstants.ConstantValue.NO : HmeConstants.ConstantValue.YES);
                            setEventId(qcEventId);
                        }}, "N");
                    }
                }
            }

            //V20210421 modify by penglin.sui for 产品 ， 批量扣减现有量
            if(CollectionUtils.isNotEmpty(onhandList)) {
                MtInvOnhandQuantityVO16 mtInvOnhandQuantityVO16 = new MtInvOnhandQuantityVO16();
                mtInvOnhandQuantityVO16.setEventId(qcEventId);
                mtInvOnhandQuantityVO16.setOnhandList(onhandList);
                mtInvOnhandQuantityRepository.onhandQtyUpdateBatchProcess(tenantId, mtInvOnhandQuantityVO16);
            }

            //将送货单行下的所有明细的NG_QTY求和，并更新扩展字段
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("NG_QTY");
            mtExtendVO5.setAttrValue(ncQty.toString());
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", instructionId, eventId, Collections.singletonList(mtExtendVO5));
        }
    }

    @Override
    public Page<QmsIqcMaterialLotVO> materialLotListQuery(Long tenantId, String iqcHeaderId, String supplierLot,PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> iqcCheckPlatformRepository.selectMaterialLotListByIqcHeader(tenantId, iqcHeaderId, supplierLot));
    }

    @Override
    public void materialLotBatchUpdate(Long tenantId, String iqcHeaderId, List<WmsMaterialLotExtendAttrVO> list) {
        WmsEventVO event = wmsEventService.createEventOnly(tenantId, WmsConstant.EventType.HME_PRODUCTION_VERSION_CHANGE);
        materialLotRepository.batchSaveExtendAttr(tenantId, event.getEventId(), list);
    }

    @Override
    @ProcessLovValue
    public List<QmsIqcCheckPlatformExportVO> export(Long tenantId, QmsIqcCheckPlatformDTO dto) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        int count = hmeInspectorItemGroupRelRepository.selectCount(new HmeInspectorItemGroupRel() {{
            setTenantId(tenantId);
            setUserId(userId);
            setPrivilegeType("INSPECTOR");
            setEnableFlag("Y");
        }});
        if(count > 0){
            dto.setRelFlag("REL");
        }
        List<QmsIqcCheckPlatformExportVO> resultList = qmsIqcCheckPlatformMapper.exportQuery(tenantId, dto, userId);
        if(CollectionUtils.isNotEmpty(resultList)){
            //批量等级
            List<String> instructionIdList = resultList.stream().map(QmsIqcCheckPlatformExportVO::getInstructionId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            List<QmsIqcGradeVO> qmsIqcGradeList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(instructionIdList)) {
                qmsIqcGradeList = qmsIqcCheckPlatformMapper.batchQueryGrade(tenantId, instructionIdList);
            }
            Map<String, List<QmsIqcGradeVO>> qmsIqcGradeMap = qmsIqcGradeList.stream().collect(Collectors.groupingBy(item -> item.getInstructionId()));

            List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("QMS.INSPECTION_RESULT", tenantId);
            for (QmsIqcCheckPlatformExportVO qmsIqcCheckPlatformExportVO : resultList) {
                //日期转字符串
                if(Objects.nonNull(qmsIqcCheckPlatformExportVO.getCreatedDate())){
                    String createdDateStr = DateUtil.date2String(qmsIqcCheckPlatformExportVO.getCreatedDate(), "yyyy-MM-dd HH:mm:ss");
                    qmsIqcCheckPlatformExportVO.setCreatedDateStr(createdDateStr);
                }
                if(Objects.nonNull(qmsIqcCheckPlatformExportVO.getInspectionFinishDate())){
                    String inspectionFinishDateStr = DateUtil.date2String(qmsIqcCheckPlatformExportVO.getInspectionFinishDate(), "yyyy-MM-dd HH:mm:ss");
                    qmsIqcCheckPlatformExportVO.setInspectionFinishDateStr(inspectionFinishDateStr);
                }
                if (Objects.nonNull(qmsIqcCheckPlatformExportVO.getInspectionUnqualifiedDate())) {
                    String inspectionUnqualifiedDateStr = DateUtil.date2String(qmsIqcCheckPlatformExportVO.getInspectionUnqualifiedDate(), "yyyy-MM-dd HH mm:ss");
                    qmsIqcCheckPlatformExportVO.setInspectionUnqualifiedDateStr(inspectionUnqualifiedDateStr);
                }
                MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(qmsIqcCheckPlatformExportVO.getReceiptBy()));
                if (mtUserInfo != null) {
                    qmsIqcCheckPlatformExportVO.setReceiptBy(mtUserInfo.getRealName());
                }
                //检验结果
                if (StringUtils.isNotEmpty(qmsIqcCheckPlatformExportVO.getInspectionResult())) {
                    for (LovValueDTO lovValueDTO:lovValueDTOS) {
                        if(qmsIqcCheckPlatformExportVO.getInspectionResult().equals(lovValueDTO.getValue())){
                            qmsIqcCheckPlatformExportVO.setInspectionResultMeaning(lovValueDTO.getMeaning());
                            break;
                        }
                    }
                }
                //供应商批次
                String instructionId = null;
                if(WmsConstant.DocType.DELIVERY_DOC.equals(qmsIqcCheckPlatformExportVO.getDocType())){
                    //如果检验单类型DOC_TYPE 为DELIVERY_DOC,
                    instructionId = qmsIqcCheckPlatformExportVO.getDocLineId();
                }else if(WmsConstant.DocType.IQC_DOC.equals(qmsIqcCheckPlatformExportVO.getDocType())){
                    //如果DOC_TYPE为IQC_DOC,则根据DOC_HEADER_ID关联QMS_IQC_HEADER得到DOC_LINE_ID
                    QmsIqcHeader qmsIqcHeader = qmsIqcHeaderRepository.selectByPrimaryKey(qmsIqcCheckPlatformExportVO.getDocHeaderId());
                    instructionId = qmsIqcHeader.getDocLineId();
                }
                if (StringUtils.isNotBlank(instructionId)) {
                    List<String> supplierLot = qmsIqcCheckPlatformMapper.getSupplierLotByInstructionId(tenantId, instructionId);
                    if (CollectionUtils.isNotEmpty(supplierLot)) {
                        qmsIqcCheckPlatformExportVO.setSupplierLot(StringUtils.join(supplierLot, ";"));
                    }
                }
                // 检验组信息 （与业务确认：测试环境数据会存在多条、正式不会）
                List<QmsIqcTagGroupVO> qmsIqcTagGroupVoList = qmsIqcCheckPlatformMapper.queryTagGroupDescriptionByMaterial2(qmsIqcCheckPlatformExportVO.getMaterialId(),
                        qmsIqcCheckPlatformExportVO.getMaterialVersion(), qmsIqcCheckPlatformExportVO.getInspectionType());
                if (CollectionUtils.isNotEmpty(qmsIqcTagGroupVoList)) {
                    qmsIqcCheckPlatformExportVO.setTagGroupCode(qmsIqcTagGroupVoList.get(0).getTagGroupCode());
                    qmsIqcCheckPlatformExportVO.setTagGroupDescription(qmsIqcTagGroupVoList.get(0).getTagGroupDescription());
                }
                //等级汇总
                List<QmsIqcGradeVO> qmsIqcGradeVOList = qmsIqcGradeMap.get(qmsIqcCheckPlatformExportVO.getInstructionId());
                if (CollectionUtils.isNotEmpty(qmsIqcGradeVOList)) {
                    qmsIqcCheckPlatformExportVO.setGrade(qmsIqcGradeVOList.get(0).getGrade());
                }
            }
        }
        return resultList;
    }

    @Override
    @ProcessLovValue
    public List<QmsQualityFileVO> qualityFileQuery(Long tenantId, String iqcHeaderId) {
        QmsIqcHeader qmsIqcHeader = qmsIqcHeaderRepository.selectByPrimaryKey(iqcHeaderId);
        // 获取行ID
        String docLineId = "";
        // 判断首次还是二次检验单 INSPECTION_TYPE为FIRST_INSPECTION 为首次 SECOND_INSPECTION为二次
        if (StringUtils.equals(qmsIqcHeader.getInspectionType(), "FIRST_INSPECTION")) {
            docLineId = qmsIqcHeader.getDocLineId();
        } else if (StringUtils.equals(qmsIqcHeader.getInspectionType(), "SECOND_INSPECTION")) {
            // 根据二次找首次检验单 再找到送货单行
            List<QmsIqcHeader> qmsIqcHeaderList = qmsIqcHeaderRepository.select(new QmsIqcHeader() {{
                setTenantId(tenantId);
                setIqcHeaderId(qmsIqcHeader.getDocHeaderId());
            }});
            docLineId = CollectionUtils.isNotEmpty(qmsIqcHeaderList) ? qmsIqcHeaderList.get(0).getDocLineId() : "";
        }
        if (StringUtils.isBlank(docLineId)) {
            throw new MtException("QMS_MATERIAL_INSP_P0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_P0042", "QMS"));
        }
        return qmsIqcCheckPlatformMapper.qualityFileQuery(tenantId, docLineId);
    }

    @Override
    @ProcessLovValue
    public Page<QmsQualityFileVO2> qualityFileImportQuery(Long tenantId, String fileUrl, PageRequest pageRequest) {
        List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("HME.QUANTITY_ANALYZE_G_TYPE", tenantId);
        Page<QmsQualityFileVO2> pageObj = PageHelper.doPage(pageRequest, () -> qmsIqcCheckPlatformMapper.qualityFileImportQuery(tenantId, fileUrl));
        for (QmsQualityFileVO2 qualityFileVO2 : pageObj.getContent()) {
            // 光学 类型要转化值集
            if ("G".equals(qualityFileVO2.getType())) {
                if (StringUtils.isNotBlank(qualityFileVO2.getTest1())) {
                    Optional<LovValueDTO> lovOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(lov.getValue(), qualityFileVO2.getTest1())).findFirst();
                    if (lovOpt.isPresent()) {
                        qualityFileVO2.setTestOneMeaning(lovOpt.get().getMeaning());
                    }
                }
            }
        }
        return pageObj;
    }
}


