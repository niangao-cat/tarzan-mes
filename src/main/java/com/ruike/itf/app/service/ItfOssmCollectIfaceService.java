package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.api.dto.FsmCollectItfDTO;
import com.ruike.itf.api.dto.OssmCollectItfDTO;

import java.util.List;

/**
 * 示波器数据采集接口表应用服务
 *
 * @author yonghui.zhu@hand-china.com 2020-07-17 07:46:07
 */
public interface ItfOssmCollectIfaceService {

    /**
     * 收集接口调用程序
     *
     * @param tenantId    租户ID
     * @param collectList 收集数据
     * @return java.util.List<com.ruike.itf.api.dto.OssmCollectItfDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com
     * @date 2020/7/17 6:52 下午
     */
    List<DataCollectReturnDTO> invoke(Long tenantId, List<OssmCollectItfDTO> collectList);
}
