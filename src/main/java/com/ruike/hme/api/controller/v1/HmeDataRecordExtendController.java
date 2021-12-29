package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeDataRecordExtend;
import com.ruike.hme.domain.repository.HmeDataRecordExtendRepository;
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
 * 采集项记录扩展表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2021-09-29 14:24:34
 */
@RestController("hmeDataRecordExtendController.v1")
@RequestMapping("/v1/{organizationId}/hme-data-record-extends")
public class HmeDataRecordExtendController extends BaseController {

    @Autowired
    private HmeDataRecordExtendRepository hmeDataRecordExtendRepository;

    @ApiOperation(value = "采集项记录扩展表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeDataRecordExtend>> list(HmeDataRecordExtend hmeDataRecordExtend, @ApiIgnore @SortDefault(value = HmeDataRecordExtend.FIELD_DATA_RECORD_EXTEND_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeDataRecordExtend> list = hmeDataRecordExtendRepository.pageAndSort(pageRequest, hmeDataRecordExtend);
        return Results.success(list);
    }

    @ApiOperation(value = "采集项记录扩展表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{dataRecordExtendId}")
    public ResponseEntity<HmeDataRecordExtend> detail(@PathVariable Long dataRecordExtendId) {
        HmeDataRecordExtend hmeDataRecordExtend = hmeDataRecordExtendRepository.selectByPrimaryKey(dataRecordExtendId);
        return Results.success(hmeDataRecordExtend);
    }

    @ApiOperation(value = "创建采集项记录扩展表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeDataRecordExtend> create(@RequestBody HmeDataRecordExtend hmeDataRecordExtend) {
        validObject(hmeDataRecordExtend);
        hmeDataRecordExtendRepository.insertSelective(hmeDataRecordExtend);
        return Results.success(hmeDataRecordExtend);
    }

    @ApiOperation(value = "修改采集项记录扩展表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeDataRecordExtend> update(@RequestBody HmeDataRecordExtend hmeDataRecordExtend) {
        SecurityTokenHelper.validToken(hmeDataRecordExtend);
        hmeDataRecordExtendRepository.updateByPrimaryKeySelective(hmeDataRecordExtend);
        return Results.success(hmeDataRecordExtend);
    }

    @ApiOperation(value = "删除采集项记录扩展表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeDataRecordExtend hmeDataRecordExtend) {
        SecurityTokenHelper.validToken(hmeDataRecordExtend);
        hmeDataRecordExtendRepository.deleteByPrimaryKey(hmeDataRecordExtend);
        return Results.success();
    }

}
