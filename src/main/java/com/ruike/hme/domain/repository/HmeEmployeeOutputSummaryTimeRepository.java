package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeEmployeeOutputSummary;
import org.apache.ibatis.annotations.Param;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEmployeeOutputSummaryTime;

import java.util.Date;
import java.util.List;

/**
 * 员工产量汇总时间表资源库
 *
 * @author penglin.sui@hand-china.com 2021-07-28 15:31:45
 */
public interface HmeEmployeeOutputSummaryTimeRepository extends BaseRepository<HmeEmployeeOutputSummaryTime> {
    /**
     * 查询当前已经汇总成功的最大时间
     *
     * @param tenantId  租户id
     * @author penglin.sui@hand-china.com 2021/7/28 15:41
     * @return java.util.Date
     */
    Date selectMaxJobTime(@Param("tenantId") Long tenantId);

    /**
     *
     * @Description 批量新增
     *
     * @author penglin.sui
     * @date 2021/7/29 18:23
     * @param insertList 新增数据列表
     * @return void
     *
     */
    void myBatchInsert(List<HmeEmployeeOutputSummaryTime> insertList);
}
