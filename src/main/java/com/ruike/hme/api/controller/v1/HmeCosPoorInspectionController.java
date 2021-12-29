package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosGetChipScanBarcodeDTO;
import com.ruike.hme.api.dto.HmeCosPoorInspectionNcRecordDTO;
import com.ruike.hme.api.dto.HmeCosPoorInspectionScrappedDTO;
import com.ruike.hme.app.service.HmeCosPoorInspectionService;
import com.ruike.hme.domain.repository.HmeMaterialLotNcRecordRepository;
import com.ruike.hme.domain.vo.HmeCosEoJobSnSiteOutVO;
import com.ruike.hme.domain.vo.HmeMaterialLotNcRecordVO2;
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

import java.util.*;

/**
 * 来料目检平台芯片不良记录
 *
 * 作者：ruijie.wang01@hand-china.com
 * 时间：2020/8/19 9:46
 */
@RestController("hmeCosPoorInspectionController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-poor-inspection")
@Api(tags = SwaggerApiConfig.HME_COS_POOR_INSPECTION)
@Slf4j
public class HmeCosPoorInspectionController {

    @Autowired
    private HmeCosPoorInspectionService hmeCosPoorInspectionService;

    @Autowired
    private HmeMaterialLotNcRecordRepository hmeMaterialLotNcRecordRepository;

    @ApiOperation(value = "芯片不良记录功能-查询进行中数据")
    @GetMapping(value = "/query-processing", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> queryProcessing(@PathVariable("organizationId") Long tenantId, HmeCosGetChipScanBarcodeDTO dto){
        log.info("<====HmeCosPoorInspectionController.queryProcessing:{}，{}", tenantId, dto);
        return Results.success(hmeCosPoorInspectionService.queryProcessing(tenantId, dto));
    }

    @ApiOperation(value = "芯片不良记录功能-进站")
    @GetMapping(value = "/site-in", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> siteIn(@PathVariable("organizationId") Long tenantId, HmeCosGetChipScanBarcodeDTO dto){
        log.info("<====HmeCosPoorInspectionController.siteIn:{}，{}", tenantId, dto);
        return Results.success(hmeCosPoorInspectionService.siteIn(tenantId, dto));
    }

    @ApiOperation(value = "确认芯片不良代码记录")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/nc-record-confirm", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> ncRecordConfirm (@PathVariable("organizationId") Long tenantId,@RequestBody HmeCosPoorInspectionNcRecordDTO ncRecordDTOList) {
        log.info("<====HmeCosPoorInspectionController.ncRecordConfirm:{}，{}", tenantId, ncRecordDTOList);
        return Results.success(hmeCosPoorInspectionService.ncRecordConfirm(tenantId, ncRecordDTOList));
    }

    @ApiOperation(value = "取消芯片不良代码记录")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/nc-record-delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> ncRecordDelete (@PathVariable("organizationId") Long tenantId,@RequestBody List<HmeMaterialLotNcRecordVO2> materialLotNcList) {
        log.info("<====HmeCosPoorInspectionController.ncRecordDelete:{}，{}", tenantId, materialLotNcList);
        hmeCosPoorInspectionService.ncRecordDelete(tenantId, materialLotNcList);
        return Results.success();
    }

    @ApiOperation(value = "芯片不良记录功能-出站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/site-out", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> siteOut (@PathVariable("organizationId") Long tenantId, @RequestBody HmeCosEoJobSnSiteOutVO hmeCosEoJobSnSiteOutVO) {
        log.info("<====HmeCosPoorInspectionController.siteOut:{}，{}", tenantId, hmeCosEoJobSnSiteOutVO);
        hmeCosPoorInspectionService.siteOut(tenantId, hmeCosEoJobSnSiteOutVO);
        return Results.success();
    }

    @ApiOperation(value = "芯片不良记录功能-获取可选的不良代码")
    @GetMapping(value = "/get-nc-code", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> getNcCode(@PathVariable("organizationId") Long tenantId, @RequestParam String operationId){
        log.info("<====HmeCosPoorInspectionController.getNcCode:{}，{}", tenantId, operationId);
        return Results.success(hmeMaterialLotNcRecordRepository.queryNcCodeByOperationId(tenantId, operationId));
    }

    @ApiOperation(value = "芯片不良记录功能-报废")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/scrapped", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> scrapped (@PathVariable("organizationId") Long tenantId,
                                       @RequestBody HmeCosPoorInspectionScrappedDTO dto) {
        log.info("<====HmeCosPoorInspectionController.scrapped:{}，{}", tenantId, dto);
        hmeCosPoorInspectionService.scrapped(tenantId, dto);
        return Results.success();
    }
}
