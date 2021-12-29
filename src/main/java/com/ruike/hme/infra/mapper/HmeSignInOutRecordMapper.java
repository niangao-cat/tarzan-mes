package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeEmployeeAssign;
import com.ruike.hme.domain.entity.HmeSignInOutRecord;
import com.ruike.hme.domain.vo.*;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModWorkcell;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 员工上下岗记录表Mapper
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-13 11:28:07
 */
public interface HmeSignInOutRecordMapper extends BaseMapper<HmeSignInOutRecord> {


	/**
	 * @param ：@param  tenantId
	 * @param ：@param  userId
	 * @param ：@return
	 * @return : HmeSignInOutRecordDTO1
	 * @Description: 查询信息包括岗位、工位
	 * @author: jianfeng xia
	 * @date 2020年7月13日 下午4:06:33
	 * @version 1.0
	 */
	HmeSignInOutRecordDTO1 getEemployeeQuery(@Param("tenantId") Long tenantId, @Param("userId") Long userId);

	/**
	 * @param ：@param  tenantId
	 * @param ：@param  employeeId
	 * @param ：@return
	 * @return : List<HmeSignInOutRecordDTO2>岗位list
	 * @Description: 查询岗位信息
	 * @author: jianfeng xia
	 * @date 2020年7月13日 下午4:45:20
	 * @version 1.0
	 */
	List<HmeSignInOutRecordDTO2> getUnitList(@Param("tenantId") Long tenantId, @Param("employeeId") Long employeeId);

	/**
	 * @param ：@param  userId
	 * @param ：@return
	 * @return : List<MtModWorkcell>
	 * @Description: 查询工位信息
	 * @author: jianfeng xia
	 * @date 2020年7月13日 下午7:45:38
	 * @version 1.0
	 */
	List<MtModWorkcell> getMtModWorkcellList(@Param("tenantId") Long tenantId, @Param("userId") Long userId);

	/**
	 * @param ：@param  tenantId 租户id
	 * @param ：@param  workcellId 工段id集合
	 * @param ：@param  qualityId 资质id集合
	 * @param ：@return
	 * @return : List<HmeSignInOutRecordDTO3>工位
	 * @Description: 查询工位信息
	 * @author: jianfeng xia
	 * @date 2020年7月14日 上午9:28:16
	 * @version 1.0
	 */
	List<HmeSignInOutRecordDTO3> findWorkcell(@Param("tenantId") Long tenantId, @Param("workcellIds") List<String> workcellIds, @Param("qualityIds") List<String> qualityIds);

	/**
	 * 查询工位对应的资质id
	 *
	 * @param tenantId      租户id
	 * @param workcellLists 工位id集合
	 * @return
	 */
	List<HmeSignInOutRecordDTO3> findWorkcellQualityId(@Param("tenantId") Long tenantId, @Param("workcellLists") List<String> workcellLists);

	/**
	 * @param ：@param  tenantId 租户
	 * @param ：@param  calendarId 日历id
	 * @param ：@param  choiceTime 选择的时间
	 * @param ：@return
	 * @return : List<MtCalendarShift> 班次
	 * @Description: 查询班次信息
	 * @author: jianfeng xia
	 * @date 2020年7月15日 上午11:13:41
	 * @version 1.0
	 */
	List<HmeSignInOutRecordDTO7> findShiftSodeList(@Param("tenantId") Long tenantId, @Param("calendarId") String calendarId,
                                                   @Param("choiceTime") String choiceTime);

	/**
	 * @param ：@param  tenantId 租户
	 * @param ：@param  calendarId 日历id
	 * @param ：@param  choiceTime 选择的时间
	 * @param ：@return
	 * @return : List<MtCalendarShift> 班次
	 * @Description: 查询班次信息
	 * @author: jianfeng xia
	 * @date 2020年7月15日 上午11:13:41
	 * @version 1.0
	 */
	List<HmeSignInOutRecordDTO7> findShiftSodeList2(@Param("tenantId") Long tenantId, @Param("calendarId") String calendarId,
                                                    @Param("choiceTime") String choiceTime, @Param("shiftCode") String shiftCode);

