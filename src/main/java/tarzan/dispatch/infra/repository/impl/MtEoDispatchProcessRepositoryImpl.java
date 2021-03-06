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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.actual.domain.entity.MtEoStepActual;
import tarzan.actual.domain.entity.MtEoStepWip;
import tarzan.actual.domain.entity.MtEoStepWorkcellActual;
import tarzan.actual.domain.repository.MtEoStepActualRepository;
import tarzan.actual.domain.repository.MtEoStepWipRepository;
import tarzan.actual.domain.repository.MtEoStepWorkcellActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.calendar.domain.entity.MtCalendarShift;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.calendar.domain.vo.MtCalendarShiftVO3;
import tarzan.calendar.domain.vo.MtCalendarShiftVO4;
import tarzan.calendar.domain.vo.MtCalendarShiftVO5;
import tarzan.dispatch.domain.entity.MtEoDispatchAction;
import tarzan.dispatch.domain.entity.MtEoDispatchProcess;
import tarzan.dispatch.domain.repository.MtEoDispatchActionRepository;
import tarzan.dispatch.domain.repository.MtEoDispatchProcessRepository;
import tarzan.dispatch.domain.vo.*;
import tarzan.dispatch.infra.mapper.MtEoDispatchActionMapper;
import tarzan.dispatch.infra.mapper.MtEoDispatchProcessMapper;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.method.domain.vo.MtRouterStepVO5;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.vo.MtEoVO30;
import tarzan.order.infra.mapper.MtEoMapper;

/**
 * ????????????????????? ???????????????
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:54:58
 */
