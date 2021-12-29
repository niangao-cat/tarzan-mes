package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEmployeeAttendanceDto;
import com.ruike.hme.api.dto.HmeEmployeeAttendanceDto1;
import com.ruike.hme.app.service.HmeEmployeeAttendanceExportService;
import com.ruike.hme.app.service.HmeEmployeeOutputSummaryService;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeEmployeeOutputSummary;
import com.ruike.hme.domain.repository.HmeEmployeeOutputSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 员工产量汇总表 管理 API
 *
 * @author penglin.sui@hand-china.com 2021-07-28 11:19:48
 */
@RestController("hmeEmployeeOutputSummaryController.v1")
@RequestMapping("/v1/{organizationId}/hme-employee-output-summarys")
public class HmeEmployeeOutputSummaryController extends BaseController {

    @Autowired
    private HmeEmployeeOutputSummaryService hmeEmployeeOutputSummaryService;

    @ApiOperation(value = "创建汇总数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value="/create")
    public ResponseEntity<?> findOneList(@PathVariable(value = "organizationId") Long tenantId) {
        hmeEmployeeOutputSummaryService.employeeOutPutSummary(tenantId);
        return Results.success();
    }
}
