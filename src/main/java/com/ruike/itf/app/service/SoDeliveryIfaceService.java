package com.ruike.itf.app.service;

import com.ruike.itf.domain.entity.SoDeliveryIface;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
/**
 * 销售发退货单接口表应用服务
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-02 13:41:40
 */
public interface SoDeliveryIfaceService {

    /**
    * 销售发退货单接口表查询参数
    *
    * @param tenantId 租户ID
    * @param soDeliveryIface 销售发退货单接口表
    * @param pageRequest 分页
    * @return 销售发退货单接口表列表
    */
    Page<SoDeliveryIface> list(Long tenantId, SoDeliveryIface soDeliveryIface, PageRequest pageRequest);

    /**
     * 销售发退货单接口表详情
     *
     * @param tenantId 租户ID
     * @param ifaceId 主键
     * @return 销售发退货单接口表列表
     */
    SoDeliveryIface detail(Long tenantId, Long ifaceId);

    /**
     * 创建销售发退货单接口表
     *
     * @param tenantId 租户ID
     * @param soDeliveryIface 销售发退货单接口表
     * @return 销售发退货单接口表
     */
    SoDeliveryIface create(Long tenantId, SoDeliveryIface soDeliveryIface);

    /**
     * 更新销售发退货单接口表
     *
     * @param tenantId 租户ID
     * @param soDeliveryIface 销售发退货单接口表
     * @return 销售发退货单接口表
     */
    SoDeliveryIface update(Long tenantId, SoDeliveryIface soDeliveryIface);

    /**
     * 删除销售发退货单接口表
     *
     * @param soDeliveryIface 销售发退货单接口表
     */
    void remove(SoDeliveryIface soDeliveryIface);
}
