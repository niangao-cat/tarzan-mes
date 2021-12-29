package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfAfterSalesRepairSyncDTO;
import com.ruike.itf.domain.entity.ItfAfterSalesRepairIfaces;
import com.ruike.itf.domain.entity.ItfWorkCenterIface;

import java.util.List;

/**
 * 售后登记平台表应用服务
 *
 * @author kejin.liu01@hand-china.com 2020-09-02 09:59:41
 */
public interface ItfAfterSalesRepairIfacesService {

    /**
     * 大仓等级平台同步接口
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com
     */
    List<ItfAfterSalesRepairIfaces> afterSalesRepairSync(List<ItfAfterSalesRepairSyncDTO> dto);
}