	/**
	 * @param ：@param  tenantId 租户id
	 * @param ：@param  workcellId 工位id
	 * @param ：@return
	 * @return : List<MtModSite>
	 * @Description: 根据工位id查询工厂信息
	 * @author: jianfeng xia
	 * @date 2020年7月15日 下午7:46:21
	 * @version 1.0
	 */
	List<MtModSite> getMtModSiteList(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId);

	/**
	 * @param ：@param  tenantId
	 * @param ：@param  hmeSignInOutRecordDTO
	 * @param ：@return
	 * @return : List<HmeSignInOutRecord>
	 * @Description: 根据条件查询员工上下岗记录
	 * @author: jianfeng xia
	 * @date 2020年7月16日 上午9:02:14
	 * @version 1.0
	 */
	List<HmeSignInOutRecord> getHmeSignInOutRecordList(@Param("tenantId") Long tenantId, @Param("vo") HmeSignInOutRecordDTO vo);

	/**
	 * @param ：@param  tenantId
	 * @param ：@param  employeeId
	 * @param ：@return
	 * @return : List<HmeEmployeeAssign>
	 * @Description:
	 * @author: jianfeng xia
	 * @date 2020年7月16日 下午8:51:18
	 * @version 1.0
	 */
	List<HmeEmployeeAssign> queryData(@Param("tenantId") Long tenantId, @Param("employeeId") Long employeeId);

	/**
	 * @param ：@param  tenantId
	 * @param ：@param  hmeSignInOutRecordDTO
	 * @param ：@return
	 * @return : List<HmeSignInOutRecord>
	 * @Description:
	 * @author: jianfeng xia
	 * @date 2020年7月18日 下午4:00:05
	 * @version 1.0
	 */
	List<HmeSignInOutRecord> getEachGroupMaxList(@Param("tenantId") Long tenantId, @Param("vo") HmeSignInOutRecordDTO hmeSignInOutRecordDTO);

	/**
	 * @param ：@param  tenantId 租户id
	 * @param ：@param  lovCode 值集编码
	 * @param ：@return
	 * @return : List<HpfmLovValueDTO>值集集合
	 * @Description: 查询值集值列表
	 * @author: jianfeng xia
	 * @date 2020年7月18日 下午3:57:55
	 * @version 1.0
	 */
    List<HpfmLovValueDTO> queryLovValues(@Param("tenantId") Long tenantId, @Param("lovCode") String lovCode);

	/**
	 * 根据条件查询员工分组数据
	 *
	 * @param tenantId              租户id
	 * @param hmeSignInOutRecordDTO
	 * @return List<HmeEmployeeAttendanceDto>员工岗位记录
	 * @author jianfeng.xia01@hand-china.com 2020/7/28 10:55
	 */
    List<HmeEmployeeAttendanceDto> findOneList(@Param("tenantId") Long tenantId, @Param("vo") HmeEmployeeAttendanceDto1 hmeSignInOutRecordDTO);

	/**
	 * 根据主键查询班组
	 *
	 * @param tenantId
	 * @param unitId
	 * @return [tenantId, unitId]
	 * @author jianfeng.xia01@hand-china.com 2020/7/28 16:51
	 */
	HmeSignInOutRecordDTO2 getUnitById(@Param("tenantId") Long tenantId, @Param("unitId") Long unitId);

	/**
	 * 统计标准考勤人数
	 *
	 * @param tenantId 租户id
	 * @param unitId   组织部门id
	 * @return [unitId]
	 * @author jianfeng.xia01@hand-china.com 2020/7/28 17:46
	 */
	Integer findEmployNumberCount(@Param("tenantId") Long tenantId, @Param("unitId") Long unitId);

	/**
	 * 统计出勤人数
	 *
	 * @param tenantId 租户id
	 * @param unitId   组织部门id
	 * @return [unitId]
	 * @author jianfeng.xia01@hand-china.com 2020/7/28 17:46
	 */
	Integer findAttendanceNumberCount(@Param("tenantId") Long tenantId, @Param("unitId") Long unitId);

	/**
	 * 根据工段查询班次时间
	 *
	 * @param tenantId
	 * @param workcell 工段id
	 * @param date     日历日期
	 * @return List<HmeEmployeeAttendanceDto2>
	 * @author jianfeng.xia01@hand-china.com 2020/7/28 20:26
	 */
	List<HmeEmployeeAttendanceDto2> findDefectsList(@Param("tenantId") Long tenantId, @Param("workcell") String workcell, @Param("date") String date);

