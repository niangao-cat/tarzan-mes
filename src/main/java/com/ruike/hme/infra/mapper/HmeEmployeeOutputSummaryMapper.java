package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.entity.HmeEmployeeOutputSummary;
import com.ruike.hme.domain.entity.HmeWoTrialCalculate;
import com.ruike.hme.domain.vo.HmeEmployeeOutputSummaryVO;
import com.ruike.hme.domain.vo.HmeOrganizationVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工产量汇总表Mapper
 *
 * @author penglin.sui@hand-china.com 2021-07-28 11:19:48
 */
public interface HmeEmployeeOutputSummaryMapper extends BaseMapper<HmeEmployeeOutputSummary> {
    /**
     * 进站信息查询
     *
     * @param tenantId  租户id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @author penglin.sui@hand-china.com 2021/7/28 15:06
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
     * @author penglin.sui@hand-china.com 2021/7/28 15:06
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEmployeeOutputSummary>
     */
    List<HmeEmployeeOutputSummary> selectDataOfSignOut(@Param("tenantId") Long tenantId,
                                                       @Param("startTime") String startTime,
                                                       @Param("endTime") String endTime);

    /**
     * 工序查询工段 产线
     *
     * @param tenantId
     * @param processIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOrganizationVO>
     * @author penglin.sui@hand-china.com 2021/7/28
     */
    List<HmeOrganizationVO> queryOrganizationByProcessIds(@Param("tenantId") Long tenantId,
                                                          @Param("siteId") String siteId,
                                                          @Param(value = "processIdList") List<String> processIdList);

    /**
     * 汇总数据查询
     *
     * @param tenantId
     * @param siteId
     * @param startTime
     * @param dtoList
     * @param endTime
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeOutputSummaryVO>
     * @author penglin.sui@hand-china.com 2021/7/28
     */
    List<HmeEmployeeOutputSummaryVO> querySummarys(@Param("tenantId") Long tenantId,
                                                   @Param("siteId") String siteId,
                                                   @Param("startTime") String startTime,
                                                   @Param("endTime") String endTime,
                                                   @Param(value = "dtoList") List<HmeEmployeeOutputSummary> dtoList);

    /**
     * 汇总数据查询
     *
     * @param tenantId
     * @param startTime
     * @param endTime
     * @param dtoList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeOutputSummaryVO>
     * @author penglin.sui@hand-china.com 2021/7/28
     */
    List<HmeEmployeeOutputSummaryVO> queryNcQtys(@Param("tenantId") Long tenantId,
                                                 @Param("startTime") String startTime,
                                                 @Param("endTime") String endTime,
                                                 @Param(value = "dtoList") List<HmeEmployeeOutputSummaryVO> dtoList);

    /**
     *
     * @Description 批量新增
     *
     * @author penglin.sui
     * @date 2021/7/29 16:51
     * @param domains 新增数据列表
     * @return void
     *
     */
    void batchInsert(@Param(value = "domains") List<HmeEmployeeOutputSummary> domains);

    /**
     * 员工产量汇总数据查询
     *
     * @param tenantId  租户id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @author penglin.sui@hand-china.com 2021/8/4 20:07
     * @return java.util.List<java.lang.String>
     */
    List<String> selectEmployeeOutSummary(@Param("tenantId") Long tenantId,
                                          @Param("startTime") String startTime,
                                          @Param("endTime") String endTime);

    /**
     *
     * @Description 批量删除
     *
     * @author penglin.sui
     * @date 2021/8/4 20:10
     * @param outSummaryIdList 删除数据主键ID列表
     * @return void
     *
     */
    void batchDelete(@Param(value = "outSummaryIdList") List<String> outSummaryIdList);
}
