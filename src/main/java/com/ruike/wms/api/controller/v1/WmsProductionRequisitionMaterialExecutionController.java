package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsProductionRequisitionMaterialExecutionService;
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

import static tarzan.config.SwaggerApiConfig.WMS_PRODUCTION_REQUISITION_MATERIAL_EXECUTION;

@RestController("WmsProductionRequisitionMaterialExecutionController.v1")
@RequestMapping("/v1/{organizationId}/wms-production-requisition")
@Api(tags = WMS_PRODUCTION_REQUISITION_MATERIAL_EXECUTION)
public class WmsProductionRequisitionMaterialExecutionController extends BaseController {

    @Autowired
    private WmsProductionRequisitionMaterialExecutionService service;

    @ApiOperation(value = "领料单查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/head-query")
    public ResponseEntity<WmsProductionRequisitionMaterialExecutionDTO> queryHead(@PathVariable("organizationId") Long tenantId,
                                                                                  @RequestParam String instructionDocNum) {
        return Results.success(service.queryHead(tenantId, instructionDocNum));
    }

    @ApiOperation(value = "扫描容器/条码查询校验")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/barcode-query")
    public ResponseEntity<List<WmsProductionRequisitionMaterialExecutionLineDTO>> queryBarcode(@PathVariable("organizationId") Long tenantId,
                                                                                               @RequestBody WmsProductionRequisitionMaterialExecutionLineDTO dto) {
        return Results.success(service.queryBarcode(tenantId, dto));
    }

/*    @ApiOperation(value = "扫描容器/条码查询/更新")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/barcode-update")
    public ResponseData<Void> updateBarcode(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody List<WmsProductionRequisitionMaterialExecutionLineDTO> dtoList) {

        ResponseData<Void> responseData = new ResponseData<>();
        try {
            responseData.setRows(service.updateBarcode(tenantId, dtoList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;

        //return Results.success(service.updateBarcode(tenantId, dto));
    }*/


    @ApiOperation(value = "执行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/execute")
    public ResponseData<WmsProductionRequisitionMaterialExecutionDTO> execute(@PathVariable("organizationId") Long tenantId,
                                                                              @RequestBody List<WmsProductionRequisitionMaterialExecutionLineDTO> dtoList,
                                                                              @RequestParam String workOrderNum,
                                                                              @RequestParam String instructionDocType) {
        ResponseData<WmsProductionRequisitionMaterialExecutionDTO> responseData = new ResponseData<>();
        try {
            responseData.setRows(service.execute(tenantId, dtoList, workOrderNum,instructionDocType));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/material_lot")
    public ResponseData<List<WmsProductionRequisitionMaterialExecutionDetailDTO>> materialLotCodeQuery(@PathVariable("organizationId") Long tenantId,
                                                                                                       @RequestBody WmsProductionRequisitionMaterialExecutionLineDTO dto) {
        ResponseData<List<WmsProductionRequisitionMaterialExecutionDetailDTO>> responseData = new ResponseData<>();
        try {
            responseData.setRows(service.materialLotCodeQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/barcode-delete")
    public ResponseData<WmsProductionRequisitionMaterialExecutionLineDTO> barcodeDelete(@PathVariable("organizationId") Long tenantId,
                                                                                        @RequestBody WmsProductionRequisitionMaterialExecutionLineDTO dto) {
        ResponseData<WmsProductionRequisitionMaterialExecutionLineDTO> responseData = new ResponseData<>();
        try {
            responseData.setRows(service.barcodeDelete(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "删除查询单据行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/line-query")
    public ResponseEntity<WmsProductionRequisitionMaterialExecutionLineDTO> queryLine(@PathVariable("organizationId") Long tenantId,
                                                                                      @RequestParam String instructionDocId,
                                                                                      @RequestParam String instructionId) {
        return Results.success(service.queryLine(tenantId, instructionDocId,instructionId));
    }

}
