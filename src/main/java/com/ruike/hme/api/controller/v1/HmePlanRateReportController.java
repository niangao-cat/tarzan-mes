package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmePlanRateReportRequestDTO;
import com.ruike.hme.api.dto.HmePlanRateReportResponseDTO;
import com.ruike.hme.app.service.HmePlanRateReportService;
import com.ruike.hme.domain.vo.HmePlanRateDetailVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.time.LocalDate;
import java.util.List;

/**
 * description 计划达成率报表
 *
 * @author quan.luo@hand-china.com 2020/11/25 20:26
 */
@RestController("hmePlanRateReportController.v1")
@RequestMapping("/v1/{organizationId}/hme-plan-rate-report")
@Api(tags = SwaggerApiConfig.HME_PLAN_RATE_REPORT)
public class HmePlanRateReportController extends BaseController {

    @Autowired
    private HmePlanRateReportService hmePlanRateReportService;

    @ApiOperation(value = "计划达成率报表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmePlanRateReportResponseDTO>> planRateReportQuery(@PathVariable("organizationId") Long tenantId,
                                                                                  HmePlanRateReportRequestDTO dto) {
        return Results.success(hmePlanRateReportService.planRateReportQuery(tenantId, dto));
    }

    @ApiOperation(value = "计划达成率报表 投产明细查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/detail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmePlanRateDetailVO>> detailQuery(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestParam("siteId") String siteId,
                                                                 @RequestParam("shiftDate") LocalDate shiftDate,
                                                                 @RequestParam("shiftCode") String shiftCode,
                                                                 @RequestParam("workcellId") String workcellId,
                                                                 PageRequest pageRequest) {
        return Results.success(hmePlanRateReportService.detailQuery(tenantId, siteId, shiftDate, shiftCode, workcellId, pageRequest));
    }

    @ApiOperation(value = "计划达成率报表 交付明细查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/detail-delivery", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmePlanRateDetailVO>> detailDeliveryQuery(@PathVariable("organizationId") Long tenantId,
                                                                         @RequestParam("siteId") String siteId,
                                                                         @RequestParam("shiftDate") LocalDate shiftDate,
                                                                         @RequestParam("shiftCode") String shiftCode,
                                                                         @RequestParam("workcellId") String workcellId,
                                                                         PageRequest pageRequest) {
        return Results.success(hmePlanRateReportService.detailDeliveryQuery(tenantId, siteId, shiftDate, shiftCode, workcellId, pageRequest));
    }
}
