package com.ruike.wms.api.controller.v1;

import com.ruike.wms.app.service.WmsOutsourceManagePlatformService;
import com.ruike.wms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;
import tarzan.inventory.api.dto.MtInvJournalDTO4;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
* @Classname WmsOutsourceManagePlatformController
* @Description 外协管理平台 -API
* @Date  2020/6/11 19:07
* @Created by Deng xu
*/
@RestController("WmsOutsourceManagePlatformController.v1")
@RequestMapping("/v1/{organizationId}/wms-outsource-manage-platform")
@Api(tags = SwaggerApiConfig.WMS_OUTSOURCE_MANAGE_PLATFORM)
@Slf4j
public class WmsOutsourceManagePlatformController extends BaseController {

    @Autowired
    private WmsOutsourceManagePlatformService service;

    /**
    * @Description: 外协管理平台-查询单据头信息
    * @author: Deng Xu
    * @date 2020/6/11 19:57
    * @param tenantId 租户ID
    * @param condition 查询条件
    * @param pageRequest 分页信息
    * @return : io.tarzan.common.domain.sys.ResponseData<com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO>
    * @version 1.0
    */
    @ApiOperation(value = "外协管理平台-查询单据头信息")
    @GetMapping(value = "/list/head/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsOutsourceOrderHeadVO>> listHeadForUi(@PathVariable("organizationId") Long tenantId,
                                                               WmsOutsourceOrderHeadVO condition,@ApiIgnore PageRequest pageRequest ) {
        log.info("<====WmsOutsourceManagePlatformController-listHeadForUi:{}，{}", tenantId, condition);
        return Results.success(service.listHeadForUi(tenantId, condition ,pageRequest));
    }

    /**
    * @Description: 外协管理平台-根据单据头ID查询行信息
    * @author: Deng Xu
    * @date 2020/6/12 14:22
    * @param tenantId 租户ID
    * @param sourceDocId 单据头ID
    * @param pageRequest 分页信息
    * @return : io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderLineVO>>
    * @version 1.0
    */
    @ApiOperation(value = "外协管理平台-根据单据头ID查询行信息")
    @GetMapping(value = "/list/line/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsOutsourceOrderLineVO>> listLineForUi(@PathVariable("organizationId") Long tenantId,
                                                                     String sourceDocId, @ApiIgnore PageRequest pageRequest ) {
        log.info("<====WmsOutsourceManagePlatformController-listLineForUi:{}，{}", tenantId, sourceDocId);
        return Results.success(service.listLineForUi(tenantId, sourceDocId ,pageRequest));
    }

    @ApiOperation(value = "外协管理平台-查询行明细信息")
    @GetMapping(value = "/list/detail/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsOutsourceOrderDetailsVO>> listLineDetailForUi(@PathVariable("organizationId") Long tenantId,
                                                                                String lineId, @ApiIgnore PageRequest pageRequest) {
        log.info("<====WmsOutsourceManagePlatformController-listLineDetailForUi:{}，{}", tenantId, lineId);
        return Results.success(service.listLineDetailForUi(tenantId, lineId, pageRequest));
    }

    /**
    * @Description: 外协管理平台-退货单创建-查询头行信息
    * @author: Deng Xu
    * @date 2020/6/16 11:48
    * @param tenantId 租户ID
    * @param sourceDocId 外协送货单头ID
    * @return : io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO>>
    * @version 1.0
    */
    @ApiOperation(value = "外协管理平台-退货单创建-查询头行信息")
    @GetMapping(value = "/create/return/doc/list/line", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsOutsourceOrderHeadVO> listLineForCreateReturnDoc(@PathVariable("organizationId") Long tenantId,
                                                                     String sourceDocId  ) {
        log.info("<====WmsOutsourceManagePlatformController-listLineForCreateReturnDoc:{}，{}", tenantId, sourceDocId);
        return Results.success(service.listLineForCreateReturnDoc(tenantId, sourceDocId));
    }

    /**
    * @Description: 外协管理平台-退货单创建-查询单号
    * @author: Deng Xu
    * @date 2020/7/2 11:41
    * @param tenantId 租户ID
    * @return : io.tarzan.common.domain.sys.ResponseData<com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO>
    * @version 1.0
    */
    @ApiOperation(value = "外协管理平台-退货单创建-查询单号")
    @GetMapping(value = "/create/return/head/doc", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsOutsourceOrderHeadVO> createHeadDoc(@PathVariable("organizationId") Long tenantId  ) {
        log.info("<====WmsOutsourceManagePlatformController-createHeadDoc:{}，{}", tenantId );
        return Results.success(service.createHeadDoc(tenantId));
    }

    /**
    * @Description: 外协管理平台-退货单创建
    * @author: Deng Xu
    * @date 2020/6/16 14:53
    * @param tenantId 租户ID
    * @param createVo 创建VO
    * @return : io.tarzan.common.domain.sys.ResponseData<com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO>
    * @version 1.0
    */
    @ApiOperation(value = "外协管理平台-退货单创建")
    @PostMapping(value = {"/create/return/doc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsOutsourceOrderHeadVO> createReturnDoc(@PathVariable("organizationId") Long tenantId,
                                                                   @RequestBody WmsOutsourceOrderHeadVO createVo) {
        return Results.success(service.createReturnDoc( tenantId ,createVo));
    }

    @ApiOperation(value = "外协管理平台-补料单创建头查询")
    @GetMapping(value = "/create/header/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsReplenishmentOrderVO> createHeaderQuery(@PathVariable("organizationId") Long tenantId,
                                                                     WmsInstructionVO2 vo2) {
        return Results.success(service.createHeaderQuery(tenantId, vo2.getSourceDocId()));
    }

    @ApiOperation(value = "外协管理平台-创建补料单")
    @PostMapping(value = "/create/replenishment/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsReplenishmentOrderVO> createReplenishment(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody WmsReplenishmentOrderVO wmsReplenishmentOrderVO) {
        return Results.success(service.createReplenishment(tenantId, wmsReplenishmentOrderVO));
    }

    @ApiOperation(value = "外协管理平台-查询库存量")
    @GetMapping(value = "/query/inventory/quantity", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsReplenishmentOrderLineVO> queryInventoryQuantity(@PathVariable("organizationId") Long tenantId,
                                                                              WmsReplenishmentOrderLineVO lineVO) {
        return Results.success(service.queryInventoryQuantity(tenantId, lineVO));
    }

    @ApiOperation(value = "外协管理平台-关闭")
    @PostMapping(value = "/close/return/doc", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsInstructionVO2> closeReturnDoc(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody WmsInstructionVO2 wmsInstructionVO2) {
        return Results.success(service.closeReturnDoc(tenantId, wmsInstructionVO2));
    }

    @ApiOperation(value = "外协管理平台-外协发料单打印")
    @PostMapping(value = {"/pdf"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> outsourceCreatePdf(@PathVariable("organizationId") Long tenantId, @RequestBody List<String> sourceDocIdList, HttpServletResponse response) {
        service.outsourceCreatePdf(tenantId, sourceDocIdList, response);
        return Results.success();
    }

    @ApiOperation(value = "外协管理平台-导出")
    @GetMapping(value = "/inventory-excel-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(WmsOutsourceExportVO.class)
    public ResponseEntity<List<WmsOutsourceExportVO>> inventoryExcelExport(@PathVariable("organizationId") Long tenantId,
                                                                           WmsOutsourceOrderHeadVO dto,
                                                                           HttpServletResponse response,
                                                                           ExportParam exportParam) {
        return Results.success(service.inventoryExcelExport(tenantId, dto));
    }
}
