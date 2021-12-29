package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.entity.HmePumpFilterRuleLine;
import com.ruike.hme.domain.repository.HmePumpFilterRuleLineRepository;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 泵浦源筛选规则行表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-20 14:28:35
 */
@RestController("hmePumpFilterRuleLineController.v1")
@RequestMapping("/v1/{organizationId}/hme-pump-filter-rule-lines")
public class HmePumpFilterRuleLineController extends BaseController {

    @Autowired
    private HmePumpFilterRuleLineRepository hmePumpFilterRuleLineRepository;

    @ApiOperation(value = "新增&更新泵浦源筛选规则行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save-rule-line")
    public ResponseEntity<HmePumpFilterRuleLine> saveRuleLineForUi(@PathVariable("organizationId") Long tenantId,
                                                                   @RequestBody HmePumpFilterRuleLine ruleLine) {
        return Results.success(hmePumpFilterRuleLineRepository.saveRuleLineForUi(tenantId, ruleLine));
    }
}
