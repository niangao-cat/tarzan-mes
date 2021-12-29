package com.ruike.hme.app.service;

import java.util.List;

/**
 * 月度计划表应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-07-16 14:36:03
 */
public interface HmeCompleteRateService {

    /**
     * 月度计划达成率报表job
     *
     * @param tenantId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/16 02:48:13
     * @return void
     */
    void monthlyPlanQuery(Long tenantId);
}
