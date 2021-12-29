package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO;
import com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO2;
import com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmePumpFilterRuleHeader;

import java.util.List;

/**
 * 泵浦源筛选规则头表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-20 14:28:35
 */
public interface HmePumpFilterRuleHeaderRepository extends BaseRepository<HmePumpFilterRuleHeader>, AopProxy<HmePumpFilterRuleHeaderRepository> {

    /**
     * 泵浦源筛选规则列表
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO>
     * @author sanfeng.zhang@hand-china.com 2021/8/20
     */
    Page<HmePumpFilterRuleHeaderVO> queryFilterRuleList(Long tenantId, HmePumpFilterRuleHeaderVO dto, PageRequest pageRequest);

    /**
     * 泵浦源筛选规则-行信息列表
     *
     * @param tenantId
     * @param ruleHeadId
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO2>
     * @author sanfeng.zhang@hand-china.com 2021/8/20
     */
    List<HmePumpFilterRuleHeaderVO2> queryRuleLineList(Long tenantId, String ruleHeadId);

    /**
     * 泵浦源筛选规则-历史列表
     *
     * @param tenantId
     * @param ruleHeadId
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO3>
     * @author sanfeng.zhang@hand-china.com 2021/8/20
     */
    Page<HmePumpFilterRuleHeaderVO3> queryRuleHistoryList(Long tenantId, String ruleHeadId, PageRequest pageRequest);

    /** 
     * 新增&更新泵浦源筛选规则头
     *
     * @param tenantId
     * @param ruleHeader
     * @return com.ruike.hme.domain.entity.HmePumpFilterRuleHeader
     * @author sanfeng.zhang@hand-china.com 2021/8/20  
     */
    HmePumpFilterRuleHeader saveRuleHeaderForUi(Long tenantId, HmePumpFilterRuleHeader ruleHeader);
}
