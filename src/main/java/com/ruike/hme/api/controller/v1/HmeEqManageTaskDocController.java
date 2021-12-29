package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEqTaskDocLineQueryDTO;
import com.ruike.hme.api.dto.HmeEqTaskDocQueryDTO;
import com.ruike.hme.api.dto.HmeFunctionExportDTO;
import com.ruike.hme.api.dto.HmePreSelectionReturnDTO8;
import com.ruike.hme.app.service.HmeEqManageTaskDocService;
import com.ruike.hme.domain.vo.HmeEqTaskDocAndLineExportVO;
import com.ruike.hme.domain.vo.HmeEqTaskHisVO;
import com.ruike.hme.domain.vo.HmeSelectionDetailsQueryVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.api.dto.WmsCostCtrMaterialDTO3;
import com.ruike.wms.api.dto.WmsCostCtrMaterialDTO4;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeEqManageTaskDoc;
import com.ruike.hme.domain.repository.HmeEqManageTaskDocRepository;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 设备管理任务单表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-06-16 16:06:11
 */
@RestController("hmeEqManageTaskDocController.v1")
@RequestMapping("/v1/{organizationId}/hme-eq-manage-task-doc")
@Api(tags = SwaggerApiConfig.HME_EQ_MANAGE_TASK_DOC)
public class HmeEqManageTaskDocController extends BaseController {

    @Autowired
    private HmeEqManageTaskDocService hmeEqManageTaskDocService;

    @ApiOperation(value = "设备管理任务单表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeEqTaskDocQueryDTO>> list(@PathVariable("organizationId") Long tenantId,
                                                         HmeEqTaskDocQueryDTO dto,
                                                         @ApiIgnore PageRequest pageRequest) {
        return Results.success((this.hmeEqManageTaskDocService.queryTaskDocList(tenantId, dto, pageRequest)));
    }

    @ApiOperation(value = "设备管理任务单行列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/line-list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeEqTaskDocLineQueryDTO>> lineList(@PathVariable("organizationId") Long tenantId,
                                                             @ApiParam(value = "单据ID", required = true) @RequestParam String taskDocId,
                                                             @ApiIgnore PageRequest pageRequest) {
        return Results.success(this.hmeEqManageTaskDocService.queryTaskDocLineList(tenantId, taskDocId, pageRequest));
    }

    @ApiOperation(value = "创建点检单")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/create", produces = "application/json;charset=UTF-8")
    public ResponseData<Void> createEqCheckDoc(@PathVariable("organizationId") Long tenantId) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
//            hmeEqManageTaskDocService.createEqTaskDoc(tenantId, HmeConstants.Cycle.SHIFT);
            hmeEqManageTaskDocService.createEqTaskDocPlus(tenantId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "编辑任务行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeEqTaskDocLineQueryDTO> updateEqCheckDoc(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEqTaskDocLineQueryDTO dto) {
        return Results.success(hmeEqManageTaskDocService.updateEqCheckDoc(tenantId, dto));
    }

    @ApiOperation(value = "编辑任务头")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/update-task-doc", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeEqTaskDocQueryDTO> updateTaskDoc(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEqTaskDocQueryDTO dto) {
        return Results.success(hmeEqManageTaskDocService.updateTaskDoc(tenantId, dto));
    }

    @ApiOperation(value = "任务行历史查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/task-history-list-query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeEqTaskHisVO>> taskHistoryListQuery(@PathVariable("organizationId") Long tenantId, String taskDocLineId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeEqManageTaskDocService.taskHistoryListQuery(tenantId, taskDocLineId, pageRequest));
    }

    @ApiOperation(value = "设备管理任务单表列表导出")
    @GetMapping(value = "/list/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeEqTaskDocQueryDTO.class)
    public ResponseEntity<List<HmeEqTaskDocQueryDTO>> listExport(@PathVariable("organizationId") Long tenantId,
                                                                 HmeEqTaskDocQueryDTO dto,
                                                                 HttpServletResponse response,
                                                                 ExportParam exportParam) {
        List<HmeEqTaskDocQueryDTO> hmeEqTaskDocQueryDTOS = hmeEqManageTaskDocService.listExport(tenantId, dto);
        return Results.success(hmeEqTaskDocQueryDTOS);
    }

    @ApiOperation(value = "设备管理任务单行导出")
    @GetMapping(value = "/list-line-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeEqTaskDocLineQueryDTO.class)
    public ResponseEntity<List<HmeEqTaskDocLineQueryDTO>> listLineExport(@PathVariable("organizationId") Long tenantId,
                                                                 String taskDocId,
                                                                 HttpServletResponse response,
                                                                 ExportParam exportParam) {
        List<HmeEqTaskDocLineQueryDTO> hmeEqTaskDocLineQueryDTOS = hmeEqManageTaskDocService.listLineExport(tenantId, taskDocId);
        return Results.success(hmeEqTaskDocLineQueryDTOS);
    }

    @ApiOperation( value = "设备管理任务单&结果导出")
    @GetMapping(value = "/list-head-and-line-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeEqTaskDocAndLineExportVO.class)
    public ResponseEntity<List<HmeEqTaskDocAndLineExportVO>> listHeadAndLineExport(@PathVariable("organizationId") Long tenantId,
                                                                                   HmeEqTaskDocQueryDTO dto,
                                                                                   HttpServletResponse response,
                                                                                   ExportParam exportParam) {
        List<HmeEqTaskDocAndLineExportVO> eqTaskDocAndLineExportVOList = hmeEqManageTaskDocService.listHeadAndLineExport(tenantId, dto);
        return Results.success(eqTaskDocAndLineExportVOList);

    }
}
