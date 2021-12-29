package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfLightTaskIfaceDTO;
import com.ruike.itf.domain.vo.ItfLightTaskIfaceVO;

import java.util.List;

/**
 * 亮灯指令接口表应用服务
 *
 * @author li.zhang13@hand-china.com 2021-08-09 11:12:14
 */
public interface ItfLightTaskIfaceService {

    /**
     * 亮灯指令接口
     *
     * @param tenantId 租户ID
     * @param dtoList 传参 条码号或容器号
     * @author li.zhang13@hand-china.com 2021-08-09 11:12:14
     * @return java.util.List<com.ruike.itf.domain.vo.ItfLightTaskIfaceVO>
     */
    List<ItfLightTaskIfaceVO> itfLightTaskIface(Long tenantId, List<ItfLightTaskIfaceDTO> dtoList);
}
