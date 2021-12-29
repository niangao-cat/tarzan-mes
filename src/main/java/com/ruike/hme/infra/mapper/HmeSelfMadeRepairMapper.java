package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.HmeSelfMadeRepairVO;
import org.apache.ibatis.annotations.Param;
import tarzan.order.domain.entity.MtWorkOrder;

import java.util.List;

/**
 * 自制件返修
 *
 * @author sanfeng.zhang@hand-china.com 2021-03-12 09:59:30
 */
public interface HmeSelfMadeRepairMapper {

    /**
     * 查询原始条码返修信息
     *
     * @param tenantId
     * @param materialLotCode
     * @return java.util.List<com.ruike.hme.domain.vo.HmeSelfMadeRepairVO>
     * @author sanfeng.zhang@hand-china.com 2021/3/12 11:07
     */
    List<HmeSelfMadeRepairVO> querySelfMadeRepairByCode(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);

    /**
     * 物料类型
     *
     * @param tenantId
     * @param siteId
     * @param workOrderId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/3/12 14:20
     */
    List<String> queryProItemType(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("workOrderId") String workOrderId);

    /**
     * 获取建议条码
     *
     * @param tenantId
     * @param eoId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/3/12 15:16
     */
    String querySuggestSnCode(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     * 返修条码查询原条码的工单
     *
     * @param tenantId
     * @param materialLotCode
     * @return java.util.List<tarzan.order.domain.entity.MtWorkOrder>
     * @author sanfeng.zhang@hand-china.com 2021/8/13
     */
    List<MtWorkOrder> querySelfReworkWorkOrder(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);
}
