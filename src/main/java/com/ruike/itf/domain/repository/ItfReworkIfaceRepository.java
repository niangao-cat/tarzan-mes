package com.ruike.itf.domain.repository;

import com.ruike.itf.api.dto.ItfReworkIfaceDTO;
import com.ruike.itf.api.dto.ItfReworkIfaceDTO2;
import com.ruike.itf.api.dto.ItfReworkIfaceDTO3;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/26 10:37
 */
public interface ItfReworkIfaceRepository {

    /**
     * 返修进站校验
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/10/26
     */
    void reworkInSiteVerify(Long tenantId, ItfReworkIfaceDTO dto);

    /**
     * 返修出站校验
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/10/26
     */
    void reworkSiteOutVerify(Long tenantId, ItfReworkIfaceDTO3 dto);

    /**
     * 投料校验
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/10/27
     */
    void reworkReleaseVerify(Long tenantId, ItfReworkIfaceDTO2 dto);
}
