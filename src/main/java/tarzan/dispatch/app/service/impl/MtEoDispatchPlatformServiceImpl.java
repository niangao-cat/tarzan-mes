package tarzan.dispatch.app.service.impl;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.calendar.domain.entity.MtCalendarShift;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.calendar.domain.vo.MtCalendarShiftVO2;
import tarzan.calendar.domain.vo.MtCalendarVO2;
import tarzan.calendar.domain.vo.MtCalendarVO6;
import tarzan.calendar.domain.vo.MtCalendarVO7;
import tarzan.dispatch.api.dto.*;
import tarzan.dispatch.app.service.MtEoDispatchPlatformService;
import tarzan.dispatch.domain.repository.MtEoDispatchActionRepository;
import tarzan.dispatch.domain.repository.MtEoDispatchProcessRepository;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.dispatch.domain.vo.*;
import tarzan.dispatch.infra.mapper.MtEoDispatchProcessMapper;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.entity.MtModWorkcellSchedule;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.repository.MtModWorkcellScheduleRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

/**
 * @author : MrZ
 * @date : 2019-12-03 17:42
 **/
@Service
public class MtEoDispatchPlatformServiceImpl implements MtEoDispatchPlatformService {
    @Autowired
    private MtUserOrganizationRepository userOrganizationRepository;
    @Autowired
    private MtModProductionLineRepository modProductionLineRepository;
    @Autowired
    private MtModOrganizationRelRepository modOrganizationRelRepository;
    @Autowired
    private MtOperationRepository operationRepository;
    @Autowired
    private MtOperationWkcDispatchRelRepository operationWkcDispatchRelRepository;
    @Autowired
    private MtModWorkcellRepository modWorkcellRepository;
    @Autowired
    private MtCalendarRepository calendarRepository;
    @Autowired
    private MtCalendarShiftRepository calendarShiftRepository;
    @Autowired
    private MtModWorkcellScheduleRepository modWorkcellScheduleRepository;
    @Autowired
    private MtEoDispatchProcessRepository eoDispatchProcessRepository;
    @Autowired
    private MtEoDispatchActionRepository eoDispatchActionRepository;
    @Autowired
    private MtErrorMessageRepository errorMessageRepository;
    @Autowired
    private MtEoDispatchProcessMapper eoDispatchProcessMapper;
    @Autowired
    private MtWorkOrderRepository workOrderRepository;
    @Autowired
    private MtMaterialRepository materialRepository;
    @Autowired
    private MtEoRepository eoRepository;
    @Autowired
    private MtGenStatusRepository genStatusRepository;

    @Override
    public String defaultSiteUi(Long tenantId) {
        // 获取用户默认站点
        Long userId = DetailsHelper.getUserDetails().getUserId();
        MtUserOrganization userOrganization = new MtUserOrganization();
        userOrganization.setUserId(userId);
        userOrganization.setOrganizationType("SITE");
        MtUserOrganization defaultSite =
                        userOrganizationRepository.userDefaultOrganizationGet(tenantId, userOrganization);
        if (null == defaultSite) {
            throw new MtException("MT_PERMISSION_0006", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PERMISSION_0006", "PERMISSION", "【API:defaultSiteUi】"));
        }
        return defaultSite.getOrganizationId();
    }

    @Override
    public List<MtModProductionLine> userProdLineUi(Long tenantId) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        MtUserOrganization userOrganization = new MtUserOrganization();
        userOrganization.setUserId(userId);
        userOrganization.setOrganizationType("PROD_LINE");
        // 获取用户有权限的生产线
        List<MtUserOrganization> userOrganizations =
                        userOrganizationRepository.userOrganizationPermissionQuery(tenantId, userOrganization);
        if (CollectionUtils.isEmpty(userOrganizations)) {
            return new ArrayList<>();
        }
        // 获取生产线详细信息
        return modProductionLineRepository.prodLineBasicPropertyBatchGet(tenantId, userOrganizations.stream()
                        .map(MtUserOrganization::getOrganizationId).collect(Collectors.toList()));
    }

