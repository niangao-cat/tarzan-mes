package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeDataCollectHeaderService;
import com.ruike.hme.domain.vo.HmeDataCollectLineVO;
import com.ruike.hme.domain.vo.HmeDataCollectLineVO2;
import com.ruike.hme.domain.vo.HmeDataCollectLineVO3;
import com.ruike.hme.domain.vo.HmeDataCollectLineVO4;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.Map;

/**
 * 生产数据采集头表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2020-07-16 19:35:58
 */
@RestController("hmeDataCollectHeaderController.v1")
@RequestMapping("/v1/{organizationId}/hme-data-collect-headers")
@Api(tags = SwaggerApiConfig.HEM_DATA_COLLECT_HEADER)
public class HmeDataCollectHeaderController extends BaseController {

    @Autowired
    private HmeDataCollectHeaderService hmeDataCollectHeaderService;


    @ApiOperation(value = "生产数据采集列表")
    @GetMapping(value = "/data-collect-line-list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeDataCollectLineVO3> queryDataCollectLineList(
            @PathVariable("organizationId") Long tenantId, HmeDataCollectLineVO lineVO) {
        return Results.success(hmeDataCollectHeaderService.queryDataCollectLineList(tenantId, lineVO));
    }

    @ApiOperation(value = "判断SN物料 0-否 1-是")
    @GetMapping(value = "/sn-material-qty", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Map<String,Object>> querySnMaterialQty(
            @PathVariable("organizationId") Long tenantId, String materialId) {
        return Results.success(hmeDataCollectHeaderService.querySnMaterialQty(tenantId, materialId));
    }

    @ApiOperation(value = "更新行表信息")
    @PostMapping(value = "/update-data-collect-line-info", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Map<String, Object>> updateDataCollectLineInfo(
            @PathVariable("organizationId") Long tenantId, @RequestBody HmeDataCollectLineVO2 lineVo) {
        hmeDataCollectHeaderService.updateDataCollectLineInfo(tenantId, lineVo);
        return Results.success();
    }

    @ApiOperation(value = "更新头表信息")
    @PostMapping(value = "/update-data-collect-header-info", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Map<String,Object>> updateDataCollectHeaderInfo(
            @PathVariable("organizationId") Long tenantId, @RequestBody HmeDataCollectLineVO lineVO) {
        hmeDataCollectHeaderService.updateDataCollectHeaderInfo(tenantId, lineVO);
        return Results.success();
    }


    @ApiOperation(value = "生产数据采集-扫描工位编码")
    @GetMapping(value = "/workcell-code-scan", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeDataCollectLineVO4> workcellCodeScan(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestParam String workcellCode) {
        return Results.success(hmeDataCollectHeaderService.workcellCodeScan(tenantId, workcellCode));
    }
}
