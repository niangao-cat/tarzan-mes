package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.repository.HmePumpModPositionLineRepository;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 泵浦源模块位置行表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-25 17:34:37
 */
@RestController("hmePumpModPositionLineController.v1")
@RequestMapping("/v1/{organizationId}/hme-pump-mod-position-lines")
public class HmePumpModPositionLineController extends BaseController {

    @Autowired
    private HmePumpModPositionLineRepository hmePumpModPositionLineRepository;


}
