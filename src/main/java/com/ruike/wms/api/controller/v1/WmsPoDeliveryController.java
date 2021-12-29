package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.api.dto.WmsInstructionDTO;
import com.ruike.wms.api.dto.WmsInstructionDetailResponseDTO;
import com.ruike.wms.api.dto.WmsInstructionDocRequestDTO;
import com.ruike.wms.api.dto.WmsInstructionDocResponseDTO;
import com.ruike.wms.app.service.WmsDeliveryDocService;
import com.ruike.wms.app.service.WmsPoDeliveryRelService;
import com.ruike.wms.domain.repository.WmsPoDeliveryRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import io.tarzan.common.domain.sys.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 送货单行与采购订单行关系表 管理 API
 *
 * @author han.zhang03@hand-china.com 2020-03-27 18:46:38
 */
@RestController("deliveryPoRelController.v1")
@RequestMapping("/v1/{organizationId}/wms-po-delivery")
@Api(tags = SwaggerApiConfig.WMS_PO_DELIVERY)
@Slf4j
public class WmsPoDeliveryController extends BaseController {

    @Autowired
    private WmsPoDeliveryRelService wmsPoDeliveryRelService;
    @Autowired
    private WmsPoDeliveryRepository wmsPoDeliveryRepository;
    @Autowired
    private WmsDeliveryDocService wmsDeliveryDocService;

