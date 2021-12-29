package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeCosWipQueryService;
import io.choerodon.core.domain.Page;
import com.ruike.hme.api.dto.HmeCosWipQueryDTO;
import com.ruike.hme.domain.vo.HmeCosWipQueryVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

@RestController("HmeCosWipQueryController.v1")
@RequestMapping("/v1/{organizationId}/hme_cos_wip_query")
@Api(tags = SwaggerApiConfig.HME_COS_WIP_QUERY)
@Slf4j
public class HmeCosWipQueryController {

    @Autowired
    private HmeCosWipQueryService hmeCosWipQueryService;

    @ApiOperation(value = "COS在制查询")
    @PostMapping(value = {"/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeCosWipQueryVO>> propertyCosWipQuery(@PathVariable("organizationId") Long tenantId,
                                                                               @RequestBody HmeCosWipQueryDTO dto,
                                                                               PageRequest pageRequest) {

        return Results.success(hmeCosWipQueryService.propertyCosWipQuery(tenantId, dto,pageRequest));
    }
}
