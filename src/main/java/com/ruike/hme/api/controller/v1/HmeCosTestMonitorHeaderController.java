package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosTestMonitorHeaderDTO;
import com.ruike.hme.app.service.HmeCosTestMonitorHeaderService;
import com.ruike.hme.domain.vo.HmeCosTestMonitorHeaderVO;
import com.ruike.hme.domain.vo.HmeCosTestMonitorResponseVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * COS测试良率监控头表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-09-16 14:29:12
 */
@RestController("hmeCosTestMonitorHeaderController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-test-monitor-headers")
public class HmeCosTestMonitorHeaderController extends BaseController {

    private final HmeCosTestMonitorHeaderService hmeCosTestMonitorHeaderService;

    public HmeCosTestMonitorHeaderController(HmeCosTestMonitorHeaderService hmeCosTestMonitorHeaderService) {
        this.hmeCosTestMonitorHeaderService = hmeCosTestMonitorHeaderService;
    }

    @ApiOperation(value = "COS测试良率监控头表列表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-cos-monitor-header")
    public ResponseEntity<Page<HmeCosTestMonitorHeaderVO>> queryCosTestMonitorHeader(@PathVariable("organizationId") Long organizationId,
                                                                                     HmeCosTestMonitorHeaderDTO dto,
                                                                                     PageRequest pageRequest) {
        dto.initParam();
        dto.initParamWafer();
        return Results.success(hmeCosTestMonitorHeaderService.queryCosTestMonitorHeader(organizationId, dto, pageRequest));
    }

    @ApiOperation(value = "COS测试良率监控头表放行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/pass-cos-monitor-header")
    public ResponseEntity<HmeCosTestMonitorResponseVO> passWafer(@PathVariable("organizationId") Long organizationId,
                                                                 @RequestBody List<HmeCosTestMonitorHeaderVO> hmeCosTestMonitorHeaderVOList
    ) {
        HmeCosTestMonitorResponseVO hmeCosTestMonitorResponseVO = new HmeCosTestMonitorResponseVO();
        try {
            hmeCosTestMonitorResponseVO = hmeCosTestMonitorHeaderService.passWafer(organizationId, hmeCosTestMonitorHeaderVOList);
        } catch (Exception e) {
            hmeCosTestMonitorResponseVO.setSuccess("false");
            hmeCosTestMonitorResponseVO.setCode("");
            hmeCosTestMonitorResponseVO.setMessage(e.getMessage());
        }
        return Results.success(hmeCosTestMonitorResponseVO);
    }

}
