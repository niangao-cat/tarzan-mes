package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.*;
import com.ruike.itf.app.service.*;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * 数据采集接口表 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2020-07-13 18:36:01
 */
@RestController("wmsFsmCollectItfController.v1")
@RequestMapping("/v1/{organizationId}/wms-data-collect-itfs")
@Api(tags = SwaggerApiConfig.WMS_DATA_COLLECT_ITF)
@Slf4j
public class   WmsDataCollectItfController extends BaseController {

    private final ItfFsmCollectIfaceService itfFsmCollectIfaceService;
    private final ItfSpecCollectIfaceService itfSpecCollectIfaceService;
    private final ItfOssmCollectIfaceService itfOssmCollectIfaceService;
    private final ItfFacCollectIfaceService itfFacCollectIfaceService;
    private final ItfRmcCollectIfaceService itfRmcCollectIfaceService;
    private final ItfApCollectIfaceService itfApCollectIfaceService;
    private final ItfDtpCollectIfaceService itfDtpCollectIfaceService;
    private final ItfCosCollectIfaceService itfCosCollectIfaceService;
    private final ItfLbpCollectIfaceService itfLbpCollectIfaceService;
    private final ItfBneCollectIfaceService itfBneCollectIfaceService;
    private final ItfOperationCollectIfaceService itfOperationCollectIfaceService;
    private final ItfZzqCollectIfaceService itfZzqCollectIfaceService;
    private final ItfPreselectedCosService itfPreselectedCosService;
    private final ItfAtpCollectIfaceService itfAtpCollectIfaceService;
    private final ItfCosaCollectIfaceService itfCosaCollectIfaceService;
    private final ItfApQueryCollectIfaceService itfApQueryCollectIfaceService;
    private final ItfSnQueryCollectIfaceService itfSnQueryCollectIfaceService;
    private final ItfCosQueryCollectIfaceService itfCosQueryCollectIfaceService;
    private final ItfInSiteCollectIfaceService itfInSiteCollectIfaceService;
    private final ItfCmsCollectIfaceService itfCmsCollectIfaceService;




    @Autowired
    public WmsDataCollectItfController(ItfFsmCollectIfaceService itfFsmCollectIfaceService, ItfSpecCollectIfaceService itfSpecCollectIfaceService, ItfOssmCollectIfaceService itfOssmCollectIfaceService, ItfFacCollectIfaceService itfFacCollectIfaceService,
                                       ItfRmcCollectIfaceService itfRmcCollectIfaceService, ItfDtpCollectIfaceService itfDtpCollectIfaceService, ItfApCollectIfaceService itfApCollectIfaceService, ItfCosCollectIfaceService itfCosCollectIfaceService,
                                       ItfLbpCollectIfaceService itfLbpCollectIfaceService,ItfBneCollectIfaceService itfBneCollectIfaceService,ItfOperationCollectIfaceService itfOperationCollectIfaceService,
                                       ItfZzqCollectIfaceService itfZzqCollectIfaceService,ItfAtpCollectIfaceService itfAtpCollectIfaceService,ItfApQueryCollectIfaceService itfApQueryCollectIfaceService,
                                       ItfSnQueryCollectIfaceService itfSnQueryCollectIfaceService,ItfCosQueryCollectIfaceService itfCosQueryCollectIfaceService,ItfInSiteCollectIfaceService itfInSiteCollectIfaceService,
                                       ItfPreselectedCosService itfPreselectedCosService,ItfCosaCollectIfaceService itfCosaCollectIfaceService, ItfCmsCollectIfaceService itfCmsCollectIfaceService) {
        this.itfFsmCollectIfaceService = itfFsmCollectIfaceService;
        this.itfSpecCollectIfaceService = itfSpecCollectIfaceService;
        this.itfOssmCollectIfaceService = itfOssmCollectIfaceService;
        this.itfFacCollectIfaceService = itfFacCollectIfaceService;
        this.itfRmcCollectIfaceService = itfRmcCollectIfaceService;
        this.itfApCollectIfaceService = itfApCollectIfaceService;
        this.itfDtpCollectIfaceService = itfDtpCollectIfaceService;
        this.itfCosCollectIfaceService = itfCosCollectIfaceService;
        this.itfLbpCollectIfaceService = itfLbpCollectIfaceService;
        this.itfBneCollectIfaceService = itfBneCollectIfaceService;
        this.itfOperationCollectIfaceService = itfOperationCollectIfaceService;
        this.itfZzqCollectIfaceService = itfZzqCollectIfaceService;
        this.itfPreselectedCosService = itfPreselectedCosService;
        this.itfAtpCollectIfaceService=itfAtpCollectIfaceService;
        this.itfCosaCollectIfaceService=itfCosaCollectIfaceService;
        this.itfApQueryCollectIfaceService=itfApQueryCollectIfaceService;
        this.itfCosQueryCollectIfaceService=itfCosQueryCollectIfaceService;
        this.itfSnQueryCollectIfaceService=itfSnQueryCollectIfaceService;
        this.itfInSiteCollectIfaceService =itfInSiteCollectIfaceService;
        this.itfCmsCollectIfaceService = itfCmsCollectIfaceService;
    }

