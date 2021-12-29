package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfReceiveMaterialProductionOrderDTO;
import com.ruike.itf.api.dto.ItfReceiveMaterialProductionOrderReturnDTO;
import com.ruike.itf.api.dto.ItfSendOutReturnNoticeDTO;
import com.ruike.itf.domain.entity.CostcenterDocIface;
import com.ruike.itf.domain.entity.SoDeliveryIface;

import java.util.List;

public interface ItfSendOutReturnNoticeService {
    ItfReceiveMaterialProductionOrderReturnDTO create(List<ItfSendOutReturnNoticeDTO> list);

    /**
     * @description:写入接口表
     * @return:
     * @author: xiaojiang
     * @time: 2021/6/30 14:00
     */
    List<SoDeliveryIface> insertIface(Long tenantId, List<ItfSendOutReturnNoticeDTO> itfSendOutReturnNoticeDTOS, Long batchId);

    /**
     * @description:写入业务表
     * @return:
     * @author: xiaojiang
     * @time: 2021/6/30 17:35
     */
    void updateDataNew(Long tenantId, List<SoDeliveryIface> costcenterDocIfaceList);

}
