package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.QmsCheckScrapBarCodeDTO;
import com.ruike.qms.api.dto.QmsCheckScrapDocLineDTO2;
import com.ruike.qms.api.dto.QmsCheckScrapParamsDTO;
import com.ruike.qms.app.service.QmsCheckScrapService;
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
@RestController("qmsCheckScrapController.v1")
@RequestMapping("/v1/{organizationId}/check-scrap")
@Api(tags = SwaggerApiConfig.QMS_CHECK_SCRAP)
public class QmsCheckScrapController extends BaseController {

    @Autowired
    private QmsCheckScrapService wmsCheckScrapService;

    @ApiOperation(value = "条码扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/bar-code-query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<QmsCheckScrapBarCodeDTO> barCodeQuery(@PathVariable("organizationId") Long tenantId,
                                                                QmsCheckScrapParamsDTO dto) {
        return Results.success(wmsCheckScrapService.barCodeQuery(tenantId, dto));
    }

    @ApiOperation(value = "提交")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/submit", produces = "application/json;charset=UTF-8")
    public ResponseEntity<QmsCheckScrapDocLineDTO2> scrapSubmit(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody QmsCheckScrapDocLineDTO2 dto) {
        return Results.success(wmsCheckScrapService.scrapSubmit(tenantId, dto));
    }

}
