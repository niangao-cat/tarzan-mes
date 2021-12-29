package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.QmsTransitionRuleDTO;
import com.ruike.qms.api.dto.QmsTransitionRuleDTO2;
import com.ruike.qms.app.service.QmsTransitionRuleService;
import com.ruike.qms.domain.entity.QmsTransitionRule;
import com.ruike.qms.domain.repository.QmsTransitionRuleRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

import static tarzan.config.SwaggerApiConfig.QMS_TRANSITION_RULE;

/**
 * 检验水平转移规则表 管理 API
 *
 * @author tong.li05@hand-china.com 2020-05-11 09:54:52
 */
@RestController("qmsTransitionRuleController.v1")
@RequestMapping("/v1/{organizationId}/qms-transition-rules")
@Api(tags = QMS_TRANSITION_RULE)
@Slf4j
public class QmsTransitionRuleController extends BaseController {

    @Autowired
    private QmsTransitionRuleRepository qmsTransitionRuleRepository;

    @Autowired
    private QmsTransitionRuleService qmsTransitionRuleService;

    @ApiOperation(value = "检验水平转移  查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/query"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<QmsTransitionRuleDTO>> query(QmsTransitionRule qmsTransitionRule, @ApiIgnore PageRequest pageRequest, @PathVariable("organizationId") Long tenantId) {
        Page<QmsTransitionRuleDTO> list = qmsTransitionRuleService.query(pageRequest,qmsTransitionRule,tenantId);
        return Results.success(list);
    }

    @ApiOperation(value = "检验水平转移  保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/createOrUpdate"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<QmsTransitionRule> createOrUpdate(@PathVariable("organizationId") Long tenantId, @RequestBody QmsTransitionRule qmsTransitionRule) {
        log.info("<====QmsTransitionRuleController-createOrUpdate:{},{} ",tenantId, qmsTransitionRule );
        validObject(qmsTransitionRule);
        QmsTransitionRule dto = qmsTransitionRuleService.createOrUpdate(tenantId,qmsTransitionRule);
        return Results.success(dto);
    }

    @ApiOperation(value = "检验水平转移  删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping(value = {"/delete"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> remove(@PathVariable("organizationId") Long tenantId,@RequestBody List<String> transitionRuleIdList) {
        log.info("<====QmsTransitionRuleController-remove:{},{} ",tenantId, transitionRuleIdList );
        qmsTransitionRuleService.delete(tenantId,transitionRuleIdList);
        return Results.success();
    }

    @ApiOperation(value = "获取用户默认站点UI")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/user/default/site/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<QmsTransitionRuleDTO2> userDefaultSiteForUi(@PathVariable("organizationId") Long tenantId) {
        QmsTransitionRuleDTO2 dto = qmsTransitionRuleService.userDefaultSiteForUi(tenantId);
        return Results.success(dto);
    }
}
