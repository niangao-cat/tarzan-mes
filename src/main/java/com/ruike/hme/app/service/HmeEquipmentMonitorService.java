package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.*;

import java.util.List;

/**
 * 设备监控平台应用服务
 *
 * @author chaonan.hu@hand-china.com 2020-07-16 18:43:32
 */
public interface HmeEquipmentMonitorService {
    /**
     * 事业部下拉框数据查询
     *
     * @param tenantId 租户Id
     * @param siteId   站点Id
     * @param areaCategory
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentMonitorVO>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/16 19:17:23
     */
    List<HmeEquipmentMonitorVO> departmentDataQuery(Long tenantId, String siteId, String areaCategory);

    /**
     * 车间下拉框数据查询
     *
     * @param tenantId     租户Id
     * @param siteId       工厂Id
     * @param departmentId 事业部Id
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentMonitorVO2>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/16 19:42:18
     */
    List<HmeEquipmentMonitorVO2> workshopDataQuery(Long tenantId, String siteId, String departmentId);

    /**
     * 产线下拉框数据查询
     *
     * @param tenantId   租户Id
     * @param siteId     工厂Id
     * @param workshopId 车间Id
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentMonitorVO3>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/16 19:48:15
     */
    List<HmeEquipmentMonitorVO3> prodLineDataQuery(Long tenantId, String siteId, String workshopId);

    /**
     * 设备信息查询
     *
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param prodLineId 产线ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/22 16:45:24
     * @return com.ruike.hme.domain.vo.HmeEquipmentMonitorVO6
     */
    HmeEquipmentMonitorVO6 equipmentStatusQuery(Long tenantId, String siteId, String prodLineId);

    /**
     * 停机设备详情总查询
     *
     * @param tenantId 租户ID
     * @param dto 设备停机信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/22 04:52:41
     * @return com.ruike.hme.domain.vo.HmeEquipmentMonitorVO12
     */
    HmeEquipmentMonitorVO12 equipmentDetailQuery(Long tenantId, HmeEquipmentMonitorVO8 dto);
}
