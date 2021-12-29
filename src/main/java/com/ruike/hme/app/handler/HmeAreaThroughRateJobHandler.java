package com.ruike.hme.app.handler;

import com.ruike.hme.domain.repository.HmeAreaThroughRateDetailsRepository;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 制造部-直通率看板 定时任务
 *
 * @author sanfeng.zhang@hand-china.com 2021/6/24 20:55
 */
@JobHandler("hmeAreaThroughRateJob")
public class HmeAreaThroughRateJobHandler implements IJobHandler {

    @Autowired
    private HmeAreaThroughRateDetailsRepository hmeAreaThroughRateDetailsRepository;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 获取任务所属租户
        Long tenantId = tool.getBelongTenantId();
        try {
            this.hmeAreaThroughRateDetailsRepository.createAreaThroughRate(tenantId);
            tool.info("Create task status document run success!!!");
        } catch (Exception ex) {
            tool.error("Create task status document run failed!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }
}
