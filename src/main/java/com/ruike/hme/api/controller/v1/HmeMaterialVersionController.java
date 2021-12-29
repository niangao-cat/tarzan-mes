package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeMaterialVersion;
import com.ruike.hme.domain.repository.HmeMaterialVersionRepository;
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
 * 物料版本表 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-09-28 15:14:48
 */
        @RestController("hmeMaterialVersionSiteController.v1")
    @RequestMapping("/v1/hme-material-versions")
    public class HmeMaterialVersionController extends BaseController {

    @Autowired
    private HmeMaterialVersionRepository hmeMaterialVersionRepository;

    @ApiOperation(value = "物料版本表列表")
                @Permission(level = ResourceLevel.SITE)
            @GetMapping
    public ResponseEntity<Page<HmeMaterialVersion>> list(HmeMaterialVersion hmeMaterialVersion, @ApiIgnore @SortDefault(value = HmeMaterialVersion.FIELD_TENANT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeMaterialVersion> list = hmeMaterialVersionRepository.pageAndSort(pageRequest, hmeMaterialVersion);
        return Results.success(list);
    }

    @ApiOperation(value = "物料版本表明细")
                @Permission(level = ResourceLevel.SITE)
            @GetMapping("/{tenantId}")
    public ResponseEntity<HmeMaterialVersion> detail(@PathVariable Long tenantId) {
        HmeMaterialVersion hmeMaterialVersion =hmeMaterialVersionRepository.selectByPrimaryKey(tenantId);
        return Results.success(hmeMaterialVersion);
    }

    @ApiOperation(value = "创建物料版本表")
                @Permission(level = ResourceLevel.SITE)
            @PostMapping
    public ResponseEntity<HmeMaterialVersion> create(@RequestBody HmeMaterialVersion hmeMaterialVersion) {
        validObject(hmeMaterialVersion);
            hmeMaterialVersionRepository.insertSelective(hmeMaterialVersion);
        return Results.success(hmeMaterialVersion);
    }

    @ApiOperation(value = "修改物料版本表")
                @Permission(level = ResourceLevel.SITE)
            @PutMapping
    public ResponseEntity<HmeMaterialVersion> update(@RequestBody HmeMaterialVersion hmeMaterialVersion) {
        SecurityTokenHelper.validToken(hmeMaterialVersion);
            hmeMaterialVersionRepository.updateByPrimaryKeySelective(hmeMaterialVersion);
        return Results.success(hmeMaterialVersion);
    }

    @ApiOperation(value = "删除物料版本表")
                @Permission(level = ResourceLevel.SITE)
            @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeMaterialVersion hmeMaterialVersion) {
        SecurityTokenHelper.validToken(hmeMaterialVersion);
            hmeMaterialVersionRepository.deleteByPrimaryKey(hmeMaterialVersion);
        return Results.success();
    }

}
