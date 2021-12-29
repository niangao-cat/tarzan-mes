package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeCosFunctionSelection;
import com.ruike.hme.domain.repository.HmeCosFunctionSelectionRepository;
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
 * 筛选芯片性能表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-08-19 09:37:16
 */
@RestController("hmeCosFunctionSelectionController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-function-selections")
public class HmeCosFunctionSelectionController extends BaseController {

    @Autowired
    private HmeCosFunctionSelectionRepository hmeCosFunctionSelectionRepository;

    @ApiOperation(value = "筛选芯片性能数据抽取Job")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> preSelectionFunctionDataJob(@PathVariable("organizationId") Long tenantId) {
        hmeCosFunctionSelectionRepository.preSelectionFunctionDataJob(tenantId);
        return Results.success();
    }

}
