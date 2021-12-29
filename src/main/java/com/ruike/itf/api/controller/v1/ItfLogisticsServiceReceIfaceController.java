package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfLogisticsServiceReceIface;
import com.ruike.itf.domain.repository.ItfLogisticsServiceReceIfaceRepository;
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
 * 售后信息回传ERP接口记录表 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-09-02 10:49:32
 */
@RestController("itfLogisticsServiceReceIfaceSiteController.v1")
@RequestMapping("/v1/itf-logistics-service-rece-ifaces")
public class ItfLogisticsServiceReceIfaceController extends BaseController {

    @Autowired
    private ItfLogisticsServiceReceIfaceRepository itfLogisticsServiceReceIfaceRepository;

    @ApiOperation(value = "售后信息回传ERP接口记录表列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<ItfLogisticsServiceReceIface>> list(ItfLogisticsServiceReceIface itfLogisticsServiceReceIface, @ApiIgnore @SortDefault(value = ItfLogisticsServiceReceIface.FIELD_TENANT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<ItfLogisticsServiceReceIface> list = itfLogisticsServiceReceIfaceRepository.pageAndSort(pageRequest, itfLogisticsServiceReceIface);
        return Results.success(list);
    }

    @ApiOperation(value = "售后信息回传ERP接口记录表明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{tenantId}")
    public ResponseEntity<ItfLogisticsServiceReceIface> detail(@PathVariable Long tenantId) {
        ItfLogisticsServiceReceIface itfLogisticsServiceReceIface = itfLogisticsServiceReceIfaceRepository.selectByPrimaryKey(tenantId);
        return Results.success(itfLogisticsServiceReceIface);
    }

    @ApiOperation(value = "创建售后信息回传ERP接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<ItfLogisticsServiceReceIface> create(@RequestBody ItfLogisticsServiceReceIface itfLogisticsServiceReceIface) {
        validObject(itfLogisticsServiceReceIface);
        itfLogisticsServiceReceIfaceRepository.insertSelective(itfLogisticsServiceReceIface);
        return Results.success(itfLogisticsServiceReceIface);
    }

    @ApiOperation(value = "修改售后信息回传ERP接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<ItfLogisticsServiceReceIface> update(@RequestBody ItfLogisticsServiceReceIface itfLogisticsServiceReceIface) {
        SecurityTokenHelper.validToken(itfLogisticsServiceReceIface);
        itfLogisticsServiceReceIfaceRepository.updateByPrimaryKeySelective(itfLogisticsServiceReceIface);
        return Results.success(itfLogisticsServiceReceIface);
    }

    @ApiOperation(value = "删除售后信息回传ERP接口记录表")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody ItfLogisticsServiceReceIface itfLogisticsServiceReceIface) {
        SecurityTokenHelper.validToken(itfLogisticsServiceReceIface);
        itfLogisticsServiceReceIfaceRepository.deleteByPrimaryKey(itfLogisticsServiceReceIface);
        return Results.success();
    }

}
