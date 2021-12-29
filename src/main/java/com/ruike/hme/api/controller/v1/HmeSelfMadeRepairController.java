package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.entity.HmeRepairWorkOrderCreate;
import com.ruike.hme.domain.service.HmeSelfMadeRepairService;
import com.ruike.hme.domain.vo.HmeSelfMadeRepairVO;
import com.ruike.hme.domain.vo.HmeSelfMadeRepairVO2;
import com.ruike.wms.domain.vo.WmsMaterialLotAttrVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 自制件返修 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/20 14:47
 */
@RestController("hmeSelfMadeRepairController.v1")
@RequestMapping("/v1/{organizationId}/hme-self-made-repair")
@Api(tags = "HmeSelfMadeRepair")
public class HmeSelfMadeRepairController extends BaseController {
    private final HmeSelfMadeRepairService service;

    public HmeSelfMadeRepairController(HmeSelfMadeRepairService service) {
        this.service = service;
    }

    @ApiOperation(value = "自制件返修 条码扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<WmsMaterialLotAttrVO> scan(@PathVariable("organizationId") Long tenantId,
                                                     @RequestParam String materialLotCode) {
        WmsMaterialLotAttrVO result = service.scan(tenantId, materialLotCode);
        return Results.success(result);
    }

    @ApiOperation(value = "自制件返修 提交")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeRepairWorkOrderCreate> submit(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody WmsMaterialLotAttrVO materialLot) {
        HmeRepairWorkOrderCreate result = service.submit(tenantId, materialLot);
        return Results.success(result);
    }

    @ApiOperation(value = "自制件返修-扫描原条码")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scan-original-code")
    public ResponseEntity<HmeSelfMadeRepairVO> scanOriginalCode(@PathVariable("organizationId") Long tenantId,
                                                                String materialLotCode) {
        return Results.success(service.scanOriginalCode(tenantId, materialLotCode));
    }

    @ApiOperation(value = "自制件返修-原条码转新条码提交")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/barcode-transform-submit")
    public ResponseEntity<HmeSelfMadeRepairVO2> barcodeTransformSubmit(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody HmeSelfMadeRepairVO hmeSelfMadeRepairVO) {
        return Results.success(service.barcodeTransformSubmit(tenantId, hmeSelfMadeRepairVO));
    }

}
