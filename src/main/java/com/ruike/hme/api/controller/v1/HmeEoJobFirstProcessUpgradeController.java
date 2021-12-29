package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEoJobFirstProcessUpgradeSnDTO;
import com.ruike.hme.app.service.HmeEoJobFirstProcessUpgradeService;
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
 * @Classname HmeEoJobFirstProcessUpgradeController
 * @Description PDA首序SN升级
 * @Date 2020/9/3 10:53
 * @Author yuchao.wang
 */
@Slf4j
@RestController("HmeEoJobFirstProcessUpgradeController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-first-process-upgrade")
@Api(tags = SwaggerApiConfig.HME_EO_JOB_FIRST_PROCESS_UPGRADE)
public class HmeEoJobFirstProcessUpgradeController {

    @Autowired
    private HmeEoJobFirstProcessUpgradeService hmeEoJobFirstProcessUpgradeService;

    @ApiOperation(value = "底座条码扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/base-barcode-scan")
    public ResponseEntity<?> baseBarcodeScan(@PathVariable("organizationId") Long tenantId, String barcode) {
        log.info("<====HmeEoJobFirstProcessUpgradeController.baseBarcodeScan:{}，{}", tenantId, barcode);
        return Results.success(hmeEoJobFirstProcessUpgradeService.baseBarcodeScan(tenantId, barcode));
    }

    @ApiOperation(value = "SN条码升级")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/sn-upgrade")
    public ResponseEntity<?> snUpgrade(@PathVariable("organizationId") Long tenantId,
                                       @RequestBody HmeEoJobFirstProcessUpgradeSnDTO upgradeSnDTO) {
        log.info("<====HmeEoJobFirstProcessUpgradeController.snUpgrade:{}，{}", tenantId, upgradeSnDTO);
        hmeEoJobFirstProcessUpgradeService.snUpgrade(tenantId, upgradeSnDTO);
        return Results.success();
    }
}