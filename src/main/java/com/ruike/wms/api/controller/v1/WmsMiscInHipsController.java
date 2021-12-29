package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsLocatorPutInStorageDTO;
import com.ruike.wms.api.dto.WmsMiscInBarCodeDTO;
import com.ruike.wms.api.dto.WmsMiscInBarCodeDetailDTO;
import com.ruike.wms.api.dto.WmsMiscInCostCenterDTO;
import com.ruike.wms.app.service.WmsMiscInHipsService;
import io.choerodon.core.base.BaseController;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * @Classname MiscInHipsController
 * @Description 杂收PDA
 * @Date 2019/9/26 13:40
 * @Author zhihao.sang
 * @Moved by yifan.xiong@hand-china.com 2020-9-24 15:15:46
 */
@RestController("wmsMiscInHipsController.v1")
@RequestMapping("/v1/{organizationId}/wms-misc-in")
@Api(tags = SwaggerApiConfig.WMS_MISC_IN_HIPS)
@Slf4j
public class WmsMiscInHipsController extends BaseController {
    private static final String NO_PERMISSION = "NO_PERMISSION";

    @Autowired
    private WmsMiscInHipsService service;

    @ApiOperation("成本中心LOV选择")
    @GetMapping(value = {"/costCenterQuery"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsMiscInCostCenterDTO>> costCenterQuery(@PathVariable("organizationId") Long tenantId,
                                                                        @RequestParam(value = "costCenter", required = false) String costCenter) {
        log.info("<==== MiscInHipsController-costCenterQuery info:{},{}", tenantId, costCenter);

        return Results.success(service.costCenterQuery(tenantId, costCenter));
    }

    @ApiOperation("条码扫描查询")
    @GetMapping(value = {"/barCodeQuery"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsMiscInBarCodeDTO> barCodeQuery(@PathVariable("organizationId") Long tenantId,
                                                            @RequestParam(value = "barCode") String barCode) {
        log.info("<==== MiscInHipsController-barCodeQuery info:{},{}", tenantId, barCode);
        return Results.success(service.barCodeQuery(tenantId, barCode));
    }

    @ApiOperation("再利用杂收条码扫描查询")
    @GetMapping(value = {"/barCodeQueryReuse"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsMiscInBarCodeDTO> barCodeQueryReuse(@PathVariable("organizationId") Long tenantId,
                                                            @RequestParam(value = "barCode") String barCode) {
        log.info("<==== MiscInHipsController-barCodeQuery info:{},{}", tenantId, barCode);
        return Results.success(service.barCodeQueryReuse(tenantId, barCode));
    }

    @ApiOperation("杂收确认")
    @PostMapping(value = {"/miscInConfirm"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> miscInConfirm(@PathVariable("organizationId") Long tenantId,
                                      @RequestBody List<WmsMiscInBarCodeDTO> dtoList,
                                      @RequestParam(value = "enableFlag",required = true) Boolean enableFlag) {
        log.info("<==== MiscInHipsController-miscInConfirm info:{},{}", tenantId, dtoList.toString());
        service.miscInConfirm(tenantId, dtoList,enableFlag);
        return  Results.success("杂收成功!");

    }

    @ApiOperation("再利用杂收确认")
    @PostMapping(value = {"/miscInConfirmReuse"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> miscInConfirmReuse(@PathVariable("organizationId") Long tenantId,
                                           @RequestBody List<WmsMiscInBarCodeDTO> dtoList,
                                           @RequestParam(value = "enableFlag",required = true) Boolean enableFlag) {
        log.info("<==== MiscInHipsController-miscInConfirm info:{},{}", tenantId, dtoList.toString());
        service.miscInConfirmReuse(tenantId, dtoList,enableFlag);
        return  Results.success("杂收成功!");

    }

    @ApiOperation("条码查询明细")
    @PostMapping(value = {"/barCodeDetailQuery"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsMiscInBarCodeDTO> barCodeDetailQuery(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody WmsMiscInBarCodeDetailDTO barCodeDetailDTO) {
        log.info("<==== MiscInHipsController-barCodeDetailQuery info:{},{}", tenantId, barCodeDetailDTO.toString());
        return Results.success(service.barCodeDetailQuery(tenantId, barCodeDetailDTO));
    }

    @ApiOperation("货位扫描")
    @GetMapping(value = {"/locator"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsLocatorPutInStorageDTO> getLocator(@PathVariable("organizationId") Long tenantId,
                                                                @RequestParam(value = "siteId", required = true) String siteId,
                                                                @RequestParam(value = "locatorCode", required = true) String locatorCode) {
        log.info("<==== MiscInHipsController-getLocator info:{},{},{}", tenantId, siteId, locatorCode);
        return Results.success(service.getLocator(tenantId, siteId, locatorCode));
    }
}
