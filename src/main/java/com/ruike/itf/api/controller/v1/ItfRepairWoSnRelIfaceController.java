package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfRepairWoSnRelIface;
import com.ruike.itf.domain.repository.ItfRepairWoSnRelIfaceRepository;
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
 * 工单和SN关系接口记录表 管理 API
 *
 * @author kejin.liu@hand-china.com 2020-09-16 15:56:49
 */
@RestController("itfRepairWoSnRelIfaceSiteController.v1")
@RequestMapping("/v1/itf-repair-wo-sn-rel-ifaces")
public class ItfRepairWoSnRelIfaceController extends BaseController {

    @Autowired
    private ItfRepairWoSnRelIfaceRepository itfRepairWoSnRelIfaceRepository;

    @ApiOperation(value = "工单和SN关系接口记录表列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<ItfRepairWoSnRelIface>> list(ItfRepairWoSnRelIface itfRepairWoSnRelIface, @ApiIgnore @SortDefault(value = ItfRepairWoSnRelIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfRepairWoSnRelIface> list = itfRepairWoSnRelIfaceRepository.pageAndSort(pageRequest, itfRepairWoSnRelIface);
        return Results.success(list);
    }

    @ApiOperation(value = "工单和SN关系接口记录表明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<ItfRepairWoSnRelIface> detail(@PathVariable Long ifaceId) {
        ItfRepairWoSnRelIface itfRepairWoSnRelIface = itfRepairWoSnRelIfaceRepository.selectByPrimaryKey(ifaceId);
        return Results.success(itfRepairWoSnRelIface);
    }

    @ApiOperation(value = "创建工单和SN关系接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<ItfRepairWoSnRelIface> create(@RequestBody ItfRepairWoSnRelIface itfRepairWoSnRelIface) {
        validObject(itfRepairWoSnRelIface);
        itfRepairWoSnRelIfaceRepository.insertSelective(itfRepairWoSnRelIface);
        return Results.success(itfRepairWoSnRelIface);
    }

    @ApiOperation(value = "修改工单和SN关系接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<ItfRepairWoSnRelIface> update(@RequestBody ItfRepairWoSnRelIface itfRepairWoSnRelIface) {
        SecurityTokenHelper.validToken(itfRepairWoSnRelIface);
        itfRepairWoSnRelIfaceRepository.updateByPrimaryKeySelective(itfRepairWoSnRelIface);
        return Results.success(itfRepairWoSnRelIface);
    }

    @ApiOperation(value = "删除工单和SN关系接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfRepairWoSnRelIface itfRepairWoSnRelIface) {
        SecurityTokenHelper.validToken(itfRepairWoSnRelIface);
        itfRepairWoSnRelIfaceRepository.deleteByPrimaryKey(itfRepairWoSnRelIface);
        return Results.success();
    }

}
