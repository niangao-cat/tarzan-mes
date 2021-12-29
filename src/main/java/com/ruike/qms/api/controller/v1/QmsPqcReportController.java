package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.QmsIqcExamineBoardDTO;
import com.ruike.qms.api.dto.QmsPqcReportDTO;
import com.ruike.qms.app.service.QmsPqcReportService;
import com.ruike.qms.domain.vo.*;
import com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandQueryExportVO;
import io.choerodon.core.base.BaseController;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
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
 * 巡检报表管理 API
 *
 * @author: chaonan.hu@hand-china.com 2020/12/11 15:13:23
 **/
@RestController("qmsPqcReportController.v1")
@RequestMapping("/v1/{organizationId}/qms-pqc-report")
public class QmsPqcReportController extends BaseController {

    @Autowired
    private QmsPqcReportService qmsPqcReportService;

    @ApiOperation(value = "巡检报表-头部数据查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/head/mes-report"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<QmsPqcReportVO>> pqcReportHeadDataQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                       QmsPqcReportDTO dto, PageRequest pageRequest){
        return Results.success(qmsPqcReportService.pqcReportHeadDataQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "巡检报表-明细数据查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/detail/mes-report"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<QmsPqcReportVO2>> pgcReportDetailDataQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                          QmsPqcReportDTO dto, PageRequest pageRequest){
        return Results.success(qmsPqcReportService.pgcReportDetailDataQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "车间维度导出巡检头部数据")
    @GetMapping(value = "/head/workshop/excel-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(QmsPqcReportVO3.class)
    public ResponseEntity<List<QmsPqcReportVO3>> pqcReportHeadDataExportByDepartment(@PathVariable("organizationId") Long tenantId,
                                                                                     QmsPqcReportDTO dto,
                                                                                     HttpServletResponse response,
                                                                                     ExportParam exportParam){
        return Results.success(qmsPqcReportService.pqcReportHeadDataExportByDepartment(tenantId, dto));
    }

    @ApiOperation(value = "工序维度导出巡检头部数据")
    @GetMapping(value = "/head/process/excel-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(QmsPqcReportVO4.class)
    public ResponseEntity<List<QmsPqcReportVO4>> pqcReportHeadDataExportByWorkshop(@PathVariable("organizationId") Long tenantId,
                                                                                     QmsPqcReportDTO dto,
                                                                                     HttpServletResponse response,
                                                                                     ExportParam exportParam){
        return Results.success(qmsPqcReportService.pqcReportHeadDataExportByWorkshop(tenantId, dto));
    }

    @ApiOperation(value = "导出巡检明细数据")
    @GetMapping(value = "/detail/excel-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(QmsPqcReportVO5.class)
    public ResponseEntity<List<QmsPqcReportVO5>> pgcReportDetailDataExport(@PathVariable("organizationId") Long tenantId,
                                                                                   QmsPqcReportDTO dto,
                                                                                   HttpServletResponse response,
                                                                                   ExportParam exportParam){
        return Results.success(qmsPqcReportService.pgcReportDetailDataExport(tenantId, dto));
    }
}
