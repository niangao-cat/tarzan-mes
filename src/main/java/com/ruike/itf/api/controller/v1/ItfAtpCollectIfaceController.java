package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfAtpCollectIface;
import com.ruike.itf.domain.repository.ItfAtpCollectIfaceRepository;
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
 * 自动化测试接口表 管理 API
 *
 * @author wenzhang.yu@hand-china 2021-01-06 11:37:08
 */
@RestController("itfAtpCollectIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-atp-collect-ifaces")
public class ItfAtpCollectIfaceController extends BaseController {

    @Autowired
    private ItfAtpCollectIfaceRepository itfAtpCollectIfaceRepository;

    @ApiOperation(value = "自动化测试接口表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<ItfAtpCollectIface>> list(ItfAtpCollectIface itfAtpCollectIface, @ApiIgnore @SortDefault(value = ItfAtpCollectIface.FIELD_INTERFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfAtpCollectIface> list = itfAtpCollectIfaceRepository.pageAndSort(pageRequest, itfAtpCollectIface);
        return Results.success(list);
    }

    @ApiOperation(value = "自动化测试接口表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{interfaceId}")
    public ResponseEntity<ItfAtpCollectIface> detail(@PathVariable Long interfaceId) {
        ItfAtpCollectIface itfAtpCollectIface = itfAtpCollectIfaceRepository.selectByPrimaryKey(interfaceId);
        return Results.success(itfAtpCollectIface);
    }

    @ApiOperation(value = "创建自动化测试接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<ItfAtpCollectIface> create(@RequestBody ItfAtpCollectIface itfAtpCollectIface) {
        validObject(itfAtpCollectIface);
        itfAtpCollectIfaceRepository.insertSelective(itfAtpCollectIface);
        return Results.success(itfAtpCollectIface);
    }

    @ApiOperation(value = "修改自动化测试接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<ItfAtpCollectIface> update(@RequestBody ItfAtpCollectIface itfAtpCollectIface) {
        SecurityTokenHelper.validToken(itfAtpCollectIface);
        itfAtpCollectIfaceRepository.updateByPrimaryKeySelective(itfAtpCollectIface);
        return Results.success(itfAtpCollectIface);
    }

    @ApiOperation(value = "删除自动化测试接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfAtpCollectIface itfAtpCollectIface) {
        SecurityTokenHelper.validToken(itfAtpCollectIface);
        itfAtpCollectIfaceRepository.deleteByPrimaryKey(itfAtpCollectIface);
        return Results.success();
    }

}
