package tarzan.actual.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtEoRouterActual;
import tarzan.actual.domain.entity.MtEoStepActual;
import tarzan.actual.domain.entity.MtEoStepWorkcellActual;
import tarzan.actual.domain.entity.MtEoStepWorkcellActualHis;
import tarzan.actual.domain.repository.MtEoRouterActualRepository;
import tarzan.actual.domain.repository.MtEoStepActualRepository;
import tarzan.actual.domain.repository.MtEoStepWorkcellActualHisRepository;
import tarzan.actual.domain.repository.MtEoStepWorkcellActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtEoStepWorkcellActualHisMapper;
import tarzan.general.domain.entity.MtEvent;
import tarzan.general.domain.entity.MtEventType;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventTypeRepository;
import tarzan.general.domain.vo.MtEventVO1;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.method.domain.repository.MtRouterRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

/**
 * 执行作业-工艺路线步骤执行明细历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtEoStepWorkcellActualHisRepositoryImpl extends BaseRepositoryImpl<MtEoStepWorkcellActualHis>
        implements MtEoStepWorkcellActualHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoStepWorkcellActualHisMapper mtEoStepWorkcellActualHisMapper;

    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private MtEoStepWorkcellActualRepository mtEoStepWorkcellActualRepository;

    @Autowired
    private MtEoStepWorkcellActualHisRepository mtEoStepWorkcellActualHisRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtEventTypeRepository mtEventTypeRepository;


    @Override
    public List<MtEoStepWorkcellActualHis> eoWkcActualHisByEventQuery(Long tenantId, String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MOVING_0001", "MOVING", "eventId", "【API:eoWkcActualHisByEventQuery】"));
        }

        MtEoStepWorkcellActualHis mtEoStepWorkcellActualHis = new MtEoStepWorkcellActualHis();
        mtEoStepWorkcellActualHis.setTenantId(tenantId);
        mtEoStepWorkcellActualHis.setEventId(eventId);
        List<MtEoStepWorkcellActualHis> mtEoStepWorkcellActualHiz =
                this.mtEoStepWorkcellActualHisMapper.select(mtEoStepWorkcellActualHis);
        if (CollectionUtils.isEmpty(mtEoStepWorkcellActualHiz)) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MOVING_0015", "MOVING", "eventId", "【API:eoWkcActualHisByEventQuery】"));
        }

        return mtEoStepWorkcellActualHiz;
    }

    @Override
    public List<MtEoStepWorkcellActualHis> eoWkcActualHisQuery(Long tenantId, MtEoStepWorkcellActualHisVO condition) {
        if (StringUtils.isEmpty(condition.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoWkcActualHisQuery】"));
        }

        MtEoStepWorkcellActualHis mtEoStepWorkcellActualHis = new MtEoStepWorkcellActualHis();
        mtEoStepWorkcellActualHis.setTenantId(tenantId);
        mtEoStepWorkcellActualHis.setEoStepActualId(condition.getEoStepActualId());
        if (null != condition.getWorkcellId()) {
            mtEoStepWorkcellActualHis.setWorkcellId(condition.getWorkcellId());
        }
        List<MtEoStepWorkcellActualHis> mtEoStepWorkcellActualHiz =
                this.mtEoStepWorkcellActualHisMapper.select(mtEoStepWorkcellActualHis);
        if (CollectionUtils.isEmpty(mtEoStepWorkcellActualHiz)) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MOVING_0015", "MOVING", "{eoStepActualId,workcellId}", "【API:eoWkcActualHisQuery】"));
        }
        return mtEoStepWorkcellActualHiz;
    }

    @Override
    public List<MtEoStepWorkcellActualVO10> eoAndOperationLimitWkcStepActualHisQuery(Long tenantId,
                                                                                     MtEoStepWorkcellActualVO9 vo) {
        List<MtEoStepWorkcellActualVO10> resultList = new ArrayList<>();
        String operationId = vo.getOperationId();
        String workcellId = vo.getWorkcellId();

        // 第一步，获取执行作业工艺路线实绩，若eoIdList有输入
        // a) 在表MT_RO_ROUTER_ACTUAL中限定：EO_ID=输入参数eoId
        List<MtEoStepWorkcellActualVO11> eoIdAndTime = vo.getEoIdAndTime();
        if (CollectionUtils.isEmpty(eoIdAndTime)) {
            return resultList;
        }

        // 获取输入的eoId列表
        List<String> eoIdList =
                eoIdAndTime.stream().map(MtEoStepWorkcellActualVO11::getEoId).collect(Collectors.toList());

        Map<String, MtEoStepWorkcellActualVO11> eoIdMap =
                eoIdAndTime.stream().collect(Collectors.toMap(t -> t.getEoId(), t -> t));

        List<MtEoRouterActual> mtEoRouterActualList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(eoIdList)) {
            mtEoRouterActualList =
                    mtEoRouterActualRepository.selectByCondition(Condition.builder(MtEoRouterActual.class)
                            .andWhere(Sqls.custom()
                                    .andEqualTo(MtEoRouterActual.FIELD_TENANT_ID, tenantId)
                                    .andIn(MtEoRouterActual.FIELD_EO_ID, eoIdList))
                            .build());
        }

        // 主体查不到数据直接返回
        if (CollectionUtils.isEmpty(mtEoRouterActualList)) {
            return Collections.emptyList();
        }

        // 组装返回的一部分数据
        for (MtEoRouterActual actual : mtEoRouterActualList) {
            MtEoStepWorkcellActualVO10 vo10 = new MtEoStepWorkcellActualVO10();
            vo10.setEoId(actual.getEoId());
            vo10.setEoRouterActualId(actual.getEoRouterActualId());
            vo10.setSequence(actual.getSequence());
            vo10.setStatus(actual.getStatus());
            vo10.setRouterId(actual.getRouterId());
            vo10.setQty(actual.getQty());
            vo10.setSubRouterFlag(actual.getSubRouterFlag());
            vo10.setSourceEoStepActualId(actual.getEoRouterActualId());
            vo10.setCompletedQty(actual.getCompletedQty());
            resultList.add(vo10);
        }
        List<String> eoIds = resultList.stream().map(MtEoStepWorkcellActualVO10::getEoId)
                .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
        List<String> routerIds = resultList.stream().map(MtEoStepWorkcellActualVO10::getRouterId)
                .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());


        // c) 获取执行作业属性，调用API{eoPropertyBatchGet}
        List<MtEo> mtEos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(eoIds)) {
            mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
        }

        Map<String, String> eoNumMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(mtEos)) {
            eoNumMap = mtEos.stream().collect(Collectors.toMap(t -> t.getEoId(), t -> t.getEoNum()));
        }

        // d) 获取工艺路线属性获取工艺路线属性
        List<MtRouter> mtRouters = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(routerIds)) {
            mtRouters = mtRouterRepository.routerBatchGet(tenantId, routerIds);
        }

        Map<String, MtRouter> routerIdMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(mtRouters)) {
            routerIdMap = mtRouters.stream().collect(Collectors.toMap(t -> t.getRouterId(), t -> t));
        }

        for (MtEoStepWorkcellActualVO10 vo10 : resultList) {
            String eoNum = eoNumMap.get(vo10.getEoId());
            MtRouter router = routerIdMap.get(vo10.getRouterId());
            String routerName = null;
            String description = null;
            // Router短描述就是name，RouterName长描述就是description
            if (null != router) {
                routerName = router.getRouterName();
                description = router.getDescription();
            }
            vo10.setEoNum(eoNum);
            vo10.setRouter(routerName);
            vo10.setRouterName(description);
        }


        // 第二步，获取执行作业工作单元实绩
        // a)在表MT_EO_STEP_ACTUAL中限定：EO_ROUTER_ACTUAL_ID=第一步输出参数eoRouterActualId
        // OPERATION_ID=输入参数operationId
        List<String> eoRouterActualIdList = mtEoRouterActualList.stream().map(MtEoRouterActual::getEoRouterActualId)
                .collect(Collectors.toList());

        List<MtEoStepActual> stepActualList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(eoRouterActualIdList)) {
            if (null != operationId) {
                stepActualList = mtEoStepActualRepository.selectByCondition(Condition.builder(MtEoStepActual.class)
                        .andWhere(Sqls.custom().andEqualTo(MtEoStepActual.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtEoStepActual.FIELD_OPERATION_ID, operationId)
                                .andIn(MtEoStepActual.FIELD_EO_ROUTER_ACTUAL_ID, eoRouterActualIdList))
                        .build());
            } else {
                stepActualList = mtEoStepActualRepository.selectByCondition(Condition.builder(MtEoStepActual.class)
                        .andWhere(Sqls.custom().andEqualTo(MtEoStepActual.FIELD_TENANT_ID, tenantId)
                                .andIn(MtEoStepActual.FIELD_EO_ROUTER_ACTUAL_ID, eoRouterActualIdList))
                        .build());
            }
        }

        // 这里没有数据就不用向下查询了，直接返回前面的结果
        if (CollectionUtils.isEmpty(stepActualList)) {
            return resultList;
        }

        // 根据上一步id分组，然后组装数据
        Map<String, List<MtEoStepActual>> eoRouterActualIdMap =
                stepActualList.stream().collect(Collectors.groupingBy(MtEoStepActual::getEoRouterActualId));

        List<String> operationIds = stepActualList.stream().map(MtEoStepActual::getOperationId)
                .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());

        // 获取工艺属性，调用API{operationBatchGet}
        List<MtOperation> mtOperations = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(operationIds)) {
            mtOperations = mtOperationRepository.operationBatchGet(tenantId, operationIds);
        }

        Map<String, String> operationIdMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(mtOperations)) {
            operationIdMap = mtOperations.stream()
                    .collect(Collectors.toMap(t -> t.getOperationId(), t -> t.getOperationName()));
        }

        // 组装第二层数据
        for (MtEoStepWorkcellActualVO10 vo10 : resultList) {
            List<MtEoStepActual> mtEoStepActuals = eoRouterActualIdMap.get(vo10.getEoRouterActualId());
            List<MtEoStepActualVO33> eoStepActualList = new ArrayList<>();

            if (null != mtEoStepActuals) {
                for (MtEoStepActual mtEoStepActual : mtEoStepActuals) {
                    MtEoStepActualVO33 vo33 = new MtEoStepActualVO33();
                    vo33.setEoRouterActualId(mtEoStepActual.getEoRouterActualId());
                    vo33.setEoStepActualId(mtEoStepActual.getEoStepActualId());
                    vo33.setOperationId(mtEoStepActual.getOperationId());
                    vo33.setOperation(operationIdMap.get(mtEoStepActual.getOperationId()));

                    eoStepActualList.add(vo33);
                }
                // 组合
                vo10.setEoStepActualList(eoStepActualList);
            }
        }

        List<String> eoStepActualIdList =
                stepActualList.stream().map(MtEoStepActual::getEoStepActualId).collect(Collectors.toList());

        // b)在表MT_EO_STEP_WORKCELL_ACTUAL中限定
        // EO_STEP_ACTUAL_ID=第二步a）输出参数eoStepActualId
        List<MtEoStepWorkcellActual> eoStepWorkcellActualList;
        if (StringUtils.isNotEmpty(workcellId)) {
            eoStepWorkcellActualList = mtEoStepWorkcellActualRepository.selectByCondition(Condition
                    .builder(MtEoStepWorkcellActual.class)
                    .andWhere(Sqls.custom().andEqualTo(MtEoStepWorkcellActual.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtEoStepWorkcellActual.FIELD_WORKCELL_ID, workcellId)
                            .andIn(MtEoStepWorkcellActual.FIELD_EO_STEP_ACTUAL_ID, eoStepActualIdList))
                    .build());
        } else {
            eoStepWorkcellActualList = mtEoStepWorkcellActualRepository.selectByCondition(Condition
                    .builder(MtEoStepWorkcellActual.class)
                    .andWhere(Sqls.custom().andEqualTo(MtEoStepWorkcellActual.FIELD_TENANT_ID, tenantId)
                            .andIn(MtEoStepWorkcellActual.FIELD_EO_STEP_ACTUAL_ID, eoStepActualIdList))
                    .build());
        }


        // 这里没有数据就不用向下查询了，直接返回前面的结果
        if (CollectionUtils.isEmpty(eoStepWorkcellActualList)) {
            return resultList;
        }

        Map<String, List<MtEoStepWorkcellActual>> eoStepWorkcellActualMap = eoStepWorkcellActualList.stream()
                .collect(Collectors.groupingBy(MtEoStepWorkcellActual::getEoStepActualId));

        // 组装最里面的历史记录
        for (MtEoStepWorkcellActualVO10 vo10 : resultList) {
            List<MtEoStepActualVO33> eoStepActualList = vo10.getEoStepActualList();
            for (MtEoStepActualVO33 vo33 : eoStepActualList) {
                List<MtEoStepWorkcellActualVO12> vo12List = new ArrayList<>();
                List<MtEoStepWorkcellActual> eoStepWorkcellActuals =
                        eoStepWorkcellActualMap.get(vo33.getEoStepActualId());
                if (CollectionUtils.isNotEmpty(eoStepWorkcellActuals)) {
                    for (MtEoStepWorkcellActual actual : eoStepWorkcellActuals) {
                        MtEoStepWorkcellActualVO12 vo12 = new MtEoStepWorkcellActualVO12();
                        vo12.setEoStepWorkcellActual(actual.getEoStepWorkcellActualId());
                        vo12.setEoStepActualId(actual.getEoStepActualId());
                        vo12.setQueueQty(actual.getQueueQty());
                        vo12.setWorkcellQty(actual.getWorkingQty());
                        vo12.setCompletePendingQty(actual.getCompletePendingQty());
                        vo12.setCompletedQty(actual.getCompletedQty());
                        vo12.setScrappedQty(actual.getScrappedQty());
                        vo12.setQueueDate(actual.getQueueDate());
                        vo12.setWorkingDate(actual.getWorkingDate());
                        vo12.setCompletePendingDate(actual.getCompletePendingDate());
                        vo12.setCompletedDate(actual.getCompletedDate());
                        vo12.setWorkcellId(actual.getWorkcellId());

                        vo12List.add(vo12);
                    }
                    // 設置值
                    vo33.setEoStepWorkcellActualList(vo12List);
                }
            }
        }

        // 构建下面查询条件输入参数
        List<String> eoStepWorkcellActualIdList = eoStepWorkcellActualList.stream()
                .map(MtEoStepWorkcellActual::getEoStepWorkcellActualId).collect(Collectors.toList());


        // c)在表MT_EO_STEP_WORKCELL_ACTUAL_HIS中限定
        // EO_STEP_WORKCELL_ACTUAL_ID=第二步b）输出参数eoStepWorkcellActualId
        List<MtEoStepWorkcellActualHis> mtEoStepWorkcellActualHis = new ArrayList<>();
        mtEoStepWorkcellActualHis = mtEoStepWorkcellActualHisRepository.selectByCondition(Condition
                .builder(MtEoStepWorkcellActualHis.class)
                .andWhere(Sqls.custom().andEqualTo(MtEoStepWorkcellActualHis.FIELD_TENANT_ID, tenantId).andIn(
                        MtEoStepWorkcellActualHis.FIELD_EO_STEP_WORKCELL_ACTUAL_ID,
                        eoStepWorkcellActualIdList))
                .build());


        Map<String, List<MtEoStepWorkcellActualHis>> eoStepWorkcellActualIdMap = mtEoStepWorkcellActualHis.stream()
                .collect(Collectors.groupingBy(MtEoStepWorkcellActualHis::getEoStepWorkcellActualId));

        // 这里根据事件id查询创建事件和创建人
        List<String> eventIdList = mtEoStepWorkcellActualHis.stream().map(MtEoStepWorkcellActualHis::getEventId)
                .collect(Collectors.toList());

        List<MtEventVO1> eventVO1s = CollectionUtils.isEmpty(eventIdList) ? Collections.emptyList()
                : mtEventRepository.eventBatchGet(tenantId, eventIdList);

        Map<String, MtEventVO1> eventVO1Map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eventVO1s)) {
            eventVO1Map = eventVO1s.stream().collect(Collectors.toMap(t -> t.getEventId(), t -> t));
        }

        // 组装最里面的历史记录
        for (MtEoStepWorkcellActualVO10 vo10 : resultList) {
            List<MtEoStepActualVO33> eoStepActualList = vo10.getEoStepActualList();
            for (MtEoStepActualVO33 vo33 : eoStepActualList) {
                List<MtEoStepWorkcellActualVO12> eoStepWorkcellActuals = vo33.getEoStepWorkcellActualList();

                // 设置MtEoStepWorkcellActual实体中历史记录
                for (MtEoStepWorkcellActualVO12 workcellActual : eoStepWorkcellActuals) {
                    List<MtEoStepWorkcellActualHisVO2> eoStepWorkcellActualHisList = new ArrayList<>();
                    List<MtEoStepWorkcellActualHis> eoStepWorkcellActualHis =
                            eoStepWorkcellActualIdMap.get(workcellActual.getEoStepWorkcellActual());

                    if (StringUtils.isNotEmpty(workcellId) && CollectionUtils.isNotEmpty(eoStepWorkcellActualHis)) {
                        eoStepWorkcellActualHis = eoStepWorkcellActualHis.stream()
                                .filter(t -> workcellId.equals(t.getWorkcellId())).collect(Collectors.toList());
                    }
                    if (null != eoStepWorkcellActualHis) {
                        for (MtEoStepWorkcellActualHis his : eoStepWorkcellActualHis) {
                            MtEventVO1 mtEventVO1 = eventVO1Map.get(his.getEventId());

                            MtEoStepWorkcellActualVO11 mtEoStepWorkcellActualVO11 = eoIdMap.get(vo10.getEoId());
                            Date eventTimeFrom = mtEoStepWorkcellActualVO11.getEventTimeFrom();
                            Date eventTimeTo = mtEoStepWorkcellActualVO11.getEventTimeTo();

                            // 筛选eventTime在输入参数eventTimeFrom与eventTimeTo区间内的数据输出

                            if (null != mtEventVO1) {
                                Date eventTime = mtEventVO1.getEventTime();
                                if (null != eventTime) {
                                    if (null != eventTimeFrom && null != eventTimeTo
                                            && eventTime.compareTo(eventTimeTo) <= 0
                                            && eventTime.compareTo(eventTimeFrom) >= 0) {
                                        eoStepWorkcellActualHisList.add(setProperty(his, mtEventVO1));
                                    }
                                    if (null != eventTimeFrom && null == eventTimeTo
                                            && eventTime.compareTo(eventTimeFrom) >= 0) {
                                        eoStepWorkcellActualHisList.add(setProperty(his, mtEventVO1));
                                    }
                                    if (null == eventTimeFrom && null != eventTimeTo
                                            && eventTime.compareTo(eventTimeTo) <= 0) {
                                        eoStepWorkcellActualHisList.add(setProperty(his, mtEventVO1));
                                    }
                                }

                                if (null == eventTimeFrom && null == eventTimeTo) {
                                    eoStepWorkcellActualHisList.add(setProperty(his, mtEventVO1));
                                }
                            } else {
                                // 这里初始化是为了防止pmd检查时 mtEventVO1为空的异常
                                mtEventVO1 = new MtEventVO1();
                                eoStepWorkcellActualHisList.add(setProperty(his, mtEventVO1));
                            }
                        }
                        // 赋值
                        workcellActual.setEoStepWorkcellActualHisList(eoStepWorkcellActualHisList);
                    }
                }


            }
        }

        for (MtEoStepWorkcellActualVO10 vo10 : resultList) {
            List<MtEoStepActualVO33> eoStepActualList = vo10.getEoStepActualList();
            for (MtEoStepActualVO33 stepActualVO33 : eoStepActualList) {
                List<MtEoStepWorkcellActualVO12> workcellActualLists = stepActualVO33.getEoStepWorkcellActualList();
                if (CollectionUtils.isEmpty(workcellActualLists)) {
                    continue;
                }

                for (MtEoStepWorkcellActualVO12 actualList : workcellActualLists) {
                    // e)获取工作单元属性，调用API{workcellBasicPropertyBatchGet}
                    // 取出工艺路线步骤历史数据
                    List<MtEoStepWorkcellActualHisVO2> eoStepWorkcellActualHisList =
                            actualList.getEoStepWorkcellActualHisList();
                    List<String> workcellIds = eoStepWorkcellActualHisList.stream()
                            .map(MtEoStepWorkcellActualHisVO2::getWorkcellId)
                            .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
                    List<MtModWorkcell> mtModWorkcells = CollectionUtils.isEmpty(workcellIds) ? Collections.emptyList()
                            : mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workcellIds);

                    Map<String, MtModWorkcell> workcellMap = new HashMap<>();
                    if (CollectionUtils.isNotEmpty(mtModWorkcells)) {
                        workcellMap = mtModWorkcells.stream().collect(Collectors.toMap(t -> t.getWorkcellId(), t -> t));
                    }

                    // 取出工艺路线步骤数据
                    List<MtModWorkcell> mtModWorkcells1 = mtModWorkcellRepository
                            .workcellBasicPropertyBatchGet(tenantId, Arrays.asList(actualList.getWorkcellId()));

                    if (CollectionUtils.isNotEmpty(mtModWorkcells1)) {
                        MtModWorkcell mtModWorkcell = mtModWorkcells1.get(0);
                        if (null != mtModWorkcell) {
                            actualList.setWorkcellName(mtModWorkcell.getWorkcellName());
                            actualList.setWorkcellCode(mtModWorkcell.getWorkcellCode());
                        }
                    }


                    // f)获取事件属性，调用API{eventBatchGet}传入参数
                    List<String> eventIds = eoStepWorkcellActualHisList.stream()
                            .map(MtEoStepWorkcellActualHisVO2::getEventId)
                            .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
                    List<MtEventVO1> eventList = CollectionUtils.isEmpty(eventIds) ? Collections.emptyList()
                            : mtEventRepository.eventBatchGet(tenantId, eventIds);

                    List<String> eventTypeIds = new ArrayList<>();
                    Map<String, MtEventVO1> mtEventVO1Map = new HashMap<>();
                    if (CollectionUtils.isNotEmpty(eventList)) {
                        mtEventVO1Map = eventList.stream().collect(Collectors.toMap(t -> t.getEventId(), t -> t));
                        eventTypeIds = eventList.stream().map(MtEvent::getEventTypeId)
                                .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());

                    }

                    List<MtEventType> mtEventTypes = CollectionUtils.isEmpty(eventTypeIds) ? Collections.emptyList()
                            : mtEventTypeRepository.eventTypeBatchGet(tenantId, eventTypeIds);
                    Map<String, String> eventCodeMap = new HashMap<>();
                    if (CollectionUtils.isNotEmpty(mtEventTypes)) {
                        eventCodeMap = mtEventTypes.stream()
                                .collect(Collectors.toMap(t -> t.getEventTypeId(), t -> t.getEventTypeCode()));
                    }

                    for (MtEoStepWorkcellActualHisVO2 hisVO2 : eoStepWorkcellActualHisList) {
                        MtModWorkcell mtModWorkcell = workcellMap.get(hisVO2.getWorkcellId());
                        MtEventVO1 mtEventVO1 = mtEventVO1Map.get(hisVO2.getEventId());
                        if (null != mtEventVO1) {
                            String eventTypeCode = eventCodeMap.get(mtEventVO1.getEventTypeId());
                            hisVO2.setEventTypeCode(eventTypeCode);
                        }

                        if (null != mtModWorkcell) {
                            hisVO2.setWorkcellName(mtModWorkcell.getWorkcellName());
                            hisVO2.setWorkcellCode(mtModWorkcell.getWorkcellCode());
                        }
                    }

                }
            }
        }

        return resultList;
    }

    /**
     * 赋值操作
     *
     * @Author peng.yuan
     * @Date 2019/12/4 18:47
     * @param his :
     * @param mtEventVO1 :
     * @return tarzan.actual.domain.vo.MtEoStepWorkcellActualHisVO2
     */
    private MtEoStepWorkcellActualHisVO2 setProperty(MtEoStepWorkcellActualHis his, MtEventVO1 mtEventVO1) {
        // 输入不为空则筛选workcellId等于输入参数workcellId的数据输出
        MtEoStepWorkcellActualHisVO2 vo2 = new MtEoStepWorkcellActualHisVO2();
        vo2.setEoStepWorkcellActualHisId(his.getEoStepWorkcellActualHisId());
        vo2.setEoStepWorkcellActualId(his.getEoStepWorkcellActualId());
        vo2.setEoStepActualId(his.getEoStepActualId());
        vo2.setQueueQty(his.getQueueQty());
        vo2.setWorkingQty(his.getWorkingQty());
        vo2.setCompletePendingQty(his.getCompletePendingQty());
        vo2.setScrappedQty(his.getScrappedQty());
        vo2.setQueueDate(his.getQueueDate());
        vo2.setWorkingDate(his.getWorkingDate());
        vo2.setCompletePendingDate(his.getCompletePendingDate());
        vo2.setCompletedDate(his.getCompletedDate());
        vo2.setWorkcellId(his.getWorkcellId());
        vo2.setEventId(his.getEventId());
        vo2.setEventBy(mtEventVO1.getEventBy());
        vo2.setEventTime(mtEventVO1.getEventTime());
        vo2.setTrxQueueQty(his.getTrxQueueQty());
        vo2.setTrxWorkingQty(his.getTrxWorkingQty());
        vo2.setTrxCompletedQty(his.getTrxCompletedQty());
        vo2.setTrxScrappedQty(his.getTrxScrappedQty());
        vo2.setTrxCompletePendingQty(his.getTrxCompletePendingQty());
        return vo2;
    }
}
