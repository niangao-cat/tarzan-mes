package com.ruike.qms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.qms.domain.entity.QmsIqcDetails;
import com.ruike.qms.domain.repository.QmsIqcDetailsRepository;
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
 * 质检单明细表 管理 API
 *
 * @author tong.li05@hand-china.com 2020-04-29 13:50:51
 */
@RestController("qmsIqcDetailsController.v1")
@RequestMapping("/v1/{organizationId}/qms-iqc-detailss")
public class QmsIqcDetailsController extends BaseController {

    @Autowired
    private QmsIqcDetailsRepository iqcDetailsRepository;

    @ApiOperation(value = "质检单明细表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<QmsIqcDetails>> list(QmsIqcDetails qmsIqcDetails, @ApiIgnore @SortDefault(value = QmsIqcDetails.FIELD_IQC_DETAILS_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<QmsIqcDetails> list = iqcDetailsRepository.pageAndSort(pageRequest, qmsIqcDetails);
        return Results.success(list);
    }

    @ApiOperation(value = "质检单明细表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{iqcDetailsId}")
    public ResponseEntity<QmsIqcDetails> detail(@PathVariable Long iqcDetailsId) {
        QmsIqcDetails qmsIqcDetails = iqcDetailsRepository.selectByPrimaryKey(iqcDetailsId);
        return Results.success(qmsIqcDetails);
    }

    @ApiOperation(value = "质检单明细表创建")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<QmsIqcDetails> create(@RequestBody QmsIqcDetails qmsIqcDetails) {
        validObject(qmsIqcDetails);
        iqcDetailsRepository.insertSelective(qmsIqcDetails);
        return Results.success(qmsIqcDetails);
    }

    @ApiOperation(value = "质检单明细表修改")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<QmsIqcDetails> update(@RequestBody QmsIqcDetails qmsIqcDetails) {
        SecurityTokenHelper.validToken(qmsIqcDetails);
        iqcDetailsRepository.updateByPrimaryKeySelective(qmsIqcDetails);
        return Results.success(qmsIqcDetails);
    }

    @ApiOperation(value = "质检单明细表删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody QmsIqcDetails qmsIqcDetails) {
        SecurityTokenHelper.validToken(qmsIqcDetails);
        iqcDetailsRepository.deleteByPrimaryKey(qmsIqcDetails);
        return Results.success();
    }

}
