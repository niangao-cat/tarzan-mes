package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.repository.HmeManyBarcodeSplitRepository;
import com.ruike.hme.domain.vo.HmeManyBarcodeSplitVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 多层条码拆分
 *
 * @author sanfeng.zhang@hand-china.com 2021/3/8 15:29
 */
@RestController("hmeManyBarcodeSplitController.v1")
@RequestMapping("/v1/{organizationId}/hme-many-barcode-split")
public class HmeManyBarcodeSplitController {

    @Autowired
    private HmeManyBarcodeSplitRepository hmeManyBarcodeSplitRepository;

    @ApiOperation(value = "多层条码拆分-扫描条码")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/scan-barcode")
    public ResponseEntity<HmeManyBarcodeSplitVO> scanBarcode(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody HmeManyBarcodeSplitVO splitVO) {
        return Results.success(hmeManyBarcodeSplitRepository.scanBarcode(tenantId, splitVO));
    }


}
