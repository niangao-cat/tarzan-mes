package com.ruike.hme.app.service;

import java.util.List;

/**
 * SN进出站设备状态记录表应用服务
 *
 * @author chaonan.hu@hand-china.com 2020-06-28 16:52:11
 */
public interface HmeEoJobEquipmentService {


    void binndHmeEoJobEquipment(Long tenantId, List<String> eoJobSnIdList, String workcellId);

    /**
     * COS作业平台-绑定设备
     *
     * @param tenantId
     * @param eoJobSnIdList
     * @param workcellId
     * @param equipmentId
     * @author sanfeng.zhang@hand-china.com 2021/3/12 18:07
     * @return void
     */
    void bindHmeEoJobEquipmentOfTimeProcess(Long tenantId, List<String> eoJobSnIdList, String workcellId, String equipmentId);
}
