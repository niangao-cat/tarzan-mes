package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsMaterialLotSplitDTO;
import com.ruike.wms.app.service.WmsMaterialLotSplitService;
import com.ruike.wms.domain.vo.WmsMaterialLotSplitVO;
import com.ruike.wms.domain.vo.WmsMaterialLotSplitVO2;
import com.ruike.wms.domain.vo.WmsMaterialLotSplitVO3;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.List;


/**
 * @description: 条码拆分
 * @author: chaonan.hu@hand-china.com 2020-09-07 15:20:11
 **/
@RestController("wmsMaterialLotSplitController.v1")
@RequestMapping("/v1/{organizationId}/wms-material-lot-split")
@Api(tags = SwaggerApiConfig.WMS_MATERIAL_LOT_SPLIT)
@Slf4j
public class WmsMaterialLotSplitController extends BaseController {

    @Autowired
    private WmsMaterialLotSplitService wmsMaterialLotSplitService;

    @ApiOperation(value = "扫描原始实物条码")
    @GetMapping(value = {"/scan/source/{materialLotCode}"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsMaterialLotSplitVO3> scanSourceBarcode(@PathVariable("organizationId") Long tenantId,
                                                                   @PathVariable("materialLotCode") String  materialLotCode){
        return Results.success(wmsMaterialLotSplitService.scanSourceBarcode(tenantId, materialLotCode));
    }

    @ApiOperation(value = "扫描条码编码")
    @GetMapping(value = {"/scan/{materialLotCode}"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsMaterialLotSplitVO2> scanBarcode(@PathVariable("organizationId") Long tenantId,
                                                              @PathVariable("materialLotCode") String  materialLotCode){
        return Results.success(wmsMaterialLotSplitService.scanBarcode(tenantId, materialLotCode));
    }

    @ApiOperation(value = "拆分")
    @PostMapping(value = {"/split/barcode"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsMaterialLotSplitVO3>> splitBarcode(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody WmsMaterialLotSplitDTO dto){
        return Results.success(wmsMaterialLotSplitService.splitBarcode(tenantId, dto));
    }
}