package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.app.service.ItfCosMonitorIfaceService;
import com.ruike.itf.domain.entity.ItfCosMonitorIface;
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
 * COS良率监控接口表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-09-30 14:14:20
 */
@RestController("itfCosMonitorIfaceController.v1" )
@RequestMapping("/v1/{organizationId}/itf-cos-monitor-ifaces" )
public class ItfCosMonitorIfaceController extends BaseController {

    private final ItfCosMonitorIfaceService itfCosMonitorIfaceService;
    @Autowired
    public ItfCosMonitorIfaceController(ItfCosMonitorIfaceService itfCosMonitorIfaceService) {
        this.itfCosMonitorIfaceService = itfCosMonitorIfaceService;
    }

    @ApiOperation(value = "COS良率监控接口表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<ItfCosMonitorIface>> list(@PathVariable("organizationId") Long organizationId, ItfCosMonitorIface itfCosMonitorIface, @ApiIgnore @SortDefault(value = ItfCosMonitorIface.FIELD_COS_MONITOR_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfCosMonitorIface> list = itfCosMonitorIfaceService.list(organizationId, itfCosMonitorIface, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "COS良率监控接口表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{cosMonitorIfaceId}")
    public ResponseEntity<ItfCosMonitorIface> detail(@PathVariable("organizationId") Long organizationId, @PathVariable Long cosMonitorIfaceId) {
        ItfCosMonitorIface itfCosMonitorIface =itfCosMonitorIfaceService.detail(organizationId, cosMonitorIfaceId);
        return Results.success(itfCosMonitorIface);
    }

    @ApiOperation(value = "创建COS良率监控接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<ItfCosMonitorIface> create(@PathVariable("organizationId") Long organizationId, @RequestBody ItfCosMonitorIface itfCosMonitorIface) {
            itfCosMonitorIfaceService.create(organizationId, itfCosMonitorIface);
        return Results.success(itfCosMonitorIface);
    }

    @ApiOperation(value = "修改COS良率监控接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<ItfCosMonitorIface> update(@PathVariable("organizationId") Long organizationId, @RequestBody ItfCosMonitorIface itfCosMonitorIface) {
        itfCosMonitorIfaceService.update(organizationId, itfCosMonitorIface);
        return Results.success(itfCosMonitorIface);
    }

    @ApiOperation(value = "删除COS良率监控接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfCosMonitorIface itfCosMonitorIface) {
        itfCosMonitorIfaceService.remove(itfCosMonitorIface);
        return Results.success();
    }

}
