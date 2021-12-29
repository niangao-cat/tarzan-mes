package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeQualificationDTO3;
import com.ruike.hme.domain.entity.HmeEmployeeAssign;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 人员与资质关系表Mapper
 *
 * @author chaonan.hu@hand-china.com 2020-06-16 11:13:32
 */
public interface HmeEmployeeAssignMapper extends BaseMapper<HmeEmployeeAssign> {

    /**
     * 查询lov
     *
     * @param tenantId        租户ID
     * @param dto             查询参数
     * @param qualityTypeList 质量类型列表
     * @return java.util.List<com.ruike.hme.api.dto.HmeQualificationDTO3>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/15 10:30 上午
     */
    List<HmeQualificationDTO3> queryLov(@Param("tenantId") Long tenantId, @Param("dto") HmeQualificationDTO3 dto,
                                        @Param("qualityTypeList") List<String> qualityTypeList);

    /**
     * 查询数据
     *
     * @param tenantId    租户ID
     * @param employeeId  员工ID
     * @param currentDate 当前日期
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEmployeeAssign>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/15 10:31 上午
     */
    List<HmeEmployeeAssign> queryData(@Param("tenantId") Long tenantId,
                                      @Param("employeeId") String employeeId,
                                      @Param("currentDate") String currentDate);

    /**
     * 查询资质数据
     *
     * @param tenantId    租户ID
     * @param employeeId  员工ID
     * @param qualityList  资质IDs
     * @param materialId 物料ID
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEmployeeAssign>
     * @author xin.t@raycuslaser.com 2021/6/25 10:31 上午
     */
    List<HmeEmployeeAssign> queryDataOptional(@Param("tenantId") Long tenantId,
                                              @Param("employeeId") String employeeId,
                                              @Param("qualityList") List<String> qualityList,
                                              @Param("materialId") String materialId);
}
