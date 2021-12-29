package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosChipInputScanBarcodeDTO;
import com.ruike.hme.api.dto.HmeTimeMaterialSplitDTO;
import com.ruike.hme.api.dto.HmeTimeMaterialSplitDTO2;
import com.ruike.hme.api.dto.HmeTimeMaterialSplitDTO3;
import com.ruike.hme.app.service.HmeTimeMaterialSplitService;
import com.ruike.hme.domain.vo.HmeTimeMaterialSplitVO;
import com.ruike.hme.domain.vo.HmeTimeMaterialSplitVO2;
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

import static tarzan.config.SwaggerApiConfig.WMS_CHECKED_WAIT_GROUDING;

/**
 * 时效物料分装 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-09-12 11:17:08
 */
@RestController("hmeTimeMaterialSplitController.v1")
@RequestMapping("/v1/{organizationId}/hme-time-material-split")
@Api(tags = SwaggerApiConfig.HME_TIME_MATERIAL_SPLIT)
public class HmeTimeMaterialSplitController extends BaseController {

    @Autowired
    private HmeTimeMaterialSplitService hmeTimeMaterialSplitService;

    @ApiOperation(value = "扫描条码")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scan/barcode/{materialLotCode}")
    public ResponseEntity<HmeTimeMaterialSplitVO> scanBarcode(@PathVariable("organizationId") Long tenantId,
                                                              @PathVariable("materialLotCode") String materialLotCode) {
        return Results.success(hmeTimeMaterialSplitService.scanBarcode(tenantId, materialLotCode));
    }

    @ApiOperation(value = "原材料剩余时长提交")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/time/submit", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeTimeMaterialSplitVO> timeSubmit(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody HmeTimeMaterialSplitDTO dto){
        return Results.success(hmeTimeMaterialSplitService.timeSubmit(tenantId, dto));
    }

    @ApiOperation(value = "目标条码确认")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/confirm", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeTimeMaterialSplitVO2> confirm(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody HmeTimeMaterialSplitDTO2 dto){
        return Results.success(hmeTimeMaterialSplitService.confirm(tenantId, dto));
    }

}