package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeProcessGatherResultReportDto;
import com.ruike.hme.app.service.HmeReportService;
import com.ruike.hme.domain.vo.HmeProcessGatherResultReportVO;
import com.ruike.hme.domain.vo.HmeProcessGatherResultReportVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 报表管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-03-22 10:00:02
 */
@RestController("hmeReportController.v1")
@RequestMapping("/v1/{organizationId}/hme-report/mes-report")
public class HmeReportController extends BaseController {

    @Autowired
    private HmeReportService hmeReportService;

    @ApiOperation(value = "工序采集结果报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/process-gather-result"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeProcessGatherResultReportVO>> processGatherResultReportQuery(@PathVariable("organizationId") Long tenantId,
                                                                                               HmeProcessGatherResultReportDto dto, PageRequest pageRequest) {
        return Results.success(hmeReportService.processGatherResultReportQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工序采集结果报表导出EXCEL")
    @GetMapping(value = "/process-gather-result/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeProcessGatherResultReportVO2.class)
    public ResponseEntity<List<HmeProcessGatherResultReportVO2>> processGatherResultReportExport(@PathVariable("organizationId") Long tenantId,
                                                                         HmeProcessGatherResultReportDto dto,
                                                                         HttpServletResponse response,
                                                                         ExportParam exportParam) {
        return Results.success(hmeReportService.processGatherResultReportExport(tenantId, dto));
    }
}
