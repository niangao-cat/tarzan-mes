package com.ruike.hme.app.handler;

import com.ruike.hme.app.service.HmeEmployeeOutputSummaryService;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 定时创建员工产量汇总数据
 *
 * @author penglin.sui@hand-china.com 2021-7-30 11:25
 */
@JobHandler("hmeEmployeeOutputSummaryJob")
public class HmeEmployeeOutputSummaryJobHandler implements IJobHandler {

    @Autowired
    private HmeEmployeeOutputSummaryService hmeEmployeeOutputSummaryService;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 获取任务所属租户
        Long tenantId = tool.getBelongTenantId();
        try {
            hmeEmployeeOutputSummaryService.employeeOutPutSummary(tenantId);
            tool.info("Update task status document run success!!!");
        } catch (Exception ex) {
            tool.error("Update task status document run failed!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }
}
