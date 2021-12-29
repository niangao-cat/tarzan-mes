package com.ruike.itf.app.handler;

import com.ruike.itf.app.service.ItfDeliveryDocIfaceService;
import com.ruike.itf.domain.vo.ItfPoDeliveryRelHandlerVO;
import com.ruike.itf.infra.mapper.ItfDeliveryDocIfaceMapper;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import java.util.List;
import java.util.Map;

/**
 * 送货单行判退回传接口
 */
@JobHandler("ItfPoDeliveryRelHandler")
public class ItfPoDeliveryRelHandler implements IJobHandler {

    private final ItfDeliveryDocIfaceMapper itfDeliveryDocIfaceMapper;
    private final ItfDeliveryDocIfaceService itfDeliveryDocIfaceService;

    public ItfPoDeliveryRelHandler(ItfDeliveryDocIfaceMapper itfDeliveryDocIfaceMapper, ItfDeliveryDocIfaceService itfDeliveryDocIfaceService) {
        this.itfDeliveryDocIfaceMapper = itfDeliveryDocIfaceMapper;

        this.itfDeliveryDocIfaceService = itfDeliveryDocIfaceService;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        Long tenantId = tool.getCreatTenantId();
        try {
            List<ItfPoDeliveryRelHandlerVO> list = itfDeliveryDocIfaceMapper.selectTHDeliveryDoc(tenantId,null);
            itfDeliveryDocIfaceService.sendTHDeliveryDoc(list);
            tool.info("TH po delivery run success!!!");
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            tool.error(e.getMessage());
            return ReturnT.FAILURE;
        }

    }
}
