package com.ruike.hme.app.service;

/**
 * COS良率计算定时任务应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-09-17 11:35:12
 */
public interface HmeCosYieldComputeService {

    /**
     * COS良率计算定时任务
     *
     * @param tenantId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/17 02:29:52
     * @return void
     */
    void cosYieldComputeJob(Long tenantId);
}
