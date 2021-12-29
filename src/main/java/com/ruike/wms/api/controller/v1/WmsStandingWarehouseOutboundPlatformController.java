package com.ruike.wms.api.controller.v1;

import com.ruike.itf.domain.vo.ItfFinishDeliveryInstructionIfaceVO;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsStandingWarehouseOutboundPlatformService;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static tarzan.config.SwaggerApiConfig.WMS_STANDING_WAREHOUSE_OUTBOUND_PLATFORM;

@RestController("WmsStandingWarehouseOutboundPlatformController.v1")
@RequestMapping("/v1/{organizationId}/wms-standing-outbound")
@Api(tags = WMS_STANDING_WAREHOUSE_OUTBOUND_PLATFORM)
public class WmsStandingWarehouseOutboundPlatformController extends BaseController {

    @Autowired
    private WmsStandingWarehouseOutboundPlatformService service;

    @ApiOperation(value = "头查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/head-query")
    public ResponseEntity<WmsStandingWarehouseOutboundPlatformHeadDTO> queryHead(@PathVariable("organizationId") Long tenantId,
                                                                                 @RequestParam String instructionDocNum) {
        return Results.success(service.queryHead(tenantId, instructionDocNum));
    }

    @ApiOperation(value = "行查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/line-query")
    public ResponseEntity<List<WmsStandingWarehouseOutboundPlatformDTO>> queryLineList(@PathVariable("organizationId") Long tenantId,
                                                                                       @RequestParam String instructionDocId) {
        return Results.success(service.queryList(tenantId, instructionDocId));
    }

    @ApiOperation(value = "SN指定查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/sn-specified")
    public ResponseEntity<List<WmsStandingWarehouseOutboundPlatformLineDTO>> snSpecified(@PathVariable("organizationId") Long tenantId,
                                                                                         @RequestBody WmsStandingWarehouseOutboundPlatformDTO dto) {
        return Results.success(service.snSpecified(tenantId, dto));
    }

    @ApiOperation(value = "SN批量录入判断是否允许编辑")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/sn-edit")
    public ResponseData<String> snEntry(@PathVariable("organizationId") Long tenantId,
                                        @RequestParam String instructionId,
                                        @RequestParam String sn) {
        ResponseData<String> responseData = new ResponseData<>();
        try {
            responseData.setRows(service.snEntry(tenantId, instructionId,sn));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
        //return Results.success(service.snEntry(tenantId, instructionId));
    }

    @ApiOperation(value = "SN批量录入校验")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/sn-batch-check")
    public ResponseData<Void> snCheck(@PathVariable("organizationId") Long tenantId,
                                      @RequestBody WmsStandingWarehouseOutboundPlatformDTO dto) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            responseData.setRows(service.snCheck(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
        //return Results.success(service.snCheck(tenantId, dto));
    }


    @ApiOperation(value = "SN批量录入")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/sn-batch-insert")
    public ResponseEntity<List<WmsStandingWarehouseOutboundPlatformLineDTO>> snBatchEntry(@PathVariable("organizationId") Long tenantId,
                                                                                          @RequestBody WmsStandingWarehouseOutboundPlatformDTO dto) {
        return Results.success(service.snBatchEntry(tenantId, dto));
    }

    @ApiOperation(value = "SN批量录入保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/sn-batch-insert-save")
    public ResponseData<Void> snBatchSaveEntry(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody WmsStandingWarehouseOutboundPlatformDTO dto) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            responseData.setRows(service.snBatchSaveEntry(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "SN保存删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/sn-delete")
    public ResponseData<Void> snDeleteEntry(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody WmsStandingWarehouseOutboundPlatformDTO dto) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            responseData.setRows(service.snDeleteEntry(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "批量出库")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/sn-batch-outbound")
    public ResponseEntity<List<ItfFinishDeliveryInstructionIfaceVO>> snBatchOutBound(@PathVariable("organizationId") Long tenantId,
                                                                                     @RequestBody List<WmsStandingWarehouseOutboundPlatformDTO> dtoList) {
        return Results.success(service.snBatchOutBound(tenantId, dtoList));
    }

    @ApiOperation(value = "界面查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/mainQuery")
    public ResponseEntity<WmsStandingWarehouseOutboundPlatformReturnDTO> mainQuery(@PathVariable("organizationId") Long tenantId) {
        return Results.success(service.mainQuery(tenantId));
    }

    @ApiOperation(value = "清单")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/figure")
    public ResponseEntity<Page<WmsStandingWarehouseOutboundPlatformReturnDTO2>> figure(@PathVariable("organizationId") Long tenantId,
                                                                                       PageRequest pageRequest) {
        return Results.success(service.figure(tenantId,pageRequest));
    }

    @ApiOperation(value = "出库任务取消")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cancel")
    public ResponseData<List<ItfFinishDeliveryInstructionIfaceVO>> snCancel(@PathVariable("organizationId") Long tenantId,
                                                                                 @RequestBody WmsStandingWarehouseOutboundPlatformReturnDTO2 dto) {
        ResponseData<List<ItfFinishDeliveryInstructionIfaceVO>> responseData = new ResponseData<>();
        try {
            responseData.setRows(service.snCancel(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
