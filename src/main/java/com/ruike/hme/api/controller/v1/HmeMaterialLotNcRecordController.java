package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeMaterialLotNcRecord;
import com.ruike.hme.domain.repository.HmeMaterialLotNcRecordRepository;
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
 * 不良明细表 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-08-19 14:50:28
 */
@RestController("hmeMaterialLotNcRecordController.v1")
@RequestMapping("/v1/{organizationId}/hme-material-lot-nc-records")
public class HmeMaterialLotNcRecordController extends BaseController {

    @Autowired
    private HmeMaterialLotNcRecordRepository hmeMaterialLotNcRecordRepository;

    @ApiOperation(value = "不良明细表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeMaterialLotNcRecord>> list(HmeMaterialLotNcRecord hmeMaterialLotNcRecord, @ApiIgnore @SortDefault(value = HmeMaterialLotNcRecord.FIELD_NC_RECORD_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeMaterialLotNcRecord> list = hmeMaterialLotNcRecordRepository.pageAndSort(pageRequest, hmeMaterialLotNcRecord);
        return Results.success(list);
    }

    @ApiOperation(value = "不良明细表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ncRecordId}")
    public ResponseEntity<HmeMaterialLotNcRecord> detail(@PathVariable Long ncRecordId) {
        HmeMaterialLotNcRecord hmeMaterialLotNcRecord = hmeMaterialLotNcRecordRepository.selectByPrimaryKey(ncRecordId);
        return Results.success(hmeMaterialLotNcRecord);
    }

    @ApiOperation(value = "创建不良明细表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeMaterialLotNcRecord> create(@RequestBody HmeMaterialLotNcRecord hmeMaterialLotNcRecord) {
        validObject(hmeMaterialLotNcRecord);
        hmeMaterialLotNcRecordRepository.insertSelective(hmeMaterialLotNcRecord);
        return Results.success(hmeMaterialLotNcRecord);
    }

    @ApiOperation(value = "修改不良明细表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeMaterialLotNcRecord> update(@RequestBody HmeMaterialLotNcRecord hmeMaterialLotNcRecord) {
        SecurityTokenHelper.validToken(hmeMaterialLotNcRecord);
        hmeMaterialLotNcRecordRepository.updateByPrimaryKeySelective(hmeMaterialLotNcRecord);
        return Results.success(hmeMaterialLotNcRecord);
    }

    @ApiOperation(value = "删除不良明细表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeMaterialLotNcRecord hmeMaterialLotNcRecord) {
        SecurityTokenHelper.validToken(hmeMaterialLotNcRecord);
        hmeMaterialLotNcRecordRepository.deleteByPrimaryKey(hmeMaterialLotNcRecord);
        return Results.success();
    }

}
