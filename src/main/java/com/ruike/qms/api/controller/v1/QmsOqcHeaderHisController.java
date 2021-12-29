package com.ruike.qms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.qms.domain.entity.QmsOqcHeaderHis;
import com.ruike.qms.domain.repository.QmsOqcHeaderHisRepository;
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
 * 出库检头历史表 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-08-28 14:18:09
 */
@RestController("qmsOqcHeaderHisController.v1")
@RequestMapping("/v1/{organizationId}/qms-oqc-header-hiss")
public class QmsOqcHeaderHisController extends BaseController {

    @Autowired
    private QmsOqcHeaderHisRepository qmsOqcHeaderHisRepository;

    @ApiOperation(value = "出库检头历史表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<QmsOqcHeaderHis>> list(QmsOqcHeaderHis qmsOqcHeaderHis, @ApiIgnore @SortDefault(value = QmsOqcHeaderHis.FIELD_TENANT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<QmsOqcHeaderHis> list = qmsOqcHeaderHisRepository.pageAndSort(pageRequest, qmsOqcHeaderHis);
        return Results.success(list);
    }

    @ApiOperation(value = "出库检头历史表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{tenantId}")
    public ResponseEntity<QmsOqcHeaderHis> detail(@PathVariable Long tenantId) {
        QmsOqcHeaderHis qmsOqcHeaderHis = qmsOqcHeaderHisRepository.selectByPrimaryKey(tenantId);
        return Results.success(qmsOqcHeaderHis);
    }

    @ApiOperation(value = "创建出库检头历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<QmsOqcHeaderHis> create(@RequestBody QmsOqcHeaderHis qmsOqcHeaderHis) {
        validObject(qmsOqcHeaderHis);
        qmsOqcHeaderHisRepository.insertSelective(qmsOqcHeaderHis);
        return Results.success(qmsOqcHeaderHis);
    }

    @ApiOperation(value = "修改出库检头历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<QmsOqcHeaderHis> update(@RequestBody QmsOqcHeaderHis qmsOqcHeaderHis) {
        SecurityTokenHelper.validToken(qmsOqcHeaderHis);
        qmsOqcHeaderHisRepository.updateByPrimaryKeySelective(qmsOqcHeaderHis);
        return Results.success(qmsOqcHeaderHis);
    }

    @ApiOperation(value = "删除出库检头历史表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody QmsOqcHeaderHis qmsOqcHeaderHis) {
        SecurityTokenHelper.validToken(qmsOqcHeaderHis);
        qmsOqcHeaderHisRepository.deleteByPrimaryKey(qmsOqcHeaderHis);
        return Results.success();
    }

}
