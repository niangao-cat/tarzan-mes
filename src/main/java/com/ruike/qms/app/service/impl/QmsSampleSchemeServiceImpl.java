package com.ruike.qms.app.service.impl;

import com.ruike.qms.api.dto.QmsSampleSchemeDTO;
import com.ruike.qms.api.dto.QmsSampleSchemeDTO2;
import com.ruike.qms.api.dto.QmsSampleSchemeDTO3;
import com.ruike.qms.api.dto.QmsSampleSchemeDTO4;
import com.ruike.qms.app.service.QmsSampleSchemeService;
import com.ruike.qms.domain.entity.QmsSampleScheme;
import com.ruike.qms.domain.repository.QmsSampleSchemeRepository;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsSampleSchemeMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;

/**
 * 抽样方案表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-04-30 15:05:10
 */
@Service
public class QmsSampleSchemeServiceImpl extends BaseServiceImpl<QmsSampleScheme>implements QmsSampleSchemeService {

    @Autowired
    private QmsSampleSchemeMapper qmsSampleSchemeMapper;
    @Autowired
    private QmsSampleSchemeRepository qmsSampleSchemeRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    @ProcessLovValue
    public Page<QmsSampleSchemeDTO> listSampleSchemeForUi(Long tenantId, QmsSampleSchemeDTO2 dto, PageRequest pageRequest) {
        Page<QmsSampleSchemeDTO> resultMap = PageHelper.doPage(pageRequest, () -> qmsSampleSchemeMapper.selectByConditionForUi(tenantId, dto));
        for (QmsSampleSchemeDTO qmsSampleSchemeDTO2:resultMap.getContent()) {
            if(StringUtils.isNotBlank(qmsSampleSchemeDTO2.getAttribute1())){
                QmsSampleSchemeDTO2 qmsSampleSchemeDTO22 = new QmsSampleSchemeDTO2();
                qmsSampleSchemeDTO22.setSchemeId(qmsSampleSchemeDTO2.getAttribute1());
                List<QmsSampleSchemeDTO> qmsSampleSchemeDTOS = qmsSampleSchemeMapper.selectByConditionForUi(tenantId, qmsSampleSchemeDTO22);
                if(CollectionUtils.isNotEmpty(qmsSampleSchemeDTOS)){
                    QmsSampleSchemeDTO qmsSampleSchemeDTO = qmsSampleSchemeDTOS.get(0);
                    String sampleStandardTypeMeaning = lovAdapter.queryLovMeaning("QMS.IQC_SAMPLE_STANDARD_TYPE", tenantId, qmsSampleSchemeDTO.getSampleStandardType());
                    qmsSampleSchemeDTO.setSampleStandardTypeMeaning(sampleStandardTypeMeaning);
                    if(StringUtils.isNotBlank(qmsSampleSchemeDTO.getSampleSizeCodeLetter())){
                        String sampleSizeCodeLetterMeaning = lovAdapter.queryLovMeaning("QMS.IQC_SAMPLE_SIZE_CODE_LEVEL", tenantId, qmsSampleSchemeDTO.getSampleSizeCodeLetter());
                        qmsSampleSchemeDTO.setSampleSizeCodeLetterMeaning(sampleSizeCodeLetterMeaning);
                    }
                    StringBuilder attribute1Meaning = new StringBuilder();
                    attribute1Meaning.append((StringUtils.isBlank(qmsSampleSchemeDTO.getSampleStandardTypeMeaning())?"":qmsSampleSchemeDTO.getSampleStandardTypeMeaning())).append("#")
                            .append(StringUtils.isBlank(qmsSampleSchemeDTO.getSampleSizeCodeLetterMeaning())?"":qmsSampleSchemeDTO.getSampleSizeCodeLetterMeaning()).append("#")
                            .append(Objects.isNull(qmsSampleSchemeDTO.getLotUpperLimit())?"":qmsSampleSchemeDTO.getLotUpperLimit()).append("#")
                            .append(Objects.isNull(qmsSampleSchemeDTO.getLotLowerLimit())?"":qmsSampleSchemeDTO.getLotLowerLimit()).append("#")
                            .append(StringUtils.isBlank(qmsSampleSchemeDTO.getAcceptanceQuantityLimitMeaning())?"":qmsSampleSchemeDTO.getAcceptanceQuantityLimitMeaning()).append("#")
                            .append(Objects.isNull(qmsSampleSchemeDTO.getSampleSize())?"":qmsSampleSchemeDTO.getSampleSize()).append("#")
                            .append(Objects.isNull(qmsSampleSchemeDTO.getAc())?"":qmsSampleSchemeDTO.getAc()).append("#")
                            .append(Objects.isNull(qmsSampleSchemeDTO.getRe())?"":qmsSampleSchemeDTO.getRe());
                    qmsSampleSchemeDTO2.setAttribute1Meaning(attribute1Meaning.toString());
                }
            }
        }
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveSampleSchemeForUi(Long tenantId, QmsSampleSchemeDTO3 dto) {
        if (dto == null) {
            return null;
        }
        if (QmsConstants.ConstantValue.STRING_ZERO.equals(dto.getSampleStandardType())) {
            if (StringUtils.isEmpty(dto.getSampleSizeCodeLetter())) {
                throw new MtException(QmsConstants.ErrorCode.QMS_SAMPLE_SCHEME_0002, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        QmsConstants.ErrorCode.QMS_SAMPLE_SCHEME_0002, QmsConstants.ConstantValue.QMS, QmsConstants.ParameterCode.P_SAMPLE_SIZE_CODE_LETTER, ""));
            }
            if (dto.getAcceptanceQuantityLimit() == null) {
                throw new MtException(QmsConstants.ErrorCode.QMS_SAMPLE_SCHEME_0002, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        QmsConstants.ErrorCode.QMS_SAMPLE_SCHEME_0002, QmsConstants.ConstantValue.QMS, QmsConstants.ParameterCode.P_ACCEPTANCE_QUANTITY_LIMIT, ""));
            }
            if (dto.getSampleSize() == null) {
                throw new MtException(QmsConstants.ErrorCode.QMS_SAMPLE_SCHEME_0002, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        QmsConstants.ErrorCode.QMS_SAMPLE_SCHEME_0002, QmsConstants.ConstantValue.QMS, QmsConstants.ParameterCode.P_SAMPLE_SIZE, ""));
            }
            if (dto.getAc() == null) {
                throw new MtException(QmsConstants.ErrorCode.QMS_SAMPLE_SCHEME_0002, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        QmsConstants.ErrorCode.QMS_SAMPLE_SCHEME_0002, QmsConstants.ConstantValue.QMS, QmsConstants.ParameterCode.P_AC, ""));
            }
            if (dto.getRe() == null) {
                throw new MtException(QmsConstants.ErrorCode.QMS_SAMPLE_SCHEME_0002, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        QmsConstants.ErrorCode.QMS_SAMPLE_SCHEME_0002, QmsConstants.ConstantValue.QMS, QmsConstants.ParameterCode.P_RE, ""));
            }
        }
        QmsSampleScheme scheme = new QmsSampleScheme();
        // 唯一性校验
        QmsSampleScheme oldScheme = new QmsSampleScheme();
        Sqls sqls = dto.getSampleSizeCodeLetter() == null ?  Sqls.custom().andIsNull(QmsSampleScheme.FIELD_SAMPLE_SIZE_CODE_LETTER) :
                Sqls.custom().andEqualTo(QmsSampleScheme.FIELD_SAMPLE_SIZE_CODE_LETTER, dto.getSampleSizeCodeLetter());
        Sqls lotUpperLimitSqls = dto.getLotUpperLimit() == null ?  Sqls.custom().andIsNull(QmsSampleScheme.FIELD_LOT_UPPER_LIMIT) :
                Sqls.custom().andEqualTo(QmsSampleScheme.FIELD_LOT_UPPER_LIMIT, dto.getLotUpperLimit());
        Sqls lotLowerLimitSqls = dto.getLotLowerLimit() == null ?  Sqls.custom().andIsNull(QmsSampleScheme.FIELD_LOT_LOWER_LIMIT) :
                Sqls.custom().andEqualTo(QmsSampleScheme.FIELD_LOT_LOWER_LIMIT, dto.getLotLowerLimit());
        List<QmsSampleScheme> oldSchemeList = qmsSampleSchemeMapper.selectByCondition(Condition.builder(QmsSampleScheme.class)
                .andWhere(Sqls.custom().andEqualTo(QmsSampleScheme.FIELD_TENANT_ID, tenantId))
                .andWhere(Sqls.custom().andEqualTo(QmsSampleScheme.FIELD_SAMPLE_PLAN_TYPE,dto.getSamplePlanType()))
                .andWhere(Sqls.custom().andEqualTo(QmsSampleScheme.FIELD_SAMPLE_STANDARD_TYPE,dto.getSampleStandardType()))
                .andWhere(sqls).andWhere(lotUpperLimitSqls).andWhere(lotLowerLimitSqls)
                .andWhere(Sqls.custom().andEqualTo(QmsSampleScheme.FIELD_ACCEPTANCE_QUANTITY_LIMIT,dto.getAcceptanceQuantityLimit()))
                .build());
        if (CollectionUtils.isNotEmpty(oldSchemeList)) {
            oldScheme = oldSchemeList.get(0);
            if (StringUtils.isEmpty(dto.getSchemeId())
                    || !dto.getSchemeId().equals(oldScheme.getSchemeId())) {
                throw new MtException(QmsConstants.ErrorCode.QMS_SAMPLE_SCHEME_0001, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        QmsConstants.ErrorCode.QMS_SAMPLE_SCHEME_0001, QmsConstants.ConstantValue.QMS));
            }
        }
        // 上下限交叉校验
        if (dto.getLotLowerLimit() != null && dto.getLotUpperLimit() != null) {
            int fromCount = qmsSampleSchemeMapper.selectByConditionCount(tenantId, dto.getSchemeId(), dto.getAcceptanceQuantityLimit(), dto.getLotLowerLimit());
            int toCount = qmsSampleSchemeMapper.selectByConditionCount(tenantId, dto.getSchemeId(), dto.getAcceptanceQuantityLimit(), dto.getLotUpperLimit());
            if (fromCount > 0 || toCount > 0) {
                throw new MtException(QmsConstants.ErrorCode.QMS_SAMPLE_LETTER_0001, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        QmsConstants.ErrorCode.QMS_SAMPLE_LETTER_0001, QmsConstants.ConstantValue.QMS));
            }
        }
        //  查询原数据
        if (StringUtils.isNotEmpty(dto.getSchemeId())) {
            oldScheme = qmsSampleSchemeRepository.selectByPrimaryKey(dto.getSchemeId());
        }
        if (oldScheme.getSchemeId() != null){
            BeanUtils.copyProperties(oldScheme, scheme);
        }
        BeanUtils.copyProperties(dto, scheme);
        scheme.setTenantId(tenantId);
        // 保存
        if (StringUtils.isNotEmpty(scheme.getSchemeId())) {
            qmsSampleSchemeMapper.updateByPrimaryKey(scheme);
        } else {
            insertSelective(scheme);
        }
        return scheme.getSchemeId();
    }

    @Override
    @ProcessLovValue
    public Page<QmsSampleSchemeDTO> sampleSchemeLovQuery(Long tenantId, QmsSampleSchemeDTO4 dto, PageRequest pageRequest) {
        Page<QmsSampleSchemeDTO> resultMap = PageHelper.doPage(pageRequest, () -> qmsSampleSchemeMapper.qmsSampleSchemeQuery(tenantId, dto));
        for (QmsSampleSchemeDTO qmsSampleSchemeDTO:resultMap.getContent()) {
            String sampleStandardTypeMeaning = lovAdapter.queryLovMeaning("QMS.IQC_SAMPLE_STANDARD_TYPE", tenantId, qmsSampleSchemeDTO.getSampleStandardType());
            qmsSampleSchemeDTO.setSampleStandardTypeMeaning(sampleStandardTypeMeaning);
            if(StringUtils.isNotBlank(qmsSampleSchemeDTO.getSampleSizeCodeLetter())){
                String sampleSizeCodeLetterMeaning = lovAdapter.queryLovMeaning("QMS.IQC_SAMPLE_SIZE_CODE_LEVEL", tenantId, qmsSampleSchemeDTO.getSampleSizeCodeLetter());
                qmsSampleSchemeDTO.setSampleSizeCodeLetterMeaning(sampleSizeCodeLetterMeaning);
            }
            StringBuilder attribute1Meaning = new StringBuilder();
            attribute1Meaning.append((StringUtils.isBlank(qmsSampleSchemeDTO.getSampleStandardTypeMeaning())?"":qmsSampleSchemeDTO.getSampleStandardTypeMeaning())).append("#")
                    .append(StringUtils.isBlank(qmsSampleSchemeDTO.getSampleSizeCodeLetterMeaning())?"":qmsSampleSchemeDTO.getSampleSizeCodeLetterMeaning()).append("#")
                    .append(Objects.isNull(qmsSampleSchemeDTO.getLotUpperLimit())?"":qmsSampleSchemeDTO.getLotUpperLimit()).append("#")
                    .append(Objects.isNull(qmsSampleSchemeDTO.getLotLowerLimit())?"":qmsSampleSchemeDTO.getLotLowerLimit()).append("#")
                    .append(StringUtils.isBlank(qmsSampleSchemeDTO.getAcceptanceQuantityLimitMeaning())?"":qmsSampleSchemeDTO.getAcceptanceQuantityLimitMeaning()).append("#")
                    .append(Objects.isNull(qmsSampleSchemeDTO.getSampleSize())?"":qmsSampleSchemeDTO.getSampleSize()).append("#")
                    .append(Objects.isNull(qmsSampleSchemeDTO.getAc())?"":qmsSampleSchemeDTO.getAc()).append("#")
                    .append(Objects.isNull(qmsSampleSchemeDTO.getRe())?"":qmsSampleSchemeDTO.getRe());
            qmsSampleSchemeDTO.setAttribute1Meaning(attribute1Meaning.toString());
        }
        return resultMap;
    }
}
