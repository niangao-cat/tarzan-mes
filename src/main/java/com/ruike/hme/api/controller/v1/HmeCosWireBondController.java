package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosWireBondDTO;
import com.ruike.hme.api.dto.HmeCosWireBondDTO1;
import com.ruike.hme.api.dto.HmeCosWireBondDTO2;
import com.ruike.hme.api.dto.HmeCosWireBondDTO4;
import com.ruike.hme.app.service.HmeCosWireBondService;
import com.ruike.hme.domain.vo.HmeCosWireBondVO1;
import com.ruike.hme.domain.vo.HmeCosWireBondVO2;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * HmeCosWireBondController
 * COS金线打线
 * @author: yifan.xiong@hand-china.com 2020-9-17 19:11:28
 **/
@RestController("HmeCosWireBondController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-wire_bond")
@Api(tags = SwaggerApiConfig.HME_COS_WIRE_BOND)
public class HmeCosWireBondController extends BaseController {

    @Autowired
    private HmeCosWireBondService hmeCosWireBondService;

    @ApiOperation(value = "COS金线打线-未出站条码数据查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/no/site/out/query")
    public ResponseEntity<HmeCosWireBondVO1> noSiteOutDataQuery(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody HmeCosWireBondDTO dto) {
        return Results.success(hmeCosWireBondService.siteOutDateNullQuery(tenantId,dto));
    }

    @ApiOperation(value = "COS金线打线-物料条码扫描进站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/scan/barcode")
    public ResponseEntity<?> scanBarcode(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeCosWireBondDTO dto) {
        hmeCosWireBondService.scanBarcode(tenantId,dto);
        return Results.success();
    }

    @ApiOperation(value = "COS金线打线-物料条码出站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/site/out")
    public ResponseEntity<?> barcodeSiteOut(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody List<HmeCosWireBondDTO1> hmeCosWireBondDTO1List) {
        hmeCosWireBondService.barcodeSiteOut(tenantId,hmeCosWireBondDTO1List);
        return Results.success();
    }

    @ApiOperation(value = "COS投料-出站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/feeding/out")
    public ResponseEntity<?> feedingSiteOut(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody List<HmeCosWireBondDTO1> hmeCosWireBondDTO1List) {
        hmeCosWireBondService.feedingSiteOut(tenantId,hmeCosWireBondDTO1List);
        return Results.success();
    }

    @ApiOperation(value = "COS金线打线-投料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/feeding")
    public ResponseEntity<?> barcodeFeeding(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody HmeCosWireBondDTO2 hmeCosWireBondDTO2) {
        hmeCosWireBondService.barcodeFeeding(tenantId,hmeCosWireBondDTO2);
        return Results.success();
    }

    @ApiOperation(value = "工位绑定信息查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/binding/material/query")
    public ResponseEntity<List<HmeCosWireBondVO2>> bandingMaterialQuery(@PathVariable("organizationId") Long tenantId,
                                                                        String workcellId, String jobId, Double qty,String materialLotId) {
        return Results.success(hmeCosWireBondService.bandingMaterialQuery(tenantId, workcellId,jobId,qty,materialLotId));
    }

    @ApiOperation(value = "COS金线打线-批量删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch/delete")
    public ResponseEntity<HmeCosWireBondDTO4> batchDelete(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody HmeCosWireBondDTO4 dto){
        return Results.success(hmeCosWireBondService.batchDelete(tenantId, dto));
    }
}
