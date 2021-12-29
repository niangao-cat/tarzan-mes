package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.repository.HmeCommonReportRepository;
import com.ruike.hme.domain.vo.HmeExceptionReportVO2;
import com.ruike.hme.domain.vo.HmeNonStandardDetailsVO;
import com.ruike.hme.domain.vo.HmeNonStandardReportVO;
import com.ruike.hme.domain.vo.HmeNonStandardReportVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/14 16:06
 */
@RestController("HmeCommonReportController.v1")
@RequestMapping("/v1/{organizationId}/hme-common-report")
@Api(tags = SwaggerApiConfig.HME_COMMON_REPORT)
public class HmeCommonReportController {

    @Autowired
    private HmeCommonReportRepository hmeCommonReportRepository;


    @ApiOperation(value = "非标产品报表")
    @GetMapping(value = "/non-standard-product-report", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeNonStandardReportVO2>> nonStandardProductReportQuery(
            @PathVariable("organizationId") Long tenantId, HmeNonStandardReportVO dto, @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeCommonReportRepository.nonStandardProductReportQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "待上线数量明细")
    @GetMapping(value = "/wait-qty-details-query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeNonStandardDetailsVO>> waitQtyDetailsQuery(
            @PathVariable("organizationId") Long tenantId, String workOrderId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeCommonReportRepository.waitQtyDetailsQuery(tenantId, workOrderId, pageRequest));
    }

    @ApiOperation(value = "在线数量明细")
    @GetMapping(value = "/online-qty-details-query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeNonStandardDetailsVO>> onlineQtyDetailsQuery(
            @PathVariable("organizationId") Long tenantId, String workOrderId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeCommonReportRepository.onlineQtyDetailsQuery(tenantId, workOrderId, pageRequest));
    }

    @ApiOperation(value = "完工数量明细")
    @GetMapping(value = "/completed-qty-details-query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeNonStandardDetailsVO>> completedQtyDetailsQuery(
            @PathVariable("organizationId") Long tenantId, String workOrderId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeCommonReportRepository.completedQtyDetailsQuery(tenantId, workOrderId, pageRequest));
    }

}
