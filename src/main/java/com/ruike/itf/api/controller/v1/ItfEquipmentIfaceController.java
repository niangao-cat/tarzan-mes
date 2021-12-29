package com.ruike.itf.api.controller.v1;

import com.ruike.itf.app.service.ItfEquipmentIfaceService;
import com.ruike.itf.domain.entity.ItfEquipmentIface;
import com.ruike.itf.domain.vo.ItfEquipmentReturnVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 设备台帐接口表 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2021-01-08 14:11:29
 */
@RestController("itfEquipmentIfaceController.v1")
@RequestMapping("/v1/itf-equipment-ifaces")
public class ItfEquipmentIfaceController extends BaseController {

    private final ItfEquipmentIfaceService service;

    public ItfEquipmentIfaceController(ItfEquipmentIfaceService service) {
        this.service = service;
    }

    @ApiOperation(value = "设备台帐接口表 调用")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<ItfEquipmentReturnVO>> invoke(@RequestBody List<ItfEquipmentIface> list) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        List<ItfEquipmentReturnVO> result = service.invoke(tenantId, list);
        return Results.success(result);
    }


}
