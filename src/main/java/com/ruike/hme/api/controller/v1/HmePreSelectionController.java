package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmePreSelectionService;
import com.ruike.hme.domain.entity.HmeSelectionDetails;
import com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandQueryExportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.List;

/**
 * 预挑选基础表 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2020-08-18 15:00:33
 */
@RestController("hmePreSelectionController.v1")
@RequestMapping("/v1/{organizationId}/hme-pre-selections")
@Api(tags = SwaggerApiConfig.HME_PRE_SELECTION)
public class HmePreSelectionController extends BaseController {

    @Autowired
    private HmePreSelectionService hmePreSelectionService;

    /**
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page < com.ruike.hme.api.dto.HmePreSelectionReturnDTO>>
     * @description 查询右侧工单数据
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/27 19:54
     **/
    @ApiOperation(value = "查询左侧工单数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/workorder/query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmePreSelectionReturnDTO>> workOrderQuery(@PathVariable("organizationId") Long tenantId,
                                                                         HmePreSelectionDTO dto,
                                                                         PageRequest pageRequest) {
        Page<HmePreSelectionReturnDTO> hmePreSelectionReturnDTs = hmePreSelectionService.workOrderQuery(tenantId, dto, pageRequest);
        return Results.success(hmePreSelectionReturnDTs);
    }

