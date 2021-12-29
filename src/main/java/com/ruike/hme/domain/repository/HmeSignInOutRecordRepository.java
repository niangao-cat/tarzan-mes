package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeEmployeeAssign;
import com.ruike.hme.domain.entity.HmeSignInOutRecord;
import com.ruike.hme.domain.vo.HmeSignInOutRecordVO4;
import org.hzero.mybatis.base.BaseRepository;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModWorkcell;

import java.util.List;

/**
 * 员工上下岗记录表资源库
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-13 11:28:07
 */
public interface HmeSignInOutRecordRepository extends BaseRepository<HmeSignInOutRecord> {

	/**
	 * @Description: 
	 * @author: jianfeng xia
	 * @date 2020年7月13日 下午4:04:17
	 * @param ： tenantId 租户id
	 * @param ： userId 用户id
	 * @return : HmeSignInOutRecordDTO1 员工id及名称
	 * @version 1.0
	 */
	HmeSignInOutRecordDTO1 getEemployeeQuery(Long tenantId, Long userId);

	/**
	 * @Description: 根据员工id查询岗位集合
	 * @author: jianfeng xia
	 * @date 2020年7月13日 下午4:42:06
	 * @param ： tenantId 租户id
	 * @param ： employeeId 员工ID
	 * @return : HmeSignInOutRecordDTO2 岗位list
	 * @version 1.0
	 */
	List<HmeSignInOutRecordDTO2> getUnitList(Long tenantId, Long employeeId);

	/**
	 * @Description: 根据用户id查询可操作的工段集合
	 * @author: jianfeng xia
	 * @date 2020年7月13日 下午7:42:25
	 * @param ： tenantId 租户id
	 * @param ： userId 用户id
	 * @return : List<MtModWorkcell>工段列表
	 * @version 1.0
	 */
	List<MtModWorkcell> getMtModWorkcellList(Long tenantId, Long userId);

	/**
	 * @Description: 
	 * @author: jianfeng xia
	 * @date 2020年7月14日 上午9:14:34
	 * @param ：@param tenantId 租户id
	 * @param ：@param workcellIds 工段id集合
	 * @param ：@param qualityIds 资质id集合
	 * @return : void
	 * @version 1.0
	 */
	List<HmeSignInOutRecordDTO3> findWorkcell(Long tenantId, List<String> workcellIds, List<String> qualityIds);

	/**
	 * 查询工位下面的资质id
	 * @param tenantId  租户id
	 * @param workcellLists 工位id集合
	 * @return
	 */
	List<HmeSignInOutRecordDTO3> findWorkcellQualityId(Long tenantId, List<String> workcellLists);

	/**
	 * @Description: 根据日历id及选择的时间查询班次信息
	 * @author: jianfeng xia
	 * @date 2020年7月15日 上午11:11:47
	 * @param ：@param tenantId 租户id
	 * @param ：@param calendarId 日历id
	 * @param ：@param choiceTime 班次日期
	 * @param ：@param shiftCode 班次编码
	 * @param ：@return
	 * @return : List<HmeSignInOutRecordDTO7>班次信息
	 * @version 1.0
	 */
	List<HmeSignInOutRecordDTO7>findShiftSodeList(Long tenantId, String calendarId, String choiceTime);

	/**
	 * @Description: 根据工位id查询工厂信息
	 * @author: jianfeng xia
	 * @date 2020年7月15日 下午7:42:46
	 * @param ：@param tenantId 租户id
	 * @param ：@param workcellId 工位id
	 * @param ：@return
	 * @return : List<MtModSite>
	 * @version 1.0
	 */
	List<MtModSite> getMtModSiteList(Long tenantId, String workcellId);

	/**
	 * @Description: 根据条件查询上下岗记录
	 * @author: jianfeng xia
	 * @date 2020年7月16日 上午8:59:11
	 * @param ：@param tenantId
	 * @param ：@param hmeSignInOutRecordDTO
	 * @param ：@return
	 * @return : List<HmeSignInOutRecord>上下岗记录
	 * @version 1.0
	 */
	List<HmeSignInOutRecord> getHmeSignInOutRecordList(Long tenantId, HmeSignInOutRecordDTO hmeSignInOutRecordDTO);

	/**
	 * @Description: 
	 * @author: jianfeng xia
	 * @date 2020年7月16日 下午8:49:29
	 * @param ：@param tenantId
	 * @param ：@param employeeId
	 * @param ：@return
	 * @return : List<HmeEmployeeAssign>
	 * @version 1.0
	 */
	List<HmeEmployeeAssign> queryData(Long tenantId, Long long1);

	/**
	 * @Description: 根据条件查询每组最近的记录
	 * @author: jianfeng xia
	 * @date 2020年7月18日 下午3:57:55
	 * @param ：@param tenantId
	 * @param ：@param hmeSignInOutRecordDTO
	 * @param ：@return
	 * @return : List<HmeSignInOutRecord>
	 * @version 1.0
	 */
	List<HmeSignInOutRecord> getEachGroupMaxList(Long tenantId, HmeSignInOutRecordDTO hmeSignInOutRecordDTO);


