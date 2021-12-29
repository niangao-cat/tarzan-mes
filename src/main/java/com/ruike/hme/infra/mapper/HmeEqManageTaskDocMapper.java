package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeEqTaskDocCreateDTO;
import com.ruike.hme.api.dto.HmeEqTaskDocEquipmentDTO;
import com.ruike.hme.api.dto.HmeEqTaskDocQueryDTO;
import com.ruike.hme.api.dto.HmeEquipmentDTO;
import com.ruike.hme.domain.entity.HmeEqManageTaskDoc;
import com.ruike.hme.domain.vo.HmeEqManageTaskCheckVO;
import com.ruike.hme.domain.vo.HmeEqManageTaskInfoVO;
import com.ruike.hme.domain.vo.HmeEqTaskDocAndLineExportVO;
import com.ruike.hme.domain.vo.HmeOrganizationVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 设备管理任务单表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-06-16 16:06:11
 */
public interface HmeEqManageTaskDocMapper extends BaseMapper<HmeEqManageTaskDoc> {

    /**
     * 获取项目组信息
     *
     * @param tenantId 租户ID
     * @return HmeEqCheckByShiftDTO
     */
    List<HmeEqTaskDocCreateDTO> selectEqTagGroup(@Param("tenantId") Long tenantId);

    /**
     * 按班次点检
     *
     * @param tenantId         租户ID
     * @param cycle
     * @param manageTagGroupId
     * @return HmeEqCheckByShiftDTO
     */
    List<HmeEqTaskDocCreateDTO> selectEqManageTag(@Param("tenantId") Long tenantId,
                                                  @Param("cycle") String cycle,
                                                  @Param("manageTagGroupId") String manageTagGroupId);

    /**
     * 根据设备类型匹配设备信息
     *
     * @param tenantId
     * @param equipmentCategory
     * @return HmeEquipmentDTO
     */
    List<HmeEquipmentDTO> selectEquipmentByCategory(@Param("tenantId") Long tenantId,
                                                    @Param("equipmentCategory") String equipmentCategory);

    /**
     * 根据设备匹配产线
     *
     * @param tenantId
     * @param siteId
     * @param equipmentId
     * @return java.lang.String
     */
    List<String> getStationId(@Param("tenantId") Long tenantId,
                              @Param("siteId") String siteId,
                              @Param("equipmentId") String equipmentId);

    /**
     * 校验单据是否存在
     *
     * @param tenantId
     * @param vo
     * @return java.lang.String
     */
    List<Integer> getDiffTime(@Param("tenantId") Long tenantId,
                              @Param("vo") HmeEqManageTaskCheckVO vo);

    /**
     * 任务单查询
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeEqTaskDocQueryDTO> queryTaskDocList(@Param("tenantId") Long tenantId,
                                                @Param("dto") HmeEqTaskDocQueryDTO dto);

    /***
     * 查询该设备当天所有点检任务（不排班情况下，用于工位设备切换）
     * @param tenantId
     * @param equipmentId
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEqManageTaskDoc>
     * @auther hcn
     * @date 2020/6/23
     */
    List<HmeEqManageTaskDoc> queryTaskDocList2(@Param("tenantId") Long tenantId,
                                               @Param("equipmentId") String equipmentId);

    /**
     * 获取点检设备信息
     *
     * @param tenantId
     * @author jiangling.zheng@hand-china.com 2020/8/13 15:00
     * @return java.util.List<com.ruike.hme.api.dto.HmeEqTaskDocEquipmentDTO>
     */

    List<HmeEqTaskDocEquipmentDTO> selectCheckEquipment(@Param("tenantId") Long tenantId);

    /**
     * 获取保养设备信息
     *
     * @param tenantId
     * @return java.util.List<com.ruike.hme.api.dto.HmeEqTaskDocEquipmentDTO>
     * @author jiangling.zheng@hand-china.com 2020/8/13 15:00
     */

    List<HmeEqTaskDocEquipmentDTO> selectMaintainEquipment(@Param("tenantId") Long tenantId);

    /**
     * 获取设备点检任务（其他情况）
     *
     * @param tenantId
     * @param hmeEqManageTaskDoc
     * @return
     */
    List<HmeEqManageTaskDoc> queryManageTask(@Param("tenantId") Long tenantId, @Param("hmeEqManageTaskDoc") HmeEqManageTaskDoc hmeEqManageTaskDoc,
                                       @Param("creationDateTo") Date creationDateTo);

    /**
     * 获取设备点检任务（其他情况）
     *
     * @param tenantId
     * @param hmeEqManageTaskDoc
     * @return
     */
    List<HmeEqManageTaskDoc> queryManageTask3(@Param("tenantId") Long tenantId, @Param("hmeEqManageTaskDoc") HmeEqManageTaskDoc hmeEqManageTaskDoc,
                                              @Param("creationDateFrom") Date creationDateFrom);

    /**
     * 点检&&保养信息信息
     *
     * @param tenantId
     * @param taskDocIdList
     * @return
     */
    List<HmeEqManageTaskInfoVO> equipmentContent(@Param("tenantId") Long tenantId, @Param("taskDocIdList") List<String> taskDocIdList);

    /**
     * 获取设备保养任务
     *
     * @param tenantId
     * @param hmeEqManageTaskDoc
     * @return
     */
    List<HmeEqManageTaskDoc> queryManageTask2(@Param("tenantId") Long tenantId, @Param("hmeEqManageTaskDoc") HmeEqManageTaskDoc hmeEqManageTaskDoc,
                                             @Param("creationDateTo") Date creationDateTo);

