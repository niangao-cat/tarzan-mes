package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfReceiveMaterialProductionOrderDTO;
import com.ruike.itf.api.dto.ItfReceiveMaterialProductionOrderReturnDTO;
import com.ruike.itf.app.service.ItfReceiveMaterialProductionOrderService;
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

@RestController("ItfReceiveMaterialProductionOrderController.v1")
@RequestMapping("/v1/itf-production-order-ifaces")
@Slf4j
public class ItfReceiveMaterialProductionOrderController extends BaseController {

    @Autowired
    private ItfReceiveMaterialProductionOrderService service;

    @ApiOperation(value = "生产领退料接口列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/receive")
    public ResponseEntity<ItfReceiveMaterialProductionOrderReturnDTO> create(@RequestBody List<ItfReceiveMaterialProductionOrderDTO> list) {
        long startTime = System.currentTimeMillis();
        log.info("<====【ItfReceiveMaterialProductionOrderController-create】生产领退料接口列表开始时间: {},单位毫秒", startTime);
        ItfReceiveMaterialProductionOrderReturnDTO itfReturnDTO = service.create(list);
        log.info("<====【ItfReceiveMaterialProductionOrderController-create】生产领退料接口列表结束时间: {},用时：{}单位毫秒", System.currentTimeMillis(), System.currentTimeMillis() - startTime);
        return Results.success(itfReturnDTO);
    }

}