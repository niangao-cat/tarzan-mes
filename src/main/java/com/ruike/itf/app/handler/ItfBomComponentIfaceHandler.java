package com.ruike.itf.app.handler;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.iface.domain.repository.MtBomComponentIfaceRepository;

import java.util.List;
import java.util.Map;

@JobHandler("ItfBomComponentIfaceHandler")
public class ItfBomComponentIfaceHandler implements IJobHandler {

    @Autowired
    private MtBomComponentIfaceRepository mtBomComponentIfaceRepository;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        Long tenantId = tool.getBelongTenantId();

        try {
            List<Long> batchIdList = mtBomComponentIfaceRepository.selectBatchId(tenantId);
            if(CollectionUtils.isEmpty(batchIdList)){
                tool.info("ItfBomComponentIfaceHandler run success , no batch data!!!");
                return ReturnT.SUCCESS;
            }
            for (Long batchId : batchIdList
                 ) {
                mtBomComponentIfaceRepository.myBomInterfaceImport(tenantId, batchId);
            }
            tool.info("ItfBomComponentIfaceHandler run success!!!");
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            tool.error("ItfBomComponentIfaceHandler run failure:" + e.getMessage());
            return ReturnT.FAILURE;
        }
    }
}
