package tarzan.dispatch.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;

/**
 * 调度执行实绩表 管理 API
 *
 * @author xiao.tang02@hand-china.com 2020-02-25 11:50:47
 */
@RestController("mtEoDispatchExecuteActualController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-dispatch-execute-actual")
@Api(tags = "MtEoDispatchExecuteActual")
public class MtEoDispatchExecuteActualController extends BaseController {

}
