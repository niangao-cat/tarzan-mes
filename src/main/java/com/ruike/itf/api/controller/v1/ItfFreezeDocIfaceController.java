package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.FreezeDocRequestDTO;
import com.ruike.itf.api.dto.FreezeDocResponseDTO;
import com.ruike.itf.app.service.ItfFreezeDocIfaceService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 条码冻结接口表 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2021-03-03 10:08:00
 */
@RestController("itfFreezeDocIfaceController.v1")
@RequestMapping("/v1/itf-freeze-doc-ifaces")
public class ItfFreezeDocIfaceController extends BaseController {

    private final ItfFreezeDocIfaceService service;

    public ItfFreezeDocIfaceController(ItfFreezeDocIfaceService service) {
        this.service = service;
    }

    @ApiOperation(value = "条码冻结回传 调用")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<FreezeDocResponseDTO> invoke(@RequestBody FreezeDocRequestDTO request) {
        return Results.success(service.invoke(request));
    }


}
