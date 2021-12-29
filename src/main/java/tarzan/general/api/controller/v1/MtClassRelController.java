package tarzan.general.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:57:18
 */
@RestController("mtClassRelController.v1")
@RequestMapping("/v1/{organizationId}/mt-class-rel")
@Api(tags = "MtClassRel")
public class MtClassRelController extends BaseController {

}
