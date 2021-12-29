package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosChipInputScanBarcodeDTO;
import com.ruike.hme.app.service.HmeCosChipInputService;
import com.ruike.hme.domain.vo.HmeCosChipInputVO;
import com.ruike.hme.domain.vo.HmeCosEoJobSnSiteOutVO;
import com.ruike.hme.domain.vo.HmeMaterialLotLoadVO3;
import io.choerodon.core.domain.Page;
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
import tarzan.inventory.domain.vo.MtMaterialLotVO3;

import java.util.List;

/**
 * @Classname HmeCosChipInputController
 * @Description COS芯片录入
 * @Date 2020/8/27 21:15
 * @Author yuchao.wang
 */
@Slf4j
@RestController("HmeCosChipInputController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-chip-input")
@Api(tags = SwaggerApiConfig.HME_COS_CHIP_INPUT)
public class HmeCosChipInputController {

    @Autowired
    private HmeCosChipInputService hmeCosChipInputService;

    @ApiOperation(value = "条码扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scan-barcode")
    public ResponseEntity<List<HmeCosChipInputVO>> scanBarcode(@PathVariable("organizationId") Long tenantId, HmeCosChipInputScanBarcodeDTO dto) {
        log.info("<====HmeCosChipInputController.scanBarcode:{}，{}", tenantId, dto);
        return Results.success(hmeCosChipInputService.scanBarcode(tenantId, dto));
    }

    @ApiOperation(value = "查询热沉号")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-hotSink")
    public ResponseEntity<List<HmeMaterialLotLoadVO3>> queryHotSink(@PathVariable("organizationId") Long tenantId, String materialLotId ) {
        log.info("<====HmeCosChipInputController.queryHotSink:{}，{}", tenantId, materialLotId);
        return Results.success(hmeCosChipInputService.queryHotsink(tenantId, materialLotId));
    }

    @ApiOperation(value = "热沉号保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save-hotSink")
    public ResponseEntity<?> saveHotSink(@PathVariable("organizationId") Long tenantId, HmeMaterialLotLoadVO3 dto) {
        log.info("<====HmeCosChipInputController.saveHotSink:{}，{}", tenantId, dto);
        hmeCosChipInputService.saveHotsink(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "出站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/site-out", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> siteOut (@PathVariable("organizationId") Long tenantId, @RequestBody HmeCosEoJobSnSiteOutVO hmeCosEoJobSnSiteOutVO) {
        log.info("<====HmeCosChipInputController.siteOut:{}，{}", tenantId, hmeCosEoJobSnSiteOutVO);
        hmeCosChipInputService.siteOut(tenantId, hmeCosEoJobSnSiteOutVO);
        return Results.success();
    }
}