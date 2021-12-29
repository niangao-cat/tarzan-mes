package com.ruike.wms.api.controller.v1;

import com.ruike.wms.app.service.WmsInvOnhandQuantityService;
import com.ruike.wms.domain.vo.WmsInvJournalExportVO;
import com.ruike.wms.domain.vo.WmsInvOnhandQuantityVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.inventory.api.dto.MtInvJournalDTO4;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO10;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static tarzan.config.SwaggerApiConfig.WMS_INV_ONHAND_QUANTITY;

/**
 * 库存量 管理 API
 *
 * @author kun.zhou 2020/04/29 13:27 现有量查询代码从产品路径迁出
 */
@RestController("wmsInvOnhandQuantityController.v1")
@RequestMapping("/v1/{organizationId}/wms-inv-onhand-quantity")
@Api(tags = WMS_INV_ONHAND_QUANTITY)
public class WmsInvOnhandQuantityController {
    private final WmsInvOnhandQuantityService service;

    public WmsInvOnhandQuantityController(WmsInvOnhandQuantityService service) { this.service = service;}

    @ApiOperation(value = "查询库存现有量")
    @PostMapping(value = "/onhandQuantityQuery", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsInvOnhandQuantityVO> onhandQuantityQuery(@PathVariable("organizationId") Long tenantId,
                                                                      MtInvOnhandQuantityVO10 dto,
                                                                      PageRequest pageRequest) {
        dto.initParam();
        return Results.success(this.service.onhandQuantityQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "导出数据")
    @GetMapping("/export")
    @ExcelExport(MtInvOnhandQuantityVO10.class)
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<MtInvOnhandQuantityVO10>> onhandQuantityExport(@PathVariable("organizationId") Long tenantId,
                                                                              MtInvOnhandQuantityVO10 dto,
                                                                              ExportParam exportParam,
                                                                              HttpServletResponse response) {
        dto.initParam();
        List<MtInvOnhandQuantityVO10> list = service.export(tenantId, dto, exportParam);
        return Results.success(list);
    }

    @ApiOperation(value = "库存日记账导出EXCEL")
    @GetMapping(value = "/inv-journal-excel-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(WmsInvJournalExportVO.class)
    public ResponseEntity<List<WmsInvJournalExportVO>> invJournalExcelExport(@PathVariable("organizationId") Long tenantId,
                                                                           MtInvJournalDTO4 dto,
                                                                           HttpServletResponse response,
                                                                           ExportParam exportParam) {
        return Results.success(service.invJournalExcelExport(tenantId, dto));
    }
}
