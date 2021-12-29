package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeWoTrialCalculateReportQueryDTO;
import com.ruike.hme.domain.entity.HmeWoTrialCalculate;
import com.ruike.hme.domain.vo.HmeWoCalendarShiftVO3;
import com.ruike.hme.domain.vo.HmeWoTrialCalculateReportWoVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.*;
import java.util.*;

/**
 * 工单入库日期试算表Mapper
 *
 * @author yuchao.wang@hand-china.com 2020-08-25 21:54:56
 */
public interface HmeWoTrialCalculateMapper extends BaseMapper<HmeWoTrialCalculate> {

    /**
     *
     * @Description 查询试算报表基础数据
     *
     * @author yuchao.wang
     * @date 2020/8/27 10:10
     * @param tenantId 租户ID
     * @param queryDTO 参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWoTrialCalculateReportWoVO>
     *
     */
    List<HmeWoTrialCalculateReportWoVO> queryTrialCalculateReport(@Param("tenantId") Long tenantId,
                                                                  @Param("queryDTO") HmeWoTrialCalculateReportQueryDTO queryDTO);

    /**
     *
     * @Description 查询时间区间内的工作日历天
     *
     * @author yuchao.wang
     * @date 2020/8/27 10:10
     * @param tenantId 租户ID
     * @param vo 参数
     * @return java.util.List<java.time.LocalDate>
     *
     */
    List<LocalDate> queryCalendarShiftByTime(@Param(value = "tenantId") Long tenantId,
                                             @Param(value = "vo") HmeWoCalendarShiftVO3 vo);

    /**
     *
     * @Description 根据工单ID删除数据
     *
     * @author yuchao.wang
     * @date 2020/8/27 13:47
     * @param tenantId 租户ID
     * @param workOrderId 工单ID
     * @return int
     *
     */
    int deleteByWorkOrderId(@Param(value = "tenantId") Long tenantId, @Param(value = "workOrderId") String workOrderId);

    /**
     *
     * @Description 是否有该工单ID下的数据
     *
     * @author yuchao.wang
     * @date 2020/8/27 13:52
     * @param tenantId 租户ID
     * @param workOrderId 工单ID
     * @return boolean
     *
     */
    Integer hasTrialCalculateDataByWoId(@Param(value = "tenantId") Long tenantId, @Param(value = "workOrderId") String workOrderId);

    /**
     *
     * @Description 批量新增
     *
     * @author yuchao.wang
     * @date 2020/8/27 16:49
     * @param domains 新增数据列表
     * @return void
     *
     */
    void batchInsert(@Param(value = "domains") List<HmeWoTrialCalculate> domains);
}
