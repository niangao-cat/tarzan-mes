package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeOrganizationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 员工产量汇总表应用服务
 *
 * @author penglin.sui@hand-china.com 2021-07-28 11:19:48
 */
public interface HmeEmployeeOutputSummaryService {
    /**
     * 员工产量汇总
     *
     * @param tenantId  租户id
     * @author penglin.sui@hand-china.com 2021/7/28 15:13
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEmployeeOutputSummary>
     */
    void employeeOutPutSummary(@Param("tenantId") Long tenantId);
}
