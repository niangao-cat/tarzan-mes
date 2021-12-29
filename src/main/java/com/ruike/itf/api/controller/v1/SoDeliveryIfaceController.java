package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.app.service.SoDeliveryIfaceService;
import com.ruike.itf.domain.entity.SoDeliveryIface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 销售发退货单接口表 管理 API
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-02 13:41:40
 */
@RestController("soDeliveryIfaceController.v1" )
@RequestMapping("/v1/{organizationId}/so-delivery-ifaces" )
public class SoDeliveryIfaceController extends BaseController {

    private final SoDeliveryIfaceService soDeliveryIfaceService;
    @Autowired
    public SoDeliveryIfaceController(SoDeliveryIfaceService soDeliveryIfaceService) {
        this.soDeliveryIfaceService = soDeliveryIfaceService;
    }

    @ApiOperation(value = "销售发退货单接口表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<SoDeliveryIface>> list(@PathVariable("organizationId") Long organizationId, SoDeliveryIface soDeliveryIface, @ApiIgnore @SortDefault(value = SoDeliveryIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<SoDeliveryIface> list = soDeliveryIfaceService.list(organizationId, soDeliveryIface, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "销售发退货单接口表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<SoDeliveryIface> detail(@PathVariable("organizationId") Long organizationId, @PathVariable Long ifaceId) {
        SoDeliveryIface soDeliveryIface =soDeliveryIfaceService.detail(organizationId, ifaceId);
        return Results.success(soDeliveryIface);
    }

    @ApiOperation(value = "创建销售发退货单接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<SoDeliveryIface> create(@PathVariable("organizationId") Long organizationId, @RequestBody SoDeliveryIface soDeliveryIface) {
            soDeliveryIfaceService.create(organizationId, soDeliveryIface);
        return Results.success(soDeliveryIface);
    }

    @ApiOperation(value = "修改销售发退货单接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<SoDeliveryIface> update(@PathVariable("organizationId") Long organizationId, @RequestBody SoDeliveryIface soDeliveryIface) {
        soDeliveryIfaceService.update(organizationId, soDeliveryIface);
        return Results.success(soDeliveryIface);
    }

    @ApiOperation(value = "删除销售发退货单接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody SoDeliveryIface soDeliveryIface) {
        soDeliveryIfaceService.remove(soDeliveryIface);
        return Results.success();
    }

}
