package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeProductionGroupDTO;
import com.ruike.hme.api.dto.HmeProductionGroupDTO2;
import com.ruike.hme.api.dto.HmeProductionGroupDTO3;
import com.ruike.hme.app.service.HmeProductionGroupService;
import com.ruike.hme.domain.entity.HmeProductionGroupLine;
import com.ruike.hme.domain.vo.HmeProductionGroupVO;
import com.ruike.hme.domain.vo.HmeProductionGroupVO2;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeProductionGroup;
import com.ruike.hme.domain.repository.HmeProductionGroupRepository;
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
 * 产品组 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-05-27 13:47:50
 */
@RestController("hmeProductionGroupController.v1")
@RequestMapping("/v1/{organizationId}/hme-production-groups")
public class HmeProductionGroupController extends BaseController {

    @Autowired
    private HmeProductionGroupService hmeProductionGroupService;

    @ApiOperation(value = "新建或者更新产品组头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeProductionGroup> createOrUpdate(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody HmeProductionGroupDTO dto) {
        return Results.success(hmeProductionGroupService.createOrUpdate(tenantId, dto));
    }

    @ApiOperation(value = "新建或者更新产品组行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/line", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeProductionGroupLine> createOrUpdateLine(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody HmeProductionGroupDTO3 dto) {
        return Results.success(hmeProductionGroupService.createOrUpdateLine(tenantId, dto));
    }

    @ApiOperation(value = "分页查询产品组头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeProductionGroupVO>> pageQuery(@PathVariable("organizationId") Long tenantId,
                                                                HmeProductionGroupDTO2 dto, PageRequest pageRequest) {
        Page<HmeProductionGroupVO> resultPage = hmeProductionGroupService.pageQuery(tenantId, dto, pageRequest);
        return Results.success(resultPage);
    }

    @ApiOperation(value = "分页查询产品组行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/line/{productionGroupId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeProductionGroupVO2>> linePageQuery(@PathVariable("organizationId") Long tenantId,
                                                                     @PathVariable("productionGroupId") String productionGroupId, PageRequest pageRequest) {
        Page<HmeProductionGroupVO2> resultPage = hmeProductionGroupService.linePageQuery(tenantId, productionGroupId, pageRequest);
        return Results.success(resultPage);
    }
}
