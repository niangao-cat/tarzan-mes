package com.ruike.itf.app.service;

import com.ruike.itf.domain.entity.WcsTaskLineIface;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
/**
 * 成品出库指令信息接口行表应用服务
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-06 11:04:14
 */
public interface WcsTaskLineIfaceService {

    /**
    * 成品出库指令信息接口行表查询参数
    *
    * @param tenantId 租户ID
    * @param wcsTaskLineIface 成品出库指令信息接口行表
    * @param pageRequest 分页
    * @return 成品出库指令信息接口行表列表
    */
    Page<WcsTaskLineIface> list(Long tenantId, WcsTaskLineIface wcsTaskLineIface, PageRequest pageRequest);

    /**
     * 成品出库指令信息接口行表详情
     *
     * @param tenantId 租户ID
     * @param ifaceId 主键
     * @return 成品出库指令信息接口行表列表
     */
    WcsTaskLineIface detail(Long tenantId, Long ifaceId);

    /**
     * 创建成品出库指令信息接口行表
     *
     * @param tenantId 租户ID
     * @param wcsTaskLineIface 成品出库指令信息接口行表
     * @return 成品出库指令信息接口行表
     */
    WcsTaskLineIface create(Long tenantId, WcsTaskLineIface wcsTaskLineIface);

    /**
     * 更新成品出库指令信息接口行表
     *
     * @param tenantId 租户ID
     * @param wcsTaskLineIface 成品出库指令信息接口行表
     * @return 成品出库指令信息接口行表
     */
    WcsTaskLineIface update(Long tenantId, WcsTaskLineIface wcsTaskLineIface);

    /**
     * 删除成品出库指令信息接口行表
     *
     * @param wcsTaskLineIface 成品出库指令信息接口行表
     */
    void remove(WcsTaskLineIface wcsTaskLineIface);
}
