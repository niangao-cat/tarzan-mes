package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.repository.HmeReportSetupRepository;
import com.ruike.hme.domain.vo.HmeReportSetupVO;
import com.ruike.hme.domain.vo.HmeReportSetupVO2;
import com.ruike.hme.domain.vo.HmeReportSetupVO3;
import com.ruike.hme.domain.vo.HmeReportSetupVO4;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * 看板配置基础数据表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2020-10-22 09:43:00
 */
@RestController("hmeReportSetupController.v1")
@RequestMapping("/v1/{organizationId}/hme-report-setups")
@Api(tags = SwaggerApiConfig.HME_REPORT_SETUP)
public class HmeReportSetupController extends BaseController {

    @Autowired
    private HmeReportSetupRepository hmeReportSetupRepository;

    @ApiOperation(value = "看板类型列表")
    @GetMapping(value = {"/query-report-type-list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeReportSetupVO>> queryReportTypeList(@PathVariable("organizationId") Long tenantId) {
        return Results.success(hmeReportSetupRepository.reportTypeList(tenantId));
    }

    @ApiOperation(value = "看板基础数据列表")
    @GetMapping(value = {"/query-report-setups-list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeReportSetupVO2>> queryReportSetupsList(@PathVariable("organizationId") Long tenantId,
                                                                         String reportType,
                                                                         @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeReportSetupRepository.queryReportSetupsList(tenantId, reportType, pageRequest));
    }

    @ApiOperation(value = "新增&编辑基础数据")
    @PostMapping(value = {"/save-report-setups"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeReportSetupVO2> saveReportSetups(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody HmeReportSetupVO2 setupVO2) {
        return Results.success(hmeReportSetupRepository.saveReportSetups(tenantId, setupVO2));
    }


    @ApiOperation(value = "删除基础数据")
    @DeleteMapping(value = {"/delete-report-setups"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> deleteReportSetups(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody HmeReportSetupVO2 setupVO2) {
        hmeReportSetupRepository.deleteReportSetups(tenantId, setupVO2);
        return Results.success();
    }

    @ApiOperation(value = "查询站点中英文")
    @GetMapping(value = {"/query-site-name"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeReportSetupVO3> querySiteName(@PathVariable("organizationId") Long tenantId,
                                                                          String siteId) {
        return Results.success(hmeReportSetupRepository.querySiteName(tenantId, siteId));
    }


    @ApiOperation(value = "产量可视化监控系统")
    @GetMapping(value = {"/prod-vision-monitor-system-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeReportSetupVO4>> queryProdVisionMonitorSystem(@PathVariable("organizationId") Long tenantId,
                                                                                String siteId,
                                                                                @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeReportSetupRepository.queryProdVisionMonitorSystem(tenantId, siteId, pageRequest));
    }


}
