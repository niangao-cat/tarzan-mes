package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeSnBindEoService;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.domain.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;
import tarzan.modeling.domain.entity.MtModArea;
import com.ruike.hme.app.service.HmeWorkOrderManagementService;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.vo.MtWorkOrderVO50;

import java.util.ArrayList;
import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;

/**
 * 工单管理平台 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-03-30 13:36:08
 */
@RestController("hmeWorkOrderManagementController.v1")
@RequestMapping("/v1/{organizationId}/mt-work-order-management")
@Api(tags = SwaggerApiConfig.HME_WORK_ORDER_MANAGEMENT)
@Slf4j
public class HmeWorkOrderManagementController extends BaseController {

    @Autowired
    private HmeWorkOrderManagementService service;

    @Autowired
    private HmeSnBindEoService hmeSnBindEoService;

    @ApiOperation(value = "工单管理平台列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/wo-list")
    public ResponseEntity<Page<HmeWorkOrderVO58>> woListQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                           HmeWorkOrderVO58 dto, @ApiIgnore PageRequest pageRequest) {
        return Results.success(this.service.woListQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工单管理扩展字段保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> saveWoAttrsForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                           @RequestBody HmeWorkOrderVO59 workOrderAttrs) {
        this.service.saveExtendAttrForUi(tenantId, workOrderAttrs);
        return Results.success();

    }

    @ApiOperation("工单下达")
    @PostMapping(value = "/wo-release", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Void> woReleaseForUi(@PathVariable(value = "organizationId") Long tenantId,
                                              @RequestBody List<MtWorkOrderVO50> mtWorkOrder) {
        validList(mtWorkOrder);
        long startDate = System.currentTimeMillis();
        hmeSnBindEoService.handleWoAndEoRelease(tenantId, mtWorkOrder);
        log.info("=================================>工单管理平台-工单下达总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return Results.success();
    }

    @ApiOperation("工单暂停")
    @PostMapping(value = "/wo-abandon", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Void> woAbandonForUi(@PathVariable(value = "organizationId") Long tenantId,
                                             @RequestBody List<HmeWorkOrderVO60> workOrderIds) {
        validList(workOrderIds);
        this.service.woAbandonForUi(tenantId, workOrderIds);
        return Results.success();
    }

    @ApiOperation("工单撤销")
    @PostMapping(value = "/wo-close-cancel", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Void> woCloseCancelForUi(@PathVariable(value = "organizationId") Long tenantId,
                                             @RequestBody List<HmeWorkOrderVO60> workOrderIds) {
        validList(workOrderIds);
        this.service.woCloseCancelForUi(tenantId, workOrderIds);
        return Results.success();
    }

    @ApiOperation("工单关闭")
    @PostMapping(value = "/wo-close", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> woCloseForUi(@PathVariable(value = "organizationId") Long tenantId,
                                             @RequestBody List<HmeWorkOrderVO60> workOrderIds) {
        validList(workOrderIds);
        this.service.woCloseForUi(tenantId, workOrderIds);
        return Results.success();
    }

    @ApiOperation(value = "工单管理-车间LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/wo-workshop-lov")
    public ResponseEntity<Page<MtModArea>> workshopQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                         @ApiIgnore PageRequest pageRequest) {
        return Results.success(this.service.workshopQuery(tenantId, pageRequest));
    }

    @ApiOperation(value = "工单管理-事业部下拉框")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/wo-department")
    public ResponseEntity<List<HmeUserOrganizationVO2>> departmentQuery(@PathVariable(value = "organizationId") Long tenantId) {
        return Results.success(this.service.departmentQuery(tenantId));
    }

    @ApiOperation(value = "工单管理-分配产线LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/wo-prodLine")
    public ResponseEntity<Page<HmeWorkOrderVO61>> prodLineQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                HmeWorkOrderVO61 dto, PageRequest pageRequest){
        return Results.success(this.service.prodLineQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工单管理-分配产线校验")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value="/wo-prodLine/check", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> allocationProdLIneCheck(@PathVariable(value = "organizationId") Long tenantId,
                                                     @RequestBody List<HmeWorkOrderVO60> workOrderIds){
        validList(workOrderIds);
        this.service.allocationProdLineCheck(tenantId, workOrderIds);
        return Results.success();
    }

    @ApiOperation("工单管理-分配产线确认")
    @PostMapping(value = "/wo-prodLine/confirm", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> allocationProdLIneConfirm(@PathVariable(value = "organizationId") Long tenantId,
                                          @RequestBody HmeWorkOrderVO62 dto) {
        this.service.allocationProdLineConfirm(tenantId, dto);
        return Results.success();
    }
}
