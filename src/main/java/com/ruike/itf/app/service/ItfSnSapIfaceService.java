package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfSnSapIfaceDTO;

import java.util.List;

/**
 * 物料组接口表应用服务
 *
 * @author kejin.liu01@hand-china.com 2020-09-04 11:31:56
 */
public interface ItfSnSapIfaceService {

    /**
     * 成品SN发货回传接口
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com
     */
    List<ItfSnSapIfaceDTO> updateMaterialStatus(List<ItfSnSapIfaceDTO> dto);
}
