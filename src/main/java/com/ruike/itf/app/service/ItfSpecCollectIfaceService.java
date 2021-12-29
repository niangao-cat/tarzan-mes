package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.api.dto.SpecCollectItfDTO;

import java.util.List;

/**
 * 光谱仪数据采集接口表应用服务
 *
 * @author yonghui.zhu@hand-china.com 2020-07-13 18:36:00
 */
public interface ItfSpecCollectIfaceService {

    /**
     * 收集接口调用程序
     *
     * @param tenantId    租户ID
     * @param collectList 收集数据
     * @return java.util.List<com.ruike.itf.api.dto.SpecCollectItfDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com
     * @date 2020/7/13 6:52 下午
     */
    List<DataCollectReturnDTO> invoke(Long tenantId, List<SpecCollectItfDTO> collectList);

}
