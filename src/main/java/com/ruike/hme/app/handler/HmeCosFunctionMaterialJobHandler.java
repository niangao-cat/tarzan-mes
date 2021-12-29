package com.ruike.hme.app.handler;

import com.ruike.hme.app.service.HmeCosFunctionMaterialService;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@JobHandler("hmeCosFunctionMaterialJob")
public class HmeCosFunctionMaterialJobHandler implements IJobHandler {

    @Autowired
    private HmeCosFunctionMaterialService hmeCosFunctionMaterialService;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 获取任务所属租户
        Long tenantId = tool.getBelongTenantId();
        try {
            hmeCosFunctionMaterialService.createCosFunctionMaterial(tenantId);
            tool.info("Create task document run success.HmeCosFunctionMaterialJobHandler!!!");
        } catch (Exception ex) {
            tool.error("Create task document run failed.HmeCosFunctionMaterialJobHandler!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }
}
