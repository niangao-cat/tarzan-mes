package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.app.service.HmeNameplatePrintRelHeaderHisService;
import com.ruike.hme.domain.entity.HmeNameplatePrintRelHeaderHis;
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
 * 铭牌打印内部识别码对应关系头历史表 管理 API
 *
 * @author wengang.qiang@hand-chian.com 2021-10-12 10:56:12
 */
@RestController("hmeNameplatePrintRelHeaderHisController.v1" )
@RequestMapping("/v1/{organizationId}/hme-nameplate-print-rel-header-hiss" )
public class HmeNameplatePrintRelHeaderHisController extends BaseController {

    private final HmeNameplatePrintRelHeaderHisService hmeNameplatePrintRelHeaderHisService;
    @Autowired
    public HmeNameplatePrintRelHeaderHisController(HmeNameplatePrintRelHeaderHisService hmeNameplatePrintRelHeaderHisService) {
        this.hmeNameplatePrintRelHeaderHisService = hmeNameplatePrintRelHeaderHisService;
    }

    @ApiOperation(value = "铭牌打印内部识别码对应关系头历史表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeNameplatePrintRelHeaderHis>> list(@PathVariable("organizationId") Long organizationId, HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis, @ApiIgnore @SortDefault(value = HmeNameplatePrintRelHeaderHis.FIELD_NAMEPLATE_HEADER_HIS_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeNameplatePrintRelHeaderHis> list = hmeNameplatePrintRelHeaderHisService.list(organizationId, hmeNameplatePrintRelHeaderHis, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "铭牌打印内部识别码对应关系头历史表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{nameplateHeaderHisId}")
    public ResponseEntity<HmeNameplatePrintRelHeaderHis> detail(@PathVariable("organizationId") Long organizationId, @PathVariable Long nameplateHeaderHisId) {
        HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis =hmeNameplatePrintRelHeaderHisService.detail(organizationId, nameplateHeaderHisId);
        return Results.success(hmeNameplatePrintRelHeaderHis);
    }

    @ApiOperation(value = "创建铭牌打印内部识别码对应关系头历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeNameplatePrintRelHeaderHis> create(@PathVariable("organizationId") Long organizationId, @RequestBody HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis) {
            hmeNameplatePrintRelHeaderHisService.create(organizationId, hmeNameplatePrintRelHeaderHis);
        return Results.success(hmeNameplatePrintRelHeaderHis);
    }

    @ApiOperation(value = "修改铭牌打印内部识别码对应关系头历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeNameplatePrintRelHeaderHis> update(@PathVariable("organizationId") Long organizationId, @RequestBody HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis) {
        hmeNameplatePrintRelHeaderHisService.update(organizationId, hmeNameplatePrintRelHeaderHis);
        return Results.success(hmeNameplatePrintRelHeaderHis);
    }

    @ApiOperation(value = "删除铭牌打印内部识别码对应关系头历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis) {
        hmeNameplatePrintRelHeaderHisService.remove(hmeNameplatePrintRelHeaderHis);
        return Results.success();
    }

}
