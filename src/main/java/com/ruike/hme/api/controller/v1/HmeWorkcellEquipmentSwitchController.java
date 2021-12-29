package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeWorkcellEquipmentSwitchService;
import com.ruike.hme.domain.repository.HmeEoJobEquipmentRepository;
import com.ruike.hme.domain.vo.HmeWkcEquSwitchVO2;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

/**
 * @description: 工位设备切换
 * @author: chaonan.hu@hand-china.com 2020-06-23 09:29:16
 **/
@RestController("hmeWorkcellEquipmentSwitchController.v1")
@RequestMapping("/v1/{organizationId}/hme-workcell-equipment-switch")
@Api(tags = SwaggerApiConfig.HME_WKC_EQUIP_SWITCH)
public class HmeWorkcellEquipmentSwitchController extends BaseController {

    @Autowired
    private HmeWorkcellEquipmentSwitchService hmeWorkcellEquipmentSwitchService;
    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;

    @ApiOperation(value = "设备类及其设备查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list")
    public ResponseEntity<HmeWkcEquSwitchVO2> wkcEquipmentQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                              HmeWkcEquSwitchDTO dto){
        return Results.success(hmeWorkcellEquipmentSwitchService.getEquCategoryAndAssetEncoding(tenantId, dto));
    }

    @ApiOperation(value = "工位设备关系绑定")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/binding", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeWkcEquSwitchDTO7> bandingStationAndEquipment(@PathVariable(value = "organizationId") Long tenantId,
                                                        @RequestBody HmeWkcEquSwitchDTO2 dto){
        return Results.success(hmeWorkcellEquipmentSwitchService.bandingStationAndEquipment(tenantId, dto));
    }

    @ApiOperation(value = "工位设备关系绑定确认")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/binding/confirm", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> confirmBandingRel(@PathVariable(value = "organizationId") Long tenantId,
                                        @RequestBody HmeWkcEquSwitchDTO2 dto){
        hmeWorkcellEquipmentSwitchService.confirmBandingRel(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "工位设备关系解绑")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping(value = "/unbinding", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> deleteStationAndEquipment(@PathVariable(value = "organizationId") Long tenantId,
                                                      @RequestBody HmeWkcEquSwitchDTO3 dto){
        hmeWorkcellEquipmentSwitchService.deleteStationAndEquipment(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "根据设备编码查询描述")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/{assetEncoding}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> getEquipmentDesc(@PathVariable(value = "organizationId") Long tenantId,
                                            @PathVariable(value = "assetEncoding") String assetEncoding){
        return Results.success(hmeWorkcellEquipmentSwitchService.getEquipmentDesc(tenantId, assetEncoding));
    }

    @ApiOperation(value = "工位设备关系更换")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/replace", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeWkcEquSwitchDTO8> replaceStationAndEquipment(@PathVariable(value = "organizationId") Long tenantId,
                                                      @RequestBody HmeWkcEquSwitchDTO4 dto){
        return Results.success(hmeWorkcellEquipmentSwitchService.replaceStationAndEquipment(tenantId, dto));
    }

    @ApiOperation(value = "工位设备关系更换确认")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/replace/confirm", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> confirmReplaceRel(@PathVariable(value = "organizationId") Long tenantId,
                                        @RequestBody HmeWkcEquSwitchDTO4 dto){
        hmeWorkcellEquipmentSwitchService.confirmReplaceRel(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "SN进出站设备状态记录")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/sn", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> snInToSiteEquipmentRecord(@PathVariable(value = "organizationId") Long tenantId,
                                                       @RequestBody HmeWkcEquSwitchDTO5 dto){
        hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, dto);
        return Results.success();
    }
}
