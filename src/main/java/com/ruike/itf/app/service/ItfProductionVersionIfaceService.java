package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import com.ruike.itf.domain.entity.ItfProductionVersionIface;

import java.util.List;

/**
 * 生产版本表应用服务
 *
 * @author kejin.liu01@hand-china.com 2020-08-20 12:21:46
 */
public interface ItfProductionVersionIfaceService {

    /**
     * 生产版本同步接口
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com
     */
    List<ItfProductionVersionIface> invoke(List<ItfSapIfaceDTO> dto);
}
