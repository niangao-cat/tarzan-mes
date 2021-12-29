package com.ruike.wms.api.controller.v1;

import static tarzan.config.SwaggerApiConfig.WMS_WAREHOUSE_LOCATOR;

import com.ruike.wms.api.dto.SiteDTO;
import com.ruike.wms.api.dto.WmsLocatorDTO;
import com.ruike.wms.api.dto.WmsWarehouseDTO;
import com.ruike.wms.app.service.WmsWarehouseLocatorService;
import com.ruike.wms.domain.repository.WmsWarehouseLocatorRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.modeling.domain.entity.MtModSite;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * WmsWarehouseLocatorController
 *
 * @author liyuan.lv@hand-china.com 2020/04/30 22:41
 */
@RestController("wmsWarehouseLocatorController.v1")
@RequestMapping("/v1/{organizationId}/wms-warehouse-locator")
@Api(tags = WMS_WAREHOUSE_LOCATOR)
@Slf4j
public class WmsWarehouseLocatorController extends BaseController {
    @Autowired
    private WmsWarehouseLocatorService service;

    @ApiOperation(value = "获取仓库")
    @GetMapping(value = "/warehouse", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsWarehouseDTO>> queryWarehouse(@PathVariable("organizationId") Long tenantId,
                                                                WmsWarehouseDTO dto,
                                                                @ApiIgnore PageRequest pageRequest) {
        return Results.success(service.getWarehouse(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "获取库位")
    @GetMapping(value = "/locator", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsLocatorDTO>> queryLocator(@PathVariable("organizationId") Long tenantId,
                                                              WmsLocatorDTO dto,
                                                              @ApiIgnore PageRequest pageRequest) {
        return Results.success(service.getLocator(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "默认工厂")
    @GetMapping(value = "/site/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<MtModSite> siteBasicPropertyGet(@PathVariable(value = "organizationId") Long tenantId) {
        return Results.success(service.siteBasicPropertyGet(tenantId));
    }

    @ApiOperation(value = "工厂下拉框")
    @GetMapping(value = "/get/site", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<SiteDTO>> getSite(@PathVariable("organizationId") Long tenantId) {
        log.info("<====MaterialGetReturnController-getSite:{}", tenantId);
        return Results.success(service.getSite(tenantId));
    }
}
