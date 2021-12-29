package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeFifthAreaKanbanService;
import com.ruike.hme.domain.entity.HmeFacYk;
import com.ruike.hme.domain.entity.HmeFacYkVO2;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeFifthAreaKanban;
import com.ruike.hme.domain.repository.HmeFifthAreaKanbanRepository;
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
 * 五部看板 管理 API
 *
 * @author penglin.sui@hand-china.com 2021-06-08 07:53:30
 */
@RestController("hmeFifthAreaKanbanController.v1")
@RequestMapping("/v1/{organizationId}/hme-fifth-area-kanbans")
public class HmeFifthAreaKanbanController extends BaseController {

    @Autowired
    private HmeFifthAreaKanbanService hmeFifthAreaKanbanService;

    @ApiOperation(value = "创建五部看板数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> createFifthArea(@PathVariable("organizationId") Long tenantId) {
        hmeFifthAreaKanbanService.createFifthArea(tenantId);
        return Results.success("success");
    }
}
