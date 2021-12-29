package com.ruike.itf.infra.repository.impl;

import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.domain.entity.ItfOperationComponentIface;
import com.ruike.itf.domain.entity.ItfRoutingOperationIface;
import com.ruike.itf.domain.repository.ItfRoutingOperationIfaceRepository;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfOperationComponentIfaceMapper;
import com.ruike.itf.infra.mapper.ItfRoutingOperationIfaceMapper;
import com.ruike.itf.infra.mapper.ItfWorkOrderIfaceMapper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.iface.domain.entity.MtOperationComponentIface;
import tarzan.iface.domain.entity.MtRoutingOperationIface;
import tarzan.iface.domain.repository.MtOperationComponentIfaceRepository;
import tarzan.iface.domain.repository.MtRoutingOperationIfaceRepository;
import tarzan.method.domain.entity.MtRouter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 工艺路线接口表 资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@Component
public class ItfRoutingOperationIfaceRepositoryImpl extends BaseRepositoryImpl<ItfRoutingOperationIface> implements ItfRoutingOperationIfaceRepository {

    @Autowired
    private ItfRoutingOperationIfaceMapper itfRoutingOperationIfaceMapper;
    @Autowired
    private ItfOperationComponentIfaceMapper itfOperationComponentIfaceMapper;
    @Autowired
    private ItfWorkOrderIfaceMapper itfWorkOrderIfaceMapper;
    @Autowired
    private MtRoutingOperationIfaceRepository mtRoutingOperationIfaceRepository;
    @Autowired
    private MtOperationComponentIfaceRepository mtOperationComponentIfaceRepository;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> selectBatchId(Long tenantId) {
        return itfRoutingOperationIfaceMapper.selectBatchId(tenantId);
    }

