package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfWorkCenterIface;
import com.ruike.itf.domain.repository.ItfWorkCenterIfaceRepository;
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
 * 工作中心接口记录表 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-08-27 16:17:14
 */
@RestController("itfWorkCenterIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-work-center-ifaces")
public class ItfWorkCenterIfaceController extends BaseController {

    @Autowired
    private ItfWorkCenterIfaceRepository itfWorkCenterIfaceRepository;

    @ApiOperation(value = "工作中心接口记录表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<ItfWorkCenterIface>> list(ItfWorkCenterIface itfWorkCenterIface, @ApiIgnore @SortDefault(value = ItfWorkCenterIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfWorkCenterIface> list = itfWorkCenterIfaceRepository.pageAndSort(pageRequest, itfWorkCenterIface);
        return Results.success(list);
    }

    @ApiOperation(value = "工作中心接口记录表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<ItfWorkCenterIface> detail(@PathVariable Long ifaceId) {
        ItfWorkCenterIface itfWorkCenterIface = itfWorkCenterIfaceRepository.selectByPrimaryKey(ifaceId);
        return Results.success(itfWorkCenterIface);
    }

    @ApiOperation(value = "创建工作中心接口记录表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<ItfWorkCenterIface> create(@RequestBody ItfWorkCenterIface itfWorkCenterIface) {
        validObject(itfWorkCenterIface);
        itfWorkCenterIfaceRepository.insertSelective(itfWorkCenterIface);
        return Results.success(itfWorkCenterIface);
    }

    @ApiOperation(value = "修改工作中心接口记录表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<ItfWorkCenterIface> update(@RequestBody ItfWorkCenterIface itfWorkCenterIface) {
        SecurityTokenHelper.validToken(itfWorkCenterIface);
        itfWorkCenterIfaceRepository.updateByPrimaryKeySelective(itfWorkCenterIface);
        return Results.success(itfWorkCenterIface);
    }

    @ApiOperation(value = "删除工作中心接口记录表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfWorkCenterIface itfWorkCenterIface) {
        SecurityTokenHelper.validToken(itfWorkCenterIface);
        itfWorkCenterIfaceRepository.deleteByPrimaryKey(itfWorkCenterIface);
        return Results.success();
    }

}
