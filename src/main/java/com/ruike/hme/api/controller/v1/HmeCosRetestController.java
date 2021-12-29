package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.entity.HmeRetestImportData;
import com.ruike.hme.domain.repository.HmeCosRetestRepository;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/19 11:37
 */
@RestController("HmeCosRetestController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-retest")
//@Api(tags = SwaggerApiConfig.HME_COS_RETEST)
public class HmeCosRetestController {

    @Autowired
    private HmeCosRetestRepository hmeCosRetestRepository;


    @ApiOperation(value = "COS超保质期库存复测-条码扫描")
    @GetMapping(value = "/cos-scan-material-lot", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosRetestVO2> scanMaterialLot(@PathVariable("organizationId") Long tenantId,
                                                           HmeCosRetestVO dto) {
        return Results.success(hmeCosRetestRepository.scanMaterialLot(tenantId, dto));
    }

    @ApiOperation(value = "COS超保质期库存复测-确认投料")
    @PostMapping(value = "/cos-retest-feed", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> cosRetestFeed(@PathVariable("organizationId") Long tenantId,
                                           @RequestBody HmeCosRetestVO3 dto) {
        hmeCosRetestRepository.cosRetestFeed(tenantId, dto);
        return Results.success();
    }


    @ApiOperation(value = "COS报废复测-根据工单获取剩余COS数量")
    @GetMapping(value = "/query-over-cos-num", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosRetestVO4> overCosNumQuery(@PathVariable("organizationId") Long tenantId,
                                                           HmeCosRetestVO4 dto) {
        return Results.success(hmeCosRetestRepository.overCosNumQuery(tenantId, dto));
    }

    @ApiOperation(value = "COS报废复测-COS类型列表")
    @GetMapping(value = "/query-cos-type", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeCosRetestVO6>> queryCosTypeByContainerType(@PathVariable("organizationId") Long tenantId,
                                                                             String containerTypeCode, String operationId) {
        return Results.success(hmeCosRetestRepository.queryCosTypeByContainerType(tenantId, containerTypeCode, operationId));
    }

    @ApiOperation(value = "COS报废复测-条码扫描")
    @GetMapping(value = "/cos-scrap-scan-material-lot", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosRetestVO2> scrapScanMaterialLot(@PathVariable("organizationId") Long tenantId,
                                                                HmeCosRetestVO dto) {
        return Results.success(hmeCosRetestRepository.scrapScanMaterialLot(tenantId, dto));
    }

    @ApiOperation(value = "COS报废复测-拆分")
    @PostMapping(value = "/cos-scrap-split", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosRetestVO5> cosScrapSplit(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody HmeCosRetestVO5 dto) {
        return Results.success(hmeCosRetestRepository.cosScrapSplit(tenantId, dto));
    }

    @ApiOperation(value = "COS返厂复测-条码扫描")
    @GetMapping(value = "/cos-back-factory-scan-material-lot", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosRetestVO8> backFactoryScanMaterialLot(@PathVariable("organizationId") Long tenantId,
                                                                      HmeCosRetestVO dto) {
        return Results.success(hmeCosRetestRepository.backFactoryScanMaterialLot(tenantId, dto));
    }

    @ApiOperation(value = "COS返厂复测-拆分")
    @PostMapping(value = "/cos-back-factory-split", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosRetestVO9> cosBackFactorySplit(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody HmeCosRetestVO9 dto) {
        return Results.success(hmeCosRetestRepository.cosBackFactorySplit(tenantId, dto));
    }


    @ApiOperation(value = "COS复测导入-查询头信息")
    @GetMapping(value = "/cos-retest-import-header-list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeCosRetestImportVO3>> cosRetestImportHeaderDataList(@PathVariable("organizationId") Long tenantId,
                                                                                     HmeCosRetestImportVO2 dto,
                                                                                     @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeCosRetestRepository.cosRetestImportHeaderDataList(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "COS复测导入-查询行信息")
    @GetMapping(value = "/cos-retest-import-line-list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeRetestImportData>> cosRetestImportLineList(@PathVariable("organizationId") Long tenantId,
                                                                             String retestImportDataId,
                                                                             @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeCosRetestRepository.cosRetestImportLineList(tenantId, retestImportDataId, pageRequest));
    }

    @ApiOperation(value = "COS复测导入-打印")
    @PostMapping(value = "/cos-retest-print", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> cosRetestPrint(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody List<String> retestImportDataIdList,
                                            HttpServletResponse response) {
        hmeCosRetestRepository.cosRetestPrint(tenantId, retestImportDataIdList, response);
        return Results.success();
    }
}
