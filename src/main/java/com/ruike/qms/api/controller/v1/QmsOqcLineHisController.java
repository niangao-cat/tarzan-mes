package com.ruike.qms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.qms.domain.entity.QmsOqcLineHis;
import com.ruike.qms.domain.repository.QmsOqcLineHisRepository;
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
 * 出库检行历史表 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-08-28 14:18:08
 */
@RestController("qmsOqcLineHisController.v1")
@RequestMapping("/v1/{organizationId}/qms-oqc-line-hiss")
public class QmsOqcLineHisController extends BaseController {

    @Autowired
    private QmsOqcLineHisRepository qmsOqcLineHisRepository;

    @ApiOperation(value = "出库检行历史表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<QmsOqcLineHis>> list(QmsOqcLineHis qmsOqcLineHis, @ApiIgnore @SortDefault(value = QmsOqcLineHis.FIELD_TENANT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<QmsOqcLineHis> list = qmsOqcLineHisRepository.pageAndSort(pageRequest, qmsOqcLineHis);
        return Results.success(list);
    }

    @ApiOperation(value = "出库检行历史表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{tenantId}")
    public ResponseEntity<QmsOqcLineHis> detail(@PathVariable Long tenantId) {
        QmsOqcLineHis qmsOqcLineHis = qmsOqcLineHisRepository.selectByPrimaryKey(tenantId);
        return Results.success(qmsOqcLineHis);
    }

    @ApiOperation(value = "创建出库检行历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<QmsOqcLineHis> create(@RequestBody QmsOqcLineHis qmsOqcLineHis) {
        validObject(qmsOqcLineHis);
        qmsOqcLineHisRepository.insertSelective(qmsOqcLineHis);
        return Results.success(qmsOqcLineHis);
    }

    @ApiOperation(value = "修改出库检行历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<QmsOqcLineHis> update(@RequestBody QmsOqcLineHis qmsOqcLineHis) {
        SecurityTokenHelper.validToken(qmsOqcLineHis);
        qmsOqcLineHisRepository.updateByPrimaryKeySelective(qmsOqcLineHis);
        return Results.success(qmsOqcLineHis);
    }

    @ApiOperation(value = "删除出库检行历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody QmsOqcLineHis qmsOqcLineHis) {
        SecurityTokenHelper.validToken(qmsOqcLineHis);
        qmsOqcLineHisRepository.deleteByPrimaryKey(qmsOqcLineHis);
        return Results.success();
    }

}
