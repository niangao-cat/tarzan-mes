package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeWkcEoRel;

/**
 * 工位EO关系表应用服务
 *
 * @author penglin.sui@hand-china.com 2021-02-20 10:27:36
 */
public interface HmeWkcEoRelService {
    /**
     *
     * @Description 保存工位EO关系
     *
     * @author penglin.sui
     * @date 2021/2/20 10:35
     * @return void
     *
     */
    void saveWkcEoRel(Long tenantId, HmeWkcEoRel dto);
}
