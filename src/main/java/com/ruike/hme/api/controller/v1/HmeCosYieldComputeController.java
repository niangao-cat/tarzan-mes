package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeCosYieldComputeService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * COS良率计算定时任务 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-09-17 11:35:12
 */
@RestController("hmeCosYieldComputeController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-yield-computes")
public class HmeCosYieldComputeController extends BaseController {

    @Autowired
    private HmeCosYieldComputeService hmeCosYieldComputeService;

    @ApiOperation(value = "COS良率计算定时任务")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value="/job")
    public ResponseEntity<?> cosYieldComputeJob(@PathVariable(value = "organizationId") Long tenantId) {
        hmeCosYieldComputeService.cosYieldComputeJob(tenantId);
        return Results.success();
    }
}
