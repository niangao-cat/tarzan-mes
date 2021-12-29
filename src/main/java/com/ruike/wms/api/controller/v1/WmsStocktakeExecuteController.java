package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsStocktakeDocService;
import com.ruike.wms.domain.repository.WmsStocktakeActualRepository;
import com.ruike.wms.domain.repository.WmsStocktakeDocRepository;
import com.ruike.wms.domain.service.WmsStocktakeService;
import com.ruike.wms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collections;
import java.util.List;

import static tarzan.config.SwaggerApiConfig.WMS_STOCKTAKE_EXECUTE;

/**
 * 库存盘点执行 API
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 12:27
 */
@RestController("wmsStocktakeExecuteController.v1")
@RequestMapping("/v1/{organizationId}/wms-stocktake-execute")
@Api(tags = WMS_STOCKTAKE_EXECUTE)
public class WmsStocktakeExecuteController extends BaseController {
    private final WmsStocktakeDocService stocktakeDocService;
    private final WmsStocktakeDocRepository stocktakeDocRepository;
    private final WmsStocktakeActualRepository stocktakeActualRepository;
    private final WmsStocktakeService wmsStocktakeService;

    public WmsStocktakeExecuteController(WmsStocktakeDocService stocktakeDocService, WmsStocktakeDocRepository stocktakeDocRepository, WmsStocktakeActualRepository stocktakeActualRepository, WmsStocktakeService wmsStocktakeService) {
        this.stocktakeDocService = stocktakeDocService;
        this.stocktakeDocRepository = stocktakeDocRepository;
        this.stocktakeActualRepository = stocktakeActualRepository;
        this.wmsStocktakeService = wmsStocktakeService;
    }

    @ApiOperation(value = "库存盘点单据 输入/扫描")
    @GetMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsStocktakeDocSelectVO> docScan(@PathVariable("organizationId") Long tenantId,
                                                           @RequestParam @ApiParam(name = "盘点单号") String stocktakeNum) {
        return Results.success(stocktakeDocService.docScan(tenantId, stocktakeNum));
    }

    @ApiOperation(value = "库存盘点单据 选择")
    @GetMapping(value = "/select", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsStocktakeDocSelectVO>> select(@PathVariable("organizationId") Long tenantId,
                                                                WmsStocktakeDocSelectQueryDTO condition) {
        return Results.success(stocktakeDocRepository.stocktakeSelectLov(tenantId, condition));
    }

    @ApiOperation(value = "库存盘点实绩 条码扫描")
    @GetMapping(value = "/scan", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsStocktakeBarcodeScanVO> scan(@PathVariable("organizationId") Long tenantId,
                                                          WmsStocktakeScanDTO dto) {
        this.validObject(dto);
        return Results.success(wmsStocktakeService.barcodeScan(tenantId, dto));
    }

    @ApiOperation(value = "库存盘点实绩执行 物料校验")
    @GetMapping(value = "/material-validation", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsMaterialVO>> materialValidation(@PathVariable("organizationId") Long tenantId,
                                                                  WmsStocktakeValidationDTO dto) {
        this.validObject(dto);
        return Results.success(wmsStocktakeService.materialValidation(tenantId, dto));
    }

    @ApiOperation(value = "库存盘点实绩执行 库存快照校验")
    @GetMapping(value = "/actual-validation", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsMaterialLotAttrVO>> actualValidation(@PathVariable("organizationId") Long tenantId,
                                                                       WmsStocktakeValidationDTO dto) {
        this.validObject(dto);
        return Results.success(wmsStocktakeService.actualValidation(tenantId, dto));
    }

    @ApiOperation(value = "库存盘点实绩执行 已盘点校验")
    @GetMapping(value = "/counted-validation", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsMaterialLotAttrVO>> countedValidation(@PathVariable("organizationId") Long tenantId,
                                                                        @RequestParam @ApiParam(name = "盘点类型") String stocktakeTypeCode,
                                                                        WmsStocktakeValidationDTO dto) {
        this.validObject(dto);
        return Results.success(wmsStocktakeService.countedValidation(tenantId, stocktakeTypeCode, dto));
    }

    @ApiOperation(value = "库存盘点实绩执行 条码明细")
    @GetMapping(value = "/detail", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsStocktakeMaterialLotVO>> materialLotList(@PathVariable("organizationId") Long tenantId,
                                                                           @RequestParam @ApiParam(name = "盘点单ID") String stocktakeId,
                                                                           @RequestParam @ApiParam(name = "盘点类型") String stocktakeTypeCode) {
        return Results.success(stocktakeActualRepository.selectMaterialLotByType(tenantId, stocktakeId, stocktakeTypeCode));
    }

    @ApiOperation(value = "库存盘点实绩执行 盘点明细")
    @GetMapping(value = "/material-detail", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsStocktakeMaterialDetailVO>> materialDetail(@PathVariable("organizationId") Long tenantId,
                                                                             @RequestParam @ApiParam(name = "盘点单ID") String stocktakeId,
                                                                             @RequestParam @ApiParam(name = "盘点类型") String stocktakeTypeCode,
                                                                             @RequestParam(required = false) String materialCode) {
        return Results.success(wmsStocktakeService.stockDetailGet(tenantId, stocktakeId, stocktakeTypeCode, materialCode));
    }

    @ApiOperation(value = "库存盘点实绩执行 容器条码查询")
    @GetMapping(value = "/material-lot-in-container", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsStocktakeMaterialLotVO>> materialLotInContainerGet(@PathVariable("organizationId") Long tenantId,
                                                                                     @RequestParam(required = false) @ApiParam(name = "盘点单ID") String stocktakeId,
                                                                                     @RequestParam @ApiParam(name = "盘点类型") String stocktakeTypeCode,
                                                                                     @RequestParam @ApiParam(name = "容器ID") String containerId) {
        return Results.success(wmsStocktakeService.materialLotInContainerGet(tenantId, stocktakeTypeCode, stocktakeId, containerId));
    }

    @ApiOperation(value = "库存盘点执行 盘点提交")
    @PostMapping(value = "/submit", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Boolean> scan(@PathVariable("organizationId") Long tenantId,
                                        @RequestBody WmsStocktakeSubmitDTO dto) {
        this.validObject(dto);
        return Results.success(wmsStocktakeService.stocktakeSubmit(tenantId, dto));
    }

    @ApiOperation(value = "库存盘点执行 下达实际行补充")
    @PostMapping(value = "/released-actual", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsStocktakeActualVO>> insertReleasedActual(@PathVariable("organizationId") Long tenantId,
                                                                           @RequestBody WmsStocktakeValidationDTO dto) {
        this.validObject(dto);
        return Results.success(wmsStocktakeService.insertReleasedActual(tenantId, dto));
    }
}
