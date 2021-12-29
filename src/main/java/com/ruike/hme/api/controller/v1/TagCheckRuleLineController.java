package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.TagCheckRuleLineService;
import com.ruike.hme.domain.entity.TagCheckRuleLine;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 数据项展示规则维护行表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-08-25 16:23:20
 */
@RestController("tagCheckRuleLineController.v1")
@RequestMapping("/v1/{organizationId}/tag-check-rule-lines")
public class TagCheckRuleLineController extends BaseController {

    private final TagCheckRuleLineService tagCheckRuleLineService;

    public TagCheckRuleLineController(TagCheckRuleLineService tagCheckRuleLineService) {
        this.tagCheckRuleLineService = tagCheckRuleLineService;
    }

    @ApiOperation(value = "修改数据项展示规则维护行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save")
    public ResponseEntity<TagCheckRuleLine> save(@PathVariable("organizationId") Long organizationId, @RequestBody TagCheckRuleLine tagCheckRuleLine) {
        tagCheckRuleLineService.save(organizationId, tagCheckRuleLine);
        return Results.success(tagCheckRuleLine);
    }


}
