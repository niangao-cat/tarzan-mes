package com.ruike.wms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsMaterialLotDocRel;
import com.ruike.wms.domain.repository.WmsMaterialLotDocRelRepository;
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
 * 物料批指令单据关系表 管理 API
 *
 * @author han.zhang03@hand-china.com 2020-04-10 17:39:11
 */
@RestController("wmsMaterialLotDocRelController.v1")
@RequestMapping("/v1/{organizationId}/wms-material-lot-oc-rels")
public class WmsMaterialLotDocRelController extends BaseController {

    @Autowired
    private WmsMaterialLotDocRelRepository wmsMaterialLotOcRelRepository;

    @ApiOperation(value = "物料批指令单据关系表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsMaterialLotDocRel>> list(WmsMaterialLotDocRel wmsMaterialLotOcRel, @ApiIgnore @SortDefault(value = WmsMaterialLotDocRel.FIELD_DOC_REL_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WmsMaterialLotDocRel> list = wmsMaterialLotOcRelRepository.pageAndSort(pageRequest, wmsMaterialLotOcRel);
        return Results.success(list);
    }

    @ApiOperation(value = "物料批指令单据关系表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{docRelId}")
    public ResponseEntity<WmsMaterialLotDocRel> detail(@PathVariable Long docRelId) {
        WmsMaterialLotDocRel wmsMaterialLotOcRel = wmsMaterialLotOcRelRepository.selectByPrimaryKey(docRelId);
        return Results.success(wmsMaterialLotOcRel);
    }

    @ApiOperation(value = "创建物料批指令单据关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<WmsMaterialLotDocRel> create(@RequestBody WmsMaterialLotDocRel wmsMaterialLotOcRel) {
        validObject(wmsMaterialLotOcRel);
        wmsMaterialLotOcRelRepository.insertSelective(wmsMaterialLotOcRel);
        return Results.success(wmsMaterialLotOcRel);
    }

    @ApiOperation(value = "修改物料批指令单据关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<WmsMaterialLotDocRel> update(@RequestBody WmsMaterialLotDocRel wmsMaterialLotOcRel) {
        SecurityTokenHelper.validToken(wmsMaterialLotOcRel);
        wmsMaterialLotOcRelRepository.updateByPrimaryKeySelective(wmsMaterialLotOcRel);
        return Results.success(wmsMaterialLotOcRel);
    }

    @ApiOperation(value = "删除物料批指令单据关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody WmsMaterialLotDocRel wmsMaterialLotOcRel) {
        SecurityTokenHelper.validToken(wmsMaterialLotOcRel);
        wmsMaterialLotOcRelRepository.deleteByPrimaryKey(wmsMaterialLotOcRel);
        return Results.success();
    }

}
