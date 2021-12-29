package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsStockAllocateSettingDTO;
import com.ruike.wms.app.service.WmsStockAllocateSettingService;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsStockAllocateSetting;
import com.ruike.wms.domain.repository.WmsStockAllocateSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

/**
 * 库存调拨审核设置表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-08-05 17:21:32
 */
@RestController("wmsStockAllocateSettingController.v1")
@RequestMapping("/v1/{organizationId}/wms-stock-allocate-settings")
@Api(tags = SwaggerApiConfig.WMS_STOCK_ALLOCATE_SETTING)
public class WmsStockAllocateSettingController extends BaseController {

    @Autowired
    private WmsStockAllocateSettingService service;

    @Autowired
    private WmsStockAllocateSettingRepository wmsStockAllocateSettingRepository;

    @ApiOperation(value = "库存调拨审核设置表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<WmsStockAllocateSettingDTO>> listStockSettingForUi(@ApiIgnore PageRequest pageRequest,
                                                                                  @PathVariable(value = "organizationId") Long tenantId,
                                                                                  WmsStockAllocateSettingDTO dto) {
        Page<WmsStockAllocateSettingDTO> list = service.listStockSettingForUi(tenantId, pageRequest, dto);
        return Results.success(list);
    }

    @ApiOperation(value = "库存调拨审核设置保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> saveSampleSchemeForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                        WmsStockAllocateSettingDTO dto) {

        return Results.success(service.saveStockSettingForUi(tenantId, dto));
    }

    @ApiOperation(value = "删除库存调拨审核设置表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody WmsStockAllocateSetting wmsStockAllocateSetting) {
        wmsStockAllocateSettingRepository.deleteByPrimaryKey(wmsStockAllocateSetting);
        return Results.success();
    }
}
