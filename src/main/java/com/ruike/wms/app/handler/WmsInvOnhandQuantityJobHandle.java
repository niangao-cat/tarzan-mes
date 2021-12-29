package com.ruike.wms.app.handler;

import java.util.Arrays;
import java.util.Map;

import com.ruike.wms.infra.mapper.WmsInvOnhandQuantityMapper;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 库存快照JOB
 *
 * @author penglin.sui@hand-china.com 2020-10-8 19:52
 */
@JobHandler("invOnhandQuantityJob")
public class WmsInvOnhandQuantityJobHandle implements IJobHandler{

    @Autowired
    private WmsInvOnhandQuantityMapper wmsInvOnhandQuantityMapper;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        try {
            //删除七天前的库存快照数据
            //wmsInvOnhandQuantityMapper.deleteInvOnhandQuantityShot();
            //新增库存快照数据
            wmsInvOnhandQuantityMapper.batchInsertInvOnhandQuantityShot();
            tool.info("onHandShot copy table data run success!!!");
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            tool.error("onHandShot copy table data run failed!!!" + e.getMessage());
            tool.error(Arrays.toString(e.getStackTrace()));
            return ReturnT.FAILURE;
        }
    }
}
