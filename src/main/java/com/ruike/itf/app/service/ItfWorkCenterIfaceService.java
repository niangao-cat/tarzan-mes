package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import com.ruike.itf.domain.entity.ItfWorkCenterIface;

import java.util.List;

/**
 * 工作中心接口记录表应用服务
 *
 * @author kejin.liu01@hand-china.com 2020-08-27 16:17:14
 */
public interface ItfWorkCenterIfaceService {

    /**
     * 工作中心同步接口
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com
     */
    List<ItfWorkCenterIface> invoke(List<ItfSapIfaceDTO> dto);
}
