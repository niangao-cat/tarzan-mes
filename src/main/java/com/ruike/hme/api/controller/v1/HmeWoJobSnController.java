package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeWoJobSnService;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.entity.HmeMaterialLotNcLoad;
import com.ruike.hme.domain.repository.HmeMaterialLotNcLoadRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;
import tarzan.inventory.domain.entity.MtMaterialLot;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * wo工艺作业记录表 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2020-08-12 10:38:22
 */
@RestController("hmeWoJobSnController.v1")
@RequestMapping("/v1/{organizationId}/hme-wo-job-sn")
@Api(tags = SwaggerApiConfig.HME_WO_JOB_SN)
public class HmeWoJobSnController extends BaseController {

    @Autowired
    private HmeWoJobSnService hmeWoJobSnService;

    @Autowired
    private HmeMaterialLotNcLoadRepository hmeMaterialLotNcLoadRepository;


    /**
     * @param tenantId
     * @param dto
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.api.dto.HmeWoJobSnReturnDTO>>
     * @description 获取工单数据
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/12 16:11
     **/
    @ApiOperation(value = "获取工单数据")
    @GetMapping(value = {"/worklist"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeWoJobSnReturnDTO>> workList(@PathVariable("organizationId") Long tenantId,
                                                              HmeWoJobSnDTO dto, PageRequest pageRequest) {

        Page<HmeWoJobSnReturnDTO> hmeWoJobSnReturnDTOs = hmeWoJobSnService.workList(tenantId, dto,pageRequest);
        return Results.success(hmeWoJobSnReturnDTOs);
    }

    /**
     * @param tenantId
     * @param workOrderId
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.api.dto.HmeWoJobSnReturnDTO6>>
     * @description 获取工单组件
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/24 9:48
     **/
    @ApiOperation(value = "获取工单组件")
    @GetMapping(value = {"/component"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeWoJobSnReturnDTO6>> component(@PathVariable("organizationId") Long tenantId,
                                                                String workOrderId) {

        List<HmeWoJobSnReturnDTO6> hmeWoJobSnReturnDTO6s = hmeWoJobSnService.component(tenantId, workOrderId);
        return Results.success(hmeWoJobSnReturnDTO6s);
    }

    /**
     * @param tenantId
     * @param dto
     * @return org.springframework.http.ResponseEntity<com.ruike.hme.api.dto.HmeWoJobSnReturnDTO2>
     * @description 获取已生成来料信息和剩余芯片数量
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/13 10:41
     **/
    @ApiOperation(value = "获取剩余芯片数")
    @GetMapping(value = {"/remaining/qty"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWoJobSnReturnDTO2> remainingQty(@PathVariable("organizationId") Long tenantId,
                                                             HmeWoJobSnDTO2 dto) {

        HmeWoJobSnReturnDTO2 hmeWoJobSnReturnDTO2 = hmeWoJobSnService.remainingQty(tenantId, dto);
        return Results.success(hmeWoJobSnReturnDTO2);
    }

    /**
     * @param tenantId
     * @param dto
     * @return org.springframework.http.ResponseEntity<com.ruike.hme.api.dto.HmeWoJobSnReturnDTO2>
     * @description 获取已生成来料信息和剩余芯片数量
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/13 10:41
     **/
    @ApiOperation(value = "获取单元芯片数")
    @GetMapping(value = {"/cosnum"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWoJobSnReturnDTO2> cosNum(@PathVariable("organizationId") Long tenantId,
                                                       HmeWoJobSnDTO2 dto) {

        HmeWoJobSnReturnDTO2 hmeWoJobSnReturnDTO2 = hmeWoJobSnService.cosNum(tenantId, dto);
        return Results.success(hmeWoJobSnReturnDTO2);
    }

    /**
     * @param tenantId
     * @param dto
     * @return org.springframework.http.ResponseEntity<com.ruike.hme.domain.entity.HmeIncomingRecord>
     * @description 来料信息记录
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/13 10:41
     **/
    @ApiOperation(value = "来料信息记录")
    @PostMapping(value = {"/add/incoming"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosOperationRecord> addIncoming(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody HmeCosOperationRecordDTO dto) {

        HmeCosOperationRecord hmeCosOperationRecord = hmeWoJobSnService.addIncoming(tenantId, dto);
        return Results.success(hmeCosOperationRecord);
    }

    /**
     * @param tenantId
     * @param dto
     * @return org.springframework.http.ResponseEntity<com.ruike.hme.api.dto.HmeWoJobSnReturnDTO3>
     * @description 点击工单获取信息
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/13 10:40
     **/
    @ApiOperation(value = "点击工单获取信息")
    @GetMapping(value = {"/work/details"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWoJobSnReturnDTO3> workDetails(@PathVariable("organizationId") Long tenantId,
                                                            HmeWoJobSnDTO3 dto) {

        HmeWoJobSnReturnDTO3 hmeWoJobSnReturnDTO3 = hmeWoJobSnService.workDetails(tenantId, dto);
        return Results.success(hmeWoJobSnReturnDTO3);
    }


    /**
     * @param tenantId
     * @param dto
     * @return org.springframework.http.ResponseEntity<com.ruike.hme.api.dto.HmeWoJobSnReturnDTO3>
     * @description 扫描物料条码
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/14 9:20
     **/
    @ApiOperation(value = "扫描物料条码")
    @PostMapping(value = {"/scan/materiallot"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWoJobSnReturnDTO3> scanMaterialLot(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody HmeWoJobSnDTO3 dto) {

        HmeWoJobSnReturnDTO3 hmeWoJobSnReturnDTO3 = hmeWoJobSnService.scanMaterialLot(tenantId, dto);
        return Results.success(hmeWoJobSnReturnDTO3);
    }

    /**
     * @param tenantId
     * @param dto
     * @return org.springframework.http.ResponseEntity<com.ruike.hme.api.dto.HmeWoJobSnReturnDTO3>
     * @description 出站
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/14 9:20
     **/
    @ApiOperation(value = "出站")
    @PostMapping(value = {"/siteout/materiallot"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWoJobSnReturnDTO3> siteOut(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody HmeWojobSnDTO4 dto) {

        HmeWoJobSnReturnDTO3 hmeWoJobSnReturnDTO3 = hmeWoJobSnService.siteOut(tenantId, dto);
        return Results.success(hmeWoJobSnReturnDTO3);
    }

    /**
     * @param tenantId
     * @param dto
     * @return org.springframework.http.ResponseEntity<?>
     * @description 不良确认
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/14 9:20
     **/
    @ApiOperation(value = "不良确认")
    @PostMapping(value = {"/nc/load"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> ncLoad(@PathVariable("organizationId") Long tenantId,
                                    @RequestBody List<HmeMaterialLotNcLoad> dto) {

        hmeWoJobSnService.ncLoad(tenantId, dto);
        return Results.success();
    }

    /**
     * @param tenantId
     * @param dto
     * @return org.springframework.http.ResponseEntity<?>
     * @description 不良位置删除
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/14 9:24
     **/
    @ApiOperation(value = "不良位置删除")
    @PostMapping(value = {"/delete/ncload"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> deleteNcLoad(@PathVariable("organizationId") Long tenantId,
                                          @RequestBody List<HmeMaterialLotNcLoad> dto) {

        hmeMaterialLotNcLoadRepository.batchDelete(dto);
        return Results.success();
    }

    /**
     *@description 更新数量
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/26 10:23
     *@param tenantId
     *@param dto
     *@return org.springframework.http.ResponseEntity<?>
     **/
    @ApiOperation(value = "更新数量")
    @PostMapping(value = {"/update/qty"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> updateQty(@PathVariable("organizationId") Long tenantId,
                                       @RequestBody HmeWoJobSnDTO5 dto) {
        hmeWoJobSnService.updateQty(tenantId, dto);
        return Results.success(dto);
    }

    /**
     *@description 根据条码获取数量
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/26 10:23
     *@param tenantId
     *@param materialLotCode
     *@param workOrderId
     *@param wkcLinetId
     *@param siteId
     *@return org.springframework.http.ResponseEntity<?>
     **/
    @ApiOperation(value = "根据条码获取数量")
    @GetMapping(value = {"/materiallot-qty-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> materiallotQtyQuery(@PathVariable("organizationId") Long tenantId,
                                                 String materialLotCode,
                                                 String workOrderId,
                                                 String wkcLinetId,
                                                 String siteId) {
        MtMaterialLot mtMaterialLot = hmeWoJobSnService.materiallotQtyQuery(tenantId, materialLotCode, workOrderId,wkcLinetId,siteId);
        return Results.success(mtMaterialLot);
    }

    /**
     *@description 拆分
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/26 10:23
     *@param tenantId
     *@param dto
     *@return org.springframework.http.ResponseEntity<?>
     **/
    @ApiOperation(value = "拆分")
    @PostMapping(value = {"/materiallot-split"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWoJobSnReturnDTO7> materialLotSplit(@PathVariable("organizationId") Long tenantId,
                                              @RequestBody HmeWoJobSnDTO6 dto) {
        HmeWoJobSnReturnDTO7 hmeWoJobSnReturnDTO7 = hmeWoJobSnService.materialLotSplit(tenantId, dto);
        return Results.success(hmeWoJobSnReturnDTO7);
    }

    @ApiOperation(value = "更新条码扩展及记录信息")
    @PostMapping(value = {"/update-barcode-record"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> updateBarcodeRecord(@PathVariable("organizationId") Long tenantId,
                                              @RequestBody HmeWoJobSnReturnDTO7 dto) {
        return Results.success(hmeWoJobSnService.updateBarcodeRecord(tenantId, dto));
    }

    /**
     *@description 查询数据
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/27 14:55
     *@param tenantId
     *@param operationRecordId
     *@return org.springframework.http.ResponseEntity<?>
     **/
    @ApiOperation(value = "查询数据")
    @GetMapping(value = {"/materiallot-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> materiallotQuery(@PathVariable("organizationId") Long tenantId,
                                             String operationRecordId) {
        HmeWoJobSnReturnDTO7 hmeWoJobSnReturnDTO7 = hmeWoJobSnService.materiallotQuery(tenantId, operationRecordId);
        return Results.success(hmeWoJobSnReturnDTO7);
    }

    @ApiOperation(value = "wafer拆分")
    @PostMapping(value = {"/wafer-split"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeWoJobSnReturnDTO7> waferSplit(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody HmeWoJobSnDTO6 dto) {
        HmeWoJobSnReturnDTO7 hmeWoJobSnReturnDTO7 = hmeWoJobSnService.waferSplit(tenantId, dto);
        return Results.success(hmeWoJobSnReturnDTO7);
    }

    @ApiOperation(value = "来料录入导出")
    @GetMapping(value = {"/incoming-export"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> incomingExport(@PathVariable("organizationId") Long tenantId,
                                                                    HmeWoJobSnDTO dto,
                                                                    HttpServletResponse response) {

        hmeWoJobSnService.incomingExport(tenantId, dto, response);
        return Results.success();
    }

}
