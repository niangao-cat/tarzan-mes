package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeCosGetChipPlatformService;
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

import javax.servlet.http.*;
import java.util.*;

/**
 * @Classname HmeCosGetChipController
 * @Description COS取片平台
 * @Date 2020/8/18 10:15
 * @Author yuchao.wang
 */
@Slf4j
@RestController("HmeCosGetChipPlatformController.v1")
@RequestMapping("/v1/{organizationId}/cos-get-chip-platform")
@Api(tags = SwaggerApiConfig.HME_COS_GET_CHIP_PLATFORM)
public class HmeCosGetChipPlatformController {

    @Autowired
    private HmeCosGetChipPlatformService hmeCosGetChipPlatformService;

    @ApiOperation(value = "待取片容器进站条码扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/scan-barcode")
    public ResponseEntity<?> scanBarcode(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeCosGetChipScanBarcodeDTO dto) {
        log.info("<====HmeCosGetChipPlatformController.scanBarcode:{}，{}", tenantId, dto);
        return Results.success(hmeCosGetChipPlatformService.scanBarcode(tenantId, dto));
    }

    @ApiOperation(value = "扫描条码确认")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/fetch-in-confirm")
    public ResponseEntity<?> fetchInConfirm(@PathVariable("organizationId") Long tenantId,
                                           @RequestBody HmeCosGetChipSiteInConfirmDTO dto) {
        log.info("<====HmeCosGetChipPlatformController.fetchInConfirm:{}，{}", tenantId, dto);
        hmeCosGetChipPlatformService.fetchInConfirm(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "新增")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/fetch-out-created")
    public ResponseEntity<?> fetchOutCreated(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody HmeCosGetChipSiteOutConfirmDTO dto) {
        log.info("<====HmeCosGetChipPlatformController.fetchOutCreated:{}，{}", tenantId, dto);
        return Results.success(hmeCosGetChipPlatformService.fetchOutCreated(tenantId, dto));
    }

    @ApiOperation(value = "查询容器对应最大装载数量")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/load-number/max/query")
    public ResponseEntity<?> queryMaxLoadNumber(@PathVariable("organizationId") Long tenantId, HmeCosGetChipMaxLoadDTO dto) {
        log.info("<====HmeCosGetChipPlatformController.queryMaxLoadNumber:{}，{}", tenantId, dto);
        return Results.success(hmeCosGetChipPlatformService.queryMaxLoadNumber(tenantId, dto));
    }

    @ApiOperation(value = "查询进行中数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-processing")
    public ResponseEntity<?> queryProcessing(@PathVariable("organizationId") Long tenantId, HmeCosGetChipSiteOutQueryDTO dto) {
        log.info("<====HmeCosGetChipPlatformController.queryProcessing:{}，{}", tenantId, dto);
        return Results.success(hmeCosGetChipPlatformService.queryProcessing(tenantId, dto));
    }

    @ApiOperation(value = "出站完成")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/fetch-out")
    public ResponseEntity<?> fetchOut(@PathVariable("organizationId") Long tenantId,
                                      @RequestBody HmeCosGetChipSiteOutPrintDTO dto,
                                      HttpServletResponse response) {
        log.info("<====HmeCosGetChipPlatformController.fetchOut:{}，{}", tenantId, dto);
        return Results.success(hmeCosGetChipPlatformService.fetchOut(tenantId, dto, response));
    }

    @ApiOperation(value = "打印")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/fetch-out-print")
    public ResponseEntity<?> fetchOutPrint(@PathVariable("organizationId") Long tenantId,
                                           @RequestBody List<String> materialLotId,
                                           HttpServletResponse response) {
        log.info("<====HmeCosGetChipPlatformController.fetchOutPrint:{}，{}", tenantId, materialLotId);
        hmeCosGetChipPlatformService.printPdf(tenantId, materialLotId, response);
        return Results.success();
    }

    @ApiOperation(value = "批量删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/fetch-out-delete")
    public ResponseEntity<HmeCosGetChipDeleteDTO> batchDelete(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody HmeCosGetChipDeleteDTO dto){
        return Results.success(hmeCosGetChipPlatformService.batchDelete(tenantId, dto));
    }

    @ApiOperation(value = "投入条码列表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-input-materiallot")
    public ResponseEntity<List<HmeCosGetChipMaterialLotListResponseDTO>> queryInputMaterialLotList(@PathVariable("organizationId") Long tenantId,
                                                                                             HmeCosGetChipMaterialLotListDTO dto){
        return Results.success(hmeCosGetChipPlatformService.queryInputMaterialLotList(tenantId, dto));
    }

    @ApiOperation(value = "芯片不良列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-nc/{materialLotId}")
    public ResponseEntity<List<HmeCosGetChipNcListDTO>> queryNcList(@PathVariable("organizationId") Long tenantId,
                                                                    @PathVariable("materialLotId") String materialLotId){
        return Results.success(hmeCosGetChipPlatformService.queryNcList(tenantId, materialLotId));
    }
}