package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.query.QualityAnalyzeQuery;
import com.ruike.hme.api.dto.representation.QualityAnalyzeRepresentation;
import com.ruike.hme.domain.service.HmeQuantityAnalyzeDomainService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 质量文件头表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-01-19 11:10:06
 */
@RestController("hmeQuantityAnalyzeDocController.v1")
@RequestMapping("/v1/{organizationId}/hme-quantity-analyze-docs")
public class HmeQuantityAnalyzeDocController extends BaseController {

    private final HmeQuantityAnalyzeDomainService domainService;

    public HmeQuantityAnalyzeDocController(HmeQuantityAnalyzeDomainService domainService) {
        this.domainService = domainService;
    }

    @ApiOperation(value = "质量文件分析 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<QualityAnalyzeRepresentation> list(@PathVariable("organizationId") Long tenantId,
                                                             QualityAnalyzeQuery dto, PageRequest pageRequest) {
        this.validObject(dto);
        QualityAnalyzeRepresentation result = domainService.page(tenantId, dto, pageRequest);
        return Results.success(result);
    }

}
