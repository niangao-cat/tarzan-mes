package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsMaterialOnShelfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.tarzan.common.domain.sys.ResponseData;
import lombok.extern.slf4j.Slf4j;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;

/**
 * 物料上架功能
 *
 * @author jiangling.zheng@hand-china.com 2020/06/09 14:51
 */
@RestController("wmsMaterialOnShelfController.v1")
@RequestMapping("/v1/{organizationId}/material-on-shelf")
@Api(tags = SwaggerApiConfig.WMS_MATERIAL_ON_SHELF)
@Slf4j
public class WmsMaterialOnShelfController extends BaseController {

    @Autowired
    private WmsMaterialOnShelfService wmsMaterialOnShelfService;

    @ApiOperation(value = "扫描送货单(查询)")
    @GetMapping(value = "/instruction-doc", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsMaterialOnShelfDocDTO> queryInstructionDocByNum(@PathVariable("organizationId") Long tenantId,
                                                                           @ApiParam(value = "扫描单据", required = true) @RequestParam String instructionDocNum) {
        log.info("<====WmsMaterialOnShelfController-queryInstructionDocByNum:{},{}",tenantId, instructionDocNum);
        return Results.success(wmsMaterialOnShelfService.queryInstructionDocByNum(tenantId, instructionDocNum));
    }

    @ApiOperation(value = "扫描条码(查询)")
    @PostMapping(value = "/barcode", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsMaterialOnShelfDTO> queryBarcode(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody WmsMaterialOnShelfBarCodeDTO2 dto) {
        log.info("<====WmsMaterialOnShelfController-queryBarcode:{},{}", tenantId, dto);
        return Results.success(wmsMaterialOnShelfService.queryBarcode(tenantId, dto));
    }

    @ApiOperation(value = "扫描库位(查询)")
    @PostMapping(value = "/locator", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsMaterialOnShelfDTO> queryLocatorByCode(@PathVariable("organizationId") Long tenantId,
                                                                  @ApiParam(value = "扫描货位", required = true) @RequestParam String locatorCode,
                                                                  @RequestBody WmsMaterialOnShelfDTO dto) {
        log.info("<====WmsMaterialOnShelfController-queryLocatorByCode:{}，{},{}", tenantId, dto);
        return Results.success(wmsMaterialOnShelfService.queryLocatorByCode(tenantId, locatorCode, dto));
    }

    @ApiOperation(value = "确认物料上架")
    @PostMapping(value = "/execute", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsMaterialOnShelfExecuteDTO3>> execute(@PathVariable("organizationId") Long tenantId,
                                     @RequestBody List<WmsMaterialOnShelfExecuteDTO3> dtoList) {
        log.info("<====PutInStorageHipsController-execute:{}，{}", tenantId, dtoList);
        return Results.success(wmsMaterialOnShelfService.execute(tenantId, dtoList));
    }

    @ApiOperation(value = "明细")
    @PostMapping(value = "/detail", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsMaterialOnShelfBarCodeDTO>> detail(@PathVariable("organizationId") Long tenantId,
                                                             @ApiParam(value = "单据行ID", required = true) @RequestParam String instructionId) {
        log.info("<====WmsMaterialOnShelfController-detail:{}，{},{}", tenantId, instructionId);
        return Results.success(wmsMaterialOnShelfService.detail(tenantId, instructionId));
    }
}
