package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsQrCodeAnalysisDTO;
import com.ruike.wms.api.dto.WmsQrCodeAnalysisDTO2;
import com.ruike.wms.app.service.WmsQrCodeAnalysisService;
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

/**
 * 二维码解析 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-8-24 12:29:57
 */
@RestController("wmsQrCodeAnalysisController.v1")
@RequestMapping("/v1/{organizationId}/qr-code")
@Api(tags = SwaggerApiConfig.WMS_QR_CODE_ANALYSIS)
public class WmsQrCodeAnalysisController extends BaseController {

    @Autowired
    private WmsQrCodeAnalysisService wmsQrCodeAnalysisService;

    @ApiOperation(value = "二维码解析")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/analysis")
    public ResponseEntity<WmsQrCodeAnalysisDTO> qrCodeAnalysis(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody WmsQrCodeAnalysisDTO2 dto) {
        return Results.success(wmsQrCodeAnalysisService.qrCodeAnalysis(tenantId, dto));
    }

}
