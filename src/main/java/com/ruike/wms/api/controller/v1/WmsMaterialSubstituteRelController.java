package com.ruike.wms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsMaterialSubstituteRel;
import com.ruike.wms.domain.repository.WmsMaterialSubstituteRelRepository;
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
 * 物料全局替代关系表 管理 API
 *
 * @author yapeng.yao@hand-china.com 2020-08-19 16:54:02
 */
@RestController("wmsMaterialSubstituteRelSiteController.v1")
@RequestMapping("/v1/wms-material-substitute-rels")
public class WmsMaterialSubstituteRelController extends BaseController {

    @Autowired
    private WmsMaterialSubstituteRelRepository wmsMaterialSubstituteRelRepository;

    @ApiOperation(value = "物料全局替代关系表列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<WmsMaterialSubstituteRel>> list(WmsMaterialSubstituteRel wmsMaterialSubstituteRel, @ApiIgnore @SortDefault(value = WmsMaterialSubstituteRel.FIELD_SUBSTITUTE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WmsMaterialSubstituteRel> list = wmsMaterialSubstituteRelRepository.pageAndSort(pageRequest, wmsMaterialSubstituteRel);
        return Results.success(list);
    }

    @ApiOperation(value = "物料全局替代关系表明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{substituteId}")
    public ResponseEntity<WmsMaterialSubstituteRel> detail(@PathVariable Long substituteId) {
        WmsMaterialSubstituteRel wmsMaterialSubstituteRel = wmsMaterialSubstituteRelRepository.selectByPrimaryKey(substituteId);
        return Results.success(wmsMaterialSubstituteRel);
    }

    @ApiOperation(value = "创建物料全局替代关系表")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<WmsMaterialSubstituteRel> create(@RequestBody WmsMaterialSubstituteRel wmsMaterialSubstituteRel) {
        validObject(wmsMaterialSubstituteRel);
        wmsMaterialSubstituteRelRepository.insertSelective(wmsMaterialSubstituteRel);
        return Results.success(wmsMaterialSubstituteRel);
    }

    @ApiOperation(value = "修改物料全局替代关系表")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<WmsMaterialSubstituteRel> update(@RequestBody WmsMaterialSubstituteRel wmsMaterialSubstituteRel) {
        SecurityTokenHelper.validToken(wmsMaterialSubstituteRel);
        wmsMaterialSubstituteRelRepository.updateByPrimaryKeySelective(wmsMaterialSubstituteRel);
        return Results.success(wmsMaterialSubstituteRel);
    }

    @ApiOperation(value = "删除物料全局替代关系表")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody WmsMaterialSubstituteRel wmsMaterialSubstituteRel) {
        SecurityTokenHelper.validToken(wmsMaterialSubstituteRel);
        wmsMaterialSubstituteRelRepository.deleteByPrimaryKey(wmsMaterialSubstituteRel);
        return Results.success();
    }

}
