package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfFirstProcessIfaceDTO;
import com.ruike.itf.api.dto.ItfFirstProcessIfaceDTO2;
import com.ruike.itf.api.dto.ItfFirstProcessIfaceDTO3;
import com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/15 10:06
 */
public interface ItfFirstProcessIfaceService {

    /**
     * 首序-进站
     *
     * @param tenantId
     * @param ifaceDTO
     * @return com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO
     * @author sanfeng.zhang@hand-china.com 2021/10/15
     */
    ItfFirstProcessIfaceVO inSiteInvoke(Long tenantId, ItfFirstProcessIfaceDTO ifaceDTO);

    /**
     * 首序-投料
     *
     * @param tenantId
     * @param ifaceDTO
     * @return com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO
     * @author sanfeng.zhang@hand-china.com 2021/10/15
     */
    ItfFirstProcessIfaceVO releaseInvoke(Long tenantId, ItfFirstProcessIfaceDTO2 ifaceDTO);

    /**
     * 首序-出站
     *
     * @param tenantId
     * @param ifaceDTO
     * @return com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO
     * @author sanfeng.zhang@hand-china.com 2021/10/15
     */
    ItfFirstProcessIfaceVO outSiteInvoke(Long tenantId, ItfFirstProcessIfaceDTO3 ifaceDTO);
}
