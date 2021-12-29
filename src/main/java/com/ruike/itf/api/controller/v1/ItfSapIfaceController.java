package com.ruike.itf.api.controller.v1;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruike.itf.api.dto.*;
import com.ruike.itf.app.service.*;
import com.ruike.itf.domain.entity.ItfProductionVersionIface;
import com.ruike.itf.domain.entity.*;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tarzan.iface.app.service.MtStandardOperationIfaceService;
import tarzan.iface.domain.entity.MtStandardOperationIface;

import java.util.List;
import java.util.Map;

/**
 * 工单接口表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@RestController("itfSapIfaceController.v1")
@RequestMapping("/v1/itf-sap-iface")
@Slf4j
public class ItfSapIfaceController extends BaseController {

    @Autowired
    private ItfWorkOrderIfaceService itfWorkOrderIfaceService;

    @Autowired
    private ItfInvItemIfaceService itfInvItemIfaceService;

    @Autowired
    private ItfItemGroupIfaceService itfItemGroupIfaceService;

    @Autowired
    private ItfInstructionDocIfaceService itfInstructionDocIfaceService;

    @Autowired
    private ItfProductionVersionIfaceService itfProductionVersionIfaceService;

    @Autowired
    private MtStandardOperationIfaceService mtStandardOperationIfaceService;

    @Autowired
    private ItfRoutingOperationIfaceService itfRoutingOperationIfaceService;

    @Autowired
    private ItfSubinventoryIfaceService itfSubinventoryIfaceService;

    @Autowired
    private ItfBomComponentIfaceService itfBomComponentIfaceService;

    @Autowired
    private ItfInternalOrderIfaceService itfInternalOrderIfaceService;

    @Autowired
    private ItfCostcenterIfaceService itfCostcenterIfaceService;

    @Autowired
    private ItfWorkCenterIfaceService itfWorkCenterIfaceService;

    @Autowired
    private ItfAfterSalesRepairIfacesService itfAfterSalesRepairIfacesService;

    @Autowired
    private ItfSnSapIfaceService itfSnSapIfaceService;

    @Autowired
    private ItfModLocatorIfaceService itfModLocatorIfaceService;

    @ApiOperation(value = "物料同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/item-sync")
    public ResponseEntity<List<ItfInvItemReturnDTO>> invItemSync(@RequestBody String dataStr) {
        log.info("<==== item-sync Success requestPayload: {}", dataStr);
        Map<String,Object> itemMap = JSONArray.parseObject(dataStr, Map.class);
        List<ItfInvItemReturnDTO> list = itfInvItemIfaceService.invoke(itemMap);
        return Results.success(list);
    }

    @ApiOperation(value = "工单同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/work-order")
    public ResponseEntity<List<ItfWorkOrderReturnDTO>> workOrderSync(@RequestBody String dataStr) {
        log.info("<==== workOrderSync request Success Payload: {}", dataStr);
        Map<String,Object> dto = JSON.parseObject(dataStr, Map.class);
        List<ItfWorkOrderReturnDTO> list = itfWorkOrderIfaceService.invoke(dto);
        return Results.success(list);
    }

    @ApiOperation(value = "物料组同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/item-group")
    public ResponseEntity<List<ItfItemGroupReturnDTO>> itemGroupSync(@RequestBody String dataStr) {
        log.info("<==== item-group Success requestPayload: {}", dataStr);
        List<ItfSapIfaceDTO> itemGroupList = JSONArray.parseArray(dataStr, ItfSapIfaceDTO.class);
        List<ItfItemGroupReturnDTO> list = itfItemGroupIfaceService.invoke(itemGroupList);
        return Results.success(list);
    }

    /**
     * 采购订单同步接口
     *
     * @param dataStr
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "采购订单同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/instruction")
    public ResponseEntity<List<ItfInstructionDocIface>> instructionSync(@RequestBody String dataStr) {
        log.info("<==== instruction Success requestPayload: {}", dataStr);
        ItfInstructionSyncDTO itfInstructionSyncDTO = JSON.parseObject(dataStr, ItfInstructionSyncDTO.class);
        List<ItfInstructionDocIface> list = itfInstructionDocIfaceService.invoke(itfInstructionSyncDTO);
        return Results.success(list);
    }

    /**
     * 工艺路线同步接口
     *
     * @param dataStr
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "工艺路线同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/routing-operation")
    public ResponseEntity routingOperationSync(@RequestBody String dataStr) {
        log.info("<==== routing-operation Success requestPayload: {}", dataStr);
        Map dataMap = JSON.parseObject(dataStr, Map.class);
        try {
            // 20211123 modfiy by sanfeng.zhang for wenxin.zhang 取消实时调用，改为调度任务异步调用
            Map<String, Object> map = itfRoutingOperationIfaceService.invoke2(dataMap);
            return Results.success(map);
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }

    /**
     * 标准工序同步接口
     *
     * @param dataStr
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "标准工序同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/operation")
    public ResponseEntity operationSync(@RequestBody String dataStr) {
        log.info("<==== StandardOperation request Success Payload: {}", dataStr);
        List<ItfSapIfaceDTO> dto = JSONArray.parseArray(dataStr, ItfSapIfaceDTO.class);
        try {
            List<MtStandardOperationIface> errorList = mtStandardOperationIfaceService.invoke(dto);
            return Results.success(errorList);
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }

    /**
     * 库存地点同步接口
     *
     * @param dataStr
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "库存地点同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/subinventory")
    public ResponseEntity subinventorySync(@RequestBody String dataStr) {
        log.info("<==== subinventory request Success Payload: {}", dataStr);
        List<ItfSapIfaceDTO> dto = JSONArray.parseArray(dataStr, ItfSapIfaceDTO.class);
        try {
            List<ItfSubinventoryIface> errorList = itfSubinventoryIfaceService.invoke(dto);
            return Results.success(errorList);
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }

    /**
     * 仓库同步接口
     *
     * @param dataStr
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "仓库同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/mod-locator")
    public ResponseEntity modLocatorSync(@RequestBody String dataStr) {
        log.info("<==== modLocatorSync request Success Payload: {}", dataStr);
        List<ItfModLocatorIfaceSyncDTO> dto = JSONArray.parseArray(dataStr, ItfModLocatorIfaceSyncDTO.class);
        try {
            List<ItfModLocatorIfaceSyncDTO> errorList = itfModLocatorIfaceService.invoke(dto);
            return Results.success(errorList);
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }


    /**
     * BOM同步接口
     *
     * @param dataStr
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "BOM同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/bom-component")
    public ResponseEntity bomComponentSync(@RequestBody String dataStr) {
        log.info("<==== bomComponent request Success Payload: {}", dataStr);
        Map dto = JSON.parseObject(dataStr, Map.class);
        try {
            List<ItfBomComponentIface> errorList = itfBomComponentIfaceService.invoke(dto);
            return Results.success(errorList);
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }

    /**
     * 生产版本同步接口
     *
     * @param dataStr
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "生产版本同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/production-version")
    public ResponseEntity productionVersionSync(@RequestBody String dataStr) {
        log.info("<==== productionVersion request Success Payload: {}", dataStr);
        List<ItfSapIfaceDTO> dto = JSONArray.parseArray(dataStr, ItfSapIfaceDTO.class);
        try {
            List<ItfProductionVersionIface> errorList = itfProductionVersionIfaceService.invoke(dto);
            return Results.success(errorList);
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }

    /**
     * 内部订单同步接口
     *
     * @param dataStr
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "内部订单同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/internal-order")
    public ResponseEntity internalOrderSync(@RequestBody String dataStr) {
        log.info("<==== internalOrderSync request Success Payload: {}", dataStr);
        List<ItfSapIfaceDTO> dto = JSONArray.parseArray(dataStr, ItfSapIfaceDTO.class);
        try {
            List<ItfInternalOrderIface> errorList = itfInternalOrderIfaceService.invoke(dto);
            return Results.success(errorList);
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }


    /**
     * 成本中心同步接口
     *
     * @param dataStr
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "成本中心同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/costcenter")
    public ResponseEntity costcenterSync(@RequestBody String dataStr) {
        log.info("<==== costcenterSync request Success Payload: {}", dataStr);
        List<ItfSapIfaceDTO> dto = JSONArray.parseArray(dataStr, ItfSapIfaceDTO.class);
        try {
            List<ItfCostcenterIface> errorList = itfCostcenterIfaceService.invoke(dto);
            return Results.success(errorList);
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }

    /**
     * 工作中心同步接口
     *
     * @param dataStr
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "工作中心同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/work-center")
    public ResponseEntity workCenterSync(@RequestBody String dataStr) {
        log.info("<==== workCenterSync request Success Payload: {}", dataStr);
        List<ItfSapIfaceDTO> dto = JSONArray.parseArray(dataStr, ItfSapIfaceDTO.class);
        try {
            List<ItfWorkCenterIface> errorList = itfWorkCenterIfaceService.invoke(dto);
            return Results.success(errorList);
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }

    /**
     * 大仓等级平台同步接口
     *
     * @param dataStr
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "大仓登记平台同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/after-sales-repair")
    public ResponseEntity afterSalesRepairSync(@RequestBody String dataStr) {
        log.info("<==== afterSalesRepairSync request Success Payload: {}", dataStr);
        List<ItfAfterSalesRepairSyncDTO> dto = JSONArray.parseArray(dataStr, ItfAfterSalesRepairSyncDTO.class);
        try {
            List<ItfAfterSalesRepairIfaces> errorList = itfAfterSalesRepairIfacesService.afterSalesRepairSync(dto);
            return Results.success(errorList);
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }

    /**
     * 成品SN发货回传接口
     *
     * @param dataStr
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "成品SN发货回传接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/update-material-status")
    public ResponseEntity updateMaterialStatus(@RequestBody String dataStr) {
        log.info("<==== updateMaterialStatus request Success Payload: {}", dataStr);
        List<ItfSnSapIfaceDTO> dto = JSONArray.parseArray(dataStr, ItfSnSapIfaceDTO.class);
        try {
            List<ItfSnSapIfaceDTO> errorList = itfSnSapIfaceService.updateMaterialStatus(dto);
            return Results.success(errorList);
        } catch (Exception e) {
            return Results.error(e.getMessage());
        }
    }


}
