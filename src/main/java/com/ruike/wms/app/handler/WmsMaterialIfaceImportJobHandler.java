package com.ruike.wms.app.handler;

import com.ruike.itf.api.dto.RfcParamDTO;
import com.ruike.itf.app.service.ISapRfcService;
import com.ruike.itf.domain.repository.ItfInvItemIfaceRepository;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.iface.domain.repository.MtInvItemIfaceRepository;

import java.util.Map;


/**
 * 物料导入JOB
 *
 * @author jiangling.zheng@hand-china.com 2020-06-23 11:43
 */
@JobHandler("materialIfaceImportJob")
public class WmsMaterialIfaceImportJobHandler implements IJobHandler {

    @Autowired
    private MtInvItemIfaceRepository repository;

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
            RfcParamDTO dto = itfInvItemIfaceRepository.getSapParams(tenantId, map);
            this.iSapRfcService.materialRfc(dto);
            // 调用API将数据同步到业务表
            this.repository.materialInterfaceImport(tenantId);
            tool.info("Material import run success!!!");
        } catch (Exception ex) {
            tool.error("Material import run failed!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }
}
