package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeWipStocktakeDocService;
import com.ruike.hme.domain.entity.HmeWipStocktakeDoc;
import com.ruike.hme.domain.repository.HmeWipStocktakeDocRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.wms.api.dto.WmsStocktakeRangeQueryDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 在制盘点单 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
@RestController("hmeWipStocktakeDocController.v1")
@RequestMapping("/v1/{organizationId}/hme-wip-stocktake-docs")
@Slf4j
public class HmeWipStocktakeDocController extends BaseController {

    @Autowired
    private HmeWipStocktakeDocRepository hmeWipStocktakeDocRepository;
    @Autowired
    private HmeWipStocktakeDocService hmeWipStocktakeDocService;

    @ApiOperation(value = "部门LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/area")
    public ResponseEntity<Page<HmeWipStocktakeDocDTO2>> departmentLovQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                           HmeWipStocktakeDocDTO2 mtModArea, PageRequest pageRequest) {
        return Results.success(hmeWipStocktakeDocRepository.departmentLovQuery(tenantId, mtModArea, pageRequest));
    }

    @ApiOperation(value = "产线LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/prodline")
    public ResponseEntity<Page<HmeWipStocktakeDocDTO3>> prodLineLovQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                         HmeWipStocktakeDocDTO3 mtModProductionLine, PageRequest pageRequest) {
        return Results.success(hmeWipStocktakeDocRepository.prodLineLovQuery(tenantId, mtModProductionLine,pageRequest));
    }

    @ApiOperation(value = "工序LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/process")
    public ResponseEntity<Page<HmeWipStocktakeDocDTO4>> processLovQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                        HmeWipStocktakeDocDTO4 mtModWorkcell, PageRequest pageRequest) {
        return Results.success(hmeWipStocktakeDocRepository.processLovQuery(tenantId, mtModWorkcell, pageRequest));
    }

    @ApiOperation(value = "在制盘点单分页查询")
    @GetMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeWipStocktakeDocVO>> wipStocktakeDocPageQuery(@PathVariable("organizationId") Long tenantId,
                                                                               HmeWipStocktakeDocDTO dto, PageRequest pageRequest) {
        return Results.success(hmeWipStocktakeDocService.wipStocktakeDocPageQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "盘点范围分页查询")
    @GetMapping(value = "/range", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeWipStocktakeDocVO4>> stocktakeRangePageQuery(@PathVariable("organizationId") Long tenantId,
                                                                               WmsStocktakeRangeQueryDTO dto, PageRequest pageRequest) {
        return Results.success(hmeWipStocktakeDocService.stocktakeRangePageQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "盘点明细分页查询")
    @GetMapping(value = "/detail/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeWipStocktakeDocVO2>> wipStocktakeDetailPageQuery(@PathVariable("organizationId") Long tenantId,
                                                                                   HmeWipStocktakeDocDTO5 dto, PageRequest pageRequest) {
        return Results.success(hmeWipStocktakeDocService.wipStocktakeDetailPageQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "盘点汇总分页查询")
    @GetMapping(value = "/sum/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeWipStocktakeDocVO3>> wipStocktakeSumPageQuery(@PathVariable("organizationId") Long tenantId,
                                                                                HmeWipStocktakeDocDTO7 dto, PageRequest pageRequest) {
        return Results.success(hmeWipStocktakeDocService.wipStocktakeSumPageQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "根据物料组查询物料")
    @GetMapping(value = "/material-by-group/{itemGroupId}", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<MtMaterial>> getMaterialByMaterialGroup(@PathVariable("organizationId") Long tenantId,
                                                                       @PathVariable("itemGroupId") String itemGroupId) {
        return Results.success(hmeWipStocktakeDocService.getMaterialByMaterialGroup(tenantId, itemGroupId));
    }

    @ApiOperation(value = "根据车间查询产线")
    @GetMapping(value = "/prodline-by-area/{areaId}", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<MtModProductionLine>> getProdLineByAreaId(@PathVariable("organizationId") Long tenantId,
                                                                       @PathVariable("areaId") String areaId) {
        return Results.success(hmeWipStocktakeDocService.getProdLineByAreaId(tenantId, areaId));
    }

    @ApiOperation(value = "根据产线查询工序")
    @GetMapping(value = "/workcell-by-prodline/{prodLineId}", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<MtModWorkcell>> getWorkcellByProdLineId(@PathVariable("organizationId") Long tenantId,
                                                                         @PathVariable("prodLineId") String prodLineId) {
        return Results.success(hmeWipStocktakeDocService.getWorkcellByProdLineId(tenantId, prodLineId));
    }

    @ApiOperation(value = "新建盘点单")
    @PostMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWipStocktakeDocDTO6> createStocktakeDoc(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody HmeWipStocktakeDocDTO6 dto) {
        return Results.success(hmeWipStocktakeDocService.createStocktakeDoc(tenantId, dto));
    }

    @ApiOperation(value = "更新盘点单")
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWipStocktakeDoc> updateStocktakeDoc(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody HmeWipStocktakeDocDTO9 dto) {
        return Results.success(hmeWipStocktakeDocService.updateStocktakeDoc(tenantId, dto));
    }

    @ApiOperation(value = "删除盘点范围")
    @PostMapping(value = "/delete/range", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWipStocktakeDocDTO10> deleteStocktakeRange(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody HmeWipStocktakeDocDTO10 dto) {
        return Results.success(hmeWipStocktakeDocService.deleteStocktakeRange(tenantId, dto));
    }

    @ApiOperation(value = "获取盘点单产线范围下的产线")
    @GetMapping(value = "/prodline-range/{stocktakeId}", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<String>> plStocktakeRangeQuery(@PathVariable("organizationId") Long tenantId,
                                                                       @PathVariable("stocktakeId") String stocktakeId) {
        return Results.success(hmeWipStocktakeDocService.plStocktakeRangeQuery(tenantId, stocktakeId));
    }

    @ApiOperation(value = "新增盘点范围")
    @PostMapping(value = "/add/range", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWipStocktakeDocDTO11> addStocktakeRange(@PathVariable("organizationId") Long tenantId,
                                                                        @RequestBody HmeWipStocktakeDocDTO11 dto) {
        return Results.success(hmeWipStocktakeDocService.addStocktakeRange(tenantId, dto));
    }

    @ApiOperation(value = "盘点单下达")
    @PostMapping(value = "/released", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWipStocktakeDocDTO12> releasedWipStocktake(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody HmeWipStocktakeDocDTO12 dto) {
        return Results.success(hmeWipStocktakeDocService.releasedWipStocktake(tenantId, dto));
    }

    @ApiOperation(value = "盘点单完成前校验")
    @PostMapping(value = "/completed-validate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWipStocktakeDocVO6> completedWipStocktakeValidate(@PathVariable("organizationId") Long tenantId,
                                                                               @RequestBody HmeWipStocktakeDocDTO12 dto) {
        return Results.success(hmeWipStocktakeDocService.completedWipStocktakeValidate(tenantId, dto));
    }

    @ApiOperation(value = "盘点单完成")
    @PostMapping(value = "/completed", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWipStocktakeDocDTO12> completedWipStocktake(@PathVariable("organizationId") Long tenantId,
                                                                               @RequestBody HmeWipStocktakeDocDTO12 dto) {
        return Results.success(hmeWipStocktakeDocService.completedWipStocktake(tenantId, dto));
    }

    @ApiOperation(value = "盘点单关闭前校验")
    @PostMapping(value = "/closed-validate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWipStocktakeDocVO6> closedWipStocktakeValidate(@PathVariable("organizationId") Long tenantId,
                                                                               @RequestBody HmeWipStocktakeDocDTO12 dto) {
        log.info("<======盘点单关闭校验开始啦");
        long startDate = System.currentTimeMillis();
        HmeWipStocktakeDocVO6 hmeWipStocktakeDocVO6 = hmeWipStocktakeDocService.closedWipStocktakeValidate(tenantId, dto);
        long endDate = System.currentTimeMillis();
        log.info("<====盘点单关闭校验结束啦。总耗时：{}毫秒", (endDate - startDate));
        return Results.success(hmeWipStocktakeDocVO6);
    }

    @ApiOperation(value = "盘点单关闭")
    @PostMapping(value = "/closed", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWipStocktakeDocDTO12> closedWipStocktake(@PathVariable("organizationId") Long tenantId,
                                                                         @RequestBody HmeWipStocktakeDocDTO12 dto) {
        log.info("<======盘点单关闭开始啦");
        long startDate = System.currentTimeMillis();
        HmeWipStocktakeDocDTO12 hmeWipStocktakeDocDTO12 = hmeWipStocktakeDocService.closedWipStocktake(tenantId, dto);
        long endDate = System.currentTimeMillis();
        log.info("<====盘点单关闭结束啦。总耗时：{}毫秒", (endDate - startDate));
        return Results.success(hmeWipStocktakeDocDTO12);
    }

    @ApiOperation(value = "根据多个产线ID集合查询其下工序")
    @GetMapping(value = "/process-prodlineList", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<MtModWorkcell>> processQueryByProdLineIdList(@PathVariable("organizationId") Long tenantId,
                                                                     HmeWipStocktakeDocDTO13 dto, PageRequest pageRequest) {
        return Results.success(hmeWipStocktakeDocService.processQueryByProdLineIdList(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "盘点投料明细汇总")
    @GetMapping(value = "/release-detail", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeWipStocktakeDocVO7>> releaseDetailPageQuery(@PathVariable("organizationId") Long tenantId,
                                                                              HmeWipStocktakeDocDTO15 dto, PageRequest pageRequest) {
        return Results.success(hmeWipStocktakeDocService.releaseDetailPageQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "根据多个产线ID集合查询产线信息")
    @GetMapping(value = "/prodline-prodlineId", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<MtModProductionLine>> prodLinePageQuery(@PathVariable("organizationId") Long tenantId,
                                                                       HmeWipStocktakeDocDTO14 dto, PageRequest pageRequest) {
        return Results.success(hmeWipStocktakeDocService.prodLinePageQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "COS盘点导出")
    @GetMapping(value = "/cos-inventory-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(HmeWipStocktakeDocVO8.class)
    public ResponseEntity<List<HmeWipStocktakeDocVO8>> cosInventoryExport(@PathVariable("organizationId") Long tenantId,
                                                                             String stocktakeNum,
                                                                             HttpServletResponse response,
                                                                             ExportParam exportParam) {
        return Results.success(hmeWipStocktakeDocService.cosInventoryExport(tenantId, stocktakeNum));
    }

    @ApiOperation(value = "在制盘点明细导出")
    @GetMapping(value = "/detail/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeWipStocktakeDocVO9.class)
    public ResponseEntity<List<HmeWipStocktakeDocVO9>> wipStocktakeDetailExport(@PathVariable("organizationId") Long tenantId,
                                                               ExportParam exportParam,
                                                               HttpServletResponse response,
                                                               HmeWipStocktakeDocDTO5 dto) {
        return Results.success(hmeWipStocktakeDocService.wipStocktakeDetailExport(tenantId, dto));
    }

    @ApiOperation(value = "在制盘点汇总导出")
    @GetMapping(value = "/sum/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeWipStocktakeDocVO10.class)
    public ResponseEntity<List<HmeWipStocktakeDocVO10>> wipStocktakeSumExport(@PathVariable("organizationId") Long tenantId,
                                                                                ExportParam exportParam,
                                                                                HttpServletResponse response,
                                                                                HmeWipStocktakeDocDTO7 dto) {
        return Results.success(hmeWipStocktakeDocService.wipStocktakeSumExport(tenantId, dto));
    }

    @ApiOperation(value = "在制盘点投料汇总导出")
    @GetMapping(value = "/release-detail/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeWipStocktakeDocVO11.class)
    public ResponseEntity<List<HmeWipStocktakeDocVO11>> releaseDetailExport(@PathVariable("organizationId") Long tenantId,
                                                                              ExportParam exportParam,
                                                                              HttpServletResponse response,
                                                                              HmeWipStocktakeDocDTO15 dto) {
        return Results.success(hmeWipStocktakeDocService.releaseDetailExport(tenantId, dto));
    }
}
