package tarzan.iface.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 成本中心 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:39:33
 */
@RestController("mtCostcenterController.v1")
@RequestMapping("/v1/{organizationId}/mt-costcenter")
@Api(tags = "MtCostcenter")
public class MtCostcenterController extends BaseController {

}
