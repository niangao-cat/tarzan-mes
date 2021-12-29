package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.api.dto.ZzqCollectItfDTO;

import java.util.List;

/**
 * 准直器耦合接口表应用服务
 *
 * @author wenzhang.yu@hand-china 2020-12-16 13:43:44
 */
public interface ItfZzqCollectIfaceService {

    List<DataCollectReturnDTO> invoke(Long tenantId, List<ZzqCollectItfDTO> collectList);
}
