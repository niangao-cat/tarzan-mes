package tarzan.material.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 物料供应比例 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@RestController("mtMaterialSupplierPerHController.v1")
@RequestMapping("/v1/{organizationId}/mt-material-supplier-per-h")
@Api(tags = "MtMaterialSupplierPerH")
public class MtMaterialSupplierPerHController extends BaseController {

}
