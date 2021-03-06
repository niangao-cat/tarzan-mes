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
 * ????????????????????? ???????????????
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
        // ????????????
        if (Objects.isNull(recordDTO)) {
            throw new MtException("HME_WO_INPUT_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0003", HME));
        }
        boolean enableFlag = StringUtils.equals(recordDTO.getEnableFlag(), YES);
        if (!reworkFlag && !enableFlag) {
            throw new MtException("HME_WO_INPUT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0004", HME, dto.getMaterialLotCode()));
        }

        // ?????????????????????????????????????????????
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


        // ???????????????
        if (StringUtils.equals(recordDTO.getMfFlag(), YES)) {
            throw new MtException("HME_WO_INPUT_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0008", HME));
        }
        // ???????????????????????????????????????????????????????????????????????????
        if (!StringUtils.equals(dto.getMaterialCode(), recordDTO.getMaterialCode())) {
            throw new MtException("HME_WO_INPUT_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0006", HME, recordDTO.getMaterialCode(), dto.getMaterialCode()));
        }
        if (StringUtils.isNotBlank(dto.getMaterialVersion()) &&
                !StringUtils.equals(dto.getMaterialVersion(), recordDTO.getMaterialVersion())) {
            throw new MtException("HME_WO_INPUT_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0007", HME, recordDTO.getMaterialVersion(), dto.getMaterialVersion()));
        }
        // ?????????????????????/??????????????????
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
        // ?????????????????????????????????
        if (!StringUtils.equals(recordDTO.getMaterialUomId(), recordDTO.getMaterialLotUomId())) {
            throw new MtException("Z_INSTRUCTION_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "Z_INSTRUCTION_0028", "INSTRUCTION"));
        }
        // ??????????????????
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
        // ??????????????????
        HmeWoInputRecordDTO6 woDto = hmeWoInputRecordMapper.workOrderNumGet(tenantId, dto.getWorkOrderId());
        // ???????????????
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
            // ????????????????????????
            List<HmeWoInputRecordDTO4> recordDTOList = hmeWoInputRecordMapper.woBomCompInfoQuery(tenantId, dto);
            // ??????????????????????????????????????????????????????????????????(???????????????lu.bai)
            if (recordDTOList.size() != 1) {
                throw new MtException("HME_WO_INPUT_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0009", HME));
            }
            recordDTO = recordDTOList.get(0);
            recordDTO.setProdLineCode(woDto.getProdLineCode());
            // ?????????
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
            // ????????????????????????
            List<HmeWoInputRecordDTO4> recordDTOList = hmeWoInputRecordMapper.woBomCompInfoQuery(tenantId, dto);
            // ??????????????????????????????????????????????????????????????????(???????????????lu.bai)
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
                // ????????????
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
            //???????????????
            BigDecimal sumQty = dtoList.stream().map(HmeWoInputRecordDTO2::getQty).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            mtWoComponentActualVO1.setAssembleExcessFlag(YES);
            mtWoComponentActualVO1.setTrxAssembleQty(sumQty.doubleValue());
        }
        // ??????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WORK_ORDER_INPUT");
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventRequestId(eventRequestId);
        eventCreate.setEventTypeCode("WORK_ORDER_INPUT");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
        recordDTO.setEventRequestId(eventRequestId);
        recordDTO.setEventId(eventId);

        // ?????????????????????????????????
        if (reworkFlag) {
            this.reworkInput(tenantId, recordDTO, dtoList);
        } else {
            // ??????????????????0
            if (recordDTO.getUnitDosage().compareTo(BigDecimal.ZERO) > 0) {
                self().mainPlanInput(tenantId, recordDTO, dtoList);
            } else {
                self().substitutePlanInput(tenantId, recordDTO, dtoList);
            }
        }

        // ????????????
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
        // ??????????????????????????????????????????
        BigDecimal assembleQty = mainMaterialAssembleQty(tenantId, recordDTO);
        // ???????????????
        BigDecimal demandQty = StringUtils.isNotBlank(recordDTO.getTotalDemandQty()) ? new BigDecimal(recordDTO.getTotalDemandQty()) : BigDecimal.ZERO;
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        recordDTO.setMoveType("261");
        recordDTO.setMoveReason("????????????");
        for (HmeWoInputRecordDTO2 dto : dtoList) {
            // ??????????????????
            inputProcess(tenantId, recordDTO.getEventRequestId(), recordDTO.getEventId(), dto);
            // ????????????????????????
            if (assembleQty.compareTo(demandQty) <= 0) {
                assembleQty = assembleQty.add(dto.getQty());
                if (assembleQty.compareTo(demandQty) <= 0) {
                    //?????????????????????
                    recordDTO.setTransactionTypeCode(HME_WO_ISSUE);
                } else {
                    // ????????????????????????????????????????????????????????????????????????????????????????????????
                    BigDecimal overstepQty = assembleQty.subtract(demandQty);
                    BigDecimal noOverstepQty = dto.getQty().subtract(overstepQty);
                    //?????????????????????
                    recordDTO.setTransactionTypeCode(HME_WO_ISSUE_EXT);
                    dto.setQty(overstepQty);
                    objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
                    //?????????????????????
                    recordDTO.setTransactionTypeCode(HME_WO_ISSUE);
                    dto.setQty(noOverstepQty);
                }
                objectTransactionRequestList.add(planInsideInputTrx(tenantId, recordDTO, dto));
            } else {
                //?????????????????????
                recordDTO.setTransactionTypeCode(HME_WO_ISSUE_EXT);
                objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
            }
        }
        //????????????
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void substitutePlanInput(Long tenantId, HmeWoInputRecordDTO4 recordDTO, List<HmeWoInputRecordDTO2> dtoList) {
        // ??????????????????
        HmeWoInputRecordDTO4 mainRecordDTO = mainMaterialInfoGet(tenantId, recordDTO);
        // ??????????????????
        BigDecimal mainDemandQty = mainRecordDTO.getMainDemandQty();
        // ???????????????????????????
        BigDecimal sumAssQty = mainRecordDTO.getSumAssembleQty();
        mainRecordDTO.setMoveReason("????????????");
        recordDTO.setMoveType("261");
        recordDTO.setMoveReason("????????????");
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        for (HmeWoInputRecordDTO2 dto : dtoList) {
            // ??????????????????
            inputProcess(tenantId, recordDTO.getEventRequestId(), recordDTO.getEventId(), dto);
            // ????????????????????????
            if (sumAssQty.compareTo(mainDemandQty) <= 0) {
                sumAssQty = sumAssQty.add(dto.getQty());
                if (sumAssQty.compareTo(mainDemandQty) <= 0) {
                    //?????????????????? ???????????????????????????????????????????????????????????????????????????????????????
                    //????????????????????????
                    mainRecordDTO.setMoveType("261");
                    mainRecordDTO.setTransactionTypeCode(HME_WO_ISSUE);
                    objectTransactionRequestList.add(mainPlanInsInputTrx(tenantId, mainRecordDTO, dto));
                    //????????????????????????
                    mainRecordDTO.setMoveType("262");
                    mainRecordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                    objectTransactionRequestList.add(mainPlanOutputTrx(tenantId, mainRecordDTO, dto));
                    //???????????????????????????
                    recordDTO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                    objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
                } else {
                    // ??????????????????????????????????????????????????????
                    BigDecimal overstepQty = sumAssQty.subtract(mainDemandQty);
                    // ???????????????
                    BigDecimal noOverstepQty = dto.getQty().subtract(overstepQty);
                    // ???????????????????????????
                    recordDTO.setTransactionTypeCode(HME_WO_ISSUE_EXT);
                    objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
                    // ?????????????????????????????????
                    dto.setQty(noOverstepQty);
                    mainRecordDTO.setMoveType("261");
                    mainRecordDTO.setTransactionTypeCode(HME_WO_ISSUE);
                    objectTransactionRequestList.add(mainPlanInsInputTrx(tenantId, mainRecordDTO, dto));
                    mainRecordDTO.setMoveType("262");
                    mainRecordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                    objectTransactionRequestList.add(mainPlanOutputTrx(tenantId, mainRecordDTO, dto));
                }
            } else {
                //?????????????????????
                recordDTO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
            }
        }
        //????????????
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
    }

    @Override
    public WmsObjectTransactionRequestVO planInsideInputTrx(Long tenantId, HmeWoInputRecordDTO4 recordDTO, HmeWoInputRecordDTO2 dto) {

        WmsObjectTransactionRequestVO requestVO;
        // ????????????
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
        // ????????????
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
        // ????????????
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
        // ????????????
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

        // ???????????????????????????????????????dtoList??????????????????
        List<HmeWoInputRecordDTO2> dtoList = dto.getDtoList();
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new MtException("HME_WO_INPUT_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0013", HME));
        }

        // ????????????
        HmeWoInputRecordDTO2 inputRecord = dtoList.get(0);
        HmeWoInputRecordDTO4 recordDTO;
        if (!reworkFlag) {
            // ????????????????????????
            List<HmeWoInputRecordDTO4> recordDTOList = hmeWoInputRecordMapper.woBomCompInfoQuery(tenantId, dto);
            // ??????????????????????????????????????????????????????????????????(???????????????lu.bai)
            if (recordDTOList.size() != 1) {
                throw new MtException("HME_WO_INPUT_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0009", HME));
            }
            recordDTO = recordDTOList.get(0);
            // ???????????????????????????????????????
            if (inputRecord.getQty().compareTo(dto.getOutQty()) < 0) {
                throw new MtException("HME_WO_INPUT_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0016", HME));
            }

            // ?????????????????????????????????OK
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

        // ??????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WORK_ORDER_INPUT_R");
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventRequestId(eventRequestId);
        eventCreate.setEventTypeCode("WORK_ORDER_INPUT_R");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
        // ????????????
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotId(inputRecord.getMaterialLotId());
        mtMaterialLotVO2.setEnableFlag(YES);
        mtMaterialLotVO2.setTrxPrimaryUomQty(dto.getOutQty().doubleValue());
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
        // ??????????????????
        if (StringUtils.isNotBlank(inputRecord.getInputRecordId())) {
            HmeWoInputRecord record = selectByPrimaryKey(inputRecord.getInputRecordId());
            record.setQty(inputRecord.getQty().subtract(dto.getOutQty()));
            OptionalHelper.optional(Collections.singletonList(HmeWoInputRecord.FIELD_QTY));
            hmeWoInputRecordMapper.updateOptional(record);
        } else {
            // ??????????????????????????????
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

        // ???????????????
        MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
        updateOnhandVO.setSiteId(inputRecord.getSiteId());
        updateOnhandVO.setMaterialId(inputRecord.getMaterialId());
        updateOnhandVO.setLocatorId(inputRecord.getLocatorId());
        updateOnhandVO.setLotCode(inputRecord.getLot());
        updateOnhandVO.setChangeQuantity(dto.getOutQty().doubleValue());
        updateOnhandVO.setEventId(eventId);
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnhandVO);
        // ??????????????????
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
        // ?????????????????????????????????
        inputRecord.setQty(dto.getOutQty());
        // ??????????????????????????????????????????
        if (reworkFlag) {
            this.reworkOutput(tenantId, recordDTO, inputRecord);
        } else {
            String operationId = "";
            // ??????????????????0????????????????????????????????????????????????
            if (recordDTO.getUnitDosage().compareTo(BigDecimal.ZERO) > 0) {
                self().mainPlanOutput(tenantId, recordDTO, inputRecord);
                operationId = this.queryMainMaterialOperationId(tenantId, recordDTO.getWorkOrderId(), dto.getBomComponentId());
            } else {
                self().substitutePlanOutput(tenantId, recordDTO, inputRecord);
                HmeWoInputRecordDTO7 mainMaterial = mainMaterialGet(tenantId, recordDTO);
                operationId = mainMaterial.getOperationId();
            }
            // ??????????????????
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
        // ???????????????
        BigDecimal assembleQty = recordDTO.getAssembleQty() == null ? BigDecimal.ZERO : recordDTO.getAssembleQty();
        // ???????????????
        BigDecimal demandQty = StringUtils.isNotBlank(recordDTO.getTotalDemandQty()) ? new BigDecimal(recordDTO.getTotalDemandQty()) : BigDecimal.ZERO;
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        recordDTO.setMoveType("262");
        recordDTO.setMoveReason("????????????");
        // ????????????????????????
        if (assembleQty.compareTo(demandQty) > 0) {
            // ?????????????????????????????????
            assembleQty = assembleQty.subtract(dto.getQty());
            if (assembleQty.compareTo(demandQty) > 0) {
                //?????????????????????
                recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
            } else {
                // ????????????????????????????????????????????????????????????????????????????????????????????????
                BigDecimal overstepQty = demandQty.subtract(assembleQty);
                BigDecimal noOverstepQty = dto.getQty().subtract(overstepQty);
                //?????????????????????
                dto.setQty(overstepQty);
                recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R);
                objectTransactionRequestList.add(planInsideInputTrx(tenantId, recordDTO, dto));
                //?????????????????????
                dto.setQty(noOverstepQty);
                recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
            }
        } else {
            //?????????????????????
            recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R);
            objectTransactionRequestList.add(planInsideInputTrx(tenantId, recordDTO, dto));

        }
        //????????????
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void substitutePlanOutput(Long tenantId, HmeWoInputRecordDTO4 recordDTO, HmeWoInputRecordDTO2 dto) {
        // ??????????????????
        HmeWoInputRecordDTO4 mainRecordDTO = mainMaterialInfoGet(tenantId, recordDTO);
        // ??????????????????
        BigDecimal mainDemandQty = mainRecordDTO.getMainDemandQty();
        // ???????????????????????????
        BigDecimal sumAssQty = mainRecordDTO.getSumAssembleQty();
        // ????????????????????????
        mainRecordDTO.setMoveType("262");
        mainRecordDTO.setMoveReason("????????????");
        recordDTO.setMoveType("262");
        recordDTO.setMoveReason("????????????");
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        if (sumAssQty.compareTo(mainDemandQty) >= 0) {
            sumAssQty = sumAssQty.subtract(dto.getQty());
            if (sumAssQty.compareTo(mainDemandQty) >= 0) {
                //???????????????????????????
                recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
            } else {
                // ???????????????
                BigDecimal noOverstepQty = mainDemandQty.subtract(sumAssQty);
                // ???????????????????????????
                recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
                // ?????????????????????????????????
                dto.setQty(noOverstepQty);
                mainRecordDTO.setTransactionTypeCode(HME_WO_ISSUE_R);
                objectTransactionRequestList.add(mainPlanInsInputTrx(tenantId, mainRecordDTO, dto));
                mainRecordDTO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                mainRecordDTO.setMoveType("261");
                objectTransactionRequestList.add(mainPlanOutputTrx(tenantId, mainRecordDTO, dto));
            }
        } else {
            //??????????????????
            //????????????????????????
            mainRecordDTO.setTransactionTypeCode(HME_WO_ISSUE_R);
            objectTransactionRequestList.add(mainPlanInsInputTrx(tenantId, mainRecordDTO, dto));
            //????????????????????????
            mainRecordDTO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
            mainRecordDTO.setMoveType("261");
            objectTransactionRequestList.add(mainPlanOutputTrx(tenantId, mainRecordDTO, dto));
            //???????????????????????????
            recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
            objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
        }
        //????????????
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
            recordDTO.setMoveReason("????????????");
            recordDTO.setTransactionTypeCode(HME_WO_ISSUE_EXT);
            inputProcess(tenantId, recordDTO.getEventRequestId(), recordDTO.getEventId(), dto);
            objectTransactionRequestList.add(planInsideInputTrx(tenantId, recordDTO, dto));
        });
        //????????????
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
    }

    private void reworkOutput(Long tenantId, HmeWoInputRecordDTO4 recordDTO, HmeWoInputRecordDTO2 dto) {
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        recordDTO.setMoveType("261");
        recordDTO.setMoveReason("????????????");
        recordDTO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
        objectTransactionRequestList.add(planOuterInputTrx(tenantId, recordDTO, dto));
        //????????????
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
    }

    private void inputProcess(Long tenantId, String eventRequestId, String eventId, HmeWoInputRecordDTO2 dto) {
        // ??????????????????
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
        // ????????????
        MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
        mtMaterialLotVO1.setMaterialLotId(dto.getMaterialLotId());
        mtMaterialLotVO1.setTrxPrimaryUomQty(dto.getQty().doubleValue());
        mtMaterialLotVO1.setEventRequestId(eventRequestId);
        mtMaterialLotVO1.setParentEventId(eventId);
        mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);
    }

    private BigDecimal mainMaterialAssembleQty(Long tenantId, HmeWoInputRecordDTO4 recordDTO) {
        List<HmeWoInputRecordDTO7> mainMaterialList = new ArrayList<>();
        // ??????????????????????????????????????????????????????
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
        //???????????????bom?????????????????????
        List<String> woSubstituteMaterial = hmeNcDisposePlatformMapper.getWoSubstituteByPrimary(tenantId, bomComponentId, recordDTO.getBomId());
        allMaterialList.addAll(woSubstituteMaterial);
        //????????????????????????????????????
        List<String> globalSubstituteMaterial = hmeNcDisposePlatformMapper.getGlobalSubstituteByPrimary(tenantId, recordDTO.getMaterialId());
        allMaterialList.addAll(globalSubstituteMaterial);
        allMaterialList = allMaterialList.stream().distinct().collect(Collectors.toList());

        List<HmeWoInputRecordDTO7> mainGlMaterialList = hmeWoInputRecordMapper.mainGlobalMaterialGet(tenantId, recordDTO.getSiteId(),
                recordDTO.getWorkOrderId(), allMaterialList);
        mainMaterialList.addAll(mainGlMaterialList);
        // ??????????????????????????????????????? ??????????????????
        if (CollectionUtils.isNotEmpty(mainGlMaterialList)) {
            List<String> mainGlMateriaIdlList = mainGlMaterialList.stream().map(HmeWoInputRecordDTO7::getMaterialId).collect(Collectors.toList());
            List<HmeWoInputRecordDTO7> mainAllWoMaterialList = hmeWoInputRecordMapper.mainWoMaterialGet(tenantId, recordDTO.getSiteId(),
                    recordDTO.getWorkOrderId(), mainGlMateriaIdlList);
            mainMaterialList.addAll(mainAllWoMaterialList);
        }
        // ??????????????????????????????????????????
        List<String> materialIds = new ArrayList<>(mainGlMaterialList.size());
        if (CollectionUtils.isNotEmpty(mainGlMaterialList)) {
            materialIds = mainGlMaterialList.stream().map(HmeWoInputRecordDTO7::getMaterialId).collect(Collectors.toList());
        }
        List<String> substMaterialIds = hmeWoInputRecordMapper.substituteMaterialGet(tenantId,
                recordDTO.getSiteId(), materialIds, recordDTO.getMaterialId());
        // ??????????????????????????????
        List<String> bomComponentIds = mainMaterialList.stream().map(HmeWoInputRecordDTO7::getBomComponentId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(bomComponentIds)) {
            bomComponentIds.add(bomComponentId);
        } else {
            bomComponentIds = Collections.singletonList(bomComponentId);
        }
        // ??????????????????????????????????????????????????????
        BigDecimal sumAssembleQty = hmeWoInputRecordMapper.assembleQtyGet(tenantId, recordDTO.getWorkOrderId(), bomComponentIds, substMaterialIds);
        BigDecimal sumAssQty = sumAssembleQty == null ? BigDecimal.ZERO : sumAssembleQty;
        return sumAssQty;
    }

    private HmeWoInputRecordDTO4 mainMaterialInfoGet(Long tenantId, HmeWoInputRecordDTO4 recordDTO) {
        List<HmeWoInputRecordDTO7> mainMaterialList = new ArrayList<>();
        // ???????????????????????????
        List<HmeWoInputRecordDTO7> mainWoMaterialList = hmeWoInputRecordMapper.mainWoMaterialGet(tenantId, recordDTO.getSiteId(),
                recordDTO.getWorkOrderId(), Collections.singletonList(recordDTO.getMaterialId()));
        mainMaterialList.addAll(mainWoMaterialList);
        // ???????????????????????????
        // 2021-03-01 add by sanfeng.zhang for bai.lu ??????????????????????????????????????????????????????????????????
        List<String> allMaterialIdList = new ArrayList<>();
        allMaterialIdList.add(recordDTO.getMaterialId());
        if (CollectionUtils.isNotEmpty(mainWoMaterialList)) {
            allMaterialIdList.addAll(mainWoMaterialList.stream().map(HmeWoInputRecordDTO7::getMaterialId).collect(Collectors.toList()));
        }
        List<HmeWoInputRecordDTO7> mainGlMaterialList = hmeWoInputRecordMapper.mainGlobalMaterialGet(tenantId, recordDTO.getSiteId(),
                recordDTO.getWorkOrderId(), allMaterialIdList);
        mainMaterialList.addAll(mainGlMaterialList);
        // ??????????????????????????????????????? ??????????????????
        if (CollectionUtils.isNotEmpty(mainGlMaterialList)) {
            List<String> mainGlMateriaIdlList = mainGlMaterialList.stream().map(HmeWoInputRecordDTO7::getMaterialId).collect(Collectors.toList());
            List<HmeWoInputRecordDTO7> mainAllWoMaterialList = hmeWoInputRecordMapper.mainWoMaterialGet(tenantId, recordDTO.getSiteId(),
                    recordDTO.getWorkOrderId(), mainGlMateriaIdlList);
            mainMaterialList.addAll(mainAllWoMaterialList);
        }
        // ??????
        List<HmeWoInputRecordDTO7> distinctList = new ArrayList<>();
        mainMaterialList.stream().forEach(p -> {
                    if (!distinctList.contains(p)) {
                        distinctList.add(p);
                    }
                }
        );
        // ??????
        List<HmeWoInputRecordDTO7> mainMaterials = distinctList.stream().filter(t ->
                t.getUnitDosage().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        // ?????? ??????????????????????????????
        if (mainMaterials.size() == 0) {
            throw new MtException("HME_WO_INPUT_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0014", HME));
        } else if (mainMaterials.size() > 1) {
            throw new MtException("HME_WO_INPUT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0015", HME));
        }
        // ????????????????????????
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
        // ??????????????????????????????
        BigDecimal mainDemandQty = BigDecimal.ZERO;
        MtExtendVO extendVO = new MtExtendVO();
        extendVO.setTableName("mt_bom_component_attr");
        extendVO.setKeyId(mainMaterials.get(0).getBomComponentId());
        extendVO.setAttrName("lineAttribute5");
        List<MtExtendAttrVO> attrs = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
        if (CollectionUtils.isNotEmpty(attrs) && StringUtils.isNotBlank(attrs.get(0).getAttrValue())) {
            mainDemandQty = new BigDecimal(attrs.get(0).getAttrValue());
        }
        // ??????????????????????????????????????????
        List<String> materialIds = new ArrayList<>(mainGlMaterialList.size());
        if (CollectionUtils.isNotEmpty(mainGlMaterialList)) {
            materialIds = mainGlMaterialList.stream().map(HmeWoInputRecordDTO7::getMaterialId).collect(Collectors.toList());
        }
        List<String> substMaterialIds = hmeWoInputRecordMapper.substituteMaterialGet(tenantId,
                recordDTO.getSiteId(), materialIds, recordDTO.getMaterialId());
        // ??????????????????????????????
        List<String> bomComponentIds = distinctList.stream().map(HmeWoInputRecordDTO7::getBomComponentId).collect(Collectors.toList());
        // ??????????????????????????????????????????????????????
        BigDecimal sumAssembleQty = hmeWoInputRecordMapper.assembleQtyGet(tenantId, recordDTO.getWorkOrderId(), bomComponentIds, substMaterialIds);
        BigDecimal sumAssQty = sumAssembleQty == null ? BigDecimal.ZERO : sumAssembleQty;
        // ??????????????????
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
        // ???????????????????????????
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
        // ???????????????????????????
        List<HmeWoInputRecordDTO7> mainWoMaterialList = hmeWoInputRecordMapper.mainWoMaterialGet(tenantId, recordDTO.getSiteId(),
                recordDTO.getWorkOrderId(), Collections.singletonList(recordDTO.getMaterialId()));
        Set<HmeWoInputRecordDTO7> mainMaterialSet = new HashSet<>(mainWoMaterialList);
        // ???????????????????????????
        List<HmeWoInputRecordDTO7> mainGlMaterialList = hmeWoInputRecordMapper.mainGlobalMaterialGet(tenantId, recordDTO.getSiteId(),
                recordDTO.getWorkOrderId(), Collections.singletonList(recordDTO.getMaterialId()));
        mainMaterialSet.addAll(mainGlMaterialList);
        // ??????
        List<HmeWoInputRecordDTO7> mainMaterials = mainMaterialSet.stream().filter(t ->
                t.getUnitDosage().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        // ?????? ??????????????????????????????
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
     * ???????????????????????????
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
