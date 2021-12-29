package com.ruike.wms.app.handler;

import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.iface.domain.repository.MtBomComponentIfaceRepository;

import java.util.Map;


/**
 * BOM导入JOB
 *
 * @author jiangling.zheng@hand-china.com 2020-06-23 11:43
 */
@JobHandler("bomIfaceImportJob")
public class WmsBomIfaceImportJobHandler implements IJobHandler {

    @Autowired
    private MtBomComponentIfaceRepository repository;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 获取任务所属租户
        Long tenantId = tool.getBelongTenantId();
        try {
            this.repository.bomInterfaceImport(tenantId);
            tool.info("Bom import run success!!!");
        } catch (Exception ex) {
            tool.error("Bom import run failed!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }
}