    @Override
    public void myRoutingOperationImport(Long tenantId, Long batchId) {
        // 工艺路线数据
        List<ItfRoutingOperationIface> routingOperationIfaces = itfRoutingOperationIfaceMapper.queryRoutingOperationIfaces(tenantId, batchId.doubleValue());
        // 工序组件数据
        List<ItfOperationComponentIface> componentIfaces = itfOperationComponentIfaceMapper.select(new ItfOperationComponentIface() {{
            setTenantId(tenantId);
            setBatchId(batchId.doubleValue());
        }});

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
        mtRoutingOperationIfaceRepository.myRouterInterfaceImport(tenantId, batchId);
        mtOperationComponentIfaceRepository.myOperationComponentInterfaceImport(tenantId, batchId);

        // 查询API处理的错误数据
        MtRoutingOperationIface mtRoutingOperationIface = new MtRoutingOperationIface();
        mtRoutingOperationIface.setBatchId(batchId.doubleValue());
        mtRoutingOperationIface.setTenantId(tenantId);
        List<MtRoutingOperationIface> mtRoutingOperationIfaces = mtRoutingOperationIfaceRepository.select(mtRoutingOperationIface);
        Map<String, MtRoutingOperationIface> mtRoutingOperationIfaceMap = mtRoutingOperationIfaces.stream().collect(Collectors.toMap(dto -> this.spliceStr(dto.getRouterObjectType(), dto.getRouterCode(), dto.getRoutingAlternate(), dto.getOperationSeqNum()), Function.identity()));

        MtOperationComponentIface mtOperationComponentIface = new MtOperationComponentIface();
        mtOperationComponentIface.setBatchId(batchId.doubleValue());
        mtOperationComponentIface.setTenantId(tenantId);
        List<MtOperationComponentIface> mtOperationComponentIfaces = mtOperationComponentIfaceRepository.select(mtOperationComponentIface);
        Map<String, MtOperationComponentIface> mtOperationComponentIfaceMap = mtOperationComponentIfaces.stream().collect(Collectors.toMap(dto -> this.componentSpliceStr(dto.getRouterObjectType(), dto.getRouterCode(), dto.getRouterAlternate(), dto.getOperationSeqNum(), dto.getBomObjectType(), dto.getBomCode(), dto.getBomAlternate(), dto.getLineNum()), Function.identity()));

        // 更新接口表的状态
        List<ItfRoutingOperationIface> updateRoutingOperationIfaces = new ArrayList<>();
        List<ItfOperationComponentIface> updateOperationComponentIfaces = new ArrayList<>();
        for (ItfRoutingOperationIface routingOperationIface : routingOperationIfaces) {
            String routingOperationIfaceKey = this.spliceStr(routingOperationIface.getRouterObjectType(), routingOperationIface.getRouterCode(), routingOperationIface.getRoutingAlternate(), routingOperationIface.getOperationSeqNum());
            MtRoutingOperationIface operationIface = mtRoutingOperationIfaceMap.get(routingOperationIfaceKey);
            if (operationIface != null) {
                routingOperationIface.setProcessTime(operationIface.getProcessTime());
                routingOperationIface.setMessage(operationIface.getMessage());
                routingOperationIface.setStatus(operationIface.getStatus());
                updateRoutingOperationIfaces.add(routingOperationIface);
            }
        }
        for (ItfOperationComponentIface componentIface : componentIfaces) {
            String componentIfaceKey = this.componentSpliceStr(componentIface.getRouterObjectType(), componentIface.getRouterCode(), componentIface.getRouterAlternate(), componentIface.getOperationSeqNum(), componentIface.getBomObjectType(), componentIface.getBomCode(), componentIface.getBomAlternate(), componentIface.getLineNum());
            MtOperationComponentIface operationComponentIface = mtOperationComponentIfaceMap.get(componentIfaceKey);
            if (operationComponentIface != null) {
                componentIface.setUpdateMethod(operationComponentIface.getUpdateMethod());
                componentIface.setMessage(operationComponentIface.getMessage());
                componentIface.setStatus(operationComponentIface.getStatus());
                updateOperationComponentIfaces.add(componentIface);
            }
        }
        if (CollectionUtils.isNotEmpty(updateRoutingOperationIfaces)) {
            List<String> sqlList = new ArrayList<>();
            for (ItfRoutingOperationIface updateRoutingOperationIface : updateRoutingOperationIfaces) {
                sqlList.addAll(mtCustomDbRepository.getUpdateSql(updateRoutingOperationIface));

            }
            if (CollectionUtils.isNotEmpty(sqlList)) {
                List<List<String>> commitSqlList = CommonUtils.splitSqlList(sqlList, 1000);
                for (List<String> commitSql : commitSqlList) {
                    jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(updateOperationComponentIfaces)) {
            List<String> sqlList = new ArrayList<>();
            for (ItfOperationComponentIface componentIface : updateOperationComponentIfaces) {
                sqlList.addAll(mtCustomDbRepository.getUpdateSql(componentIface));
            }
            if (CollectionUtils.isNotEmpty(sqlList)) {
                List<List<String>> commitSqlList = CommonUtils.splitSqlList(sqlList, 1000);
                for (List<String> commitSql : commitSqlList) {
                    jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
                }
            }
        }
    }

    private String spliceStr (String routerObjectType, String routerCode, String routingAlternate, String operationSeqNum) {
        StringBuffer sb = new StringBuffer();
        sb.append(routerObjectType);
        sb.append(routerCode);
        sb.append(routingAlternate);
        sb.append(operationSeqNum);
        return sb.toString();
    }

    private String componentSpliceStr (String routerObjectType, String routerCode, String routingAlternate, String operationSeqNum, String bomObjectType, String bomCode, String bomAlternate, Long lineNum) {
        StringBuffer sb = new StringBuffer();
        sb.append(routerObjectType);
        sb.append(routerCode);
        sb.append(routingAlternate);
        sb.append(operationSeqNum);
        sb.append(bomObjectType);
        sb.append(bomCode);
        sb.append(bomAlternate);
        sb.append(lineNum);
        return sb.toString();
    }
}
