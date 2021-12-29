package tarzan.dispatch.infra.repository.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.MtBaseConstants;
import tarzan.actual.domain.entity.MtEoStepActual;
import tarzan.actual.domain.entity.MtEoStepWip;
import tarzan.actual.domain.entity.MtEoStepWorkcellActual;
import tarzan.actual.domain.repository.MtEoStepActualRepository;
import tarzan.actual.domain.repository.MtEoStepWipRepository;
import tarzan.actual.domain.repository.MtEoStepWorkcellActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.calendar.domain.vo.MtCalendarShiftVO3;
import tarzan.calendar.domain.vo.MtCalendarShiftVO4;
import tarzan.dispatch.domain.entity.MtEoDispatchAction;
import tarzan.dispatch.domain.entity.MtEoDispatchHistory;
import tarzan.dispatch.domain.entity.MtEoDispatchProcess;
import tarzan.dispatch.domain.repository.MtDispatchStrategyOrgRelRepository;
import tarzan.dispatch.domain.repository.MtEoDispatchActionRepository;
import tarzan.dispatch.domain.repository.MtEoDispatchHistoryRepository;
import tarzan.dispatch.domain.repository.MtEoDispatchProcessRepository;
import tarzan.dispatch.domain.vo.*;
import tarzan.dispatch.infra.mapper.MtEoDispatchActionMapper;
import tarzan.dispatch.infra.mapper.MtEoDispatchProcessMapper;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.method.domain.repository.MtRouterRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationRelVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

/**
 * 调度结果执行表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:54:56
 */
