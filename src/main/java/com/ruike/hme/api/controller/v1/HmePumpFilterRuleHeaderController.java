package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.entity.HmePumpFilterRuleHeader;
import com.ruike.hme.domain.repository.HmePumpFilterRuleHeaderRepository;
import com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO;
import com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO2;
import com.ruike.hme.domain.vo.HmePumpFilterRuleHeaderVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 泵浦源筛选规则头表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-20 14:28:35
 */
@RestController("hmePumpFilterRuleHeaderController.v1")
@RequestMapping("/v1/{organizationId}/hme-pump-filter-rule-headers")
public class HmePumpFilterRuleHeaderController extends BaseController {

    @Autowired
    private HmePumpFilterRuleHeaderRepository hmePumpFilterRuleHeaderRepository;

    @ApiOperation(value = "泵浦源筛选规则列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmePumpFilterRuleHeaderVO>> queryFilterRuleList(@PathVariable("organizationId") Long tenantId,
                                                                               HmePumpFilterRuleHeaderVO headerVO,
                                                                               @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmePumpFilterRuleHeaderRepository.queryFilterRuleList(tenantId, headerVO, pageRequest));
    }

    @ApiOperation(value = "泵浦源筛选规则-行信息列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/rule-line-list")
    public ResponseEntity<List<HmePumpFilterRuleHeaderVO2>> queryRuleLineList(@PathVariable("organizationId") Long tenantId,
                                                                              String ruleHeadId) {
        return Results.success(hmePumpFilterRuleHeaderRepository.queryRuleLineList(tenantId, ruleHeadId));
    }

    @ApiOperation(value = "泵浦源筛选规则-历史列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/rule-history-list")
    public ResponseEntity<Page<HmePumpFilterRuleHeaderVO3>> queryRuleHistoryList(@PathVariable("organizationId") Long tenantId,
                                                                                 String ruleHeadId,
                                                                                 @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmePumpFilterRuleHeaderRepository.queryRuleHistoryList(tenantId, ruleHeadId, pageRequest));
    }

    @ApiOperation(value = "新增&更新泵浦源筛选规则头")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save-rule-header")
    public ResponseEntity<HmePumpFilterRuleHeader> saveRuleHeaderForUi(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody HmePumpFilterRuleHeader ruleHeader) {
        return Results.success(hmePumpFilterRuleHeaderRepository.saveRuleHeaderForUi(tenantId, ruleHeader));
    }

}
