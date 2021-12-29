package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosReturnScanDTO;
import com.ruike.hme.app.service.HmeCosMaterialReturnService;
import com.ruike.hme.domain.vo.HmeCosMaterialReturnVO;
import com.ruike.hme.domain.vo.HmeCosMaterialReturnVO2;
import com.ruike.hme.domain.vo.HmeCosMaterialReturnVO3;
import com.ruike.hme.domain.vo.HmeCosScanBarcodeVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.inventory.domain.entity.MtMaterialLot;

import static tarzan.config.SwaggerApiConfig.HME_COS_MATERIAL_RETURN;

/**
 * @ClassName HmeCOSMaterialReturnController
 * @Description COS退料
 * @Author lkj
 * @Date 2020/12/11
 */
@Slf4j
@RestController("HmeCOSMaterialReturnController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-material-return")
@Api(tags = HME_COS_MATERIAL_RETURN)
public class HmeCosMaterialReturnController {

    private final HmeCosMaterialReturnService service;

    public HmeCosMaterialReturnController(HmeCosMaterialReturnService service) {
        this.service = service;
    }

    @ApiOperation(value = "COS退料-工单扫描")
    @PostMapping(value = "/cos/scan-work-order", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosMaterialReturnVO> scanWorkOrderNum(@PathVariable("organizationId") Long tenantId,
                                                                   @RequestParam("workOrderId") String workOrderId) {
        log.info("<====HmeCOSMaterialReturnController-scanWorkOrderNum:{}，{}", tenantId, workOrderId);
        HmeCosMaterialReturnVO vo = service.scanWorkOrderNum(tenantId, workOrderId);
        return Results.success(vo);
    }

    @ApiOperation(value = "COS退料-退料确认")
    @PostMapping(value = "/cos/material-return", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosMaterialReturnVO2> cosMaterialReturn(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody HmeCosMaterialReturnVO3 dto) {
        log.info("<====HmeCOSMaterialReturnController-cosMaterialReturn:{}，{}", tenantId, dto);
        return Results.success(service.cosMaterialReturn(tenantId, dto));
    }

    @ApiOperation(value = "COS退料-退料条码扫描")
    @GetMapping(value = "/cos/scan-material-lot", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosScanBarcodeVO> scanMaterialLot(@PathVariable("organizationId") Long tenantId,
                                                               HmeCosReturnScanDTO dto) {
        log.info("<====HmeCOSMaterialReturnController-scanMaterialLot:{}，{}", tenantId, dto);
        return Results.success(service.scanMaterialLot(tenantId, dto));
    }

    @ApiOperation(value = "COS退料-目标条码扫描")
    @GetMapping(value = "/cos/scan-target-material-lot", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<MtMaterialLot> scanTargetMaterialLot(@PathVariable("organizationId") Long tenantId,
                                                               @RequestParam("materialLotCode") String materialLotCode) {
        log.info("<====HmeCOSMaterialReturnController-scanTargetMaterialLot:{}，{}", tenantId, materialLotCode);
        return Results.success(service.scanTargetMaterialLot(tenantId, materialLotCode));
    }

}