package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmePumpFilterRuleLine;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 泵浦源筛选规则行表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-20 14:28:35
 */
public interface HmePumpFilterRuleLineMapper extends BaseMapper<HmePumpFilterRuleLine> {

    /**
     * 最近泵浦源筛选规则头历史ID
     * @param tenantId
     * @param ruleHeadId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/8/20
     */
    String queryNearHeaderHisId(@Param("tenantId") Long tenantId, @Param("ruleHeadId") String ruleHeadId);

    /**
     * 更新数据
     * @param tenantId
     * @param userId
     * @param ruleLine
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/8/20
     */
    void myUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("dto") HmePumpFilterRuleLine ruleLine);
}
