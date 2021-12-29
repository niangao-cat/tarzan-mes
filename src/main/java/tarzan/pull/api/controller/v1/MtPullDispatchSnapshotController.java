package tarzan.pull.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 拉动调度结果快照 管理 API
 *
 * @author peng.yuan@@hand-china.com 2020-02-04 14:38:41
 */
@RestController("mtPullDispatchSnapshotController.v1")
@RequestMapping("/v1/{organizationId}/mt-pull-dispatch-snapshot")
@Api(tags = "MtPullDispatchSnapshot")
public class MtPullDispatchSnapshotController extends BaseController {

}
