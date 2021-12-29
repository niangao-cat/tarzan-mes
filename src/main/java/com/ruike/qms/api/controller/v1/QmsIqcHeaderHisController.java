package com.ruike.qms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.qms.domain.entity.QmsIqcHeaderHis;
import com.ruike.qms.domain.repository.QmsIqcHeaderHisRepository;
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
 * 质检单头历史表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-06 11:43:28
 */
@RestController("qmsIqcHeaderHisController.v1")
@RequestMapping("/v1/{organizationId}/qms-iqc-header-hiss")
public class QmsIqcHeaderHisController extends BaseController {

    @Autowired
    private QmsIqcHeaderHisRepository qmsIqcHeaderHisRepository;

    @ApiOperation(value = "质检单头历史表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<QmsIqcHeaderHis>> list(QmsIqcHeaderHis qmsIqcHeaderHis, @ApiIgnore @SortDefault(value = QmsIqcHeaderHis.FIELD_IQC_HEADER_HIS_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<QmsIqcHeaderHis> list = qmsIqcHeaderHisRepository.pageAndSort(pageRequest, qmsIqcHeaderHis);
        return Results.success(list);
    }

    @ApiOperation(value = "质检单头历史表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{iqcHeaderHisId}")
    public ResponseEntity<QmsIqcHeaderHis> detail(@PathVariable Long iqcHeaderHisId) {
        QmsIqcHeaderHis qmsIqcHeaderHis = qmsIqcHeaderHisRepository.selectByPrimaryKey(iqcHeaderHisId);
        return Results.success(qmsIqcHeaderHis);
    }

    @ApiOperation(value = "创建质检单头历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<QmsIqcHeaderHis> create(@RequestBody QmsIqcHeaderHis qmsIqcHeaderHis) {
        validObject(qmsIqcHeaderHis);
        qmsIqcHeaderHisRepository.insertSelective(qmsIqcHeaderHis);
        return Results.success(qmsIqcHeaderHis);
    }

    @ApiOperation(value = "修改质检单头历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<QmsIqcHeaderHis> update(@RequestBody QmsIqcHeaderHis qmsIqcHeaderHis) {
        SecurityTokenHelper.validToken(qmsIqcHeaderHis);
        qmsIqcHeaderHisRepository.updateByPrimaryKeySelective(qmsIqcHeaderHis);
        return Results.success(qmsIqcHeaderHis);
    }

    @ApiOperation(value = "删除质检单头历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody QmsIqcHeaderHis qmsIqcHeaderHis) {
        SecurityTokenHelper.validToken(qmsIqcHeaderHis);
        qmsIqcHeaderHisRepository.deleteByPrimaryKey(qmsIqcHeaderHis);
        return Results.success();
    }

}
