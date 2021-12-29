package com.ruike.reports.api.controller.v1;

import com.ruike.reports.api.dto.HmeDocSummaryQueryDTO;
import com.ruike.reports.domain.repository.HmeDocSummaryQueryRepository;
import com.ruike.reports.domain.vo.HmeDocSummaryQueryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

/**
 * @ClassName HmeDocSummaryQueryController
 * @Description 单据汇总查询报表
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/2/25 16:22
 * @Version 1.0
 **/
@RestController("hmeDocSummaryQueryController.v1")
@RequestMapping("/v1/{organizationId}/doc-summary")
@Api(tags = SwaggerApiConfig.HME_PLAN_RATE_REPORT)
public class HmeDocSummaryQueryController extends BaseController {

    @Autowired
    private HmeDocSummaryQueryRepository hmeDocSummaryQueryRepository;

    @ApiOperation(value = "单据汇总查询报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeDocSummaryQueryVO>> list(@PathVariable("organizationId") Long tenantId,
                                                           HmeDocSummaryQueryDTO dto,
                                                           @ApiIgnore PageRequest pageRequest) {
        Page<HmeDocSummaryQueryVO> list = hmeDocSummaryQueryRepository.pageList(tenantId, dto, pageRequest);
        return Results.success(list);
    }


}