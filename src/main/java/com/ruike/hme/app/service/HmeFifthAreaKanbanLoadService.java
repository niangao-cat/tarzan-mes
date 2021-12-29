package com.ruike.hme.app.service;

import java.util.List;

/**
 * 五部看板装载信息应用服务
 *
 * @author penglin.sui@hand-china.com 2021-06-08 16:01:49
 */
public interface HmeFifthAreaKanbanLoadService {
    /**
     * 创建五部看板装载数据
     *
     * @param tenantId
     * @return void
     * @author penglin.sui@hand-china.com 2021/6/8 9:26
     */
    void createFifthAreaLoad(Long tenantId, List<String> eoIdList);
}
