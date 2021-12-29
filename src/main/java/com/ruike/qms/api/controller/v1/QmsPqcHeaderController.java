package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.*;
import com.ruike.qms.app.service.QmsPqcHeaderService;
import com.ruike.qms.domain.vo.*;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.qms.domain.entity.QmsPqcHeader;
import com.ruike.qms.domain.repository.QmsPqcHeaderRepository;
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

import java.util.List;

/**
 * 巡检单头表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-08-17 11:49:31
 */
@RestController("qmsPqcHeaderController.v1")
@RequestMapping("/v1/{organizationId}/qms-pqc-headers")
@Api(tags = SwaggerApiConfig.QMS_PQC_HEADER)
public class QmsPqcHeaderController extends BaseController {

    @Autowired
    private QmsPqcHeaderService qmsPqcHeaderService;

    @ApiOperation(value = "产线树状图查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/prodLine/query")
    public ResponseEntity<List<QmsPqcHeaderVO>> prodLineQuery(@PathVariable(value = "organizationId")Long tenantId, QmsPqcHeaderDTO2 dto) {
        return Results.success(qmsPqcHeaderService.prodLineQuery(tenantId, dto));
    }

    @ApiOperation(value = "工序树状图查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/process/query")
    public ResponseEntity<List<QmsPqcHeaderVO2>> processQuery(@PathVariable(value = "organizationId")Long tenantId, QmsPqcHeaderDTO3 dto) {
        return Results.success(qmsPqcHeaderService.processQuery(tenantId, dto));
    }

    @ApiOperation(value = "巡检单生成")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/pgc/create")
    public ResponseEntity<?> pgcCreate(@PathVariable(value = "organizationId")Long tenantId,
                                       @RequestBody QmsPqcHeaderDTO dto) {
        qmsPqcHeaderService.pgcCreate(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "巡检单生成PDA版")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/pgc/create/pda")
    public ResponseEntity<QmsPqcHeaderDTO> pgcCreatePda(@PathVariable(value = "organizationId")Long tenantId,
                                       @RequestBody QmsPqcHeaderDTO dto) {
        qmsPqcHeaderService.pgcCreate(tenantId, dto);
        return Results.success(dto);
    }

    @ApiOperation(value = "巡检列表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/pqc/list/query")
    public ResponseEntity<Page<QmsPqcHeaderVO3>> pqcListQuery(@PathVariable(value = "organizationId")Long tenantId,
                                                              QmsPqcHeaderDTO4 dto, PageRequest pageRequest) {
        return Results.success(qmsPqcHeaderService.pqcListQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "巡检信息查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/pqc/info/query")
    public ResponseEntity<QmsPqcHeaderVO6> pqcInfoQuery(@PathVariable(value = "organizationId")Long tenantId,
                                                        String pqcHeaderId, PageRequest pageRequest) {
        return Results.success(qmsPqcHeaderService.pqcInfoQuery(tenantId, pqcHeaderId, pageRequest));
    }

    @ApiOperation(value = "巡检结果查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/pqc/result/query")
    public ResponseEntity<Page<QmsPqcHeaderVO7>> pqcResultQuery(@PathVariable(value = "organizationId")Long tenantId,
                                                              String pqcLineId, PageRequest pageRequest) {
        return Results.success(qmsPqcHeaderService.pqcResultQuery(tenantId, pqcLineId, pageRequest));
    }

    @ApiOperation(value = "文件上传")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/pgc/upload")
    public ResponseEntity<?> attachmentUpload(@PathVariable(value = "organizationId")Long tenantId,
                                       @RequestBody QmsPqcHeaderDTO5 dto) {
        qmsPqcHeaderService.attachmentUpload(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "文件上传PDA版")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/pgc/upload/pda")
    public ResponseEntity<QmsPqcHeaderDTO5> attachmentUploadPda(@PathVariable(value = "organizationId")Long tenantId,
                                              @RequestBody QmsPqcHeaderDTO5 dto) {
        qmsPqcHeaderService.attachmentUpload(tenantId, dto);
        return Results.success(dto);
    }

    @ApiOperation(value = "巡检保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/pgc/save")
    public ResponseEntity<QmsPqcHeaderDTO7> pqcSave(@PathVariable(value = "organizationId")Long tenantId,
                                       @RequestBody QmsPqcHeaderDTO7 dto) {
        return Results.success(qmsPqcHeaderService.pqcSave(tenantId, dto));
    }

    @ApiOperation(value = "巡检提交")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/pgc/submit")
    public ResponseEntity<?> pqcSubmit(@PathVariable(value = "organizationId")Long tenantId,
                                              @RequestBody QmsPqcHeaderDTO6 dto) {
        qmsPqcHeaderService.pqcSubmit(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "巡检平台海马汇版事业部LOV查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/pqc/area/query")
    public ResponseEntity<List<QmsPqcHeaderVO10>> areaLovQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                               QmsPqcHeaderVO10 dto){
        return Results.success(qmsPqcHeaderService.areaLovQuery(tenantId, dto));
    }

    @ApiOperation(value = "巡检平台海马汇版车间LOV查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/pqc/workshop/query")
    public ResponseEntity<List<QmsPqcHeaderVO11>> workshopLovQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                   QmsPqcHeaderVO11 dto){
        return Results.success(qmsPqcHeaderService.workshopLovQuery(tenantId, dto));
    }

    @ApiOperation(value = "巡检平台海马汇版产线LOV查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/pqc/prodLine/query")
    public ResponseEntity<List<QmsPqcHeaderVO12>> prodLineLovQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                   QmsPqcHeaderVO12 dto){
        return Results.success(qmsPqcHeaderService.prodLineLovQuery(tenantId, dto));
    }

    @ApiOperation(value = "巡检提交PDA版")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/pgc/submit/pda")
    public ResponseEntity<QmsPqcHeaderDTO6> pqcSubmitPda(@PathVariable(value = "organizationId")Long tenantId,
                                       @RequestBody QmsPqcHeaderDTO6 dto) {
        qmsPqcHeaderService.pqcSubmit(tenantId, dto);
        return Results.success(dto);
    }
}
