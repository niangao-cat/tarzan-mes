package io.tarzan.common.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 号码段按对象序列号记录表 管理 API
 *
 * @author MrZ 2019-08-22 21:38:58
 */
@RestController("mtNumrangeObjectNumController.v1")
@RequestMapping("/v1/{organizationId}/mt-numrange-object-num")
@Api(tags = "MtNumrangeObjectNum")
public class MtNumrangeObjectNumController extends BaseController {

}
