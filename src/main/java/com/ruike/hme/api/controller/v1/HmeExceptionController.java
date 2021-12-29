package com.ruike.hme.api.controller.v1;

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

import com.ruike.hme.app.service.HmeExceptionService;
import com.ruike.hme.domain.entity.HmeException;
import com.ruike.hme.api.dto.HmeExceptionDTO;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import tarzan.config.SwaggerApiConfig;

/**
 * 异常维护基础数据头表 管理 API
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:25
 */
@RestController("hmeExceptionController.v1")
@RequestMapping("/v1/{organizationId}/hme-exception")
@Api(tags = SwaggerApiConfig.HME_EXCEPTION)
@Slf4j
public class HmeExceptionController extends BaseController {

    @Autowired
    private HmeExceptionService service;

    @ApiOperation(value = "获取异常基础信息（前台）")
    @GetMapping(value = "/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeException>> listForUi(
            @PathVariable("organizationId") Long tenantId, HmeExceptionDTO dto, PageRequest pageRequest) {
        return Results.success(service.listForUi(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "新增&更新异常基础信息（前台）")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeException> saveForUi(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody HmeException dto) {
        return Results.success(service.saveForUi(tenantId, dto));
    }

}
