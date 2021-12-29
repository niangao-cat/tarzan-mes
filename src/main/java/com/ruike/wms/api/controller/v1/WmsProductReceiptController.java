package com.ruike.wms.api.controller.v1;

import com.ruike.wms.app.service.WmsProductReceiptService;
import com.ruike.wms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 入库单查询
 *
 * @author sanfeng.zhang@hand-china.com 2020/9/9 14:50
 */
@RestController("wmsProductReceiptController.v1")
@RequestMapping("/v1/{organizationId}/wms-product-receipt")
@Api(tags = SwaggerApiConfig.WMS_PRODUCT_RECEIPT)
@Slf4j
public class WmsProductReceiptController {

    @Autowired
    private WmsProductReceiptService wmsProductReceiptService;

    @ApiOperation(value = "入库单头查询")
    @GetMapping(value = {"/receipt-doc-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue
    public ResponseEntity<Page<WmsReceiptDocVO>> receiptDocQuery(@PathVariable("organizationId") Long tenantId,
                                                                 WmsReceiptDocReqVO reqVO, @ApiIgnore PageRequest pageRequest) {
        return Results.success(wmsProductReceiptService.receiptDocQuery(tenantId, reqVO, pageRequest));
    }

    @ApiOperation(value = "入库单行查询")
    @GetMapping(value = {"/receipt-doc-line-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue
    public ResponseEntity<Page<WmsReceiptLineVO>> receiptDocLineQuery(@PathVariable("organizationId") Long tenantId,
                                                                      WmsReceiptDocVO docVO, @ApiIgnore PageRequest pageRequest) {
        return Results.success(wmsProductReceiptService.receiptDocLineQuery(tenantId, docVO, pageRequest));
    }

    @ApiOperation(value = "入库单明细查询")
    @GetMapping(value = {"/receipt-doc-line-detail"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue
    public ResponseEntity<Page<WmsReceiptDetailVO>> receiptDocLineDetail(@PathVariable("organizationId") Long tenantId,
                                                                         WmsReceiptDetailReqVO reqVO, @ApiIgnore PageRequest pageRequest) {
        return Results.success(wmsProductReceiptService.receiptDocLineDetail(tenantId, reqVO, pageRequest));
    }

    @PostMapping("/pdf")
    @ApiOperation("入库单PDF打印")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> deliveryPrintPdf(@PathVariable("organizationId") Long tenantId,
                                              @RequestBody List<String> instructionDocIds,
                                              HttpServletResponse response) {
        log.info("<==== WmsProductReceiptController-deliveryPrintPdf info:{},{},{}", tenantId, instructionDocIds);

        wmsProductReceiptService.multiplePrint(tenantId, instructionDocIds,response);
        return Results.success();
    }

    @ApiOperation(value = "入库单撤回")
    @PostMapping(value = {"/retract-receipt-doc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> retractReceiptDoc(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody List<String> instructionDocIds) {
        wmsProductReceiptService.retractReceiptDoc(tenantId, instructionDocIds);
        return Results.success();
    }
}
