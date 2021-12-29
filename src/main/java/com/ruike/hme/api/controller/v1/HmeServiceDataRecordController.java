package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.domain.entity.HmeServiceDataRecord;
import com.ruike.hme.domain.repository.HmeServiceDataRecordRepository;
import com.ruike.hme.domain.vo.HmeEoJobSnVO4;
import com.ruike.hme.domain.vo.HmeServiceDataRecordVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

/**
 * 售后返品信息采集确认表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2020-09-03 15:20:59
 */
@RestController("hmeServiceDataRecordController.v1")
@RequestMapping("/v1/{organizationId}/hme-service-data-records")
@Api(tags = SwaggerApiConfig.HME_SERVICE_DATA_RECORD)
public class HmeServiceDataRecordController extends BaseController {

    @Autowired
    private HmeServiceDataRecordRepository hmeServiceDataRecordRepository;

    @ApiOperation(value = "工位扫描")
    @PostMapping(value = {"/workcell-scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEoJobSnVO4> workcellScan(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody HmeEoJobSnDTO dto) {
        return Results.success(hmeServiceDataRecordRepository.workcellScan(tenantId, dto));
    }

    @ApiOperation(value = "扫描返修序列号")
    @PostMapping(value = {"/scan-repair-code"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeServiceDataRecordVO> scanRepairCode(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody HmeServiceDataRecord record) {
        return Results.success(hmeServiceDataRecordRepository.scanRepairCode(tenantId, record));
    }


    @ApiOperation(value = "保存")
    @PostMapping(value = {"/save-record"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeServiceDataRecordVO> saveRecord(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody HmeServiceDataRecordVO record) {
        return Results.success(hmeServiceDataRecordRepository.saveRecord(tenantId, record));
    }

    @ApiOperation(value = "完成")
    @PostMapping(value = {"/complete-record"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeServiceDataRecordVO> completeRecord(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody HmeServiceDataRecordVO record) {
        return Results.success(hmeServiceDataRecordRepository.completeRecord(tenantId, record));
    }
}
