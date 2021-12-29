package com.ruike.wms.api.controller.v1;

import com.ruike.wms.app.service.WmsPurchaseReturnService;
import com.ruike.wms.domain.vo.WmsPurchaseReturnDetailsVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnHeadVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnLineVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 采购退货平台
 *
 * @author sanfeng.zhang@hand-china.com 2020/11/9 12:51
 */
@RestController("wmsPurchaseReturnController.v1")
@RequestMapping("/v1/{organizationId}/wms-purchase-return")
@Api(tags = SwaggerApiConfig.WMS_PURCHASE_RETURN)
public class WmsPurchaseReturnController {

    @Autowired
    private WmsPurchaseReturnService wmsPurchaseReturnService;

    @ApiOperation(value = "采购退货平台-头信息查询")
    @GetMapping(value = {"/list-head-ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsPurchaseReturnHeadVO>> purchaseReturnHeaderQuery(@PathVariable("organizationId") Long tenantId,
                                                                                   WmsPurchaseReturnVO wmsPurchaseReturnVO, @ApiIgnore PageRequest pageRequest) {
        return Results.success(wmsPurchaseReturnService.purchaseReturnHeaderQuery(tenantId, pageRequest, wmsPurchaseReturnVO));
    }

    @ApiOperation(value = "采购退货平台-行信息查询")
    @GetMapping(value = {"/list-line-ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsPurchaseReturnLineVO>> purchaseReturnLineQuery(@PathVariable("organizationId") Long tenantId,
                                                                                 @RequestParam("sourceDocId") String sourceDocId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(wmsPurchaseReturnService.purchaseReturnLineQuery(tenantId, pageRequest, sourceDocId));
    }

    @ApiOperation(value = "采购退货平台-明细信息查询")
    @GetMapping(value = {"/list-details-ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsPurchaseReturnDetailsVO>> purchaseReturnDetailsQuery(@PathVariable("organizationId") Long tenantId,
                                                                                       @RequestParam("instructionId") String instructionId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(wmsPurchaseReturnService.purchaseReturnDetailsQuery(tenantId, pageRequest, instructionId));
    }

    @PostMapping("/pdf")
    @ApiOperation("退货采购PDF打印")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> deliveryPrintPdf(@PathVariable("organizationId") Long tenantId,
                                              @RequestBody List<String> instructionDocIds,
                                              HttpServletResponse response) {
        wmsPurchaseReturnService.multiplePrint(tenantId, instructionDocIds,response);
        return Results.success();
    }
}
