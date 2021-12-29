package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeCosFunctionMaterialTime;

import java.util.List;

/**
 * COS投料性能时间记录表应用服务
 *
 * @author penglin.sui@hand-china.com 2021-06-23 18:09:37
 */
public interface HmeCosFunctionMaterialTimeService {
    /**
     * 查询本次同步的时间范围
     *
     * @param tenantId 租户ID
     * @return
     */
    HmeCosFunctionMaterialTime selectCurrentTime(Long tenantId);
}
