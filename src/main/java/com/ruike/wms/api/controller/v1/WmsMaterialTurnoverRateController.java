package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsIqcInspectionDetailsDTO;
import com.ruike.wms.api.dto.WmsMaterialTurnoverRateDTO;
import com.ruike.wms.api.dto.WmsPurchaseOrderReceiptInspectionDTO;
import com.ruike.wms.app.service.WmsIqcInspectionDetailsService;
import com.ruike.wms.app.service.WmsMaterialTurnoverRateService;
import com.ruike.wms.domain.vo.WmsIqcInspectionDetailsVO;
import com.ruike.wms.domain.vo.WmsMaterialTurnoverRateVO;
import com.ruike.wms.domain.vo.WmsPurchaseOrderReceiptInspectionVO;
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
 * 物料周转率报表
 *
 * @author li.zhang 2021/09/27 17:00
 */
@Slf4j
@RestController("wmsMaterialTurnoverRateController.v1")
@RequestMapping("/v1/{organizationId}/wms-material-turnover-rate")
@Api(tags = SwaggerApiConfig.WMS_MATERIAL_TURNOVER_RATE)
public class WmsMaterialTurnoverRateController extends BaseController {

    @Autowired
    private WmsMaterialTurnoverRateService wmsMaterialTurnoverRateService;

    @ApiOperation(value = "物料周转率报表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/list")
    public ResponseEntity<Page<WmsMaterialTurnoverRateVO>> queryList(@PathVariable(value = "organizationId") Long tenantId,
                                                                     WmsMaterialTurnoverRateDTO dto, PageRequest pageRequest) {
        dto.initParam();
        return Results.success(wmsMaterialTurnoverRateService.queryList(tenantId,dto,pageRequest));
    }

    @ApiOperation(value = "物料周转率报表导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/export", produces = "application/json;charset=UTF-8")
    @ExcelExport(WmsMaterialTurnoverRateVO.class)
    public ResponseEntity<List<WmsMaterialTurnoverRateVO>> export(@PathVariable(value = "organizationId") Long tenantId,
                                                                            WmsMaterialTurnoverRateDTO dto,
                                                                            ExportParam exportParam,
                                                                            HttpServletResponse response) {
        dto.initParam();
        return Results.success(wmsMaterialTurnoverRateService.export(tenantId,dto,exportParam));
    }
}
