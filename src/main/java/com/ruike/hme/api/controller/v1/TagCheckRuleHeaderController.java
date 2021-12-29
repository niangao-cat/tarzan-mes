package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.TagCheckRuleHeaderDTO;
import com.ruike.hme.app.service.TagCheckRuleHeaderService;
import com.ruike.hme.domain.entity.TagCheckRuleHeader;
import com.ruike.hme.domain.entity.TagCheckRuleLine;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 数据项展示规则维护头表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-08-25 16:23:18
 */
@RestController("tagCheckRuleHeaderController.v1")
@RequestMapping("/v1/{organizationId}/tag-check-rule-headers")
public class TagCheckRuleHeaderController extends BaseController {

    private final TagCheckRuleHeaderService tagCheckRuleHeaderService;


    public TagCheckRuleHeaderController(TagCheckRuleHeaderService tagCheckRuleHeaderService) {
        this.tagCheckRuleHeaderService = tagCheckRuleHeaderService;
    }

    @ApiOperation(value = "数据项展示规则维护头表列表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<Page<TagCheckRuleHeader>> list(@PathVariable("organizationId") Long organizationId,
                                                         TagCheckRuleHeaderDTO checkRuleHeaderDTO,
                                                         @ApiIgnore PageRequest pageRequest) {
        Page<TagCheckRuleHeader> list = tagCheckRuleHeaderService.list(organizationId, checkRuleHeaderDTO, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "根据头表id查询行数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-line-id")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<Page<TagCheckRuleLine>> queryById(@PathVariable("organizationId") Long organizationId,
                                                            String headerId,
                                                            @ApiIgnore PageRequest pageRequest) {
        Page<TagCheckRuleLine> list = tagCheckRuleHeaderService.queryById(organizationId, headerId, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "修改数据项展示规则维护头表保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save")
    public ResponseEntity<TagCheckRuleHeader> save(@PathVariable("organizationId") Long organizationId,
                                                   @RequestBody TagCheckRuleHeader tagCheckRuleHeader) {
        tagCheckRuleHeaderService.save(organizationId, tagCheckRuleHeader);
        return Results.success(tagCheckRuleHeader);
    }

}
