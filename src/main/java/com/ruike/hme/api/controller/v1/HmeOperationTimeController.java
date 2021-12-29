package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeOperationTimeDto;
import com.ruike.hme.api.dto.HmeOperationTimeDto2;
import com.ruike.hme.api.dto.HmeOperationTimeDto3;
import com.ruike.hme.api.dto.HmeOperationTimeDto4;
import com.ruike.hme.app.service.HmeOperationTimeService;
import com.ruike.hme.domain.entity.HmeOperationTimeMaterial;
import com.ruike.hme.domain.entity.HmeOperationTimeObject;
import com.ruike.hme.domain.vo.*;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeOperationTime;
import com.ruike.hme.domain.repository.HmeOperationTimeRepository;
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
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * 工艺时效要求表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-08-11 11:44:06
 */
@RestController("hmeOperationTimeController.v1")
@RequestMapping("/v1/{organizationId}/hme-operation-times")
@Api(tags = SwaggerApiConfig.HME_OP_TIME)
public class HmeOperationTimeController extends BaseController {

    @Autowired
    private HmeOperationTimeService hmeOperationTimeService;

    @ApiOperation(value = "查询工艺时效要求表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeOperationTimeVO>> query(@PathVariable("organizationId") Long tenantId,
                                                          HmeOperationTimeDto4 dto, @ApiIgnore @SortDefault(value = HmeOperationTime.FIELD_OPERATION_TIME_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeOperationTimeVO> list = hmeOperationTimeService.query(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "创建或者更新工艺时效要求表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<HmeOperationTime>> createOrUpdate(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody List<HmeOperationTime> hmeOperationTimeList) {
        return Results.success(hmeOperationTimeService.createOrUpdate(tenantId, hmeOperationTimeList));
    }

    @ApiOperation(value = "查询关联物料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query/material", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeOperationTimeVO2>> queryMaterial(@PathVariable("organizationId") Long tenantId,
                                                                   HmeOperationTimeDto dto, @ApiIgnore @SortDefault(value = HmeOperationTime.FIELD_OPERATION_TIME_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeOperationTimeVO2> list = hmeOperationTimeService.queryMaterial(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "创建或者更新关联物料表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/create/update/material", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeOperationTimeMaterial>> createOrUpdateMaterial(@PathVariable("organizationId") Long tenantId,
                                                                         @RequestBody List<HmeOperationTimeMaterial> list) {
        return Results.success(hmeOperationTimeService.createOrUpdateMaterial(tenantId, list));
    }

    @ApiOperation(value = "查询关联对象")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query/object", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeOperationTimeVO3>> queryObject(@PathVariable("organizationId") Long tenantId,
                                                                 HmeOperationTimeDto2 dto, @ApiIgnore @SortDefault(value = HmeOperationTime.FIELD_OPERATION_TIME_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeOperationTimeVO3> list = hmeOperationTimeService.queryObject(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "创建或者更新关联对象表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/create/update/object", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeOperationTimeObject>> createOrUpdateObject(@PathVariable("organizationId") Long tenantId,
                                                                               @RequestBody List<HmeOperationTimeObject> list) {
        return Results.success(hmeOperationTimeService.createOrUpdateObject(tenantId, list));
    }

    @ApiOperation(value = "查询工艺时效要求历史")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query/his", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeOperationTimeVO4>> queryHis(@PathVariable("organizationId") Long tenantId,
                                                              HmeOperationTimeDto3 dto, PageRequest pageRequest) {
        return Results.success(hmeOperationTimeService.queryHis(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "查询关联物料历史")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query/material/his", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeOperationTimeVO5>> queryMaterialHis(@PathVariable("organizationId") Long tenantId,
                                                              HmeOperationTimeDto3 dto, PageRequest pageRequest) {
        return Results.success(hmeOperationTimeService.queryMaterialHis(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "查询关联对象历史")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query/object/his", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeOperationTimeVO6>> queryObjectHis(@PathVariable("organizationId") Long tenantId,
                                                                      HmeOperationTimeDto3 dto, PageRequest pageRequest) {
        return Results.success(hmeOperationTimeService.queryObjectHis(tenantId, dto, pageRequest));
    }
}
