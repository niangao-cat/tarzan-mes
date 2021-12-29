package com.ruike.itf.app.handler;

import com.ruike.hme.domain.entity.HmeRepairWorkOrderCreate;
import com.ruike.hme.domain.repository.HmeRepairWorkOrderCreateRepository;
import com.ruike.hme.infra.mapper.HmeRepairWorkOrderCreateMapper;
import com.ruike.itf.app.service.ItfRepairWorkOrderCreateService;
import com.ruike.itf.domain.entity.ItfObjectTransactionIface;
import com.ruike.wms.domain.entity.WmsObjectTransaction;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@JobHandler("ItfWorkOrderCreateHandler")
public class ItfRepairWorkOrderCreateHandler implements IJobHandler {

    private final ItfRepairWorkOrderCreateService itfRepairWorkOrderCreateService;

    private final HmeRepairWorkOrderCreateRepository hmeRepairWorkOrderCreateRepository;

    public ItfRepairWorkOrderCreateHandler(ItfRepairWorkOrderCreateService itfRepairWorkOrderCreateService, HmeRepairWorkOrderCreateRepository hmeRepairWorkOrderCreateRepository) {
        this.itfRepairWorkOrderCreateService = itfRepairWorkOrderCreateService;
        this.hmeRepairWorkOrderCreateRepository = hmeRepairWorkOrderCreateRepository;
    }


    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        HmeRepairWorkOrderCreate workOrderCreate = new HmeRepairWorkOrderCreate();
        workOrderCreate.setStatus("N");
        List<HmeRepairWorkOrderCreate> select = hmeRepairWorkOrderCreateRepository.select(workOrderCreate);
        try {
            itfRepairWorkOrderCreateService.hmeRepairWorkOrderCreateService(tool.getBelongTenantId(), select.get(0));
            tool.info("itf repair work order create run success!!!");
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            tool.error("itf repair work order create run failed!!!" + e.getMessage());
            tool.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            return ReturnT.FAILURE;
        }
    }
}
