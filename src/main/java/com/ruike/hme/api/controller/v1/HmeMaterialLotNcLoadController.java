package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeMaterialLotNcLoad;
import com.ruike.hme.domain.repository.HmeMaterialLotNcLoadRepository;
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
 * 不良位置 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2020-08-13 20:22:28
 */
@RestController("hmeMaterialLotNcLoadController.v1")
@RequestMapping("/v1/{organizationId}/hme-material-lot-nc-loads")
public class HmeMaterialLotNcLoadController extends BaseController {

    @Autowired
    private HmeMaterialLotNcLoadRepository hmeMaterialLotNcLoadRepository;

    @ApiOperation(value = "不良位置列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeMaterialLotNcLoad>> list(HmeMaterialLotNcLoad hmeMaterialLotNcLoad, @ApiIgnore @SortDefault(value = HmeMaterialLotNcLoad.FIELD_NC_LOAD_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeMaterialLotNcLoad> list = hmeMaterialLotNcLoadRepository.pageAndSort(pageRequest, hmeMaterialLotNcLoad);
        return Results.success(list);
    }

    @ApiOperation(value = "不良位置明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ncLoadId}")
    public ResponseEntity<HmeMaterialLotNcLoad> detail(@PathVariable Long ncLoadId) {
        HmeMaterialLotNcLoad hmeMaterialLotNcLoad = hmeMaterialLotNcLoadRepository.selectByPrimaryKey(ncLoadId);
        return Results.success(hmeMaterialLotNcLoad);
    }

    @ApiOperation(value = "创建不良位置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeMaterialLotNcLoad> create(@RequestBody HmeMaterialLotNcLoad hmeMaterialLotNcLoad) {
        validObject(hmeMaterialLotNcLoad);
        hmeMaterialLotNcLoadRepository.insertSelective(hmeMaterialLotNcLoad);
        return Results.success(hmeMaterialLotNcLoad);
    }

    @ApiOperation(value = "修改不良位置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeMaterialLotNcLoad> update(@RequestBody HmeMaterialLotNcLoad hmeMaterialLotNcLoad) {
        SecurityTokenHelper.validToken(hmeMaterialLotNcLoad);
        hmeMaterialLotNcLoadRepository.updateByPrimaryKeySelective(hmeMaterialLotNcLoad);
        return Results.success(hmeMaterialLotNcLoad);
    }

    @ApiOperation(value = "删除不良位置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeMaterialLotNcLoad hmeMaterialLotNcLoad) {
        SecurityTokenHelper.validToken(hmeMaterialLotNcLoad);
        hmeMaterialLotNcLoadRepository.deleteByPrimaryKey(hmeMaterialLotNcLoad);
        return Results.success();
    }

}
