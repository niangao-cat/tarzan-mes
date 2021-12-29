package tarzan.pull.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 配送节点表 管理 API
 *
 * @author peng.yuan@@hand-china.com 2020-02-04 14:38:42
 */
@RestController("mtModDisLocatorDetealController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-dis-locator-deteal")
@Api(tags = "MtModDisLocatorDeteal")
public class MtModDisLocatorDetealController extends BaseController {

}
