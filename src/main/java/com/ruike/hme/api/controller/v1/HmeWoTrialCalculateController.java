package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeWoTrialCalculateReportQueryDTO;
import com.ruike.hme.api.dto.HmeWoTrialCalculateReportSaveDTO;
import com.ruike.hme.app.service.HmeWoTrialCalculateService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

/**
 * 工单入库日期试算表 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-08-25 21:54:56
 */
@Slf4j
@RestController("hmeWoTrialCalculateController.v1")
@RequestMapping("/v1/{organizationId}/hme-wo-trial-calculates")
@Api(tags = SwaggerApiConfig.HME_WO_TRIAL_CALCULATE)
public class HmeWoTrialCalculateController extends BaseController {

    @Autowired
    private HmeWoTrialCalculateService hmeWoTrialCalculateService;

    @ApiOperation(value = "查询试算报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-report")
    public ResponseEntity<?> queryReport(@PathVariable("organizationId") Long tenantId, HmeWoTrialCalculateReportQueryDTO dto) {
        log.info("<====HmeWoTrialCalculateController.queryReport:{}，{}", tenantId, dto);
        return Results.success(hmeWoTrialCalculateService.queryReport(tenantId, dto));
    }

    @ApiOperation(value = "试算报表重排")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping("/reschedule")
    public ResponseEntity<?> reschedule(@PathVariable("organizationId") Long tenantId, @RequestParam String workOrderId) {
        log.info("<====HmeWoTrialCalculateController.reschedule:{}，{}", tenantId, workOrderId);
        hmeWoTrialCalculateService.reschedule(tenantId, workOrderId);
        return Results.success();
    }

    @ApiOperation(value = "试算报表保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save")
    public ResponseEntity<?> save(@PathVariable("organizationId") Long tenantId,
                                  @RequestBody HmeWoTrialCalculateReportSaveDTO dto) {
        log.info("<====HmeWoTrialCalculateController.reschedule:{}，{}", tenantId, dto);
        hmeWoTrialCalculateService.save(tenantId, dto);
        return Results.success();
    }

}
