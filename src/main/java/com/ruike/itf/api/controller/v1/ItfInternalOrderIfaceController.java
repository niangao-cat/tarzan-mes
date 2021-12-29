package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfInternalOrderIface;
import com.ruike.itf.domain.repository.ItfInternalOrderIfaceRepository;
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
 * 内部订单接口表 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-08-21 09:29:25
 */
@RestController("itfInternalOrderIfaceSiteController.v1")
@RequestMapping("/v1/itf-internal-order-ifaces")
public class ItfInternalOrderIfaceController extends BaseController {

    @Autowired
    private ItfInternalOrderIfaceRepository itfInternalOrderIfaceRepository;

    @ApiOperation(value = "内部订单接口表列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<ItfInternalOrderIface>> list(ItfInternalOrderIface itfInternalOrderIface, @ApiIgnore @SortDefault(value = ItfInternalOrderIface.FIELD_INTERNAL_ORDER_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfInternalOrderIface> list = itfInternalOrderIfaceRepository.pageAndSort(pageRequest, itfInternalOrderIface);
        return Results.success(list);
    }

    @ApiOperation(value = "内部订单接口表明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{internalOrderId}")
    public ResponseEntity<ItfInternalOrderIface> detail(@PathVariable Long internalOrderId) {
        ItfInternalOrderIface itfInternalOrderIface = itfInternalOrderIfaceRepository.selectByPrimaryKey(internalOrderId);
        return Results.success(itfInternalOrderIface);
    }

    @ApiOperation(value = "创建内部订单接口表")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<ItfInternalOrderIface> create(@RequestBody ItfInternalOrderIface itfInternalOrderIface) {
        validObject(itfInternalOrderIface);
        itfInternalOrderIfaceRepository.insertSelective(itfInternalOrderIface);
        return Results.success(itfInternalOrderIface);
    }

    @ApiOperation(value = "修改内部订单接口表")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<ItfInternalOrderIface> update(@RequestBody ItfInternalOrderIface itfInternalOrderIface) {
        SecurityTokenHelper.validToken(itfInternalOrderIface);
        itfInternalOrderIfaceRepository.updateByPrimaryKeySelective(itfInternalOrderIface);
        return Results.success(itfInternalOrderIface);
    }

    @ApiOperation(value = "删除内部订单接口表")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfInternalOrderIface itfInternalOrderIface) {
        SecurityTokenHelper.validToken(itfInternalOrderIface);
        itfInternalOrderIfaceRepository.deleteByPrimaryKey(itfInternalOrderIface);
        return Results.success();
    }

}
