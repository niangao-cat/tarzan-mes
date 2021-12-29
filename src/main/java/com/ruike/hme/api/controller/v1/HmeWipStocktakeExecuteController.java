package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.service.HmeWipStocktakeExecuteService;
import com.ruike.hme.domain.vo.HmeWipStocktakeExecuteVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 在制品盘点执行
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 11:05
 */
@RestController("hmeWipStocktakeExecuteController.v1")
@RequestMapping("/v1/{organizationId}/hme-wip-stocktake-execute")
public class HmeWipStocktakeExecuteController extends BaseController {
    private final HmeWipStocktakeExecuteService service;

    public HmeWipStocktakeExecuteController(HmeWipStocktakeExecuteService service) {
        this.service = service;
    }

    @ApiOperation(value = "在制品盘点执行 单据选择")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<List<HmeWipStocktakeExecuteDocRepresentationDTO>> docSelect(@PathVariable(value = "organizationId") Long tenantId,
                                                                                      HmeWipStocktakeExecuteDocQueryDTO dto) {
        dto.paramInit();
        return Results.success(service.docSelect(tenantId, dto));
    }

    @ApiOperation(value = "在制品盘点执行 单据扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{stocktakeNum}")
    public ResponseEntity<HmeWipStocktakeExecuteDocRepresentationDTO> docScan(@PathVariable(value = "organizationId") Long tenantId,
                                                                              @PathVariable(value = "stocktakeNum") String stocktakeNum) {
        return Results.success(service.docScan(tenantId, stocktakeNum));
    }

    @ApiOperation(value = "在制品盘点执行 范围扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/range")
    public ResponseEntity<WipStocktakeRangeScanResponseDTO> rangeScan(@PathVariable(value = "organizationId") Long tenantId,
                                                                      WipStocktakeRangeScanQueryDTO dto) {
        this.validObject(dto);
        return Results.success(service.rangeScan(tenantId, dto));
    }

    @ApiOperation(value = "在制品盘点执行 条码扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/barcode")
    public ResponseEntity<WipStocktakeBarcodeScanResponseDTO> barcodeScan(@PathVariable(value = "organizationId") Long tenantId,
                                                                          WipStocktakeBarcodeScanQueryDTO dto) {
        this.validObject(dto);
        return Results.success(service.barcodeScan(tenantId, dto));
    }

    @ApiOperation(value = "在制品盘点执行 物料明细列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/material-detail")
    public ResponseEntity<List<WipStocktakeMaterialDetailRepresentationDTO>> materialDetail(@PathVariable(value = "organizationId") Long tenantId,
                                                                                            WipStocktakeMaterialDetailQueryDTO query) {
        this.validObject(query);
        return Results.success(service.materialDetailGet(tenantId, query));
    }

    @ApiOperation(value = "在制品盘点执行 条码明细列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/sn-detail")
    public ResponseEntity<Page<WipStocktakeExecSnRepresentationDTO>> snDetailGet(@PathVariable(value = "organizationId") Long tenantId,
                                                                                 WipStocktakeSnDetailQueryDTO query, PageRequest pageRequest) {
        this.validObject(query);
        return Results.success(service.snDetailGet(tenantId, query, pageRequest));
    }

    @ApiOperation(value = "在制品盘点执行 验证-是否存在实际")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/valid/actual-exists")
    public ResponseEntity<WipStocktakeBarcodeScanResponseDTO> actualExistsValid(@PathVariable(value = "organizationId") Long tenantId,
                                                                                @RequestBody WipStocktakeMaterialLotValidQueryDTO dto) {
        this.validObject(dto);
        this.validList(dto.getMaterialLotList());
        return Results.success(service.actualExistsValid(tenantId, dto));
    }

    @ApiOperation(value = "在制品盘点执行 验证-是否已盘")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/valid/counted")
    public ResponseEntity<WipStocktakeBarcodeScanResponseDTO> countedValid(@PathVariable(value = "organizationId") Long tenantId,
                                                                           @RequestBody WipStocktakeMaterialLotValidQueryDTO dto) {
        this.validObject(dto);
        this.validList(dto.getMaterialLotList());
        return Results.success(service.countedValid(tenantId, dto));
    }

    @ApiOperation(value = "在制品盘点执行 范围数据补充")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/addition/range")
    @Deprecated
    public ResponseEntity<Integer> rangeAddition(@PathVariable(value = "organizationId") Long tenantId,
                                                 @RequestBody WipStocktakeRangeSaveCommandDTO command) {
        this.validObject(command);
        this.validList(command.getRangeList());
        return Results.success(service.stocktakeRangeAddition(tenantId, command));
    }

    @ApiOperation(value = "在制品盘点执行 盘点实际数据补充")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/addition/actual")
    public ResponseEntity<Integer> actualAddition(@PathVariable(value = "organizationId") Long tenantId,
                                                  @RequestBody WipStocktakeActualSaveCommandDTO command) {
        this.validObject(command);
        this.validList(command.getMaterialLots());
        return Results.success(service.actualAddition(tenantId, command));
    }

    @ApiOperation(value = "在制品盘点执行 执行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeWipStocktakeExecuteVO> execute(@PathVariable(value = "organizationId") Long tenantId,
                                                            @RequestBody WipStocktakeExecuteCommandDTO command) {
        this.validObject(command);
        this.validList(command.getMaterialLots());
        return Results.success(service.execute(tenantId, command));
    }
}
