package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeDocActionCommand;
import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeDocCreateCommand;
import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeDocModifyCommand;
import com.ruike.hme.api.dto.query.HmeEquipmentStocktakeDocQuery;
import com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation;
import com.ruike.hme.app.service.HmeEquipmentStocktakeDocService;
import com.ruike.hme.domain.repository.HmeEquipmentStocktakeDocRepository;
import com.ruike.hme.domain.service.HmeEquipmentStocktakeDomainService;
import com.ruike.hme.domain.vo.HmeEquipmentStocktakeExportVO;
import com.ruike.hme.domain.vo.HmeEquipmentVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 设备盘点单 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2021-03-31 09:32:46
 */
@RestController("hmeEquipmentStocktakeDocController.v1")
@RequestMapping("/v1/{organizationId}/hme-equipment-stocktake-docs")
@Api(tags = SwaggerApiConfig.HME_EQUIPMENT_STOCKTAKE_DOC)
public class HmeEquipmentStocktakeDocController extends BaseController {

    private final HmeEquipmentStocktakeDocRepository hmeEquipmentStocktakeDocRepository;
    private final HmeEquipmentStocktakeDomainService domainService;
    private final HmeEquipmentStocktakeDocService docService;

    public HmeEquipmentStocktakeDocController(HmeEquipmentStocktakeDocRepository hmeEquipmentStocktakeDocRepository, HmeEquipmentStocktakeDomainService domainService, HmeEquipmentStocktakeDocService docService) {
        this.hmeEquipmentStocktakeDocRepository = hmeEquipmentStocktakeDocRepository;
        this.domainService = domainService;
        this.docService = docService;
    }

    @ApiOperation(value = "设备盘点单 分页列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeEquipmentStocktakeDocRepresentation>> list(@PathVariable("organizationId") Long tenantId,
                                                                             HmeEquipmentStocktakeDocQuery query,
                                                                             @ApiIgnore PageRequest pageRequest) {
        query.paramInit(tenantId);
        Page<HmeEquipmentStocktakeDocRepresentation> list = hmeEquipmentStocktakeDocRepository.page(tenantId,query, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "设备盘点单 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(HmeEquipmentStocktakeExportVO.class)
    public ResponseEntity<List<HmeEquipmentStocktakeExportVO>> export(@PathVariable("organizationId") Long tenantId,
                                                                      HmeEquipmentStocktakeDocQuery query,
                                                                      ExportParam exportParam,
                                                                      HttpServletResponse response) {
        query.paramInit(tenantId);
        List<HmeEquipmentStocktakeExportVO> list = hmeEquipmentStocktakeDocRepository.export(tenantId,query);
        return Results.success(list);
    }

    @ApiOperation(value = "设备盘点单 明细查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{stocktakeId}")
    public ResponseEntity<HmeEquipmentStocktakeDocRepresentation> detail(@PathVariable("organizationId") Long tenantId,
                                                                         @PathVariable String stocktakeId) {
        HmeEquipmentStocktakeDocRepresentation representation = hmeEquipmentStocktakeDocRepository.byId(stocktakeId, tenantId);
        return Results.success(representation);
    }

    @ApiOperation(value = "设备盘点单 完成校验")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{stocktakeId}/valid/complete")
    public ResponseEntity<Boolean> completeValid(@PathVariable("organizationId") Long tenantId,
                                                 @PathVariable String stocktakeId) {
        return Results.success(domainService.completeValid(stocktakeId));
    }

    @ApiOperation(value = "设备盘点单 创建单据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeEquipmentStocktakeDocRepresentation> create(@PathVariable("organizationId") Long tenantId,
                                                                         @RequestBody HmeEquipmentStocktakeDocCreateCommand command) {
        command.setTenantId(tenantId);
        this.validObject(command);
        HmeEquipmentStocktakeDocRepresentation representation = domainService.createDoc(command);
        return Results.success(representation);
    }

    @ApiOperation(value = "设备盘点单 完成单据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/complete")
    public ResponseEntity<Void> update(@PathVariable("organizationId") Long tenantId,
                                       @RequestBody HmeEquipmentStocktakeDocActionCommand command) {
        command.setTenantId(tenantId);
        this.validObject(command);
        docService.complete(command);
        return Results.success();
    }

    @ApiOperation(value = "设备盘点单 取消单据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cancel")
    public ResponseEntity<Void> cancel(@PathVariable("organizationId") Long tenantId,
                                       @RequestBody HmeEquipmentStocktakeDocActionCommand command) {
        command.setTenantId(tenantId);
        this.validObject(command);
        docService.cancel(command);
        return Results.success();
    }

    @ApiOperation(value = "设备盘点单 更新单据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<Void> update(@PathVariable("organizationId") Long tenantId,
                                       @RequestBody HmeEquipmentStocktakeDocModifyCommand command) {
        command.setTenantId(tenantId);
        this.validObject(command);
        docService.update(command);
        return Results.success();
    }

    @ApiOperation(value = "设备盘点单-创建")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/create-doc")
    public ResponseEntity<HmeEquipmentStocktakeDocRepresentation> createDoc(@PathVariable("organizationId") Long tenantId,
                                       @RequestBody HmeEquipmentVO equipmentVO) {
        equipmentVO.initParam();
        return Results.success(docService.createDoc(tenantId, equipmentVO));
    }

}
