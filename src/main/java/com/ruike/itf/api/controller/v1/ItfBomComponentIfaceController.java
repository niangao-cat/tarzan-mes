package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfBomComponentIface;
import com.ruike.itf.domain.repository.ItfBomComponentIfaceRepository;
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
 * BOM接口表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@RestController("itfBomComponentIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-bom-component-ifaces")
public class ItfBomComponentIfaceController extends BaseController {

    @Autowired
    private ItfBomComponentIfaceRepository itfBomComponentIfaceRepository;

    @ApiOperation(value = "BOM接口表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<ItfBomComponentIface>> list(ItfBomComponentIface itfBomComponentIface, @ApiIgnore @SortDefault(value = ItfBomComponentIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfBomComponentIface> list = itfBomComponentIfaceRepository.pageAndSort(pageRequest, itfBomComponentIface);
        return Results.success(list);
    }

    @ApiOperation(value = "BOM接口表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<ItfBomComponentIface> detail(@PathVariable Long ifaceId) {
        ItfBomComponentIface itfBomComponentIface = itfBomComponentIfaceRepository.selectByPrimaryKey(ifaceId);
        return Results.success(itfBomComponentIface);
    }

    @ApiOperation(value = "创建BOM接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<ItfBomComponentIface> create(@RequestBody ItfBomComponentIface itfBomComponentIface) {
        validObject(itfBomComponentIface);
        itfBomComponentIfaceRepository.insertSelective(itfBomComponentIface);
        return Results.success(itfBomComponentIface);
    }

    @ApiOperation(value = "修改BOM接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<ItfBomComponentIface> update(@RequestBody ItfBomComponentIface itfBomComponentIface) {
        SecurityTokenHelper.validToken(itfBomComponentIface);
        itfBomComponentIfaceRepository.updateByPrimaryKeySelective(itfBomComponentIface);
        return Results.success(itfBomComponentIface);
    }

    @ApiOperation(value = "删除BOM接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfBomComponentIface itfBomComponentIface) {
        SecurityTokenHelper.validToken(itfBomComponentIface);
        itfBomComponentIfaceRepository.deleteByPrimaryKey(itfBomComponentIface);
        return Results.success();
    }

}
