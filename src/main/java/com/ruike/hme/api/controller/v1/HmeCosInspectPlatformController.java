package com.ruike.hme.api.controller.v1;

import java.util.List;

import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ruike.hme.api.dto.HmeCosGetChipSiteOutPrintDTO;
import com.ruike.hme.app.service.HmeCosInspectPlatformService;
import com.ruike.hme.domain.vo.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import tarzan.config.SwaggerApiConfig;

/**
 * <p>
 * COS检验工作台
 * </p>
 *
 * @author yapeng.yao@hand-china.com 2020/08/25 14:22
 */
@RestController("hmeCosInspectPlatformController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-inspect-platform")
@Api(tags = SwaggerApiConfig.HME_COS_INSPECT_PLATFORM)
@Slf4j
public class HmeCosInspectPlatformController {

    @Autowired
    private HmeCosInspectPlatformService hmeCosInspectPlatformService;

    /**
     * 进入界面，自动查询信息
     * @param tenantId
     * @param requestVO
     * @return
     */
    @ApiOperation(value = "COS检验平台-自动查询数据")
    @GetMapping(value = "/auto-query-info", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeCosInspectPlatformAutoQueryInfoResponseVO>> autoQueryInfo(
                    @PathVariable("organizationId") Long tenantId, HmeCosInspectPlatformQueryInfoRequestVO requestVO) {
        log.info("<====HmeCosInspectPlatformController.autoQueryInfo:{}，{}", tenantId, requestVO);
        return Results.success(hmeCosInspectPlatformService.autoQueryInfo(tenantId, requestVO));
    }

    /**
     * 点击查询按钮，查询信息
     * @param tenantId
     * @param requestVO
     * @return
     */
    @ApiOperation(value = "COS检验平台-查询按钮")
    @GetMapping(value = "/query-info", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeCosInspectPlatformAutoQueryInfoResponseVO>> queryInfo(
                    @PathVariable("organizationId") Long tenantId, HmeCosInspectPlatformQueryInfoRequestVO requestVO) {
        log.info("<====HmeCosInspectPlatformController.queryInfo:{}，{}", tenantId, requestVO);
        return Results.success(hmeCosInspectPlatformService.queryInfo(tenantId, requestVO));
    }

    /**
     * 扫描一个盒子
     * @param tenantId
     * @param inVO
     * @return
     */
    @ApiOperation(value = "COS检验平台-扫描盒子")
    @GetMapping(value = "/scan-materialLotCode", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosInspectPlatformScanMaterialLotCodeResponseVO> scanMaterialLotCode(
                    @PathVariable("organizationId") Long tenantId, HmeCosInspectPlatformQueryInfoRequestVO inVO) {
        log.info("<====HmeCosInspectPlatformController.scanMaterialLotCode:{}，{}", tenantId, inVO);
        return Results.success(hmeCosInspectPlatformService.scanMaterialLotCode(tenantId, inVO));
    }

    /**
     * 点击行数据，查询物料装载信息
     * @param tenantId
     * @param inVO
     * @return
     */
    @ApiOperation(value = "COS检验平台-行数据查询物料装载信息")
    @GetMapping(value = "/query-load-data", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosInspectPlatformScanMaterialLotCodeResponseVO> queryLoadData(
                    @PathVariable("organizationId") Long tenantId, HmeCosInspectPlatformQueryInfoRequestVO inVO) {
        log.info("<====HmeCosInspectPlatformController.scanMaterialLotCode:{}，{}", tenantId, inVO);
        return Results.success(hmeCosInspectPlatformService.queryLoadData(tenantId, inVO));
    }

    /**
     * 查询芯片及新增数据采集项
     * @param tenantId
     * @param vo
     * @return
     */
    @ApiOperation(value = "COS检验平台-查询COS、数据采集")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/cos-inspection-query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeEoJobDataRecordVO>> cosInspectionQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody HmeCosInspectPlatformCosInspectRequestVO vo) {
        log.info("<====HmeCosInspectPlatformController.cosInspectionQuery:{}，{}", tenantId, vo);
        List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOS =
                        hmeCosInspectPlatformService.cosInspectionQuery(tenantId, vo);
        return Results.success(hmeEoJobDataRecordVOS);
    }

    /**
     * 保存COS检验结果
     * @param tenantId
     * @param vo
     * @return
     */
    @ApiOperation(value = "COS检验平台-保存COS检验结果")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/cos-inspection", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeEoJobDataRecordVO> cosInspection(@PathVariable("organizationId") Long tenantId,
                    @RequestBody HmeEoJobDataRecordVO vo) {
        log.info("<====HmeCosInspectPlatformController.cosInspection:{}，{}", tenantId, vo);
        return Results.success(hmeCosInspectPlatformService.cosInspection(tenantId, vo));
    }

    /**
     * 出站结束
     * @param tenantId
     * @param responseVO
     * @return
     */
    @ApiOperation(value = "COS检验平台-出站前校验")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/check-site-out", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> checkSiteOut(@PathVariable("organizationId") Long tenantId,
                                     @RequestBody HmeCosInspectPlatformSiteOutRequestVO responseVO) {
        log.info("<====HmeCosInspectPlatformController.checkSiteOutCheck:{}，{}", tenantId, responseVO);
        // 暂时不需要这个校验
        // hmeCosInspectPlatformService.checkSiteOut(tenantId, responseVO)
        return Results.success();
    }

    /**
     * 出站结束
     * @param tenantId
     * @param responseVO
     * @return
     */
    @ApiOperation(value = "COS检验平台-完成检验出站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/site-out", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> siteOut(@PathVariable("organizationId") Long tenantId,
                    @RequestBody HmeCosInspectPlatformSiteOutRequestVO responseVO) {
        log.info("<====HmeCosInspectPlatformController.siteOut:{}，{}", tenantId, responseVO);
        hmeCosInspectPlatformService.siteOut(tenantId, responseVO);
        return Results.success();
    }

}
