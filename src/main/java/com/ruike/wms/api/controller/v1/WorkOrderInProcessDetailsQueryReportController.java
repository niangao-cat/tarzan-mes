package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WorkOrderInProcessDetailsQueryReportDTO;
import com.ruike.wms.domain.repository.WorkOrderInProcessDetailsQueryReportRepository;
import com.ruike.wms.domain.vo.WorkOrderInProcessDetailsQueryReportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @description 工单在制明细查询报表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @version 0.0.1
 * @return
 */
@RestController("workOrderInProcessDetailsQueryReportController.v1")
@RequestMapping("/v1/{organizationId}/work-order-in-process-details-query-report")
public class WorkOrderInProcessDetailsQueryReportController {

    @Autowired
    private WorkOrderInProcessDetailsQueryReportRepository workOrderInProcessDetailsQueryReportRepository;

    @ApiOperation(value = "查询报表信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<Page<WorkOrderInProcessDetailsQueryReportVO>> list(@PathVariable("organizationId") Long tenantId,
                                                                             PageRequest pageRequest,
                                                                             @RequestBody WorkOrderInProcessDetailsQueryReportDTO dto) {
        WorkOrderInProcessDetailsQueryReportDTO.validParam(dto);
        return Results.success(workOrderInProcessDetailsQueryReportRepository.list(tenantId, pageRequest, dto));
    }
}
