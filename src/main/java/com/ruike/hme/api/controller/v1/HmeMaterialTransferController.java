package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeMaterialTransferDTO;
import com.ruike.hme.api.dto.HmeMaterialTransferDTO2;
import com.ruike.hme.api.dto.HmeMaterialTransferDTO3;
import com.ruike.hme.api.dto.HmeMaterialTransferDTO4;
import com.ruike.hme.app.service.HmeMaterialTransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * Iqc检验看板 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
 */
@RestController("hmeMaterialTransferController.v1")
@RequestMapping("/v1/{organizationId}/hme-material-transfers")
@Api(tags = SwaggerApiConfig.HME_MATERIAL_TRANSFER)
public class HmeMaterialTransferController extends BaseController {

    @Autowired
    private HmeMaterialTransferService hmeMaterialTransferService;

    @ApiOperation(value = "待转移区物料信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"transfer/list/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeMaterialTransferDTO2> transferMaterialLotGetForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                                             @RequestBody List<HmeMaterialTransferDTO> dtoList) {
        return Results.success(hmeMaterialTransferService.transferMaterialLotGet(tenantId, dtoList));
    }

    @ApiOperation(value = "目标区物料信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/target/list/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeMaterialTransferDTO4> targetMaterialLotGetForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                                         @RequestBody List<HmeMaterialTransferDTO> dtoList) {
        return Results.success(hmeMaterialTransferService.targetMaterialLotGet(tenantId, dtoList));
    }

    @ApiOperation(value = "转移确认")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/confirm/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeMaterialTransferDTO2> targetMaterialLotConfirmForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                                                 @RequestBody HmeMaterialTransferDTO3 targetDto) {
        return Results.success(hmeMaterialTransferService.targetMaterialLotConfirmForUi(tenantId, targetDto));
    }

    @ApiOperation(value = "目标区物料供应商批次信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/target/info/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeMaterialTransferDTO4> targetMaterialLotInfoGet(@PathVariable(value = "organizationId") Long tenantId,
                                                                            String materialLotCode) {
        return Results.success(hmeMaterialTransferService.targetMaterialLotInfoGet(tenantId, materialLotCode));
    }
//    @ApiOperation(value = "锁定")
//    @Permission(level = ResourceLevel.ORGANIZATION)
//    @GetMapping(value = {"/getLock"}, produces = "application/json;charset=UTF-8")
//    public ResponseData<Void> getLock(@PathVariable(value = "organizationId") Long tenantId, String materialLotCode) {
//        ResponseData<Void> responseData = new ResponseData<>();
//        try {
//            hmeMaterialTransferService.getLock(tenantId, materialLotCode);
//            responseData.setSuccess(true);
//        } catch (Exception ex) {
//            responseData.setSuccess(false);
//            responseData.setMessage(ex.getMessage());
//        }
//        return responseData;
//    }
//
//    @ApiOperation(value = "解除锁定")
//    @Permission(level = ResourceLevel.ORGANIZATION)
//    @GetMapping(value = {"/releaseLock"}, produces = "application/json;charset=UTF-8")
//    public ResponseData<Void> releaseLock(@PathVariable(value = "organizationId") Long tenantId, String materialLotCode) {
//        ResponseData<Void> responseData = new ResponseData<>();
//        try {
//            hmeMaterialTransferService.releaseLock(tenantId, materialLotCode);
//            responseData.setSuccess(true);
//        } catch (Exception ex) {
//            responseData.setSuccess(false);
//            responseData.setMessage(ex.getMessage());
//        }
//        return responseData;
//    }
}
