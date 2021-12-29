package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsPurchaseOrderDTO;
import com.ruike.wms.app.service.WmsPurchaseOrderService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

/**
 * @program: tarzan-mes
 * @description: 采购订单相关api
 * @author: han.zhang
 * @create: 2020/03/19 10:43
 */
@RestController("purchaseOrderController.v1")
@RequestMapping("/v1/{organizationId}/purchase-order")
@Api(tags = SwaggerApiConfig.WMS_PURCHASE_ORDER)
public class WmsPurchaseOrderController extends BaseController {
    @Autowired
    private WmsPurchaseOrderService mtPurchaseOrderService;

    /**
     * @Description 采购订单头信息查询
     * @param tenantId 租户id
     * @param condition 查询条件
     * @param pageRequest 页码
     * @return org.springframework.http.ResponseEntity<?>
     * @Date 2020-03-19 16:01
     * @Author han.zhang
     */
    @ApiOperation(value = "检验项目 数据查询")
    @GetMapping(value = {"/list/ui/head"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    public ResponseEntity<?> listPurchaseOrderForUi(@PathVariable("organizationId") Long tenantId,
                                                    WmsPurchaseOrderDTO condition, @ApiIgnore PageRequest pageRequest) {
        return Results.success(mtPurchaseOrderService.selectHeadData(condition,pageRequest, tenantId));
    }

    /**
     * @Description
     * @param tenantId 租户id
     * @param sourceInstructionId 头id
     * @param pageRequest 页码
     * @return org.springframework.http.ResponseEntity<?>
     * @Date 2020-03-19 16:01
     * @Author han.zhang
     */
    @ApiOperation(value = "检验项目 数据查询")
    @GetMapping(value = {"/list/ui/line"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    public ResponseEntity<?> listPurchaseOrderLineForUi(@PathVariable("organizationId") Long tenantId,
                                                        String sourceInstructionId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(mtPurchaseOrderService.selectLineData(sourceInstructionId,pageRequest));
    }


}