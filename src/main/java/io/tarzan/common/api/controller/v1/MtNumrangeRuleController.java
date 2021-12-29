package io.tarzan.common.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 号码段定义组合规则表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:43
 */
@RestController("mtNumrangeRuleController.v1")
@RequestMapping("/v1/{organizationId}/mt-numrange-rule")
@Api(tags = "MtNumrangeRule")
public class MtNumrangeRuleController extends BaseController {

}
