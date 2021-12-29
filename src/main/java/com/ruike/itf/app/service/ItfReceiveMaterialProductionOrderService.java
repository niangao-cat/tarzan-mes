package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfReceiveMaterialProductionOrderDTO;
import com.ruike.itf.api.dto.ItfReceiveMaterialProductionOrderReturnDTO;
import com.ruike.itf.domain.entity.CostcenterDocIface;

import java.util.List;

public interface ItfReceiveMaterialProductionOrderService {

    /**
     * @description:接口API
     * @return:
     * @author: xiaojiang
     * @time: 2021/6/30 14:01
     */
    ItfReceiveMaterialProductionOrderReturnDTO create(List<ItfReceiveMaterialProductionOrderDTO> list);

    /**
     * @description:写入接口表
     * @return:
     * @author: xiaojiang
     * @time: 2021/6/30 14:00
     */
    List<CostcenterDocIface> insertIface(Long tenantId, List<ItfReceiveMaterialProductionOrderDTO> itfReceiveMaterialProductionOrderDTOList, Long batchId);

    /**
     * @description:写入业务表
     * @return:
     * @author: xiaojiang
     * @time: 2021/6/30 17:35
     */
    void updateDataNew(Long tenantId, List<CostcenterDocIface> costcenterDocIfaceList);


}
