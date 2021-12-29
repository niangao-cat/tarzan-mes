package com.ruike.wms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsDistDemandSubstitution;
import com.ruike.wms.domain.repository.WmsDistDemandSubstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 配送需求替代关系表 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2020-08-31 21:05:13
 */
@RestController("wmsDistDemandSubstitutionController.v1")
@RequestMapping("/v1/{organizationId}/wms-dist-demand-substitutions")
public class WmsDistDemandSubstitutionController extends BaseController {

    @Autowired
    private WmsDistDemandSubstitutionRepository wmsDistDemandSubstitutionRepository;

    @ApiOperation(value = "配送需求替代关系表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsDistDemandSubstitution>> list(WmsDistDemandSubstitution wmsDistDemandSubstitution, @ApiIgnore @SortDefault(value = WmsDistDemandSubstitution.FIELD_SUBSTITUTE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WmsDistDemandSubstitution> list = wmsDistDemandSubstitutionRepository.pageAndSort(pageRequest, wmsDistDemandSubstitution);
        return Results.success(list);
    }

    @ApiOperation(value = "配送需求替代关系表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{substituteId}")
    public ResponseEntity<WmsDistDemandSubstitution> detail(@PathVariable Long substituteId) {
        WmsDistDemandSubstitution wmsDistDemandSubstitution = wmsDistDemandSubstitutionRepository.selectByPrimaryKey(substituteId);
        return Results.success(wmsDistDemandSubstitution);
    }

    @ApiOperation(value = "创建配送需求替代关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<WmsDistDemandSubstitution> create(@RequestBody WmsDistDemandSubstitution wmsDistDemandSubstitution) {
        validObject(wmsDistDemandSubstitution);
        wmsDistDemandSubstitutionRepository.insertSelective(wmsDistDemandSubstitution);
        return Results.success(wmsDistDemandSubstitution);
    }

    @ApiOperation(value = "修改配送需求替代关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<WmsDistDemandSubstitution> update(@RequestBody WmsDistDemandSubstitution wmsDistDemandSubstitution) {
        SecurityTokenHelper.validToken(wmsDistDemandSubstitution);
        wmsDistDemandSubstitutionRepository.updateByPrimaryKeySelective(wmsDistDemandSubstitution);
        return Results.success(wmsDistDemandSubstitution);
    }

    @ApiOperation(value = "删除配送需求替代关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody WmsDistDemandSubstitution wmsDistDemandSubstitution) {
        SecurityTokenHelper.validToken(wmsDistDemandSubstitution);
        wmsDistDemandSubstitutionRepository.deleteByPrimaryKey(wmsDistDemandSubstitution);
        return Results.success();
    }

}
