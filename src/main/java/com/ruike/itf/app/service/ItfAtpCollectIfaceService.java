package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.AtpCollectItfDTO;
import com.ruike.itf.api.dto.DataCollectReturnDTO;

import java.util.List;

/**
 * 自动化测试接口表应用服务
 *
 * @author wenzhang.yu@hand-china 2021-01-06 11:37:08
 */
public interface ItfAtpCollectIfaceService {

    List<DataCollectReturnDTO> invoke(Long tenantId, List<AtpCollectItfDTO> collectList);

}
