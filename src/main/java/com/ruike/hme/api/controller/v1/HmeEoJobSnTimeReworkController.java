package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.app.service.HmeEoJobSnTimeReworkService;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
import com.ruike.hme.domain.vo.HmeEoJobTimeSnVO2;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

/**
 * 时效返修作业平台-SN作业 管理 API
 *
 * @author yuchao.wang@hand-china.com 2021-01-26 15:04:39
 */
@Slf4j
@RestController("HmeEoJobSnTimeReworkController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-sn-time-rework")
@Api(tags = SwaggerApiConfig.HME_EO_JOB_SN_TIME_REWORK)
public class HmeEoJobSnTimeReworkController extends BaseController {

    @Autowired
    private HmeEoJobSnTimeReworkService service;

    @ApiOperation(value = "时效作业-入炉")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/in-site-scan")
    public ResponseEntity<?> inSiteScan(@PathVariable("organizationId") Long tenantId,
                                        @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnTimeReworkController.inSiteScan tenantId={},dto={}", tenantId, dto);
        service.inSiteScan(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "时效作业-出炉")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/out-site-scan")
    public ResponseEntity<?> outSiteScan(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnTimeReworkController.outSiteScan tenantId={},dto={}", tenantId, dto);
        return Results.success(service.outSiteScan(tenantId, dto));
    }

    @ApiOperation(value = "时效作业-继续返修")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/continue-rework")
    public ResponseEntity<?> continueRework(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnTimeReworkController.continueRework tenantId={},dto={}", tenantId, dto);
        service.continueRework(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "时效作业平台-条码扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/time-sn-scan")
    public ResponseEntity<HmeEoJobTimeSnVO2> snScan(@PathVariable("organizationId") Long tenantId,
                                                    HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnTimeReworkController.snScan tenantId={},dto={}", tenantId, dto);
        return Results.success(service.timeSnScan(tenantId, dto));
    }

    @ApiOperation(value = "未出站时效工序作业-分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list-for-time-sn/page")
    public ResponseEntity<?> pageListForTimeSn(@PathVariable("organizationId") Long tenantId,
                                               HmeEoJobSnDTO dto,
                                               @ApiIgnore PageRequest pageRequest) {
        log.info("<====== HmeEoJobSnTimeReworkController.pageListForTimeSn tenantId={},dto={}", tenantId, dto);
        return Results.success(service.queryPageTimeSnByWorkcell(tenantId, dto, pageRequest));
    }
}
