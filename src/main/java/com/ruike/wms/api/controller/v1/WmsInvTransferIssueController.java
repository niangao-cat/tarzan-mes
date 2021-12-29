package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsInvTransferIssueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;

/**
 * 库存调拨发出执行 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-04-27 09:50:00
 */
@RestController("wmsInvTransferIssueController.v1")
@RequestMapping("/v1/{organizationId}/inv-transfer-issues")
@Api(tags = SwaggerApiConfig.WMS_INV_TRANSFER_ISSUE)
public class WmsInvTransferIssueController extends BaseController {

    @Autowired
    private WmsInvTransferIssueService wmsInvTransferIssueService;

    @ApiOperation(value = "单据扫码查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/doc", produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsInvTransferDTO> docQuery(@PathVariable("organizationId") Long tenantId, String docBarCode) {
        return Results.success(wmsInvTransferIssueService.docQuery(tenantId, docBarCode));
    }

    @ApiOperation(value = "实物条码扫码查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/material-lot", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<WmsCostCtrMaterialDTO3>> containerOrMaterialLotQuery(@PathVariable("organizationId") Long tenantId,
                                                                            @RequestBody WmsInvTransferDTO3 dto) {
        return Results.success(wmsInvTransferIssueService.containerOrMaterialLotQuery(tenantId, dto));
    }

    @ApiOperation(value = "执行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/execute", produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsInvTransferDTO> execute(@PathVariable("organizationId") Long tenantId, @RequestBody WmsInvTransferDTO5 dto) {
        return Results.success(wmsInvTransferIssueService.execute(tenantId, dto));
    }

    @ApiOperation(value = "明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/detail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsInvTransferDTO7> docDetailQuery(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody WmsInvTransferDTO6 dto) {
        return Results.success(wmsInvTransferIssueService.docDetailQuery(tenantId, dto));
    }

    @ApiOperation(value = "删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsInvTransferDTO7> deleteMaterialLot(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody WmsInvTransferDTO6 dto) {
        return Results.success(wmsInvTransferIssueService.deleteMaterialLot(tenantId, dto));
    }
}
