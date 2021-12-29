package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfTimeProcessIfaceDTO;
import com.ruike.itf.api.dto.ItfTimeProcessIfaceDTO2;
import com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/28 16:25
 */
public interface ItfTimeProcessIfaceService {

    /**
     * 时效进站
     *
     * @param tenantId
     * @param request
     * @return com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO
     * @author sanfeng.zhang@hand-china.com 2021/10/28
     */
    ItfProcessReturnIfaceVO inSiteInvoke(Long tenantId, ItfTimeProcessIfaceDTO request);

    /**
     * 时效/时效返修-进站
     *
     * @param tenantId
     * @param request
     * @return com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO
     * @author sanfeng.zhang@hand-china.com 2021/11/1
     */
    ItfProcessReturnIfaceVO commonInSiteInvoke(Long tenantId, ItfTimeProcessIfaceDTO request);

    /**
     * 时效出战
     *
     * @param tenantId
     * @param request
     * @return com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO
     * @author sanfeng.zhang@hand-china.com 2021/10/28
     */
    ItfProcessReturnIfaceVO outSiteInvoke(Long tenantId, ItfTimeProcessIfaceDTO2 request);

}
