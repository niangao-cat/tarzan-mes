package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfMaterialVersionIface;
import com.ruike.itf.domain.repository.ItfMaterialVersionIfaceRepository;
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
 * 物料版本接口记录表 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-09-28 15:14:35
 */
        @RestController("itfMaterialVersionIfaceSiteController.v1")
    @RequestMapping("/v1/itf-material-version-ifaces")
    public class ItfMaterialVersionIfaceController extends BaseController {

    @Autowired
    private ItfMaterialVersionIfaceRepository itfMaterialVersionIfaceRepository;

    @ApiOperation(value = "物料版本接口记录表列表")
                @Permission(level = ResourceLevel.SITE)
            @GetMapping
    public ResponseEntity<Page<ItfMaterialVersionIface>> list(ItfMaterialVersionIface itfMaterialVersionIface, @ApiIgnore @SortDefault(value = ItfMaterialVersionIface.FIELD_TENANT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfMaterialVersionIface> list = itfMaterialVersionIfaceRepository.pageAndSort(pageRequest, itfMaterialVersionIface);
        return Results.success(list);
    }

    @ApiOperation(value = "物料版本接口记录表明细")
                @Permission(level = ResourceLevel.SITE)
            @GetMapping("/{tenantId}")
    public ResponseEntity<ItfMaterialVersionIface> detail(@PathVariable Long tenantId) {
        ItfMaterialVersionIface itfMaterialVersionIface =itfMaterialVersionIfaceRepository.selectByPrimaryKey(tenantId);
        return Results.success(itfMaterialVersionIface);
    }

    @ApiOperation(value = "创建物料版本接口记录表")
                @Permission(level = ResourceLevel.SITE)
            @PostMapping
    public ResponseEntity<ItfMaterialVersionIface> create(@RequestBody ItfMaterialVersionIface itfMaterialVersionIface) {
        validObject(itfMaterialVersionIface);
            itfMaterialVersionIfaceRepository.insertSelective(itfMaterialVersionIface);
        return Results.success(itfMaterialVersionIface);
    }

    @ApiOperation(value = "修改物料版本接口记录表")
                @Permission(level = ResourceLevel.SITE)
            @PutMapping
    public ResponseEntity<ItfMaterialVersionIface> update(@RequestBody ItfMaterialVersionIface itfMaterialVersionIface) {
        SecurityTokenHelper.validToken(itfMaterialVersionIface);
            itfMaterialVersionIfaceRepository.updateByPrimaryKeySelective(itfMaterialVersionIface);
        return Results.success(itfMaterialVersionIface);
    }

    @ApiOperation(value = "删除物料版本接口记录表")
                @Permission(level = ResourceLevel.SITE)
            @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfMaterialVersionIface itfMaterialVersionIface) {
        SecurityTokenHelper.validToken(itfMaterialVersionIface);
            itfMaterialVersionIfaceRepository.deleteByPrimaryKey(itfMaterialVersionIface);
        return Results.success();
    }

}
