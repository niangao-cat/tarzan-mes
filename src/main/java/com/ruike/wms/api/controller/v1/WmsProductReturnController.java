package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsProductReturnService;
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
import static tarzan.config.SwaggerApiConfig.WMS_PRODUCT_RETURN;

/**
 * 成品退货执行Api
 *
 * @author li.zhang 2021/07/07 14:34
 */
@RestController("WmsProductReturnController.v1")
@RequestMapping("/v1/{organizationId}/wms-product-return")
@Api(tags = WMS_PRODUCT_RETURN)
public class WmsProductReturnController extends BaseController {

    @Autowired
    private WmsProductReturnService wmsProductReturnService;

    /**
     * 成品退货单扫描
     *
     * @param tenantId          租户
     * @param instructionDocNum 退货号
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 15:00:00
     */
    @ApiOperation(value = "成品退货单扫描")
    @GetMapping(value = {"/doc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsProductReturnVO> docScan(@PathVariable("organizationId") Long tenantId,
                                                      @RequestParam String instructionDocNum) {
        return Results.success(wmsProductReturnService.docScan(tenantId, instructionDocNum));
    }

    /**
     * 物料批扫描
     *
     * @param tenantId          租户
     * @param wmsMaterialDocReceviceDto
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 15:00:00
     */
    @ApiOperation(value = "物料批扫描")
    @PostMapping(value = {"/material/doc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsMaterialDocReturnVO> materialDocScan(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody WmsMaterialDocReceviceDto wmsMaterialDocReceviceDto) {
        return Results.success(wmsProductReturnService.materialDocScan(tenantId, wmsMaterialDocReceviceDto.getMaterialLotCode(),wmsMaterialDocReceviceDto.getInstructionDocId(),wmsMaterialDocReceviceDto.getInstructionList(),wmsMaterialDocReceviceDto.getInstructionselectedList(),wmsMaterialDocReceviceDto.getLocatorId(),wmsMaterialDocReceviceDto.getWmsMaterialDocReturnVO()));
    }

    /**
     * 数量更改
     *
     * @param tenantId          租户
     * @param wmsProductQtyChangeReceviceDto
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 15:00:00
     */
    @ApiOperation(value = "数量更改")
    @PostMapping(value = {"/qty/change"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsQtyChangeVO> qtyChange(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody WmsProductQtyChangeReceviceDto wmsProductQtyChangeReceviceDto) {
        return Results.success(wmsProductReturnService.qtyChange(tenantId,wmsProductQtyChangeReceviceDto.getChangeQty(),wmsProductQtyChangeReceviceDto.getWmsMaterialDocReturnVO()));
    }

    /**
     * 货位扫描
     *
     * @param tenantId          租户
     * @param wmsLocatorDocDTO
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 15:00:00
     */
    @ApiOperation(value = "货位扫描")
    @PostMapping(value = {"/locator/doc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsLocatorDocReturnVO> locatorDocScan(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody WmsLocatorDocDTO wmsLocatorDocDTO) {
        return Results.success(wmsProductReturnService.locatorDocScan(tenantId, wmsLocatorDocDTO.getLocatorCode(),wmsLocatorDocDTO.getWmsMaterialDocReturnVO()));
    }

    /**
     * 明细
     *
     * @param tenantId          租户
     * @param wmsDetailReceviceDto
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 15:00:00
     */
    @ApiOperation(value = "明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/detail"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsMaterialDocReturnVO2> docDetailQuery(@PathVariable("organizationId") Long tenantId,
                                                                            @RequestBody WmsDetailReceviceDto wmsDetailReceviceDto) {
        return Results.success(wmsProductReturnService.docDetailQuery(tenantId, wmsDetailReceviceDto.getWmsProductReturnVO2()));
    }

    /**
     * 明细删除
     *
     * @param tenantId          租户
     * @param wmsMaterialDocReturnVO2
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 15:00:00
     */
    @ApiOperation(value = "明细删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/detail/delete"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsMaterialDeleteReturnVO2> docDetailDelete(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody WmsMaterialDocReturnVO2 wmsMaterialDocReturnVO2) {
        return Results.success(wmsProductReturnService.docDetailDelete(tenantId, wmsMaterialDocReturnVO2));
    }

    /**
     * 执行
     *
     * @param tenantId          租户
     * @param wmsProductReturnVO
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 15:00:00
     */
    @ApiOperation(value = "执行")
    @PostMapping(value = {"/execute"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsProductExecuteVO> execute(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody WmsProductReturnVO wmsProductReturnVO) {
        return Results.success(wmsProductReturnService.execute(tenantId, wmsProductReturnVO));
    }

    /**
     * 退出时判断
     *
     * @param tenantId          租户
     * @param wmsProductReturnVO
     * @author li.zhang13@hand-china.com 2021/07/20
     */
    @ApiOperation(value = "退出时判断")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/exit/judge"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsProductExitVO> exitJudge(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody WmsProductReturnVO wmsProductReturnVO) {
        return Results.success(wmsProductReturnService.exitJudge(tenantId, wmsProductReturnVO));
    }

    /**
     * 退出时删除不完整的明细
     *
     * @param tenantId          租户
     * @param wmsProductReturnVO
     * @author li.zhang13@hand-china.com 2021/07/20
     */
    @ApiOperation(value = "退出时删除不完整的明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/exit/delete"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsMaterialDeleteReturnVO2> exitDelete(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody WmsProductReturnVO wmsProductReturnVO) {
        wmsProductReturnService.exitDelete(tenantId, wmsProductReturnVO);
        return Results.success();
    }
}
