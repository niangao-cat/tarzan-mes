package com.ruike.qms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.qms.domain.entity.QmsIqcDetailsHis;
import com.ruike.qms.domain.repository.QmsIqcDetailsHisRepository;
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
 * 质检单明细历史表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-06 11:43:28
 */
@RestController("qmsIqcDetailsHisController.v1")
@RequestMapping("/v1/{organizationId}/qms-iqc-details-hiss")
public class QmsIqcDetailsHisController extends BaseController {

    @Autowired
    private QmsIqcDetailsHisRepository qmsIqcDetailsHisRepository;

    @ApiOperation(value = "质检单明细历史表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<QmsIqcDetailsHis>> list(QmsIqcDetailsHis qmsIqcDetailsHis, @ApiIgnore @SortDefault(value = QmsIqcDetailsHis.FIELD_IQC_DETAILS_HIS_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<QmsIqcDetailsHis> list = qmsIqcDetailsHisRepository.pageAndSort(pageRequest, qmsIqcDetailsHis);
        return Results.success(list);
    }

    @ApiOperation(value = "质检单明细历史表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{iqcDetailsHisId}")
    public ResponseEntity<QmsIqcDetailsHis> detail(@PathVariable Long iqcDetailsHisId) {
        QmsIqcDetailsHis qmsIqcDetailsHis = qmsIqcDetailsHisRepository.selectByPrimaryKey(iqcDetailsHisId);
        return Results.success(qmsIqcDetailsHis);
    }

    @ApiOperation(value = "创建质检单明细历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<QmsIqcDetailsHis> create(@RequestBody QmsIqcDetailsHis qmsIqcDetailsHis) {
        validObject(qmsIqcDetailsHis);
        qmsIqcDetailsHisRepository.insertSelective(qmsIqcDetailsHis);
        return Results.success(qmsIqcDetailsHis);
    }

    @ApiOperation(value = "修改质检单明细历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<QmsIqcDetailsHis> update(@RequestBody QmsIqcDetailsHis qmsIqcDetailsHis) {
        SecurityTokenHelper.validToken(qmsIqcDetailsHis);
        qmsIqcDetailsHisRepository.updateByPrimaryKeySelective(qmsIqcDetailsHis);
        return Results.success(qmsIqcDetailsHis);
    }

    @ApiOperation(value = "删除质检单明细历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody QmsIqcDetailsHis qmsIqcDetailsHis) {
        SecurityTokenHelper.validToken(qmsIqcDetailsHis);
        qmsIqcDetailsHisRepository.deleteByPrimaryKey(qmsIqcDetailsHis);
        return Results.success();
    }

}
