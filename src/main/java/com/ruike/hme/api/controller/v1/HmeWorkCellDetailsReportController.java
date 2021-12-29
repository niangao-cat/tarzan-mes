package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeWorkCellDetailsReportService;
import com.ruike.hme.domain.repository.HmeWorkCellDetailsReportRepository;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;

/**
 * 工位产量明细报表、工序采集项报表及异常信息查看报表
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/08 14:31
 */
@RestController("hmeWorkCellDetailsReportController.v1")
@RequestMapping("/v1/{organizationId}/hme-work-cell-details-report")
@Api(tags = SwaggerApiConfig.HME_WORK_CELL_DETAILS_REPORT)
public class HmeWorkCellDetailsReportController {

    private final HmeWorkCellDetailsReportService hmeWorkCellDetailsReportService;
    private final HmeWorkCellDetailsReportRepository hmeWorkCellDetailsReportRepository;

    public HmeWorkCellDetailsReportController(HmeWorkCellDetailsReportService hmeWorkCellDetailsReportService, HmeWorkCellDetailsReportRepository hmeWorkCellDetailsReportRepository) {
        this.hmeWorkCellDetailsReportService = hmeWorkCellDetailsReportService;
        this.hmeWorkCellDetailsReportRepository = hmeWorkCellDetailsReportRepository;
    }


    @ApiOperation(value = "工位产量明细报表查询")
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeWorkCellDetailsReportVO2>> listForUi(
            @PathVariable("organizationId") Long tenantId, HmeWorkCellDetailsReportVO dto, PageRequest pageRequest) {
        return Results.success(hmeWorkCellDetailsReportService.listForUi(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工段LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/workcell-lov")
    public ResponseEntity<Page<HmeWorkCellVO>> workCellUiQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                               HmeWorkCellVO dto,
                                                               @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeWorkCellDetailsReportService.workCellUiQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工序采集项报表 分页查询")
    @GetMapping(value = "/process-report-list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeProcessReportVo2>> queryProcessReportList(@PathVariable("organizationId") Long tenantId,
                                                                            HmeProcessReportVo dto,
                                                                            PageRequest pageRequest) {
        HmeProcessReportVo.validParam(dto);
        return Results.success(hmeWorkCellDetailsReportService.queryProcessReportList(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工序采集项报表 详情分页查询")
    @GetMapping(value = "/process-report/detail/{jobId}", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeProcessJobDetailVO>> jobDetailQuery(@PathVariable("organizationId") Long tenantId,
                                                                      @PathVariable("jobId") String jobId,
                                                                      PageRequest pageRequest) {
        return Results.success(hmeWorkCellDetailsReportRepository.pagedJobDetail(tenantId, jobId, pageRequest));
    }

    @ApiOperation(value = "工序采集项报表导出")
    @GetMapping(value = "/process-report-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> queryProcessReportExport(
            @PathVariable("organizationId") Long tenantId, HmeProcessReportVo dto, HttpServletResponse response) {
        hmeWorkCellDetailsReportService.queryProcessReportExport(tenantId, dto, response);
        return Results.success();
    }


    @ApiOperation(value = "异常信息查看报表")
    @GetMapping(value = "/exception-report-list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeExceptionReportVO2>> queryExceptionReportList(
            @PathVariable("organizationId") Long tenantId, HmeExceptionReportVO dto, PageRequest pageRequest) {
        return Results.success(hmeWorkCellDetailsReportService.queryExceptionReportList(tenantId, dto, pageRequest));
    }


}
