package com.ruike.wms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsDistributionBasicDataProductionLine;
import com.ruike.wms.domain.repository.WmsDistributionBasicDataProductionLineRepository;
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
 * 配送物料产线表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-07-21 21:09:25
 */
@RestController("wmsDistributionBasicDataProductionLineController.v1")
@RequestMapping("/v1/{organizationId}/wms-distribution-basic-data-production-lines")
public class WmsDistributionBasicDataProductionLineController extends BaseController {

    @Autowired
    private WmsDistributionBasicDataProductionLineRepository wmsDistributionBasicDataProductionLineRepository;

    @ApiOperation(value = "配送物料产线表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsDistributionBasicDataProductionLine>> list(WmsDistributionBasicDataProductionLine wmsDistributionBasicDataProductionLine, @ApiIgnore @SortDefault(value = WmsDistributionBasicDataProductionLine.FIELD_LINE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WmsDistributionBasicDataProductionLine> list = wmsDistributionBasicDataProductionLineRepository.pageAndSort(pageRequest, wmsDistributionBasicDataProductionLine);
        return Results.success(list);
    }

    @ApiOperation(value = "配送物料产线表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{lineId}")
    public ResponseEntity<WmsDistributionBasicDataProductionLine> detail(@PathVariable Long lineId) {
        WmsDistributionBasicDataProductionLine wmsDistributionBasicDataProductionLine = wmsDistributionBasicDataProductionLineRepository.selectByPrimaryKey(lineId);
        return Results.success(wmsDistributionBasicDataProductionLine);
    }

    @ApiOperation(value = "创建配送物料产线表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<WmsDistributionBasicDataProductionLine> create(@RequestBody WmsDistributionBasicDataProductionLine wmsDistributionBasicDataProductionLine) {
        validObject(wmsDistributionBasicDataProductionLine);
        wmsDistributionBasicDataProductionLineRepository.insertSelective(wmsDistributionBasicDataProductionLine);
        return Results.success(wmsDistributionBasicDataProductionLine);
    }

    @ApiOperation(value = "修改配送物料产线表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<WmsDistributionBasicDataProductionLine> update(@RequestBody WmsDistributionBasicDataProductionLine wmsDistributionBasicDataProductionLine) {
        SecurityTokenHelper.validToken(wmsDistributionBasicDataProductionLine);
        wmsDistributionBasicDataProductionLineRepository.updateByPrimaryKeySelective(wmsDistributionBasicDataProductionLine);
        return Results.success(wmsDistributionBasicDataProductionLine);
    }

    @ApiOperation(value = "删除配送物料产线表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody WmsDistributionBasicDataProductionLine wmsDistributionBasicDataProductionLine) {
        SecurityTokenHelper.validToken(wmsDistributionBasicDataProductionLine);
        wmsDistributionBasicDataProductionLineRepository.deleteByPrimaryKey(wmsDistributionBasicDataProductionLine);
        return Results.success();
    }

}
