package com.ruike.itf.app.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ruike.hme.domain.entity.HmeCosNcRecord;
import com.ruike.itf.api.dto.ItfRoutingOperationComSyncDTO;
import com.ruike.itf.api.dto.ItfRoutingOperationOprSyncDTO;
import com.ruike.itf.app.service.ItfRoutingOperationIfaceService;
import com.ruike.itf.domain.entity.ItfOperationComponentIface;
import com.ruike.itf.domain.entity.ItfRoutingOperationIface;
import com.ruike.itf.domain.repository.ItfOperationComponentIfaceRepository;
import com.ruike.itf.domain.repository.ItfRoutingOperationIfaceRepository;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfOperationComponentIfaceMapper;
import com.ruike.itf.infra.mapper.ItfRoutingOperationIfaceMapper;
import com.ruike.itf.infra.mapper.ItfWorkOrderIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.itf.utils.Utils;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.iface.domain.entity.MtOperationComponentIface;
import tarzan.iface.domain.entity.MtRoutingOperationIface;
import tarzan.iface.domain.repository.MtOperationComponentIfaceRepository;
import tarzan.iface.domain.repository.MtRoutingOperationIfaceRepository;
import tarzan.method.domain.entity.MtRouter;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 工艺路线接口表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@Slf4j
@Service
public class ItfRoutingOperationIfaceServiceImpl implements ItfRoutingOperationIfaceService {

    @Autowired
    private ItfRoutingOperationIfaceMapper itfRoutingOperationIfaceMapper;

    @Autowired
    private ItfRoutingOperationIfaceRepository itfRoutingOperationIfaceRepository;

    @Autowired
    private ItfOperationComponentIfaceRepository itfOperationComponentIfaceRepository;

    @Autowired
    private ItfOperationComponentIfaceMapper itfOperationComponentIfaceMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    MtRoutingOperationIfaceRepository mtRoutingOperationIfaceRepository;

    @Autowired
    private MtOperationComponentIfaceRepository mtOperationComponentIfaceRepository;

    @Autowired
    private ItfWorkOrderIfaceMapper itfWorkOrderIfaceMapper;

    @Autowired
    private WmsSiteRepository wmsSiteRepository;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    /**
     * 处理工艺路线接口数据
     *
     * @param dataMap
     * @return
     * @author kejin.liu01@hand-china.com 2020/8/17 12:22
     */
    @Override
    public Map<String, Object> invoke(Map dataMap) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String batchDate = format.format(new Date());
        String batchIdS = this.customDbRepository.getNextKey("mt_routing_operation_iface_batch_id_s");
        int index = batchIdS.lastIndexOf(".1");
        if(index > 0){
            batchIdS = batchIdS.substring(0 ,index);
        }
        Double batchId = Double.valueOf(batchIdS);
        // 工艺路线数据
        List<ItfRoutingOperationOprSyncDTO> oprSyncDTOList = JSONArray.parseArray(dataMap.get("OPR").toString(), ItfRoutingOperationOprSyncDTO.class);

