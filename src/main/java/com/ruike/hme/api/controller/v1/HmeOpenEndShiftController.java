package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeOpenEndShiftDTO;
import com.ruike.hme.api.dto.HmeOpenEndShiftDTO2;
import com.ruike.hme.api.dto.HmeOpenEndShiftEndCancelCommandDTO;
import com.ruike.hme.api.dto.HmeShiftDTO;
import com.ruike.hme.app.service.HmeOpenEndShiftService;
import com.ruike.hme.app.service.HmeWkcShiftAttrService;
import com.ruike.hme.domain.entity.HmeWkcShiftAttr;
import com.ruike.hme.domain.repository.HmeOpenEndShiftRepository;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.Date;
import java.util.List;

/**
 * 班组工作平台-开班结班管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-07-07 09:44:03
 */
@RestController("hmeOpenEndShiftController.v1")
@RequestMapping("/v1/{organizationId}/hme-open-end-shift")
@Api(tags = SwaggerApiConfig.HME_OPEN_END_SHIFT)
public class HmeOpenEndShiftController extends BaseController {

    @Autowired
    private HmeOpenEndShiftService hmeOpenEndShiftService;
    @Autowired
    private HmeOpenEndShiftRepository hmeOpenEndShiftRepository;
    @Autowired
    private HmeWkcShiftAttrService hmeWkcShiftAttrService;

    @ApiOperation(value = "工段下拉框")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/line", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeOpenEndShiftVO>> lineWorkellDataQuery(@PathVariable(value = "organizationId") Long tenantId) {
        return Results.success(hmeOpenEndShiftService.lineWorkellDataQuery(tenantId));
    }

    @ApiOperation(value = "根据工段查询当前班次和日期")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/current/shift", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeOpenEndShiftVO4> shiftDateAndCodeQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                    String lineWorkcellId) {
        return Results.success(hmeOpenEndShiftService.shiftDateAndCodeQuery(tenantId, lineWorkcellId));
    }

    @ApiOperation(value = "班次下拉框")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/shift", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeOpenEndShiftVO2>> shiftQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                               HmeOpenEndShiftDTO dto) {
        return Results.success(hmeOpenEndShiftService.shiftQuery(tenantId, dto));
    }

    @ApiOperation(value = "自动查询班次开结班时间")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeOpenEndShiftVO3> dateTimeQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                            HmeOpenEndShiftDTO2 dto) {
        return Results.success(hmeOpenEndShiftService.dateTimeQuery(tenantId, dto));
    }

    @ApiOperation(value = "开班")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/open/shift", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Date> openShiftActualDate(@PathVariable(value = "organizationId") Long tenantId,
                                                    @RequestBody HmeOpenEndShiftDTO2 dto) {
        return Results.success(hmeOpenEndShiftService.openShiftActualDate(tenantId, dto));
    }

    @ApiOperation(value = "结班")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/end/shift", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Date> endShiftActualDate(@PathVariable(value = "organizationId") Long tenantId,
                                                   @RequestBody HmeOpenEndShiftDTO2 dto) {
        return Results.success(hmeOpenEndShiftService.endShiftActualDate(tenantId, dto));
    }

    @ApiOperation(value = "结班撤销")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/shift/end-cancel", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Void> shiftEndCancel(@PathVariable(value = "organizationId") Long tenantId,
                                               @RequestBody HmeOpenEndShiftEndCancelCommandDTO command) {
        hmeOpenEndShiftService.shiftEndCancel(tenantId, command);
        return Results.success();
    }

    @ApiOperation(value = "班组信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/shift/data/query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeShiftVO> shiftDataQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                     HmeShiftDTO dto) {
        this.validObject(dto);
        return Results.success(hmeOpenEndShiftRepository.shiftDataQuery(tenantId, dto));
    }

    @ApiOperation(value = "完工统计")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/complete/statistical", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeShiftVO5>> completeStatistical(@PathVariable(value = "organizationId") Long tenantId,
                                                                 HmeShiftDTO dto, PageRequest pageRequest) {
        return Results.success(hmeOpenEndShiftRepository.completeStatistical(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "产品节拍")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/product/beat", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeShiftVO7> productBeat(@PathVariable(value = "organizationId") Long tenantId,
                                                   HmeShiftDTO dto) {
        return Results.success(hmeOpenEndShiftRepository.productBeat(tenantId, dto));
    }

    @ApiOperation(value = "交接注意事项提交")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/handover/matter/submit", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeWkcShiftAttr> handoverMatters(@PathVariable(value = "organizationId") Long tenantId,
                                                           @RequestBody HmeWkcShiftAttr dto) {
        return Results.success(hmeWkcShiftAttrService.createOrUpdate(tenantId, dto));
    }

    @ApiOperation(value = "交接注意事项查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/handover/matter/query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeShiftVO8> handoverMattersQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                            String wkcShiftId) {
        return Results.success(hmeOpenEndShiftRepository.handoverMattersQuery(tenantId, wkcShiftId));
    }

    @ApiOperation(value = "人员安全")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/employee/security", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeShiftVO9>> monthSecurityCalendar(@PathVariable(value = "organizationId") Long tenantId,
                                                                   String siteId, String lineWorkcellId) {
        return Results.success(hmeOpenEndShiftRepository.monthSecurityCalendar(tenantId, siteId, lineWorkcellId));
    }

    @ApiOperation(value = "工艺质量")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/operation/quality", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeShiftVO7> operationQuality(@PathVariable(value = "organizationId") Long tenantId,
                                                        HmeShiftDTO dto) {
        return Results.success(hmeOpenEndShiftRepository.operationQuality(tenantId, dto));
    }

    @ApiOperation(value = "设备管理")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/equipment/manage", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeShiftVO7> equipmentManage(@PathVariable(value = "organizationId") Long tenantId,
                                                       HmeShiftDTO dto) {
        return Results.success(hmeOpenEndShiftRepository.equipmentManage(tenantId, dto));
    }

    @ApiOperation(value = "其他异常")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/other/exception", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeShiftVO11>> otherException(@PathVariable(value = "organizationId") Long tenantId,
                                                             HmeShiftDTO dto, PageRequest pageRequest) {
        return Results.success(hmeOpenEndShiftRepository.otherException(tenantId, dto, pageRequest));
    }
}
