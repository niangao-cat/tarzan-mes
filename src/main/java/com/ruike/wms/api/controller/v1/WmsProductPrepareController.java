package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsBarcodeDTO;
import com.ruike.wms.api.dto.WmsProductPrepareDocQueryDTO;
import com.ruike.wms.domain.service.WmsProductPrepareService;
import com.ruike.wms.domain.vo.*;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static tarzan.config.SwaggerApiConfig.WMS_PRODUCT_PREPARE;

/**
 * <p>
 * 成品备料 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 09:57
 */
@RestController("wmsProductReturnController.v1")
@RequestMapping("/v1/{organizationId}/wms-product-prepare")
@Api(tags = WMS_PRODUCT_PREPARE)
public class WmsProductPrepareController {
    private final WmsProductPrepareService productPrepareService;

    public WmsProductPrepareController(WmsProductPrepareService productPrepareService) {
        this.productPrepareService = productPrepareService;
    }

    @ApiOperation(value = "成品备料单据 单据扫描")
    @GetMapping(value = {"/doc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsProductPrepareDocVO> docScan(@PathVariable("organizationId") Long tenantId,
                                                          @RequestParam String instructionDocNum) {
        return Results.success(productPrepareService.deliveryDocScan(tenantId, instructionDocNum));
    }

    @ApiOperation(value = "成品备料单据 单据选择")
    @GetMapping(value = {"/doc-select"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsProductPrepareDocVO>> docSelect(@PathVariable("organizationId") Long tenantId,
                                                                  WmsProductPrepareDocQueryDTO dto) {
        return Results.success(productPrepareService.deliveryDocLovGet(tenantId, dto));
    }

    @ApiOperation(value = "成品备料行 列表查询")
    @GetMapping(value = {"/line-list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsProductPrepareLineVO>> lineListQuery(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestParam String instructionDocId) {
        return Results.success(productPrepareService.prepareListGet(tenantId, instructionDocId));
    }

    @ApiOperation(value = "成品备料单据 容器扫描")
    @GetMapping(value = {"/container"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsContainerVO> containerScan(@PathVariable("organizationId") Long tenantId,
                                                        @RequestParam String containerCode,
                                                        @RequestParam String instructionDocId) {
        return Results.success(productPrepareService.containerScan(tenantId, containerCode, instructionDocId));
    }

    @ApiOperation(value = "成品备料单据 条码扫描")
    @GetMapping(value = {"/barcode"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsProdPrepareScanVO> prepareListGet(@PathVariable("organizationId") Long tenantId,
                                                               @RequestParam String barcode,
                                                               @RequestParam String instructionDocId,
                                                               @RequestParam String unBundingFlag) {
        return Results.success(productPrepareService.barcodeScan(tenantId, barcode, instructionDocId, unBundingFlag));
    }

    @ApiOperation(value = "成品备料明细 列表查询")
    @GetMapping(value = {"/detail-list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsProductPrepareDetailVO>> detailListQuery(@PathVariable("organizationId") Long tenantId,
                                                                           @RequestParam String instructionId) {
        return Results.success(productPrepareService.detailListQuery(tenantId, instructionId));
    }

    @ApiOperation(value = "成品备料单据 条码提交")
    @PostMapping(value = {"/barcode"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsProductPrepareLineVO>> barcodeMatchSubmit(@PathVariable("organizationId") Long tenantId,
                                                                            @RequestParam String instructionDocId,
                                                                            @RequestParam(required = false) String containerId,
                                                                            @RequestBody WmsProdPrepareScanVO vo) {
        return Results.success(productPrepareService.barcodeMatchSubmit(tenantId, instructionDocId, vo, containerId));
    }

/*    @ApiOperation(value = "成品备料单据 条码取消")
    @DeleteMapping(value = {"/barcode"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsProductPrepareLineVO>> barcodeCancel(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestParam String instructionDocId,
                                                                       @RequestBody List<WmsBarcodeDTO> barcodeList) {
        return Results.success(productPrepareService.barcodeCancel(tenantId, instructionDocId, barcodeList));
    }*/

    @ApiOperation(value = "成品备料单据 条码取消")
    @PostMapping(value = {"/barcodeCancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsProductPrepareLineVO>> barcodeCancel(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestParam String instructionDocId,
                                                                       @RequestParam String instructionId,
                                                                       @RequestBody WmsProdPrepareScanVO vo) {
        return Results.success(productPrepareService.barcodeCancel(tenantId, instructionDocId, instructionId, vo));
    }

    @ApiOperation(value = "成品备料单据 执行")
    @PostMapping(value = {"/execute"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsProductPrepareDocVO> execute(@PathVariable("organizationId") Long tenantId,
                                                          @RequestParam String instructionDocId) {
        return Results.success(productPrepareService.execute(tenantId, instructionDocId));
    }
}
