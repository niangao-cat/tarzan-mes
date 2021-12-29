package com.ruike.hme.app.handler;

import com.ruike.hme.app.service.HmeEqManageTaskDocService;
import com.ruike.hme.infra.constant.HmeConstants;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


/**
 * 按班次生成任务单JOB
 *
 * @author jiangling.zheng@hand-china.com 2020-06-16 20:34
 */
@JobHandler("hmeCreateTaskByShiftJob")
public class HmeEqCheckByShiftJobHandler implements IJobHandler {

    @Autowired
    private HmeEqManageTaskDocService hmeEqManageTaskDocService;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 获取任务所属租户
        Long tenantId = tool.getBelongTenantId();
        try {
            this.hmeEqManageTaskDocService.createEqTaskDoc(tenantId, HmeConstants.Cycle.SHIFT);
            tool.info("Create task document run success!!!");
        } catch (Exception ex) {
            tool.error("Create task document run failed!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }
}
