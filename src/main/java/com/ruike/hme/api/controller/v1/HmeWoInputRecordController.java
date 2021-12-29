package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeWoInputRecordDTO;
import com.ruike.hme.api.dto.HmeWoInputRecordDTO2;
import com.ruike.hme.api.dto.HmeWoInputRecordDTO3;
import com.ruike.hme.api.dto.HmeWoInputRecordDTO5;
import com.ruike.hme.app.service.HmeWoInputRecordService;
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

/**
 * 工单投料记录表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-10-27 17:41:58
 */
@RestController("hmeWoInputRecordController.v1")
@RequestMapping("/v1/{organizationId}/hme-wo-input-records")
@Api(tags = SwaggerApiConfig.HME_WO_INPUT_RECORD)
public class HmeWoInputRecordController extends BaseController {

    @Autowired
    private HmeWoInputRecordService hmeWoInputRecordService;

    @ApiOperation(value = "查询工单信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/wo/{workOrderNum}")
    public ResponseEntity<HmeWoInputRecordDTO> workOrderForUi(@PathVariable("organizationId") Long tenantId,
                                                              @PathVariable String workOrderNum) {
        HmeWoInputRecordDTO dto = hmeWoInputRecordService.workOrderForUi(tenantId, workOrderNum);
        return Results.success(dto);
    }

    @ApiOperation(value = "工单投料记录明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/record")
    public ResponseEntity<Page<HmeWoInputRecordDTO2>> woInputRecordForUi(@PathVariable("organizationId") Long tenantId,
                                                                         HmeWoInputRecordDTO3 dto, PageRequest pageRequest) {
        Page<HmeWoInputRecordDTO2> list = hmeWoInputRecordService.woInputRecordForUi(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "条码扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/code-scan")
    public ResponseEntity<HmeWoInputRecordDTO2> materialLotScanForUi(@PathVariable("organizationId") Long tenantId,
                                                                     HmeWoInputRecordDTO3 dto) {
        this.validObject(dto);
        HmeWoInputRecordDTO2 recordDTO = hmeWoInputRecordService.materialLotScanForUi(tenantId, dto);
        return Results.success(recordDTO);
    }

    @ApiOperation(value = "工单投料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/wo-input")
    public ResponseEntity<HmeWoInputRecordDTO5> woInputForUi(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody HmeWoInputRecordDTO5 dto) {
        return Results.success(hmeWoInputRecordService.woInputForUi(tenantId, dto));
    }

    @ApiOperation(value = "工单退料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/wo-output")
    public ResponseEntity<HmeWoInputRecordDTO5> woOutputForUi(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody HmeWoInputRecordDTO5 dto) {
        return Results.success(hmeWoInputRecordService.woOutputForUi(tenantId, dto));
    }
}
