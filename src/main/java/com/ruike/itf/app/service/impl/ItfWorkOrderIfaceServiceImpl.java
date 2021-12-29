package com.ruike.itf.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruike.hme.domain.entity.HmeRepairWoSnRel;
import com.ruike.hme.domain.repository.HmeRepairWoSnRelRepository;
import com.ruike.itf.api.dto.*;
import com.ruike.itf.app.service.ItfWorkOrderIfaceService;
import com.ruike.itf.domain.entity.*;
import com.ruike.itf.domain.repository.ItfErpReducedSettleRadioIfaceRepository;
import com.ruike.itf.domain.repository.ItfRepairWoSnRelIfaceRepository;
import com.ruike.itf.domain.vo.ItfWoStatusSendErp;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.*;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.itf.utils.GetDeclaredFields;
import com.ruike.itf.utils.SendESBConnect;
import com.ruike.itf.utils.Utils;
import io.choerodon.core.exception.CommonException;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.iface.domain.repository.MtBomComponentIfaceRepository;
import tarzan.iface.domain.repository.MtOperationComponentIfaceRepository;
import tarzan.iface.domain.repository.MtRoutingOperationIfaceRepository;
import tarzan.iface.domain.repository.MtWorkOrderIfaceRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModProductionLine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.itf.infra.constant.ItfConstant.ConstantValue.YES;


