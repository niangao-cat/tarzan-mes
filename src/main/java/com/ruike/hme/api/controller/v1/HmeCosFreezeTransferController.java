package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.repository.HmeCosFreezeTransferRepository;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 芯片转移记录表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2020-11-18 21:06:06
 */
@RestController("hmeCosFreezeTransferController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-freeze-transfer")
public class HmeCosFreezeTransferController {

    @Autowired
    private HmeCosFreezeTransferRepository hmeCosFreezeTransferRepository;

    @ApiOperation(value = "来源条码进站")
    @PostMapping(value = {"/source-code-site-in"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosFreezeTransferVO2> sourceCodeSiteIn(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody HmeCosFreezeTransferVO vo) {
        return Results.success(hmeCosFreezeTransferRepository.sourceCodeSiteIn(tenantId, vo));
    }


    @ApiOperation(value = "目标条码进站")
    @PostMapping(value = {"/target-code-site-in"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosFreezeTransferVO2> targetCodeSiteIn(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody HmeCosFreezeTransferVO vo) {
        return Results.success(hmeCosFreezeTransferRepository.targetCodeSiteIn(tenantId, vo));
    }

    @ApiOperation(value = "合格自动分配转移")
    @PostMapping(value = {"/auto-ok-assign-transfer"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> autoOkAssignTransfer(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody HmeChipTransferVO4 vo) {
        hmeCosFreezeTransferRepository.autoOkAssignTransfer(tenantId, vo);
        return Results.success();
    }

    @ApiOperation(value = "冻结自动分配转移")
    @PostMapping(value = {"/auto-freeze-assign-transfer"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> autoFreezeAssignTransfer(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody HmeChipTransferVO4 vo) {
        hmeCosFreezeTransferRepository.autoFreezeAssignTransfer(tenantId, vo);
        return Results.success();
    }

    @ApiOperation(value = "手动单元格全量转移")
    @PostMapping(value = {"/handle-all-transfer"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> handleAllTransfer(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody HmeChipTransferVO2 vo) {
        hmeCosFreezeTransferRepository.handleAllTransfer(tenantId, vo);
        return Results.success();
    }

    @ApiOperation(value = "转移完成")
    @PostMapping(value = {"/handle-chip-transfer-complete"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> handleChipTransferComplete(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody HmeChipTransferVO3 vo) {
        hmeCosFreezeTransferRepository.handleChipTransferComplete(tenantId, vo);
        return Results.success();
    }

    @ApiOperation(value = "获取工位未出站条码")
    @GetMapping(value = {"/site-in-material-code-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeChipTransferVO6> siteInMaterialCodeQuery(@PathVariable("organizationId") Long tenantId, HmeChipTransferVO7 vo7) {
        return Results.success(hmeCosFreezeTransferRepository.siteInMaterialCodeQuery(tenantId, vo7.getWorkcellId(), vo7.getOperationId()));
    }
}