    @ApiOperation(value = "熔接机数据采集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/fsm")
    public ResponseEntity<List<DataCollectReturnDTO>> fsmInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<FsmCollectItfDTO> collectList) {
        log.info("<====WmsDataCollectItfController-fsmInvoke.start:sn{},设备{}", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding());
        long startDate = System.currentTimeMillis();
        List<DataCollectReturnDTO> list = itfFsmCollectIfaceService.invoke(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-fsmInvoke.end:sn{},设备{}。总耗时：{}毫秒", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding(), (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "光谱仪数据采集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/spec")
    public ResponseEntity<List<DataCollectReturnDTO>> specInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<SpecCollectItfDTO> collectList) {
        log.info("<====WmsDataCollectItfController-specInvoke.start:sn{},设备{}", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding());
        long startDate = System.currentTimeMillis();
        List<DataCollectReturnDTO> list = itfSpecCollectIfaceService.invoke(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-specInvoke.end:sn{},设备{}。总耗时：{}毫秒", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding(), (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "示波器数据采集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/ossm")
    public ResponseEntity<List<DataCollectReturnDTO>> ossmInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<OssmCollectItfDTO> collectList) {
        log.info("<====WmsDataCollectItfController-ossmInvoke.start:sn{},设备{}", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding());
        long startDate = System.currentTimeMillis();
        List<DataCollectReturnDTO> list = itfOssmCollectIfaceService.invoke(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-ossmInvoke.end:sn{},设备{}。总耗时：{}毫秒", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding(), (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "FAC数据采集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/fac")
    public ResponseEntity<List<DataCollectReturnDTO>> facInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<FacCollectItfDTO> collectList) {
        log.info("<====WmsDataCollectItfController-facInvoke.start:sn{},设备{}", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding());
        long startDate = System.currentTimeMillis();
        List<DataCollectReturnDTO> list = itfFacCollectIfaceService.invoke(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-facInvoke.end:sn{},设备{}。总耗时：{}毫秒", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding(), (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "反射镜数据采集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/rmc")
    public ResponseEntity<List<DataCollectReturnDTO>> rmcInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<RmcCollectItfDTO> collectList) {
        log.info("<====WmsDataCollectItfController-rmcInvoke.start:sn{},设备{}", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding());
        long startDate = System.currentTimeMillis();
        List<DataCollectReturnDTO> list = itfRmcCollectIfaceService.invoke(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-rmcInvoke.end:sn{},设备{}。总耗时：{}毫秒", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding(), (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "器件测试台数据采集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/dtp")
    public ResponseEntity<List<DataCollectReturnDTO>> dtpInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<DtpCollectItfDTO> collectList) {
        log.info("<====WmsDataCollectItfController-dtpInvoke.start:sn{},设备{}", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding());
        long startDate = System.currentTimeMillis();
        List<DataCollectReturnDTO> list = itfDtpCollectIfaceService.invoke(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-dtpInvoke.end:sn{},设备{}。总耗时：{}毫秒", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding(), (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "老化台数据采集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/ap")
    public ResponseEntity<List<DataCollectReturnDTO>> apInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<ApCollectItfDTO> collectList) {
        log.info("<====WmsDataCollectItfController-apInvoke.start:sn{},设备{}", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding());
        long startDate = System.currentTimeMillis();
        List<DataCollectReturnDTO> list = itfApCollectIfaceService.invoke(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-apInvoke.end:sn{},设备{}。总耗时：{}毫秒", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding(), (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "COS测试台数据采集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cos")
    public ResponseEntity<List<DataCollectReturnDTO>> cosInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<CosCollectItfDTO> collectList) {
        log.info("<====WmsDataCollectItfController-cosInvoke.start:sn{},设备{}", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding());
        long startDate = System.currentTimeMillis();
        List<DataCollectReturnDTO> list = itfCosCollectIfaceService.invoke(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-cosInvoke.end:sn{},设备{}。总耗时：{}毫秒", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding(), (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "lbp读取pdf数据采集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/lbp")
    public ResponseEntity<List<DataCollectReturnDTO>> lbpInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<LbpCollectItfDTO> collectList) {
        log.info("<====WmsDataCollectItfController-lbpInvoke.start:sn{},设备{}", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding());
        long startDate = System.currentTimeMillis();
        List<DataCollectReturnDTO> list = itfLbpCollectIfaceService.invoke(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-lbpInvoke.end:sn{},设备{}。总耗时：{}毫秒", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding(), (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "bne数据采集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/bne")
    public ResponseEntity<List<DataCollectReturnDTO>> bneInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<BneCollectItfDTO> collectList) {
        log.info("<====WmsDataCollectItfController-bneInvoke.start:sn{},设备{}", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding());
        long startDate = System.currentTimeMillis();
        List<DataCollectReturnDTO> list = itfBneCollectIfaceService.invoke(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-bneInvoke.end:sn{},设备{}。总耗时：{}毫秒", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding(), (endDate - startDate));
        return Results.success(list);
    }
    @ApiOperation(value = "operation数据采集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/operation")
    public ResponseEntity<List<OperationCollectItfDTO>> operationInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<OperationCollectItfDTO> collectList) {
        log.info("<====WmsDataCollectItfController-operationInvoke.start:sn{}", collectList.get(0).getSn());
        long startDate = System.currentTimeMillis();
        List<OperationCollectItfDTO> list = itfOperationCollectIfaceService.invoke(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-operationInvoke.end:sn{}。总耗时：{}毫秒", collectList.get(0).getSn(), (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "准直器")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/zzq")
    public ResponseEntity<List<DataCollectReturnDTO>> zzqInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<ZzqCollectItfDTO> collectList) {
        log.info("<====WmsDataCollectItfController-zzqInvoke.start:sn{},设备{}", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding());
        long startDate = System.currentTimeMillis();
        List<DataCollectReturnDTO> list = itfZzqCollectIfaceService.invoke(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-zzqInvoke.end:sn{},设备{}。总耗时：{}毫秒", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding(), (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "自动测试")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/atp")
    public ResponseEntity<List<DataCollectReturnDTO>> atpInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<AtpCollectItfDTO> collectList) {
        log.info("<====WmsDataCollectItfController-atpInvoke.start:sn{},设备{}", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding());
        long startDate = System.currentTimeMillis();
        List<DataCollectReturnDTO> list = itfAtpCollectIfaceService.invoke(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-atpInvoke.end:sn{},设备{}。总耗时：{}毫秒", collectList.get(0).getSn(), collectList.get(0).getAssetEncoding(), (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "老化基础数据查询接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/ap/query")
    public ResponseEntity<List<ApQueryCollectItfReturnDTO>> apQueryInvoke(@PathVariable("organizationId") Long tenantId,
                                                                          @RequestBody List<String> materialLotCodes) {
        log.info("<====WmsDataCollectItfController-apQueryInvoke.start:materialLotCode{}", materialLotCodes);
        long startDate = System.currentTimeMillis();
        List<ApQueryCollectItfReturnDTO> returnDTO = itfApQueryCollectIfaceService.apQueryInvoke(tenantId, materialLotCodes);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-apQueryInvoke.end:materialLotCode{},总耗时：{}毫秒", materialLotCodes, (endDate - startDate));
        return Results.success(returnDTO);
    }

    @ApiOperation(value = "自动测试")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/snquery")
    public ResponseEntity<SnQueryCollectItfReturnDTO> snQueryInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody SnQueryCollectItfDTO snQueryCollectItfDTO) {
        log.info("<====WmsDataCollectItfController-snQueryInvoke.start:工艺编码{},设备{}", snQueryCollectItfDTO.getOperationName(), snQueryCollectItfDTO.getAssetEncoding());
        long startDate = System.currentTimeMillis();
        SnQueryCollectItfReturnDTO list = itfSnQueryCollectIfaceService.invoke(tenantId, snQueryCollectItfDTO);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-snQueryInvoke.end:工艺编码{},设备{}。总耗时：{}毫秒", snQueryCollectItfDTO.getOperationName(), snQueryCollectItfDTO.getAssetEncoding(), (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "cos查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cosquery")
    public ResponseEntity<CosQueryCollectItfReturnDTO> cosQueryInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody String materialLotCode) {
        log.info("<====WmsDataCollectItfController-snQueryInvoke.start:工艺编码{}", materialLotCode);
        long startDate = System.currentTimeMillis();
        CosQueryCollectItfReturnDTO list = itfCosQueryCollectIfaceService.invoke(tenantId, materialLotCode);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-snQueryInvoke.end:工艺编码{}。总耗时：{}毫秒", materialLotCode, (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "时效进站接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/insite")
    public ResponseEntity<DataCollectReturnDTO> inSiteInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody InSIteDTO inSIteDTO) {
        log.info("<====WmsDataCollectItfController-snQueryInvoke.start:工位{}", inSIteDTO.getWorkcellCode());
        long startDate = System.currentTimeMillis();
        DataCollectReturnDTO list = itfInSiteCollectIfaceService.invoke(tenantId, inSIteDTO);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-snQueryInvoke.end:工艺编码{}。总耗时：{}毫秒", inSIteDTO.getWorkcellCode(), (endDate - startDate));
        return Results.success(list);
    }


    @ApiOperation(value = "COS挑选接口-1")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cos/first")
    public ResponseEntity<List<ItfPreselectedCosOneReturnDTO>> cosFirstInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<String> materialLotCodeList) {
        log.info("<====WmsDataCollectItfController-cosFirstInvoke.start:materialLotCode{}", materialLotCodeList.get(0));
        long startDate = System.currentTimeMillis();
        List<ItfPreselectedCosOneReturnDTO> returnDTO = itfPreselectedCosService.invoke(tenantId, materialLotCodeList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-cosFirstInvoke.end:materialLotCode{},总耗时：{}毫秒", materialLotCodeList.get(0), (endDate - startDate));
        return Results.success(returnDTO);
    }

    @ApiOperation(value = "COS挑选接口-2")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cos/second")
    public ResponseEntity<List<ItfPreselectedCosTwoReturnDTO>> cosSecondInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<String> virtualNumList) {
        log.info("<====WmsDataCollectItfController-cosSecondInvoke.start:materialLotCode{}", virtualNumList.get(0));
        long startDate = System.currentTimeMillis();
        List<ItfPreselectedCosTwoReturnDTO> returnDTO = itfPreselectedCosService.invokeSecond(tenantId, virtualNumList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-cosSecondInvoke.end:materialLotCode{},总耗时：{}毫秒", virtualNumList.get(0), (endDate - startDate));
        return Results.success(returnDTO);
    }

    @ApiOperation(value = "COS挑选接口v2-合并原接口1/2功能")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cos/select")
    public ResponseEntity<ItfPreselectedCosSelectReturnDTO> cosSelectInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<String> materialLotCodeList){
        log.info("<====WmsDataCollectItfController-cosSelectInvoke.start:materialLotCode{}", materialLotCodeList.get(0));
        long startDate = System.currentTimeMillis();
        ItfPreselectedCosSelectReturnDTO returnDTO = itfPreselectedCosService.invokeSelect(tenantId, materialLotCodeList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-cosSelectInvoke.end:materialLotCode{},总耗时：{}毫秒", materialLotCodeList.get(0), (endDate - startDate));
        return Results.success(returnDTO);
    }

    @ApiOperation(value = "芯片转移接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cosa")
    public ResponseEntity<List<DataCollectReturnDTO>> cosaInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<CosaCollectItfDTO> collectList) {
        log.info("<====WmsDataCollectItfController-cosaInvoke.start:来源盒子{},目标盒子{}", collectList.get(0).getSourceMaterialLotCode(), collectList.get(0).getTargetMaterialLotCode());
        long startDate = System.currentTimeMillis();
        List<DataCollectReturnDTO> list = itfCosaCollectIfaceService.invoke(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-cosaInvoke.end:来源盒子{},目标盒子{}。总耗时：{}毫秒", collectList.get(0).getSourceMaterialLotCode(), collectList.get(0).getTargetMaterialLotCode(), (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "COS芯片归集接口-查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cos/arrange")
    public ResponseEntity<List<ItfPreselectedCosArrangeReturnDTO>> cosArrange(@PathVariable("organizationId") Long tenantId, @RequestBody List<String> materialLotCodeList){
        log.info("<====WmsDataCollectItfController-cosSelectInvoke.start:materialLotCode{}", materialLotCodeList.get(0));
        long startDate = System.currentTimeMillis();
        List<ItfPreselectedCosArrangeReturnDTO> returnDTO = itfPreselectedCosService.invokeArrange(tenantId, materialLotCodeList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-cosSelectInvoke.end:materialLotCode{},总耗时：{}毫秒", materialLotCodeList.get(0), (endDate - startDate));
        return Results.success(returnDTO);
    }

    @ApiOperation(value = "COS芯片归集接口-归集")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cos/cosb")
    public ResponseEntity<List<DataCollectReturnDTO>> cosbInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<CosaCollectItfDTO> collectList){
        log.info("<====WmsDataCollectItfController-cosbInvoke.start:来源盒子{},目标盒子{}", collectList.get(0).getSourceMaterialLotCode(), collectList.get(0).getTargetMaterialLotCode());
        long startDate = System.currentTimeMillis();
        List<DataCollectReturnDTO> returnDTO = itfCosaCollectIfaceService.invokeb(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-cosbInvoke.end:来源盒子{},目标盒子{}。总耗时：{}毫秒", collectList.get(0).getSourceMaterialLotCode(), collectList.get(0).getTargetMaterialLotCode(), (endDate - startDate));
        return Results.success(returnDTO);
    }

    @ApiOperation(value = "CMS封装固化线数据采集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cms")
    public ResponseEntity<List<DataCollectReturnDTO>> cmsInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody List<CmsCollectItfDTO> collectList) {
        log.info("<====WmsDataCollectItfController-cmsInvoke.start:EO{},设备{}", collectList.get(0).getIdentification(), collectList.get(0).getEquipmentNum());
        long startDate = System.currentTimeMillis();
        List<DataCollectReturnDTO> list = itfCmsCollectIfaceService.invoke(tenantId, collectList);
        long endDate = System.currentTimeMillis();
        log.info("<====WmsDataCollectItfController-cmsInvoke.start:EO{},设备{}", collectList.get(0).getIdentification(),collectList.get(0).getEquipmentNum());
        return Results.success(list);
    }
}
