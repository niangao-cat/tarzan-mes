package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfErpReturnWoStatusIface;
import com.ruike.itf.domain.repository.ItfErpReturnWoStatusIfaceRepository;
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
 * ERP返回生产订单状态 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-08-27 13:53:32
 */
@RestController("itfErpReturnWoStatusIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-erp-return-wo-status-ifaces")
public class ItfErpReturnWoStatusIfaceController extends BaseController {

    @Autowired
    private ItfErpReturnWoStatusIfaceRepository itfErpReturnWoStatusIfaceRepository;

    @ApiOperation(value = "ERP返回生产订单状态列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<ItfErpReturnWoStatusIface>> list(ItfErpReturnWoStatusIface itfErpReturnWoStatusIface, @ApiIgnore @SortDefault(value = ItfErpReturnWoStatusIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfErpReturnWoStatusIface> list = itfErpReturnWoStatusIfaceRepository.pageAndSort(pageRequest, itfErpReturnWoStatusIface);
        return Results.success(list);
    }

    @ApiOperation(value = "ERP返回生产订单状态明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<ItfErpReturnWoStatusIface> detail(@PathVariable Long ifaceId) {
        ItfErpReturnWoStatusIface itfErpReturnWoStatusIface = itfErpReturnWoStatusIfaceRepository.selectByPrimaryKey(ifaceId);
        return Results.success(itfErpReturnWoStatusIface);
    }

    @ApiOperation(value = "创建ERP返回生产订单状态")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<ItfErpReturnWoStatusIface> create(@RequestBody ItfErpReturnWoStatusIface itfErpReturnWoStatusIface) {
        validObject(itfErpReturnWoStatusIface);
        itfErpReturnWoStatusIfaceRepository.insertSelective(itfErpReturnWoStatusIface);
        return Results.success(itfErpReturnWoStatusIface);
    }

    @ApiOperation(value = "修改ERP返回生产订单状态")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<ItfErpReturnWoStatusIface> update(@RequestBody ItfErpReturnWoStatusIface itfErpReturnWoStatusIface) {
        SecurityTokenHelper.validToken(itfErpReturnWoStatusIface);
        itfErpReturnWoStatusIfaceRepository.updateByPrimaryKeySelective(itfErpReturnWoStatusIface);
        return Results.success(itfErpReturnWoStatusIface);
    }

    @ApiOperation(value = "删除ERP返回生产订单状态")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfErpReturnWoStatusIface itfErpReturnWoStatusIface) {
        SecurityTokenHelper.validToken(itfErpReturnWoStatusIface);
        itfErpReturnWoStatusIfaceRepository.deleteByPrimaryKey(itfErpReturnWoStatusIface);
        return Results.success();
    }

}
