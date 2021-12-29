package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfFinishDeliveryInstructionIfaceDTO;
import com.ruike.itf.app.service.ItfFinishDeliveryInstructionIfaceService;
import com.ruike.itf.domain.vo.ItfFinishDeliveryInstructionIfaceVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 成品出库指令信息接口
 *
 * @author sanfeng.zhang@hand-china.com 2021/7/16 15:35
 */
@RestController("itfFinishDeliveryInstructionIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-finish-delivery-instruction-ifaces")
public class ItfFinishDeliveryInstructionIfaceController {

    @Autowired
    private ItfFinishDeliveryInstructionIfaceService itfFinishDeliveryInstructionIfaceService;

    @ApiOperation(value = "成品出库指令信息接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<ItfFinishDeliveryInstructionIfaceVO>> itfWCSTaskIface(@PathVariable(value = "organizationId") Long tenantId,
                                                                                     @RequestBody List<ItfFinishDeliveryInstructionIfaceDTO> dtoList){
        List<ItfFinishDeliveryInstructionIfaceVO> resultList = itfFinishDeliveryInstructionIfaceService.itfWCSTaskIface(tenantId, dtoList);
        return Results.success(resultList);
    }
}
