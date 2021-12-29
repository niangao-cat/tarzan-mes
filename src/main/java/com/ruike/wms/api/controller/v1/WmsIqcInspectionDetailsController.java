package com.ruike.wms.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosWorkcellExceptionDTO;
import com.ruike.hme.app.service.HmeCosWorkcellExceptionService;
import com.ruike.hme.domain.vo.HmeCosWorkcellExceptionVO;
import com.ruike.hme.domain.vo.HmeTagExportVO;
import com.ruike.wms.api.dto.WmsIqcInspectionDetailsDTO;
import com.ruike.wms.app.service.WmsIqcInspectionDetailsService;
import com.ruike.wms.domain.vo.WmsIqcInspectionDetailsVO;
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
 * IQC检验明细报表
 *
 * @author li.zhang 2021/09/09 9:51
 */
@Slf4j
@RestController("wmsIqcInspectionDetailsController.v1")
@RequestMapping("/v1/{organizationId}/wms-iqc-inspection-details")
@Api(tags = SwaggerApiConfig.WMS_IQC_INSPECTION_DETAILS)
public class WmsIqcInspectionDetailsController extends BaseController {

    @Autowired
    private WmsIqcInspectionDetailsService wmsIqcInspectionDetailsService;

    @ApiOperation(value = "IQC检验明细报表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/list")
    public ResponseEntity<Page<WmsIqcInspectionDetailsVO>> queryList(@PathVariable(value = "organizationId") Long tenantId,
                                                                     WmsIqcInspectionDetailsDTO dto, PageRequest pageRequest) {
        dto.initParam();
        return Results.success(wmsIqcInspectionDetailsService.queryList(tenantId,dto,pageRequest));
    }

    @ApiOperation(value = "IQC检验明细报表导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/export", produces = "application/json;charset=UTF-8")
    @ExcelExport(WmsIqcInspectionDetailsVO.class)
    public ResponseEntity<List<WmsIqcInspectionDetailsVO>> export(@PathVariable(value = "organizationId") Long tenantId,
                                                                  WmsIqcInspectionDetailsDTO dto,
                                                                  ExportParam exportParam,
                                                                  HttpServletResponse response) {
        dto.initParam();
        return Results.success(wmsIqcInspectionDetailsService.export(tenantId,dto,exportParam));
    }

}
