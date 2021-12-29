package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsCostCenterReturnService;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.ResponseData;
import org.apache.poi.ss.formula.functions.T;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName WmsCostCenterReturnController
 * @Deacription 成本中心退料单执行
 * @Author ywz
 * @Date 2020/4/20 08:03
 * @Version 1.0
 **/
@RestController("wmsCostCenterReturnController.v1")
@RequestMapping("/v1/{organizationId}/wms-cost-center-return")
@Api(tags = SwaggerApiConfig.WMS_COST_CENTER_RETURN)
public class WmsCostCenterReturnController extends BaseController {

    @Autowired
    private WmsCostCenterReturnService wmsCostCenterReturnService;

    /**
     * @Description 成本中心退料单扫描条码
     * @Author wenzhang.yu
     * @Date 2020/4/20 11:59
     * @param tenantId
     * @param instructionDocNum
     * @return org.springframework.http.ResponseEntity<?>
     * @version 1.0
     **/
    @ApiOperation(value = "扫描/输入成本中心退料单号")
    @GetMapping(value = {"/scan/doc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsMaterialReturnScanDTO> returnCodeScan(@PathVariable("organizationId") Long tenantId,
                                                                 String instructionDocNum, String instructionDocId) {
        return Results.success(wmsCostCenterReturnService.returnCodeScan(tenantId, instructionDocNum,instructionDocId));
    }


    @ApiOperation(value = "扫描/输入货位")
    @PostMapping(value = {"/scan/locator"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsMaterialReturnScanLocatorDTO> returnLocatorScan(@PathVariable("organizationId") Long tenantId,@RequestBody WmsCostCenterPickReturnVO1 wmsCostCenterPickReturnVO1) {
        return Results.success(wmsCostCenterReturnService.returnLocatorScan(tenantId, wmsCostCenterPickReturnVO1.getLocatorCode(),wmsCostCenterPickReturnVO1.getBarCodeList(),wmsCostCenterPickReturnVO1.getDocLineList()));
    }
    /**
     * @Description 成本中心退料单扫描实物条码
     * @Author wenzhang.yu
     * @Date 2020/4/20 13:57
     * @param tenantId
     * @return org.springframework.http.ResponseEntity<?>
     * @version 1.0
     **/
    @ApiOperation(value = "扫描/输入退料单实物条码")
    @PostMapping(value = {"/material/scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsMaterialReturnScanDTO2> returnMaterialCodeScan(@PathVariable("organizationId") Long tenantId,
                                                                          @RequestBody WmsCostCenterPickReturnVO2 wmsCostCenterPickReturnVO2) {
        return Results.success(wmsCostCenterReturnService.returnMaterialCodeScan(tenantId, wmsCostCenterPickReturnVO2.getBarCode(),wmsCostCenterPickReturnVO2.getInstructionDocId(),wmsCostCenterPickReturnVO2.getLocatorCode(),wmsCostCenterPickReturnVO2.getBarCodeList(),wmsCostCenterPickReturnVO2.getDocLineList()));
    }


    /**
     * @Description 退料确认
     * @Author wenzhang.yu
     * @Date 2020/4/22 16:59
     * @param tenantId
     * @param dto
     * @return io.tarzan.common.domain.sys.ResponseData<java.lang.Void>
     * @version 1.0
     **/
    @ApiOperation(value = "退料确认")
    @PostMapping(value = {"/return/confirm"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsMaterialReturnScanDTO> returnCodeConfirm(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody WmsMaterialReturnConfirmDTO dto) {
        return Results.success(wmsCostCenterReturnService.returnCodeConfirm(tenantId, dto));
    }

    /**
     * @Description 明细页面
     * @Author wenzhang.yu
     * @Date 2020/4/23 9:54
     * @param tenantId
     * @param dto
     * @return io.tarzan.common.domain.sys.ResponseData<java.util.List < com.ruike.wms.api.dto.WmsMaterialReturnDetailDTO2>>
     * @version 1.0
     **/
    @ApiOperation(value = "明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/detail"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<WmsMaterialReturnDetailDTO2>> docDetailQuery(@PathVariable("organizationId") Long tenantId,
                                                                            @RequestBody WmsMaterialReturnDetailDTO dto) {
        return Results.success(wmsCostCenterReturnService.docDetailQuery(tenantId, dto));
    }
}
