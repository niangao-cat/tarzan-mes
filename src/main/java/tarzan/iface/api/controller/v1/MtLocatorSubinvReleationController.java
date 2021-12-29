package tarzan.iface.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 子库存与库位对应关系 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:39:44
 */
@RestController("mtLocatorSubinvReleationController.v1")
@RequestMapping("/v1/{organizationId}/mt-locator-subinv-releation")
@Api(tags = "MtLocatorSubinvReleation")
public class MtLocatorSubinvReleationController extends BaseController {

}
