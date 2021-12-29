package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.repository.HmeMaterialLotLoadRepository;
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
 * 来料装载位置表 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2020-08-13 20:22:29
 */
@RestController("hmeMaterialLotLoadController.v1")
@RequestMapping("/v1/{organizationId}/hme-material-lot-loads")
public class HmeMaterialLotLoadController extends BaseController {

    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;

    @ApiOperation(value = "来料装载位置表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeMaterialLotLoad>> list(HmeMaterialLotLoad hmeMaterialLotLoad, @ApiIgnore @SortDefault(value = HmeMaterialLotLoad.FIELD_MATERIAL_LOT_LOAD_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeMaterialLotLoad> list = hmeMaterialLotLoadRepository.pageAndSort(pageRequest, hmeMaterialLotLoad);
        return Results.success(list);
    }

    @ApiOperation(value = "来料装载位置表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{materialLotLoadId}")
    public ResponseEntity<HmeMaterialLotLoad> detail(@PathVariable Long materialLotLoadId) {
        HmeMaterialLotLoad hmeMaterialLotLoad = hmeMaterialLotLoadRepository.selectByPrimaryKey(materialLotLoadId);
        return Results.success(hmeMaterialLotLoad);
    }

    @ApiOperation(value = "创建来料装载位置表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeMaterialLotLoad> create(@RequestBody HmeMaterialLotLoad hmeMaterialLotLoad) {
        validObject(hmeMaterialLotLoad);
        hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
        return Results.success(hmeMaterialLotLoad);
    }

    @ApiOperation(value = "修改来料装载位置表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeMaterialLotLoad> update(@RequestBody HmeMaterialLotLoad hmeMaterialLotLoad) {
        SecurityTokenHelper.validToken(hmeMaterialLotLoad);
        hmeMaterialLotLoadRepository.updateByPrimaryKeySelective(hmeMaterialLotLoad);
        return Results.success(hmeMaterialLotLoad);
    }

    @ApiOperation(value = "删除来料装载位置表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeMaterialLotLoad hmeMaterialLotLoad) {
        SecurityTokenHelper.validToken(hmeMaterialLotLoad);
        hmeMaterialLotLoadRepository.deleteByPrimaryKey(hmeMaterialLotLoad);
        return Results.success();
    }

}
