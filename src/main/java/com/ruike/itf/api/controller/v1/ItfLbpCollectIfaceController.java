package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfLbpCollectIface;
import com.ruike.itf.domain.repository.ItfLbpCollectIfaceRepository;
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
 * lbp数据采集接口 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2020-09-04 16:35:53
 */
@RestController("itfLbpCollectIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-lbp-collect-ifaces")
public class ItfLbpCollectIfaceController extends BaseController {

    @Autowired
    private ItfLbpCollectIfaceRepository itfLbpCollectIfaceRepository;

    @ApiOperation(value = "lbp数据采集接口列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<ItfLbpCollectIface>> list(ItfLbpCollectIface itfLbpCollectIface, @ApiIgnore @SortDefault(value = ItfLbpCollectIface.FIELD_INTERFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfLbpCollectIface> list = itfLbpCollectIfaceRepository.pageAndSort(pageRequest, itfLbpCollectIface);
        return Results.success(list);
    }

    @ApiOperation(value = "lbp数据采集接口明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{interfaceId}")
    public ResponseEntity<ItfLbpCollectIface> detail(@PathVariable Long interfaceId) {
        ItfLbpCollectIface itfLbpCollectIface = itfLbpCollectIfaceRepository.selectByPrimaryKey(interfaceId);
        return Results.success(itfLbpCollectIface);
    }

    @ApiOperation(value = "创建lbp数据采集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<ItfLbpCollectIface> create(@RequestBody ItfLbpCollectIface itfLbpCollectIface) {
        validObject(itfLbpCollectIface);
        itfLbpCollectIfaceRepository.insertSelective(itfLbpCollectIface);
        return Results.success(itfLbpCollectIface);
    }

    @ApiOperation(value = "修改lbp数据采集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<ItfLbpCollectIface> update(@RequestBody ItfLbpCollectIface itfLbpCollectIface) {
        SecurityTokenHelper.validToken(itfLbpCollectIface);
        itfLbpCollectIfaceRepository.updateByPrimaryKeySelective(itfLbpCollectIface);
        return Results.success(itfLbpCollectIface);
    }

    @ApiOperation(value = "删除lbp数据采集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfLbpCollectIface itfLbpCollectIface) {
        SecurityTokenHelper.validToken(itfLbpCollectIface);
        itfLbpCollectIfaceRepository.deleteByPrimaryKey(itfLbpCollectIface);
        return Results.success();
    }

}
