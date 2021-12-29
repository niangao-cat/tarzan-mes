package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeChipLabCodeInputDTO;
import com.ruike.hme.app.service.HmeChipLabCodeInputService;
import com.ruike.hme.domain.vo.HmeChipLabCodeInputVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 芯片实验代码录入 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-11-01 11:01:00
 */
@RestController("hmeChipLabCodeInputController.v1")
@RequestMapping("/v1/{organizationId}/hme-chip-lab-code-input")
public class HmeChipLabCodeInputController extends BaseController {

    @Autowired
    private HmeChipLabCodeInputService hmeChipLabCodeInputService;

    @ApiOperation(value = "扫描盒子号")
    @GetMapping(value = "/scan-barcode/{barcode}", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeChipLabCodeInputVO> scanBarCode(@PathVariable("organizationId") Long tenantId,
                                                             @PathVariable("barcode") String barcode) {
        return Results.success(hmeChipLabCodeInputService.scanBarCode(tenantId, barcode));
    }

    @ApiOperation(value = "确认")
    @PostMapping(value = "/confirm", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> confirm(@PathVariable("organizationId") Long tenantId,
                                     @RequestBody HmeChipLabCodeInputDTO dto) {
        hmeChipLabCodeInputService.confirm(tenantId, dto);
        return Results.success();
    }
}
