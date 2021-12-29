package com.ruike.itf.app.service;

/**
 * 设备数据采集接口表数据删除应用服务
 *
 * @author penglin.sui@hand-china.com 2021-07-24 11:53
 */
public interface ItfDeleteCollectTableDataService {
    /**
     * 设备数据采集接口表数据删除
     * @param tenantId 租户ID
     * @return
     */
    void invokeDel(Long tenantId);
}
