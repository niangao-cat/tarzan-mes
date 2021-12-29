package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsMaterialLotService;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import tarzan.modeling.domain.entity.MtModSite;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname Te
 * @Description 条码操作
 * @Date 2019/9/16 13:07
 * @Created by admin
 */

@RestController("wmsPcMaterialLotController.v1")
@RequestMapping("/v1/{organizationId}/pc-material-lot")
@Api(tags = SwaggerApiConfig.WMS_PC_MATERIAL_LOT)
@Slf4j
public class WmsMaterialLotController extends BaseController {

    @Autowired
    private WmsMaterialLotService wmsmaterialLotService;

    /**
     * @Description 条码查询
      * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page<tarzan.inventory.domain.entity.MtMaterialLot>>
     * @Date 2019/9/16 14:32
     * @Created by admin
     */
    @ApiOperation(value = "条码查询")
    @GetMapping(value = {"/barCodeQuery"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsMaterialLotQryResultDTO>> barCodeQuery(@PathVariable("organizationId") Long tenantId,
                                                                       WmsMaterialLotQryDTO dto,
                                                                       @ApiIgnore PageRequest pageRequest) {
        log.info("<====WmsMaterialLotController-barCodeQuery:{},{}", tenantId, dto.toString());
        dto.initParam();
        return Results.success(wmsmaterialLotService.selectBarCodeCondition(pageRequest,dto, tenantId));
    }

    @ApiOperation(value = "导出条码数据")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(WmsMaterialLotQryExportResultDTO.class)
    @GetMapping(value = {"/barCodeQuery-export"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<WmsMaterialLotQryExportResultDTO>> barCodeQueryExport(@PathVariable("organizationId") Long tenantId,
                                                                                     WmsMaterialLotQryDTO dto,
                                                                                     HttpServletResponse response,
                                                                                     ExportParam exportParam){
        dto.initParam();
        return Results.success(wmsmaterialLotService.barCodeQueryExport(tenantId, dto));
    }

    @ApiOperation("默认工厂")
    @GetMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<MtModSite> siteBasicPropertyGet(@PathVariable(value = "organizationId") Long tenantId) {
        return Results.success(wmsmaterialLotService.siteBasicPropertyGet(tenantId));
    }

    @ApiOperation(value = "条码创建")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<String> materialLotCreate(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody WmsMaterialLotAddDTO dto) {
        log.info("<====WmsMaterialLotController-materialLotCreate:{},{}",tenantId,dto.toString());
        return Results.success(wmsmaterialLotService.materialLotCreate(tenantId,dto));
    }

    @ApiOperation(value = "历史条码查询")
    @GetMapping(value = {"/barCodeQueryHis"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsMaterialLotHisResultDTO>> selectBarCodeHis(@PathVariable("organizationId") Long tenantId,
                                                                           WmsMaterialLotHisQryDTO dto,
                                                                           @ApiIgnore PageRequest pageRequest) {
        log.info("<==== WmsMaterialLotController-selectBarCodeHis:{},{}",tenantId,dto.toString());
        return Results.success(wmsmaterialLotService.selectBarCodeHis(pageRequest,dto,tenantId));
    }

    @ApiOperation(value = "导出历史条码数据")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(WmsMaterialLotHisExportResultDTO.class)
    @GetMapping(value = {"/barCodeQueryHis-export"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<WmsMaterialLotHisExportResultDTO>> barCodeHisExport(@PathVariable("organizationId") Long tenantId,
                                                                             WmsMaterialLotHisQryDTO dto,
                                                                             HttpServletResponse response,
                                                                             ExportParam exportParam){
        return Results.success(wmsmaterialLotService.barCodeHisExport(tenantId, dto));
    }

    /**
     * @Description 条码查询
     * @param tenantId
     * @param materialLotList
     * @return io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page<tarzan.inventory.domain.entity.MtMaterialLot>>
     * @Date 2019/9/16 14:32
     * @Created by admin
     */
    @ApiOperation(value = "查询打印数据")
    @PostMapping(value = {"/queryPrintData"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<String> queryPrintData(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody List<WmsMaterialLotQryResultDTO> materialLotList) {
        log.info("<====WmsMaterialLotController-queryPrintData:{},{}",tenantId,materialLotList);
        List<String> materialLotIds = materialLotList.stream().map(WmsMaterialLotQryResultDTO::getMaterialLotId).collect(Collectors.toList());

        return Results.success(wmsmaterialLotService.queryPrintData(tenantId,materialLotIds));
    }

    /**
     * @Description 条码打印
     * @param tenantId
     * @return io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page<tarzan.inventory.domain.entity.MtMaterialLot>>
     * @Date 2019/9/16 14:32
     * @Created by admin
     * @change by junhui.liu
     */
    @ApiOperation(value = "打印")
    @PostMapping(value = {"/print"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<String> print(@PathVariable("organizationId") Long tenantId, @RequestBody List<WmsMaterialLotQryResultDTO> materialLotList) {
        log.info("<====WmsMaterialLotController-print:{},{}",tenantId,materialLotList);
        List<String> materialLotIds = materialLotList.stream().map(WmsMaterialLotQryResultDTO::getMaterialLotId).collect(Collectors.toList());

        return Results.success(wmsmaterialLotService.print(tenantId,materialLotIds));
    }

    @ApiOperation(value = "条码新建")
    @PostMapping(value = {"/new"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<String> materialLotNew(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody WmsMaterialLotAddDTO dto) {
        log.info("<====WmsMaterialLotController-materialLotCreate:{},{}",tenantId,dto.toString());
        return Results.success(wmsmaterialLotService.materialLotNew(tenantId,dto));
    }

}
