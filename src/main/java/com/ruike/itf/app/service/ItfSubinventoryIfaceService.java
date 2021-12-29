package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import com.ruike.itf.domain.entity.ItfSubinventoryIface;

import java.util.List;

/**
 * ERP子库存接口表应用服务
 *
 * @author kejin.liu01@hand-china.com 2020-08-18 10:52:45
 */
public interface ItfSubinventoryIfaceService {

    /**
     * 库存地点同步接口
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com
     */
    List<ItfSubinventoryIface> invoke(List<ItfSapIfaceDTO> dto);
}
