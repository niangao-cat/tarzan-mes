package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import com.ruike.itf.domain.entity.ItfInternalOrderIface;

import java.util.List;

/**
 * 内部订单接口表应用服务
 *
 * @author kejin.liu01@hand-china.com 2020-08-21 09:29:25
 */
public interface ItfInternalOrderIfaceService {

    /**
     * 内部订单同步接口
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com
     */
    List<ItfInternalOrderIface> invoke(List<ItfSapIfaceDTO> dto);
}
