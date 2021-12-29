package tarzan.iface.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.tarzan.common.domain.vo.MtExtendVO5;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtRoutingOperationIface;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtRoutingOperationIfaceRepository;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.iface.domain.vo.MtSitePlantReleationVO;
import tarzan.iface.domain.vo.MtSitePlantReleationVO1;
import tarzan.iface.infra.mapper.MtRoutingOperationIfaceMapper;
import tarzan.material.domain.entity.MtPfepManufacturing;
import tarzan.material.domain.repository.MtPfepManufacturingRepository;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.method.domain.repository.MtRouterRepository;
import tarzan.method.domain.repository.MtRouterSiteAssignRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.method.domain.vo.MtRouterOperationVO;
import tarzan.method.domain.vo.MtRouterStepVO;
import tarzan.method.domain.vo.MtRouterVO10;
import tarzan.order.domain.repository.MtWorkOrderRepository;

/**
 * 工艺路线接口表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@Component
@Slf4j
public class MtRoutingOperationIfaceRepositoryImpl extends BaseRepositoryImpl<MtRoutingOperationIface>
        implements MtRoutingOperationIfaceRepository {

    private static final int SQL_ITEM_COUNT_LIMIT = 10000;
    private static final String ROUTER_ATTR_TABLE = "mt_router_attr";
    private static final String ROUTER_STEP_ATTR_TABLE = "mt_router_step_attr";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtRouterRepository mtRouterRepository;
    @Autowired
    private MtRouterSiteAssignRepository mtRouterSiteAssignRepository;
    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;
    @Autowired
    private MtOperationRepository mtOperationRepository;
    @Autowired
    private MtPfepManufacturingRepository mtPfepManufacturingRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepository;

    @Autowired
    private MtRoutingOperationIfaceMapper mtRoutingOperationIfaceMapper;

    @Override
    public void routerInterfaceImport(Long tenantId) {
        List<MtRoutingOperationIface> bomComponentIfaceList =
                mtRoutingOperationIfaceMapper.getUnprocessedList(tenantId);

        // get data list
        Map<Double, List<MtRoutingOperationIface>> ifacePerBatch = bomComponentIfaceList.stream().collect(
                Collectors.groupingBy(MtRoutingOperationIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtRoutingOperationIface>> entry : ifacePerBatch.entrySet()) {
            try {
                // change data status
                self().updateIfaceStatus(tenantId, entry.getValue(), "P");

                List<MtRoutingOperationIface> ifaceList = self().saveRouterOperationIface(tenantId, entry.getValue());

                self().log(tenantId, ifaceList);
            } catch (Exception e) {
                List<MtRoutingOperationIface> ifaceList = new ArrayList<>(entry.getValue().size());

                String errorMsg = StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage().replace("'", "\"") : "数据异常.";
                if (errorMsg.length() > 1000) {
                    errorMsg = errorMsg.substring(errorMsg.length() - 1000);
                }
                final String msg = errorMsg;

                entry.getValue().forEach(ifs -> {
                    ifs.setStatus("E");
                    ifs.setMessage(msg);
                    ifs.setTenantId(tenantId);
                    ifaceList.add(ifs);
                });

                self().log(tenantId, ifaceList);
            }
        }
    }

    @Override
    public void myRouterInterfaceImport(Long tenantId, Long batchId) {
        List<MtRoutingOperationIface> bomComponentIfaceList =
                mtRoutingOperationIfaceMapper.getMyUnprocessedList(tenantId,batchId);

        // get data list
        Map<Double, List<MtRoutingOperationIface>> ifacePerBatch = bomComponentIfaceList.stream().collect(
                Collectors.groupingBy(MtRoutingOperationIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtRoutingOperationIface>> entry : ifacePerBatch.entrySet()) {
            try {
                // change data status
                self().updateIfaceStatus(tenantId, entry.getValue(), "P");

                List<MtRoutingOperationIface> ifaceList = self().saveRouterOperationIface(tenantId, entry.getValue());

                log(tenantId, ifaceList);
            } catch (Exception e) {
                List<MtRoutingOperationIface> ifaceList = new ArrayList<>(entry.getValue().size());
                e.printStackTrace();
                String error = e.getMessage().length() > 1000 ? e.getMessage().substring(0, 999) : e.getMessage();
                String msg = error + ";数据异常.";
                entry.getValue().forEach(ifs -> {
                    ifs.setStatus("E");
                    ifs.setMessage(msg);
                    ifs.setTenantId(tenantId);
                    ifaceList.add(ifs);
                });

                log(tenantId, ifaceList);
            }
        }
    }


    @Override
    public List<MtRoutingOperationIface> saveRouterOperationIface(Long tenantId,
                                                                  List<MtRoutingOperationIface> mtRoutingOperationIfaceList) {
        List<MtRoutingOperationIface> ifaceList = new ArrayList<>(mtRoutingOperationIfaceList.size());

        // group by routerCode, plantCode, assemblyItemCode, routingAlternate
        Map<Tuple, List<MtRoutingOperationIface>> ifacePerRouterCode = mtRoutingOperationIfaceList.stream()
                .collect(Collectors.groupingBy(r -> new Tuple(r.getRouterCode(), r.getPlantCode(),
                        r.getRouterObjectType(), r.getRoutingAlternate())));

        // get site plant relation
        Map<String, MtSitePlantReleation> sitePlantMap = getSitePlantRelation(tenantId, mtRoutingOperationIfaceList);

        // create event
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("ROUTER_INTERFACE_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        for (Map.Entry<Tuple, List<MtRoutingOperationIface>> entry : ifacePerRouterCode.entrySet()) {
//            MtRoutingOperationIface headRoutingOperation = entry.getValue().get(0);
//            try {
//                MtRouterVO10 routerDataVO;
//                String allUpdate = headRoutingOperation.getUpdateMethod();
//                // construct router & router site
//                MtRouter mtRouter = constructRouter(tenantId, headRoutingOperation);
//                MtRouterSiteAssign mtRouterSiteAssign = constructRouterSiteAssign(tenantId, headRoutingOperation,
//                        mtRouter.getRouterId(), sitePlantMap);
//
//                // query origin router & steps
//                List<MtRouterStepVO> routerStepList;
//                if (StringUtils.isNotEmpty(mtRouter.getRouterId())) {
//                    routerDataVO = mtRouterRepository.routerAllQuery(tenantId, mtRouter.getRouterId());
//                    routerStepList = routerDataVO.getRouterSteps();
//                    routerStepList = CollectionUtils.isNotEmpty(routerStepList) ? routerStepList : new ArrayList<>();
//                } else {
//                    routerDataVO = new MtRouterVO10();
//                    routerStepList = new ArrayList<>(entry.getValue().size());
//                }
//                routerDataVO.setRouter(mtRouter);
//                routerDataVO.setRouterSiteAssigns(Collections.singletonList(mtRouterSiteAssign));
//
//                // check router operation, if do not find operation then continue loop
//                String errMsg = null;
//                Map<String, MtOperation> routerOperationMap = new HashMap<>(entry.getValue().size());
//                MtOperation mtOperation;
//                Boolean allLapseFlag = true;
//                String workOrderId = null;
//                for (MtRoutingOperationIface iface : entry.getValue()) {
//                    // get operation
//                    mtOperation = getOperation(tenantId, iface, mtRouterSiteAssign.getSiteId());
//                    if (mtOperation == null) {
//                        errMsg = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0010",
//                                "INTERFACE", iface.getIfaceId(), "【API：routerInterfaceImport】");
//                        break;
//                    }
//
//                    // check if wo exists
//                    if ("WO".equals(headRoutingOperation.getRouterObjectType())) {
//                        workOrderId = mtWorkOrderRepository.numberLimitWoGet(tenantId, iface.getRouterObjectCode());
//
//                        if (StringUtils.isEmpty(workOrderId)) {
//                            errMsg = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0022",
//                                    "INTERFACE", String.valueOf(iface.getIfaceId()), "【API:routerInterfaceImport】");
//                            break;
//                        }
//                    }
//
//                    // 校验是否所有的工序全失效
//                    if (allLapseFlag && iface.getOperationEndDate() == null) {
//                        allLapseFlag = false;
//                    }
//                    routerOperationMap.put(iface.getIfaceId(), mtOperation);
//                }
//                if (StringUtils.isNotEmpty(errMsg)) {
//                    String finalOpErrMsg = errMsg;
//                    entry.getValue().forEach(
//                            t -> ifaceList.add(constructIfaceMessage(tenantId, t, "E", finalOpErrMsg)));
//                    continue;
//                }
//                if (allLapseFlag) {
//                    String finalOpErrMsg = mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "MT_INTERFACE_0048", "INTERFACE");
//                    entry.getValue().forEach(
//                            t -> ifaceList.add(constructIfaceMessage(tenantId, t, "E", finalOpErrMsg)));
//                    continue;
//                }
//
//                for (MtRoutingOperationIface iface : entry.getValue()) {
//
//                    MtRouterStepVO addRouterStepVO = new MtRouterStepVO();
//
//                    // construct router step
//                    MtRouterStep mtRouterStep = constructRouterStep(tenantId, iface);
//                    addRouterStepVO.setRouterStep(mtRouterStep);
//
//                    // set router operation
//                    MtRouterOperationVO routerOperationVO = new MtRouterOperationVO();
//                    MtRouterOperation mtRouterOperation = new MtRouterOperation();
//                    mtRouterOperation.setOperationId(routerOperationMap.get(iface.getIfaceId()).getOperationId());
//                    mtRouterOperation.setSpecialIntruction(iface.getSpecialIntruction());
//                    mtRouterOperation.setRequiredTimeInProcess(iface.getProcessTime());
//                    routerOperationVO.setRouterOperation(mtRouterOperation);
//                    addRouterStepVO.setRouterOperation(routerOperationVO);
//
//                    // set router process flag
//                    MtRouterStepVO originRouterStepVO = routerStepList.stream()
//                            .filter(s -> mtRouterStep.getStepName().equals(s.getRouterStep().getStepName()))
//                            .findAny().orElse(null);
//                    if (originRouterStepVO != null) {
//                        if (iface.getOperationEndDate() != null) {
//                            originRouterStepVO.setProcessFlag("delete");
//                        } else {
//                            originRouterStepVO.setProcessFlag("update");
//                            originRouterStepVO.getRouterStep()
//                                    .setDescription(addRouterStepVO.getRouterStep().getDescription());
//                            originRouterStepVO.getRouterStep()
//                                    .setRouterStepType(addRouterStepVO.getRouterStep().getRouterStepType());
//                            originRouterStepVO.getRouterStep().setQueueDecisionType(
//                                    addRouterStepVO.getRouterStep().getQueueDecisionType());
//                            originRouterStepVO.setRouterOperation(addRouterStepVO.getRouterOperation());
//                        }
//                    } else {
//                        if (iface.getOperationEndDate() == null) {
//                            addRouterStepVO.setProcessFlag("add");
//                            routerStepList.add(addRouterStepVO);
//                        }
//                    }
//                    ifaceList.add(constructIfaceMessage(tenantId, iface, "S", "成功."));
//                }
//
//                // 接口表中不存在，存在业务表中的数据删除，process flag=delete
//                List<String> routerStepName = entry.getValue().stream().map(MtRoutingOperationIface::getOperationSeqNum)
//                        .distinct().collect(Collectors.toList());
//                if ("ALL".equalsIgnoreCase(allUpdate) && CollectionUtils.isNotEmpty(routerStepName)) {
//                    routerStepList.stream().filter(s -> !routerStepName.contains(s.getRouterStep().getStepName()))
//                            .forEach(t -> t.setProcessFlag("delete"));
//                }
//
//                routerDataVO.setRouterSteps(constructRouterStepRelation(routerStepList));
//
//                // save router, API may not have a return value (need to judge)
//                String routingId = mtRouterRepository.routerAllUpdate(tenantId, routerDataVO, "N");
//                if (StringUtils.isNotEmpty(routingId)) {
//                    if ("MATERIAL".equals(headRoutingOperation.getRouterObjectType())) {
//                        // map assembly item to material
//                        Map<String, MtSitePlantReleationVO1> assemblyMaterialMap = getMaterialSite(tenantId,
//                                entry.getKey().plantCode,
//                                entry.getValue().stream().map(MtRoutingOperationIface::getRouterObjectCode)
//                                        .collect(Collectors.toList()));
//                        // update pfep
//                        if (assemblyMaterialMap == null || assemblyMaterialMap
//                                .get(headRoutingOperation.getRouterObjectCode()) == null) {
//                            throw new MtException("MT_INTERFACE_0013",
//                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                            "MT_INTERFACE_0013", "INTERFACE",
//                                            headRoutingOperation.getIfaceId(),
//                                            "【API：routerInterfaceImport】"));
//                        }
//                        updatePfepManufacturing(tenantId, routingId, headRoutingOperation, assemblyMaterialMap);
//                    } else if ("WO".equals(headRoutingOperation.getRouterObjectType())) {
//                        updateWoRel(tenantId, routingId, eventId, workOrderId, headRoutingOperation);
//                    }
//
//                    // save router extend columns
//                    saveAttrColumn(tenantId, headRoutingOperation, ROUTER_ATTR_TABLE, routingId, "headAttribute",
//                            eventId);
//
//                    // save router step extend columns
//                    MtRouterStep queryRouterStep;
//                    for (MtRoutingOperationIface iface : ifaceList) {
//                        if (!"S".equals(iface.getStatus())) {
//                            continue;
//                        }
//                        queryRouterStep = new MtRouterStep();
//                        queryRouterStep.setTenantId(tenantId);
//                        queryRouterStep.setRouterId(routingId);
//                        queryRouterStep.setStepName(iface.getOperationSeqNum());
//
//                        Criteria criteria = new Criteria(queryRouterStep);
//                        List<WhereField> whereFields = new ArrayList<WhereField>();
//                        whereFields.add(new WhereField(MtRouterStep.FIELD_TENANT_ID, Comparison.EQUAL));
//                        whereFields.add(new WhereField(MtRouterStep.FIELD_ROUTER_ID, Comparison.EQUAL));
//                        whereFields.add(new WhereField(MtRouterStep.FIELD_STEP_NAME, Comparison.EQUAL));
//                        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
//
//                        List<MtRouterStep> queryRouterStepList =
//                                mtRouterStepRepository.selectOptional(queryRouterStep, criteria);
//                        if (CollectionUtils.isNotEmpty(queryRouterStepList)) {
//                            saveAttrColumn(tenantId, iface, ROUTER_STEP_ATTR_TABLE,
//                                    queryRouterStepList.get(0).getRouterStepId(), "lineAttribute", eventId);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                throw new MtException("MT_INTERFACE_0011",
//                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0011",
//                                "INTERFACE", headRoutingOperation.getIfaceId(), e.getMessage(),
//                                "【API:routerInterfaceImport】"));
//            }
            try {
                ifaceList.addAll(save(tenantId, eventId, entry, sitePlantMap));
            } catch (Exception e){
                List<MtRoutingOperationIface> ifaceList1 = new ArrayList<>(entry.getValue().size());

                String errorMsg = StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage().replace("'", "\"") : "数据异常.";
                if (errorMsg.length() > 1000) {
                    errorMsg = errorMsg.substring(errorMsg.length() - 1000);
                }
                final String msg = errorMsg;

                entry.getValue().forEach(ifs -> {
                    ifs.setStatus("E");
                    ifs.setMessage(msg);
                    ifs.setTenantId(tenantId);
                    ifaceList1.add(ifs);
                });

                self().log(tenantId, ifaceList1);
            }
        }

        return ifaceList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<MtRoutingOperationIface> save(Long tenantId, String eventId, Map.Entry<Tuple, List<MtRoutingOperationIface>> entry, Map<String, MtSitePlantReleation> sitePlantMap){
        List<MtRoutingOperationIface> ifaceList = new ArrayList<>(entry.getValue().size());
        MtRoutingOperationIface headRoutingOperation = entry.getValue().get(0);
        try {
            MtRouterVO10 routerDataVO;
            String allUpdate = headRoutingOperation.getUpdateMethod();
            // construct router & router site
            MtRouter mtRouter = constructRouter(tenantId, headRoutingOperation);
            MtRouterSiteAssign mtRouterSiteAssign = constructRouterSiteAssign(tenantId, headRoutingOperation,
                    mtRouter.getRouterId(), sitePlantMap);

            // query origin router & steps
            List<MtRouterStepVO> routerStepList;
            if (StringUtils.isNotEmpty(mtRouter.getRouterId())) {
                log.info("<==============mtRouterRepository.routerAllQuery=================>");
                routerDataVO = mtRouterRepository.routerAllQuery(tenantId, mtRouter.getRouterId());
                routerStepList = routerDataVO.getRouterSteps();
                routerStepList = CollectionUtils.isNotEmpty(routerStepList) ? routerStepList : new ArrayList<>();
                log.info("<==============routerStepList.size=================>" + routerStepList.size());
            } else {
                routerDataVO = new MtRouterVO10();
                routerStepList = new ArrayList<>(entry.getValue().size());
            }
            routerDataVO.setRouter(mtRouter);
            routerDataVO.setRouterSiteAssigns(Collections.singletonList(mtRouterSiteAssign));

            // check router operation, if do not find operation then continue loop
            String errMsg = null;
            Map<String, MtOperation> routerOperationMap = new HashMap<>(entry.getValue().size());
            MtOperation mtOperation;
            Boolean allLapseFlag = true;
            String workOrderId = null;
            for (MtRoutingOperationIface iface : entry.getValue()) {
                // get operation
                mtOperation = getOperation(tenantId, iface, mtRouterSiteAssign.getSiteId());
                if (mtOperation == null) {
                    errMsg = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0010",
                            "INTERFACE", iface.getIfaceId(), "【API：routerInterfaceImport】");
                    break;
                }

                // check if wo exists
                if ("WO".equals(headRoutingOperation.getRouterObjectType())) {
                    workOrderId = mtWorkOrderRepository.numberLimitWoGet(tenantId, iface.getRouterObjectCode());

                    if (StringUtils.isEmpty(workOrderId)) {
                        errMsg = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0022",
                                "INTERFACE", String.valueOf(iface.getIfaceId()), "【API:routerInterfaceImport】");
                        break;
                    }
                }

                // 校验是否所有的工序全失效
                if (allLapseFlag && iface.getOperationEndDate() == null) {
                    allLapseFlag = false;
                }
                routerOperationMap.put(iface.getIfaceId(), mtOperation);
            }
            if (StringUtils.isNotEmpty(errMsg)) {
                String finalOpErrMsg = errMsg;
                entry.getValue().forEach(
                        t -> ifaceList.add(constructIfaceMessage(tenantId, t, "E", finalOpErrMsg)));
                return ifaceList;
            }
            if (allLapseFlag) {
                String finalOpErrMsg = mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INTERFACE_0048", "INTERFACE");
                entry.getValue().forEach(
                        t -> ifaceList.add(constructIfaceMessage(tenantId, t, "E", finalOpErrMsg)));
                return ifaceList;
            }

            for (MtRoutingOperationIface iface : entry.getValue()) {

                MtRouterStepVO addRouterStepVO = new MtRouterStepVO();

                // construct router step
                MtRouterStep mtRouterStep = constructRouterStep(tenantId, iface);
                addRouterStepVO.setRouterStep(mtRouterStep);

                // set router operation
                MtRouterOperationVO routerOperationVO = new MtRouterOperationVO();
                MtRouterOperation mtRouterOperation = new MtRouterOperation();
                mtRouterOperation.setOperationId(routerOperationMap.get(iface.getIfaceId()).getOperationId());
                mtRouterOperation.setSpecialIntruction(iface.getSpecialIntruction());
                mtRouterOperation.setRequiredTimeInProcess(iface.getProcessTime());
                routerOperationVO.setRouterOperation(mtRouterOperation);
                addRouterStepVO.setRouterOperation(routerOperationVO);

                // set router process flag
                MtRouterStepVO originRouterStepVO = routerStepList.stream()
                        .filter(s -> mtRouterStep.getStepName().equals(s.getRouterStep().getStepName()))
                        .findAny().orElse(null);
                if (originRouterStepVO != null) {
                    if (iface.getOperationEndDate() != null) {
                        originRouterStepVO.setProcessFlag("delete");
                    } else {
                        originRouterStepVO.setProcessFlag("update");
                        originRouterStepVO.getRouterStep()
                                .setDescription(addRouterStepVO.getRouterStep().getDescription());
                        originRouterStepVO.getRouterStep()
                                .setRouterStepType(addRouterStepVO.getRouterStep().getRouterStepType());
                        originRouterStepVO.getRouterStep().setQueueDecisionType(
                                addRouterStepVO.getRouterStep().getQueueDecisionType());
                        originRouterStepVO.setRouterOperation(addRouterStepVO.getRouterOperation());
                    }
                } else {
                    if (iface.getOperationEndDate() == null) {
                        addRouterStepVO.setProcessFlag("add");
                        routerStepList.add(addRouterStepVO);
                    }
                }
                ifaceList.add(constructIfaceMessage(tenantId, iface, "S", "成功."));
            }

            // 接口表中不存在，存在业务表中的数据删除，process flag=delete
            List<String> routerStepName = entry.getValue().stream().map(MtRoutingOperationIface::getOperationSeqNum)
                    .distinct().collect(Collectors.toList());
            if ("ALL".equalsIgnoreCase(allUpdate) && CollectionUtils.isNotEmpty(routerStepName)) {
                routerStepList.stream().filter(s -> !routerStepName.contains(s.getRouterStep().getStepName()))
                        .forEach(t -> t.setProcessFlag("delete"));
                log.info("<============routerStepList delete===========>");
            }

            routerDataVO.setRouterSteps(constructRouterStepRelation(routerStepList));

            log.info("<============routerDataVO.getRouterSteps() begin===========>");
            for (MtRouterStepVO item :routerDataVO.getRouterSteps()
                 ) {
                log.info("item.getRouterStep().getRouterStepId():" + item.getRouterStep().getRouterStepId());
            }
            log.info("<============routerDataVO.getRouterSteps() end===========>");

            // save router, API may not have a return value (need to judge)
            String routingId = mtRouterRepository.routerAllUpdate(tenantId, routerDataVO, "N");
            if (StringUtils.isNotEmpty(routingId)) {
                if ("MATERIAL".equals(headRoutingOperation.getRouterObjectType())) {
                    // map assembly item to material
                    Map<String, MtSitePlantReleationVO1> assemblyMaterialMap = getMaterialSite(tenantId,
                            entry.getKey().plantCode,
                            entry.getValue().stream().map(MtRoutingOperationIface::getRouterObjectCode)
                                    .collect(Collectors.toList()));
                    // update pfep
                    if (assemblyMaterialMap == null || assemblyMaterialMap
                            .get(headRoutingOperation.getRouterObjectCode()) == null) {
                        throw new MtException("MT_INTERFACE_0013",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "MT_INTERFACE_0013", "INTERFACE",
                                        headRoutingOperation.getIfaceId(),
                                        "【API：routerInterfaceImport】"));
                    }
                    updatePfepManufacturing(tenantId, routingId, headRoutingOperation, assemblyMaterialMap);
                } else if ("WO".equals(headRoutingOperation.getRouterObjectType())) {
                    updateWoRel(tenantId, routingId, eventId, workOrderId, headRoutingOperation);
                }

                // save router extend columns
                saveAttrColumn(tenantId, headRoutingOperation, ROUTER_ATTR_TABLE, routingId, "headAttribute",
                        eventId);

                // save router step extend columns
                MtRouterStep queryRouterStep;
                for (MtRoutingOperationIface iface : ifaceList) {
                    if (!"S".equals(iface.getStatus())) {
                        continue;
                    }
                    queryRouterStep = new MtRouterStep();
                    queryRouterStep.setTenantId(tenantId);
                    queryRouterStep.setRouterId(routingId);
                    queryRouterStep.setStepName(iface.getOperationSeqNum());

                    Criteria criteria = new Criteria(queryRouterStep);
                    List<WhereField> whereFields = new ArrayList<WhereField>();
                    whereFields.add(new WhereField(MtRouterStep.FIELD_TENANT_ID, Comparison.EQUAL));
                    whereFields.add(new WhereField(MtRouterStep.FIELD_ROUTER_ID, Comparison.EQUAL));
                    whereFields.add(new WhereField(MtRouterStep.FIELD_STEP_NAME, Comparison.EQUAL));
                    criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

                    List<MtRouterStep> queryRouterStepList =
                            mtRouterStepRepository.selectOptional(queryRouterStep, criteria);
                    if (CollectionUtils.isNotEmpty(queryRouterStepList)) {
                        saveAttrColumn(tenantId, iface, ROUTER_STEP_ATTR_TABLE,
                                queryRouterStepList.get(0).getRouterStepId(), "lineAttribute", eventId);
                    }
                }
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (StringUtils.isEmpty(errorMessage)) {
                StackTraceElement stackTraceElement = e.getStackTrace()[0];
                StringBuffer sb = new StringBuffer();
                sb.append("Exception type:").append(e.getClass().getName())
                        .append(",Exception method:").append(stackTraceElement.getClassName()).append(".").append(stackTraceElement.getMethodName())
                        .append(",Exception location:").append(stackTraceElement.getFileName()).append(":").append(stackTraceElement.getLineNumber());
                errorMessage = sb.toString();
            }
            throw new MtException("MT_INTERFACE_0011",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0011",
                            "INTERFACE", headRoutingOperation.getIfaceId(), errorMessage,
                            "【API:routerInterfaceImport】"));
        }

        return ifaceList;
    }

    /**
     * 保存扩展属性
     *
     * @author benjamin
     * @date 2019-08-19 21:50
     * @param tenantId 租户Id
     * @param iface MtRoutingOperationIface
     * @param keyId Id
     * @param eventId 事件Id
     */
    private void saveAttrColumn(Long tenantId, MtRoutingOperationIface iface, String tableName, String keyId,
                                String attributePrefix, String eventId) {
        MtExtendVO mtExtendVO = new MtExtendVO();
        mtExtendVO.setTableName(tableName);
        mtExtendVO.setKeyId(keyId);
        List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);

        Map<String, String> originMap = extendAttrList.stream()
                .collect(Collectors.toMap(MtExtendAttrVO::getAttrName, MtExtendAttrVO::getAttrValue));

        Map<String, String> fieldMap = ObjectFieldsHelper.getAttributeFields(iface, attributePrefix);

        for (Map.Entry<String, String> entry : originMap.entrySet()) {
            fieldMap.putIfAbsent(entry.getKey(), "");
        }

        List<MtExtendVO5> saveExtendList = new ArrayList<>();
        MtExtendVO5 saveExtend;
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            saveExtend = new MtExtendVO5();
            saveExtend.setAttrName(entry.getKey());
            saveExtend.setAttrValue(entry.getValue());
            saveExtendList.add(saveExtend);
        }

        if (CollectionUtils.isNotEmpty(saveExtendList)) {
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, tableName, keyId, eventId, saveExtendList);
        }
    }

    /**
     * 根据工厂编码获取工厂与站点关系
     * <p>
     * 站点类型指定为manufacturing
     *
     * @param tenantId 租户Id
     * @param mtRoutingOperationIfaceList List<MtRoutingOperationIface>
     * @return Map<String, MtSitePlantReleation>
     * @author benjamin
     * @date 2019-06-27 10:21
     */
    private Map<String, MtSitePlantReleation> getSitePlantRelation(Long tenantId,
                                                                   List<MtRoutingOperationIface> mtRoutingOperationIfaceList) {
        List<String> plantCodeList = mtRoutingOperationIfaceList.stream().map(MtRoutingOperationIface::getPlantCode)
                .collect(Collectors.toList());

        List<String> distinctPlantCodeList = plantCodeList.stream().distinct().collect(Collectors.toList());

        Map<String, MtSitePlantReleation> sitePlantMap = new HashMap<>(distinctPlantCodeList.size());

        MtSitePlantReleation mtSitePlantReleation;
        for (String plantCode : distinctPlantCodeList) {
            mtSitePlantReleation = new MtSitePlantReleation();
            mtSitePlantReleation.setPlantCode(plantCode);
            mtSitePlantReleation.setSiteType("MANUFACTURING");
            mtSitePlantReleation.setTenantId(tenantId);

            Criteria criteria = new Criteria(mtSitePlantReleation);
            List<WhereField> whereFields = new ArrayList<WhereField>();
            whereFields.add(new WhereField(MtSitePlantReleation.FIELD_TENANT_ID, Comparison.EQUAL));
            whereFields.add(new WhereField(MtSitePlantReleation.FIELD_PLANT_CODE, Comparison.EQUAL));
            whereFields.add(new WhereField(MtSitePlantReleation.FIELD_SITE_TYPE, Comparison.EQUAL));
            criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

            List<MtSitePlantReleation> sitePlantReleationList =
                    mtSitePlantReleationRepository.selectOptional(mtSitePlantReleation, criteria);

            if (CollectionUtils.isNotEmpty(sitePlantReleationList)) {
                mtSitePlantReleation = sitePlantReleationList.get(0);
                sitePlantMap.put(plantCode, mtSitePlantReleation);
            }
        }

        return sitePlantMap;
    }

    /**
     * 根据工厂物料获取物料站点关系
     * <p>
     * 站点类型指定为manufacturing
     *
     * @param tenantId 租户Id
     * @param plantCode 工厂编码
     * @param itemCodeList 物料编码集合
     * @return Map<String, MtInterFaceVO1>
     * @author benjamin
     * @date 2019-06-27 10:29
     */
    private Map<String, MtSitePlantReleationVO1> getMaterialSite(Long tenantId, String plantCode,
                                                                 List<String> itemCodeList) {
        Map<String, MtSitePlantReleationVO1> codeMaterialMap = new HashMap<>(itemCodeList.size());
        MtSitePlantReleationVO mtInterFaceVO = new MtSitePlantReleationVO();
        mtInterFaceVO.setPlantCodeList(Arrays.asList(plantCode));
        mtInterFaceVO.setItemCodeList(itemCodeList);
        mtInterFaceVO.setSiteType("MANUFACTURING");

        List<MtSitePlantReleationVO1> list =
                mtSitePlantReleationRepository.itemMaterialSiteIdBatchQuery(tenantId, mtInterFaceVO);

        for (MtSitePlantReleationVO1 vo : list) {
            MtSitePlantReleationVO1 rel = new MtSitePlantReleationVO1(vo.getPlantCode(), vo.getItemCode(),
                    vo.getSiteId(), vo.getMaterialId(), vo.getMaterialSiteId());
            codeMaterialMap.put(vo.getItemCode(), rel);
        }

        return codeMaterialMap;
    }

    /**
     * 获取工艺
     *
     * @param tenantId 租户Id
     * @param iface MtRoutingOperationIface
     * @param siteId 站点Id
     * @return MtOperation
     * @author benjamin
     * @date 2019-07-15 11:06
     */
    private MtOperation getOperation(Long tenantId, MtRoutingOperationIface iface, String siteId) {
        MtOperation mtOperation = new MtOperation();
        mtOperation.setOperationName(StringUtils.isEmpty(iface.getStandardOperationCode()) ? "GENERAL"
                : iface.getStandardOperationCode());
        mtOperation.setSiteId(siteId);
        mtOperation.setRevision("MAIN");
        mtOperation.setTenantId(tenantId);

        Criteria criteria = new Criteria();
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtOperation.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtOperation.FIELD_OPERATION_NAME, Comparison.EQUAL));
        whereFields.add(new WhereField(MtOperation.FIELD_SITE_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtOperation.FIELD_REVISION, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

        List<MtOperation> operationList = mtOperationRepository.selectOptional(mtOperation, criteria);
        if (CollectionUtils.isEmpty(operationList)) {
            mtOperation = null;
        } else {
            mtOperation = operationList.get(0);
        }

        return mtOperation;
    }

    /**
     * 构建工艺路线对象
     *
     * @param tenantId 租户Id
     * @param iface MtRoutingOperationIface
     * @return MtRouter
     * @author benjamin
     * @date 2019-07-15 10:48
     */
    private MtRouter constructRouter(Long tenantId, MtRoutingOperationIface iface) {
        MtRouter mtRouter = new MtRouter();
        mtRouter.setRouterName(iface.getRouterCode());
        mtRouter.setRouterType(iface.getRouterObjectType());
        mtRouter.setRevision(StringUtils.isEmpty(iface.getRoutingAlternate()) ? "MAIN" : iface.getRoutingAlternate());
        mtRouter.setTenantId(tenantId);

        Criteria criteria = new Criteria();
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtRouter.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtRouter.FIELD_ROUTER_NAME, Comparison.EQUAL));
        whereFields.add(new WhereField(MtRouter.FIELD_ROUTER_TYPE, Comparison.EQUAL));
        whereFields.add(new WhereField(MtRouter.FIELD_REVISION, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

        List<MtRouter> routerList = mtRouterRepository.selectOptional(mtRouter, criteria);

        mtRouter.setDateFrom(iface.getRouterStartDate());
        mtRouter.setDateTo(iface.getRouterEndDate());
        mtRouter.setDescription(iface.getRouterDescription());
        String routerStatus = iface.getRouterStatus();
        String enableFlag = iface.getEnableFlag();
        routerStatus = (StringUtils.isEmpty(routerStatus) || "ACTIVE".equals(routerStatus))
                && (StringUtils.isEmpty(enableFlag) || "Y".equals(enableFlag)) ? "CAN_RELEASE" : "NEW";
        mtRouter.setRouterStatus(routerStatus);
        if (CollectionUtils.isEmpty(routerList)) {
            mtRouter.setCurrentFlag("Y");
            mtRouter.setTemporaryRouterFlag("N");
            mtRouter.setRelaxedFlowFlag("Y");
            mtRouter.setHasBeenReleasedFlag("Y");
        } else {
            MtRouter router = routerList.get(0);
            mtRouter.setRouterId(router.getRouterId());
            mtRouter.setCurrentFlag(router.getCurrentFlag());
            mtRouter.setOriginalStatus(router.getOriginalStatus());
            mtRouter.setBomId(router.getBomId());
            mtRouter.setTemporaryRouterFlag(router.getTemporaryRouterFlag());
            mtRouter.setRelaxedFlowFlag(router.getRelaxedFlowFlag());
            mtRouter.setHasBeenReleasedFlag(router.getHasBeenReleasedFlag());
            mtRouter.setCopiedFromRouterId(router.getCopiedFromRouterId());
            mtRouter.setDispositionGroupId(router.getDispositionGroupId());
            mtRouter.setAutoRevisionFlag(router.getAutoRevisionFlag());
            mtRouter.setHoldId(router.getHoldId());
            mtRouter.setCid(router.getCid());
            mtRouter.setCreationDate(router.getCreationDate());
            mtRouter.setCreatedBy(router.getCreatedBy());
            mtRouter.setLastUpdateDate(router.getLastUpdateDate());
            mtRouter.setLastUpdatedBy(router.getLastUpdatedBy());
            mtRouter.setObjectVersionNumber(router.getObjectVersionNumber());
        }

        return mtRouter;
    }

    /**
     * 构建工艺路线站点分配对象集合
     *
     * @param tenantId 租户Id
     * @param iface MtRoutingOperationIface
     * @param routerId 工艺路线Id
     * @param sitePlantMap 站点工厂Map
     * @return List<MtRouterSiteAssign>
     * @author benjamin
     * @date 2019-07-23 17:32
     */
    private MtRouterSiteAssign constructRouterSiteAssign(Long tenantId, MtRoutingOperationIface iface, String routerId,
                                                         Map<String, MtSitePlantReleation> sitePlantMap) {
        if (sitePlantMap == null || sitePlantMap.get(iface.getPlantCode()) == null
                || StringUtils.isEmpty(sitePlantMap.get(iface.getPlantCode()).getSiteId())) {
            throw new MtException("MT_INTERFACE_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INTERFACE_0012", "INTERFACE", iface.getIfaceId(), "【API:routerInterfaceImport】"));
        }
        String siteId = sitePlantMap.get(iface.getPlantCode()).getSiteId();

        boolean routerEndDateCheck = iface.getRouterEndDate() == null;
        boolean routerStatusCheck =
                StringUtils.isEmpty(iface.getRouterStatus()) || "ACTIVE".equals(iface.getRouterStatus());
        boolean enableFlagCheck = StringUtils.isEmpty(iface.getEnableFlag()) || "Y".equals(iface.getEnableFlag());
        String enableFlag = routerEndDateCheck && routerStatusCheck && enableFlagCheck ? "Y" : "N";

        MtRouterSiteAssign mtRouterSiteAssign = new MtRouterSiteAssign();
        mtRouterSiteAssign.setEnableFlag(enableFlag);

        if (StringUtils.isEmpty(routerId)) {
            mtRouterSiteAssign.setSiteId(siteId);
        } else {
            MtRouterSiteAssign queryRouterSiteAssign = new MtRouterSiteAssign();
            queryRouterSiteAssign.setRouterId(routerId);
            queryRouterSiteAssign.setSiteId(siteId);
            queryRouterSiteAssign.setTenantId(tenantId);

            Criteria criteria = new Criteria();
            List<WhereField> whereFields = new ArrayList<WhereField>();
            whereFields.add(new WhereField(MtRouterSiteAssign.FIELD_TENANT_ID, Comparison.EQUAL));
            whereFields.add(new WhereField(MtRouterSiteAssign.FIELD_ROUTER_ID, Comparison.EQUAL));
            whereFields.add(new WhereField(MtRouterSiteAssign.FIELD_SITE_ID, Comparison.EQUAL));
            criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
            List<MtRouterSiteAssign> routerSiteAssignList =
                    mtRouterSiteAssignRepository.selectOptional(queryRouterSiteAssign, criteria);

            mtRouterSiteAssign.setRouterId(routerId);
            mtRouterSiteAssign.setSiteId(siteId);
            if (CollectionUtils.isNotEmpty(routerSiteAssignList)) {
                MtRouterSiteAssign routerSiteAssign = routerSiteAssignList.get(0);
                mtRouterSiteAssign.setRouterSiteAssignId(routerSiteAssign.getRouterSiteAssignId());
                mtRouterSiteAssign.setCid(routerSiteAssign.getCid());
                mtRouterSiteAssign.setCreationDate(routerSiteAssign.getCreationDate());
                mtRouterSiteAssign.setCreatedBy(routerSiteAssign.getCreatedBy());
                mtRouterSiteAssign.setLastUpdateDate(routerSiteAssign.getLastUpdateDate());
                mtRouterSiteAssign.setLastUpdatedBy(routerSiteAssign.getLastUpdatedBy());
                mtRouterSiteAssign.setObjectVersionNumber(routerSiteAssign.getObjectVersionNumber());
            }
        }

        return mtRouterSiteAssign;
    }

    /**
     * 构建工艺路线步骤对象
     *
     * @param iface MtRoutingOperationIface
     * @return MtRouterStep
     * @author benjamin
     * @date 2019-07-15 10:57
     */
    private MtRouterStep constructRouterStep(Long tenantId, MtRoutingOperationIface iface) {
        MtRouterStep mtRouterStep = new MtRouterStep();
        mtRouterStep.setRouterStepId(iface.getOperationSeqNum());
        mtRouterStep.setStepName(iface.getOperationSeqNum());
        mtRouterStep.setSequence(Long.valueOf(iface.getOperationSeqNum()));
        mtRouterStep.setDescription(iface.getOperationDescription());
        mtRouterStep.setRouterStepType("OPERATION");
        mtRouterStep.setQueueDecisionType("NEXT");
        mtRouterStep.setTenantId(tenantId);

        return mtRouterStep;
    }

    /**
     * 构建工艺路线步骤关系集合
     *
     * @param routerStepList RouterStepList
     * @return List
     * @author benjamin
     * @date 2019-07-15 15:38
     */
    private List<MtRouterStepVO> constructRouterStepRelation(List<MtRouterStepVO> routerStepList) {
        List<MtRouterStepVO> stepList = new ArrayList<>();

        routerStepList.forEach(s -> {
            if (StringUtils.isEmpty(s.getProcessFlag())) {
                s.setProcessFlag("update");
            }
        });
        List<MtRouterStepVO> saveStepList = routerStepList.stream().filter(s -> !"delete".equals(s.getProcessFlag()))
                .collect(Collectors.toList());
        List<MtRouterStepVO> removeStepList = routerStepList.stream().filter(s -> "delete".equals(s.getProcessFlag()))
                .collect(Collectors.toList());

        log.info("<==============saveStepList begin====================>");
        for (MtRouterStepVO item : saveStepList
             ) {
            log.info("<==============item.getRouterStep().getRouterStepId()====================>:" + item.getRouterStep().getRouterStepId());
        }
        log.info("<==============saveStepList end====================>");

        log.info("<==============removeStepList begin====================>");
        for (MtRouterStepVO item : removeStepList
        ) {
            log.info("<==============item.getRouterStep().getRouterStepId()====================>:" + item.getRouterStep().getRouterStepId());
        }
        log.info("<==============removeStepList end====================>");

        // sort by step name
        List<MtRouterStepVO> sortList = saveStepList.stream()
                .sorted(Comparator.comparingInt(r -> Integer.parseInt(r.getRouterStep().getStepName())))
                .collect(Collectors.toList());

        for (int i = 0; i < sortList.size(); i++) {
            // first step, set entry step flag
            if (i == 0) {
                sortList.get(i).getRouterStep().setEntryStepFlag("Y");
            } else {
                sortList.get(i).getRouterStep().setEntryStepFlag("N");
            }
            if (i < sortList.size() - 1) {
                MtRouterNextStep nextStep = new MtRouterNextStep();
                nextStep.setNextStepId(StringUtils.isEmpty(sortList.get(i + 1).getRouterStep().getRouterStepId())
                        ? sortList.get(i + 1).getRouterStep().getStepName()
                        : sortList.get(i + 1).getRouterStep().getRouterStepId());
                nextStep.setNextDecisionType("MAIN");

                sortList.get(i).setRouterNextSteps(Collections.singletonList(nextStep));
                sortList.get(i).setRouterDoneStep(null);
            } else {
                // tail of step list, set done step
                MtRouterDoneStep doneStep = new MtRouterDoneStep();

                sortList.get(i).setRouterDoneStep(doneStep);
                sortList.get(i).setRouterNextSteps(null);
            }
        }

        stepList.addAll(sortList);
        stepList.addAll(removeStepList);

        return stepList;
    }

    /**
     * 构建返回消息
     *
     * @param mtRoutingOperationIface MtRoutingOperationIface
     * @param message 错误消息
     * @return MtBomComponentIface
     * @author benjamin
     * @date 2019-06-27 17:02
     */
    private MtRoutingOperationIface constructIfaceMessage(Long tenantId,
                                                          MtRoutingOperationIface mtRoutingOperationIface, String status, String message) {
        Date date = new Date(System.currentTimeMillis());
        Long userId = Long.valueOf(-1L);

        mtRoutingOperationIface.setTenantId(tenantId);
        mtRoutingOperationIface.setStatus(status);
        mtRoutingOperationIface.setMessage(message);
        mtRoutingOperationIface.setLastUpdateDate(date);
        mtRoutingOperationIface.setLastUpdatedBy(userId);

        return mtRoutingOperationIface;
    }

    /**
     * 更新物料生产属性对象
     *
     * @param tenantId 租户Id
     * @param routingId 工艺路线Id
     * @param mtRoutingOperationIface MtRoutingOperationIface
     * @param assemblyMaterialMap 装配件物料对应站点关系集合
     * @author benjamin
     * @date 2019-06-27 16:51
     */
    private void updatePfepManufacturing(Long tenantId, String routingId,
                                         MtRoutingOperationIface mtRoutingOperationIface,
                                         Map<String, MtSitePlantReleationVO1> assemblyMaterialMap) {
        MtPfepManufacturing mtPfepManufacturing = new MtPfepManufacturing();
        mtPfepManufacturing.setMaterialSiteId(
                assemblyMaterialMap.get(mtRoutingOperationIface.getRouterObjectCode()).getMaterialSiteId());
        mtPfepManufacturing.setOrganizationId("");
        mtPfepManufacturing.setOrganizationType("");
        mtPfepManufacturing.setTenantId(tenantId);

        Criteria criteria = new Criteria(mtPfepManufacturing);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtPfepManufacturing.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtPfepManufacturing.FIELD_MATERIAL_SITE_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtPfepManufacturing.FIELD_ORGANIZATION_TYPE, Comparison.EQUAL));
        whereFields.add(new WhereField(MtPfepManufacturing.FIELD_ORGANIZATION_ID, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

        List<MtPfepManufacturing> pfepManufacturingList =
                mtPfepManufacturingRepository.selectOptional(mtPfepManufacturing, criteria);

        if (CollectionUtils.isEmpty(pfepManufacturingList)) {
            mtPfepManufacturing.setDefaultRoutingId(routingId);
            mtPfepManufacturing.setEnableFlag("Y");
            mtPfepManufacturing.setTenantId(tenantId);
            mtPfepManufacturingRepository.insertSelective(mtPfepManufacturing);
        } else {
            mtPfepManufacturing.setPfepManufacturingId(pfepManufacturingList.get(0).getPfepManufacturingId());
            mtPfepManufacturing.setDefaultRoutingId(routingId);
            mtPfepManufacturing.setTenantId(tenantId);
            mtPfepManufacturingRepository.updateByPrimaryKeySelective(mtPfepManufacturing);
        }
    }

    /**
     * 更新Router和工单关系
     *
     * @author benjamin
     * @date 2019-08-27 16:01
     * @param tenantId 租户Id
     * @param routerId 工艺路线Id
     * @param eventId 事件Id
     * @param mtRoutingOperationIface MtRoutingOperationIface
     */
    private void updateWoRel(Long tenantId, String routerId, String eventId, String workOrderId,
                             MtRoutingOperationIface mtRoutingOperationIface) {
//        log.info("tenantId：============================================================>{}" , tenantId);
//        log.info("mtRoutingOperationIface.getRouterObjectCode()：============================================================>{}" , mtRoutingOperationIface.getRouterObjectCode());
//        String workOrderId =
//                        mtWorkOrderRepository.numberLimitWoGet(tenantId, mtRoutingOperationIface.getRouterObjectCode());
//        log.info("workOrderId：============================================================>{}" , workOrderId);
//        log.info("DetailsHelper.getUserDetails().getUserId()：============================================================>{}" , DetailsHelper.getUserDetails().getUserId());

//        if (StringUtils.isEmpty(workOrderId)) {
//            throw new MtException("MT_INTERFACE_0022",
//                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0022",
//                                            "INTERFACE", mtRoutingOperationIface.getIfaceId(),
//                                            "【API:routerInterfaceImport】"));
//        }
        mtWorkOrderRepository.woRouterUpdate(tenantId, workOrderId, routerId, eventId);
    }

    @Override
    public void updateIfaceStatus(Long tenantId, List<MtRoutingOperationIface> mtRoutingOperationIfaceList,
                                  String status) {
        if (CollectionUtils.isNotEmpty(mtRoutingOperationIfaceList)) {
            List<String> sqlList = new ArrayList<>(mtRoutingOperationIfaceList.size());
            mtRoutingOperationIfaceList.forEach(ifs -> {
                ifs.setStatus(status);
                ifs.setTenantId(tenantId);
                sqlList.addAll(customDbRepository.getUpdateSql(ifs));
            });

            List<List<String>> splitSqlList = splitSqlList(sqlList);

            for (List<String> sql : splitSqlList) {
                jdbcTemplate.batchUpdate(sql.toArray(new String[sql.size()]));
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void log(Long tenantId, List<MtRoutingOperationIface> mtRoutingOperationIfaceList) {
        if (CollectionUtils.isNotEmpty(mtRoutingOperationIfaceList)) {
            List<String> sqlList = new ArrayList<>(mtRoutingOperationIfaceList.size());
            mtRoutingOperationIfaceList.forEach(ifs -> {
                ifs.setTenantId(tenantId);
                sqlList.addAll(customDbRepository.getUpdateSql(ifs));
            });

            List<List<String>> splitSqlList = splitSqlList(sqlList);

            for (List<String> sql : splitSqlList) {
                jdbcTemplate.batchUpdate(sql.toArray(new String[sql.size()]));
            }
        }
    }

    /**
     * 分割数据集合 限制数量每项不多于SQL_ITEM_COUNT_LIMIT
     *
     * @param sqlList Sql数据集合
     * @return List<List>
     * @author benjamin
     * @date 2019-06-25 18:40
     */
    private List<List<String>> splitSqlList(List<String> sqlList) {
        List<List<String>> returnList = new ArrayList<>();

        if (sqlList.size() <= SQL_ITEM_COUNT_LIMIT) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / SQL_ITEM_COUNT_LIMIT;
            int splitRest = sqlList.size() % SQL_ITEM_COUNT_LIMIT;

            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * SQL_ITEM_COUNT_LIMIT, (i + 1) * SQL_ITEM_COUNT_LIMIT));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * SQL_ITEM_COUNT_LIMIT, sqlList.size()));
            }
        }

        return returnList;
    }

    public static class Tuple {
        private String routerCode;
        private String plantCode;
        private String routerObjectType;
        private String routingAlternate;

        Tuple(String routerCode, String plantCode, String routerObjectType, String routingAlternate) {
            this.routerCode = routerCode;
            this.plantCode = plantCode;
            this.routerObjectType = routerObjectType;
            this.routingAlternate = routingAlternate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple tuple = (Tuple) o;
            return Objects.equals(routerCode, tuple.routerCode) && Objects.equals(plantCode, tuple.plantCode)
                    && Objects.equals(routerObjectType, tuple.routerObjectType)
                    && Objects.equals(routingAlternate, tuple.routingAlternate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(routerCode, plantCode, routerObjectType, routingAlternate);
        }
    }

}