package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsDullMaterialImportQueryRequestDTO;
import com.ruike.wms.api.dto.WmsDullMaterialImportQueryResponseDTO;
import com.ruike.wms.api.dto.WmsDullMaterialQueryRequestDTO;
import com.ruike.wms.api.dto.WmsDullMaterialQueryResponseDTO;
import com.ruike.wms.app.service.WmsDullMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import lombok.extern.slf4j.Slf4j;

import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;

import javax.servlet.http.HttpServletResponse;

/**
 * @Classname DullMaterialController
 * @Description 呆滞物料报表
 * @Date 2019/10/29 14:09
 * @Author by {HuangYuBin}
 */
@RestController("wmsDullMaterialController.v1")
@RequestMapping("/v1/{organizationId}/dull-material")
@Api(tags = "wmsDullMaterial")
@Slf4j
public class WmsDullMaterialController extends BaseController {
	@Autowired
	WmsDullMaterialService service;

	@ApiOperation(value = "呆滞物料报表查询")
	@GetMapping(value = "/query/list", produces = "application/json;charset=UTF-8")
	@Permission(level = ResourceLevel.ORGANIZATION)
	public ResponseEntity<Page<WmsDullMaterialQueryResponseDTO>> queryList(@PathVariable("organizationId") Long tenantId,
																		 WmsDullMaterialQueryRequestDTO dto,
																		 PageRequest pageRequest) {
		log.info("<====TransactionTypeController-queryList:{},{}", tenantId, dto);
		return Results.success(service.queryList(tenantId, pageRequest, dto));
	}

	@ApiOperation(value = "呆滞物料报表-导出")
	@GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
	@Permission(level = ResourceLevel.ORGANIZATION)
	@ExcelExport(WmsDullMaterialQueryResponseDTO.class)
	public ResponseEntity<List<WmsDullMaterialQueryResponseDTO>> listForExport(@PathVariable("organizationId") Long tenantId,
															   ExportParam exportParam,
															   HttpServletResponse response,
																WmsDullMaterialQueryRequestDTO condition) {
		return Results.success(service.exportList(tenantId, condition));
	}

	@ApiOperation(value = "呆滞物料导入查询")
	@GetMapping(value = "/import/query", produces = "application/json;charset=UTF-8")
	@Permission(level = ResourceLevel.ORGANIZATION)
	public ResponseEntity<Page<WmsDullMaterialImportQueryResponseDTO>> importQuery(@PathVariable("organizationId") Long tenantId,
																				 WmsDullMaterialImportQueryRequestDTO dto,
																				 PageRequest pageRequest) {
		log.info("<====TransactionTypeController-importQuery:{},{}", tenantId, dto);
		return Results.success(service.importQuery(tenantId, pageRequest, dto));
	}

	@ApiOperation(value = "呆滞物料导入保存")
	@PostMapping(value = "/import/save", produces = "application/json;charset=UTF-8")
	@Permission(level = ResourceLevel.ORGANIZATION)
	public ResponseEntity<String> importSave(@PathVariable("organizationId") Long tenantId,
											 @RequestBody List<WmsDullMaterialImportQueryResponseDTO> dto) {
		log.info("<====TransactionTypeController-importSave:{},{}", tenantId, dto);
		service.importSave(tenantId, dto);
		return Results.success();
	}
}