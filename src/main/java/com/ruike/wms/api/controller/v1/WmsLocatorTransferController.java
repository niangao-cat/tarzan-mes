package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsLocatorScanDTO;
import com.ruike.wms.api.dto.WmsLocatorTransferDTO;
import com.ruike.wms.api.dto.WmsMaterialLotScanDTO;
import com.ruike.wms.app.service.WmsLocatorTransferService;
import com.ruike.wms.domain.vo.WmsLocatorTransferVO;
import com.ruike.wms.domain.vo.WmsLocatorTransferVO2;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 库位转移
 * @author: han.zhang
 * @create: 2020/05/08 10:52
 */
@RestController("wmsLocatorTransferController2.v1")
@RequestMapping("/v1/{organizationId}/locator-transfer")
@Api(tags = SwaggerApiConfig.WMS_LOCATOR_TRANSFER)
public class WmsLocatorTransferController extends BaseController {

    @Autowired
    private WmsLocatorTransferService wmsLocatorTransferService;

    /**
     * @Description 货位扫描
     * @param tenantId
     * @param locatorScanDTO
     * @return io.tarzan.common.domain.sys.ResponseData<tarzan.modeling.domain.entity.MtModLocator>
     * @Date 2020-05-08 11:59
     * @Author han.zhang
     */
    @ApiOperation(value = "货位扫描")
    @GetMapping(value = {"/locator/scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<MtModLocator> locatorScan(@PathVariable("organizationId") Long tenantId,
                                                  WmsLocatorScanDTO locatorScanDTO) {
        return Results.success(wmsLocatorTransferService.locatorScan(tenantId, locatorScanDTO));
    }

    /**
     * @Description 条码扫描
     * @param tenantId
     * @param wmsMaterialLotScanDTO
     * @return io.tarzan.common.domain.sys.ResponseData<java.util.List < com.ruike.wms.domain.vo.WmsLocatorTransferVO>>
     * @Date 2020-05-08 18:23
     * @Author han.zhang
     */
    @ApiOperation(value = "条码扫描")
    @GetMapping(value = {"/material-lot/scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsLocatorTransferVO2> materialLotScan(@PathVariable("organizationId") Long tenantId,
                                                                 WmsMaterialLotScanDTO wmsMaterialLotScanDTO) {
        return Results.success(wmsLocatorTransferService.materialLotScan(tenantId, wmsMaterialLotScanDTO));
    }

    @ApiOperation(value = "转移")
    @PostMapping(value = {"/transfer"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsLocatorTransferDTO> transfer(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody  WmsLocatorTransferDTO wmsLocatorTransferDTO) {
        return Results.success(wmsLocatorTransferService.transfer(tenantId, wmsLocatorTransferDTO));
    }
}