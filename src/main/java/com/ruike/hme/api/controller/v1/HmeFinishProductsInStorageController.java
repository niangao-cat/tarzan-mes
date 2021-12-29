package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeFinishProductsInStorageService;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
* @Classname HmeFinishProductsInStorageController
* @Description  半成品/成品入库扫描 API
* @Date  2020/6/2 16:04
* @Created by Deng xu
*/
@RestController("HmeFinishProductsInStorageController.v1")
@RequestMapping("/v1/{organizationId}/hme-finish-products-in-storage")
@Api(tags = SwaggerApiConfig.HME_FINISH_PRODUCTS_IN_STORAGE)
@Slf4j
public class HmeFinishProductsInStorageController extends BaseController {

    @Autowired
    private HmeFinishProductsInStorageService service;

    /**
    * @Description: 半成品/成品入库扫描-扫描外箱条码获取信息
    * @author: Deng Xu
    * @date 2020/6/3 10:58
    * @param tenantId 租户ID
    * @param containerVO 外箱条码
    * @return : io.tarzan.common.domain.sys.ResponseData<com.ruike.hme.domain.vo.HmeInStorageContainerVO>
    * @version 1.0
    */
    @ApiOperation(value = "半成品/成品入库扫描-扫描外箱条码获取信息")
    @PostMapping(value = "/query/container", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeInStorageContainerVO> queryContainer(
            @PathVariable("organizationId") Long tenantId, @RequestBody HmeInStorageContainerVO containerVO) {
        log.info("<====HmeFinishProductsInStorageController-queryContainer:{}，{}", tenantId, containerVO);
        return Results.success(service.queryContainer(tenantId,containerVO));
    }

    /**
    * @Description: 半成品/成品入库扫描-装箱明细查询
    * @author: Deng Xu
    * @date 2020/6/3 11:32
    * @param tenantId 租户ID
    * @param containerId 外箱ID
    * @return : io.tarzan.common.domain.sys.ResponseData<java.util.List<com.ruike.hme.domain.vo.HmeInStorageMaterialVO>>
    * @version 1.0
    */
    @ApiOperation(value = "半成品/成品入库扫描-装箱明细查询")
    @GetMapping(value = "/query/container/detail", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeInStorageMaterialVO>> queryContainerDetail(
            @PathVariable("organizationId") Long tenantId, @RequestParam String containerId) {
        log.info("<====HmeFinishProductsInStorageController-queryContainerDetail:{}，{}", tenantId, containerId);
        return Results.success(service.queryContainerDetail(tenantId,containerId));
    }

    /**
    * @Description: 半成品/成品入库扫描-入库
    * @author: Deng Xu
    * @date 2020/6/3 13:51
    * @param tenantId 租户ID
    * @param containerVOList 容器对象集合
    * @return : io.tarzan.common.domain.sys.ResponseData<java.lang.Void>
    * @version 1.0
    */
    @ApiOperation(value = "半成品/成品入库扫描-入库")
    @PostMapping(value = {"/in/storage"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> finishProductsInStorage(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody  List<HmeInStorageContainerVO> containerVOList) {
        return Results.success(service.finishProductsInStorage(tenantId, containerVOList));
    }

    @ApiOperation(value = "目标仓库列表")
    @GetMapping(value = "/query-locator-list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<MtModLocator>> queryLocatorList(@PathVariable("organizationId") Long tenantId) {
        log.info("<====HmeFinishProductsInStorageController-queryLocatorList:{}，{}", tenantId);
        return Results.success(service.queryLocatorList(tenantId));
    }

    @ApiOperation(value = "默认目标仓库列表")
    @GetMapping(value = "/query-default-locator-list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<MtModLocator> queryDefaultLocatorList(@PathVariable("organizationId") Long tenantId) {
        log.info("<====HmeFinishProductsInStorageController-queryLocatorList:{}，{}", tenantId);
        return Results.success(service.queryDefaultLocatorList(tenantId));
    }

    @ApiOperation(value = "COS完工-扫描条码")
    @PostMapping(value = "/scan-material-lot", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosResVO> scanMaterialLot(@PathVariable("organizationId") Long tenantId, @RequestBody HmeCosScanCodeVO codeVO) {
        log.info("<====HmeFinishProductsInStorageController-scanMaterialLot:{}，{}", tenantId);
        return Results.success(service.scanMaterialLot(tenantId, codeVO));
    }

    @ApiOperation(value = "COS完工-条码出站")
    @PostMapping(value = "/material-lot-site-out", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosScanCodeVO2> materialLotSiteOut(@PathVariable("organizationId") Long tenantId, @RequestBody HmeCosScanCodeVO2 codeVO) {
        log.info("<====HmeFinishProductsInStorageController-scanMaterialLot:{}，{}", tenantId);
        return Results.success(service.materialLotSiteOut(tenantId, codeVO));
    }

    @ApiOperation(value = "COS完工-查询未出站条码")
    @GetMapping(value = "/query-site-in-material-lot", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeCosResVO>> querySiteInMaterialLot(@PathVariable("organizationId") Long tenantId, HmeCosScanCodeVO codeVO) {
        log.info("<====HmeFinishProductsInStorageController-scanMaterialLot:{}，{}", tenantId);
        return Results.success(service.querySiteInMaterialLot(tenantId, codeVO));
    }

    @ApiOperation(value = "COS完工-取消进站")
    @PostMapping(value = "/batch-cancel-site-in", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeCosResVO>> batchCancelSiteIn (@PathVariable("organizationId") Long tenantId, @RequestBody List<HmeCosResVO> cosResVOList) {
        return Results.success(service.batchCancelSiteIn(tenantId, cosResVOList));
    }

    @ApiOperation(value = "有权限的目标仓库列表")
    @GetMapping(value = "/query-locator-list-permission", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<MtModLocator>> queryLocatorListPermission(@PathVariable("organizationId") Long tenantId) {
        log.info("<====HmeFinishProductsInStorageController-queryLocatorList:{}，{}", tenantId);
        return Results.success(service.queryLocatorListPermission(tenantId));
    }
}
