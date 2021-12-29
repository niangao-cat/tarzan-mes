package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfCosaCollectIface;
import com.ruike.itf.domain.repository.ItfCosaCollectIfaceRepository;
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
 * 芯片转移接口表 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2021-01-21 14:53:19
 */
@RestController("itfCosaCollectIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-cosa-collect-ifaces")
public class ItfCosaCollectIfaceController extends BaseController {

    @Autowired
    private ItfCosaCollectIfaceRepository itfCosaCollectIfaceRepository;

    @ApiOperation(value = "芯片转移接口表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<ItfCosaCollectIface>> list(ItfCosaCollectIface itfCosaCollectIface, @ApiIgnore @SortDefault(value = ItfCosaCollectIface.FIELD_INTERFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfCosaCollectIface> list = itfCosaCollectIfaceRepository.pageAndSort(pageRequest, itfCosaCollectIface);
        return Results.success(list);
    }

    @ApiOperation(value = "芯片转移接口表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{interfaceId}")
    public ResponseEntity<ItfCosaCollectIface> detail(@PathVariable Long interfaceId) {
        ItfCosaCollectIface itfCosaCollectIface = itfCosaCollectIfaceRepository.selectByPrimaryKey(interfaceId);
        return Results.success(itfCosaCollectIface);
    }

    @ApiOperation(value = "创建芯片转移接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<ItfCosaCollectIface> create(@RequestBody ItfCosaCollectIface itfCosaCollectIface) {
        validObject(itfCosaCollectIface);
        itfCosaCollectIfaceRepository.insertSelective(itfCosaCollectIface);
        return Results.success(itfCosaCollectIface);
    }

    @ApiOperation(value = "修改芯片转移接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<ItfCosaCollectIface> update(@RequestBody ItfCosaCollectIface itfCosaCollectIface) {
        SecurityTokenHelper.validToken(itfCosaCollectIface);
        itfCosaCollectIfaceRepository.updateByPrimaryKeySelective(itfCosaCollectIface);
        return Results.success(itfCosaCollectIface);
    }

    @ApiOperation(value = "删除芯片转移接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfCosaCollectIface itfCosaCollectIface) {
        SecurityTokenHelper.validToken(itfCosaCollectIface);
        itfCosaCollectIfaceRepository.deleteByPrimaryKey(itfCosaCollectIface);
        return Results.success();
    }

}
