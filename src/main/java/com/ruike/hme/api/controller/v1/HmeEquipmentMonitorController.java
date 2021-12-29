package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeEquipmentMonitorService;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * 设备监控平台
 *
 * @author chaonan.hu@hand-china.com 2020-07-16 18:40:11
 */
@RestController("hmeEquipmentMonitorController.v1")
@RequestMapping("/v1/{organizationId}/hme-equipment-monitor")
@Api(tags = SwaggerApiConfig.HME_EQUIPMENT_MONITOR)
public class HmeEquipmentMonitorController extends BaseController {

    @Autowired
    private HmeEquipmentMonitorService hmeEquipmentMonitorService;

    @ApiOperation(value = "事业部下拉框")
    @GetMapping(value = "/department", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeEquipmentMonitorVO>> departmentDataQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                           String siteId,
                                                                           String areaCategory){
        return Results.success(hmeEquipmentMonitorService.departmentDataQuery(tenantId, siteId, areaCategory));
    }

    @ApiOperation(value = "车间下拉框")
    @GetMapping(value = "/workshop", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeEquipmentMonitorVO2>> workshopDataQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                            String siteId, String departmentId){
        return Results.success(hmeEquipmentMonitorService.workshopDataQuery(tenantId, siteId, departmentId));
    }

    @ApiOperation(value = "产线下拉框")
    @GetMapping(value = "/prodline", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeEquipmentMonitorVO3>> prodLineDataQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                          String siteId, String workshopId){
        return Results.success(hmeEquipmentMonitorService.prodLineDataQuery(tenantId, siteId, workshopId));
    }

    @ApiOperation(value = "总查询")
    @GetMapping(value = "/equipment/total", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEquipmentMonitorVO6> equipmentStatusQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                       String siteId, String prodLineId){
        return Results.success(hmeEquipmentMonitorService.equipmentStatusQuery(tenantId, siteId, prodLineId));
    }

    @ApiOperation(value = "停机设备详情查询")
    @GetMapping(value = "/equipment", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEquipmentMonitorVO12> equipmentDetailQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                        HmeEquipmentMonitorVO8 dto){
        return Results.success(hmeEquipmentMonitorService.equipmentDetailQuery(tenantId, dto));
    }
}
