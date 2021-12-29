package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.HmeShiftVO10;
import com.ruike.hme.domain.vo.HmeShiftVO11;
import com.ruike.hme.domain.vo.HmeShiftVO4;
import com.ruike.hme.domain.vo.HmeShiftVO5;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 班组工作台Mapper
 *
 * @author chaonan.hu@hand-china.com 2020-07-28 16:20:45
 */
public interface HmeOpenEndShiftMapper {

    /**
     * 根据工段ID查询unitId
     *
     * @param organizationId 工段ID
     * @return java.lang.String
     */
    String getUnitByLineWorkcellId(@Param(value = "organizationId") String organizationId);

    /**
     * 实际出勤人数查询
     *
     * @param tenantId 租户ID
     * @param shiftDate 班次日期
     * @param shiftCode 班次编码
     * @param unitId 部门ID
     * @return java.lang.Long
     */
    Long actualAttendanceQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "shiftDate") String shiftDate,
                               @Param(value = "shiftCode") String shiftCode, @Param(value = "unitId") String unitId);

    /**
     * 班次数据查询
     *
     * @param tenantId 租户ID
     * @param workcellIdList 工位ID
     * @param wkcShiftId 班次日历ID
     * @param siteOutDateFrom 出站日期起
     * @param siteOutDateTo 出站日期至
     * @return java.util.List<com.ruike.hme.domain.vo.HmeShiftVO4>
     */
    List<HmeShiftVO4> shiftDataQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "workcellIdList") List<String> workcellIdList,
                                     @Param(value = "wkcShiftId") String wkcShiftId, @Param(value = "siteOutDateFrom") Date siteOutDateFrom,
                                     @Param(value = "siteOutDateTo") Date siteOutDateTo);

    /**
     * 员工上下岗时间查询
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param unitId 部门ID
     * @param workcellId 工位ID
     * @param shiftCode 班次编码
     * @param date 班次日期
     * @param operation 上下岗标识
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/31 09:21:29
     * @return java.util.Date
     */
    Date mountLaidDateQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "userId") String userId, @Param(value = "unitId") String unitId,
                            @Param(value = "workcellId") String workcellId, @Param(value = "shiftCode") String shiftCode, @Param(value = "date") String date,
                            @Param(value = "operation") String operation);

    /**
     * 查询物料批ID,用于查询产量
     * 
     * @param tenantId 租户ID
     * @param siteOutDateFrom 出站日期起
     * @param siteOutDateTo 出站日期至
     * @param siteOutBy 出站人
     * @param workcellId 工位
     * @return java.util.List<java.lang.String>
     */
    List<String> getMaterialLotId(@Param(value = "tenantId") Long tenantId, @Param(value = "siteOutDateFrom") Date siteOutDateFrom,
                                  @Param(value = "siteOutDateTo") Date siteOutDateTo, @Param(value = "siteOutBy") String siteOutBy,
                                  @Param(value = "workcellId") String workcellId);

    /**
     * 查询物料批ID,用于查询不良数
     *
     * @param tenantId 租户ID
     * @param dateTimeFrom NC记录时间起
     * @param dateTimeTo NC记录时间至
     * @param userId 记录人
     * @param workcellId 工位
     * @return java.util.List<java.lang.String>
     */
    List<String> getMaterialLotId2(@Param(value = "tenantId") Long tenantId, @Param(value = "dateTimeFrom") Date dateTimeFrom,
                                  @Param(value = "dateTimeTo") Date dateTimeTo, @Param(value = "userId") String userId,
                                   @Param(value = "workcellId") String workcellId);

    /**
     * 查询物料批ID,用于查询返修数
     *
     * @param tenantId 租户ID
     * @param siteOutDateFrom 出站日期起
     * @param siteOutDateTo 出站日期至
     * @param siteOutBy 出站人
     * @param workcellId 工位
     * @return java.util.List<java.lang.String>
     */
    List<String> getMaterialLotId3(@Param(value = "tenantId") Long tenantId, @Param(value = "siteOutDateFrom") Date siteOutDateFrom,
                                  @Param(value = "siteOutDateTo") Date siteOutDateTo, @Param(value = "siteOutBy") String siteOutBy,
                                   @Param(value = "workcellId") String workcellId);

    /**
     * 工序作业记录表数据查询
     * 
     * @param tenantId 租户ID
     * @param siteInDateFrom 进站日期起
     * @param siteInDateTo 进站日期至
     * @param workcellIdList 工位ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/29 11:45:23 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeShiftVO5>
     */
    List<HmeShiftVO5> eoJobSnDataQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "siteInDateFrom") Date siteInDateFrom,
                                       @Param(value = "siteInDateTo") Date siteInDateTo, @Param(value = "workcellIdList") List<String> workcellIdList);

    /**
     * 批量查询物料批ID，用于完工统计-本班投产、本班完成
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param workOrderId 工单ID
     * @param workcellIds 工位ID集合
     * @return java.util.List<java.lang.String>
     */
    List<String> getMaterialLotId4(@Param(value = "tenantId") Long tenantId, @Param(value = "materialId") String materialId,
                                  @Param(value = "workOrderId") String workOrderId, @Param(value = "workcellIds") List<String> workcellIds,
                                  @Param(value = "siteInDateFrom") Date siteInDateFrom, @Param(value = "siteInDateTo") Date siteInDateTo);

    /**
     * 批量查询物料批ID，用于完工统计-不良数
     *
     * @param tenantId 租户ID
     * @param dateTimeFrom NC记录时间起
     * @param dateTimeTo NC记录时间至
     * @param workcellIds 工位ID集合
     * @param eoIds eoId集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/29 14:49:40
     * @return java.util.List<java.lang.String>
     */
    List<String> getMaterialLotId5(@Param(value = "tenantId") Long tenantId, @Param(value = "dateTimeFrom") Date dateTimeFrom,
                                   @Param(value = "dateTimeTo") Date dateTimeTo, @Param(value = "workcellIds") List<String> workcellIds,
                                   @Param(value = "eoIds") List<String> eoIds);

    /**
     * 查询物料批ID，用于产品节拍
     *
     * @param tenantId 租户ID
     * @param siteInDateFrom 进站日期起
     * @param siteInDateTo 进站日期至
     * @param workcellIds 末道工序的工位集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/30 17:31:13
     * @return java.util.List<java.lang.String>
     */
    List<String> getMaterialLotId6(@Param(value = "tenantId") Long tenantId, @Param(value = "siteInDateFrom") Date siteInDateFrom,
                                   @Param(value = "siteInDateTo") Date siteInDateTo, @Param(value = "workcellIds") List<String> workcellIds);

    /**
     * 查询特定日期下发生的异常-用于人员安全
     * 
     * @param tenantId 租户ID
     * @param date 日期
     * @param workcellIds 工位ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/1 14:12:19
     * @return java.util.List<java.lang.String>
     */
    List<String> getExceptionIdList(@Param(value = "tenantId") Long tenantId, @Param(value = "date") String date,
                                    @Param(value = "workcellIds") List<String> workcellIds);
    
    /**
     * 查询特定日期下发生的异常-用于工艺质量
     * 
     * @param tenantId 租户ID
     * @param dateFrom 创建日期起
     * @param dateTo 创建日期至
     * @param workcellIds 工位ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/3 09:46:55 
     * @return java.util.List<java.lang.String>
     */
    List<String> getExceptionIdList2(@Param(value = "tenantId") Long tenantId, @Param(value = "dateFrom") Date dateFrom,
                                     @Param(value = "dateTo") Date dateTo, @Param(value = "workcellIds") List<String> workcellIds);

    /**
     * 设备管理
     * 
     * @param tenantId 租户ID
     * @param dateFrom 创建日期起
     * @param dateTo 创建日期至
     * @param workcellIds 工位ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/3 11:08:04 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeShiftVO10>
     */
    List<HmeShiftVO10> equipmenteExceptionQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dateFrom") Date dateFrom,
                                                @Param(value = "dateTo") Date dateTo, @Param(value = "workcellIds") List<String> workcellIds);

    /**
     * 其他异常
     * 
     * @param tenantId 租户ID
     * @param dateFrom 创建日期起
     * @param dateTo 创建日期至
     * @param workcellIds 工位ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/3 14:46:48
     * @return java.util.List<com.ruike.hme.domain.vo.HmeShiftVO11>
     */
    List<HmeShiftVO11> otherExceptionQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dateFrom") Date dateFrom,
                                           @Param(value = "dateTo") Date dateTo, @Param(value = "workcellIds") List<String> workcellIds);

    /**
     * 完工统计-完成数量查询
     * 
     * @param tenantId 租户ID
     * @param workOrderId 工单ID
     * @param wkcShiftId 班次ID
     * @param workcellId 工段ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/22 15:04:07
     * @return java.math.BigDecimal
     */
    BigDecimal getShiftComplete(@Param(value = "tenantId") Long tenantId, @Param(value = "workOrderId") String workOrderId,
                                @Param(value = "wkcShiftId") String wkcShiftId, @Param(value = "workcellId") String workcellId);

    /**
     * 完工统计-投产数量查询
     *
     * @param tenantId 租户ID
     * @param workOrderId 工单ID
     * @param workcellId 工段ID
     * @param calendarShiftId 班次分配ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/22 15:12:21
     * @return java.math.BigDecimal
     */
    String getShiftProduction(@Param(value = "tenantId") Long tenantId, @Param(value = "workOrderId") String workOrderId,
                                @Param(value = "workcellId") String workcellId, @Param(value = "calendarShiftId") String calendarShiftId);
}
