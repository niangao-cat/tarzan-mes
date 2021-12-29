package tarzan.method.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 工艺路线步骤对应工序组件历史 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@RestController("mtRouterOperationCompHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-router-operation-comp-his")
@Api(tags = "MtRouterOperationCompHis")
public class MtRouterOperationCompHisController extends BaseController {

}
