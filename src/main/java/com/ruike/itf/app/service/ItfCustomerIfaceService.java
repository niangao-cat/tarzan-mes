package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfCommonReturnDTO;
import com.ruike.itf.api.dto.ItfCustomerDTO;

import java.util.List;
import java.util.Map;

/**
 * 客户数据全量接口表应用服务
 *
 * @author yapeng.yao@hand-china.com 2020-08-21 11:19:59
 */
public interface ItfCustomerIfaceService {

    /**
     * 客户数据同步接口
     *
     * @param dataMap
     * @return
     */
    List<ItfCommonReturnDTO> invoke(Map dataMap);
}