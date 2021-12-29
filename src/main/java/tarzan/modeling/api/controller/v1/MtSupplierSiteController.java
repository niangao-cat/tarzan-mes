package tarzan.modeling.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 供应商地点 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:31:22
 */
@RestController("mtSupplierSiteController.v1")
@RequestMapping("/v1/{organizationId}/mt-supplier-site")
@Api(tags = "MtSupplierSite")
public class MtSupplierSiteController extends BaseController {

}
