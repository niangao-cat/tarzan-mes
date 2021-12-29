package com.ruike.wms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsInternalOrder;
import com.ruike.wms.domain.repository.WmsInternalOrderRepository;
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
 * 内部订单表 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-08-21 09:30:34
 */
@RestController("wmsInternalOrderController.v1")
@RequestMapping("/v1/{organizationId}/wms-internal-orders")
public class WmsInternalOrderController extends BaseController {

    @Autowired
    private WmsInternalOrderRepository wmsInternalOrderRepository;

    @ApiOperation(value = "内部订单表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsInternalOrder>> list(WmsInternalOrder wmsInternalOrder, @ApiIgnore @SortDefault(value = WmsInternalOrder.FIELD_INTERNAL_ORDER_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WmsInternalOrder> list = wmsInternalOrderRepository.pageAndSort(pageRequest, wmsInternalOrder);
        return Results.success(list);
    }

    @ApiOperation(value = "内部订单表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{internalOrderId}")
    public ResponseEntity<WmsInternalOrder> detail(@PathVariable Long internalOrderId) {
        WmsInternalOrder wmsInternalOrder = wmsInternalOrderRepository.selectByPrimaryKey(internalOrderId);
        return Results.success(wmsInternalOrder);
    }

    @ApiOperation(value = "创建内部订单表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<WmsInternalOrder> create(@RequestBody WmsInternalOrder wmsInternalOrder) {
        validObject(wmsInternalOrder);
        wmsInternalOrderRepository.insertSelective(wmsInternalOrder);
        return Results.success(wmsInternalOrder);
    }

    @ApiOperation(value = "修改内部订单表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<WmsInternalOrder> update(@RequestBody WmsInternalOrder wmsInternalOrder) {
        SecurityTokenHelper.validToken(wmsInternalOrder);
        wmsInternalOrderRepository.updateByPrimaryKeySelective(wmsInternalOrder);
        return Results.success(wmsInternalOrder);
    }

    @ApiOperation(value = "删除内部订单表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody WmsInternalOrder wmsInternalOrder) {
        SecurityTokenHelper.validToken(wmsInternalOrder);
        wmsInternalOrderRepository.deleteByPrimaryKey(wmsInternalOrder);
        return Results.success();
    }

}
