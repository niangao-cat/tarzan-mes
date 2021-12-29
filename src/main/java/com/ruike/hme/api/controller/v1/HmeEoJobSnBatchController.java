package com.ruike.hme.api.controller.v1;
import java.util.List;

import com.ruike.hme.api.dto.HmeEoJobSnBatchDTO2;
import com.ruike.hme.api.dto.HmeEoJobSnBatchDTO3;
import com.ruike.hme.api.dto.HmeEoJobSnBatchDTO4;
import com.ruike.hme.app.service.HmeEoJobSnBatchOutSiteService;
import com.ruike.hme.app.service.HmeEoJobSnBatchService;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
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

import javax.servlet.http.HttpServletResponse;

/**
 * 批量工序作业平台-SN作业 管理 API
 *
 * @author penglin.sui@hand-china.com 2020-11-12 16:30:00
 */
@Slf4j
@RestController("HmeEoJobSnBatchController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-sn-batch")
@Api(tags = SwaggerApiConfig.HME_EO_JOB_SN_BATCH)
public class HmeEoJobSnBatchController extends BaseController {

    @Autowired
    private HmeEoJobSnBatchService service;

    @Autowired
    private HmeEoJobSnBatchOutSiteService outSiteService;

    @ApiOperation(value = "进站扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/in-site-scan")
    public ResponseEntity<HmeEoJobSnVO> inSiteScan(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnBatchController.inSiteScan tenantId={},dto={}", tenantId, dto);
        long startDate = System.currentTimeMillis();
        HmeEoJobSnVO hmeEoJobSnVO = this.service.inSiteScan(tenantId, dto);
        log.info("=================================>批量工序作业平台-进站总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return Results.success(hmeEoJobSnVO);
    }

    @ApiOperation(value = "出站扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/out-site-scan")
    public ResponseEntity<?> outSiteScan(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnBatchController.outSiteScan tenantId={},dto={}", tenantId, dto);
        long startDate = System.currentTimeMillis();
        List<HmeEoJobSn> hmeEoJobSnList = outSiteService.outSiteScan(tenantId, dto);
        log.info("=================================>批量工序作业平台-出站总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return Results.success(hmeEoJobSnList);
    }

    @ApiOperation(value = "进站查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/in-site-query")
    public ResponseEntity<HmeEoJobSnVO> inSiteQuery(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnBatchController.inSiteQuery tenantId={},dto={}", tenantId, dto);
        return Results.success(this.service.inSiteQuery(tenantId, dto));
    }

    @ApiOperation(value = "投料查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release-query")
    public ResponseEntity<List<HmeEoJobSnBatchVO4>> releaseQuery(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody HmeEoJobSnBatchDTO3 dto) {
        log.info("<====== HmeEoJobSnBatchController.releaseQuery tenantId={},dto={}", tenantId, dto);
        long startDate = System.currentTimeMillis();
        List<HmeEoJobSnBatchVO4> hmeEoJobSnBatchVO4List = this.service.releaseQuery(tenantId, dto);
        log.info("=================================>批量工序作业平台-投料查询总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return Results.success(hmeEoJobSnBatchVO4List);
    }

    @ApiOperation(value = "条码绑定")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release-scan")
    public ResponseEntity<HmeEoJobSnBatchVO14> releaseScan(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody HmeEoJobSnBatchDTO2 dto) {
        log.info("<====== HmeEoJobSnBatchController.releaseScan tenantId={},dto={}", tenantId, dto);
        return Results.success(this.service.releaseScan(tenantId, dto));
    }

    @ApiOperation(value = "删除物料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/delete-material")
    public ResponseEntity<HmeEoJobSnBatchVO4> deleteMaterial(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody HmeEoJobSnBatchVO4 dto) {
        log.info("<====== HmeEoJobSnBatchController.deleteMaterial tenantId={},dto={}", tenantId, dto);
        return Results.success(this.service.deleteMaterial(tenantId,dto));
    }
    @ApiOperation(value = "批量更新是否投料标识")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-update-is-released")
    public ResponseEntity<List<HmeEoJobSnBatchVO6>> batchUpdateIsReleased(@PathVariable("organizationId") Long tenantId,
                                                                           @RequestBody List<HmeEoJobSnBatchVO6> dtoList) {
        log.info("<====== HmeEoJobSnBatchController.batchUpdateIsReleased tenantId={},dtoList={}", tenantId, dtoList);
        return Results.success(this.service.batchUpdateIsReleased(tenantId, dtoList));
    }
    @ApiOperation(value = "投料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release")
    public ResponseEntity<List<HmeEoJobSnBatchVO4>> release(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody HmeEoJobSnBatchDTO4 dto) {
        log.info("<====== HmeEoJobSnBatchController.release tenantId={},dto={}", tenantId, dto);
        long startDate = System.currentTimeMillis();
        List<HmeEoJobSnBatchVO4> hmeEoJobSnBatchVO4List = this.service.release(tenantId, dto);
        log.info("=================================>批量工序作业平台-投料总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return Results.success(hmeEoJobSnBatchVO4List);
    }

    @ApiOperation(value = "条码打印")
    @PostMapping(value = {"/barcode-print/{type}"})
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> barcodePrint(@PathVariable("organizationId") Long tenantId, @PathVariable("type") String type,
                                                @RequestBody List<String> materialLotCodeList, HttpServletResponse response) {
        log.info("<==== HmeEoJobSnBatchController-barcodePrint info:{},{},{}", tenantId, type, materialLotCodeList);
        try {
            service.materialLotCodePrint(tenantId, type, materialLotCodeList, response);
        } catch (Exception ex) {
            log.error("<==== HmeEoJobSnBatchController-barcodePrint error: {}:{}", ex.getMessage(), ex);
        }
        return Results.success();
    }

    @ApiOperation(value = "投料退回")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release-back")
    public ResponseEntity<HmeEoJobSnVO9> releaseBack(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody HmeEoJobSnVO9 dto) {
        log.info("<====== HmeEoJobSnBatchController.releaseBack tenantId={},dto={}", tenantId, dto);
        long startDate = System.currentTimeMillis();
        HmeEoJobSnVO9 resultVO = this.service.releaseBack(tenantId, dto);
        log.info("<====== 批量工序作业平台投料退回总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return Results.success(resultVO);
    }
}
