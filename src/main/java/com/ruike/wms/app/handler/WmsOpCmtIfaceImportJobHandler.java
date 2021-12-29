package com.ruike.wms.app.handler;

import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.iface.domain.repository.MtOperationComponentIfaceRepository;
import tarzan.iface.domain.repository.MtWorkOrderIfaceRepository;

import java.util.Map;


/**
 * 工序组件关系导入JOB
 *
 * @author jiangling.zheng@hand-china.com 2020-06-23 11:43
 */
@JobHandler("opCmtIfaceImportJob")
public class WmsOpCmtIfaceImportJobHandler implements IJobHandler {

    @Autowired
    private MtOperationComponentIfaceRepository repository;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 获取任务所属租户
        Long tenantId = tool.getBelongTenantId();
        try {
            this.repository.operationComponentInterfaceImport(tenantId);
            tool.info("Operation component import run success!!!");
        } catch (Exception ex) {
            tool.error("Operation component import run failed!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }
}
