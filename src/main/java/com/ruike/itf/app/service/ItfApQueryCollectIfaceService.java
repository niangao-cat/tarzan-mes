package com.ruike.itf.app.service;


import com.ruike.itf.api.dto.ApQueryCollectItfReturnDTO;

import java.util.List;

public interface ItfApQueryCollectIfaceService {
    List<ApQueryCollectItfReturnDTO> apQueryInvoke(Long tenantId, List<String> materialLotCodes);
}
