package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;

import com.ruike.hme.api.dto.HmeEoJobContainerDTO;
import io.swagger.annotations.Api;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.config.SwaggerApiConfig;
import com.ruike.hme.domain.entity.HmeEoJobContainer;
import com.ruike.hme.domain.repository.HmeEoJobContainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import com.ruike.hme.domain.vo.HmeEoJobContainerVO;
import com.ruike.hme.domain.vo.HmeEoJobContainerVO2;

/**
 * 工序作业平台-容器 管理 API
 *
 * @author liyuan.lv@hand-china.com 2020-03-23 12:48:53
 */
@RestController("hmeEoJobContainerController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-container")
@Api(tags = SwaggerApiConfig.HME_EO_JOB_CONTAINER)
public class HmeEoJobContainerController extends BaseController {

    @Autowired
    private HmeEoJobContainerRepository repository;

    @ApiOperation(value = "更新容器")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/update-container"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeEoJobContainerVO2> updateContainer(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody HmeEoJobContainerVO dto) {
        return Results.success(repository.updateEoJobContainer(tenantId, dto));
    }

    @ApiOperation(value = "容器明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/detail"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeEoJobContainerVO2> detail(@PathVariable("organizationId") Long tenantId,
                                                       HmeEoJobContainerDTO dto) {
        return Results.success(repository.constructVO(tenantId, dto.getWorkcellId()));
    }

    @ApiOperation(value = "卸载容器")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/unload-container"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> unLoadContainer(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody HmeEoJobContainerVO dto) {
        repository.unLoadEoJobContainer(tenantId, dto);
        return Results.success();
    }
}
