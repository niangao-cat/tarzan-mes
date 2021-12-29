package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsDistributionRevokeDTO;
import com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO;
import com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO4;
import com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO5;
import com.ruike.wms.app.service.WmsDistributionRevokeService;
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

import java.util.List;

/**
 * @ClassName WmsDistributionRevokeController
 * @Description 配送撤销
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/8 17:02
 * @Version 1.0
 **/
@RestController("WmsDistributionRevokeController.v1")
@RequestMapping("/v1/{organizationId}/wms-distribution-revoke-detail")
@Api(tags = SwaggerApiConfig.WMS_DISTRIBUTION_REVOKE)
@Slf4j
public class WmsDistributionRevokeController extends BaseController {

    @Autowired
    private WmsDistributionRevokeService wmsDistributionRevokeService;

    /**
     * @param tenantId
     * @param instructionDocNum
     * @return org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO>
     * @description 1.    配送单扫描
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/8 17:30
     **/
    @ApiOperation(value = "扫描配送单号")
    @GetMapping(value = {"/scan/instruction/doc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<WmsDistributionRevokeReturnDTO> scanInstructionDocNum(@PathVariable("organizationId") Long tenantId,
                                                                                String instructionDocNum) {
        WmsDistributionRevokeReturnDTO wmsDistributionRevokeReturnDTO = wmsDistributionRevokeService.scanInstructionDocNum(tenantId, instructionDocNum);
        return Results.success(wmsDistributionRevokeReturnDTO);
    }

    /**
     * @param tenantId
     * @param scanCode
     * @param instructionDocId
     * @return org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO4>
     * @description 扫描条码
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/9 17:33
     **/
    @ApiOperation(value = "扫描条码栏/物流器具栏")
    @GetMapping(value = {"/scan/code"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<WmsDistributionRevokeReturnDTO4> scanCode(@PathVariable("organizationId") Long tenantId,
                                                                    String scanCode,
                                                                    String instructionDocId) {
        WmsDistributionRevokeReturnDTO4 wmsDistributionRevokeReturnDTO4 = wmsDistributionRevokeService.scanCode(tenantId, scanCode, instructionDocId);
        return Results.success(wmsDistributionRevokeReturnDTO4);
    }

    /**
     * @param tenantId
     * @param locatorCode
     * @return org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO4>
     * @description 货位扫描逻辑
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/9 17:36
     **/
    @ApiOperation(value = "货位扫描逻辑")
    @GetMapping(value = {"/scan/locator"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<WmsDistributionRevokeReturnDTO4> scanLocatorCode(@PathVariable("organizationId") Long tenantId,
                                                                           String locatorCode) {
        WmsDistributionRevokeReturnDTO4 wmsDistributionRevokeReturnDTO4 = wmsDistributionRevokeService.scanLocatorCode(tenantId, locatorCode);
        return Results.success(wmsDistributionRevokeReturnDTO4);
    }

    /**
     * @param tenantId
     * @param instructionId
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO5>>
     * @description 明细界面
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/9 19:32
     **/
    @ApiOperation(value = "明细界面")
    @GetMapping(value = {"/instruction/detail"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<List<WmsDistributionRevokeReturnDTO5>> instructionDetail(@PathVariable("organizationId") Long tenantId,
                                                                                   String instructionId) {
        List<WmsDistributionRevokeReturnDTO5> wmsDistributionRevokeReturnDTO5s = wmsDistributionRevokeService.instructionDetail(tenantId, instructionId);
        return Results.success(wmsDistributionRevokeReturnDTO5s);
    }

    /**
     *@description 确认
     *@author wenzhang.yu@hand-china.com
     *@date 2020/9/9 19:44
     *@param tenantId
     *@param dto
     *@return org.springframework.http.ResponseEntity<?>
     **/
    @ApiOperation(value = "确认按钮")
    @PostMapping(value = {"/confirm"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<WmsDistributionRevokeDTO> confirm(@PathVariable("organizationId") Long tenantId,
                                     @RequestBody WmsDistributionRevokeDTO dto) {
        wmsDistributionRevokeService.confirm(tenantId, dto);
        return Results.success(dto);
    }
}
