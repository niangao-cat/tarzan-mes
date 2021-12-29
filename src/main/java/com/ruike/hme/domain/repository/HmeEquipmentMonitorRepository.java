package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.*;

import java.util.List;

/**
 * 设备监控平台资源库
 *
 * @author chaonan.hu@hand-china.com 2020-07-16 18:45:13
 */
public interface HmeEquipmentMonitorRepository {

    /**
     * 事业部下拉框数据查询
     *
     * @param tenantId 租户Id
     * @param siteId   站点Id
     * @param areaCategory 类型
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentMonitorVO>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/16 19:08:31
     */
    List<HmeEquipmentMonitorVO> departmentDataQuery(Long tenantId, String siteId, String areaCategory);

    /**
     * 车间下拉框数据查询
     *
     * @param tenantId     租户Id
     * @param siteId       工厂Id
     * @param departmentId 事业部Id
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentMonitorVO2>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/16 19:31:43
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
     * @param tenantId   租户Id
     * @param siteId     工厂Id
     * @param prodLineId 产线Id
     * @return com.ruike.hme.domain.vo.HmeEquipmentMonitorVO6
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/16 20:09:47
     */
    HmeEquipmentMonitorVO6 equipmentStatusQuery(Long tenantId, String siteId, String prodLineId);

    /**
     * 当月异常停机TOP10数据查询
     *
     * @param tenantId 租户Id
     * @param equipmentIdList 设备Id集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/22 09:44:04
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentMonitorVO8>
     */
    List<HmeEquipmentMonitorVO8> downEquipmentQuery(Long tenantId, List<String> equipmentIdList);

    /**
     * 停机设备详情
     * 
     * @param tenantId 租户ID
     * @param dto 设备停机信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/22 14:51:58
     * @return com.ruike.hme.domain.vo.HmeEquipmentMonitorVO9
     */
    HmeEquipmentMonitorVO9 downEquipmentDetailQuery(Long tenantId, HmeEquipmentMonitorVO8 dto);

    /**
     * 30天内异常历史
     *
     * @param tenantId 租户ID
     * @param dto 设备停机信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/22 15:44:15
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentMonitorVO10>
     */
    List<HmeEquipmentMonitorVO10> exceptionHistoryQuery(Long tenantId, HmeEquipmentMonitorVO8 dto);

    /**
     * 同异常类型最近三次
     * 
     * @param tenantId 租户ID
     * @param dto 设备停机信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/22 16:23:14
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentMonitorVO11>
     */
    List<HmeEquipmentMonitorVO11> sameExceptionTypeQuery(Long tenantId, HmeEquipmentMonitorVO8 dto);

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
