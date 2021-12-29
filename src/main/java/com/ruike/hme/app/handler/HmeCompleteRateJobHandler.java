package com.ruike.hme.app.handler;

import com.ruike.hme.app.service.HmeCompleteRateService;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 月度计划报表定时任务
 *
 * @author: chaonan.hu@hand-china.com 2021-07-16 14:36:03
 **/
@JobHandler("hmeCompleteRateJob")
public class HmeCompleteRateJobHandler implements IJobHandler {

    @Autowired
    private HmeCompleteRateService hmeCompleteRateService;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 获取任务所属租户
        Long tenantId = tool.getBelongTenantId();
        try {
            this.hmeCompleteRateService.monthlyPlanQuery(tenantId);
            tool.info("Create task status document run success!!!");
        } catch (Exception ex) {
            tool.error("Create task status document run failed!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }

}
