package com.ruike.itf.app.handler;

import com.ruike.itf.domain.repository.ItfSoDeliveryChanOrPostIfaceRepository;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Map;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/16 10:08
 */
@JobHandler("ItfSoDeliveryChanOrPostHandler")
public class ItfSoDeliveryChanOrPostIfaceHandler implements IJobHandler {

    @Autowired
    private ItfSoDeliveryChanOrPostIfaceRepository itfSoDeliveryChanOrPostIfaceRepository;


    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        Long tenantId = tool.getBelongTenantId();
        try {
            itfSoDeliveryChanOrPostIfaceRepository.postIfaceToESB(tenantId);
            tool.info("发货通知单修改回传&拣配接口成功!!!");
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            tool.error("发货通知单修改回传&拣配接口失败!!!" + e.getMessage());
            tool.error(Arrays.toString(e.getStackTrace()));
            return ReturnT.FAILURE;
        }
    }
}