@Component
public class MtEoDispatchProcessRepositoryImpl extends BaseRepositoryImpl<MtEoDispatchProcess>
                implements MtEoDispatchProcessRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtCalendarShiftRepository mtCalendarShiftRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private MtEoStepWipRepository mtEoStepWipRepository;

    @Autowired
    private MtEoDispatchProcessMapper mtEoDispatchProcessMapper;

    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtEoDispatchActionRepository mtEoDispatchActionRepository;

    @Autowired
    private MtEoStepWorkcellActualRepository mtEoStepWorkcellActualRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private MtEoMapper mtEoMapper;

    @Autowired
    private MtEoDispatchActionMapper mtEoDispatchActionMapper;


    @Override
    public List<MtEoDispatchProcess> wkcShiftLimitDispatchedProcessEoQuery(Long tenantId, MtEoDispatchProcessVO1 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getWorkcellId()) && StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0002",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0002", "DISPATCH",
                                            "workcellId???operationId", "???API???wkcShiftLimitDispatchedProcessEoQuery???"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "shiftCode", "???API???wkcShiftLimitDispatchedProcessEoQuery???"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "shiftDate", "???API???wkcShiftLimitDispatchedProcessEoQuery???"));
        }
        if (StringUtils.isNotEmpty(dto.getSiteId()) && StringUtils.isEmpty(dto.getProductionLineId())
                        || StringUtils.isNotEmpty(dto.getProductionLineId()) && StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_DISPATCH_0021",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0021", "DISPATCH",
                                            "productionLineId???siteId", "???API???wkcShiftLimitDispatchedProcessEoQuery???"));
        }

        // 2. ????????????????????????????????????????????????????????????
        MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
        mtEoDispatchProcess.setTenantId(tenantId);
        mtEoDispatchProcess.setWorkcellId(dto.getWorkcellId());
        mtEoDispatchProcess.setShiftCode(dto.getShiftCode());
        mtEoDispatchProcess.setShiftDate(dto.getShiftDate());
        mtEoDispatchProcess.setOperationId(dto.getOperationId());
        List<MtEoDispatchProcess> mtEoDispatchProcessList = mtEoDispatchProcessMapper.select(mtEoDispatchProcess);
        if (CollectionUtils.isEmpty(mtEoDispatchProcessList)) {
            return Collections.emptyList();
        }

        List<MtEoDispatchProcess> resultList = new ArrayList<>();

        // ??????siteId ???prodLineId?????????????????????????????????
        if (StringUtils.isNotEmpty(dto.getSiteId())) {
            List<String> eoIds = mtEoDispatchProcessList.stream().map(MtEoDispatchProcess::getEoId).distinct()
                            .collect(Collectors.toList());

            List<MtEo> mtEoList = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
            if (CollectionUtils.isNotEmpty(mtEoList)) {
                Map<String, List<MtEoDispatchProcess>> eoDispatchProcessMap = mtEoDispatchProcessList.stream()
                                .collect(Collectors.groupingBy(MtEoDispatchProcess::getEoId));

                for (Map.Entry<String, List<MtEoDispatchProcess>> tempEntry : eoDispatchProcessMap.entrySet()) {
                    List<MtEo> currentEoList = mtEoList.stream().filter(t -> t.getEoId().equals(tempEntry.getKey()))
                                    .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(currentEoList)) {
                        // ????????????
                        MtEo currentEo = currentEoList.get(0);
                        if (dto.getSiteId().equals(currentEo.getSiteId())
                                        && dto.getProductionLineId().equals(currentEo.getProductionLineId())) {
                            resultList.addAll(tempEntry.getValue());
                        }
                    }
                }
            }
        } else {
            resultList = mtEoDispatchProcessList;
        }

        // ??????????????????????????????1970???
        resultList.sort(Comparator.comparingLong(MtEoDispatchProcess::getPriority).thenComparing(
                        (MtEoDispatchProcess c) -> c.getPlanStartTime() == null ? new Date(0) : c.getPlanStartTime())
                        .thenComparing((MtEoDispatchProcess c) -> c.getPlanEndTime() == null ? new Date(0)
                                        : c.getPlanEndTime()));

        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String dispatchedEoUpdate(Long tenantId, MtEoDispatchProcessVO2 dto, String fullUpdate) {
        if (StringUtils.isEmpty(dto.getEoDispatchProcessId()) && StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_DISPATCH_0002", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0002", "DISPATCH", "eoDispatchProcessId???eoId", "???API???dispatchedEoUpdate???"));
        }

        if (StringUtils.isEmpty(dto.getEoDispatchProcessId()) && StringUtils.isNotEmpty(dto.getEoId())) {
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0001", "DISPATCH", "routerStepId", "???API???dispatchedEoUpdate???"));
            }
            if (StringUtils.isEmpty(dto.getWorkcellId())) {
                throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0001", "DISPATCH", "workcellId", "???API???dispatchedEoUpdate???"));
            }
            if (dto.getShiftDate() == null) {
                throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0001", "DISPATCH", "shiftDate", "???API???dispatchedEoUpdate???"));
            }
            if (StringUtils.isEmpty(dto.getShiftCode())) {
                throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0001", "DISPATCH", "shiftCode", "???API???dispatchedEoUpdate???"));
            }
        }

        if (StringUtils.isNotEmpty(dto.getEoDispatchProcessId()) && StringUtils.isNotEmpty(dto.getEoId())
                        && StringUtils.isNotEmpty(dto.getRouterStepId())) {
            MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
            mtEoDispatchProcess.setTenantId(tenantId);
            mtEoDispatchProcess.setEoDispatchProcessId(dto.getEoDispatchProcessId());
            mtEoDispatchProcess.setEoId(dto.getEoId());
            mtEoDispatchProcess.setRouterStepId(dto.getRouterStepId());
            List<MtEoDispatchProcess> existList = mtEoDispatchProcessMapper.select(mtEoDispatchProcess);
            if (CollectionUtils.isEmpty(existList)) {
                throw new MtException("MT_DISPATCH_0016",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0016", "DISPATCH",
                                                "eoDispatchProcessId", "routerStepId???eoId",
                                                "???API???dispatchedEoUpdate???"));
            }
        }

        // ????????????
        if (dto.getAssignQty() != null && dto.getTrxAssignQty() != null) {
            throw new MtException("MT_DISPATCH_0009", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0009", "DISPATCH", "assignQty???trxAssignQty", "???API???dispatchedEoUpdate???"));
        }

        // 2. ???????????? EoDispatchProcessId ??????????????????????????????
        if (StringUtils.isNotEmpty(dto.getEoDispatchProcessId())) {
            // ????????????
            // 3. ????????????????????????EoDispatchProcess??????
            MtEoDispatchProcess oldEoDispatchProcess = new MtEoDispatchProcess();
            oldEoDispatchProcess.setTenantId(tenantId);
            oldEoDispatchProcess.setEoDispatchProcessId(dto.getEoDispatchProcessId());
            oldEoDispatchProcess = mtEoDispatchProcessMapper.selectOne(oldEoDispatchProcess);
            if (oldEoDispatchProcess == null || StringUtils.isEmpty(oldEoDispatchProcess.getEoDispatchProcessId())) {
                throw new MtException("MT_DISPATCH_0014", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0014", "DISPATCH", "???API???dispatchedEoUpdate???"));
            }

            // ????????????????????????
            oldEoDispatchProcess.setStatus(dto.getStatus());
            oldEoDispatchProcess.setWorkcellId(dto.getWorkcellId());
            oldEoDispatchProcess.setPriority(dto.getPriority());
            oldEoDispatchProcess.setPlanStartTime(dto.getPlanStartTime());
            oldEoDispatchProcess.setPlanEndTime(dto.getPlanEndTime());
            oldEoDispatchProcess.setShiftDate(dto.getShiftDate());
            oldEoDispatchProcess.setShiftCode(dto.getShiftCode());
            if (dto.getAssignQty() != null) {
                oldEoDispatchProcess.setAssignQty(dto.getAssignQty());
            }
            if (dto.getTrxAssignQty() != null) {
                oldEoDispatchProcess.setAssignQty(BigDecimal
                                .valueOf(oldEoDispatchProcess.getAssignQty() == null ? 0.0D
                                                : oldEoDispatchProcess.getAssignQty())
                                .add(BigDecimal.valueOf(dto.getTrxAssignQty())).doubleValue());
            }
            if ("Y".equals(fullUpdate)) {
                oldEoDispatchProcess =
                                (MtEoDispatchProcess) ObjectFieldsHelper.setStringFieldsEmpty(oldEoDispatchProcess);
                self().updateByPrimaryKey(oldEoDispatchProcess);
            } else {
                self().updateByPrimaryKeySelective(oldEoDispatchProcess);
            }
            return oldEoDispatchProcess.getEoDispatchProcessId();
        } else {
            // ??????
            // ?????? eoStepActual ??????
            if (dto.getAssignQty() == null && dto.getTrxAssignQty() == null) {
                throw new MtException("MT_DISPATCH_0002", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0002", "DISPATCH", "assignQty???trxAssignQty", "???API???dispatchedEoUpdate???"));
            }
            if (dto.getPriority() == null) {
                throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0001", "DISPATCH", "priority ", "???API???dispatchedEoUpdate???"));
            }

            // ?????? Eo ??????
            MtEo eo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
            if (eo == null || StringUtils.isEmpty(eo.getEoId())) {
                throw new MtException("MT_DISPATCH_0004", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0004", "DISPATCH", "eoId", "???API???dispatchedEoUpdate???"));
            }

            String routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
            if (StringUtils.isEmpty(routerId)) {
                throw new MtException("MT_DISPATCH_0004", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0004", "DISPATCH", "routerId", "???API???dispatchedEoUpdate???"));
            }

            List<MtRouterStepVO5> routerStepOps = mtRouterStepRepository.routerStepListQuery(tenantId, routerId);
            if (CollectionUtils.isEmpty(routerStepOps)) {
                throw new MtException("MT_DISPATCH_0028", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0028", "DISPATCH", "???API???dispatchedEoUpdate???"));
            }
            List<String> routerStepOpStrs =
                            routerStepOps.stream().map(MtRouterStepVO5::getRouterStepId).collect(Collectors.toList());
            if (!routerStepOpStrs.contains(dto.getRouterStepId())) {
                throw new MtException("MT_DISPATCH_0003",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0003", "DISPATCH",
                                                "routerStepId", routerStepOpStrs.toString(),
                                                "???API???dispatchedEoUpdate???"));
            }
            MtRouterStepVO5 mtRouterStepVO5 = routerStepOps.stream()
                            .filter(t -> t.getRouterStepId().equals(dto.getRouterStepId())).findFirst().get();

            // ??????????????????
            MtEoDispatchProcess newEoDispatchProcess = new MtEoDispatchProcess();
            newEoDispatchProcess.setTenantId(tenantId);
            newEoDispatchProcess.setEoId(dto.getEoId());
            newEoDispatchProcess.setRouterStepId(dto.getRouterStepId());
            newEoDispatchProcess.setOperationId(mtRouterStepVO5.getOperationId());
            newEoDispatchProcess.setStatus(dto.getStatus());
            newEoDispatchProcess.setWorkcellId(dto.getWorkcellId());
            newEoDispatchProcess.setPriority(dto.getPriority());
            newEoDispatchProcess.setPlanStartTime(
                            dto.getPlanStartTime() == null ? eo.getPlanStartTime() : dto.getPlanStartTime());
            newEoDispatchProcess
                            .setPlanEndTime(dto.getPlanEndTime() == null ? eo.getPlanEndTime() : dto.getPlanEndTime());
            newEoDispatchProcess.setShiftDate(dto.getShiftDate());
            newEoDispatchProcess.setShiftCode(dto.getShiftCode());
            if (dto.getAssignQty() != null) {
                newEoDispatchProcess.setAssignQty(dto.getAssignQty());
            }
            if (dto.getTrxAssignQty() != null) {
                newEoDispatchProcess.setAssignQty(dto.getTrxAssignQty());
            }
            self().insertSelective(newEoDispatchProcess);
            return newEoDispatchProcess.getEoDispatchProcessId();
        }
    }

    @Override
    public Double toBeDispatchedEoDispatchableQtyGet(Long tenantId, MtEoDispatchProcessVO8 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "routerStepId", "???API:toBeDispatchedEoDispatchableQtyGet???"));
        }
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "eoId", "???API:toBeDispatchedEoDispatchableQtyGet???"));
        }

        // 2. ??????
        String rangeStrategy = null;
        MtEoDispatchActionVO24 strategyGet =
                        mtEoDispatchActionRepository.eoLimitDispatchStrategyGet(tenantId, dto.getEoId());
        if (strategyGet != null) {
            rangeStrategy = strategyGet.getRangeStrategy();
        }
        if ("ACTUAL_DISPATCH".equals(rangeStrategy) || StringUtils.isEmpty(rangeStrategy)) {
            List<String> eoStepActualIds = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getEoId());
            List<MtEoStepActual> eoStepActuals =
                            this.mtEoStepActualRepository.eoStepPropertyBatchGet(tenantId, eoStepActualIds);
            Optional<MtEoStepActual> optional = eoStepActuals.stream()
                            .filter(t -> t.getRouterStepId().equals(dto.getRouterStepId())).findFirst();
            String eoStepActualId = null;
            BigDecimal queueQty = BigDecimal.ZERO;
            if (optional.isPresent()) {
                eoStepActualId = optional.get().getEoStepActualId();
            }
            if (StringUtils.isEmpty(eoStepActualId)) {
                queueQty = BigDecimal.ZERO;
            }
            // 2. ??????????????????????????????????????????
            MtEoStepWipVO1 mtEoStepWipVO1 = new MtEoStepWipVO1();
            mtEoStepWipVO1.setEoStepActualId(eoStepActualId);
            mtEoStepWipVO1.setWorkcellId("");
            List<MtEoStepWip> eoStepWipVO2s = mtEoStepWipRepository.eoWkcAndStepWipQuery(tenantId, mtEoStepWipVO1);
            if (CollectionUtils.isNotEmpty(eoStepWipVO2s)) {
                // ????????????
                queueQty = new BigDecimal(eoStepWipVO2s.get(0).getQueueQty().toString());
            }

            // 3. ???????????????????????????????????????????????????
            BigDecimal sumAssignQty = BigDecimal.ZERO;

            MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
            mtEoDispatchProcess.setTenantId(tenantId);
            mtEoDispatchProcess.setRouterStepId(dto.getRouterStepId());
            mtEoDispatchProcess.setEoId(dto.getEoId());
            List<MtEoDispatchProcess> mtEoDispatchProcessList = mtEoDispatchProcessMapper.select(mtEoDispatchProcess);
            if (CollectionUtils.isNotEmpty(mtEoDispatchProcessList)) {
                // ????????????
                sumAssignQty = mtEoDispatchProcessList.stream().collect(CollectorsUtil.summingBigDecimal(
                                c -> BigDecimal.valueOf(c.getAssignQty() == null ? 0.0D : c.getAssignQty())));
            }

            return queueQty.subtract(sumAssignQty).doubleValue();
        } else {
            // 5
            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
            if (mtEo == null) {
                throw new MtException("MT_DISPATCH_0004",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0004", "DISPATCH",
                                                "eoId:" + dto.getEoId(), "???API:toBeDispatchedEoDispatchableQtyGet???"));
            }

            // ?????????????????????????????????
            BigDecimal sum_proAssignQty = BigDecimal.ZERO;

            MtEoDispatchProcess mtEoDispatchProcess = new MtEoDispatchProcess();
            mtEoDispatchProcess.setTenantId(tenantId);
            mtEoDispatchProcess.setEoId(dto.getEoId());
            mtEoDispatchProcess.setRouterStepId(dto.getRouterStepId());
            List<MtEoDispatchProcess> mtEoDispatchProcessList = mtEoDispatchProcessMapper.select(mtEoDispatchProcess);
            if (CollectionUtils.isNotEmpty(mtEoDispatchProcessList)) {
                sum_proAssignQty = mtEoDispatchProcessList.stream().collect(CollectorsUtil.summingBigDecimal(
                                t -> BigDecimal.valueOf(t.getAssignQty() == null ? 0.0D : t.getAssignQty())));
            }

            // ?????????????????????????????????
            BigDecimal sum_actAssignQty = BigDecimal.ZERO;

            MtEoDispatchAction mtEoDispatchAction = new MtEoDispatchAction();
            mtEoDispatchAction.setTenantId(tenantId);
            mtEoDispatchAction.setEoId(dto.getEoId());
            mtEoDispatchAction.setRouterStepId(dto.getRouterStepId());
            List<MtEoDispatchAction> mtEoDispatchActionList = mtEoDispatchActionRepository.select(mtEoDispatchAction);
            if (CollectionUtils.isNotEmpty(mtEoDispatchActionList)) {
                sum_actAssignQty = mtEoDispatchActionList.stream().collect(CollectorsUtil.summingBigDecimal(
                                t -> BigDecimal.valueOf(t.getAssignQty() == null ? 0.0D : t.getAssignQty())));
            }

            MtEoStepActualVO3 mtEoStepActualVO3 = new MtEoStepActualVO3();
            mtEoStepActualVO3.setEoId(dto.getEoId());
            mtEoStepActualVO3.setRouterStepId(dto.getRouterStepId());
            List<MtEoStepActualVO4> mtEoStepActualVO4List =
                            mtEoStepActualRepository.operationLimitEoStepActualQuery(tenantId, mtEoStepActualVO3);
            if (CollectionUtils.isEmpty(mtEoStepActualVO4List)) {
                return BigDecimal.valueOf(mtEo.getQty()).subtract(sum_proAssignQty).subtract(sum_actAssignQty)
                                .doubleValue();
            } else {
                // ??????????????????
                String eoStepActualId = mtEoStepActualVO4List.get(0).getEoStepActualId();
                // update by peng.yuan 2019/10/11
                MtEoStepWorkcellActualVO7 actualVO7 = new MtEoStepWorkcellActualVO7();
                actualVO7.setEoStepActualId(eoStepActualId);
                List<MtEoStepWorkcellActualVO8> workcellActualVO8List = mtEoStepWorkcellActualRepository
                                .propertyLimitEoStepWkcActualPropertyQuery(tenantId, actualVO7);
                BigDecimal sumQueueQtyResult = BigDecimal.ZERO;

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
                            sum_queueQty = queueQtyList.stream().collect(CollectorsUtil.summingBigDecimal(
                                            t -> BigDecimal.valueOf(t.getQueueQty() == null ? 0.0D : t.getQueueQty())));
                        }
                        sumQueueQtyResult = sumQueueQtyResult.add(sum_queueQty);
                    }
                    // ??????????????????routerStepId???eoId????????????????????????
                    if (0 == sum_actAssignQty.compareTo(BigDecimal.ZERO)) {
                        return BigDecimal.valueOf(mtEo.getQty()).subtract(sumQueueQtyResult).subtract(sum_proAssignQty)
                                        .doubleValue();

                    }
                }
                return BigDecimal.valueOf(mtEo.getQty()).subtract(sum_actAssignQty).subtract(sum_proAssignQty)
                                .doubleValue();
            }
        }
    }

    @Override
    public List<MtEoDispatchProcessVO4> operationLimitToBeDispatchedEoQuery(Long tenantId, MtEoDispatchProcessVO3 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "operationId", "???API:operationLimitToBeDispatchedEoQuery???"));
        }
        if (StringUtils.isNotEmpty(dto.getSiteId()) && StringUtils.isEmpty(dto.getProductionLineId())
                        || StringUtils.isEmpty(dto.getSiteId()) && StringUtils.isNotEmpty(dto.getProductionLineId())) {
            throw new MtException("MT_DISPATCH_0021",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0021", "DISPATCH",
                                            "productionLineId???siteId", "???API???operationLimitToBeDispatchedEoQuery???"));
        }

        List<MtEoDispatchProcessVO4> resultList = new ArrayList<>();

        // 2. ???????????????????????????????????????????????????????????????
        MtEoStepActualVO3 mtEoStepActualVO3 = new MtEoStepActualVO3();
        mtEoStepActualVO3.setOperationId(dto.getOperationId());
        mtEoStepActualVO3.setStepName(dto.getStepName());
        mtEoStepActualVO3.setStatus(Arrays.asList("QUEUE", "WORKING"));
        List<MtEoStepActualVO4> mtEoStepActualVO4List =
                        mtEoStepActualRepository.operationLimitEoStepActualQuery(tenantId, mtEoStepActualVO3);

        if (CollectionUtils.isNotEmpty(mtEoStepActualVO4List)) {

            // ??????eoId
            List<String> eoIds =
                            mtEoStepActualVO4List.stream().map(MtEoStepActualVO4::getEoId).collect(Collectors.toList());

            // ????????????eo??????
            List<MtEo> mtEoList = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
            if (CollectionUtils.isEmpty(mtEoList)) {
                return resultList;
            }

            // ??????map??????????????????
            Map<String, MtEo> mtEoMap = mtEoList.stream().collect(Collectors.toMap(MtEo::getEoId, t -> t));

            for (MtEoStepActualVO4 temp : mtEoStepActualVO4List) {
                MtEoDispatchProcessVO8 mtEoDispatchProcessVO8 = new MtEoDispatchProcessVO8();
                mtEoDispatchProcessVO8.setRouterStepId(temp.getRouterStepId());
                mtEoDispatchProcessVO8.setEoId(temp.getEoId());
                Double dispatchableQty = toBeDispatchedEoDispatchableQtyGet(tenantId, mtEoDispatchProcessVO8);

                if (new BigDecimal(dispatchableQty.toString()).compareTo(BigDecimal.ZERO) != 0) {
                    MtEo mtEo = mtEoMap.get(temp.getEoId());
                    if (mtEo != null) {
                        // ???????????????siteId???prodLineId???????????????
                        if (StringUtils.isNotEmpty(dto.getSiteId())
                                        && StringUtils.isNotEmpty(dto.getProductionLineId())) {
                            // ????????????siteId???prodLineId??????????????????????????????
                            if (!(dto.getSiteId().equals(mtEo.getSiteId())
                                            && dto.getProductionLineId().equals(mtEo.getProductionLineId()))) {
                                continue;
                            }
                        }

                        if (!Arrays.asList("NEW", "WORKING", "RELEASED").contains(mtEo.getStatus())) {
                            continue;
                        }

                        MtEoDispatchProcessVO4 result = new MtEoDispatchProcessVO4();
                        result.setEoId(mtEo.getEoId());
                        result.setRouterStepId(temp.getRouterStepId());
                        result.setPlanEndTime(mtEo.getPlanEndTime());
                        result.setPlanStartTime(mtEo.getPlanStartTime());
                        result.setOperationId(temp.getOperationId());
                        result.setStepName(temp.getStepName());
                        result.setUnassignQty(dispatchableQty);

                        resultList.add(result);
                    }
                }
            }
        }

        resultList.sort(Comparator.comparing(MtEoDispatchProcessVO4::getPlanStartTime)
                        .thenComparing(MtEoDispatchProcessVO4::getPlanEndTime)
                        .thenComparingDouble((MtEoDispatchProcessVO4 t) -> Double.valueOf(t.getEoId()))
                        .thenComparingDouble((MtEoDispatchProcessVO4 t) -> Double.valueOf(t.getRouterStepId()))
                        .thenComparing(MtEoDispatchProcessVO4::getStepName));
        return resultList;
    }

    @Override
    public List<MtEoDispatchProcessVO4> planTimeLimitToBeDispatchedEoQuery(Long tenantId, MtEoDispatchProcessVO5 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "operationId", "???API:planTimeLimitToBeDispatchedEoQuery???"));
        }
        if (dto.getPlanTimeFrom() == null && dto.getPlanTimeTo() == null) {
            throw new MtException("MT_DISPATCH_0002",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0002", "DISPATCH",
                                            "planTimeFrom???planTimeTo", "???API:planTimeLimitToBeDispatchedEoQuery???"));
        }

        // 2. ????????????
        List<MtEoDispatchProcessVO4> resultList = new ArrayList<>();

        MtEoDispatchProcessVO3 mtEoDispatchProcessVO3 = new MtEoDispatchProcessVO3();
        mtEoDispatchProcessVO3.setOperationId(dto.getOperationId());
        mtEoDispatchProcessVO3.setStepName(dto.getStepName());
        mtEoDispatchProcessVO3.setProductionLineId(dto.getProductionLineId());
        mtEoDispatchProcessVO3.setSiteId(dto.getSiteId());
        List<MtEoDispatchProcessVO4> mtEoDispatchProcessVO4List =
                        operationLimitToBeDispatchedEoQuery(tenantId, mtEoDispatchProcessVO3);
        if (CollectionUtils.isNotEmpty(mtEoDispatchProcessVO4List)) {
            for (MtEoDispatchProcessVO4 result : mtEoDispatchProcessVO4List) {
                // update by chuang.yang 2019/7/10 ????????????
                // ??? planStartTime ??? planTimeFrom ???????????????planStartTime ???????????????planTimeFrom ?????????????????????
                // planStartTime
                Date maxTime;
                if (dto.getPlanTimeFrom() == null) {
                    maxTime = result.getPlanStartTime();
                } else {
                    // ????????????
                    maxTime = result.getPlanStartTime().before(dto.getPlanTimeFrom()) ? dto.getPlanTimeFrom()
                                    : result.getPlanStartTime();
                }

                Date minTime;
                if (dto.getPlanTimeTo() == null) {
                    minTime = result.getPlanEndTime();
                } else {
                    // ????????????
                    minTime = result.getPlanEndTime().before(dto.getPlanTimeTo()) ? result.getPlanEndTime()
                                    : dto.getPlanTimeTo();
                }

                // ?????? minTime >= maxTime ?????????
                if (minTime.after(maxTime)) {
                    resultList.add(result);
                }
            }
        }

        resultList.sort(Comparator.comparing(MtEoDispatchProcessVO4::getPlanStartTime)
                        .thenComparing(MtEoDispatchProcessVO4::getPlanEndTime)
                        .thenComparingDouble((MtEoDispatchProcessVO4 t) -> Double.valueOf(t.getEoId()))
                        .thenComparingDouble((MtEoDispatchProcessVO4 t) -> Double.valueOf(t.getRouterStepId()))
                        .thenComparing(MtEoDispatchProcessVO4::getStepName));
        return resultList;
    }

    @Override
    public List<MtEoDispatchProcessVO4> wkcShiftLimitToBeDispatchedEoQuery(Long tenantId, MtEoDispatchProcessVO6 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "operationId", "???API???wkcShiftLimitToBeDispatchedEoQuery???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "workcellId", "???API???wkcShiftLimitToBeDispatchedEoQuery???"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftCode", "???API???wkcShiftLimitToBeDispatchedEoQuery???"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftDate", "???API???wkcShiftLimitToBeDispatchedEoQuery???"));
        }

        // 2. ???????????????????????????????????????????????????????????????
        MtCalendarShiftVO5 mtCalendarShiftVO5 = new MtCalendarShiftVO5();
        mtCalendarShiftVO5.setOrganizationId(dto.getWorkcellId());
        mtCalendarShiftVO5.setOrganizationType("WORKCELL");
        mtCalendarShiftVO5.setShiftCode(dto.getShiftCode());
        mtCalendarShiftVO5.setShiftDate(dto.getShiftDate());
        String calendarShiftId = mtCalendarShiftRepository.organizationAndShiftLimitCalendarShiftGet(tenantId,
                        mtCalendarShiftVO5);

        if (StringUtils.isEmpty(calendarShiftId)) {
            return Collections.emptyList();
        }
        MtCalendarShift mtCalendarShift = mtCalendarShiftRepository.calendarShiftGet(tenantId, calendarShiftId);

        MtEoDispatchProcessVO5 mtEoDispatchProcessVO5 = new MtEoDispatchProcessVO5();
        mtEoDispatchProcessVO5.setOperationId(dto.getOperationId());
        mtEoDispatchProcessVO5.setStepName(dto.getStepName());
        mtEoDispatchProcessVO5.setPlanTimeFrom(mtCalendarShift.getShiftStartTime());
        mtEoDispatchProcessVO5.setPlanTimeTo(mtCalendarShift.getShiftEndTime());
        mtEoDispatchProcessVO5.setProductionLineId(dto.getProductionLineId());
        mtEoDispatchProcessVO5.setSiteId(dto.getSiteId());
        return planTimeLimitToBeDispatchedEoQuery(tenantId, mtEoDispatchProcessVO5);
    }

    @Override
    public List<MtEoDispatchProcessVO4> wkcShiftPeriodLimitToBeDispatchedEoQuery(Long tenantId,
                    MtEoDispatchProcessVO7 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "operationId", "???API???wkcShiftPeriodLimitToBeDispatchedEoQuery???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "workcellId", "???API???wkcShiftPeriodLimitToBeDispatchedEoQuery???"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "shiftCode", "???API???wkcShiftPeriodLimitToBeDispatchedEoQuery???"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "shiftDate", "???API???wkcShiftPeriodLimitToBeDispatchedEoQuery???"));
        }

        // ?????????????????????
        if (dto.getForwardPeriod() == null) {
            dto.setForwardPeriod(0);
        }
        if (dto.getBackwardPeriod() == null) {
            dto.setBackwardPeriod(0);
        }

        // 2. ???????????????????????????????????????????????????????????????????????????
        MtCalendarShiftVO3 mtCalendarShiftVO3 = new MtCalendarShiftVO3();
        mtCalendarShiftVO3.setWorkcellId(dto.getWorkcellId());
        mtCalendarShiftVO3.setShiftDate(dto.getShiftDate());
        mtCalendarShiftVO3.setShiftCode(dto.getShiftCode());
        mtCalendarShiftVO3.setForwardPeriod(dto.getForwardPeriod());
        mtCalendarShiftVO3.setBackwardPeriod(dto.getBackwardPeriod());
        List<MtCalendarShiftVO4> mtCalendarShiftList =
                        mtCalendarShiftRepository.calendarShiftLimitNearShiftQuery(tenantId, mtCalendarShiftVO3);

        // ??????????????????
        List<MtEoDispatchProcessVO4> resultList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(mtCalendarShiftList)) {
            for (MtCalendarShiftVO4 calendarShift : mtCalendarShiftList) {
                MtEoDispatchProcessVO6 mtEoDispatchProcessVO6 = new MtEoDispatchProcessVO6();
                mtEoDispatchProcessVO6.setOperationId(dto.getOperationId());
                mtEoDispatchProcessVO6.setWorkcellId(dto.getWorkcellId());
                mtEoDispatchProcessVO6.setShiftCode(calendarShift.getShiftCode());
                mtEoDispatchProcessVO6.setShiftDate(calendarShift.getShiftDate());
                mtEoDispatchProcessVO6.setStepName(dto.getStepName());
                mtEoDispatchProcessVO6.setSiteId(dto.getSiteId());
                mtEoDispatchProcessVO6.setProductionLineId(dto.getProductionLineId());
                List<MtEoDispatchProcessVO4> tempList =
                                wkcShiftLimitToBeDispatchedEoQuery(tenantId, mtEoDispatchProcessVO6);

                if (CollectionUtils.isNotEmpty(tempList)) {
                    resultList.addAll(tempList.stream().distinct().collect(Collectors.toList()));
                }
            }
        }

        resultList = resultList.stream().sorted(Comparator.comparing(MtEoDispatchProcessVO4::getPlanStartTime)
                        .thenComparing(MtEoDispatchProcessVO4::getPlanEndTime)
                        .thenComparingDouble((MtEoDispatchProcessVO4 t) -> Double
                                        .valueOf(StringUtils.isEmpty(t.getEoId()) ? "0" : t.getEoId()))
                        .thenComparingDouble((MtEoDispatchProcessVO4 t) -> Double
                                        .valueOf(StringUtils.isEmpty(t.getRouterStepId()) ? "0" : t.getRouterStepId()))
                        .thenComparing(MtEoDispatchProcessVO4::getStepName)).distinct().collect(Collectors.toList());

        return resultList;
    }

    @Override
    public List<MtEoDispatchProcessVO10> propertyLimitDispatchedProcessPropertyQuery(Long tenantId,
                    MtEoDispatchProcessVO9 vo) {
        // ??????????????????
        List<MtEoDispatchProcess> processes =
                        mtEoDispatchProcessMapper.propertyLimitDispatchedProcessPropertyQuery(tenantId, vo);
        if (CollectionUtils.isNotEmpty(processes)) {
            List<MtEoDispatchProcessVO10> result = new ArrayList<>();
            // ??????????????????????????????????????? workcellId???????????????API{ workcellBasicPropertyBatchGet }????????????????????????????????????????????????????????????????????????

            List<String> workCellIds = processes.stream().map(MtEoDispatchProcess::getWorkcellId)
                            .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

            Map<String, MtModWorkcell> workcellMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(workCellIds)) {
                List<MtModWorkcell> mtModWorkcells =
                                mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workCellIds);
                if (CollectionUtils.isNotEmpty(mtModWorkcells)) {
                    workcellMap = mtModWorkcells.stream()
                                    .collect(Collectors.toMap(MtModWorkcell::getWorkcellId, t -> t));
                }
            }

            // ????????????????????????????????? operationId???????????????API{operationBatchGet }???????????????????????????????????????
            List<String> operationIds = processes.stream().map(MtEoDispatchProcess::getOperationId)
                            .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

            Map<String, MtOperation> operationMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(operationIds)) {
                List<MtOperation> mtOperations = mtOperationRepository.operationBatchGet(tenantId, operationIds);
                if (CollectionUtils.isNotEmpty(mtOperations)) {
                    operationMap = mtOperations.stream().collect(Collectors.toMap(MtOperation::getOperationId, t -> t));
                }
            }


            // ??????????????????????????????????????? eoId???????????????API{eoPropertyBatchGet }????????????????????????
            List<String> eoIds = processes.stream().map(MtEoDispatchProcess::getEoId).filter(StringUtils::isNotEmpty)
                            .distinct().collect(Collectors.toList());

            Map<String, String> eoNums = new HashMap<>();

            if (CollectionUtils.isNotEmpty(eoIds)) {
                List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
                if (CollectionUtils.isNotEmpty(mtEos)) {
                    eoNums = mtEos.stream().collect(Collectors.toMap(MtEo::getEoId, MtEo::getEoNum));
                }
            }


            // ????????????????????????????????????????????? stepId???????????????API{ routerStepBatchGet }??????????????????????????????
            List<String> stepIds = processes.stream().map(MtEoDispatchProcess::getRouterStepId)
                            .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

            Map<String, String> stepNames = new HashMap<>();

            if (CollectionUtils.isNotEmpty(stepIds)) {
                List<MtRouterStep> mtRouterSteps = mtRouterStepRepository.routerStepBatchGet(tenantId, stepIds);
                if (CollectionUtils.isNotEmpty(mtRouterSteps)) {
                    stepNames = mtRouterSteps.stream().collect(
                                    Collectors.toMap(MtRouterStep::getRouterStepId, MtRouterStep::getStepName));
                }
            }

            for (MtEoDispatchProcess process : processes) {
                MtEoDispatchProcessVO10 vo10 = new MtEoDispatchProcessVO10();
                vo10.setEoDispatchProcessId(process.getEoDispatchProcessId());
                vo10.setEoId(process.getEoId());
                vo10.setEoNum(eoNums.get(process.getEoId()));
                vo10.setRouterStepId(process.getRouterStepId());
                vo10.setStepName(stepNames.get(process.getRouterStepId()));
                vo10.setOperationId(process.getOperationId());

                MtOperation operation = operationMap.get(process.getOperationId());
                vo10.setOperationName(null == operation ? null : operation.getOperationName());
                vo10.setOperationDescription(null == operation ? null : operation.getDescription());

                vo10.setPriority(process.getPriority());
                vo10.setPlanStartTime(process.getPlanStartTime());
                vo10.setPlanEndTime(process.getPlanEndTime());
                vo10.setShiftDate(process.getShiftDate());
                vo10.setShiftCode(process.getShiftCode());
                vo10.setWorkcellId(process.getWorkcellId());

                MtModWorkcell workcell = workcellMap.get(process.getWorkcellId());
                vo10.setWorkcellCode(null == workcell ? null : workcell.getWorkcellCode());
                vo10.setWorkcellName(null == workcell ? null : workcell.getWorkcellName());
                vo10.setWorkcellDescription(null == workcell ? null : workcell.getDescription());
                vo10.setStatus(process.getStatus());
                vo10.setAssignQty(process.getAssignQty());
                result.add(vo10);
            }
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    public List<MtEoDispatchProcessVO10> propertyLimitDispatchedProcessPropertyBatchQuery(Long tenantId,
                    MtEoDispatchProcessVO13 vo) {
        // ??????????????????
        List<MtEoDispatchProcess> processes =
                        mtEoDispatchProcessMapper.propertyLimitDispatchedProcessPropertyBatchQuery(tenantId, vo);
        if (CollectionUtils.isNotEmpty(processes)) {
            List<MtEoDispatchProcessVO10> result = new ArrayList<>();
            // ??????????????????????????????????????? workcellId???????????????API{ workcellBasicPropertyBatchGet }????????????????????????????????????????????????????????????????????????

            List<String> workCellIds = processes.stream().map(MtEoDispatchProcess::getWorkcellId)
                            .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

            Map<String, MtModWorkcell> workcellMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(workCellIds)) {
                List<MtModWorkcell> mtModWorkcells =
                                mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workCellIds);
                if (CollectionUtils.isNotEmpty(mtModWorkcells)) {
                    workcellMap = mtModWorkcells.stream()
                                    .collect(Collectors.toMap(MtModWorkcell::getWorkcellId, t -> t));
                }
            }

            // ????????????????????????????????? operationId???????????????API{operationBatchGet }???????????????????????????????????????
            List<String> operationIds = processes.stream().map(MtEoDispatchProcess::getOperationId)
                            .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

            Map<String, MtOperation> operationMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(operationIds)) {
                List<MtOperation> mtOperations = mtOperationRepository.operationBatchGet(tenantId, operationIds);
                if (CollectionUtils.isNotEmpty(mtOperations)) {
                    operationMap = mtOperations.stream().collect(Collectors.toMap(MtOperation::getOperationId, t -> t));
                }
            }


            // ??????????????????????????????????????? eoId???????????????API{eoPropertyBatchGet }????????????????????????
            List<String> eoIds = processes.stream().map(MtEoDispatchProcess::getEoId).filter(StringUtils::isNotEmpty)
                            .distinct().collect(Collectors.toList());

            Map<String, String> eoNums = new HashMap<>();

            if (CollectionUtils.isNotEmpty(eoIds)) {
                List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
                if (CollectionUtils.isNotEmpty(mtEos)) {
                    eoNums = mtEos.stream().collect(Collectors.toMap(MtEo::getEoId, MtEo::getEoNum));
                }
            }


            // ????????????????????????????????????????????? stepId???????????????API{ routerStepBatchGet }??????????????????????????????
            List<String> stepIds = processes.stream().map(MtEoDispatchProcess::getRouterStepId)
                            .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

            Map<String, String> stepNames = new HashMap<>();

            if (CollectionUtils.isNotEmpty(stepIds)) {
                List<MtRouterStep> mtRouterSteps = mtRouterStepRepository.routerStepBatchGet(tenantId, stepIds);
                if (CollectionUtils.isNotEmpty(mtRouterSteps)) {
                    stepNames = mtRouterSteps.stream().collect(
                                    Collectors.toMap(MtRouterStep::getRouterStepId, MtRouterStep::getStepName));
                }
            }

            for (MtEoDispatchProcess process : processes) {
                MtEoDispatchProcessVO10 vo10 = new MtEoDispatchProcessVO10();
                vo10.setEoDispatchProcessId(process.getEoDispatchProcessId());
                vo10.setEoId(process.getEoId());
                vo10.setEoNum(eoNums.get(process.getEoId()));
                vo10.setRouterStepId(process.getRouterStepId());
                vo10.setStepName(stepNames.get(process.getRouterStepId()));
                vo10.setOperationId(process.getOperationId());

                MtOperation operation = operationMap.get(process.getOperationId());
                vo10.setOperationName(null == operation ? null : operation.getOperationName());
                vo10.setOperationDescription(null == operation ? null : operation.getDescription());

                vo10.setPriority(process.getPriority());
                vo10.setPlanStartTime(process.getPlanStartTime());
                vo10.setPlanEndTime(process.getPlanEndTime());
                vo10.setShiftDate(process.getShiftDate());
                vo10.setShiftCode(process.getShiftCode());
                vo10.setWorkcellId(process.getWorkcellId());

                MtModWorkcell workcell = workcellMap.get(process.getWorkcellId());
                vo10.setWorkcellCode(null == workcell ? null : workcell.getWorkcellCode());
                vo10.setWorkcellName(null == workcell ? null : workcell.getWorkcellName());
                vo10.setWorkcellDescription(null == workcell ? null : workcell.getDescription());
                vo10.setStatus(process.getStatus());
                vo10.setAssignQty(process.getAssignQty());
                result.add(vo10);
            }
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    public List<MtEoDispatchProcessVO12> rangeLimitToBeDispatchedEoQuery(Long tenantId, MtEoDispatchProcessVO11 dto) {

        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "operationId", "???API???rangeLimitToBeDispatchedEoQuery???"));
        }
        if (StringUtils.isEmpty(dto.getProductionLineId())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "productionLineId", "???API???rangeLimitToBeDispatchedEoQuery???"));
        }
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "siteId", "???API???rangeLimitToBeDispatchedEoQuery???"));
        }

        if (StringUtils.isEmpty(dto.getSiteId()) && StringUtils.isNotEmpty(dto.getProductionLineId())
                        || StringUtils.isNotEmpty(dto.getSiteId()) && StringUtils.isEmpty(dto.getProductionLineId())) {
            throw new MtException("MT_DISPATCH_0021",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0021", "DISPATCH",
                                            "productionLineId???siteId", "???API???rangeLimitToBeDispatchedEoQuery???"));
        }
        // a???a)
        // ??????????????????productionLineId???siteId??????API{propertyLimitEoPropertyQuery}????????????????????????RELEASED???WORKING???HOLD????????????eoId???planStartTime???planEndTime
        MtEoVO30 mtEoVO30 = new MtEoVO30();
        mtEoVO30.setProductionLineId(dto.getProductionLineId());
        mtEoVO30.setSiteId(dto.getSiteId());
        List<MtEo> eoList = mtEoMapper.propertyLimitEoPropertyQuery(tenantId, mtEoVO30);

        if (dto.getStartTime() != null) {
            eoList = eoList.stream().filter(c -> dto.getStartTime().getTime() <= c.getPlanEndTime().getTime())
                            .collect(Collectors.toList());
        }
        if (dto.getEndTime() != null) {
            eoList = eoList.stream().filter(c -> dto.getEndTime().getTime() >= c.getPlanStartTime().getTime())
                            .collect(Collectors.toList());
        }

        // b) ??????a???????????????eoId??????API{ eoRouterGet
        // }?????????routerId????????????????????????routerId??????API{routerStepListQuery}?????????routerStepId???operationId??????????????????operationId=????????????operationId???
        List<MtEoDispatchProcessVO12> eoDispatchProcessVO12List = new ArrayList<>();
        for (MtEo mtEo : eoList) {
            MtEoDispatchProcessVO12 processVO12 = new MtEoDispatchProcessVO12();
            processVO12.setEoId(mtEo.getEoId());
            processVO12.setPlanEndTime(mtEo.getPlanEndTime());
            processVO12.setPlanStartTime(mtEo.getPlanStartTime());
            String routerId = mtEoRouterRepository.eoRouterGet(tenantId, mtEo.getEoId());
            List<MtRouterStepVO5> mtRouterStepVO5s = mtRouterStepRepository.routerStepListQuery(tenantId, routerId);
            Optional<MtRouterStepVO5> optional = Optional.empty();
            if (CollectionUtils.isNotEmpty(mtRouterStepVO5s)) {
                optional = mtRouterStepVO5s.stream().filter(t -> dto.getOperationId().equals(t.getOperationId()))
                                .findFirst();
            }
            if (optional.isPresent()) {
                MtRouterStepVO5 mtRouterStepVO5 = optional.get();
                processVO12.setRouterStepId(mtRouterStepVO5.getRouterStepId());
                processVO12.setOperationId(mtRouterStepVO5.getOperationId());
                // c) ??????b???????????????routerStepId??????API{routerStepGet}?????????????????????stepName???
                MtRouterStep mtRouterStep =
                                mtRouterStepRepository.routerStepGet(tenantId, mtRouterStepVO5.getRouterStepId());

                processVO12.setStepName(null == mtRouterStep ? null : mtRouterStep.getStepName());

            }
            // d) ?????? a)??? b???????????????eoId???routerStepId?????????API{ toBeDispatchedEoDispatchableQtyGet
            // }???????????????0???dispatchableQty?????????unassignQty=dispatchableQty
            MtEoDispatchProcessVO8 mtEoDispatchProcessVO8 = new MtEoDispatchProcessVO8();
            mtEoDispatchProcessVO8.setRouterStepId(processVO12.getRouterStepId());
            mtEoDispatchProcessVO8.setEoId(processVO12.getEoId());
            Double dispatchableQty = toBeDispatchedEoDispatchableQtyGet(tenantId, mtEoDispatchProcessVO8);

            if (BigDecimal.valueOf(dispatchableQty).compareTo(BigDecimal.ZERO) != 0) {
                processVO12.setUnassignQty(dispatchableQty);
            }
            eoDispatchProcessVO12List.add(processVO12);
        }

        eoDispatchProcessVO12List.sort(Comparator.comparing(MtEoDispatchProcessVO12::getPlanStartTime)
                        .thenComparing(MtEoDispatchProcessVO12::getPlanEndTime)
                        .thenComparingDouble((MtEoDispatchProcessVO12 t) -> Double
                                        .valueOf(StringUtils.isEmpty(t.getEoId()) ? "0" : t.getEoId()))
                        .thenComparingDouble((MtEoDispatchProcessVO12 t) -> Double
                                        .valueOf(StringUtils.isEmpty(t.getRouterStepId()) ? "0" : t.getRouterStepId()))
                        .thenComparingDouble((MtEoDispatchProcessVO12 t) -> Double
                                        .valueOf(StringUtils.isEmpty(t.getOperationId()) ? "0" : t.getOperationId()))
                        .thenComparing(MtEoDispatchProcessVO12::getStepName)
                        .thenComparingDouble((MtEoDispatchProcessVO12 t) -> t.getUnassignQty() == 0D ? 0D
                                        : t.getUnassignQty()));
        return eoDispatchProcessVO12List;
    }

    /**
     * dispatchedEoAssignQtyBatchGet-???????????????????????????????????????
     *
     * @author chuang.yang
     * @date 2019/12/19
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.dispatch.domain.vo.MtEoDispatchProcessVO14>
     */
    @Override
    public List<MtEoDispatchProcessVO14> dispatchedEoAssignQtyBatchGet(Long tenantId, MtEoDispatchProcessVO15 dto) {
        // 1. ?????????????????????
        if (CollectionUtils.isEmpty(dto.getEoMessageList())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "eoMessageList", "???API???dispatchedEoAssignQtyBatchGet???"));
        }

        Optional<MtEoStepActualVO37> any =
                        dto.getEoMessageList().stream().filter(t -> StringUtils.isEmpty(t.getEoId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "eoMessageList.eoId", "???API???dispatchedEoAssignQtyBatchGet???"));
        }
        any = dto.getEoMessageList().stream().filter(t -> StringUtils.isEmpty(t.getRouterStepId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "eoMessageList.routerStepId", "???API???dispatchedEoAssignQtyBatchGet???"));
        }

        // ??????eoId????????????????????????map??????
        Map<String, List<MtEoStepActualVO37>> eoMessageListMap =
                        dto.getEoMessageList().stream().collect(Collectors.groupingBy(t -> t.getEoId()));

        // ?????? dispatchStrategy
        // eoId??????
        List<String> eoIdList = dto.getEoMessageList().stream().map(MtEoStepActualVO37::getEoId).distinct()
                        .collect(Collectors.toList());
        List<MtEoDispatchActionVO22> eoDispatchStrategyList = new ArrayList<>();

        MtEoDispatchActionVO24 mtEoDispatchActionVO24 =
                        mtEoDispatchActionRepository.eoLimitDispatchStrategyBatchGet(tenantId, eoIdList);
        for (String eoId : eoIdList) {
            MtEoDispatchActionVO22 vo22 = new MtEoDispatchActionVO22();
            if (null != mtEoDispatchActionVO24) {
                vo22.setDispatchStrategy(mtEoDispatchActionVO24.getPublishStrategy());
            }
            vo22.setEoId(eoId);
            eoDispatchStrategyList.add(vo22);
        }

        if (CollectionUtils.isEmpty(eoDispatchStrategyList)) {
            return Collections.emptyList();
        }

        // ????????????????????????????????????????????????ACTUAL_DISPATCH???PLAN_DISPATCH
        Map<String, List<MtEoDispatchActionVO22>> eoDispatchStrategyMap =
                        eoDispatchStrategyList.stream().collect(Collectors.groupingBy(t -> t.getDispatchStrategy()));

        List<MtEoDispatchActionVO22> eoActualDispatchStrategyList =
                        eoDispatchStrategyMap.get(MtBaseConstants.GEN_TYPE_CODE.QUEUE);
        List<MtEoDispatchActionVO22> eoPlanDispatchStrategyList =
                        eoDispatchStrategyMap.get(MtBaseConstants.GEN_TYPE_CODE.WITHOUT_QUEUING);


        // 1.1 ????????????????????????????????????????????????????????????????????????
        List<MtEoStepActualVO36> eoStepActualList =
                        mtEoStepActualRepository.eoAndStepLimitStepActualBatchQuery(tenantId, dto.getEoMessageList());

        List<MtEoDispatchProcessVO14> resultList = new ArrayList<>(dto.getEoMessageList().size());

        // 2. ??????????????? QUEUE?????????
        if (CollectionUtils.isNotEmpty(eoActualDispatchStrategyList)) {
            Map<String, List<MtEoStepActualVO36>> eoStepActualMap = null;

            List<MtEoStepWorkcellActualVO3> mtEoStepWorkcellActualVO3s = null;

            // ???????????????????????????????????????eoMessageList
            List<MtEoStepActualVO37> eoMessageDispatchList = new ArrayList<>();

            // ?????????????????????????????????????????????????????????????????????????????????????????????
            List<MtEoStepActualVO36> eoActualDispatchStepActualList = new ArrayList<>();

            // ????????????????????????????????????????????????
            Map<String, List<MtEoStepWip>> mtEoStepWipMap = null;

            // ???????????????????????????????????????????????????????????????????????????0
            if (CollectionUtils.isNotEmpty(eoStepActualList)) {
                // ??????eoId??????Map??????
                eoStepActualMap = eoStepActualList.stream().collect(Collectors.groupingBy(t -> t.getEoId()));
            }

            // 2.1 ?????????????????? ???????????? eoStepActualId ??????
            for (MtEoDispatchActionVO22 mtEoDispatchAction : eoActualDispatchStrategyList) {
                if (MapUtils.isNotEmpty(eoStepActualMap)) {
                    List<MtEoStepActualVO36> currentEoStepActualList =
                                    eoStepActualMap.get(mtEoDispatchAction.getEoId());
                    if (CollectionUtils.isNotEmpty(currentEoStepActualList)) {
                        eoActualDispatchStepActualList.addAll(currentEoStepActualList);
                    }
                }

                eoMessageDispatchList.addAll(eoMessageListMap.get(mtEoDispatchAction.getEoId()));
            }

            List<String> eoStepActualIdList = eoActualDispatchStepActualList.stream()
                            .map(MtEoStepActualVO36::getEoStepActualId).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(eoStepActualIdList)) {
                // 2.2 ??????????????????????????????????????????
                List<MtEoStepWipVO1> mtEoStepWipVO1List = new ArrayList<>(eoStepActualIdList.size());
                List<MtEoStepWorkcellActualVO3> finalMtEoStepWorkcellActualVO3s =
                                new ArrayList<>(eoStepActualIdList.size());
                eoStepActualIdList.stream().forEach(eoStepActualId -> {
                    // ?????????wkc
                    MtEoStepWipVO1 mtEoStepWipVO1 = new MtEoStepWipVO1();
                    mtEoStepWipVO1.setEoStepActualId(eoStepActualId);
                    mtEoStepWipVO1.setWorkcellId(null);
                    mtEoStepWipVO1List.add(mtEoStepWipVO1);

                    MtEoStepWorkcellActualVO3 mtEoStepWorkcellActualVO3 = new MtEoStepWorkcellActualVO3();
                    mtEoStepWorkcellActualVO3.setEoStepActualId(eoStepActualId);
                    mtEoStepWorkcellActualVO3.setWorkcellId(dto.getWorkcellId());
                    finalMtEoStepWorkcellActualVO3s.add(mtEoStepWorkcellActualVO3);
                });

                mtEoStepWorkcellActualVO3s = finalMtEoStepWorkcellActualVO3s;

                // ??????????????????????????????????????????
                List<MtEoStepWip> mtEoStepWips =
                                mtEoStepWipRepository.eoWkcAndStepWipBatchQuery(tenantId, mtEoStepWipVO1List);
                if (CollectionUtils.isNotEmpty(mtEoStepWips)) {
                    // ??????Map??????
                    mtEoStepWipMap = mtEoStepWips.stream().collect(Collectors.groupingBy(t -> t.getEoStepActualId()));
                }
            }

            // ??????wkc???????????????
            if (StringUtils.isEmpty(dto.getWorkcellId())) {
                // ?????? eoStepActualId ??? wipQueueQty ?????????
                Map<String, BigDecimal> eoStepActualWipQueueQtyMap = null;

                // ?????? eoStepActualId ??? wipTotalQueueQty ?????????
                Map<String, BigDecimal> eoStepActualWipTotalQueueQtyMap = null;

                if (MapUtils.isNotEmpty(mtEoStepWipMap)) {
                    eoStepActualWipQueueQtyMap = new HashMap<>(mtEoStepWipMap.size());
                    eoStepActualWipTotalQueueQtyMap = new HashMap<>(mtEoStepWipMap.size());

                    // ?????? eoStepActualId ??? wipQueueQty ?????????
                    for (Map.Entry<String, List<MtEoStepWip>> entry : mtEoStepWipMap.entrySet()) {
                        BigDecimal curWipQueueQty = entry.getValue().stream().filter(c -> "".equals(c.getWorkcellId()))
                                        .collect(CollectorsUtil
                                                        .summingBigDecimal(t -> BigDecimal.valueOf(t.getQueueQty())));
                        eoStepActualWipQueueQtyMap.put(entry.getKey(), curWipQueueQty);

                        BigDecimal curWipTotalQueueQty = entry.getValue().stream().collect(
                                        CollectorsUtil.summingBigDecimal(t -> BigDecimal.valueOf(t.getQueueQty())));
                        eoStepActualWipTotalQueueQtyMap.put(entry.getKey(), curWipTotalQueueQty);
                    }
                }

                // ?????? eoStepActualId ??? totalQueueQty ?????????
                Map<String, BigDecimal> eoStepActualTotalQueueQtyMap = null;

                // ?????? eoId ??? routerStepId ??? processAssignQty ?????????
                Map<EoRouterStepIdTuple, BigDecimal> eoRouterStepProcessAssignQtyMap = null;

                Map<EoRouterStepIdTuple, String> eoRouterStepActualIdMap = null;

                if (CollectionUtils.isNotEmpty(eoActualDispatchStepActualList)) {
                    // 2.3 ??????????????????????????????????????????
                    List<MtEoStepActualVO35> mtEoStepActualVO35s = mtEoStepActualRepository
                                    .eoStepActualProcessedBatchGet(tenantId, eoStepActualIdList);
                    if (CollectionUtils.isNotEmpty(mtEoStepActualVO35s)) {
                        eoStepActualTotalQueueQtyMap = mtEoStepActualVO35s.stream().collect(Collectors
                                        .toMap(t -> t.getEoStepActualId(), t -> BigDecimal.valueOf(t.getQueueQty())));
                    }

                    // 2.4 ???????????????????????????????????????????????????
                    List<MtEoDispatchProcess> mtEoDispatchProcesses = mtEoDispatchProcessMapper
                                    .eoAndRouterStepLimitQuery(tenantId, eoActualDispatchStepActualList, null);
                    if (CollectionUtils.isNotEmpty(mtEoDispatchProcesses)) {
                        // ???eoId ??? routerStepId??????
                        Map<EoRouterStepIdTuple, List<MtEoDispatchProcess>> eoRouterStepIdTupleListMap =
                                        mtEoDispatchProcesses.stream().collect(
                                                        Collectors.groupingBy(t -> new EoRouterStepIdTuple(t.getEoId(),
                                                                        t.getRouterStepId())));

                        eoRouterStepProcessAssignQtyMap = new HashMap<>(eoRouterStepIdTupleListMap.size());
                        for (Map.Entry<EoRouterStepIdTuple, List<MtEoDispatchProcess>> entry : eoRouterStepIdTupleListMap
                                        .entrySet()) {
                            BigDecimal curProcessAssignQty = entry.getValue().stream().collect(CollectorsUtil
                                            .summingBigDecimal(t -> BigDecimal.valueOf(t.getAssignQty())));
                            eoRouterStepProcessAssignQtyMap.put(entry.getKey(), curProcessAssignQty);
                        }
                    }

                    // 2.5 ???????????????????????????????????????ID
                    eoRouterStepActualIdMap = eoActualDispatchStepActualList.stream()
                                    .collect(Collectors.toMap(
                                                    t -> new EoRouterStepIdTuple(t.getEoId(), t.getRouterStepId()),
                                                    t -> t.getEoStepActualId()));
                }

                // 2.5 ?????????????????????
                for (MtEoStepActualVO37 mtEoStepActual : eoMessageDispatchList) {
                    MtEoDispatchProcessVO14 result = new MtEoDispatchProcessVO14();
                    result.setEoId(mtEoStepActual.getEoId());
                    result.setRouterStepId(mtEoStepActual.getRouterStepId());

                    if (MapUtils.isEmpty(eoRouterStepActualIdMap)) {
                        result.setPublishQty(BigDecimal.ZERO.doubleValue());
                        result.setWorkingQty(BigDecimal.ZERO.doubleValue());
                        result.setAssignQty(BigDecimal.ZERO.doubleValue());
                    } else {
                        String eoStepActualId =
                                        eoRouterStepActualIdMap.get(new EoRouterStepIdTuple(mtEoStepActual.getEoId(),
                                                        mtEoStepActual.getRouterStepId()));
                        BigDecimal curWipQueueQty = BigDecimal.ZERO;
                        BigDecimal curWipTotalQueueQty = BigDecimal.ZERO;
                        BigDecimal curTotalQueueQty = BigDecimal.ZERO;
                        BigDecimal curProcessAssignQty = BigDecimal.ZERO;

                        BigDecimal tempQty = null;
                        if (MapUtils.isNotEmpty(eoStepActualWipQueueQtyMap)) {
                            tempQty = eoStepActualWipQueueQtyMap.get(eoStepActualId);
                            if (tempQty != null) {
                                curWipQueueQty = tempQty;
                            }
                        }

                        if (MapUtils.isNotEmpty(eoStepActualWipTotalQueueQtyMap)) {
                            tempQty = eoStepActualWipTotalQueueQtyMap.get(eoStepActualId);
                            if (tempQty != null) {
                                curWipTotalQueueQty = tempQty;
                            }
                        }

                        if (MapUtils.isNotEmpty(eoStepActualTotalQueueQtyMap)) {
                            tempQty = eoStepActualTotalQueueQtyMap.get(eoStepActualId);
                            if (tempQty != null) {
                                curTotalQueueQty = tempQty;
                            }
                        }

                        if (MapUtils.isNotEmpty(eoRouterStepProcessAssignQtyMap)) {
                            tempQty = eoRouterStepProcessAssignQtyMap.get(new EoRouterStepIdTuple(
                                            mtEoStepActual.getEoId(), mtEoStepActual.getRouterStepId()));
                            if (tempQty != null) {
                                curProcessAssignQty = tempQty;
                            }
                        }

                        // ?????????????????????assignQty = total_queueQty - wip_queueQty + process_assignQty
                        // ??????????????????????????????publishQty = total_queueQty - wip_queueQty
                        // ???????????????????????????workingQty = total_queueQty - wiptotal_queue
                        result.setAssignQty(curTotalQueueQty.subtract(curWipQueueQty).add(curProcessAssignQty)
                                        .doubleValue());
                        result.setPublishQty(curTotalQueueQty.subtract(curWipQueueQty).doubleValue());
                        result.setWorkingQty(curTotalQueueQty.subtract(curWipTotalQueueQty).doubleValue());
                    }
                    resultList.add(result);
                }
            } else {
                // ?????? eoStepActualId ??? wkcWipQueueQty ?????????
                Map<String, BigDecimal> eoStepActualWkcWipQueueQtyMap = null;

                if (MapUtils.isNotEmpty(mtEoStepWipMap)) {
                    eoStepActualWkcWipQueueQtyMap = new HashMap<>(mtEoStepWipMap.size());

                    // ?????? eoStepActualId ??? wipQueueQty ?????????
                    for (Map.Entry<String, List<MtEoStepWip>> entry : mtEoStepWipMap.entrySet()) {
                        BigDecimal curWkcWipQueueQty = entry.getValue().stream()
                                        .filter(c -> dto.getWorkcellId().equals(c.getWorkcellId()))
                                        .collect(CollectorsUtil
                                                        .summingBigDecimal(t -> BigDecimal.valueOf(t.getQueueQty())));
                        eoStepActualWkcWipQueueQtyMap.put(entry.getKey(), curWkcWipQueueQty);
                    }
                }

                // ?????? eoStepActualId ??? wkcTotalQueueQty ?????????
                Map<String, BigDecimal> eoStepActualWkcTotalQueueQtyMap = null;

                // 2.3 ??????????????????????????????????????????????????????????????????????????????????????????????????????
                List<MtEoStepWorkcellActual> mtEoStepWorkcellActuals = mtEoStepWorkcellActualRepository
                                .eoWkcProductionResultBatchGet(tenantId, mtEoStepWorkcellActualVO3s);
                if (CollectionUtils.isNotEmpty(mtEoStepWorkcellActuals)) {
                    eoStepActualWkcTotalQueueQtyMap = mtEoStepWorkcellActuals.stream().collect(Collectors
                                    .toMap(t -> t.getEoStepActualId(), t -> BigDecimal.valueOf(t.getQueueQty())));
                }

                // ?????? eoId???routerStepId???workcellId ??? processAssignQty ?????????
                Map<EoRouterStepIdTuple, BigDecimal> eoRouterStepWkcProcessAssignQtyMap = null;

                // 2.5 ???????????????????????????????????????????????????????????????????????????
                List<MtEoDispatchProcess> mtEoDispatchProcesses = mtEoDispatchProcessMapper.eoAndRouterStepLimitQuery(
                                tenantId, eoActualDispatchStepActualList, dto.getWorkcellId());
                if (CollectionUtils.isNotEmpty(mtEoDispatchProcesses)) {
                    // ???eoId ??? routerStepId??????
                    Map<EoRouterStepIdTuple, List<MtEoDispatchProcess>> eoRouterStepIdTupleListMap =
                                    mtEoDispatchProcesses.stream().collect(Collectors.groupingBy(
                                                    t -> new EoRouterStepIdTuple(t.getEoId(), t.getRouterStepId())));

                    eoRouterStepWkcProcessAssignQtyMap = new HashMap<>(eoRouterStepIdTupleListMap.size());

                    for (Map.Entry<EoRouterStepIdTuple, List<MtEoDispatchProcess>> entry : eoRouterStepIdTupleListMap
                                    .entrySet()) {
                        BigDecimal curWkcProcessAssignQty = entry.getValue().stream().collect(
                                        CollectorsUtil.summingBigDecimal(t -> BigDecimal.valueOf(t.getAssignQty())));
                        eoRouterStepWkcProcessAssignQtyMap.put(entry.getKey(), curWkcProcessAssignQty);
                    }
                }

                // ???????????????????????????????????????ID??????
                Map<EoRouterStepIdTuple, String> eoRouterStepActualIdMap = null;

                if (CollectionUtils.isNotEmpty(eoActualDispatchStepActualList)) {
                    eoRouterStepActualIdMap = eoActualDispatchStepActualList.stream()
                                    .collect(Collectors.toMap(
                                                    t -> new EoRouterStepIdTuple(t.getEoId(), t.getRouterStepId()),
                                                    t -> t.getEoStepActualId()));
                }

                // 2.6 ?????????????????????
                for (MtEoStepActualVO37 mtEoStepActual : eoMessageDispatchList) {
                    MtEoDispatchProcessVO14 result = new MtEoDispatchProcessVO14();
                    result.setEoId(mtEoStepActual.getEoId());
                    result.setRouterStepId(mtEoStepActual.getRouterStepId());

                    if (MapUtils.isEmpty(eoRouterStepActualIdMap)) {
                        result.setPublishQty(BigDecimal.ZERO.doubleValue());
                        result.setWorkingQty(BigDecimal.ZERO.doubleValue());
                        result.setAssignQty(BigDecimal.ZERO.doubleValue());
                    } else {
                        String eoStepActualId =
                                        eoRouterStepActualIdMap.get(new EoRouterStepIdTuple(mtEoStepActual.getEoId(),
                                                        mtEoStepActual.getRouterStepId()));

                        BigDecimal curWkcWipQueueQty = BigDecimal.ZERO;
                        BigDecimal curWkcTotalQueueQty = BigDecimal.ZERO;
                        BigDecimal curWkcProcessAssignQty = BigDecimal.ZERO;

                        BigDecimal tempQty = null;
                        if (MapUtils.isNotEmpty(eoStepActualWkcWipQueueQtyMap)) {
                            tempQty = eoStepActualWkcWipQueueQtyMap.get(eoStepActualId);
                            if (tempQty != null) {
                                curWkcWipQueueQty = tempQty;
                            }
                        }

                        if (MapUtils.isNotEmpty(eoStepActualWkcTotalQueueQtyMap)) {
                            tempQty = eoStepActualWkcTotalQueueQtyMap.get(eoStepActualId);
                            if (tempQty != null) {
                                curWkcTotalQueueQty = tempQty;
                            }
                        }

                        if (MapUtils.isNotEmpty(eoRouterStepWkcProcessAssignQtyMap)) {
                            tempQty = eoRouterStepWkcProcessAssignQtyMap.get(new EoRouterStepIdTuple(
                                            mtEoStepActual.getEoId(), mtEoStepActual.getRouterStepId()));
                            if (tempQty != null) {
                                curWkcProcessAssignQty = tempQty;
                            }
                        }

                        // ???????????????????????????????????????assignQty = wkctotal_queueQty??????????????? + wkcprocess_assignQty???????????????
                        // ?????????????????????????????????????????????publishQty = wkctotal_queueQty(?????????)
                        // ??????????????????????????????????????????workingQty = wkctotal_queueQty(?????????) ??? wkcwip_queueQty(?????????)
                        result.setAssignQty(curWkcTotalQueueQty.add(curWkcProcessAssignQty).doubleValue());
                        result.setPublishQty(curWkcTotalQueueQty.doubleValue());
                        result.setWorkingQty(curWkcTotalQueueQty.subtract(curWkcWipQueueQty).doubleValue());
                        resultList.add(result);
                    }
                }
            }
        }

        // 2. ???????????????PLAN_DISPATCH?????????
        if (CollectionUtils.isNotEmpty(eoPlanDispatchStrategyList)) {
            Map<String, List<MtEoStepActualVO36>> eoStepActualMap = null;

            // 2.1 ?????????????????????????????????????????????????????????????????????????????????????????????
            List<MtEoStepActualVO36> eoPlanDispatchStepActualList = new ArrayList<>();

            // ??????????????????????????????????????? eoMessageList
            List<MtEoStepActualVO37> eoMessagePlanList = new ArrayList<>();

            eoStepActualMap = eoStepActualList.stream().collect(Collectors.groupingBy(t -> t.getEoId()));

            // 2.1 ?????????????????? ???????????? eoStepActualId ??????
            for (MtEoDispatchActionVO22 mtEoDispatchAction : eoPlanDispatchStrategyList) {
                List<MtEoStepActualVO36> currentEoStepActualList = eoStepActualMap.get(mtEoDispatchAction.getEoId());
                if (CollectionUtils.isNotEmpty(currentEoStepActualList)) {
                    eoPlanDispatchStepActualList.addAll(currentEoStepActualList);
                }

                eoMessagePlanList.addAll(eoMessageListMap.get(mtEoDispatchAction.getEoId()));
            }

            // ?????????????????????????????????Map??????: eoId-routerStepId
            Map<EoRouterStepIdTuple, MtEoStepActualVO36> eoPlanDispatchStepActualMap =
                            eoPlanDispatchStepActualList.stream().collect(Collectors.toMap(
                                            t -> new EoRouterStepIdTuple(t.getEoId(), t.getRouterStepId()), t -> t));

            // ???????????????????????? eoStepActualId ???????????????????????????
            List<MtEoStepActualVO36> isEoStepActualList = new ArrayList<>();
            List<MtEoStepActualVO36> unEoStepActualList = new ArrayList<>();
            for (MtEoStepActualVO37 mtEoStepActual : eoMessagePlanList) {
                MtEoStepActualVO36 mtEoStepActualVO36 = eoPlanDispatchStepActualMap.get(
                                new EoRouterStepIdTuple(mtEoStepActual.getEoId(), mtEoStepActual.getRouterStepId()));
                if (mtEoStepActualVO36 != null) {
                    isEoStepActualList.add(mtEoStepActualVO36);
                } else {
                    MtEoStepActualVO36 mtEoStepActualTemp = new MtEoStepActualVO36();
                    mtEoStepActualTemp.setEoId(mtEoStepActual.getEoId());
                    mtEoStepActualTemp.setRouterStepId(mtEoStepActual.getRouterStepId());
                    unEoStepActualList.add(mtEoStepActualTemp);
                }
            }

            // ????????? eoStepActualId ???????????????
            if (CollectionUtils.isNotEmpty(unEoStepActualList)) {
                // ???????????? workcellId ????????????????????????????????????????????????wkc??????????????????????????????
                // 2.2 ???????????????????????????????????????????????????
                List<MtEoDispatchProcess> mtEoDispatchProcesses = mtEoDispatchProcessMapper
                                .eoAndRouterStepLimitQuery(tenantId, unEoStepActualList, dto.getWorkcellId());

                // 2.3 ?????????????????????????????????????????????
                List<MtEoDispatchAction> mtEoDispatchActions = mtEoDispatchActionMapper
                                .eoAndRouterStepLimitQuery(tenantId, unEoStepActualList, dto.getWorkcellId());

                // ???eoId???routerStepId?????????????????????
                Map<EoRouterStepIdTuple, BigDecimal> eoRouterStepProAssignQtyMap = null;
                if (CollectionUtils.isNotEmpty(mtEoDispatchProcesses)) {
                    Map<EoRouterStepIdTuple, List<MtEoDispatchProcess>> eoRouterStepIdTupleListMap =
                                    mtEoDispatchProcesses.stream().collect(Collectors.groupingBy(
                                                    t -> new EoRouterStepIdTuple(t.getEoId(), t.getRouterStepId())));

                    eoRouterStepProAssignQtyMap = new HashMap<>(eoRouterStepIdTupleListMap.size());

                    for (Map.Entry<EoRouterStepIdTuple, List<MtEoDispatchProcess>> entry : eoRouterStepIdTupleListMap
                                    .entrySet()) {
                        BigDecimal curProAssignQty = entry.getValue().stream().collect(
                                        CollectorsUtil.summingBigDecimal(t -> BigDecimal.valueOf(t.getAssignQty())));
                        eoRouterStepProAssignQtyMap.put(entry.getKey(), curProAssignQty);
                    }
                }

                Map<EoRouterStepIdTuple, BigDecimal> eoRouterStepActAssignQtyMap = null;
                if (CollectionUtils.isNotEmpty(mtEoDispatchActions)) {
                    Map<EoRouterStepIdTuple, List<MtEoDispatchAction>> eoRouterStepIdTupleListMap =
                                    mtEoDispatchActions.stream().collect(Collectors.groupingBy(
                                                    t -> new EoRouterStepIdTuple(t.getEoId(), t.getRouterStepId())));

                    eoRouterStepActAssignQtyMap = new HashMap<>(eoRouterStepIdTupleListMap.size());

                    for (Map.Entry<EoRouterStepIdTuple, List<MtEoDispatchAction>> entry : eoRouterStepIdTupleListMap
                                    .entrySet()) {
                        BigDecimal curActAssignQty = entry.getValue().stream().collect(
                                        CollectorsUtil.summingBigDecimal(t -> BigDecimal.valueOf(t.getAssignQty())));
                        eoRouterStepActAssignQtyMap.put(entry.getKey(), curActAssignQty);
                    }
                }

                // 2.4 ?????????????????????
                for (MtEoStepActualVO36 mtEoStepActual : unEoStepActualList) {
                    MtEoDispatchProcessVO14 result = new MtEoDispatchProcessVO14();
                    result.setEoId(mtEoStepActual.getEoId());
                    result.setRouterStepId(mtEoStepActual.getRouterStepId());

                    BigDecimal proAssignQty = BigDecimal.ZERO;
                    BigDecimal actAssignQty = BigDecimal.ZERO;

                    BigDecimal tempQty = null;
                    if (MapUtils.isNotEmpty(eoRouterStepProAssignQtyMap)) {
                        tempQty = eoRouterStepProAssignQtyMap.get(new EoRouterStepIdTuple(mtEoStepActual.getEoId(),
                                        mtEoStepActual.getRouterStepId()));
                        if (tempQty != null) {
                            proAssignQty = tempQty;
                        }
                    }

                    if (MapUtils.isNotEmpty(eoRouterStepActAssignQtyMap)) {
                        tempQty = eoRouterStepActAssignQtyMap.get(new EoRouterStepIdTuple(mtEoStepActual.getEoId(),
                                        mtEoStepActual.getRouterStepId()));
                        if (tempQty != null) {
                            actAssignQty = tempQty;
                        }
                    }

                    // ????????????????????????assignQty=proAssignQty+actAssignQty???
                    // ??????????????????????????????publishQty = actAssignQty???
                    // ???????????????????????????workingQty =0
                    result.setAssignQty(proAssignQty.add(actAssignQty).doubleValue());
                    result.setPublishQty(actAssignQty.doubleValue());
                    result.setWorkingQty(BigDecimal.ZERO.doubleValue());
                    resultList.add(result);
                }
            }

            // ????????? eoStepActualId ???????????????
            if (CollectionUtils.isNotEmpty(isEoStepActualList)) {
                if (StringUtils.isEmpty(dto.getWorkcellId())) {
                    // 2.2 ?????? eoStepActualId ?????? workcellId
                    MtEoStepWorkcellActualVO13 eoStepWorkcellActual = new MtEoStepWorkcellActualVO13();
                    eoStepWorkcellActual.setEoStepActualIdList(isEoStepActualList.stream()
                                    .map(MtEoStepActualVO36::getEoStepActualId).collect(Collectors.toList()));
                    List<MtEoStepWorkcellActualVO14> workcellActualList = mtEoStepWorkcellActualRepository
                                    .propertyLimitEoStepWkcActualPropertyBatchQuery(tenantId, eoStepWorkcellActual);

                    BigDecimal sumQueueQty = BigDecimal.ZERO;
                    if (CollectionUtils.isNotEmpty(workcellActualList)) {
                        // 2.3 ??????????????????????????????????????????????????????????????????
                        List<MtEoStepWorkcellActualVO3> eoStepWorkcellActualList =
                                        workcellActualList.stream().map(t -> {
                                            MtEoStepWorkcellActualVO3 mtEoStepWorkcellActualVO3 =
                                                            new MtEoStepWorkcellActualVO3();
                                            mtEoStepWorkcellActualVO3.setWorkcellId(t.getWorkcellId());
                                            mtEoStepWorkcellActualVO3.setEoStepActualId(t.getEoStepActualId());
                                            return mtEoStepWorkcellActualVO3;
                                        }).collect(Collectors.toList());

                        List<MtEoStepWorkcellActual> mtEoStepWorkcellActuals = mtEoStepWorkcellActualRepository
                                        .eoWkcProductionResultBatchGet(tenantId, eoStepWorkcellActualList);
                        if (CollectionUtils.isNotEmpty(mtEoStepWorkcellActuals)) {
                            sumQueueQty = mtEoStepWorkcellActuals.stream().collect(
                                            CollectorsUtil.summingBigDecimal(t -> BigDecimal.valueOf(t.getQueueQty())));
                        }
                    }

                    // 2.3 ???????????????????????????????????????????????????
                    List<MtEoDispatchProcess> mtEoDispatchProcesses = mtEoDispatchProcessMapper
                                    .eoAndRouterStepLimitQuery(tenantId, isEoStepActualList, null);

                    // 2.4 ?????????????????????????????????????????????
                    List<MtEoDispatchAction> mtEoDispatchActions = mtEoDispatchActionMapper
                                    .eoAndRouterStepLimitQuery(tenantId, isEoStepActualList, null);

                    // ???eoId???routerStepId?????????????????????
                    Map<EoRouterStepIdTuple, BigDecimal> eoRouterStepProAssignQtyMap = null;
                    if (CollectionUtils.isNotEmpty(mtEoDispatchProcesses)) {
                        Map<EoRouterStepIdTuple, List<MtEoDispatchProcess>> eoRouterStepIdTupleListMap =
                                        mtEoDispatchProcesses.stream().collect(
                                                        Collectors.groupingBy(t -> new EoRouterStepIdTuple(t.getEoId(),
                                                                        t.getRouterStepId())));

                        eoRouterStepProAssignQtyMap = new HashMap<>(eoRouterStepIdTupleListMap.size());

                        for (Map.Entry<EoRouterStepIdTuple, List<MtEoDispatchProcess>> entry : eoRouterStepIdTupleListMap
                                        .entrySet()) {
                            BigDecimal curProAssignQty = entry.getValue().stream().collect(CollectorsUtil
                                            .summingBigDecimal(t -> BigDecimal.valueOf(t.getAssignQty())));
                            eoRouterStepProAssignQtyMap.put(entry.getKey(), curProAssignQty);
                        }
                    }

                    Map<EoRouterStepIdTuple, BigDecimal> eoRouterStepActAssignQtyMap = null;
                    if (CollectionUtils.isNotEmpty(mtEoDispatchActions)) {
                        Map<EoRouterStepIdTuple, List<MtEoDispatchAction>> eoRouterStepIdTupleListMap =
                                        mtEoDispatchActions.stream().collect(
                                                        Collectors.groupingBy(t -> new EoRouterStepIdTuple(t.getEoId(),
                                                                        t.getRouterStepId())));

                        eoRouterStepActAssignQtyMap = new HashMap<>(eoRouterStepIdTupleListMap.size());

                        for (Map.Entry<EoRouterStepIdTuple, List<MtEoDispatchAction>> entry : eoRouterStepIdTupleListMap
                                        .entrySet()) {
                            BigDecimal curActAssignQty = entry.getValue().stream().collect(CollectorsUtil
                                            .summingBigDecimal(t -> BigDecimal.valueOf(t.getAssignQty())));
                            eoRouterStepActAssignQtyMap.put(entry.getKey(), curActAssignQty);
                        }
                    }

                    // 2.5 ?????????????????????
                    boolean hasQtyFlag = false;
                    BigDecimal tempQty = null;
                    BigDecimal proAssignQty = BigDecimal.ZERO;
                    BigDecimal actAssignQty = BigDecimal.ZERO;
                    for (MtEoStepActualVO36 mtEoStepActual : isEoStepActualList) {
                        MtEoDispatchProcessVO14 result = new MtEoDispatchProcessVO14();
                        result.setEoId(mtEoStepActual.getEoId());
                        result.setRouterStepId(mtEoStepActual.getRouterStepId());

                        proAssignQty = BigDecimal.ZERO;
                        actAssignQty = BigDecimal.ZERO;

                        if (MapUtils.isNotEmpty(eoRouterStepProAssignQtyMap)) {
                            tempQty = eoRouterStepProAssignQtyMap.get(new EoRouterStepIdTuple(mtEoStepActual.getEoId(),
                                            mtEoStepActual.getRouterStepId()));
                            if (tempQty != null) {
                                proAssignQty = tempQty;
                                hasQtyFlag = true;
                            }
                        }

                        if (MapUtils.isNotEmpty(eoRouterStepActAssignQtyMap)) {
                            tempQty = eoRouterStepActAssignQtyMap.get(new EoRouterStepIdTuple(mtEoStepActual.getEoId(),
                                            mtEoStepActual.getRouterStepId()));
                            if (tempQty != null) {
                                actAssignQty = tempQty;
                                hasQtyFlag = true;
                            }
                        }

                        if (hasQtyFlag) {
                            // ?????????????????????
                            // ?????????????????????:ssignQty=proAssignQty+actAssignQty???
                            // ??????????????????????????????publishQty = actAssignQty???
                            // ???????????????????????????workingQty =sum_queueQty???
                            result.setAssignQty(proAssignQty.add(actAssignQty).doubleValue());
                            result.setPublishQty(actAssignQty.doubleValue());
                            result.setWorkingQty(sumQueueQty.doubleValue());
                        } else {
                            // ???????????????????????????????????????
                            // ????????????????????????assignQty= sum_queueQty???
                            // ??????????????????????????????publishQty = sum_queueQty???
                            // ???????????????????????????workingQty =sum_queueQty???
                            result.setAssignQty(sumQueueQty.doubleValue());
                            result.setPublishQty(sumQueueQty.doubleValue());
                            result.setWorkingQty(sumQueueQty.doubleValue());
                        }
                        resultList.add(result);
                    }
                } else {
                    // 2.2 ?????? eoStepActualId???workcellId
                    BigDecimal sumQueueQty = BigDecimal.ZERO;
                    if (CollectionUtils.isNotEmpty(isEoStepActualList)) {
                        // 2.3 ??????????????????????????????????????????????????????????????????
                        List<MtEoStepWorkcellActualVO3> eoStepWorkcellActualList =
                                        isEoStepActualList.stream().map(t -> {
                                            MtEoStepWorkcellActualVO3 mtEoStepWorkcellActualVO3 =
                                                            new MtEoStepWorkcellActualVO3();
                                            mtEoStepWorkcellActualVO3.setWorkcellId(dto.getWorkcellId());
                                            mtEoStepWorkcellActualVO3.setEoStepActualId(t.getEoStepActualId());
                                            return mtEoStepWorkcellActualVO3;
                                        }).collect(Collectors.toList());

                        List<MtEoStepWorkcellActual> mtEoStepWorkcellActuals = mtEoStepWorkcellActualRepository
                                        .eoWkcProductionResultBatchGet(tenantId, eoStepWorkcellActualList);
                        if (CollectionUtils.isNotEmpty(mtEoStepWorkcellActuals)) {
                            sumQueueQty = mtEoStepWorkcellActuals.stream().collect(
                                            CollectorsUtil.summingBigDecimal(t -> BigDecimal.valueOf(t.getQueueQty())));
                        }
                    }

                    // 2.3 ???????????????????????????????????????????????????
                    List<MtEoDispatchProcess> mtEoDispatchProcesses = mtEoDispatchProcessMapper
                                    .eoAndRouterStepLimitQuery(tenantId, isEoStepActualList, dto.getWorkcellId());

                    // 2.4 ?????????????????????????????????????????????
                    List<MtEoDispatchAction> mtEoDispatchActions = mtEoDispatchActionMapper
                                    .eoAndRouterStepLimitQuery(tenantId, isEoStepActualList, dto.getWorkcellId());

                    // ???eoId???routerStepId?????????????????????
                    Map<EoRouterStepIdTuple, BigDecimal> eoRouterStepProAssignQtyMap = null;
                    if (CollectionUtils.isNotEmpty(mtEoDispatchProcesses)) {
                        Map<EoRouterStepIdTuple, List<MtEoDispatchProcess>> eoRouterStepIdTupleListMap =
                                        mtEoDispatchProcesses.stream().collect(
                                                        Collectors.groupingBy(t -> new EoRouterStepIdTuple(t.getEoId(),
                                                                        t.getRouterStepId())));

                        eoRouterStepProAssignQtyMap = new HashMap<>(eoRouterStepIdTupleListMap.size());

                        for (Map.Entry<EoRouterStepIdTuple, List<MtEoDispatchProcess>> entry : eoRouterStepIdTupleListMap
                                        .entrySet()) {
                            BigDecimal curProAssignQty = entry.getValue().stream().collect(CollectorsUtil
                                            .summingBigDecimal(t -> BigDecimal.valueOf(t.getAssignQty())));
                            eoRouterStepProAssignQtyMap.put(entry.getKey(), curProAssignQty);
                        }
                    }

                    Map<EoRouterStepIdTuple, BigDecimal> eoRouterStepActAssignQtyMap = null;
                    if (CollectionUtils.isNotEmpty(mtEoDispatchActions)) {
                        Map<EoRouterStepIdTuple, List<MtEoDispatchAction>> eoRouterStepIdTupleListMap =
                                        mtEoDispatchActions.stream().collect(
                                                        Collectors.groupingBy(t -> new EoRouterStepIdTuple(t.getEoId(),
                                                                        t.getRouterStepId())));

                        eoRouterStepActAssignQtyMap = new HashMap<>(eoRouterStepIdTupleListMap.size());

                        for (Map.Entry<EoRouterStepIdTuple, List<MtEoDispatchAction>> entry : eoRouterStepIdTupleListMap
                                        .entrySet()) {
                            BigDecimal curActAssignQty = entry.getValue().stream().collect(CollectorsUtil
                                            .summingBigDecimal(t -> BigDecimal.valueOf(t.getAssignQty())));
                            eoRouterStepActAssignQtyMap.put(entry.getKey(), curActAssignQty);
                        }
                    }

                    // 2.5 ?????????????????????
                    boolean hasQtyFlag = false;
                    BigDecimal tempQty = null;
                    BigDecimal proAssignQty = BigDecimal.ZERO;
                    BigDecimal actAssignQty = BigDecimal.ZERO;
                    for (MtEoStepActualVO36 mtEoStepActual : isEoStepActualList) {
                        MtEoDispatchProcessVO14 result = new MtEoDispatchProcessVO14();
                        result.setEoId(mtEoStepActual.getEoId());
                        result.setRouterStepId(mtEoStepActual.getRouterStepId());

                        proAssignQty = BigDecimal.ZERO;
                        actAssignQty = BigDecimal.ZERO;

                        if (MapUtils.isNotEmpty(eoRouterStepProAssignQtyMap)) {
                            tempQty = eoRouterStepProAssignQtyMap.get(new EoRouterStepIdTuple(mtEoStepActual.getEoId(),
                                            mtEoStepActual.getRouterStepId()));
                            if (tempQty != null) {
                                proAssignQty = tempQty;
                                hasQtyFlag = true;
                            }
                        }

                        if (MapUtils.isNotEmpty(eoRouterStepActAssignQtyMap)) {
                            tempQty = eoRouterStepActAssignQtyMap.get(new EoRouterStepIdTuple(mtEoStepActual.getEoId(),
                                            mtEoStepActual.getRouterStepId()));
                            if (tempQty != null) {
                                actAssignQty = tempQty;
                                hasQtyFlag = true;
                            }
                        }

                        if (hasQtyFlag) {
                            // ?????????????????????
                            // ?????????????????????:ssignQty=proAssignQty+actAssignQty???
                            // ??????????????????????????????publishQty = actAssignQty???
                            // ???????????????????????????workingQty =sum_queueQty???
                            result.setAssignQty(proAssignQty.add(actAssignQty).doubleValue());
                            result.setPublishQty(actAssignQty.doubleValue());
                            result.setWorkingQty(sumQueueQty.doubleValue());
                        } else {
                            // ???????????????????????????????????????
                            // ????????????????????????assignQty= sum_queueQty???
                            // ??????????????????????????????publishQty = sum_queueQty???
                            // ???????????????????????????workingQty =sum_queueQty???
                            result.setAssignQty(sumQueueQty.doubleValue());
                            result.setPublishQty(sumQueueQty.doubleValue());
                            result.setWorkingQty(sumQueueQty.doubleValue());
                        }
                        resultList.add(result);
                    }
                }
            }
        }


        // ????????????????????????????????????????????????????????????????????????
        // List<MtEoStepActualVO36> eoStepActualList =
        // mtEoStepActualRepository.eoAndStepLimitStepActualBatchQuery(tenantId, dto.getEoMessageList());
        //
        // List<MtEoDispatchProcessVO14> resultList = new ArrayList<>(dto.getEoMessageList().size());

        // if (CollectionUtils.isEmpty(eoStepActualList)){
        // if (StringUtils.isEmpty(dto.getWorkcellId())){
        // List<MtEoStepActualVO37> eoMessageList = dto.getEoMessageList();
        // for (MtEoStepActualVO37 vo37 : eoMessageList) {
        // MtEoDispatchProcessVO14 vo14 = new MtEoDispatchProcessVO14();
        //
        // MtEoDispatchProcess process = new MtEoDispatchProcess();
        // process.setRouterStepId(vo37.getRouterStepId());
        // process.setEoId(vo37.getEoId());
        // process.setTenantId(tenantId);
        // mtEoDispatchProcessMapper.select(process)
        //
        // }
        //
        // }else{
        //
        // }
        // }else{
        //
        // }
        return resultList;
    }

    private static class EoRouterStepIdTuple implements Serializable {

        private static final long serialVersionUID = 6965257114663873859L;
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
