package tarzan.modeling.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 客户 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:29:33
 */
@RestController("mtCustomerController.v1")
@RequestMapping("/v1/{organizationId}/mt-customer")
@Api(tags = "MtCustomer")
public class MtCustomerController extends BaseController {

}
