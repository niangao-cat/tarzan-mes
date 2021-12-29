package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmePumpFilterRuleHeader;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmePumpFilterRuleLine;

/**
 * 泵浦源筛选规则行表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-20 14:28:35
 */
public interface HmePumpFilterRuleLineRepository extends BaseRepository<HmePumpFilterRuleLine>, AopProxy<HmePumpFilterRuleLineRepository> {

    /**
     * 新增&更新泵浦源筛选规则行
     *
     * @param tenantId
     * @param ruleLine
     * @return com.ruike.hme.domain.entity.HmePumpFilterRuleLine
     * @author sanfeng.zhang@hand-china.com 2021/8/20
     */
    HmePumpFilterRuleLine saveRuleLineForUi(Long tenantId, HmePumpFilterRuleLine ruleLine);
    
}
