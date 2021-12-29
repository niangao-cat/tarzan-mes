package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeCosTestPassRateHisService;
import com.ruike.hme.domain.vo.HmeCosTestPassRateHisVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * COS测试良率维护历史表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-09-06 11:44:40
 */
@RestController("hmeCosTestPassRateHisController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-test-pass-rate-hiss")
public class HmeCosTestPassRateHisController extends BaseController {

    private final HmeCosTestPassRateHisService hmeCosTestPassRateHisService;

    public HmeCosTestPassRateHisController(HmeCosTestPassRateHisService hmeCosTestPassRateHisService) {
        this.hmeCosTestPassRateHisService = hmeCosTestPassRateHisService;
    }

    @ApiOperation(value = "COS测试良率维护历史表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/cos-test-pass-rate-his-query")
    public List<HmeCosTestPassRateHisVO> queryHmeCosTestPassRateHis(@PathVariable("organizationId") Long tenantId,
                                                                    String testId) {
        return hmeCosTestPassRateHisService.queryHmeCosTestPassRateHis(tenantId, testId);
    }
}
