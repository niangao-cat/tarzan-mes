package com.ruike.wms.api.controller.v1;

import com.ruike.itf.domain.vo.ItfMaterialLotConfirmIfaceVO4;
import com.ruike.wms.api.dto.WmsStandingWarehouseEntryReviewDTO;
import com.ruike.wms.app.service.WmsStandingWarehouseEntryReviewService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static tarzan.config.SwaggerApiConfig.WMS_STANDING_WAREHOUSE_ENTRY_REVIEW;

/**
 * @description:立库入库复核
 * @return:
 * @author: xiaojiang
 * @time: 2021/6/28 13:55
 */
@RestController("WmsStandingWarehouseEntryReviewController.v1")
@RequestMapping("/v1/{organizationId}/wms-standing-warehouse")
@Api(tags = WMS_STANDING_WAREHOUSE_ENTRY_REVIEW)
public class WmsStandingWarehouseEntryReviewController extends BaseController {
    @Autowired
    private WmsStandingWarehouseEntryReviewService wmsStandingWarehouseEntryReviewService;

    @ApiOperation(value = "条码扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/query-barcode")
    public ResponseEntity<WmsStandingWarehouseEntryReviewDTO> queryBarcode(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody WmsStandingWarehouseEntryReviewDTO dto) {
        return Results.success(wmsStandingWarehouseEntryReviewService.queryBarcode(tenantId, dto));
    }




    @ApiOperation(value = "确认复核")
    @PostMapping(value = "/confirm", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<ItfMaterialLotConfirmIfaceVO4>> confirm(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody WmsStandingWarehouseEntryReviewDTO dto) {
//        ResponseData<List<ItfMaterialLotConfirmIfaceVO4>> result = new ResponseData<>();
//        try {
//            wmsStandingWarehouseEntryReviewService.confirm(tenantId, dto);
//        } catch (Exception ex) {
//            result.setSuccess(false);
//            result.setMessage(ex.getMessage());
//        }
//        return result;
        return Results.success(wmsStandingWarehouseEntryReviewService.confirm(tenantId, dto));
    }
}
