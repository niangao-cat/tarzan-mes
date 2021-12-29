package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeProductionVersion;
import com.ruike.hme.domain.repository.HmeProductionVersionRepository;
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
 * 生产版本表 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-08-20 14:01:23
 */
@RestController("hmeProductionVersionController.v1")
@RequestMapping("/v1/{organizationId}/hme-production-versions")
public class HmeProductionVersionController extends BaseController {

    @Autowired
    private HmeProductionVersionRepository hmeProductionVersionRepository;

    @ApiOperation(value = "生产版本表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeProductionVersion>> list(HmeProductionVersion hmeProductionVersion, @ApiIgnore @SortDefault(value = HmeProductionVersion.FIELD_PRODUCTION_VERSION_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeProductionVersion> list = hmeProductionVersionRepository.pageAndSort(pageRequest, hmeProductionVersion);
        return Results.success(list);
    }

    @ApiOperation(value = "生产版本表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{productionVersionId}")
    public ResponseEntity<HmeProductionVersion> detail(@PathVariable Long productionVersionId) {
        HmeProductionVersion hmeProductionVersion = hmeProductionVersionRepository.selectByPrimaryKey(productionVersionId);
        return Results.success(hmeProductionVersion);
    }

    @ApiOperation(value = "创建生产版本表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeProductionVersion> create(@RequestBody HmeProductionVersion hmeProductionVersion) {
        validObject(hmeProductionVersion);
        hmeProductionVersionRepository.insertSelective(hmeProductionVersion);
        return Results.success(hmeProductionVersion);
    }

    @ApiOperation(value = "修改生产版本表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeProductionVersion> update(@RequestBody HmeProductionVersion hmeProductionVersion) {
        SecurityTokenHelper.validToken(hmeProductionVersion);
        hmeProductionVersionRepository.updateByPrimaryKeySelective(hmeProductionVersion);
        return Results.success(hmeProductionVersion);
    }

    @ApiOperation(value = "删除生产版本表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeProductionVersion hmeProductionVersion) {
        SecurityTokenHelper.validToken(hmeProductionVersion);
        hmeProductionVersionRepository.deleteByPrimaryKey(hmeProductionVersion);
        return Results.success();
    }

}
