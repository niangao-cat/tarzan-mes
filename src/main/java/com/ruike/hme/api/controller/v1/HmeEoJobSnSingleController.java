package com.ruike.hme.api.controller.v1;

import java.util.List;

import com.ruike.hme.api.dto.HmeEoJobSnSingleDTO;
import com.ruike.hme.app.service.HmeEoJobSnSingleInService;
import com.ruike.hme.app.service.HmeEoJobSnSingleService;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.HmeEoJobSnBatchVO4;
import com.ruike.hme.domain.vo.HmeEoJobSnVO;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
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
 * 单件作业平台-SN作业 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-11-21 00:04:39
 */
@Slf4j
@RestController("HmeEoJobSnSingleController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-sn-single")
@Api(tags = SwaggerApiConfig.HME_EO_JOB_SN_SINGLE)
public class HmeEoJobSnSingleController extends BaseController {

    @Autowired
    private HmeEoJobSnSingleService service;

    @Autowired
    private HmeEoJobSnSingleInService inService;

    @ApiOperation(value = "单件作业-进站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/in-site-scan")
    public ResponseEntity<?> inSiteScan(@PathVariable("organizationId") Long tenantId,
                                        @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnSingleController.inSiteScan tenantId={},dto={}", tenantId, dto);
        long startDate = System.currentTimeMillis();
        HmeEoJobSnVO hmeEoJobSnVO = inService.inSiteScan(tenantId, dto);
        log.info("=================================>单件作业-进站总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return Results.success(hmeEoJobSnVO);
    }

    @ApiOperation(value = "单件作业-出炉")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/out-site-scan")
    public ResponseEntity<?> outSiteScan(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnSingleController.outSiteScan tenantId={},dto={}", tenantId, dto);
        long startDate = System.currentTimeMillis();
        HmeEoJobSn hmeEoJobSn = service.outSiteScan(tenantId, dto);
        log.info("=================================>单件作业-出炉总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return Results.success(hmeEoJobSn);
    }

    @ApiOperation(value = "单件作业-工位绑定条码查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/wkc-bind-query")
    public ResponseEntity<?> wkcBindMaterialLotQuery(@PathVariable("organizationId") Long tenantId,
                                                      @RequestParam String workcellId,
                                                      @RequestParam String siteId) {
        return Results.success(service.wkcBindMaterialLotQuery(tenantId, workcellId, siteId));
    }

    @ApiOperation(value = "单件作业-投料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release")
    public ResponseEntity<?> release(@PathVariable("organizationId") Long tenantId,
                                      @RequestBody HmeEoJobSnSingleDTO dto) {
        log.info("<====== HmeEoJobSnSingleController.release tenantId={},dto={}", tenantId, dto);
        long startDate = System.currentTimeMillis();
        List<HmeEoJobSnBatchVO4> hmeEoJobSnBatchVO4List = service.release(tenantId, dto);
        log.info("=================================>单件作业-投料总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return Results.success(hmeEoJobSnBatchVO4List);
    }
}
