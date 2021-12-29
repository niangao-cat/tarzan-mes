package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEoJobPumpCombDTO;
import com.ruike.hme.api.dto.HmeEoJobPumpCombDTO2;
import com.ruike.hme.api.dto.HmeEoJobPumpCombDTO3;
import com.ruike.hme.api.dto.HmeEoJobSnBatchDTO2;
import com.ruike.hme.app.service.HmeEoJobPumpCombService;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeEoJobPumpComb;
import com.ruike.hme.domain.repository.HmeEoJobPumpCombRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 泵浦源工序作业平台 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-08-23 10:34:03
 */
@RestController("hmeEoJobPumpCombController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-pump-combs")
@Slf4j
public class HmeEoJobPumpCombController extends BaseController {

    @Autowired
    private HmeEoJobPumpCombService hmeEoJobPumpCombService;

    @ApiOperation(value = "泵浦源工序作业平台-扫描工单")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scan-wo")
    public ResponseEntity<HmeEoJobPumpCombVO> scanWorkOrder(@PathVariable("organizationId") Long tenantId,
                                                            HmeEoJobPumpCombDTO dto) {
        HmeEoJobPumpCombVO result = hmeEoJobPumpCombService.scanWorkOrder(tenantId, dto);
        return Results.success(result);
    }

    @ApiOperation("进站标签打印")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/site-in-print")
    public ResponseEntity<?> siteInPrint(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEoJobPumpCombDTO2 dto,
                                         HttpServletResponse response) {
        try {
            hmeEoJobPumpCombService.siteInPrint(tenantId, dto, response);
        } catch (Exception ex) {
            log.error("<==== HmeEoJobPumpCombController-siteInPrint error: {}:{}", ex.getMessage(), ex);
        }
        return Results.success();
    }

    @ApiOperation("条码扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release-scan")
    public ResponseEntity<HmeEoJobSnBatchVO14> releaseScan(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody HmeEoJobSnBatchDTO2 dto) {
        log.info("<====== HmeEoJobPumpCombController.releaseScan tenantId={},dto={}", tenantId, dto);
        return Results.success(hmeEoJobPumpCombService.releaseScan(tenantId, dto));
    }

    @ApiOperation("组合子条码打印")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/subbarcode-print")
    public ResponseEntity<?> subBarcodePrint(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEoJobPumpCombDTO3 dto,
                                         HttpServletResponse response) {
        try {
            hmeEoJobPumpCombService.subBarcodePrint(tenantId, dto, response);
        } catch (Exception ex) {
            log.error("<==== HmeEoJobPumpCombController-siteInPrint error: {}:{}", ex.getMessage(), ex);
        }
        return Results.success();
    }

    @ApiOperation(value = "投料退回")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release-back")
    public ResponseEntity<HmeEoJobSnVO9> releaseBack(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody HmeEoJobSnVO9 dto) {
        log.info("<====== HmeEoJobPumpCombController.releaseBack tenantId={},dto={}", tenantId, dto);
        long startDate = System.currentTimeMillis();
        HmeEoJobSnVO9 resultVO = hmeEoJobPumpCombService.releaseBack(tenantId, dto);
        log.info("<====== 泵浦源工序作业平台投料退回总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return Results.success(resultVO);
    }

    @ApiOperation(value = "出站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/out-site-scan")
    public ResponseEntity<?> outSiteScan(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobPumpCombController.outSiteScan tenantId={},dto={}", tenantId, dto);
        long startDate = System.currentTimeMillis();
        HmeEoJobSn hmeEoJobSn = hmeEoJobPumpCombService.outSiteScan(tenantId, dto);
        log.info("=================================>泵浦源工序作业-出炉总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return Results.success(hmeEoJobSn);
    }

    @ApiOperation(value = "删除物料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/delete-material")
    public ResponseEntity<HmeEoJobSnBatchVO4> deleteMaterial(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody HmeEoJobSnBatchVO4 dto) {
        log.info("<====== HmeEoJobPumpCombController.deleteMaterial tenantId={},dto={}", tenantId, dto);
        return Results.success(hmeEoJobPumpCombService.deleteMaterial(tenantId,dto));
    }

    @ApiOperation(value = "进站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/in-site-scan")
    public ResponseEntity<?> inSiteScan(@PathVariable("organizationId") Long tenantId,
                                        @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====HmeEoJobPumpCombController.inSiteScan:{}，{}", tenantId, dto);
        long startDate = System.currentTimeMillis();
        HmeEoJobSnVO hmeEoJobSnVO = hmeEoJobPumpCombService.inSiteScan(tenantId, dto);
        log.info("=================================>泵浦源工序作业平台-进站总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return Results.success(hmeEoJobSnVO);
    }
}
