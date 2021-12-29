package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.app.service.WcsTaskIfaceService;
import com.ruike.itf.domain.entity.WcsTaskIface;
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
 * 成品出库指令信息接口表 管理 API
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-06 11:04:14
 */
@RestController("wcsTaskIfaceController.v1" )
@RequestMapping("/v1/{organizationId}/wcs-task-ifaces" )
public class WcsTaskIfaceController extends BaseController {

    private final WcsTaskIfaceService wcsTaskIfaceService;
    @Autowired
    public WcsTaskIfaceController(WcsTaskIfaceService wcsTaskIfaceService) {
        this.wcsTaskIfaceService = wcsTaskIfaceService;
    }

    @ApiOperation(value = "成品出库指令信息接口表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WcsTaskIface>> list(@PathVariable("organizationId") Long organizationId, WcsTaskIface wcsTaskIface, @ApiIgnore @SortDefault(value = WcsTaskIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WcsTaskIface> list = wcsTaskIfaceService.list(organizationId, wcsTaskIface, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "成品出库指令信息接口表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<WcsTaskIface> detail(@PathVariable("organizationId") Long organizationId, @PathVariable Long ifaceId) {
        WcsTaskIface wcsTaskIface =wcsTaskIfaceService.detail(organizationId, ifaceId);
        return Results.success(wcsTaskIface);
    }

    @ApiOperation(value = "创建成品出库指令信息接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<WcsTaskIface> create(@PathVariable("organizationId") Long organizationId, @RequestBody WcsTaskIface wcsTaskIface) {
            wcsTaskIfaceService.create(organizationId, wcsTaskIface);
        return Results.success(wcsTaskIface);
    }

    @ApiOperation(value = "修改成品出库指令信息接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<WcsTaskIface> update(@PathVariable("organizationId") Long organizationId, @RequestBody WcsTaskIface wcsTaskIface) {
        wcsTaskIfaceService.update(organizationId, wcsTaskIface);
        return Results.success(wcsTaskIface);
    }

    @ApiOperation(value = "删除成品出库指令信息接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody WcsTaskIface wcsTaskIface) {
        wcsTaskIfaceService.remove(wcsTaskIface);
        return Results.success();
    }

}
