package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.domain.repository.HmeChipTransferRepository;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * 芯片转移平台
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/17 10:14
 */
@RestController("HmeChipTransferController.v1")
@RequestMapping("/v1/{organizationId}/hme-chip-transfer")
@Api(tags = SwaggerApiConfig.HME_CHIP_TRANSFER)
@Slf4j
public class HmeChipTransferController extends BaseController {

    @Autowired
    private HmeChipTransferRepository hmeChipTransferRepository;


    @ApiOperation(value = "工位扫描")
    @PostMapping(value = {"/workcell-scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEoJobSnVO4> workcellScan(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody HmeEoJobSnDTO dto) {
        return Results.success(hmeChipTransferRepository.workcellScan(tenantId, dto));
    }

    @ApiOperation(value = "待转移容器进站")
    @PostMapping(value = {"/in-site-transfer"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeChipTransferVO> inSiteTransfer(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody HmeChipTransferVO vo) {
        return Results.success(hmeChipTransferRepository.inSiteTransfer(tenantId, vo));
    }

    @ApiOperation(value = "目标容器进站")
    @PostMapping(value = {"/in-site-target"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeChipTransferVO> inSiteTarget(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody HmeChipTransferVO vo) {
        return Results.success(hmeChipTransferRepository.inSiteTarget(tenantId, vo));
    }

    @ApiOperation(value = "手动单元格全量转移")
    @PostMapping(value = {"/handle-all-transfer"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeChipTransferVO8> handleAllTransfer(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody HmeChipTransferVO2 vo) {
        HmeChipTransferVO8 hmeChipTransferVO8 = hmeChipTransferRepository.handleAllTransfer(tenantId, vo);
        return Results.success(hmeChipTransferVO8);
    }


    @ApiOperation(value = "自动分配转移")
    @PostMapping(value = {"/auto-assign-transfer"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> autoAssignTransfer(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody HmeChipTransferVO4 vo) {
        hmeChipTransferRepository.autoAssignTransfer(tenantId, vo);
        return Results.success();
    }

    @ApiOperation(value = "不良自动分配转移")
    @PostMapping(value = {"/auto-ng-assign-transfer"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> autoNgAssignTransfer(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody HmeChipTransferVO4 vo) {
        hmeChipTransferRepository.autoNgAssignTransfer(tenantId, vo);
        return Results.success();
    }

    @ApiOperation(value = "全部转移完成")
    @PostMapping(value = {"/chip-transfer-complete"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> chipTransferComplete(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody HmeChipTransferVO3 vo) {
        hmeChipTransferRepository.chipTransferComplete(tenantId, vo);
        return Results.success();
    }

    @ApiOperation(value = "手动转移完成")
    @PostMapping(value = {"/handle-chip-transfer-complete"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> handleChipTransferComplete(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody HmeChipTransferVO3 vo) {
        hmeChipTransferRepository.handleChipTransferComplete(tenantId, vo);
        return Results.success();
    }

    @ApiOperation(value = "容器类型获取默认")
    @GetMapping(value = {"/container-info-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> containerInfoQuery(@PathVariable("organizationId") Long tenantId, HmeChipTransferVO7 vo7) {
        return Results.success(hmeChipTransferRepository.containerInfoQuery(tenantId, vo7.getContainerType(), vo7.getOperationId(), vo7.getCosType()));
    }

    @ApiOperation(value = "获取工位未出站条码")
    @GetMapping(value = {"/site-in-material-code-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeChipTransferVO6> siteInMaterialCodeQuery(@PathVariable("organizationId") Long tenantId, HmeChipTransferVO7 vo7) {
        return Results.success(hmeChipTransferRepository.siteInMaterialCodeQuery(tenantId, vo7.getWorkcellId(), vo7.getOperationId()));
    }

    @ApiOperation(value = "工单与工位对应产线是否一致")
    @GetMapping(value = {"/prod-line-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> verifyProdLine (@PathVariable("organizationId") Long tenantId,
                                                              String materialLotCode, String prodLineId, String workcellId) {
        return Results.success(hmeChipTransferRepository.verifyProdLine(tenantId, materialLotCode, prodLineId, workcellId));
    }


}
