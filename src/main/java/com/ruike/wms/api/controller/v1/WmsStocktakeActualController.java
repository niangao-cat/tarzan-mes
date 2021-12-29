package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsStocktakeActualExportDTO;
import com.ruike.wms.api.dto.WmsStocktakeActualQueryDTO;
import com.ruike.wms.api.dto.WmsStocktakeValidationDTO;
import com.ruike.wms.app.service.WmsStocktakeActualService;
import com.ruike.wms.domain.vo.WmsStocktakeActualVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static tarzan.config.SwaggerApiConfig.WMS_STOCKTAKE_ACTUAL;

/**
 * 库存盘点实绩 API
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 12:27
 */
@RestController("wmsStocktakeActualController.v1")
@RequestMapping("/v1/{organizationId}/wms-stocktake-actual")
@Api(tags = WMS_STOCKTAKE_ACTUAL)
public class WmsStocktakeActualController extends BaseController {
    private final WmsStocktakeActualService stocktakeActualService;

    public WmsStocktakeActualController(WmsStocktakeActualService stocktakeActualService) {
        this.stocktakeActualService = stocktakeActualService;
    }

    @ApiOperation(value = "库存盘点实绩 查询")
    @GetMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsStocktakeActualVO>> list(@PathVariable("organizationId") Long tenantId,
                                                           WmsStocktakeActualQueryDTO condition, @ApiIgnore @SortDefault(value = WmsStocktakeActualVO.FIELD_STOCKTAKE_ACTUAL_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        this.validObject(condition);
        condition.setStocktakeIdList(Arrays.asList(condition.getStocktakeIds().split(",")));
        return Results.success(stocktakeActualService.pageAndSort(tenantId, condition, pageRequest));
    }

    @ApiOperation(value = "库存盘点实绩 导出")
    @GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(WmsStocktakeActualExportDTO.class)
    public ResponseEntity<List<WmsStocktakeActualExportDTO>> export(@PathVariable("organizationId") Long tenantId,
                                                                    ExportParam exportParam,
                                                                    HttpServletResponse response,
                                                                    WmsStocktakeActualQueryDTO condition) {
        this.validObject(condition);
        condition.setStocktakeIdList(Arrays.asList(condition.getStocktakeIds().split(",")));
        return Results.success(stocktakeActualService.export(tenantId, exportParam, condition));
    }

    @ApiOperation(value = "库存盘点实绩 新增")
    @PostMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsStocktakeActualVO>> insertByLoadObject(@PathVariable("organizationId") Long tenantId,
                                                                         @RequestBody WmsStocktakeValidationDTO dto) {
        this.validObject(dto);
        return Results.success(stocktakeActualService.insertByLoadObject(tenantId, dto));
    }

}
