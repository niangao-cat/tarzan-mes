package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeWkcShiftAttr;

/**
 * 班组交接事项记录表应用服务
 *
 * @author chaonan.hu@hand-china.com 2020-07-31 11:00:48
 */
public interface HmeWkcShiftAttrService {

    /**
     * 创建或者更新班组交接事项记录
     *
     * @param tenantId 租户ID
     * @param dto 交接数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/31 11:26:35
     * @return com.ruike.hme.domain.entity.HmeWkcShiftAttr
     */
    HmeWkcShiftAttr createOrUpdate(Long tenantId, HmeWkcShiftAttr dto);
}
