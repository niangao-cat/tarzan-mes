package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfModLocatorIface;
import com.ruike.itf.domain.repository.ItfModLocatorIfaceRepository;
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
 * 仓库接口记录表 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-09-07 15:05:17
 */
@RestController("itfModLocatorIfaceSiteController.v1")
@RequestMapping("/v1/itf-mod-locator-ifaces")
public class ItfModLocatorIfaceController extends BaseController {

    @Autowired
    private ItfModLocatorIfaceRepository itfModLocatorIfaceRepository;

    @ApiOperation(value = "仓库接口记录表列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<ItfModLocatorIface>> list(ItfModLocatorIface itfModLocatorIface, @ApiIgnore @SortDefault(value = ItfModLocatorIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfModLocatorIface> list = itfModLocatorIfaceRepository.pageAndSort(pageRequest, itfModLocatorIface);
        return Results.success(list);
    }

    @ApiOperation(value = "仓库接口记录表明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<ItfModLocatorIface> detail(@PathVariable Long ifaceId) {
        ItfModLocatorIface itfModLocatorIface = itfModLocatorIfaceRepository.selectByPrimaryKey(ifaceId);
        return Results.success(itfModLocatorIface);
    }

    @ApiOperation(value = "创建仓库接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<ItfModLocatorIface> create(@RequestBody ItfModLocatorIface itfModLocatorIface) {
        validObject(itfModLocatorIface);
        itfModLocatorIfaceRepository.insertSelective(itfModLocatorIface);
        return Results.success(itfModLocatorIface);
    }

    @ApiOperation(value = "修改仓库接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<ItfModLocatorIface> update(@RequestBody ItfModLocatorIface itfModLocatorIface) {
        SecurityTokenHelper.validToken(itfModLocatorIface);
        itfModLocatorIfaceRepository.updateByPrimaryKeySelective(itfModLocatorIface);
        return Results.success(itfModLocatorIface);
    }

    @ApiOperation(value = "删除仓库接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfModLocatorIface itfModLocatorIface) {
        SecurityTokenHelper.validToken(itfModLocatorIface);
        itfModLocatorIfaceRepository.deleteByPrimaryKey(itfModLocatorIface);
        return Results.success();
    }

}
