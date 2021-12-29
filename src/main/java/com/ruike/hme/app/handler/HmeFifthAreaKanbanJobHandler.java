package com.ruike.hme.app.handler;

import com.ruike.hme.app.service.HmeFifthAreaKanbanService;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@JobHandler("hmeFifthAreaKanbanJob")
public class HmeFifthAreaKanbanJobHandler implements IJobHandler {

    @Autowired
    private HmeFifthAreaKanbanService hmeFifthAreaKanbanService;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 获取任务所属租户
        Long tenantId = tool.getBelongTenantId();
        try {
            hmeFifthAreaKanbanService.createFifthArea(tenantId);
            tool.info("Create task document run success.HmeFifthAreaKanbanJobHandler!!!");
        } catch (Exception ex) {
            tool.error("Create task document run failed.HmeFifthAreaKanbanJobHandler!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }
}
