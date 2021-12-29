package com.ruike.qms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.qms.domain.entity.QmsOqcLine;
import com.ruike.qms.domain.repository.QmsOqcLineRepository;
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
 * 出库检行表 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-08-28 14:18:08
 */
@RestController("qmsOqcLineController.v1")
@RequestMapping("/v1/{organizationId}/qms-oqc-lines")
public class QmsOqcLineController extends BaseController {

    @Autowired
    private QmsOqcLineRepository qmsOqcLineRepository;

    @ApiOperation(value = "出库检行表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<QmsOqcLine>> list(QmsOqcLine qmsOqcLine, @ApiIgnore @SortDefault(value = QmsOqcLine.FIELD_TENANT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<QmsOqcLine> list = qmsOqcLineRepository.pageAndSort(pageRequest, qmsOqcLine);
        return Results.success(list);
    }

    @ApiOperation(value = "出库检行表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{tenantId}")
    public ResponseEntity<QmsOqcLine> detail(@PathVariable Long tenantId) {
        QmsOqcLine qmsOqcLine = qmsOqcLineRepository.selectByPrimaryKey(tenantId);
        return Results.success(qmsOqcLine);
    }

    @ApiOperation(value = "创建出库检行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<QmsOqcLine> create(@RequestBody QmsOqcLine qmsOqcLine) {
        validObject(qmsOqcLine);
        qmsOqcLineRepository.insertSelective(qmsOqcLine);
        return Results.success(qmsOqcLine);
    }

    @ApiOperation(value = "修改出库检行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<QmsOqcLine> update(@RequestBody QmsOqcLine qmsOqcLine) {
        SecurityTokenHelper.validToken(qmsOqcLine);
        qmsOqcLineRepository.updateByPrimaryKeySelective(qmsOqcLine);
        return Results.success(qmsOqcLine);
    }

    @ApiOperation(value = "删除出库检行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody QmsOqcLine qmsOqcLine) {
        SecurityTokenHelper.validToken(qmsOqcLine);
        qmsOqcLineRepository.deleteByPrimaryKey(qmsOqcLine);
        return Results.success();
    }

}
