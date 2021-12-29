package tarzan.iface.infra.repository.impl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import io.choerodon.core.oauth.CustomUserDetails;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.util.StringHelper;
import tarzan.iface.domain.entity.MtOperationComponentIface;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtOperationComponentIfaceRepository;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.iface.domain.vo.MtOperationComponentIfaceVO;
import tarzan.iface.domain.vo.MtOperationComponentIfaceVO1;
import tarzan.iface.domain.vo.MtOperationComponentIfaceVO3;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;
import tarzan.iface.infra.mapper.MtOperationComponentIfaceMapper;
import tarzan.method.domain.entity.MtBomSiteAssign;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.entity.MtRouterOperationComponent;
import tarzan.method.domain.entity.MtRouterSiteAssign;
import tarzan.method.domain.repository.MtBomSiteAssignRepository;
import tarzan.method.domain.repository.MtRouterOperationComponentRepository;
import tarzan.method.domain.repository.MtRouterRepository;
import tarzan.method.domain.repository.MtRouterSiteAssignRepository;
import tarzan.method.domain.vo.MtRouterOpComponentVO4;

/**
 * 工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口） 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@Component
public class MtOperationComponentIfaceRepositoryImpl extends BaseRepositoryImpl<MtOperationComponentIface>
        implements MtOperationComponentIfaceRepository {

    private static final int SQL_ITEM_COUNT_LIMIT = 10000;

    private static final String IFACE = "【API:operationComponentInterfaceImport】";
    private static final String MODULE = "INTERFACE";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepository;

    @Autowired
    private MtRouterSiteAssignRepository mtRouterSiteAssignRepository;

    @Autowired
    private MtBomSiteAssignRepository mtBomSiteAssignRepository;

    @Autowired
    private MtOperationComponentIfaceMapper mtOperationComponentIfaceMapper;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Autowired
    private MtRouterOperationComponentRepository mtRouterOperationComponentRepository;

    @Override
    public void operationComponentInterfaceImport(Long tenantId) {
        Long start = System.currentTimeMillis();
        // 1.获取Status为E或N的数据
        List<MtOperationComponentIface> unprocessedList = self().updateIfaceStatus(tenantId, "P");

        // 2.根据配置batchId分批进行数据导入
        Map<Double, List<MtOperationComponentIface>> ifaceMap = unprocessedList.stream().collect(Collectors
                .groupingBy(MtOperationComponentIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtOperationComponentIface>> entry : ifaceMap.entrySet()) {
            try {

                List<AuditDomain> ifaceList = self().saveInterfaceData(tenantId, entry.getValue());
                self().log(tenantId, ifaceList);
            } catch (Exception e) {
                List<AuditDomain> ifaceList = new ArrayList<>(entry.getValue().size());
                String errorMsg = StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage().replace("'", "\"") : "数据异常.";
                if (errorMsg.length() > 1000) {
                    errorMsg = errorMsg.substring(errorMsg.length() - 1000);
                }
                final String msg = errorMsg;
                entry.getValue().forEach(ifs -> {
                    ifs.setStatus("E");
                    ifs.setMessage(msg);

                });
                ifaceList.addAll(entry.getValue());
                self().log(tenantId, ifaceList);
            }
        }
        System.out.println(
                "工序组件接口导入" + unprocessedList.size() + "条数据，总耗时：" + (System.currentTimeMillis() - start) + "毫秒");
    }

    @Override
    public void myOperationComponentInterfaceImport(Long tenantId, Long batchId) {

        Long start = System.currentTimeMillis();
        // 1.获取Status为E或N的数据
        List<MtOperationComponentIface> unprocessedList = self().myUpdateIfaceStatus(tenantId, "P",batchId);

        // 2.根据配置batchId分批进行数据导入
        Map<Double, List<MtOperationComponentIface>> ifaceMap = unprocessedList.stream().collect(Collectors
                .groupingBy(MtOperationComponentIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtOperationComponentIface>> entry : ifaceMap.entrySet()) {
            try {

                List<AuditDomain> ifaceList = self().saveInterfaceData(tenantId, entry.getValue());
                log(tenantId, ifaceList);
            } catch (Exception e) {
                List<AuditDomain> ifaceList = new ArrayList<>(entry.getValue().size());
                e.printStackTrace();
                String error = e.getMessage().length() > 1000 ? e.getMessage().substring(0, 999) : e.getMessage();
                String msg = error + ";数据异常.";
                entry.getValue().forEach(ifs -> {
                    ifs.setStatus("E");
                    ifs.setMessage(msg);

                });
                ifaceList.addAll(entry.getValue());
                log(tenantId, ifaceList);
            }
        }
        System.out.println(
                "工序组件接口导入" + unprocessedList.size() + "条数据，总耗时：" + (System.currentTimeMillis() - start) + "毫秒");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<MtOperationComponentIface> updateIfaceStatus(Long tenantId, String status) {
        List<MtOperationComponentIface> list = mtOperationComponentIfaceMapper.getUnprocessedList(tenantId);
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> sqlList = new ArrayList<>(list.size());
            List<AuditDomain> auditDomains = new ArrayList<>(list.size());
            list.stream().forEach(ifs -> {
                ifs.setStatus(status);
                ifs.setTenantId(tenantId);
            });
            auditDomains.addAll(list);
            sqlList.addAll(constructSql(auditDomains));
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
        return list;
    }

    @Override
    public List<MtOperationComponentIface> myUpdateIfaceStatus(Long tenantId, String status, Long batchId) {
        List<MtOperationComponentIface> list = mtOperationComponentIfaceMapper.getMyUnprocessedList(tenantId, batchId);
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> sqlList = new ArrayList<>(list.size());
            List<AuditDomain> auditDomains = new ArrayList<>(list.size());
            list.stream().forEach(ifs -> {
                ifs.setStatus(status);
                ifs.setTenantId(tenantId);
            });
            auditDomains.addAll(list);
            sqlList.addAll(constructSql(auditDomains));
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
        return list;
    }

    @Override
    public List<AuditDomain> saveInterfaceData(Long tenantId,
                                               List<MtOperationComponentIface> operationComponentIfaceList) {
        Long tempUserId = Long.valueOf(-1L);
        CustomUserDetails customUserDetails = DetailsHelper.getUserDetails();
        if(Objects.nonNull(customUserDetails)
                && Objects.nonNull(customUserDetails.getUserId())){
            tempUserId = customUserDetails.getUserId();
        }
        final Long userId = tempUserId;
        Date now = new Date(System.currentTimeMillis());
        List<AuditDomain> ifaceSqlList = new ArrayList<>(operationComponentIfaceList.size());

        // 根据PLANT_CODE，ASSEMBLY_ITEM_CODE，ALTERNATE_ROUTING_DESIGNATOR分组
        Map<MtOperationComponentIfaceVO3, List<MtOperationComponentIface>> operationSequenceMap =
                operationComponentIfaceList.stream()
                        .collect(Collectors.groupingBy(t -> new MtOperationComponentIfaceVO3(
                                t.getPlantCode(), t.getRouterObjectType(), t.getRouterCode(),
                                t.getRouterAlternate(), t.getBomObjectType(), t.getBomCode(),
                                t.getBomAlternate())));

        // 获取工厂对应站点
        Map<String, MtSitePlantReleation> sitePlantMap = getSitePlantRelation(tenantId, operationComponentIfaceList);

        // 获取头数据列表
        List<MtOperationComponentIfaceVO3> headList = new ArrayList<>(operationSequenceMap.keySet());

        // Router
        List<MtOperationComponentIfaceVO> routerOperationList =
                mtOperationComponentIfaceMapper.getRouterOperationIdList(tenantId, headList);
        Map<String, List<MtRouterSiteAssign>> routerSiteAssignMap = null;
        Map<OperationTuple, List<MtOperationComponentIfaceVO>> tupleStringMap = null;
        Map<String, List<MtRouterOpComponentVO4>> routerMap = null;
        Map<String, MtRouter> mtRouterMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(routerOperationList)) {
            tupleStringMap = routerOperationList.stream().collect(Collectors.groupingBy(
                    c -> new OperationTuple(c.getRouterCode(), c.getRouterAlternate(), c.getRouterType())));
            List<String> routerIds = routerOperationList.stream().map(MtOperationComponentIfaceVO::getRouterId)
                    .distinct().collect(Collectors.toList());
            // Router site
            List<MtRouterSiteAssign> routerSiteAssignList =
                    mtRouterSiteAssignRepository.routerLimitRouterSiteAssignBatchQuery(tenantId, routerIds);

            routerSiteAssignMap = routerSiteAssignList.stream()
                    .collect(Collectors.groupingBy(MtRouterSiteAssign::getRouterId));

            // 获取所有的工序组件
            List<MtRouterOpComponentVO4> mtRouterOpComponentVO4s = mtRouterOperationComponentRepository
                    .selectRouterOpComponentByRouterIds(tenantId, routerIds);
            if (CollectionUtils.isNotEmpty(mtRouterOpComponentVO4s)) {
                routerMap = mtRouterOpComponentVO4s.stream()
                        .collect(Collectors.groupingBy(MtRouterOpComponentVO4::getRouterId));
            }
            // 获取所有的ROUTER
            List<MtRouter> mtRouters = mtRouterRepository.routerBatchGet(tenantId, routerIds);
            if (CollectionUtils.isNotEmpty(mtRouters)) {
                mtRouterMap.putAll(mtRouters.stream().collect(Collectors.toMap(MtRouter::getRouterId, c -> c)));
            }

        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("t.ROUTER_OPERATION_ID",
                routerOperationList.stream().filter(t -> StringUtils.isNotEmpty(t.getRouterOperationId()))
                        .map(MtOperationComponentIfaceVO::getRouterOperationId)
                        .collect(Collectors.toList()),
                1000);
        List<MtRouterOperationComponent> operationComponent =
                mtOperationComponentIfaceMapper.getOperationComponent(tenantId, whereInValuesSql);


        // Bom
        List<MtOperationComponentIfaceVO1> bomComponentList =
                mtOperationComponentIfaceMapper.getBomComponentIdList(tenantId, headList);
        Map<String, List<MtBomSiteAssign>> bomSiteAssignMap = null;
        Map<BomTuple, List<MtOperationComponentIfaceVO1>> bomTupleListMap = null;
        if (CollectionUtils.isNotEmpty(bomComponentList)) {
            bomTupleListMap = bomComponentList.stream().collect(Collectors
                    .groupingBy(c -> new BomTuple(c.getBomCode(), c.getBomType(), c.getBomAlternate())));
            // BOM site
            List<MtBomSiteAssign> bomSiteAssignList = mtBomSiteAssignRepository
                    .bomLimitBomSiteAssignBatchQuery(tenantId, bomComponentList.stream()
                            .map(MtOperationComponentIfaceVO1::getBomId).collect(Collectors.toList()));
            bomSiteAssignMap = bomSiteAssignList.stream().collect(Collectors.groupingBy(MtBomSiteAssign::getBomId));
        }
        whereInValuesSql = StringHelper.getWhereInValuesSql("t.BOM_COMPONENT_ID", bomComponentList.stream()
                .map(MtOperationComponentIfaceVO1::getBomComponentId).collect(Collectors.toList()), 1000);

        List<MtRouterOperationComponent> bomComponentId =
                mtOperationComponentIfaceMapper.getBomComponentId(tenantId, whereInValuesSql);

        // Router Operation Component List
        List<MtRouterOperationComponent> routerOperationComponentList = new ArrayList<>();
        routerOperationComponentList.addAll(operationComponent);
        routerOperationComponentList.addAll(bomComponentId);

        Map<ComTuple, List<MtRouterOperationComponent>> componentMap = routerOperationComponentList.stream().collect(
                Collectors.groupingBy(c -> new ComTuple(c.getRouterOperationId(), c.getBomComponentId())));
        MtRouterOperationComponent mtRouterOperationComponent;
        MtRouterOperationComponent temp;
        MtRouter mtRouter;
        OperationTuple operationTuple;
        BomTuple bomTuple;
        String routerId = null;
        String bomId = null;
        List<MtOperationComponentIfaceVO> ifaceVOS = null;
        List<MtOperationComponentIfaceVO1> bomVOS = null;
        List<MtRouterOperationComponent> updateList = new ArrayList<>();
        List<AuditDomain> mtRouterList = new ArrayList<>(mtRouterMap.size());
        // 批量获取Id、Cid
        List<String> mtRouterCids = this.customDbRepository.getNextKeys("mt_router_cid_s", mtRouterMap.keySet().size());

        for (Map.Entry<MtOperationComponentIfaceVO3, List<MtOperationComponentIface>> entry : operationSequenceMap
                .entrySet()) {
            // 获取头部信息校验
            MtOperationComponentIfaceVO3 head = entry.getKey();
            MtOperationComponentIface tempIface = entry.getValue().get(0);
            String allUpdate = tempIface.getUpdateMethod();
            Boolean updateFlag = true;
            String messageCode = "";
            if (MapUtils.isEmpty(sitePlantMap) || sitePlantMap.get(head.getPlantCode()) == null) {
                messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0012", MODULE,
                        tempIface.getIfaceId(), IFACE);
                updateFlag = false;
            }

            // 匹配 router
            operationTuple = new OperationTuple(entry.getKey().getRouterCode(),
                    StringUtils.isEmpty(entry.getKey().getRouterAlternate()) ? "MAIN"
                            : entry.getKey().getRouterAlternate(),
                    entry.getKey().getRouterObjectType());

            if (updateFlag && (MapUtils.isEmpty(tupleStringMap)
                    || CollectionUtils.isEmpty(tupleStringMap.get(operationTuple)))) {
                messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0037", MODULE,
                        tempIface.getIfaceId(), IFACE);
                updateFlag = false;
            }

            boolean routerSiteEmptyCheck = false;
            if (updateFlag) {
                routerId = tupleStringMap.get(operationTuple).get(0).getRouterId();
                ifaceVOS = tupleStringMap.get(operationTuple);
                routerSiteEmptyCheck = MapUtils.isEmpty(routerSiteAssignMap)
                        || CollectionUtils.isEmpty(routerSiteAssignMap.get(routerId))
                        || !routerSiteAssignMap.get(routerId).stream().map(MtRouterSiteAssign::getSiteId)
                        .collect(Collectors.toList())
                        .contains(sitePlantMap.get(head.getPlantCode()).getSiteId());
            }
            if (updateFlag && routerSiteEmptyCheck) {
                messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0015", MODULE,
                        tempIface.getIfaceId(), IFACE);
                updateFlag = false;
            }

            // 匹配 bom
            bomTuple = new BomTuple(entry.getKey().getBomCode(), entry.getKey().getBomObjectType(),
                    StringUtils.isEmpty(entry.getKey().getBomAlternate()) ? "MAIN"
                            : entry.getKey().getBomAlternate());

            if (updateFlag && (MapUtils.isEmpty(bomTupleListMap)
                    || CollectionUtils.isEmpty(bomTupleListMap.get(bomTuple)))) {
                messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0040", MODULE,
                        tempIface.getIfaceId(), IFACE);
                updateFlag = false;
            }
            boolean bomSiteEmptyCheck = false;
            if (updateFlag) {
                bomId = bomTupleListMap.get(bomTuple).get(0).getBomId();
                bomVOS = bomTupleListMap.get(bomTuple);
                bomSiteEmptyCheck = bomSiteAssignMap == null || CollectionUtils.isEmpty(bomSiteAssignMap.get(bomId))
                        || !bomSiteAssignMap.get(bomId).stream().map(MtBomSiteAssign::getSiteId)
                        .collect(Collectors.toList())
                        .contains(sitePlantMap.get(tempIface.getPlantCode()).getSiteId());
            }
            if (updateFlag && bomSiteEmptyCheck) {
                messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0016", MODULE,
                        tempIface.getIfaceId(), IFACE);
                updateFlag = false;
            }

            // 头信息不满足时所有行信息均报错
            if (!updateFlag) {
                ifaceSqlList.addAll(constructIfaceMessages(tenantId, entry.getValue(), "E", messageCode, now, userId));
                continue;
            }
            ifaceVOS = CollectionUtils.isEmpty(ifaceVOS) ? new ArrayList<>() : ifaceVOS;
            Map<String, List<MtOperationComponentIfaceVO>> ifaceVOMap = ifaceVOS.stream()
                    .collect(Collectors.groupingBy(MtOperationComponentIfaceVO::getOperationSeqNum));

            bomVOS = CollectionUtils.isEmpty(bomVOS) ? new ArrayList<>() : bomVOS;
            Map<Long, List<MtOperationComponentIfaceVO1>> bomVOMap =
                    bomVOS.stream().collect(Collectors.groupingBy(MtOperationComponentIfaceVO1::getLineNum));

            Boolean allFlag = "ALL".equals(allUpdate);
            Boolean allUpdateFlag = true;
            List<MtOperationComponentIface> correctList = new ArrayList<>();
            MtOperationComponentIfaceVO operationComponentIfaceVO;
            MtOperationComponentIfaceVO1 mtOperationComponentIfaceVO1;
            for (MtOperationComponentIface iface : entry.getValue()) {
                iface.setTenantId(tenantId);
                // 匹配router_operation_id
                operationComponentIfaceVO = CollectionUtils.isEmpty(ifaceVOMap.get(iface.getOperationSeqNum())) ? null
                        : ifaceVOMap.get(iface.getOperationSeqNum()).get(0);
                if (operationComponentIfaceVO == null) {
                    if (allFlag) {
                        allUpdateFlag = false;
                        messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0008",
                                MODULE, iface.getIfaceId(), iface.getRouterCode(), iface.getOperationSeqNum(),
                                IFACE);
                        break;
                    } else {
                        ifaceSqlList.add(constructIfaceMessage(tenantId, iface, "E",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "MT_INTERFACE_0008", MODULE, iface.getIfaceId(),
                                        iface.getRouterCode(), iface.getOperationSeqNum(), IFACE),
                                now, userId));
                        continue;
                    }
                }

                // 匹配bom_component_id
                mtOperationComponentIfaceVO1 = CollectionUtils.isEmpty(bomVOMap.get(iface.getLineNum())) ? null
                        : bomVOMap.get(iface.getLineNum()).get(0);
                if (mtOperationComponentIfaceVO1 == null) {
                    if (allFlag) {
                        allUpdateFlag = false;
                        messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0009",
                                MODULE, iface.getIfaceId(), iface.getBomCode(), iface.getLineNum().toString(),
                                IFACE);
                        break;
                    } else {
                        ifaceSqlList.add(constructIfaceMessage(tenantId, iface, "E",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "MT_INTERFACE_0009", MODULE, iface.getIfaceId(),
                                        iface.getBomCode(), iface.getLineNum().toString(), IFACE),
                                now, userId));
                        continue;
                    }
                }
                correctList.add(iface);
            }

            // 全量更新时行数据需保证全部正确
            if (allFlag && !allUpdateFlag) {
                ifaceSqlList.addAll(constructIfaceMessages(tenantId, entry.getValue(), "E", messageCode, now, userId));
                continue;
            }
            // 全量更新时 失效所有的组件
            if (allFlag && MapUtils.isNotEmpty(routerMap) && CollectionUtils.isNotEmpty(routerMap.get(routerId))) {
                for (MtRouterOpComponentVO4 mtRouterOpComponentVO4 : routerMap.get(routerId)) {
                    temp = new MtRouterOperationComponent();
                    temp.setTenantId(tenantId);
                    temp.setRouterOperationComponentId(mtRouterOpComponentVO4.getRouterOperationComponentId());
                    temp.setRouterOperationId(mtRouterOpComponentVO4.getRouterOperationId());
                    temp.setBomComponentId(mtRouterOpComponentVO4.getBomComponentId());
                    temp.setSequence(mtRouterOpComponentVO4.getSequence());
                    temp.setEnableFlag("N");
                    //temp.setLatestHisId(mtRouterOpComponentVO4.getLatestHisId());
                    temp.setCid(mtRouterOpComponentVO4.getCid());
                    temp.setCreationDate(mtRouterOpComponentVO4.getCreationDate());
                    temp.setCreatedBy(mtRouterOpComponentVO4.getCreatedBy());
                    temp.setObjectVersionNumber(mtRouterOpComponentVO4.getObjectVersionNumber() + 1L);
                    temp.setLastUpdateDate(now);
                    temp.setLastUpdatedBy(userId);
                    updateList.add(temp);
                }
            }

            ComTuple comTuple;
            MtOperationComponentIfaceVO router;
            MtOperationComponentIfaceVO1 bom;
            for (MtOperationComponentIface iface : correctList) {

                // 匹配router_operation_id
                router = CollectionUtils.isEmpty(ifaceVOMap.get(iface.getOperationSeqNum())) ? null
                        : ifaceVOMap.get(iface.getOperationSeqNum()).get(0);

                // 匹配bom_component_id
                bom = CollectionUtils.isEmpty(bomVOMap.get(iface.getLineNum())) ? null
                        : bomVOMap.get(iface.getLineNum()).get(0);

                if (router == null || bom == null) {
                    continue;
                }
                comTuple = new ComTuple(router.getRouterOperationId(), bom.getBomComponentId());
                mtRouterOperationComponent = CollectionUtils.isEmpty(componentMap.get(comTuple)) ? null
                        : componentMap.get(comTuple).get(0);


                // 更新数据
                if (mtRouterOperationComponent != null) {
                    if ("N".equals(iface.getEnableFlag()) || iface.getComponentEndDate() != null) {
                        mtRouterOperationComponent.setEnableFlag("N");
                    } else {
                        mtRouterOperationComponent.setEnableFlag("Y");
                    }
                    mtRouterOperationComponent
                            .setObjectVersionNumber(mtRouterOperationComponent.getObjectVersionNumber() + 1L);

                }
                // 新增数据
                else {
                    mtRouterOperationComponent = new MtRouterOperationComponent();
                    mtRouterOperationComponent.setRouterOperationId(router.getRouterOperationId());
                    mtRouterOperationComponent.setBomComponentId(bom.getBomComponentId());
                    mtRouterOperationComponent.setSequence(iface.getLineNum());
                    mtRouterOperationComponent.setEnableFlag("Y");

                    mtRouterOperationComponent.setCreatedBy(userId);
                    mtRouterOperationComponent.setCreationDate(now);
                    mtRouterOperationComponent.setObjectVersionNumber(1L);
                }
                mtRouterOperationComponent.setTenantId(tenantId);
                mtRouterOperationComponent.setLastUpdateDate(now);
                mtRouterOperationComponent.setLastUpdatedBy(userId);
                updateList.add(mtRouterOperationComponent);
                ifaceSqlList.add(constructIfaceMessage(tenantId, iface, "S", "成功.", now, userId));
            }

            // 更新router表中的bomId
            if (CollectionUtils.isNotEmpty(updateList) && MapUtils.isNotEmpty(mtRouterMap)
                    && mtRouterMap.get(routerId) != null) {
                mtRouter = mtRouterMap.get(routerId);
                mtRouter.setBomId(bomId);
                mtRouter.setTenantId(tenantId);
                mtRouter.setLastUpdateDate(now);
                mtRouter.setLastUpdatedBy(userId);
                mtRouter.setObjectVersionNumber(mtRouter.getObjectVersionNumber() + 1L);
                mtRouter.setCid(Long.valueOf(mtRouterCids.remove(0)));
                mtRouterList.add(mtRouter);
            }
        }
        List<String> sqlList = new ArrayList<>();
        /**
         * 处理数据
         */
        if (CollectionUtils.isNotEmpty(mtRouterList)) {
            sqlList.addAll(constructSql(mtRouterList));
            mtRouterList = null;
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            List<String> cids = this.customDbRepository.getNextKeys("mt_router_operation_component_cid_s",
                    updateList.size());
            List<MtRouterOperationComponent> insertList =
                    updateList.stream().filter(t -> StringUtils.isEmpty(t.getRouterOperationComponentId()))
                            .collect(Collectors.toList());
            List<String> ids =
                    this.customDbRepository.getNextKeys("mt_router_operation_component_s", insertList.size());
            List<AuditDomain> tempList = new ArrayList<>(updateList.size());
            updateList.stream().forEach(c -> {
                if (StringUtils.isEmpty(c.getRouterOperationComponentId())) {
                    c.setRouterOperationComponentId(ids.remove(0));
                }
                c.setCid(Long.valueOf(cids.remove(0)));
            });
            tempList.addAll(updateList);
            sqlList.addAll(constructSql(tempList));
            updateList = null;
            tempList = null;
        }
        /**
         * 保存数据
         */
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
            sqlList.clear();
        }
        return ifaceSqlList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void log(Long tenantId, List<AuditDomain> ifaceSqlList) {
        if (CollectionUtils.isNotEmpty(ifaceSqlList)) {
            List<String> sqlList = constructSql(ifaceSqlList);
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }

        }
    }

    /**
     * 根据工厂编码获取工厂与站点关系
     * <p>
     * 站点类型指定为manufacturing
     *
     * @param mtBomComponentIfaceList List<MtBomComponentIface>
     * @return Map<String   ,       MtSitePlantReleation>
     * @author benjamin
     * @date 2019-06-27 10:21
     */
    private Map<String, MtSitePlantReleation> getSitePlantRelation(Long tenantId,
                                                                   List<MtOperationComponentIface> mtBomComponentIfaceList) {
        List<String> plantCodeList = mtBomComponentIfaceList.stream().map(MtOperationComponentIface::getPlantCode)
                .distinct().collect(Collectors.toList());
        MtSitePlantReleationVO3 mtSitePlantReleationVO3 = new MtSitePlantReleationVO3();
        mtSitePlantReleationVO3.setPlantCodes(plantCodeList);
        mtSitePlantReleationVO3.setSiteType("MANUFACTURING");
        List<MtSitePlantReleation> relationByPlantAndSiteType =
                mtSitePlantReleationRepository.getRelationByPlantAndSiteType(tenantId, mtSitePlantReleationVO3);
        Map<String, MtSitePlantReleation> sitePlantMap = null;
        if (CollectionUtils.isNotEmpty(relationByPlantAndSiteType)) {
            sitePlantMap = relationByPlantAndSiteType.stream()
                    .collect(Collectors.toMap(MtSitePlantReleation::getPlantCode, t -> t));
        }
        return sitePlantMap;
    }

    /**
     * 构建返回消息
     *
     * @param mtOperationComponentIface MtOperationComponentIface
     * @param message                   错误消息
     * @return MtBomComponentIface
     * @author benjamin
     * @date 2019-06-27 17:02
     */
    private MtOperationComponentIface constructIfaceMessage(Long tenantId,
                                                            MtOperationComponentIface mtOperationComponentIface, String status, String message, Date date,
                                                            Long userId) {
        mtOperationComponentIface.setTenantId(tenantId);
        mtOperationComponentIface.setStatus(status);
        mtOperationComponentIface.setMessage(message);
        mtOperationComponentIface.setLastUpdateDate(date);
        mtOperationComponentIface.setLastUpdatedBy(userId);

        return mtOperationComponentIface;
    }

    /**
     * 构建返回消息--全量更新时
     *
     * @param ifaces  MtOperationComponentIface
     * @param message 错误消息
     * @return List
     * @author benjamin
     * @date 2019-06-27 17:02
     */
    private List<MtOperationComponentIface> constructIfaceMessages(Long tenantId,
                                                                   List<MtOperationComponentIface> ifaces, String status, String message, Date date, Long userId) {
        ifaces.stream().forEach(t -> {
            t.setStatus(status);
            t.setTenantId(tenantId);
            t.setMessage(message);
            t.setLastUpdateDate(date);
            t.setLastUpdatedBy(userId);
        });
        return ifaces;
    }

    private List<String> constructSql(List<AuditDomain> ifaceSqlList) {
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ifaceSqlList)) {
            List<List<AuditDomain>> splitSqlList = StringHelper.splitSqlList(ifaceSqlList, SQL_ITEM_COUNT_LIMIT);
            for (List<AuditDomain> domains : splitSqlList) {
                sqlList.addAll(customDbRepository.getReplaceSql(domains));
            }
        }
        return sqlList;
    }

    private List<List<String>> commitSqlList(List<String> sqlList, int splitNum) {

        List<List<String>> returnList = new ArrayList<>();
        if (sqlList.size() <= splitNum) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / splitNum;
            int splitRest = sqlList.size() % splitNum;

            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * splitNum, (i + 1) * splitNum));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * splitNum, sqlList.size()));
            }
        }
        return returnList;
    }

    private static class OperationTuple implements Serializable {
        private static final long serialVersionUID = 7791600144687712294L;
        private String routerName;
        private String routerType;
        private String revision;

        public OperationTuple(String routerName, String routerType, String revision) {
            this.routerName = routerName;
            this.routerType = routerType;
            this.revision = revision;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            OperationTuple that = (OperationTuple) o;
            return Objects.equals(routerName, that.routerName) && Objects.equals(routerType, that.routerType)
                    && Objects.equals(revision, that.revision);
        }

        @Override
        public int hashCode() {
            return Objects.hash(routerName, routerType, revision);
        }
    }

    private static class ComTuple implements Serializable {
        private static final long serialVersionUID = 3337579030715958694L;
        private String routerOperationId;
        private String bomComponentId;

        public ComTuple(String routerOperationId, String bomComponentId) {
            this.routerOperationId = routerOperationId;
            this.bomComponentId = bomComponentId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ComTuple comTuple = (ComTuple) o;
            return Objects.equals(routerOperationId, comTuple.routerOperationId)
                    && Objects.equals(bomComponentId, comTuple.bomComponentId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(routerOperationId, bomComponentId);
        }
    }

    private static class BomTuple implements Serializable {
        private static final long serialVersionUID = 7791600144687712294L;
        private String bomName;
        private String bomType;
        private String revision;

        public BomTuple(String bomName, String bomType, String revision) {
            this.bomName = bomName;
            this.bomType = bomType;
            this.revision = revision;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BomTuple bomTuple = (BomTuple) o;
            return Objects.equals(bomName, bomTuple.bomName) && Objects.equals(bomType, bomTuple.bomType)
                    && Objects.equals(revision, bomTuple.revision);
        }

        @Override
        public int hashCode() {
            return Objects.hash(bomName, bomType, revision);
        }
    }

}
