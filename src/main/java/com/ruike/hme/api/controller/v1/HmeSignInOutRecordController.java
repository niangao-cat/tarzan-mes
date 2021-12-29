package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeSignInOutRecordService;
import com.ruike.hme.domain.entity.HmeSignInOutRecord;
import com.ruike.hme.domain.vo.HmeSignInOutRecordVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * 员工上下岗记录表 管理 API
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-13 11:28:07
 */
@RestController("HmeSignInOutRecordController.v1")
@RequestMapping("/v1/{organizationId}/hme-sign-in-out-records")
@Api(tags = SwaggerApiConfig.HME_SIGN_IN_OUT_RECORD)
public class HmeSignInOutRecordController extends BaseController {

    @Autowired
    private HmeSignInOutRecordService hmeSignInOutRecordService;

    @ApiOperation(value = "获取员工上下岗员工信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/user-info")
    public ResponseEntity<HmeSignInOutRecordDTO1> getUserQuery(@ApiParam(required = true, value = "租户id")@PathVariable(value = "organizationId") Long tenantId,
                                                               @ApiParam(required = true, value = "用户id") Long userId
    		) {
        return Results.success(hmeSignInOutRecordService.getUserQuery(tenantId,userId));
    }
    
    @ApiOperation(value = "员工上下岗获取考勤信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/user-attendance")
    public ResponseEntity<List<HmeSignInOutRecordDTO4>> getUserAttendanceQuery(@PathVariable(value = "organizationId") Long tenantId,
    		HmeSignInOutRecordDTO10 dto) {
        return Results.success(hmeSignInOutRecordService.getUserAttendanceQuery(tenantId,dto));
    }
    
    @ApiOperation(value = "上下岗班次信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/user-frequency")
    public ResponseEntity<List<HmeSignInOutRecordDTO6>> getUserFrequencyQuery(@PathVariable(value = "organizationId") Long tenantId,
    		HmeSignInOutRecordDTO5 dto) {
        return Results.success(hmeSignInOutRecordService.getUserFrequencyQuery(tenantId,dto));
    }
    
    
    @ApiOperation(value = "员工上下岗按钮操作")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/record-creat")
    public ResponseEntity<HmeSignInOutRecord> creat(@PathVariable(value = "organizationId") Long tenantId, @RequestBody HmeSignInOutRecord hmeSignInOutRecord) {
        validObject(hmeSignInOutRecord);
        return Results.success(hmeSignInOutRecordService.creat(tenantId, hmeSignInOutRecord));
    }
    
    @ApiOperation(value = "员工上下岗记录列表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/record-list")
    public ResponseEntity<Page<HmeSignInOutRecordDTO8>> hmeSignInOutRecordListQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                                    HmeSignInOutRecordDTO9 hmeSignInOutRecordDto9, PageRequest pageRequest) {
        return Results.success(hmeSignInOutRecordService.hmeSignInOutRecordListQuery(tenantId, hmeSignInOutRecordDto9,pageRequest));
    }

    @ApiOperation(value = "员工出勤列表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/sign-in-out-list")
    @ProcessLovValue
    public ResponseEntity<Page<HmeSignInOutRecordVO>> listQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                HmeSignInOutRecordDTO12 dto, PageRequest pageRequest) {
        return Results.success(hmeSignInOutRecordService.listQuery(tenantId, dto,pageRequest));
    }
    

}
