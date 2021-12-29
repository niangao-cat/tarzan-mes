package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeAreaDayPlanReachRate;
import com.ruike.hme.domain.vo.HmeCalendarShiftVO;
import com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO2;
import com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO3;
import com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO4;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 制造部日计划达成率看板Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2021-07-02 14:31:13
 */
public interface HmeAreaDayPlanReachRateMapper extends BaseMapper<HmeAreaDayPlanReachRate> {

    /**
     * 查询计划班次
     * @param tenantId
     * @param siteId
     * @param prodLineIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCalendarShiftVO>
     * @author sanfeng.zhang@hand-china.com 2021/7/2
     */
    List<HmeCalendarShiftVO> queryCalendarShiftList(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("prodLineIdList") List<String> prodLineIdList);

    /**
     * 获取当天实绩班次
     * @param tenantId
     * @param calendarShiftIdList
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/6/1
     */
    List<String> queryShiftIdList(@Param("tenantId") Long tenantId, @Param("calendarShiftIdList") List<String> calendarShiftIdList);

    /**
     * 取产线当班所做所有工单
     *
     * @param tenantId
     * @param shiftIdList
     * @param prodLineIdList
     * @param cosJobTypeList
     * @author sanfeng.zhang@hand-china.com 2021/5/30 22:06
     * @return java.util.List<java.lang.String>
     */
    List<String> queryWorkOrderByShiftIdAndProdLineId(@Param("tenantId") Long tenantId, @Param("shiftIdList") List<String> shiftIdList, @Param("prodLineIdList") List<String> prodLineIdList, @Param("cosJobTypeList") List<String> cosJobTypeList);

    /**
     * 取产线当班派工的所有工单
     *
     * @param tenantId
     * @param prodLineIdList
     * @param calendarShiftIdList
     * @author sanfeng.zhang@hand-china.com 2021/5/30 22:06
     * @return java.util.List<java.lang.String>
     */
    List<String> queryDispatchWorkOrderByShiftIdAndProdLineId(@Param("tenantId") Long tenantId, @Param("prodLineIdList") List<String> prodLineIdList, @Param("calendarShiftIdList") List<String> calendarShiftIdList);

    /**
     * 取工单工艺路线的工序及所属工段
     *
     * @param tenantId
     * @param workOrderIdList
     * @param siteId
     * @author sanfeng.zhang@hand-china.com 2021/5/30 22:55
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO3>
     */
    List<HmeMakeCenterProduceBoardVO3> queryLineWorkcellAndProcess(@Param("tenantId") Long tenantId, @Param("workOrderIdList") List<String> workOrderIdList, @Param("siteId") String siteId);

    /**
     * 实际交付
     *
     * @param tenantId
     * @param shiftIdList
     * @param workOrderIdList
     * @param endProcessId
     * @param siteId
     * @author sanfeng.zhang@hand-china.com 2021/5/30 23:50
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO2>
     */
    List<HmeMakeCenterProduceBoardVO2> queryActualDeliverQty(@Param("tenantId") Long tenantId, @Param("shiftIdList") List<String> shiftIdList, @Param("workOrderIdList") List<String> workOrderIdList, @Param("endProcessId") List<String> endProcessId, @Param("siteId") String siteId);

    /**
     * 派工数量
     *
     * @param tenantId
     * @param shiftIdList
     * @param workOrderIdList
     * @param lineWorkcellIdList
     * @author sanfeng.zhang@hand-china.com 2021/5/31 0:25
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO2>
     */
    List<HmeMakeCenterProduceBoardVO2> queryDispatchQty(@Param("tenantId") Long tenantId, @Param("shiftIdList") List<String> shiftIdList, @Param("workOrderIdList") List<String> workOrderIdList, @Param("lineWorkcellIdList") List<String> lineWorkcellIdList);

    /**
     * 派工工单查询物料
     * @param tenantId
     * @param dispatchWorkOrderIdList
     * @param siteId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO4>
     * @author sanfeng.zhang@hand-china.com 2021/6/8
     */
    List<HmeMakeCenterProduceBoardVO4> queryDispatchMaterialList(@Param("tenantId") Long tenantId, @Param("dispatchWorkOrderIdList") List<String> dispatchWorkOrderIdList, @Param("siteId") String siteId);

    /**
     * 删除当天日计划达成率信息
     * @param tenantId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/7/2
     */
    void batchDeleteDayPlanReachRate(@Param("tenantId") Long tenantId);
}
