package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEoJobDataDefaultTagQueryDTO;
import com.ruike.hme.api.dto.HmeEoJobDataRecordQueryDTO;
import com.ruike.hme.api.dto.HmeEoJobSnTimeDTO;
import com.ruike.hme.app.service.HmeEoJobSnTimeService;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
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

import java.util.*;

/**
 * 时效作业平台-SN作业 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-11-03 00:04:39
 */
@Slf4j
@RestController("HmeEoJobSnTimeController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-sn-time")
@Api(tags = SwaggerApiConfig.HME_EO_JOB_SN_TIME)
public class HmeEoJobSnTimeController extends BaseController {

    @Autowired
    private HmeEoJobSnTimeService service;

    @ApiOperation(value = "时效作业-入炉")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/in-site-scan")
    public ResponseEntity<?> inSiteScan(@PathVariable("organizationId") Long tenantId,
                                        @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnTimeController.inSiteScan tenantId={},dto={}", tenantId, dto);
        service.inSiteScan(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "时效作业-出炉")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/out-site-scan")
    public ResponseEntity<?> outSiteScan(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnTimeController.outSiteScan tenantId={},dto={}", tenantId, dto);
        return Results.success(service.outSiteScan(tenantId, dto));
    }

    @ApiOperation(value = "时效作业-继续返修")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/continue-rework")
    public ResponseEntity<HmeEoJobSnTimeDTO> continueRework(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnTimeController.continueRework tenantId={},dto={}", tenantId, dto);
        return Results.success(service.continueRework(tenantId, dto));
    }

    @ApiOperation(value = "时效作业-查询数据采集项")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/eo-job-data-record/page")
    public ResponseEntity<?> queryDataRecord(@PathVariable("organizationId") Long tenantId,
                                             HmeEoJobDataRecordQueryDTO dto,
                                             @ApiIgnore PageRequest pageRequest) {
        log.info("<====== HmeEoJobSnTimeController.queryDataRecord tenantId={},dto={}", tenantId, dto);
        return Results.success(service.queryDataRecord(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "时效作业-保存数据采集项")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/eo-job-data-record/save")
    public ResponseEntity<?> saveDataRecord(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody List<HmeEoJobDataRecord> dto) {
        log.info("<====== HmeEoJobSnTimeController.saveDataRecord tenantId={},dto={}", tenantId, dto);
        service.saveDataRecord(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "时效作业-查询工艺默认数据项")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/default-data-tag/query")
    public ResponseEntity<?> queryDefaultDataTag(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody HmeEoJobDataDefaultTagQueryDTO dto) {
        log.info("<====== HmeEoJobSnTimeController.queryDefaultDataTag tenantId={},dto={}", tenantId, dto);
        return Results.success(service.queryDefaultDataTag(tenantId, dto));
    }

}
