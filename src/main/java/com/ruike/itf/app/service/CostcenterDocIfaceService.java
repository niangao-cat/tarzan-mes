package com.ruike.itf.app.service;

import com.ruike.itf.domain.entity.CostcenterDocIface;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
/**
 * 生产领退料单接口应用服务
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-06-30 13:48:47
 */
public interface CostcenterDocIfaceService {

    /**
    * 生产领退料单接口查询参数
    *
    * @param tenantId 租户ID
    * @param costcenterDocIface 生产领退料单接口
    * @param pageRequest 分页
    * @return 生产领退料单接口列表
    */
    Page<CostcenterDocIface> list(Long tenantId, CostcenterDocIface costcenterDocIface, PageRequest pageRequest);

    /**
     * 生产领退料单接口详情
     *
     * @param tenantId 租户ID
     * @param ifaceId 主键
     * @return 生产领退料单接口列表
     */
    CostcenterDocIface detail(Long tenantId, Long ifaceId);

    /**
     * 创建生产领退料单接口
     *
     * @param tenantId 租户ID
     * @param costcenterDocIface 生产领退料单接口
     * @return 生产领退料单接口
     */
    CostcenterDocIface create(Long tenantId, CostcenterDocIface costcenterDocIface);

    /**
     * 更新生产领退料单接口
     *
     * @param tenantId 租户ID
     * @param costcenterDocIface 生产领退料单接口
     * @return 生产领退料单接口
     */
    CostcenterDocIface update(Long tenantId, CostcenterDocIface costcenterDocIface);

    /**
     * 删除生产领退料单接口
     *
     * @param costcenterDocIface 生产领退料单接口
     */
    void remove(CostcenterDocIface costcenterDocIface);
}
