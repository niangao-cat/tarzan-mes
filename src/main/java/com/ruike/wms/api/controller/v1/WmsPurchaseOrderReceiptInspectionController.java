package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsPurchaseOrderReceiptInspectionDTO;
import com.ruike.wms.app.service.WmsPurchaseOrderReceiptInspectionService;
import com.ruike.wms.domain.vo.WmsPurchaseOrderReceiptInspectionVO;
import groovy.util.logging.Slf4j;
import io.choerodon.core.base.BaseController;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 采购订单接收检验统计报表
 *
 * @author li.zhang 2021/09/09 13:38
 */
@Slf4j
@RestController("wmsPurchaseOrderReceiptInspectionController.v1")
@RequestMapping("/v1/{organizationId}/wms-purchase-order-receipt-inspection")
@Api(tags = SwaggerApiConfig.WMS_PURCHASE_ORDER_RECEIPT_INSPECTION)
public class WmsPurchaseOrderReceiptInspectionController extends BaseController{

    @Autowired
    private WmsPurchaseOrderReceiptInspectionService wmsPurchaseOrderReceiptInspectionService;

    @ApiOperation(value = "采购订单接收检验统计报表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/list")
    public ResponseEntity<Page<WmsPurchaseOrderReceiptInspectionVO>> queryList(@PathVariable(value = "organizationId") Long tenantId,
                                                                               WmsPurchaseOrderReceiptInspectionDTO dto, PageRequest pageRequest) {
        dto.initParam();
        return Results.success(wmsPurchaseOrderReceiptInspectionService.queryList(tenantId,dto,pageRequest));
    }

    @ApiOperation(value = "采购订单接收检验统计报表导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/export", produces = "application/json;charset=UTF-8")
    @ExcelExport(WmsPurchaseOrderReceiptInspectionVO.class)
    public ResponseEntity<List<WmsPurchaseOrderReceiptInspectionVO>> export(@PathVariable(value = "organizationId") Long tenantId,
                                                                            WmsPurchaseOrderReceiptInspectionDTO dto,
                                                                            ExportParam exportParam,
                                                                            HttpServletResponse response) {
        dto.initParam();
        return Results.success(wmsPurchaseOrderReceiptInspectionService.export(tenantId,dto,exportParam));
    }
}
