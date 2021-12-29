package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeAfterSalesRepair;
import com.ruike.hme.domain.repository.HmeAfterSalesRepairRepository;
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
 * 售后登记平台表 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-09-02 09:59:30
 */
@RestController("hmeAfterSalesRepairController.v1")
@RequestMapping("/v1/{organizationId}/hme-after-sales-repairs")
public class HmeAfterSalesRepairController extends BaseController {

    @Autowired
    private HmeAfterSalesRepairRepository hmeAfterSalesRepairRepository;

    @ApiOperation(value = "售后登记平台表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeAfterSalesRepair>> list(HmeAfterSalesRepair hmeAfterSalesRepair, @ApiIgnore @SortDefault(value = HmeAfterSalesRepair.FIELD_TENANT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeAfterSalesRepair> list = hmeAfterSalesRepairRepository.pageAndSort(pageRequest, hmeAfterSalesRepair);
        return Results.success(list);
    }

    @ApiOperation(value = "售后登记平台表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{tenantId}")
    public ResponseEntity<HmeAfterSalesRepair> detail(@PathVariable Long tenantId) {
        HmeAfterSalesRepair hmeAfterSalesRepair = hmeAfterSalesRepairRepository.selectByPrimaryKey(tenantId);
        return Results.success(hmeAfterSalesRepair);
    }

    @ApiOperation(value = "创建售后登记平台表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeAfterSalesRepair> create(@RequestBody HmeAfterSalesRepair hmeAfterSalesRepair) {
        validObject(hmeAfterSalesRepair);
        hmeAfterSalesRepairRepository.insertSelective(hmeAfterSalesRepair);
        return Results.success(hmeAfterSalesRepair);
    }

    @ApiOperation(value = "修改售后登记平台表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeAfterSalesRepair> update(@RequestBody HmeAfterSalesRepair hmeAfterSalesRepair) {
        SecurityTokenHelper.validToken(hmeAfterSalesRepair);
        hmeAfterSalesRepairRepository.updateByPrimaryKeySelective(hmeAfterSalesRepair);
        return Results.success(hmeAfterSalesRepair);
    }

    @ApiOperation(value = "删除售后登记平台表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeAfterSalesRepair hmeAfterSalesRepair) {
        SecurityTokenHelper.validToken(hmeAfterSalesRepair);
        hmeAfterSalesRepairRepository.deleteByPrimaryKey(hmeAfterSalesRepair);
        return Results.success();
    }

}
