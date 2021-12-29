package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfMonthlyPlanIfaceDTO2;

import java.util.List;
import java.util.Map;

/**
 * 月度计划接口表应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-06-01 14:21:59
 */
public interface ItfMonthlyPlanIfaceService {

    /**
     * 月度计划接口
     *
     * @param monthlyPlanMap 报文
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/1 02:44:25
     * @return java.util.List<com.ruike.itf.api.dto.ItfMonthlyPlanIfaceDTO2>
     */
    List<ItfMonthlyPlanIfaceDTO2> invoke(Map<String,Object> monthlyPlanMap);

}
