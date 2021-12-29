package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmePumpFilterRuleLine;
import com.ruike.hme.domain.entity.HmePumpFilterRuleLineHis;
import com.ruike.hme.domain.repository.HmePumpFilterRuleLineHisRepository;
import com.ruike.hme.domain.repository.HmePumpFilterRuleLineRepository;
import com.ruike.hme.infra.mapper.HmePumpFilterRuleLineMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 泵浦源筛选规则行表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-20 14:28:35
 */
@Component
public class HmePumpFilterRuleLineRepositoryImpl extends BaseRepositoryImpl<HmePumpFilterRuleLine> implements HmePumpFilterRuleLineRepository {

    @Autowired
    private HmePumpFilterRuleLineMapper hmePumpFilterRuleLineMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmePumpFilterRuleLineHisRepository hmePumpFilterRuleLineHisRepository;

    private void verifyData (Long tenantId, HmePumpFilterRuleLine ruleLine) {
        // 参数代码为WPE，计算类型为计算，字段公式仅能填正整数
        if (StringUtils.equals("WPE", ruleLine.getParameterCode()) && "CALCULATION".equals(ruleLine.getCalculateType())) {
            boolean numeric = CommonUtils.isNumeric(ruleLine.getFormula());
            if (!numeric || BigDecimal.valueOf(Double.valueOf(ruleLine.getFormula())).compareTo(BigDecimal.ZERO) <= 0) {
                throw new MtException("HME_PUMP_RULE_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_RULE_005", "HME"));
            }
            // 组合公式进行存储 20 -> P/(V*20)
            ruleLine.setFormula("P/(V*?)".replace("?", ruleLine.getFormula()));
        }
        // 校验最大值和最小值 最大值不能小于最小值
        if (ruleLine.getMinValue() != null && ruleLine.getMaxValue() != null) {
            if (ruleLine.getMinValue().compareTo(ruleLine.getMaxValue()) > 0) {
                throw new MtException("HME_PUMP_RULE_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_RULE_006", "HME"));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmePumpFilterRuleLine saveRuleLineForUi(Long tenantId, HmePumpFilterRuleLine ruleLine) {
        this.verifyData(tenantId, ruleLine);
        // 在泵浦源筛选规则头下 行的参数代码要唯一 排序也要唯一
        List<HmePumpFilterRuleLine> hmePumpFilterRuleLines = hmePumpFilterRuleLineMapper.selectByCondition(Condition.builder(HmePumpFilterRuleLine.class).select(
                        HmePumpFilterRuleLine.FIELD_RULE_LINE_ID,
                        HmePumpFilterRuleLine.FIELD_PARAMETER_CODE,
                        HmePumpFilterRuleLine.FIELD_RULE_HEAD_ID,
                        HmePumpFilterRuleLine.FIELD_SEQUENCE,
                        HmePumpFilterRuleLine.FIELD_CALCULATE_TYPE,
                        HmePumpFilterRuleLine.FIELD_TAG_ID,
                        HmePumpFilterRuleLine.FIELD_FORMULA,
                        HmePumpFilterRuleLine.FIELD_MAX_VALUE,
                        HmePumpFilterRuleLine.FIELD_MIN_VALUE)
                .andWhere(Sqls.custom()
                        .andEqualTo(HmePumpFilterRuleLine.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmePumpFilterRuleLine.FIELD_RULE_HEAD_ID, ruleLine.getRuleHeadId())).build());
        // 单个符合类型的同一个数据项第一条数据最大值≤第二条数据最小值 序号来确定顺序
        List<HmePumpFilterRuleLine> singleList = hmePumpFilterRuleLines.stream().filter(line -> StringUtils.equals(line.getCalculateType(), "SINGLE") && StringUtils.equals(line.getTagId(), ruleLine.getTagId())).collect(Collectors.toList());
        List<HmePumpFilterRuleLine> filterPumpFilterRuleLines = hmePumpFilterRuleLines.stream().filter(vo -> StringUtils.equals(vo.getParameterCode(), ruleLine.getParameterCode())).collect(Collectors.toList());
        List<Long> sequenceList = hmePumpFilterRuleLines.stream().map(HmePumpFilterRuleLine::getSequence).distinct().collect(Collectors.toList());
        // 新建&更新
        if (StringUtils.isNotBlank(ruleLine.getRuleLineId())) {
            if (CollectionUtils.isNotEmpty(filterPumpFilterRuleLines) && !StringUtils.equals(ruleLine.getRuleLineId(), filterPumpFilterRuleLines.get(0).getRuleLineId())) {
                throw new MtException("HME_PUMP_RULE_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_RULE_002", "HME"));
            }
            List<HmePumpFilterRuleLine> filterSequencePumpFilterRuleLines = hmePumpFilterRuleLines.stream().filter(vo -> vo.getSequence().compareTo(ruleLine.getSequence()) == 0).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(filterSequencePumpFilterRuleLines) && !StringUtils.equals(ruleLine.getRuleLineId(), filterSequencePumpFilterRuleLines.get(0).getRuleLineId())) {
                throw new MtException("HME_PUMP_RULE_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_RULE_003", "HME"));
            }
            // 计算类型为SUM 数据项能重复
            List<HmePumpFilterRuleLine> filterCalculateTypeLines = hmePumpFilterRuleLines.stream().filter(vo -> StringUtils.equals(vo.getCalculateType(), "SUM") && StringUtils.equals(vo.getTagId(), ruleLine.getTagId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(filterCalculateTypeLines) &&
                    !StringUtils.equals(ruleLine.getRuleLineId(), filterCalculateTypeLines.get(0).getRuleLineId()) &&
                    "SUM".equals(ruleLine.getCalculateType())) {
                throw new MtException("HME_PUMP_RULE_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_RULE_004", "HME"));
            }
            // 拿到前后的数据比较 最小值大于前一条的最大值 最大值小于后一条的最小值 最小值为空 为负无穷 最大值为空 则为正无穷 进行卡控
            if (CollectionUtils.isNotEmpty(singleList)) {
                // 将自身过滤掉
                Optional<HmePumpFilterRuleLine> beforeOpt = singleList.stream().filter(line -> line.getSequence().compareTo(ruleLine.getSequence()) < 0 && !line.getRuleLineId().equals(ruleLine.getRuleLineId())).sorted(Comparator.comparing(HmePumpFilterRuleLine::getSequence).reversed()).findFirst();
                if (beforeOpt.isPresent()) {
                    if (beforeOpt.get().getMaxValue() == null || beforeOpt.get().getMaxValue().compareTo(ruleLine.getMinValue()) > 0) {
                        throw new MtException("HME_PUMP_RULE_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_PUMP_RULE_007", "HME"));
                    }
                }
                Optional<HmePumpFilterRuleLine> afterOpt = singleList.stream().filter(line -> line.getSequence().compareTo(ruleLine.getSequence()) > 0 && !line.getRuleLineId().equals(ruleLine.getRuleLineId())).sorted(Comparator.comparing(HmePumpFilterRuleLine::getSequence)).findFirst();
                if (afterOpt.isPresent()) {
                    if (afterOpt.get().getMinValue() == null || afterOpt.get().getMinValue().compareTo(ruleLine.getMaxValue()) < 0) {
                        throw new MtException("HME_PUMP_RULE_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_PUMP_RULE_007", "HME"));
                    }
                }
            }
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            hmePumpFilterRuleLineMapper.myUpdate(tenantId, userId, ruleLine);
        } else {
            if (CollectionUtils.isNotEmpty(filterPumpFilterRuleLines)) {
                throw new MtException("HME_PUMP_RULE_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_RULE_002", "HME"));
            }
            if (sequenceList.contains(ruleLine.getSequence())) {
                throw new MtException("HME_PUMP_RULE_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_RULE_003", "HME"));
            }
            // 计算类型为SUM 数据项能重复
            List<HmePumpFilterRuleLine> filterCalculateTypeLines = hmePumpFilterRuleLines.stream().filter(vo -> StringUtils.equals(vo.getCalculateType(), "SUM") && StringUtils.equals(vo.getTagId(), ruleLine.getTagId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(filterCalculateTypeLines) && "SUM".equals(ruleLine.getCalculateType())) {
                throw new MtException("HME_PUMP_RULE_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_RULE_004", "HME"));
            }
            // 拿到前后的数据比较 最小值大于前一条的最大值 最大值小于后一条的最小值 最小值为空 为负无穷 最大值为空 则为正无穷 进行卡控
            if (CollectionUtils.isNotEmpty(singleList)) {
                Optional<HmePumpFilterRuleLine> beforeOpt = singleList.stream().filter(line -> line.getSequence().compareTo(ruleLine.getSequence()) < 0).sorted(Comparator.comparing(HmePumpFilterRuleLine::getSequence).reversed()).findFirst();
                if (beforeOpt.isPresent()) {
                    if (beforeOpt.get().getMaxValue() == null || beforeOpt.get().getMaxValue().compareTo(ruleLine.getMinValue()) > 0) {
                        throw new MtException("HME_PUMP_RULE_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_PUMP_RULE_007", "HME"));
                    }
                }
                Optional<HmePumpFilterRuleLine> afterOpt = singleList.stream().filter(line -> line.getSequence().compareTo(ruleLine.getSequence()) > 0).sorted(Comparator.comparing(HmePumpFilterRuleLine::getSequence)).findFirst();
                if (afterOpt.isPresent()) {
                    if (afterOpt.get().getMinValue() == null || afterOpt.get().getMinValue().compareTo(ruleLine.getMaxValue()) < 0) {
                        throw new MtException("HME_PUMP_RULE_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_PUMP_RULE_007", "HME"));
                    }
                }
            }
            ruleLine.setTenantId(tenantId);
            self().insertSelective(ruleLine);
        }
        // 记录历史 找最近的头更新历史
        String headerHisId = hmePumpFilterRuleLineMapper.queryNearHeaderHisId(tenantId, ruleLine.getRuleHeadId());
        HmePumpFilterRuleLineHis lineHis = new HmePumpFilterRuleLineHis();
        BeanUtils.copyProperties(ruleLine, lineHis);
        lineHis.setTenantId(tenantId);
        lineHis.setRuleHeadHisId(headerHisId);
        hmePumpFilterRuleLineHisRepository.insertSelective(lineHis);
        return ruleLine;
    }
}
