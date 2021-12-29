package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfSnSapIface;
import com.ruike.itf.domain.repository.ItfSnSapIfaceRepository;
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
 * 物料组接口表 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-09-04 11:31:56
 */
@RestController("itfSnSapIfaceSiteController.v1")
@RequestMapping("/v1/itf-sn-sap-ifaces")
public class ItfSnSapIfaceController extends BaseController {

    @Autowired
    private ItfSnSapIfaceRepository itfSnSapIfaceRepository;

    @ApiOperation(value = "物料组接口表列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<ItfSnSapIface>> list(ItfSnSapIface itfSnSapIface, @ApiIgnore @SortDefault(value = ItfSnSapIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfSnSapIface> list = itfSnSapIfaceRepository.pageAndSort(pageRequest, itfSnSapIface);
        return Results.success(list);
    }

    @ApiOperation(value = "物料组接口表明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<ItfSnSapIface> detail(@PathVariable Long ifaceId) {
        ItfSnSapIface itfSnSapIface = itfSnSapIfaceRepository.selectByPrimaryKey(ifaceId);
        return Results.success(itfSnSapIface);
    }

    @ApiOperation(value = "创建物料组接口表")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<ItfSnSapIface> create(@RequestBody ItfSnSapIface itfSnSapIface) {
        validObject(itfSnSapIface);
        itfSnSapIfaceRepository.insertSelective(itfSnSapIface);
        return Results.success(itfSnSapIface);
    }

    @ApiOperation(value = "修改物料组接口表")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<ItfSnSapIface> update(@RequestBody ItfSnSapIface itfSnSapIface) {
        SecurityTokenHelper.validToken(itfSnSapIface);
        itfSnSapIfaceRepository.updateByPrimaryKeySelective(itfSnSapIface);
        return Results.success(itfSnSapIface);
    }

    @ApiOperation(value = "删除物料组接口表")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfSnSapIface itfSnSapIface) {
        SecurityTokenHelper.validToken(itfSnSapIface);
        itfSnSapIfaceRepository.deleteByPrimaryKey(itfSnSapIface);
        return Results.success();
    }

}
