package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeQualificationDTO2;
import com.ruike.hme.app.service.HmeOperationAssignService;
import io.swagger.annotations.Api;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeOperationAssign;
import com.ruike.hme.domain.repository.HmeOperationAssignRepository;
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
 * 资质与工艺关系表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-06-16 19:08:35
 */
@RestController("hmeOperationAssignController.v1")
@RequestMapping("/v1/{organizationId}/hme-operation-assigns")
@Api(tags = SwaggerApiConfig.HME_OPERATION_ASSIGN)
public class HmeOperationAssignController extends BaseController {

    @Autowired
    private HmeOperationAssignRepository hmeOperationAssignRepository;
    @Autowired
    private HmeOperationAssignService hmeOperationAssignService;

    @ApiOperation(value = "资质与工艺关系表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeQualificationDTO2>> list(@PathVariable("organizationId") Long tenantId, HmeQualificationDTO2 dto, @ApiIgnore @SortDefault(value = HmeOperationAssign.FIELD_OPERATION_ASSIGN_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(hmeOperationAssignRepository.query(tenantId, dto.getOperationId(), pageRequest));
    }

//    @ApiOperation(value = "资质与工艺关系表明细")
//    @Permission(level = ResourceLevel.ORGANIZATION)
//    @GetMapping("/{operationAssignId}")
//    public ResponseEntity<HmeOperationAssign> detail(@PathVariable Long operationAssignId) {
//        HmeOperationAssign hmeOperationAssign = hmeOperationAssignRepository.selectByPrimaryKey(operationAssignId);
//        return Results.success(hmeOperationAssign);
//    }

    @ApiOperation(value = "创建资质与工艺关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<HmeOperationAssign>> create(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody List<HmeOperationAssign> hmeOperationAssignS) {
        for (HmeOperationAssign hmeOperationAssign:hmeOperationAssignS) {
            hmeOperationAssign.setTenantId(tenantId);
            validObject(hmeOperationAssign);
        }
        return Results.success(hmeOperationAssignRepository.create(hmeOperationAssignS));
    }

//    @ApiOperation(value = "修改资质与工艺关系表")
//    @Permission(level = ResourceLevel.ORGANIZATION)
//    @PutMapping
//    public ResponseEntity<HmeOperationAssign> update(@RequestBody HmeOperationAssign hmeOperationAssign) {
//        SecurityTokenHelper.validToken(hmeOperationAssign);
//        hmeOperationAssignRepository.updateByPrimaryKeySelective(hmeOperationAssign);
//        return Results.success(hmeOperationAssign);
//    }

    @ApiOperation(value = "删除资质与工艺关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody List<HmeOperationAssign> hmeOperationAssignS) {
        hmeOperationAssignRepository.batchDelete(hmeOperationAssignS);
        return Results.success();
    }

    @ApiOperation(value = "资质查询LOV")
    @GetMapping(value = "/list/lov/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeQualificationDTO2>> listForUi(@PathVariable("organizationId") Long tenantId,
                                                                HmeQualificationDTO2 dto, PageRequest pageRequest){
        return Results.success(hmeOperationAssignService.listForUi(tenantId, dto, pageRequest));
    }
}
