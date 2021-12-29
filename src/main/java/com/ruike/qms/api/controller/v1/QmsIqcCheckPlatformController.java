package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.*;
import com.ruike.qms.app.service.QmsIqcCheckPlatformService;
import com.ruike.qms.domain.entity.QmsIqcLine;
import com.ruike.qms.domain.vo.*;
import com.ruike.wms.app.service.WmsMaterialLotService;
import com.ruike.wms.domain.vo.WmsMaterialLotExtendAttrVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description IQC检验平台
 * @Author tong.li
 * @Date 2020/5/12 10:21
 * @Version 1.0
 */
@RestController("qmsIqcCheckPlatformController.v1")
@RequestMapping("/v1/{organizationId}/qms-iqc-check-platform")
@Api(tags = SwaggerApiConfig.QMS_IQC_CHECK_PLATFORM)
@Slf4j
public class QmsIqcCheckPlatformController extends BaseController {
    @Autowired
    private QmsIqcCheckPlatformService qmsIqcCheckPlatformService;

    @ApiOperation(value = "主界面查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/main/query")
    public ResponseEntity<Page<QmsIqcCheckPlatformMainReturnDTO>> mainQuery(@PathVariable("organizationId") Long tenantId, QmsIqcCheckPlatformDTO qmsIqcCheckPlatformDTO, @ApiIgnore PageRequest pageRequest) {
        Page<QmsIqcCheckPlatformMainReturnDTO> list = qmsIqcCheckPlatformService.mainQuery(tenantId, pageRequest, qmsIqcCheckPlatformDTO);
        return Results.success(list);
    }

    @ApiOperation(value = "IQC检验界面  头查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/iqc/head/query")
    public ResponseEntity<Page<QmsIqcCheckPlatformIqcReturnDTO>> iqcHeadQuery(@PathVariable("organizationId") Long tenantId, QmsIqcCheckPlatformIqcQueryDTO iqcCheckPlatformIqcQueryDTO, @ApiIgnore PageRequest pageRequest) {
        Page<QmsIqcCheckPlatformIqcReturnDTO> list = qmsIqcCheckPlatformService.iqcHeadQuery(tenantId, pageRequest, iqcCheckPlatformIqcQueryDTO);
        return Results.success(list);
    }


    @ApiOperation(value = "IQC检验界面  明细查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/iqc/detail/query")
    public ResponseEntity<Page<QmsIqcCheckPlatformIqcReturnDetailDTO>> iqcDetailQuery(@PathVariable("organizationId") Long tenantId, String iqcLineId, @ApiIgnore PageRequest pageRequest) {
        Page<QmsIqcCheckPlatformIqcReturnDetailDTO> list = qmsIqcCheckPlatformService.iqcDetailQuery(tenantId, pageRequest, iqcLineId);
        return Results.success(list);
    }

    @ApiOperation(value = "IQC检验界面  行查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/iqc/line/query")
    public ResponseEntity<List<QmsIqcCheckPlatformIqcReturnLineDTO>> iqcLineQuery(@PathVariable("organizationId") Long tenantId, String iqcHeaderId, @ApiIgnore PageRequest pageRequest) {
        List<QmsIqcCheckPlatformIqcReturnLineDTO> list = qmsIqcCheckPlatformService.iqcLineQuery(tenantId, pageRequest, iqcHeaderId);
        return Results.success(list);
    }

    @ApiOperation(value = "IQC检验界面  保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/iqc/save")
    public ResponseEntity<List<QmsIqcCheckPlatformIqcSaveDTO>> iqcSave(@PathVariable("organizationId") Long tenantId, @RequestBody QmsIqcCheckPlatformIqcSaveDTO dto) {
        log.info("<====QmsIqcCheckPlatformController-iqcSave:{},{} ", tenantId, dto);
        List<QmsIqcCheckPlatformIqcSaveDTO> list = qmsIqcCheckPlatformService.iqcSave(tenantId, dto);
        return Results.success(list);
    }

