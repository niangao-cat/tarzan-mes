package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeWoInputRecord;
import com.ruike.hme.domain.repository.HmeWoInputRecordRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeNcDisposePlatformMapper;
import com.ruike.hme.infra.mapper.HmeWoInputRecordMapper;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.helper.OptionalHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.lov.feign.LovFeignClient;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.MtWoComponentActualVO1;
import tarzan.actual.domain.vo.MtWoComponentActualVO12;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotVO1;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtRouterOperationRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;
import static com.ruike.hme.infra.constant.HmeConstants.TransactionTypeCode.*;

/**
 * 工单投料记录表 资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-10-27 17:41:58
 */
@Component
public class HmeWoInputRecordRepositoryImpl extends BaseRepositoryImpl<HmeWoInputRecord> implements HmeWoInputRecordRepository {

    private final HmeWoInputRecordMapper hmeWoInputRecordMapper;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtUserClient userClient;
    private final MtEventRequestRepository mtEventRequestRepository;
    private final MtEventRepository mtEventRepository;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;
    private final WmsObjectTransactionRepository wmsObjectTransactionRepository;
    private final MtModLocatorRepository mtModLocatorRepository;
    private final MtExtendSettingsRepository mtExtendSettingsRepository;
    private final MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    private final ProfileClient profileClient;
    private final MtWorkOrderRepository workOrderRepository;
    private final LovFeignClient lovFeignClient;
    private final MtModProductionLineRepository productionLineRepository;
    private final MtRouterStepRepository mtRouterStepRepository;
    private final MtWorkOrderRepository mtWorkOrderRepository;
    private final MtRouterOperationRepository mtRouterOperationRepository;
    private final MtBomComponentRepository mtBomComponentRepository;
    private final HmeNcDisposePlatformMapper hmeNcDisposePlatformMapper;

    public HmeWoInputRecordRepositoryImpl(MtUserClient userClient, HmeWoInputRecordMapper hmeWoInputRecordMapper, MtErrorMessageRepository mtErrorMessageRepository, MtEventRequestRepository mtEventRequestRepository, ProfileClient profileClient, MtEventRepository mtEventRepository, MtMaterialLotRepository mtMaterialLotRepository, MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository, WmsObjectTransactionRepository wmsObjectTransactionRepository, MtModLocatorRepository mtModLocatorRepository, MtExtendSettingsRepository mtExtendSettingsRepository, MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository, MtWorkOrderRepository workOrderRepository, LovFeignClient lovFeignClient, MtModProductionLineRepository productionLineRepository, MtRouterStepRepository mtRouterStepRepository, MtWorkOrderRepository mtWorkOrderRepository, MtRouterOperationRepository mtRouterOperationRepository, MtBomComponentRepository mtBomComponentRepository, HmeNcDisposePlatformMapper hmeNcDisposePlatformMapper) {
        this.userClient = userClient;
        this.hmeWoInputRecordMapper = hmeWoInputRecordMapper;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.mtEventRequestRepository = mtEventRequestRepository;
        this.profileClient = profileClient;
        this.mtEventRepository = mtEventRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.mtWorkOrderComponentActualRepository = mtWorkOrderComponentActualRepository;
        this.wmsObjectTransactionRepository = wmsObjectTransactionRepository;
        this.mtModLocatorRepository = mtModLocatorRepository;
        this.mtExtendSettingsRepository = mtExtendSettingsRepository;
        this.mtInvOnhandQuantityRepository = mtInvOnhandQuantityRepository;
        this.workOrderRepository = workOrderRepository;
        this.lovFeignClient = lovFeignClient;
        this.productionLineRepository = productionLineRepository;
        this.mtRouterStepRepository = mtRouterStepRepository;
        this.mtWorkOrderRepository = mtWorkOrderRepository;
        this.mtRouterOperationRepository = mtRouterOperationRepository;
        this.mtBomComponentRepository = mtBomComponentRepository;
        this.hmeNcDisposePlatformMapper = hmeNcDisposePlatformMapper;
    }