    /**
     * @param tenantId
     * @param dto
     * @return org.springframework.http.ResponseEntity<com.ruike.hme.api.dto.HmePreSelectionReturnDTO2>
     * @description 确认开始挑选
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/27 19:54
     **/
    @ApiOperation(value = "确认按钮")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/confirm", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmePreSelectionReturnDTO2> confirm(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody HmePreSelectionDTO2 dto) {
        HmePreSelectionReturnDTO2 hmePreSelectionReturnDT2 = hmePreSelectionService.confirm(tenantId, dto);
        return Results.success(hmePreSelectionReturnDT2);
    }

    /**
     * @param tenantId
     * @param ruleId
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.api.dto.HmeCosRuleLogicDTO>>
     * @description 挑选规则
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/2 14:42
     **/
    @ApiOperation(value = "挑选规则")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/select/rule", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeCosRuleLogicDTO>> selectRule(@PathVariable("organizationId") Long tenantId,
                                                               String ruleId) {
        List<HmeCosRuleLogicDTO> hmeCosRuleLogicDTOs = hmePreSelectionService.selectRule(tenantId, ruleId);
        return Results.success(hmeCosRuleLogicDTOs);
    }

    /**
     * @param tenantId
     * @param selectLot
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.api.dto.HmePreSelectionReturnDTO3>>
     * @description 挑选批次查询具体数据
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/27 19:55
     **/
    @ApiOperation(value = "挑选批次")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/select/lot", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmePreSelectionReturnDTO3>> selectLot(@PathVariable("organizationId") Long tenantId,
                                                                     String selectLot) {
        List<HmePreSelectionReturnDTO3> hmePreSelectionReturnDT3s = hmePreSelectionService.selectLot(tenantId, selectLot);
        return Results.success(hmePreSelectionReturnDT3s);
    }

    /**
     * @param tenantId
     * @param materialLotCode
     * @param selectLot
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.api.dto.HmePreSelectionReturnDTO4>>
     * @description 查询盒子号具体数据
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/27 19:59
     **/
    @ApiOperation(value = "挑选盒子号")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/material/lot", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmePreSelectionReturnDTO4>> materialLot(@PathVariable("organizationId") Long tenantId,
                                                                       String materialLotCode,
                                                                       String selectLot) {
        List<HmePreSelectionReturnDTO4> hmePreSelectionReturnDT4s = hmePreSelectionService.materialLot(tenantId, materialLotCode, selectLot);
        return Results.success(hmePreSelectionReturnDT4s);
    }

    /**
     * @param tenantId
     * @param materialLotCode
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.api.dto.HmePreSelectionReturnDTO4>>
     * @description 扫描新盒子
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/27 20:00
     **/
    @ApiOperation(value = "装入盒子号")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/material/lot/to", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmePreSelectionReturnDTO4>> tomaterialLot(@PathVariable("organizationId") Long tenantId,
                                                                         String materialLotCode) {
        List<HmePreSelectionReturnDTO4> hmePreSelectionReturnDT4s = hmePreSelectionService.tomaterialLot(tenantId, materialLotCode);
        return Results.success(hmePreSelectionReturnDT4s);
    }


    /**
     * @param tenantId
     * @param selectLot
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.domain.entity.HmeSelectionDetails>>
     * @description 查询明细
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/27 20:01
     **/
    @ApiOperation(value = "查询明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/select/details", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeSelectionDetails>> selectDetails(@PathVariable("organizationId") Long tenantId,
                                                                   String selectLot) {
        List<HmeSelectionDetails> hmeSelectionDetails = hmePreSelectionService.selectDetails(tenantId, selectLot);
        return Results.success(hmeSelectionDetails);
    }

    /**
     * @param tenantId
     * @param dtos
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.api.dto.HmePreSelectionReturnDTO4>>
     * @description 装入新盒子
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/27 20:01
     **/
    @ApiOperation(value = "装入")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/load/details", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmePreSelectionReturnDTO4>> load(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody List<HmePreSelectionDTO3> dtos) {
        List<HmePreSelectionReturnDTO4> HmePreSelectionReturnDTO4s = hmePreSelectionService.load(tenantId, dtos);
        return Results.success(HmePreSelectionReturnDTO4s);
    }

    @ApiOperation(value = "根据库位获取盒子号")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/getmateriallot", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmePreSelectionReturnDTO5>> selectMaterialLot(@PathVariable("organizationId") Long tenantId,
                                                                             String locatorCode) {
        List<HmePreSelectionReturnDTO5> hmePreSelectionReturnDTO5s = hmePreSelectionService.getMateriallot(tenantId, locatorCode);
        return Results.success(hmePreSelectionReturnDTO5s);
    }

    //2020112修改

    /**
     * @param tenantId
     * @param containerCode
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.api.dto.HmePreSelectionReturnDTO5>>
     * @description 根据容器获取盒子信息
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/3 15:07
     **/
    @ApiOperation(value = "根据容器获取盒子信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/materialLot-query-container", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmePreSelectionReturnDTO5>> materialLotQueryByContainer(@PathVariable("organizationId") Long tenantId,
                                                                                       String containerCode) {
        List<HmePreSelectionReturnDTO5> hmePreSelectionReturnDTO5 = hmePreSelectionService.materialLotQueryByContainer(tenantId, containerCode);
        return Results.success(hmePreSelectionReturnDTO5);
    }

    /**
     * @param tenantId
     * @param materialLotCode
     * @return org.springframework.http.ResponseEntity<com.ruike.hme.api.dto.HmePreSelectionReturnDTO5>
     * @description 查询盒子信息
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/2 14:29
     **/
    @ApiOperation(value = "查询盒子信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/materialLot-query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmePreSelectionReturnDTO5> materialLotQuery(@PathVariable("organizationId") Long tenantId,
                                                                      String materialLotCode) {
        HmePreSelectionReturnDTO5 hmePreSelectionReturnDTO5 = hmePreSelectionService.materialLotQuery(tenantId, materialLotCode);
        return Results.success(hmePreSelectionReturnDTO5);
    }

    /**
     * @param tenantId
     * @param materialLotCode
     * @return org.springframework.http.ResponseEntity<java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO5>>
     * @description 更新查询到的盒子信息
     * @author xin.t@raycuslaser.com
     * @date 2021/08/06 16:29
     **/
    @ApiOperation(value = "更新查询到的盒子信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/materialLot-query-update", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmePreSelectionReturnDTO5>> materialLotReQuery(@PathVariable("organizationId") Long tenantId,
                                                                              String materialLotCode) {
        List<String> materialLotCodeList = StringUtils.isNotBlank(materialLotCode) ? Arrays.asList(StringUtils.split(materialLotCode, ",")) : null;
        List<HmePreSelectionReturnDTO5> hmePreSelectionReturnDTO5List = hmePreSelectionService.materialLotReQuery(tenantId, materialLotCodeList);
        return Results.success(hmePreSelectionReturnDTO5List);
    }

    /**
     * @param tenantId
     * @param selectLot
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.api.dto.HmePreSelectionReturnDTO3>>
     * @description 挑选批次查询具体数据
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/27 19:55
     **/
    @ApiOperation(value = "挑选批次")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/select-lot-query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmePreSelectionReturnDTO3>> selectLotQuery(@PathVariable("organizationId") Long tenantId,
                                                                          String selectLot,
                                                                          PageRequest pageRequest) {
        Page<HmePreSelectionReturnDTO3> hmePreSelectionReturnDT3s = hmePreSelectionService.selectLotQuery(tenantId, selectLot, pageRequest);
        return Results.success(hmePreSelectionReturnDT3s);
    }

    /**
     * @param tenantId
     * @param dtos
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.api.dto.HmePreSelectionDTO6>>
     * @description 装入
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/2 15:42
     **/
    @ApiOperation(value = "装入")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/load/details/new", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmePreSelectionDTO6>> loadNew(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody List<HmePreSelectionDTO6> dtos) {
        hmePreSelectionService.loadNew(tenantId, dtos);
        return Results.success(dtos);
    }

    /**
     * @param tenantId
     * @param productTypeMeaning
     * @param cosRuleCode
     * @param statusMeaning
     * @param materialCode
     * @param materialName
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.api.dto.HmePreSelectionReturnDTO6>>
     * @description 挑选批次查询
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/3 16:23
     **/
    @ApiOperation(value = "挑选批次查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/select-lot-query-all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmePreSelectionReturnDTO6>> selectLotQueryAll(@PathVariable("organizationId") Long tenantId,
                                                                             String productTypeMeaning,
                                                                             String cosRuleCode,
                                                                             String statusMeaning,
                                                                             String materialCode,
                                                                             String materialName,
                                                                             PageRequest pageRequest) {
        Page<HmePreSelectionReturnDTO6> hmePreSelectionReturnDTO6S = hmePreSelectionService.selectLotQueryAll(tenantId, productTypeMeaning, cosRuleCode, statusMeaning, materialCode, materialName,pageRequest);
        return Results.success(hmePreSelectionReturnDTO6S);
    }

    /**
     * @param tenantId
     * @param containerCode
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.api.dto.HmePreSelectionReturnDTO7>>
     * @description 挑选未挑选批次查询
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/2 17:33
     **/
    @ApiOperation(value = "挑选未挑选批次查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/select-lot-query-else", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmePreSelectionReturnDTO7>> selectLotQueryElse(@PathVariable("organizationId") Long tenantId,
                                                                              String containerCode) {
        List<HmePreSelectionReturnDTO7> hmePreSelectionReturnDTO7S = hmePreSelectionService.selectLotQueryElse(tenantId, containerCode);
        return Results.success(hmePreSelectionReturnDTO7S);
    }


    @ApiOperation(value = "挑选信息导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/select-lot-information", produces = "application/json;charset=UTF-8")
    @ExcelExport(HmePreSelectionReturnDTO8.class)
    public ResponseEntity<List<HmePreSelectionReturnDTO8>> selectLotInformation(@PathVariable("organizationId") Long tenantId,
                                                                                String selectLot,
                                                                                HttpServletResponse response,
                                                                                ExportParam exportParam) {
        List<HmePreSelectionReturnDTO8> hmePreSelectionReturnDTO8s = hmePreSelectionService.selectLotInformation(tenantId, selectLot);
        return Results.success(hmePreSelectionReturnDTO8s);
    }

    /**
     * @param tenantId
     * @param dto
     * @return org.springframework.http.ResponseEntity<com.ruike.hme.api.dto.HmePreSelectionReturnDTO2>
     * @description 确认开始挑选
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/30 14:00
     **/
    @ApiOperation(value = "确认开始挑选")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/confirm-new", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmePreSelectionReturnDTO2> confirmNew(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody HmePreSelectionDTO1 dto) {
        HmePreSelectionReturnDTO2 hmePreSelectionReturnDT2 = hmePreSelectionService.confirmNew(tenantId, dto);
        return Results.success(hmePreSelectionReturnDT2);
    }

    /**
     * @param tenantId
     * @param containerCode
     * @return org.springframework.http.ResponseEntity<java.util.List < com.ruike.hme.api.dto.HmePreSelectionReturnDTO5>>
     * @description 根据容器获取数量
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/3 15:07
     **/
    @ApiOperation(value = "根据容器获取数量")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/qty-query-container", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> qtyQueryByContainer(@PathVariable("organizationId") Long tenantId,
                                                      String containerCode) {
        String qty = hmePreSelectionService.qtyQueryByContainer(tenantId, containerCode);
        return Results.success(qty);
    }

    @ApiOperation(value = "转移")
    @PostMapping(value = "/transfer", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmePreSelectionDTO7> transfer(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody HmePreSelectionDTO7 dto) {
        hmePreSelectionService.transfer(tenantId, dto);
        return Results.success(dto);
    }

    @ApiOperation(value = "挑选结果撤回数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/recall-data-query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmePreSelectionReturnDTO12>> recallDataQuery(@PathVariable("organizationId") Long tenantId,
                                                                            HmePreSelectionReturnDTO11 dto,
                                                                            PageRequest pageRequest) {
        Page<HmePreSelectionReturnDTO12> hmePreSelectionReturnDT3s = hmePreSelectionService.recallDataQuery(tenantId, dto, pageRequest);
        return Results.success(hmePreSelectionReturnDT3s);
    }

    @ApiOperation(value = "挑选结果撤回")
    @PostMapping(value = "/recall", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmePreSelectionReturnDTO13> recallData(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody HmePreSelectionReturnDTO13 dto) {
        hmePreSelectionService.recallData(tenantId, dto);
        return Results.success(dto);
    }
}
