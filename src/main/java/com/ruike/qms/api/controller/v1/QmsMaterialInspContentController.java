package com.ruike.qms.api.controller.v1;

import com.ruike.qms.domain.entity.QmsMaterialInspContent;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.qms.domain.repository.QmsMaterialInspContentRepository;
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
 * 物料检验项目表 管理 API
 *
 * @author han.zhang03@hand-china.com 2020-04-21 21:33:42
 */
@RestController("qmsMaterialInspectionContentController.v1")
@RequestMapping("/v1/{organizationId}/qms-material-inspection-contents")
public class QmsMaterialInspContentController extends BaseController {

    @Autowired
    private QmsMaterialInspContentRepository qmsMaterialInspectionContentRepository;

    @ApiOperation(value = "物料检验项目表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<QmsMaterialInspContent>> list(QmsMaterialInspContent qmsMaterialInspectionContent, @ApiIgnore @SortDefault(value = com.ruike.qms.domain.entity.QmsMaterialInspContent.FIELD_MATERIAL_INSPECTION_CONTENT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<QmsMaterialInspContent> list = qmsMaterialInspectionContentRepository.pageAndSort(pageRequest, qmsMaterialInspectionContent);
        return Results.success(list);
    }

    @ApiOperation(value = "物料检验项目表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{materialInspectionContentId}")
    public ResponseEntity<QmsMaterialInspContent> detail(@PathVariable Long materialInspectionContentId) {
        QmsMaterialInspContent qmsMaterialInspectionContent = qmsMaterialInspectionContentRepository.selectByPrimaryKey(materialInspectionContentId);
        return Results.success(qmsMaterialInspectionContent);
    }

    @ApiOperation(value = "创建物料检验项目表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<QmsMaterialInspContent> create(@RequestBody QmsMaterialInspContent qmsMaterialInspectionContent) {
        validObject(qmsMaterialInspectionContent);
        qmsMaterialInspectionContentRepository.insertSelective(qmsMaterialInspectionContent);
        return Results.success(qmsMaterialInspectionContent);
    }

    @ApiOperation(value = "修改物料检验项目表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<QmsMaterialInspContent> update(@RequestBody QmsMaterialInspContent qmsMaterialInspectionContent) {
        SecurityTokenHelper.validToken(qmsMaterialInspectionContent);
        qmsMaterialInspectionContentRepository.updateByPrimaryKeySelective(qmsMaterialInspectionContent);
        return Results.success(qmsMaterialInspectionContent);
    }

    @ApiOperation(value = "删除物料检验项目表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody QmsMaterialInspContent qmsMaterialInspectionContent) {
        SecurityTokenHelper.validToken(qmsMaterialInspectionContent);
        qmsMaterialInspectionContentRepository.deleteByPrimaryKey(qmsMaterialInspectionContent);
        return Results.success();
    }

}
