package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.app.service.WcsTaskLineIfaceService;
import com.ruike.itf.domain.entity.WcsTaskLineIface;
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
 * 成品出库指令信息接口行表 管理 API
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-06 11:04:14
 */
@RestController("wcsTaskLineIfaceController.v1" )
@RequestMapping("/v1/{organizationId}/wcs-task-line-ifaces" )
public class WcsTaskLineIfaceController extends BaseController {

    private final WcsTaskLineIfaceService wcsTaskLineIfaceService;
    @Autowired
    public WcsTaskLineIfaceController(WcsTaskLineIfaceService wcsTaskLineIfaceService) {
        this.wcsTaskLineIfaceService = wcsTaskLineIfaceService;
    }

    @ApiOperation(value = "成品出库指令信息接口行表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WcsTaskLineIface>> list(@PathVariable("organizationId") Long organizationId, WcsTaskLineIface wcsTaskLineIface, @ApiIgnore @SortDefault(value = WcsTaskLineIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WcsTaskLineIface> list = wcsTaskLineIfaceService.list(organizationId, wcsTaskLineIface, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "成品出库指令信息接口行表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<WcsTaskLineIface> detail(@PathVariable("organizationId") Long organizationId, @PathVariable Long ifaceId) {
        WcsTaskLineIface wcsTaskLineIface =wcsTaskLineIfaceService.detail(organizationId, ifaceId);
        return Results.success(wcsTaskLineIface);
    }

    @ApiOperation(value = "创建成品出库指令信息接口行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<WcsTaskLineIface> create(@PathVariable("organizationId") Long organizationId, @RequestBody WcsTaskLineIface wcsTaskLineIface) {
            wcsTaskLineIfaceService.create(organizationId, wcsTaskLineIface);
        return Results.success(wcsTaskLineIface);
    }

    @ApiOperation(value = "修改成品出库指令信息接口行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<WcsTaskLineIface> update(@PathVariable("organizationId") Long organizationId, @RequestBody WcsTaskLineIface wcsTaskLineIface) {
        wcsTaskLineIfaceService.update(organizationId, wcsTaskLineIface);
        return Results.success(wcsTaskLineIface);
    }

    @ApiOperation(value = "删除成品出库指令信息接口行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody WcsTaskLineIface wcsTaskLineIface) {
        wcsTaskLineIfaceService.remove(wcsTaskLineIface);
        return Results.success();
    }

}
