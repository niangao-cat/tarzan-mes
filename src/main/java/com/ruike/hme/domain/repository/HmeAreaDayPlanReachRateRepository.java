package com.ruike.hme.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeAreaDayPlanReachRate;

/**
 * 制造部日计划达成率看板资源库
 *
 * @author sanfeng.zhang@hand-china.com 2021-07-02 14:31:13
 */
public interface HmeAreaDayPlanReachRateRepository extends BaseRepository<HmeAreaDayPlanReachRate>, AopProxy<HmeAreaDayPlanReachRateRepository> {

    /**
     * 保存日计划达成率数据
     * @param tenantId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/7/2
     */
    void createDayPlanReachRate(Long tenantId);
}