    @Override
    public HmeWoInputRecordDTO workOrderGet(Long tenantId, String workOrderNum) {
        if (StringUtils.isBlank(workOrderNum)) {
            throw new MtException("HME_WO_INPUT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0001", HME, "workOrderNum"));
        }
        HmeWoInputRecordDTO dto = hmeWoInputRecordMapper.workOrderGet(tenantId, workOrderNum);
        if (Objects.isNull(dto)) {
            return new HmeWoInputRecordDTO();
        }
        dto.setReworkFlag(this.reworkFlagGet(tenantId, dto.getWorkOrderId()));
        List<HmeWoInputRecordDTO4> list = woBomCompInfoQuery(tenantId, dto.getWorkOrderId());
        dto.setDtoList(list);
        return dto;
    }

    @Override
    public List<HmeWoInputRecordDTO4> woBomCompInfoQuery(Long tenantId, String workOrderId) {
        if (StringUtils.isBlank(workOrderId)) {
            throw new MtException("HME_WO_INPUT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0001", HME, "workOrderId"));
        }
        HmeWoInputRecordDTO5 dto = new HmeWoInputRecordDTO5();
        dto.setWorkOrderId(workOrderId);
        return hmeWoInputRecordMapper.woBomCompInfoQuery(tenantId, dto);
    }

    @Override
    public List<HmeWoInputRecordDTO2> woInputRecordQuery(Long tenantId, HmeWoInputRecordDTO3 dto) {
        if (StringUtils.isBlank(dto.getWorkOrderId())) {
            throw new MtException("HME_WO_INPUT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0001", HME, "WorkOrderId"));
        }
        if (StringUtils.isBlank(dto.getMaterialId())) {
            throw new MtException("HME_WO_INPUT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0001", HME, "MaterialId"));
        }
        return hmeWoInputRecordMapper.woInputRecordQuery(tenantId, dto);
    }

    @Override
    public HmeWoInputRecordDTO2 materialLotGet(Long tenantId, HmeWoInputRecordDTO3 dto) {
        boolean reworkFlag = reworkFlagGet(tenantId, dto.getWorkOrderId());
        if (StringUtils.isBlank(dto.getMaterialLotCode())) {
            throw new MtException("HME_WO_INPUT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0001", HME, "materialLotCode"));
        }
        if (StringUtils.isBlank(dto.getMaterialCode())) {
            throw new MtException("HME_WO_INPUT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0001", HME, "materialCode"));
        }
        HmeWoInputRecordDTO2 recordDTO = hmeWoInputRecordMapper.materialLotGet(tenantId, dto.getMaterialLotCode());
        // 校验条码
        if (Objects.isNull(recordDTO)) {
            throw new MtException("HME_WO_INPUT_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0003", HME));
        }
        boolean enableFlag = StringUtils.equals(recordDTO.getEnableFlag(), YES);
        if (!reworkFlag && !enableFlag) {
            throw new MtException("HME_WO_INPUT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0004", HME, dto.getMaterialLotCode()));
        }

        // 当条码状态为有效时执行如下校验
        if (enableFlag) {
            if (!StringUtils.equals(recordDTO.getQualityStatus(), OK)) {
                throw new MtException("HME_WO_INPUT_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0005", HME, dto.getMaterialLotCode()));
            }
            if (StringUtils.equals(recordDTO.getStocktakeFlag(), YES)) {
                throw new MtException("HME_WO_JOB_SN_015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_015", HME, dto.getMaterialLotCode()));
            }
            if (StringUtils.equals(recordDTO.getFreezeFlag(), YES)) {
                throw new MtException("HME_WO_JOB_SN_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_014", HME, dto.getMaterialLotCode()));
            }
        }


        // 在制品校验
        if (StringUtils.equals(recordDTO.getMfFlag(), YES)) {
            throw new MtException("HME_WO_INPUT_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0008", HME));
        }
        // 校验条码物料、物料版本与投料物料、物料版本是否一致
        if (!StringUtils.equals(dto.getMaterialCode(), recordDTO.getMaterialCode())) {
            throw new MtException("HME_WO_INPUT_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0006", HME, recordDTO.getMaterialCode(), dto.getMaterialCode()));
        }
        if (StringUtils.isNotBlank(dto.getMaterialVersion()) &&
                !StringUtils.equals(dto.getMaterialVersion(), recordDTO.getMaterialVersion())) {
            throw new MtException("HME_WO_INPUT_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0007", HME, recordDTO.getMaterialVersion(), dto.getMaterialVersion()));
        }
        // 校验销售订单号/销售订单行号
        if (StringUtils.equals(dto.getSpecialInvFlag(), E)) {
            if (!StringUtils.equals(dto.getSoNum(), recordDTO.getSoNum())) {
                throw new MtException("HME_WO_INPUT_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0011", HME, dto.getMaterialLotCode()));
            }
            if (!StringUtils.equals(dto.getSoLineNum(), recordDTO.getSoLineNum())) {
                throw new MtException("HME_WO_INPUT_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0012", HME, dto.getMaterialLotCode()));
            }
        }
        // 校验条码单位与物料单位
        if (!StringUtils.equals(recordDTO.getMaterialUomId(), recordDTO.getMaterialLotUomId())) {
            throw new MtException("Z_INSTRUCTION_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "Z_INSTRUCTION_0028", "INSTRUCTION"));
        }
        // 获取当前用户
        Long userId = DetailsHelper.getUserDetails().getUserId();
        MtUserInfo mtUserInfo = userClient.userInfoGet(tenantId, Long.valueOf(userId));
        if (Objects.isNull(mtUserInfo)) {
            throw new MtException("HME_WO_INPUT_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0002", HME));
        }
        recordDTO.setFeeder(userId);
        recordDTO.setFeederName(mtUserInfo.getLoginName());
        recordDTO.setFeedDate(new Date());
        return recordDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeWoInputRecordDTO5 woInputRecordUpdate(Long tenantId, HmeWoInputRecordDTO5 dto) {
        boolean reworkFlag = reworkFlagGet(tenantId, dto.getWorkOrderId());
        if (StringUtils.isBlank(dto.getPlanFlag())) {
            throw new MtException("HME_WO_INPUT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0001", HME, "PlanFlag"));
        }
        List<HmeWoInputRecordDTO2> dtoList = dto.getDtoList();
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new MtException("HME_WO_INPUT_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0013", HME));
        }
        HmeWoInputRecordDTO4 recordDTO;
        MtWoComponentActualVO1 mtWoComponentActualVO1 = new MtWoComponentActualVO1();
        // 获取工单产线
        HmeWoInputRecordDTO6 woDto = hmeWoInputRecordMapper.workOrderNumGet(tenantId, dto.getWorkOrderId());
        // 计划内投料
        if (StringUtils.equals(dto.getPlanFlag(), HmeConstants.PlanFlag.INSIDE)) {
            if (StringUtils.isBlank(dto.getWorkOrderId())) {
                throw new MtException("HME_WO_INPUT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0001", HME, "WorkOrderId"));
            }
            if (StringUtils.isBlank(dto.getBomComponentId())) {
                throw new MtException("HME_WO_INPUT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0001", HME, "BomComponentId"));
            }
            if (StringUtils.isBlank(dto.getRouterStepId())) {
                throw new MtException("HME_WO_INPUT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0001", HME, "RouterStepId"));
            }
            // 查询当前装配清单
            List<HmeWoInputRecordDTO4> recordDTOList = hmeWoInputRecordMapper.woBomCompInfoQuery(tenantId, dto);
            // 此处通过以上条件查询组件信息，只能查询到一条(确认业务：lu.bai)
            if (recordDTOList.size() != 1) {
                throw new MtException("HME_WO_INPUT_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0009", HME));
            }
            recordDTO = recordDTOList.get(0);
            recordDTO.setProdLineCode(woDto.getProdLineCode());
            // 总数量
            BigDecimal sumQty = dtoList.stream().map(HmeWoInputRecordDTO2::getQty).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            mtWoComponentActualVO1.setBomComponentId(recordDTO.getBomComponentId());
            mtWoComponentActualVO1.setRouterStepId(recordDTO.getRouterStepId());
            mtWoComponentActualVO1.setAssembleExcessFlag(NO);
            mtWoComponentActualVO1.setTrxAssembleQty(sumQty.doubleValue());
        } else {
            if (StringUtils.isBlank(dto.getMaterialId())) {
                throw new MtException("HME_WO_INPUT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0001", HME, "MaterialId"));
            }
            if (StringUtils.isBlank(dto.getWorkOrderId())) {
                throw new MtException("HME_WO_INPUT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0001", HME, "WorkOrderId"));
            }
            // 查询当前装配清单
            List<HmeWoInputRecordDTO4> recordDTOList = hmeWoInputRecordMapper.woBomCompInfoQuery(tenantId, dto);
            // 此处通过以上条件查询组件信息，只能查询到一条(确认业务：lu.bai)
            if (recordDTOList.size() != 1) {
                recordDTO = new HmeWoInputRecordDTO4();
                recordDTO.setWorkOrderId(dto.getWorkOrderId());
                recordDTO.setWorkOrderNum(woDto.getWorkOrderNum());
                recordDTO.setMaterialId(dto.getMaterialId());
                recordDTO.setSiteId(woDto.getSiteId());
                recordDTO.setBomId(woDto.getBomId());
            } else {
                recordDTO = recordDTOList.get(0);
            }
            if (!reworkFlag) {
                // 获取主料
                HmeWoInputRecordDTO7 mainMaterial = mainMaterialGet(tenantId, recordDTO);
                recordDTO.setRouterStepId(mainMaterial.getRouterStepId());
                mtWoComponentActualVO1.setOperationId(mainMaterial.getOperationId());
            } else {
                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
                List<MtRouterStep> mtRouterStepList = mtRouterStepRepository.selectByCondition(Condition.builder(MtRouterStep.class).andWhere(Sqls.custom()
                        .andEqualTo(MtRouterStep.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtRouterStep.FIELD_ROUTER_ID, mtWorkOrder.getRouterId())).orderByAsc(MtRouterStep.FIELD_SEQUENCE).build());
                if (CollectionUtils.isNotEmpty(mtRouterStepList)) {
                    recordDTO.setRouterStepId(mtRouterStepList.get(0).getRouterStepId());
                    MtRouterOperation routerOperation = mtRouterOperationRepository.routerOperationGet(tenantId, mtRouterStepList.get(0).getRouterStepId());
                    mtWoComponentActualVO1.setOperationId(routerOperation != null ? routerOperation.getOperationId() : "");
                }
            }

            recordDTO.setProdLineCode(woDto.getProdLineCode());
            recordDTO.setUnitDosage(BigDecimal.ZERO);
            //计划外投料
            BigDecimal sumQty = dtoList.stream().map(HmeWoInputRecordDTO2::getQty).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            mtWoComponentActualVO1.setAssembleExcessFlag(YES);
            mtWoComponentActualVO1.setTrxAssembleQty(sumQty.doubleValue());
        }
        // 创建请求事件
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WORK_ORDER_INPUT");
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventRequestId(eventRequestId);
        eventCreate.setEventTypeCode("WORK_ORDER_INPUT");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
        recordDTO.setEventRequestId(eventRequestId);
        recordDTO.setEventId(eventId);

        // 记录事务，特殊工单逻辑
        if (reworkFlag) {
            this.reworkInput(tenantId, recordDTO, dtoList);
        } else {
            // 单位用量大于0
            if (recordDTO.getUnitDosage().compareTo(BigDecimal.ZERO) > 0) {
                self().mainPlanInput(tenantId, recordDTO, dtoList);
            } else {
                self().substitutePlanInput(tenantId, recordDTO, dtoList);
            }
        }

        // 工单装配
        mtWoComponentActualVO1.setParentEventId(eventId);
        mtWoComponentActualVO1.setEventRequestId(eventRequestId);
        mtWoComponentActualVO1.setWorkOrderId(recordDTO.getWorkOrderId());
        mtWoComponentActualVO1.setMaterialId(recordDTO.getMaterialId());
        mtWorkOrderComponentActualRepository.woComponentAssemble(tenantId, mtWoComponentActualVO1);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mainPlanInput(Long tenantId, HmeWoInputRecordDTO4 recordDTO, List<HmeWoInputRecordDTO2> dtoList) {
        // 主料也获取所有料的已装配数量
        BigDecimal assembleQty = mainMaterialAssembleQty(tenantId, recordDTO);
        // 净需求数量
        BigDecimal demandQty = StringUtils.isNotBlank(recordDTO.getTotalDemandQty()) ? new BigDecimal(recordDTO.getTotalDemandQty()) : BigDecimal.ZERO;
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        recordDTO.setMoveType("261");
        recordDTO.setMoveReason("工单投料");
        for (HmeWoInputRecordDTO2 dto : dtoList) {
            // 创建投料记录
            inputProcess(tenantId, recordDTO.getEventRequestId(), recordDTO.getEventId(), dto);
            // 判断投料事务类型
            if (assembleQty.compareTo(demandQty) <= 0) {
                assembleQty = assembleQty.add(dto.getQty());
                if (assembleQty.compareTo(demandQty) <= 0) {
                    //计划内投料事务
                    recordDTO.setTransactionTypeCode(HME_WO_ISSUE);
                } else {
                    // 如果部分超出净需求，则需要拆为两部分，一部分计划内，一部分计划外
                    BigDecimal overstepQty = assembleQty.subtract(demandQty);
                    BigDecimal noOverstepQty = dto.getQty().subtract(overstepQty);
                    //计划外投料事务
                    recordDTO.setTransactionTypeCode(HME_WO_ISSUE_EXT);
                    dto.setQty(overstepQty);
                    objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
                    //计划内投料事务
                    recordDTO.setTransactionTypeCode(HME_WO_ISSUE);
                    dto.setQty(noOverstepQty);
                }
                objectTransactionRequestList.add(planInsideInputTrx(tenantId, recordDTO, dto));
            } else {
                //计划外投料事务
                recordDTO.setTransactionTypeCode(HME_WO_ISSUE_EXT);
                objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
            }
        }
        //记录事务
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void substitutePlanInput(Long tenantId, HmeWoInputRecordDTO4 recordDTO, List<HmeWoInputRecordDTO2> dtoList) {
        // 获取主料信息
        HmeWoInputRecordDTO4 mainRecordDTO = mainMaterialInfoGet(tenantId, recordDTO);
        // 主料需求数量
        BigDecimal mainDemandQty = mainRecordDTO.getMainDemandQty();
        // 所有料的已装配数量
        BigDecimal sumAssQty = mainRecordDTO.getSumAssembleQty();
        mainRecordDTO.setMoveReason("工单投料");
        recordDTO.setMoveType("261");
        recordDTO.setMoveReason("工单投料");
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        for (HmeWoInputRecordDTO2 dto : dtoList) {
            // 创建投料记录
            inputProcess(tenantId, recordDTO.getEventRequestId(), recordDTO.getEventId(), dto);
            // 判断投料事务类型
            if (sumAssQty.compareTo(mainDemandQty) <= 0) {
                sumAssQty = sumAssQty.add(dto.getQty());
                if (sumAssQty.compareTo(mainDemandQty) <= 0) {
                    //记录三笔事务 主料的计划内投料，主料的计划外退料，以及替代料的计划外投料
                    //主料的计划内投料
                    mainRecordDTO.setMoveType("261");
                    mainRecordDTO.setTransactionTypeCode(HME_WO_ISSUE);
                    objectTransactionRequestList.add(mainPlanInsInputTrx(tenantId, mainRecordDTO, dto));
                    //主料的计划外退料
                    mainRecordDTO.setMoveType("262");
                    mainRecordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                    objectTransactionRequestList.add(mainPlanOutputTrx(tenantId, mainRecordDTO, dto));
                    //替代料的计划外投料
                    recordDTO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                    objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
                } else {
                    // 如果部分超出净需求，则需要拆为两部分
                    BigDecimal overstepQty = sumAssQty.subtract(mainDemandQty);
                    // 未超出数量
                    BigDecimal noOverstepQty = dto.getQty().subtract(overstepQty);
                    // 替代料的计划外投料
                    recordDTO.setTransactionTypeCode(HME_WO_ISSUE_EXT);
                    objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
                    // 未超部分做主料投，退料
                    dto.setQty(noOverstepQty);
                    mainRecordDTO.setMoveType("261");
                    mainRecordDTO.setTransactionTypeCode(HME_WO_ISSUE);
                    objectTransactionRequestList.add(mainPlanInsInputTrx(tenantId, mainRecordDTO, dto));
                    mainRecordDTO.setMoveType("262");
                    mainRecordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                    objectTransactionRequestList.add(mainPlanOutputTrx(tenantId, mainRecordDTO, dto));
                }
            } else {
                //计划外投料事务
                recordDTO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
            }
        }
        //记录事务
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
    }

    @Override
    public WmsObjectTransactionRequestVO planInsideInputTrx(Long tenantId, HmeWoInputRecordDTO4 recordDTO, HmeWoInputRecordDTO2 dto) {

        WmsObjectTransactionRequestVO requestVO;
        // 记录事务
        requestVO = new WmsObjectTransactionRequestVO();
        requestVO.setTransactionId(null);
        requestVO.setTransactionTypeCode(recordDTO.getTransactionTypeCode());
        requestVO.setEventId(recordDTO.getEventId());
        requestVO.setMaterialLotId(dto.getMaterialLotId());
        requestVO.setMaterialId(dto.getMaterialId());
        requestVO.setTransactionQty(dto.getQty());
        requestVO.setTransferLotNumber(null);
        requestVO.setLotNumber(dto.getLot());
        requestVO.setDeliveryBatch(null);
        requestVO.setTransactionUom(dto.getUomCode());
        requestVO.setTransactionTime(new Date());
        requestVO.setTransactionReasonCode(null);
        requestVO.setPlantId(dto.getSiteId());
        requestVO.setWorkOrderNum(recordDTO.getWorkOrderNum());
        requestVO.setProdLineCode(recordDTO.getProdLineCode());
        if (StringUtils.isNotBlank(dto.getLocatorId())) {
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(dto.getLocatorId());
            requestVO.setWarehouseId(mtModLocator.getParentLocatorId());
            requestVO.setLocatorId(dto.getLocatorId());
        }
        requestVO.setMoveType(recordDTO.getMoveType());
        requestVO.setMoveReason(recordDTO.getMoveReason());
        requestVO.setMergeFlag("N");
        requestVO.setBomReserveNum(recordDTO.getRelatedProjectNum());
        requestVO.setBomReserveLineNum(recordDTO.getLineNumber());
        if (StringUtils.equals(recordDTO.getSpecialInvFlag(), HmeConstants.ConstantValue.E)) {
            requestVO.setSoNum(recordDTO.getSoNum());
            requestVO.setSoLineNum(recordDTO.getSoLineNum());
        }
        return requestVO;
    }

    @Override
    public WmsObjectTransactionRequestVO planOuterInputTrx(Long tenantId, HmeWoInputRecordDTO4 recordDTO, HmeWoInputRecordDTO2 dto) {

        WmsObjectTransactionRequestVO requestVO;
        // 记录事务
        requestVO = new WmsObjectTransactionRequestVO();
        requestVO.setTransactionId(null);
        requestVO.setTransactionTypeCode(recordDTO.getTransactionTypeCode());
        requestVO.setEventId(recordDTO.getEventId());
        requestVO.setMaterialLotId(dto.getMaterialLotId());
        requestVO.setMaterialId(dto.getMaterialId());
        requestVO.setTransactionQty(dto.getQty());
        requestVO.setTransferLotNumber(null);
        requestVO.setLotNumber(dto.getLot());
        requestVO.setDeliveryBatch(null);
        requestVO.setTransactionUom(dto.getUomCode());
        requestVO.setTransactionTime(new Date());
        requestVO.setTransactionReasonCode(null);
        requestVO.setPlantId(dto.getSiteId());
        requestVO.setWorkOrderNum(recordDTO.getWorkOrderNum());
        requestVO.setProdLineCode(recordDTO.getProdLineCode());
        if (StringUtils.isNotBlank(dto.getLocatorId())) {
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(dto.getLocatorId());
            requestVO.setWarehouseId(mtModLocator.getParentLocatorId());
            requestVO.setLocatorId(dto.getLocatorId());
        }
        if (StringUtils.equals(recordDTO.getSpecialInvFlag(), HmeConstants.ConstantValue.E)) {
            requestVO.setSoNum(recordDTO.getSoNum());
            requestVO.setSoLineNum(recordDTO.getSoLineNum());
        }
        requestVO.setMoveType(recordDTO.getMoveType());
        requestVO.setMoveReason(recordDTO.getMoveReason());
        requestVO.setMergeFlag("N");
        return requestVO;
    }

    @Override
    public WmsObjectTransactionRequestVO mainPlanInsInputTrx(Long tenantId, HmeWoInputRecordDTO4 mainRecordDTO, HmeWoInputRecordDTO2 dto) {

        WmsObjectTransactionRequestVO requestVO;
        // 记录事务
        requestVO = new WmsObjectTransactionRequestVO();
        requestVO.setTransactionId(null);
        requestVO.setTransactionTypeCode(mainRecordDTO.getTransactionTypeCode());
        requestVO.setEventId(mainRecordDTO.getEventId());
        requestVO.setMaterialId(mainRecordDTO.getMaterialId());
        requestVO.setTransactionQty(dto.getQty());
        requestVO.setTransferLotNumber(null);
        requestVO.setLotNumber(dto.getLot());
        requestVO.setDeliveryBatch(null);
        requestVO.setTransactionUom(mainRecordDTO.getUomCode());
        requestVO.setTransactionTime(new Date());
        requestVO.setTransactionReasonCode(null);
        requestVO.setPlantId(mainRecordDTO.getSiteId());
        requestVO.setWorkOrderNum(mainRecordDTO.getWorkOrderNum());
        requestVO.setProdLineCode(mainRecordDTO.getProdLineCode());
        requestVO.setWarehouseId(mainRecordDTO.getMainWarehouseId());
        requestVO.setLocatorId(mainRecordDTO.getMainLocatorId());
        requestVO.setMoveType(mainRecordDTO.getMoveType());
        requestVO.setMoveReason(mainRecordDTO.getMoveReason());
        requestVO.setMergeFlag("N");
        requestVO.setBomReserveNum(mainRecordDTO.getRelatedProjectNum());
        requestVO.setBomReserveLineNum(mainRecordDTO.getLineNumber());
        if (StringUtils.equals(mainRecordDTO.getSpecialInvFlag(), HmeConstants.ConstantValue.E)) {
            requestVO.setSoNum(mainRecordDTO.getSoNum());
            requestVO.setSoLineNum(mainRecordDTO.getSoLineNum());
        }
        return requestVO;
    }

    @Override
    public WmsObjectTransactionRequestVO mainPlanOutputTrx(Long tenantId, HmeWoInputRecordDTO4 mainRecordDTO, HmeWoInputRecordDTO2 dto) {

        WmsObjectTransactionRequestVO requestVO;
        // 记录事务
        requestVO = new WmsObjectTransactionRequestVO();
        requestVO.setTransactionId(null);
        requestVO.setTransactionTypeCode(mainRecordDTO.getTransactionTypeCode());
        requestVO.setEventId(mainRecordDTO.getEventId());
        requestVO.setMaterialId(mainRecordDTO.getMaterialId());
        requestVO.setTransactionQty(dto.getQty());
        requestVO.setTransferLotNumber(null);
        requestVO.setLotNumber(dto.getLot());
        requestVO.setDeliveryBatch(null);
        requestVO.setTransactionUom(mainRecordDTO.getUomCode());
        requestVO.setTransactionTime(new Date());
        requestVO.setTransactionReasonCode(null);
        requestVO.setPlantId(mainRecordDTO.getSiteId());
        requestVO.setWorkOrderNum(mainRecordDTO.getWorkOrderNum());
        requestVO.setProdLineCode(mainRecordDTO.getProdLineCode());
        requestVO.setWarehouseId(mainRecordDTO.getMainWarehouseId());
        requestVO.setLocatorId(mainRecordDTO.getMainLocatorId());
        requestVO.setMoveType(mainRecordDTO.getMoveType());
        requestVO.setMoveReason(mainRecordDTO.getMoveReason());
        requestVO.setMergeFlag("N");
        requestVO.setBomReserveNum(mainRecordDTO.getRelatedProjectNum());
        requestVO.setBomReserveLineNum(mainRecordDTO.getLineNumber());
        if (StringUtils.equals(mainRecordDTO.getSpecialInvFlag(), HmeConstants.ConstantValue.E)) {
            requestVO.setSoNum(mainRecordDTO.getSoNum());
            requestVO.setSoLineNum(mainRecordDTO.getSoLineNum());
        }
        return requestVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeWoInputRecordDTO5 woOutputRecordUpdate(Long tenantId, HmeWoInputRecordDTO5 dto) {
        boolean reworkFlag = reworkFlagGet(tenantId, dto.getWorkOrderId());
        if (StringUtils.isBlank(dto.getPlanFlag())) {
            throw new MtException("HME_WO_INPUT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0001", HME, "PlanFlag"));
        }

        // 前台界面限制单条投料，所以dtoList只有一条数据
        List<HmeWoInputRecordDTO2> dtoList = dto.getDtoList();
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new MtException("HME_WO_INPUT_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0013", HME));
        }

        // 退料数据
        HmeWoInputRecordDTO2 inputRecord = dtoList.get(0);
        HmeWoInputRecordDTO4 recordDTO;
        if (!reworkFlag) {
            // 查询当前装配清单
            List<HmeWoInputRecordDTO4> recordDTOList = hmeWoInputRecordMapper.woBomCompInfoQuery(tenantId, dto);
            // 此处通过以上条件查询组件信息，只能查询到一条(确认业务：lu.bai)
            if (recordDTOList.size() != 1) {
                throw new MtException("HME_WO_INPUT_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0009", HME));
            }
            recordDTO = recordDTOList.get(0);
            // 退料数量不得大于已投料数量
            if (inputRecord.getQty().compareTo(dto.getOutQty()) < 0) {
                throw new MtException("HME_WO_INPUT_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0016", HME));
            }

            // 退料条码的质量状态需为OK
            if (!StringUtils.equals(inputRecord.getQualityStatus(), OK)) {
                throw new MtException("HME_WO_INPUT_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0005", HME, inputRecord.getMaterialLotCode()));
            }
        } else {
            MtWorkOrder workOrder = workOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
            MtModProductionLine prodLine = productionLineRepository.selectByPrimaryKey(workOrder.getProductionLineId());
            recordDTO = new HmeWoInputRecordDTO4();
            recordDTO.setWorkOrderId(dto.getWorkOrderId());
            recordDTO.setWorkOrderNum(workOrder.getWorkOrderNum());
            recordDTO.setProdLineCode(prodLine.getProdLineCode());
        }

        // 创建请求事件
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WORK_ORDER_INPUT_R");
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventRequestId(eventRequestId);
        eventCreate.setEventTypeCode("WORK_ORDER_INPUT_R");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
        // 条码更新
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotId(inputRecord.getMaterialLotId());
        mtMaterialLotVO2.setEnableFlag(YES);
        mtMaterialLotVO2.setTrxPrimaryUomQty(dto.getOutQty().doubleValue());
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
        // 更新投料记录
        if (StringUtils.isNotBlank(inputRecord.getInputRecordId())) {
            HmeWoInputRecord record = selectByPrimaryKey(inputRecord.getInputRecordId());
            record.setQty(inputRecord.getQty().subtract(dto.getOutQty()));
            OptionalHelper.optional(Collections.singletonList(HmeWoInputRecord.FIELD_QTY));
            hmeWoInputRecordMapper.updateOptional(record);
        } else {
            // 不存在投料记录则新增
            HmeWoInputRecord record = new HmeWoInputRecord();
            record.setTenantId(tenantId);
            record.setWorkOrderId(dto.getWorkOrderId());
            record.setRouterStepId(dto.getRouterStepId());
            record.setMaterialId(dto.getMaterialId());
            record.setMaterialVersion(inputRecord.getMaterialVersion());
            record.setMaterialLotId(inputRecord.getMaterialLotId());
            record.setLot(inputRecord.getLot());
            record.setQty(dto.getOutQty().negate());
            self().insertSelective(record);
        }

        // 更新现有量
        MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
        updateOnhandVO.setSiteId(inputRecord.getSiteId());
        updateOnhandVO.setMaterialId(inputRecord.getMaterialId());
        updateOnhandVO.setLocatorId(inputRecord.getLocatorId());
        updateOnhandVO.setLotCode(inputRecord.getLot());
        updateOnhandVO.setChangeQuantity(dto.getOutQty().doubleValue());
        updateOnhandVO.setEventId(eventId);
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnhandVO);
        // 工单装配取消
        MtWoComponentActualVO12 componentV12 = new MtWoComponentActualVO12();
        if (StringUtils.equals(dto.getPlanFlag(), HmeConstants.PlanFlag.INSIDE)) {
            componentV12.setAssembleExcessFlag(NO);
            componentV12.setBomComponentId(dto.getBomComponentId());
        } else {
            componentV12.setAssembleExcessFlag(YES);
            recordDTO.setUnitDosage(BigDecimal.ZERO);
        }
        recordDTO.setEventRequestId(eventRequestId);
        recordDTO.setEventId(eventId);
        // 设置投料记录的退料数量
        inputRecord.setQty(dto.getOutQty());
        // 返修工单走单独的事务记录逻辑
        if (reworkFlag) {
            this.reworkOutput(tenantId, recordDTO, inputRecord);
        } else {
            String operationId = "";
            // 单位用量大于0，判断走主料退料，或者替代料退料
            if (recordDTO.getUnitDosage().compareTo(BigDecimal.ZERO) > 0) {
                self().mainPlanOutput(tenantId, recordDTO, inputRecord);
                operationId = this.queryMainMaterialOperationId(tenantId, recordDTO.getWorkOrderId(), dto.getBomComponentId());
            } else {
                self().substitutePlanOutput(tenantId, recordDTO, inputRecord);
                HmeWoInputRecordDTO7 mainMaterial = mainMaterialGet(tenantId, recordDTO);
                operationId = mainMaterial.getOperationId();
            }
            // 进行组件取消
            componentV12.setOperationId(operationId);
            componentV12.setBomId(recordDTO.getBomId());
            componentV12.setEventRequestId(eventRequestId);
            componentV12.setParentEventId(eventId);
            componentV12.setLocatorId(inputRecord.getLocatorId());
            componentV12.setMaterialId(inputRecord.getMaterialId());
            componentV12.setRouterStepId(inputRecord.getRouterStepId());
            componentV12.setTrxAssembleQty(dto.getOutQty().doubleValue());
            componentV12.setWorkOrderId(dto.getWorkOrderId());
            mtWorkOrderComponentActualRepository.woComponentAssembleCancel(tenantId, componentV12);
        }
        return dto;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mainPlanOutput(Long tenantId, HmeWoInputRecordDTO4 recordDTO, HmeWoInputRecordDTO2 dto) {
        // 已装配数量
        BigDecimal assembleQty = recordDTO.getAssembleQty() == null ? BigDecimal.ZERO : recordDTO.getAssembleQty();
        // 总需求数量
        BigDecimal demandQty = StringUtils.isNotBlank(recordDTO.getTotalDemandQty()) ? new BigDecimal(recordDTO.getTotalDemandQty()) : BigDecimal.ZERO;
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        recordDTO.setMoveType("262");
        recordDTO.setMoveReason("工单退料");
        // 判断退料事务类型
        if (assembleQty.compareTo(demandQty) > 0) {
            // 本次退料后的已装配数量
            assembleQty = assembleQty.subtract(dto.getQty());
            if (assembleQty.compareTo(demandQty) > 0) {
                //计划外退料事务
                recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
            } else {
                // 如果部分超出净需求，则需要拆为两部分，一部分计划内，一部分计划外
                BigDecimal overstepQty = demandQty.subtract(assembleQty);
                BigDecimal noOverstepQty = dto.getQty().subtract(overstepQty);
                //计划内退料事务
                dto.setQty(overstepQty);
                recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R);
                objectTransactionRequestList.add(planInsideInputTrx(tenantId, recordDTO, dto));
                //计划外退料事务
                dto.setQty(noOverstepQty);
                recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
            }
        } else {
            //计划内退料事务
            recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R);
            objectTransactionRequestList.add(planInsideInputTrx(tenantId, recordDTO, dto));

        }
        //记录事务
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void substitutePlanOutput(Long tenantId, HmeWoInputRecordDTO4 recordDTO, HmeWoInputRecordDTO2 dto) {
        // 获取主料信息
        HmeWoInputRecordDTO4 mainRecordDTO = mainMaterialInfoGet(tenantId, recordDTO);
        // 主料需求数量
        BigDecimal mainDemandQty = mainRecordDTO.getMainDemandQty();
        // 所有料的已装配数量
        BigDecimal sumAssQty = mainRecordDTO.getSumAssembleQty();
        // 判断投料事务类型
        mainRecordDTO.setMoveType("262");
        mainRecordDTO.setMoveReason("工单退料");
        recordDTO.setMoveType("262");
        recordDTO.setMoveReason("工单退料");
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        if (sumAssQty.compareTo(mainDemandQty) >= 0) {
            sumAssQty = sumAssQty.subtract(dto.getQty());
            if (sumAssQty.compareTo(mainDemandQty) >= 0) {
                //替代料的计划外退料
                recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
            } else {
                // 未超出数量
                BigDecimal noOverstepQty = mainDemandQty.subtract(sumAssQty);
                // 替代料的计划外退料
                recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
                // 未超部分做主料投，退料
                dto.setQty(noOverstepQty);
                mainRecordDTO.setTransactionTypeCode(HME_WO_ISSUE_R);
                objectTransactionRequestList.add(mainPlanInsInputTrx(tenantId, mainRecordDTO, dto));
                mainRecordDTO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                mainRecordDTO.setMoveType("261");
                objectTransactionRequestList.add(mainPlanOutputTrx(tenantId, mainRecordDTO, dto));
            }
        } else {
            //记录三笔事务
            //主料的计划内退料
            mainRecordDTO.setTransactionTypeCode(HME_WO_ISSUE_R);
            objectTransactionRequestList.add(mainPlanInsInputTrx(tenantId, mainRecordDTO, dto));
            //主料的计划外投料
            mainRecordDTO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
            mainRecordDTO.setMoveType("261");
            objectTransactionRequestList.add(mainPlanOutputTrx(tenantId, mainRecordDTO, dto));
            //替代料的计划外退料
            recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
            objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
        }
        //记录事务
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
    }

    private boolean reworkFlagGet(Long tenantId, String workOrderId) {
        List<LovValueDTO> lovValueList = lovFeignClient.queryLovValue("HME.WO_PRODUCTION_TYPE", tenantId);
        Set<String> workOrderTypeSet = lovValueList.stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        MtWorkOrder workOrder = workOrderRepository.selectByPrimaryKey(workOrderId);
        return workOrderTypeSet.contains(workOrder.getWorkOrderType());
    }

    private void reworkInput(Long tenantId, HmeWoInputRecordDTO4 recordDTO, List<HmeWoInputRecordDTO2> dtoList) {
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        dtoList.forEach(dto -> {
            recordDTO.setMoveType("261");
            recordDTO.setMoveReason("工单投料");
            recordDTO.setTransactionTypeCode(HME_WO_ISSUE_EXT);
            inputProcess(tenantId, recordDTO.getEventRequestId(), recordDTO.getEventId(), dto);
            objectTransactionRequestList.add(planInsideInputTrx(tenantId, recordDTO, dto));
        });
        //记录事务
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
    }

    private void reworkOutput(Long tenantId, HmeWoInputRecordDTO4 recordDTO, HmeWoInputRecordDTO2 dto) {
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        recordDTO.setMoveType("261");
        recordDTO.setMoveReason("工单退料");
        recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
        objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
        //记录事务
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
    }

    private void inputProcess(Long tenantId, String eventRequestId, String eventId, HmeWoInputRecordDTO2 dto) {
        // 创建投料记录
        HmeWoInputRecord record = new HmeWoInputRecord();
        record.setWorkOrderId(dto.getWorkOrderId());
        record.setRouterStepId(dto.getRouterStepId());
        record.setMaterialId(dto.getMaterialId());
        record.setMaterialVersion(dto.getMaterialVersion());
        record.setMaterialLotId(dto.getMaterialLotId());
        record.setLot(dto.getLot());
        record.setQty(dto.getQty());
        record.setTenantId(tenantId);
        self().insertSelective(record);
        // 条码更新
        MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
        mtMaterialLotVO1.setMaterialLotId(dto.getMaterialLotId());
        mtMaterialLotVO1.setTrxPrimaryUomQty(dto.getQty().doubleValue());
        mtMaterialLotVO1.setEventRequestId(eventRequestId);
        mtMaterialLotVO1.setParentEventId(eventId);
        mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);
    }

    private BigDecimal mainMaterialAssembleQty(Long tenantId, HmeWoInputRecordDTO4 recordDTO) {
        List<HmeWoInputRecordDTO7> mainMaterialList = new ArrayList<>();
        // 根据主料查询工单替代料、全局替代料等
        List<String> allMaterialList = new ArrayList<>();
        allMaterialList.add(recordDTO.getMaterialId());
        String bomComponentId = null;
        if (StringUtils.isNotBlank(recordDTO.getBomComponentId())) {
            bomComponentId = recordDTO.getBomComponentId();
        } else {
            List<MtBomComponent> bomComponentList = mtBomComponentRepository.select(new MtBomComponent() {{
                setTenantId(tenantId);
                setMaterialId(recordDTO.getMaterialId());
                setBomId(recordDTO.getBomId());
            }});
            if (CollectionUtils.isEmpty(bomComponentList)) {
                throw new MtException("HME_NC_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0054", "HME"));
            } else if (bomComponentList.size() > 0) {
                bomComponentId = bomComponentList.get(0).getBomComponentId();
            }
        }
        //根据主键料bom查询工单替代料
        List<String> woSubstituteMaterial = hmeNcDisposePlatformMapper.getWoSubstituteByPrimary(tenantId, bomComponentId, recordDTO.getBomId());
        allMaterialList.addAll(woSubstituteMaterial);
        //根据主键料查询全局替代料
        List<String> globalSubstituteMaterial = hmeNcDisposePlatformMapper.getGlobalSubstituteByPrimary(tenantId, recordDTO.getMaterialId());
        allMaterialList.addAll(globalSubstituteMaterial);
        allMaterialList = allMaterialList.stream().distinct().collect(Collectors.toList());

        List<HmeWoInputRecordDTO7> mainGlMaterialList = hmeWoInputRecordMapper.mainGlobalMaterialGet(tenantId, recordDTO.getSiteId(),
                recordDTO.getWorkOrderId(), allMaterialList);
        mainMaterialList.addAll(mainGlMaterialList);
        // 再根据上面找到的全局替代料 找工单替代料
        if (CollectionUtils.isNotEmpty(mainGlMaterialList)) {
            List<String> mainGlMateriaIdlList = mainGlMaterialList.stream().map(HmeWoInputRecordDTO7::getMaterialId).collect(Collectors.toList());
            List<HmeWoInputRecordDTO7> mainAllWoMaterialList = hmeWoInputRecordMapper.mainWoMaterialGet(tenantId, recordDTO.getSiteId(),
                    recordDTO.getWorkOrderId(), mainGlMateriaIdlList);
            mainMaterialList.addAll(mainAllWoMaterialList);
        }
        // 获取不在装配清单内替代料信息
        List<String> materialIds = new ArrayList<>(mainGlMaterialList.size());
        if (CollectionUtils.isNotEmpty(mainGlMaterialList)) {
            materialIds = mainGlMaterialList.stream().map(HmeWoInputRecordDTO7::getMaterialId).collect(Collectors.toList());
        }
        List<String> substMaterialIds = hmeWoInputRecordMapper.substituteMaterialGet(tenantId,
                recordDTO.getSiteId(), materialIds, recordDTO.getMaterialId());
        // 获取在装配清单内信息
        List<String> bomComponentIds = mainMaterialList.stream().map(HmeWoInputRecordDTO7::getBomComponentId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(bomComponentIds)) {
            bomComponentIds.add(bomComponentId);
        } else {
            bomComponentIds = Collections.singletonList(bomComponentId);
        }
        // 获取所有存在替代关系的物料已装配数量
        BigDecimal sumAssembleQty = hmeWoInputRecordMapper.assembleQtyGet(tenantId, recordDTO.getWorkOrderId(), bomComponentIds, substMaterialIds);
        BigDecimal sumAssQty = sumAssembleQty == null ? BigDecimal.ZERO : sumAssembleQty;
        return sumAssQty;
    }

    private HmeWoInputRecordDTO4 mainMaterialInfoGet(Long tenantId, HmeWoInputRecordDTO4 recordDTO) {
        List<HmeWoInputRecordDTO7> mainMaterialList = new ArrayList<>();
        // 获取工单替代料信息
        List<HmeWoInputRecordDTO7> mainWoMaterialList = hmeWoInputRecordMapper.mainWoMaterialGet(tenantId, recordDTO.getSiteId(),
                recordDTO.getWorkOrderId(), Collections.singletonList(recordDTO.getMaterialId()));
        mainMaterialList.addAll(mainWoMaterialList);
        // 获取全局替代料信息
        // 2021-03-01 add by sanfeng.zhang for bai.lu 根据上面找到的工单替代料和替代料找全局替代料
        List<String> allMaterialIdList = new ArrayList<>();
        allMaterialIdList.add(recordDTO.getMaterialId());
        if (CollectionUtils.isNotEmpty(mainWoMaterialList)) {
            allMaterialIdList.addAll(mainWoMaterialList.stream().map(HmeWoInputRecordDTO7::getMaterialId).collect(Collectors.toList()));
        }
        List<HmeWoInputRecordDTO7> mainGlMaterialList = hmeWoInputRecordMapper.mainGlobalMaterialGet(tenantId, recordDTO.getSiteId(),
                recordDTO.getWorkOrderId(), allMaterialIdList);
        mainMaterialList.addAll(mainGlMaterialList);
        // 再根据上面找到的全局替代料 找工单替代料
        if (CollectionUtils.isNotEmpty(mainGlMaterialList)) {
            List<String> mainGlMateriaIdlList = mainGlMaterialList.stream().map(HmeWoInputRecordDTO7::getMaterialId).collect(Collectors.toList());
            List<HmeWoInputRecordDTO7> mainAllWoMaterialList = hmeWoInputRecordMapper.mainWoMaterialGet(tenantId, recordDTO.getSiteId(),
                    recordDTO.getWorkOrderId(), mainGlMateriaIdlList);
            mainMaterialList.addAll(mainAllWoMaterialList);
        }
        // 去重
        List<HmeWoInputRecordDTO7> distinctList = new ArrayList<>();
        mainMaterialList.stream().forEach(p -> {
                    if (!distinctList.contains(p)) {
                        distinctList.add(p);
                    }
                }
        );
        // 主料
        List<HmeWoInputRecordDTO7> mainMaterials = distinctList.stream().filter(t ->
                t.getUnitDosage().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        // 校验 主料是否有且仅有一个
        if (mainMaterials.size() == 0) {
            throw new MtException("HME_WO_INPUT_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0014", HME));
        } else if (mainMaterials.size() > 1) {
            throw new MtException("HME_WO_INPUT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0015", HME));
        }
        // 查询主料装配清单
        HmeWoInputRecordDTO5 mainDto = new HmeWoInputRecordDTO5();
        mainDto.setBomComponentId(mainMaterials.get(0).getBomComponentId());
        mainDto.setMaterialId(mainMaterials.get(0).getMaterialId());
        mainDto.setRouterStepId(mainMaterials.get(0).getRouterStepId());
        mainDto.setWorkOrderId(recordDTO.getWorkOrderId());
        mainDto.setPlanFlag(HmeConstants.PlanFlag.INSIDE);
        List<HmeWoInputRecordDTO4> recordDTOList = hmeWoInputRecordMapper.woBomCompInfoQuery(tenantId, mainDto);
        if (recordDTOList.size() != 1) {
            throw new MtException("HME_WO_INPUT_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0014", HME));
        }
        HmeWoInputRecordDTO4 mainRecordDTO = recordDTOList.get(0);
        // 获取主料的净需求数量
        BigDecimal mainDemandQty = BigDecimal.ZERO;
        MtExtendVO extendVO = new MtExtendVO();
        extendVO.setTableName("mt_bom_component_attr");
        extendVO.setKeyId(mainMaterials.get(0).getBomComponentId());
        extendVO.setAttrName("lineAttribute5");
        List<MtExtendAttrVO> attrs = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
        if (CollectionUtils.isNotEmpty(attrs) && StringUtils.isNotBlank(attrs.get(0).getAttrValue())) {
            mainDemandQty = new BigDecimal(attrs.get(0).getAttrValue());
        }
        // 获取不在装配清单内替代料信息
        List<String> materialIds = new ArrayList<>(mainGlMaterialList.size());
        if (CollectionUtils.isNotEmpty(mainGlMaterialList)) {
            materialIds = mainGlMaterialList.stream().map(HmeWoInputRecordDTO7::getMaterialId).collect(Collectors.toList());
        }
        List<String> substMaterialIds = hmeWoInputRecordMapper.substituteMaterialGet(tenantId,
                recordDTO.getSiteId(), materialIds, recordDTO.getMaterialId());
        // 获取在装配清单内信息
        List<String> bomComponentIds = distinctList.stream().map(HmeWoInputRecordDTO7::getBomComponentId).collect(Collectors.toList());
        // 获取所有存在替代关系的物料已装配数量
        BigDecimal sumAssembleQty = hmeWoInputRecordMapper.assembleQtyGet(tenantId, recordDTO.getWorkOrderId(), bomComponentIds, substMaterialIds);
        BigDecimal sumAssQty = sumAssembleQty == null ? BigDecimal.ZERO : sumAssembleQty;
        // 投料虚拟仓库
        String warehouseCode = profileClient.getProfileValueByOptions(tenantId,
                DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(),
                "ISSUE_WAREHOUSE_CODE");
        if (StringUtils.isBlank(warehouseCode)) {
            throw new MtException("HME_WO_INPUT_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0017", HME));
        }
        MtModLocator warehouse = mtModLocatorRepository.selectOne(new MtModLocator() {{
            setLocatorCode(warehouseCode);
            setTenantId(tenantId);
        }});
        if (Objects.isNull(warehouse)) {
            throw new MtException("HME_WO_INPUT_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0018", HME));
        }
        // 获取虚拟仓库下货位
        MtModLocator locator = mtModLocatorRepository.selectOne(new MtModLocator() {{
            setParentLocatorId(warehouse.getLocatorId());
            setTenantId(tenantId);
        }});
        if (Objects.isNull(locator)) {
            throw new MtException("HME_WO_INPUT_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0019", HME));
        }
        mainRecordDTO.setProdLineCode(recordDTO.getProdLineCode());
        mainRecordDTO.setEventRequestId(recordDTO.getEventRequestId());
        mainRecordDTO.setEventId(recordDTO.getEventId());
        mainRecordDTO.setMainWarehouseId(warehouse.getLocatorId());
        mainRecordDTO.setMainLocatorId(locator.getLocatorId());
        mainRecordDTO.setSumAssembleQty(sumAssQty);
        mainRecordDTO.setMainDemandQty(mainDemandQty);
        return mainRecordDTO;
    }

    private HmeWoInputRecordDTO7 mainMaterialGet(Long tenantId, HmeWoInputRecordDTO4 recordDTO) {
        // 获取工单替代料信息
        List<HmeWoInputRecordDTO7> mainWoMaterialList = hmeWoInputRecordMapper.mainWoMaterialGet(tenantId, recordDTO.getSiteId(),
                recordDTO.getWorkOrderId(), Collections.singletonList(recordDTO.getMaterialId()));
        Set<HmeWoInputRecordDTO7> mainMaterialSet = new HashSet<>(mainWoMaterialList);
        // 获取全局替代料信息
        List<HmeWoInputRecordDTO7> mainGlMaterialList = hmeWoInputRecordMapper.mainGlobalMaterialGet(tenantId, recordDTO.getSiteId(),
                recordDTO.getWorkOrderId(), Collections.singletonList(recordDTO.getMaterialId()));
        mainMaterialSet.addAll(mainGlMaterialList);
        // 主料
        List<HmeWoInputRecordDTO7> mainMaterials = mainMaterialSet.stream().filter(t ->
                t.getUnitDosage().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        // 校验 主料是否有且仅有一个
        if (mainMaterials.size() == 0) {
            throw new MtException("HME_WO_INPUT_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0014", HME));
        } else if (mainMaterials.size() > 1) {
            throw new MtException("HME_WO_INPUT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0015", HME));
        }
        return mainMaterials.get(0);
    }

    /**
     * 获取主料对应的工艺
     *
     * @param tenantId
     * @param workOrderId
     * @param bomComponentId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/4/11 23:17
     */
    private String queryMainMaterialOperationId(Long tenantId, String workOrderId, String bomComponentId) {
        List<String> operationIdList = hmeWoInputRecordMapper.queryMainMaterialOperationId(tenantId, workOrderId, bomComponentId);
        if (CollectionUtils.isEmpty(operationIdList) || operationIdList.size() > 1) {
            throw new MtException("HME_WO_INPUT_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0020", "HME"));
        }
        return operationIdList.get(0);
    }
}
