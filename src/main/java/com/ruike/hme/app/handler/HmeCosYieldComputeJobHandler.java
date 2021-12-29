package com.ruike.hme.app.handler;

import com.ruike.hme.app.service.HmeCosYieldComputeService;
import com.ruike.hme.domain.repository.HmeAreaDayPlanReachRateRepository;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * COS良率计算定时任务
 *
 * @author sanfeng.zhang@hand-china.com 2021/7/2 14:37
 */
@JobHandler("hmeCosYieldComputeJob")
public class HmeCosYieldComputeJobHandler implements IJobHandler {

    @Autowired
    private HmeCosYieldComputeService hmeCosYieldComputeService;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 获取任务所属租户
        Long tenantId = tool.getBelongTenantId();
        try {
            this.hmeCosYieldComputeService.cosYieldComputeJob(tenantId);
            tool.info("Create task status document run success!!!");
        } catch (Exception ex) {
            tool.error("Create task status document run failed!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }
}
