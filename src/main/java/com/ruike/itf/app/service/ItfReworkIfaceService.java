package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfReworkIfaceDTO;
import com.ruike.itf.api.dto.ItfReworkIfaceDTO2;
import com.ruike.itf.api.dto.ItfReworkIfaceDTO3;
import com.ruike.itf.domain.vo.ItfReworkIfaceVO;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/26 10:36
 */
public interface ItfReworkIfaceService {

    /**
     * 返修进站
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.itf.domain.vo.ItfReworkIfaceVO
     * @author sanfeng.zhang@hand-china.com 2021/10/26
     */
    ItfReworkIfaceVO inSiteInvoke(Long tenantId, ItfReworkIfaceDTO dto);

    /**
     * 返修/单件公用进站
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.itf.domain.vo.ItfReworkIfaceVO
     * @author sanfeng.zhang@hand-china.com 2021/10/26
     */
    ItfReworkIfaceVO commonInSiteInvoke(Long tenantId, ItfReworkIfaceDTO dto);

    /**
     * 返修投料
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.itf.domain.vo.ItfReworkIfaceVO
     * @author sanfeng.zhang@hand-china.com 2021/10/26
     */
    ItfReworkIfaceVO releaseInvoke(Long tenantId, ItfReworkIfaceDTO2 dto);

    /**
     * 返修出站
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.itf.domain.vo.ItfReworkIfaceVO
     * @author sanfeng.zhang@hand-china.com 2021/10/26
     */
    ItfReworkIfaceVO outSiteInvoke(Long tenantId, ItfReworkIfaceDTO3 dto);
}
