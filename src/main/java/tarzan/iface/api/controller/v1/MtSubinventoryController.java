package tarzan.iface.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * ERP库存表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:40:10
 */
@RestController("mtSubinventoryController.v1")
@RequestMapping("/v1/{organizationId}/mt-subinventory")
@Api(tags = "MtSubinventory")
public class MtSubinventoryController extends BaseController {

}
