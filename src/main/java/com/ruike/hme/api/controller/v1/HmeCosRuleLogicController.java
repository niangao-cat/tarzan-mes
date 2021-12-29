package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeCosRuleLogic;
import com.ruike.hme.domain.repository.HmeCosRuleLogicRepository;
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
 * 芯片规则逻辑 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2020-08-10 16:16:47
 */
@RestController("hmeCosRuleLogicController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-rule-logics")
public class HmeCosRuleLogicController extends BaseController {

    @Autowired
    private HmeCosRuleLogicRepository hmeCosRuleLogicRepository;

    @ApiOperation(value = "芯片规则逻辑列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeCosRuleLogic>> list(HmeCosRuleLogic hmeCosRuleLogic, @ApiIgnore @SortDefault(value = HmeCosRuleLogic.FIELD_COS_RULE_LOGIC_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeCosRuleLogic> list = hmeCosRuleLogicRepository.pageAndSort(pageRequest, hmeCosRuleLogic);
        return Results.success(list);
    }

    @ApiOperation(value = "芯片规则逻辑明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{cosRuleLogicId}")
    public ResponseEntity<HmeCosRuleLogic> detail(@PathVariable Long cosRuleLogicId) {
        HmeCosRuleLogic hmeCosRuleLogic = hmeCosRuleLogicRepository.selectByPrimaryKey(cosRuleLogicId);
        return Results.success(hmeCosRuleLogic);
    }

    @ApiOperation(value = "创建芯片规则逻辑")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeCosRuleLogic> create(@RequestBody HmeCosRuleLogic hmeCosRuleLogic) {
        validObject(hmeCosRuleLogic);
        hmeCosRuleLogicRepository.insertSelective(hmeCosRuleLogic);
        return Results.success(hmeCosRuleLogic);
    }

    @ApiOperation(value = "修改芯片规则逻辑")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeCosRuleLogic> update(@RequestBody HmeCosRuleLogic hmeCosRuleLogic) {
        SecurityTokenHelper.validToken(hmeCosRuleLogic);
        hmeCosRuleLogicRepository.updateByPrimaryKeySelective(hmeCosRuleLogic);
        return Results.success(hmeCosRuleLogic);
    }

    @ApiOperation(value = "删除芯片规则逻辑")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeCosRuleLogic hmeCosRuleLogic) {
        SecurityTokenHelper.validToken(hmeCosRuleLogic);
        hmeCosRuleLogicRepository.deleteByPrimaryKey(hmeCosRuleLogic);
        return Results.success();
    }

}
