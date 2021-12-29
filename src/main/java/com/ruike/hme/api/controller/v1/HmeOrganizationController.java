package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeOrganizationDTO;
import com.ruike.hme.api.dto.HmeOrganizationDTO2;
import com.ruike.hme.api.dto.HmeOrganizationDTO3;
import com.ruike.hme.api.dto.HmeOrganizationDTO4;
import com.ruike.hme.domain.service.HmeOrganizationService;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;

/**
 * 组织架构通用LOV查询 管理API
 *
 * @author: chaonan.hu@hand-china.com 2021-04-13 09:52:12
 **/
@RestController("hmeOrganizationController.v1")
@RequestMapping("/v1/{organizationId}/hme-organization")
public class HmeOrganizationController extends BaseController {

    @Autowired
    private HmeOrganizationService hmeOrganizationService;

    @ApiOperation(value = "工段LOV查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/lineworkcell-lov/query")
    public ResponseEntity<Page<MtModWorkcell>> lineWorkcellLovQuery(@PathVariable("organizationId") Long tenantId,
                                                                HmeOrganizationDTO dto,
                                                                PageRequest pageRequest) {
        return Results.success(hmeOrganizationService.lineWorkcellLovQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工序LOV查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/process-lov/query")
    public ResponseEntity<Page<MtModWorkcell>> processLovQuery(@PathVariable("organizationId") Long tenantId,
                                                               HmeOrganizationDTO2 dto,
                                                               PageRequest pageRequest) {
        return Results.success(hmeOrganizationService.processLovQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "产线LOV查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/prodline-lov/query")
    public ResponseEntity<Page<MtModProductionLine>> prodLineLovQuery(@PathVariable("organizationId") Long tenantId,
                                                                      HmeOrganizationDTO3 dto,
                                                                      PageRequest pageRequest) {
        return Results.success(hmeOrganizationService.prodLineLovQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工位LOV查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/workcell-lov/query")
    public ResponseEntity<Page<MtModWorkcell>> workcellLovQuery(@PathVariable("organizationId") Long tenantId,
                                                                HmeOrganizationDTO4 dto,
                                                                PageRequest pageRequest) {
        return Results.success(hmeOrganizationService.workcellLovQuery(tenantId, dto, pageRequest));
    }
}