    @ApiOperation(value = "IQC检验界面  提交")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/iqc/submit")
    public ResponseEntity<List<QmsIqcCheckPlatformIqcSaveDTO>> iqcSubmit(@PathVariable("organizationId") Long tenantId, @RequestBody QmsIqcCheckPlatformIqcSaveDTO dto) {
        log.info("<====QmsIqcCheckPlatformController-iqcSubmit:{},{} ", tenantId, dto);
        List<QmsIqcCheckPlatformIqcSaveDTO> list = qmsIqcCheckPlatformService.iqcSubmit(tenantId, dto);
        return Results.success(list);
    }


    @ApiOperation(value = "新建页面进入后自动查询行信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/create/page/line/query")
    public ResponseEntity<Page<QmsIqcCheckPlatformIqcReturnLineDTO>> createPageLineQuery(@PathVariable("organizationId") Long tenantId, String iqcHeaderId, @ApiIgnore PageRequest pageRequest) {
        Page<QmsIqcCheckPlatformIqcReturnLineDTO> list = qmsIqcCheckPlatformService.createPageLineQuery(tenantId, pageRequest, iqcHeaderId);
        return Results.success(list);
    }


    @ApiOperation(value = "新建页面点击新建按钮后跳转页面   选择检验组LOV后带出信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/create/page/lov/bring/data/query")
    public ResponseEntity<Page<QmsIqcCheckPlatformCreateBringDTO>> lovBringDataQuery(@PathVariable("organizationId") Long tenantId, String tagGroupId, String iqcHeaderId, @ApiIgnore PageRequest pageRequest) {
        Page<QmsIqcCheckPlatformCreateBringDTO> list = qmsIqcCheckPlatformService.lovBringDataQuery(tenantId, pageRequest, tagGroupId, iqcHeaderId);
        return Results.success(list);
    }

    @ApiOperation(value = "新建页面点击新建按钮后跳转页面   选择抽样方案LOV后带出信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/sample/lov/bring/data/query")
    public ResponseEntity<QmsIqcCheckPlatformCreateBringDTO2> sampleLovBringDataQuery(@PathVariable("organizationId") Long tenantId, String sampleTypeId, String iqcHeaderId) {
        QmsIqcCheckPlatformCreateBringDTO2 dto = qmsIqcCheckPlatformService.sampleLovBringDataQuery(tenantId, sampleTypeId, iqcHeaderId);
        return Results.success(dto);
    }


    @ApiOperation(value = "新建页面点击新建按钮或手动新建后保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/create/page/save")
    public ResponseEntity<Void> createPageSave(@PathVariable("organizationId") Long tenantId, @RequestBody QmsIqcCheckPlatformCreatePageSaveDTO2 saveData) {
        log.info("<====QmsIqcCheckPlatformController-createPageSave:{},{} ", tenantId, saveData);
        qmsIqcCheckPlatformService.createPageSave(tenantId, saveData);
        return Results.success();
    }


    @ApiOperation(value = "新建页面点击删除按钮")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping("/create/page/delete")
    public ResponseEntity<Void> createPageDelete(@PathVariable("organizationId") Long tenantId, @RequestBody QmsIqcCheckPlatformIqcDeleteDTO dto) {
        log.info("<====QmsIqcCheckPlatformController-createPageDelete:{},{} ", tenantId, dto);
        qmsIqcCheckPlatformService.createPageDelete(tenantId, dto);
        return Results.success();
    }


    @ApiOperation("上传附件")
    @PostMapping("/upload/attachment")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<QmsIqcLine> uploadAttachment(@PathVariable(value = "organizationId") Long tenantId, String iqcLineId, String uuid) {
        QmsIqcLine lineDto = qmsIqcCheckPlatformService.uploadAttachment(tenantId, iqcLineId, uuid);
        return Results.success(lineDto);
    }


