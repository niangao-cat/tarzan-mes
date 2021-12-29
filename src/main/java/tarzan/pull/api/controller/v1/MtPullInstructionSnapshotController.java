package tarzan.pull.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 拉动驱动指令快照 管理 API
 *
 * @author yiyang.xie 2020-02-04 15:53:01
 */
@RestController("mtPullInstructionSnapshotController.v1")
@RequestMapping("/v1/{organizationId}/mt-pull-instruction-snapshot")
@Api(tags = "MtPullInstructionSnapshot")
public class MtPullInstructionSnapshotController extends BaseController {

}
