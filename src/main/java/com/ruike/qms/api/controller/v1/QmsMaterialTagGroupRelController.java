package com.ruike.qms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.qms.domain.entity.QmsMaterialTagGroupRel;
import com.ruike.qms.domain.repository.QmsMaterialTagGroupRelRepository;
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
 * 物料与检验组关系表 管理 API
 *
 * @author han.zhang03@hand-china.com 2020-04-21 21:33:41
 */
@RestController("qmsMaterialTagGroupRelController.v1")
@RequestMapping("/v1/{organizationId}/qms-material-tag-group-rels")
public class QmsMaterialTagGroupRelController extends BaseController {

    @Autowired
    private QmsMaterialTagGroupRelRepository qmsMaterialTagGroupRelRepository;

    @ApiOperation(value = "物料与检验组关系表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<QmsMaterialTagGroupRel>> list(QmsMaterialTagGroupRel qmsMaterialTagGroupRel, @ApiIgnore @SortDefault(value = QmsMaterialTagGroupRel.FIELD_TAG_GROUP_REL_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<QmsMaterialTagGroupRel> list = qmsMaterialTagGroupRelRepository.pageAndSort(pageRequest, qmsMaterialTagGroupRel);
        return Results.success(list);
    }

    @ApiOperation(value = "物料与检验组关系表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{tagGroupRelId}")
    public ResponseEntity<QmsMaterialTagGroupRel> detail(@PathVariable Long tagGroupRelId) {
        QmsMaterialTagGroupRel qmsMaterialTagGroupRel = qmsMaterialTagGroupRelRepository.selectByPrimaryKey(tagGroupRelId);
        return Results.success(qmsMaterialTagGroupRel);
    }

    @ApiOperation(value = "创建物料与检验组关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<QmsMaterialTagGroupRel> create(@RequestBody QmsMaterialTagGroupRel qmsMaterialTagGroupRel) {
        validObject(qmsMaterialTagGroupRel);
        qmsMaterialTagGroupRelRepository.insertSelective(qmsMaterialTagGroupRel);
        return Results.success(qmsMaterialTagGroupRel);
    }

    @ApiOperation(value = "修改物料与检验组关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<QmsMaterialTagGroupRel> update(@RequestBody QmsMaterialTagGroupRel qmsMaterialTagGroupRel) {
        SecurityTokenHelper.validToken(qmsMaterialTagGroupRel);
        qmsMaterialTagGroupRelRepository.updateByPrimaryKeySelective(qmsMaterialTagGroupRel);
        return Results.success(qmsMaterialTagGroupRel);
    }

    @ApiOperation(value = "删除物料与检验组关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody QmsMaterialTagGroupRel qmsMaterialTagGroupRel) {
        SecurityTokenHelper.validToken(qmsMaterialTagGroupRel);
        qmsMaterialTagGroupRelRepository.deleteByPrimaryKey(qmsMaterialTagGroupRel);
        return Results.success();
    }

}
