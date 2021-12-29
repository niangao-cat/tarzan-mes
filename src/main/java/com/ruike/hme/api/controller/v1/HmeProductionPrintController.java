package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeProductionPrintService;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.order.api.dto.MtEoDTO5;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * description 打印接口
 *
 * @author wengang.qiang@hand-china 2021/09/23 19:33
 */
@RestController("hmeProductionPrintController.v1")
@RequestMapping("/v1/{organizationId}/hme-production-prints")
public class HmeProductionPrintController extends BaseController {
    private final HmeProductionPrintService hmeProductionPrintService;

    public HmeProductionPrintController(HmeProductionPrintService hmeProductionPrintService) {
        this.hmeProductionPrintService = hmeProductionPrintService;
    }

    @ApiOperation(value = "打印接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/production-prints")
    public ResponseEntity<?> boxPrints(@PathVariable("organizationId") Long tenantId,
                                       @RequestBody HmeProductionPrintVO printVO) {
        hmeProductionPrintService.boxPrints(tenantId, printVO);
        return Results.success();
    }

    @ApiOperation(value = "打印接口二")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/production-prints-second")
    public ResponseEntity<?> boxPrintSecond(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody HmeProductionPrintSecondVO hmeProductionPrintSecondVO) {
        hmeProductionPrintService.boxPrintSecond(tenantId, hmeProductionPrintSecondVO);
        return Results.success();
    }

    @ApiOperation(value = "多模板打印查询接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/production-multi-query")
    public ResponseEntity<List<HmeProductionPrintVO10>> multiTemplateQuery(@PathVariable("organizationId") Long tenantId,
                                                                           HmeProductionPrintDTO dto) {
        return Results.success(hmeProductionPrintService.multiTemplateQuery(tenantId, dto));
    }

    @ApiOperation(value = "多模板打印接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/production-multi-print")
    public ResponseEntity<?> multiTemplatePrints(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody HmeProductionPrintDTO dto,
                                                 HttpServletResponse response) {
        hmeProductionPrintService.multiTemplatePrints(tenantId, dto , response);
        return Results.success();
    }

    @ApiOperation(value = "铭牌打印查询internalCode")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/internal-code-query")
    public ResponseEntity<HmeProductionPrintVO9> internalCodeQuery(@PathVariable("organizationId") Long tenantId,
                                                                   @RequestBody List<MtEoDTO5> dtoList) {
        return Results.success(hmeProductionPrintService.internalCodeQuery(tenantId, dtoList));
    }

    @ApiOperation(value = "执行作业铭牌打印")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/eo-nameplate-print")
    public ResponseEntity<?> eoNameplatePrint(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody HmeProductionPrintVO9 dto,
                                                 HttpServletResponse response) {
        hmeProductionPrintService.eoNameplatePrint(tenantId, dto , response);
        return Results.success();
    }
}
