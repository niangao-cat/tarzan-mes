package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.api.dto.LbpCollectItfDTO;

import java.util.List;

/**
 * lbp数据采集接口应用服务
 *
 * @author wenzhang.yu@hand-china.com 2020-09-04 16:35:53
 */
public interface ItfLbpCollectIfaceService {


    List<DataCollectReturnDTO> invoke(Long tenantId, List<LbpCollectItfDTO> collectList);
}
