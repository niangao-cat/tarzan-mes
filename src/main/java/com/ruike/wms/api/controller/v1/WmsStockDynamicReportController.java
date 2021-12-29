package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsStockDynamicReportDTO;
import com.ruike.wms.app.service.WmsStockDynamicReportService;
import com.ruike.wms.domain.vo.WmsStockDynamicReportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 出入库动态报表
 *
 * @author li.zhang 2021/09/28 9:13
 */
@Slf4j
@RestController("wmsStockDynamicReportController.v1")
@RequestMapping("/v1/{organizationId}/wms-stock-dynamic-report")
@Api(tags = SwaggerApiConfig.WMS_STOCK_DYNAMIC_REPORT)
public class WmsStockDynamicReportController extends BaseController {

    @Autowired
    private WmsStockDynamicReportService wmsStockDynamicReportService;

    @ApiOperation(value = "出入库动态报表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/list")
    public ResponseEntity<Page<WmsStockDynamicReportVO>> queryList(@PathVariable(value = "organizationId") Long tenantId,
                                                                   WmsStockDynamicReportDTO dto, PageRequest pageRequest) {
        dto.initParam();
        return Results.success(wmsStockDynamicReportService.queryList(tenantId,dto,pageRequest));
    }

    @ApiOperation(value = "出入库动态报表导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/export", produces = "application/json;charset=UTF-8")
    @ExcelExport(WmsStockDynamicReportVO.class)
    public ResponseEntity<List<WmsStockDynamicReportVO>> export(@PathVariable(value = "organizationId") Long tenantId,
                                                                  WmsStockDynamicReportDTO dto,
                                                                  ExportParam exportParam,
                                                                  HttpServletResponse response) {
        dto.initParam();
        return Results.success(wmsStockDynamicReportService.export(tenantId,dto,exportParam));
    }
}
