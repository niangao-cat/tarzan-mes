package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeEdginkService;
import io.choerodon.core.base.BaseController;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tarzan.config.SwaggerApiConfig;

/**
 * edgink采集数据 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2020-07-29 09:35:58
 */
@RestController("hmeEdginkController.v1")
@RequestMapping("/v1/{organizationId}/hme-edgink")
@Api(tags = SwaggerApiConfig.HME_EDGINK)
@Slf4j
public class HmeEdginkController extends BaseController {

    @Autowired
    private HmeEdginkService service;

    /**
     * @Description 获取激光功率计数据
     * @param tenantId
     * @param equipmentCode
     * @Date 2020-07-29 09:35:58
     * @Author wenzhnag.yu
     */
    @ApiOperation(value = "获取激光功率计数据")
    @GetMapping(value = "/getOphir", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<String> getOphir(
            @PathVariable("organizationId") Long tenantId, String equipmentCode) {
        log.info("<====HmeEdginkController-getOphir:{},{}",tenantId, equipmentCode);
        String str = service.getOphir(tenantId, equipmentCode);
        return Results.success(str);
    }

    /**
     * @Description 获取毫瓦功率计数据
     * @param tenantId
     * @param equipmentCode
     * @Date 2020-07-29 09:35:58
     * @Author wenzhnag.yu
     */
    @ApiOperation(value = "获取毫瓦功率计数据")
    @GetMapping(value = "/getThorlabs", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<String> getThorlabs(
            @PathVariable("organizationId") Long tenantId, String equipmentCode) {
        log.info("<====HmeEdginkController-getThorlabs:{},{}",tenantId, equipmentCode);
        String str = service.getThorlabs(tenantId, equipmentCode);
        return Results.success(str);
    }
}
