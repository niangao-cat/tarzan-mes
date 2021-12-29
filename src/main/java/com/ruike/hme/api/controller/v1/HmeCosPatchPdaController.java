package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeCosGetChipPlatformService;
import com.ruike.hme.app.service.HmeCosPatchPdaService;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.repository.HmeCosPatchPdaRepository;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * HmeCosPatchPdaController
 * COS贴片平台管理API
 * @author: chaonan.hu@hand-china.com 2020/8/24 16:18:23
 **/
@RestController("hmeCosPatchPdaController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-patch-pda")
@Api(tags = SwaggerApiConfig.HME_COS_PATCH)
public class HmeCosPatchPdaController extends BaseController {

    @Autowired
    private HmeCosPatchPdaService hmeCosPatchPdaService;
    @Autowired
    private HmeCosGetChipPlatformService hmeCosGetChipPlatformService;
    @Autowired
    private HmeCosPatchPdaRepository hmeCosPatchPdaRepository;

    @ApiOperation(value = "投入芯片盒子-未出站条码数据查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/no/site/out/query")
    public ResponseEntity<HmeCosPatchPdaVO> noSiteOutDataQuery(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody HmeCosPatchPdaDTO7 dto) {
        return Results.success(hmeCosPatchPdaService.noSiteOutDataQuery(tenantId,dto));
    }

    @ApiOperation(value = "投入芯片盒子-扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/input/box/scan")
    public ResponseEntity<HmeCosPatchPdaVO10> scanBarcode(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody HmeCosPatchPdaDTO dto) {
        return Results.success(hmeCosPatchPdaService.scanBarcode(tenantId,dto));
    }

    @ApiOperation(value = "投入芯片盒子-删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/input/box/delete")
    public ResponseEntity<List<HmeCosPatchPdaDTO2>> delete(@PathVariable("organizationId") Long tenantId,
                                    @RequestBody List<HmeCosPatchPdaDTO2> dtoList) {
        hmeCosPatchPdaService.delete(tenantId, dtoList);
        return Results.success(dtoList);
    }

    @ApiOperation(value = "投入芯片盒子-进站确认")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/site/in/confirm")
    public ResponseEntity<List<HmeCosPatchPdaDTO3>> siteIn(@PathVariable("organizationId") Long tenantId,
                                    @RequestBody List<HmeCosPatchPdaDTO3> dtoList) {
        hmeCosPatchPdaService.siteIn(tenantId, dtoList);
        return Results.success(dtoList);
    }

    @ApiOperation(value = "贴片后芯片盒子-新增")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/site/out")
    public ResponseEntity<HmeCosPatchPdaVO3> siteOut(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody HmeCosPatchPdaDTO4 dto) {
        return Results.success(hmeCosPatchPdaService.siteOut(tenantId, dto));
    }

    @ApiOperation(value = "贴片后芯片盒子-出站条码数据查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/site/out/query")
    public ResponseEntity<HmeCosPatchPdaVO3> query(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody HmeCosPatchPdaDTO5 dto) {
        return Results.success(hmeCosPatchPdaService.query(tenantId,dto));
    }

    @ApiOperation(value = "贴片后芯片盒子-出站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/site/out/print")
    public ResponseEntity<List<String>> siteOutPrint(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody HmeCosPatchPdaDTO5 dto) {
        return Results.success(hmeCosPatchPdaService.print(tenantId,dto));
    }

    @ApiOperation(value = "贴片后芯片盒子-打印")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/print")
    public ResponseEntity<?> print(@PathVariable("organizationId") Long tenantId,
                                              @RequestBody HmeCosPatchPdaDTO8 dto, HttpServletResponse response) {
        hmeCosPatchPdaService.printPdf(tenantId,dto.getMaterialLotIdList(), response);
        return Results.success();
    }

    @ApiOperation(value = "批次物料绑定工位")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/binding/workcell")
    public ResponseEntity<HmeCosPatchPdaVO4> bandingWorkcell(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody HmeCosPatchPdaDTO6 dto) {
        return Results.success(hmeCosPatchPdaService.bandingWorkcell(tenantId,dto));
    }

    @ApiOperation(value = "批次物料解绑工位")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/unbinding/workcell")
    public ResponseEntity<HmeCosPatchPdaDTO6> unBindingWorkcell(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody HmeCosPatchPdaDTO6 dto) {
        return Results.success(hmeCosPatchPdaService.unBindingWorkcell(tenantId,dto));
    }

    @ApiOperation(value = "工位绑定信息查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/binding/material/query")
    public ResponseEntity<List<HmeCosPatchPdaVO4>> bandingMaterialQuery(@PathVariable("organizationId") Long tenantId,
                                                                        String workcellId) {
        return Results.success(hmeCosPatchPdaService.bandingMaterialQuery(tenantId, workcellId));
    }

    @ApiOperation(value = "贴片后芯片盒子-条码芯片数查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/chip/qty/query")
    public ResponseEntity<HmeCosPatchPdaVO6> getChipQty(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody HmeCosPatchPdaDTO9 dto) {
        return Results.success(hmeCosPatchPdaRepository.getChipQty(tenantId,dto));
    }

    @ApiOperation(value = "贴片后芯片盒子-新增条码撤回")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/materiallot/recall"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeCosPatchPdaVO3> materialLotRecall(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody HmeCosPatchPdaVO3 dto) {
        return Results.success(hmeCosPatchPdaService.materialLotRecall(tenantId,dto));
    }

    @ApiOperation(value = "贴片后芯片盒子-打印撤回")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/print/recall")
    public ResponseEntity<HmeCosPatchPdaDTO5> printRecall(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody HmeCosPatchPdaDTO5 dto) {
        return Results.success(hmeCosPatchPdaService.printRecall(tenantId,dto));
    }
}