	/**
	 * @Description: 查询值集值列表
	 * @author: jianfeng xia
	 * @date 2020年7月18日 下午3:57:55
	 * @param ：@param tenantId 租户id
	 * @param ：@param lovCode 值集编码
	 * @param ：@return
	 * @return : List<HpfmLovValueDTO>值集集合
	 * @version 1.0
	 */
    List<HpfmLovValueDTO> queryLovValues(Long tenantId, String lovCode);

	/**
	 * 根据条件查询员工分组数据
	 * @param tenantId  租户id
	 * @param hmeSignInOutRecordDTO
	 * @author jianfeng.xia01@hand-china.com 2020/7/28 10:55
	 * @return List<HmeEmployeeAttendanceDto>员工岗位记录
	 */
	List<HmeEmployeeAttendanceDto> findOneList(Long tenantId, HmeEmployeeAttendanceDto1 hmeSignInOutRecordDTO);
	/**
	 * 根据主键查询班组
	 * @param tenantId
	 * @param unitId 
	 * @author jianfeng.xia01@hand-china.com 2020/7/28 16:51
	 * @return [tenantId, unitId]
	 */
	HmeSignInOutRecordDTO2 getUnitById(Long tenantId, Long unitId);
	/**
	 * 统计标准考勤人数
	 * @param tenantId  租户id
	 * @param unitId  组织部门id
	 * @author jianfeng.xia01@hand-china.com 2020/7/28 17:46
	 * @return [unitId]
	 */
	Integer findEmployNumberCount(Long tenantId, Long unitId);
	/**
	 * 统计出勤人数
	 * @param tenantId  租户id
	 * @param unitId  组织部门id
	 * @author jianfeng.xia01@hand-china.com 2020/7/28 17:46
	 * @return [unitId]
	 */
	Integer findAttendanceNumberCount(Long tenantId, Long unitId);

	/**
	 * 根据工段查询班次时间
	 * @param tenantId
	 * @param workcell 工段id
	 * @author jianfeng.xia01@hand-china.com 2020/7/28 20:26
	 * @return List<HmeEmployeeAttendanceDto2>
	 */
	List<HmeEmployeeAttendanceDto2> findDefectsList(Long tenantId, String workcell, String date);
	/**
	 * 通过工位时间统计不良数
	 * @param tenantId
	 * @param workcellId
	 * @param hmeEmployeeAttendanceDto2 
	 * @author jianfeng.xia01@hand-china.com 2020/7/29 8:51
	 * @return [tenantId, workcellId, hmeEmployeeAttendanceDto2]
	 */
	Integer findDefectsNumber(Long tenantId, String workcellId, HmeEmployeeAttendanceDto2 hmeEmployeeAttendanceDto2);
	/**
	 * 查询班组长
	 * @param tenantId
	 * @param unitId
	 * @author jianfeng.xia01@hand-china.com 2020/7/29 9:13
	 * @return [tenantId, unitId]
	 */
	List<String> findMonitor(Long tenantId, Long unitId);
	/**
	 * 查找员工考勤明细
	 * @param tenantId
	 * @param dto
	 * @author jianfeng.xia01@hand-china.com 2020/7/29 9:51
	 * @return [tenantId, dto]
	 */
	List<HmeEmployeeAttendanceRecordDto> findInfoList(Long tenantId, HmeEmployeeAttendanceDto1 dto);
	/**
	 *查询班次日历
	 * @param tenantId 租户id
	 * @param calendarId 日历id
	 * @param dateTime 班次时间
	 * @param shiftCode 班次
	 * @author jianfeng.xia01@hand-china.com 2020/7/29 15:35
	 * @return [tenantId, calendarId, dateTime, shiftCode]
	 */
    List<HmeSignInOutRecordDTO7> findShiftList(Long tenantId, String calendarId, String dateTime, String shiftCode);

	/**
	 * 查询上次同一个工位存在未结班数据
	 * @param tenantId
	 * @param employeeId
	 * @param workcellId
	 * @return
	 */
	List<HmeEmployeeAttendanceDTO11> findOperation(Long tenantId, Long employeeId, String workcellId);


	/**
	 *@description 获取不需要资质的工位
	 *@author wenzhang.yu@hand-china.com
	 *@date 2020/8/14 20:10
	 *@param tenantId
	 *@param userId
	 *@return java.util.List<com.ruike.hme.api.dto.HmeWorkCellDTO>
	 **/
	List<HmeWorkCellDTO> getNoModWorkcellList(Long tenantId, Long userId);


	/**
	 *@description 获取工位资质需求
	 *@author wenzhang.yu@hand-china.com
	 *@date 2020/8/14 20:11
	 *@param tenantId
	 *@return java.util.List<com.ruike.hme.api.dto.HmeWorkCellDTO>
	 **/
	List<HmeWorkCellDTO> getModWorkcellList(Long tenantId);


	/**
	 *@description 获取人员资质
	 *@author wenzhang.yu@hand-china.com
	 *@date 2020/8/14 20:11
	 *@param tenantId
	 *@param employeeId
	 *@return java.util.List<com.ruike.hme.api.dto.HmeWorkCellDTO>
	 **/
	List<HmeWorkCellDTO> getEmployeeList(Long tenantId, Long employeeId);

	/**
	 * 
	 *
	 * @param tenantId
	 * @param dto
	 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-16 15:53
	 * @return HmeSignInOutRecordVO4
	 */
    List<HmeSignInOutRecordVO4> listQuery(HmeSignInOutRecordDTO12 dto, Long tenantId);
}
