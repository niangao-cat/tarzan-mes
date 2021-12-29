package com.ruike.hme.api.controller.v1;

import io.swagger.annotations.Api;
import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.*;

import tarzan.config.SwaggerApiConfig;

/**
 * 物流信息历史表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-09-02 16:37:01
 */
@RestController("hmeLogisticsInfoHisController.v1")
@RequestMapping("/v1/{organizationId}/hme-logistics-info-hiss")
@Api(tags = SwaggerApiConfig.HME_LOGISTICS_INFO_HIS)
public class HmeLogisticsInfoHisController extends BaseController {

}
