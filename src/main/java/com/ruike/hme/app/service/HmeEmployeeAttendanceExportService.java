package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO3;
import com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO4;
import com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO5;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
* @Classname HmeEmployeeAttendanceExportService
* @Description 员工考勤报表
* @Date  2020/7/27 16:57
* @Created by Jianfeng xia
*/
public interface HmeEmployeeAttendanceExportService {

    /**
     * 根据条件查询第一个表数据集
     * @param tenantId 租户id
     * @param dto
     * @author jianfeng.xia01@hand-china.com 2020/7/27 17:13
     * @return [tenantId, dto]
     */
    Page<HmeEmployeeAttendanceDto> headDataQuery(Long tenantId, HmeEmployeeAttendanceDto1 dto, PageRequest pageRequest);

    /**
     * 查找员工考勤明细
     * @param tenantId 租户ID
     * @param dto 头数据信息
     * @pageRequest pageRequest 分页信息
     * @author jianfeng.xia01@hand-china.com 2020/7/29 9:51
     * @return [tenantId, dto]
     */
    Page<HmeEmployeeAttendanceRecordDto> lineDataQuery(Long tenantId, HmeEmployeeAttendanceDto5 dto, PageRequest pageRequest);

    /**
     * 产线LOV-限定车间
     *
     * @param tenantId 租户ID
     * @param dto　查询信息
     * @param pageRequest　分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/5 09:21:41
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeEmployeeAttendanceDto8>
     */
    Page<HmeEmployeeAttendanceDto8> prodLineLovQuery(Long tenantId, HmeEmployeeAttendanceDto7 dto, PageRequest pageRequest);

    /**
     * 工段Lov-限定产线
     *
     * @param tenantId 租户Id
     * @param dto 查询信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/5 10:07:51
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeEmployeeAttendanceDto10>
     */
    Page<HmeEmployeeAttendanceDto10> processLovQuery(Long tenantId, HmeEmployeeAttendanceDto9 dto, PageRequest pageRequest);

    /**
     * job信息查询-用于行上产量明细、返修明细查询
     *
     * @param tenantId
     * @param jobIdList
     * @param pageRequest
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/21 03:57:51
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO3>
     */
    Page<HmeEmployeeAttendanceExportVO3> jobDetailInfoQuery(Long tenantId, List<String> jobIdList, PageRequest pageRequest);

    /**
     * 不良信息查询-用于行上不良明细查询
     *
     * @param tenantId
     * @param ncRecordIdList
     * @param pageRequest
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/21 04:36:50
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO4>
     */
    Page<HmeEmployeeAttendanceExportVO4> ncRecordInfoQuery(Long tenantId, List<String> ncRecordIdList, PageRequest pageRequest);

    /**
     * 员工产量汇总报表分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 10:34:25
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO5>
     */
    Page<HmeEmployeeAttendanceExportVO5> sumQuery(Long tenantId, HmeEmployeeAttendanceDTO13 dto, PageRequest pageRequest);
    
    /**
     * 员工产量汇总报表实际产出、产量、在制数、返修数明细查询
     * 
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/19 09:59:08 
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO3>
     */
    Page<HmeEmployeeAttendanceExportVO3> sumNumberDeatilQuery(Long tenantId, HmeEmployeeAttendanceDTO14 dto, PageRequest pageRequest);

    /**
     * 员工产量汇总报表不良数明细查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/19 10:10:37
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO4>
     */
    Page<HmeEmployeeAttendanceExportVO4> sumDefectsNumQuery(Long tenantId, HmeEmployeeAttendanceDTO14 dto, PageRequest pageRequest);

    /**
     * 员工产量汇总导出
     * 
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/12 04:42:43 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO5>
     */
    List<HmeEmployeeAttendanceExportVO5> sumExport(Long tenantId, HmeEmployeeAttendanceDTO13 dto);

    /**
     * 工段产量导出
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeEmployeeAttendanceDto> lineWorkcellProductExport(Long tenantId, HmeEmployeeAttendanceDto1 dto);

    /**
     * 工段产量-总产量明细查询
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return
     */
    Page<HmeEmployeeAttendanceExportVO3> lineWorkcellProductDetails(Long tenantId, HmeEmployeeAttendanceDTO15 dto, PageRequest pageRequest);

    /**
     * 工段产量-不良数明细查询
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return
     */
    Page<HmeEmployeeAttendanceExportVO4> lineWorkcellNcDetails(Long tenantId, HmeEmployeeAttendanceDTO15 dto, PageRequest pageRequest);
}
