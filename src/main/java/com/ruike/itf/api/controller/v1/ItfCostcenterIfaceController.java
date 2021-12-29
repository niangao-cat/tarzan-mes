package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfCostcenterIface;
import com.ruike.itf.domain.repository.ItfCostcenterIfaceRepository;
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
 * 成本中心数据接口记录表 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-08-24 09:19:52
 */
@RestController("itfCostcenterIfaceSiteController.v1")
@RequestMapping("/v1/itf-costcenter-ifaces")
public class ItfCostcenterIfaceController extends BaseController {

    @Autowired
    private ItfCostcenterIfaceRepository itfCostcenterIfaceRepository;

    @ApiOperation(value = "成本中心数据接口记录表列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<ItfCostcenterIface>> list(ItfCostcenterIface itfCostcenterIface, @ApiIgnore @SortDefault(value = ItfCostcenterIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfCostcenterIface> list = itfCostcenterIfaceRepository.pageAndSort(pageRequest, itfCostcenterIface);
        return Results.success(list);
    }

    @ApiOperation(value = "成本中心数据接口记录表明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<ItfCostcenterIface> detail(@PathVariable Long ifaceId) {
        ItfCostcenterIface itfCostcenterIface = itfCostcenterIfaceRepository.selectByPrimaryKey(ifaceId);
        return Results.success(itfCostcenterIface);
    }

    @ApiOperation(value = "创建成本中心数据接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<ItfCostcenterIface> create(@RequestBody ItfCostcenterIface itfCostcenterIface) {
        validObject(itfCostcenterIface);
        itfCostcenterIfaceRepository.insertSelective(itfCostcenterIface);
        return Results.success(itfCostcenterIface);
    }

    @ApiOperation(value = "修改成本中心数据接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<ItfCostcenterIface> update(@RequestBody ItfCostcenterIface itfCostcenterIface) {
        SecurityTokenHelper.validToken(itfCostcenterIface);
        itfCostcenterIfaceRepository.updateByPrimaryKeySelective(itfCostcenterIface);
        return Results.success(itfCostcenterIface);
    }

    @ApiOperation(value = "删除成本中心数据接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfCostcenterIface itfCostcenterIface) {
        SecurityTokenHelper.validToken(itfCostcenterIface);
        itfCostcenterIfaceRepository.deleteByPrimaryKey(itfCostcenterIface);
        return Results.success();
    }

}
