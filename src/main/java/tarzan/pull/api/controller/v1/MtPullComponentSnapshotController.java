package tarzan.pull.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 拉动订单组件快照 管理 API
 *
 * @author peng.yuan@@hand-china.com 2020-02-04 14:38:42
 */
@RestController("mtPullComponentSnapshotController.v1")
@RequestMapping("/v1/{organizationId}/mt-pull-component-snapshot")
@Api(tags = "MtPullComponentSnapshot")
public class MtPullComponentSnapshotController extends BaseController {

}
