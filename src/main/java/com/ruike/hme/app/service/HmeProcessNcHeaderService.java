package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeProcessNcHeader;

/**
 * 工序不良头表应用服务
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
public interface HmeProcessNcHeaderService {

    /**
     * 创建工序不良头表
     *
     * @param tenantId
     * @param hmeProcessNcHeader
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    void createHeader(Long tenantId, HmeProcessNcHeader hmeProcessNcHeader);

    /**
     * 更新工序不良头表
     *
     * @param tenantId
     * @param hmeProcessNcHeader
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    void updateHeader(Long tenantId, HmeProcessNcHeader hmeProcessNcHeader);
}
