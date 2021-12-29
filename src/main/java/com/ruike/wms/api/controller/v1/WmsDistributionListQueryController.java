package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsReplenishmentCreateDTO;
import com.ruike.wms.app.service.WmsDistributionListQueryService;
import com.ruike.wms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.entity.MtNumrangeHis;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 配送单查询 API管理
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/3 03:38:26
 */
@Slf4j
@RestController("WmsDistributionListQueryController.v1")
@RequestMapping("/v1/{organizationId}/wms-distribution-list-query")
@Api(tags = SwaggerApiConfig.WMS_DISTRIBUTION_LIST_QUERY)
public class WmsDistributionListQueryController extends BaseController {

    private final WmsDistributionListQueryService wmsDistributionListQueryService;

    public WmsDistributionListQueryController(WmsDistributionListQueryService wmsDistributionListQueryService) {
        this.wmsDistributionListQueryService = wmsDistributionListQueryService;
    }

    @ApiOperation(value = "查询配送单头")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-instructionDoc")
    public ResponseEntity<Page<WmsDistributionListQueryVO>> queryInstructionDoc(@PathVariable("organizationId") Long tenantId, WmsDistributionListQueryVO dto, @ApiIgnore @SortDefault(value = MtNumrangeHis.FIELD_CREATION_DATE,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(wmsDistributionListQueryService.propertyDistributionDocQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "查询配送单行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-instruction")
    public ResponseEntity<Page<WmsDistributionListQueryVO1>> queryInstruction(@PathVariable("organizationId") Long tenantId, String instructionDocId, @ApiIgnore @SortDefault(value = MtNumrangeHis.FIELD_CREATION_DATE,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(wmsDistributionListQueryService.propertyDistributionQuery(tenantId, instructionDocId, pageRequest));
    }

    @ApiOperation(value = "查询配送单明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-instructionDtl")
    public ResponseEntity<Page<WmsDistributionListQueryVO2>> queryInstructionDtl(@PathVariable("organizationId") Long tenantId, String instructionId, @ApiIgnore @SortDefault(value = MtNumrangeHis.FIELD_CREATION_DATE,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(wmsDistributionListQueryService.propertyDistributionDtlQuery(tenantId, instructionId, pageRequest));
    }

    @ApiOperation(value = "查询补料单行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/replenishment-line")
    public ResponseEntity<List<WmsReplenishmentLineVO>> queryReplenishmentLine(@PathVariable("organizationId") Long tenantId, String idList, @ApiIgnore PageRequest pageRequest) {
        List<String> docIdList = Arrays.asList(idList.split(","));
        return Results.success(wmsDistributionListQueryService.replenishmentLineGet(tenantId, docIdList, pageRequest));
    }

    @PostMapping("/pdf")
    @ApiOperation("配送单PDF打印")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> distributionPrintPdf(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody List<String> instructionDocIds,
                                                  HttpServletResponse response) {
        wmsDistributionListQueryService.multiplePrint(tenantId, instructionDocIds, response);
        return Results.success();
    }

    @ApiOperation(value = "取消配送单")
    @PostMapping("/cancel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> cancelDistribution(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody List<String> instructionDocIds) {
        wmsDistributionListQueryService.cancelDistribution(tenantId, instructionDocIds);
        return Results.success();
    }

    @ApiOperation(value = "关闭配送单")
    @PostMapping("/close")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> closeDistribution(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody List<String> instructionDocIds) {
        wmsDistributionListQueryService.closeDistribution(tenantId, instructionDocIds);
        return Results.success();
    }

    @ApiOperation(value = "生成补料单")
    @PostMapping("/replenishment-doc")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> replenishmentDocCreate(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody WmsReplenishmentCreateDTO dto) {
        this.validObject(dto);
        wmsDistributionListQueryService.replenishmentDocCreate(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "配送单导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/instruction-doc-export")
    @ExcelExport(WmsDistributionDocVO.class)
    public ResponseEntity<List<WmsDistributionDocVO>> instructionDocExport(@PathVariable("organizationId") Long tenantId,
                                                                                 String instructionDocId,
                                                                                 ExportParam exportParam,
                                                                                 HttpServletResponse response) {
        return Results.success(wmsDistributionListQueryService.instructionDocExport(tenantId, instructionDocId));
    }
}
