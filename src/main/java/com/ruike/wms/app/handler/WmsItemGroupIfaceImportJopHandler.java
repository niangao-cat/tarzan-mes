package com.ruike.wms.app.handler;


import com.ruike.itf.app.service.ISapRfcService;
import com.ruike.itf.domain.repository.ItfInvItemIfaceRepository;
import com.ruike.itf.domain.repository.ItfItemGroupIfaceRepository;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


/**
 * 工单导入JOB
 *
 * @author jiangling.zheng@hand-china.com 2020-07-17 17:59
 */
@JobHandler("itemGroupIfaceImportJob")
public class WmsItemGroupIfaceImportJopHandler implements IJobHandler {

    @Autowired
    private ItfItemGroupIfaceRepository repository;

    @Autowired
    private ISapRfcService iSapRfcService;

    @Autowired
    private ItfInvItemIfaceRepository itfInvItemIfaceRepository;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 获取任务所属租户
        Long tenantId = tool.getBelongTenantId();
        try {
            // 同步sap数据到接口表
            this.iSapRfcService.itemGroupRfc();
            // 调用API将数据同步到业务表
            this.repository.itemGroupIfaceImport(tenantId);
            tool.info("Item group import run success!!!");
        } catch (Exception ex) {
            tool.error("Item group import run failed!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }


}
