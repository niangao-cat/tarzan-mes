package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsInstructionReturnDTO;
import com.ruike.wms.api.dto.WmsStockTransferDTO;
import com.ruike.wms.api.dto.WmsStockTransferHeadDTO;
import com.ruike.wms.api.dto.WmsStockTransferSaveDTO;
import com.ruike.wms.app.service.WmsStockTransferService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.instruction.domain.entity.MtInstructionDoc;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static tarzan.config.SwaggerApiConfig.WMS_STOCK_TRANSFER;


/**
 * @Description 库存调拨平台功能
 * @Author xuanyu.huang
 * @Date 2:48 下午 2020/4/22
 */
@RestController("wmsStockTransferController.v1")
@RequestMapping("/v1/{organizationId}/wms-stock-transfer")
@Api(tags = WMS_STOCK_TRANSFER)
@Slf4j
public class WmsStockTransferController extends BaseController {


    @Autowired
    private WmsStockTransferService wmsStockTransferService;


    @ApiOperation(value = "库存调拨 数据头查询")
    @GetMapping(value = {"/list/head"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> listStockTransferForUi(@PathVariable("organizationId") Long tenantId,
                                                    WmsStockTransferDTO condition, @ApiIgnore PageRequest pageRequest) {
        condition.initParam();
        return Results.success(wmsStockTransferService.queryHeadData(condition, tenantId, pageRequest));
    }


    @ApiOperation(value = "库存调拨 数据行查询")
    @GetMapping(value = {"/list/line"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> listStockTransferLineForUi(@PathVariable("organizationId") Long tenantId,
                                                        String sourceInstructionId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(wmsStockTransferService.queryLineDetailByHeadId(sourceInstructionId, tenantId, pageRequest));
    }

    @ApiOperation(value = "库存调拨 更新页面行查询")
    @GetMapping(value = {"/list/update/line"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> listStockUpdateTransferLineForUi(@PathVariable("organizationId") Long tenantId,
                                                        String sourceInstructionId) {
        return Results.success(wmsStockTransferService.listStockUpdateTransferLineForUi(sourceInstructionId, tenantId));
    }

    /**
    * @Description: 库存调拨-通过行ID查询条码信息
    * @author: Deng Xu
    * @date 2020/6/8 13:44
    * @param tenantId 租户ID
    * @param instructionId 调拨单行ID
    * @param pageRequest 分页信息
    * @return : org.springframework.http.ResponseEntity<?>
    * @version 1.0
    */
    @ApiOperation(value = "库存调拨-通过行ID查询条码信息")
    @GetMapping(value = {"/list/material/lot"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> listMaterialLotForUi(@PathVariable("organizationId") Long tenantId,
                                                        String instructionId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(wmsStockTransferService.listMaterialLotForUi(tenantId, instructionId,  pageRequest));
    }

    /**
    * @Description: 库存调拨 新建/更新
    * @author: Modified by Deng Xu
    * @date 2020/6/8 15:08
    * @param tenantId 租户ID
    * @param wmsStockTransferSaveDTO 2
    * @return : io.tarzan.common.domain.sys.ResponseData<tarzan.instruction.domain.entity.MtInstructionDoc>
    * @version 1.0
    */
    @ApiOperation(value = "库存调拨 新建/更新")
    @PostMapping(value = {"/create"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<MtInstructionDoc> save(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody WmsStockTransferSaveDTO wmsStockTransferSaveDTO) {
        return Results.success(wmsStockTransferService.save(wmsStockTransferSaveDTO,tenantId));
    }

    /**
    * @Description: 库存调拨 删除调拨单行
    * @author: Deng Xu
    * @date 2020/6/8 15:16
    * @param tenantId 租户ID
    * @param deleteDto 包含需要删除的行DTO集合
    * @return : io.tarzan.common.domain.sys.ResponseData<java.lang.Void>
    * @version 1.0
    */
    @ApiOperation(value = "库存调拨 删除调拨单行")
    @PostMapping(value = {"/delete/line"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> deleteLine(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody WmsStockTransferSaveDTO.LineDTO deleteDto) {
        wmsStockTransferService.deleteLine(tenantId,deleteDto);
        return Results.success();
    }

    @ApiOperation(value = "库存调拨 站点下拉列表")
    @GetMapping(value = {"/list/site/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> getSiteList(@PathVariable("organizationId") Long tenantId) {

        return Results.success(wmsStockTransferService.getSite(tenantId));
    }

    @ApiOperation(value = "库存调拨 仓库下仓库列表")
    @GetMapping(value = {"/list/warehouse/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> getWarehouse(@PathVariable("organizationId") Long tenantId, String siteId) {

        return Results.success(wmsStockTransferService.getWarehouse(tenantId,siteId));
    }

    @ApiOperation(value = "库存调拨 货位下仓库下货位列表")
    @GetMapping(value = {"/list/locator/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> getLocator(@PathVariable("organizationId") Long tenantId, String locatorId) {
        return Results.success(wmsStockTransferService.getLocator(tenantId,locatorId));
    }

    @ApiOperation(value = "库存调拨 单据打印")
    @PostMapping(value = "/print",produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> print(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody  List<WmsStockTransferHeadDTO> wmsStockTransferHeadDtoList) {
        return Results.success(wmsStockTransferService.print(tenantId,wmsStockTransferHeadDtoList));
    }

    @ApiOperation(value = "库存调拨 暂挂")
    @PostMapping(value = "/hold",produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> hold(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody  List<WmsStockTransferHeadDTO> wmsStockTransferHeadDtoList) {
        return Results.success(wmsStockTransferService.hold(tenantId,wmsStockTransferHeadDtoList));
    }

    @ApiOperation(value = "库存调拨 取消暂挂")
    @PostMapping(value = "/hold/cancel",produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> holdCancel(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody  List<WmsStockTransferHeadDTO> wmsStockTransferHeadDtoList) {
        return Results.success(wmsStockTransferService.holdCancel(tenantId,wmsStockTransferHeadDtoList));
    }


    /**
     * 库存调拨平台关闭按钮
     * @param wmsStockTransferHeadDTO
     * @Author 2021-07-15 16:36:23 taowen.wang@hand-china.com
     * @return
     */
    @ApiOperation(value = "库存调拨 关闭按钮")
    @PutMapping(value = "/close/button", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public WmsInstructionReturnDTO putInstruction(@PathVariable("organizationId") Long tenantId, @RequestBody WmsStockTransferHeadDTO wmsStockTransferHeadDTO) {
        WmsInstructionReturnDTO wmsInstructionReturnDTO = wmsStockTransferService.putInstruction(tenantId, wmsStockTransferHeadDTO);
        return wmsInstructionReturnDTO;
    }

    @ApiOperation(value = "库存调拨 取消")
    @PostMapping(value = "/cancel",produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> cancel(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody  List<WmsStockTransferHeadDTO> wmsStockTransferHeadDtoList) {
        return Results.success(wmsStockTransferService.cancel(tenantId,wmsStockTransferHeadDtoList));
    }

    @ApiOperation(value = "库存调拨 审核")
    @PostMapping(value = "/approval",produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> approval(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody  List<WmsStockTransferHeadDTO> wmsStockTransferHeadDtoList) {
        return Results.success(wmsStockTransferService.approval(tenantId,wmsStockTransferHeadDtoList));
    }

    @ApiOperation(value = "调拨单打印")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/instruction/doc/print")
    public ResponseEntity<?> printPdf(@PathVariable("organizationId") Long tenantId,
                                      @RequestBody List<String> instructionDocIdList, HttpServletResponse response){
        wmsStockTransferService.printPdf(tenantId, instructionDocIdList, response);
        return Results.success();
    }
}