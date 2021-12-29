package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeWoDispatchCompSuiteQueryDTO;
import com.ruike.hme.domain.entity.HmeWoDispatchRecode;
import com.ruike.hme.domain.vo.*;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModArea;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 工单派工记录表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */
public interface HmeWoDispatchRecodeMapper extends BaseMapper<HmeWoDispatchRecode> {

    /**
     * 查询车间-产品线信息
     *
     * @param tenantId   租户
     * @param topSiteId  站点
     * @param prodLineId 产线ID
     * @return List<HmeModAreaVO>
     */
    List<HmeModAreaVO> woModAreaQuery(@Param("tenantId") Long tenantId,
                                      @Param("topSiteId") String topSiteId,
                                      @Param("prodLineId") String prodLineId);

    /**
     * 查询产品信息
     *
     * @param tenantId        租户
     * @param prodLineId      产线ID
     * @param workOrderIdList 工单
     * @return List<HmeWoDispatchVO>
     */
    List<HmeWoDispatchVO> woProdQuery(@Param("tenantId") Long tenantId,
                                      @Param("prodLineId") String prodLineId,
                                      @Param("workOrderIdList") List<String> workOrderIdList);

    /**
     * 查询产品线信息
     *
     * @param tenantId        租户
     * @param productId       产品ID
     * @param prodLineId      产线ID
     * @param workOrderIdList 工单
     * @return List<HmeWoDispatchVO2>
     */
    List<HmeWoDispatchVO2> woProdLineQuery(@Param("tenantId") Long tenantId,
                                           @Param("productId") String productId,
                                           @Param("prodLineId") String prodLineId,
                                           @Param("workOrderIdList") List<String> workOrderIdList);

    /**
     * 查询路线信息
     *
     * @param tenantId
     * @param workOrderId
     * @param prodLineId
     * @param topSiteId
     * @return
     */
    List<HmeWoDispatchVO3> woWorkCellQuery(@Param("tenantId") Long tenantId,
                                           @Param("workOrderId") String workOrderId,
                                           @Param("topSiteId") String topSiteId,
                                           @Param("prodLineId") String prodLineId);

    /**
     * 查询当前日期前一天工作日日期
     *
     * @return
     */
    Date shiftDateFromGet();

    /**
     * 查询当前日期后14天工作日日期（包含当日）
     *
     * @return
     */
    Date shiftDateToGet();

    /**
     * 查询日期范围内的派工数量
     *
     * @param tenantId        租户
     * @param dateFrom        日期从
     * @param dateTo          日期至
     * @param calendarId      日历
     * @param workcellId      工段
     * @param workOrderIdList 工单列表
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWoCalendarShiftVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/2 03:04:45
     */
    List<HmeWoCalendarShiftVO> dispatchShiftDateQuery(@Param(value = "tenantId") Long tenantId,
                                                      @Param(value = "dateFrom") Date dateFrom,
                                                      @Param(value = "dateTo") Date dateTo,
                                                      @Param(value = "calendarId") String calendarId,
                                                      @Param(value = "workcellId") String workcellId,
                                                      @Param(value = "workOrderIdList") List<String> workOrderIdList);

    /**
     * 查询日期范围内的派工数量
     *
     * @param tenantId   租户
     * @param dateFrom   日期从
     * @param dateTo     日期至
     * @param calendarId 日历
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWoCalendarShiftVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/2 03:04:45
     */
    List<HmeWoCalendarShiftVO> selectCalendarShiftByDateRange(@Param(value = "tenantId") Long tenantId,
                                                              @Param(value = "dateFrom") Date dateFrom,
                                                              @Param(value = "dateTo") Date dateTo,
                                                              @Param(value = "calendarId") String calendarId);

    /**
     * 查询当前工段数据库派工总数量
     *
     * @param workOrderId
     * @param prodLineId
     * @param workcellId
     * @return
     */
    Double dispatchQtyGet(@Param(value = "workOrderId") String workOrderId,
                          @Param(value = "prodLineId") String prodLineId,
                          @Param(value = "workcellId") String workcellId,
                          @Param(value = "calendarShiftIds") List<String> calendarShiftIds);

    /**
     * 获取当前人员区域基础属性
     *
     * @param tenantId
     * @param userId
     * @param areaCategory
     * @return
     */
    List<MtModArea> userModAreaPropertyQuery(@Param(value = "tenantId") Long tenantId,
                                             @Param(value = "userId") Long userId,
                                             @Param(value = "areaCategory") String areaCategory);

    /**
     * 获取当前车间产线及产线对应工段
     *
     * @param tenantId   租户
     * @param prodLineId 产线
     * @return List<HmeWoDispatchVO4>
     */
    List<HmeWoDispatchVO4> prodLineWkcQuery(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "prodLineId") String prodLineId);

    /**
     * 获取工单信息
     *
     * @param tenantId
     * @param prodLineId
     * @param workCellId
     * @return
     */
    List<HmeWoDispatchVO5> woProdListQuery(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "prodLineId") String prodLineId,
                                           @Param(value = "workCellId") String workCellId);

    /**
     * 查询当前日期后14天工作日日历
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<HmeWoCalendarShiftVO> limitShiftDateQuery(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "vo") HmeWoCalendarShiftVO2 vo);

    /**
     * 工段完工数量查询
     *
     * @param tenantId    租户
     * @param workOrderId 工单ID
     * @param workcellId  工段ID
     * @return java.lang.Integer
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/15 06:36:35
     */
    BigDecimal workcellCompletionQtyQuery(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "workOrderId") String workOrderId,
                                          @Param(value = "workcellId") String workcellId);

    /**
     * 查询派工组件需求齐套信息列表
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWoDispatchComponentSuiteVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/14 11:18:00
     */
    List<HmeWoDispatchComponentSuiteVO> selectDispatchComponentSuiteList(@Param("tenantId") Long tenantId,
                                                                         @Param("dto") HmeWoDispatchCompSuiteQueryDTO dto);

    /**
     * 查询派工明细
     *
     * @param tenantId        租户
     * @param prodLineId      产线
     * @param topSiteId       顶层站点
     * @param userId          用户ID
     * @param workOrderIdList 工单列表
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWoDispatchDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/2 01:52:28
     */
    List<HmeWoDispatchWkcVO> selectDispatchDetailList(@Param("tenantId") Long tenantId,
                                                      @Param("topSiteId") String topSiteId,
                                                      @Param("prodLineId") String prodLineId,
                                                      @Param("userId") Long userId,
                                                      @Param("workOrderIdList") List<String> workOrderIdList);

}
