package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeWoTrialCalculate;
import org.apache.ibatis.annotations.Param;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEmployeeOutputSummary;

import java.util.List;

/**
 * 员工产量汇总表资源库
 *
 * @author penglin.sui@hand-china.com 2021-07-28 11:19:48
 */
public interface HmeEmployeeOutputSummaryRepository extends BaseRepository<HmeEmployeeOutputSummary> {
    /**
     * 进站信息查询
     *
     * @param tenantId  租户id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @author penglin.sui@hand-china.com 2021/7/28 15:09
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEmployeeOutputSummary>
     */
    List<HmeEmployeeOutputSummary> selectDataOfSignIn(@Param("tenantId") Long tenantId,
                                                      @Param("startTime") String startTime,
                                                      @Param("endTime") String endTime);

    /**
     * 出站信息查询
     *
     * @param tenantId  租户id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @author penglin.sui@hand-china.com 2021/7/28 15:09
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEmployeeOutputSummary>
     */
    List<HmeEmployeeOutputSummary> selectDataOfSignOut(@Param("tenantId") Long tenantId,
                                                       @Param("startTime") String startTime,
                                                       @Param("endTime") String endTime);

    /**
     *
     * @Description 批量新增
     *
     * @author penglin.sui
     * @date 2021/7/29 16:31
     * @param insertList 新增数据列表
     * @return void
     *
     */
    void myBatchInsert(List<HmeEmployeeOutputSummary> insertList);

    /**
     *
     * @Description 批量删除
     *
     * @author penglin.sui
     * @date 2021/8/4 20:09
     * @param outSummaryIdList 删除数据主键ID列表
     * @return void
     *
     */
    void myBatchDelete(List<String> outSummaryIdList);
}
