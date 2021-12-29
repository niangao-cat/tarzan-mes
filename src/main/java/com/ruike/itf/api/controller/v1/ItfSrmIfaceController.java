package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfInstructionAddrIfaceDTO;
import com.ruike.itf.api.dto.ItfSrmMaterialWasteIfaceSyncDTO;
import com.ruike.itf.app.service.ItfInstructionAddrIfaceService;
import com.ruike.itf.app.service.ItfSrmInstructionIfaceService;
import com.ruike.itf.app.service.ItfSrmMaterialWasteIfaceService;
import com.ruike.itf.domain.entity.ItfSrmInstructionIface;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController("ItfSrmIfaceController.v1")
@RequestMapping("/v1/itf-arm-iface")
@Slf4j
public class ItfSrmIfaceController extends BaseController {

    @Autowired
    private ItfSrmInstructionIfaceService itfSrmInstructionIfaceService;

    @Autowired
    private ItfSrmMaterialWasteIfaceService itfSrmMaterialWasteIfaceService;

    @Autowired
    private ItfInstructionAddrIfaceService itfInstructionAddrIfaceService;

    @ApiOperation(value = "送货单状态回传SRM")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/send-instruction-doc-status-srm")
    public ResponseEntity<List<ItfSrmInstructionIface>> sendInstructionDocStatusSrm(@RequestBody List<ItfSrmInstructionIface> itfSrmInstructionIface) throws ParseException {
        log.info("<==== send-instruction-doc-status-srm Success requestPayload: {}", itfSrmInstructionIface);
        List<ItfSrmInstructionIface> list = itfSrmInstructionIfaceService.sendInstructionDocStatusSrm(itfSrmInstructionIface, 0L);
        return Results.success(list);
    }

    @ApiOperation(value = "SRM系统-料废调换创建Rest接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/srm-material-waste-exchange-create")
    public ResponseEntity<List<ItfSrmMaterialWasteIfaceSyncDTO>> srmMaterialWasteExchangeCreate(@RequestBody List<ItfSrmMaterialWasteIfaceSyncDTO> ifaceSyncDTOS) throws ParseException {
        log.info("<==== srm-material-waste-exchange-create Success requestPayload: {}", ifaceSyncDTOS);
        List<ItfSrmMaterialWasteIfaceSyncDTO> list = itfSrmMaterialWasteIfaceService.srmMaterialWasteExchangeCreate(ifaceSyncDTOS, 0L);
        return Results.success(list);
    }

    @ApiOperation(value = "SRM系统-送货单地址创建Rest接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/srm-instruction-addr-create")
    public ResponseEntity<List<ItfInstructionAddrIfaceDTO>> srmInstructionAddrCreate(@RequestBody List<ItfInstructionAddrIfaceDTO> ifaceSyncDTOS) {
        log.info("<==== srm-instruction-addr-create Success requestPayload: {}", ifaceSyncDTOS);
        List<ItfInstructionAddrIfaceDTO> list = itfInstructionAddrIfaceService.srmInstructionAddrCreate(ifaceSyncDTOS);
        return Results.success(list);
    }


}
