package com.ruike.wms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsJobRecord;
import com.ruike.wms.domain.repository.WmsJobRecordRepository;
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
 * Job同步成功记录表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-11-19 09:33:40
 */
@RestController("wmsJobRecordController.v1")
@RequestMapping("/v1/{organizationId}/wms-job-records")
public class WmsJobRecordController extends BaseController {

    @Autowired
    private WmsJobRecordRepository wmsJobRecordRepository;

    @ApiOperation(value = "Job同步成功记录表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsJobRecord>> list(WmsJobRecord wmsJobRecord, @ApiIgnore @SortDefault(value = WmsJobRecord.FIELD_JOB_RECORD_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WmsJobRecord> list = wmsJobRecordRepository.pageAndSort(pageRequest, wmsJobRecord);
        return Results.success(list);
    }

    @ApiOperation(value = "Job同步成功记录表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{jobRecordId}")
    public ResponseEntity<WmsJobRecord> detail(@PathVariable Long jobRecordId) {
        WmsJobRecord wmsJobRecord = wmsJobRecordRepository.selectByPrimaryKey(jobRecordId);
        return Results.success(wmsJobRecord);
    }

    @ApiOperation(value = "创建Job同步成功记录表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<WmsJobRecord> create(@RequestBody WmsJobRecord wmsJobRecord) {
        validObject(wmsJobRecord);
        wmsJobRecordRepository.insertSelective(wmsJobRecord);
        return Results.success(wmsJobRecord);
    }

    @ApiOperation(value = "修改Job同步成功记录表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<WmsJobRecord> update(@RequestBody WmsJobRecord wmsJobRecord) {
        SecurityTokenHelper.validToken(wmsJobRecord);
        wmsJobRecordRepository.updateByPrimaryKeySelective(wmsJobRecord);
        return Results.success(wmsJobRecord);
    }

    @ApiOperation(value = "删除Job同步成功记录表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody WmsJobRecord wmsJobRecord) {
        SecurityTokenHelper.validToken(wmsJobRecord);
        wmsJobRecordRepository.deleteByPrimaryKey(wmsJobRecord);
        return Results.success();
    }

}
