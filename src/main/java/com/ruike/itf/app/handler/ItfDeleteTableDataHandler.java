package com.ruike.itf.app.handler;

import com.ruike.itf.app.service.ItfDeleteTableDataService;
import com.ruike.itf.infra.mapper.ItfDeleteTableDataMapper;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
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

/**
 * 定时删除数据
 *
 * @author kejin.liu01@hand-china.com 2020/9/8 14:56
 * modify by penglin.sui@hand-china.com 2021/7/25 16:45
 */
@JobHandler("ItfDeleteTableDataHandler")
public class ItfDeleteTableDataHandler implements IJobHandler {

    @Autowired
    private ItfDeleteTableDataService itfDeleteTableDataService;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        try {
            itfDeleteTableDataService.invokeDel(tool.getBelongTenantId());
            tool.info("delete collect table data run success!!!");
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            tool.error("delete collect table data run failed!!!" + e.getMessage() + ",API【ItfDeleteTableDataHandler.execute】");
            tool.error(Arrays.toString(e.getStackTrace()));
            return ReturnT.FAILURE;
        }
    }
}
