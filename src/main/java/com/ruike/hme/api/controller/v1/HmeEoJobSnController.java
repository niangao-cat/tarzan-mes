package com.ruike.hme.api.controller.v1;

import java.util.List;

import com.ruike.hme.api.dto.HmeBackFlushDTO;
import com.ruike.hme.api.dto.HmeLocatorOnhandQuantityDTO;
import com.ruike.hme.domain.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;

import com.ruike.hme.app.service.HmeEoJobSnService;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;
import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;

import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;

/**
 * 工序作业平台-SN作业 管理 API
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 00:04:39
 */
@Slf4j
@RestController("hmeEoJobSnController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-sn")
@Api(tags = SwaggerApiConfig.HME_EO_JOB_SN)
public class HmeEoJobSnController extends BaseController {
    @Autowired
    private HmeEoJobSnService service;
    @Autowired
    private HmeEoJobSnRepository repository;

    @ApiOperation(value = "通用-工位扫描")
    @PostMapping(value = {"/workcell-scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEoJobSnVO4> workcellScan(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody HmeEoJobSnDTO dto) {
        log.info("<====== HmeEoJobSnController.workcellScan tenantId={},dto={}", tenantId, dto);
        return Results.success(repository.workcellScan(tenantId, dto));
    }

    @ApiOperation(value = "通用-进站扫描(入炉)")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/in-site-scan")
    public ResponseEntity<HmeEoJobSnVO> inSiteScan(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnController.inSiteScan tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.inSiteScan(tenantId, dto));
    }

    @ApiOperation(value = "通用-进站扫描工位检查(入炉)")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/in-site-scan-check")
    public ResponseEntity<HmeEoJobSnVO3> inSiteScanCheck(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnController.inSiteScan tenantId={},dto={}", tenantId, dto);
        return Results.success(repository.inSiteScanCheck(tenantId, dto));
    }

