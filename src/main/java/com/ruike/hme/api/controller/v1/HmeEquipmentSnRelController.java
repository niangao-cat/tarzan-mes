package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeEquipmentSnRel;
import com.ruike.hme.domain.repository.HmeEquipmentSnRelRepository;
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
 * 设备-SN接口表 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2021-03-01 15:57:17
 */
@RestController("hmeEquipmentSnRelController.v1")
@RequestMapping("/v1/{organizationId}/hme-equipment-sn-rels")
public class HmeEquipmentSnRelController extends BaseController {

    @Autowired
    private HmeEquipmentSnRelRepository hmeEquipmentSnRelRepository;

    @ApiOperation(value = "设备-SN接口表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeEquipmentSnRel>> list(HmeEquipmentSnRel hmeEquipmentSnRel, @ApiIgnore @SortDefault(value = HmeEquipmentSnRel.FIELD_TENANT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeEquipmentSnRel> list = hmeEquipmentSnRelRepository.pageAndSort(pageRequest, hmeEquipmentSnRel);
        return Results.success(list);
    }

    @ApiOperation(value = "设备-SN接口表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{tenantId}")
    public ResponseEntity<HmeEquipmentSnRel> detail(@PathVariable Long tenantId) {
        HmeEquipmentSnRel hmeEquipmentSnRel = hmeEquipmentSnRelRepository.selectByPrimaryKey(tenantId);
        return Results.success(hmeEquipmentSnRel);
    }

    @ApiOperation(value = "创建设备-SN接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeEquipmentSnRel> create(@RequestBody HmeEquipmentSnRel hmeEquipmentSnRel) {
        validObject(hmeEquipmentSnRel);
        hmeEquipmentSnRelRepository.insertSelective(hmeEquipmentSnRel);
        return Results.success(hmeEquipmentSnRel);
    }

    @ApiOperation(value = "修改设备-SN接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeEquipmentSnRel> update(@RequestBody HmeEquipmentSnRel hmeEquipmentSnRel) {
        SecurityTokenHelper.validToken(hmeEquipmentSnRel);
        hmeEquipmentSnRelRepository.updateByPrimaryKeySelective(hmeEquipmentSnRel);
        return Results.success(hmeEquipmentSnRel);
    }

    @ApiOperation(value = "删除设备-SN接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeEquipmentSnRel hmeEquipmentSnRel) {
        SecurityTokenHelper.validToken(hmeEquipmentSnRel);
        hmeEquipmentSnRelRepository.deleteByPrimaryKey(hmeEquipmentSnRel);
        return Results.success();
    }

}
