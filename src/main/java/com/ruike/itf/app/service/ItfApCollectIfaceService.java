package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ApCollectItfDTO;
import com.ruike.itf.api.dto.DataCollectReturnDTO;

import java.util.List;

/**
 * 老化台数据采集接口表应用服务
 *
 * @author wenzhang.yu@hand-china.com 2020-08-25 19:00:42
 */
public interface ItfApCollectIfaceService {


    List<DataCollectReturnDTO> invoke(Long tenantId, List<ApCollectItfDTO> collectList);
}
