package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfBneCollectIface;
import com.ruike.itf.domain.repository.ItfBneCollectIfaceRepository;
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
 * BNE数据采集接口表 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2020-09-12 13:59:43
 */
@RestController("itfBneCollectIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-bne-collect-ifaces")
public class ItfBneCollectIfaceController extends BaseController {

    @Autowired
    private ItfBneCollectIfaceRepository itfBneCollectIfaceRepository;

    @ApiOperation(value = "BNE数据采集接口表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<ItfBneCollectIface>> list(ItfBneCollectIface itfBneCollectIface, @ApiIgnore @SortDefault(value = ItfBneCollectIface.FIELD_INTERFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfBneCollectIface> list = itfBneCollectIfaceRepository.pageAndSort(pageRequest, itfBneCollectIface);
        return Results.success(list);
    }

    @ApiOperation(value = "BNE数据采集接口表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{interfaceId}")
    public ResponseEntity<ItfBneCollectIface> detail(@PathVariable Long interfaceId) {
        ItfBneCollectIface itfBneCollectIface = itfBneCollectIfaceRepository.selectByPrimaryKey(interfaceId);
        return Results.success(itfBneCollectIface);
    }

    @ApiOperation(value = "创建BNE数据采集接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<ItfBneCollectIface> create(@RequestBody ItfBneCollectIface itfBneCollectIface) {
        validObject(itfBneCollectIface);
        itfBneCollectIfaceRepository.insertSelective(itfBneCollectIface);
        return Results.success(itfBneCollectIface);
    }

    @ApiOperation(value = "修改BNE数据采集接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<ItfBneCollectIface> update(@RequestBody ItfBneCollectIface itfBneCollectIface) {
        SecurityTokenHelper.validToken(itfBneCollectIface);
        itfBneCollectIfaceRepository.updateByPrimaryKeySelective(itfBneCollectIface);
        return Results.success(itfBneCollectIface);
    }

    @ApiOperation(value = "删除BNE数据采集接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfBneCollectIface itfBneCollectIface) {
        SecurityTokenHelper.validToken(itfBneCollectIface);
        itfBneCollectIfaceRepository.deleteByPrimaryKey(itfBneCollectIface);
        return Results.success();
    }

}
