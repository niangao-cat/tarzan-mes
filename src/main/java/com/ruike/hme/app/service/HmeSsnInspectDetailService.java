package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeSsnInspectDetail;

import java.util.List;

/**
 * 标准件检验标准明细应用服务
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:11
 */
public interface HmeSsnInspectDetailService {

    /**
     * 创建工序不良行明细表
     *
     * @param tenantId
     * @param hmeSsnInspectDetail
     * @author li.zhang13@hand-china.com
     * @date 2021-02-01 10:45:11
     */
    void insertDetail(Long tenantId, List<HmeSsnInspectDetail> hmeSsnInspectDetail);

    /**
     * 创建工序不良行明细表
     *
     * @param tenantId
     * @param hmeSsnInspectDetail
     * @author li.zhang13@hand-china.com
     * @date 2021-02-01 10:45:11
     */
    void updateDetail(Long tenantId, List<HmeSsnInspectDetail> hmeSsnInspectDetail);
}
