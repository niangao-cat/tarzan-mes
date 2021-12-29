package com.ruike.itf.app.service;

import com.ruike.itf.domain.entity.ItfBomComponentIface;

import java.util.List;
import java.util.Map;

/**
 * BOM接口表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
public interface ItfBomComponentIfaceService {

    /**
     * BOM同步接口
     *
     * @param dto
     * @author kejin.liu01@hand-china.com
     */
    List<ItfBomComponentIface> invoke(Map dto);
}
