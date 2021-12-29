package tarzan.method.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 工艺路线站点分配历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@RestController("mtRouterSiteAssignHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-router-site-assign-his")
@Api(tags = "MtRouterSiteAssignHis")
public class MtRouterSiteAssignHisController extends BaseController {

}
