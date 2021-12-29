package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.api.dto.HmeEquipmentLocatingDTO;
import com.ruike.hme.api.dto.HmeEquipmentUpadteDTO;
import com.ruike.hme.app.service.HmeEquipmentService;
import com.ruike.hme.domain.entity.HmeEqManageTaskDocLine;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
* @Classname HmeEquipmentController
* @Description 设备台账管理 API
* @Date  2020/6/3 18:47
 * @Created by Deng xu
 */
@RestController("hmeEquipmentController.v1")
@RequestMapping("/v1/{organizationId}/hme-equipment")
@Api(tags = SwaggerApiConfig.HME_EQUIPMENT)
@Slf4j
public class HmeEquipmentController extends BaseController {

    @Autowired
    private HmeEquipmentService service;
    @Autowired
    private HmeEquipmentRepository repository;

    /**
     * @Description: 设备台账管理-获取设备基础信息
     * @author: Deng Xu
     * @date 2020/6/3 18:44
     * @param tenantId 租户ID
     * @param condition 查询条件
     * @param pageRequest 分页条件
     * @return : org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEquipmentVO>>
     * @version 1.0
     */
    @ApiOperation(value = "设备台账管理-获取设备基础信息")
    @GetMapping(value = "/list/ui", produces = "application/json;charset=UTF-8")
    @ProcessLovValue
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeEquipmentVO>> listForUi(@PathVariable("organizationId") Long tenantId,
                                                          HmeEquipmentVO condition, PageRequest pageRequest) {
        log.info("<====HmeEquipmentController-listForUi:{}，{}", tenantId, condition);
        condition.initParam();
        return Results.success(service.listForUi(tenantId, condition, pageRequest));
    }

    @ApiOperation(value = "设备台账管理 列表查询")
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    @ProcessLovValue
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeEquipmentVO>> list(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody HmeEquipmentVO condition) {
        log.info("<====HmeEquipmentController-listForUi:{}，{}", tenantId, condition);
        condition.initParam();
        return Results.success(repository.queryEquipmentList(tenantId, condition));
    }

