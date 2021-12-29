package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.service.WmsPrepareExecuteService;
import com.ruike.wms.domain.vo.WmsPrepareExecInsDocVO;
import com.ruike.wms.domain.vo.WmsPrepareExecInsVO;
import com.ruike.wms.domain.vo.WmsPrepareExecuteBarcodeVO;
import com.ruike.wms.domain.vo.WmsInstructionActualDetailVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

import static tarzan.config.SwaggerApiConfig.WMS_PREPARE_EXECUTE;

/**
 * 备料执行 管理 API
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 17:04
 */
@RestController("wmsPrepareExecuteController.v1")
@RequestMapping("/v1/{organizationId}/wms-prepare-execute")
@Api(tags = WMS_PREPARE_EXECUTE)
public class WmsPrepareExecuteController extends BaseController {

    private final WmsPrepareExecuteService service;

    public WmsPrepareExecuteController(WmsPrepareExecuteService wmsPrepareExecuteService) {
        this.service = wmsPrepareExecuteService;
    }

    @ApiOperation(value = "查询配送单")
    @GetMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPrepareExecInsDocVO> deliveryQuery(@PathVariable("organizationId") Long tenantId,
                                                                @RequestParam String instructionDocNum) {
        return Results.success(service.instructionDocQuery(tenantId, instructionDocNum));
    }

    @ApiOperation(value = "查询配送单行")
    @GetMapping(value = "/line", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsPrepareExecInsVO>> deliveryLineQuery(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestParam String instructionDocId) {
        return Results.success(service.instructionQuery(tenantId, instructionDocId));
    }

    @ApiOperation(value = "查询配送单明细")
    @GetMapping(value = "/detail", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsInstructionActualDetailVO>> actualDetailQuery(@PathVariable("organizationId") Long tenantId,
                                                                                @RequestParam String instructionId) {
        return Results.success(service.actualDetailQuery(tenantId, instructionId));
    }

    @ApiOperation(value = "查询推荐货位")
    @GetMapping(value = "/locator", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<MtModLocator> locatorQuery(@PathVariable("organizationId") Long tenantId,
                                                     @RequestParam String instructionId) {
        return Results.success(service.getRecommendLocator(tenantId, instructionId));
    }

    @ApiOperation(value = "条码扫描")
    @PostMapping(value = "/scan", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPrepareExecuteBarcodeVO> barcodeScan(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody WmsPrepareExecScanDTO scan) {
        return Results.success(service.barcodeScan(tenantId, scan));
    }

    @ApiOperation(value = "备料执行")
    @PostMapping(value = "/execute", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Boolean> prepareExecute(@PathVariable("organizationId") Long tenantId,
                                                  @RequestParam String instructionDocId) {
        return Results.success(service.execute(tenantId, instructionDocId));
    }

    @ApiOperation(value = "备料执行前验证")
    @PostMapping(value = "/execute-validate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Boolean> validateBeforeExecute(@PathVariable("organizationId") Long tenantId,
                                                         @RequestParam String instructionDocId) {
        return Results.success(service.validateBeforeExecute(tenantId, instructionDocId));
    }

    @ApiOperation(value = "条码拆分")
    @PostMapping(value = "/split", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<String> split(@PathVariable("organizationId") Long tenantId,
                                        @RequestBody WmsPrepareExecSplitDTO dto) {
        return Results.success(service.split(tenantId, dto));
    }

    @ApiOperation(value = "提交已扫描条码")
    @PutMapping(value = "/scan", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPrepareExecuteBarcodeVO> submitScanned(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody WmsPrepareExecScannedDTO scan) {
        return Results.success(service.submitScanned(tenantId, scan));
    }

    @ApiOperation(value = "删除已扫描条码")
    @DeleteMapping(value = "/scan", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPrepareExecInsVO> removeScanned(@PathVariable("organizationId") Long tenantId,
                                                             @RequestParam String instructionId,
                                                             @RequestBody List<WmsBarcodeDTO> barcodeList) {
        return Results.success(service.removeScanned(tenantId, instructionId, barcodeList));
    }
}
