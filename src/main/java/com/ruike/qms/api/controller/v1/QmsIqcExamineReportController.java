package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.QmsIqcExamineReportDTO;
import com.ruike.qms.app.service.QmsIqcExamineReportService;
import com.ruike.qms.domain.vo.QmsIqcExamineReportVO;
import com.ruike.qms.domain.vo.QmsIqcExamineReportVO2;
import com.ruike.qms.domain.vo.QmsIqcExamineReportVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;

/**
 * Iqc检验报表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-12-10 09:56:23
 */
@RestController("qmsIqcExamineReportController.v1")
@RequestMapping("/v1/{organizationId}/qms-iqc-examine-Report")
public class QmsIqcExamineReportController extends BaseController {

    @Autowired
    private QmsIqcExamineReportService qmsIqcExamineReportService;

    @ApiOperation(value = "Iqc检验报表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<QmsIqcExamineReportVO>> iqcExamineReportQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                             QmsIqcExamineReportDTO dto, PageRequest pageRequest) {
        return Results.success(qmsIqcExamineReportService.iqcExamineReportQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "Iqc检验报表饼状图查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/pie/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<QmsIqcExamineReportVO2> iqcExaminePieChartQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                          QmsIqcExamineReportDTO dto){
        return Results.success(qmsIqcExamineReportService.iqcExaminePieChartQuery(tenantId, dto));
    }

    @ApiOperation(value = "Iqc检验报表折线图查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/line/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<QmsIqcExamineReportVO3> iqcExamineLineChartQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                           QmsIqcExamineReportDTO dto){
        return Results.success(qmsIqcExamineReportService.iqcExamineLineChartQuery(tenantId, dto));
    }
}
