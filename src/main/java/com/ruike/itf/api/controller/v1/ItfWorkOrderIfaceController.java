package com.ruike.itf.api.controller.v1;

import com.alibaba.fastjson.JSONArray;
import com.ruike.itf.api.dto.*;
import com.ruike.itf.app.service.ItfWorkOrderIfaceService;
import com.ruike.itf.domain.vo.ItfWoStatusSendErp;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import tarzan.iface.domain.repository.MtBomComponentIfaceRepository;
import tarzan.iface.domain.repository.MtOperationComponentIfaceRepository;
import tarzan.iface.domain.repository.MtRoutingOperationIfaceRepository;
import tarzan.iface.domain.repository.MtWorkOrderIfaceRepository;

import java.util.List;

/**
 * 工单接口表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@Slf4j
@RestController("itfWorkOrderIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-work-order-ifaces")
public class ItfWorkOrderIfaceController extends BaseController {

    @Autowired
    private ItfWorkOrderIfaceService service;

    @Autowired
    private MtWorkOrderIfaceRepository mtWorkOrderIfaceRepository;

    @Autowired
    private MtRoutingOperationIfaceRepository mtRoutingOperationIfaceRepository;

    @Autowired
    private MtBomComponentIfaceRepository mtBomComponentIfaceRepository;

    @Autowired
    private MtOperationComponentIfaceRepository mtOperationComponentIfaceRepository;


    /**
     * 工单状态回传接口
     *
     * @param tenantId
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "工单状态回传接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/ErpWorkOrderStatusReturnRestProxy")
    public ResponseEntity ErpWorkOrderStatusReturnRestProxy(@PathVariable("organizationId") Long tenantId,@RequestBody List<String> workOrderIds) {
        try {
            service.erpWorkOrderStatusReturnRestProxy(tenantId,workOrderIds);
            return Results.success();
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }

    /**
     * 工单状态回传接口
     *
     * @param tenantId
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "工单降阶品结算比例接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/ErpReducedSettleRadioUpdateRestProxy")
    public ResponseEntity ErpReducedSettleRadioUpdateRestProxy(@PathVariable("organizationId") Long tenantId,@RequestBody String dataStr) {
        log.info("<==== ErpReducedSettleRadioUpdateRestProxy request Success Payload: {}", dataStr);
        try {
            List<ErpReducedSettleRadioUpdateDTO> dto = JSONArray.parseArray(dataStr, ErpReducedSettleRadioUpdateDTO.class);
            List<ErpReducedSettleRadioUpdateDTO> result = service.erpReducedSettleRadioUpdateRestProxy(tenantId,dto);
            return Results.success(result);
        } catch (Exception e) {
            log.info("<==== ErpReducedSettleRadioUpdateRestProxy request Error Payload: {}", e.getMessage());
            return Results.error(e.getMessage());
        }
    }

    /**
     * 工单接口API
     *
     * @param tenantId
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "工单接口API")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/work-order-interface-import")
    public ResponseEntity workOrderInterfaceImport(@PathVariable("organizationId") Long tenantId, Long batchId) {
        try {
            mtWorkOrderIfaceRepository.myWorkOrderInterfaceImport(tenantId,batchId);
            return Results.success();
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }

    /**
     * 工艺路线接口API
     *
     * @param tenantId
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "工艺路线接口API")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/router-interface-import")
    public ResponseEntity routerInterfaceImport(@PathVariable("organizationId") Long tenantId, Long batchId) {
        try {
            mtRoutingOperationIfaceRepository.myRouterInterfaceImport(tenantId,batchId);
            return Results.success();
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }

    /**
     * BOM接口API
     *
     * @param tenantId
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "BOM接口API")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/bom-interface-import")
    public ResponseEntity bomInterfaceImport(@PathVariable("organizationId") Long tenantId, Long batchId) {
        try {
            mtBomComponentIfaceRepository.myBomInterfaceImport(tenantId,batchId);
            return Results.success();
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }

    /**
     * 工序组件接口API
     *
     * @param tenantId
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "工序组件接口API")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/operation-component-interface-import")
    public ResponseEntity operationComponentInterfaceImport(@PathVariable("organizationId") Long tenantId, Long batchId) {
        try {
            mtOperationComponentIfaceRepository.myOperationComponentInterfaceImport(tenantId,batchId);
            return Results.success();
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }

}
