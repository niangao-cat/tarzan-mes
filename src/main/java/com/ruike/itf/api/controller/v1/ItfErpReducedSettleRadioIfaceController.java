package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfErpReducedSettleRadioIface;
import com.ruike.itf.domain.repository.ItfErpReducedSettleRadioIfaceRepository;
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
 * 工单降阶品结算比例接口记录表 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-09-10 09:25:24
 */
@RestController("itfErpReducedSettleRadioIfaceSiteController.v1")
@RequestMapping("/v1/itf-erp-reduced-settle-radio-ifaces")
public class ItfErpReducedSettleRadioIfaceController extends BaseController {

    @Autowired
    private ItfErpReducedSettleRadioIfaceRepository itfErpReducedSettleRadioIfaceRepository;

    @ApiOperation(value = "工单降阶品结算比例接口记录表列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<ItfErpReducedSettleRadioIface>> list(ItfErpReducedSettleRadioIface itfErpReducedSettleRadioIface, @ApiIgnore @SortDefault(value = ItfErpReducedSettleRadioIface.FIELD_IFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfErpReducedSettleRadioIface> list = itfErpReducedSettleRadioIfaceRepository.pageAndSort(pageRequest, itfErpReducedSettleRadioIface);
        return Results.success(list);
    }

    @ApiOperation(value = "工单降阶品结算比例接口记录表明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{ifaceId}")
    public ResponseEntity<ItfErpReducedSettleRadioIface> detail(@PathVariable Long ifaceId) {
        ItfErpReducedSettleRadioIface itfErpReducedSettleRadioIface = itfErpReducedSettleRadioIfaceRepository.selectByPrimaryKey(ifaceId);
        return Results.success(itfErpReducedSettleRadioIface);
    }

    @ApiOperation(value = "创建工单降阶品结算比例接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<ItfErpReducedSettleRadioIface> create(@RequestBody ItfErpReducedSettleRadioIface itfErpReducedSettleRadioIface) {
        validObject(itfErpReducedSettleRadioIface);
        itfErpReducedSettleRadioIfaceRepository.insertSelective(itfErpReducedSettleRadioIface);
        return Results.success(itfErpReducedSettleRadioIface);
    }

    @ApiOperation(value = "修改工单降阶品结算比例接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<ItfErpReducedSettleRadioIface> update(@RequestBody ItfErpReducedSettleRadioIface itfErpReducedSettleRadioIface) {
        SecurityTokenHelper.validToken(itfErpReducedSettleRadioIface);
        itfErpReducedSettleRadioIfaceRepository.updateByPrimaryKeySelective(itfErpReducedSettleRadioIface);
        return Results.success(itfErpReducedSettleRadioIface);
    }

    @ApiOperation(value = "删除工单降阶品结算比例接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfErpReducedSettleRadioIface itfErpReducedSettleRadioIface) {
        SecurityTokenHelper.validToken(itfErpReducedSettleRadioIface);
        itfErpReducedSettleRadioIfaceRepository.deleteByPrimaryKey(itfErpReducedSettleRadioIface);
        return Results.success();
    }

}
