package com.ruike.itf.api.controller.v1;

import com.ruike.itf.app.service.ItfSrmInstructionIfaceService;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfSrmInstructionIface;
import com.ruike.itf.domain.repository.ItfSrmInstructionIfaceRepository;
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

import java.util.List;

/**
 * 送货单状态接口记录表 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-09-09 10:05:04
 */
@RestController("itfSrmInstructionIfaceSiteController.v1")
@RequestMapping("/v1/itf-srm-instruction-ifaces")
public class ItfSrmInstructionIfaceController extends BaseController {

    @Autowired
    private ItfSrmInstructionIfaceRepository itfSrmInstructionIfaceRepository;

    @Autowired
    private ItfSrmInstructionIfaceService itfSrmInstructionIfaceService;

    @ApiOperation(value = "送货单状态回传SRM")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("send-instruction-doc-status-srm")
    public ResponseEntity<List<ItfSrmInstructionIface>> sendInstructionDocStatusSrm(List<ItfSrmInstructionIface> itfSrmInstructionIface) {
        List<ItfSrmInstructionIface> list = itfSrmInstructionIfaceService.sendInstructionDocStatusSrm(itfSrmInstructionIface, 0L);
        return Results.success(list);
    }

    @ApiOperation(value = "送货单状态接口记录表列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<ItfSrmInstructionIface>> list(ItfSrmInstructionIface itfSrmInstructionIface, @ApiIgnore @SortDefault(value = ItfSrmInstructionIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfSrmInstructionIface> list = itfSrmInstructionIfaceRepository.pageAndSort(pageRequest, itfSrmInstructionIface);
        return Results.success(list);
    }

    @ApiOperation(value = "送货单状态接口记录表明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<ItfSrmInstructionIface> detail(@PathVariable Long ifaceId) {
        ItfSrmInstructionIface itfSrmInstructionIface = itfSrmInstructionIfaceRepository.selectByPrimaryKey(ifaceId);
        return Results.success(itfSrmInstructionIface);
    }

    @ApiOperation(value = "创建送货单状态接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<ItfSrmInstructionIface> create(@RequestBody ItfSrmInstructionIface itfSrmInstructionIface) {
        validObject(itfSrmInstructionIface);
        itfSrmInstructionIfaceRepository.insertSelective(itfSrmInstructionIface);
        return Results.success(itfSrmInstructionIface);
    }

    @ApiOperation(value = "修改送货单状态接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<ItfSrmInstructionIface> update(@RequestBody ItfSrmInstructionIface itfSrmInstructionIface) {
        SecurityTokenHelper.validToken(itfSrmInstructionIface);
        itfSrmInstructionIfaceRepository.updateByPrimaryKeySelective(itfSrmInstructionIface);
        return Results.success(itfSrmInstructionIface);
    }

    @ApiOperation(value = "删除送货单状态接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfSrmInstructionIface itfSrmInstructionIface) {
        SecurityTokenHelper.validToken(itfSrmInstructionIface);
        itfSrmInstructionIfaceRepository.deleteByPrimaryKey(itfSrmInstructionIface);
        return Results.success();
    }

}
