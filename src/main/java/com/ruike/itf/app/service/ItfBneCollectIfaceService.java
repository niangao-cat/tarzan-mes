package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.BneCollectItfDTO;
import com.ruike.itf.api.dto.DataCollectReturnDTO;

import java.util.List;

/**
 * BNE数据采集接口表应用服务
 *
 * @author wenzhang.yu@hand-china.com 2020-09-12 13:59:43
 */
public interface ItfBneCollectIfaceService {


    List<DataCollectReturnDTO> invoke(Long tenantId, List<BneCollectItfDTO> collectList);
}
