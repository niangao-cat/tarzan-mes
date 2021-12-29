package com.ruike.itf.app.handler;

import com.ruike.itf.app.service.ItfWorkOrderIfaceService;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;

import java.util.Arrays;
import java.util.Map;

@JobHandler("ItfWoReturnStatusHandler")
public class ItfWorkOrderReturnStatusHandler implements IJobHandler {
    private final ItfWorkOrderIfaceService itfWorkOrderIfaceService;

    public ItfWorkOrderReturnStatusHandler(ItfWorkOrderIfaceService itfWorkOrderIfaceService) {
        this.itfWorkOrderIfaceService = itfWorkOrderIfaceService;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        try {
            itfWorkOrderIfaceService.erpWorkOrderStatusReturnRestProxy(tool.getBelongTenantId());
            tool.info("work order status return erp update run success!!!");
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            tool.error("work order status return erp update run failed!!!" + e.getMessage());
            tool.error(Arrays.toString(e.getStackTrace()));
            return ReturnT.FAILURE;
        }
    }
}
