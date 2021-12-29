package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCenterKanbanHeaderDTO;
import com.ruike.hme.api.dto.HmeCenterKanbanHeaderDTO2;
import com.ruike.hme.api.dto.HmeCenterKanbanLineDTO;
import com.ruike.hme.app.service.HmeCenterKanbanHeaderService;
import com.ruike.hme.domain.entity.HmeCenterKanbanLine;
import com.ruike.hme.domain.vo.HmeCenterKanbanHeaderVO;
import com.ruike.hme.domain.vo.HmeCenterKanbanLineVO;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeCenterKanbanHeader;
import com.ruike.hme.domain.repository.HmeCenterKanbanHeaderRepository;
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
 * 制造中心看板信息头表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-05-28 13:49:51
 */
@RestController("hmeCenterKanbanHeaderController.v1")
@RequestMapping("/v1/{organizationId}/hme-center-kanban-headers")
public class HmeCenterKanbanHeaderController extends BaseController {

    @Autowired
    private HmeCenterKanbanHeaderService hmeCenterKanbanHeaderService;

    @ApiOperation(value = "新建或者更新制造中心看板信息头")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeCenterKanbanHeader> createOrUpdateHeader(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody HmeCenterKanbanHeaderDTO dto) {
        return Results.success(hmeCenterKanbanHeaderService.createOrUpdateHeader(tenantId, dto));
    }

    @ApiOperation(value = "新建或者更新制造中心看板信息行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/line", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeCenterKanbanLine> createOrUpdateLine(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody HmeCenterKanbanLineDTO dto) {
        return Results.success(hmeCenterKanbanHeaderService.createOrUpdateLine(tenantId, dto));
    }

    @ApiOperation(value = "分页查询制造中心看板信息头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeCenterKanbanHeaderVO>> headPageQuery(@PathVariable("organizationId") Long tenantId,
                                                                      HmeCenterKanbanHeaderDTO2 dto, PageRequest pageRequest) {
        return Results.success(hmeCenterKanbanHeaderService.headPageQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "分页查询制造中心看板信息行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{centerKanbanHeaderId}")
    public ResponseEntity<Page<HmeCenterKanbanLineVO>> linePageQuery(@PathVariable("organizationId") Long tenantId,
                                                                     @PathVariable("centerKanbanHeaderId") String centerKanbanHeaderId,
                                                                     PageRequest pageRequest) {
        return Results.success(hmeCenterKanbanHeaderService.linePageQuery(tenantId, centerKanbanHeaderId, pageRequest));
    }

}