        // 插入记录表数据
        Long operationCidId = Long.valueOf(this.customDbRepository.getNextKey("mt_routing_operation_iface_cid_s"));
        long startDate = System.currentTimeMillis();
        List<ItfRoutingOperationIface> routingOperationIfaces = insertItfRoutingOperation(oprSyncDTOList, batchDate, batchId, operationCidId);
        log.info("=================================>ItfRoutingOperationIfaceServiceImpl-insertItfRoutingOperation总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        // 先删除同批次日期的数据

        // 工序组件数据
        String com = dataMap.get("COM").toString();
        List<ItfRoutingOperationComSyncDTO> comSyncDTOList = JSONArray.parseArray(com, ItfRoutingOperationComSyncDTO.class);
        log.info("<============comSyncDTOList==========>" + comSyncDTOList.size());
        // 先删除同批次日期的
        // 整理工序组件记录表数据
        Long operationComponentCidId = Long.valueOf(this.customDbRepository.getNextKey("mt_operation_component_iface_cid_s"));
        startDate = System.currentTimeMillis();
        List<ItfOperationComponentIface> componentIfaces = insertItfOperationComponent(comSyncDTOList, batchDate, batchId, operationComponentCidId);
        log.info("<============componentIfaces==========>" + componentIfaces.size());
        log.info("=================================>ItfRoutingOperationIfaceServiceImpl-insertItfOperationComponent总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");

        // 查找头数据，进行关联，然后插入数据
        for (int i = 0; i < routingOperationIfaces.size(); i++) {
            for (int j = 0; j < componentIfaces.size(); j++) {
                if (routingOperationIfaces.get(i).getRouterCode().equals(componentIfaces.get(j).getRouterCode())
                        && routingOperationIfaces.get(i).getRoutingAlternate().equals(componentIfaces.get(j).getRouterAlternate())
                        && routingOperationIfaces.get(i).getOperationSeqNum().equals(componentIfaces.get(j).getOperationSeqNum())) {
                    componentIfaces.get(j).setPlantCode(Strings.isBlank(routingOperationIfaces.get(i).getPlantCode()) ? null : routingOperationIfaces.get(i).getPlantCode());
                    componentIfaces.get(j).setComponentStartDate(Objects.isNull(routingOperationIfaces.get(i).getOperationStartDate()) ? null : routingOperationIfaces.get(i).getOperationStartDate());
                    componentIfaces.get(j).setComponentEndDate(Objects.isNull(routingOperationIfaces.get(i).getOperationEndDate()) ? null : routingOperationIfaces.get(i).getOperationEndDate());
                    componentIfaces.get(j).setErpCreationDate(Objects.isNull(routingOperationIfaces.get(i).getErpCreationDate()) ? null : routingOperationIfaces.get(i).getErpCreationDate());
                    componentIfaces.get(j).setErpLastUpdateDate(Objects.isNull(routingOperationIfaces.get(i).getErpLastUpdateDate()) ? null : routingOperationIfaces.get(i).getErpLastUpdateDate());
                }
            }
        }

        if(CollectionUtils.isNotEmpty(routingOperationIfaces)){

            //按照router_name + revision 分组
            Map<String, List<ItfRoutingOperationIface>> routingOperationIfaceMap = routingOperationIfaces.stream().collect(
                    Collectors.groupingBy(item -> item.getRouterCode() + "#" + item.getRoutingAlternate()));
            List<MtRouter> routers = new ArrayList<>();
            for (String key : routingOperationIfaceMap.keySet()) {
                List<ItfRoutingOperationIface> routingOperationIfaceList = routingOperationIfaceMap.get(key);
                MtRouter router = new MtRouter();
                router.setRouterName(routingOperationIfaceList.get(0).getRouterCode());
                router.setRevision(routingOperationIfaceList.get(0).getRoutingAlternate());
                routers.add(router);
            }

            //V20210717 modify by penglin.sui for tianyang.xie 先删除ROUTER相关数据

            // 查询 ROUTER_STEP_ID
            List<String> routerStepId = itfWorkOrderIfaceMapper.selectRouterStepId2(tenantId, routers);
            if (CollectionUtils.isNotEmpty(routerStepId)) {
                itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_DONE_STEP.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
                itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_LINK.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
                itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_NEXT_STEP.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
                itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_OPERATION.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
                itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_OPERATION_TL.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
                itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_OPERATION_COMPONENT.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
                itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_RETURN_STEP.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
                itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_STEP_GROUP.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
                itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_STEP_GROUP_STEP.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
                itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_SUBSTEP.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
                itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_SUBSTEP_COMPONENT.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
                itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_STEP_TL.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
                itfWorkOrderIfaceMapper.deleteBomRelTable(ItfConstant.deleteTableData.DELETE_ROUTER_STEP.replace("?", "('" + StringUtils.join(routerStepId, "','") + "')"));
            }
        }

        // 插入mt_routing_operation_iface业务表
        List<ItfRoutingOperationIface> roList = routingOperationIfaces.stream().filter(a -> Strings.isBlank(a.getMessage())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(roList)) {
//            roList.forEach(a -> {
//                MtRoutingOperationIface mtRoutingOperationIface = new MtRoutingOperationIface();
//                BeanUtils.copyProperties(a, mtRoutingOperationIface);
//                mtRoutingOperationIfaceRepository.insertSelective(mtRoutingOperationIface);
//            });
            //V20210707 modify by penglin.sui for tianyang.xie 修改为分批批量
            startDate = System.currentTimeMillis();
            List<List<ItfRoutingOperationIface>> splitSqlList = InterfaceUtils.splitSqlList(roList, 3000);
            for (List<ItfRoutingOperationIface> domains : splitSqlList) {
                itfRoutingOperationIfaceMapper.batchInsertRoutingOpera("mt_routing_operation_iface", domains);
            }
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-batchInsertRoutingOpera分批批次："+ splitSqlList.size());
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-batchInsertRoutingOpera总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            //itfRoutingOperationIfaceMapper.batchInsertRoutingOpera("mt_routing_operation_iface", roList);
        }
        // 插入mt_operation_component_iface 业务表
        log.info("=================================>operationComponentIsNull begin");
        List<ItfOperationComponentIface> componentIfaces1 = operationComponentIsNull(componentIfaces);
        log.info("<============componentIfaces1==========>" + componentIfaces1.size());
        log.info("=================================>operationComponentIsNull end");
        List<ItfOperationComponentIface> ocList = componentIfaces1.stream().filter(a -> Strings.isBlank(a.getMessage())).collect(Collectors.toList());
        List<ItfOperationComponentIface> mtOcList = new ArrayList<>();
        for (int i = 0; i < roList.size(); i++) {
            for (int j = 0; j < ocList.size(); j++) {
                if (roList.get(i).getRouterCode().equals(ocList.get(j).getRouterCode())
                        && roList.get(i).getRoutingAlternate().equals(ocList.get(j).getRouterAlternate())
                        && roList.get(i).getOperationSeqNum().equals(ocList.get(j).getOperationSeqNum())) {
                    mtOcList.add(ocList.get(j));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(mtOcList)) {
            log.info("=================================>come in mtOcList");
//            mtOcList.forEach(a -> {
//                MtOperationComponentIface mtOperationComponentIface = new MtOperationComponentIface();
//                BeanUtils.copyProperties(a, mtOperationComponentIface);
//                mtOperationComponentIfaceRepository.insertSelective(mtOperationComponentIface);
//            });
            //V20210707 modify by penglin.sui for tianyang.xie 修改为分批批量
            startDate = System.currentTimeMillis();
            List<List<ItfOperationComponentIface>> splitSqlList = InterfaceUtils.splitSqlList(mtOcList, 3000);
            for (List<ItfOperationComponentIface> domains : splitSqlList) {
                itfOperationComponentIfaceMapper.batchInsertOperaComp("mt_operation_component_iface", domains);
            }
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-batchInsertOperaComp分批批次："+ splitSqlList.size());
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-batchInsertOperaComp总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            //itfOperationComponentIfaceMapper.batchInsertOperaComp("mt_operation_component_iface", mtOcList);
        }
        List<ItfRoutingOperationIface> oprError = routingOperationIfaces.stream().filter(a -> Strings.isNotEmpty(a.getMessage())).collect(Collectors.toList());
        List<ItfOperationComponentIface> comError = componentIfaces1.stream().filter(a -> Strings.isNotBlank(a.getMessage())).collect(Collectors.toList());
        log.info("<===========comError.size===========>" + comError.size());
        // 调用API
        startDate = System.currentTimeMillis();
        log.info("=================================>myRouterInterfaceImport begin");
        mtRoutingOperationIfaceRepository.myRouterInterfaceImport(tenantId, Long.valueOf(batchIdS));
        log.info("=================================>ItfRoutingOperationIfaceServiceImpl-myRouterInterfaceImport总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        startDate = System.currentTimeMillis();
        log.info("=================================>myOperationComponentInterfaceImport begin");
        mtOperationComponentIfaceRepository.myOperationComponentInterfaceImport(tenantId, Long.valueOf(batchIdS));
        log.info("=================================>ItfRoutingOperationIfaceServiceImpl-myOperationComponentInterfaceImport总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        // 查询API处理的错误数据
        MtRoutingOperationIface mtRoutingOperationIface = new MtRoutingOperationIface();
        mtRoutingOperationIface.setBatchId(batchId);
        mtRoutingOperationIface.setTenantId(tenantId);
        startDate = System.currentTimeMillis();
        List<MtRoutingOperationIface> mtRoutingOperationIfaces = mtRoutingOperationIfaceRepository.select(mtRoutingOperationIface);
        log.info("=================================>ItfRoutingOperationIfaceServiceImpl-mtRoutingOperationIfaceRepository.select总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        MtOperationComponentIface mtOperationComponentIface = new MtOperationComponentIface();
        mtOperationComponentIface.setBatchId(batchId);
        mtOperationComponentIface.setTenantId(tenantId);
        startDate = System.currentTimeMillis();
        List<MtOperationComponentIface> mtOperationComponentIfaces = mtOperationComponentIfaceRepository.select(mtOperationComponentIface);
        log.info("=================================>ItfRoutingOperationIfaceServiceImpl-mtOperationComponentIfaceRepository.select总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        mtRoutingOperationIfaces.forEach(a -> {
            ItfRoutingOperationIface iface = new ItfRoutingOperationIface();
            BeanUtils.copyProperties(a, iface);
            oprError.add(iface);
        });
        log.info("<============mtOperationComponentIfaces==========>" + mtOperationComponentIfaces.size());
        mtOperationComponentIfaces.forEach(a -> {
            ItfOperationComponentIface iface = new ItfOperationComponentIface();
            BeanUtils.copyProperties(a, iface);
            log.info("<============add comError==========>");
            comError.add(iface);
        });
        // 插入接口记录表
        //V20210707 modify by penglin.sui for tianyang.xie 修改为分批批量
        if(CollectionUtils.isNotEmpty(oprError)) {
            startDate = System.currentTimeMillis();
            List<List<ItfRoutingOperationIface>> splitSqlList = InterfaceUtils.splitSqlList(oprError, 3000);
            for (List<ItfRoutingOperationIface> domains : splitSqlList) {
                itfRoutingOperationIfaceMapper.batchInsertRoutingOperaIface("itf_routing_operation_iface", domains);
            }
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-itfRoutingOperationIfaceMapper.batchInsertRoutingOperaIface分批批次：" + splitSqlList.size());
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-itfRoutingOperationIfaceMapper.batchInsertRoutingOperaIface总耗时：" + (System.currentTimeMillis() - startDate) + "毫秒");
        }
        //V20210707 modify by penglin.sui for tianyang.xie 修改为分批批量
        if(CollectionUtils.isNotEmpty(comError)) {
            startDate = System.currentTimeMillis();
            List<List<ItfOperationComponentIface>> comSplitSqlList = InterfaceUtils.splitSqlList(comError, 3000);
            log.info("<============comSplitSqlList.size==========>" + comSplitSqlList.size());
            for (List<ItfOperationComponentIface> domains : comSplitSqlList) {
                log.info("<============domains==========>" + domains.get(0).getIfaceId() + domains.get(0).getMessage());
                itfOperationComponentIfaceMapper.batchInsertOperaCompIface("itf_operation_component_iface", domains);
            }
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-itfRoutingOperationIfaceMapper.batchInsertRoutingOperaIface分批批次：" + comSplitSqlList.size());
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-itfRoutingOperationIfaceMapper.batchInsertRoutingOperaIface总耗时：" + (System.currentTimeMillis() - startDate) + "毫秒");
        }
        // 获取错误数据
        Map<String, Object> map = new HashMap<>();
        map.put("OPR", oprError);
        map.put("COM", comError);
        return map;
    }

    @Override
    public Map<String, Object> invoke2(Map dataMap) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String batchDate = format.format(new Date());
        String batchIdS = this.customDbRepository.getNextKey("mt_routing_operation_iface_batch_id_s");
        int index = batchIdS.lastIndexOf(".1");
        if(index > 0){
            batchIdS = batchIdS.substring(0 ,index);
        }
        Double batchId = Double.valueOf(batchIdS);
        // 工艺路线数据
        List<ItfRoutingOperationOprSyncDTO> oprSyncDTOList = JSONArray.parseArray(dataMap.get("OPR").toString(), ItfRoutingOperationOprSyncDTO.class);

        // 插入记录表数据
        Long operationCidId = Long.valueOf(this.customDbRepository.getNextKey("mt_routing_operation_iface_cid_s"));
        long startDate = System.currentTimeMillis();
        List<ItfRoutingOperationIface> routingOperationIfaces = insertItfRoutingOperation(oprSyncDTOList, batchDate, batchId, operationCidId);
        log.info("=================================>ItfRoutingOperationIfaceServiceImpl-insertItfRoutingOperation总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        // 先删除同批次日期的数据

        // 工序组件数据
        String com = dataMap.get("COM").toString();
        List<ItfRoutingOperationComSyncDTO> comSyncDTOList = JSONArray.parseArray(com, ItfRoutingOperationComSyncDTO.class);
        log.info("<============comSyncDTOList==========>" + comSyncDTOList.size());
        // 先删除同批次日期的
        // 整理工序组件记录表数据
        Long operationComponentCidId = Long.valueOf(this.customDbRepository.getNextKey("mt_operation_component_iface_cid_s"));
        startDate = System.currentTimeMillis();
        List<ItfOperationComponentIface> componentIfaces = insertItfOperationComponent(comSyncDTOList, batchDate, batchId, operationComponentCidId);
        log.info("<============componentIfaces==========>" + componentIfaces.size());
        log.info("=================================>ItfRoutingOperationIfaceServiceImpl-insertItfOperationComponent总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");

        // 查找头数据，进行关联，然后插入数据
        for (int i = 0; i < routingOperationIfaces.size(); i++) {
            for (int j = 0; j < componentIfaces.size(); j++) {
                if (routingOperationIfaces.get(i).getRouterCode().equals(componentIfaces.get(j).getRouterCode())
                        && routingOperationIfaces.get(i).getRoutingAlternate().equals(componentIfaces.get(j).getRouterAlternate())
                        && routingOperationIfaces.get(i).getOperationSeqNum().equals(componentIfaces.get(j).getOperationSeqNum())) {
                    componentIfaces.get(j).setPlantCode(Strings.isBlank(routingOperationIfaces.get(i).getPlantCode()) ? null : routingOperationIfaces.get(i).getPlantCode());
                    componentIfaces.get(j).setComponentStartDate(Objects.isNull(routingOperationIfaces.get(i).getOperationStartDate()) ? null : routingOperationIfaces.get(i).getOperationStartDate());
                    componentIfaces.get(j).setComponentEndDate(Objects.isNull(routingOperationIfaces.get(i).getOperationEndDate()) ? null : routingOperationIfaces.get(i).getOperationEndDate());
                    componentIfaces.get(j).setErpCreationDate(Objects.isNull(routingOperationIfaces.get(i).getErpCreationDate()) ? null : routingOperationIfaces.get(i).getErpCreationDate());
                    componentIfaces.get(j).setErpLastUpdateDate(Objects.isNull(routingOperationIfaces.get(i).getErpLastUpdateDate()) ? null : routingOperationIfaces.get(i).getErpLastUpdateDate());
                }
            }
        }

        // 插入mt_routing_operation_iface业务表
        List<ItfRoutingOperationIface> roList = routingOperationIfaces.stream().filter(a -> Strings.isBlank(a.getMessage())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(roList)) {
            //V20210707 modify by penglin.sui for tianyang.xie 修改为分批批量
            startDate = System.currentTimeMillis();
            List<List<ItfRoutingOperationIface>> splitSqlList = InterfaceUtils.splitSqlList(roList, 3000);
            for (List<ItfRoutingOperationIface> domains : splitSqlList) {
                itfRoutingOperationIfaceMapper.batchInsertRoutingOpera("mt_routing_operation_iface", domains);
            }
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-batchInsertRoutingOpera分批批次："+ splitSqlList.size());
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-batchInsertRoutingOpera总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }
        // 插入mt_operation_component_iface 业务表
        log.info("=================================>operationComponentIsNull begin");
        List<ItfOperationComponentIface> componentIfaces1 = operationComponentIsNull(componentIfaces);
        log.info("<============componentIfaces1==========>" + componentIfaces1.size());
        log.info("=================================>operationComponentIsNull end");
        List<ItfOperationComponentIface> ocList = componentIfaces1.stream().filter(a -> Strings.isBlank(a.getMessage())).collect(Collectors.toList());
        List<ItfOperationComponentIface> mtOcList = new ArrayList<>();
        for (int i = 0; i < roList.size(); i++) {
            for (int j = 0; j < ocList.size(); j++) {
                if (roList.get(i).getRouterCode().equals(ocList.get(j).getRouterCode())
                        && roList.get(i).getRoutingAlternate().equals(ocList.get(j).getRouterAlternate())
                        && roList.get(i).getOperationSeqNum().equals(ocList.get(j).getOperationSeqNum())) {
                    mtOcList.add(ocList.get(j));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(mtOcList)) {
            log.info("=================================>come in mtOcList");
            //V20210707 modify by penglin.sui for tianyang.xie 修改为分批批量
            startDate = System.currentTimeMillis();
            List<List<ItfOperationComponentIface>> splitSqlList = InterfaceUtils.splitSqlList(mtOcList, 3000);
            for (List<ItfOperationComponentIface> domains : splitSqlList) {
                itfOperationComponentIfaceMapper.batchInsertOperaComp("mt_operation_component_iface", domains);
            }
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-batchInsertOperaComp分批批次："+ splitSqlList.size());
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-batchInsertOperaComp总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            //itfOperationComponentIfaceMapper.batchInsertOperaComp("mt_operation_component_iface", mtOcList);
        }
        // 插入接口记录表
        //V20210707 modify by penglin.sui for tianyang.xie 修改为分批批量
        if(CollectionUtils.isNotEmpty(routingOperationIfaces)) {
            startDate = System.currentTimeMillis();
            List<List<ItfRoutingOperationIface>> splitSqlList = InterfaceUtils.splitSqlList(routingOperationIfaces, 3000);
            for (List<ItfRoutingOperationIface> domains : splitSqlList) {
                itfRoutingOperationIfaceMapper.batchInsertRoutingOperaIface("itf_routing_operation_iface", domains);
            }
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-itfRoutingOperationIfaceMapper.batchInsertRoutingOperaIface分批批次：" + splitSqlList.size());
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-itfRoutingOperationIfaceMapper.batchInsertRoutingOperaIface总耗时：" + (System.currentTimeMillis() - startDate) + "毫秒");
        }
        //V20210707 modify by penglin.sui for tianyang.xie 修改为分批批量
        if(CollectionUtils.isNotEmpty(componentIfaces)) {
            startDate = System.currentTimeMillis();
            List<List<ItfOperationComponentIface>> comSplitSqlList = InterfaceUtils.splitSqlList(componentIfaces, 3000);
            log.info("<============comSplitSqlList.size==========>" + comSplitSqlList.size());
            for (List<ItfOperationComponentIface> domains : comSplitSqlList) {
                log.info("<============domains==========>" + domains.get(0).getIfaceId() + domains.get(0).getMessage());
                itfOperationComponentIfaceMapper.batchInsertOperaCompIface("itf_operation_component_iface", domains);
            }
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-itfRoutingOperationIfaceMapper.batchInsertRoutingOperaIface分批批次：" + comSplitSqlList.size());
            log.info("=================================>ItfRoutingOperationIfaceServiceImpl-itfRoutingOperationIfaceMapper.batchInsertRoutingOperaIface总耗时：" + (System.currentTimeMillis() - startDate) + "毫秒");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("OPR", routingOperationIfaces);
        map.put("COM", componentIfaces);
        return map;
    }

    /**
     * @param componentIfaces
     * @return
     * @author kejin.liu01@hand-china.com 2020/8/17 15:36
     */
    public List<ItfOperationComponentIface> operationComponentIsNull(List<ItfOperationComponentIface> componentIfaces) {
        List<ItfOperationComponentIface> isnull = new ArrayList<>();
        for (ItfOperationComponentIface isNull : componentIfaces) {
            StringBuffer errMsg = new StringBuffer();
            if (Strings.isEmpty(isNull.getPlantCode())) {
                errMsg.append("PlantCode 工厂不允许为空！");
            }
            if (Objects.isNull(isNull.getLineNum())) {
                errMsg.append("LineNum BOM项目号不允许为空不允许为空！");
            }
            if (Strings.isEmpty(isNull.getOperationSeqNum())) {
                errMsg.append("OperationSeqNum 操作/活动编号不允许为空！");
            }
            if (Objects.isNull(isNull.getComponentStartDate())) {
                errMsg.append("ComponentStartDate ROUTER不允许为空！");
            }
            if (Objects.isNull(isNull.getErpCreationDate())) {
                errMsg.append("ErpCreationDate ERP创建时间不允许为空！");
            }
            if (Objects.isNull(isNull.getErpLastUpdateDate())) {
                errMsg.append("ErpLastUpdateDate ERP最后修改时间不允许为空！");
            }
            if (Strings.isNotEmpty(isNull.getMessage())) {
                errMsg.append(isNull.getMessage());
            }
            isNull.setMessage(errMsg.toString());
            isnull.add(isNull);
        }

        return isnull;
    }

    /**
     * 整理工序组件表数据
     *
     * @param comSyncDTOList
     * @param batchDate
     * @param batchId
     * @param cidId
     * @return
     * @author kejin.liu01@hand-china.com 2020/8/17 12:26
     */
    public List<ItfOperationComponentIface> insertItfOperationComponent(List<ItfRoutingOperationComSyncDTO> comSyncDTOList, String batchDate, Double batchId, Long cidId) {
        List<ItfOperationComponentIface> componentIfaces = new ArrayList<>();
        Utils utils = new Utils();
        try {

            List<String> operationComponentIfaceList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(comSyncDTOList)){
                operationComponentIfaceList = customDbRepository.getNextKeys("mt_operation_component_iface_s", comSyncDTOList.size());
            }
            int index = 0;
            for (ItfRoutingOperationComSyncDTO com : comSyncDTOList) {
                ItfOperationComponentIface iface = new ItfOperationComponentIface();
                iface.setTenantId(tenantId);
                iface.setBatchDate(batchDate);
                iface.setBatchId(batchId);
                iface.setCid(cidId);
                iface.setIfaceId(operationComponentIfaceList.get(index++));
                iface.setRouterObjectType("MATERIAL");
                iface.setRouterCode(Strings.isEmpty(com.getPLNNR()) ? "" : com.getPLNNR());
                iface.setRouterAlternate(Strings.isEmpty(com.getPLNAL()) ? "" : com.getPLNAL());
                iface.setBomObjectType("MATERIAL");
                iface.setBomCode(Strings.isEmpty(com.getSTLNR()) ? "" : com.getSTLNR().replaceAll("^(0+)", ""));
                iface.setBomAlternate(Strings.isEmpty(com.getSTLAL()) ? "" : com.getSTLAL());
                String lineNum = Strings.isEmpty(com.getPOSNR()) ? null : com.getPOSNR().replace(" ", "");
                String newLineNum = lineNum.replaceAll("^(0+)", "");
                if (utils.isNum(newLineNum)) {
                    iface.setLineNum(Long.valueOf(newLineNum));
                } else {
                    iface.setMessage("BOM项目号必须为数字类型！");
                }

                iface.setOperationSeqNum(Strings.isEmpty(com.getVORNR()) ? "" : com.getVORNR());
                iface.setComponentItemNum(Strings.isEmpty(com.getIDNRK()) ? "" : com.getIDNRK().replaceAll("^(0+)", ""));
                iface.setBomUsage(Strings.isEmpty(com.getMENGE()) ? null : Double.valueOf(com.getMENGE()));
                iface.setUpdateMethod("UPDATE");
                iface.setUom(Strings.isEmpty(com.getMEINS()) ? "" : com.getMEINS());
                iface.setErpCreatedBy(-1L);
                iface.setErpLastUpdatedBy(-1L);
                iface.setStatus("N");
                iface.setCreatedBy(-1L);
                iface.setCreationDate(new Date());
                iface.setLastUpdatedBy(-1L);
                iface.setLastUpdateDate(new Date());
                iface.setObjectVersionNumber(1L);
                componentIfaces.add(iface);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return componentIfaces;
    }

    /**
     * 插入工艺路线接口记录表
     *
     * @param oprSyncDTOList
     * @param batchDate
     * @param batchId
     * @param cidId
     * @return
     * @author kejin.liu01@hand-china.com 2020/8/17 12:22
     */
    public List<ItfRoutingOperationIface> insertItfRoutingOperation(List<ItfRoutingOperationOprSyncDTO> oprSyncDTOList, String batchDate, Double batchId, Long cidId) {
        List<ItfRoutingOperationIface> routingOperationIfaces = new ArrayList<>();
        try {

            List<String> routerOperationIfaceList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(oprSyncDTOList)){
                routerOperationIfaceList = customDbRepository.getNextKeys("mt_routing_operation_iface_s", oprSyncDTOList.size());
            }
            int index = 0;
            for (ItfRoutingOperationOprSyncDTO opr : oprSyncDTOList) {
                ItfRoutingOperationIface iface = new ItfRoutingOperationIface();
                iface.setTenantId(tenantId);
                iface.setBatchDate(batchDate);
                iface.setBatchId(batchId);
                iface.setCid(cidId);
                iface.setIfaceId(routerOperationIfaceList.get(index++));
                iface.setRouterObjectType("MATERIAL");
                iface.setUpdateMethod("UPDATE");
                iface.setRouterObjectCode(Strings.isEmpty(opr.getMATNR()) ? "" : opr.getMATNR().replaceAll("^(0+)", ""));
                iface.setPlantCode(Strings.isEmpty(opr.getWERKS()) ? "" : opr.getWERKS());
                iface.setRouterDescription(Strings.isEmpty(opr.getKTEXT()) ? "" : opr.getKTEXT());
                iface.setRouterCode(Strings.isEmpty(opr.getPLNNR()) ? "" : opr.getPLNNR());
                iface.setRouterStartDate(Objects.isNull(opr.getDATUV1()) ? null : opr.getDATUV1());
                iface.setRouterEndDate(Objects.isNull(opr.getDATUV1()) ? null : opr.getVALID_TO1());
                iface.setEnableFlag(Strings.isEmpty(opr.getLOEKZ()) ? "" : opr.getLOEKZ());
                iface.setRoutingAlternate(Strings.isEmpty(opr.getPLNAL()) ? "" : opr.getPLNAL());
                iface.setOperationSeqNum(Strings.isEmpty(opr.getVORNR()) ? "" : opr.getVORNR());
                iface.setStandardOperationCode(Strings.isEmpty(opr.getKTSCH()) ? "" : opr.getKTSCH());
                iface.setOperationDescription(Strings.isEmpty(opr.getLTXA1()) ? "" : opr.getLTXA1());
                iface.setOperationStartDate(Objects.isNull(opr.getDATUV()) ? null : opr.getDATUV());
                //iface.setOperationEndDate(Objects.isNull(opr.getVALID_TO()) ? null : opr.getVALID_TO());
                iface.setOperationEndDate(null);// 2020-08-25 修改字段为空 kejin.liu01
                iface.setErpCreationDate(Objects.isNull(opr.getANDAT()) ? null : opr.getANDAT());
                iface.setErpCreatedBy(-1L);
                if (Objects.isNull(opr.getAEDAT())) {
                    iface.setErpLastUpdateDate(opr.getANDAT());
                } else {
                    iface.setErpLastUpdateDate(opr.getAEDAT());
                }
                iface.setErpLastUpdatedBy(-1L);
                iface.setStatus("N");
                iface.setHeadAttribute1(Strings.isEmpty(opr.getLOEKZ()) ? "" : opr.getLOEKZ());
                iface.setHeadAttribute2(Strings.isEmpty(opr.getMATNR()) ? "" : opr.getMATNR().replaceAll("^(0+)", ""));
                iface.setCreatedBy(-1L);
                iface.setCreationDate(new Date());
                iface.setLastUpdatedBy(-1L);
                iface.setLastUpdateDate(new Date());
                iface.setObjectVersionNumber(1L);
                routingOperationIfaces.add(iface);
            }
            // 判断为空字段
            for (ItfRoutingOperationIface routingOperationIface : routingOperationIfaces) {
                StringBuilder errMsg = new StringBuilder();
                if (Strings.isEmpty(routingOperationIface.getRouterObjectCode())) {
                    errMsg.append("RouterObjectCode 物料不允许为空！");
                }
                if (Strings.isEmpty(routingOperationIface.getPlantCode())) {
                    errMsg.append("PlantCode 工厂不允许为空！");
                }
                if (Strings.isEmpty(routingOperationIface.getRouterDescription())) {
                    errMsg.append("RouterDescription 不允许为空！");
                }
                if (Strings.isEmpty(routingOperationIface.getRouterCode())) {
                    errMsg.append("RouterCode 编号不允许为空！");
                }
                if (routingOperationIface.getRouterStartDate() == null) {
                    errMsg.append("RouterStartDate 开始日期不允许为空！");
                }
                if (Strings.isEmpty(routingOperationIface.getStandardOperationCode())) {
                    errMsg.append("StandardOperationCode 标准文本码不允许为空！");
                }
                if (routingOperationIface.getErpCreationDate() == null) {
                    errMsg.append("ErpCreationDate 创建时间不允许为空！");
                }
                if (routingOperationIface.getErpLastUpdateDate() == null) {
                    errMsg.append("ErpLastUpdateDate 最后修改时间不允许为空！");
                }
                routingOperationIface.setMessage(errMsg.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routingOperationIfaces;
    }
}
