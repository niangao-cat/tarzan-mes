package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfModLocatorIfaceSyncDTO;
import com.ruike.itf.domain.entity.ItfModLocatorIface;
import com.ruike.itf.domain.entity.ItfSubinventoryIface;

import java.util.List;

/**
 * 仓库接口记录表应用服务
 *
 * @author kejin.liu01@hand-china.com 2020-09-07 15:05:17
 */
public interface ItfModLocatorIfaceService {

    /**
     * 仓库同步接口
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com
     */
    List<ItfModLocatorIfaceSyncDTO> invoke(List<ItfModLocatorIfaceSyncDTO> dto);
}
