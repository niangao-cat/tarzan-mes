package com.ruike.wms.api.controller.v1;

import com.ruike.wms.domain.service.WmsDistributionSignService;
import com.ruike.wms.domain.vo.WmsDistributionSignDocVO;
import com.ruike.wms.domain.vo.WmsInstructionActualDetailVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static tarzan.config.SwaggerApiConfig.WMS_DISTRIBUTION_SIGN;

/**
 * 配送签收 API
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/9 15:42
 */
@RestController("wmsDistributionSignController.v1")
@RequestMapping("/v1/{organizationId}/wms-distribution-sign")
@Api(tags = WMS_DISTRIBUTION_SIGN)
public class WmsDistributionSignController {

    private final WmsDistributionSignService distributionSignService;

    public WmsDistributionSignController(WmsDistributionSignService distributionSignService) {
        this.distributionSignService = distributionSignService;
    }

    @ApiOperation(value = "配送单扫描")
    @GetMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsDistributionSignDocVO> docScan(@PathVariable("organizationId") Long tenantId,
                                                            @RequestParam String instructionDocNum) {
        return Results.success(distributionSignService.docScan(tenantId, instructionDocNum));
    }

    @ApiOperation(value = "配送单明细查询")
    @GetMapping(value = "/detail", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsInstructionActualDetailVO>> detailQuery(@PathVariable("organizationId") Long tenantId,
                                                                          @RequestParam String instructionId) {
        return Results.success(distributionSignService.detailQuery(tenantId, instructionId));
    }

    @ApiOperation(value = "领料执行")
    @PostMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> execute(@PathVariable("organizationId") Long tenantId,
                                     @RequestParam String instructionDocId,
                                     @RequestBody List<String> instructionIdList) {
        distributionSignService.execute(tenantId, instructionDocId, instructionIdList);
        return Results.success();
    }
}
