package com.ruike.wms.api.controller.v1;

import com.ruike.wms.app.service.WmsComponentDemandRecordService;
import com.ruike.wms.domain.vo.WmsComponentDemandSumVO;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsComponentDemandRecord;
import com.ruike.wms.domain.repository.WmsComponentDemandRecordRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;

import static tarzan.config.SwaggerApiConfig.WMS_COMPONENT_DEMAND_RECORD;

/**
 * 组件需求记录表 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2020-08-24 14:00:05
 */
@RestController("wmsComponentDemandRecordController.v1")
@RequestMapping("/v1/{organizationId}/wms-component-demand-records")
@Api(tags = WMS_COMPONENT_DEMAND_RECORD)
public class WmsComponentDemandRecordController extends BaseController {

    private final WmsComponentDemandRecordRepository wmsComponentDemandRecordRepository;
    private final WmsComponentDemandRecordService wmsComponentDemandRecordService;

    public WmsComponentDemandRecordController(WmsComponentDemandRecordRepository wmsComponentDemandRecordRepository, WmsComponentDemandRecordService wmsComponentDemandRecordService) {
        this.wmsComponentDemandRecordRepository = wmsComponentDemandRecordRepository;
        this.wmsComponentDemandRecordService = wmsComponentDemandRecordService;
    }

    @ApiOperation(value = "组件需求记录表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsComponentDemandRecord>> list(@PathVariable("organizationId") Long tenantId, WmsComponentDemandRecord wmsComponentDemandRecord, @ApiIgnore @SortDefault(value = WmsComponentDemandRecord.FIELD_DEMAND_RECORD_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WmsComponentDemandRecord> list = wmsComponentDemandRecordRepository.pageAndSort(pageRequest, wmsComponentDemandRecord);
        return Results.success(list);
    }

    @ApiOperation(value = "工单组件需求查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/requirement")
    public ResponseEntity<Page<WmsComponentDemandSumVO>> requirement(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestParam("workOrderId") String workOrderId,
                                                                     @RequestParam("startDate") Date startDate,
                                                                     @ApiIgnore PageRequest pageRequest) {
        Page<WmsComponentDemandSumVO> list = wmsComponentDemandRecordService.pagedRequirement(tenantId, workOrderId, startDate, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "组件需求生成配送需求")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/distribution-demand")
    public ResponseEntity<WmsComponentDemandRecord> create(@PathVariable("organizationId") Long tenantId) {
        wmsComponentDemandRecordService.createDistributionDemand(tenantId);
        return Results.success();
    }
}
