package tarzan.iface.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * ERP货位业务表 管理 API
 *
 * @author guichuan.li@hand-china.com 2019-09-24 10:49:01
 */
@RestController("mtErpSubinvLocatorController.v1")
@RequestMapping("/v1/{organizationId}/mt-erp-subinv-locator")
@Api(tags = "MtErpSubinvLocator")
public class MtErpSubinvLocatorController extends BaseController {

}
