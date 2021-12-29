package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.RkTableSummaryInfo;
import com.ruike.hme.domain.repository.RkTableSummaryInfoRepository;
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
 * 大表信息汇总统计数据 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-10-03 14:47:56
 */
@RestController("rkTableSummaryInfoController.v1")
@RequestMapping("/v1/{organizationId}/rk-table-summary-infos")
public class RkTableSummaryInfoController extends BaseController {

    @Autowired
    private RkTableSummaryInfoRepository rkTableSummaryInfoRepository;

    @ApiOperation(value = "大表信息汇总统计数据统计查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/report/query")
    public ResponseEntity<Page<RkTableSummaryInfo>> queryReport(@PathVariable("organizationId") Long tenantId,
                                                                RkTableSummaryInfo rkTableSummaryInfo,
                                                                @ApiIgnore PageRequest pageRequest) {
        Page<RkTableSummaryInfo> list = rkTableSummaryInfoRepository.queryReport(tenantId, pageRequest, rkTableSummaryInfo);
        return Results.success(list);
    }

}
