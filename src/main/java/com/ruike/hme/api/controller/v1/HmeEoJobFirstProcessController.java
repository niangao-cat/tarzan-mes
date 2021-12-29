package com.ruike.hme.api.controller.v1;

import java.util.List;

import com.ruike.hme.api.dto.HmeEoJobSnSingleDTO;
import com.ruike.hme.api.dto.HmeEoLovQueryDTO;
import com.ruike.hme.api.dto.HmeMaterialLotLovQueryDTO;
import com.ruike.hme.api.dto.HmeWoLovQueryDTO;
import com.ruike.hme.app.service.HmeEoJobFirstProcessService;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import java.util.*;

/**
 * @Classname HmeEoJobFirstProcessController
 * @Description 首序作业平台
 * @Date 2020/9/1 9:39
 * @Author yuchao.wang
 */
@Slf4j
@RestController("HmeEoJobFirstProcessController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-first-process")
@Api(tags = SwaggerApiConfig.HME_EO_JOB_FIRST_PROCESS)
public class HmeEoJobFirstProcessController {

    @Autowired
    private HmeEoJobFirstProcessService hmeEoJobFirstProcessService;

    @ApiOperation(value = "进站条码扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/in-site-scan")
    public ResponseEntity<?> inSiteScan(@PathVariable("organizationId") Long tenantId,
                                        @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====HmeEoJobFirstProcessController.inSiteScan:{}，{}", tenantId, dto);
        long startDate = System.currentTimeMillis();
        HmeEoJobSnVO hmeEoJobSnVO = hmeEoJobFirstProcessService.inSiteScan(tenantId, dto);
        log.info("=================================>首序工序作业平台-进站总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return Results.success(hmeEoJobSnVO);
    }

    @ApiOperation(value = "查询条码是否在物料批中")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/query-material-lot")
    public ResponseEntity<?> queryMaterialLot(@PathVariable("organizationId") Long tenantId,
                                              @RequestParam String barcode) {
        log.info("<====HmeEoJobFirstProcessController.queryMaterialLot:{}，{}", tenantId, barcode);
        return Results.success(hmeEoJobFirstProcessService.queryMaterialLot(tenantId, barcode));
    }

    @ApiOperation(value = "查询容器条码是否存在")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/query-container")
    public ResponseEntity<?> queryContainer(@PathVariable("organizationId") Long tenantId,
                                            @RequestParam String barcode) {
        log.info("<====HmeEoJobFirstProcessController.queryContainer:{}，{}", tenantId, barcode);
        return Results.success(hmeEoJobFirstProcessService.queryContainer(tenantId, barcode));
    }

    @ApiOperation(value = "序列物料投料扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release-scan")
    public ResponseEntity<List<HmeEoJobMaterialVO>> releaseScan(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody HmeEoJobMaterialVO dto) {
        return Results.success(hmeEoJobFirstProcessService.releaseScan(tenantId, dto));
    }

    @ApiOperation(value = "工单号LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/wo-lov")
    public ResponseEntity<Page<HmeWorkOrderVO>> workOrderLovQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                 HmeWoLovQueryDTO dto,
                                                                 @ApiIgnore PageRequest pageRequest) {
        log.info("<====HmeEoJobFirstProcessController.workOrderLovQuery:{}，{}", tenantId, dto);
        return Results.success(hmeEoJobFirstProcessService.workOrderQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "EO LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/eo-lov")
    public ResponseEntity<Page<HmeEoVO>> eoLovQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                    HmeEoLovQueryDTO dto,
                                                    @ApiIgnore PageRequest pageRequest) {
        log.info("<====HmeEoJobFirstProcessController.eoLovQuery:{}，{}", tenantId, dto);
        return Results.success(hmeEoJobFirstProcessService.eoQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "物料批LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/material-lot-lov")
    public ResponseEntity<Page<HmeMaterialLotVO2>> materialLotLovQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                       HmeMaterialLotLovQueryDTO dto,
                                                                       @ApiIgnore PageRequest pageRequest) {
        log.info("<====HmeEoJobFirstProcessController.materialLotLovQuery:{}，{}", tenantId, dto);
        return Results.success(hmeEoJobFirstProcessService.materialLotLovQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "查询条码下的虚拟号")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/material-lot/virtual_num")
    public ResponseEntity<?> virtualNumQuery(@PathVariable(value = "organizationId") Long tenantId,
                                             @RequestBody List<String> materialLotCodeList) {
        log.info("<====HmeEoJobFirstProcessController.virtualNumQuery:{}，{}", tenantId, materialLotCodeList);
        return Results.success(hmeEoJobFirstProcessService.virtualNumQuery(tenantId, materialLotCodeList));
    }

    @ApiOperation(value = "SN首序配置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/material-lot/first-part")
    public ResponseEntity<?> firstPartQuery(@PathVariable(value = "organizationId") Long tenantId,
                                              String materialId) {
        log.info("<====HmeEoJobFirstProcessController.firstPartQuery:{}，{}", tenantId, materialId);
        return Results.success(hmeEoJobFirstProcessService.firstPartQuery(tenantId, materialId));
    }

    @ApiOperation(value = "首序作业-投料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release")
    public ResponseEntity<?> release(@PathVariable("organizationId") Long tenantId,
                                     @RequestBody HmeEoJobSnSingleDTO dto) {
        log.info("<====== HmeEoJobFirstProcessController.release tenantId={},dto={}", tenantId, dto);
        long startDate = System.currentTimeMillis();
        List<HmeEoJobSnBatchVO4> hmeEoJobSnBatchVO4List = hmeEoJobFirstProcessService.release(tenantId, dto);
        log.info("=================================>首序作业-投料总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return Results.success(hmeEoJobSnBatchVO4List);
    }

    @ApiOperation(value = "首序作业-投料退回")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release-back")
    public ResponseEntity<HmeEoJobSnVO9> releaseBack(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody HmeEoJobSnVO9 dto) {
        log.info("<====== HmeEoJobFirstProcessController.releaseBack tenantId={},dto={}", tenantId, dto);
        long startDate = System.currentTimeMillis();
        HmeEoJobSnVO9 resultVO = hmeEoJobFirstProcessService.releaseBack(tenantId, dto);
        log.info("<====== 首序工序作业平台投料退回总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return Results.success(resultVO);
    }
}