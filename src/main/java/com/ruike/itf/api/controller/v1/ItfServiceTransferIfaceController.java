package com.ruike.itf.api.controller.v1;

import java.util.List;

import com.ruike.itf.app.service.ItfServiceTransferIfaceService;
import com.ruike.itf.domain.vo.ServiceTransferIfaceInvokeVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 物料组接口表 管理 API
 *
 * @author penglin.sui@hand-china.com 2021-04-02 10:13:00
 */
@RestController("itfServiceTransferIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-service-transfer-ifaces")
@Slf4j
public class ItfServiceTransferIfaceController {
    @Autowired
    private ItfServiceTransferIfaceService itfServiceTransferIfaceService;

    @ApiOperation(value = "客户机状态回传")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/ErpAfterSaleGoodMvtCreateRestProxy")
    public ResponseEntity<?> ErpAfterSaleGoodMvtCreateRestProxy(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody List<ServiceTransferIfaceInvokeVO> list){
        itfServiceTransferIfaceService.invoke(list);
        return Results.success();
    }
}
