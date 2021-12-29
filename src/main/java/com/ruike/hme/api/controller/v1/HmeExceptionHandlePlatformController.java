package com.ruike.hme.api.controller.v1;

import java.util.List;
import java.util.Map;

import com.ruike.hme.domain.vo.HmeAreaWorkshopProdLineVO;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruike.hme.domain.entity.HmeExcWkcRecord;
import com.ruike.hme.domain.repository.HmeExceptionHandlePlatformRepository;
import com.ruike.hme.domain.vo.HmeExcRecordCreateVO;
import com.ruike.hme.domain.vo.HmeExceptionHandlePlatFormVO;
import com.ruike.hme.domain.vo.HmeExceptionRecordQueryVO;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import tarzan.config.SwaggerApiConfig;

/**
 * @Classname HmeExceptionHandlePlatformController
 * @Description 异常处理平台
 * @Date 2020/6/22 14:32
 * @Created by Deng xu
 */
@RestController("HmeExceptionHandlePlatformController.v1")
@RequestMapping("/v1/{organizationId}/hme-exception-handle-platform")
@Api(tags = SwaggerApiConfig.HME_EXCEPTION_HANDLE_PLATFORM)
@Slf4j
public class HmeExceptionHandlePlatformController extends BaseController {

    @Autowired
    private HmeExceptionHandlePlatformRepository platformRepository;

    /**
     * @param tenantId     租户ID
     * @param workcellCode 工位编码
     * @return : io.tarzan.common.domain.sys.ResponseData<java.util.List<com.ruike.hme.domain.vo.HmeExceptionHandleQueryVO>>
     * @Description: 异常处理平台-主界面查询
     * @author: Deng Xu
     * @date 2020/6/22 14:41
     * @version 1.0
     */
    @ApiOperation(value = "异常处理平台-主界面查询")
    @GetMapping(value = "/list/exception/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeExceptionHandlePlatFormVO>> listExceptionForUi(@PathVariable("organizationId") Long tenantId,
                                                                                 @RequestParam String workcellCode, @RequestParam String siteId) {
        log.info("<====HmeExceptionHandlePlatformController-listExceptionForUi:{}，{}，{}", tenantId, workcellCode, siteId);
        return Results.success(platformRepository.listExceptionForUi(tenantId, workcellCode, siteId));
    }

    /**
     * @param tenantId 租户ID
     * @param createVO 创建VO（包含设备编码）
     * @return : io.tarzan.common.domain.sys.ResponseData<com.ruike.hme.domain.vo.HmeExcRecordCreateVO>
     * @Description: 异常处理平台-扫描设备编码后校验并返回设备ID
     * @author: Deng Xu
     * @date 2020/6/23 11:32
     * @version 1.0
     */
    @ApiOperation(value = "异常处理平台-扫描设备编码后校验并返回设备ID")
    @GetMapping(value = "/equipment/verification/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExcRecordCreateVO> equipmentVerification(@PathVariable("organizationId") Long tenantId,
                                                                      HmeExcRecordCreateVO createVO) {
        log.info("<====HmeExceptionHandlePlatformController-equipmentVerification:{}，{}", tenantId, createVO);
        return Results.success(platformRepository.equipmentVerification(tenantId, createVO));
    }

    /**
     * @param tenantId 租户ID
     * @param createVO 创建VO（包含物料条码）
     * @return : io.tarzan.common.domain.sys.ResponseData<com.ruike.hme.domain.vo.HmeExcRecordCreateVO>
     * @Description: 异常处理平台-扫描物料条码后校验是否存在并返回物料信息
     * @author: Deng Xu
     * @date 2020/6/23 11:53
     * @version 1.0
     */
    @ApiOperation(value = "异常处理平台-扫描物料条码后校验是否存在并返回物料信息")
    @GetMapping(value = "/material/lot/verification/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExcRecordCreateVO> materialLotVerification(@PathVariable("organizationId") Long tenantId,
                                                                        HmeExcRecordCreateVO createVO) {
        log.info("<====HmeExceptionHandlePlatformController-materialLotVerification:{}，{}", tenantId, createVO);
        return Results.success(platformRepository.materialLotVerification(tenantId, createVO));
    }

