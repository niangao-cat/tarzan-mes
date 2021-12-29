package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfWorkOrderTimeChangeDTO;
import com.ruike.itf.app.service.ItfWorkOrderTimeChangeService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工单时间变更API
 *
 * @author kejin.liu01@hand-china.com 2020年12月17日11:38:30
 */
@RestController("ItfWorkOrderTimeChangeController.v1")
@RequestMapping("/v1/{organizationId}/itf-work-order-time-change-ifacess")
public class ItfWorkOrderTimeChangeController extends BaseController {

    private final ItfWorkOrderTimeChangeService itfWorkOrderTimeChangeService;

    public ItfWorkOrderTimeChangeController(ItfWorkOrderTimeChangeService itfWorkOrderTimeChangeService) {
        this.itfWorkOrderTimeChangeService = itfWorkOrderTimeChangeService;
    }

    @ApiOperation(value = "工单时间变更回传ERP")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<ItfWorkOrderTimeChangeDTO>> hmeWorkOrderTimeChange(@RequestBody List<ItfWorkOrderTimeChangeDTO> dto,
                                                                            @PathVariable("organizationId") Long tenantId) {

        return Results.success(itfWorkOrderTimeChangeService.hmeWorkOrderTimeChange(tenantId, dto));
    }


}
