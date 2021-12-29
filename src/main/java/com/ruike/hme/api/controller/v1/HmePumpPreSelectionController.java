package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmePumpPreSelectionService;
import com.ruike.hme.domain.vo.*;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmePumpPreSelection;
import com.ruike.hme.domain.repository.HmePumpPreSelectionRepository;
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

import java.util.List;

/**
 * 泵浦源预筛选基础表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-08-30 10:59:48
 */
@RestController("hmePumpPreSelectionController.v1")
@RequestMapping("/v1/{organizationId}/hme-pump-pre-selections")
public class HmePumpPreSelectionController extends BaseController {

    @Autowired
    private HmePumpPreSelectionService hmePumpPreSelectionService;

    @ApiOperation(value = "根据物料查询泵浦源筛选规则行数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{materialId}")
    public ResponseEntity<List<HmePumpPreSelectionVO>> queryTagInfoByMaterial(@PathVariable("organizationId") Long tenantId,
                                                                              @PathVariable("materialId") String materialId) {
        return Results.success(hmePumpPreSelectionService.queryTagInfoByMaterial(tenantId, materialId));
    }

    @ApiOperation(value = "扫描容器或条码")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scan-container-barcode")
    public ResponseEntity<HmePumpPreSelectionVO3> scanContainerOrPumpMaterialLot(@PathVariable("organizationId") Long tenantId,
                                                                      HmePumpPreSelectionDTO dto) {
        return Results.success(hmePumpPreSelectionService.scanContainerOrPumpMaterialLot(tenantId, dto));
    }

    @ApiOperation(value = "筛选撤回界面-筛选批次LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/pump-selection-lot")
    public ResponseEntity<Page<HmePumpPreSelectionVO2>> pumpSelectionLotLovQuery(@PathVariable("organizationId") Long tenantId,
                                                                                 HmePumpPreSelectionDTO2 dto, PageRequest pageRequest) {
        Page<HmePumpPreSelectionVO2> resultPage = hmePumpPreSelectionService.pumpSelectionLotLovQuery(tenantId, dto, pageRequest);
        return Results.success(resultPage);
    }

    @ApiOperation(value = "筛选撤回界面主查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/pump-pre-selection-recall-query")
    public ResponseEntity<Page<HmePumpPreSelectionVO5>> pumpPreSelectionRecallQuery(@PathVariable("organizationId") Long tenantId,
                                                                               HmePumpPreSelectionDTO3 dto, PageRequest pageRequest) {
        Page<HmePumpPreSelectionVO5> resultPage = hmePumpPreSelectionService.pumpPreSelectionRecallQuery(tenantId, dto, pageRequest);
        return Results.success(resultPage);
    }

    @ApiOperation(value = "筛选撤回")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/pump-pre-selection-recall")
    public ResponseEntity<?> pumpPreSelectionRecall(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody List<HmePumpPreSelectionVO5> recallList) {
        hmePumpPreSelectionService.pumpPreSelectionRecall(tenantId, recallList);
        return Results.success();
    }

    @ApiOperation(value = "筛选确认")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/pump-pre-selection-confirm")
    public ResponseEntity<?> pumpPreSelectionConfirm(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody HmePumpPreSelectionDTO4 dto) {
        hmePumpPreSelectionService.pumpPreSelectionConfirm(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "筛选")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmePumpPreSelectionVO15> pumPreSelection(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody HmePumpPreSelectionDTO5 dto) {
        HmePumpPreSelectionVO15 result = hmePumpPreSelectionService.pumPreSelection(tenantId, dto);
        return Results.success(result);
    }

    @ApiOperation(value = "扫描筛选批次")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scan-selection-lot")
    public ResponseEntity<HmePumpPreSelectionVO3> scanSelectionLot(@PathVariable("organizationId") Long tenantId,
                                                                   HmePumpPreSelectionDTO6 dto) {
        HmePumpPreSelectionVO3 result = hmePumpPreSelectionService.scanSelectionLot(tenantId, dto);
        return Results.success(result);
    }

    @ApiOperation(value = "根据筛选批次查询套数")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/sets-num-query/{selectionLot}")
    public ResponseEntity<HmePumpPreSelectionVO16> setsNumQueryBySelectionLot(@PathVariable("organizationId") Long tenantId,
                                                                              @PathVariable("selectionLot") String selectionLot) {
        return Results.success(hmePumpPreSelectionService.setsNumQueryBySelectionLot(tenantId, selectionLot));
    }
}
