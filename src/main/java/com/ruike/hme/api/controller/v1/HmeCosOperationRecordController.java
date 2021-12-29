package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.entity.HmeCosOperationRecordHis;
import com.ruike.hme.domain.repository.HmeCosOperationRecordHisRepository;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.repository.HmeCosOperationRecordRepository;
import org.springframework.beans.BeanUtils;
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
 * 来料信息记录 管理 API
 *
 * @author wenzhnag.yu@hand-china.com 2020-08-17 17:26:54
 */
@RestController("hmeCosOperationRecordController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-operation-records")
public class HmeCosOperationRecordController extends BaseController {

    @Autowired
    private HmeCosOperationRecordRepository hmeCosOperationRecordRepository;
    @Autowired
    private HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository;

    @ApiOperation(value = "来料信息记录列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeCosOperationRecord>> list(HmeCosOperationRecord hmeCosOperationRecord, @ApiIgnore @SortDefault(value = HmeCosOperationRecord.FIELD_OPERATION_RECORD_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeCosOperationRecord> list = hmeCosOperationRecordRepository.pageAndSort(pageRequest, hmeCosOperationRecord);
        return Results.success(list);
    }

    @ApiOperation(value = "来料信息记录明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{operationRecordId}")
    public ResponseEntity<HmeCosOperationRecord> detail(@PathVariable Long operationRecordId) {
        HmeCosOperationRecord hmeCosOperationRecord = hmeCosOperationRecordRepository.selectByPrimaryKey(operationRecordId);
        return Results.success(hmeCosOperationRecord);
    }

    @ApiOperation(value = "创建来料信息记录")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeCosOperationRecord> create(@RequestBody HmeCosOperationRecord hmeCosOperationRecord) {
        validObject(hmeCosOperationRecord);
        hmeCosOperationRecordRepository.insertSelective(hmeCosOperationRecord);
        // 保存历史记录
        HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
        BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
        hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
        return Results.success(hmeCosOperationRecord);
    }

    @ApiOperation(value = "修改来料信息记录")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeCosOperationRecord> update(@RequestBody HmeCosOperationRecord hmeCosOperationRecord) {
        SecurityTokenHelper.validToken(hmeCosOperationRecord);
        hmeCosOperationRecordRepository.updateByPrimaryKeySelective(hmeCosOperationRecord);
        return Results.success(hmeCosOperationRecord);
    }

    @ApiOperation(value = "删除来料信息记录")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeCosOperationRecord hmeCosOperationRecord) {
        SecurityTokenHelper.validToken(hmeCosOperationRecord);
        hmeCosOperationRecordRepository.deleteByPrimaryKey(hmeCosOperationRecord);
        return Results.success();
    }

}
