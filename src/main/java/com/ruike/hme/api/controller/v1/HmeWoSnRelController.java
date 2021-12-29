package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeWoSnRel;
import com.ruike.hme.domain.repository.HmeWoSnRelRepository;
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
 * 生产订单SN码关系表 管理 API
 *
 * @author penglin.sui@hand-china.com 2020-08-14 17:31:42
 */
@RestController("hmeWoSnRelController.v1")
@RequestMapping("/v1/{organizationId}/hme-wo-sn-rels")
public class HmeWoSnRelController extends BaseController {

    @Autowired
    private HmeWoSnRelRepository hmeWoSnRelRepository;

    @ApiOperation(value = "生产订单SN码关系表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeWoSnRel>> list(HmeWoSnRel hmeWoSnRel, @ApiIgnore @SortDefault(value = HmeWoSnRel.FIELD_REL_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeWoSnRel> list = hmeWoSnRelRepository.pageAndSort(pageRequest, hmeWoSnRel);
        return Results.success(list);
    }

    @ApiOperation(value = "生产订单SN码关系表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{relId}")
    public ResponseEntity<HmeWoSnRel> detail(@PathVariable Long relId) {
        HmeWoSnRel hmeWoSnRel = hmeWoSnRelRepository.selectByPrimaryKey(relId);
        return Results.success(hmeWoSnRel);
    }

    @ApiOperation(value = "创建生产订单SN码关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeWoSnRel> create(@RequestBody HmeWoSnRel hmeWoSnRel) {
        validObject(hmeWoSnRel);
        hmeWoSnRelRepository.insertSelective(hmeWoSnRel);
        return Results.success(hmeWoSnRel);
    }

    @ApiOperation(value = "修改生产订单SN码关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeWoSnRel> update(@RequestBody HmeWoSnRel hmeWoSnRel) {
        SecurityTokenHelper.validToken(hmeWoSnRel);
        hmeWoSnRelRepository.updateByPrimaryKeySelective(hmeWoSnRel);
        return Results.success(hmeWoSnRel);
    }

    @ApiOperation(value = "删除生产订单SN码关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeWoSnRel hmeWoSnRel) {
        SecurityTokenHelper.validToken(hmeWoSnRel);
        hmeWoSnRelRepository.deleteByPrimaryKey(hmeWoSnRel);
        return Results.success();
    }

}
