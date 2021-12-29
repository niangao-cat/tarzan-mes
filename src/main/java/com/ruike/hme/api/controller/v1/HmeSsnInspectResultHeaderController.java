package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.domain.entity.HmeServiceDataRecord;
import com.ruike.hme.domain.entity.HmeSsnInspectResultHeader;
import com.ruike.hme.domain.repository.HmeSsnInspectResultHeaderRepository;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标准件检验结果头 管理 API
 *
 * @author sanfeng.zhang@hand-china 2021-02-04 14:51:27
 */
@RestController("hmeSsnInspectResultHeaderController.v1")
@RequestMapping("/v1/{organizationId}/hme-ssn-inspect-result-headers")
public class HmeSsnInspectResultHeaderController extends BaseController {

    @Autowired
    private HmeSsnInspectResultHeaderRepository hmeSsnInspectResultHeaderRepository;

    @ApiOperation(value = "工位扫描")
    @PostMapping(value = {"/workcell-scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeSsnInspectResultVO4> workcellScan(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody HmeEoJobSnDTO dto) {
        return Results.success(hmeSsnInspectResultHeaderRepository.workcellScan(tenantId, dto));
    }

    @ApiOperation(value = "标准件检验列表查询")
    @PostMapping(value = {"/query-ssn-inspect-tag"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeSsnInspectResultVO2>> querySsnInspectTag(@PathVariable("organizationId") Long tenantId,
                                                                           @RequestBody HmeSsnInspectResultVO resultVO) {
        return Results.success(hmeSsnInspectResultHeaderRepository.querySsnInspectTag(tenantId, resultVO));
    }

    @ApiOperation(value = "录入检验结果")
    @PostMapping(value = {"/save-ssn-inspect-result"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeSsnInspectResultVO2> saveSsnInspectResult(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody HmeSsnInspectResultVO2 resultVO2) {
        return Results.success(hmeSsnInspectResultHeaderRepository.saveSsnInspectResult(tenantId, resultVO2));
    }

    @ApiOperation(value = "提交")
    @PostMapping(value = {"/ssn-inspect-result-submit"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> ssnInspectResultSubmit(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody HmeSsnInspectResultVO3 resultVO3) {
        hmeSsnInspectResultHeaderRepository.ssnInspectResultSubmit(tenantId, resultVO3);
        return Results.success();
    }


}
