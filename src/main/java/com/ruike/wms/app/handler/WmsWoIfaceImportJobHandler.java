package com.ruike.wms.app.handler;

import com.ruike.itf.api.dto.RfcParamDTO;
import com.ruike.itf.app.service.ISapRfcService;
import com.ruike.itf.domain.repository.ItfInvItemIfaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.iface.domain.repository.MtBomComponentIfaceRepository;
import tarzan.iface.domain.repository.MtOperationComponentIfaceRepository;
import tarzan.iface.domain.repository.MtRoutingOperationIfaceRepository;
import tarzan.iface.domain.repository.MtWorkOrderIfaceRepository;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 工单导入JOB
 *
 * @author jiangling.zheng@hand-china.com 2020-06-23 11:43
 */
@JobHandler("woIfaceImportJob")
@Slf4j
public class WmsWoIfaceImportJobHandler implements IJobHandler {

    @Autowired
    private MtWorkOrderIfaceRepository mtWorkOrderIfaceRepository;

    @Autowired
    private ISapRfcService iSapRfcService;

    @Autowired
    private ItfInvItemIfaceRepository itfInvItemIfaceRepository;
    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtBomComponentIfaceRepository bomComponentIfaceRepository;

    @Autowired
    private MtRoutingOperationIfaceRepository mtRoutingOperationIfaceRepository;

    @Autowired
    private MtOperationComponentIfaceRepository mtOperationComponentIfaceRepository;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        Long tenantId = tool.getBelongTenantId();
        log.info("JOB.tenantId：============================================================>{}", tenantId);
        try {
            // 同步sap数据到接口表
            RfcParamDTO dto = itfInvItemIfaceRepository.getSapParams(tenantId, map);
            this.iSapRfcService.workOrderRfc(dto);
            // 调用API将数据同步到业务表
            this.mtWorkOrderIfaceRepository.workOrderInterfaceImport(tenantId);
            tool.info("Work order import run success!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
            tool.error("Work order import run failed!!!" + ex.getMessage());
        }

        try {
            this.bomComponentIfaceRepository.bomInterfaceImport(tenantId);
            tool.info("Bom import run success!!!");
        } catch (Exception ex) {
            tool.error("Bom import run failed!!!" + ex.getMessage());
        }

        try {
            this.mtRoutingOperationIfaceRepository.routerInterfaceImport(tenantId);
            tool.info("Router import run success!!!");
        } catch (Exception ex) {
            tool.error("Router import run failed!!!" + ex.getMessage());
        }


        try {
            this.mtOperationComponentIfaceRepository.operationComponentInterfaceImport(tenantId);
            tool.info("Operation component import run success!!!");
        } catch (Exception ex) {
            tool.error("Operation component import run failed!!!" + ex.getMessage());
        }

        return ReturnT.SUCCESS;
    }

}