    /**
     * 获取已完成的点检项或保养项数量
     *
     * @param tenantId
     * @param taskDocIdList
     * @return
     */
    int getCompleteQty(@Param("tenantId") Long tenantId, @Param("taskDocIdList") List<String> taskDocIdList);

    /**
     * 获取管理任务单
     *
     * @param tenantId 租户ID
     * @param equipmentIdList 设备ID
     * @param shiftCode 班次编码
     * @param shiftDate 班次日期
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEqManageTaskDoc>
     */
    List<HmeEqManageTaskDoc> queryTaskDocList3(@Param("tenantId") Long tenantId,
                                               @Param("equipmentIdList") List<String> equipmentIdList,
                                               @Param("shiftCode") String shiftCode,
                                               @Param("shiftDate") Date shiftDate);
    /**
     * 批量更新设备任务状态
     *
     * @param tenantId
     * @param userId
     * @param manageTaskDocList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/5 0:50
     */
    void batchUpdateManageTaskDoc(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("manageTaskDocList") List<HmeEqManageTaskDoc> manageTaskDocList);

    /**
     * 查询超过24小时周期为天的
     *
     * @param tenantId
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEqManageTaskDoc>
     * @author sanfeng.zhang@hand-china.com 2021/3/5 14:18
     */
    List<HmeEqManageTaskDoc> queryTaskDocOver24Hour(@Param("tenantId") Long tenantId);

    /**
     * 查询超过周期的
     *
     * @param tenantId
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEqManageTaskDoc>
     * @author sanfeng.zhang@hand-china.com 2021/3/5 14:55
     */
    List<HmeEqManageTaskDoc> queryTaskDocOverTaskCycle(@Param("tenantId") Long tenantId);

    /**
     *
     *
     * @param dto
     * @param tenantId
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-15 18:19
     * @return int
     */
    int queryTaskDocListCount(@Param("tenantId")Long tenantId, @Param("dto")HmeEqTaskDocQueryDTO dto);

    /**
     * 根据产线查询工位
     *
     * @param tenantId
     * @param siteId
     * @param prodLineId
     * @author sanfeng.zhang@hand-china.com 2021/3/17 22:17
     * @return java.util.List<java.lang.String>
     */
    List<String> queryWorkcellIdByProdLineId(@Param("tenantId")Long tenantId, @Param("siteId") String siteId, @Param("prodLineId") String prodLineId);

    /**
     * 根据车间查询工位
     *
     * @param tenantId
     * @param siteId
     * @param workshopId
     * @author sanfeng.zhang@hand-china.com 2021/3/17 22:19
     * @return java.util.List<java.lang.String>
     */
    List<String> queryWorkcellIdByWorkShopId(@Param("tenantId")Long tenantId, @Param("siteId") String siteId, @Param("workshopId") String workshopId);

    /**
     * 制造部查询工位
     *
     * @param tenantId
     * @param siteId
     * @param areaId
     * @author sanfeng.zhang@hand-china.com 2021/3/17 22:19
     * @return java.util.List<java.lang.String>
     */
    List<String> queryWorkcellIdByAreaId(@Param("tenantId")Long tenantId, @Param("siteId") String siteId, @Param("areaId") String areaId);

    /**
     * 根据工位找组织信息
     *
     * @param tenantId
     * @param workcellIdList
     * @param siteId
     * @author sanfeng.zhang@hand-china.com 2021/5/8 11:38
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOrganizationVO>
     */
    List<HmeOrganizationVO> queryOrganizationByWorkcellIds(@Param("tenantId")Long tenantId, @Param("siteId") String siteId,@Param("workcellIdList") List<String> workcellIdList);

    /**
     * 查询有进出站记录的设备任务
     *
     * @param tenantId
     * @param taskDocIdList
     * @author sanfeng.zhang@hand-china.com 2021/5/24 0:37
     * @return java.util.List<java.lang.String>
     */
    List<String> queryUndoneTaskDocList(@Param("tenantId")Long tenantId, @Param("taskDocIdList") List<String> taskDocIdList);

    /**
     * 根据开班结班时间查询有进出站记录的设备任务
     *
     * @param tenantId
     * @param taskDocIdList
     * @param shiftStartTime
     * @param shiftEndTime
     * @author sanfeng.zhang@hand-china.com 2021/5/24 0:53
     * @return java.util.List<java.lang.String>
     */
    List<String> queryUndoneTaskDocByWkcShift(@Param("tenantId")Long tenantId, @Param("taskDocIdList") List<String> taskDocIdList, @Param("shiftStartTime") Date shiftStartTime, @Param("shiftEndTime") Date shiftEndTime);

    /**
     * 查询没有开班记录的班次任务
     * @param tenantId
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEqManageTaskDoc>
     * @author sanfeng.zhang@hand-china.com 2021/5/31
     */
    List<HmeEqManageTaskDoc> queryTaskDocNonShift(@Param("tenantId") Long tenantId, @Param("nowDay") String nowDay);

    /**
     * 查询有开班记录的班次任务
     * @param tenantId
     * @param nowDay
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEqManageTaskDoc>
     * @author sanfeng.zhang@hand-china.com 2021/6/1
     */
    List<HmeEqManageTaskDoc> queryTaskDocShift(@Param("tenantId") Long tenantId, @Param("nowDay") String nowDay);

    /**
     * 查询任务单和任务行-导出
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeEqTaskDocAndLineExportVO> queryTaskDocAndLineList(@Param("tenantId") Long tenantId, @Param("dto") HmeEqTaskDocQueryDTO dto);
}
