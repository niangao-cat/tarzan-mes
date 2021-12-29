package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeRepairWoSnRel;
import com.ruike.hme.domain.repository.HmeRepairWoSnRelRepository;
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
 * 工单和SN关系表 管理 API
 *
 * @author kejin.liu@hand-china.com 2020-09-16 15:56:38
 */
@RestController("hmeRepairWoSnRelController.v1")
@RequestMapping("/v1/{organizationId}/hme-repair-wo-sn-rels")
public class HmeRepairWoSnRelController extends BaseController {

    @Autowired
    private HmeRepairWoSnRelRepository hmeRepairWoSnRelRepository;

    @ApiOperation(value = "工单和SN关系表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeRepairWoSnRel>> list(HmeRepairWoSnRel hmeRepairWoSnRel, @ApiIgnore @SortDefault(value = HmeRepairWoSnRel.FIELD_REL_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeRepairWoSnRel> list = hmeRepairWoSnRelRepository.pageAndSort(pageRequest, hmeRepairWoSnRel);
        return Results.success(list);
    }

    @ApiOperation(value = "工单和SN关系表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{relId}")
    public ResponseEntity<HmeRepairWoSnRel> detail(@PathVariable Long relId) {
        HmeRepairWoSnRel hmeRepairWoSnRel = hmeRepairWoSnRelRepository.selectByPrimaryKey(relId);
        return Results.success(hmeRepairWoSnRel);
    }

    @ApiOperation(value = "创建工单和SN关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeRepairWoSnRel> create(@RequestBody HmeRepairWoSnRel hmeRepairWoSnRel) {
        validObject(hmeRepairWoSnRel);
        hmeRepairWoSnRelRepository.insertSelective(hmeRepairWoSnRel);
        return Results.success(hmeRepairWoSnRel);
    }

    @ApiOperation(value = "修改工单和SN关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeRepairWoSnRel> update(@RequestBody HmeRepairWoSnRel hmeRepairWoSnRel) {
        SecurityTokenHelper.validToken(hmeRepairWoSnRel);
        hmeRepairWoSnRelRepository.updateByPrimaryKeySelective(hmeRepairWoSnRel);
        return Results.success(hmeRepairWoSnRel);
    }

    @ApiOperation(value = "删除工单和SN关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeRepairWoSnRel hmeRepairWoSnRel) {
        SecurityTokenHelper.validToken(hmeRepairWoSnRel);
        hmeRepairWoSnRelRepository.deleteByPrimaryKey(hmeRepairWoSnRel);
        return Results.success();
    }

}
