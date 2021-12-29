package com.ruike.qms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.qms.domain.entity.QmsOqcHeader;
import com.ruike.qms.domain.repository.QmsOqcHeaderRepository;
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
 * 出库检头表 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-08-28 14:18:10
 */
@RestController("qmsOqcHeaderController.v1")
@RequestMapping("/v1/{organizationId}/qms-oqc-headers")
public class QmsOqcHeaderController extends BaseController {

    @Autowired
    private QmsOqcHeaderRepository qmsOqcHeaderRepository;

    @ApiOperation(value = "出库检头表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<QmsOqcHeader>> list(QmsOqcHeader qmsOqcHeader, @ApiIgnore @SortDefault(value = QmsOqcHeader.FIELD_OQC_HEADER_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<QmsOqcHeader> list = qmsOqcHeaderRepository.pageAndSort(pageRequest, qmsOqcHeader);
        return Results.success(list);
    }

    @ApiOperation(value = "出库检头表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{oqcHeaderId}")
    public ResponseEntity<QmsOqcHeader> detail(@PathVariable Long oqcHeaderId) {
        QmsOqcHeader qmsOqcHeader = qmsOqcHeaderRepository.selectByPrimaryKey(oqcHeaderId);
        return Results.success(qmsOqcHeader);
    }

    @ApiOperation(value = "创建出库检头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<QmsOqcHeader> create(@RequestBody QmsOqcHeader qmsOqcHeader) {
        validObject(qmsOqcHeader);
        qmsOqcHeaderRepository.insertSelective(qmsOqcHeader);
        return Results.success(qmsOqcHeader);
    }

    @ApiOperation(value = "修改出库检头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<QmsOqcHeader> update(@RequestBody QmsOqcHeader qmsOqcHeader) {
        SecurityTokenHelper.validToken(qmsOqcHeader);
        qmsOqcHeaderRepository.updateByPrimaryKeySelective(qmsOqcHeader);
        return Results.success(qmsOqcHeader);
    }

    @ApiOperation(value = "删除出库检头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody QmsOqcHeader qmsOqcHeader) {
        SecurityTokenHelper.validToken(qmsOqcHeader);
        qmsOqcHeaderRepository.deleteByPrimaryKey(qmsOqcHeader);
        return Results.success();
    }

}
