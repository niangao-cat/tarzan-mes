package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeTimeProcessPdaService;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO2;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO4;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * COS作业平台
 *
 * @author chaonan.hu@hand-china.com 2020-08-19 13:42:11
 **/
@RestController("hmeTimeProcessPdaController.v1")
@RequestMapping("/v1/{organizationId}/hme-time-process-pda")
@Api(tags = SwaggerApiConfig.HME_TIME_PROCESS_PDA)
public class HmeTimeProcessPdaController extends BaseController {

    @Autowired
    private HmeTimeProcessPdaService hmeTimeProcessPdaService;

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping(value = "/user/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<MtUserInfo> getCurrentUser(@PathVariable(value = "organizationId") Long tenantId){
        return Results.success(hmeTimeProcessPdaService.getCurrentUser(tenantId));
    }

    @ApiOperation(value = "设备查询")
    @GetMapping(value = "/equipment/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeTimeProcessPdaVO4>> equipmentQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                     HmeTimeProcessPdaDTO5 dto){
        return Results.success(hmeTimeProcessPdaService.equipmentQuery(tenantId, dto));
    }

    @ApiOperation(value = "默认设备查询")
    @GetMapping(value = "/defect/equipment/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeTimeProcessPdaVO4> defectEquipmentQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                     HmeTimeProcessPdaDTO5 dto){
        return Results.success(hmeTimeProcessPdaService.defectEquipmentQuery(tenantId, dto));
    }

    @ApiOperation(value = "扫描设备编码")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/scan/equipment", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeTimeProcessPdaVO> scanEquipment(@PathVariable("organizationId") Long tenantId,
                                                             HmeTimeProcessPdaDTO dto) {
        return Results.success(hmeTimeProcessPdaService.scanEquipment(tenantId, dto));
    }

    @ApiOperation(value = "扫描实物条码")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/scan/bar", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeTimeProcessPdaVO2> scanBarcode(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody HmeTimeProcessPdaDTO2 dto) {
        return Results.success(hmeTimeProcessPdaService.scanBarcode(tenantId, dto));
    }

    @ApiOperation(value = "进站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/site/in", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeTimeProcessPdaDTO3> siteIn(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody HmeTimeProcessPdaDTO3 dto) {
        return Results.success(hmeTimeProcessPdaService.siteIn(tenantId, dto));
    }

    @ApiOperation(value = "出站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/site/out", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeTimeProcessPdaDTO4> siteOut(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody HmeTimeProcessPdaDTO4 dto) {
        return Results.success(hmeTimeProcessPdaService.siteOut(tenantId, dto));
    }
}
