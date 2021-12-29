package com.ruike.qms.api.controller.v1;

import com.ruike.qms.domain.entity.QmsOqcDetails;
import com.ruike.qms.domain.repository.QmsOqcDetailsRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 出库检明细表 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-08-28 14:18:07
 */
@RestController("qmsOqcDetailsController.v1")
@RequestMapping("/v1/{organizationId}/qms-oqc-detailss")
public class QmsOqcDetailsController extends BaseController {

    @Autowired
    private QmsOqcDetailsRepository qmsOqcDetailsRepository;

    @ApiOperation(value = "出库检明细表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<QmsOqcDetails>> list(QmsOqcDetails qmsOqcDetails, @ApiIgnore @SortDefault(value = QmsOqcDetails.FIELD_TENANT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<QmsOqcDetails> list = qmsOqcDetailsRepository.pageAndSort(pageRequest, qmsOqcDetails);
        return Results.success(list);
    }

    @ApiOperation(value = "出库检明细表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{tenantId}")
    public ResponseEntity<QmsOqcDetails> detail(@PathVariable Long tenantId) {
        QmsOqcDetails qmsOqcDetails = qmsOqcDetailsRepository.selectByPrimaryKey(tenantId);
        return Results.success(qmsOqcDetails);
    }

    @ApiOperation(value = "创建出库检明细表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<QmsOqcDetails> create(@RequestBody QmsOqcDetails qmsOqcDetails) {
        validObject(qmsOqcDetails);
        qmsOqcDetailsRepository.insertSelective(qmsOqcDetails);
        return Results.success(qmsOqcDetails);
    }

    @ApiOperation(value = "修改出库检明细表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<QmsOqcDetails> update(@RequestBody QmsOqcDetails qmsOqcDetails) {
        SecurityTokenHelper.validToken(qmsOqcDetails);
        qmsOqcDetailsRepository.updateByPrimaryKeySelective(qmsOqcDetails);
        return Results.success(qmsOqcDetails);
    }

    @ApiOperation(value = "删除出库检明细表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody QmsOqcDetails qmsOqcDetails) {
        SecurityTokenHelper.validToken(qmsOqcDetails);
        qmsOqcDetailsRepository.deleteByPrimaryKey(qmsOqcDetails);
        return Results.success();
    }

}
