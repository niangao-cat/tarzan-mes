package com.ruike.itf.app.handler;

import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.domain.entity.ItfObjectTransactionIface;
import com.ruike.wms.domain.entity.WmsObjectTransaction;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@JobHandler("ItfProductionStatementHandler")
public class ItfProductionStatementHandler implements IJobHandler {

    private final ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;

    @Autowired
    public ItfProductionStatementHandler(ItfObjectTransactionIfaceService itfObjectTransactionIfaceService) {
        this.itfObjectTransactionIfaceService = itfObjectTransactionIfaceService;
    }
    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        try {
            //V20211012 modify by penglin.sui for hui.ma 报工单独汇总报工数据
            itfObjectTransactionIfaceService.processSummary(tool.getBelongTenantId(), "N,HME_WORK_REPORT", new ArrayList<>());

            itfObjectTransactionIfaceService.productionStatementInvoke(tool.getBelongTenantId(),"N", new ArrayList<>());
            tool.info("object transaction production Statement update run success!!!");
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            tool.error("object transaction production Statement update run failed!!!" + e.getMessage());
            tool.error(Arrays.toString(e.getStackTrace()));
            return ReturnT.FAILURE;
        }
    }
}
