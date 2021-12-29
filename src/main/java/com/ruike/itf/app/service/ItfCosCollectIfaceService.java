package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.CosCollectItfDTO;
import com.ruike.itf.api.dto.DataCollectReturnDTO;

import java.util.List;

/**
 * cos测试数据采集接口表应用服务
 *
 * @author wenzhang.yu@hand-china.com 2020-08-28 11:18:23
 */
public interface ItfCosCollectIfaceService {

    List<DataCollectReturnDTO> invoke(Long tenantId, List<CosCollectItfDTO> collectList);
}
