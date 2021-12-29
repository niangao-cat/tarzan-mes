package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO;
import com.ruike.itf.app.service.ItfSoDeliveryChanOrPostIfaceService;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanOrPostIface;
import com.ruike.itf.domain.entity.ItfSrmInstructionIface;
import com.ruike.itf.domain.repository.ItfSoDeliveryChanOrPostIfaceRepository;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 交货单修改过账接口头表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2021-07-09 16:58:10
 */
@RestController("itfSoDeliveryChanOrPostIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-so-delivery-chan-or-post-ifaces")
@Slf4j
public class ItfSoDeliveryChanOrPostIfaceController extends BaseController {

    @Autowired
    private ItfSoDeliveryChanOrPostIfaceService itfSoDeliveryChanOrPostIfaceService;

    @ApiOperation(value = "发货通知单修改回传&拣配接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<ItfSrmInstructionIface>> soDeliveryChangeOrPostIface(@PathVariable("organizationId") Long tenantId,
                                                                                    @RequestBody List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostList) {
        log.info("<==== soDeliveryChangeOrPostIface Success requestPayload: {}", itfSoDeliveryChanOrPostList);
        itfSoDeliveryChanOrPostIfaceService.soDeliveryChangeOrPostIface(tenantId, itfSoDeliveryChanOrPostList);
        return Results.success();
    }

}
