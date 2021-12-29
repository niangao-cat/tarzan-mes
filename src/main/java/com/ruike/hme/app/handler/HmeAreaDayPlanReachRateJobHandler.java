package com.ruike.hme.app.handler;

import com.ruike.hme.domain.repository.HmeAreaDayPlanReachRateRepository;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 制造部-日计划达成率看板 定时任务
 *
 * @author sanfeng.zhang@hand-china.com 2021/7/2 14:37
 */
@JobHandler("hmeAreaDayPlanReachRateJob")
public class HmeAreaDayPlanReachRateJobHandler implements IJobHandler {

    @Autowired
    private HmeAreaDayPlanReachRateRepository hmeAreaDayPlanReachRateRepository;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 获取任务所属租户
        Long tenantId = tool.getBelongTenantId();
        try {
            this.hmeAreaDayPlanReachRateRepository.createDayPlanReachRate(tenantId);
            tool.info("Create task status document run success!!!");
        } catch (Exception ex) {
            tool.error("Create task status document run failed!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }
}
