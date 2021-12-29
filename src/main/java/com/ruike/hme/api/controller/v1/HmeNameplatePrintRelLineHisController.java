package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.app.service.HmeNameplatePrintRelLineHisService;
import com.ruike.hme.domain.entity.HmeNameplatePrintRelLineHis;
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
 * 铭牌打印内部识别码对应关系行历史表 管理 API
 *
 * @author wengang.qiang@hand-chian.com 2021-10-12 10:56:14
 */
@RestController("hmeNameplatePrintRelLineHisController.v1" )
@RequestMapping("/v1/{organizationId}/hme-nameplate-print-rel-line-hiss" )
public class HmeNameplatePrintRelLineHisController extends BaseController {

    private final HmeNameplatePrintRelLineHisService hmeNameplatePrintRelLineHisService;
    @Autowired
    public HmeNameplatePrintRelLineHisController(HmeNameplatePrintRelLineHisService hmeNameplatePrintRelLineHisService) {
        this.hmeNameplatePrintRelLineHisService = hmeNameplatePrintRelLineHisService;
    }

    @ApiOperation(value = "铭牌打印内部识别码对应关系行历史表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeNameplatePrintRelLineHis>> list(@PathVariable("organizationId") Long organizationId, HmeNameplatePrintRelLineHis hmeNameplatePrintRelLineHis, @ApiIgnore @SortDefault(value = HmeNameplatePrintRelLineHis.FIELD_NAMEPLATE_LINE_HIS_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeNameplatePrintRelLineHis> list = hmeNameplatePrintRelLineHisService.list(organizationId, hmeNameplatePrintRelLineHis, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "铭牌打印内部识别码对应关系行历史表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{nameplateLineHisId}")
    public ResponseEntity<HmeNameplatePrintRelLineHis> detail(@PathVariable("organizationId") Long organizationId, @PathVariable Long nameplateLineHisId) {
        HmeNameplatePrintRelLineHis hmeNameplatePrintRelLineHis =hmeNameplatePrintRelLineHisService.detail(organizationId, nameplateLineHisId);
        return Results.success(hmeNameplatePrintRelLineHis);
    }

    @ApiOperation(value = "创建铭牌打印内部识别码对应关系行历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeNameplatePrintRelLineHis> create(@PathVariable("organizationId") Long organizationId, @RequestBody HmeNameplatePrintRelLineHis hmeNameplatePrintRelLineHis) {
            hmeNameplatePrintRelLineHisService.create(organizationId, hmeNameplatePrintRelLineHis);
        return Results.success(hmeNameplatePrintRelLineHis);
    }

    @ApiOperation(value = "修改铭牌打印内部识别码对应关系行历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeNameplatePrintRelLineHis> update(@PathVariable("organizationId") Long organizationId, @RequestBody HmeNameplatePrintRelLineHis hmeNameplatePrintRelLineHis) {
        hmeNameplatePrintRelLineHisService.update(organizationId, hmeNameplatePrintRelLineHis);
        return Results.success(hmeNameplatePrintRelLineHis);
    }

    @ApiOperation(value = "删除铭牌打印内部识别码对应关系行历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeNameplatePrintRelLineHis hmeNameplatePrintRelLineHis) {
        hmeNameplatePrintRelLineHisService.remove(hmeNameplatePrintRelLineHis);
        return Results.success();
    }

}
