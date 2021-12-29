package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfOperationComponentIface;
import com.ruike.itf.domain.repository.ItfOperationComponentIfaceRepository;
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
 * 工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口） 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@RestController("itfOperationComponentIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-operation-component-ifaces")
public class ItfOperationComponentIfaceController extends BaseController {

    @Autowired
    private ItfOperationComponentIfaceRepository itfOperationComponentIfaceRepository;

    @ApiOperation(value = "工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口）列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<ItfOperationComponentIface>> list(ItfOperationComponentIface itfOperationComponentIface, @ApiIgnore @SortDefault(value = ItfOperationComponentIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfOperationComponentIface> list = itfOperationComponentIfaceRepository.pageAndSort(pageRequest, itfOperationComponentIface);
        return Results.success(list);
    }

    @ApiOperation(value = "工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口）明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<ItfOperationComponentIface> detail(@PathVariable Long ifaceId) {
        ItfOperationComponentIface itfOperationComponentIface = itfOperationComponentIfaceRepository.selectByPrimaryKey(ifaceId);
        return Results.success(itfOperationComponentIface);
    }

    @ApiOperation(value = "创建工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口）")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<ItfOperationComponentIface> create(@RequestBody ItfOperationComponentIface itfOperationComponentIface) {
        validObject(itfOperationComponentIface);
        itfOperationComponentIfaceRepository.insertSelective(itfOperationComponentIface);
        return Results.success(itfOperationComponentIface);
    }

    @ApiOperation(value = "修改工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口）")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<ItfOperationComponentIface> update(@RequestBody ItfOperationComponentIface itfOperationComponentIface) {
        SecurityTokenHelper.validToken(itfOperationComponentIface);
        itfOperationComponentIfaceRepository.updateByPrimaryKeySelective(itfOperationComponentIface);
        return Results.success(itfOperationComponentIface);
    }

    @ApiOperation(value = "删除工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口）")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfOperationComponentIface itfOperationComponentIface) {
        SecurityTokenHelper.validToken(itfOperationComponentIface);
        itfOperationComponentIfaceRepository.deleteByPrimaryKey(itfOperationComponentIface);
        return Results.success();
    }

}
