package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsProductionReturnService;
import com.ruike.wms.domain.vo.*;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static tarzan.config.SwaggerApiConfig.WMS_PRODUCTION_RETURN;

/**
 * 生产退料执行
 *
 * @author li.zhang 2021/07/13 9:46
 */
@RestController("WmsProductionReturnController.v1")
@RequestMapping("/v1/{organizationId}/wms-production-return")
@Api(tags = WMS_PRODUCTION_RETURN)
public class WmsProductionReturnController extends BaseController {

    @Autowired
    private WmsProductionReturnService wmsProductionReturnService;

    /**
     * 生产退料单扫描
     *
     * @param tenantId          租户
     * @param instructionDocNum 退货号
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/13
     */
    @ApiOperation(value = "生产退料单扫描")
    @GetMapping(value = {"/doc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsProductionReturnVO> docScan(@PathVariable("organizationId") Long tenantId,
                                                         @RequestParam String instructionDocNum,
                                                         String instructionDocId) {
        return Results.success(wmsProductionReturnService.docScan(tenantId, instructionDocNum,instructionDocId));
    }

    /**
     * 物料批条码扫描
     *
     * @param tenantId          租户
     * @param wmsProductionMaterialReceviceDto
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionMaterialReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/13
     */
    @ApiOperation(value = "物料批条码扫描")
    @PostMapping(value = {"/material/doc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsProductionMaterialReturnVO> materialDocScan(@PathVariable("organizationId") Long tenantId,
                                                                         @RequestBody WmsProductionMaterialReceviceDto wmsProductionMaterialReceviceDto) {
        return Results.success(wmsProductionReturnService.materialDocScan(tenantId, wmsProductionMaterialReceviceDto.getMaterialLotCode(),wmsProductionMaterialReceviceDto.getInstructionDocId(),wmsProductionMaterialReceviceDto.getInstructionList(),wmsProductionMaterialReceviceDto.getWmsProductionMaterialReturnVO(),wmsProductionMaterialReceviceDto.getLocatorId()));
    }

    /**
     * 数量更改
     *
     * @param tenantId          租户
     * @param wmsProductionQtyChangeReceviceDto
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionQtyChangeVO>
     * @author li.zhang13@hand-china.com 2021/07/13
     */
    @ApiOperation(value = "数量更改")
    @PostMapping(value = {"/qty/change"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsProductionQtyChangeVO> qtyChange(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody WmsProductionQtyChangeReceviceDto wmsProductionQtyChangeReceviceDto) {
        return Results.success(wmsProductionReturnService.qtyChange(tenantId,wmsProductionQtyChangeReceviceDto.getChangeQty(),wmsProductionQtyChangeReceviceDto.getWmsProductionMaterialReturnVO()));
    }

    /**
     * 货位扫描输入
     *
     * @param tenantId          租户
     * @param wmsLocatorDocReceviceDto
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionLocatorDocReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/13
     */
    @ApiOperation(value = "货位扫描")
    @PostMapping(value = {"/locator/doc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsProductionLocatorDocReturnVO> locatorDocScan(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody WmsLocatorDocReceviceDto wmsLocatorDocReceviceDto) {
        return Results.success(wmsProductionReturnService.locatorDocScan(tenantId, wmsLocatorDocReceviceDto.getLocatorCode(),wmsLocatorDocReceviceDto.getWmsProductionMaterialReturnVO()));
    }

    /**
     * 明细
     * @param tenantId          租户
     * @param wmsProductionDetailDTO
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionDetailVO>
     * @author li.zhang13@hand-china.com 2021/07/13
     */
    @ApiOperation(value = "明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/detail"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsProductionDetailVO> docDetailQuery(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody WmsProductionDetailDTO wmsProductionDetailDTO) {
        return Results.success(wmsProductionReturnService.docDetailQuery(tenantId,wmsProductionDetailDTO.getWmsProductionReturnInstructionVO()));
    }

    /**
     * 明细删除
     *
     * @param tenantId          租户
     * @param wmsProductionDetailDeleteDTO
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionDetailDeleteVO>
     * @author li.zhang13@hand-china.com 2021/07/14
     */
    @ApiOperation(value = "明细删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/detail/delete"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsProductionDetailDeleteVO> docDetailDelete(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody WmsProductionDetailDeleteDTO wmsProductionDetailDeleteDTO) {
        return Results.success(wmsProductionReturnService.docDetailDelete(tenantId, wmsProductionDetailDeleteDTO.getWmsProductionReturnInstructionVO(),wmsProductionDetailDeleteDTO.getWmsProductionReturnInstructionDetailVOList()));
    }

    /**
     * 执行
     *
     * @param tenantId          租户
     * @param wmsProductionReturnVO 整个单据信息
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/14
     */
    @ApiOperation(value = "执行")
    @PostMapping(value = {"/execute"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsProductionReturnVO> execute(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody WmsProductionReturnVO wmsProductionReturnVO) {
        wmsProductionReturnService.execute(tenantId, wmsProductionReturnVO);
        return Results.success();
    }

    /**
     * 退出时判断
     *
     * @param tenantId          租户
     * @param wmsProductionReturnVO
     * @author li.zhang13@hand-china.com 2021/08/04
     */
    @ApiOperation(value = "退出时判断")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/exit/judge"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsProductExitVO> exitJudge(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody WmsProductionReturnVO wmsProductionReturnVO) {
        return Results.success(wmsProductionReturnService.exitJudge(tenantId, wmsProductionReturnVO));
    }

    /**
     * 退出时删除不完整的明细
     *
     * @param tenantId          租户
     * @param wmsProductionReturnVO
     * @author li.zhang13@hand-china.com 2021/08/04
     */
    @ApiOperation(value = "退出时删除不完整的明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/exit/delete"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsMaterialDeleteReturnVO2> exitDelete(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody WmsProductionReturnVO wmsProductionReturnVO) {
        wmsProductionReturnService.exitDelete(tenantId, wmsProductionReturnVO);
        return Results.success();
    }
}
