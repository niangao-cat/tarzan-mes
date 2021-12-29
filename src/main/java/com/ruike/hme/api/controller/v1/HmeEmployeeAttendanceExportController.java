package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeEmployeeAttendanceExportService;
import com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO3;
import com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO4;
import com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO5;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 工段产量报表
 *
 *@author jianfeng.xia01@hand-china.com 2020-07-27 11:28:07
 */
@RestController("hmeEmployeeAttendanceExportController.v1")
@RequestMapping("/v1/{organizationId}/hme-employee-export")
@Api(tags = SwaggerApiConfig.HME_EMPLOYEE_ATTENDANCE_EXPORT)
public class HmeEmployeeAttendanceExportController extends BaseController {

    @Autowired
    private HmeEmployeeAttendanceExportService hmeEmployeeAttendanceExportService;

    @ApiOperation(value = "头表查询逻辑")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/findOne")
    public ResponseEntity<Page<HmeEmployeeAttendanceDto>> findOneList(@PathVariable(value = "organizationId") Long tenantId,
                                                                      HmeEmployeeAttendanceDto1 dto, PageRequest pageRequest) {
        return Results.success(hmeEmployeeAttendanceExportService.headDataQuery(tenantId,dto,pageRequest));
    }
    @ApiOperation(value = "表行明细查询逻辑")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/findInfoList")
    public ResponseEntity<Page<HmeEmployeeAttendanceRecordDto>> findInfoList(@PathVariable(value = "organizationId") Long tenantId,
                                                                             HmeEmployeeAttendanceDto5 dto, PageRequest pageRequest) {
        return Results.success(hmeEmployeeAttendanceExportService.lineDataQuery(tenantId,dto,pageRequest));
    }

    @ApiOperation(value = "产线Lov-限定车间")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/prodline")
    public ResponseEntity<Page<HmeEmployeeAttendanceDto8>> prodLineLovQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                            HmeEmployeeAttendanceDto7 dto, PageRequest pageRequest){
        return Results.success(hmeEmployeeAttendanceExportService.prodLineLovQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工段Lov-限定产线")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/process")
    public ResponseEntity<Page<HmeEmployeeAttendanceDto10>> processLovQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                            HmeEmployeeAttendanceDto9 dto, PageRequest pageRequest){
        return Results.success(hmeEmployeeAttendanceExportService.processLovQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "产量明细/在制明细/返修明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value="/job", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeEmployeeAttendanceExportVO3>> jobDetailInfoQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                                   @RequestBody HmeEmployeeAttendanceDTO12 dto){
        PageRequest pageRequest = new PageRequest(dto.getPage(), dto.getSize());
        return Results.success(hmeEmployeeAttendanceExportService.jobDetailInfoQuery(tenantId, dto.getIdList(), pageRequest));
    }

    @ApiOperation(value = "不良明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value="/nc", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeEmployeeAttendanceExportVO4>> ncRecordInfoQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                                   @RequestBody HmeEmployeeAttendanceDTO12 dto){
        PageRequest pageRequest = new PageRequest(dto.getPage(), dto.getSize());
        return Results.success(hmeEmployeeAttendanceExportService.ncRecordInfoQuery(tenantId, dto.getIdList(), pageRequest));
    }

    @ApiOperation(value = "员工产量汇总报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/sum")
    public ResponseEntity<Page<HmeEmployeeAttendanceExportVO5>> sumQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                         HmeEmployeeAttendanceDTO13 dto, PageRequest pageRequest) {
        return Results.success(hmeEmployeeAttendanceExportService.sumQuery(tenantId,dto,pageRequest));
    }

    @ApiOperation(value = "员工产量汇总报表-实际产出、产量、在制数、返修数明细查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/sum/number")
    public ResponseEntity<Page<HmeEmployeeAttendanceExportVO3>> sumNumberDeatilQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                                     HmeEmployeeAttendanceDTO14 dto, PageRequest pageRequest) {
        return Results.success(hmeEmployeeAttendanceExportService.sumNumberDeatilQuery(tenantId,dto,pageRequest));
    }

    @ApiOperation(value = "员工产量汇总报表-不良数明细查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/sum/nc-number")
    public ResponseEntity<Page<HmeEmployeeAttendanceExportVO4>> sumDefectsNumQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                                     HmeEmployeeAttendanceDTO14 dto, PageRequest pageRequest) {
        return Results.success(hmeEmployeeAttendanceExportService.sumDefectsNumQuery(tenantId,dto,pageRequest));
    }

    @ApiOperation(value = "员工产量汇总报表-导出")
    @GetMapping(value = "/sum/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeEmployeeAttendanceExportVO5.class)
    public ResponseEntity<List<HmeEmployeeAttendanceExportVO5>> sumExport(@PathVariable("organizationId") Long tenantId,
                                                                          ExportParam exportParam,
                                                                          HttpServletResponse response,
                                                                          HmeEmployeeAttendanceDTO13 dto) {
        return Results.success(hmeEmployeeAttendanceExportService.sumExport(tenantId, dto));
    }

    @ApiOperation(value = "工段产量报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/line-workcell-product-export")
    @ExcelExport(HmeEmployeeAttendanceDto.class)
    public ResponseEntity<List<HmeEmployeeAttendanceDto>> lineWorkcellProductExport(@PathVariable(value = "organizationId") Long tenantId,
                                                                                    HmeEmployeeAttendanceDto1 dto,
                                                                                    ExportParam exportParam,
                                                                                    HttpServletResponse response) {
        return Results.success(hmeEmployeeAttendanceExportService.lineWorkcellProductExport(tenantId,dto));
    }

    @ApiOperation(value = "工段产量-总产量明细查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/line-workcell-product-details")
    public ResponseEntity<Page<HmeEmployeeAttendanceExportVO3>> lineWorkcellProductDetails(@PathVariable(value = "organizationId") Long tenantId,
                                                                                           HmeEmployeeAttendanceDTO15 dto, PageRequest pageRequest) {
        dto.initParam();
        return Results.success(hmeEmployeeAttendanceExportService.lineWorkcellProductDetails(tenantId,dto,pageRequest));
    }

    @ApiOperation(value = "工段产量-不良数明细查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/line-workcell-nc-details")
    public ResponseEntity<Page<HmeEmployeeAttendanceExportVO4>> lineWorkcellNcDetails(@PathVariable(value = "organizationId") Long tenantId,
                                                                                      HmeEmployeeAttendanceDTO15 dto,
                                                                                      PageRequest pageRequest) {
        dto.initParam();
        return Results.success(hmeEmployeeAttendanceExportService.lineWorkcellNcDetails(tenantId,dto,pageRequest));
    }
}
