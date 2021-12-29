package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeEoTestDataRecord;
import com.ruike.hme.domain.repository.HmeEoTestDataRecordRepository;
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
 * 数据采集回测对比记录信息表 管理 API
 *
 * @author penglin.sui@hand-china.com 2020-09-20 16:35:24
 */
@RestController("hmeEoTestDataRecordController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-test-data-records")
public class HmeEoTestDataRecordController extends BaseController {

    @Autowired
    private HmeEoTestDataRecordRepository hmeEoTestDataRecordRepository;

    @ApiOperation(value = "数据采集回测对比记录信息表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeEoTestDataRecord>> list(HmeEoTestDataRecord hmeEoTestDataRecord, @ApiIgnore @SortDefault(value = HmeEoTestDataRecord.FIELD_TENANT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeEoTestDataRecord> list = hmeEoTestDataRecordRepository.pageAndSort(pageRequest, hmeEoTestDataRecord);
        return Results.success(list);
    }

    @ApiOperation(value = "数据采集回测对比记录信息表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{tenantId}")
    public ResponseEntity<HmeEoTestDataRecord> detail(@PathVariable Long tenantId) {
        HmeEoTestDataRecord hmeEoTestDataRecord = hmeEoTestDataRecordRepository.selectByPrimaryKey(tenantId);
        return Results.success(hmeEoTestDataRecord);
    }

    @ApiOperation(value = "创建数据采集回测对比记录信息表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeEoTestDataRecord> create(@RequestBody HmeEoTestDataRecord hmeEoTestDataRecord) {
        validObject(hmeEoTestDataRecord);
        hmeEoTestDataRecordRepository.insertSelective(hmeEoTestDataRecord);
        return Results.success(hmeEoTestDataRecord);
    }

    @ApiOperation(value = "修改数据采集回测对比记录信息表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeEoTestDataRecord> update(@RequestBody HmeEoTestDataRecord hmeEoTestDataRecord) {
        SecurityTokenHelper.validToken(hmeEoTestDataRecord);
        hmeEoTestDataRecordRepository.updateByPrimaryKeySelective(hmeEoTestDataRecord);
        return Results.success(hmeEoTestDataRecord);
    }

    @ApiOperation(value = "删除数据采集回测对比记录信息表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeEoTestDataRecord hmeEoTestDataRecord) {
        SecurityTokenHelper.validToken(hmeEoTestDataRecord);
        hmeEoTestDataRecordRepository.deleteByPrimaryKey(hmeEoTestDataRecord);
        return Results.success();
    }

}
