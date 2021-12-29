package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeEmployeeAssign;
import com.ruike.hme.domain.entity.HmeSignInOutRecord;
import com.ruike.hme.domain.repository.HmeSignInOutRecordRepository;
import com.ruike.hme.domain.vo.HmeSignInOutRecordVO4;
import com.ruike.hme.infra.mapper.HmeSignInOutRecordMapper;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModWorkcell;

import java.util.List;

/**
 * 员工上下岗记录表 资源库实现
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-13 11:28:07
 */
@Component
public class HmeSignInOutRecordRepositoryImpl extends BaseRepositoryImpl<HmeSignInOutRecord> implements HmeSignInOutRecordRepository {
	
	@Autowired
	private HmeSignInOutRecordMapper hmeSignInOutRecordMapper;
	
	@Override
	public HmeSignInOutRecordDTO1 getEemployeeQuery(Long tenantId, Long userId) {
		return hmeSignInOutRecordMapper.getEemployeeQuery(tenantId,userId);
	}

	@Override
	public List<HmeSignInOutRecordDTO2> getUnitList(Long tenantId, Long employeeId) {
		return hmeSignInOutRecordMapper.getUnitList(tenantId,employeeId);
	}

	@Override
	public List<MtModWorkcell> getMtModWorkcellList(Long tenantId,Long userId) {
		return hmeSignInOutRecordMapper.getMtModWorkcellList(tenantId,userId);
	}

	@Override
	public List<HmeSignInOutRecordDTO3> findWorkcell(Long tenantId, List<String> workcellIds, List<String> qualityIds) {
		return hmeSignInOutRecordMapper.findWorkcell(tenantId,workcellIds,qualityIds);
	}

	@Override
	public List<HmeSignInOutRecordDTO3> findWorkcellQualityId(Long tenantId, List<String> workcellLists) {
		return hmeSignInOutRecordMapper.findWorkcellQualityId(tenantId, workcellLists);
	}

	@Override
	public List<HmeSignInOutRecordDTO7> findShiftSodeList(Long tenantId, String calendarId, String choiceTime) {
		return hmeSignInOutRecordMapper.findShiftSodeList(tenantId,calendarId,choiceTime);
	}

	@Override
	public List<MtModSite> getMtModSiteList(Long tenantId, String workcellId) {
		return hmeSignInOutRecordMapper.getMtModSiteList(tenantId,workcellId);
	}

	@Override
	public List<HmeSignInOutRecord> getHmeSignInOutRecordList(Long tenantId,
			HmeSignInOutRecordDTO hmeSignInOutRecordDTO) {
		return hmeSignInOutRecordMapper.getHmeSignInOutRecordList(tenantId,hmeSignInOutRecordDTO);
	}

	@Override
	public List<HmeEmployeeAssign> queryData(Long tenantId, Long employeeId) {
		return hmeSignInOutRecordMapper.queryData(tenantId, employeeId);
	}

	@Override
	public List<HmeSignInOutRecord> getEachGroupMaxList(Long tenantId, HmeSignInOutRecordDTO hmeSignInOutRecordDTO) {
		return hmeSignInOutRecordMapper.getEachGroupMaxList(tenantId, hmeSignInOutRecordDTO);
	}

	@Override
	public List<HpfmLovValueDTO> queryLovValues(Long tenantId, String lovCode) {
		return hmeSignInOutRecordMapper.queryLovValues(tenantId, lovCode);
	}

	@Override
	public List<HmeEmployeeAttendanceDto> findOneList(Long tenantId, HmeEmployeeAttendanceDto1 hmeSignInOutRecordDTO) {
		return hmeSignInOutRecordMapper.findOneList(tenantId, hmeSignInOutRecordDTO);
	}

	@Override
	public HmeSignInOutRecordDTO2 getUnitById(Long tenantId, Long unitId) {
		return hmeSignInOutRecordMapper.getUnitById(tenantId, unitId) ;
	}

	@Override
	public Integer findEmployNumberCount(Long tenantId,Long unitId) {
		return hmeSignInOutRecordMapper.findEmployNumberCount(tenantId,unitId);
	}

	@Override
	public Integer findAttendanceNumberCount(Long tenantId,Long unitId) {
		return hmeSignInOutRecordMapper.findAttendanceNumberCount(tenantId,unitId);
	}

	@Override
	public List<HmeEmployeeAttendanceDto2> findDefectsList(Long tenantId, String workcell,String date) {
		return hmeSignInOutRecordMapper.findDefectsList(tenantId,workcell,date);
	}

	@Override
	public Integer findDefectsNumber(Long tenantId, String workcellId, HmeEmployeeAttendanceDto2 hmeEmployeeAttendanceDto2) {
		return hmeSignInOutRecordMapper.findDefectsNumber(tenantId, workcellId, hmeEmployeeAttendanceDto2);
	}

	@Override
	public List<String> findMonitor(Long tenantId, Long unitId) {
		return hmeSignInOutRecordMapper.findMonitor(tenantId,unitId);
	}

	@Override
	public List<HmeEmployeeAttendanceRecordDto> findInfoList(Long tenantId, HmeEmployeeAttendanceDto1 dto) {
		return hmeSignInOutRecordMapper.findInfoList(tenantId, dto);
	}

	@Override
	public List<HmeSignInOutRecordDTO7> findShiftList(Long tenantId, String calendarId, String dateTime, String shiftCode) {
		return hmeSignInOutRecordMapper.findShiftList(tenantId, calendarId,dateTime, shiftCode);
	}

	@Override
	public List<HmeEmployeeAttendanceDTO11> findOperation(Long tenantId, Long employeeId, String workcellId) {
		return hmeSignInOutRecordMapper.findOperation(tenantId,employeeId,workcellId);
	}

	/**
	 *@description 获取不需要资质的工位
	 *@author wenzhang.yu@hand-china.com
	 *@date 2020/8/14 20:10
	 *@param tenantId
	 *@param userId
	 *@return java.util.List<com.ruike.hme.api.dto.HmeWorkCellDTO>
	 **/
    @Override
    public List<HmeWorkCellDTO> getNoModWorkcellList(Long tenantId, Long userId) {

		return 	 hmeSignInOutRecordMapper.getNoModWorkcellList(tenantId,userId);
    }

    /**
     *@description 获取工位资质需求
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/14 20:12
     *@param tenantId
     *@return java.util.List<com.ruike.hme.api.dto.HmeWorkCellDTO>
     **/
	@Override
	public List<HmeWorkCellDTO> getModWorkcellList(Long tenantId) {
		return hmeSignInOutRecordMapper.getModWorkcellList(tenantId);
	}

	/**
	 *@description 获取人员资质
	 *@author wenzhang.yu@hand-china.com
	 *@date 2020/8/14 20:13
	 *@param tenantId
	 *@param employeeId
	 *@return java.util.List<com.ruike.hme.api.dto.HmeWorkCellDTO>
	 **/
	@Override
	public List<HmeWorkCellDTO> getEmployeeList(Long tenantId, Long employeeId) {
		return hmeSignInOutRecordMapper.getEmployeeList(tenantId,employeeId);
	}

	@Override
	@ProcessLovValue
	public List<HmeSignInOutRecordVO4> listQuery(HmeSignInOutRecordDTO12 dto, Long tenantId) {
		return hmeSignInOutRecordMapper.listQuery(dto, tenantId);
	}


}
