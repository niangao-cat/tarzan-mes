package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO;

import java.util.List;

/**
 * 交货单修改过账接口头表应用服务
 *
 * @author sanfeng.zhang@hand-china.com 2021-07-09 16:58:10
 */
public interface ItfSoDeliveryChanOrPostIfaceService {

    /**
     * 发货通知单修改回传&拣配接口
     *
     * @param tenantId
     * @param itfSoDeliveryChanOrPostList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/7/9
     */
    void soDeliveryChangeOrPostIface(Long tenantId, List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostList);
}
