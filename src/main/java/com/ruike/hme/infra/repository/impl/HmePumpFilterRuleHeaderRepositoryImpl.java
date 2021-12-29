package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmePumpFilterRuleHeaderHis;
import com.ruike.hme.domain.repository.HmePumpFilterRuleHeaderHisRepository;
import com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO;
import com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO2;
import com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO3;
import com.ruike.hme.infra.mapper.HmePumpFilterRuleHeaderMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmePumpFilterRuleHeader;
import com.ruike.hme.domain.repository.HmePumpFilterRuleHeaderRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 泵浦源筛选规则头表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-20 14:28:35
 */
@Component
public class HmePumpFilterRuleHeaderRepositoryImpl extends BaseRepositoryImpl<HmePumpFilterRuleHeader> implements HmePumpFilterRuleHeaderRepository {

    @Autowired
    private HmePumpFilterRuleHeaderMapper hmePumpFilterRuleHeaderMapper;
    @Autowired
    private HmePumpFilterRuleHeaderHisRepository hmePumpFilterRuleHeaderHisRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    @ProcessLovValue
    public Page<HmePumpFilterRuleHeaderVO> queryFilterRuleList(Long tenantId, HmePumpFilterRuleHeaderVO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmePumpFilterRuleHeaderMapper.queryFilterRuleList(tenantId, dto));
    }

    @Override
    @ProcessLovValue
    public List<HmePumpFilterRuleHeaderVO2> queryRuleLineList(Long tenantId, String ruleHeadId) {
        List<HmePumpFilterRuleHeaderVO2> ruleLineList = hmePumpFilterRuleHeaderMapper.queryRuleLineList(tenantId, ruleHeadId);
        for (HmePumpFilterRuleHeaderVO2 ruleLine : ruleLineList) {
            ruleLine.setFormulaAssemble(ruleLine.getFormula());
            // 参数WPE 计算类型时 公式处理
            if (StringUtils.equals("WPE", ruleLine.getParameterCode()) && "CALCULATION".equals(ruleLine.getCalculateType())) {
                ruleLine.setFormulaAssemble(ruleLine.getFormula());
                // 取出数值 P/(V*20) -> 20
                if (StringUtils.isNotBlank(ruleLine.getFormula())) {
                    ruleLine.setFormula(this.getNumbers(ruleLine.getFormula()));
                }
            }
        }
        return ruleLineList;
    }

    //截取数字
    private static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("[1-9]\\d*\\.?\\d*");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    @Override
    @ProcessLovValue
    public Page<HmePumpFilterRuleHeaderVO3> queryRuleHistoryList(Long tenantId, String ruleHeadId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmePumpFilterRuleHeaderMapper.queryRuleHistoryList(tenantId, ruleHeadId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmePumpFilterRuleHeader saveRuleHeaderForUi(Long tenantId, HmePumpFilterRuleHeader ruleHeader) {
        // 校验物料编码具有唯一性
        List<HmePumpFilterRuleHeader> ruleHeaderList = hmePumpFilterRuleHeaderMapper.selectByCondition(Condition.builder(HmePumpFilterRuleHeader.class).select(HmePumpFilterRuleHeader.FIELD_RULE_HEAD_ID, HmePumpFilterRuleHeader.FIELD_MATERIAL_ID)
                .andWhere(Sqls.custom()
                        .andEqualTo(HmePumpFilterRuleHeader.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmePumpFilterRuleHeader.FIELD_MATERIAL_ID, ruleHeader.getMaterialId())).build());
        // 新建&更新
        if (StringUtils.isNotBlank(ruleHeader.getRuleHeadId())) {
            if (CollectionUtils.isNotEmpty(ruleHeaderList) && !StringUtils.equals(ruleHeader.getRuleHeadId(), ruleHeaderList.get(0).getRuleHeadId())) {
                throw new MtException("HME_PUMP_RULE_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_RULE_001", "HME"));
            }
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            hmePumpFilterRuleHeaderMapper.myUpdate(tenantId, userId, ruleHeader);
        } else {
            if (CollectionUtils.isNotEmpty(ruleHeaderList)) {
                throw new MtException("HME_PUMP_RULE_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_RULE_001", "HME"));
            }
            ruleHeader.setTenantId(tenantId);
            self().insertSelective(ruleHeader);
        }
        // 记录历史
        HmePumpFilterRuleHeaderHis headerHis = new HmePumpFilterRuleHeaderHis();
        BeanUtils.copyProperties(ruleHeader, headerHis);
        headerHis.setTenantId(tenantId);
        hmePumpFilterRuleHeaderHisRepository.insertSelective(headerHis);
        return ruleHeader;
    }
}
