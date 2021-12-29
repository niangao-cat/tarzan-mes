package com.ruike.hme.api.controller.v1;

import io.swagger.annotations.Api;
import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tarzan.config.SwaggerApiConfig;

/**
 * 组织职能关系表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2020-07-28 10:43:15
 */
@RestController("hmeOrganizationUnitRelController.v1")
@RequestMapping("/v1/{organizationId}/hme-organization-unit-rels")
@Api(tags = SwaggerApiConfig.HME_ORGANIZATION_UNIT_REL)
public class HmeOrganizationUnitRelController extends BaseController {



}