	/**
	 * 通过工位时间统计不良数
	 *
	 * @param tenantId
	 * @param workcellId
	 * @param hmeEmployeeAttendanceDto2
	 * @return Integer
	 * @author jianfeng.xia01@hand-china.com 2020/7/29 8:51
	 */
    Integer findDefectsNumber(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId, @Param("vo") HmeEmployeeAttendanceDto2 hmeEmployeeAttendanceDto2);

	/**
	 * 查询班组长
	 *
	 * @param tenantId
	 * @param unitId
	 * @return [tenantId, unitId]
	 * @author jianfeng.xia01@hand-china.com 2020/7/29 9:13
	 */
	List<String> findMonitor(@Param("tenantId") Long tenantId, @Param("unitId") Long unitId);

	/**
	 * 查找员工考勤明细
	 *
	 * @param tenantId
	 * @param dto
	 * @return [tenantId, dto]
	 * @author jianfeng.xia01@hand-china.com 2020/7/29 9:51
	 */
    List<HmeEmployeeAttendanceRecordDto> findInfoList(@Param("tenantId") Long tenantId, @Param("vo") HmeEmployeeAttendanceDto1 dto);

	/**
	 * 查询班次日历
	 *
	 * @param tenantId   租户id
	 * @param calendarId 日历id
	 * @param dateTime   班次时间
	 * @param shiftCode  班次
	 * @return [tenantId, calendarId, dateTime, shiftCode]
	 * @author jianfeng.xia01@hand-china.com 2020/7/29 15:35
	 */
	List<HmeSignInOutRecordDTO7> findShiftList(@Param("tenantId") Long tenantId, @Param("calendarId") String calendarId, @Param("dateTime") String dateTime, @Param("shiftCode") String shiftCode);

