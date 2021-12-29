package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsStocktakeRangeQueryDTO;
import com.ruike.wms.app.service.WmsStocktakeRangeService;
import com.ruike.wms.domain.repository.WmsStocktakeRangeRepository;
import com.ruike.wms.domain.vo.WmsStocktakeRangeVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

import static tarzan.config.SwaggerApiConfig.WMS_STOCKTAKE_RANGE;

/**
 * <p>
 * 库存盘点范围 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/11 14:10
 */
@RestController("wmsStocktakeRangeController.v1")
@RequestMapping("/v1/{organizationId}/wms-stocktake-range")
@Api(tags = WMS_STOCKTAKE_RANGE)
public class WmsStocktakeRangeController {
    private final WmsStocktakeRangeRepository wmsStocktakeRangeRepository;
    private final WmsStocktakeRangeService wmsStocktakeRangeService;

    public WmsStocktakeRangeController(WmsStocktakeRangeRepository wmsStocktakeRangeRepository, WmsStocktakeRangeService wmsStocktakeRangeService) {
        this.wmsStocktakeRangeRepository = wmsStocktakeRangeRepository;
        this.wmsStocktakeRangeService = wmsStocktakeRangeService;
    }

    @ApiOperation(value = "库存盘点范围 分页查询")
    @GetMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsStocktakeRangeVO>> page(@PathVariable("organizationId") Long tenantId,
                                                          WmsStocktakeRangeQueryDTO dto,
                                                          PageRequest pageRequest) {
        return Results.success(wmsStocktakeRangeRepository.pageStocktakeRange(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "库存盘点范围 盘点货位获取")
    @GetMapping(value = "/locator", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<MtModLocator> stocktakeLocatorGet(@PathVariable("organizationId") Long tenantId,
                                                            String stocktakeId,
                                                            String locatorCode) {
        return Results.success(wmsStocktakeRangeService.locatorGet(tenantId, stocktakeId, locatorCode));
    }

    @ApiOperation(value = "库存盘点范围 批量新增")
    @PostMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsStocktakeRangeVO>> create(@PathVariable("organizationId") Long tenantId,
                                                            @RequestParam String stocktakeId,
                                                            @RequestParam String rangeObjectType,
                                                            @RequestBody List<WmsStocktakeRangeVO> list) {
        return Results.success(wmsStocktakeRangeService.batchInsert(tenantId, stocktakeId, rangeObjectType, list));
    }

    @ApiOperation(value = "库存盘点范围 批量删除范围对象")
    @DeleteMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsStocktakeRangeVO>> materialVersion(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestParam String stocktakeId,
                                                                     @RequestParam String rangeObjectType,
                                                                     @RequestBody List<WmsStocktakeRangeVO> list) {
        return Results.success(wmsStocktakeRangeRepository.batchDeleteByPrimaryKey(tenantId, stocktakeId, rangeObjectType, list));
    }
}
