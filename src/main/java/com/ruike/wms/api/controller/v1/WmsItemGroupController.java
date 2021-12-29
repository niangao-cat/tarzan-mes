package com.ruike.wms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsItemGroup;
import com.ruike.wms.domain.repository.WmsItemGroupRepository;
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
 * 物料组表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-07-17 10:12:56
 */
@RestController("wmsItemGroupController.v1")
@RequestMapping("/v1/{organizationId}/wms-item-groups")
public class WmsItemGroupController extends BaseController {

    @Autowired
    private WmsItemGroupRepository wmsItemGroupRepository;

    @ApiOperation(value = "物料组表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsItemGroup>> list(WmsItemGroup wmsItemGroup, @ApiIgnore @SortDefault(value = WmsItemGroup.FIELD_ITEM_GROUP_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WmsItemGroup> list = wmsItemGroupRepository.pageAndSort(pageRequest, wmsItemGroup);
        return Results.success(list);
    }

    @ApiOperation(value = "物料组表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{itemGroupId}")
    public ResponseEntity<WmsItemGroup> detail(@PathVariable Long itemGroupId) {
        WmsItemGroup wmsItemGroup = wmsItemGroupRepository.selectByPrimaryKey(itemGroupId);
        return Results.success(wmsItemGroup);
    }

    @ApiOperation(value = "创建物料组表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<WmsItemGroup> create(@RequestBody WmsItemGroup wmsItemGroup) {
        validObject(wmsItemGroup);
        wmsItemGroupRepository.insertSelective(wmsItemGroup);
        return Results.success(wmsItemGroup);
    }

    @ApiOperation(value = "修改物料组表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<WmsItemGroup> update(@RequestBody WmsItemGroup wmsItemGroup) {
        SecurityTokenHelper.validToken(wmsItemGroup);
        wmsItemGroupRepository.updateByPrimaryKeySelective(wmsItemGroup);
        return Results.success(wmsItemGroup);
    }

    @ApiOperation(value = "删除物料组表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody WmsItemGroup wmsItemGroup) {
        SecurityTokenHelper.validToken(wmsItemGroup);
        wmsItemGroupRepository.deleteByPrimaryKey(wmsItemGroup);
        return Results.success();
    }

}
