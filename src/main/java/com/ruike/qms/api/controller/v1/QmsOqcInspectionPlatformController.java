package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.QmsOqcInspectionSaveDTO;
import com.ruike.qms.app.service.QmsOqcInspectionPlatformService;
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

/**
 * @Classname QmsOqcInspectionPlatformController
 * @Description OQC检验平台
 * @Date 2020/8/28 14:35
 * @Author yuchao.wang
 */
@Slf4j
@RestController("QmsOqcInspectionPlatformController.v1")
@RequestMapping("/v1/{organizationId}/qms-oqc-inspection-platform")
@Api(tags = SwaggerApiConfig.QMS_OQC_INSPECTION_PLATFORM)
public class QmsOqcInspectionPlatformController {

    @Autowired
    private QmsOqcInspectionPlatformService qmsOqcInspectionPlatformService;

    @ApiOperation(value = "条码扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scan-barcode")
    public ResponseEntity<?> scanBarcode(@PathVariable("organizationId") Long tenantId, String scanBarcode) {
        log.info("<====QmsOqcInspectionPlatformController.scanBarcode:{}，{}", tenantId, scanBarcode);
        return Results.success(qmsOqcInspectionPlatformService.scanBarcode(tenantId, scanBarcode));
    }

    @ApiOperation(value = "单据创建")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/doc-create")
    public ResponseEntity<?> docCreate(@PathVariable("organizationId") Long tenantId, String scanBarcode) {
        log.info("<====QmsOqcInspectionPlatformController.docCreate:{}，{}", tenantId, scanBarcode);
        return Results.success(qmsOqcInspectionPlatformService.docCreate(tenantId, scanBarcode));
    }

    @ApiOperation(value = "单据保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/doc-save")
    public ResponseEntity<?> docSave(@PathVariable("organizationId") Long tenantId,
                                     @RequestBody QmsOqcInspectionSaveDTO oqcInspectionSaveDTO) {
        log.info("<====QmsOqcInspectionPlatformController.docSave:{}，{}", tenantId, oqcInspectionSaveDTO);
        qmsOqcInspectionPlatformService.docSave(tenantId, false, oqcInspectionSaveDTO);
        return Results.success();
    }

    @ApiOperation(value = "单据保存PDA版")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/doc-save/pda")
    public ResponseEntity<QmsOqcInspectionSaveDTO> docSavePda(@PathVariable("organizationId") Long tenantId,
                                     @RequestBody QmsOqcInspectionSaveDTO oqcInspectionSaveDTO) {
        log.info("<====QmsOqcInspectionPlatformController.docSave:{}，{}", tenantId, oqcInspectionSaveDTO);
        qmsOqcInspectionPlatformService.docSave(tenantId, false, oqcInspectionSaveDTO);
        return Results.success(oqcInspectionSaveDTO);
    }

    @ApiOperation(value = "单据提交")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/doc-submit")
    public ResponseEntity<?> docSubmit(@PathVariable("organizationId") Long tenantId,
                                       @RequestBody QmsOqcInspectionSaveDTO oqcInspectionSaveDTO) {
        log.info("<====QmsOqcInspectionPlatformController.docSubmit:{}，{}", tenantId, oqcInspectionSaveDTO);
        qmsOqcInspectionPlatformService.docSave(tenantId, true, oqcInspectionSaveDTO);
        return Results.success();
    }

    @ApiOperation(value = "单据提交PDA版")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/doc-submit/pda")
    public ResponseEntity<QmsOqcInspectionSaveDTO> docSubmitPda(@PathVariable("organizationId") Long tenantId,
                                       @RequestBody QmsOqcInspectionSaveDTO oqcInspectionSaveDTO) {
        log.info("<====QmsOqcInspectionPlatformController.docSubmit:{}，{}", tenantId, oqcInspectionSaveDTO);
        qmsOqcInspectionPlatformService.docSave(tenantId, true, oqcInspectionSaveDTO);
        return Results.success(oqcInspectionSaveDTO);
    }
}