package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsStocktakeDocService;
import com.ruike.wms.domain.repository.WmsStocktakeDocRepository;
import com.ruike.wms.domain.vo.WmsStocktakeDocSelectVO;
import com.ruike.wms.domain.vo.WmsStocktakeDocVO;
import com.ruike.wms.domain.vo.WmsStocktakeMaterialDetailVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.stocktake.domain.vo.MtStocktakeDocVO1;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static tarzan.config.SwaggerApiConfig.WMS_STOCKTAKE_DOC;

/**
 * 库存盘点单据 API
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 12:27
 */
@RestController("wmsStocktakeDocController.v1")
@RequestMapping("/v1/{organizationId}/wms-stocktake-doc")
@Api(tags = WMS_STOCKTAKE_DOC)
@Slf4j
public class WmsStocktakeDocController extends BaseController {

    private final WmsStocktakeDocService stocktakeDocService;
    private final WmsStocktakeDocRepository wmsStocktakeDocRepository;

    public WmsStocktakeDocController(WmsStocktakeDocService stocktakeDocService, WmsStocktakeDocRepository wmsStocktakeDocRepository) {
        this.stocktakeDocService = stocktakeDocService;
        this.wmsStocktakeDocRepository = wmsStocktakeDocRepository;
    }

    @ApiOperation(value = "库存盘点单据 汇总查询")
    @GetMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsStocktakeDocVO>> list(@PathVariable("organizationId") Long tenantId,
                                                        WmsStocktakeDocQueryDTO condition, @ApiIgnore @SortDefault(value = WmsStocktakeDocVO.FIELD_STOCKTAKE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(stocktakeDocService.pageAndSort(tenantId, condition, pageRequest));
    }

    @ApiOperation(value = "库存盘点单据 盘点明细导出")
    @GetMapping(value = "/material-detail/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(WmsStocktakeDetailExportDTO.class)
    public ResponseEntity<List<WmsStocktakeDetailExportDTO>> exportDetail(@PathVariable("organizationId") Long tenantId,
                                                                          ExportParam exportParam,
                                                                          HttpServletResponse response,
                                                                          @RequestParam String stocktakeIds,
                                                                          WmsStocktakeMaterialDetailQueryDTO dto) {
        List<String> stocktakeIdList = Arrays.asList(stocktakeIds.split(","));
        dto.setStocktakeIdList(stocktakeIdList);
        return Results.success(stocktakeDocService.exportDetail(tenantId, exportParam, dto));
    }

    @ApiOperation(value = "库存盘点单据 查询漏判单据")
    @PostMapping(value = "/leak", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<String>> isLeak(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody List<String> stocktakeIdList) {
        return Results.success(wmsStocktakeDocRepository.leakDocGet(tenantId, stocktakeIdList));
    }

    @ApiOperation(value = "库存盘点单据 新增")
    @PostMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsStocktakeDocVO> create(@PathVariable("organizationId") Long tenantId, @RequestBody MtStocktakeDocVO1 vo) {
        return Results.success(stocktakeDocService.create(tenantId, vo));
    }

    @ApiOperation(value = "库存盘点单据 下达")
    @PostMapping(value = "/release", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> release(@PathVariable("organizationId") Long tenantId, @RequestBody List<String> stocktakeIdList) {
        long startDate = System.currentTimeMillis();
        stocktakeDocService.release(tenantId, stocktakeIdList);
        long endDate = System.currentTimeMillis();
        log.info("===========>盘点单" + stocktakeIdList.get(0) + "下达总耗时："+(endDate - startDate) + "毫秒");
        return Results.success();
    }

    @ApiOperation(value = "库存盘点单据 完成")
    @PostMapping(value = "/complete", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> complete(@PathVariable("organizationId") Long tenantId, @RequestBody List<String> stocktakeIdList) {
        stocktakeDocService.complete(tenantId, stocktakeIdList);
        return Results.success();
    }

    @ApiOperation(value = "库存盘点单据 关闭")
    @PostMapping(value = "/close", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> close(@PathVariable("organizationId") Long tenantId, @RequestBody List<String> stocktakeIdList) {
        stocktakeDocService.close(tenantId, stocktakeIdList);
        return Results.success();
    }

    @ApiOperation(value = "库存盘点单据 物料明细查询")
    @PostMapping(value = "/material-detail", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsStocktakeMaterialDetailVO>> materialVersion(@PathVariable("organizationId") Long tenantId,
                                                                              @RequestBody WmsStocktakeMaterialDetailQueryDTO condition,
                                                                              PageRequest pageRequest) {
        return Results.success(stocktakeDocService.pageMaterialVersion(tenantId, condition, pageRequest));
    }

    @ApiOperation(value = "库存盘点单据 更新")
    @PutMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsStocktakeDocVO> update(@PathVariable("organizationId") Long tenantId, @RequestBody WmsStocktakeDocUpdateDTO vo) {
        validObject(vo);
        return Results.success(stocktakeDocService.update(tenantId, vo));
    }
}
