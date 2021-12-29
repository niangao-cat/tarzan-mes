package com.ruike.itf.app.service;

/**
 * 接口表数据删除应用服务
 *
 * @author penglin.sui@hand-china.com 2021-07-25 16:06
 */
public interface ItfDeleteTableDataService {
    /**
     * 接口表数据删除
     * @param tenantId 租户ID
     * @return
     */
    void invokeDel(Long tenantId);
}
