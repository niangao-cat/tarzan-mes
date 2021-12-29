package com.ruike.wms.app.service;

import com.ruike.wms.domain.vo.WmsEventVO;

/**
 * 事件服务
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/9 11:48
 */
public interface WmsEventService {

    /**
     * 仅创建事件
     *
     * @param tenantId  租户
     * @param eventType 事件类型
     * @return com.ruike.wms.domain.vo.WmsEventVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/9 11:51:59
     */
    WmsEventVO createEventOnly(Long tenantId, String eventType);

    /**
     * 创建事件请求
     *
     * @param tenantId  租户
     * @param eventType 事件类型
     * @return com.ruike.wms.domain.vo.WmsEventVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/9 11:51:59
     */
    WmsEventVO createEventRequest(Long tenantId, String eventType);

    /**
     * 创建事件以及事件请求
     *
     * @param tenantId  租户
     * @param eventType 事件类型
     * @return com.ruike.wms.domain.vo.WmsEventVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/9 11:52:03
     */
    WmsEventVO createEventWithRequest(Long tenantId, String eventType);

    /**
     * 使用事件请求创建事件
     *
     * @param tenantId       租户
     * @param eventType      事件类型
     * @param eventRequestId 事件请求ID
     * @return com.ruike.wms.domain.vo.WmsEventVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/9 11:52:03
     */
    WmsEventVO createEventByRequestId(Long tenantId, String eventType, String eventRequestId);

    /**
     * 创建带父请求ID的事件
     *
     * @param tenantId       租户
     * @param eventType      事件类型
     * @param parentEventId  父事件ID
     * @param eventRequestId 事件请求ID
     * @return com.ruike.wms.domain.vo.WmsEventVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/9 11:51:59
     */
    WmsEventVO createEventRequestWithParent(Long tenantId, String parentEventId, String eventType, String eventRequestId);
}
