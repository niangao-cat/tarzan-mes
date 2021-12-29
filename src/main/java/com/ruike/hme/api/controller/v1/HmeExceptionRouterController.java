package com.ruike.hme.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruike.hme.api.dto.HmeExceptionRouterDTO;
import com.ruike.hme.app.service.HmeExceptionRouterService;
import com.ruike.hme.domain.entity.HmeExceptionRouter;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import lombok.extern.slf4j.Slf4j;
import tarzan.config.SwaggerApiConfig;

/**
 * 异常反馈路线基础数据表 管理 API
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:25
 */
@RestController("hmeExceptionRouterController.v1")
@RequestMapping("/v1/{organizationId}/hme-exception-router")
@Api(tags = SwaggerApiConfig.HME_EXCEPTION_ROUTER)
@Slf4j
public class HmeExceptionRouterController extends BaseController {

    @Autowired
    private HmeExceptionRouterService service;

    @ApiOperation(value = "获取异常反馈路线信息（前台）")
    @GetMapping(value = "/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeExceptionRouter>> listForUi(
            @PathVariable("organizationId") Long tenantId, String exceptionId, PageRequest pageRequest) {
        log.info("<====HmeExceptionRouterController-listForUi:{}，{}", tenantId, exceptionId);
        return Results.success(service.listForUi(tenantId, exceptionId, pageRequest));
    }

    @ApiOperation(value = "新增&更新异常反馈路线信息（前台）")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeExceptionRouter>> saveForUi(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody List<HmeExceptionRouter> dtoList) {
        return Results.success(service.batchSaveForUi(tenantId, dtoList));
    }

    @ApiOperation(value = "删除异常反馈路线信息（前台）")
    @PostMapping(value = {"/delete/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> deleteForUi(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody HmeExceptionRouterDTO dto) {
        service.deleteForUi(tenantId, dto);
        return Results.success();
    }
}
