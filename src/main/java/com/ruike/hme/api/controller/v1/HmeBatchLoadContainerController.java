package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeBatchLoadContainerService;
import com.ruike.hme.domain.vo.HmeLoadContainerVO;
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

/**
* @Classname HmeBatchLoadContainerController
* @Description 批量完工装箱 API
* @Date  2020/6/5 9:44
* @Created by Deng xu
*/
@RestController("HmeBatchLoadContainerController.v1")
@RequestMapping("/v1/{organizationId}/hme-batch-load-container")
@Api(tags = SwaggerApiConfig.HME_BATCH_LOAD_CONTAINER)
@Slf4j
public class HmeBatchLoadContainerController extends BaseController {

    @Autowired
    private HmeBatchLoadContainerService service;

    /**
    * @Description: 批量完工装箱-扫描外箱条码
    * @author: Deng Xu
    * @date 2020/6/5 11:20
    * @param tenantId 租户ID
    * @param outerContainerCode 外箱条码
    * @return : io.tarzan.common.domain.sys.ResponseData<com.ruike.hme.domain.vo.HmeLoadContainerVO>
    * @version 1.0
    */
    @ApiOperation(value = "批量完工装箱-扫描外箱条码")
    @GetMapping(value = "/scan/outer/container", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeLoadContainerVO> scanOuterContainer(@PathVariable("organizationId") Long tenantId,
                                                                 String outerContainerCode ) {
        log.info("<====HmeBatchLoadContainerController-scanOuterContainer:{}，{}", tenantId, outerContainerCode);
        return Results.success(service.scanOuterContainer(tenantId, outerContainerCode));
    }

    /**
    * @Description: 批量完工装箱-扫描待装箱条码
    * @author: Deng Xu
    * @date 2020/6/5 15:11
    * @param tenantId 租户ID
    * @param loadContainerVO 外箱条码ID、待装箱条码code
    * @return : io.tarzan.common.domain.sys.ResponseData<com.ruike.hme.domain.vo.HmeLoadContainerVO>
    * @version 1.0
    */
    @ApiOperation(value = "批量完工装箱-扫描待装箱条码")
    @GetMapping(value = "/scan/container", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeLoadContainerVO> scanContainer(@PathVariable("organizationId") Long tenantId,
                                                          HmeLoadContainerVO loadContainerVO) {
        log.info("<====HmeBatchLoadContainerController-scanContainer:{}，{}", tenantId, loadContainerVO);
        return Results.success(service.scanContainer(tenantId, loadContainerVO ));
    }

    /**
    * @Description: 批量完工装箱-卸载容器/物料批
    * @author: Deng Xu
    * @date 2020/6/5 12:30
    * @param tenantId 租户ID
    * @param loadContainerVO 待装箱条码信息VO
    * @return : io.tarzan.common.domain.sys.ResponseData<java.lang.Void>
    * @version 1.0
    */
    @ApiOperation(value = "批量完工装箱-卸载容器/物料批")
    @PostMapping(value = {"/unload/container"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> unloadContainer(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody HmeLoadContainerVO loadContainerVO) {
        log.info("<====HmeBatchLoadContainerController-unloadContainer:{}，{}", tenantId, loadContainerVO);
        return Results.success(service.unloadContainer(tenantId,loadContainerVO));
    }

    /**
    * @Description: 批量完工装箱-容器/物料批装箱
    * @author: Deng Xu
    * @date 2020/6/6 16:23
    * @param tenantId 租户ID
    * @param loadContainerVO 待装箱容器/物料批、外箱ID
    * @return : io.tarzan.common.domain.sys.ResponseData<java.lang.Void>
    * @version 1.0
    */
    @ApiOperation(value = "批量完工装箱-装载容器/物料批")
    @PostMapping(value = {"/load/container"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> loadContainer(@PathVariable("organizationId") Long tenantId,
                                              @RequestBody HmeLoadContainerVO loadContainerVO) {
        log.info("<====HmeBatchLoadContainerController-loadContainer:{}，{}", tenantId, loadContainerVO);
        return Results.success(service.loadContainer(tenantId,loadContainerVO));
    }

    @ApiOperation(value = "批量完工装箱-验证容器/物料批")
    @GetMapping(value = {"/verify-container"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeLoadContainerVO> verifyContainer(@PathVariable("organizationId") Long tenantId,
                                              HmeLoadContainerVO loadContainerVO) {
        log.info("<====HmeBatchLoadContainerController-verifyContainer:{}，{}", tenantId, loadContainerVO);
        return Results.success(service.verifyContainer(tenantId,loadContainerVO));
    }

    @ApiOperation(value = "批量完工装箱-卸载原外箱容器/物料批")
    @PostMapping(value = {"/unload-original-container"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> unloadOriginalContainer(@PathVariable("organizationId") Long tenantId,
                                           @RequestBody HmeLoadContainerVO loadContainerVO) {
        log.info("<====HmeBatchLoadContainerController-loadContainer:{}，{}", tenantId, loadContainerVO);
        return Results.success(service.unloadOriginalContainer(tenantId,loadContainerVO));
    }

}