    @ApiOperation(value = "创建送货单")
    @PostMapping(value = {"/gen-po-delivery-data"},produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<MtInstructionDocVO3> createOrder(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody WmsPoDeliveryDTO dto) {
        return Results.success(wmsPoDeliveryRelService.createOrder(tenantId, dto));
    }

    /**
     * @Description 生成送货单号并将采购订单行合并
     * @param tenantId 租户id
     * @return org.springframework.http.ResponseEntity<?>
     * @Date 2020-03-30 15:14
     * @Author han.zhang
     */
    @ApiOperation(value = "生成单号，如果传了采购订单行还能处理合并行数据")
    @PostMapping(value = {"/gen-po-delivery-number"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> generatePoDeliveryNum(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody WmsDeliveryNumberCreateDTO createDTO) {
        return Results.success(wmsPoDeliveryRelService.createOrderNumber(tenantId,createDTO));
    }

    @ApiOperation(value = "送货单查询")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsPoDeliveryVO3>> propertyLimitPoDeliveryQuery(@PathVariable("organizationId") Long tenantId,
                                                                               @RequestBody WmsPoDeliveryVO dto) {

        return Results.success(wmsPoDeliveryRepository.propertyLimitPoDeliveryQuery(tenantId, dto));
    }

    /**
     * @Description 送货单扫描类
     * @param tenantId 租户id
     * @param dto 参数类
     * @return org.springframework.http.ResponseEntity<?>
     * @Date 2020-04-07 10:52
     * @Author han.zhang
     */
    @ApiOperation(value = "送货单扫描")
    @GetMapping(value = {"/scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPoDeliveryScanReturnDTO> poDeliveryScan(@PathVariable("organizationId") Long tenantId,
                                                                   WmsPoDeliveryScanDTO dto) {

        return Results.success(wmsPoDeliveryRelService.poDeliveryScan(tenantId,dto));
    }

    /**
     * @Description 条码创建
     * @param tenantId 租户id
     * @param dto 创建参数
     * @return org.springframework.http.ResponseEntity<?>
     * @Date 2020-04-08 21:12
     * @Author han.zhang
     */
    @ApiOperation(value = "条码创建")
    @PostMapping(value = {"/create/barcode"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> createBarcode(@PathVariable("organizationId") Long tenantId,
                                           @RequestBody WmsCreateBarcodeDTO dto) {
        return Results.success(wmsPoDeliveryRelService.createBarcode(tenantId, dto));
    }


    @ApiOperation(value = "取消送货单")
    @GetMapping(value = {"/cancel/{instructionDocId}"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> createBarcode(@PathVariable("organizationId") Long tenantId,
                                           @PathVariable("instructionDocId") String instructionDocId) {
        wmsPoDeliveryRelService.cancelPoDelivery(tenantId, instructionDocId);
        return Results.success();
    }

    /**
     * @Description 实物条码扫描
     * @Author wenzhang.yu
     * @Date 2020/4/10 13:44
     * @param tenantId
     * @param dto
     * @return org.springframework.http.ResponseEntity<?>
     * @version 1.0
     **/
    @ApiOperation(value = "实物条码扫描")
    @GetMapping(value = {"/material/scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> materialLotCodeScan(@PathVariable("organizationId") Long tenantId,
                                                 WmsPoDeliveryScanDTO2 dto) {
        return Results.success(wmsPoDeliveryRelService.materialLotCodeScan(tenantId,dto));
    }


    /**
     * @Description 校验是否全部接收
     * @Author wenzhang.yu
     * @Date 2020/4/14 9:40
     * @param tenantId
     * @param instructionDocNum
     * @return org.springframework.http.ResponseEntity<?>
     * @version 1.0
     **/
    @ApiOperation(value = "校验是否全部接收")
    @GetMapping(value = {"/iscompleted/{instructionDocNum}"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPoDeliveryVO5> iscompleted(@PathVariable("organizationId") Long tenantId,
                                                        @PathVariable("instructionDocNum") String  instructionDocNum) {
        return Results.success(wmsPoDeliveryRelService.iscompleted(tenantId,instructionDocNum));
    }
    /**
     * @Description 过账：点击确认接收按钮
     * @Author wenzhang.yu
     * @Date 2020/4/14 9:40
     * @param tenantId
     * @param dto
     * @return org.springframework.http.ResponseEntity<?>
     * @version 1.0
     **/
    @ApiOperation(value = "过账")
    @GetMapping(value = {"/confirm/acceptance"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPoDeliveryConfirmDTO> confirmAcceptance(@PathVariable("organizationId") Long tenantId,
                                                WmsPoDeliveryConfirmDTO dto) {
        return Results.success(wmsPoDeliveryRelService.confirmAcceptance(tenantId,dto));
    }

    /**
     * @Description 明细页面查询
     * @Author wenzhang.yu
     * @Date 2020/4/14 15:55
     * @param tenantId
     * @param instructionId
     * @return org.springframework.http.ResponseEntity<?>
     * @version 1.0
     **/
    @ApiOperation(value = "明細")
    @GetMapping(value = {"/detail/{instructionId}"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> queryDetail(@PathVariable("organizationId") Long tenantId,
                                         @PathVariable("instructionId") String  instructionId) {
        return Results.success(wmsPoDeliveryRelService.queryDetail(tenantId,instructionId));
    }



    /**
     * @Description 明細页面删除
     * @Author wenzhang.yu
     * @Date 2020/4/14 18:06
     * @param tenantId
     * @param dtos
     * @return org.springframework.http.ResponseEntity<?>
     * @version 1.0
     **/
    @ApiOperation(value = "明細页面删除")
    @PostMapping(value = {"/detail/delete"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPoDeliveryScanLineReturnDTO> deleteDetail(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody List<WmsPoDeliveryDetailDTO> dtos) {

        return Results.success(wmsPoDeliveryRelService.deleteDetail(tenantId, dtos));
    }

    /** @param tenantId
     * @param dto
     * @return io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page < com.ruike.wms.api.controller.dto.InstructionDocRequestDTO>>
     * @Description 查询指令单据列表
     * @Date 2019/9/19 20:03
     * @Created by han.zhang
     */
    @ApiOperation(value = "查询指令单据列表")
    @GetMapping(value = "/instruction/doc/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    public ResponseEntity<Page<WmsInstructionDocResponseDTO>> instructionDocQuery(@PathVariable("organizationId") Long tenantId,
                                                                                WmsInstructionDocRequestDTO dto,
                                                                                PageRequest pageRequest) {
        return Results.success(wmsDeliveryDocService.instructionDocQuery(tenantId, dto, pageRequest));
    }

    /**
     * @Description 查询指令列表
     * @param tenantId
     * @param instructionDocId
     * @param pageRequest
     * @return io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page<com.ruike.wms.api.controller.dto.InstructionDTO>>
     * @Date 2019/9/22 17:43
     * @Created by han.zhang
     */
    @ApiOperation(value = "查询指令列表")
    @GetMapping(value = "/instruction/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    public ResponseEntity<Page<WmsInstructionDTO>> instructionQuery(@PathVariable("organizationId") Long tenantId,
                                                                  String instructionDocId,
                                                                  PageRequest pageRequest) {
        return Results.success(wmsDeliveryDocService.instructionQuery(tenantId, instructionDocId, pageRequest));
    }

    /**
     * @Description 送货单行（指令）明细查询
     * @param tenantId
     * @param DTO
     * @param pageRequest
     * @return io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page<com.ruike.wms.api.controller.dto.InstructionDetailResponseDTO>>
     * @Date 2019/9/22 17:44
     * @Created by han.zhang
     */
    @ApiOperation(value = "送货单行（指令）明细查询")
    @GetMapping(value = "/instruction/detail/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    public ResponseEntity<Page<WmsInstructionDetailResponseDTO>> instructionDetailQuery(@PathVariable("organizationId") Long tenantId,
                                                                                      WmsInstructionDetailRequestDTO DTO,
                                                                                      PageRequest pageRequest) {
        return Results.success(wmsDeliveryDocService.instructionDetailQuery(tenantId, DTO, pageRequest));
    }

    @ApiOperation(value = "获得可制单数量")
    @PostMapping(value = "/get-avail-quantity", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    public ResponseEntity<List<WmsAvailQuantityReturnDTO>> getAvailQuantity(@PathVariable("organizationId") Long tenantId,
                                                                          @RequestBody  WmsAvailQuantityGetDTO wmsAvailQuantityGetDTO) {
        return Results.success(wmsDeliveryDocService.getAvailQuantity(tenantId, wmsAvailQuantityGetDTO));
    }

    @PostMapping("/pdf")
    @ApiOperation("送货单PDF打印")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> deliveryPrintPdf(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody List<String> instructionDocIds,
                                              HttpServletResponse response) {
        log.info("<==== WmsPoDeliveryController-deliveryPrintPdf info:{},{},{}", tenantId, instructionDocIds);

       /* ResponseData<Map<String, List<String>>> responseData = new ResponseData<>();
        responseData.setSuccess(false);
        if(instructionDocIds != null) {
            Map<String, List<String>> urlMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            if(instructionDocIds.size() > 0) {
                try {
                    List<String> urls = wmsDeliveryDocService.multiplePrint(tenantId, instructionDocIds);
                    responseData.setSuccess(true);
                    urlMap.put("url", urls);
                    responseData.setRows(urlMap);
                } catch (Exception ex) {
                    log.error("<==== WmsPoDeliveryController-deliveryPrintPdf error: {}:{}", ex.getMessage(), ex);
                    responseData.setSuccess(false);
                    responseData.setMessage(ex.getMessage());
                }
            }
        }*/
        wmsDeliveryDocService.multiplePrint(tenantId, instructionDocIds,response);
        return Results.success();
    }

    @ApiOperation(value = "查询明细")
    @GetMapping(value = "/detail/query/{instructionId}", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    public ResponseEntity<Page<WmsDeliveryDocVO>> detailQuery(@PathVariable("organizationId") Long tenantId,
                                                              @PathVariable("instructionId") String instructionId,
                                                              PageRequest pageRequest){
        return Results.success(wmsDeliveryDocService.detailQuery(tenantId, instructionId, pageRequest));
    }

    @ApiOperation(value = "查询送货单条码信息")
    @GetMapping(value = "/instruction/material/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsMaterialLotLineVO>> instructionMaterialLotQuery(@PathVariable("organizationId") Long tenantId,
                                                                                  String  docLineId,
                                                                                  PageRequest pageRequest){
        return Results.success(wmsDeliveryDocService.instructionMaterialLotQuery(tenantId, docLineId, pageRequest));
    }
}
