package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfProductionPickingIfaceDTO;

import java.util.List;

/**
 * 生产领料执行数据回传接口 应用服务
 *
 * @author li.zhang13@hand-china.com 2021/08/11 10:45
 */
public interface ItfProductionPickingIfaceService {

    /**
     * 亮灯指令接口
     *
     * @param tenantId 租户ID
     * @param dtoList 传参
     * @author li.zhang13@hand-china.com 2021/08/11 10:45
     */
    void itfProductionPickingIface(Long tenantId, List<ItfProductionPickingIfaceDTO> dtoList);
}
