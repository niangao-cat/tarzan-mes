package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeExcGroupWkcAssignDTO;
import com.ruike.hme.app.service.HmeExcGroupWkcAssignService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.config.SwaggerApiConfig;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 异常收集组分配WKC表 管理 API
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:23
 */
@RestController("hmeExcGroupWkcAssignController.v1")
@RequestMapping("/v1/{organizationId}/hme-exc-group-wkc-assign")
@Api(tags = SwaggerApiConfig.HME_EXC_GROUP_WKC_ASSIGN)
public class HmeExcGroupWkcAssignController extends BaseController {

    @Autowired
    private HmeExcGroupWkcAssignService service;

    @ApiOperation(value = "删除异常收集组分配WKC（前台）")
    @PostMapping(value = {"/delete/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Void> deleteForUi(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody HmeExcGroupWkcAssignDTO dto) {
        service.deleteForUi(tenantId, dto);
        return Results.success();
    }
}
