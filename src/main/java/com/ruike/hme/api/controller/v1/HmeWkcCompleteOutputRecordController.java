package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeWkcCompleteOutputRecord;
import com.ruike.hme.domain.repository.HmeWkcCompleteOutputRecordRepository;
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
 * 工段完工数据统计表 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-09-21 10:30:07
 */
@RestController("hmeWkcCompleteOutputRecordController.v1")
@RequestMapping("/v1/{organizationId}/hme-wkc-complete-output-records")
public class HmeWkcCompleteOutputRecordController extends BaseController {

    @Autowired
    private HmeWkcCompleteOutputRecordRepository hmeWkcCompleteOutputRecordRepository;

    @ApiOperation(value = "工段完工数据统计表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeWkcCompleteOutputRecord>> list(HmeWkcCompleteOutputRecord hmeWkcCompleteOutputRecord, @ApiIgnore @SortDefault(value = HmeWkcCompleteOutputRecord.FIELD_WKC_OUTPUT_RECORD_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeWkcCompleteOutputRecord> list = hmeWkcCompleteOutputRecordRepository.pageAndSort(pageRequest, hmeWkcCompleteOutputRecord);
        return Results.success(list);
    }

    @ApiOperation(value = "工段完工数据统计表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{wkcOutputRecordId}")
    public ResponseEntity<HmeWkcCompleteOutputRecord> detail(@PathVariable Long wkcOutputRecordId) {
        HmeWkcCompleteOutputRecord hmeWkcCompleteOutputRecord = hmeWkcCompleteOutputRecordRepository.selectByPrimaryKey(wkcOutputRecordId);
        return Results.success(hmeWkcCompleteOutputRecord);
    }

    @ApiOperation(value = "创建工段完工数据统计表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeWkcCompleteOutputRecord> create(@RequestBody HmeWkcCompleteOutputRecord hmeWkcCompleteOutputRecord) {
        validObject(hmeWkcCompleteOutputRecord);
        hmeWkcCompleteOutputRecordRepository.insertSelective(hmeWkcCompleteOutputRecord);
        return Results.success(hmeWkcCompleteOutputRecord);
    }

    @ApiOperation(value = "修改工段完工数据统计表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeWkcCompleteOutputRecord> update(@RequestBody HmeWkcCompleteOutputRecord hmeWkcCompleteOutputRecord) {
        SecurityTokenHelper.validToken(hmeWkcCompleteOutputRecord);
        hmeWkcCompleteOutputRecordRepository.updateByPrimaryKeySelective(hmeWkcCompleteOutputRecord);
        return Results.success(hmeWkcCompleteOutputRecord);
    }

    @ApiOperation(value = "删除工段完工数据统计表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeWkcCompleteOutputRecord hmeWkcCompleteOutputRecord) {
        SecurityTokenHelper.validToken(hmeWkcCompleteOutputRecord);
        hmeWkcCompleteOutputRecordRepository.deleteByPrimaryKey(hmeWkcCompleteOutputRecord);
        return Results.success();
    }

}
