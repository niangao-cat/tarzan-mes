package com.ruike.itf.api.controller.v1;

import com.ruike.itf.domain.service.QualityAnalyzeIfaceDomainService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 质量文件解析接口头 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2021-04-06 10:04:45
 */
@RestController("itfQualityAnalyzeIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-quality-analyze-ifaces")
public class ItfQualityAnalyzeIfaceController extends BaseController {

    private final QualityAnalyzeIfaceDomainService domainService;

    public ItfQualityAnalyzeIfaceController(QualityAnalyzeIfaceDomainService domainService) {
        this.domainService = domainService;
    }


    @ApiOperation(value = "质量文件解析接口 调用")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<Void> invoke(@PathVariable("organizationId") Long tenantId) {
        domainService.invoke(tenantId);
        return Results.success();
    }
}
