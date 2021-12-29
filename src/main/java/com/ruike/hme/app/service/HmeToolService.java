package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeToolInsertDto;

import java.util.List;

/**
 * 工装基础数据表应用服务
 *
 * @author li.zhang13@hand-china.com 2021-01-07 10:06:45
 */
public interface HmeToolService {


    /**
     *  新增或更改工装基本数据
     *
     * @param tenantId      租户Id
     * @param hmeToolInsertDtos      工装基本数据
     * @author li.zhang13@hand-china.com
     */
    void insertorupdateSelective(String tenantId, List<HmeToolInsertDto> hmeToolInsertDtos);
}
