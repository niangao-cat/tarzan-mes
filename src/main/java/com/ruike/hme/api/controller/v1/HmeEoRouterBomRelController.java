package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeEoRouterBomRel;
import com.ruike.hme.domain.repository.HmeEoRouterBomRelRepository;
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
 * EO 工艺路线 装配清单关系表 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-12-29 20:14:41
 */
@RestController("hmeEoRouterBomRelController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-router-bom-rels")
public class HmeEoRouterBomRelController extends BaseController {

    @Autowired
    private HmeEoRouterBomRelRepository hmeEoRouterBomRelRepository;

    @ApiOperation(value = "EO 工艺路线 装配清单关系表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeEoRouterBomRel>> list(HmeEoRouterBomRel hmeEoRouterBomRel, @ApiIgnore @SortDefault(value = HmeEoRouterBomRel.FIELD_EO_ROUTER_BOM_REL_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeEoRouterBomRel> list = hmeEoRouterBomRelRepository.pageAndSort(pageRequest, hmeEoRouterBomRel);
        return Results.success(list);
    }

    @ApiOperation(value = "EO 工艺路线 装配清单关系表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{eoRouterBomRelId}")
    public ResponseEntity<HmeEoRouterBomRel> detail(@PathVariable Long eoRouterBomRelId) {
        HmeEoRouterBomRel hmeEoRouterBomRel = hmeEoRouterBomRelRepository.selectByPrimaryKey(eoRouterBomRelId);
        return Results.success(hmeEoRouterBomRel);
    }

    @ApiOperation(value = "创建EO 工艺路线 装配清单关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeEoRouterBomRel> create(@RequestBody HmeEoRouterBomRel hmeEoRouterBomRel) {
        validObject(hmeEoRouterBomRel);
        hmeEoRouterBomRelRepository.insertSelective(hmeEoRouterBomRel);
        return Results.success(hmeEoRouterBomRel);
    }

    @ApiOperation(value = "修改EO 工艺路线 装配清单关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeEoRouterBomRel> update(@RequestBody HmeEoRouterBomRel hmeEoRouterBomRel) {
        SecurityTokenHelper.validToken(hmeEoRouterBomRel);
        hmeEoRouterBomRelRepository.updateByPrimaryKeySelective(hmeEoRouterBomRel);
        return Results.success(hmeEoRouterBomRel);
    }

    @ApiOperation(value = "删除EO 工艺路线 装配清单关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeEoRouterBomRel hmeEoRouterBomRel) {
        SecurityTokenHelper.validToken(hmeEoRouterBomRel);
        hmeEoRouterBomRelRepository.deleteByPrimaryKey(hmeEoRouterBomRel);
        return Results.success();
    }

}
