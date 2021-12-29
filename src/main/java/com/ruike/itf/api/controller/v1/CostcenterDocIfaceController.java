package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.app.service.CostcenterDocIfaceService;
import com.ruike.itf.domain.entity.CostcenterDocIface;
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
 * 生产领退料单接口 管理 API
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-06-30 13:48:47
 */
@RestController("costcenterDocIfaceController.v1" )
@RequestMapping("/v1/{organizationId}/costcenter-doc-ifaces" )
public class CostcenterDocIfaceController extends BaseController {

    private final CostcenterDocIfaceService costcenterDocIfaceService;
    @Autowired
    public CostcenterDocIfaceController(CostcenterDocIfaceService costcenterDocIfaceService) {
        this.costcenterDocIfaceService = costcenterDocIfaceService;
    }

    @ApiOperation(value = "生产领退料单接口列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<CostcenterDocIface>> list(@PathVariable("organizationId") Long organizationId, CostcenterDocIface costcenterDocIface, @ApiIgnore @SortDefault(value = CostcenterDocIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<CostcenterDocIface> list = costcenterDocIfaceService.list(organizationId, costcenterDocIface, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "生产领退料单接口明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<CostcenterDocIface> detail(@PathVariable("organizationId") Long organizationId, @PathVariable Long ifaceId) {
        CostcenterDocIface costcenterDocIface =costcenterDocIfaceService.detail(organizationId, ifaceId);
        return Results.success(costcenterDocIface);
    }

    @ApiOperation(value = "创建生产领退料单接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<CostcenterDocIface> create(@PathVariable("organizationId") Long organizationId, @RequestBody CostcenterDocIface costcenterDocIface) {
            costcenterDocIfaceService.create(organizationId, costcenterDocIface);
        return Results.success(costcenterDocIface);
    }

    @ApiOperation(value = "修改生产领退料单接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<CostcenterDocIface> update(@PathVariable("organizationId") Long organizationId, @RequestBody CostcenterDocIface costcenterDocIface) {
        costcenterDocIfaceService.update(organizationId, costcenterDocIface);
        return Results.success(costcenterDocIface);
    }

    @ApiOperation(value = "删除生产领退料单接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody CostcenterDocIface costcenterDocIface) {
        costcenterDocIfaceService.remove(costcenterDocIface);
        return Results.success();
    }

}
