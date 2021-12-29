package com.ruike.itf.app.handler;

import com.ruike.itf.app.service.ItfDeleteCollectTableDataService;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@JobHandler("ItfDeleteCollectDataHandler")
public class ItfDeleteCollectTableDataHandler implements IJobHandler {

    @Autowired
    private ItfDeleteCollectTableDataService itfDeleteCollectTableDataService;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        try {
            itfDeleteCollectTableDataService.invokeDel(tool.getBelongTenantId());
            tool.info("delete collect table data run success!!!");
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            tool.error("delete collect table data run failed!!!" + e.getMessage() + ",API【ItfDeleteCollectTableDataHandler.execute】");
            tool.error(Arrays.toString(e.getStackTrace()));
            return ReturnT.FAILURE;
        }
    }
}
