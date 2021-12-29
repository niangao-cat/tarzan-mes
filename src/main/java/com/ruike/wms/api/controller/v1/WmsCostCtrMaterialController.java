package com.ruike.wms.api.controller.v1;

import com.ruike.itf.api.dto.ItfLightTaskIfaceDTO;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsCostCtrMaterialService;
import com.ruike.wms.domain.vo.WmsCostCtrMaterialVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;

/**
 * 成本中心领料单执行 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-04-15 15:26:34
 */
@RestController("wmsCostCtrMaterialController.v1")
@RequestMapping("/v1/{organizationId}/wms-cost-ctr-materials")
@Api(tags = SwaggerApiConfig.WMS_COST_CTR_MATERIAL)
public class WmsCostCtrMaterialController extends BaseController {

    @Autowired
    private WmsCostCtrMaterialService wmsCostCtrMaterialService;

    @ApiOperation(value = "单据扫码查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/doc", produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsCostCtrMaterialDTO> docQuery(@PathVariable("organizationId") Long tenantId, String docBarCode) {
        return Results.success(wmsCostCtrMaterialService.docQuery(tenantId, docBarCode));
    }

    @ApiOperation(value = "实物条码扫码查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/material-lot", produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsCostCtrMaterialDTO11> containerOrMaterialLotQuery(@PathVariable("organizationId") Long tenantId,
                                                                                    @RequestBody WmsCostCtrMaterialDTO4 dto) {
        return Results.success(wmsCostCtrMaterialService.containerOrMaterialLotQuery(tenantId, dto));
    }

    @ApiOperation(value = "校验实物条码质量状态")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/check-material-lot-quality", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> checkMaterialLotQuality(@PathVariable("organizationId") Long tenantId,
                                                                                    @RequestBody WmsCostCtrMaterialDTO4 dto) {
        return Results.success(wmsCostCtrMaterialService.checkMaterialLotQuality(tenantId, dto));
    }

    @ApiOperation(value = "执行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/execute", produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsCostCtrMaterialDTO> execute(@PathVariable("organizationId") Long tenantId, @RequestBody WmsCostCtrMaterialDTO7 dto) {
        return Results.success(wmsCostCtrMaterialService.execute(tenantId, dto));

    }

    @ApiOperation(value = "明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/detail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsCostCtrMaterialDTO9> docDetailQuery(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody WmsCostCtrMaterialDTO8 dto) {
        return Results.success(wmsCostCtrMaterialService.docDetailQuery(tenantId, dto));
    }

    @ApiOperation(value = "删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsCostCtrMaterialDTO9> deleteMaterialLot(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody WmsCostCtrMaterialDTO8 dto) {
        return Results.success(wmsCostCtrMaterialService.deleteMaterialLot(tenantId, dto));
    }

    @ApiOperation(value = "亮灯/关灯")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/light", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<WmsCostCtrMaterialVO>> lightOnOrOff(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody List<ItfLightTaskIfaceDTO> dtoList) {
        return Results.success(wmsCostCtrMaterialService.lightOnOrOff(tenantId, dtoList));
    }
}
