package com.ruike.qms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.qms.domain.entity.QmsOqcDetailsHis;
import com.ruike.qms.domain.repository.QmsOqcDetailsHisRepository;
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
 * 出库检明细历史表 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-08-28 14:18:07
 */
@RestController("qmsOqcDetailsHisController.v1")
@RequestMapping("/v1/{organizationId}/qms-oqc-details-hiss")
public class QmsOqcDetailsHisController extends BaseController {

    @Autowired
    private QmsOqcDetailsHisRepository qmsOqcDetailsHisRepository;

    @ApiOperation(value = "出库检明细历史表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<QmsOqcDetailsHis>> list(QmsOqcDetailsHis qmsOqcDetailsHis, @ApiIgnore @SortDefault(value = QmsOqcDetailsHis.FIELD_TENANT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<QmsOqcDetailsHis> list = qmsOqcDetailsHisRepository.pageAndSort(pageRequest, qmsOqcDetailsHis);
        return Results.success(list);
    }

    @ApiOperation(value = "出库检明细历史表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{tenantId}")
    public ResponseEntity<QmsOqcDetailsHis> detail(@PathVariable Long tenantId) {
        QmsOqcDetailsHis qmsOqcDetailsHis = qmsOqcDetailsHisRepository.selectByPrimaryKey(tenantId);
        return Results.success(qmsOqcDetailsHis);
    }

    @ApiOperation(value = "创建出库检明细历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<QmsOqcDetailsHis> create(@RequestBody QmsOqcDetailsHis qmsOqcDetailsHis) {
        validObject(qmsOqcDetailsHis);
        qmsOqcDetailsHisRepository.insertSelective(qmsOqcDetailsHis);
        return Results.success(qmsOqcDetailsHis);
    }

    @ApiOperation(value = "修改出库检明细历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<QmsOqcDetailsHis> update(@RequestBody QmsOqcDetailsHis qmsOqcDetailsHis) {
        SecurityTokenHelper.validToken(qmsOqcDetailsHis);
        qmsOqcDetailsHisRepository.updateByPrimaryKeySelective(qmsOqcDetailsHis);
        return Results.success(qmsOqcDetailsHis);
    }

    @ApiOperation(value = "删除出库检明细历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody QmsOqcDetailsHis qmsOqcDetailsHis) {
        SecurityTokenHelper.validToken(qmsOqcDetailsHis);
        qmsOqcDetailsHisRepository.deleteByPrimaryKey(qmsOqcDetailsHis);
        return Results.success();
    }

}
