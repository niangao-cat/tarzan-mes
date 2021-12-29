package com.ruike.qms.api.controller.v1;

import com.ruike.hme.domain.vo.HmeChipTransferVO4;
import com.ruike.qms.domain.repository.QmsQcDocMaterialLotRelRepository;
import com.ruike.qms.domain.vo.QmsDocMaterialLotVO;
import com.ruike.qms.domain.vo.QmsDocMaterialLotVO2;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

/**
 * 二次送检条码 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-24 17:28:04
 */
@RestController("qmsQcDocMaterialLotRelController.v1")
@RequestMapping("/v1/{organizationId}/qms-qc-doc-material-lot-rel")
@Api(tags = SwaggerApiConfig.QMS_QC_DOC_MATERIAL_LOT_REL)
public class QmsQcDocMaterialLotRelController extends BaseController {

    @Autowired
    private QmsQcDocMaterialLotRelRepository qmsQcDocMaterialLotRelRepository;


    @ApiOperation(value = "扫描物料条码")
    @GetMapping(value = {"/scan-material-lot"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<QmsDocMaterialLotVO> scanMaterialLot(@PathVariable("organizationId") Long tenantId, @RequestParam("materialLotCode") String materialLotCode, @RequestParam("instructionDocNum") String instructionDocNum) {
        return Results.success(qmsQcDocMaterialLotRelRepository.scanMaterialLot(tenantId, materialLotCode, instructionDocNum));
    }

    @ApiOperation(value = "删除条码")
    @PostMapping(value = {"/delete-material-lot-code"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<QmsDocMaterialLotVO> deleteMaterialLotCode(@PathVariable("organizationId") Long tenantId, @RequestBody HmeChipTransferVO4 vo4) {
        qmsQcDocMaterialLotRelRepository.deleteMaterialLotCode(tenantId, vo4);
        return Results.success();
    }

    @ApiOperation(value = "确认")
    @PostMapping(value = {"/confirm-material-lot-code"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<QmsDocMaterialLotVO> confirmMaterialLotCode(@PathVariable("organizationId") Long tenantId, @RequestBody QmsDocMaterialLotVO2 vo2) {
        qmsQcDocMaterialLotRelRepository.confirmMaterialLotCode(tenantId, vo2);
        return Results.success();
    }

    @ApiOperation(value = "提交")
    @PostMapping(value = {"/submit-material-lot-code"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<QmsDocMaterialLotVO2> submitMaterialLotCode(@PathVariable("organizationId") Long tenantId, @RequestBody QmsDocMaterialLotVO2 vo2) {
        return Results.success(qmsQcDocMaterialLotRelRepository.submitMaterialLotCode(tenantId, vo2));
    }
}