@Component
public class MtEoDispatchActionRepositoryImpl extends BaseRepositoryImpl<MtEoDispatchAction>
                implements MtEoDispatchActionRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtCalendarShiftRepository mtCalendarShiftRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtEoDispatchActionRepository mtEoDispatchActionRepository;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private MtEoStepWipRepository mtEoStepWipRepository;

    @Autowired
    private MtEoStepWorkcellActualRepository mtEoStepWorkcellActualRepository;

    @Autowired
    private MtEoDispatchHistoryRepository mtEoDispatchHistoryRepository;

    @Autowired
    private MtEoDispatchProcessRepository mtEoDispatchProcessRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtDispatchStrategyOrgRelRepository mtDispatchStrategyOrgRelRepository;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    private MtEoDispatchActionMapper mtEoDispatchActionMapper;

    @Autowired
    private MtEoDispatchProcessMapper mtEoDispatchProcessMapper;
    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Override
    public List<MtEoDispatchAction> wkcShiftLimitDispatchedPublishedEoQuery(Long tenantId, MtEoDispatchActionVO1 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkcellId()) && StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0002",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0002", "DISPATCH",
                                            "workcellId、operationId", "【API：wkcShiftLimitDispatchedPublishedEoQuery】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "shiftCode", "【API：wkcShiftLimitDispatchedPublishedEoQuery】"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "shiftDate", "【API：wkcShiftLimitDispatchedPublishedEoQuery】"));
        }
        if (StringUtils.isNotEmpty(dto.getSiteId()) && StringUtils.isEmpty(dto.getProductionLineId())
                        || StringUtils.isNotEmpty(dto.getProductionLineId()) && StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_DISPATCH_0021",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0021", "DISPATCH",
                                            "productionLineId、siteId",
                                            "【API：wkcShiftLimitDispatchedPublishedEoQuery】"));
        }

        // 2. 根据输入工作单元和班次信息获取调度结果表
        MtEoDispatchAction mtEoDispatchAction = new MtEoDispatchAction();
        mtEoDispatchAction.setTenantId(tenantId);
        mtEoDispatchAction.setWorkcellId(dto.getWorkcellId());
        mtEoDispatchAction.setShiftCode(dto.getShiftCode());
        mtEoDispatchAction.setShiftDate(dto.getShiftDate());
        mtEoDispatchAction.setOperationId(dto.getOperationId());
        List<MtEoDispatchAction> mtEoDispatchActionList = mtEoDispatchActionMapper.select(mtEoDispatchAction);

        List<MtEoDispatchAction> resultList = new ArrayList<>();
        // 如果siteId 和prodLineId输入的情况下，需要筛选
        if (StringUtils.isNotEmpty(dto.getSiteId())) {
            List<String> eoIds = mtEoDispatchActionList.stream().map(MtEoDispatchAction::getEoId).distinct()
                            .collect(Collectors.toList());

            List<MtEo> mtEoList = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
            if (CollectionUtils.isNotEmpty(mtEoList)) {
                Map<String, List<MtEoDispatchAction>> eoDispatchProcessMap = mtEoDispatchActionList.stream()
                                .collect(Collectors.groupingBy(MtEoDispatchAction::getEoId));

                for (Map.Entry<String, List<MtEoDispatchAction>> tempEntry : eoDispatchProcessMap.entrySet()) {
                    List<MtEo> currentEoList = mtEoList.stream().filter(t -> t.getEoId().equals(tempEntry.getKey()))
                                    .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(currentEoList)) {
                        // 数据唯一
                        MtEo currentEo = currentEoList.get(0);
                        if (dto.getSiteId().equals(currentEo.getSiteId())
                                        && dto.getProductionLineId().equals(currentEo.getProductionLineId())) {
                            resultList.addAll(tempEntry.getValue());
                        }
                    }
                }
            }
        } else {
            resultList = mtEoDispatchActionList;
        }

        // 日期为空的情况转换为1970年
        resultList.sort(Comparator.comparingLong(MtEoDispatchAction::getPriority).thenComparing(
                        (MtEoDispatchAction c) -> c.getPlanStartTime() == null ? new Date(0) : c.getPlanStartTime())
                        .thenComparing((MtEoDispatchAction c) -> c.getPlanEndTime() == null ? new Date(0)
                                        : c.getPlanEndTime()));

        return resultList;
    }

    @Override
    public Double dispatchedEoRevocableQtyGet(Long tenantId, MtEoDispatchActionVO2 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "workcellId", "【API：dispatchedEoRevocableQtyGet】"));
        }
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "routerStepId", "【API：dispatchedEoRevocableQtyGet】"));
        }
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "eoId", "【API：dispatchedEoRevocableQtyGet】"));
        }
        String publishStrategy = null;
        MtEoDispatchActionVO24 strategyGet = this.eoLimitDispatchStrategyGet(tenantId, dto.getEoId());
        if (strategyGet != null) {
            publishStrategy = strategyGet.getPublishStrategy();
        }

        MtEoStepActualVO3 eoStepActualVO3 = new MtEoStepActualVO3();
        eoStepActualVO3.setEoId(dto.getEoId());
        eoStepActualVO3.setRouterStepId(dto.getRouterStepId());
        List<MtEoStepActualVO4> eoStepActualVO4List =
                        mtEoStepActualRepository.operationLimitEoStepActualQuery(tenantId, eoStepActualVO3);
        String eoStepActualId = null;
        if (CollectionUtils.isNotEmpty(eoStepActualVO4List)) {
            eoStepActualId = eoStepActualVO4List.get(0).getEoStepActualId();
        }

        if ("QUEUE".equalsIgnoreCase(publishStrategy) || null == publishStrategy) {
            if (null != eoStepActualId) {
                // 2. 获取输入步骤和工作单元的 queueQty
                MtEoStepWipVO1 mtEoStepWipVO1 = new MtEoStepWipVO1();
                mtEoStepWipVO1.setWorkcellId(dto.getWorkcellId());
                mtEoStepWipVO1.setEoStepActualId(eoStepActualId);
                List<MtEoStepWip> mtEoStepWipList =
                                mtEoStepWipRepository.eoWkcAndStepWipQuery(tenantId, mtEoStepWipVO1);
                if (CollectionUtils.isEmpty(mtEoStepWipList)) {
                    return 0.0D;
                }
                return mtEoStepWipList.get(0).getQueueQty() == null ? Double.valueOf(0.0D)
                                : mtEoStepWipList.get(0).getQueueQty();
            } else {
                return 0.0D;
            }
        }
        if ("WITHOUT_QUEUING".equalsIgnoreCase(publishStrategy)) {
            if (StringUtils.isEmpty(eoStepActualId)) {
                return mtEoRepository.eoPropertyGet(tenantId, dto.getEoId()).getQty();
            }
            MtEoStepWorkcellActualVO3 mtEoStepWorkcellActualVO3 = new MtEoStepWorkcellActualVO3();
            mtEoStepWorkcellActualVO3.setWorkcellId(dto.getWorkcellId());
            mtEoStepWorkcellActualVO3.setEoStepActualId(eoStepActualId);
            List<MtEoStepWorkcellActual> queueQtyList = mtEoStepWorkcellActualRepository
                            .eoWkcProductionResultGet(tenantId, mtEoStepWorkcellActualVO3);
            Double sumQueueQty =
                            queueQtyList.stream().map(MtEoStepWorkcellActual::getQueueQty).reduce(0.0D, Double::sum);
            Double queueQty = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId()).getQty();
            return BigDecimal.valueOf(queueQty).subtract(BigDecimal.valueOf(sumQueueQty)).doubleValue();
        }
        return 0.0D;
    }

    @Override
    public List<MtEoDispatchActionVO3> wkcShiftLimitDispatchedEoQuery(Long tenantId, MtEoDispatchActionVO1 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkcellId()) && StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0002",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0002", "DISPATCH",
                                            "workcellId、operationId", "【API：wkcShiftLimitDispatchedEoQuery】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftCode", "【API：wkcShiftLimitDispatchedEoQuery】"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftDate", "【API：wkcShiftLimitDispatchedEoQuery】"));
        }

        List<MtEoDispatchActionVO3> resultList = new ArrayList<>();

        // 2. 根据输入参数获取数据
        MtEoDispatchProcessVO1 mtEoDispatchProcessVO1 = new MtEoDispatchProcessVO1();
        mtEoDispatchProcessVO1.setOperationId(dto.getOperationId());
        mtEoDispatchProcessVO1.setShiftCode(dto.getShiftCode());
        mtEoDispatchProcessVO1.setShiftDate(dto.getShiftDate());
        mtEoDispatchProcessVO1.setWorkcellId(dto.getWorkcellId());
        mtEoDispatchProcessVO1.setSiteId(dto.getSiteId());
        mtEoDispatchProcessVO1.setProductionLineId(dto.getProductionLineId());
        List<MtEoDispatchProcess> mtEoDispatchProcessList = mtEoDispatchProcessRepository
                        .wkcShiftLimitDispatchedProcessEoQuery(tenantId, mtEoDispatchProcessVO1);

        if (CollectionUtils.isNotEmpty(mtEoDispatchProcessList)) {
            mtEoDispatchProcessList.forEach(dispatchProcess -> {
                MtEoDispatchActionVO3 result = new MtEoDispatchActionVO3();
                result.setEoId(dispatchProcess.getEoId());
                result.setRouterStepId(dispatchProcess.getRouterStepId());
                result.setOperationId(dispatchProcess.getOperationId());
                result.setPriority(dispatchProcess.getPriority());
                result.setPlanStartTime(dispatchProcess.getPlanStartTime());
                result.setPlanEndTime(dispatchProcess.getPlanEndTime());
                result.setAssignQty(dispatchProcess.getAssignQty());
                result.setRevocableQty(dispatchProcess.getAssignQty());
                result.setWorkcellId(dispatchProcess.getWorkcellId());
                resultList.add(result);
            });
        }

        MtEoDispatchActionVO1 mtEoDispatchActionVO1 = new MtEoDispatchActionVO1();
        mtEoDispatchActionVO1.setOperationId(dto.getOperationId());
        mtEoDispatchActionVO1.setShiftCode(dto.getShiftCode());
        mtEoDispatchActionVO1.setShiftDate(dto.getShiftDate());
        mtEoDispatchActionVO1.setWorkcellId(dto.getWorkcellId());
        mtEoDispatchActionVO1.setSiteId(dto.getSiteId());
        mtEoDispatchActionVO1.setProductionLineId(dto.getProductionLineId());
        List<MtEoDispatchAction> mtEoDispatchActionList =
                        wkcShiftLimitDispatchedPublishedEoQuery(tenantId, mtEoDispatchActionVO1);
        if (CollectionUtils.isNotEmpty(mtEoDispatchActionList)) {
            for (MtEoDispatchAction dispatchAction : mtEoDispatchActionList) {
                MtEoDispatchActionVO3 result = new MtEoDispatchActionVO3();
                result.setEoId(dispatchAction.getEoId());
                result.setRouterStepId(dispatchAction.getRouterStepId());
                result.setOperationId(dispatchAction.getOperationId());
                result.setPriority(dispatchAction.getPriority());
                result.setPlanStartTime(dispatchAction.getPlanStartTime());
                result.setPlanEndTime(dispatchAction.getPlanEndTime());
                result.setAssignQty(dispatchAction.getAssignQty());
                result.setRevision(dispatchAction.getRevision());
                result.setWorkcellId(dispatchAction.getWorkcellId());

                MtEoDispatchActionVO2 mtEoDispatchActionVO2 = new MtEoDispatchActionVO2();
                mtEoDispatchActionVO2.setRouterStepId(dispatchAction.getRouterStepId());
                mtEoDispatchActionVO2.setWorkcellId(dispatchAction.getWorkcellId());
                mtEoDispatchActionVO2.setEoId(dispatchAction.getEoId());
                Double aDouble = dispatchedEoRevocableQtyGet(tenantId, mtEoDispatchActionVO2);
                if (aDouble != null) {
                    result.setRevocableQty(Math.min(aDouble,
                                    dispatchAction.getAssignQty() == null ? 0.0D : dispatchAction.getAssignQty()));
                }
                resultList.add(result);
            }
        }

        if (CollectionUtils.isEmpty(resultList)) {
            return Collections.emptyList();
        }

        // 日期为空的情况转换为1970年
        resultList.sort(Comparator.comparingLong(MtEoDispatchActionVO3::getPriority).thenComparing(
                        (MtEoDispatchActionVO3 c) -> c.getPlanStartTime() == null ? new Date(0) : c.getPlanStartTime())
                        .thenComparing((MtEoDispatchActionVO3 c) -> c.getPlanEndTime() == null ? new Date(0)
                                        : c.getPlanEndTime()));
        return resultList;
    }

    @Override
    public List<MtEoDispatchActionVO3> wkcShiftLimitDispatchedOngoingEoQuery(Long tenantId, MtEoDispatchActionVO1 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkcellId()) && StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0002",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0002", "DISPATCH",
                                            "workcellId、operationId", "【API：wkcShiftLimitDispatchedOngoingEoQuery】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "shiftCode", "【API：wkcShiftLimitDispatchedOngoingEoQuery】"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "shiftDate", "【API：wkcShiftLimitDispatchedOngoingEoQuery】"));
        }

        // 2. 获取所有调度已发布的执行作业的调度信息
        MtEoDispatchActionVO1 mtEoDispatchActionVO1 = new MtEoDispatchActionVO1();
        mtEoDispatchActionVO1.setOperationId(dto.getOperationId());
        mtEoDispatchActionVO1.setShiftCode(dto.getShiftCode());
        mtEoDispatchActionVO1.setShiftDate(dto.getShiftDate());
        mtEoDispatchActionVO1.setWorkcellId(dto.getWorkcellId());
        mtEoDispatchActionVO1.setSiteId(dto.getSiteId());
        mtEoDispatchActionVO1.setProductionLineId(dto.getProductionLineId());
        List<MtEoDispatchActionVO3> dispatchedEoList = wkcShiftLimitDispatchedEoQuery(tenantId, mtEoDispatchActionVO1);
        if (CollectionUtils.isEmpty(dispatchedEoList)) {
            return Collections.emptyList();
        }

        List<MtEoDispatchActionVO3> resultList = new ArrayList<>();

        // 3. 获取执行作业状态status，筛选其中status = [‘QUEUE’、‘WORKING’、‘HOLD’]的执行作业
        // 新增逻辑(筛选status为RELEASED,WORKING的数据)
        for (MtEoDispatchActionVO3 t : dispatchedEoList) {
            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, t.getEoId());
            if (mtEo != null && StringUtils.isNotEmpty(mtEo.getEoId())
                            && Arrays.asList("RELEASED", "WORKING").contains(mtEo.getStatus())) {
                MtEoDispatchActionVO3 result = new MtEoDispatchActionVO3();
                result.setEoId(t.getEoId());
                result.setRouterStepId(t.getRouterStepId());
                result.setOperationId(t.getOperationId());
                result.setPriority(t.getPriority());
                result.setPlanStartTime(t.getPlanStartTime());
                result.setPlanEndTime(t.getPlanEndTime());
                result.setAssignQty(t.getAssignQty());
                result.setRevocableQty(t.getAssignQty());
                result.setRevision(t.getRevision());
                result.setWorkcellId(t.getWorkcellId());
                resultList.add(result);
            }
        }

        resultList.sort(Comparator.comparingLong(MtEoDispatchActionVO3::getPriority)
                        .thenComparing(MtEoDispatchActionVO3::getPlanStartTime)
                        .thenComparing(MtEoDispatchActionVO3::getPlanEndTime));

        return resultList;
    }

    @Override
    public List<MtEoDispatchAction> wkcShiftPeriodLimitDispatchedPublishedEoQuery(Long tenantId,
                    MtEoDispatchActionVO4 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkcellId()) && StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0002",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0002", "DISPATCH",
                                            "workcellId、operationId",
                                            "【API：wkcShiftPeriodLimitDispatchedPublishedEoQuery】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "shiftCode", "【API：wkcShiftPeriodLimitDispatchedPublishedEoQuery】"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "shiftDate", "【API：wkcShiftPeriodLimitDispatchedPublishedEoQuery】"));
        }
        if (StringUtils.isNotEmpty(dto.getSiteId()) && StringUtils.isEmpty(dto.getProductionLineId())
                        || StringUtils.isNotEmpty(dto.getProductionLineId()) && StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_DISPATCH_0021",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0021", "DISPATCH",
                                            "productionLineId、siteId",
                                            "【API：wkcShiftPeriodLimitDispatchedPublishedEoQuery】"));
        }

        // 2. 获取时域范围内所有班次日期p_shiftDate和班次编码p_shiftCode
        MtCalendarShiftVO3 mtCalendarShiftVO3 = new MtCalendarShiftVO3();
        mtCalendarShiftVO3.setWorkcellId(dto.getWorkcellId());
        mtCalendarShiftVO3.setShiftCode(dto.getShiftCode());
        mtCalendarShiftVO3.setShiftDate(dto.getShiftDate());
        mtCalendarShiftVO3.setBackwardPeriod(
                        dto.getBackwardPeriod() == null ? Integer.valueOf("0") : dto.getBackwardPeriod());
        mtCalendarShiftVO3.setForwardPeriod(
                        dto.getForwardPeriod() == null ? Integer.valueOf("0") : dto.getForwardPeriod());
        List<MtCalendarShiftVO4> periodCalendarShiftList =
                        mtCalendarShiftRepository.calendarShiftLimitNearShiftQuery(tenantId, mtCalendarShiftVO3);

        if (CollectionUtils.isEmpty(periodCalendarShiftList)) {
            return Collections.emptyList();
        }

        List<MtEoDispatchAction> mtEoDispatchActionList =
                        mtEoDispatchActionMapper.selectByShiftCodesAndDates(tenantId, periodCalendarShiftList, dto);

        List<MtEoDispatchAction> resultList = new ArrayList<>();

        // 如果siteId 和prodLineId输入的情况下，需要筛选
        if (StringUtils.isNotEmpty(dto.getSiteId())) {
            List<String> eoIds = mtEoDispatchActionList.stream().map(MtEoDispatchAction::getEoId).distinct()
                            .collect(Collectors.toList());

            List<MtEo> mtEoList = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
            if (CollectionUtils.isNotEmpty(mtEoList)) {
                Map<String, List<MtEoDispatchAction>> eoDispatchProcessMap = mtEoDispatchActionList.stream()
                                .collect(Collectors.groupingBy(MtEoDispatchAction::getEoId));

                for (Map.Entry<String, List<MtEoDispatchAction>> tempEntry : eoDispatchProcessMap.entrySet()) {
                    List<MtEo> currentEoList = mtEoList.stream().filter(t -> t.getEoId().equals(tempEntry.getKey()))
                                    .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(currentEoList)) {
                        // 数据唯一
                        MtEo currentEo = currentEoList.get(0);
                        if (dto.getSiteId().equals(currentEo.getSiteId())
                                        && dto.getProductionLineId().equals(currentEo.getProductionLineId())) {
                            resultList.addAll(tempEntry.getValue());
                        }
                    }
                }
            }
        } else {
            resultList = mtEoDispatchActionList;
        }

        // 班次日期、班次编码、优先级、计划开始时间、计划结束时间
        // 日期为空的情况转换为1970年
        resultList.sort(Comparator.comparing(MtEoDispatchAction::getShiftDate)
                        .thenComparing(MtEoDispatchAction::getShiftCode)
                        .thenComparingLong(MtEoDispatchAction::getPriority)
                        .thenComparing((MtEoDispatchAction c) -> c.getPlanStartTime() == null ? new Date(0)
                                        : c.getPlanStartTime())
                        .thenComparing((MtEoDispatchAction c) -> c.getPlanEndTime() == null ? new Date(0)
                                        : c.getPlanEndTime()));
        return resultList;
    }

    @Override
    public List<MtEoDispatchActionVO5> eoOnStandbyQuery(Long tenantId, MtEoDispatchActionVO1 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "workcellId", "【API：eoOnStandbyQuery】"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftDate", "【API：eoOnStandbyQuery】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftCode", "【API：eoOnStandbyQuery】"));
        }
        if (StringUtils.isNotEmpty(dto.getSiteId()) && StringUtils.isEmpty(dto.getProductionLineId())
                        || StringUtils.isNotEmpty(dto.getProductionLineId()) && StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_DISPATCH_0021", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0021", "DISPATCH", "productionLineId、siteId", "【API：eoOnStandbyQuery】"));
        }

        // 2. 根据输入工作单元和班次信息获取调度结果表
        MtEoDispatchAction mtEoDispatchAction = new MtEoDispatchAction();
        mtEoDispatchAction.setTenantId(tenantId);
        mtEoDispatchAction.setWorkcellId(dto.getWorkcellId());
        mtEoDispatchAction.setShiftDate(dto.getShiftDate());
        mtEoDispatchAction.setShiftCode(dto.getShiftCode());
        mtEoDispatchAction.setOperationId(dto.getOperationId());
        List<MtEoDispatchAction> eoDispatchActionList = mtEoDispatchActionMapper.select(mtEoDispatchAction);
        if (CollectionUtils.isEmpty(eoDispatchActionList)) {
            return Collections.emptyList();
        }

        List<MtEoDispatchAction> eoDispatchActionAvailableList = new ArrayList<>();

        // 如果输入了siteId和ProdLineId，需要筛选
        if (StringUtils.isNotEmpty(dto.getSiteId()) && StringUtils.isNotEmpty(dto.getProductionLineId())) {
            List<String> eoIds = eoDispatchActionList.stream().map(MtEoDispatchAction::getEoId).distinct()
                            .collect(Collectors.toList());
            List<MtEo> mtEoList = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);

            // 根据EO属性筛选
            if (CollectionUtils.isNotEmpty(mtEoList)) {
                Map<String, List<MtEoDispatchAction>> eoDispatchActionMap = eoDispatchActionList.stream()
                                .collect(Collectors.groupingBy(MtEoDispatchAction::getEoId));

                for (Map.Entry<String, List<MtEoDispatchAction>> tempMap : eoDispatchActionMap.entrySet()) {
                    List<MtEo> availableEos = mtEoList.stream().filter(t -> tempMap.getKey().equals(t.getEoId()))
                                    .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(availableEos)) {
                        // 结果唯一
                        MtEo currentEo = availableEos.get(0);
                        if (dto.getProductionLineId().equals(currentEo.getProductionLineId())
                                        && dto.getSiteId().equals(currentEo.getSiteId())) {
                            eoDispatchActionAvailableList.addAll(tempMap.getValue());
                        }
                    }
                }
            }
        } else {
            eoDispatchActionAvailableList = eoDispatchActionList;
        }

        final List<MtEoDispatchActionVO5> resultList = new ArrayList<>();

        for (MtEoDispatchAction eoDispatchAction : eoDispatchActionAvailableList) {

            List<String> eoStepActualIds =
                            mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, eoDispatchAction.getEoId());
            // update 2019/10/9 peng.yuan 增加空判断,当eoStepActualIds为空时跳过下面步骤
            if (CollectionUtils.isEmpty(eoStepActualIds)) {
                continue;
            }
            // 根据第三步的筛选获取第二步的eoId调用API{ eoLimitDispatchStrategyGet }，获取过程参数publishStrategy
            String publishStrategy = null;
            MtEoDispatchActionVO24 strategyGet = this.eoLimitDispatchStrategyGet(tenantId, eoDispatchAction.getEoId());
            if (strategyGet != null) {
                publishStrategy = strategyGet.getPublishStrategy();
            }
            List<MtEoStepActual> eoStepActuals =
                            this.mtEoStepActualRepository.eoStepPropertyBatchGet(tenantId, eoStepActualIds);
            Optional<MtEoStepActual> optional = eoStepActuals.stream()
                            .filter(t -> t.getRouterStepId().equals(eoDispatchAction.getRouterStepId())).findFirst();
            String eoStepActualId = null;
            if (optional.isPresent()) {
                eoStepActualId = optional.get().getEoStepActualId();
            }

            MtEoDispatchActionVO5 result;
            if ("QUEUE".equals(publishStrategy) || StringUtils.isEmpty(publishStrategy)) {
                // 只获取queueQty不为0的数据
                // if (BigDecimal.valueOf(eoStepWip.getQueueQty()).compareTo(BigDecimal.ZERO) != 0)
                // {
                // 结果变量赋值
                result = new MtEoDispatchActionVO5();
                result.setEoDispatchActionId(eoDispatchAction.getEoDispatchActionId());
                result.setEoId(eoDispatchAction.getEoId());
                result.setRouterStepId(eoDispatchAction.getRouterStepId());
                result.setWorkcellId(eoDispatchAction.getWorkcellId());
                result.setOperationId(eoDispatchAction.getOperationId());
                result.setPriority(eoDispatchAction.getPriority());
                result.setPlanStartTime(eoDispatchAction.getPlanStartTime());
                result.setPlanEndTime(eoDispatchAction.getPlanEndTime());
                result.setAssignQty(eoDispatchAction.getAssignQty());
                result.setRevision(eoDispatchAction.getRevision());

                if (null == eoStepActualId) {
                    continue;
                }

                HashMap<String, String> wkcEoStepActualIds = new HashMap<>(0);
                wkcEoStepActualIds.put(eoStepActualId, eoDispatchAction.getWorkcellId());
                List<MtEoStepWip> eoStepWipList =
                                mtEoStepWipRepository.eoWkcAndStepWipBatchGet(tenantId, wkcEoStepActualIds);

                if (CollectionUtils.isNotEmpty(eoStepWipList)) {
                    // 结果唯一
                    MtEoStepWip eoStepWip = eoStepWipList.get(0);
                    result.setOnStandbyQty(eoStepWip.getQueueQty());
                    // }
                }
                resultList.add(result);
            }

            if ("WITHOUT_QUEUING".equalsIgnoreCase(publishStrategy)) {
                MtEoStepActualVO3 mtEoStepActualVO3 = new MtEoStepActualVO3();
                mtEoStepActualVO3.setEoId(eoDispatchAction.getEoId());
                mtEoStepActualVO3.setRouterStepId(eoDispatchAction.getRouterStepId());
                List<MtEoStepActualVO4> mtEoStepActualVO4List =
                                mtEoStepActualRepository.operationLimitEoStepActualQuery(tenantId, mtEoStepActualVO3);
                if (CollectionUtils.isEmpty(mtEoStepActualVO4List)) {
                    result = new MtEoDispatchActionVO5();
                    BeanUtils.copyProperties(eoDispatchAction, result);
                    result.setOnStandbyQty(eoDispatchAction.getAssignQty());
                    resultList.add(result);
                } else {
                    result = new MtEoDispatchActionVO5();
                    BeanUtils.copyProperties(eoDispatchAction, result);

                    MtEoStepWorkcellActualVO3 mtEoStepWorkcellActualVO3 = new MtEoStepWorkcellActualVO3();
                    mtEoStepWorkcellActualVO3.setWorkcellId(dto.getWorkcellId());
                    mtEoStepWorkcellActualVO3.setEoStepActualId(mtEoStepActualVO4List.get(0).getEoStepActualId());
                    List<MtEoStepWorkcellActual> queueQtyList = mtEoStepWorkcellActualRepository
                                    .eoWkcProductionResultGet(tenantId, mtEoStepWorkcellActualVO3);
                    BigDecimal sumQueueQty = queueQtyList.stream().collect(
                                    CollectorsUtil.summingBigDecimal(c -> c.getQueueQty() == null ? BigDecimal.ZERO
                                                    : BigDecimal.valueOf(c.getQueueQty())));
                    // 修改计划调度获取数量 2019-10-31 by guichuan.li
                    MtEoStepWipVO1 mtEoStepWipVO1 = new MtEoStepWipVO1();
                    mtEoStepWipVO1.setEoStepActualId(mtEoStepActualVO4List.get(0).getEoStepActualId());
                    List<MtEoStepWip> wipList = mtEoStepWipRepository.eoWkcAndStepWipQuery(tenantId, mtEoStepWipVO1);
                    BigDecimal wipQueueQty = BigDecimal.ZERO;
                    if (CollectionUtils.isNotEmpty(wipList)) {
                        wipQueueQty = wipList.stream().collect(
                                        CollectorsUtil.summingBigDecimal(c -> c.getQueueQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(c.getQueueQty())));
                    }
                    result.setOnStandbyQty(BigDecimal.valueOf(eoDispatchAction.getAssignQty())
                                    .subtract(sumQueueQty == null ? BigDecimal.ZERO : sumQueueQty).add(wipQueueQty)
                                    .doubleValue());
                    resultList.add(result);
                }
            }
        }
        resultList.sort(Comparator
                        .comparingDouble((MtEoDispatchActionVO5 t) -> Double
                                        .valueOf(StringUtils.isEmpty(t.getOperationId()) ? "0" : t.getOperationId()))
                        .thenComparing(MtEoDispatchActionVO5::getPriority)
                        .thenComparing(MtEoDispatchActionVO5::getPlanStartTime));

        return resultList;
    }

    @Override
    public MtEoDispatchActionVO7 dispatchedEoAssignQtyGet(Long tenantId, MtEoDispatchActionVO2 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "routerStepId", "【API：dispatchedEoAssignQtyGet】"));
        }
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "eoId", "【API：dispatchedEoAssignQtyGet】"));
        }

        String publishStrategy = null;
        MtEoDispatchActionVO24 strategyGet = this.eoLimitDispatchStrategyGet(tenantId, dto.getEoId());
        if (strategyGet != null) {
            publishStrategy = strategyGet.getPublishStrategy();
        }
        MtEoDispatchActionVO7 result = new MtEoDispatchActionVO7();

        BigDecimal wipQueueQty = BigDecimal.ZERO;
        BigDecimal wipTotalQueueQty = BigDecimal.ZERO;
        BigDecimal totalQueueQty = BigDecimal.ZERO;
        BigDecimal processAssignQty = BigDecimal.ZERO;
        BigDecimal actAssignQty = BigDecimal.ZERO;
        if (MtBaseConstants.GEN_TYPE_CODE.QUEUE.equalsIgnoreCase(publishStrategy) || null == publishStrategy) {
            // 2. 判断是否输入 workcellId
            if (StringUtils.isEmpty(dto.getWorkcellId())) {
                List<String> eoStepActualIds = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getEoId());
                if (CollectionUtils.isEmpty(eoStepActualIds)) {
                    result.setAssignQty(0D);
                    result.setPublishQty(0D);
                    result.setWorkingQty(0D);
                    return result;
                }
                List<MtEoStepActual> eoStepActuals =
                                this.mtEoStepActualRepository.eoStepPropertyBatchGet(tenantId, eoStepActualIds);
                Optional<MtEoStepActual> optional = eoStepActuals.stream()
                                .filter(t -> t.getRouterStepId().equals(dto.getRouterStepId())).findFirst();
                String eoStepActualId = null;
                if (optional.isPresent()) {
                    eoStepActualId = optional.get().getEoStepActualId();
                }

                // 2.1. 获取执行作业步骤在制排队数量
                // 2.1.1. 限制workcell为空查询
                if (eoStepActualId != null) {
                    MtEoStepWipVO1 mtEoStepWipVO1 = new MtEoStepWipVO1();
                    mtEoStepWipVO1.setWorkcellId("");
                    mtEoStepWipVO1.setEoStepActualId(eoStepActualId);
                    List<MtEoStepWip> eoStepWipList =
                                    mtEoStepWipRepository.eoWkcAndStepWipQuery(tenantId, mtEoStepWipVO1);
                    if (CollectionUtils.isNotEmpty(eoStepWipList)) {
                        // 汇总数量
                        wipQueueQty = eoStepWipList.stream().collect(
                                        CollectorsUtil.summingBigDecimal(c -> c.getQueueQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(c.getQueueQty())));
                    }

                    // 2.1.2. 不限制workcell查询
                    mtEoStepWipVO1.setWorkcellId(null);
                    List<MtEoStepWip> eoStepWipTotalList =
                                    mtEoStepWipRepository.eoWkcAndStepWipQuery(tenantId, mtEoStepWipVO1);
                    if (CollectionUtils.isNotEmpty(eoStepWipTotalList)) {
                        // 汇总数量
                        wipTotalQueueQty = eoStepWipTotalList.stream().collect(
                                        CollectorsUtil.summingBigDecimal(c -> c.getQueueQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(c.getQueueQty())));
                    }

                    // 2.2. 获取执行作业步骤累计排队数量
                    MtEoStepActualVO5 eoStepActual =
                                    mtEoStepActualRepository.eoStepActualProcessedGet(tenantId, eoStepActualId);
                    if (eoStepActual != null && StringUtils.isNotEmpty(eoStepActual.getEoStepActualId())) {
                        totalQueueQty = new BigDecimal(eoStepActual.getQueueQty().toString());
                    }

                    // 2.3. 获取执行作业步骤已调度未发布的数量
                    MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
                    mtEoDispatchProcess.setTenantId(tenantId);
                    mtEoDispatchProcess.setRouterStepId(dto.getRouterStepId());
                    mtEoDispatchProcess.setEoId(dto.getEoId());
                    List<MtEoDispatchProcess> mtEoDispatchProcessList =
                                    mtEoDispatchProcessMapper.select(mtEoDispatchProcess);
                    if (CollectionUtils.isNotEmpty(mtEoDispatchProcessList)) {
                        processAssignQty = mtEoDispatchProcessList.stream().collect(
                                        CollectorsUtil.summingBigDecimal(c -> c.getAssignQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(c.getAssignQty())));
                    }
                }


                // 2.4 计算并输出结果
                // assignQty: totalQueueQty - wipQueueQty + processAssignQty
                result.setAssignQty(totalQueueQty.subtract(wipQueueQty).add(processAssignQty).doubleValue());

                // publishQty = totalQueueQty- wipQueueQty
                result.setPublishQty(totalQueueQty.subtract(wipQueueQty).doubleValue());

                // workingQty = totalQueueQty- wipTotalQueueQty
                result.setWorkingQty(totalQueueQty.subtract(wipTotalQueueQty).doubleValue());

            } else {
                List<String> eoStepActualIds = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getEoId());
                List<MtEoStepActual> eoStepActuals =
                                this.mtEoStepActualRepository.eoStepPropertyBatchGet(tenantId, eoStepActualIds);
                Optional<MtEoStepActual> optional = eoStepActuals.stream()
                                .filter(t -> t.getRouterStepId().equals(dto.getRouterStepId())).findFirst();
                String eoStepActualId = null;
                if (optional.isPresent()) {
                    eoStepActualId = optional.get().getEoStepActualId();
                }


                BigDecimal wkcWipQueueQty = BigDecimal.ZERO;
                BigDecimal wkcProcessAssignQty = BigDecimal.ZERO;
                BigDecimal wkcTotalQueueQty = BigDecimal.ZERO;
                // update by peng.yuan 2019/9/20
                // 上面操作后eoStepActualId任然可能为null
                if (eoStepActualId != null) {
                    // 3.1. 获取执行作业步骤在指定工作单元上在制排队数量，工作单元指定为输入参数
                    MtEoStepWipVO1 mtEoStepWipVO1 = new MtEoStepWipVO1();
                    mtEoStepWipVO1.setWorkcellId(dto.getWorkcellId());
                    mtEoStepWipVO1.setEoStepActualId(eoStepActualId);
                    List<MtEoStepWip> eoStepWipList =
                                    mtEoStepWipRepository.eoWkcAndStepWipQuery(tenantId, mtEoStepWipVO1);
                    if (CollectionUtils.isNotEmpty(eoStepWipList)) {
                        // 结果唯一
                        wkcWipQueueQty = new BigDecimal(eoStepWipList.get(0).getQueueQty().toString());
                    }

                    // 3.2. 获取执行作业步骤在指定工作单元上累计排队数量，工作单元指定为输入参数
                    MtEoStepWorkcellActualVO3 eoStepWorkcellActualVO3 = new MtEoStepWorkcellActualVO3();
                    eoStepWorkcellActualVO3.setEoStepActualId(eoStepActualId);
                    eoStepWorkcellActualVO3.setWorkcellId(dto.getWorkcellId());
                    List<MtEoStepWorkcellActual> eoStepWorkcellActualList = mtEoStepWorkcellActualRepository
                                    .eoWkcProductionResultGet(tenantId, eoStepWorkcellActualVO3);
                    if (CollectionUtils.isNotEmpty(eoStepWorkcellActualList)) {
                        // 结果唯一
                        wkcTotalQueueQty = new BigDecimal(eoStepWorkcellActualList.get(0).getQueueQty().toString());
                    }

                    // 3.3 获取执行作业步骤在指定工作单元上已调度未发布的数量
                    MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
                    mtEoDispatchProcess.setTenantId(tenantId);
                    mtEoDispatchProcess.setRouterStepId(dto.getRouterStepId());
                    mtEoDispatchProcess.setWorkcellId(dto.getWorkcellId());
                    mtEoDispatchProcess.setEoId(dto.getEoId());
                    List<MtEoDispatchProcess> mtEoDispatchProcessList =
                                    mtEoDispatchProcessMapper.select(mtEoDispatchProcess);
                    if (CollectionUtils.isNotEmpty(mtEoDispatchProcessList)) {
                        // 汇总数量
                        wkcProcessAssignQty = mtEoDispatchProcessList.stream().collect(
                                        CollectorsUtil.summingBigDecimal(c -> c.getAssignQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(c.getAssignQty())));
                    }
                }

                // 3.4 计算并输出结果
                // assignQty: wkcTotalQueueQty + wkcProcessAssignQty
                result.setAssignQty(wkcTotalQueueQty.add(wkcProcessAssignQty).doubleValue());
                // publishQty = wkcTotalQueueQty
                result.setPublishQty(wkcTotalQueueQty.doubleValue());
                // workingQty = wkcTotalQueueQty- wkcWipQueueQty
                result.setWorkingQty(wkcTotalQueueQty.subtract(wkcWipQueueQty).doubleValue());
            }
        }
        if (MtBaseConstants.GEN_TYPE_CODE.WITHOUT_QUEUING.equalsIgnoreCase(publishStrategy)) {
            MtEoStepActualVO3 mtEoStepActualVO3 = new MtEoStepActualVO3();
            mtEoStepActualVO3.setEoId(dto.getEoId());
            mtEoStepActualVO3.setRouterStepId(dto.getRouterStepId());
            List<MtEoStepActualVO4> eoStepActualVO4List =
                            mtEoStepActualRepository.operationLimitEoStepActualQuery(tenantId, mtEoStepActualVO3);
            String eoStepActualId = null;
            if (CollectionUtils.isNotEmpty(eoStepActualVO4List)) {
                eoStepActualId = eoStepActualVO4List.get(0).getEoStepActualId();
            }

            BigDecimal sumQueueQty = BigDecimal.ZERO;
            if (StringUtils.isEmpty(eoStepActualId)) {
                if (StringUtils.isEmpty(dto.getWorkcellId())) {
                    MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
                    mtEoDispatchProcess.setTenantId(tenantId);
                    mtEoDispatchProcess.setRouterStepId(dto.getRouterStepId());
                    mtEoDispatchProcess.setEoId(dto.getEoId());
                    List<MtEoDispatchProcess> mtEoDispatchProcessList =
                                    mtEoDispatchProcessMapper.select(mtEoDispatchProcess);
                    if (CollectionUtils.isNotEmpty(mtEoDispatchProcessList)) {
                        processAssignQty = mtEoDispatchProcessList.stream().collect(
                                        CollectorsUtil.summingBigDecimal(c -> c.getAssignQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(c.getAssignQty())));
                    }
                    MtEoDispatchAction eoDispatchAction = new MtEoDispatchAction();
                    eoDispatchAction.setTenantId(tenantId);
                    eoDispatchAction.setEoId(dto.getEoId());
                    eoDispatchAction.setRouterStepId(dto.getRouterStepId());
                    List<MtEoDispatchAction> mtEoDispatchActionList =
                                    this.mtEoDispatchActionRepository.select(eoDispatchAction);
                    if (CollectionUtils.isNotEmpty(mtEoDispatchActionList)) {
                        actAssignQty = mtEoDispatchActionList.stream().collect(
                                        CollectorsUtil.summingBigDecimal(c -> c.getAssignQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(c.getAssignQty())));
                    }
                } else {
                    MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
                    mtEoDispatchProcess.setTenantId(tenantId);
                    mtEoDispatchProcess.setRouterStepId(dto.getRouterStepId());
                    mtEoDispatchProcess.setEoId(dto.getEoId());
                    mtEoDispatchProcess.setWorkcellId(dto.getWorkcellId());
                    List<MtEoDispatchProcess> mtEoDispatchProcessList =
                                    mtEoDispatchProcessMapper.select(mtEoDispatchProcess);
                    if (CollectionUtils.isNotEmpty(mtEoDispatchProcessList)) {
                        processAssignQty = mtEoDispatchProcessList.stream().collect(
                                        CollectorsUtil.summingBigDecimal(c -> c.getAssignQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(c.getAssignQty())));
                    }

                    MtEoDispatchAction eoDispatchAction = new MtEoDispatchAction();
                    eoDispatchAction.setTenantId(tenantId);
                    eoDispatchAction.setEoId(dto.getEoId());
                    eoDispatchAction.setRouterStepId(dto.getRouterStepId());
                    eoDispatchAction.setWorkcellId(dto.getWorkcellId());
                    List<MtEoDispatchAction> mtEoDispatchActionList =
                                    this.mtEoDispatchActionRepository.select(eoDispatchAction);
                    if (CollectionUtils.isNotEmpty(mtEoDispatchActionList)) {
                        actAssignQty = mtEoDispatchActionList.stream().collect(
                                        CollectorsUtil.summingBigDecimal(c -> c.getAssignQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(c.getAssignQty())));
                    }

                }

                // 累计已调度数量：assignQty=proAssignQty+actAssignQty；
                result.setAssignQty(processAssignQty.add(actAssignQty).doubleValue());
                // 累计调度已发布数量：publishQty = actAssignQty；
                result.setPublishQty(actAssignQty.doubleValue());
                // 累计调度运行数量：workingQty =0。
                result.setWorkingQty(0.0D);
            } else {

                if (StringUtils.isEmpty(dto.getWorkcellId())) {

                    // update by peng.yuan 2019/10/11 增加新API逻辑查询workcellId
                    MtEoStepWorkcellActualVO7 actualVO7 = new MtEoStepWorkcellActualVO7();
                    actualVO7.setEoStepActualId(eoStepActualId);
                    List<MtEoStepWorkcellActualVO8> workcellActualVO8List = mtEoStepWorkcellActualRepository
                                    .propertyLimitEoStepWkcActualPropertyQuery(tenantId, actualVO7);

                    if (CollectionUtils.isNotEmpty(workcellActualVO8List)) {
                        List<String> workcellIds = workcellActualVO8List.stream()
                                        .map(MtEoStepWorkcellActualVO8::getWorkcellId).collect(Collectors.toList());

                        for (String workcellId : workcellIds) {
                            MtEoStepWorkcellActualVO3 mtEoStepWorkcellActualVO3 = new MtEoStepWorkcellActualVO3();
                            mtEoStepWorkcellActualVO3.setEoStepActualId(eoStepActualId);
                            mtEoStepWorkcellActualVO3.setWorkcellId(workcellId);
                            List<MtEoStepWorkcellActual> queueQtyList = mtEoStepWorkcellActualRepository
                                            .eoWkcProductionResultGet(tenantId, mtEoStepWorkcellActualVO3);
                            BigDecimal sum_queueQty = BigDecimal.ZERO;
                            if (CollectionUtils.isNotEmpty(queueQtyList)) {
                                sum_queueQty = queueQtyList.stream()
                                                .collect(CollectorsUtil.summingBigDecimal(t -> BigDecimal.valueOf(
                                                                t.getQueueQty() == null ? 0.0D : t.getQueueQty())));
                            }
                            sumQueueQty = sumQueueQty.add(sum_queueQty);
                        }
                    }

                    MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
                    mtEoDispatchProcess.setTenantId(tenantId);
                    mtEoDispatchProcess.setRouterStepId(dto.getRouterStepId());
                    mtEoDispatchProcess.setEoId(dto.getEoId());
                    List<MtEoDispatchProcess> mtEoDispatchProcessList =
                                    mtEoDispatchProcessMapper.select(mtEoDispatchProcess);
                    if (CollectionUtils.isNotEmpty(mtEoDispatchProcessList)) {
                        processAssignQty = mtEoDispatchProcessList.stream().collect(
                                        CollectorsUtil.summingBigDecimal(c -> c.getAssignQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(c.getAssignQty())));
                    }
                    MtEoDispatchAction eoDispatchAction = new MtEoDispatchAction();
                    eoDispatchAction.setTenantId(tenantId);
                    eoDispatchAction.setEoId(dto.getEoId());
                    eoDispatchAction.setRouterStepId(dto.getRouterStepId());
                    List<MtEoDispatchAction> mtEoDispatchActionList =
                                    this.mtEoDispatchActionRepository.select(eoDispatchAction);
                    if (CollectionUtils.isNotEmpty(mtEoDispatchActionList)) {
                        actAssignQty = mtEoDispatchActionList.stream().collect(
                                        CollectorsUtil.summingBigDecimal(c -> c.getAssignQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(c.getAssignQty())));
                    }
                } else {
                    MtEoStepWorkcellActualVO3 mtEoStepWorkcellActualVO3 = new MtEoStepWorkcellActualVO3();
                    mtEoStepWorkcellActualVO3.setEoStepActualId(eoStepActualId);
                    mtEoStepWorkcellActualVO3.setWorkcellId(dto.getWorkcellId());
                    List<MtEoStepWorkcellActual> queueQtyList = mtEoStepWorkcellActualRepository
                                    .eoWkcProductionResultGet(tenantId, mtEoStepWorkcellActualVO3);
                    if (CollectionUtils.isNotEmpty(queueQtyList)) {
                        sumQueueQty = queueQtyList.stream().collect(
                                        CollectorsUtil.summingBigDecimal(c -> c.getQueueQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(c.getQueueQty())));
                    }

                    MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
                    mtEoDispatchProcess.setTenantId(tenantId);
                    mtEoDispatchProcess.setRouterStepId(dto.getRouterStepId());
                    mtEoDispatchProcess.setEoId(dto.getEoId());
                    mtEoDispatchProcess.setWorkcellId(dto.getWorkcellId());
                    List<MtEoDispatchProcess> mtEoDispatchProcessList =
                                    mtEoDispatchProcessMapper.select(mtEoDispatchProcess);
                    if (CollectionUtils.isNotEmpty(mtEoDispatchProcessList)) {
                        processAssignQty = mtEoDispatchProcessList.stream().collect(
                                        CollectorsUtil.summingBigDecimal(c -> c.getAssignQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(c.getAssignQty())));
                    }

                    MtEoDispatchAction eoDispatchAction = new MtEoDispatchAction();
                    eoDispatchAction.setTenantId(tenantId);
                    eoDispatchAction.setEoId(dto.getEoId());
                    eoDispatchAction.setRouterStepId(dto.getRouterStepId());
                    eoDispatchAction.setWorkcellId(dto.getWorkcellId());
                    List<MtEoDispatchAction> mtEoDispatchActionList =
                                    this.mtEoDispatchActionRepository.select(eoDispatchAction);
                    if (CollectionUtils.isNotEmpty(mtEoDispatchActionList)) {
                        actAssignQty = mtEoDispatchActionList.stream().collect(
                                        CollectorsUtil.summingBigDecimal(c -> c.getAssignQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(c.getAssignQty())));
                    }
                }
                // 判断是否获取数据
                if (0 != processAssignQty.compareTo(BigDecimal.ZERO) || 0 != actAssignQty.compareTo(BigDecimal.ZERO)) {
                    // 累计已调度数量：assignQty=proAssignQty+actAssignQty；
                    result.setAssignQty(processAssignQty.add(actAssignQty).doubleValue());
                    // 累计调度已发布数量：publishQty = actAssignQty；
                    result.setPublishQty(actAssignQty.doubleValue());
                    // 累计调度运行数量：workingQty =sum_queueQty。
                } else {
                    result.setAssignQty(sumQueueQty.doubleValue());
                    result.setPublishQty(sumQueueQty.doubleValue());
                }
                result.setWorkingQty(sumQueueQty.doubleValue());
            }
        }

        return result;
    }

    @Override
    public void eoDispatchVerify(Long tenantId, MtEoDispatchActionVO8 dto) {
        // 1. 验证参数有效性
        if (dto.getAssignQty() == null) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "assignQty", "【API：eoDispatchVerify】"));
        }
        if (BigDecimal.valueOf(dto.getAssignQty()).compareTo(BigDecimal.ZERO) != 1) {
            throw new MtException("MT_DISPATCH_0007", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0007", "DISPATCH", "assignQty", "【API：eoDispatchVerify】"));
        }
        if (StringUtils.isEmpty(dto.getEoId()) && StringUtils.isEmpty(dto.getEoDispatchProcessId())) {
            throw new MtException("MT_DISPATCH_0002", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0002", "DISPATCH", "eoId、eoDispatchProcessId", "【API：eoDispatchVerify】"));
        }
        if (StringUtils.isNotEmpty(dto.getEoId()) && StringUtils.isNotEmpty(dto.getEoDispatchProcessId())) {
            throw new MtException("MT_DISPATCH_0012", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0012", "DISPATCH", "eoId、eoDispatchProcessId", "【API：eoDispatchVerify】"));
        }
        if (StringUtils.isNotEmpty(dto.getEoId()) && StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "routerStepId", "【API：eoDispatchVerify】"));
        }
        if (StringUtils.isEmpty(dto.getEoId()) && StringUtils.isNotEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "eoId", "【API：eoDispatchVerify】"));
        }

        // 2.
        boolean eoStepActualIdIsNullFlag = false;
        if (StringUtils.isEmpty(dto.getEoId())) {
            eoStepActualIdIsNullFlag = true;
            MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
            mtEoDispatchProcess.setTenantId(tenantId);
            mtEoDispatchProcess.setEoDispatchProcessId(dto.getEoDispatchProcessId());
            mtEoDispatchProcess = mtEoDispatchProcessMapper.selectOne(mtEoDispatchProcess);
            if (mtEoDispatchProcess == null) {
                throw new MtException("MT_DISPATCH_0014", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0014", "DISPATCH", "【API：eoDispatchVerify】"));
            }
            dto.setEoId(mtEoDispatchProcess.getEoId());
            dto.setRouterStepId(mtEoDispatchProcess.getRouterStepId());
        }

        // 3. 根据 eoId 获取执行作业步骤实绩对应的执行作业并判断执行作业状态
        MtEo mtEo = this.mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (null == mtEo || !Arrays.asList("RELEASED", "WORKING", "HOLD").contains(mtEo.getStatus())) {
            throw new MtException("MT_DISPATCH_0013", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0013", "DISPATCH", "[ RELEASED、WORKING、HOLD]", "【API：eoDispatchVerify】"));
        }

        // 根据输入的eoId调用API{ eoLimitDispatchStrategyGet }，获取过程参数dispatchStrategy
        String publishStrategy = null;
        MtEoDispatchActionVO24 strategyGet = this.eoLimitDispatchStrategyGet(tenantId, dto.getEoId());
        if (strategyGet != null) {
            publishStrategy = strategyGet.getRangeStrategy();
        }
        if (StringUtils.isEmpty(publishStrategy) || "ACTUAL_DISPATCH".equals(publishStrategy)) {

            // 4.1. 获取执行作业步骤已调度数量
            MtEoDispatchActionVO2 mtEoDispatchActionVO2 = new MtEoDispatchActionVO2();
            mtEoDispatchActionVO2.setRouterStepId(dto.getRouterStepId());
            mtEoDispatchActionVO2.setEoId(dto.getEoId());
            MtEoDispatchActionVO7 eoDispatchActionQty = dispatchedEoAssignQtyGet(tenantId, mtEoDispatchActionVO2);
            // 4.2. 获取步骤实绩进度
            BigDecimal queueQty = BigDecimal.ZERO;
            List<String> eoStepActualIds = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getEoId());
            List<MtEoStepActual> eoStepActuals =
                            this.mtEoStepActualRepository.eoStepPropertyBatchGet(tenantId, eoStepActualIds);
            Optional<MtEoStepActual> optional = eoStepActuals.stream()
                            .filter(t -> t.getRouterStepId().equals(dto.getRouterStepId())).findFirst();
            String eoStepActualId = null;
            if (optional.isPresent()) {
                eoStepActualId = optional.get().getEoStepActualId();
            }

            MtEoStepActualVO5 eoStepActualQty =
                            mtEoStepActualRepository.eoStepActualProcessedGet(tenantId, eoStepActualId);
            if (eoStepActualQty != null && eoStepActualQty.getQueueQty() != null) {
                queueQty = new BigDecimal(eoStepActualQty.getQueueQty().toString());
            }

            // 5. 判断结果
            if (eoStepActualIdIsNullFlag) {
                if (new BigDecimal(dto.getAssignQty().toString())
                                .add(new BigDecimal(eoDispatchActionQty.getPublishQty().toString()))
                                .compareTo(queueQty) == 1) {
                    throw new MtException("MT_DISPATCH_0015", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_DISPATCH_0015", "DISPATCH", "【API：eoDispatchVerify】"));
                }
            } else {
                if (new BigDecimal(dto.getAssignQty().toString())
                                .add(new BigDecimal(eoDispatchActionQty.getAssignQty().toString()))
                                .compareTo(queueQty) == 1) {
                    throw new MtException("MT_DISPATCH_0015", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_DISPATCH_0015", "DISPATCH", "【API：eoDispatchVerify】"));
                }
            }
        } else if ("PLAN_DISPATCH".equals(publishStrategy)) {
            // 若输入值eoId为空，eoDispatchProcessId不为空
            if (eoStepActualIdIsNullFlag) {
                MtEo eo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
                BigDecimal eoQty = BigDecimal.ZERO;
                if (eo != null && eo.getQty() != null) {
                    eoQty = BigDecimal.valueOf(eo.getQty());
                }
                MtEoDispatchActionVO2 mtEoDispatchActionVO2 = new MtEoDispatchActionVO2();
                mtEoDispatchActionVO2.setEoId(dto.getEoId());
                mtEoDispatchActionVO2.setRouterStepId(dto.getRouterStepId());
                MtEoDispatchActionVO7 mtEoDispatchActionVO7 =
                                self().dispatchedEoAssignQtyGet(tenantId, mtEoDispatchActionVO2);
                BigDecimal publicQty = BigDecimal.ZERO;
                if (mtEoDispatchActionVO7 != null && mtEoDispatchActionVO7.getPublishQty() != null) {
                    publicQty = BigDecimal.valueOf(mtEoDispatchActionVO7.getPublishQty());
                }
                if (BigDecimal.valueOf(dto.getAssignQty()).add(publicQty).compareTo(eoQty) > 0) {
                    throw new MtException("MT_DISPATCH_0029", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_DISPATCH_0029", "DISPATCH", "【API：eoDispatchVerify】"));
                }
            } else {
                String routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
                MtRouter router = mtRouterRepository.routerGet(tenantId, routerId);

                MtEo eo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());

                if (null != router && "N".equals(router.getRelaxedFlowFlag())) {
                    if (BigDecimal.valueOf(dto.getAssignQty()).compareTo(BigDecimal.valueOf(eo.getQty())) != 0) {
                        throw new MtException("MT_DISPATCH_0040", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                        "MT_DISPATCH_0040", "DISPATCH", "【API：eoDispatchVerify】"));
                    }
                }

                MtEoDispatchProcess dispatchProcess = new MtEoDispatchProcess();
                dispatchProcess.setEoId(dto.getEoId());
                dispatchProcess.setRouterStepId(dto.getRouterStepId());
                dispatchProcess = mtEoDispatchProcessMapper.selectOne(dispatchProcess);
                BigDecimal assignQty = BigDecimal.ZERO;
                if (null != dispatchProcess && dispatchProcess.getAssignQty() != null) {
                    assignQty = BigDecimal.valueOf(dispatchProcess.getAssignQty());
                }

                MtEoDispatchAction mtEoDispatchAction = new MtEoDispatchAction();
                mtEoDispatchAction.setTenantId(tenantId);
                mtEoDispatchAction.setEoId(dto.getEoId());
                mtEoDispatchAction.setRouterStepId(dto.getRouterStepId());
                mtEoDispatchAction = mtEoDispatchActionMapper.selectOne(mtEoDispatchAction);
                BigDecimal actAssignQty = BigDecimal.ZERO;
                if (null != mtEoDispatchAction && mtEoDispatchAction.getAssignQty() != null) {
                    actAssignQty = BigDecimal.valueOf(mtEoDispatchAction.getAssignQty());
                }


                BigDecimal eoQty = BigDecimal.ZERO;
                if (eo != null && eo.getQty() != null) {
                    eoQty = BigDecimal.valueOf(eo.getQty());
                }
                if (BigDecimal.valueOf(dto.getAssignQty()).add(assignQty).add(actAssignQty).compareTo(eoQty) > 0) {
                    throw new MtException("MT_DISPATCH_0029", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_DISPATCH_0029", "DISPATCH", "【API：eoDispatchVerify】"));
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String eoDispatch(Long tenantId, MtEoDispatchActionVO9 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoId()) && StringUtils.isEmpty(dto.getEoDispatchProcessId())) {
            throw new MtException("MT_DISPATCH_0002", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0002", "DISPATCH", "eoId、eoDispatchProcessId", "【API：eoDispatch】"));
        }

        // 2. 输入参数判断此次进行调度结果发布还是调度新增
        if (StringUtils.isEmpty(dto.getEoDispatchProcessId())) {
            // 2.1. 判定为新增调度模式
            MtEoDispatchProcessVO2 mtEoDispatchProcessVO2 = new MtEoDispatchProcessVO2();
            BeanUtils.copyProperties(dto, mtEoDispatchProcessVO2);

            return mtEoDispatchProcessRepository.dispatchedEoUpdate(tenantId, mtEoDispatchProcessVO2, "N");
        } else {
            // 2.2. 判定为发布调度模式
            MtEoDispatchProcess oldEoDispatchProcess = new MtEoDispatchProcess();
            oldEoDispatchProcess.setTenantId(tenantId);
            oldEoDispatchProcess.setEoDispatchProcessId(dto.getEoDispatchProcessId());
            oldEoDispatchProcess = mtEoDispatchProcessMapper.selectOne(oldEoDispatchProcess);
            if (oldEoDispatchProcess == null || StringUtils.isEmpty(oldEoDispatchProcess.getEoDispatchProcessId())) {
                throw new MtException("MT_DISPATCH_0014", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0014", "DISPATCH", "【API：eoDispatch】"));
            }

            MtEoDispatchAction newEoDispatchAction = new MtEoDispatchAction();
            BeanUtils.copyProperties(oldEoDispatchProcess, newEoDispatchAction);
            newEoDispatchAction.setTenantId(tenantId);

            // 2.2.1. 获取最大版本
            MtEoDispatchActionVO1 condition = new MtEoDispatchActionVO1();
            condition.setWorkcellId(oldEoDispatchProcess.getWorkcellId());
            condition.setShiftDate(oldEoDispatchProcess.getShiftDate());
            condition.setShiftCode(oldEoDispatchProcess.getShiftCode());
            condition.setOperationId(oldEoDispatchProcess.getOperationId());
            Long maxRevision = mtEoDispatchActionMapper.getMaxRevision(tenantId, condition);

            newEoDispatchAction.setRevision(maxRevision == null ? Long.valueOf(1L) : maxRevision);
            self().insertSelective(newEoDispatchAction);
            // 获取eoId
            String eoId = null;
            MtEoDispatchProcess process = new MtEoDispatchProcess();
            process.setEoDispatchProcessId(dto.getEoDispatchProcessId());
            process.setTenantId(tenantId);
            MtEoDispatchProcess selectOne = mtEoDispatchProcessMapper.selectOne(process);
            if (null != selectOne) {
                eoId = selectOne.getEoId();
            }
            // 根据输入的eoId调用API{ eoLimitDispatchStrategyGet }，获取过程参数dispatchStrategy
            String publishStrategy = null;
            MtEoDispatchActionVO24 strategyGet = this.eoLimitDispatchStrategyGet(tenantId, eoId);
            if (strategyGet != null) {
                publishStrategy = strategyGet.getPublishStrategy();
            }
            if (StringUtils.isEmpty(publishStrategy) || "QUEUE".equals(publishStrategy)) {

                List<String> eoStepActualIds = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, eoId);
                String processRouterStepId = oldEoDispatchProcess.getRouterStepId();
                List<MtEoStepActual> eoStepActuals =
                                this.mtEoStepActualRepository.eoStepPropertyBatchGet(tenantId, eoStepActualIds);
                Optional<MtEoStepActual> optional = eoStepActuals.stream()
                                .filter(t -> t.getRouterStepId().equals(processRouterStepId)).findFirst();
                String eoStepActualId = null;
                if (optional.isPresent()) {
                    eoStepActualId = optional.get().getEoStepActualId();
                }
                MtEoStepWipVO4 mtEoStepWipVO4 = new MtEoStepWipVO4();
                mtEoStepWipVO4.setEoStepActualId(eoStepActualId);
                mtEoStepWipVO4.setQueueQty(oldEoDispatchProcess.getAssignQty());
                mtEoStepWipVO4.setWorkcellId(oldEoDispatchProcess.getWorkcellId());
                mtEoStepWipRepository.eoWkcQueue(tenantId, mtEoStepWipVO4);

                // 2.2.3. 录调度发布历史
                MtEoDispatchHistory mtEoDispatchHistory = new MtEoDispatchHistory();
                BeanUtils.copyProperties(newEoDispatchAction, mtEoDispatchHistory);
                mtEoDispatchHistory.setTenantId(tenantId);
                mtEoDispatchHistoryRepository.insertSelective(mtEoDispatchHistory);
            } else if ("WITHOUT_QUEUING".equals(publishStrategy)) {
                // 2.2.3. 录调度发布历史
                MtEoDispatchHistory mtEoDispatchHistory = new MtEoDispatchHistory();
                BeanUtils.copyProperties(newEoDispatchAction, mtEoDispatchHistory);
                mtEoDispatchHistory.setTenantId(tenantId);
                mtEoDispatchHistoryRepository.insertSelective(mtEoDispatchHistory);
            }
            // 2.2.4删除调度过程数据
            MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
            mtEoDispatchProcess.setEoDispatchProcessId(dto.getEoDispatchProcessId());
            mtEoDispatchProcessRepository.deleteByPrimaryKey(mtEoDispatchProcess);
            return newEoDispatchAction.getEoDispatchActionId();
        }
    }

    @Override
    public void eoDispatchCancelVerify(Long tenantId, MtEoDispatchActionVO6 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoDispatchActionId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "eoDispatchActionId", "【API：eoDispatchCancelVerify】"));
        }
        if (dto.getCancelQty() == null) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "cancelQty", "【API：eoDispatchCancelVerify】"));
        }

        if (new BigDecimal(dto.getCancelQty().toString()).compareTo(BigDecimal.ZERO) != 1) {
            throw new MtException("MT_DISPATCH_0007", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0007", "DISPATCH", "cancelQty", "【API：eoDispatchCancelVerify】"));
        }

        // 2. 根据输入参数eoDispatchActionId限定从调度结果表获取数据
        MtEoDispatchAction mtEoDispatchAction = new MtEoDispatchAction();
        mtEoDispatchAction.setTenantId(tenantId);
        mtEoDispatchAction.setEoDispatchActionId(dto.getEoDispatchActionId());
        mtEoDispatchAction = mtEoDispatchActionMapper.selectOne(mtEoDispatchAction);
        if (mtEoDispatchAction == null || StringUtils.isEmpty(mtEoDispatchAction.getEoDispatchActionId())) {
            throw new MtException("MT_DISPATCH_0010", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0010", "DISPATCH", "【API：eoDispatchCancelVerify】"));
        }

        // 3. 获取可修改调度数量 revocableQty
        MtEoDispatchActionVO2 mtEoDispatchActionVO2 = new MtEoDispatchActionVO2();
        mtEoDispatchActionVO2.setRouterStepId(mtEoDispatchAction.getRouterStepId());
        mtEoDispatchActionVO2.setWorkcellId(mtEoDispatchAction.getWorkcellId());
        mtEoDispatchActionVO2.setEoId(mtEoDispatchAction.getEoId());
        Double revocableQty = dispatchedEoRevocableQtyGet(tenantId, mtEoDispatchActionVO2);

        // 4. 若cancelQty ＞ ASSIGN_QTY 或 cancelQty ＞ revocableQty 校验失败
        if (new BigDecimal(dto.getCancelQty().toString())
                        .compareTo(new BigDecimal(mtEoDispatchAction.getAssignQty().toString())) == 1
                        || new BigDecimal(dto.getCancelQty().toString())
                                        .compareTo(new BigDecimal(revocableQty.toString())) == 1) {
            throw new MtException("MT_DISPATCH_0011", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0011", "DISPATCH", "【API：eoDispatchCancelVerify】"));
        }
    }

    @Override
    public MtEoDispatchActionVO10 toBeDispatchedEoUnassignQtyGet(Long tenantId, MtEoDispatchActionVO16 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "【API:toBeDispatchedEoUnassignQtyGet】"));
        }
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "【API:toBeDispatchedEoUnassignQtyGet】"));
        }

        String rangeStrategy = null;
        MtEoDispatchActionVO24 strategyGet = this.eoLimitDispatchStrategyGet(tenantId, dto.getEoId());
        if (strategyGet != null) {
            rangeStrategy = strategyGet.getRangeStrategy();
        }
        MtEoDispatchActionVO10 result = new MtEoDispatchActionVO10();
        MtEoStepActualVO1 eoStepActualVO1 = new MtEoStepActualVO1();
        MtEoDispatchActionVO7 mtEoDispatchActionVO7;
        if ("ACTUAL_DISPATCH".equals(rangeStrategy) || StringUtils.isEmpty(rangeStrategy)) {
            List<String> eoStepActualIds = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getEoId());
            if (CollectionUtils.isEmpty(eoStepActualIds)) {
                result.setDispatchableQty(0D);
                result.setUnassignQty(0D);
                return result;
            }
            List<MtEoStepActual> eoStepActuals =
                            this.mtEoStepActualRepository.eoStepPropertyBatchGet(tenantId, eoStepActualIds);
            Optional<MtEoStepActual> optional = eoStepActuals.stream()
                            .filter(t -> t.getRouterStepId().equals(dto.getRouterStepId())).findFirst();
            String eoStepActualId = null;
            if (optional.isPresent()) {
                eoStepActualId = optional.get().getEoStepActualId();
            }

            // 3.a 获取执行作业数量 eoQty
            // update by peng.yuan 2019/9/20
            MtEo eo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
            eoStepActualVO1.setEoQty(eo.getQty());

            // 3.b 获取执行作业步骤实绩累计排队数量 queueQty
            MtEoStepActualVO5 eoStepActualVO5 =
                            mtEoStepActualRepository.eoStepActualProcessedGet(tenantId, eoStepActualId);

            // 4.a 获取执行作业步骤累计调度数量 assignQty
            MtEoDispatchActionVO2 mtEoDispatchActionVO2 = new MtEoDispatchActionVO2();
            mtEoDispatchActionVO2.setRouterStepId(dto.getRouterStepId());
            mtEoDispatchActionVO2.setWorkcellId(null);
            mtEoDispatchActionVO2.setEoId(dto.getEoId());
            mtEoDispatchActionVO7 = dispatchedEoAssignQtyGet(tenantId, mtEoDispatchActionVO2);

            // update by peng.yuan 2019/9/20
            if (null == eoStepActualVO5) {
                eoStepActualVO5 = new MtEoStepActualVO5();
            }
            if (null == eoStepActualVO5.getEoStepActualId()) {
                eoStepActualVO5.setQueueQty(0.0D);
            }

            result.setDispatchableQty(new BigDecimal(eoStepActualVO5.getQueueQty().toString())
                            .subtract(new BigDecimal(mtEoDispatchActionVO7.getAssignQty().toString())).doubleValue());
            result.setUnassignQty(new BigDecimal(eoStepActualVO1.getEoQty().toString())
                            .subtract(new BigDecimal(mtEoDispatchActionVO7.getAssignQty().toString())).doubleValue());
        }
        // update by peng.yuan 2019/9/25
        if ("PLAN_DISPATCH".equalsIgnoreCase(rangeStrategy)) {
            // 3.a 获取执行作业数量 eoQty
            MtEo eo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
            eoStepActualVO1.setEoQty(eo.getQty());
            // 4.a 获取执行作业步骤累计调度数量 assignQty
            MtEoDispatchActionVO2 mtEoDispatchActionVO2 = new MtEoDispatchActionVO2();
            mtEoDispatchActionVO2.setRouterStepId(dto.getRouterStepId());
            mtEoDispatchActionVO2.setWorkcellId(null);
            mtEoDispatchActionVO2.setEoId(dto.getEoId());
            mtEoDispatchActionVO7 = dispatchedEoAssignQtyGet(tenantId, mtEoDispatchActionVO2);

            MtEoStepActualVO3 mtEoStepActualVO3 = new MtEoStepActualVO3();
            mtEoStepActualVO3.setEoId(dto.getEoId());
            mtEoStepActualVO3.setRouterStepId(dto.getRouterStepId());
            List<MtEoStepActualVO4> eoStepActualVO4List =
                            mtEoStepActualRepository.operationLimitEoStepActualQuery(tenantId, mtEoStepActualVO3);
            Double unassignQty = new BigDecimal(eoStepActualVO1.getEoQty().toString())
                            .subtract(new BigDecimal(mtEoDispatchActionVO7.getAssignQty().toString())).doubleValue();
            if (CollectionUtils.isEmpty(eoStepActualVO4List)) {
                result.setUnassignQty(unassignQty);
                result.setDispatchableQty(unassignQty);
            } else {
                // update by peng.yuan 2019/10/11
                MtEoStepWorkcellActualVO7 actualVO7 = new MtEoStepWorkcellActualVO7();
                actualVO7.setEoStepActualId(eoStepActualVO4List.get(0).getEoStepActualId());
                List<MtEoStepWorkcellActualVO8> workcellActualVO8List = mtEoStepWorkcellActualRepository
                                .propertyLimitEoStepWkcActualPropertyQuery(tenantId, actualVO7);
                BigDecimal sumQueueQtyResult = BigDecimal.ZERO;
                if (CollectionUtils.isNotEmpty(workcellActualVO8List)) {
                    List<String> workcellIds = workcellActualVO8List.stream()
                                    .map(MtEoStepWorkcellActualVO8::getWorkcellId).collect(Collectors.toList());
                    for (String workcellId : workcellIds) {
                        MtEoStepWorkcellActualVO3 mtEoStepWorkcellActualVO3 = new MtEoStepWorkcellActualVO3();
                        mtEoStepWorkcellActualVO3.setEoStepActualId(eoStepActualVO4List.get(0).getEoStepActualId());
                        mtEoStepWorkcellActualVO3.setWorkcellId(workcellId);
                        List<MtEoStepWorkcellActual> queueQtyList = mtEoStepWorkcellActualRepository
                                        .eoWkcProductionResultGet(tenantId, mtEoStepWorkcellActualVO3);
                        BigDecimal sum_queueQty = BigDecimal.ZERO;
                        if (CollectionUtils.isNotEmpty(queueQtyList)) {
                            sum_queueQty = queueQtyList.stream().collect(CollectorsUtil.summingBigDecimal(
                                            t -> BigDecimal.valueOf(t.getQueueQty() == null ? 0.0D : t.getQueueQty())));
                        }
                        sumQueueQtyResult = sumQueueQtyResult.add(sum_queueQty);
                    }
                }

                result.setUnassignQty(new BigDecimal(eo.getQty().toString())
                                .subtract(new BigDecimal(mtEoDispatchActionVO7.getAssignQty().toString()))
                                .subtract(new BigDecimal(sumQueueQtyResult.toString())).doubleValue());
                result.setDispatchableQty(result.getUnassignQty());
            }
        }
        return result;
    }

    @Override
    public Long dispatchedEoPriorityGenerate(Long tenantId, MtEoDispatchActionVO1 vo) {
        // 1. 校验输入参数是否为空
        if (vo == null || StringUtils.isEmpty(vo.getWorkcellId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "workcellId", "【API：dispatchedEoPriorityGenerate】"));
        }
        if (null == vo.getShiftDate()) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftDate", "【API：dispatchedEoPriorityGenerate】"));
        }
        if (StringUtils.isEmpty(vo.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftCode", "【API：dispatchedEoPriorityGenerate】"));
        }

        // 2.获取调度完成待生产的所有执行作业的优先级
        List<MtEoDispatchActionVO5> vo5s = eoOnStandbyQuery(tenantId, vo);

        // 3.获取调度过程中的所有执行作业的优先级
        MtEoDispatchProcessVO1 mtEoDispatchProcessVO1 = new MtEoDispatchProcessVO1();
        mtEoDispatchProcessVO1.setWorkcellId(vo.getWorkcellId());
        mtEoDispatchProcessVO1.setShiftCode(vo.getShiftCode());
        mtEoDispatchProcessVO1.setShiftDate(vo.getShiftDate());
        List<MtEoDispatchProcess> processes = mtEoDispatchProcessRepository
                        .wkcShiftLimitDispatchedProcessEoQuery(tenantId, mtEoDispatchProcessVO1);

        Long priority = Long.valueOf(0L);
        // 4.获取最大优先级containerUpdate

        for (MtEoDispatchProcess process : processes) {
            if (process.getPriority() > priority) {
                priority = process.getPriority();
            }
        }

        for (MtEoDispatchActionVO5 vo5 : vo5s) {
            if (vo5.getPriority() > priority) {
                priority = vo5.getPriority();
            }
        }
        priority++;

        return priority;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoDispatchActionVO17 processLimitDispatchActionAndHistoryUpdate(Long tenantId,
                    String eoDispatchProcessId) {
        // 1.判断是否有值输入
        if (StringUtils.isEmpty(eoDispatchProcessId)) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "eoDispatchProcessId ",
                                            "【API：processLimitDispatchActionAndHistoryUpdate】"));
        }

        // 2.将调度过程数据复制新增到调度结果数据
        // 2.1根据eoDispatchProcessId获取数据MT_EO_DISPATCH_PROCESS
        MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
        mtEoDispatchProcess.setTenantId(tenantId);
        mtEoDispatchProcess.setEoDispatchProcessId(eoDispatchProcessId);
        mtEoDispatchProcess = mtEoDispatchProcessRepository.selectOne(mtEoDispatchProcess);
        // 2.2调度过程是否存在
        if (mtEoDispatchProcess == null) {
            throw new MtException("MT_DISPATCH_0014", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0014", "DISPATCH", "【API：processLimitDispatchActionAndHistoryUpdate】"));
        }
        // 2.3将调度过程数据存入调度结果中
        MtEoDispatchAction mtEoDispatchAction = new MtEoDispatchAction();
        BeanUtils.copyProperties(mtEoDispatchProcess, mtEoDispatchAction);
        mtEoDispatchAction.setTenantId(tenantId);

        // 2.3 根据调度过程表中获取到数据的operationId、shiftDate、shiftCode、workcellId，
        // 从调度结果表获取最大版本
        MtEoDispatchActionVO1 mtEoDispatchActionVO1 = new MtEoDispatchActionVO1();
        BeanUtils.copyProperties(mtEoDispatchProcess, mtEoDispatchActionVO1);

        Long maxVersion = mtEoDispatchActionMapper.getMaxRevision(tenantId, mtEoDispatchActionVO1);
        // 确认过,版本与最大一致
        if (maxVersion == null) {
            maxVersion = Long.valueOf(1L);
        }
        mtEoDispatchAction.setRevision(maxVersion);
        self().insertSelective(mtEoDispatchAction);

        // 2.4 记录调度发布历史
        MtEoDispatchHistory mtEoDispatchHistory = new MtEoDispatchHistory();
        mtEoDispatchHistory.setRevision(maxVersion);
        BeanUtils.copyProperties(mtEoDispatchProcess, mtEoDispatchHistory);
        mtEoDispatchHistory.setTenantId(tenantId);
        mtEoDispatchHistoryRepository.insertSelective(mtEoDispatchHistory);

        // 2.5 删除调度过程数据
        mtEoDispatchProcessRepository.deleteByPrimaryKey(mtEoDispatchProcess);

        // 新增返回参数
        MtEoDispatchActionVO17 result = new MtEoDispatchActionVO17();
        result.setEoDispatchActionId(mtEoDispatchAction.getEoDispatchActionId());
        result.setEoDispatchHistoryId(mtEoDispatchHistory.getEoDispatchHistoryId());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoDispatchPublish(Long tenantId, MtEoDispatchActionVO20 dto) {
        // 1.判断输入参数是否为空
        if (dto == null || StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "operationId ", "【API：eoDispatchPublish】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftCode ", "【API：eoDispatchPublish】"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftDate ", "【API：eoDispatchPublish】"));
        }
        List<String> sqlList = new ArrayList<>();
        // 2.根据输入参数获取执行作业调度过程
        MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
        mtEoDispatchProcess.setTenantId(tenantId);
        mtEoDispatchProcess.setOperationId(dto.getOperationId());
        mtEoDispatchProcess.setShiftCode(dto.getShiftCode());
        mtEoDispatchProcess.setShiftDate(dto.getShiftDate());
        mtEoDispatchProcess.setWorkcellId(dto.getWorkcellId());
        List<MtEoDispatchProcess> mtEoDispatchProcessList = mtEoDispatchProcessMapper.select(mtEoDispatchProcess);
        if (CollectionUtils.isNotEmpty(mtEoDispatchProcessList)) {
            final Long userId = DetailsHelper.getUserDetails().getUserId();
            final Date currentDate = new Date(System.currentTimeMillis());

            // 3.将第二步获取到的数据全部复制,版本存入临时值 0
            for (MtEoDispatchProcess process : mtEoDispatchProcessList) {
                MtEoDispatchAction mtEoDispatchAction = new MtEoDispatchAction();
                BeanUtils.copyProperties(process, mtEoDispatchAction);
                mtEoDispatchAction.setTenantId(tenantId);
                mtEoDispatchAction.setRevision(Long.valueOf(0L));
                self().insertSelective(mtEoDispatchAction);
                // 根据获取到的eoId调用API{ eoLimitDispatchStrategyGet }，获取过程参数dispatchStrategy

                String publishStrategy = null;
                MtEoDispatchActionVO24 strategyGet = this.eoLimitDispatchStrategyGet(tenantId, process.getEoId());
                if (strategyGet != null) {
                    publishStrategy = strategyGet.getPublishStrategy();
                }
                if (StringUtils.isEmpty(publishStrategy) || "QUEUE".equals(publishStrategy)) {

                    List<String> eoStepActualIds =
                                    mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, process.getEoId());
                    List<MtEoStepActual> eoStepActuals =
                                    this.mtEoStepActualRepository.eoStepPropertyBatchGet(tenantId, eoStepActualIds);
                    Optional<MtEoStepActual> optional = eoStepActuals.stream()
                                    .filter(t -> t.getRouterStepId().equals(process.getRouterStepId())).findFirst();
                    String eoStepActualId = null;
                    if (optional.isPresent()) {
                        eoStepActualId = optional.get().getEoStepActualId();
                    }
                    MtEoStepWipVO4 mtEoStepWipVO4 = new MtEoStepWipVO4();
                    mtEoStepWipVO4.setEoStepActualId(eoStepActualId);
                    mtEoStepWipVO4.setQueueQty(process.getAssignQty());
                    mtEoStepWipVO4.setWorkcellId(process.getWorkcellId());
                    mtEoStepWipRepository.eoWkcQueue(tenantId, mtEoStepWipVO4);

                    // 5-6步
                    // List<String> result = this.eoDispatchPublishReuse(tenantId, dto);
                    // if (CollectionUtils.isNotEmpty(result)) {
                    // sqlList.addAll(result);
                    // }

                    // 7.删除调度过程数据
                    sqlList.addAll(customDbRepository.getDeleteSql(process));
                } else if ("WITHOUT_QUEUING".equals(publishStrategy)) {

                    // 5-6步
                    // update by peng.yuan 2020 2/5 删除 5-6步
                    // sqlList.addAll(this.eoDispatchPublishReuse(tenantId, dto));
                    // 7.删除调度过程数据
                    sqlList.addAll(customDbRepository.getDeleteSql(process));
                }

                // 6.记录历史
                MtEoDispatchHistory mtEoDispatchHistory = new MtEoDispatchHistory();
                BeanUtils.copyProperties(mtEoDispatchAction, mtEoDispatchHistory);
                mtEoDispatchHistory.setTenantId(tenantId);
                mtEoDispatchHistory.setCreatedBy(userId);
                mtEoDispatchHistory.setCreationDate(currentDate);
                mtEoDispatchHistory.setLastUpdatedBy(userId);
                mtEoDispatchHistory.setLastUpdateDate(currentDate);
                mtEoDispatchHistory
                                .setEoDispatchHistoryId(this.customDbRepository.getNextKey("mt_eo_dispatch_history_s"));
                mtEoDispatchHistory.setCid(
                                Long.valueOf(this.customDbRepository.getNextKey("mt_eo_dispatch_history_cid_s")));
                sqlList.addAll(customDbRepository.getInsertSql(mtEoDispatchHistory));
            }
        }

        if (CollectionUtils.isNotEmpty(sqlList)) {
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    private List<String> eoDispatchPublishReuse(Long tenantId, MtEoDispatchActionVO20 dto) {
        List<String> sqlList = new ArrayList<>();
        // 5.删除当前调度结果已执行完成的数据并更新执行作业调度结果版本
        Long maxVersion = null;
        MtEoDispatchAction maxVersionDispatchAction = null;
        MtEoDispatchAction mtEoDispatchAction = new MtEoDispatchAction();
        mtEoDispatchAction.setTenantId(tenantId);
        mtEoDispatchAction.setOperationId(dto.getOperationId());
        mtEoDispatchAction.setShiftCode(dto.getShiftCode());
        mtEoDispatchAction.setShiftDate(dto.getShiftDate());
        mtEoDispatchAction.setWorkcellId(dto.getWorkcellId());
        List<MtEoDispatchAction> mtEoDispatchActions = mtEoDispatchActionMapper.select(mtEoDispatchAction);

        if (CollectionUtils.isNotEmpty(mtEoDispatchActions)) {
            maxVersionDispatchAction = mtEoDispatchActions.stream()
                            .sorted(Comparator.comparing(MtEoDispatchAction::getRevision).reversed()).findFirst().get();
        }

        if (maxVersionDispatchAction != null) {
            maxVersion = maxVersionDispatchAction.getRevision() + 1;
        }
        final Long userId = DetailsHelper.getUserDetails().getUserId();
        final Date currentDate = new Date(System.currentTimeMillis());
        for (MtEoDispatchAction action : mtEoDispatchActions) {
            String publishStrategy = null;
            MtEoDispatchActionVO24 strategyGet = this.eoLimitDispatchStrategyGet(tenantId, action.getEoId());
            if (strategyGet != null) {
                publishStrategy = strategyGet.getPublishStrategy();
            }
            if (StringUtils.isEmpty(publishStrategy) || "ACTUAL_DISPATCH".equals(publishStrategy)) {

                MtEoDispatchActionVO2 mtEoDispatchActionVO2 = new MtEoDispatchActionVO2();
                mtEoDispatchActionVO2.setRouterStepId(action.getRouterStepId());
                mtEoDispatchActionVO2.setWorkcellId(action.getWorkcellId());
                mtEoDispatchActionVO2.setEoId(action.getEoId());
                Double revocableQty = dispatchedEoRevocableQtyGet(tenantId, mtEoDispatchActionVO2);
                // 确认过只需要等于0就行
                // 5.删除数据
                if (BigDecimal.ZERO.compareTo(new BigDecimal(revocableQty.toString())) == 0) {
                    self().deleteByPrimaryKey(action);
                }
            } else if ("PLAN_DISPATCH".equals(publishStrategy)) {
                // 根据eoId、routerStepId、workcellId进行分组
                Map<Tuple, List<MtEoDispatchAction>> map = mtEoDispatchActions.stream().collect(Collectors
                                .groupingBy(d -> new Tuple(d.getEoId(), d.getRouterStepId(), d.getWorkcellId())));
                List<Tuple> tuples = new ArrayList<>(map.keySet());
                for (Tuple tuple : tuples) {
                    List<MtEoDispatchAction> actions = map.get(tuple);
                    if (CollectionUtils.isNotEmpty(actions)) {
                        MtEoStepActualVO3 actualVO3 = new MtEoStepActualVO3();
                        actualVO3.setEoId(tuple.getEoId());
                        actualVO3.setRouterStepId(tuple.getRouterStepId());
                        List<MtEoStepActualVO4> list =
                                        mtEoStepActualRepository.operationLimitEoStepActualQuery(tenantId, actualVO3);
                        if (CollectionUtils.isNotEmpty(list)) {
                            String eoStepActualId = list.get(0).getEoStepActualId();
                            MtEoStepWorkcellActualVO3 workcellActualVO3 = new MtEoStepWorkcellActualVO3();
                            workcellActualVO3.setEoStepActualId(eoStepActualId);
                            workcellActualVO3.setWorkcellId(tuple.getWorkcellId());
                            List<MtEoStepWorkcellActual> eoStepWorkcellActuals = mtEoStepWorkcellActualRepository
                                            .eoWkcProductionResultGet(tenantId, workcellActualVO3);

                            // 获取输出参数wipQty
                            MtEoStepWipVO1 mtEoStepWipVO1 = new MtEoStepWipVO1();
                            mtEoStepWipVO1.setEoStepActualId(eoStepActualId);
                            List<MtEoStepWip> eoStepWipList =
                                            mtEoStepWipRepository.eoWkcAndStepWipQuery(tenantId, mtEoStepWipVO1);
                            if (CollectionUtils.isNotEmpty(eoStepWipList)) {
                                BigDecimal wkcWipQueueQty = eoStepWipList.stream()
                                                .collect(CollectorsUtil.summingBigDecimal(
                                                                c -> c.getQueueQty() == null ? BigDecimal.ZERO
                                                                                : BigDecimal.valueOf(c.getQueueQty())));
                                if (CollectionUtils.isNotEmpty(eoStepWorkcellActuals)) {
                                    BigDecimal sumQueueQty = eoStepWorkcellActuals.stream().collect(CollectorsUtil
                                                    .summingBigDecimal(c -> c.getQueueQty() == null ? BigDecimal.ZERO
                                                                    : BigDecimal.valueOf(c.getQueueQty())));
                                    BigDecimal sumCompletedQty = eoStepWorkcellActuals.stream().collect(
                                                    CollectorsUtil.summingBigDecimal(c -> c.getCompletedQty() == null
                                                                    ? BigDecimal.ZERO
                                                                    : BigDecimal.valueOf(c.getCompletedQty())));
                                    if (sumQueueQty.compareTo(sumCompletedQty) == 0
                                                    && wkcWipQueueQty.compareTo(BigDecimal.ZERO) == 0) {
                                        for (MtEoDispatchAction eoDispatchAction : actions) {
                                            sqlList.addAll(customDbRepository.getDeleteSql(eoDispatchAction));
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
            // 更新版本
            action.setRevision(maxVersion);
            action.setTenantId(tenantId);
            action.setLastUpdatedBy(userId);
            action.setLastUpdateDate(currentDate);
            action.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_eo_dispatch_action_cid_s")));
            // sqlList.addAll(customDbRepository.getUpdateSql(action));

            // 6.记录历史
            MtEoDispatchHistory mtEoDispatchHistory = new MtEoDispatchHistory();
            BeanUtils.copyProperties(action, mtEoDispatchHistory);
            mtEoDispatchHistory.setTenantId(tenantId);
            mtEoDispatchHistory.setCreatedBy(userId);
            mtEoDispatchHistory.setCreationDate(currentDate);
            mtEoDispatchHistory.setLastUpdatedBy(userId);
            mtEoDispatchHistory.setLastUpdateDate(currentDate);
            mtEoDispatchHistory.setEoDispatchHistoryId(this.customDbRepository.getNextKey("mt_eo_dispatch_history_s"));
            mtEoDispatchHistory
                            .setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_eo_dispatch_history_cid_s")));
            sqlList.addAll(customDbRepository.getInsertSql(mtEoDispatchHistory));
        }
        return sqlList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String eoDispatchCancel(Long tenantId, MtEoDispatchActionVO11 dto) {
        // 校验有一个必输入
        if (null == dto || StringUtils.isEmpty(dto.getEoDispatchActionId())
                        && StringUtils.isEmpty(dto.getEoDispatchProcessId())) {
            throw new MtException("MT_DISPATCH_0002",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0002", "DISPATCH",
                                            "eoDispatchActionId,eoDispatchProcessId", "【API：eoDispatchCancel】"));
        }
        if (StringUtils.isNotEmpty(dto.getEoDispatchActionId())
                        && StringUtils.isNotEmpty(dto.getEoDispatchProcessId())) {
            throw new MtException("MT_DISPATCH_0012",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0012", "DISPATCH",
                                            "eoDispatchActionId,eoDispatchProcessId", "【API：eoDispatchCancel】"));
        }

        if (StringUtils.isNotEmpty(dto.getUndoDispatchedFlag()) && !"N".equals(dto.getUndoDispatchedFlag())
                        && !"Y".equals(dto.getUndoDispatchedFlag())) {
            throw new MtException("MT_DISPATCH_0003", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0003", "DISPATCH", "undoDispatchedFlag", "【Y，N】", "【API：eoDispatchCancel】"));
        }

        if (dto.getCancelQty() == null) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "cancelQty", "【API：eoDispatchCancel】"));
        }
        if (new BigDecimal(dto.getCancelQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_DISPATCH_0007", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0007", "DISPATCH", "cancelQty", "【API：eoDispatchCancel】"));
        }

        if (StringUtils.isNotEmpty(dto.getEoDispatchProcessId())) {
            // 调度过程取消
            MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
            mtEoDispatchProcess.setTenantId(tenantId);
            mtEoDispatchProcess.setEoDispatchProcessId(dto.getEoDispatchProcessId());
            mtEoDispatchProcess = mtEoDispatchProcessMapper.selectOne(mtEoDispatchProcess);

            if (mtEoDispatchProcess == null) {
                throw new MtException("MT_DISPATCH_0014", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0014", "DISPATCH", "【API：eoDispatchCancel】"));
            }

            BigDecimal assignQty = new BigDecimal(mtEoDispatchProcess.getAssignQty().toString());
            assignQty = assignQty.subtract(new BigDecimal(dto.getCancelQty().toString()));
            mtEoDispatchProcess.setAssignQty(assignQty.doubleValue());
            // 若更新后数量小于等于0
            if (assignQty.compareTo(BigDecimal.ZERO) <= 0) {
                // 删除
                mtEoDispatchProcessRepository.deleteByPrimaryKey(mtEoDispatchProcess);
            } else {
                // 更新数量
                mtEoDispatchProcessRepository.updateByPrimaryKeySelective(mtEoDispatchProcess);
            }
        }

        if (StringUtils.isNotEmpty(dto.getEoDispatchActionId())) {
            // 调度结果数据的撤销
            MtEoDispatchAction mtEoDispatchAction = new MtEoDispatchAction();
            mtEoDispatchAction.setTenantId(tenantId);
            mtEoDispatchAction.setEoDispatchActionId(dto.getEoDispatchActionId());
            mtEoDispatchAction = mtEoDispatchActionMapper.selectOne(mtEoDispatchAction);
            if (mtEoDispatchAction == null) {
                throw new MtException("MT_DISPATCH_0010", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0010", "DISPATCH", "【API：eoDispatchCancel】"));
            }

            String actionRouterStepId = mtEoDispatchAction.getRouterStepId();
            String actionEoId = mtEoDispatchAction.getEoId();
            String actionWorkcellId = mtEoDispatchAction.getWorkcellId();
            BigDecimal oldAssignQty = BigDecimal.valueOf(
                            mtEoDispatchAction.getAssignQty() == null ? 0.0D : mtEoDispatchAction.getAssignQty());
            // 根据获取到的eoId调用API{ eoLimitDispatchStrategyGet}，获取过程参数dispatchStrategy
            String publishStrategy = null;
            MtEoDispatchActionVO24 strategyGet = this.eoLimitDispatchStrategyGet(tenantId, actionEoId);
            if (strategyGet != null) {
                publishStrategy = strategyGet.getPublishStrategy();
            }
            if (StringUtils.isEmpty(publishStrategy) || "QUEUE".equals(publishStrategy)) {
                List<String> eoStepActualIds = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, actionEoId);
                List<MtEoStepActual> eoStepActuals =
                                this.mtEoStepActualRepository.eoStepPropertyBatchGet(tenantId, eoStepActualIds);
                eoStepActuals = eoStepActuals.stream().filter(t -> t.getRouterStepId().equals(actionRouterStepId))
                                .collect(Collectors.toList());
                for (MtEoStepActual stepActual : eoStepActuals) {
                    BigDecimal queueQty = BigDecimal
                                    .valueOf(stepActual.getQueueQty() == null ? 0.0D : stepActual.getQueueQty());
                    String eoStepActualId = stepActual.getEoStepActualId();
                    MtEoStepWorkcellActualVO3 eoStepWorkcellActualVO3 = new MtEoStepWorkcellActualVO3();
                    eoStepWorkcellActualVO3.setEoStepActualId(eoStepActualId);
                    eoStepWorkcellActualVO3.setWorkcellId(actionWorkcellId);
                    List<MtEoStepWorkcellActual> eoStepWorkcellActualList = mtEoStepWorkcellActualRepository
                                    .eoWkcProductionResultGet(tenantId, eoStepWorkcellActualVO3);

                    // eoStepWorkcellActualList不为空才去计算，不然sumQueueQty值会==null
                    BigDecimal sumQueueQty = BigDecimal.ZERO;
                    if (CollectionUtils.isNotEmpty(eoStepWorkcellActualList)) {
                        sumQueueQty = eoStepWorkcellActualList.stream().collect(CollectorsUtil.summingBigDecimal(
                                        c -> BigDecimal.valueOf(c.getQueueQty() == null ? 0.0D : c.getQueueQty())));
                    }
                    if ((queueQty.add(sumQueueQty)).compareTo(oldAssignQty) < 0) {
                        MtEoStepWipVO4 mtEoStepWipVO4 = new MtEoStepWipVO4();
                        mtEoStepWipVO4.setEoStepActualId(eoStepActualId);
                        mtEoStepWipVO4.setWorkcellId(actionWorkcellId);
                        // 取cancelQty，assignQty小的
                        if (new BigDecimal(dto.getCancelQty().toString()).compareTo(
                                        new BigDecimal(mtEoDispatchAction.getAssignQty().toString())) == -1) {
                            mtEoStepWipVO4.setQueueQty(dto.getCancelQty());
                        } else {
                            mtEoStepWipVO4.setQueueQty(mtEoDispatchAction.getAssignQty());
                        }
                        mtEoStepWipRepository.eoWkcQueueCancel(tenantId, mtEoStepWipVO4);
                    }
                }
            }

            if ("WITHOUT_QUEUING".equals(publishStrategy)) {
                BigDecimal qty = new BigDecimal(mtEoDispatchAction.getAssignQty().toString())
                                .subtract(new BigDecimal(dto.getCancelQty().toString()));
                mtEoDispatchAction.setAssignQty(qty.doubleValue());
                if (qty.compareTo(BigDecimal.ZERO) == 1) {
                    self().updateByPrimaryKeySelective(mtEoDispatchAction);
                } else {
                    // 小于等于0则删除
                    self().deleteByPrimaryKey(mtEoDispatchAction);
                }
            }

            if (!"Y".equals(dto.getUndoDispatchedFlag())) {
                // 创建调度过程数据
                MtEoDispatchProcessVO2 mtEoDispatchProcessVO2 = new MtEoDispatchProcessVO2();
                mtEoDispatchProcessVO2.setRouterStepId(actionRouterStepId);
                mtEoDispatchProcessVO2.setWorkcellId(mtEoDispatchAction.getWorkcellId());
                mtEoDispatchProcessVO2.setPriority(mtEoDispatchAction.getPriority());
                mtEoDispatchProcessVO2.setPlanStartTime(mtEoDispatchAction.getPlanStartTime());
                mtEoDispatchProcessVO2.setPlanEndTime(mtEoDispatchAction.getPlanEndTime());
                mtEoDispatchProcessVO2.setShiftDate(mtEoDispatchAction.getShiftDate());
                mtEoDispatchProcessVO2.setShiftCode(mtEoDispatchAction.getShiftCode());
                mtEoDispatchProcessVO2.setTrxAssignQty(Math.min(dto.getCancelQty(), oldAssignQty.doubleValue()));
                mtEoDispatchProcessVO2.setEoId(actionEoId);
                return mtEoDispatchProcessRepository.dispatchedEoUpdate(tenantId, mtEoDispatchProcessVO2, "N");
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sequenceLimitDispatchedEoPriorityAdjust(Long tenantId, List<MtEoDispatchActionVO15> dto) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date(System.currentTimeMillis());
        // 第一步，判断输入参数是否合规
        if (CollectionUtils.isEmpty(dto)) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "sequence", "【API：sequenceLimitDispatchedEoPriorityAdjust】"));
        }

        List<String> processIds = new ArrayList<>();
        List<MtEoDispatchActionVO12> inputProcessList = new ArrayList<>();

        List<String> actionIds = new ArrayList<>();
        List<MtEoDispatchActionVO12> inputActionList = new ArrayList<>();

        for (MtEoDispatchActionVO15 vo : dto) {
            if (vo == null || vo.getSequence() == null) {
                throw new MtException("MT_DISPATCH_0001",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                                "sequence", "【API：sequenceLimitDispatchedEoPriorityAdjust】"));
            }

            if (null == vo.getAdjustObject() || StringUtils.isEmpty(vo.getAdjustObject().getAdjustObjectId())) {
                throw new MtException("MT_DISPATCH_0001",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                                "adjustObjectId", "【API：sequenceLimitDispatchedEoPriorityAdjust】"));
            }
            if (StringUtils.isEmpty(vo.getAdjustObject().getAdjustObjectType())) {
                throw new MtException("MT_DISPATCH_0001",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                                "adjustObjectType", "【API：sequenceLimitDispatchedEoPriorityAdjust】"));
            }

            if ("PROCESS".equals(vo.getAdjustObject().getAdjustObjectType())) {
                processIds.add(vo.getAdjustObject().getAdjustObjectId());
                MtEoDispatchActionVO12 vo12 = new MtEoDispatchActionVO12();
                BeanUtils.copyProperties(vo, vo12);
                inputProcessList.add(vo12);
            }

            if ("ACTION".equals(vo.getAdjustObject().getAdjustObjectType())) {
                actionIds.add(vo.getAdjustObject().getAdjustObjectId());
                MtEoDispatchActionVO12 vo12 = new MtEoDispatchActionVO12();
                BeanUtils.copyProperties(vo, vo12);
                inputActionList.add(vo12);
            }

        }

        // 2.第二步，获取输入调度对象当前优先级

        // 获取调度过程
        if (CollectionUtils.isNotEmpty(processIds)) {
            List<MtEoDispatchProcess> mtEoDispatchProcesses =
                            mtEoDispatchProcessMapper.eoDispatchProcessQueryById(tenantId, processIds);
            // 存在数据未获取到优先级
            if (mtEoDispatchProcesses.size() != processIds.size()) {
                throw new MtException("MT_DISPATCH_0014", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0014", "DISPATCH", "【API：sequenceLimitDispatchedEoPriorityAdjust】"));
            }
            inputProcessList.forEach(vo -> {
                for (MtEoDispatchProcess mtEoDispatchProcesse : mtEoDispatchProcesses) {
                    MtEoDispatchActionVO14 vo14 = (MtEoDispatchActionVO14) vo.getAdjustObject();
                    if (vo14.getAdjustObjectId().equals(mtEoDispatchProcesse.getEoDispatchProcessId())) {
                        vo.setPriority(mtEoDispatchProcesse.getPriority());
                        break;
                    }
                }
            });
        }


        // 获取调度结果
        if (CollectionUtils.isNotEmpty(actionIds)) {
            List<MtEoDispatchAction> mtEoDispatchActions =
                            mtEoDispatchActionMapper.eoDispatchActionQueryById(tenantId, actionIds);
            // 存在数据未获取到优先级
            if (mtEoDispatchActions.size() != actionIds.size()) {
                throw new MtException("MT_DISPATCH_0010", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0010", "DISPATCH", "【API：sequenceLimitDispatchedEoPriorityAdjust】"));
            }
            inputActionList.forEach(vo -> {
                for (MtEoDispatchAction dispatchAction : mtEoDispatchActions) {
                    MtEoDispatchActionVO14 vo14 = (MtEoDispatchActionVO14) vo.getAdjustObject();
                    if (vo14.getAdjustObjectId().equals(dispatchAction.getEoDispatchActionId())) {
                        vo.setPriority(dispatchAction.getPriority());
                        break;
                    }
                }
            });
        }

        // 3.第三步，根据顺序调整优先级：

        inputActionList.addAll(inputProcessList);
        inputActionList = sequenceLimitPriorityAdjust(tenantId, inputActionList);

        List<String> sqlList = new ArrayList<>();

        for (MtEoDispatchActionVO12 vo : inputActionList) {

            MtEoDispatchActionVO14 vo14 = (MtEoDispatchActionVO14) vo.getAdjustObject();
            if ("ACTION".equals(vo14.getAdjustObjectType())) {
                MtEoDispatchAction mtEoDispatchAction = new MtEoDispatchAction();
                mtEoDispatchAction.setTenantId(tenantId);
                mtEoDispatchAction.setEoDispatchActionId(vo14.getAdjustObjectId());
                mtEoDispatchAction.setPriority(vo.getPriority());
                mtEoDispatchAction.setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_dispatch_action_cid_s")));
                mtEoDispatchAction.setLastUpdateDate(now);
                mtEoDispatchAction.setLastUpdatedBy(userId);
                sqlList.addAll(customDbRepository.getUpdateSql(mtEoDispatchAction));
            } else if ("PROCESS".equals(vo14.getAdjustObjectType())) {
                MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
                mtEoDispatchProcess.setTenantId(tenantId);
                mtEoDispatchProcess.setEoDispatchProcessId(vo14.getAdjustObjectId());
                mtEoDispatchProcess.setPriority(vo.getPriority());
                mtEoDispatchProcess.setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_dispatch_process_cid_s")));
                mtEoDispatchProcess.setLastUpdateDate(now);
                mtEoDispatchProcess.setLastUpdatedBy(userId);
                sqlList.addAll(customDbRepository.getUpdateSql(mtEoDispatchProcess));
            }

        }
        if (CollectionUtils.isEmpty(sqlList)) {
            throw new MtException("MT_DISPATCH_0019", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0019", "DISPATCH", "【API：sequenceLimitDispatchedEoPriorityAdjust】"));
        }

        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
    }

    @Override
    public List<MtEoDispatchActionVO12> sequenceLimitPriorityAdjust(Long tenantId, List<MtEoDispatchActionVO12> dto) {
        if (dto == null) {
            return Collections.emptyList();
        }
        if (dto.size() <= 1) {
            return dto;
        }

        // 对输入数据中所有的数据按优先级priority进行排序,默认升顺序
        String sortBy = dto.get(0).getSortBy();

        List<Long> priorityList = dto.stream().sorted(Comparator.comparingLong(
                        (MtEoDispatchActionVO12 t) -> null == t.getPriority() ? Long.valueOf(-1L) : t.getPriority()))
                        .map(MtEoDispatchActionVO12::getPriority).collect(Collectors.toList());

        // 将输入数据中所有的数据按照顺序sequence排序，默认升顺序
        List<MtEoDispatchActionVO12> sequence = dto.stream().sorted(Comparator.comparingLong(
                        (MtEoDispatchActionVO12 t) -> null == t.getSequence() ? Long.valueOf(-1L) : t.getSequence()))
                        .collect(Collectors.toList());

        for (int i = 0; i < dto.size(); i++) {
            sequence.get(i).setPriority(priorityList.get(i));
        }

        if ("DESC".equals(sortBy)) {
            Collections.reverse(sequence);
        }

        return sequence;
    }

    @Override
    public MtEoDispatchActionVO24 eoLimitDispatchStrategyGet(Long tenantId, String eoId) {
        // 判断条件必输
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "eoId", "【API：eoLimitDispatchStrategyGet】"));
        }
        // 第一步：根据输入的eoId调用API{ eoPropertyGet }
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, eoId);
        if (null == mtEo) {
            throw new MtException("MT_DISPATCH_0033", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0033", "DISPATCH", "eoId", "【API：eoLimitDispatchStrategyGet】"));
        }
        String productionLineId;
        if (StringUtils.isEmpty(mtEo.getProductionLineId())) {
            // 第二步：根据第二步获取到的workOrderId，调用API{ woPropertyGet }
            if (StringUtils.isEmpty(mtEo.getWorkOrderId())) {
                return null;
            }
            MtWorkOrder workOrder = mtWorkOrderRepository.woPropertyGet(tenantId, mtEo.getWorkOrderId());
            if (workOrder == null || StringUtils.isEmpty(workOrder.getProductionLineId())) {
                return null;
            }
            productionLineId = workOrder.getProductionLineId();
        } else {
            productionLineId = mtEo.getProductionLineId();
        }
        // 第三步：根据获取到的productionLineId，调用API{productionLineLimitDispatchStrategyGet}
        return productionLineLimitDispatchStrategyGet(tenantId, productionLineId);
    }

    @Override
    public List<MtEoDispatchActionVO19> propertyLimitDispatchedActionPropertyQuery(Long tenantId,
                    MtEoDispatchActionVO18 vo) {
        // 根据条件查询数据
        List<MtEoDispatchAction> dispatchActions =
                        mtEoDispatchActionMapper.propertyLimitDispatchedActionPropertyQuery(tenantId, vo);
        List<MtEoDispatchActionVO19> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dispatchActions)) {
            // 根据第一步获取到的工作单元 workcellId列表，调用API{ workcellBasicPropertyBatchGet }获取工作单元短描述、工作单元长描述和工作单元编码
            List<String> workCellIds = dispatchActions.stream().map(MtEoDispatchAction::getWorkcellId)
                            .collect(Collectors.toList());
            List<MtModWorkcell> mtModWorkcells =
                            mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workCellIds);
            Map<String, MtModWorkcell> workcellMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(mtModWorkcells)) {
                workcellMap = mtModWorkcells.stream().collect(Collectors.toMap(MtModWorkcell::getWorkcellId, t -> t));
            }

            // 根据第一步获取到的工艺 operationId列表，调用API{operationBatchGet }获取工艺短描述、工艺长描述
            List<String> operationIds = dispatchActions.stream().map(MtEoDispatchAction::getOperationId)
                            .collect(Collectors.toList());
            List<MtOperation> mtOperations = mtOperationRepository.operationBatchGet(tenantId, operationIds);
            Map<String, MtOperation> operationMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(mtOperations)) {
                operationMap = mtOperations.stream().collect(Collectors.toMap(MtOperation::getOperationId, t -> t));
            }

            // 根据第一步获取到的执行作业 eoId列表，调用API{eoPropertyBatchGet }获取执行作业编号
            List<String> eoIds = dispatchActions.stream().map(MtEoDispatchAction::getEoId).collect(Collectors.toList());
            List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
            Map<String, String> eoNums = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(mtEos)) {
                eoNums = mtEos.stream().collect(Collectors.toMap(MtEo::getEoId, MtEo::getEoNum));
            }

            // 根据第一步获取到的工艺路线步骤 stepId列表，调用API{ routerStepBatchGet }获取工艺路线步骤描述
            List<String> stepIds = dispatchActions.stream().map(MtEoDispatchAction::getRouterStepId)
                            .collect(Collectors.toList());
            List<MtRouterStep> mtRouterSteps = mtRouterStepRepository.routerStepBatchGet(tenantId, stepIds);
            Map<String, String> stepNames = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(mtRouterSteps)) {
                stepNames = mtRouterSteps.stream()
                                .collect(Collectors.toMap(MtRouterStep::getRouterStepId, MtRouterStep::getStepName));
            }
            for (MtEoDispatchAction action : dispatchActions) {
                MtEoDispatchActionVO19 vo19 = new MtEoDispatchActionVO19();
                vo19.setEoDispatchActionId(action.getEoDispatchActionId());
                vo19.setEoId(action.getEoId());
                vo19.setEoNum(eoNums.get(action.getEoId()));
                vo19.setRouterStepId(action.getRouterStepId());
                vo19.setStepName(stepNames.get(action.getRouterStepId()));
                vo19.setOperationId(action.getOperationId());
                vo19.setOperationName(null == operationMap.get(action.getOperationId()) ? null
                                : operationMap.get(action.getOperationId()).getOperationName());
                vo19.setOperationDescription(null == operationMap.get(action.getOperationId()) ? null
                                : operationMap.get(action.getOperationId()).getDescription());
                vo19.setPriority(action.getPriority());
                vo19.setPlanStartTime(action.getPlanStartTime());
                vo19.setPlanEndTime(action.getPlanEndTime());
                vo19.setShiftDate(action.getShiftDate());
                vo19.setShiftCode(action.getShiftCode());
                vo19.setWorkcellId(action.getWorkcellId());
                vo19.setWorkcellCode(null == workcellMap.get(action.getWorkcellId()) ? null
                                : workcellMap.get(action.getWorkcellId()).getWorkcellCode());
                vo19.setWorkcellName(null == workcellMap.get(action.getWorkcellId()) ? null
                                : workcellMap.get(action.getWorkcellId()).getWorkcellName());
                vo19.setWorkcellDescription(null == workcellMap.get(action.getWorkcellId()) ? null
                                : workcellMap.get(action.getWorkcellId()).getDescription());
                vo19.setStatus(action.getStatus());
                vo19.setRevision(action.getRevision());
                vo19.setAssignQty(action.getAssignQty());
                result.add(vo19);
            }
        }
        return result;
    }

    @Override
    public List<MtEoDispatchActionVO19> propertyLimitDispatchedActionPropertyBatchQuery(Long tenantId,
                    MtEoDispatchActionVO21 vo) {
        // 根据条件查询数据
        List<MtEoDispatchAction> dispatchActions =
                        mtEoDispatchActionMapper.propertyLimitDispatchedActionPropertyBatchQuery(tenantId, vo);
        List<MtEoDispatchActionVO19> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dispatchActions)) {
            // 根据第一步获取到的工作单元 workcellId列表，调用API{ workcellBasicPropertyBatchGet }获取工作单元短描述、工作单元长描述和工作单元编码
            List<String> workCellIds = dispatchActions.stream().map(MtEoDispatchAction::getWorkcellId)
                            .collect(Collectors.toList());
            List<MtModWorkcell> mtModWorkcells =
                            mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workCellIds);
            Map<String, MtModWorkcell> workcellMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(mtModWorkcells)) {
                workcellMap = mtModWorkcells.stream().collect(Collectors.toMap(MtModWorkcell::getWorkcellId, t -> t));
            }

            // 根据第一步获取到的工艺 operationId列表，调用API{operationBatchGet }获取工艺短描述、工艺长描述
            List<String> operationIds = dispatchActions.stream().map(MtEoDispatchAction::getOperationId)
                            .collect(Collectors.toList());
            List<MtOperation> mtOperations = mtOperationRepository.operationBatchGet(tenantId, operationIds);
            Map<String, MtOperation> operationMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(mtOperations)) {
                operationMap = mtOperations.stream().collect(Collectors.toMap(MtOperation::getOperationId, t -> t));
            }

            // 根据第一步获取到的执行作业 eoId列表，调用API{eoPropertyBatchGet }获取执行作业编号
            List<String> eoIds = dispatchActions.stream().map(MtEoDispatchAction::getEoId).collect(Collectors.toList());
            List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
            Map<String, String> eoNums = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(mtEos)) {
                eoNums = mtEos.stream().collect(Collectors.toMap(MtEo::getEoId, MtEo::getEoNum));
            }

            // 根据第一步获取到的工艺路线步骤 stepId列表，调用API{ routerStepBatchGet }获取工艺路线步骤描述
            List<String> stepIds = dispatchActions.stream().map(MtEoDispatchAction::getRouterStepId)
                            .collect(Collectors.toList());
            List<MtRouterStep> mtRouterSteps = mtRouterStepRepository.routerStepBatchGet(tenantId, stepIds);
            Map<String, String> stepNames = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(mtRouterSteps)) {
                stepNames = mtRouterSteps.stream()
                                .collect(Collectors.toMap(MtRouterStep::getRouterStepId, MtRouterStep::getStepName));
            }
            for (MtEoDispatchAction action : dispatchActions) {
                MtEoDispatchActionVO19 vo19 = new MtEoDispatchActionVO19();
                vo19.setEoDispatchActionId(action.getEoDispatchActionId());
                vo19.setEoId(action.getEoId());
                vo19.setEoNum(eoNums.get(action.getEoId()));
                vo19.setRouterStepId(action.getRouterStepId());
                vo19.setStepName(stepNames.get(action.getRouterStepId()));
                vo19.setOperationId(action.getOperationId());
                vo19.setOperationName(null == operationMap.get(action.getOperationId()) ? null
                                : operationMap.get(action.getOperationId()).getOperationName());
                vo19.setOperationDescription(null == operationMap.get(action.getOperationId()) ? null
                                : operationMap.get(action.getOperationId()).getDescription());
                vo19.setPriority(action.getPriority());
                vo19.setPlanStartTime(action.getPlanStartTime());
                vo19.setPlanEndTime(action.getPlanEndTime());
                vo19.setShiftDate(action.getShiftDate());
                vo19.setShiftCode(action.getShiftCode());
                vo19.setWorkcellId(action.getWorkcellId());
                vo19.setWorkcellCode(null == workcellMap.get(action.getWorkcellId()) ? null
                                : workcellMap.get(action.getWorkcellId()).getWorkcellCode());
                vo19.setWorkcellName(null == workcellMap.get(action.getWorkcellId()) ? null
                                : workcellMap.get(action.getWorkcellId()).getWorkcellName());
                vo19.setWorkcellDescription(null == workcellMap.get(action.getWorkcellId()) ? null
                                : workcellMap.get(action.getWorkcellId()).getDescription());
                vo19.setStatus(action.getStatus());
                vo19.setRevision(action.getRevision());
                vo19.setAssignQty(action.getAssignQty());
                result.add(vo19);
            }
        }
        return result;
    }

    public static class Tuple {
        private String eoId;
        private String routerStepId;
        private String workcellId;

        public String getEoId() {
            return eoId;
        }

        public void setEoId(String eoId) {
            this.eoId = eoId;
        }

        public String getRouterStepId() {
            return routerStepId;
        }

        public void setRouterStepId(String routerStepId) {
            this.routerStepId = routerStepId;
        }

        public String getWorkcellId() {
            return workcellId;
        }

        public void setWorkcellId(String workcellId) {
            this.workcellId = workcellId;
        }

        public Tuple(String eoId, String routerStepId, String workcellId) {
            this.eoId = eoId;
            this.routerStepId = routerStepId;
            this.workcellId = workcellId;
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
            return Objects.equals(eoId, tuple.eoId) && Objects.equals(routerStepId, tuple.routerStepId)
                            && Objects.equals(workcellId, tuple.workcellId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(eoId, routerStepId, workcellId);
        }
    }

    @Override
    public MtEoDispatchActionVO24 eoLimitDispatchStrategyBatchGet(Long tenantId, List<String> eoIds) {
        // 判断条件必输
        if (CollectionUtils.isEmpty(eoIds)) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "eoIdList", "【API：eoLimitDispatchStrategyBatchGet】"));
        }
        // 查询EO 去重
        List<String> disEoIds = new ArrayList<>(eoIds).stream().distinct().collect(Collectors.toList());
        List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, disEoIds);
        // 存在EO不存在则报错
        if (CollectionUtils.isEmpty(mtEos) || mtEos.size() != disEoIds.size()) {
            List<String> temp = mtEos.stream().map(MtEo::getEoId).collect(Collectors.toList());
            disEoIds.removeAll(temp);
            throw new MtException("MT_DISPATCH_0033",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0033", "DISPATCH",
                                            disEoIds.toString(), "【API：eoLimitDispatchStrategyBatchGet】"));
        }
        Map<String, String> eoIdMap =
                        mtEos.stream().collect(Collectors.toMap(MtEo::getEoId, MtEo::getProductionLineId));

        Map<String, String> woToEoMap = mtEos.stream().filter(t -> StringUtils.isEmpty(t.getProductionLineId()))
                        .collect(Collectors.toMap(MtEo::getEoId, MtEo::getWorkOrderId));
        // 获取productionLineId
        if (MapUtils.isNotEmpty(woToEoMap)) {
            List<MtWorkOrder> mtWorkOrders =
                            mtWorkOrderRepository.woPropertyBatchGet(tenantId, new ArrayList<>(woToEoMap.values()));
            if (CollectionUtils.isNotEmpty(mtWorkOrders)) {
                Map<String, String> woMap = mtWorkOrders.stream().collect(
                                Collectors.toMap(MtWorkOrder::getWorkOrderId, MtWorkOrder::getProductionLineId));
                woToEoMap.entrySet().stream().forEach(c -> {
                    eoIdMap.put(c.getKey(), woMap.get(c.getValue()));
                });
            }
        }

        // 汇总第一步和第二步获取到的所有productionLineId，判断第一步或是否一致，若不一致则报错
        List<String> productionLineIds = eoIdMap.values().stream().distinct().filter(t -> StringUtils.isNotEmpty(t))
                        .collect(Collectors.toList());

        if (productionLineIds.size() != 1) {
            throw new MtException("MT_DISPATCH_0036", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0036", "DISPATCH", "【API：eoLimitDispatchStrategyBatchGet】"));
        }
        return mtEoDispatchActionRepository.productionLineLimitDispatchStrategyGet(tenantId, productionLineIds.get(0));
    }

    @Override
    public List<MtEoDispatchActionVO23> toBeDispatchedEoUnassignQtyBatchGet(Long tenantId,
                    List<MtEoStepActualVO37> dto) {
        // 判断条件必输

        if (CollectionUtils.isEmpty(dto)) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "eoMessageList", "【API：toBeDispatchedEoUnassignQtyBatchGet】"));
        }
        if (dto.stream().anyMatch(c -> StringUtils.isEmpty(c.getEoId()))) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "eoId", "【API：toBeDispatchedEoUnassignQtyBatchGet】"));
        }
        if (dto.stream().anyMatch(c -> StringUtils.isEmpty(c.getRouterStepId()))) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "routerStepId", "【API：toBeDispatchedEoUnassignQtyBatchGet】"));
        }
        // 获取执行作业以及执行作业步骤实绩属性
        List<String> eoIds = dto.stream().map(MtEoStepActualVO37::getEoId).distinct().collect(Collectors.toList());
        List<MtEoDispatchActionVO22> mtEoDispatchActionVO22s = new ArrayList<>();
        MtEoDispatchActionVO24 mtEoDispatchActionVO24 = null;
        if (CollectionUtils.isNotEmpty(eoIds)) {
            mtEoDispatchActionVO24 = eoLimitDispatchStrategyBatchGet(tenantId, eoIds);

            for (String eoId : eoIds) {
                MtEoDispatchActionVO22 vo22 = new MtEoDispatchActionVO22();
                vo22.setEoId(eoId);
                if (null != mtEoDispatchActionVO24) {
                    vo22.setDispatchStrategy(mtEoDispatchActionVO24.getRangeStrategy());
                }
                mtEoDispatchActionVO22s.add(vo22);
            }
        }


        if (CollectionUtils.isEmpty(mtEoDispatchActionVO22s)) {
            return Collections.emptyList();
        }
        Map<String, String> eoDisMap = mtEoDispatchActionVO22s.stream().collect(
                        Collectors.toMap(MtEoDispatchActionVO22::getEoId, MtEoDispatchActionVO22::getDispatchStrategy));

        // 根据EO获取获取执行作业
        List<String> actionEoIds = mtEoDispatchActionVO22s.stream().map(MtEoDispatchActionVO22::getEoId)
                        .collect(Collectors.toList());
        List<MtEo> mtEos = CollectionUtils.isEmpty(actionEoIds) ? Collections.emptyList()
                        : mtEoRepository.eoPropertyBatchGet(tenantId, actionEoIds);
        Map<String, Double> eoQtyMap = CollectionUtils.isEmpty(mtEos) ? new HashMap<>()
                        : mtEos.stream().collect(Collectors.toMap(MtEo::getEoId, MtEo::getQty));

        // 获取eoStepActualId
        List<MtEoStepActualVO36> mtEoStepActualVO36s =
                        mtEoStepActualRepository.eoAndStepLimitStepActualBatchQuery(tenantId, dto);
        Map<EoRouterStepIdTuple, String> vo36Map =
                        CollectionUtils.isEmpty(mtEoStepActualVO36s) ? new HashMap<>()
                                        : mtEoStepActualVO36s.stream().collect(Collectors.toMap(
                                                        c -> new EoRouterStepIdTuple(c.getEoId(), c.getRouterStepId()),
                                                        MtEoStepActualVO36::getEoStepActualId));

        // 获取执行作业步骤实绩累计排队数量
        List<String> eoStepActualIds = mtEoStepActualVO36s.stream().map(MtEoStepActualVO36::getEoStepActualId)
                        .distinct().collect(Collectors.toList());
        List<MtEoStepActualVO35> actualVO35s = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(eoStepActualIds)) {
            actualVO35s = mtEoStepActualRepository.eoStepActualProcessedBatchGet(tenantId, eoStepActualIds);
        }
        Map<String, Double> queueMap = CollectionUtils.isEmpty(actualVO35s) ? new HashMap<>()
                        : actualVO35s.stream().collect(Collectors.toMap(MtEoStepActualVO35::getEoStepActualId,
                                        MtEoStepActualVO35::getQueueQty));

        List<MtEoStepActualVO37> mtEoStepActualVO37s = new ArrayList<>(dto.size());
        dto.forEach(c -> {
            MtEoStepActualVO37 temp = new MtEoStepActualVO37();
            temp.setEoId(c.getEoId());
            temp.setRouterStepId(c.getRouterStepId());
            mtEoStepActualVO37s.add(temp);
        });

        // 获取执行作业步骤累计已调度数量
        MtEoDispatchProcessVO15 mtEoDispatchProcessVO15s = new MtEoDispatchProcessVO15();
        mtEoDispatchProcessVO15s.setEoMessageList(mtEoStepActualVO37s);
        List<MtEoDispatchProcessVO14> mtEoDispatchProcessVO14s =
                        mtEoDispatchProcessRepository.dispatchedEoAssignQtyBatchGet(tenantId, mtEoDispatchProcessVO15s);

        Map<EoRouterStepIdTuple, Double> assignQtyMap =
                        CollectionUtils.isEmpty(mtEoDispatchProcessVO14s) ? new HashMap<>()
                                        : mtEoDispatchProcessVO14s.stream().collect(Collectors.toMap(
                                                        c -> new EoRouterStepIdTuple(c.getEoId(), c.getRouterStepId()),
                                                        MtEoDispatchProcessVO14::getAssignQty));

        // 获取输出参数workcellId
        MtEoStepWorkcellActualVO13 mtEoStepWorkcellActualVO13 = new MtEoStepWorkcellActualVO13();
        mtEoStepWorkcellActualVO13.setEoStepActualIdList(eoStepActualIds);
        List<MtEoStepWorkcellActualVO14> workcellActualVO14s =
                        CollectionUtils.isEmpty(eoStepActualIds) ? Collections.emptyList()
                                        : mtEoStepWorkcellActualRepository
                                                        .propertyLimitEoStepWkcActualPropertyBatchQuery(tenantId,
                                                                        mtEoStepWorkcellActualVO13);

        // 获取排队数量
        List<MtEoStepWorkcellActualVO3> workcellActualVO3s = new ArrayList<>();
        workcellActualVO14s.forEach(c -> {
            MtEoStepWorkcellActualVO3 temp = new MtEoStepWorkcellActualVO3();
            temp.setEoStepActualId(c.getEoStepActualId());
            temp.setWorkcellId(c.getWorkcellId());
            workcellActualVO3s.add(temp);
        });

        List<MtEoStepWorkcellActual> mtEoStepWorkcellActuals = CollectionUtils.isEmpty(workcellActualVO3s)
                        ? Collections.emptyList()
                        : mtEoStepWorkcellActualRepository.eoWkcProductionResultBatchGet(tenantId, workcellActualVO3s);
        Map<String, BigDecimal> sumQueueMap = CollectionUtils.isEmpty(mtEoStepWorkcellActuals) ? new HashMap<>()
                        : mtEoStepWorkcellActuals.stream()
                                        .collect(Collectors.groupingBy(MtEoStepWorkcellActual::getEoStepActualId,
                                                        CollectorsUtil.summingBigDecimal(c -> c.getQueueQty() == null
                                                                        ? BigDecimal.ZERO
                                                                        : BigDecimal.valueOf(c.getQueueQty()))));

        // 计算数量
        List<MtEoDispatchActionVO23> result = new ArrayList<>(dto.size());
        MtEoDispatchActionVO23 mtEoDispatchActionVO23;
        EoRouterStepIdTuple tuple;
        String eoStepActualId;
        Double eoQty;
        Double queueQty;
        Double assignQty;
        BigDecimal sumQueueQty;
        BigDecimal unassignQty;
        for (MtEoStepActualVO37 c : dto) {
            mtEoDispatchActionVO23 = new MtEoDispatchActionVO23();
            mtEoDispatchActionVO23.setEoId(c.getEoId());
            mtEoDispatchActionVO23.setRouterStepId(c.getRouterStepId());
            tuple = new EoRouterStepIdTuple(c.getEoId(), c.getRouterStepId());
            eoStepActualId = (MapUtils.isEmpty(vo36Map) || vo36Map.get(tuple) == null) ? null : vo36Map.get(tuple);
            eoQty = (MapUtils.isEmpty(eoQtyMap) || eoQtyMap.get(c.getEoId()) == null) ? Double.valueOf(0.0D)
                            : eoQtyMap.get(c.getEoId());
            queueQty = (MapUtils.isEmpty(queueMap) || StringUtils.isEmpty(eoStepActualId)
                            || queueMap.get(eoStepActualId) == null) ? Double.valueOf(0.0D)
                                            : queueMap.get(eoStepActualId);
            assignQty = (MapUtils.isEmpty(assignQtyMap) || assignQtyMap.get(tuple) == null) ? Double.valueOf(0.0D)
                            : assignQtyMap.get(tuple);
            unassignQty = BigDecimal.valueOf(eoQty).subtract(BigDecimal.valueOf(assignQty));
            if ("ACTUAL_DISPATCH".equalsIgnoreCase(eoDisMap.get(c.getEoId()))) {
                mtEoDispatchActionVO23.setUnassignQty(unassignQty.doubleValue());
                mtEoDispatchActionVO23.setDispatchableQty(
                                BigDecimal.valueOf(queueQty).subtract(BigDecimal.valueOf(assignQty)).doubleValue());
                result.add(mtEoDispatchActionVO23);
                continue;
            }

            if ("PLAN_DISPATCH".equalsIgnoreCase(eoDisMap.get(c.getEoId()))) {
                if (StringUtils.isEmpty(eoStepActualId)) {
                    mtEoDispatchActionVO23.setUnassignQty(unassignQty.doubleValue());
                } else {
                    sumQueueQty = (MapUtils.isEmpty(sumQueueMap) || sumQueueMap.get(eoStepActualId) == null)
                                    ? BigDecimal.ZERO
                                    : sumQueueMap.get(eoStepActualId);
                    mtEoDispatchActionVO23.setUnassignQty(unassignQty.subtract(sumQueueQty).doubleValue());
                }
                mtEoDispatchActionVO23.setDispatchableQty(mtEoDispatchActionVO23.getUnassignQty());
                result.add(mtEoDispatchActionVO23);
            }
        }
        return result;
    }


    @Override
    public MtEoDispatchActionVO24 productionLineLimitDispatchStrategyGet(Long tenantId, String productionLineId) {
        // 返回结果
        MtEoDispatchActionVO24 result = new MtEoDispatchActionVO24();
        // 1、参数合规性校验
        if (StringUtils.isEmpty(productionLineId)) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "productionLineId", "【API：productionLineLimitDispatchStrategyGet】"));
        }

        // 2、获取调度策略
        // 调用调用API{organizationLimitDispatchStrategyQuery}传入参数
        MtDispatchStrategyOrgRelVO2 orgRelVO2 = new MtDispatchStrategyOrgRelVO2();
        orgRelVO2.setOrganizationType("PROD_LINE");
        orgRelVO2.setOrganizationId(productionLineId);
        List<MtDispatchStrategyOrgRelVO3> strategyOrgRels =
                        mtDispatchStrategyOrgRelRepository.organizationLimitDispatchStrategyQuery(tenantId, orgRelVO2);
        // 确认获取一个
        if (CollectionUtils.isNotEmpty(strategyOrgRels)) {
            MtDispatchStrategyOrgRelVO3 vo3 = strategyOrgRels.get(0);
            result.setPublishStrategy(vo3.getPublishStrategy());
            result.setRangeStrategy(vo3.getRangeStrategy());
            result.setMoveStrategy(vo3.getMoveStrategy());
            return result;
        } else {
            // 获取生产线上层区域调度策略
            // 获取顶层站点，调用API{ organizationLimitSiteQuery}传入参数
            MtModOrganizationRelVO relVO = new MtModOrganizationRelVO();
            relVO.setOrganizationId(productionLineId);
            relVO.setOrganizationType("PROD_LINE");
            List<String> siteIds = mtModOrganizationRelRepository.organizationLimitSiteQuery(tenantId, relVO);

            if (CollectionUtils.isNotEmpty(siteIds)) {
                // 获取上层区域，调用API{ parentOrganizationRelQuery}
                MtModOrganizationVO2 vo2 = new MtModOrganizationVO2();
                vo2.setTopSiteId(siteIds.get(0));
                vo2.setOrganizationType("PROD_LINE");
                vo2.setOrganizationId(productionLineId);
                vo2.setParentOrganizationType("AREA");
                vo2.setQueryType("BOTTOM");

                List<MtModOrganizationItemVO> itemVOS =
                                mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, vo2);

                if (CollectionUtils.isNotEmpty(itemVOS) && StringUtils.isNotEmpty(itemVOS.get(0).getOrganizationId())) {
                    // 获取区域调度策略，调用API{organizationLimitDispatchStrategyQuery}
                    orgRelVO2 = new MtDispatchStrategyOrgRelVO2();
                    orgRelVO2.setOrganizationType("AREA");
                    orgRelVO2.setOrganizationId(itemVOS.get(0).getOrganizationId());
                    List<MtDispatchStrategyOrgRelVO3> orgRels = mtDispatchStrategyOrgRelRepository
                                    .organizationLimitDispatchStrategyQuery(tenantId, orgRelVO2);

                    if (CollectionUtils.isNotEmpty(orgRels)) {
                        MtDispatchStrategyOrgRelVO3 vo3 = orgRels.get(0);
                        result.setPublishStrategy(vo3.getPublishStrategy());
                        result.setRangeStrategy(vo3.getRangeStrategy());
                        result.setMoveStrategy(vo3.getMoveStrategy());
                        return result;
                    } else {
                        // 第四步，获取区域上层站点调度策略
                        // 生产线获取到区域但未获取到调度策略调用API{parentOrganizationRelQuery}传入参数
                        return returnDispatchStrategy(tenantId, siteIds.get(0), itemVOS.get(0).getOrganizationId(),
                                        productionLineId);
                    }
                } else {
                    // 第三步获取区域上层站点调度策略
                    // 生产线未获取到区域，调用API{parentOrganizationRelQuery}
                    vo2 = new MtModOrganizationVO2();
                    vo2.setTopSiteId(siteIds.get(0));
                    vo2.setOrganizationType("PROD_LINE");
                    vo2.setOrganizationId(productionLineId);
                    vo2.setParentOrganizationType("SITE");
                    List<MtModOrganizationItemVO> vos =
                                    mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, vo2);

                    if (CollectionUtils.isNotEmpty(vos)) {
                        orgRelVO2 = new MtDispatchStrategyOrgRelVO2();
                        orgRelVO2.setOrganizationType("SITE");
                        orgRelVO2.setOrganizationId(vos.get(0).getOrganizationId());
                        List<MtDispatchStrategyOrgRelVO3> relVO3s = mtDispatchStrategyOrgRelRepository
                                        .organizationLimitDispatchStrategyQuery(tenantId, orgRelVO2);

                        if (CollectionUtils.isNotEmpty(relVO3s)) {
                            MtDispatchStrategyOrgRelVO3 vo3 = relVO3s.get(0);
                            result.setPublishStrategy(vo3.getPublishStrategy());
                            result.setRangeStrategy(vo3.getRangeStrategy());
                            result.setMoveStrategy(vo3.getMoveStrategy());
                            return result;
                        }
                    }
                }
            }
        }
        // 若以上均未获取到数据，则默认
        result.setPublishStrategy("WITHOUT_QUEUING");
        result.setRangeStrategy("PLAN_DISPATCH");
        result.setMoveStrategy("");
        return result;
    }

    private MtEoDispatchActionVO24 returnDispatchStrategy(Long tenantId, String siteId, String organizationId,
                    String productionLineId) {
        MtEoDispatchActionVO24 result = new MtEoDispatchActionVO24();
        MtModOrganizationVO2 vo2 = new MtModOrganizationVO2();
        MtDispatchStrategyOrgRelVO2 orgRelVO2;
        vo2.setTopSiteId(siteId);
        vo2.setOrganizationType("AREA");
        vo2.setOrganizationId(organizationId);
        vo2.setParentOrganizationType("AREA");
        vo2.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> vos = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, vo2);
        if (CollectionUtils.isNotEmpty(vos)) {
            orgRelVO2 = new MtDispatchStrategyOrgRelVO2();
            orgRelVO2.setOrganizationType("AREA");
            orgRelVO2.setOrganizationId(vos.get(0).getOrganizationId());
            List<MtDispatchStrategyOrgRelVO3> relVO3s = mtDispatchStrategyOrgRelRepository
                            .organizationLimitDispatchStrategyQuery(tenantId, orgRelVO2);

            if (CollectionUtils.isNotEmpty(relVO3s)) {
                MtDispatchStrategyOrgRelVO3 vo3 = relVO3s.get(0);
                result.setPublishStrategy(vo3.getPublishStrategy());
                result.setRangeStrategy(vo3.getRangeStrategy());
                result.setMoveStrategy(vo3.getMoveStrategy());
                return result;
            } else {
                return returnDispatchStrategy(tenantId, siteId, vos.get(0).getOrganizationId(), productionLineId);
            }
        } else {
            // 生产线未获取到区域，调用API{parentOrganizationRelQuery}
            vo2 = new MtModOrganizationVO2();
            vo2.setTopSiteId(siteId);
            vo2.setOrganizationType("PROD_LINE");
            vo2.setOrganizationId(productionLineId);
            vo2.setParentOrganizationType("SITE");
            List<MtModOrganizationItemVO> organizations =
                            mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, vo2);

            if (CollectionUtils.isNotEmpty(organizations)) {
                orgRelVO2 = new MtDispatchStrategyOrgRelVO2();
                orgRelVO2.setOrganizationType("SITE");
                orgRelVO2.setOrganizationId(organizations.get(0).getOrganizationId());
                List<MtDispatchStrategyOrgRelVO3> relVO3s = mtDispatchStrategyOrgRelRepository
                                .organizationLimitDispatchStrategyQuery(tenantId, orgRelVO2);

                if (CollectionUtils.isNotEmpty(relVO3s)) {
                    MtDispatchStrategyOrgRelVO3 vo3 = relVO3s.get(0);
                    result.setPublishStrategy(vo3.getPublishStrategy());
                    result.setRangeStrategy(vo3.getRangeStrategy());
                    result.setMoveStrategy(vo3.getMoveStrategy());
                    return result;
                }
            }
        }
        result.setPublishStrategy("WITHOUT_QUEUING");
        result.setRangeStrategy("PLAN_DISPATCH");
        result.setMoveStrategy("");
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dispatchActionDelete(Long tenantId, MtEoDispatchActionVO20 dto) {
        // 第一步：判断输入合规
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "operationId", "【API：dispatchActionDelete】"));
        }

        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftCode", "【API：dispatchActionDelete】"));
        }

        if (dto.getShiftDate() == null) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftDate", "【API：dispatchActionDelete】"));
        }

        // 第二步，删除当前调度结果已执行完成的数据并更新执行作业调度结果版本
        MtEoDispatchAction action = new MtEoDispatchAction();
        action.setTenantId(tenantId);
        action.setOperationId(dto.getOperationId());
        action.setShiftCode(dto.getShiftCode());
        action.setShiftDate(dto.getShiftDate());
        action.setWorkcellId(dto.getWorkcellId());
        List<MtEoDispatchAction> dispatchActions = mtEoDispatchActionMapper.select(action);

        // 批量执行语句
        List<String> sqlList = new ArrayList<>();
        // 最大版本
        Long maxVersion = null;

        List<String> actionCids = customDbRepository.getNextKeys("mt_eo_dispatch_action_cid_s", dispatchActions.size());

        List<String> historyIds = customDbRepository.getNextKeys("mt_eo_dispatch_history_s", dispatchActions.size());
        List<String> historyCids =
                        customDbRepository.getNextKeys("mt_eo_dispatch_history_cid_s", dispatchActions.size());

        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date(System.currentTimeMillis());
        int num = 0;

        // 获取到的版本的最大值增加 1
        if (CollectionUtils.isNotEmpty(dispatchActions)) {
            Long version = dispatchActions.stream().map(MtEoDispatchAction::getRevision).filter(Objects::nonNull)
                            .max(Long::compareTo).get();
            if (version != null) {
                maxVersion = version + 1;
            }
        }

        for (MtEoDispatchAction dispatchAction : dispatchActions) {
            // 根据获取到的eoId调用API{ eoLimitDispatchStrategyGet }，获取过程参数publishStrategy
            String publishStrategy = null;
            MtEoDispatchActionVO24 strategyGet = this.eoLimitDispatchStrategyGet(tenantId, dispatchAction.getEoId());
            if (strategyGet != null) {
                publishStrategy = strategyGet.getPublishStrategy();
            }

            if ("QUEUE".equals(publishStrategy)) {
                // 调用API{ dispatchedEoRevocableQtyGet }获取执行作业调度结果步骤上可修改数量revocableQty
                MtEoDispatchActionVO2 mtEoDispatchActionVO2 = new MtEoDispatchActionVO2();
                mtEoDispatchActionVO2.setRouterStepId(dispatchAction.getRouterStepId());
                mtEoDispatchActionVO2.setWorkcellId(dispatchAction.getWorkcellId());
                mtEoDispatchActionVO2.setEoId(dispatchAction.getEoId());
                Double revocableQty = dispatchedEoRevocableQtyGet(tenantId, mtEoDispatchActionVO2);

                // 若revocableQty = 0，删除MT_EO_DISPATCH_ACTION中对应数据
                if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(revocableQty)) == 0) {
                    sqlList.addAll(customDbRepository.getDeleteSql(dispatchAction));
                }

            } else if ("WITHOUT_QUEUING".equals(publishStrategy)) {
                // 根据eoId、routerStepId、workcellId进行分组
                Map<Tuple, List<MtEoDispatchAction>> map = dispatchActions.stream().collect(Collectors
                                .groupingBy(d -> new Tuple(d.getEoId(), d.getRouterStepId(), d.getWorkcellId())));
                List<Tuple> tuples = new ArrayList<>(map.keySet());
                for (Tuple tuple : tuples) {
                    List<MtEoDispatchAction> actions = map.get(tuple);

                    // 分别输入各组的eoId、routerStepId调用API{operationLimitEoStepActualQuery}获取输出参数eoStepActualId
                    MtEoStepActualVO3 actualVO3 = new MtEoStepActualVO3();
                    actualVO3.setEoId(tuple.getEoId());
                    actualVO3.setRouterStepId(tuple.getRouterStepId());
                    List<MtEoStepActualVO4> list =
                                    mtEoStepActualRepository.operationLimitEoStepActualQuery(tenantId, actualVO3);

                    // 若获取不为空时，执行B
                    if (CollectionUtils.isNotEmpty(list)) {
                        String eoStepActualId = list.get(0).getEoStepActualId();
                        MtEoStepWorkcellActualVO3 workcellActualVO3 = new MtEoStepWorkcellActualVO3();
                        workcellActualVO3.setEoStepActualId(eoStepActualId);
                        workcellActualVO3.setWorkcellId(tuple.getWorkcellId());
                        List<MtEoStepWorkcellActual> eoStepWorkcellActuals = mtEoStepWorkcellActualRepository
                                        .eoWkcProductionResultGet(tenantId, workcellActualVO3);

                        // 获取输出参数wipQty
                        MtEoStepWipVO1 mtEoStepWipVO1 = new MtEoStepWipVO1();
                        mtEoStepWipVO1.setEoStepActualId(eoStepActualId);
                        List<MtEoStepWip> eoStepWipList =
                                        mtEoStepWipRepository.eoWkcAndStepWipQuery(tenantId, mtEoStepWipVO1);
                        if (CollectionUtils.isNotEmpty(eoStepWipList)) {
                            BigDecimal wkcWipQueueQty = eoStepWipList.stream()
                                            .collect(CollectorsUtil.summingBigDecimal(
                                                            c -> c.getQueueQty() == null ? BigDecimal.ZERO
                                                                            : BigDecimal.valueOf(c.getQueueQty())));
                            if (CollectionUtils.isNotEmpty(eoStepWorkcellActuals)) {
                                BigDecimal sumQueueQty = eoStepWorkcellActuals.stream()
                                                .collect(CollectorsUtil.summingBigDecimal(
                                                                c -> c.getQueueQty() == null ? BigDecimal.ZERO
                                                                                : BigDecimal.valueOf(c.getQueueQty())));
                                BigDecimal sumCompletedQty = eoStepWorkcellActuals.stream().collect(CollectorsUtil
                                                .summingBigDecimal(c -> c.getCompletedQty() == null ? BigDecimal.ZERO
                                                                : BigDecimal.valueOf(c.getCompletedQty())));
                                if (sumQueueQty.compareTo(sumCompletedQty) == 0
                                                && wkcWipQueueQty.compareTo(BigDecimal.ZERO) == 0) {
                                    for (MtEoDispatchAction eoDispatchAction : actions) {
                                        sqlList.addAll(customDbRepository.getDeleteSql(eoDispatchAction));
                                    }
                                }
                            }
                        }

                    }
                }
            }
            // 更新版本
            dispatchAction.setRevision(maxVersion);
            dispatchAction.setTenantId(tenantId);
            dispatchAction.setLastUpdatedBy(userId);
            dispatchAction.setLastUpdateDate(now);
            dispatchAction.setCid(Long.valueOf(actionCids.get(num)));
            sqlList.addAll(customDbRepository.getUpdateSql(dispatchAction));

            // 6.记录历史
            MtEoDispatchHistory mtEoDispatchHistory = new MtEoDispatchHistory();
            mtEoDispatchHistory.setEoId(dispatchAction.getEoId());
            mtEoDispatchHistory.setRouterStepId(dispatchAction.getRouterStepId());
            mtEoDispatchHistory.setOperationId(dispatchAction.getOperationId());
            mtEoDispatchHistory.setStatus(dispatchAction.getStatus());
            mtEoDispatchHistory.setWorkcellId(dispatchAction.getWorkcellId());
            mtEoDispatchHistory.setPriority(dispatchAction.getPriority());
            mtEoDispatchHistory.setPlanStartTime(dispatchAction.getPlanStartTime());
            mtEoDispatchHistory.setPlanEndTime(dispatchAction.getPlanEndTime());
            mtEoDispatchHistory.setShiftDate(dispatchAction.getShiftDate());
            mtEoDispatchHistory.setShiftCode(dispatchAction.getShiftCode());
            mtEoDispatchHistory.setAssignQty(dispatchAction.getAssignQty());
            mtEoDispatchHistory.setRevision(dispatchAction.getRevision());

            mtEoDispatchHistory.setTenantId(tenantId);
            mtEoDispatchHistory.setCreatedBy(userId);
            mtEoDispatchHistory.setCreationDate(now);
            mtEoDispatchHistory.setLastUpdatedBy(userId);
            mtEoDispatchHistory.setLastUpdateDate(now);
            mtEoDispatchHistory.setEoDispatchHistoryId(historyIds.get(num));
            mtEoDispatchHistory.setCid(Long.valueOf(historyCids.get(num)));
            sqlList.addAll(customDbRepository.getInsertSql(mtEoDispatchHistory));

            num++;
        }

        if (CollectionUtils.isNotEmpty(sqlList)) {
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    private static class EoRouterStepIdTuple implements Serializable {
        private static final long serialVersionUID = 1176247310152680285L;
        private String eoId;
        private String routerStepId;

        public EoRouterStepIdTuple(String eoId, String routerStepId) {
            this.eoId = eoId;
            this.routerStepId = routerStepId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            EoRouterStepIdTuple that = (EoRouterStepIdTuple) o;
            return Objects.equals(eoId, that.eoId) && Objects.equals(routerStepId, that.routerStepId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(eoId, routerStepId);
        }
    }
}
