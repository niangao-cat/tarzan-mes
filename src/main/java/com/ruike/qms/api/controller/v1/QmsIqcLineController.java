package com.ruike.qms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.qms.domain.entity.QmsIqcLine;
import com.ruike.qms.domain.repository.QmsIqcLineRepository;
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
 * 质检单行表 管理 API
 *
 * @author tong.li05@hand-china.com 2020-04-29 13:43:23
 */
@RestController("qmsIqcLineController.v1")
@RequestMapping("/v1/{organizationId}/qms-iqc-lines")
public class QmsIqcLineController extends BaseController {

    @Autowired
    private QmsIqcLineRepository sampleSchemeRepository;

    @ApiOperation(value = "质检单行表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<QmsIqcLine>> list(QmsIqcLine qmsIqcLine, @ApiIgnore @SortDefault(value = QmsIqcLine.FIELD_IQC_LINE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<QmsIqcLine> list = sampleSchemeRepository.pageAndSort(pageRequest, qmsIqcLine);
        return Results.success(list);
    }

    @ApiOperation(value = "质检单行表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{iqcLineId}")
    public ResponseEntity<QmsIqcLine> detail(@PathVariable Long iqcLineId) {
        QmsIqcLine qmsIqcLine = sampleSchemeRepository.selectByPrimaryKey(iqcLineId);
        return Results.success(qmsIqcLine);
    }

    @ApiOperation(value = "质检单行表创建")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<QmsIqcLine> create(@RequestBody QmsIqcLine qmsIqcLine) {
        validObject(qmsIqcLine);
        sampleSchemeRepository.insertSelective(qmsIqcLine);
        return Results.success(qmsIqcLine);
    }

    @ApiOperation(value = "质检单行表修改")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<QmsIqcLine> update(@RequestBody QmsIqcLine qmsIqcLine) {
        SecurityTokenHelper.validToken(qmsIqcLine);
        sampleSchemeRepository.updateByPrimaryKeySelective(qmsIqcLine);
        return Results.success(qmsIqcLine);
    }

    @ApiOperation(value = "质检单行表删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody QmsIqcLine qmsIqcLine) {
        SecurityTokenHelper.validToken(qmsIqcLine);
        sampleSchemeRepository.deleteByPrimaryKey(qmsIqcLine);
        return Results.success();
    }

}
