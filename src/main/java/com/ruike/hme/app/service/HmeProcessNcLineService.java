package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeProcessNcLine;

/**
 * 工序不良行表应用服务
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
public interface HmeProcessNcLineService {

    /**
     * 创建工序不良行表
     *
     * @param tenantId
     * @param hmeProcessNcLine
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    void createLine(Long tenantId, HmeProcessNcLine hmeProcessNcLine);

    /**
     * 更新工序不良行表
     *
     * @param tenantId
     * @param hmeProcessNcLine
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    void updateLine(Long tenantId, HmeProcessNcLine hmeProcessNcLine);
}
