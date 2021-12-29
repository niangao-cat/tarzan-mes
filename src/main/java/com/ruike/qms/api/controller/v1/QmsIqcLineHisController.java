package com.ruike.qms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.qms.domain.entity.QmsIqcLineHis;
import com.ruike.qms.domain.repository.QmsIqcLineHisRepository;
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
 * 质检单行历史表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-06 11:43:28
 */
@RestController("qmsIqcLineHisController.v1")
@RequestMapping("/v1/{organizationId}/qms-iqc-line-hiss")
public class QmsIqcLineHisController extends BaseController {

    @Autowired
    private QmsIqcLineHisRepository qmsIqcLineHisRepository;

    @ApiOperation(value = "质检单行历史表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<QmsIqcLineHis>> list(QmsIqcLineHis qmsIqcLineHis, @ApiIgnore @SortDefault(value = QmsIqcLineHis.FIELD_IQC_LINE_HIS_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<QmsIqcLineHis> list = qmsIqcLineHisRepository.pageAndSort(pageRequest, qmsIqcLineHis);
        return Results.success(list);
    }

    @ApiOperation(value = "质检单行历史表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{iqcLineHisId}")
    public ResponseEntity<QmsIqcLineHis> detail(@PathVariable Long iqcLineHisId) {
        QmsIqcLineHis qmsIqcLineHis = qmsIqcLineHisRepository.selectByPrimaryKey(iqcLineHisId);
        return Results.success(qmsIqcLineHis);
    }

    @ApiOperation(value = "创建质检单行历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<QmsIqcLineHis> create(@RequestBody QmsIqcLineHis qmsIqcLineHis) {
        validObject(qmsIqcLineHis);
        qmsIqcLineHisRepository.insertSelective(qmsIqcLineHis);
        return Results.success(qmsIqcLineHis);
    }

    @ApiOperation(value = "修改质检单行历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<QmsIqcLineHis> update(@RequestBody QmsIqcLineHis qmsIqcLineHis) {
        SecurityTokenHelper.validToken(qmsIqcLineHis);
        qmsIqcLineHisRepository.updateByPrimaryKeySelective(qmsIqcLineHis);
        return Results.success(qmsIqcLineHis);
    }

    @ApiOperation(value = "删除质检单行历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody QmsIqcLineHis qmsIqcLineHis) {
        SecurityTokenHelper.validToken(qmsIqcLineHis);
        qmsIqcLineHisRepository.deleteByPrimaryKey(qmsIqcLineHis);
        return Results.success();
    }

}
