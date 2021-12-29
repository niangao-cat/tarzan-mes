package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeNcCheckService;
import com.ruike.hme.domain.entity.HmeEqManageTagGroup;
import com.ruike.hme.domain.repository.HmeNcCheckRepository;
import com.ruike.hme.domain.vo.HmeEquipmentTagVO2;
import com.ruike.hme.domain.vo.HmeModAreaVO3;
import com.ruike.hme.domain.vo.HmeNcCheckVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.entity.MtGenStatus;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @description: 不良申请单审核
 * @author: chaonan.hu@hand-china.com 2020-07-03 10:09:11
 **/
@RestController("hmeNcCheckController.v1")
@RequestMapping("/v1/{organizationId}/hme-nc-check")
@Api(tags = SwaggerApiConfig.HME_NC_CHECK)
public class HmeNcCheckController extends BaseController {

    @Autowired
    private HmeNcCheckRepository hmeNcCheckRepository;
    @Autowired
    private HmeNcCheckService hmeNcCheckService;

    @ApiOperation(value = "不良代码记录查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list")
    public ResponseEntity<Page<HmeNcDisposePlatformDTO2>> ncRecordQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                        HmeNcCheckDTO dto, PageRequest pageRequest){
        return Results.success(hmeNcCheckRepository.ncRecordQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "不良处理提交")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/submit")
    public ResponseEntity<?> ncCheck(@PathVariable(value = "organizationId") Long tenantId, @RequestBody HmeNcCheckDTO2 dto){
        hmeNcCheckService.ncCheck(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "不良代码组LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/nc/group")
    public ResponseEntity<Page<HmeNcCheckVO>> ncGroupLovQuery(@PathVariable(value = "organizationId") Long tenantId, HmeNcCheckDTO3 dto, PageRequest pageRequest) {
        return Results.success(hmeNcCheckRepository.ncGroupLovQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "不良状态下拉框")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/nc/status")
    public ResponseEntity<List<MtGenStatus>> ncStatusQuery(@PathVariable(value = "organizationId") Long tenantId) {
        return Results.success(hmeNcCheckRepository.ncStatusQuery(tenantId));
    }

    @ApiOperation(value = "事业部LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/nc/area-unit")
    public ResponseEntity<List<HmeModAreaVO3>> areaUnitQuery(@PathVariable(value = "organizationId") Long tenantId) {
        return Results.success(hmeNcCheckRepository.areaUnitQuery(tenantId));
    }

    @ApiOperation(value = "批量不良处理审核提交")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-check-submit")
    public ResponseEntity<?> batchCheckSubmit(@PathVariable(value = "organizationId") Long tenantId, @RequestBody HmeNcCheckDTO4 dto) {
        hmeNcCheckService.batchCheckSubmit(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "不良申请单导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/export")
    @ExcelExport(HmeNcDisposePlatformDTO2.class)
    public ResponseEntity<List<HmeNcDisposePlatformDTO2>> export(@PathVariable("organizationId") Long tenantId,
                                                                 HmeNcCheckDTO dto,
                                                                 ExportParam exportParam,
                                                                 HttpServletResponse response) {
        return Results.success(hmeNcCheckRepository.export(tenantId, dto));
    }
}