	/**
	 * 头部数据查询 - 第一版逻辑 for lu.bai
	 *
	 * @param tenantId 租户ID
	 * @param dto      查询信息
	 * @return java.util.List<com.ruike.hme.api.dto.HmeEmployeeAttendanceDto>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/4 08:54:21
	 */
	List<HmeEmployeeAttendanceDto> headDataQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeEmployeeAttendanceDto1 dto,
                                                 @Param("workcellIdList") List<String> workcellIdList);

	/**
	 * 头部数据查询 - 第二版逻辑 for ni.xu
	 *
	 * @param tenantId 租户ID
	 * @param dto      查询信息
	 * @return java.util.List<com.ruike.hme.api.dto.HmeEmployeeAttendanceDto>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/4 08:54:21
	 */
	List<HmeEmployeeAttendanceDto> headDataQuery2(@Param("tenantId") Long tenantId, @Param("dto") HmeEmployeeAttendanceDto1 dto,
                                                  @Param("workcellIdList") List<String> workcellIdList);

	/**
	 * 查询分组下的relId数据--用于查询头数据
	 *
	 * @param tenantId   租户ID
	 * @param workcellId 工位ID
	 * @param unitId     班组Id
	 * @param shiftCode  班次编码
	 * @param date       班次日期
	 * @return java.util.List<java.lang.String>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/4 11:16:09
	 */
	List<String> getRelId(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                          @Param("unitId") Long unitId, @Param("shiftCode") String shiftCode,
                          @Param("date") Date date);

	/**
	 * 查询分组下的relId数据--用于查询头数据
	 *
	 * @param tenantId   租户ID
	 * @param workcellId 工位ID
	 * @param unitId     班组Id
	 * @param shiftCode  班次编码
	 * @param date       班次日期
	 * @return java.util.List<com.ruike.hme.api.dto.HmeEmployeeAttendanceRecordDto>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/4 15:46:09
	 */
	List<HmeEmployeeAttendanceRecordDto> getRelId2(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                                                   @Param("unitId") Long unitId, @Param("shiftCode") String shiftCode,
                                                   @Param("date") Date date);

	/**
	 * 查询指定relId下operationDate最大的那笔数据
	 *
	 * @param tenantId 租户ID
	 * @param relId    relId
	 * @return com.ruike.hme.domain.entity.HmeSignInOutRecord
	 * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/4 01:53:56
	 */
	HmeSignInOutRecord maxOperationDateQuery(@Param("tenantId") Long tenantId, @Param("relId") String relId);

	/**
	 * 查询物料批ID,用于查询不良数
	 *
	 * @param tenantId     租户ID
	 * @param dateTimeFrom NC记录时间起
	 * @param dateTimeTo   NC记录时间至
	 * @param workcellId   工位
	 * @return java.util.List<java.lang.String>
	 */
	List<String> getMaterialLotId(@Param(value = "tenantId") Long tenantId, @Param(value = "dateTimeFrom") Date dateTimeFrom,
                                  @Param(value = "dateTimeTo") Date dateTimeTo, @Param(value = "workcellId") String workcellId);

	/**
	 * 员工姓名、工号查询
	 *
	 * @param tenantId   租户ID
	 * @param employeeId 员工ID
	 * @return com.ruike.hme.api.dto.HmeEmployeeAttendanceDto6
	 * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/4 16:09:00
	 */
	HmeEmployeeAttendanceDto6 employeeInfoQuery(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "employeeId") String employeeId);

	/**
	 * 班次数据查询
	 *
	 * @param tenantId        租户ID
	 * @param workcellIdList  工位ID
	 * @param wkcShiftId      班次日历ID
	 * @param siteOutDateFrom 出站日期起
	 * @param siteOutDateTo   出站日期至
	 * @param siteId          站点ID
	 * @param workId          g工段ID
	 * @return java.util.List<com.ruike.hme.api.dto.HmeEmployeeAttendanceRecordDto>
	 */
	List<HmeEmployeeAttendanceRecordDto> shiftDataQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "workcellIdList") List<String> workcellIdList,
                                                        @Param(value = "wkcShiftId") String wkcShiftId, @Param(value = "siteOutDateFrom") Date siteOutDateFrom,
                                                        @Param(value = "siteOutDateTo") Date siteOutDateTo, @Param(value = "siteId") String siteId, @Param(value = "workId") String workId);

	/**
	 * 行表最小查询维度查询-工位、员工、物料
	 *
	 * @param tenantId       租户ID
	 * @param workcellIdList 工段下的所有工位
	 * @param wkcShiftId     班次
	 * @param dto            限制条件
	 * @return java.util.List<com.ruike.hme.api.dto.HmeEmployeeAttendanceRecordDto>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/12 09:44:46
	 */
	List<HmeEmployeeAttendanceRecordDto> shiftDataQueryNew(@Param(value = "tenantId") Long tenantId, @Param(value = "workcellIdList") List<String> workcellIdList,
														   @Param(value = "wkcShiftId") String wkcShiftId, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

	/**
	 * 批量查询物料批ID，用于头部数据查询-总产量
	 *
	 * @param tenantId       租户ID
	 * @param workcellIds    工位ID集合
	 * @param siteInDateFrom 日期起
	 * @param siteInDateTo   日期至
	 * @return java.util.List<java.lang.String>
	 */
	List<String> getMaterialLotId4(@Param(value = "tenantId") Long tenantId, @Param(value = "workcellIds") List<String> workcellIds,
                                   @Param(value = "siteInDateFrom") Date siteInDateFrom, @Param(value = "siteInDateTo") Date siteInDateTo);

	/**
	 * 批量查询物料批ID，用于行数据查询-产量
	 *
	 * @param tenantId       租户ID
	 * @param workcellId     工位ID
	 * @param siteInDateFrom 日期起
	 * @param siteInDateTo   日期至
	 * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO>
	 */
	List<HmeEmployeeAttendanceExportVO> getMaterialLotId5(@Param(value = "tenantId") Long tenantId, @Param(value = "workcellId") String workcellId,
														  @Param(value = "siteInDateFrom") Date siteInDateFrom, @Param(value = "siteInDateTo") Date siteInDateTo,
														  @Param(value = "employeeId") String employeeId);

	/**
     * 查询上次同一个工位存在未结班数据
     *
     * @param tenantId
     * @param employeeId
     * @param workcellId
     * @return
     */
    List<HmeEmployeeAttendanceDTO11> findOperation(@Param("tenantId") Long tenantId, @Param("employeeId") Long employeeId,
                                                   @Param("workcellId") String workcellId);


    /**
	 * @param tenantId
	 * @param userId
	 * @return java.util.List<com.ruike.hme.api.dto.HmeWorkCellDTO>
	 * @description 获取不需要资质的工位
	 * @author wenzhang.yu@hand-china.com
	 * @date 2020/8/14 20:11
	 **/
    List<HmeWorkCellDTO> getNoModWorkcellList(@Param("tenantId") Long tenantId, @Param("userId") Long userId);


    /**
	 * @param tenantId
	 * @return java.util.List<com.ruike.hme.api.dto.HmeWorkCellDTO>
	 * @description 获取工位资质需求
	 * @author wenzhang.yu@hand-china.com
	 * @date 2020/8/14 20:12
	 **/
	List<HmeWorkCellDTO> getModWorkcellList(@Param("tenantId") Long tenantId);


	List<HmeWorkCellDTO> getEmployeeList(@Param("tenantId") Long tenantId, @Param("employeeId") Long employeeId);

	/**
	 * 查询物料批Id-用于头数据查询不良数
	 *
	 * @param tenantId       租户ID
	 * @param workcellIdList 工位ID
	 * @param dateTimeFrom   开始时间
	 * @param dateTimeTo     截止时间
	 * @return java.util.List<java.lang.String>
	 */
	List<String> getMaterialLot(@Param("tenantId") Long tenantId, @Param("workcellIdList") List<String> workcellIdList,
								@Param(value = "dateTimeFrom") Date dateTimeFrom, @Param(value = "dateTimeTo") Date dateTimeTo);

	/**
	 * 查询物料批ID,用于行上查询不良数
	 *
	 * @param tenantId     租户ID
	 * @param dateTimeFrom NC记录时间起
	 * @param dateTimeTo   NC记录时间至
	 * @param userId       记录人
	 * @param workcellId   工位
	 * @return java.util.List<java.lang.String>
	 */
	List<HmeEmployeeAttendanceExportVO2> getMaterialLotId2(@Param(value = "tenantId") Long tenantId, @Param(value = "dateTimeFrom") Date dateTimeFrom,
														   @Param(value = "dateTimeTo") Date dateTimeTo, @Param(value = "userId") String userId,
														   @Param(value = "workcellId") String workcellId);

	/**
	 * 批量查询JobID，用于行数据查询-返修
	 *
	 * @param tenantId       租户ID
	 * @param workcellId     工位ID
	 * @param siteInDateFrom 日期起
	 * @param siteInDateTo   日期至
	 * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO>
	 */
	List<HmeEmployeeAttendanceExportVO> getRepairJobId(@Param(value = "tenantId") Long tenantId, @Param(value = "workcellId") String workcellId,
													   @Param(value = "siteInDateFrom") Date siteInDateFrom, @Param(value = "siteInDateTo") Date siteInDateTo,
													   @Param(value = "employeeId") String employeeId);

	/**
	 * job信息查询-用于行上产量明细、返修明细查询
	 *
	 * @param jobIdList
	 * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO3>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/21 03:50:49
	 */
	List<HmeEmployeeAttendanceExportVO3> jobDetailInfoQuery(@Param(value = "jobIdList") List<String> jobIdList);

	/**
	 * 不良信息查询-用于行上不良明细查询
	 *
	 * @param ncRecordIdList
	 * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO4>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/21 04:36:50
	 */
	List<HmeEmployeeAttendanceExportVO4> ncRecordInfoQuery(@Param(value = "ncRecordIdList") List<String> ncRecordIdList);

	/**
	 * 头部数据总产量取值逻辑
	 *
	 * @param tenantId   租户ID
	 * @param wkcShiftId 班次ID
	 * @param workcellId 工位ID
	 * @return java.math.BigDecimal
	 * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/22 02:34:13
	 */
	BigDecimal getCountNumber(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
							  @Param(value = "workcellId") String workcellId);

	/**
	 * 头部数据总产量取值逻辑 V20210311
	 *
	 * @param tenantId   租户ID
	 * @param wkcShiftId 班次ID
	 * @param workcellId 工段ID
	 * @param dto        查询条件
	 * @return java.math.BigDecimal
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/11 02:23:11
	 */
	BigDecimal getCountNumberNew(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
								 @Param(value = "workcellId") String workcellId, @Param(value = "dto") HmeEmployeeAttendanceDto1 dto);

	/**
	 * 头部数据实际产出取值逻辑 V20210311
	 *
	 * @param tenantId   租户ID
	 * @param wkcShiftId 班次ID
	 * @param workcellId 工段ID
	 * @param dto        查询条件
	 * @return java.math.BigDecimal
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/11 05:42:01
	 */
	BigDecimal getActualOutputNumber(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
									 @Param(value = "workcellId") String workcellId, @Param(value = "dto") HmeEmployeeAttendanceDto1 dto);

	/**
	 * 头部数据不良数取值逻辑 V20210311
	 *
	 * @param tenantId   租户ID
	 * @param wkcShiftId 班次ID
	 * @param workcellId 工段ID
	 * @param dto        查询条件
	 * @return java.math.BigDecimal
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/11 03:39:25
	 */
	BigDecimal defectNumberNew(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
							   @Param(value = "workcellId") String workcellId, @Param(value = "dto") HmeEmployeeAttendanceDto1 dto,
							   @Param("shiftStartDate") Date shiftStartDate, @Param("shiftEndDate") Date shiftEndDate);

	/**
	 * 根据工位名称模糊查询工位ID
	 *
	 * @param tenantId     租户ID
	 * @param workcellName 工位名称
	 * @return java.util.List<java.lang.String>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/18 10:22:12
	 */
	List<String> workcellNameLikeQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "workcellName") String workcellName);

	/**
	 * 行上在制数量查询
	 *
	 * @param tenantId       租户ID
	 * @param workcellId     工位ID
	 * @param siteInDateFrom 起始时间
	 * @param siteInDateTo   截止时间
	 * @param employeeId     员工ID
	 * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/18 10:41:15
	 */
	List<HmeEmployeeAttendanceExportVO> noSiteOutMaterialLotIdQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "workcellId") String workcellId,
														  @Param(value = "siteInDateFrom") Date siteInDateFrom, @Param(value = "siteInDateTo") Date siteInDateTo,
														  @Param(value = "employeeId") String employeeId);
	/**
	 * 员工出勤报表
	 *
	 * @param dto
	 * @param tenantId
	 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-05 14:49
	 * @return HmeSignInOutRecordVO4
	 */
    List<HmeSignInOutRecordVO4> listQuery(@Param(value = "dto") HmeSignInOutRecordDTO12 dto, @Param(value = "tenantId") Long tenantId);

	/**
	 * 根据工位查询工序
	 *
	 * @param tenantId   租户ID
	 * @param workcellId 工位ID
	 * @return tarzan.modeling.domain.entity.MtModWorkcell
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/12 10:16:24
	 */
	MtModWorkcell getProcessByWorkcell(@Param(value = "tenantId") Long tenantId, @Param(value = "workcellId") String workcellId);

	/**
	 * 行上数据产量取值逻辑
	 *
	 * @param tenantId   租户ID
	 * @param wkcShiftId 班次ID
	 * @param hejs       分组维度信息
	 * @param dto        限制条件
	 * @return java.math.BigDecimal
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/12 10:30:59
	 */
	BigDecimal lineMakeNumQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
								@Param(value = "hejs") HmeEmployeeAttendanceRecordDto hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

	/**
	 * 行上数据产量明细取值逻辑
	 *
	 * @param tenantId   租户ID
	 * @param wkcShiftId 班次ID
	 * @param hejs       分组维度信息
	 * @param dto        限制条件
	 * @return java.util.List<java.lang.String>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/12 10:30:59
	 */
	List<String> lineMakeNumDetailQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
										@Param(value = "hejs") HmeEmployeeAttendanceRecordDto hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

	/**
	 * 行上数据在制数取值逻辑
	 *
	 * @param tenantId   租户ID
	 * @param wkcShiftId 班次ID
	 * @param hejs       分组维度信息
	 * @param dto        限制条件
	 * @return java.math.BigDecimal
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/12 10:36:51
	 */
	BigDecimal inMakeNumQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
							  @Param(value = "hejs") HmeEmployeeAttendanceRecordDto hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

	/**
	 * 行上数据在制数明细取值逻辑
	 *
	 * @param tenantId   租户ID
	 * @param wkcShiftId 班次ID
	 * @param hejs       分组维度信息
	 * @param dto        限制条件
	 * @return java.util.List<java.lang.String>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/12 10:36:51
	 */
	List<String> inMakeNumDetailQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
									  @Param(value = "hejs") HmeEmployeeAttendanceRecordDto hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

	/**
	 * 行上数据不良数取值逻辑
	 *
	 * @param tenantId   租户ID
	 * @param wkcShiftId 班次ID
	 * @param hejs       分组维度信息
	 * @param dto        限制条件
	 * @return java.util.List<java.lang.String>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/12 10:45:23
	 */
	List<String> defectsNumbQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
								  @Param(value = "hejs") HmeEmployeeAttendanceRecordDto hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

	/**
	 * 行上数据返修数取值逻辑
	 *
	 * @param tenantId   租户ID
	 * @param wkcShiftId 班次ID
	 * @param hejs       分组维度信息
	 * @param dto        限制条件
	 * @return java.math.BigDecimal
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/12 11:05:55
	 */
	BigDecimal repairNumQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
							  @Param(value = "hejs") HmeEmployeeAttendanceRecordDto hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

	/**
	 * 行上数据返修数明细取值逻辑
	 *
	 * @param tenantId   租户ID
	 * @param wkcShiftId 班次ID
	 * @param hejs       分组维度信息
	 * @param dto        限制条件
	 * @return java.util.List<java.lang.String>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/12 11:05:55
	 */
	List<String> repairNumDetailQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
									  @Param(value = "hejs") HmeEmployeeAttendanceRecordDto hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

	/**
	 * 行上数据一次合格率-被除数取值逻辑
	 *
	 * @param tenantId   租户ID
	 * @param wkcShiftId 班次ID
	 * @param hejs       分组维度信息
	 * @param dto        限制条件
	 * @return java.math.BigDecimal
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/12 11:20:35
	 */
	BigDecimal eoWorkcellGroupQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
									@Param(value = "hejs") HmeEmployeeAttendanceRecordDto hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

	/**
	 * 行上数据一次合格率-除数取值逻辑
	 *
	 * @param tenantId   租户ID
	 * @param wkcShiftId 班次ID
	 * @param hejs       分组维度信息
	 * @param dto        限制条件
	 * @return java.math.BigDecimal
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/12 11:20:35
	 */
	List<String> eoWorkcellReworkFlagNGroupQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
												 @Param(value = "hejs") HmeEmployeeAttendanceRecordDto hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

	/**
	 * 行上数据一次合格率-除数取值逻辑
	 *
	 * @param tenantId   租户ID
	 * @param wkcShiftId 班次ID
	 * @param hejs       分组维度信息
	 * @param dto        限制条件
	 * @return java.math.BigDecimal
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/12 11:20:35
	 */
	List<String> eoWorkcellReworkFlagYGroupQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
												 @Param(value = "hejs") HmeEmployeeAttendanceRecordDto hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

	/**
	 * 根据产线查询工位
	 *
	 * @param tenantId   租户ID
	 * @param prodLineId 产线ID
	 * @return java.util.List<java.lang.String>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 11:04:04
	 */
	List<String> getWorkcellByProdLine(@Param(value = "tenantId") Long tenantId, @Param(value = "prodLineId") String prodLineId);

	/**
	 * 根据工序查询工位
	 *
	 * @param tenantId   租户ID
	 * @param processId  工序ID
	 * @return java.util.List<java.lang.String>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 11:04:04
	 */
	List<String> getWorkcellByProcess(@Param(value = "tenantId") Long tenantId, @Param(value = "processId") String processId);

	/**
	 * 根据工段查询工位
	 *
	 * @param tenantId   租户ID
	 * @param lineWorkcellId  工段ID
	 * @return java.util.List<java.lang.String>
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 11:04:04
	 */
	List<String> getWorkcellByLineWorkcellId(@Param(value = "tenantId") Long tenantId, @Param(value = "lineWorkcellId") String lineWorkcellId);

	/**
	 * 员工产量汇总报表-分组最小维度(员工、工序、物料、物料版本)确定
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 11:30:39
	 * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO5>
	 */
	List<HmeEmployeeAttendanceExportVO5> sumQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceDTO13 dto);

	/**
	 * 根据工序查询工段
	 *
	 * @param tenantId 租户ID
	 * @param processId 工序ID
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 01:53:33
	 * @return tarzan.modeling.domain.entity.MtModWorkcell
	 */
	MtModWorkcell getLineWorkcellByProcess(@Param(value = "tenantId") Long tenantId, @Param(value = "processId") String processId);

	/**
	 * 根据工段查询产线
	 *
	 * @param tenantId 租户ID
	 * @param lineWorkcellId 工段ID
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/12 04:31:29
	 * @return tarzan.modeling.domain.entity.MtModProductionLine
	 */
	MtModProductionLine getProdLineByLineWorkcell(@Param(value = "tenantId") Long tenantId, @Param(value = "lineWorkcellId") String lineWorkcellId);

	/**
	 * 员工产量汇总报表-实际产出查询
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 02:15:22
	 * @return java.math.BigDecimal
	 */
	BigDecimal getSumActualOutputNumber(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceExportVO5 dto);

	/**
	 * 员工产量汇总报表-产量查询
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 02:26:25
	 * @return java.math.BigDecimal
	 */
	BigDecimal getSumCountNumber(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceExportVO5 dto);

	/**
	 * 员工产量汇总报表-在制数查询
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 02:34:04
	 * @return java.math.BigDecimal
	 */
	BigDecimal getSumInMakeNum(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceExportVO5 dto);

	/**
	 * 员工产量汇总报表-不良数查询
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @param dateFrom 起始日期
	 * @param dateTo 截止日期
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 02:40:19
	 * @return java.math.BigDecimal
	 */
	BigDecimal getSumDefectsNumb(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceExportVO5 dto,
								 @Param(value = "dateFrom") Date dateFrom, @Param(value = "dateTo") Date dateTo);

	/**
	 * 员工产量汇总报表-返修数查询
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 02:47:17
	 * @return java.math.BigDecimal
	 */
	BigDecimal getSumRepairNum(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceExportVO5 dto);

	/**
	 * 员工产量汇总报表-一次合格率被除数查询
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 02:52:27
	 * @return java.math.BigDecimal
	 */
	BigDecimal getSumEoWorkcellGroup(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceExportVO5 dto);

	/**
	 * 员工产量汇总报表-一次合格率除数查询
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 02:57:15
	 * @return java.util.List<java.lang.String>
	 */
	List<String> sumEoWorkcellReworkFlagNGroupQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceExportVO5 dto);

	/**
	 * 员工产量汇总报表-一次合格率除数查询
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 03:00:13
	 * @return java.util.List<java.lang.String>
	 */
	List<String> sumEoWorkcellReworkFlagYGroupQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceExportVO5 dto);

	/**
	 * 员工产量汇总报表-生产总时长查询
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 03:24:18
	 * @return java.math.BigDecimal
	 */
	BigDecimal getTotalProductionTime(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceExportVO5 dto);

	/**
	 * 员工产量汇总报表-实际产出明细查询
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/19 09:26:50
	 * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO3>
	 */
	List<HmeEmployeeAttendanceExportVO3> sumActualOutputNumberQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceDTO14 dto);

	/**
	 * 员工产量汇总报表-产量明细查询
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/19 09:40:30
	 * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO3>
	 */
	List<HmeEmployeeAttendanceExportVO3> sumCountNumberQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceDTO14 dto);

	/**
	 * 员工产量汇总报表-在制数明细查询
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/19 09:44:24
	 * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO3>
	 */
	List<HmeEmployeeAttendanceExportVO3> sumInMakeNumQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceDTO14 dto);

	/**
	 * 员工产量汇总报表-返修数明细查询
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/19 09:47:32
	 * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO3>
	 */
	List<HmeEmployeeAttendanceExportVO3> sumRepairNumQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceDTO14 dto);

	/**
	 * 员工产量汇总报表-不良数明细查询
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/19 09:51:58
	 * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO4>
	 */
	List<HmeEmployeeAttendanceExportVO4> sumDefectsNumQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceDTO14 dto);

	/**
	 * 总产量弹框
	 * @param tenantId
	 * @param dto
	 * @return
	 */
	List<HmeEmployeeAttendanceExportVO3> lineWorkcellProductDetails(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceDTO15 dto);

	/**
	 * 不良数弹框
	 * @param tenantId
	 * @param dto
	 * @return
	 */
	List<HmeEmployeeAttendanceExportVO4> lineWorkcellNcDetails(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceDTO15 dto);
}
