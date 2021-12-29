package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfDeliveryDocLineIface;
import com.ruike.itf.domain.repository.ItfDeliveryDocLineIfaceRepository;
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
 * 送货单接口行表 管理 API
 *
 * @author yapeng.yao@hand-china.com 2020-09-04 16:29:21
 */
@RestController("itfDeliveryDocLineIfaceSiteController.v1")
@RequestMapping("/v1/itf-delivery-doc-line-ifaces")
public class ItfDeliveryDocLineIfaceController extends BaseController {

    @Autowired
    private ItfDeliveryDocLineIfaceRepository itfDeliveryDocLineIfaceRepository;

    @ApiOperation(value = "送货单接口行表列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<ItfDeliveryDocLineIface>> list(ItfDeliveryDocLineIface itfDeliveryDocLineIface, @ApiIgnore @SortDefault(value = ItfDeliveryDocLineIface.FIELD_INTERFACE_LINE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfDeliveryDocLineIface> list = itfDeliveryDocLineIfaceRepository.pageAndSort(pageRequest, itfDeliveryDocLineIface);
        return Results.success(list);
    }

    @ApiOperation(value = "送货单接口行表明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{interfaceLineId}")
    public ResponseEntity<ItfDeliveryDocLineIface> detail(@PathVariable Long interfaceLineId) {
        ItfDeliveryDocLineIface itfDeliveryDocLineIface = itfDeliveryDocLineIfaceRepository.selectByPrimaryKey(interfaceLineId);
        return Results.success(itfDeliveryDocLineIface);
    }

    @ApiOperation(value = "创建送货单接口行表")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<ItfDeliveryDocLineIface> create(@RequestBody ItfDeliveryDocLineIface itfDeliveryDocLineIface) {
        validObject(itfDeliveryDocLineIface);
        itfDeliveryDocLineIfaceRepository.insertSelective(itfDeliveryDocLineIface);
        return Results.success(itfDeliveryDocLineIface);
    }

    @ApiOperation(value = "修改送货单接口行表")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<ItfDeliveryDocLineIface> update(@RequestBody ItfDeliveryDocLineIface itfDeliveryDocLineIface) {
        SecurityTokenHelper.validToken(itfDeliveryDocLineIface);
        itfDeliveryDocLineIfaceRepository.updateByPrimaryKeySelective(itfDeliveryDocLineIface);
        return Results.success(itfDeliveryDocLineIface);
    }

    @ApiOperation(value = "删除送货单接口行表")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfDeliveryDocLineIface itfDeliveryDocLineIface) {
        SecurityTokenHelper.validToken(itfDeliveryDocLineIface);
        itfDeliveryDocLineIfaceRepository.deleteByPrimaryKey(itfDeliveryDocLineIface);
        return Results.success();
    }

}
