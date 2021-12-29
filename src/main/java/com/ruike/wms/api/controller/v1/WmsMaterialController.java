package com.ruike.wms.api.controller.v1;

import com.ruike.wms.app.service.WmsMaterialService;
import com.ruike.wms.domain.repository.WmsMaterialRepository;
import com.ruike.wms.domain.vo.WmsMaterialVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * WmsMaterialController
 *
 * @author liyuan.lv@hand-china.com 2020/05/06 15:46
 */
@RestController("wmsMaterialController.v1")
@RequestMapping("/v1/{organizationId}/wms-material")
@Api(tags = SwaggerApiConfig.WMS_MATERIAL)
@Slf4j
public class WmsMaterialController extends BaseController {

    private final WmsMaterialService service;
    private final WmsMaterialRepository repository;

    public WmsMaterialController(WmsMaterialService service, WmsMaterialRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @ApiOperation(value = "根据地点限制物料")
    @GetMapping(value = "/site-limit", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsMaterialVO>> siteLimitMaterialQuery(@PathVariable("organizationId") Long tenantId,
                                                                      WmsMaterialVO dto,
                                                                      @ApiIgnore PageRequest pageRequest) {
        return Results.success(service.siteLimitMaterialQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "当前用户可访问物料")
    @GetMapping(value = "/user-permission", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsMaterialVO>> userPermissionMaterialQuery(@PathVariable("organizationId") Long tenantId,
                                                                           WmsMaterialVO dto,
                                                                           @ApiIgnore PageRequest pageRequest) {
        return Results.success(service.userPermissionMaterialQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "根据类别查询现有量物料物料")
    @GetMapping(value = "/group-onhand", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsMaterialVO>> groupOnhandItemQuery(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestParam String siteId,
                                                                    @RequestParam String itemGroupCode,
                                                                    @RequestParam String warehouseId) {
        return Results.success(repository.listGetByItemGroup(tenantId, siteId, itemGroupCode, warehouseId));
    }
}
