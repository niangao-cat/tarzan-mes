package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.api.dto.InSIteDTO;

public interface ItfInSiteCollectIfaceService {

    DataCollectReturnDTO invoke(Long tenantId, InSIteDTO inSIteDTO);
}
