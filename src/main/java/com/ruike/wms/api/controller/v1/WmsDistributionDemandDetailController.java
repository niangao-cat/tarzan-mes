package com.ruike.wms.api.controller.v1;

import static tarzan.config.SwaggerApiConfig.WMS_DISTRIBUTION_DEMAND_DETAIL;

import com.ruike.wms.app.service.WmsDistributionDemandDetailService;
import com.ruike.wms.domain.entity.WmsDistributionDemandDetail;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配送平台明细 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2020-08-31 11:09:51
 */
@RestController("WmsDistributionDemandDetailController.v1")
@RequestMapping("/v1/{organizationId}/wms-distribution-demand-detail")
@Api(tags = WMS_DISTRIBUTION_DEMAND_DETAIL)
public class WmsDistributionDemandDetailController extends BaseController {
    private final WmsDistributionDemandDetailService service;

    public WmsDistributionDemandDetailController(WmsDistributionDemandDetailService service) {
        this.service = service;
    }

    @ApiOperation(value = "配送需求明细列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<List<WmsDistributionDemandDetail>> list(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestParam("distDemandId") String distDemandId) {
        List<WmsDistributionDemandDetail> list = service.selectListByDemandId(tenantId, distDemandId);
        return Results.success(list);
    }

    @ApiOperation(value = "配送需求替代明细保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> insertOrUpdateSubstitution(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody WmsDistributionDemandDetail detail) {
        service.insertOrUpdateSubstitution(tenantId, detail);
        return Results.success();
    }

    @ApiOperation(value = "配送需求替代明细删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> removeSubstitution(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody WmsDistributionDemandDetail detail) {
        service.removeSubstitution(tenantId, detail);
        return Results.success();
    }

}
