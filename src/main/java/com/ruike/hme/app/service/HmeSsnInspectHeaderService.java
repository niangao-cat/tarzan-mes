package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeSsnInspectHeader;

/**
 * 标准件检验标准头应用服务
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:11
 */
public interface HmeSsnInspectHeaderService {

    /**
     * 创建标准件检验标准头头表
     *
     * @param tenantId
     * @param hmeSsnInspectHeader
     * @author li.zhang13@hand-china.com
     * @date 2021-02-01 10:45:11
     */
    void createHeader(Long tenantId, HmeSsnInspectHeader hmeSsnInspectHeader);

    /**
     * 更新标准件检验标准头头表
     *
     * @param tenantId
     * @param hmeSsnInspectHeader
     * @author li.zhang13@hand-china.com
     * @date 2021-02-01 10:45:11
     */
    void updateHeader(Long tenantId, HmeSsnInspectHeader hmeSsnInspectHeader);
}
