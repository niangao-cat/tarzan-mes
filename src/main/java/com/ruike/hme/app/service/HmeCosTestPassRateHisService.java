package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeCosTestPassRateHisVO;

import java.util.List;

/**
 * COS测试良率维护历史表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-09-06 11:44:40
 */
public interface HmeCosTestPassRateHisService {

    /**
     * COS测试良率维护历史表基础查询
     *
     * @param tenantId
     * @param testId
     * @return
     */
    List<HmeCosTestPassRateHisVO> queryHmeCosTestPassRateHis(Long tenantId, String testId);


}
