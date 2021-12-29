package com.ruike.itf.app.handler;


import com.ruike.itf.api.dto.ItfSrmMaterialWasteIfaceSyncDTO;
import com.ruike.itf.app.service.ItfSrmMaterialWasteIfaceService;
import com.ruike.itf.domain.repository.ItfSrmInstructionIfaceRepository;
import com.ruike.itf.domain.repository.ItfSrmMaterialWasteIfaceRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * SRM料废调换接口
 *
 * @author kejin.liu01@hand-china.com 2020/9/8 14:56
 */
@JobHandler("ItfSrmMaterialWasteHandler")
public class ItfSrmMaterialWasteHandler implements IJobHandler {

    @Autowired
    private ItfSrmMaterialWasteIfaceRepository itfSrmMaterialWasteIfaceRepository;

    @Autowired
    private ItfSrmMaterialWasteIfaceService itfSrmMaterialWasteIfaceService;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        Long tenantId = tool.getBelongTenantId();
        // 查询SRM料废调换接口
        List<ItfSrmMaterialWasteIfaceSyncDTO> syncDTOS = itfSrmMaterialWasteIfaceRepository.selectSrmMaterialWaste(tenantId);
        if (CollectionUtils.isNotEmpty(syncDTOS)) {
            itfSrmMaterialWasteIfaceService.srmMaterialWasteExchangeCreate(syncDTOS, tenantId);
        }

        return ReturnT.SUCCESS;
    }
}
