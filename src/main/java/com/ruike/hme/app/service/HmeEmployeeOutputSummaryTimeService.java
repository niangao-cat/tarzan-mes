package com.ruike.hme.app.service;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 员工产量汇总时间表应用服务
 *
 * @author penglin.sui@hand-china.com 2021-07-28 15:31:45
 */
public interface HmeEmployeeOutputSummaryTimeService {
    /**
     * 查询当前已经汇总成功的最大时间
     *
     * @param tenantId  租户id
     * @author penglin.sui@hand-china.com 2021/7/28 15:43
     * @return java.util.Date
     */
    Date selectMaxJobTime(@Param("tenantId") Long tenantId);

    /**
     * 保存数据
     *
     * @param tenantId  租户id
     * @param startTime 开始时间
     * @param endTime  结束时间
     * @author penglin.sui@hand-china.com 2021/7/29 17:01
     * @return
     */
    void batchInserData(@Param("tenantId") Long tenantId,
                        @Param("startTime") Date startTime,
                        @Param("endTime") Date endTime);
}
