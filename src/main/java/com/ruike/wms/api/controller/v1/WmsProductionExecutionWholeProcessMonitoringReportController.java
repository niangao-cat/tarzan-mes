package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsProductionExecutionWholeProcessMonitoringReportDTO;
import com.ruike.wms.domain.repository.WmsProductionExecutionWholeProcessMonitoringReportRepository;
import com.ruike.wms.domain.vo.WmsProductionExecutionWholeProcessMonitoringReportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/***
 * @description：生产执行全过程监控报表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2021/2/19
 * @time 15:03
 * @version 0.0.1
 * @return
 */
@RestController("WmsProductionExecutionWholeProcessMonitoringReportController.v1")
@RequestMapping("/v1/{organizationId}/production-execution-whole-process-monitoring-report")
public class WmsProductionExecutionWholeProcessMonitoringReportController {

    @Autowired
    private WmsProductionExecutionWholeProcessMonitoringReportRepository productionExecutionWholeProcessMonitoringReportRepository;

    @ApiOperation(value = "查询报表信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<WmsProductionExecutionWholeProcessMonitoringReportVO>> list(@PathVariable("organizationId") Long tenantId,
                                                                                           WmsProductionExecutionWholeProcessMonitoringReportDTO dto, PageRequest pageRequest) {
        return Results.success(productionExecutionWholeProcessMonitoringReportRepository.list(tenantId, pageRequest, dto));
    }
}
