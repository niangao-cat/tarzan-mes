package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsPoDeliveryConfirmDTO;
import com.ruike.wms.api.dto.WmsPoDeliveryDetailDTO;
import com.ruike.wms.api.dto.WmsPoDeliveryScanDTO2;
import com.ruike.wms.domain.repository.WmsPurchaseReturnExecuteRepository;
import com.ruike.wms.domain.vo.WmsPurchaseLineVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnExecuteDetailsVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnExecuteDocVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnScanVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.List;


/**
 * 采购退货-执行
 *
 * @author sanfeng.zhang@hand-china.com 2020/11/9 19:48
 */
@RestController("wmsPurchaseReturnExecuteController.v1")
@RequestMapping("/v1/{organizationId}/wms-purchase-return-execute")
@Api(tags = SwaggerApiConfig.WMS_PURCHASE_RETURN_EXECUTE)
public class WmsPurchaseReturnExecuteController {

    @Autowired
    private WmsPurchaseReturnExecuteRepository wmsPurchaseReturnExecuteRepository;


    @ApiOperation(value = "扫描采购订单")
    @GetMapping(value = "/scan-instruction-doc-num", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPurchaseReturnExecuteDocVO> scanInstructionDocNum(@PathVariable("organizationId") Long tenantId,
                                                                               @RequestParam String instructionDocNum) {
        return Results.success(wmsPurchaseReturnExecuteRepository.scanInstructionDocNum(tenantId, instructionDocNum));
    }

    @ApiOperation(value = "扫描条码")
    @GetMapping(value = "/scan-material-lot-code", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPurchaseReturnScanVO> scanMaterialLotCode(@PathVariable("organizationId") Long tenantId,
                                                                       WmsPoDeliveryScanDTO2 dto2) {
        return Results.success(wmsPurchaseReturnExecuteRepository.scanMaterialLotCode(tenantId, dto2));
    }

    @ApiOperation(value = "条码明细")
    @GetMapping(value = "/query-code-details", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPurchaseReturnExecuteDetailsVO> queryCodeDetails(@PathVariable("organizationId") Long tenantId,
                                                                              @RequestParam String instructionId) {
        return Results.success(wmsPurchaseReturnExecuteRepository.queryCodeDetails(tenantId, instructionId));
    }

    @ApiOperation(value = "条码删除")
    @PostMapping(value = "/delete-code-details", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPurchaseLineVO> deleteCodeDetails(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody List<WmsPoDeliveryDetailDTO> dtoList) {
        return Results.success(wmsPurchaseReturnExecuteRepository.deleteCodeDetails(tenantId, dtoList));
    }

    @ApiOperation(value = "查询是否全部退货")
    @GetMapping(value = "/query-all-return", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> queryAllReturn(@PathVariable("organizationId") Long tenantId,
                                                                              @RequestParam String instructionDocId) {
        return Results.success(wmsPurchaseReturnExecuteRepository.queryAllReturn(tenantId, instructionDocId));
    }

    @ApiOperation(value = "采购退货-执行")
    @PostMapping(value = "/purchase-return-execute", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPoDeliveryConfirmDTO> purchaseReturnExecute(@PathVariable("organizationId") Long tenantId,
                                                                               @RequestBody WmsPoDeliveryConfirmDTO confirmDTO) {
        return Results.success(wmsPurchaseReturnExecuteRepository.purchaseReturnExecute(tenantId, confirmDTO));
    }
}
