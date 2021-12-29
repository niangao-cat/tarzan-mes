package com.ruike.wms.api.controller.v1;

import com.ruike.wms.app.service.WmsInstructionSnRelService;
import com.ruike.wms.domain.entity.WmsInstructionSnRel;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
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
 * 单据SN指定表 管理 API
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-06 11:16:18
 */
@RestController("WmsInstructionSnRelController.v1" )
@RequestMapping("/v1/{organizationId}/instruction-sn-rels" )
public class WmsInstructionSnRelController extends BaseController {

    private final WmsInstructionSnRelService wmsInstructionSnRelService;
    @Autowired
    public WmsInstructionSnRelController(WmsInstructionSnRelService wmsInstructionSnRelService) {
        this.wmsInstructionSnRelService = wmsInstructionSnRelService;
    }

    @ApiOperation(value = "单据SN指定表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsInstructionSnRel>> list(@PathVariable("organizationId") Long organizationId, WmsInstructionSnRel wmsInstructionSnRel, @ApiIgnore @SortDefault(value = WmsInstructionSnRel.FIELD_WMS_INSTRUCTION_SN_REL_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WmsInstructionSnRel> list = wmsInstructionSnRelService.list(organizationId, wmsInstructionSnRel, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "单据SN指定表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{wmsInstructionSnRelId}")
    public ResponseEntity<WmsInstructionSnRel> detail(@PathVariable("organizationId") Long organizationId, @PathVariable Long wmsInstructionSnRelId) {
        WmsInstructionSnRel wmsInstructionSnRel = wmsInstructionSnRelService.detail(organizationId, wmsInstructionSnRelId);
        return Results.success(wmsInstructionSnRel);
    }

    @ApiOperation(value = "创建单据SN指定表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<WmsInstructionSnRel> create(@PathVariable("organizationId") Long organizationId, @RequestBody WmsInstructionSnRel wmsInstructionSnRel) {
            wmsInstructionSnRelService.create(organizationId, wmsInstructionSnRel);
        return Results.success(wmsInstructionSnRel);
    }

    @ApiOperation(value = "修改单据SN指定表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<WmsInstructionSnRel> update(@PathVariable("organizationId") Long organizationId, @RequestBody WmsInstructionSnRel wmsInstructionSnRel) {
        wmsInstructionSnRelService.update(organizationId, wmsInstructionSnRel);
        return Results.success(wmsInstructionSnRel);
    }

    @ApiOperation(value = "删除单据SN指定表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody WmsInstructionSnRel wmsInstructionSnRel) {
        wmsInstructionSnRelService.remove(wmsInstructionSnRel);
        return Results.success();
    }

}
