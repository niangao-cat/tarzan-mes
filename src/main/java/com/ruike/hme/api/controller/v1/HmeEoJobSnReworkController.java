package com.ruike.hme.api.controller.v1;

import java.util.List;

import com.ruike.hme.api.dto.HmeEoJobSnReworkDTO;
import com.ruike.hme.api.dto.HmeEoJobSnReworkDTO2;
import com.ruike.hme.app.service.HmeEoJobSnReworkService;
import com.ruike.hme.domain.vo.*;
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

/**
 * 单件作业平台-自制件返修 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-11-21 00:04:39
 */
@Slf4j
@RestController("HmeEoJobSnReworkController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-sn-rework")
@Api(tags = SwaggerApiConfig.HME_EO_JOB_SN_REWORK)
public class HmeEoJobSnReworkController extends BaseController {

    @Autowired
    private HmeEoJobSnReworkService service;

    @ApiOperation(value = "自制件返修-进站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/in-site-scan")
    public ResponseEntity<?> inSiteScan(@PathVariable("organizationId") Long tenantId,
                                        @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnReworkController.inSiteScan tenantId={},dto={}", tenantId, dto);
        return Results.success(service.inSiteScan(tenantId, dto));
    }

    @ApiOperation(value = "自制件返修-出炉")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/out-site-scan")
    public ResponseEntity<?> outSiteScan(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnReworkController.outSiteScan tenantId={},dto={}", tenantId, dto);
        return Results.success(service.outSiteScan(tenantId, dto));
    }

    @ApiOperation(value = "自制件返修-投料")
    @PostMapping(value = {"/release"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeEoJobSnReworkVO>> release(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEoJobSnReworkVO4 dto) {
        log.info("<====== HmeEoJobSnReworkController.release tenantId={},dto={}", tenantId, dto);
        return Results.success(service.release(tenantId, dto));
    }

    @ApiOperation(value = "自制件返修-投料退回")
    @PostMapping(value = {"/release-back"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEoJobSnVO9> releaseBack(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEoJobSnVO9 dto) {
        log.info("<====== HmeEoJobSnReworkController.releaseBack tenantId={},dto={}", tenantId, dto);
        return Results.success(service.releaseBack(tenantId, dto));
    }

    @ApiOperation(value = "条码绑定")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release-scan")
    public ResponseEntity<HmeEoJobSnBatchVO14> releaseScan(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody HmeEoJobSnReworkDTO dto) {
        log.info("<====== HmeEoJobSnReworkController.releaseScan tenantId={},dto={}", tenantId, dto);
        return Results.success(this.service.releaseScan(tenantId, dto));
    }

    @ApiOperation(value = "自制件返修-投料记录报废")
    @PostMapping(value = {"/release-record-scrap"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEoJobSnVO9> releaseRecordScrap(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEoJobSnVO9 dto) {
        log.info("<====== HmeEoJobSnReworkController.releaseBack tenantId={},dto={}", tenantId, dto);
        return Results.success(service.releaseRecordScrap(tenantId, dto));
    }

    @ApiOperation(value = "通用-投料退回查询")
    @GetMapping(value = {"/release-back-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeEoJobSnVO9>> releaseBackQuery(@PathVariable("organizationId") Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnController.releaseBackQuery tenantId={},dto={}", tenantId, dto);
        return Results.success(service.releaseBackQuery(tenantId, dto));
    }

    @ApiOperation(value = "删除物料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/delete-material")
    public ResponseEntity<HmeEoJobSnBatchVO4> deleteMaterial(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody HmeEoJobSnBatchVO4 dto) {
        return Results.success(service.deleteMaterial(tenantId,dto));
    }

    @ApiOperation(value = "单件作业-工位绑定条码查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/wkc-bind-query")
    public ResponseEntity<?> wkcBindMaterialLotQuery(@PathVariable("organizationId") Long tenantId,
                                                     @RequestParam String workcellId,
                                                     @RequestParam String siteId) {
        return Results.success(service.wkcBindMaterialLotQuery(tenantId, workcellId, siteId));
    }
}