/**
 * 工单接口表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@Slf4j
@Service
public class ItfWorkOrderIfaceServiceImpl implements ItfWorkOrderIfaceService {

    private static final int SQL_ITEM_COUNT_LIMIT = 1000;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private ItfWorkOrderIfaceMapper itfWorkOrderIfaceMapper;

    @Autowired
    private ItfBomComponentIfaceMapper itfBomComponentIfaceMapper;

    @Autowired
    private ItfRoutingOperationIfaceMapper itfRoutingOperationIfaceMapper;

    @Autowired
    private ItfOperationComponentIfaceMapper itfOperationComponentIfaceMapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ItfErpReturnWoStatusIfaceMapper itfErpReturnWoStatusIfaceMapper;

    @Autowired
    private SendESBConnect sendESBConnect;

    @Autowired
    private MtWorkOrderIfaceRepository mtWorkOrderIfaceRepository;

    @Autowired
    private MtRoutingOperationIfaceRepository mtRoutingOperationIfaceRepository;

    @Autowired
    private MtBomComponentIfaceRepository mtBomComponentIfaceRepository;

    @Autowired
    private MtOperationComponentIfaceRepository mtOperationComponentIfaceRepository;

    @Autowired
    private ItfErpReducedSettleRadioIfaceRepository itfErpReducedSettleRadioIfaceRepository;

    @Autowired
    private ItfRepairWoSnRelIfaceRepository itfRepairWoSnRelIfaceRepository;

    @Autowired
    private HmeRepairWoSnRelRepository hmeRepairWoSnRelRepository;

    @Autowired
    private MtUserOrganizationRepository userOrganizationRepository;


    private final String REDIS_LAST_UPDATE_DATE = "MES_ERP_WO_LAST_UPDATE";
    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    /**
     * <strong>Title : invoke</strong><br/>
     * <strong>Description : 工单接口 </strong><br/>
     * <strong>Create on : 2020/12/15 下午2:39</strong><br/>
     *
     * @param map
     * @return java.util.List<com.ruike.itf.api.dto.ItfWorkOrderReturnDTO>
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * kejin.liu | 2020年12月15日14:40:23 | 添加字段<br/>
     * ALTER TABLE `tarzan_mes`.`itf_work_order_iface`
     * ADD COLUMN `COMPLETE_CONTROL_TYPE` varchar(100) NULL COMMENT '百分比' AFTER `MESSAGE`,
     * ADD COLUMN `COMPLETE_CONTROL_QTY` decimal(5,2) NULL COMMENT '超量交货容差限制' AFTER `COMPLETE_CONTROL_TYPE`;<br/>
     * ALTER TABLE `tarzan_mes`.`mt_work_order_iface`
     * ADD COLUMN `COMPLETE_CONTROL_TYPE` varchar(100) NULL COMMENT '百分比' AFTER `MESSAGE`,
     * ADD COLUMN `COMPLETE_CONTROL_QTY` decimal(5,2) NULL COMMENT '超量交货容差限制' AFTER `COMPLETE_CONTROL_TYPE`;<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ItfWorkOrderReturnDTO> invoke(Map<String, Object> map) {
        List<ItfSapIfaceDTO> workOrder = JSONArray.parseArray(map.get("HEADER").toString(), ItfSapIfaceDTO.class);
        List<ItfSapIfaceDTO> bomComponent = JSONArray.parseArray(map.get("COMPONENT").toString(), ItfSapIfaceDTO.class);
        List<ItfSapIfaceDTO> routingOperationComponent = JSONArray.parseArray(map.get("OPERATION").toString(), ItfSapIfaceDTO.class);
        List<ItfWorkOrderSnRelSyncDTO> serial = JSONArray.parseArray(map.get("SERIAL").toString(), ItfWorkOrderSnRelSyncDTO.class);
        // 工单
        List<ItfWorkOrderSyncDTO> workOrderList = new ArrayList<>();
        for (ItfSapIfaceDTO dto : workOrder) {
            workOrderList.add(new ItfWorkOrderSyncDTO(dto));
        }
        // BOM
        List<LovValueDTO> itfNomControl = lovAdapter.queryLovValue(ItfConstant.LovCode.ITF_UOM_CONTROL, tenantId);
        List<ItfBomComponentSyncDTO> bomComponentList = new ArrayList<>();
        for (ItfSapIfaceDTO dto : bomComponent) {
            bomComponentList.add(new ItfBomComponentSyncDTO(dto, workOrderList, itfNomControl));
        }
        // 工艺路线
        List<ItfRoutingOperationSyncDTO> routingOperationList = new ArrayList<>();
        for (ItfSapIfaceDTO dto : routingOperationComponent) {
            routingOperationList.add(new ItfRoutingOperationSyncDTO(dto));
        }
        // 导入批次
        Long batchId = Long.valueOf(this.customDbRepository.getNextKey("mt_work_order_cid_s"));
        Double batchIdD = Double.valueOf(batchId);
        // 工单和SN关系
        workOrderSnRelImport(serial);
        // 处理工单DOM工序工序数据
        ItfWorkOrderSyncParamDTO paramDTO = new ItfWorkOrderSyncParamDTO();
        paramDTO.setWorkOrderList(workOrderList);
        paramDTO.setBomComponentList(bomComponentList);
        paramDTO.setRoutingOperationList(routingOperationList);
        List<ItfWorkOrderReturnDTO> itfWorkOrderReturnDTOS = woBomRoImport(paramDTO, batchIdD);
        // 调用API
        mtWorkOrderIfaceRepository.myWorkOrderInterfaceImport(tenantId, batchId);
        mtRoutingOperationIfaceRepository.myRouterInterfaceImport(tenantId, batchId);
        mtBomComponentIfaceRepository.myBomInterfaceImport(tenantId, batchId);
        mtOperationComponentIfaceRepository.myOperationComponentInterfaceImport(tenantId, batchId);
        return itfWorkOrderReturnDTOS;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void deleteBomComponent(List<String> workOrderNums) {
        if (CollectionUtils.isEmpty(workOrderNums)) {
            return;
        }
        // 查询BOM_ID
        List<String> bomIds = itfWorkOrderIfaceMapper.selectBomId2(workOrderNums);
        if (CollectionUtils.isEmpty(bomIds)) {
            return;
        }
        // 查询BOM_COMPONENT_ID
        List<String> bomComponentIds = itfWorkOrderIfaceMapper.selectBomComponentId(bomIds);
        if (CollectionUtils.isEmpty(bomComponentIds)) {
            return;
        }
        // 查询BOM_SUBSTITUTE_GROUP_ID
        List<String> bomSubstituteGroupIds = itfWorkOrderIfaceMapper.selectBomSubstituteGroupId(bomComponentIds);
        // 删BOM组件表
        if (CollectionUtils.isNotEmpty(bomIds)) {
            String deleteBomComSql = "DELETE FROM mt_bom_component where bom_id in " + "('" + StringUtils.join(bomIds, "','") + "')";
            itfWorkOrderIfaceMapper.deleteBomRelTable(deleteBomComSql);

        }
        // 删BOM组件表
        if (CollectionUtils.isNotEmpty(bomIds)) {
            String deleteBomComSql = "DELETE FROM mt_bom_component where bom_id in " + "('" + StringUtils.join(bomIds, "','") + "')";
            itfWorkOrderIfaceMapper.deleteBomRelTable(deleteBomComSql);

        }
        if (CollectionUtils.isNotEmpty(bomComponentIds)) {
            String deleteBomComAttrSql = "DELETE FROM mt_bom_component_attr where bom_component_id in " + "('" + StringUtils.join(bomComponentIds, "','") + "')";
            String deleteBomComSubGroupSql = "DELETE FROM mt_bom_substitute_group where bom_component_id in " + "('" + StringUtils.join(bomComponentIds, "','") + "')";
            String deleteRoBomComSql = "DELETE FROM mt_router_operation_component where bom_component_id in " + "('" + StringUtils.join(bomComponentIds, "','") + "')";
            itfWorkOrderIfaceMapper.deleteBomRelTable(deleteBomComAttrSql);
            itfWorkOrderIfaceMapper.deleteBomRelTable(deleteBomComSubGroupSql);
            itfWorkOrderIfaceMapper.deleteBomRelTable(deleteRoBomComSql);
        }
        if (CollectionUtils.isNotEmpty(bomSubstituteGroupIds)) {
            String deleteBomComSubSql = "DELETE FROM mt_bom_substitute where bom_substitute_group_id in " + "('" + StringUtils.join(bomSubstituteGroupIds, "','") + "')";
            itfWorkOrderIfaceMapper.deleteBomRelTable(deleteBomComSubSql);
        }
        // 查询 ROUTER_STEP_ID
        List<String> routerStepId = itfWorkOrderIfaceMapper.selectRouterStepId(workOrderNums);
        if (CollectionUtils.isNotEmpty(routerStepId)) {
            itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_DONE_STEP.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
            itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_LINK.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
            itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_NEXT_STEP.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
            itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_OPERATION_TL.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
            itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_OPERATION_COMPONENT.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
            itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_OPERATION.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
            itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_RETURN_STEP.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
            itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_STEP_GROUP_STEP.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
            itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_STEP_GROUP.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
            itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_SUBSTEP_COMPONENT.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
            itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_SUBSTEP.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
            itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_STEP_TL.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
            itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_STEP.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
        }
    }


    /**
     * 工单状态回传接口
     *
     * @param tenantId
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @Override
    public void erpWorkOrderStatusReturnRestProxy(Long tenantId) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        String lastUpdateDate = opsForValue.get(REDIS_LAST_UPDATE_DATE);
        if (StringUtils.isEmpty(lastUpdateDate)) {
            lastUpdateDate = "2000-01-01 00:00:00";
        }
        List<ItfWoStatusSendErp> statusData = itfWorkOrderIfaceMapper.selectWoStatusByLastUpdate(format.parse(lastUpdateDate));
        // 更新REDIS缓存时间
        if (statusData.size() != 0) {
            lastUpdateDate = format.format(statusData.get(0).getLastUpdateDate());
        }
        opsForValue.set(REDIS_LAST_UPDATE_DATE, lastUpdateDate);
        sendErpWorkOrderStatus(tenantId, statusData, "N");
    }

    @Override
    public void erpWorkOrderStatusReturnRestProxy(Long tenantId, List<String> workOrderIds) {
        if (CollectionUtils.isEmpty(workOrderIds)) {
            throw new CommonException("erpWorkOrderStatusReturnRestProxy(workOrderIds)：不允许为空");
        }
        List<ItfWoStatusSendErp> statusData = itfWorkOrderIfaceMapper.selectWoStatusByIds(workOrderIds);
        sendErpWorkOrderStatus(tenantId, statusData, "Y");
    }

    public void sendErpWorkOrderStatus(Long tenantId, List<ItfWoStatusSendErp> workOrderStatus, String type) {
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG, tenantId);
        if (CollectionUtils.isEmpty(lovValueDTOS)) {
            throw new CommonException(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG + "值集没有维护\n含义值为Y或N【Y为实时发送，N为不实时发送】");
        }
        String interfaceFlag = lovValueDTOS.get(0).getMeaning();
        if (YES.equals(type)) {
            if (!YES.equals(interfaceFlag)) {
                return;
            }
        }
        if (CollectionUtils.isEmpty(workOrderStatus)) {
            throw new CommonException("sendErpWorkOrderStatus(workOrderStatus)：不允许为空");
        }
        // 整理发送数据
        List<Map<String, Object>> sendList = new ArrayList<>();
        for (ItfWoStatusSendErp itfWoStatusSendErp : workOrderStatus) {
            Map<String, Object> map = new HashMap<>();
            map.put("DWERK", itfWoStatusSendErp.getPlantCode());
            map.put("AUFNR", itfWoStatusSendErp.getWorkOrderNum());
            map.put("TXT04", itfWoStatusSendErp.getWoStatus());
            // 若status为close则为X,否则为空
            if ("CLOS".equals(itfWoStatusSendErp.getWoStatus())) {
                map.put("ZJHBS", "X");
            } else {
                map.put("ZJHBS", null);
            }
            sendList.add(map);
        }
        Map<String, Object> returnData = sendESBConnect.sendEsb(sendList, "INPUT",
                "ItfWorkOrderReturnStatusHandler.erpWorkOrderStatusReturnRestProxy",
                ItfConstant.InterfaceCode.ESB_WO_STATUS_RETURN_SYNC);
        String outputStr = JSON.toJSON(returnData.get("OUTPUT")).toString();
        List<Map> output = JSONArray.parseArray(outputStr, Map.class);
        for (ItfWoStatusSendErp itfWoStatusSendErp : workOrderStatus) {
            for (Map map : output) {
                if (itfWoStatusSendErp.getPlantCode().equals(map.get("DWERK").toString())
                        && itfWoStatusSendErp.getWorkOrderNum().equals(map.get("AUFNR").toString())) {
                    ItfErpReturnWoStatusIface itfErpReturnWoStatusIface = new ItfErpReturnWoStatusIface();
                    itfErpReturnWoStatusIface.setTenantId(tenantId);
                    itfErpReturnWoStatusIface.setPlantCode(itfWoStatusSendErp.getPlantCode());
                    itfErpReturnWoStatusIface.setWorkOrderNum(itfWoStatusSendErp.getWorkOrderNum());
                    itfErpReturnWoStatusIface.setStarus(itfWoStatusSendErp.getWoStatus());
                    itfErpReturnWoStatusIface.setIsSuccess(map.get("FLAG").toString());
                    itfErpReturnWoStatusIface.setMessage(map.get("MESSAGE").toString());
                    itfErpReturnWoStatusIfaceMapper.insert(itfErpReturnWoStatusIface);
                }
            }
        }
    }

    /**
     * 工单降阶品结算比例接口
     *
     * @param tenantId
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com 2020/9/4 10:12
     */
    @Override
    public List<ErpReducedSettleRadioUpdateDTO> erpReducedSettleRadioUpdateRestProxy(Long tenantId, List<ErpReducedSettleRadioUpdateDTO> dto) throws Exception {
        try {
            Map<String, Object> returnData = sendESBConnect.sendEsb(dto, "ORDER",
                    "ItfWorkOrderIfaceServiceImpl.erpReducedSettleRadioUpdateRestProxy",
                    ItfConstant.InterfaceCode.ESB_REDUCED_SETTLE_RADIO_SYNC);
            String aReturn = JSON.toJSON(returnData.get("RETURN")).toString();
            List<ErpReducedSettleRadioUpdateDTO> erpReducedSettleRadioUpdateDTOS = JSONArray.parseArray(aReturn, ErpReducedSettleRadioUpdateDTO.class);
            for (ErpReducedSettleRadioUpdateDTO dto1 : erpReducedSettleRadioUpdateDTOS) {
                ItfErpReducedSettleRadioIface iface = new ItfErpReducedSettleRadioIface();
                iface.setIfaceId(customDbRepository.getNextKey("itf_erp_reduced_settle_radio_iface_s"));
                iface.setTenantId(tenantId);
                iface.setZflag(dto1.getZFLAG());
                iface.setZmessage(dto1.getZMESSAGE());
                iface.setAufnr(dto1.getAUFNR().replaceAll("^(0+)", ""));
                iface.setMatnr(dto1.getMATNR().replaceAll("^(0+)", ""));
                iface.setProzs(dto1.getPROZS());
                itfErpReducedSettleRadioIfaceRepository.insertSelective(iface);
            }
            return erpReducedSettleRadioUpdateDTOS;
        } catch (Exception e) {
            for (ErpReducedSettleRadioUpdateDTO dto1 : dto) {
                ItfErpReducedSettleRadioIface iface = new ItfErpReducedSettleRadioIface();
                iface.setIfaceId(customDbRepository.getNextKey("itf_erp_reduced_settle_radio_iface_s"));
                iface.setTenantId(tenantId);
                iface.setZflag("N");
                iface.setZmessage(e.getMessage());
                iface.setAufnr(dto1.getAUFNR().replaceAll("^(0+)", ""));
                iface.setMatnr(dto1.getMATNR().replaceAll("^(0+)", ""));
                iface.setProzs(dto1.getPROZS());
                itfErpReducedSettleRadioIfaceRepository.insertSelective(iface);
            }
            throw new Exception(e);
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<ItfWorkOrderReturnDTO> woBomRoImport(ItfWorkOrderSyncParamDTO paramDto, Double batchId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String batchDate = format.format(Utils.getNowDate());
        List<ItfWorkOrderSyncDTO> workOrderList = paramDto.getWorkOrderList();
        List<ItfBomComponentSyncDTO> bomComponentList = paramDto.getBomComponentList();
        List<ItfRoutingOperationSyncDTO> routingOperationList = paramDto.getRoutingOperationList();
        if (CollectionUtils.isEmpty(workOrderList)) {
            return new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(bomComponentList)) {
            return new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(routingOperationList)) {
            return new ArrayList<>();
        }
        List<ItfWorkOrderReturnDTO> returnList = new ArrayList<>();
        try {
            Date newDate = Utils.getNowDate();

            // 工单导入
            List<ItfWorkOrderIface> woIfaceList = workOrderImport(tenantId, batchId, workOrderList);
            // 筛选异常数据
            List<ItfWorkOrderIface> woErrIfaceList = woIfaceList.stream().filter(item ->
                    !"N".equals(item.getStatus())).collect(Collectors.toList());
            // 分批全量插入接口表
            if (CollectionUtils.isNotEmpty(woIfaceList)) {
                for (int i = 0; i < woIfaceList.size(); i++) {
                    woIfaceList.get(i).setBatchDate(batchDate);
                }
                List<List<ItfWorkOrderIface>> splitSqlList = InterfaceUtils.splitSqlList(woIfaceList, SQL_ITEM_COUNT_LIMIT);
                for (List<ItfWorkOrderIface> domains : splitSqlList) {
                    itfWorkOrderIfaceMapper.batchInsertWorkOrderIface("itf_work_order_iface", domains);
                }
            }
            List<ItfWorkOrderReturnDTO> errList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(woErrIfaceList)) {
                List<ItfWorkOrderReturnDTO> woReturnList = new ArrayList<>();
                woErrIfaceList.forEach(err -> {
                    ItfWorkOrderReturnDTO dto = new ItfWorkOrderReturnDTO();
                    dto.setWorkOrderNum(err.getWorkOrderNum());
                    dto.setPlantCode(err.getPlantCode());
                    dto.setProcessDate(newDate);
                    dto.setProcessStatus(err.getStatus());
                    dto.setProcessMessage(err.getMessage());
                    woReturnList.add(dto);
                });
                errList.addAll(woReturnList);
            }
            // BOM组件导入
            List<ItfBomComponentIface> bomIfaceList = bomComponentImport(tenantId, batchId, bomComponentList);
            // 筛选异常数据
            List<ItfBomComponentIface> bomErrIfaceList = bomIfaceList.stream().filter(item ->
                    !"N".equals(item.getStatus())).collect(Collectors.toList());
            // 分批全量插入接口表
            if (CollectionUtils.isNotEmpty(bomIfaceList)) {
                for (int i = 0; i < bomIfaceList.size(); i++) {
                    bomIfaceList.get(i).setBatchDate(batchDate);
                }
                List<List<ItfBomComponentIface>> splitSqlList = InterfaceUtils.splitSqlList(bomIfaceList, SQL_ITEM_COUNT_LIMIT);
                for (List<ItfBomComponentIface> domains : splitSqlList) {
                    itfBomComponentIfaceMapper.batchInsertBomIface("itf_bom_component_iface", domains);
                }
            }
            if (CollectionUtils.isNotEmpty(bomErrIfaceList)) {
                // 按工厂 + bom编号（与工单号相同）分组
                List<ItfBomComponentIface> bomErrIfaceGroupList = bomErrIfaceList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(o -> o.getPlantCode() + ";" + o.getBomCode()))), ArrayList::new));
                List<ItfWorkOrderReturnDTO> bomReturnList = new ArrayList<>();
                bomErrIfaceGroupList.forEach(err -> {
                    ItfWorkOrderReturnDTO dto = new ItfWorkOrderReturnDTO();
                    dto.setWorkOrderNum(err.getBomCode());
                    dto.setPlantCode(err.getPlantCode());
                    dto.setProcessDate(newDate);
                    dto.setProcessStatus("E");
                    dto.setProcessMessage("工单对应组件导入失败");
                    bomReturnList.add(dto);
                });
                errList.addAll(bomReturnList);
            }
            // 工艺路线导入
            List<ItfRoutingOperationIface> operaIfaceList = RoutingOperationImport(tenantId, batchId, routingOperationList);
            // 筛选异常数据
            List<ItfRoutingOperationIface> operaErrIfaceList = operaIfaceList.stream().filter(item ->
                    !"N".equals(item.getStatus())).collect(Collectors.toList());
            // 分批全量插入接口表
            if (CollectionUtils.isNotEmpty(operaIfaceList)) {
                for (int i = 0; i < operaIfaceList.size(); i++) {
                    operaIfaceList.get(i).setBatchDate(batchDate);
                }
                List<List<ItfRoutingOperationIface>> splitSqlList = InterfaceUtils.splitSqlList(operaIfaceList, SQL_ITEM_COUNT_LIMIT);
                for (List<ItfRoutingOperationIface> domains : splitSqlList) {
                    itfRoutingOperationIfaceMapper.batchInsertRoutingOperaIface("itf_routing_operation_iface", domains);
                }
            }
            if (CollectionUtils.isNotEmpty(operaErrIfaceList)) {
                // 按工厂 + 工艺路线编码（与工单号相同）分组
                List<ItfRoutingOperationIface> operaErrIfaceGroupList = operaErrIfaceList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(o -> o.getPlantCode() + ";" + o.getRouterCode()))), ArrayList::new));
                List<ItfWorkOrderReturnDTO> operaReturnList = new ArrayList<>();
                operaErrIfaceGroupList.forEach(err -> {
                    ItfWorkOrderReturnDTO dto = new ItfWorkOrderReturnDTO();
                    dto.setWorkOrderNum(err.getRouterCode());
                    dto.setPlantCode(err.getPlantCode());
                    dto.setProcessDate(newDate);
                    dto.setProcessStatus("E");
                    dto.setProcessMessage("工单对应工艺路线导入失败");
                    operaReturnList.add(dto);
                });
                errList.addAll(operaReturnList);
            }
            // 创建关系
            List<ItfOperationComponentIface> operaCompIfaceList = routingComponentImport(tenantId, batchId, operaIfaceList, bomIfaceList);
            // 筛选异常数据
            List<ItfOperationComponentIface> operaCompErrIfaceList = operaCompIfaceList.stream().filter(item ->
                    !"N".equals(item.getStatus())).collect(Collectors.toList());
            // 分批全量插入接口表
            if (CollectionUtils.isNotEmpty(operaCompIfaceList)) {
                for (int i = 0; i < operaCompIfaceList.size(); i++) {
                    operaCompIfaceList.get(i).setBatchDate(batchDate);
                }
                List<List<ItfOperationComponentIface>> splitSqlList = InterfaceUtils.splitSqlList(operaCompIfaceList, SQL_ITEM_COUNT_LIMIT);
                for (List<ItfOperationComponentIface> domains : splitSqlList) {
                    itfOperationComponentIfaceMapper.batchInsertOperaCompIface("itf_operation_component_iface", domains);
                }
            }
            if (CollectionUtils.isNotEmpty(operaCompErrIfaceList)) {
                // 按工厂 + BOM编码（与工单号相同）分组
                List<ItfOperationComponentIface> operaErrIfaceGroupList = operaCompErrIfaceList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(o -> o.getPlantCode() + ";" + o.getBomCode()))), ArrayList::new));
                List<ItfWorkOrderReturnDTO> operaCompReturnList = new ArrayList<>();
                operaErrIfaceGroupList.forEach(err -> {
                    ItfWorkOrderReturnDTO dto = new ItfWorkOrderReturnDTO();
                    dto.setWorkOrderNum(err.getBomCode());
                    dto.setPlantCode(err.getPlantCode());
                    dto.setProcessDate(newDate);
                    dto.setProcessStatus("E");
                    dto.setProcessMessage("工艺组件关系导入失败");
                    operaCompReturnList.add(dto);
                });
                errList.addAll(operaCompReturnList);
            }
            returnList = errList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(o -> o.getPlantCode() + ";" + o.getWorkOrderNum()))), ArrayList::new));
            List<String> errAllList = returnList.stream().map(o -> {
                return o.getPlantCode() + ";" + o.getWorkOrderNum();
            }).collect(Collectors.toList());
            // 筛选可导入数据（工单，组件，工艺路线，组件关系数据校验时，任一数据校验不通过，所对应的工单与其相关数据都无法导 入）
            List<ItfWorkOrderIface> woSucIfaceList = woIfaceList.stream().filter(item ->
                    !errAllList.contains(item.getPlantCode() + ";" + item.getWorkOrderNum())).collect(Collectors.toList());
            List<ItfBomComponentIface> bomSucIfaceList = bomIfaceList.stream().filter(item ->
                    !errAllList.contains(item.getPlantCode() + ";" + item.getBomCode())).collect(Collectors.toList());
            List<ItfRoutingOperationIface> operaSucIfaceList = operaIfaceList.stream().filter(item ->
                    !errAllList.contains(item.getPlantCode() + ";" + item.getRouterCode())).collect(Collectors.toList());
            List<ItfOperationComponentIface> operaCompSucIfaceList = operaCompIfaceList.stream().filter(item ->
                    !errAllList.contains(item.getPlantCode() + ";" + item.getBomCode())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(woSucIfaceList) || CollectionUtils.isEmpty(bomSucIfaceList)
                    || CollectionUtils.isEmpty(operaSucIfaceList) || CollectionUtils.isEmpty(operaCompSucIfaceList)) {
                return returnList;
            }
            // 工单
            List<List<ItfWorkOrderIface>> woSplitSqlList = InterfaceUtils.splitSqlList(woSucIfaceList, SQL_ITEM_COUNT_LIMIT);
            for (List<ItfWorkOrderIface> domains : woSplitSqlList) {
                itfWorkOrderIfaceMapper.batchInsertWorkOrder("mt_work_order_iface", domains);
            }
            // bom
            List<List<ItfBomComponentIface>> bomSplitSqlList = InterfaceUtils.splitSqlList(bomSucIfaceList, SQL_ITEM_COUNT_LIMIT);
            for (List<ItfBomComponentIface> domains : bomSplitSqlList) {
                itfBomComponentIfaceMapper.batchInsertBom("mt_bom_component_iface", domains);
            }
            // 工艺路线
            List<List<ItfRoutingOperationIface>> routingSplitSqlList = InterfaceUtils.splitSqlList(operaSucIfaceList, SQL_ITEM_COUNT_LIMIT);
            for (List<ItfRoutingOperationIface> domains : routingSplitSqlList) {
                itfRoutingOperationIfaceMapper.batchInsertRoutingOpera("mt_routing_operation_iface", domains);
            }
            // 组件关系
            List<List<ItfOperationComponentIface>> opComSplitSqlList = InterfaceUtils.splitSqlList(operaCompSucIfaceList, SQL_ITEM_COUNT_LIMIT);
            for (List<ItfOperationComponentIface> domains : opComSplitSqlList) {
                itfOperationComponentIfaceMapper.batchInsertOperaComp("mt_operation_component_iface", domains);
            }
        } catch (Exception e) {
            throw new MtException("ITF_INV_ITEM_0005",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "ITF_INV_ITEM_0005",
                            "ITF", e.getMessage()));
        }
        return returnList;
    }

    private List<ItfWorkOrderIface> workOrderImport(Long tenantId, Double batchId, List<ItfWorkOrderSyncDTO> workOrderList) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = Utils.getNowDate();
        List<ItfWorkOrderIface> woIfaceList = new ArrayList<>();
        BeanCopier copier = BeanCopier.create(ItfWorkOrderSyncDTO.class, ItfWorkOrderIface.class, false);
        List<String> workOrderNums = new ArrayList<>();
        for (ItfWorkOrderSyncDTO dto : workOrderList) {
            String status = "N";
            String message = "";
            String plantCode = dto.getPlantCode();
            String itemCode = dto.getItemCode();
            String workOrderNum = dto.getWorkOrderNum();
            Double qty = dto.getQuantity();
            String workOrderType = dto.getWorkOrderType();
            String scheduleStartDateStr = dto.getScheduleStartDateStr();
            String scheduleEndDateStr = dto.getScheduleEndDateStr();
            Date scheduleStartDate = null;
            Date scheduleEndDate = null;
            // 校验接口表必输字段不能为空
            if (StringUtils.isEmpty(plantCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "plantCode");
            }
            if (StringUtils.isEmpty(workOrderNum)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "workOrderNum");
            } else {
                workOrderNum = workOrderNum.replaceAll("^(0+)", "");
            }
            if (StringUtils.isEmpty(itemCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "itemCode");
            } else {
                itemCode = itemCode.replaceAll("^(0+)", "");
            }
            if (qty == null || qty.doubleValue() == 0) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "quantity");
            }
            if (StringUtils.isEmpty(workOrderType)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "workOrderType");
            }
            if (StringUtils.isEmpty(scheduleStartDateStr)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "scheduleStartDate");
            } else {
                try {
                    scheduleStartDate = format.parse(scheduleStartDateStr);
                } catch (ParseException e) {
                    status = "E";
                    message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_INV_ITEM_0002", "ITF", "scheduleStartDate");
                }
            }
            if (StringUtils.isEmpty(scheduleEndDateStr)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "scheduleEndDate");
            } else {
                try {
                    scheduleEndDate = format.parse(scheduleEndDateStr);
                } catch (ParseException e) {
                    status = "E";
                    message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_INV_ITEM_0002", "ITF", "scheduleEndDate");
                }
            }

            if (StringUtils.isEmpty(dto.getCompleteLocator())) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "completeLocator");
            }
            String workOrderStatus = dto.getWorkOrderStatus();
            if (StringUtils.isEmpty(workOrderStatus)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "workOrderStatus");
            }
            // 工单状态处理
            String woStatus = itfWorkOrderIfaceMapper.selectWoStatus(tenantId, plantCode, workOrderNum);
            if (StringUtils.isNotEmpty(woStatus)) {
                if (ItfConstant.InstructionStatus.NEW.equals(woStatus) || ItfConstant.InstructionStatus.RELEASED.equals(woStatus)) {
                    if (workOrderStatus.contains("CRTD")) {
                        workOrderStatus = ItfConstant.InstructionStatus.NEW;
                    } else if (workOrderStatus.contains("REL")) {
                        workOrderStatus = ItfConstant.InstructionStatus.RELEASED;
                    } else {
                        workOrderStatus = ItfConstant.InstructionStatus.NEW;
                    }
                    workOrderNums.add(workOrderNum);
                } else {
                    status = "E";
                    message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_INV_ITEM_0006", "ITF", workOrderNum, woStatus);
                }
            } else {
                if (workOrderStatus.contains("CRTD")) {
                    workOrderStatus = ItfConstant.InstructionStatus.NEW;
                } else if (workOrderStatus.contains("REL")) {
                    workOrderStatus = ItfConstant.InstructionStatus.RELEASED;
                } else {
                    workOrderStatus = ItfConstant.InstructionStatus.NEW;
                }
            }
            // 获取默认产线
            String workshop = lovAdapter.queryLovMeaning("WMS.WORKSHOP_MAPPING_REL", tenantId, dto.getAttribute5());
            String prodLineCode = getDefaultProdLine(tenantId, itemCode, plantCode, workshop);

            ItfWorkOrderIface itfWorkOrderIface = new ItfWorkOrderIface();
            copier.copy(dto, itfWorkOrderIface, null);
            itfWorkOrderIface.setTenantId(tenantId);
            itfWorkOrderIface.setPlantCode(plantCode);
            itfWorkOrderIface.setItemCode(itemCode);
            itfWorkOrderIface.setWorkOrderNum(workOrderNum);
            itfWorkOrderIface.setQuantity(Double.valueOf(qty));
            itfWorkOrderIface.setWorkOrderType(workOrderType);
            itfWorkOrderIface.setWorkOrderStatus(workOrderStatus);
            itfWorkOrderIface.setScheduleStartDate(scheduleStartDate);
            itfWorkOrderIface.setScheduleEndDate(scheduleEndDate);
            itfWorkOrderIface.setProdLineCode(prodLineCode);
            itfWorkOrderIface.setErpCreationDate(Utils.getNowDate());
            itfWorkOrderIface.setErpCreatedBy(-1L);
            itfWorkOrderIface.setErpLastUpdateDate(nowDate);
            itfWorkOrderIface.setErpLastUpdatedBy(-1L);
            itfWorkOrderIface.setStatus(status);
            itfWorkOrderIface.setMessage(message);
            itfWorkOrderIface.setBatchId(batchId);
            // 车间
            itfWorkOrderIface.setAttribute4(workshop);
            String ifaceId = this.customDbRepository.getNextKey("mt_work_order_iface_s");
            Long cidId = Long.valueOf(this.customDbRepository.getNextKey("mt_work_order_iface_cid_s"));
            itfWorkOrderIface.setIfaceId(ifaceId);
            itfWorkOrderIface.setCid(cidId);
            itfWorkOrderIface.setObjectVersionNumber(1L);
            itfWorkOrderIface.setCreatedBy(-1L);
            itfWorkOrderIface.setCreationDate(nowDate);
            itfWorkOrderIface.setLastUpdatedBy(-1L);
            itfWorkOrderIface.setLastUpdateDate(nowDate);
            woIfaceList.add(itfWorkOrderIface);
        }
        // 根据工单号删除bom关系
        deleteBomComponent(workOrderNums);
        return woIfaceList;
    }

    private String getDefaultProdLine(Long tenantId, String itemCode, String plantCode, String workshop) {
        MtMaterial mtMaterial = mtMaterialRepository.selectOne(new MtMaterial() {{
            setMaterialCode(itemCode);
            setTenantId(tenantId);
        }});
        String prodLineCode;
        List<String> prodLineCodeList = new ArrayList<>();
        // 先查询部门默认产线
        MtModProductionLine defaultProdLine = null;
        if (StringUtils.isNotBlank(workshop)) {
            defaultProdLine = itfWorkOrderIfaceMapper.selectDefaultProdLine(tenantId, plantCode, workshop);
        }
        if (Objects.nonNull(defaultProdLine)) {
            prodLineCode = defaultProdLine.getProdLineCode();
        } else {
            // 若未找到默认产线，则查询物料所在产线
            if (!ObjectUtils.isEmpty(mtMaterial)) {
                prodLineCodeList = itfWorkOrderIfaceMapper.selectProdLineCode(tenantId, mtMaterial.getMaterialId(), plantCode);
            }
            if (prodLineCodeList.size() == 1) {
                prodLineCode = prodLineCodeList.get(0);
            } else {
                prodLineCode = "-1";
            }
        }

        return prodLineCode;
    }

    private List<ItfBomComponentIface> bomComponentImport(Long tenantId, Double batchId, List<ItfBomComponentSyncDTO> bomComponentList) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = Utils.getNowDate();
        BeanCopier copier = BeanCopier.create(ItfBomComponentSyncDTO.class, ItfBomComponentIface.class, false);
        List<ItfBomComponentIface> bomIfaceList = new ArrayList<>();
        for (ItfBomComponentSyncDTO dto : bomComponentList) {
            String status = "N";
            String message = "";
            String bomCode = dto.getBomCode();
            String plantCode = dto.getPlantCode();
            Long componentLineNum = dto.getComponentLineNum();
            String itemCode = dto.getComponentItemCode();
            String operationSequence = dto.getOperationSequence();
            String bomStartDateStr = dto.getBomStartDateStr();
            Double bomUsage = dto.getBomUsage();
            String dumps = dto.getLineAttribute1();
            String issueLocatorCode = dto.getIssueLocatorCode();
            Date bomStartDate = null;
            // 校验接口表必输字段不能为空
            if (StringUtils.isEmpty(plantCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "plantCode");
            }
            if (StringUtils.isEmpty(bomCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "bomCode");
            } else {
                bomCode = bomCode.replaceAll("^(0+)", "");
            }
            if (StringUtils.isEmpty(operationSequence)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "operationSequence");
            }
            if (StringUtils.isEmpty(bomStartDateStr)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "bomStartDate");
            } else {
                try {
                    bomStartDate = format.parse(bomStartDateStr);
                } catch (ParseException e) {
                    status = "E";
                    message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_INV_ITEM_0002", "ITF", "bomStartDate");
                }
            }
            if (componentLineNum == null || componentLineNum.longValue() == 0) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "componentLineNum");
            }
            if (StringUtils.isEmpty(itemCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "itemCode");
            } else {
                itemCode = itemCode.replaceAll("^(0+)", "");
            }
            if (bomUsage == null) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "bomUsage");
            }
            // LineAttribute1 不为"X" ，必输校验
            if (!"X".equals(dumps)) {
                if (StringUtils.isEmpty(issueLocatorCode)) {
                    status = "E";
                    message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_INV_ITEM_0001", "ITF", "issueLocatorCode");
                }
            }
            ItfBomComponentIface itfBomComponentIface = new ItfBomComponentIface();
            copier.copy(dto, itfBomComponentIface, null);
            itfBomComponentIface.setTenantId(tenantId);
            itfBomComponentIface.setPlantCode(plantCode);
            itfBomComponentIface.setBomCode(bomCode);
            itfBomComponentIface.setBomDescription(bomCode);
            itfBomComponentIface.setBomStartDate(bomStartDate);
            itfBomComponentIface.setBomObjectType("WO");
            itfBomComponentIface.setBomObjectCode(bomCode);
            itfBomComponentIface.setStandardQty(1D);
            itfBomComponentIface.setComponentItemCode(itemCode);
            itfBomComponentIface.setBomUsage(bomUsage);
            itfBomComponentIface.setComponentStartDate(bomStartDate);
            itfBomComponentIface.setEnableFlag("X".equals(dto.getEnableFlag()) ? "N" : "Y");
            itfBomComponentIface.setErpCreationDate(nowDate);
            itfBomComponentIface.setErpCreatedBy(-1L);
            itfBomComponentIface.setErpLastUpdateDate(nowDate);
            itfBomComponentIface.setErpLastUpdatedBy(-1L);
            itfBomComponentIface.setAssembleMethod("X".equals(dto.getAssembleMethod()) ? "BACKFLASH" : "ISSUE");
            itfBomComponentIface.setBomComponentType("X".equals(dto.getBomComponentType()) ? "PHANTOM" : "ASSEMBLING");
            itfBomComponentIface.setUpdateMethod("UPDATE");
            itfBomComponentIface.setStatus(status);
            itfBomComponentIface.setMessage(message);
            itfBomComponentIface.setBatchId(batchId);
            // 追溯需求
            String baugr = StringUtils.isEmpty(dto.getLineAttribute2()) ? null : dto.getLineAttribute2().replaceAll("^(0+)", "");
            itfBomComponentIface.setLineAttribute2(baugr);
            String ifaceId = this.customDbRepository.getNextKey("mt_bom_component_iface_s");
            Long cidId = Long.valueOf(this.customDbRepository.getNextKey("mt_bom_component_iface_cid_s"));
            itfBomComponentIface.setIfaceId(ifaceId);
            itfBomComponentIface.setCid(cidId);
            itfBomComponentIface.setObjectVersionNumber(1L);
            itfBomComponentIface.setCreatedBy(-1L);
            itfBomComponentIface.setCreationDate(nowDate);
            itfBomComponentIface.setLastUpdatedBy(-1L);
            itfBomComponentIface.setLastUpdateDate(nowDate);
            // add 刘克金 ， 王康
            itfBomComponentIface.setLineAttribute22(dto.getLineAttribute22());
            itfBomComponentIface.setLineAttribute23(dto.getLineAttribute23());
            // add 刘克金，王康 2020年11月03日15:24:09
            itfBomComponentIface.setLineAttribute24(dto.getLineAttribute24());
            bomIfaceList.add(itfBomComponentIface);
        }
        return bomIfaceList;
    }

    private List<ItfRoutingOperationIface> RoutingOperationImport(Long tenantId, Double batchId,
                                                                  List<ItfRoutingOperationSyncDTO> routingOperationList) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = Utils.getNowDate();
        BeanCopier copier = BeanCopier.create(ItfRoutingOperationSyncDTO.class, ItfRoutingOperationIface.class, false);
        List<ItfRoutingOperationIface> operaIfaceList = new ArrayList<>();
        for (ItfRoutingOperationSyncDTO dto : routingOperationList) {
            String status = "N";
            String message = "";
            String plantCode = dto.getPlantCode();
            String routerCode = dto.getRouterCode();
            String operationSeqNum = dto.getOperationSeqNum();
            String routerStartDateStr = dto.getRouterStartDate();
            String operationCode = dto.getStandardOperationCode();
            Date routerStartDate = null;
            String operationDesc = null;
            // 校验接口表必输字段不能为空
            if (StringUtils.isEmpty(plantCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "plantCode");
            }
            if (StringUtils.isEmpty(routerCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "routerCode");
            } else {
                routerCode = routerCode.replaceAll("^(0+)", "");
            }
            if (StringUtils.isEmpty(operationSeqNum)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "operationSeqNum");
            }
            if (StringUtils.isEmpty(routerStartDateStr)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "routerStartDate");
            } else {
                try {
                    routerStartDate = format.parse(routerStartDateStr);
                } catch (ParseException e) {
                    status = "E";
                    message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_INV_ITEM_0002", "ITF", "routerStartDate");
                }
            }
            if (StringUtils.isEmpty(operationCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "standardOperationCode");
            } else {
                operationDesc = itfWorkOrderIfaceMapper.selectOperationDesc(tenantId, operationCode, plantCode);
            }
            ItfRoutingOperationIface itfRoutingOperationIface = new ItfRoutingOperationIface();
            copier.copy(dto, itfRoutingOperationIface, null);
            itfRoutingOperationIface.setTenantId(tenantId);
            itfRoutingOperationIface.setPlantCode(plantCode);
            itfRoutingOperationIface.setRouterObjectType("WO");
            itfRoutingOperationIface.setRouterObjectCode(routerCode);
            itfRoutingOperationIface.setRouterDescription(routerCode);
            itfRoutingOperationIface.setRouterCode(routerCode);
            itfRoutingOperationIface.setRouterStartDate(routerStartDate);
            itfRoutingOperationIface.setStandardOperationCode(operationCode);
            itfRoutingOperationIface.setOperationDescription(StringUtils.isEmpty(dto.getOperationDescription()) ? operationDesc : dto.getOperationDescription());
            itfRoutingOperationIface.setEnableFlag("X".equals(dto.getEnableFlag()) ? "N" : "Y");
            itfRoutingOperationIface.setErpCreationDate(nowDate);
            itfRoutingOperationIface.setErpCreatedBy(-1L);
            itfRoutingOperationIface.setErpLastUpdateDate(nowDate);
            itfRoutingOperationIface.setErpLastUpdatedBy(-1L);
            itfRoutingOperationIface.setStatus(status);
            itfRoutingOperationIface.setMessage(message);
            itfRoutingOperationIface.setBatchId(batchId);
            String ifaceId = this.customDbRepository.getNextKey("mt_routing_operation_iface_s");
            Long cidId = Long.valueOf(this.customDbRepository.getNextKey("mt_routing_operation_iface_cid_s"));
            itfRoutingOperationIface.setIfaceId(ifaceId);
            itfRoutingOperationIface.setCid(cidId);
            itfRoutingOperationIface.setObjectVersionNumber(1L);
            itfRoutingOperationIface.setCreatedBy(-1L);
            itfRoutingOperationIface.setCreationDate(nowDate);
            itfRoutingOperationIface.setLastUpdatedBy(-1L);
            itfRoutingOperationIface.setLastUpdateDate(nowDate);
            operaIfaceList.add(itfRoutingOperationIface);
        }
        return operaIfaceList;
    }

    private List<ItfOperationComponentIface> routingComponentImport(Long tenantId, Double batchId
            , List<ItfRoutingOperationIface> operaIfaceList, List<ItfBomComponentIface> bomIfaceList) {
        List<ItfOperationComponentIface> operaCompIfaceList = new ArrayList<>();
        Date nowDate = Utils.getNowDate();
        for (ItfBomComponentIface bomCompIface : bomIfaceList) {
            String status = "N";
            String message = "";
            ItfOperationComponentIface operaCompIface = new ItfOperationComponentIface();
            ItfRoutingOperationIface operationIface = new ItfRoutingOperationIface();
            List<ItfRoutingOperationIface> operaList = operaIfaceList.stream().filter(item -> item.getRouterCode().equals(bomCompIface.getBomCode()) &&
                    item.getOperationSeqNum().equals(bomCompIface.getOperationSequence())).collect(Collectors.toList());
            if (operaList.size() == 1) {
                operationIface = operaList.get(0);
            } else {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0003", "ITF", bomCompIface.getBomCode());
            }
            operaCompIface.setTenantId(tenantId);
            operaCompIface.setPlantCode(bomCompIface.getPlantCode());
            operaCompIface.setRouterObjectType("WO");
            operaCompIface.setRouterCode(operationIface.getRouterCode());
            operaCompIface.setBomObjectType(bomCompIface.getBomObjectType());
            operaCompIface.setBomCode(bomCompIface.getBomCode());
            operaCompIface.setLineNum(bomCompIface.getComponentLineNum());
            operaCompIface.setOperationSeqNum(bomCompIface.getOperationSequence());
            operaCompIface.setComponentItemNum(bomCompIface.getComponentItemCode());
            operaCompIface.setComponentStartDate(bomCompIface.getComponentStartDate());
            operaCompIface.setErpCreationDate(bomCompIface.getErpCreationDate());
            operaCompIface.setErpCreatedBy(-1L);
            operaCompIface.setErpLastUpdateDate(bomCompIface.getErpLastUpdateDate());
            operaCompIface.setErpLastUpdatedBy(-1L);
            operaCompIface.setUpdateMethod("UPDATE");
            operaCompIface.setStatus(status);
            operaCompIface.setMessage(message);
            operaCompIface.setBatchId(batchId);
            String ifaceId = this.customDbRepository.getNextKey("mt_operation_component_iface_s");
            Long cidId = Long.valueOf(this.customDbRepository.getNextKey("mt_operation_component_iface_cid_s"));
            operaCompIface.setIfaceId(ifaceId);
            operaCompIface.setCid(cidId);
            operaCompIface.setObjectVersionNumber(1L);
            operaCompIface.setCreatedBy(-1L);
            operaCompIface.setCreationDate(nowDate);
            operaCompIface.setLastUpdatedBy(-1L);
            operaCompIface.setLastUpdateDate(nowDate);
            operaCompIfaceList.add(operaCompIface);
        }
        return operaCompIfaceList;
    }


    private List<ItfRepairWoSnRelIface> workOrderSnRelImport(List<ItfWorkOrderSnRelSyncDTO> serial) {
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue(ItfConstant.LovCode.HME_REPAIR_WO_TYPE, tenantId);
        List<ItfRepairWoSnRelIface> itfRepairWoSnRelIfaces = new ArrayList<>();
        Long batchId = Long.valueOf(customDbRepository.getNextKey("hme_repair_wo_sn_rel_cid_s"));
        String[] fields = {"AUFNR", "AUART", "SERNR"};
        GetDeclaredFields<ItfWorkOrderSnRelSyncDTO> declaredField = new GetDeclaredFields();
        for (ItfWorkOrderSnRelSyncDTO dto : serial) {
            String kid = customDbRepository.getNextKey("hme_repair_wo_sn_rel_s");
            ItfRepairWoSnRelIface iface = new ItfRepairWoSnRelIface();
            iface.setTenantId(tenantId);
            iface.setIfaceId(kid);
            iface.setWorkOrderNum(dto.getAUFNR().replaceAll("^(0+)", ""));
            iface.setWorkOrderType(dto.getAUART());
            iface.setSnNum(dto.getSERNR());
            iface.setSttxt(dto.getSTTXT());
            iface.setCid(batchId);
            iface.setBatchId(batchId);
            List<String> declaredFields = declaredField.getDeclaredFields(dto, fields);
            if (CollectionUtils.isNotEmpty(declaredFields)) {
                iface.setMessage(declaredFields.toString() + "不可为空！");
                iface.setStatus("N");
                itfRepairWoSnRelIfaceRepository.insertSelective(iface);
                continue;
            }
            if (CollectionUtils.isEmpty(lovValueDTOS)) {
                iface.setMessage(ItfConstant.LovCode.HME_REPAIR_WO_TYPE + ":值集没有维护数据!");
                iface.setStatus("N");
                itfRepairWoSnRelIfaceRepository.insertSelective(iface);
                continue;
            }
            // 判断工单类型
            boolean isType = false;
            for (LovValueDTO lovValueDTO : lovValueDTOS) {
                if (iface.getWorkOrderType().equals(lovValueDTO.getValue())) {
                    isType = true;
                    break;
                }
            }
            if (!isType) {
                iface.setMessage(iface.getWorkOrderType() + ":工单类型不正确!");
                iface.setStatus("N");
                itfRepairWoSnRelIfaceRepository.insertSelective(iface);
                continue;
            }
            // 记录业务表
            HmeRepairWoSnRel hmeRepairWoSnRel = new HmeRepairWoSnRel();
            hmeRepairWoSnRel.setTenantId(tenantId);
            hmeRepairWoSnRel.setWorkOrderNum(iface.getWorkOrderNum());
            hmeRepairWoSnRel.setSnNum(iface.getSnNum());
            List<HmeRepairWoSnRel> hmeRepairWoSnRels = hmeRepairWoSnRelRepository.select(hmeRepairWoSnRel);
            BeanUtils.copyProperties(iface, hmeRepairWoSnRel);
            if (CollectionUtils.isEmpty(hmeRepairWoSnRels)) {
                hmeRepairWoSnRel.setRelId(kid);
                hmeRepairWoSnRelRepository.insertSelective(hmeRepairWoSnRel);
            } else {
                hmeRepairWoSnRel.setObjectVersionNumber(hmeRepairWoSnRels.get(0).getObjectVersionNumber());
                hmeRepairWoSnRel.setRelId(hmeRepairWoSnRels.get(0).getRelId());
                hmeRepairWoSnRelRepository.updateByPrimaryKeySelective(hmeRepairWoSnRel);
            }

            iface.setMessage("成功.");
            iface.setStatus("Y");
            itfRepairWoSnRelIfaceRepository.insertSelective(iface);
            itfRepairWoSnRelIfaces.add(iface);
        }
        return itfRepairWoSnRelIfaces;
    }

}
