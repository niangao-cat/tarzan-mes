package com.ruike.qms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.qms.domain.entity.QmsInspectionLevelsRecord;
import com.ruike.qms.domain.repository.QmsInspectionLevelsRecordRepository;
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
 * 供应商物料检验水平记录表 管理 API
 *
 * @author tong.li05@hand-china.com 2020-05-09 16:09:46
 */
@RestController("qmsInspectionLevelsRecordController.v1")
@RequestMapping("/v1/{organizationId}/qms-inspection-levels-records")
public class QmsInspectionLevelsRecordController extends BaseController {

    @Autowired
    private QmsInspectionLevelsRecordRepository inspectionLevelsRecordRepository;

    @ApiOperation(value = "供应商物料检验水平记录表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<QmsInspectionLevelsRecord>> list(QmsInspectionLevelsRecord qmsInspectionLevelsRecord, @ApiIgnore @SortDefault(value = QmsInspectionLevelsRecord.FIELD_RECORD_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<QmsInspectionLevelsRecord> list = inspectionLevelsRecordRepository.pageAndSort(pageRequest, qmsInspectionLevelsRecord);
        return Results.success(list);
    }

    @ApiOperation(value = "供应商物料检验水平记录表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{recordId}")
    public ResponseEntity<QmsInspectionLevelsRecord> detail(@PathVariable Long recordId) {
        QmsInspectionLevelsRecord qmsInspectionLevelsRecord = inspectionLevelsRecordRepository.selectByPrimaryKey(recordId);
        return Results.success(qmsInspectionLevelsRecord);
    }

    @ApiOperation(value = "创建供应商物料检验水平记录表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<QmsInspectionLevelsRecord> create(@RequestBody QmsInspectionLevelsRecord qmsInspectionLevelsRecord) {
        validObject(qmsInspectionLevelsRecord);
        inspectionLevelsRecordRepository.insertSelective(qmsInspectionLevelsRecord);
        return Results.success(qmsInspectionLevelsRecord);
    }

    @ApiOperation(value = "修改供应商物料检验水平记录表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<QmsInspectionLevelsRecord> update(@RequestBody QmsInspectionLevelsRecord qmsInspectionLevelsRecord) {
        SecurityTokenHelper.validToken(qmsInspectionLevelsRecord);
        inspectionLevelsRecordRepository.updateByPrimaryKeySelective(qmsInspectionLevelsRecord);
        return Results.success(qmsInspectionLevelsRecord);
    }

    @ApiOperation(value = "删除供应商物料检验水平记录表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody QmsInspectionLevelsRecord qmsInspectionLevelsRecord) {
        SecurityTokenHelper.validToken(qmsInspectionLevelsRecord);
        inspectionLevelsRecordRepository.deleteByPrimaryKey(qmsInspectionLevelsRecord);
        return Results.success();
    }

}
