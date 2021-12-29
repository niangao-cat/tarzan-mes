package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeFifthAreaKanbanLoad;
import com.ruike.hme.domain.repository.HmeFifthAreaKanbanLoadRepository;
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
 * 五部看板装载信息 管理 API
 *
 * @author penglin.sui@hand-china.com 2021-06-08 16:01:49
 */
@RestController("hmeFifthAreaKanbanLoadController.v1")
@RequestMapping("/v1/{organizationId}/hme-fifth-area-kanban-loads")
public class HmeFifthAreaKanbanLoadController extends BaseController {

}
