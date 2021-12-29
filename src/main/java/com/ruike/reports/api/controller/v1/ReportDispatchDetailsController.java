package com.ruike.reports.api.controller.v1;

import com.ruike.reports.api.dto.ReportDispatchDetailsDTO;
import com.ruike.reports.app.service.ReportDispatchDetailsService;
import com.ruike.reports.domain.vo.ReportDispatchDetailsVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

/**
 * 派工明细报表 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-09-02 09:59:30
 */
@RestController("ReportDispatchDetailsController.v1")
@RequestMapping("/v1/{organizationId}/report-dispatch-details")
@Api(tags = "ReportDispatchDetails")
public class ReportDispatchDetailsController extends BaseController {

    private final ReportDispatchDetailsService reportDispatchDetailsService;

    public ReportDispatchDetailsController(ReportDispatchDetailsService reportDispatchDetailsService) {
        this.reportDispatchDetailsService = reportDispatchDetailsService;
    }

    @ApiOperation(value = "派工明细报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<ReportDispatchDetailsVO>> list(@RequestBody ReportDispatchDetailsDTO dto,
                                                              @ApiIgnore PageRequest pageRequest,
                                                              @PathVariable("organizationId") String tenantId) {
        Page<ReportDispatchDetailsVO> list = reportDispatchDetailsService.selectDispatchDetails(pageRequest, tenantId, dto);
        return Results.success(list);
    }

    @ApiOperation(value = "派工明细报表-导出")
    @GetMapping(value = "/report-dispatch-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> reportDispatchExport(@PathVariable("organizationId") String tenantId,
                                                  ReportDispatchDetailsDTO dto,
                                                  HttpServletResponse response) {
        try {
            reportDispatchDetailsService.reportDispatchExport(tenantId, dto, response);
        } catch (Exception e) {
            throw new CommonException("导出失败");
        }
        return Results.success();
    }
}
