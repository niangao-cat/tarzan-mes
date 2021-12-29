package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import com.ruike.itf.domain.entity.ItfCostcenterIface;

import java.util.List;

/**
 * 成本中心数据接口记录表应用服务
 *
 * @author kejin.liu01@hand-china.com 2020-08-24 09:19:52
 */
public interface ItfCostcenterIfaceService {

    /**
     * 成本中心同步接口
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com
     */
    List<ItfCostcenterIface> invoke(List<ItfSapIfaceDTO> dto);
}
