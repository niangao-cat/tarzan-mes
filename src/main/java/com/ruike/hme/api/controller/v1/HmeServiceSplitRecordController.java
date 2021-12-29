package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO;
import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO3;
import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO4;
import com.ruike.hme.app.service.HmeServiceSplitRecordService;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.repository.HmeServiceSplitRecordRepository;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * 售后返品拆机表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 14:21:01
 */
@RestController("hmeServiceSplitRecordController.v1")
@RequestMapping("/v1/{organizationId}/hme-service-split-records")
@Api(tags = SwaggerApiConfig.HME_SERVICE_SPLIT_RECORD)
public class HmeServiceSplitRecordController extends BaseController {

    private final HmeServiceSplitRecordService hmeServiceSplitRecordService;
    private final HmeEoJobSnRepository hmeEoJobSnRepository;
    private final HmeServiceSplitRecordRepository hmeServiceSplitRecordRepository;

    public HmeServiceSplitRecordController(HmeServiceSplitRecordService hmeServiceSplitRecordService, HmeEoJobSnRepository hmeEoJobSnRepository, HmeServiceSplitRecordRepository hmeServiceSplitRecordRepository) {
        this.hmeServiceSplitRecordService = hmeServiceSplitRecordService;
        this.hmeEoJobSnRepository = hmeEoJobSnRepository;
        this.hmeServiceSplitRecordRepository = hmeServiceSplitRecordRepository;
    }

    @ApiOperation(value = "组件编码扫描")
    @GetMapping(value = {"/material-lot-scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeServiceSplitRecordDTO3> materialLotScan(@PathVariable("organizationId") Long tenantId,
                                                                     HmeServiceSplitMaterialLotVO vo) {
        return Results.success(hmeServiceSplitRecordService.materialLotScan(tenantId, vo));
    }

    @ApiOperation(value = "bom信息 查询")
    @GetMapping(value = {"/bom"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeServiceSplitBomHeaderVO> bomGet(@PathVariable("organizationId") Long tenantId,
                                                             @RequestParam("siteId") String siteId,
                                                             @RequestParam("materialId") String materialId) {
        return Results.success(hmeServiceSplitRecordService.bomGet(tenantId, siteId, materialId));
    }

    @ApiOperation(value = "退库检测 列表查询")
    @GetMapping(value = {"/return-check"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeServiceSplitReturnCheckVO>> returnCheckList(@PathVariable("organizationId") Long tenantId,
                                                                              @RequestParam("snNum") String snNum,
                                                                              @RequestParam(value = "allFlag", required = false) String allFlag) {
        return Results.success(hmeServiceSplitRecordRepository.returnCheckListGet(tenantId, snNum, allFlag));
    }

    @ApiOperation(value = "工位扫描")
    @PostMapping(value = {"/wkc-scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEoJobSnVO4> workcellScan(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody HmeEoJobSnDTO dto) {
        return Results.success(hmeEoJobSnRepository.workcellScan(tenantId, dto));
    }

    @ApiOperation(value = "返修序列号扫描")
    @PostMapping(value = {"/sn-scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeServiceSplitRecordDTO> snNumScan(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody HmeServiceSplitRecordVO vo) {
        return Results.success(hmeServiceSplitRecordService.snNumScan(tenantId, vo));
    }

    @ApiOperation(value = "整机拆机")
    @PostMapping(value = {"/split"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeServiceSplitRecordDTO3> split(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody HmeServiceSplitRecordDTO3 dto) {
        return Results.success(hmeServiceSplitRecordService.split(tenantId, dto));
    }

    @ApiOperation(value = "登记撤销")
    @PostMapping(value = {"/cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeServiceSplitRecordDTO4> registerCancel(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody HmeServiceSplitRecordDTO4 dto) {
        return Results.success(hmeServiceSplitRecordService.registerCancel(tenantId, dto));
    }

}
