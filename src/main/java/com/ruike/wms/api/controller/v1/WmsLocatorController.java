package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsLocatorScanDTO;
import com.ruike.wms.domain.repository.WmsLocatorRepository;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * <p>
 * WMS库位 API 管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/12 14:34
 */
@RestController("wmsLocatorController.v1")
@RequestMapping("/v1/{organizationId}/locator")
@Api(tags = SwaggerApiConfig.WMS_LOCATOR)
public class WmsLocatorController {

    private final WmsLocatorRepository wmsLocatorRepository;

    public WmsLocatorController(WmsLocatorRepository wmsLocatorRepository) {
        this.wmsLocatorRepository = wmsLocatorRepository;
    }

    @ApiOperation(value = "仓库下的货位查询")
    @GetMapping(value = {"/inventory-locator"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<MtModLocator>> inventroyLocatoryQuery(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestParam("warehouseId") String warehouseId,
                                                                     @RequestParam(value = "locatorType", required = false) String locatorType) {
        return Results.success(wmsLocatorRepository.selectListByType(tenantId, warehouseId, locatorType));
    }
}
