package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.api.dto.DtpCollectItfDTO;

import java.util.List;

/**
 * 器件测试台数据采集接口表应用服务
 *
 * @author wenzhang.yu@hand-china.com 2020-08-25 19:00:41
 */
public interface ItfDtpCollectIfaceService {


    List<DataCollectReturnDTO> invoke(Long tenantId, List<DtpCollectItfDTO> collectList);
}
