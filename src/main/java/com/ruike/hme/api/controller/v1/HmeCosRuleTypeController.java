package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeCosRuleType;
import com.ruike.hme.domain.repository.HmeCosRuleTypeRepository;
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
 * 芯片规则类型 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2020-08-10 16:16:46
 */
@RestController("hmeCosRuleTypeController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-rule-types")
public class HmeCosRuleTypeController extends BaseController {

    @Autowired
    private HmeCosRuleTypeRepository hmeCosRuleTypeRepository;

    @ApiOperation(value = "芯片规则类型列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeCosRuleType>> list(HmeCosRuleType hmeCosRuleType, @ApiIgnore @SortDefault(value = HmeCosRuleType.FIELD_COS_RULE_TYPE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeCosRuleType> list = hmeCosRuleTypeRepository.pageAndSort(pageRequest, hmeCosRuleType);
        return Results.success(list);
    }

    @ApiOperation(value = "芯片规则类型明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{cosRuleTypeId}")
    public ResponseEntity<HmeCosRuleType> detail(@PathVariable Long cosRuleTypeId) {
        HmeCosRuleType hmeCosRuleType = hmeCosRuleTypeRepository.selectByPrimaryKey(cosRuleTypeId);
        return Results.success(hmeCosRuleType);
    }

    @ApiOperation(value = "创建芯片规则类型")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeCosRuleType> create(@RequestBody HmeCosRuleType hmeCosRuleType) {
        validObject(hmeCosRuleType);
        hmeCosRuleTypeRepository.insertSelective(hmeCosRuleType);
        return Results.success(hmeCosRuleType);
    }

    @ApiOperation(value = "修改芯片规则类型")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeCosRuleType> update(@RequestBody HmeCosRuleType hmeCosRuleType) {
        SecurityTokenHelper.validToken(hmeCosRuleType);
        hmeCosRuleTypeRepository.updateByPrimaryKeySelective(hmeCosRuleType);
        return Results.success(hmeCosRuleType);
    }

    @ApiOperation(value = "删除芯片规则类型")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeCosRuleType hmeCosRuleType) {
        SecurityTokenHelper.validToken(hmeCosRuleType);
        hmeCosRuleTypeRepository.deleteByPrimaryKey(hmeCosRuleType);
        return Results.success();
    }

}
