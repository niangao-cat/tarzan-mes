package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeVirtualNum;
import com.ruike.hme.domain.repository.HmeVirtualNumRepository;
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
 * 虚拟号基础表 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2020-09-28 19:47:55
 */
@RestController("hmeVirtualNumController.v1")
@RequestMapping("/v1/{organizationId}/hme-virtual-nums")
public class HmeVirtualNumController extends BaseController {

    @Autowired
    private HmeVirtualNumRepository hmeVirtualNumRepository;

    @ApiOperation(value = "虚拟号基础表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeVirtualNum>> list(HmeVirtualNum hmeVirtualNum, @ApiIgnore @SortDefault(value = HmeVirtualNum.FIELD_TENANT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeVirtualNum> list = hmeVirtualNumRepository.pageAndSort(pageRequest, hmeVirtualNum);
        return Results.success(list);
    }

    @ApiOperation(value = "虚拟号基础表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{tenantId}")
    public ResponseEntity<HmeVirtualNum> detail(@PathVariable Long tenantId) {
        HmeVirtualNum hmeVirtualNum = hmeVirtualNumRepository.selectByPrimaryKey(tenantId);
        return Results.success(hmeVirtualNum);
    }

    @ApiOperation(value = "创建虚拟号基础表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeVirtualNum> create(@RequestBody HmeVirtualNum hmeVirtualNum) {
        validObject(hmeVirtualNum);
        hmeVirtualNumRepository.insertSelective(hmeVirtualNum);
        return Results.success(hmeVirtualNum);
    }

    @ApiOperation(value = "修改虚拟号基础表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeVirtualNum> update(@RequestBody HmeVirtualNum hmeVirtualNum) {
        SecurityTokenHelper.validToken(hmeVirtualNum);
        hmeVirtualNumRepository.updateByPrimaryKeySelective(hmeVirtualNum);
        return Results.success(hmeVirtualNum);
    }

    @ApiOperation(value = "删除虚拟号基础表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeVirtualNum hmeVirtualNum) {
        SecurityTokenHelper.validToken(hmeVirtualNum);
        hmeVirtualNumRepository.deleteByPrimaryKey(hmeVirtualNum);
        return Results.success();
    }

}
