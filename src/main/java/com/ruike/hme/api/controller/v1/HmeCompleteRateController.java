package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeCompleteRateService;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeCompleteRate;
import com.ruike.hme.domain.repository.HmeCompleteRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 月度计划表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-07-16 14:36:03
 */
@RestController("hmeCompleteRateController.v1")
@RequestMapping("/v1/{organizationId}/hme-complete-rates")
public class HmeCompleteRateController extends BaseController {

    @Autowired
    private HmeCompleteRateService hmeCompleteRateService;

    @ApiOperation(value = "月度计划表")
    @PostMapping
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> monthlyPlanQuery(@PathVariable("organizationId") Long tenantId) {
        hmeCompleteRateService.monthlyPlanQuery(tenantId);
        return Results.success();
    }

}