    @ApiOperation(value = "获取抽样方案信息（前台）")
    @GetMapping(value = "/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<QmsIqcCheckPlatformDTO2>> listForUi(
            @PathVariable("organizationId") Long tenantId, QmsIqcCheckPlatformDTO2 dto, PageRequest pageRequest) {
        return Results.success(qmsIqcCheckPlatformService.listForUi(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "不良项数据查询")
    @GetMapping(value = "/nc/data/query/{inspectionId}", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<QmsIqcCheckPlatformVO>> ncDataQuery(@PathVariable("organizationId") Long tenantId,
                                                                   @PathVariable("inspectionId") String inspectionId,
                                                                   PageRequest pageRequest) {
        return Results.success(qmsIqcCheckPlatformService.ncDataQuery(tenantId, inspectionId, pageRequest));
    }

    @ApiOperation(value = "不良项数据删除")
    @PostMapping(value = "/nc/data/delete", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<QmsIqcCheckPlatformVO>> ncDataDelete(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody List<QmsIqcCheckPlatformVO> dtoList) {
        return Results.success(qmsIqcCheckPlatformService.ncDataDelete(tenantId, dtoList));
    }

    @ApiOperation(value = "不良项数据新增或更新")
    @PostMapping(value = "/nc/data/update", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<QmsIqcCheckPlatformDTO3> ncDataUpdate(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody QmsIqcCheckPlatformDTO3 dto) {
        return Results.success(qmsIqcCheckPlatformService.ncDataUpdate(tenantId, dto));
    }

    @ApiOperation(value = "物料批查询")
    @GetMapping(value = "/materialLot", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<QmsIqcMaterialLotVO>> materialLotListQuery(@PathVariable("organizationId") Long tenantId,
                                                                          @RequestParam("iqcHeaderId") String iqcHeaderId,
                                                                          String supplierLot,
                                                                          PageRequest pageRequest) {
        return Results.success(qmsIqcCheckPlatformService.materialLotListQuery(tenantId, iqcHeaderId, supplierLot, pageRequest));
    }

    @ApiOperation(value = "物料批更新")
    @PostMapping(value = "/materialLot", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Void> materialLotListUpdate(@PathVariable("organizationId") Long tenantId,
                                                      @RequestParam("iqcHeaderId") String iqcHeaderId,
                                                      @RequestBody List<WmsMaterialLotExtendAttrVO> list) {
        qmsIqcCheckPlatformService.materialLotBatchUpdate(tenantId, iqcHeaderId, list);
        return Results.success();
    }

    @ApiOperation(value = "IQC检验平台导出EXCEL")
    @GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(QmsIqcCheckPlatformExportVO.class)
    public ResponseEntity<List<QmsIqcCheckPlatformExportVO>> export(@PathVariable("organizationId") Long tenantId,
                                                                    QmsIqcCheckPlatformDTO dto,
                                                                    HttpServletResponse response,
                                                                    ExportParam exportParam) {
        return Results.success(qmsIqcCheckPlatformService.export(tenantId, dto));
    }

    @ApiOperation(value = "IQC检验平台-质量文件")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/quality-file-query")
    public ResponseEntity<List<QmsQualityFileVO>> qualityFileQuery(@PathVariable("organizationId") Long tenantId,
                                                                   String iqcHeaderId) {
        return Results.success(qmsIqcCheckPlatformService.qualityFileQuery(tenantId, iqcHeaderId));
    }


    @ApiOperation(value = "IQC检验平台-导入信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/quality-file-import-query")
    public ResponseEntity<Page<QmsQualityFileVO2>> qualityFileImportQuery(@PathVariable("organizationId") Long tenantId,
                                                                          String fileUrl,
                                                                          @ApiIgnore PageRequest pageRequest) {
        return Results.success(qmsIqcCheckPlatformService.qualityFileImportQuery(tenantId, fileUrl, pageRequest));
    }


}
