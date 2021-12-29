package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeSsnInspectLine;

import java.util.List;

/**
 * 标准件检验标准行应用服务
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:10
 */
public interface HmeSsnInspectLineService {

    /**
     * 创建标准件检验标准行表
     *
     * @param tenantId
     * @param hmeSsnInspectLine
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    void createLine(Long tenantId, List<HmeSsnInspectLine> hmeSsnInspectLine);

    /**
     * 更新标准件检验标准行表
     *
     * @param tenantId
     * @param hmeSsnInspectLine
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    void updateLine(Long tenantId, List<HmeSsnInspectLine> hmeSsnInspectLine);
}
