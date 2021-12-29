package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.api.dto.FacCollectItfDTO;

import java.util.List;

/**
 * FAC数据采集接口表应用服务
 *
 * @author yonghui.zhu@hand-china.com 2020-08-04 19:51:39
 */
public interface ItfFacCollectIfaceService {
    /**
     * 收集接口调用程序
     *
     * @param tenantId    租户ID
     * @param collectList 收集数据
     * @return java.util.List<com.ruike.itf.api.dto.SpecCollectItfDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com
     * @date 2020/7/13 6:52 下午
     */
    List<DataCollectReturnDTO> invoke(Long tenantId, List<FacCollectItfDTO> collectList);
}
