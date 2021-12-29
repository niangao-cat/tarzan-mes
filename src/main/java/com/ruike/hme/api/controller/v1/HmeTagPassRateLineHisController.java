package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.app.service.HmeTagPassRateLineHisService;
import com.ruike.hme.domain.entity.HmeTagPassRateLineHis;
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
 * 偏振度和发散角良率维护行历史表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:38
 */
@RestController("hmeTagPassRateLineHisController.v1")
@RequestMapping("/v1/{organizationId}/hme-tag-pass-rate-line-hiss")
public class HmeTagPassRateLineHisController extends BaseController {

    private final HmeTagPassRateLineHisService hmeTagPassRateLineHisService;

    public HmeTagPassRateLineHisController(HmeTagPassRateLineHisService hmeTagPassRateLineHisService) {
        this.hmeTagPassRateLineHisService = hmeTagPassRateLineHisService;
    }

    @ApiOperation(value = "偏振度和发散角良率维护行历史表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeTagPassRateLineHis>> list(@PathVariable("organizationId") Long organizationId, HmeTagPassRateLineHis hmeTagPassRateLineHis, @ApiIgnore @SortDefault(value = HmeTagPassRateLineHis.FIELD_LINE_HIS_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeTagPassRateLineHis> list = hmeTagPassRateLineHisService.list(organizationId, hmeTagPassRateLineHis, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "偏振度和发散角良率维护行历史表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{lineHisId}")
    public ResponseEntity<HmeTagPassRateLineHis> detail(@PathVariable("organizationId") Long organizationId, @PathVariable Long lineHisId) {
        HmeTagPassRateLineHis hmeTagPassRateLineHis = hmeTagPassRateLineHisService.detail(organizationId, lineHisId);
        return Results.success(hmeTagPassRateLineHis);
    }

    @ApiOperation(value = "创建偏振度和发散角良率维护行历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeTagPassRateLineHis> create(@PathVariable("organizationId") Long organizationId, @RequestBody HmeTagPassRateLineHis hmeTagPassRateLineHis) {
        hmeTagPassRateLineHisService.create(organizationId, hmeTagPassRateLineHis);
        return Results.success(hmeTagPassRateLineHis);
    }

    @ApiOperation(value = "修改偏振度和发散角良率维护行历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeTagPassRateLineHis> update(@PathVariable("organizationId") Long organizationId, @RequestBody HmeTagPassRateLineHis hmeTagPassRateLineHis) {
        hmeTagPassRateLineHisService.update(organizationId, hmeTagPassRateLineHis);
        return Results.success(hmeTagPassRateLineHis);
    }

    @ApiOperation(value = "删除偏振度和发散角良率维护行历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeTagPassRateLineHis hmeTagPassRateLineHis) {
        hmeTagPassRateLineHisService.remove(hmeTagPassRateLineHis);
        return Results.success();
    }

}
