package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsProductionExecutionWholeProcessMonitoringReportDTO;
import com.ruike.wms.domain.vo.WmsProductionExecutionWholeProcessMonitoringReportVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * @description：生产执行全过程监控报表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2021/2/19
 * @time 15:03
 * @version 0.0.1
 * @return
 */
public interface WmsProductionExecutionWholeProcessMonitoringReportMapper {

    List<WmsProductionExecutionWholeProcessMonitoringReportVO> list(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") WmsProductionExecutionWholeProcessMonitoringReportDTO dto);
}
