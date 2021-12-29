package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.entity.HmeRepairWorkOrderCreate;
import com.ruike.hme.domain.repository.HmeRepairWorkOrderCreateRepository;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
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
 * 工单创建 管理 API
 *
 * @author kejin.liu01@hand-china.com 2020-12-09 10:21:47
 */
        @RestController("hmeRepairWorkOrderCreateController.v1")
    @RequestMapping("/v1/{organizationId}/hme-repair-work-order-creates")
    public class HmeRepairWorkOrderCreateController extends BaseController {

    @Autowired
    private HmeRepairWorkOrderCreateRepository hmeRepairWorkOrderCreateRepository;

    @ApiOperation(value = "工单创建列表")
                @Permission(level = ResourceLevel.ORGANIZATION)
            @GetMapping
    public ResponseEntity<Page<HmeRepairWorkOrderCreate>> list(HmeRepairWorkOrderCreate hmeRepairWorkOrderCreate, @ApiIgnore @SortDefault(value = HmeRepairWorkOrderCreate.FIELD_WORK_ORDER_CREATE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeRepairWorkOrderCreate> list = hmeRepairWorkOrderCreateRepository.pageAndSort(pageRequest, hmeRepairWorkOrderCreate);
        return Results.success(list);
    }

    @ApiOperation(value = "工单创建明细")
                @Permission(level = ResourceLevel.ORGANIZATION)
            @GetMapping("/{workOrderCreateId}")
    public ResponseEntity<HmeRepairWorkOrderCreate> detail(@PathVariable Long workOrderCreateId) {
        HmeRepairWorkOrderCreate hmeRepairWorkOrderCreate =hmeRepairWorkOrderCreateRepository.selectByPrimaryKey(workOrderCreateId);
        return Results.success(hmeRepairWorkOrderCreate);
    }

    @ApiOperation(value = "创建工单创建")
                @Permission(level = ResourceLevel.ORGANIZATION)
            @PostMapping
    public ResponseEntity<HmeRepairWorkOrderCreate> create(@RequestBody HmeRepairWorkOrderCreate hmeRepairWorkOrderCreate) {
        validObject(hmeRepairWorkOrderCreate);
            hmeRepairWorkOrderCreateRepository.insertSelective(hmeRepairWorkOrderCreate);
        return Results.success(hmeRepairWorkOrderCreate);
    }

    @ApiOperation(value = "修改工单创建")
                @Permission(level = ResourceLevel.ORGANIZATION)
            @PutMapping
    public ResponseEntity<HmeRepairWorkOrderCreate> update(@RequestBody HmeRepairWorkOrderCreate hmeRepairWorkOrderCreate) {
        SecurityTokenHelper.validToken(hmeRepairWorkOrderCreate);
            hmeRepairWorkOrderCreateRepository.updateByPrimaryKeySelective(hmeRepairWorkOrderCreate);
        return Results.success(hmeRepairWorkOrderCreate);
    }

    @ApiOperation(value = "删除工单创建")
                @Permission(level = ResourceLevel.ORGANIZATION)
            @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeRepairWorkOrderCreate hmeRepairWorkOrderCreate) {
        SecurityTokenHelper.validToken(hmeRepairWorkOrderCreate);
            hmeRepairWorkOrderCreateRepository.deleteByPrimaryKey(hmeRepairWorkOrderCreate);
        return Results.success();
    }

}