    @ApiOperation(value = "通用-获取指定工位未出站工序作业")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list-for-sn")
    public ResponseEntity<List<HmeEoJobSnVO>> listForSn(@PathVariable("organizationId") Long tenantId,
                                                      HmeEoJobSnDTO dto) {
        log.info("<====== HmeEoJobSnController.listForSn tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.querySnByWorkcell(tenantId, dto.getWorkcellId()));
}

    @Deprecated
    @ApiOperation(value = "通用-获取指定工位未出站时效工序作业")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list-for-time-sn")
    public ResponseEntity<HmeEoJobTimeSnVO4> listForTimeSn(@PathVariable("organizationId") Long tenantId,
                                                           HmeEoJobSnDTO dto) {
        log.info("<====== HmeEoJobSnController.listForTimeSn tenantId={},dto={}", tenantId, dto);
        return Results.success(repository.queryTimeSnByWorkcell(tenantId, dto.getWorkcellId(), dto.getOperationId()));
    }

    @ApiOperation(value = "通用-获取指定工位未出站时效工序作业-分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list-for-time-sn/page")
    public ResponseEntity<?> pageListForTimeSn(@PathVariable("organizationId") Long tenantId,
                                               HmeEoJobSnDTO dto,
                                               @ApiIgnore PageRequest pageRequest) {
        log.info("<====== HmeEoJobSnController.pageListForTimeSn tenantId={},dto={}", tenantId, dto);
        return Results.success(repository.queryPageTimeSnByWorkcell(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "时效作业平台-条码扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/time-sn-scan")
    public ResponseEntity<HmeEoJobTimeSnVO2> snScan(@PathVariable("organizationId") Long tenantId,
                                                    HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnController.snScan tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.timeSnScan(tenantId, dto));
    }

    @ApiOperation(value = "通用-出站扫描(出炉)")
    @PostMapping(value = {"/out-site-scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEoJobSn> outSiteScan(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnController.outSiteScan tenantId={},dto={}", tenantId, dto);
        return Results.success(repository.outSiteScan(tenantId, dto));
    }

    @ApiOperation(value = "批量出站扫描")
    @PostMapping(value = {"/batch-out-site-scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeEoJobSn>> batchOutSiteScan(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEoJobSnVO6 dto) {
        log.info("<====== HmeEoJobSnController.batchOutSiteScan tenantId={},dto={}", tenantId, dto);
        long startDate = System.currentTimeMillis();
        List<HmeEoJobSn> HmeEoJobSnList = repository.batchOutSiteScan(tenantId, dto);
        long endDate = System.currentTimeMillis();
        log.info("=================================>批量出站扫描总耗时："+(endDate - startDate) + "毫秒");
        return Results.success(HmeEoJobSnList);
    }

    @ApiOperation(value = "工单号LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/wo-lov")
    public ResponseEntity<Page<HmeWorkOrderVO>> workOrderUiQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                               HmeWorkOrderVO dto,
                                                               @ApiIgnore PageRequest pageRequest) {
        log.info("<====== HmeEoJobSnController.workOrderUiQuery tenantId={},dto={}", tenantId, dto);
        return Results.success(this.service.workOrderQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "预装物料LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/material-lov")
    public ResponseEntity<Page<HmePrepareMaterialVO>> materialUiQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                      HmeWorkOrderVO dto,
                                                                      @ApiIgnore PageRequest pageRequest) {
        log.info("<====== HmeEoJobSnController.materialUiQuery tenantId={},dto={}", tenantId, dto);
        return Results.success(this.service.materialQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "预装物料已完工查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/material-prepared")
    public ResponseEntity<HmePrepareMaterialVO> materialPreparedQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                      HmeWorkOrderVO dto) {
        log.info("<====== HmeEoJobSnController.materialPreparedQuery tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.materialPreparedQuery(tenantId, dto));
    }

    @ApiOperation(value = "通用-投料")
    @PostMapping(value = {"/release"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEoJobSnVO3> release(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnController.release tenantId={},dto={}", tenantId, dto);
        return Results.success(repository.release(tenantId, dto));
    }

    @ApiOperation(value = "通用-投料退回查询")
    @GetMapping(value = {"/release-back-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeEoJobSnVO9>> releaseBackQuery(@PathVariable("organizationId") Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnController.releaseBackQuery tenantId={},dto={}", tenantId, dto);
        return Results.success(repository.releaseBackQuery(tenantId, dto));
    }

    @ApiOperation(value = "投料退回")
    @PostMapping(value = {"/release-back"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeEoJobSnVO9>> releaseBack(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEoJobSnVO9 dto) {
        log.info("<====== HmeEoJobSnController.releaseBack tenantId={},dto={}", tenantId, dto);
        return Results.success(repository.releaseBack(tenantId, dto));
    }

    @ApiOperation(value = "刷新")
    @PostMapping(value = {"/refresh"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEoJobSnVO2> refresh(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnController.refresh tenantId={},dto={}", tenantId, dto);
        return Results.success(repository.refresh(tenantId, dto));
    }
    @ApiOperation(value = "库位LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/locator-lov")
    public ResponseEntity<Page<HmeModLocatorVO>> locatorLovQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                 HmeModLocatorVO dto,
                                                                 @ApiIgnore PageRequest pageRequest) {
        log.info("<====== HmeEoJobSnController.locatorLovQuery tenantId={},dto={}", tenantId, dto);
        return Results.success(this.service.locatorLovQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "库位现有量查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/locator-onhand-query")
    public ResponseEntity<Page<HmeLocatorOnhandQuantityVO>> locatorOnhandQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                               @RequestBody HmeLocatorOnhandQuantityDTO dto,
                                                                               @ApiIgnore PageRequest pageRequest) {
        log.info("<====== HmeEoJobSnController.locatorOnhandQuery tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.locatorOnhandQuantityQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "反冲料查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/back-flush-query")
    public ResponseEntity<Page<HmeBackFlushVO>> backFlushQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                               @RequestBody HmeBackFlushDTO dto,
                                                               @ApiIgnore PageRequest pageRequest) {
        log.info("<====== HmeEoJobSnController.backFlushQuery tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.backFlushQuery(tenantId, dto, pageRequest));
    }
}
