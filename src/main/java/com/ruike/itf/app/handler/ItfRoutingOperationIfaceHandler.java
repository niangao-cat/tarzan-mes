package com.ruike.itf.app.handler;

import com.ruike.itf.domain.repository.ItfRoutingOperationIfaceRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/23 14:35
 */
@JobHandler("ItfRoutingOperationHandler")
public class ItfRoutingOperationIfaceHandler implements IJobHandler {

    @Autowired
    private ItfRoutingOperationIfaceRepository itfRoutingOperationIfaceRepository;


    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        Long tenantId = tool.getBelongTenantId();

        try {
            List<Long> batchIdList = itfRoutingOperationIfaceRepository.selectBatchId(tenantId);
            if(CollectionUtils.isEmpty(batchIdList)){
                tool.info("ItfRoutingOperationIfaceHandler run success , no batch data!!!");
                return ReturnT.SUCCESS;
            }
            for (Long batchId : batchIdList
            ) {
                itfRoutingOperationIfaceRepository.myRoutingOperationImport(tenantId, batchId);
            }
            tool.info("ItfRoutingOperationIfaceHandler run success!!!");
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            tool.error("ItfRoutingOperationIfaceHandler run failure:" + e.getMessage());
            return ReturnT.FAILURE;
        }
    }
}