    /**
     * @param tenantId 租戶ID
     * @param createVO 創建VO
     * @return : io.tarzan.common.domain.sys.ResponseData<com.ruike.hme.domain.entity.HmeExcWkcRecord>
     * @Description: 异常处理平台-创建异常记录
     * @author: Deng Xu
     * @date 2020/6/23 14:43
     * @version 1.0
     */
    @ApiOperation(value = "异常处理平台-创建异常记录")
    @PostMapping(value = {"/create/exception/record"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExcWkcRecord> createExceptionRecord(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody HmeExcRecordCreateVO createVO) {
        log.info("<====HmeExceptionHandlePlatformController-createExceptionRecord:{}，{}", tenantId, createVO);
        return Results.success(platformRepository.createExceptionRecord(tenantId, createVO));
    }

    /**
     * @param tenantId             租户ID
     * @param exceptionWkcRecordId 异常记录ID
     * @return : io.tarzan.common.domain.sys.ResponseData<com.ruike.hme.domain.vo.HmeExceptionQueryVO>
     * @Description: 异常处理平台-异常历史查看
     * @author: Deng Xu
     * @date 2020/6/23 10:29
     * @version 1.0
     */
    @ApiOperation(value = "异常处理平台-异常历史查看")
    @GetMapping(value = "/query/exception/history/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExceptionRecordQueryVO> queryHistoryForUi(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestParam String exceptionWkcRecordId) {
        log.info("<====HmeExceptionHandlePlatformController-queryHistoryForUi:{}，{}", tenantId, exceptionWkcRecordId);
        return Results.success(platformRepository.queryHistoryForUi(tenantId, exceptionWkcRecordId));
    }

    /**
     * 异常处理平台-异常关闭
     *
     * @param tenantId 租户id
     * @param createVO 创建VO
     * @return org.springframework.http.ResponseEntity<com.ruike.hme.domain.entity.HmeExcWkcRecord>
     * @author sanfeng.zhang@hand-china.com 2020/8/3 11:50
     */
    @ApiOperation(value = "异常处理平台-异常关闭")
    @PostMapping(value = {"/close-exception-record"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExcWkcRecord> closeExceptionRecord(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody HmeExcRecordCreateVO createVO) {
        log.info("<====HmeExceptionHandlePlatformController-closeExceptionRecord:{}，{}", tenantId, createVO);
        return Results.success(platformRepository.closeExceptionRecord(tenantId, createVO));
    }

    /**
     * <strong>Title : areaCJProdLineByUserId</strong><br/>
     * <strong>Description : 异常处理平台-根据用户查询有效的区域、车间、产线 </strong><br/>
     * <strong>Create on : 2021/2/25 2:17 下午</strong><br/>
     *
     * @param tenantId
     * @param userId
     * @return org.springframework.http.ResponseEntity<com.ruike.hme.domain.entity.HmeExcWkcRecord>
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    @ApiOperation(value = "异常处理平台-根据用户查询有效的区域、车间、产线")
    @PostMapping(value = {"/query-area-workshop-prodLine-by-userId"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeAreaWorkshopProdLineVO> areaCJProdLineByUserId(@PathVariable("organizationId") Long tenantId,
                                                                            @RequestParam Long userId) {
        log.info("<====HmeExceptionHandlePlatformController-areaCJProdLineByUserId:tenantId:{}，userId:{}", tenantId, userId);
        return Results.success(platformRepository.areaCJProdLineByUserId(tenantId, userId));
    }


    /**
     * <strong>Title : listExceptionNotLoginForUi</strong><br/>
     * <strong>Description : 异常处理平台-主界面查询-不登陆工位查询 </strong><br/>
     * <strong>Create on : 2021/2/25 4:37 下午</strong><br/>
     *
     * @param tenantId
     * @param siteId
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.domain.vo.HmeExceptionHandlePlatFormVO>>
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    @ApiOperation(value = "异常处理平台-主界面查询-不登陆工位查询")
    @GetMapping(value = "/list/exception/not/login/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeExceptionHandlePlatFormVO>> listExceptionNotLoginForUi(@PathVariable("organizationId") Long tenantId,
                                                                                         @RequestParam String siteId) {
        log.info("<====HmeExceptionHandlePlatformController-listExceptionNotLoginForUi:{}，{}", tenantId, siteId);
        return Results.success(platformRepository.listExceptionNotLoginForUi(tenantId, siteId));
    }

    /**
     * <strong>Title : listExceptionNotLoginHistoryForUi</strong><br/>
     * <strong>Description : 异常处理平台-异常工单查询历史-不登陆工位查询-限制当前用户 </strong><br/>
     * <strong>Create on : 2021/3/4 4:25 下午</strong><br/>
     *
     * @param tenantId
     * @param exceptionWkcRecordId
     * @return org.springframework.http.ResponseEntity<java.util.List<com.ruike.hme.domain.vo.HmeExceptionHandlePlatFormVO>>
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    @ApiOperation(value = "异常处理平台-异常清单查询历史-不登陆工位查询-限制当前用户")
    @GetMapping(value = "/list/exception/history/not/login/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExceptionRecordQueryVO> listExceptionNotLoginHistoryForUi(@PathVariable("organizationId") Long tenantId,
                                                                                       @RequestParam String exceptionWkcRecordId) {
        log.info("<====HmeExceptionHandlePlatformController-listExceptionNotLoginHistoryForUi:{}，{}", tenantId, exceptionWkcRecordId);
        return Results.success(platformRepository.listExceptionNotLoginHistoryForUi(tenantId, exceptionWkcRecordId));
    }


}
