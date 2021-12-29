package io.tarzan.common.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.domain.entity.MtNumrangeRule;
import io.tarzan.common.domain.vo.MtNumrangeRuleVO2;

/**
 * 号码段定义组合规则表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:43
 */
public interface MtNumrangeRuleMapper extends BaseMapper<MtNumrangeRule> {
    List<MtNumrangeRule> queryNumrangeRuleByCondition(@Param(value = "tenantId") Long tenantId,
                                                      @Param(value = "numrangeRuleIds") List<String> numrangeRuleIds);

    /**
     * 查询规则
     * @param tenantId
     * @param numrangeRuleId
     * @author YY 2019-08-26 11:44 上午
     * @return MtNumrangeRuleVO2
     */
    MtNumrangeRuleVO2 queryNumrangeRule(@Param(value = "tenantId") Long tenantId,
                                        @Param(value = "numrangeRuleId") String numrangeRuleId);

    /**
     * 添加行锁
     * 
     * @param tenantId
     * @param numrangeRuleIds
     * @author sanfeng.zhang@hand-china.com 2020/11/30 21:15 
     * @return java.util.List<io.tarzan.common.domain.entity.MtNumrangeRule>
     */
    List<MtNumrangeRule> selectForUpdates(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "numrangeRuleIds") List<String> numrangeRuleIds);
}
