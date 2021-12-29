package com.ruike.hme.app.service;

/**
 * COS投料性能表应用服务
 *
 * @author penglin.sui@hand-china.com 2021-06-22 20:50:13
 */
public interface HmeCosFunctionMaterialService {

    /**
     * 新增COS投料性能表
     *
     * @param tenantId 租户ID
     * @return
     */
    void createCosFunctionMaterial(Long tenantId);
}
