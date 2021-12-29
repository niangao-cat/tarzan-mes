package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WorkOrderInProcessDetailsQueryReportDTO;
import com.ruike.wms.domain.vo.WorkOrderInProcessDetailsQueryReportVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ywj
 * @version 0.0.1
 * @description 工单在制明细查询报表
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @return
 */
public interface WorkOrderInProcessDetailsQueryReportMapper {

    /**
     * 列表查询
     *
     * @param tenantId 租户
     * @param userId   用户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.wms.domain.vo.WorkOrderInProcessDetailsQueryReportVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/25 10:42:39
     */
    List<WorkOrderInProcessDetailsQueryReportVO> list(@Param(value = "tenantId") Long tenantId,
                                                      @Param(value = "userId") Long userId,
                                                      @Param(value = "dto") WorkOrderInProcessDetailsQueryReportDTO dto);
}
