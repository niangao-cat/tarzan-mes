package com.ruike.itf.app.service;

import com.ruike.itf.domain.entity.ItfCosMonitorIface;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
/**
 * COS良率监控接口表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-09-30 14:14:20
 */
public interface ItfCosMonitorIfaceService {

    /**
    * COS良率监控接口表查询参数
    *
    * @param tenantId 租户ID
    * @param itfCosMonitorIface COS良率监控接口表
    * @param pageRequest 分页
    * @return COS良率监控接口表列表
    */
    Page<ItfCosMonitorIface> list(Long tenantId, ItfCosMonitorIface itfCosMonitorIface, PageRequest pageRequest);

    /**
     * COS良率监控接口表详情
     *
     * @param tenantId 租户ID
     * @param cosMonitorIfaceId 主键
     * @return COS良率监控接口表列表
     */
    ItfCosMonitorIface detail(Long tenantId, Long cosMonitorIfaceId);

    /**
     * 创建COS良率监控接口表
     *
     * @param tenantId 租户ID
     * @param itfCosMonitorIface COS良率监控接口表
     * @return COS良率监控接口表
     */
    ItfCosMonitorIface create(Long tenantId, ItfCosMonitorIface itfCosMonitorIface);

    /**
     * 更新COS良率监控接口表
     *
     * @param tenantId 租户ID
     * @param itfCosMonitorIface COS良率监控接口表
     * @return COS良率监控接口表
     */
    ItfCosMonitorIface update(Long tenantId, ItfCosMonitorIface itfCosMonitorIface);

    /**
     * 删除COS良率监控接口表
     *
     * @param itfCosMonitorIface COS良率监控接口表
     */
    void remove(ItfCosMonitorIface itfCosMonitorIface);
}