    /**
     *@description 设备台账管理-导出
     *@author wenzhang.yu@hand-china.com
     *@date 2021/3/15 15:47
     *@param tenantId
     *@param exportParam
     *@param response
     *@param condition
     *@return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.domain.vo.HmeEquipmentVO5>>
     **/
    @ApiOperation(value = "设备台账管理-导出")
    @GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
    @ProcessLovValue
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeEquipmentVO5.class)
    public ResponseEntity<List<HmeEquipmentVO5>> listForExport(@PathVariable("organizationId") Long tenantId,
                                                               ExportParam exportParam,
                                                               HttpServletResponse response,
                                                               HmeEquipmentVO condition) {
        log.info("<====HmeEquipmentController-listForExport:{}，{}", tenantId, condition);
        condition.initParam();
        return Results.success(service.listForExport(tenantId, condition, exportParam));
    }
    /**
    * @Description: 设备台账管理-根据设备ID获取设备基础信息
    * @author: Deng Xu
    * @date 2020/6/4 14:24
    * @param tenantId 租户ID
    * @param condition 查询条件（设备ID）
    * @return : io.tarzan.common.domain.sys.ResponseData<com.ruike.hme.domain.vo.HmeEquipmentVO>
    * @version 1.0
    */
    @ApiOperation(value = "设备台账管理-根据设备ID获取设备基础信息")
    @GetMapping(value = "/query/one/ui", produces = "application/json;charset=UTF-8")
    @ProcessLovValue
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEquipmentVO> queryOneForUi(@PathVariable("organizationId") Long tenantId,
                                                  HmeEquipmentVO condition ) {
        log.info("<====HmeEquipmentController-queryOneForUi:{}，{}", tenantId, condition);
        return Results.success(service.queryOneForUi(tenantId, condition ));
    }

    /**
    * @Description: 设备台账管理-新增&更新设备基础信息
    * @author: Deng Xu
    * @date 2020/6/3 18:45
    * @param tenantId 租户ID
    * @param dto 设备台账信息DTO
    * @return : io.tarzan.common.domain.sys.ResponseData<com.ruike.hme.domain.entity.HmeEquipment>
    * @version 1.0
    */
    @ApiOperation(value = "设备台账管理-新增&更新设备基础信息")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEquipment> saveForUi(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody HmeEquipment dto) {
        log.info("<====HmeEquipmentController-saveForUi:{}，{}", tenantId, dto);
        return Results.success(service.saveForUi(tenantId, dto));
    }

    @ApiOperation(value = "工位变更历史查询")
    @GetMapping(value = {"/query/workcellHis/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeEquipmentHisVO2>> queryWorkcellHisForUi(@PathVariable("organizationId") Long tenantId,
                                                                          HmeEquipmentHisVO dto, PageRequest pageRequest) {
        log.info("<====HmeEquipmentController-queryWorkcellHisForUi:{}，{}", tenantId, dto);
        return Results.success(service.queryWorkcellHisForUi(tenantId, dto,pageRequest));
    }

    /**
     * 扫描设备相关信息
     * @param tenantId
     * @param dto
     * @return
     */
    @ApiModelProperty(value = "扫描设备相关信息")
    @GetMapping(value = "/one/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEquipmentVO3> queryOneInfo(@PathVariable("organizationId") Long tenantId, HmeEquipment dto) {
        log.info("<====HmeEquipmentController-/one/ui:{}，{}", tenantId, dto);
        return Results.success(service.queryOneInfo(tenantId, dto));
    }

    /**
     * 设备-工位扫描
     * @param tenantId
     * @param dto
     * @return
     */
    @ApiOperation(value = "设备-工位扫描")
    @PostMapping(value = {"/workcell-scan"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEoJobSnVO7> workcellScan(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody HmeEoJobSnDTO dto) {
        log.info("<====HmeEquipmentController-/workcell-scan:{}，{}", tenantId, dto);
        return Results.success(service.workcellScan(tenantId, dto));
    }

    /**
     * 获取设备点检内容
     * @param tenantId
     * @param dto
     * @return
     */
    @ApiModelProperty(value = "获取设备点检内容")
    @GetMapping(value = {"/equipment/content"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEqManageTaskInfoVO2> equipmentContent(@PathVariable("organizationId") Long tenantId,
                                                                        HmeEquipmentLocatingDTO dto, PageRequest pageRequest) {
        log.info("<====HmeEquipmentController-/equipment/content:{}，{}", tenantId, dto);
        return Results.success(service.equipmentContent(tenantId, dto, pageRequest));
    }

    /**
     * 获取设备保养内容
     * @param tenantId
     * @param dto
     * @return
     */
    @ApiModelProperty(value = "获取设备保养内容")
    @GetMapping(value = {"/maintain/content"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEqManageTaskInfoVO2> maintainContent(@PathVariable("organizationId") Long tenantId,
                                                                       HmeEquipmentLocatingDTO dto, PageRequest pageRequest) {
        log.info("<====HmeEquipmentController-/maintain/content:{}，{}", tenantId, dto);
        return Results.success(service.maintainContent(tenantId, dto, pageRequest));
    }
    /**
     * 点检&&保养按钮
     * @param tenantId
     * @param dto
     * @return
     */
    @ApiModelProperty(value = "点检&&保养按钮")
    @PostMapping(value = {"/equipment/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEqManageTaskDocLine> update(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEquipmentUpadteDTO dto) {
        log.info("<====HmeEquipmentController-/equipment/update:{}，{}", tenantId, dto);
        return Results.success(service.update(tenantId, dto));
    }

    @ApiModelProperty(value = "保养备注保存按钮")
    @PostMapping(value = {"/maintain-equipment-update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeEqManageTaskDocLine> maintainEquipmentUpdate(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEqManageTaskDocLine dto) {
        log.info("<====HmeEquipmentController-/equipment/update:{}，{}", tenantId, dto);
        return Results.success(service.maintainEquipmentUpdate(tenantId, dto));
    }

    @ApiModelProperty(value = "设备台账修改历史")
    @GetMapping(value = {"/query-equipment-his"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeEquipmentHisVO3>> queryEquipmentHis(@PathVariable("organizationId") Long tenantId,
                                                                      String equipmentId,
                                                                      @ApiIgnore PageRequest pageRequest) {
        return Results.success(service.queryEquipmentHis(tenantId, equipmentId, pageRequest));
    }

    /**
     * @param tenantId
     * @param hmeEquipmentVO6
     * @param response
     * @Description 设备资产编码标签打印
     * @Date 2021/04/01 22:38
     * @Created by penglin.sui
     */
    @ApiOperation(value = "设备资产编码标签打印")
    @PostMapping(value = {"/print"})
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> print(@PathVariable("organizationId") Long tenantId,
                                   @RequestBody HmeEquipmentVO6 hmeEquipmentVO6,
                                   HttpServletResponse response) {
        try {
            service.print(tenantId, hmeEquipmentVO6, response);
        } catch (Exception ex) {
            log.error("<==== HmeEquipmentController-print error: {}:{}", ex.getMessage(), ex);
        }
        return Results.success();
    }


    @ApiOperation(value = "设备资产编码标签打印校验")
    @PostMapping(value = {"/print-check"})
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> printCheck(@PathVariable("organizationId") Long tenantId,
                                   @RequestBody HmeEquipmentVO6 hmeEquipmentVO6) {
        service.printCheck(tenantId, hmeEquipmentVO6);
        return Results.success();
    }
}
