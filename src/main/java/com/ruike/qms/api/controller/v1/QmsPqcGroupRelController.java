package com.ruike.qms.api.controller.v1;

import com.ruike.qms.domain.repository.QmsPqcGroupRelRepository;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 巡检物料与检验组关系表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-12 16:41:12
 */
@RestController("qmsPqcGroupRelController.v1")
@RequestMapping("/v1/{organizationId}/qms-pqc-group-rels")
public class QmsPqcGroupRelController extends BaseController {

    @Autowired
    private QmsPqcGroupRelRepository qmsPqcGroupRelRepository;



}
