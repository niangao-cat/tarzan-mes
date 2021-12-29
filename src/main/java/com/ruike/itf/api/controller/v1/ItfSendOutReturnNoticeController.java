package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfReceiveMaterialProductionOrderReturnDTO;
import com.ruike.itf.api.dto.ItfSendOutReturnNoticeDTO;
import com.ruike.itf.app.service.ItfSendOutReturnNoticeService;
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

import java.util.List;

@RestController("ItfSendOutReturnNoticeController.v1")
@RequestMapping("/v1/itf-send-out-ifaces")
@Slf4j
public class ItfSendOutReturnNoticeController extends BaseController {

    @Autowired
    private ItfSendOutReturnNoticeService service;

    @ApiOperation(value = "发退货单通知接口列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/receive")
    public ResponseEntity<ItfReceiveMaterialProductionOrderReturnDTO> create(@RequestBody List<ItfSendOutReturnNoticeDTO> list) {
        long startTime = System.currentTimeMillis();
        log.info("<====【ItfSendOutReturnNoticeController-create】发退货单通知接口列表开始时间: {},单位毫秒", startTime);
        ItfReceiveMaterialProductionOrderReturnDTO itfReturnDTO = service.create(list);
        log.info("<====【ItfSendOutReturnNoticeController-create】发退货单通知接口列表结束时间: {},用时：{}单位毫秒", System.currentTimeMillis(), System.currentTimeMillis() - startTime);
        return Results.success(itfReturnDTO);
    }
}
