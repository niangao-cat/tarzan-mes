package com.ruike.wms.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsWorkCenter;
import com.ruike.wms.domain.repository.WmsWorkCenterRepository;
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
 * 工作中心 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-08-27 16:17:06
 */
@RestController("wmsWorkCenterController.v1")
@RequestMapping("/v1/{organizationId}/wms-work-centers")
public class WmsWorkCenterController extends BaseController {

    @Autowired
    private WmsWorkCenterRepository wmsWorkCenterRepository;

    @ApiOperation(value = "工作中心列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsWorkCenter>> list(WmsWorkCenter wmsWorkCenter, @ApiIgnore @SortDefault(value = WmsWorkCenter.FIELD_WORK_CENTER_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WmsWorkCenter> list = wmsWorkCenterRepository.pageAndSort(pageRequest, wmsWorkCenter);
        return Results.success(list);
    }

    @ApiOperation(value = "工作中心明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{workCenterId}")
    public ResponseEntity<WmsWorkCenter> detail(@PathVariable Long workCenterId) {
        WmsWorkCenter wmsWorkCenter = wmsWorkCenterRepository.selectByPrimaryKey(workCenterId);
        return Results.success(wmsWorkCenter);
    }

    @ApiOperation(value = "创建工作中心")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<WmsWorkCenter> create(@RequestBody WmsWorkCenter wmsWorkCenter) {
        validObject(wmsWorkCenter);
        wmsWorkCenterRepository.insertSelective(wmsWorkCenter);
        return Results.success(wmsWorkCenter);
    }

    @ApiOperation(value = "修改工作中心")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<WmsWorkCenter> update(@RequestBody WmsWorkCenter wmsWorkCenter) {
        SecurityTokenHelper.validToken(wmsWorkCenter);
        wmsWorkCenterRepository.updateByPrimaryKeySelective(wmsWorkCenter);
        return Results.success(wmsWorkCenter);
    }

    @ApiOperation(value = "删除工作中心")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody WmsWorkCenter wmsWorkCenter) {
        SecurityTokenHelper.validToken(wmsWorkCenter);
        wmsWorkCenterRepository.deleteByPrimaryKey(wmsWorkCenter);
        return Results.success();
    }

}
