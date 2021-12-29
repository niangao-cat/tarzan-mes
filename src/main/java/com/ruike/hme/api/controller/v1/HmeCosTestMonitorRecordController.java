package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeCosTestMonitorRecordService;
import com.ruike.hme.domain.vo.HmeCosTestMonitorRecordVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * COS测试良率监控记录表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-09-16 14:29:14
 */
@RestController("hmeCosTestMonitorRecordController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-test-monitor-records")
public class HmeCosTestMonitorRecordController extends BaseController {

    private final HmeCosTestMonitorRecordService hmeCosTestMonitorRecordService;

    public HmeCosTestMonitorRecordController(HmeCosTestMonitorRecordService hmeCosTestMonitorRecordService) {
        this.hmeCosTestMonitorRecordService = hmeCosTestMonitorRecordService;
    }

    @ApiOperation(value = "COS测试良率监控记录表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-cos-monitor-record")
    public ResponseEntity<Page<HmeCosTestMonitorRecordVO>> queryCosTestMonitorRecord(@PathVariable("organizationId") Long organizationId, String cosMonitorHeaderId, PageRequest pageRequest) {
        hmeCosTestMonitorRecordService.queryCosTestMonitorRecord(organizationId, cosMonitorHeaderId, pageRequest);
        return Results.success(hmeCosTestMonitorRecordService.queryCosTestMonitorRecord(organizationId, cosMonitorHeaderId, pageRequest));
    }
}
