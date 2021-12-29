package com.ruike.itf.app.handler;

import com.ruike.itf.app.service.ItfWoSnRelIfaceService;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Map;

/**
 * SAP 工单SN码关系接口调度任务
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/24 5:31 下午
 */
@JobHandler("ItfWoSnRelIfaceJob")
public class ItfWoSnRelIfaceHandler implements IJobHandler {
    private final ItfWoSnRelIfaceService itfWoSnRelIfaceService;

    @Autowired
    public ItfWoSnRelIfaceHandler(ItfWoSnRelIfaceService itfWoSnRelIfaceService) {
        this.itfWoSnRelIfaceService = itfWoSnRelIfaceService;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        try {
            itfWoSnRelIfaceService.invoke(tool.getBelongTenantId());
            tool.info("wo sn update run success!!!");
        } catch (Exception e) {
            tool.error("wo sn update run failed!!!" + e.getMessage());
            tool.error(Arrays.toString(e.getStackTrace()));
            return ReturnT.FAILURE;
        }

        return ReturnT.SUCCESS;
    }
}
