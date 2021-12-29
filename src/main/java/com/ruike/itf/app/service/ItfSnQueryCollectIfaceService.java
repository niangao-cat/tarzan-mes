package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.api.dto.SnQueryCollectItfDTO;
import com.ruike.itf.api.dto.SnQueryCollectItfReturnDTO;

import java.util.List;

public interface ItfSnQueryCollectIfaceService {

    SnQueryCollectItfReturnDTO invoke(Long tenantId, SnQueryCollectItfDTO snQueryCollectItfDTO);
}
