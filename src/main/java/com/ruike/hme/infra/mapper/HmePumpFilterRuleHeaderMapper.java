package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmePumpFilterRuleHeader;
import com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO;
import com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO2;
import com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO3;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 泵浦源筛选规则头表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-20 14:28:35
 */
public interface HmePumpFilterRuleHeaderMapper extends BaseMapper<HmePumpFilterRuleHeader> {

    /**
     * 泵浦源筛选规则列表
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO>
     * @author sanfeng.zhang@hand-china.com 2021/8/20
     */
    List<HmePumpFilterRuleHeaderVO> queryFilterRuleList(@Param("tenantId") Long tenantId, @Param("dto") HmePumpFilterRuleHeaderVO dto);

    /**
     * 泵浦源筛选规则-行信息列表
     * @param tenantId
     * @param ruleHeadId
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO2>
     * @author sanfeng.zhang@hand-china.com 2021/8/20
     */
    List<HmePumpFilterRuleHeaderVO2> queryRuleLineList(@Param("tenantId") Long tenantId, @Param("ruleHeadId") String ruleHeadId);

    /**
     * 泵浦源筛选规则-历史列表
     * @param tenantId
     * @param ruleHeadId
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO3>
     * @author sanfeng.zhang@hand-china.com 2021/8/20
     */
    List<HmePumpFilterRuleHeaderVO3> queryRuleHistoryList(@Param("tenantId") Long tenantId, @Param("ruleHeadId") String ruleHeadId);

    /**
     * 更新头
     * @param tenantId
     * @param userId
     * @param ruleHeader
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/8/20
     */
    void myUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("dto") HmePumpFilterRuleHeader ruleHeader);
}
