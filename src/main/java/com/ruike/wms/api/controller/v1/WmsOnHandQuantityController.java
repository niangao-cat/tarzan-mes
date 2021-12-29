package com.ruike.wms.api.controller.v1;

import com.ruike.wms.app.service.WmsOnhandQuantityService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.inventory.api.dto.MtInvOnhandQuantityDTO;
import tarzan.inventory.app.service.MtInvOnhandQuantityService;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO4;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/24 16:20
 */
@RestController("wmsOnHandQuantityController.v1")
@RequestMapping("/v1/{organizationId}/wms-inv-onhand-quantity")
@Api(tags = "WmsInvOnhandQuantity")
public class WmsOnHandQuantityController extends BaseController {

    @Autowired
    private WmsOnhandQuantityService wmsOnhandQuantityService;

    @ApiOperation(value = "库存导出")
    @GetMapping(value = "/quantity-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(MtInvOnhandQuantityVO4.class)
    public ResponseEntity<List<MtInvOnhandQuantityVO4>> export(
            @PathVariable("organizationId") Long tenantId,
            MtInvOnhandQuantityDTO dto,
            ExportParam exportParam,
            HttpServletResponse response) {
        return Results.success(this.wmsOnhandQuantityService.export(tenantId, dto, exportParam));
    }
}
