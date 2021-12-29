package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfAfterSalesRepairIfaces;
import com.ruike.itf.domain.repository.ItfAfterSalesRepairIfacesRepository;
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
 * @author kejin.liu01@hand-china.com 2020-09-02 09:59:41
 */
@RestController("itfAfterSalesRepairIfacesSiteController.v1")
@RequestMapping("/v1/itf-after-sales-repair-ifacess")
public class ItfAfterSalesRepairIfacesController extends BaseController {

    @Autowired
    private ItfAfterSalesRepairIfacesRepository itfAfterSalesRepairIfacesRepository;

    @ApiOperation(value = "售后登记平台表列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<ItfAfterSalesRepairIfaces>> list(ItfAfterSalesRepairIfaces itfAfterSalesRepairIfaces, @ApiIgnore @SortDefault(value = ItfAfterSalesRepairIfaces.FIELD_TENANT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfAfterSalesRepairIfaces> list = itfAfterSalesRepairIfacesRepository.pageAndSort(pageRequest, itfAfterSalesRepairIfaces);
        return Results.success(list);
    }

    @ApiOperation(value = "售后登记平台表明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{tenantId}")
    public ResponseEntity<ItfAfterSalesRepairIfaces> detail(@PathVariable Long tenantId) {
        ItfAfterSalesRepairIfaces itfAfterSalesRepairIfaces = itfAfterSalesRepairIfacesRepository.selectByPrimaryKey(tenantId);
        return Results.success(itfAfterSalesRepairIfaces);
    }

    @ApiOperation(value = "创建售后登记平台表")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<ItfAfterSalesRepairIfaces> create(@RequestBody ItfAfterSalesRepairIfaces itfAfterSalesRepairIfaces) {
        validObject(itfAfterSalesRepairIfaces);
        itfAfterSalesRepairIfacesRepository.insertSelective(itfAfterSalesRepairIfaces);
        return Results.success(itfAfterSalesRepairIfaces);
    }

    @ApiOperation(value = "修改售后登记平台表")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<ItfAfterSalesRepairIfaces> update(@RequestBody ItfAfterSalesRepairIfaces itfAfterSalesRepairIfaces) {
        SecurityTokenHelper.validToken(itfAfterSalesRepairIfaces);
        itfAfterSalesRepairIfacesRepository.updateByPrimaryKeySelective(itfAfterSalesRepairIfaces);
        return Results.success(itfAfterSalesRepairIfaces);
    }

    @ApiOperation(value = "删除售后登记平台表")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfAfterSalesRepairIfaces itfAfterSalesRepairIfaces) {
        SecurityTokenHelper.validToken(itfAfterSalesRepairIfaces);
        itfAfterSalesRepairIfacesRepository.deleteByPrimaryKey(itfAfterSalesRepairIfaces);
        return Results.success();
    }

}
