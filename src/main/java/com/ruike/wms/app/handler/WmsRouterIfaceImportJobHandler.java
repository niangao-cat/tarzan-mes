package com.ruike.wms.app.handler;

import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.iface.domain.repository.MtRoutingOperationIfaceRepository;

import java.util.Map;


/**
 * 工艺路线JOB
 *
 * @author jiangling.zheng@hand-china.com 2020-06-23 11:43
 */
@JobHandler("routerIfaceImportJob")
public class WmsRouterIfaceImportJobHandler implements IJobHandler {

    @Autowired
    private MtRoutingOperationIfaceRepository repository;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 获取任务所属租户
        Long tenantId = tool.getBelongTenantId();
        try {
            this.repository.routerInterfaceImport(tenantId);
            tool.info("Router import run success!!!");
        } catch (Exception ex) {
            tool.error("Router import run failed!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }
}
