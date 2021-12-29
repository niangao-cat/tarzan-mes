package com.ruike.hme.api.controller.v1;

import java.util.List;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;

import com.ruike.hme.domain.vo.HmeEoJobBeyondMaterialVO;
import com.ruike.hme.app.service.HmeEoJobBeyondMaterialService;
import com.ruike.hme.domain.entity.HmeEoJobBeyondMaterial;
import com.ruike.hme.domain.repository.HmeEoJobBeyondMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

/**
 * 工序作业平台-计划外物料 管理 API
 *
 * @author liyuan.lv@hand-china.com 2020-07-15 15:27:06
 */
@RestController("hmeEoJobBeyondMaterialController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-beyond-material")
@Api(tags = SwaggerApiConfig.HME_EO_JOB_BEYOND_MATERIAL)
public class HmeEoJobBeyondMaterialController extends BaseController {

    @Autowired
    private HmeEoJobBeyondMaterialRepository repository;

    @ApiOperation(value = "工序作业平台-计划外物料列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<List<HmeEoJobBeyondMaterial>> listForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                                  HmeEoJobBeyondMaterialVO dto) {
        return Results.success(repository.list(tenantId, dto));
    }

    @ApiOperation(value = "工序作业平台-计划外物料保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-save")
    public ResponseEntity<List<HmeEoJobBeyondMaterial>> batchSave(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody List<HmeEoJobBeyondMaterial> dtoList) {
        return Results.success(this.repository.batchSave(tenantId, dtoList));
    }

    @ApiOperation(value = "删除工序作业平台-计划外物料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping("/batch-remove")
    public ResponseEntity<?> batchRemove(@RequestBody List<HmeEoJobBeyondMaterial> dtoList) {
        dtoList.forEach(SecurityTokenHelper::validToken);
        repository.batchRemove(dtoList);
        return Results.success();
    }

}
