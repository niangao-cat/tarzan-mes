package com.ruike.hme.app.service;

import org.hzero.core.base.AopProxy;

public interface HmeEdginkService extends AopProxy<HmeEdginkService> {

    /**
     * @Description 获取激光功率计数据
     * @param tenantId
     * @param equipmentCode
     * @Date 2020-07-29 09:35:58
     * @Author wenzhnag.yu
     */
    String getOphir(Long tenantId, String equipmentCode);

    /**
     * @Description 获取毫瓦功率计数据
     * @param tenantId
     * @param equipmentCode
     * @Date 2020-07-29 09:35:58
     * @Author wenzhnag.yu
     */
    String getThorlabs(Long tenantId, String equipmentCode);

}
