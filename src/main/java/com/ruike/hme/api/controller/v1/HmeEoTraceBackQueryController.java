package com.ruike.hme.api.controller.v1;

import java.io.IOException;
import java.util.List;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine;
import com.ruike.hme.domain.vo.HmeEoTraceBackExportVO;
import com.ruike.hme.domain.vo.HmeEoTraceBackQueryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ruike.hme.app.service.HmeEoTraceBackQueryService;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;

/**
 * 产品追溯查询 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-04-21 13:18
 */
@Slf4j
@RestController("hmeEoTraceBackQueryController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-trace-back")
@Api(tags = SwaggerApiConfig.HME_EO_TRACE_BACK)
public class HmeEoTraceBackQueryController extends BaseController {

    @Autowired
    private HmeEoTraceBackQueryService hmeEoTraceBackQueryService;

    @ApiOperation(value = "工序流转查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/eo-workcell")
    public ResponseEntity<List<HmeEoTraceBackQueryDTO>> eoWorkcellQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                          HmeEoTraceBackQueryDTO4 dto) {
        return Results.success(hmeEoTraceBackQueryService.eoWorkcellQuery(tenantId, dto));
    }

    @ApiOperation(value = "明细查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/eo-workcell-detail")
    public ResponseEntity<HmeEoTraceBackQueryDTO5> eoMaterialQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                   String workcellId, String eoId, String jobId, String collectHeaderId) {
        return Results.success(hmeEoTraceBackQueryService.eoWorkcellDetailQuery(tenantId, workcellId, eoId, jobId, collectHeaderId));
    }

    @ApiOperation(value = "产品组件列查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/product-component")
    public ResponseEntity<List<HmeEoTraceBackQueryDTO7>> productComponentQuery(@PathVariable(value = "organizationId")Long tenantId,
                                                                               HmeEoTraceBackQueryDTO6 dto){
        return Results.success(hmeEoTraceBackQueryService.productComponentQuery(tenantId, dto));
    }

    @ApiOperation(value = "设备查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/equipment")
    public ResponseEntity<List<HmeEoTraceBackQueryDTO8>> equipmentQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                        HmeEoTraceBackQueryDTO dto){
        return Results.success(hmeEoTraceBackQueryService.equipmentQuery(tenantId, dto));
    }

    @ApiOperation(value = "异常信息查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/exception")
    public ResponseEntity<List<HmeEoTraceBackQueryDTO9>> exceptionInfoQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                            HmeEoTraceBackQueryDTO dto){
        return Results.success(hmeEoTraceBackQueryService.exceptionInfoQuery(tenantId, dto));
    }

    @ApiOperation(value = "不良信息查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/nc")
    public ResponseEntity<List<HmeEoTraceBackQueryDTO10>> ncInfoQuery(@PathVariable(value = "organizationId")  Long tenantId,
                                                                      HmeEoTraceBackQueryDTO dto){
        return Results.success(hmeEoTraceBackQueryService.ncInfoQuery(tenantId, dto));
    }

    @ApiOperation(value = "逆向追溯")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/reverse/trace")
    public ResponseEntity<Page<HmeEoTraceBackQueryVO>> reverseTrace(@PathVariable(value = "organizationId")  Long tenantId,
                                                                    String materialLotCode, PageRequest pageRequest){
        return Results.success(hmeEoTraceBackQueryService.reverseTrace(tenantId, materialLotCode, pageRequest));
    }

    @ApiOperation(value = "连续光纤激光器检验报告导出校验")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/report-print-check/{eoIdentification}")
    public ResponseEntity<?> reportPrintCheck(@PathVariable(value = "organizationId")  Long tenantId,
                                         @PathVariable String eoIdentification,
                                         HttpServletResponse response){
        hmeEoTraceBackQueryService.reportPrintCheck(tenantId, eoIdentification, response);
        return Results.success(true);
    }

    @ApiOperation(value = "连续光纤激光器检验报告导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/report-print/{eoIdentification}")
    public ResponseEntity<?> reportPrint(@PathVariable(value = "organizationId") Long tenantId,
                                         @PathVariable String eoIdentification,
                                         HttpServletResponse response) throws IOException {
        try {
            hmeEoTraceBackQueryService.reportPrint(tenantId, eoIdentification, response);
        } catch (Exception e) {
            log.error("<==== HmeEoTraceBackQueryController-report-print error: {}:{}", e.getMessage(), e);
        }
        return Results.success();
    }

    @ApiOperation(value = "质量文件解析-检验项目")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/quantity-analyze-query")
    public ResponseEntity<List<HmeQuantityAnalyzeLine>> quantityAnalyzeQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                             String materialLotCode) {
        return Results.success(hmeEoTraceBackQueryService.quantityAnalyzeQuery(tenantId, materialLotCode));
    }

    @ApiOperation(value = "产品生产履历导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/eo-workcell-export")
    @ExcelExport(HmeEoTraceBackExportVO.class)
    public ResponseEntity<List<HmeEoTraceBackExportVO>> eoWorkcellExport(@PathVariable(value = "organizationId") Long tenantId,
                                                                         HmeEoTraceBackQueryDTO4 dto,
                                                                         ExportParam exportParam,
                                                                         HttpServletResponse httpServletResponse) {
        return Results.success(hmeEoTraceBackQueryService.eoWorkcellExport(tenantId, dto));
    }
}
