package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmePumpingSourceDTO;
import com.ruike.hme.app.service.HmePumpingSourceService;
import com.ruike.hme.domain.vo.HmePumpingSourceAllVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 泵浦源性能数据展示
 *
 * @author wengang.qiang@hand-china 2021/09/02 16:23
 */
@RestController("hmePumpingSourceController.v1")
@RequestMapping("/v1/{organizationId}/hme_pumping_sources")
public class HmePumpingSourceController {

    private final HmePumpingSourceService hmePumpingSourceService;

    public HmePumpingSourceController(HmePumpingSourceService hmePumpingSourceService) {
        this.hmePumpingSourceService = hmePumpingSourceService;
    }

    @ApiOperation(value = "泵浦源性能数据查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/hme-pumping-source-query")
    public ResponseEntity<HmePumpingSourceAllVO> queryFilterRuleList(@PathVariable("organizationId") Long tenantId,
                                                                     HmePumpingSourceDTO hmePumpingSourceDTO,
                                                                     @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmePumpingSourceService.queryHmePumpingSource(tenantId, hmePumpingSourceDTO, pageRequest));
    }
}
