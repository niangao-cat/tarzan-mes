package com.ruike.hme.app.service;

import java.util.List;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeSignInOutRecord;

import com.ruike.hme.domain.vo.HmeSignInOutRecordVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 员工上下岗记录表应用服务
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-13 11:28:07
 */
public interface HmeSignInOutRecordService {

	/**
     * @param ：@param  tenantId 租户id
     * @param ：@param  hmeSignInOutRecordDTO 用户+员工 +岗位+工位+日期 +班次
     * @param ：@return
     * @return : List<HmeSignInOutRecord>员工上下岗记录列表
     * @Description: 根据条件查询上下岗记录列表
     * @author: jianfeng xia
     * @date 2020年7月13日 下午2:33:39
     * @version 1.0
     */
	Page<HmeSignInOutRecordDTO8> hmeSignInOutRecordListQuery(Long tenantId, HmeSignInOutRecordDTO9 hmeSignInOutRecordDto9, PageRequest pageRequest);

	/**
	 * @Description: 根据用户id查询用户信息
	 * @author: jianfeng xia
	 * @date 2020年7月13日 下午3:19:13
	 * @param ：@tenantId 租户id
	 * @param ：@userId 用户id
	 * @param ：@return HmeSignInOutRecordDTO1 用户相关的信息
	 * @return : Object
	 * @version 1.0
	 */
	HmeSignInOutRecordDTO1 getUserQuery(Long tenantId, Long userId);

	/**
	 * @Description: 根据条件查询考勤集合
	 * @author: jianfeng xia
	 * @date 2020年7月14日 上午11:14:04
	 * @param ：@param tenantId 租户id
	 * @param ：@param userId 用户id
	 * @param ：@param employeeId 岗位id
	 * @param ：@param workcellId 工位id
	 * @param : @param year 年份
	 * @param : @param month 月份
	 * @param ：@return
	 * @return : List<HmeSignInOutRecordDTO4>月份的考勤集合
	 * @version 1.0
	 */
	List<HmeSignInOutRecordDTO4> getUserAttendanceQuery(Long tenantId, HmeSignInOutRecordDTO10 dto);

	/**
	 * @Description: 根据条件查询班次信息
	 * @author: jianfeng xia
	 * @date 2020年7月15日 上午9:07:18
	 * @param ：@param tenantId
	  * @param ：@param tenantId 租户id
	 * @param ：@param userId 用户id
	 * @param ：@param employeeId 岗位id
	 * @param ：@param workcellId 工位id
	 * @param : @param year 年份
	 * @param : @param month 月份
	 * @param : @param day 选择的天数
	 * @return : List<HmeSignInOutRecordDTO6> 上下岗班次信息
	 * @version 1.0
	 */
	List<HmeSignInOutRecordDTO6> getUserFrequencyQuery(Long tenantId, HmeSignInOutRecordDTO5 dto);

	/**
	 * @Description: 添加员工上下岗记录
	 * @author: jianfeng xia
	 * @date 2020年7月15日 下午5:26:19
	 * @param ：@param tenantId
	 * @param ：@param hmeSignInOutRecord
	 * @param ：@return
	 * @return : Object
	 * @version 1.0
	 */
	HmeSignInOutRecord creat(Long tenantId,HmeSignInOutRecord hmeSignInOutRecord);

	/**
	 * 员工出勤报表
	 *
	 * @param tenantId
	 * @param dto
	 * @param pageRequest
	 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-05 14:41
	 * @return HmeSignInOutRecordVO
	 */
	Page<HmeSignInOutRecordVO> listQuery(Long tenantId, HmeSignInOutRecordDTO12 dto, PageRequest pageRequest);
}
