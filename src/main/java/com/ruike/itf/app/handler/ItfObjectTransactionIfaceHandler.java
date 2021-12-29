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

/**
 * 物料移动定时JOB
 *
 * @author kejin.liu01@hand-china.com 2020/8/13 16:30
 */
@JobHandler("ItfObjectTransactionIfaceJob")
public class ItfObjectTransactionIfaceHandler implements IJobHandler {


    private final ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;

    @Autowired
    public ItfObjectTransactionIfaceHandler(ItfObjectTransactionIfaceService itfObjectTransactionIfaceService) {
        this.itfObjectTransactionIfaceService = itfObjectTransactionIfaceService;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        try {
            List<WmsObjectTransaction> detailList = new ArrayList<>();
            List<ItfObjectTransactionIface> itfObjectTransactionIfaces = itfObjectTransactionIfaceService.processSummary(tool.getBelongTenantId(), "N,EXCLUDE_HME_WORK_REPORT", detailList);
            itfObjectTransactionIfaceService.materialMove(tool.getBelongTenantId(), "N", itfObjectTransactionIfaces);
            tool.info("object transaction process summary update run success!!!");
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            tool.error("object transaction process summary update run failed!!!" + e.getMessage());
            tool.error(Arrays.toString(e.getStackTrace()));
            return ReturnT.FAILURE;
        }


    }
}
