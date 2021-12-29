package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsOutSourceDTO;
import com.ruike.wms.api.dto.WmsOutSourceSendDTO;
import com.ruike.wms.domain.repository.WmsOutSourceRepository;
import com.ruike.wms.domain.vo.*;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 外协发货控制层
 * @author: han.zhang
 * @create: 2020/06/18 10:43
 */
@RestController("wmsOutSourceController.v1")
@RequestMapping("/v1/{organizationId}/wms-out-source")
@Slf4j
@Api(tags = SwaggerApiConfig.WMS_OUT_SOURCE)
public class WmsOutSourceController extends BaseController {
    @Autowired
    private WmsOutSourceRepository wmsOutSourceRepository;

    /**
     * @Description 外协发货订单查询
     * @param tenantId
     * @param instructionDocNum
     * @return org.springframework.http.ResponseEntity<?>
     * @Date 2020-06-18 11:14
     * @Author han.zhang
     */
    @ApiOperation(value = "外协发货订单查询")
    @GetMapping(value = {"/query-order"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsOutSourceOrderQueryVO>> selectOutSourceOrder(@PathVariable("organizationId") Long tenantId,
                                            String instructionDocNum) {
        log.info("<====WmsOutSourceController-selectOutSourceOrder:{},{}", tenantId, instructionDocNum);
        return Results.success(wmsOutSourceRepository.selectOutSourceOrder(tenantId, instructionDocNum));
    }

    /**
     * @Description 外协发货单扫描
     * @param tenantId
     * @param instructionDocNum
     * @return io.tarzan.common.domain.sys.ResponseData<java.util.List<com.ruike.wms.domain.vo.WmsOutSourceScanVO>>
     * @Date 2020-06-22 09:16
     * @Author han.zhang
     */
    @ApiOperation(value = "外协发货单扫描")
    @GetMapping(value = {"/out-source-order/scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsOutSourceScanVO> scanOutSourceOrder(@PathVariable("organizationId") Long tenantId,
                                                                     String instructionDocNum) {
        log.info("<====WmsOutSourceController-selectOutSourceOrder:{},{}", tenantId, instructionDocNum);
        return Results.success(wmsOutSourceRepository.scanOutSourceOrder(tenantId ,instructionDocNum));
    }

    /**
     * @Description 外协发货扫描物料条码
     * @param tenantId
     * @param materialQueryVO
     * @return io.tarzan.common.domain.sys.ResponseData<com.ruike.wms.domain.vo.WmsOutSourceMaterialReturnVO>
     * @Date 2020-06-22 11:39
     * @Author han.zhang
     */
    @ApiOperation(value = "外协发货扫描物料条码")
    @PostMapping(value = {"/out-source-order/material-lot-scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsOutSourceMaterialReturnVO> materialLotScan(@PathVariable("organizationId") Long tenantId,
                                                                        @RequestBody WmsOutSourceScanMaterialQueryVO materialQueryVO) {
        log.info("<====WmsOutSourceController-materialLotScan:{},{}", tenantId, materialQueryVO);
        return Results.success(wmsOutSourceRepository.materialLotScan(tenantId, materialQueryVO));
    }

    /**
     * @Description 外协发货确认发货
     * @param tenantId
     * @param sendDto
     * @return io.tarzan.common.domain.sys.ResponseData<com.ruike.wms.domain.vo.WmsOutSourceMaterialReturnVO>
     * @Date 2020-06-22 11:43
     * @Author han.zhang
     */
    @ApiOperation(value = "外协发货确认发货")
    @PostMapping(value = {"/out-source-order/send"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsOutSourceSendDTO> send(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody WmsOutSourceSendDTO sendDto) {
        log.info("<====WmsOutSourceController-send:{},{}", tenantId, sendDto);
        return Results.success(wmsOutSourceRepository.send(tenantId, sendDto));
    }

    /**
     * @Description 查询明细
     * @param tenantId
     * @param instructionId
     * @return io.tarzan.common.domain.sys.ResponseData<com.ruike.wms.domain.vo.WmsOutSourceScanVO>
     * @Date 2020-06-24 11:39
     * @Author han.zhang
     */
    @ApiOperation(value = "明细查询")
    @GetMapping(value = {"/out-source-order/detail/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsOsDetailVO>> queryDetail(@PathVariable("organizationId") Long tenantId,
                                                               String instructionId) {
        log.info("<====WmsOutSourceController-queryDetail:{},{}", tenantId, instructionId);
        return Results.success(wmsOutSourceRepository.queryDetail(tenantId, instructionId));
    }


    @ApiOperation(value = "条码撤回")
    @PostMapping(value = {"/out-source-order/return/material-lot"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsOutSourceScanMaterialQueryVO> returnMaterialLot(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody WmsOutSourceScanMaterialQueryVO materialQueryVO) {
        log.info("<====WmsOutSourceController-returnMaterialLot:{},{}", tenantId, materialQueryVO);
        return Results.success(wmsOutSourceRepository.returnMaterialLot(tenantId, materialQueryVO));
    }

    @ApiOperation(value = "条码拆分")
    @PostMapping(value = {"/out-source-order/split/material-lot"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsOutSourceDTO> materialLotSplit(@PathVariable("organizationId") Long tenantId ,
                                                            @RequestBody WmsOutSourceDTO dto){
        return Results.success(wmsOutSourceRepository.materialLotSplit(tenantId, dto));
    }
}