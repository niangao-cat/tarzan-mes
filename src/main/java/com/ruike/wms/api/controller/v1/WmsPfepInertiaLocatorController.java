package com.ruike.wms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsPfepInertiaLocator;
import com.ruike.wms.domain.repository.WmsPfepInertiaLocatorRepository;
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
 * 惯性货位记录表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-09-21 19:31:16
 */
@RestController("wmsPfepInertiaLocatorController.v1")
@RequestMapping("/v1/{organizationId}/wms-pfep-inertia-locators")
public class WmsPfepInertiaLocatorController extends BaseController {

    @Autowired
    private WmsPfepInertiaLocatorRepository wmsPfepInertiaLocatorRepository;

    @ApiOperation(value = "惯性货位记录表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsPfepInertiaLocator>> list(WmsPfepInertiaLocator wmsPfepInertiaLocator, @ApiIgnore @SortDefault(value = WmsPfepInertiaLocator.FIELD_INERTIA_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WmsPfepInertiaLocator> list = wmsPfepInertiaLocatorRepository.pageAndSort(pageRequest, wmsPfepInertiaLocator);
        return Results.success(list);
    }

    @ApiOperation(value = "惯性货位记录表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{inertiaId}")
    public ResponseEntity<WmsPfepInertiaLocator> detail(@PathVariable Long inertiaId) {
        WmsPfepInertiaLocator wmsPfepInertiaLocator = wmsPfepInertiaLocatorRepository.selectByPrimaryKey(inertiaId);
        return Results.success(wmsPfepInertiaLocator);
    }

    @ApiOperation(value = "创建惯性货位记录表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<WmsPfepInertiaLocator> create(@RequestBody WmsPfepInertiaLocator wmsPfepInertiaLocator) {
        validObject(wmsPfepInertiaLocator);
        wmsPfepInertiaLocatorRepository.insertSelective(wmsPfepInertiaLocator);
        return Results.success(wmsPfepInertiaLocator);
    }

    @ApiOperation(value = "修改惯性货位记录表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<WmsPfepInertiaLocator> update(@RequestBody WmsPfepInertiaLocator wmsPfepInertiaLocator) {
        SecurityTokenHelper.validToken(wmsPfepInertiaLocator);
        wmsPfepInertiaLocatorRepository.updateByPrimaryKeySelective(wmsPfepInertiaLocator);
        return Results.success(wmsPfepInertiaLocator);
    }

    @ApiOperation(value = "删除惯性货位记录表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody WmsPfepInertiaLocator wmsPfepInertiaLocator) {
        SecurityTokenHelper.validToken(wmsPfepInertiaLocator);
        wmsPfepInertiaLocatorRepository.deleteByPrimaryKey(wmsPfepInertiaLocator);
        return Results.success();
    }

}
