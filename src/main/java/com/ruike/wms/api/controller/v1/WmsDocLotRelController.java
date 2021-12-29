package com.ruike.wms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsDocLotRel;
import com.ruike.wms.domain.repository.WmsDocLotRelRepository;
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
 * 单据批次关系表 管理 API
 *
 * @author tong.li05@hand-china.com 2020-06-11 15:18:04
 */
@RestController("wmsDocLotRelController.v1")
@RequestMapping("/v1/{organizationId}/wms-doc-lot-rels")
public class WmsDocLotRelController extends BaseController {

    @Autowired
    private WmsDocLotRelRepository wmsDocLotRelRepository;

    @ApiOperation(value = "单据批次关系表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsDocLotRel>> list(WmsDocLotRel wmsDocLotRel, @ApiIgnore @SortDefault(value = WmsDocLotRel.FIELD_DOC_REL_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WmsDocLotRel> list = wmsDocLotRelRepository.pageAndSort(pageRequest, wmsDocLotRel);
        return Results.success(list);
    }

    @ApiOperation(value = "单据批次关系表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{docRelId}")
    public ResponseEntity<WmsDocLotRel> detail(@PathVariable Long docRelId) {
        WmsDocLotRel wmsDocLotRel = wmsDocLotRelRepository.selectByPrimaryKey(docRelId);
        return Results.success(wmsDocLotRel);
    }

    @ApiOperation(value = "创建单据批次关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<WmsDocLotRel> create(@RequestBody WmsDocLotRel wmsDocLotRel) {
        validObject(wmsDocLotRel);
        wmsDocLotRelRepository.insertSelective(wmsDocLotRel);
        return Results.success(wmsDocLotRel);
    }

    @ApiOperation(value = "修改单据批次关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<WmsDocLotRel> update(@RequestBody WmsDocLotRel wmsDocLotRel) {
        SecurityTokenHelper.validToken(wmsDocLotRel);
        wmsDocLotRelRepository.updateByPrimaryKeySelective(wmsDocLotRel);
        return Results.success(wmsDocLotRel);
    }

    @ApiOperation(value = "删除单据批次关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody WmsDocLotRel wmsDocLotRel) {
        SecurityTokenHelper.validToken(wmsDocLotRel);
        wmsDocLotRelRepository.deleteByPrimaryKey(wmsDocLotRel);
        return Results.success();
    }

}
