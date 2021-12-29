package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfReworkTimeIfaceDTO;
import com.ruike.itf.api.dto.ItfReworkTimeIfaceDTO2;
import com.ruike.itf.api.dto.ItfTimeProcessIfaceDTO;
import com.ruike.itf.api.dto.ItfTimeProcessIfaceDTO2;
import com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/29 16:04
 */
public interface ItfReworkTimeIfaceService {

    /**
     * 时效进站
     *
     * @param tenantId
     * @param request
     * @return com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO
     * @author sanfeng.zhang@hand-china.com 2021/10/28
     */
    ItfProcessReturnIfaceVO inSiteInvoke(Long tenantId, ItfReworkTimeIfaceDTO request);

    /**
     * 时效出战
     *
     * @param tenantId
     * @param request
     * @return com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO
     * @author sanfeng.zhang@hand-china.com 2021/10/28
     */
    ItfProcessReturnIfaceVO outSiteInvoke(Long tenantId, ItfReworkTimeIfaceDTO2 request);
}