    @Override
    public List<MtOperation> operationByProdLineUi(Long tenantId, MtEoDispatchPlatformDTO11 dto) {
        // 传入必输性验证
        if (StringUtils.isEmpty(dto.getDefaultSiteId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "defaultSiteId", "【API:operationByProdLineUi】"));
        }
        if (StringUtils.isEmpty(dto.getProdLineId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "prodLineId", "【API:operationByProdLineUi】"));
        }
        // 获取生产线下工作单元
        MtModOrganizationVO2 modOrganizationVo2 = new MtModOrganizationVO2();
        modOrganizationVo2.setTopSiteId(dto.getDefaultSiteId());
        modOrganizationVo2.setParentOrganizationType("PROD_LINE");
        modOrganizationVo2.setParentOrganizationId(dto.getProdLineId());
        modOrganizationVo2.setOrganizationType("WORKCELL");
        modOrganizationVo2.setQueryType("ALL");
        List<MtModOrganizationItemVO> organizationItems =
                        modOrganizationRelRepository.subOrganizationRelQuery(tenantId, modOrganizationVo2);
        if (CollectionUtils.isEmpty(organizationItems)) {
            return new ArrayList<>();
        }
        MtOpWkcDispatchRelVO9 rel = new MtOpWkcDispatchRelVO9();
        rel.setWorkcellIdList(organizationItems.stream().map(MtModOrganizationItemVO::getOrganizationId)
                        .collect(Collectors.toList()));
        List<MtOpWkcDispatchRelVO10> rels =
                        operationWkcDispatchRelRepository.wkcLimitAvailableOperationBatchQuery(tenantId, rel);
        if (CollectionUtils.isEmpty(rels)) {
            return new ArrayList<>();
        }
        return operationRepository.operationBatchGet(tenantId, rels.stream().map(MtOpWkcDispatchRelVO10::getOperationId)
                        .distinct().collect(Collectors.toList()));
    }

    @Override
    public List<MtEoDispatchPlatformDTO4> wkcDispatchRangeUi(Long tenantId, MtEoDispatchPlatformDTO3 dto) {
        // 传入必输性验证
        if (StringUtils.isEmpty(dto.getDefaultSiteId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "defaultSiteId", "【API:wkcDispatchRangeUi】"));
        }
        if (StringUtils.isEmpty(dto.getProdLineId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "prodLineId", "【API:wkcDispatchRangeUi】"));
        }
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "operationId", "【API:wkcDispatchRangeUi】"));
        }
        List<MtEoDispatchPlatformDTO4> result = new ArrayList<>();
        MtOpWkcDispatchRelVO3 rel = new MtOpWkcDispatchRelVO3();
        rel.setOperationId(dto.getOperationId());
        rel.setProductionLineId(dto.getProdLineId());
        rel.setSiteId(dto.getDefaultSiteId());
        // 4) 获取调度范围逻辑
        List<MtOpWkcDispatchRelVO2> rels =
                        operationWkcDispatchRelRepository.operationLimitAvailableWorkcellQuery(tenantId, rel);
        if (CollectionUtils.isEmpty(rels)) {
            return result;
        }
        // 获取WKC详细信息
        List<MtModWorkcell> workcells = modWorkcellRepository.workcellBasicPropertyBatchGet(tenantId,
                        rels.stream().map(MtOpWkcDispatchRelVO2::getWorkcellId).collect(Collectors.toList()));
        // 准备计算延期数量的数据
        List<String> workcellIds = workcells.stream().map(MtModWorkcell::getWorkcellId).collect(Collectors.toList());
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = c.getTime();
        // 获取发布的信息
        MtEoDispatchActionVO21 action = new MtEoDispatchActionVO21();
        action.setOperationIdList(Collections.singletonList(dto.getOperationId()));
        action.setWorkcellIdList(workcellIds);
        action.setShiftDateTo(yesterday);
        List<MtEoDispatchActionVO19> actions =
                        eoDispatchActionRepository.propertyLimitDispatchedActionPropertyBatchQuery(tenantId, action);
        // 获取未发布的信息
        MtEoDispatchProcessVO13 process = new MtEoDispatchProcessVO13();
        process.setOperationIdList(Collections.singletonList(dto.getOperationId()));
        process.setWorkcellIdList(workcellIds);
        process.setShiftDateTo(yesterday);
        List<MtEoDispatchProcessVO10> processes =
                        eoDispatchProcessRepository.propertyLimitDispatchedProcessPropertyBatchQuery(tenantId, process);
        // 拼接结果
        for (MtModWorkcell ever : workcells) {
            MtEoDispatchPlatformDTO4 one = new MtEoDispatchPlatformDTO4();
            BeanUtils.copyProperties(ever, one);
            // 计算延期数量
            double extensionsQty = 0D;
            if (CollectionUtils.isNotEmpty(actions)) {
                double actionQty = actions.stream()
                                .filter(t -> t.getWorkcellId().equals(one.getWorkcellId()) && t.getAssignQty() != null)
                                .mapToDouble(MtEoDispatchActionVO19::getAssignQty).sum();
                extensionsQty = BigDecimal.valueOf(extensionsQty).add(BigDecimal.valueOf(actionQty)).doubleValue();
            }
            if (CollectionUtils.isNotEmpty(processes)) {
                double processQty = processes.stream()
                                .filter(t -> t.getWorkcellId().equals(one.getWorkcellId()) && t.getAssignQty() != null)
                                .mapToDouble(MtEoDispatchProcessVO10::getAssignQty).sum();
                extensionsQty = BigDecimal.valueOf(extensionsQty).add(BigDecimal.valueOf(processQty)).doubleValue();
            }
            one.setExtensionsQty(extensionsQty);
            result.add(one);
        }
        return result;
    }

    @Override
    public Page<MtEoDispatchPlatformDTO2> dispatchPlatformTableUi(Long tenantId, MtEoDispatchPlatformDTO dto,
                    PageRequest pageRequest) {
        // 传入必输性验证
        if (StringUtils.isEmpty(dto.getDefaultSiteId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "defaultSiteId", "【API:dispatchPlatformTableUi】"));
        }
        if (StringUtils.isEmpty(dto.getProdLineId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "prodLineId", "【API:dispatchPlatformTableUi】"));
        }
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "operationId", "【API:dispatchPlatformTableUi】"));
        }
        // 这里没有使用业务给出文档的rangeLimitToBeDispatchedEoQuery API，这个API不能分页
        // 获取需调度执行作业及其步骤信息,这里就已经获取所有的行了
        Page<MtEoDispatchPlatformVO1> base = PageHelper.doPage(pageRequest,
                        () -> eoDispatchProcessMapper.dispatchPlatformEoQueryUi(tenantId, dto));
        if (CollectionUtils.isEmpty(base.getContent())) {
            return new Page<>();
        }
        // 获取WO信息
        List<MtWorkOrder> workOrders = workOrderRepository.woPropertyBatchGet(tenantId, base.getContent().stream()
                        .map(MtEoDispatchPlatformVO1::getWorkOrderId).collect(Collectors.toList()));
        List<MtMaterialVO> materials = materialRepository.materialPropertyBatchGet(tenantId, base.getContent().stream()
                        .map(MtEoDispatchPlatformVO1::getMaterialId).collect(Collectors.toList()));
        /// todo 这里临时注释掉
        // 批量获取未调度数
        // List<MtEoStepActualVO37> batchUnAssign = base.getContent().stream().map(t -> {
        // MtEoStepActualVO37 one = new MtEoStepActualVO37();
        // one.setEoId(t.getEoId());
        // one.setRouterStepId(t.getRouterStepId());
        // return one;
        // }).collect(Collectors.toList());
        // List<MtEoDispatchActionVO23> unAssignList =
        // eoDispatchActionRepository.toBeDispatchedEoUnassignQtyBatchGet(tenantId, batchUnAssign);
        // 批量获取调度数
        // List<MtEoDispatchProcessVO15> batchAssign = base.getContent().stream().map(t -> {
        // MtEoDispatchProcessVO15 one = new MtEoDispatchProcessVO15();
        // one.setEoId(t.getEoId());
        // one.setRouterStepId(t.getRouterStepId());
        // return one;
        // }).collect(Collectors.toList());
        // List<MtEoDispatchProcessVO14> assignList =
        // eoDispatchProcessRepository.dispatchedEoAssignQtyBatchGet(tenantId, batchAssign);

        List<MtEoDispatchPlatformDTO2> resultList = new ArrayList<>();
        for (MtEoDispatchPlatformVO1 ever : base.getContent()) {
            MtEoDispatchPlatformDTO2 one = new MtEoDispatchPlatformDTO2();
            BeanUtils.copyProperties(ever, one);
            one.setRouterOperationId(ever.getSpecialId());
            // 赋值WO名称
            Optional<MtWorkOrder> woOp = workOrders.stream()
                            .filter(t -> ever.getWorkOrderId().equals(t.getWorkOrderId())).findFirst();
            woOp.ifPresent(mtWorkOrder -> one.setWorkOrderNum(mtWorkOrder.getWorkOrderNum()));
            // 赋值物料编码和物料名称
            Optional<MtMaterialVO> materialOp =
                            materials.stream().filter(t -> ever.getMaterialId().equals(t.getMaterialId())).findFirst();
            if (materialOp.isPresent()) {
                one.setMaterialCode(materialOp.get().getMaterialCode());
                one.setMaterialName(materialOp.get().getMaterialName());
            }
            // 计算未调度数
            /// todo 改造批量获取未调度数
            MtEoDispatchActionVO16 unDispatchQuery = new MtEoDispatchActionVO16();
            unDispatchQuery.setRouterStepId(ever.getRouterStepId());
            unDispatchQuery.setEoId(ever.getEoId());
            MtEoDispatchActionVO10 unDispatchQty =
                            eoDispatchActionRepository.toBeDispatchedEoUnassignQtyGet(tenantId, unDispatchQuery);
            if (null != unDispatchQty) {
                one.setDispatchableQty(unDispatchQty.getDispatchableQty());
            } else {
                one.setDispatchableQty(0D);
            }
            /// todo 改造批量获取调度数
            MtEoDispatchActionVO2 dispatchQuery = new MtEoDispatchActionVO2();
            dispatchQuery.setRouterStepId(ever.getRouterStepId());
            dispatchQuery.setEoId(ever.getEoId());
            MtEoDispatchActionVO7 dispatchQty =
                            eoDispatchActionRepository.dispatchedEoAssignQtyGet(tenantId, dispatchQuery);
            if (null != dispatchQty) {
                one.setAssignQty(dispatchQty.getAssignQty());
            } else {
                one.setAssignQty(0D);
            }
            /// todo 这里临时注释掉
            // 计算未调度数
            // Optional<MtEoDispatchActionVO23> unDispatchQtyOp =
            // unAssignList.stream()
            // .filter(t -> t.getEoId().equals(ever.getEoId())
            // && t.getRouterStepId().equals(ever.getRouterStepId()))
            // .findFirst();
            // if (unDispatchQtyOp.isPresent()) {
            // one.setDispatchableQty(unDispatchQtyOp.get().getDispatchableQty());
            // } else {
            // one.setDispatchableQty(0D);
            // }
            // 计算调度数
            // Optional<MtEoDispatchProcessVO14> dispatchQtyOp =
            // assignList.stream()
            // .filter(t -> t.getEoId().equals(ever.getEoId())
            // && t.getRouterStepId().equals(ever.getRouterStepId()))
            // .findFirst();
            // if (dispatchQtyOp.isPresent()) {
            // one.setAssignQty(dispatchQtyOp.get().getAssignQty());
            // } else {
            // one.setAssignQty(0D);
            // }
            // 结果保存
            resultList.add(one);
        }

        Page<MtEoDispatchPlatformDTO2> result = new Page<MtEoDispatchPlatformDTO2>();
        result.setTotalPages(base.getTotalPages());
        result.setTotalElements(base.getTotalElements());
        result.setNumberOfElements(base.getNumberOfElements());
        result.setSize(base.getSize());
        result.setNumber(base.getNumber());
        result.setContent(resultList);
        return result;
    }

    @Override
    public List<MtEoDispatchPlatformDTO10> dispatchPlatformSubTableUi(Long tenantId, MtEoDispatchPlatformDTO9 dto) {
        // 传入必输性验证
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "eoId", "【API:dispatchPlatformSubTableUi】"));
        }
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "routerStepId", "【API:dispatchPlatformSubTableUi】"));
        }
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "operationId", "【API:dispatchPlatformSubTableUi】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "materialId", "【API:dispatchPlatformSubTableUi】"));
        }
        if (CollectionUtils.isEmpty(dto.getWorkcellIdList())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "workcellIdList", "【API:dispatchPlatformSubTableUi】"));
        }
        // 获取发布的信息
        MtEoDispatchActionVO21 action = new MtEoDispatchActionVO21();
        action.setEoIdList(Collections.singletonList(dto.getEoId()));
        action.setRouterStepIdList(Collections.singletonList(dto.getRouterStepId()));
        action.setOperationIdList(Collections.singletonList(dto.getOperationId()));
        action.setWorkcellIdList(dto.getWorkcellIdList());
        List<MtEoDispatchActionVO19> actions =
                        eoDispatchActionRepository.propertyLimitDispatchedActionPropertyBatchQuery(tenantId, action);
        // 获取未发布的信息
        MtEoDispatchProcessVO13 process = new MtEoDispatchProcessVO13();
        process.setEoIdList(Collections.singletonList(dto.getEoId()));
        process.setRouterStepIdList(Collections.singletonList(dto.getRouterStepId()));
        process.setOperationIdList(Collections.singletonList(dto.getOperationId()));
        process.setWorkcellIdList(dto.getWorkcellIdList());
        List<MtEoDispatchProcessVO10> processes =
                        eoDispatchProcessRepository.propertyLimitDispatchedProcessPropertyBatchQuery(tenantId, process);
        MtGenStatus publishDesc = genStatusRepository.getGenStatus(tenantId, "DISPATCH", "DISPATCH_STATUS", "PUBLISH");
        MtGenStatus unPublishDesc =
                        genStatusRepository.getGenStatus(tenantId, "DISPATCH", "DISPATCH_STATUS", "UNPUBLISH");
        // 拼接发布结果
        List<MtEoDispatchPlatformDTO10> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(actions)) {
            for (MtEoDispatchActionVO19 ever : actions) {
                MtEoDispatchPlatformDTO10 one = new MtEoDispatchPlatformDTO10();
                one.setEoDispatchId(ever.getEoDispatchActionId());
                one.setEoDispatchStatus("PUBLISH");
                if (null != publishDesc) {
                    one.setEoDispatchStatusDesc(publishDesc.getDescription());
                }
                one.setWorkcellId(ever.getWorkcellId());
                one.setWorkcellCode(ever.getWorkcellCode());
                one.setMaterialId(dto.getMaterialId());
                one.setMaterialCode(dto.getMaterialCode());
                one.setMaterialName(dto.getMaterialName());
                one.setShiftDate(ever.getShiftDate());
                one.setShiftCode(ever.getShiftCode());
                one.setDispatchQty(ever.getAssignQty());
                result.add(one);
            }
        }
        // 拼接未发布结果
        if (CollectionUtils.isNotEmpty(processes)) {
            for (MtEoDispatchProcessVO10 ever : processes) {
                MtEoDispatchPlatformDTO10 one = new MtEoDispatchPlatformDTO10();
                one.setEoDispatchId(ever.getEoDispatchProcessId());
                one.setEoDispatchStatus("UNPUBLISH");
                if (null != unPublishDesc) {
                    one.setEoDispatchStatusDesc(unPublishDesc.getDescription());
                }
                one.setWorkcellId(ever.getWorkcellId());
                one.setWorkcellCode(ever.getWorkcellCode());
                one.setMaterialId(dto.getMaterialId());
                one.setMaterialCode(dto.getMaterialCode());
                one.setMaterialName(dto.getMaterialName());
                one.setShiftDate(ever.getShiftDate());
                one.setShiftCode(ever.getShiftCode());
                one.setDispatchQty(ever.getAssignQty());
                result.add(one);
            }
        }
        return result;
    }

    @Override
    public List<MtEoDispatchPlatformDTO19> scheduledSubTableUi(Long tenantId, MtEoDispatchPlatformDTO18 dto) {
        // 传入必输性验证
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "workcellId", "【API:scheduledSubTableUi】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftCode", "【API:scheduledSubTableUi】"));
        }
        if (null == dto.getShiftDate()) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftDate", "【API:scheduledSubTableUi】"));
        }
        // 获取发布的信息
        MtEoDispatchActionVO18 action = new MtEoDispatchActionVO18();
        action.setWorkcellId(dto.getWorkcellId());
        action.setShiftDateFrom(dto.getShiftDate());
        action.setShiftDateTo(dto.getShiftDate());
        action.setShiftCode(dto.getShiftCode());
        List<MtEoDispatchActionVO19> actions =
                        eoDispatchActionRepository.propertyLimitDispatchedActionPropertyQuery(tenantId, action);
        // 获取未发布的信息
        MtEoDispatchProcessVO9 process = new MtEoDispatchProcessVO9();
        process.setWorkcellId(dto.getWorkcellId());
        process.setShiftDateFrom(dto.getShiftDate());
        process.setShiftDateTo(dto.getShiftDate());
        process.setShiftCode(dto.getShiftCode());
        List<MtEoDispatchProcessVO10> processes =
                        eoDispatchProcessRepository.propertyLimitDispatchedProcessPropertyQuery(tenantId, process);
        MtGenStatus publishDesc = genStatusRepository.getGenStatus(tenantId, "DISPATCH", "DISPATCH_STATUS", "PUBLISH");
        MtGenStatus unPublishDesc =
                        genStatusRepository.getGenStatus(tenantId, "DISPATCH", "DISPATCH_STATUS", "UNPUBLISH");
        // 拼接发布结果
        List<MtEoDispatchPlatformDTO19> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(actions)) {
            for (MtEoDispatchActionVO19 ever : actions) {
                MtEoDispatchPlatformDTO19 one = new MtEoDispatchPlatformDTO19();
                one.setEoDispatchId(ever.getEoDispatchActionId());
                one.setEoDispatchStatus("PUBLISH");
                if (null != publishDesc) {
                    one.setEoDispatchStatusDesc(publishDesc.getDescription());
                }
                one.setWorkcellId(ever.getWorkcellId());
                one.setWorkcellCode(ever.getWorkcellCode());
                one.setShiftDate(ever.getShiftDate());
                one.setShiftCode(ever.getShiftCode());
                one.setDispatchQty(ever.getAssignQty());
                one.setEoId(ever.getEoId());
                one.setEoNum(ever.getEoNum());
                one.setRouterStepId(ever.getRouterStepId());
                one.setStepName(ever.getStepName());
                one.setPriority(ever.getPriority());
                result.add(one);
            }
        }
        // 拼接未发布结果
        if (CollectionUtils.isNotEmpty(processes)) {
            for (MtEoDispatchProcessVO10 ever : processes) {
                MtEoDispatchPlatformDTO19 one = new MtEoDispatchPlatformDTO19();
                one.setEoDispatchId(ever.getEoDispatchProcessId());
                one.setEoDispatchStatus("UNPUBLISH");
                if (null != unPublishDesc) {
                    one.setEoDispatchStatusDesc(unPublishDesc.getDescription());
                }
                one.setWorkcellId(ever.getWorkcellId());
                one.setWorkcellCode(ever.getWorkcellCode());
                one.setShiftDate(ever.getShiftDate());
                one.setShiftCode(ever.getShiftCode());
                one.setDispatchQty(ever.getAssignQty());
                one.setEoId(ever.getEoId());
                one.setEoNum(ever.getEoNum());
                one.setRouterStepId(ever.getRouterStepId());
                one.setStepName(ever.getStepName());
                one.setPriority(ever.getPriority());
                result.add(one);
            }
        }
        if (CollectionUtils.isEmpty(result)) {
            return result;
        }
        // 最后准备一部分仅前台使用展示字段
        List<MtEo> eos = eoRepository.eoPropertyBatchGet(tenantId,
                        result.stream().map(MtEoDispatchPlatformDTO19::getEoId).collect(Collectors.toList()));
        List<MtMaterialVO> materials = materialRepository.materialPropertyBatchGet(tenantId,
                        eos.stream().map(MtEo::getMaterialId).collect(Collectors.toList()));
        // 拼接物料信息
        for (MtEoDispatchPlatformDTO19 ever : result) {
            Optional<MtEo> eoOp = eos.stream().filter(t -> ever.getEoId().equals(t.getEoId())).findFirst();
            if (eoOp.isPresent()) {
                Optional<MtMaterialVO> materialOp = materials.stream()
                                .filter(t -> eoOp.get().getMaterialId().equals(t.getMaterialId())).findFirst();
                if (materialOp.isPresent()) {
                    ever.setMaterialId(eoOp.get().getMaterialId());
                    ever.setMaterialCode(materialOp.get().getMaterialCode());
                    ever.setMaterialName(materialOp.get().getMaterialName());
                }
            }
        }
        return result.stream().sorted(Comparator.comparing(MtEoDispatchPlatformDTO19::getPriority))
                        .collect(Collectors.toList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scheduledSubTableReorderUi(Long tenantId, List<MtEoDispatchPlatformDTO17> dto) {
        if (CollectionUtils.isEmpty(dto)) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "LIST", "【API:subTableReorderUi】"));
        }
        List<MtEoDispatchActionVO15> param = new ArrayList<>();
        for (MtEoDispatchPlatformDTO17 ever : dto) {
            if (StringUtils.isEmpty(ever.getEoDispatchId())) {
                throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0001", "DISPATCH", "eoDispatchActionId", "【API:subTableReorderUi】"));
            }
            if (null == ever.getSequence()) {
                throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0001", "DISPATCH", "sequence", "【API:subTableReorderUi】"));
            }
            MtEoDispatchActionVO15 one = new MtEoDispatchActionVO15();
            one.setSequence(ever.getSequence());
            MtEoDispatchActionVO14 oneParam = new MtEoDispatchActionVO14();
            oneParam.setAdjustObjectId(ever.getEoDispatchId());
            if ("PUBLISH".equals(ever.getEoDispatchStatus())) {
                oneParam.setAdjustObjectType("ACTION");
            } else if ("UNPUBLISH".equals(ever.getEoDispatchStatus())) {
                oneParam.setAdjustObjectType("PROCESS");
            }
            one.setAdjustObject(oneParam);
            param.add(one);
        }
        eoDispatchActionRepository.sequenceLimitDispatchedEoPriorityAdjust(tenantId, param);
    }

    @Override
    public List<MtEoDispatchPlatformDTO7> dispatchPlatformChartDayUi(Long tenantId, MtEoDispatchPlatformDTO6 dto) {
        // 传入必输性验证
        if (null == dto.getShiftDate()) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftDate", "【API:dispatchPlatformChartDayUi】"));
        }
        if (StringUtils.isEmpty(dto.getDefaultSiteId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "defaultSiteId", "【API:dispatchPlatformChartDayUi】"));
        }
        if (StringUtils.isEmpty(dto.getProdLineId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "prodLineId", "【API:dispatchPlatformChartDayUi】"));
        }
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "operationId", "【API:dispatchPlatformChartDayUi】"));
        }
        List<MtEoDispatchPlatformDTO7> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(dto.getWorkcellIdList())) {
            return result;
        }

        // 这里记录WKC和日历的关系
        MtCalendarVO6 mtCalendarVO6 = new MtCalendarVO6();
        mtCalendarVO6.setCalendarType("STANDARD");
        mtCalendarVO6.setSiteType("MANUFACTURING");
        mtCalendarVO6.setOrganizationType("WORKCELL");
        mtCalendarVO6.setOrganizationIdList(dto.getWorkcellIdList());
        List<MtCalendarVO7> mtCalendarVO7s =
                        calendarRepository.organizationLimitOnlyCalendarBatchGet(tenantId, mtCalendarVO6);
        Map<String, String> wkcCalendarMap = mtCalendarVO7s.stream()
                        .collect(Collectors.toMap(MtCalendarVO7::getOrganizationId, MtCalendarVO7::getCalendarId));
        // 这里如果找不到日历
        if (wkcCalendarMap.isEmpty()) {
            return result;
        }
        // 准备进行批量查询
        List<MtCalendarShiftVO2> queryShiftList = new ArrayList<>();
        for (String ever : wkcCalendarMap.values()) {
            MtCalendarShiftVO2 one = new MtCalendarShiftVO2();
            one.setCalendarId(ever);
            one.setShiftDate(dto.getShiftDate());
            queryShiftList.add(one);
        }
        // 获取所有的班次日历
        List<String> calendarShiftIds = calendarShiftRepository.calendarLimitShiftBatchQuery(tenantId, queryShiftList);
        // 获取班次详细信息
        List<MtCalendarShift> calendarShifts =
                        calendarShiftRepository.calendarShiftBatchGet(tenantId, calendarShiftIds);
        // 这里如果找不到班次
        if (CollectionUtils.isEmpty(calendarShifts)) {
            return result;
        }
        // 批量获取WKC属性
        List<MtModWorkcellSchedule> schedules = modWorkcellScheduleRepository.workcellSchedulePropertyBatchGet(tenantId,
                        dto.getWorkcellIdList());
        List<String> shiftCodeList = calendarShifts.stream().map(MtCalendarShift::getShiftCode).distinct()
                        .collect(Collectors.toList());
        // 批量获取进度数据
        MtEoDispatchProcessVO13 process = new MtEoDispatchProcessVO13();
        process.setWorkcellIdList(dto.getWorkcellIdList());
        process.setShiftDateFrom(dto.getShiftDate());
        process.setShiftDateTo(dto.getShiftDate());
        process.setShiftCodeList(shiftCodeList);
        List<MtEoDispatchProcessVO10> processes =
                        eoDispatchProcessRepository.propertyLimitDispatchedProcessPropertyBatchQuery(tenantId, process);
        // 批量获取实绩数据
        MtEoDispatchActionVO21 action = new MtEoDispatchActionVO21();
        action.setWorkcellIdList(dto.getWorkcellIdList());
        action.setShiftDateFrom(dto.getShiftDate());
        action.setShiftDateTo(dto.getShiftDate());
        action.setShiftCodeList(shiftCodeList);
        List<MtEoDispatchActionVO19> actions =
                        eoDispatchActionRepository.propertyLimitDispatchedActionPropertyBatchQuery(tenantId, action);

        // 循环获取每个图表
        for (String workcellId : dto.getWorkcellIdList()) {
            // 根据WKC获取对应的日历，如果没有日历，这个wkc忽略就好
            if (!wkcCalendarMap.containsKey(workcellId)) {
                continue;
            }
            String calendarId = wkcCalendarMap.get(workcellId);
            // 获取日历对应班次
            List<MtCalendarShift> shifts = calendarShifts.stream().filter(t -> calendarId.equals(t.getCalendarId()))
                            .sorted(Comparator.comparing(MtCalendarShift::getShiftStartTime))
                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(shifts)) {
                for (MtCalendarShift shift : shifts) {
                    MtEoDispatchPlatformDTO7 one = new MtEoDispatchPlatformDTO7();
                    one.setShiftCode(shift.getShiftCode());
                    one.setShiftDate(shift.getShiftDate());
                    one.setWorkcellId(workcellId);
                    // 计算调度未发布数
                    if (CollectionUtils.isNotEmpty(processes)) {
                        // 根据wkc和shiftCode筛选，这里shiftDate只有一天，不需要筛选
                        double sumUnPublishQty = processes.stream()
                                        .filter(t -> shift.getShiftCode().equals(t.getShiftCode())
                                                        && workcellId.equals(t.getWorkcellId())
                                                        && null != t.getAssignQty())
                                        .mapToDouble(MtEoDispatchProcessVO10::getAssignQty).sum();
                        one.setUnPublishedQty(sumUnPublishQty);
                    }
                    if (null == one.getUnPublishedQty()) {
                        one.setUnPublishedQty(0D);
                    }
                    // 计算调度已发布数
                    if (CollectionUtils.isNotEmpty(actions)) {
                        // 根据wkc和shiftCode筛选，这里shiftDate只有一天，不需要筛选
                        double sumPublishQty = actions.stream()
                                        .filter(t -> shift.getShiftCode().equals(t.getShiftCode())
                                                        && workcellId.equals(t.getWorkcellId())
                                                        && null != t.getAssignQty())
                                        .mapToDouble(MtEoDispatchActionVO19::getAssignQty).sum();
                        one.setPublishedQty(sumPublishQty);
                    }
                    if (null == one.getPublishedQty()) {
                        one.setPublishedQty(0D);
                    }
                    // 计算WKC产能
                    if (CollectionUtils.isNotEmpty(schedules)) {
                        Optional<MtModWorkcellSchedule> scheduleOp = schedules.stream()
                                        .filter(t -> workcellId.equals(t.getWorkcellId())).findFirst();
                        if (scheduleOp.isPresent()) {
                            MtModWorkcellSchedule schedule = scheduleOp.get();
                            one.setCapacityQty(getCapacityQtyByWkc(schedule, shift));
                        }
                    }
                    if (null == one.getCapacityQty()) {
                        one.setCapacityQty(0D);
                    }
                    result.add(one);
                }
            }
        }
        return result;
    }

    private Double getCapacityQtyByWkc(MtModWorkcellSchedule schedule, MtCalendarShift calendarShift) {
        Double result;
        if (schedule == null || StringUtils.isEmpty(schedule.getRateType())) {
            // 这里如果为空，总量是调度已发布数和调度未发布数之和，指针指向其中间值
            result = null;
        } else if ("PERHOUR".equals(schedule.getRateType())) {
            // 获取班次时长,这里转换为小时
            double hours = (double) (calendarShift.getShiftEndTime().getTime()
                            - calendarShift.getShiftStartTime().getTime()) / (1000 * 60 * 60);
            // 四舍五入取两位小数
            double hoursBig = BigDecimal.valueOf(hours).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 产能=rate*activity*[（shiftEndTime-shiftStartTime）换算为小时]
            result = schedule.getRate() * schedule.getActivity() * hoursBig / 100;
        } else if ("SECOND".equals(schedule.getRateType())) {
            // 获取班次时长,这里转换为小时
            double seconds = (double) (calendarShift.getShiftEndTime().getTime()
                            - calendarShift.getShiftStartTime().getTime()) / (1000);
            // 四舍五入取两位小数
            double secondsBig = BigDecimal.valueOf(seconds).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 产能=rate*activity*[（shiftEndTime-shiftStartTime）换算为秒]
            result = schedule.getRate() * schedule.getActivity() * secondsBig / 100;
        } else {
            result = null;
        }
        return result;
    }

    @Override
    public MtEoDispatchPlatformDTO7 dispatchPlatformChartUi(Long tenantId, MtEoDispatchPlatformDTO5 dto) {
        // 传入必输性验证
        if (null == dto.getShiftDate()) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftDate", "【API:dispatchPlatformChartUi】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftCode", "【API:dispatchPlatformChartUi】"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "workcellId", "【API:dispatchPlatformChartUi】"));
        }
        MtEoDispatchPlatformDTO7 result = new MtEoDispatchPlatformDTO7();
        BeanUtils.copyProperties(dto, result);
        result.setUnPublishedQty(0D);
        result.setPublishedQty(0D);
        // WKC产能不重新计算，改为前端传回来,业务确认，这块WKC产能不重新计算
        if (null == result.getCapacityQty()) {
            result.setCapacityQty(0D);
        }
        // 重写获取调度未发布数
        MtEoDispatchProcessVO9 process = new MtEoDispatchProcessVO9();
        process.setWorkcellId(dto.getWorkcellId());
        process.setShiftDateFrom(dto.getShiftDate());
        process.setShiftDateTo(dto.getShiftDate());
        process.setShiftCode(dto.getShiftCode());
        List<MtEoDispatchProcessVO10> processes =
                        eoDispatchProcessRepository.propertyLimitDispatchedProcessPropertyQuery(tenantId, process);
        if (CollectionUtils.isNotEmpty(processes)) {
            double sumUnPublishQty = processes.stream().filter(t -> null != t.getAssignQty())
                            .mapToDouble(MtEoDispatchProcessVO10::getAssignQty).sum();
            result.setUnPublishedQty(sumUnPublishQty);
        }
        if (null == result.getUnPublishedQty()) {
            result.setUnPublishedQty(0D);
        }

        // 重写获取调度已发布
        MtEoDispatchActionVO18 action = new MtEoDispatchActionVO18();
        action.setWorkcellId(dto.getWorkcellId());
        action.setShiftDateFrom(dto.getShiftDate());
        action.setShiftDateTo(dto.getShiftDate());
        action.setShiftCode(dto.getShiftCode());
        List<MtEoDispatchActionVO19> actions =
                        eoDispatchActionRepository.propertyLimitDispatchedActionPropertyQuery(tenantId, action);
        if (CollectionUtils.isNotEmpty(actions)) {
            double sumPublishQty = actions.stream().filter(t -> null != t.getAssignQty())
                            .mapToDouble(MtEoDispatchActionVO19::getAssignQty).sum();
            result.setPublishedQty(sumPublishQty);
        }
        if (null == result.getPublishedQty()) {
            result.setPublishedQty(0D);
        }

        // 获取日历
        return result;
    }

    @Override
    public Page<MtModWorkcell> wkcLovUi(Long tenantId, MtEoDispatchPlatformDTO3 condition, PageRequest pageRequest) {
        // 传入必输性验证
        if (StringUtils.isEmpty(condition.getDefaultSiteId()) || StringUtils.isEmpty(condition.getProdLineId())
                        || StringUtils.isEmpty(condition.getOperationId())) {
            return new Page<>();
        }
        MtOpWkcDispatchRelVO3 rel = new MtOpWkcDispatchRelVO3();
        rel.setOperationId(condition.getOperationId());
        rel.setProductionLineId(condition.getProdLineId());
        rel.setSiteId(condition.getDefaultSiteId());
        // 4) 获取调度范围逻辑
        List<MtOpWkcDispatchRelVO2> rels =
                        operationWkcDispatchRelRepository.operationLimitAvailableWorkcellQuery(tenantId, rel);
        if (CollectionUtils.isEmpty(rels)) {
            return new Page<>();
        }
        return PageHelper.doPage(pageRequest, () -> modWorkcellRepository.workcellBasicPropertyBatchGet(tenantId,
                        rels.stream().map(MtOpWkcDispatchRelVO2::getWorkcellId).collect(Collectors.toList())));
    }

    @Override
    public List<String> shiftCodeComboBoxUi(Long tenantId, MtEoDispatchPlatformDTO12 dto) {
        if (null == dto.getShiftDate()) {
            return new ArrayList<>();
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            return new ArrayList<>();
        }
        // 获取日历信息
        MtCalendarVO2 calendarVO = new MtCalendarVO2();
        calendarVO.setCalendarType("STANDARD");
        calendarVO.setSiteType("MANUFACTURING");
        calendarVO.setOrganizationType("WORKCELL");
        calendarVO.setOrganizationId(dto.getWorkcellId());
        String calendarId = calendarRepository.organizationLimitOnlyCalendarGet(tenantId, calendarVO);
        // 获取日历班次信息ID
        MtCalendarShiftVO2 calendarShiftVO = new MtCalendarShiftVO2();
        calendarShiftVO.setCalendarId(calendarId);
        calendarShiftVO.setShiftDate(dto.getShiftDate());
        List<String> calendarShiftIds = calendarShiftRepository.calendarLimitShiftQuery(tenantId, calendarShiftVO);
        if (CollectionUtils.isEmpty(calendarShiftIds)) {
            throw new MtException("MT_DISPATCH_0032",
                            errorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0032", "DISPATCH"));
        }
        // 获取班次详细信息
        List<MtCalendarShift> calendarShifts =
                        calendarShiftRepository.calendarShiftBatchGet(tenantId, calendarShiftIds);
        return calendarShifts.stream().map(MtCalendarShift::getShiftCode).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtEoDispatchPlatformDTO14 dispatchConfirmUi(Long tenantId, MtEoDispatchPlatformDTO13 dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "eoId", "【API:dispatchConfirmUi】"));
        }
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "routerStepId", "【API:dispatchConfirmUi】"));
        }
        if (null == dto.getAssignQty()) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "assignQty", "【API:dispatchConfirmUi】"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "workcellId", "【API:dispatchConfirmUi】"));
        }
        if (null == dto.getShiftDate()) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftDate", "【API:dispatchConfirmUi】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftCode", "【API:dispatchConfirmUi】"));
        }
        if (dto.getAssignQty().equals(0.0D)) {
            throw new MtException("MT_DISPATCH_0034", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0034", "DISPATCH", "【API:dispatchConfirmUi】"));
        }
        // 获取EO详细信息
        MtEo eo = eoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (null == eo) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "eoId", "【API:dispatchConfirmUi】"));
        }
        // 调度验证
        MtEoDispatchActionVO8 check = new MtEoDispatchActionVO8();
        check.setEoId(dto.getEoId());
        check.setRouterStepId(dto.getRouterStepId());
        check.setAssignQty(dto.getAssignQty());
        eoDispatchActionRepository.eoDispatchVerify(tenantId, check);
        // 获取下一个优先级
        MtEoDispatchActionVO1 action = new MtEoDispatchActionVO1();
        action.setWorkcellId(dto.getWorkcellId());
        action.setShiftDate(dto.getShiftDate());
        action.setShiftCode(dto.getShiftCode());
        Long priority = eoDispatchActionRepository.dispatchedEoPriorityGenerate(tenantId, action);
        // 执行调度
        MtEoDispatchProcessVO2 process = new MtEoDispatchProcessVO2();
        process.setEoId(dto.getEoId());
        process.setRouterStepId(dto.getRouterStepId());
        process.setStatus(eo.getStatus());
        process.setAssignQty(dto.getAssignQty());
        process.setWorkcellId(dto.getWorkcellId());
        process.setShiftCode(dto.getShiftCode());
        process.setShiftDate(dto.getShiftDate());
        process.setPlanStartTime(eo.getPlanStartTime());
        process.setPlanEndTime(eo.getPlanEndTime());
        process.setPriority(priority);
        eoDispatchProcessRepository.dispatchedEoUpdate(tenantId, process, "Y");
        MtEoDispatchPlatformDTO14 result = new MtEoDispatchPlatformDTO14();
        result.setShiftCode(dto.getShiftCode());
        result.setShiftDate(dto.getShiftDate());
        result.setWorkcellId(dto.getWorkcellId());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dispatchReleaseUi(Long tenantId, MtEoDispatchPlatformDTO15 dto) {
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "operationId", "【API:dispatchReleaseUi】"));
        }
        if (null == dto.getShiftDateFrom()) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftDateFrom", "【API:dispatchReleaseUi】"));
        }
        if (null == dto.getShiftDateTo()) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftDateTo", "【API:dispatchReleaseUi】"));
        }
        if (CollectionUtils.isEmpty(dto.getWorkcellIdList())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "workcellIdList", "【API:dispatchReleaseUi】"));
        }
        // 这里记录WKC和日历的关系
        MtCalendarVO6 mtCalendarVO6 = new MtCalendarVO6();
        mtCalendarVO6.setCalendarType("STANDARD");
        mtCalendarVO6.setSiteType("MANUFACTURING");
        mtCalendarVO6.setOrganizationType("WORKCELL");
        mtCalendarVO6.setOrganizationIdList(dto.getWorkcellIdList());
        List<MtCalendarVO7> mtCalendarVO7s =
                        calendarRepository.organizationLimitOnlyCalendarBatchGet(tenantId, mtCalendarVO6);
        Map<String, String> wkcCalendarMap = mtCalendarVO7s.stream()
                        .collect(Collectors.toMap(MtCalendarVO7::getOrganizationId, MtCalendarVO7::getCalendarId));
        // 这里如果找不到日历
        if (wkcCalendarMap.isEmpty()) {
            return;
        }
        // 准备进行批量查询
        List<MtCalendarShiftVO2> queryShiftList = new ArrayList<>();
        for (String ever : wkcCalendarMap.values()) {
            MtCalendarShiftVO2 one = new MtCalendarShiftVO2();
            one.setCalendarId(ever);
            one.setShiftDate(dto.getShiftDateFrom());
            queryShiftList.add(one);
            MtCalendarShiftVO2 two = new MtCalendarShiftVO2();
            two.setCalendarId(ever);
            two.setShiftDate(dto.getShiftDateTo());
            queryShiftList.add(two);
        }
        // 获取所有的班次日历
        List<String> calendarShiftIds = calendarShiftRepository.calendarLimitShiftBatchQuery(tenantId, queryShiftList);
        // 获取班次详细信息
        List<MtCalendarShift> calendarShifts =
                        calendarShiftRepository.calendarShiftBatchGet(tenantId, calendarShiftIds);
        // 这里如果找不到班次
        if (CollectionUtils.isEmpty(calendarShifts)) {
            return;
        }
        // 记录wkc和班次的关系
        Map<String, List<String>> wkcShiftCodeMap = new HashMap<>();
        for (Map.Entry<String, String> ever : wkcCalendarMap.entrySet()) {
            wkcShiftCodeMap.put(ever.getKey(),
                            calendarShifts.stream().filter(t -> t.getCalendarId().equals(ever.getValue()))
                                            .map(MtCalendarShift::getShiftCode).distinct()
                                            .collect(Collectors.toList()));
        }
        List<String> shiftCodeList = calendarShifts.stream().map(MtCalendarShift::getShiftCode).distinct()
                        .collect(Collectors.toList());
        // 获取未发布数据
        MtEoDispatchProcessVO13 process = new MtEoDispatchProcessVO13();
        process.setWorkcellIdList(dto.getWorkcellIdList());
        process.setShiftDateFrom(dto.getShiftDateFrom());
        process.setShiftDateTo(dto.getShiftDateTo());
        process.setShiftCodeList(shiftCodeList);
        // 这里获取的有问题,后面需要筛选
        List<MtEoDispatchProcessVO10> processes =
                        eoDispatchProcessRepository.propertyLimitDispatchedProcessPropertyBatchQuery(tenantId, process);

        /// todo 这里需要批量发布校验
        for (MtEoDispatchProcessVO10 ever : processes) {
            if (wkcShiftCodeMap.containsKey(ever.getWorkcellId())
                            && wkcShiftCodeMap.get(ever.getWorkcellId()).contains(ever.getShiftCode())) {
                MtEoDispatchActionVO8 one = new MtEoDispatchActionVO8();
                one.setEoDispatchProcessId(ever.getEoDispatchProcessId());
                one.setAssignQty(ever.getAssignQty());
                eoDispatchActionRepository.eoDispatchVerify(tenantId, one);
            }
        }

        /// todo 这里需要批量发布
        for (MtEoDispatchProcessVO10 ever : processes) {
            // 执行发布
            MtEoDispatchActionVO20 action = new MtEoDispatchActionVO20();
            action.setOperationId(ever.getOperationId());
            action.setShiftDate(ever.getShiftDate());
            action.setShiftCode(ever.getShiftCode());
            action.setWorkcellId(ever.getWorkcellId());
            eoDispatchActionRepository.eoDispatchPublish(tenantId, action);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtEoDispatchPlatformDTO14 dispatchRevokeUi(Long tenantId, MtEoDispatchPlatformDTO16 dto) {
        if (StringUtils.isEmpty(dto.getEoDispatchId())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "eoDispatchId", "【API:dispatchReleaseUi】"));
        }
        if (StringUtils.isEmpty(dto.getEoDispatchStatus())) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "eoDispatchStatus", "【API:dispatchReleaseUi】"));
        }
        if (null == dto.getDispatchQty()) {
            throw new MtException("MT_DISPATCH_0001", errorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "publishQty", "【API:dispatchReleaseUi】"));
        }
        if ("PUBLISH".equals(dto.getEoDispatchStatus())) {
            // 撤销验证
            MtEoDispatchActionVO6 action = new MtEoDispatchActionVO6();
            action.setEoDispatchActionId(dto.getEoDispatchId());
            action.setCancelQty(dto.getDispatchQty());
            eoDispatchActionRepository.eoDispatchCancelVerify(tenantId, action);
            // 执行撤销逻辑
            MtEoDispatchActionVO11 cancel = new MtEoDispatchActionVO11();
            cancel.setEoDispatchActionId(dto.getEoDispatchId());
            cancel.setCancelQty(dto.getDispatchQty());
            cancel.setUndoDispatchedFlag("Y");
            eoDispatchActionRepository.eoDispatchCancel(tenantId, cancel);
        } else {
            // 执行撤销逻辑
            MtEoDispatchActionVO11 cancel = new MtEoDispatchActionVO11();
            cancel.setEoDispatchProcessId(dto.getEoDispatchId());
            cancel.setCancelQty(dto.getDispatchQty());
            cancel.setUndoDispatchedFlag("Y");
            eoDispatchActionRepository.eoDispatchCancel(tenantId, cancel);
        }

        // 返回数据通知前台刷新数据
        MtEoDispatchPlatformDTO14 result = new MtEoDispatchPlatformDTO14();
        result.setShiftCode(dto.getShiftCode());
        result.setShiftDate(dto.getShiftDate());
        result.setWorkcellId(dto.getWorkcellId());
        return result;
    }
}
