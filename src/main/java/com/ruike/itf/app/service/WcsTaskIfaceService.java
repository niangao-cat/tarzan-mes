package com.ruike.itf.app.service;

import com.ruike.itf.domain.entity.WcsTaskIface;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
/**
 * 成品出库指令信息接口表应用服务
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-06 11:04:14
 */
public interface WcsTaskIfaceService {

    /**
    * 成品出库指令信息接口表查询参数
    *
    * @param tenantId 租户ID
    * @param wcsTaskIface 成品出库指令信息接口表
    * @param pageRequest 分页
    * @return 成品出库指令信息接口表列表
    */
    Page<WcsTaskIface> list(Long tenantId, WcsTaskIface wcsTaskIface, PageRequest pageRequest);

    /**
     * 成品出库指令信息接口表详情
     *
     * @param tenantId 租户ID
     * @param ifaceId 主键
     * @return 成品出库指令信息接口表列表
     */
    WcsTaskIface detail(Long tenantId, Long ifaceId);

    /**
     * 创建成品出库指令信息接口表
     *
     * @param tenantId 租户ID
     * @param wcsTaskIface 成品出库指令信息接口表
     * @return 成品出库指令信息接口表
     */
    WcsTaskIface create(Long tenantId, WcsTaskIface wcsTaskIface);

    /**
     * 更新成品出库指令信息接口表
     *
     * @param tenantId 租户ID
     * @param wcsTaskIface 成品出库指令信息接口表
     * @return 成品出库指令信息接口表
     */
    WcsTaskIface update(Long tenantId, WcsTaskIface wcsTaskIface);

    /**
     * 删除成品出库指令信息接口表
     *
     * @param wcsTaskIface 成品出库指令信息接口表
     */
    void remove(WcsTaskIface wcsTaskIface);
}
