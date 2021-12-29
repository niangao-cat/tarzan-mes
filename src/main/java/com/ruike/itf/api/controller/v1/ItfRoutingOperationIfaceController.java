package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfRoutingOperationIface;
import com.ruike.itf.domain.repository.ItfRoutingOperationIfaceRepository;
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
 * 工艺路线接口表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@RestController("itfRoutingOperationIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-routing-operation-ifaces")
public class ItfRoutingOperationIfaceController extends BaseController {

    @Autowired
    private ItfRoutingOperationIfaceRepository itfRoutingOperationIfaceRepository;

    @ApiOperation(value = "工艺路线接口表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<ItfRoutingOperationIface>> list(ItfRoutingOperationIface itfRoutingOperationIface, @ApiIgnore @SortDefault(value = ItfRoutingOperationIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfRoutingOperationIface> list = itfRoutingOperationIfaceRepository.pageAndSort(pageRequest, itfRoutingOperationIface);
        return Results.success(list);
    }

    @ApiOperation(value = "工艺路线接口表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<ItfRoutingOperationIface> detail(@PathVariable Long ifaceId) {
        ItfRoutingOperationIface itfRoutingOperationIface = itfRoutingOperationIfaceRepository.selectByPrimaryKey(ifaceId);
        return Results.success(itfRoutingOperationIface);
    }

    @ApiOperation(value = "创建工艺路线接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<ItfRoutingOperationIface> create(@RequestBody ItfRoutingOperationIface itfRoutingOperationIface) {
        validObject(itfRoutingOperationIface);
        itfRoutingOperationIfaceRepository.insertSelective(itfRoutingOperationIface);
        return Results.success(itfRoutingOperationIface);
    }

    @ApiOperation(value = "修改工艺路线接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<ItfRoutingOperationIface> update(@RequestBody ItfRoutingOperationIface itfRoutingOperationIface) {
        SecurityTokenHelper.validToken(itfRoutingOperationIface);
        itfRoutingOperationIfaceRepository.updateByPrimaryKeySelective(itfRoutingOperationIface);
        return Results.success(itfRoutingOperationIface);
    }

    @ApiOperation(value = "删除工艺路线接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfRoutingOperationIface itfRoutingOperationIface) {
        SecurityTokenHelper.validToken(itfRoutingOperationIface);
        itfRoutingOperationIfaceRepository.deleteByPrimaryKey(itfRoutingOperationIface);
        return Results.success();
    }

}
