package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfSrmMaterialWasteIface;
import com.ruike.itf.domain.repository.ItfSrmMaterialWasteIfaceRepository;
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
 * 料废调换接口记录表 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-09-21 11:05:25
 */
@RestController("itfSrmMaterialWasteIfaceSiteController.v1")
@RequestMapping("/v1/itf-srm-material-waste-ifaces")
public class ItfSrmMaterialWasteIfaceController extends BaseController {

    @Autowired
    private ItfSrmMaterialWasteIfaceRepository itfSrmMaterialWasteIfaceRepository;

    @ApiOperation(value = "料废调换接口记录表列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<ItfSrmMaterialWasteIface>> list(ItfSrmMaterialWasteIface itfSrmMaterialWasteIface, @ApiIgnore @SortDefault(value = ItfSrmMaterialWasteIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfSrmMaterialWasteIface> list = itfSrmMaterialWasteIfaceRepository.pageAndSort(pageRequest, itfSrmMaterialWasteIface);
        return Results.success(list);
    }

    @ApiOperation(value = "料废调换接口记录表明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<ItfSrmMaterialWasteIface> detail(@PathVariable Long ifaceId) {
        ItfSrmMaterialWasteIface itfSrmMaterialWasteIface = itfSrmMaterialWasteIfaceRepository.selectByPrimaryKey(ifaceId);
        return Results.success(itfSrmMaterialWasteIface);
    }

    @ApiOperation(value = "创建料废调换接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<ItfSrmMaterialWasteIface> create(@RequestBody ItfSrmMaterialWasteIface itfSrmMaterialWasteIface) {
        validObject(itfSrmMaterialWasteIface);
        itfSrmMaterialWasteIfaceRepository.insertSelective(itfSrmMaterialWasteIface);
        return Results.success(itfSrmMaterialWasteIface);
    }

    @ApiOperation(value = "修改料废调换接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<ItfSrmMaterialWasteIface> update(@RequestBody ItfSrmMaterialWasteIface itfSrmMaterialWasteIface) {
        SecurityTokenHelper.validToken(itfSrmMaterialWasteIface);
        itfSrmMaterialWasteIfaceRepository.updateByPrimaryKeySelective(itfSrmMaterialWasteIface);
        return Results.success(itfSrmMaterialWasteIface);
    }

    @ApiOperation(value = "删除料废调换接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfSrmMaterialWasteIface itfSrmMaterialWasteIface) {
        SecurityTokenHelper.validToken(itfSrmMaterialWasteIface);
        itfSrmMaterialWasteIfaceRepository.deleteByPrimaryKey(itfSrmMaterialWasteIface);
        return Results.success();
    }

}
