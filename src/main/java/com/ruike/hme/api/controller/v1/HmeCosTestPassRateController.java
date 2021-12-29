package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosTestPassRateDTO;
import com.ruike.hme.app.service.HmeCosTestPassRateService;
import com.ruike.hme.domain.vo.HmeCosTestPassRateVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * COS测试良率维护表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-09-06 11:44:38
 */
@RestController("hmeCosTestPassRateController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-test-pass-rates")
public class HmeCosTestPassRateController extends BaseController {

    private final HmeCosTestPassRateService hmeCosTestPassRateService;

    public HmeCosTestPassRateController(HmeCosTestPassRateService hmeCosTestPassRateService) {
        this.hmeCosTestPassRateService = hmeCosTestPassRateService;
    }

    @ApiOperation(value = "COS测试良率维护表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-cos-pass-rate")
    public ResponseEntity<Page<HmeCosTestPassRateVO>> list(@PathVariable("organizationId") Long tenantId,
                                                           HmeCosTestPassRateDTO hmeCosTestPassRateDTO,
                                                           PageRequest pageRequest) {
        return Results.success(hmeCosTestPassRateService.queryCosTestPassRate(tenantId, hmeCosTestPassRateDTO, pageRequest));
    }


    @ApiOperation(value = "创建COS测试良率维护表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save-cos-pass-rate")
    public ResponseEntity<HmeCosTestPassRateVO> saveHmeCosTestPassRate(@PathVariable("organizationId") Long organizationId, @RequestBody HmeCosTestPassRateVO hmeCosTestPassRateVO) {

        hmeCosTestPassRateService.saveHmeCosTestPassRate(organizationId, hmeCosTestPassRateVO);
        return Results.success();
    }
}
