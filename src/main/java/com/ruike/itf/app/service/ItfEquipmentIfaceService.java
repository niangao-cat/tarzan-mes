package com.ruike.itf.app.service;

import com.ruike.itf.domain.entity.ItfEquipmentIface;
import com.ruike.itf.domain.vo.ItfEquipmentReturnVO;

import java.util.List;

/**
 * 设备台帐接口表应用服务
 *
 * @author yonghui.zhu@hand-china.com 2021-01-08 14:11:29
 */
public interface ItfEquipmentIfaceService {

    /**
     * 接口调用
     *
     * @param tenantId 租户
     * @param list     参数列表
     * @return java.util.List<com.ruike.itf.domain.vo.ItfEquipmentReturnVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/8 04:12:28
     */
    List<ItfEquipmentReturnVO> invoke(Long tenantId, List<ItfEquipmentIface> list);
}
