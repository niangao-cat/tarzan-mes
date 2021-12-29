package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeWoTrialCalculateReportQueryDTO;
import com.ruike.hme.api.dto.HmeWoTrialCalculateReportSaveDTO;
import com.ruike.hme.domain.vo.HmeWoTrialCalculateReportVO;
import org.hzero.core.base.AopProxy;

/**
 * 工单入库日期试算表应用服务
 *
 * @author yuchao.wang@hand-china.com 2020-08-25 21:54:56
 */
public interface HmeWoTrialCalculateService extends AopProxy<HmeWoTrialCalculateService> {

    /**
     *
     * @Description 查询试算报表
     *
     * @author yuchao.wang
     * @date 2020/8/27 10:15
     * @param tenantId 租户ID
     * @param dto 参数
     * @return com.ruike.hme.domain.vo.HmeWoTrialCalculateReportVO
     *
     */
    HmeWoTrialCalculateReportVO queryReport(Long tenantId, HmeWoTrialCalculateReportQueryDTO dto);

    /**
     *
     * @Description 试算报表重排
     *
     * @author yuchao.wang
     * @date 2020/8/27 11:33
     * @param tenantId 租户ID
     * @param workOrderId 工单ID
     * @return
     *
     */
    void reschedule(Long tenantId, String workOrderId);

    /**
     *
     * @Description 试算报表保存
     *
     * @author yuchao.wang
     * @date 2020/8/27 13:58
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    void save(Long tenantId, HmeWoTrialCalculateReportSaveDTO dto);
}
