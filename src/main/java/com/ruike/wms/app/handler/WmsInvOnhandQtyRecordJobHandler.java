package com.ruike.wms.app.handler;

import com.ruike.wms.domain.repository.WmsInvOnhandQtyRecordRepository;
import com.ruike.wms.infra.constant.WmsConstant;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-11-18 20:22
 */
@JobHandler("invOnhandQtyRecordJob")
public class WmsInvOnhandQtyRecordJobHandler implements IJobHandler {

    @Autowired
    private WmsInvOnhandQtyRecordRepository repository;
    @Autowired
    private ProfileClient profileClient;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 获取任务所属租户
        Long tenantId = tool.getBelongTenantId();
        String syncType = Optional.ofNullable(profileClient.getProfileValueByOptions(WmsConstant.Profile.WMS_ONHAND_IVN_RECORD))
                .orElse(WmsConstant.SyncType.INIT);
        try {
            this.repository.invOnhandQtyRecordSync(tenantId, syncType);
            tool.info("Inv Onhand Qty Record import run success!!!");
        } catch (Exception ex) {
            tool.error("Inv Onhand Qty Record import run failed!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }
}
