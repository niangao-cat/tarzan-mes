package com.ruike.itf.app.service;

import java.util.Map;

/**
 * 工艺路线接口表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
public interface ItfRoutingOperationIfaceService {

    /**
     * 工艺路线同步接口
     *
     * @param dataMap
     * @author kejin.liu01@hand-china.com
     * @return
     */
    Map<String, Object> invoke(Map dataMap);

    /**
     * 工艺路线同步接口2
     *
     * @param dataMap
     * @author sanfeng.zhang@hand-china.com 2021/11/23 20:57
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> invoke2(Map dataMap);
}
