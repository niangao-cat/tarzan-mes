package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsLibraryAgeReportDTO;
import com.ruike.wms.api.dto.WmsLibraryAgeReportDTO2;
import com.ruike.wms.app.service.WmsLibraryAgeReportService;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO2;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO4;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 库龄报表 管理API
 *
 * @author: chaonan.hu@hand-china.com 2020-11-18 14:24:34
 **/
@RestController("wmsLibraryAgeReportController.v1")
@RequestMapping("/v1/{organizationId}/wms-library-age-report")
public class WmsLibraryAgeReportController extends BaseController {

    @Autowired
    private WmsLibraryAgeReportService wmsLibraryAgeReportService;

    @ApiOperation(value = "库龄报表数据查询")
    @GetMapping(value = {"/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<Page<WmsLibraryAgeReportVO>> libraryAgeReportQuery(@PathVariable("organizationId") Long tenantId, WmsLibraryAgeReportDTO dto,
                                                                            PageRequest pageRequest) {
        return Results.success(wmsLibraryAgeReportService.libraryAgeReportQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "库龄区间分组数据查询")
    @GetMapping(value = {"/group/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<Page<WmsLibraryAgeReportVO2>> libraryAgeGroupQuery(@PathVariable("organizationId") Long tenantId, WmsLibraryAgeReportDTO2 dto,
                                                                             PageRequest pageRequest) {
        return Results.success(wmsLibraryAgeReportService.libraryAgeGroupQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "库龄报表导出EXCEL")
    @GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(WmsLibraryAgeReportVO4.class)
    public ResponseEntity<List<WmsLibraryAgeReportVO4>> libraryAgeExport(@PathVariable("organizationId") Long tenantId,
                                                                              WmsLibraryAgeReportDTO dto,
                                                                             HttpServletResponse response,
                                                                             ExportParam exportParam) {
        return Results.success(wmsLibraryAgeReportService.libraryAgeExport(tenantId, dto));
    }
}
