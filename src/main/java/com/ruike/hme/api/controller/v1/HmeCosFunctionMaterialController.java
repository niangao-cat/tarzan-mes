package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeCosFunctionMaterialService;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeCosFunctionMaterial;
import com.ruike.hme.domain.repository.HmeCosFunctionMaterialRepository;
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
 * COS投料性能表 管理 API
 *
 * @author penglin.sui@hand-china.com 2021-06-22 20:50:13
 */
@RestController("hmeCosFunctionMaterialController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-function-materials")
public class HmeCosFunctionMaterialController extends BaseController {

    @Autowired
    private HmeCosFunctionMaterialService hmeCosFunctionMaterialService;

    @ApiOperation(value = "创建COS投料性能数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> createCosFunctionMaterial(@PathVariable("organizationId") Long tenantId) {
        hmeCosFunctionMaterialService.createCosFunctionMaterial(tenantId);
        return Results.success("success");
    }
}
