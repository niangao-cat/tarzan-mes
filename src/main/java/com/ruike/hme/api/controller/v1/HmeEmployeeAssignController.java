package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.infra.constant.HmeConstants;
import io.swagger.annotations.Api;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeEmployeeAssign;
import com.ruike.hme.domain.repository.HmeEmployeeAssignRepository;
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
 * 人员与资质关系表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-06-16 11:13:32
 */
@RestController("hmeEmployeeAssignController.v1")
@RequestMapping("/v1/{organizationId}/hme-employee-assigns")
@Api(tags = SwaggerApiConfig.HME_EMPLOYEE_ASSIGN)
public class HmeEmployeeAssignController extends BaseController {

    @Autowired
    private HmeEmployeeAssignRepository hmeEmployeeAssignRepository;

    @ApiOperation(value = "人员与资质关系表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeEmployeeAssignDTO>> list(@PathVariable("organizationId") Long tenantId, HmeEmployeeAssignDTO2 dto, @ApiIgnore @SortDefault(value = HmeEmployeeAssign.FIELD_EMPLOYEE_ASSIGN_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(hmeEmployeeAssignRepository.query(tenantId, dto.getEmployeeId(), pageRequest));
    }

//    @ApiOperation(value = "人员与资质关系表明细")
//    @Permission(level = ResourceLevel.ORGANIZATION)
//    @GetMapping("/{employeeAssignId}")
//    public ResponseEntity<HmeEmployeeAssign> detail(@PathVariable Long employeeAssignId) {
//        HmeEmployeeAssign hmeEmployeeAssign = hmeEmployeeAssignRepository.selectByPrimaryKey(employeeAssignId);
//        return Results.success(hmeEmployeeAssign);
//    }

    @ApiOperation(value = "人员与资质关系表 保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/createOrUpdate"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeEmployeeAssign>> createOrUpdate(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody List<HmeEmployeeAssign> hmeEmployeeAssigns) {
        for (HmeEmployeeAssign hmeEmployeeAssign:hmeEmployeeAssigns) {
            hmeEmployeeAssign.setEnableFlag(HmeConstants.ConstantValue.YES);
            hmeEmployeeAssign.setTenantId(tenantId);
            validObject(hmeEmployeeAssign);
        }
        return Results.success(hmeEmployeeAssignRepository.createOrUpdate(hmeEmployeeAssigns));
    }

//    @ApiOperation(value = "修改人员与资质关系表")
//    @Permission(level = ResourceLevel.ORGANIZATION)
//    @PutMapping
//    public ResponseEntity<HmeEmployeeAssign> update(@RequestBody HmeEmployeeAssign hmeEmployeeAssign) {
//        SecurityTokenHelper.validToken(hmeEmployeeAssign);
//        hmeEmployeeAssignRepository.updateByPrimaryKeySelective(hmeEmployeeAssign);
//        return Results.success(hmeEmployeeAssign);
//    }

    @ApiOperation(value = "删除人员与资质关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody List<HmeEmployeeAssign> hmeEmployeeAssignS) {
        hmeEmployeeAssignRepository.batchDeleteByPrimaryKey(hmeEmployeeAssignS);
        return Results.success();
    }

    @ApiOperation(value = "员工资质查询LOV")
    @GetMapping(value = "/list/lov/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeQualificationDTO3>> listForUi(@PathVariable("organizationId") Long tenantId,
                                                                HmeQualificationDTO3 dto, PageRequest pageRequest){
        return Results.success(hmeEmployeeAssignRepository.listForUi(tenantId, dto, pageRequest));
    }
}
