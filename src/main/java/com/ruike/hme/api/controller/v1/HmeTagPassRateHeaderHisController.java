package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.app.service.HmeTagPassRateHeaderHisService;
import com.ruike.hme.domain.entity.HmeTagPassRateHeaderHis;
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
 * 偏振度和发散角良率维护头历史表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:36
 */
@RestController("hmeTagPassRateHeaderHisController.v1" )
@RequestMapping("/v1/{organizationId}/hme-tag-pass-rate-header-hiss" )
public class HmeTagPassRateHeaderHisController extends BaseController {

    private final HmeTagPassRateHeaderHisService hmeTagPassRateHeaderHisService;
    public HmeTagPassRateHeaderHisController(HmeTagPassRateHeaderHisService hmeTagPassRateHeaderHisService) {
        this.hmeTagPassRateHeaderHisService = hmeTagPassRateHeaderHisService;
    }

    @ApiOperation(value = "偏振度和发散角良率维护头历史表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeTagPassRateHeaderHis>> list(@PathVariable("organizationId") Long organizationId, HmeTagPassRateHeaderHis hmeTagPassRateHeaderHis, @ApiIgnore @SortDefault(value = HmeTagPassRateHeaderHis.FIELD_HEADER_HIS_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeTagPassRateHeaderHis> list = hmeTagPassRateHeaderHisService.list(organizationId, hmeTagPassRateHeaderHis, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "偏振度和发散角良率维护头历史表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{headerHisId}")
    public ResponseEntity<HmeTagPassRateHeaderHis> detail(@PathVariable("organizationId") Long organizationId, @PathVariable Long headerHisId) {
        HmeTagPassRateHeaderHis hmeTagPassRateHeaderHis =hmeTagPassRateHeaderHisService.detail(organizationId, headerHisId);
        return Results.success(hmeTagPassRateHeaderHis);
    }

    @ApiOperation(value = "创建偏振度和发散角良率维护头历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeTagPassRateHeaderHis> create(@PathVariable("organizationId") Long organizationId, @RequestBody HmeTagPassRateHeaderHis hmeTagPassRateHeaderHis) {
            hmeTagPassRateHeaderHisService.create(organizationId, hmeTagPassRateHeaderHis);
        return Results.success(hmeTagPassRateHeaderHis);
    }

    @ApiOperation(value = "修改偏振度和发散角良率维护头历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeTagPassRateHeaderHis> update(@PathVariable("organizationId") Long organizationId, @RequestBody HmeTagPassRateHeaderHis hmeTagPassRateHeaderHis) {
        hmeTagPassRateHeaderHisService.update(organizationId, hmeTagPassRateHeaderHis);
        return Results.success(hmeTagPassRateHeaderHis);
    }

    @ApiOperation(value = "删除偏振度和发散角良率维护头历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeTagPassRateHeaderHis hmeTagPassRateHeaderHis) {
        hmeTagPassRateHeaderHisService.remove(hmeTagPassRateHeaderHis);
        return Results.success();
    }

}
