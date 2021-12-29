package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.QmsInvoiceAssemblyReturnDTO;
import com.ruike.qms.api.dto.QmsInvoiceDataQueryDTO;
import com.ruike.qms.api.dto.QmsInvoiceDataReturnDTO;
import com.ruike.qms.app.service.QmsInvoiceService;
import com.ruike.qms.domain.vo.QmsOutsourceInvoiceVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

/**
 *
 * @description: 外协发货单创建
 * @author: chaonan.hu@hand-china.com 2020-06-10 09:15:16
 **/
@RestController("qmsInvoiceController.v1")
@RequestMapping("/v1/{organizationId}/qms-invoice")
@Api(tags = SwaggerApiConfig.QMS_INVOICE)
@Slf4j
public class QmsInvoiceController extends BaseController {

    @Autowired
    private QmsInvoiceService qmsInvoiceService;

    @ApiOperation(value = "订单组件查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{instructionId}")
    public ResponseEntity<QmsInvoiceAssemblyReturnDTO> assemblyList(@PathVariable("organizationId") Long tenantId, @PathVariable String instructionId){
        QmsInvoiceAssemblyReturnDTO qmsInvoiceAssemblyReturnDTO = qmsInvoiceService.assemblyDataForUi(tenantId, instructionId);
        return Results.success(qmsInvoiceAssemblyReturnDTO);
    }

    @ApiOperation(value = "外协发货单预生成")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/invoice/query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<QmsInvoiceDataReturnDTO> invoiceQuery(@PathVariable("organizationId") Long tenantId, @RequestBody QmsInvoiceDataQueryDTO queryDTO){
        QmsInvoiceDataReturnDTO qmsInvoiceDataReturnDTO = qmsInvoiceService.invoiceDataForUi(tenantId, queryDTO);
        return Results.success(qmsInvoiceDataReturnDTO);
    }

    @ApiOperation(value = "外协发货单展示")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/outsource/invoice/query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<QmsInvoiceDataReturnDTO> outsourceInvoiceQuery(@PathVariable("organizationId") Long tenantId, @RequestBody QmsOutsourceInvoiceVO invoiceVO){
        QmsInvoiceDataReturnDTO qmsInvoiceDataReturnDTO = qmsInvoiceService.outsourceInvoiceQuery(tenantId, invoiceVO);
        return Results.success(qmsInvoiceDataReturnDTO);
    }

    @ApiOperation(value = "外协发货单创建")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/invoice/create")
    public ResponseEntity<Void> invoiceCreate(@PathVariable("organizationId") Long tenantId, @RequestBody QmsInvoiceDataReturnDTO dto){
        log.info("<====QmsInvoiceController-invoiceCreate:{},{} ",tenantId, dto );
        qmsInvoiceService.invoiceCreate(tenantId, dto);
        return Results.success();
    }

}
