package com.ruike.hme.app.handler;

import com.ruike.hme.domain.repository.HmeCosFunctionSelectionRepository;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 筛选芯片性能数据抽取定时任务
 *
 * @author: chaonan.hu@hand-china.com 2021-08-19 15:18:03
 **/
@JobHandler("hmeCosFunctionSelectionJob")
public class HmeCosFunctionSelectionJobHandler implements IJobHandler {

    @Autowired
    private HmeCosFunctionSelectionRepository hmeCosFunctionSelectionRepository;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 获取任务所属租户
        Long tenantId = tool.getBelongTenantId();
        try {
            this.hmeCosFunctionSelectionRepository.preSelectionFunctionDataJob(tenantId);
            tool.info("Create task status document run success!!!");
        } catch (Exception ex) {
            tool.error("Create task status document run failed!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }
}
