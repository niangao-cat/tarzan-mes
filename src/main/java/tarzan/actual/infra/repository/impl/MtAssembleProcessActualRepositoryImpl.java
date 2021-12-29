package tarzan.actual.infra.repository.impl;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.SequenceInfo;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.MtFieldsHelper;
import io.tarzan.common.domain.util.MtIdHelper;
import io.tarzan.common.domain.util.MtSqlHelper;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.actual.domain.entity.MtAssembleConfirmActual;
import tarzan.actual.domain.entity.MtAssembleConfirmActualHis;
import tarzan.actual.domain.entity.MtAssembleProcessActual;
import tarzan.actual.domain.repository.*;
import tarzan.actual.domain.trans.MtAssembleProcessActualTransMapper;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtAssemblePointActualMapper;
import tarzan.actual.infra.mapper.MtAssembleProcessActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtPfepInventory;
import tarzan.material.domain.repository.MtPfepInventoryRepository;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.material.domain.vo.MtPfepInventoryVO10;
import tarzan.material.domain.vo.MtPfepInventoryVO3;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.*;
import tarzan.modeling.domain.entity.MtModProdLineManufacturing;
import tarzan.modeling.domain.repository.MtModProdLineManufacturingRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoBomRepository;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtEoVO49;
import tarzan.order.domain.vo.MtWorkOrderVO27;
import tarzan.order.domain.vo.MtWorkOrderVO68;
import tarzan.order.domain.vo.MtWorkOrderVO69;

/**
 * 装配过程实绩，记录每一次执行作业的材料明细装配记录 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtAssembleProcessActualRepositoryImpl extends BaseRepositoryImpl<MtAssembleProcessActual>
                implements MtAssembleProcessActualRepository {

    private static final String TABLE_NAME = "mt_assemble_process_actual";
    private static final String ATTR_TABLE_NAME = "mt_assemble_pro_act_attr";

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtAssembleProcessActualMapper mtAssembleProcessActualMapper;

    @Autowired
    private MtAssembleConfirmActualRepository mtAssembleConfirmActualRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtAssemblePointRepository mtAssemblePointRepository;

    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;

    @Autowired
    private MtAssemblePointActualRepository mtAssemblePointActualRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;

    @Autowired
    private MtEoBomRepository mtEoBomRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtAssembleGroupActualRepository mtAssembleGroupActualRepository;

    @Autowired
    private MtAssembleGroupRepository mtAssembleGroupRepository;

    @Autowired
    private MtAssembleControlRepository mtAssembleControlRepository;

    @Autowired
    private MtAssemblePointControlRepository mtAssemblePointControlRepository;

    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtPfepInventoryRepository mtPfepInventoryRepository;

    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;

    @Autowired
    private MtModProdLineManufacturingRepository mtModProdLineManufacturingRepository;

    @Autowired
    private MtAssemblePointActualMapper mtAssemblePointActualMapper;

    @Autowired
    private MtAssembleProcessActualTransMapper mtAssembleProcessActualTransMapper;

    @Override
    public MtAssembleProcessActual assembleProcessActualPropertyGet(Long tenantId, String assembleProcessActualId) {
        if (StringUtils.isEmpty(assembleProcessActualId)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleProcessActualId", "【API:assembleProcessActualPropertyGet】"));
        }

        MtAssembleProcessActual mtAssembleProcessActual = new MtAssembleProcessActual();
        mtAssembleProcessActual.setTenantId(tenantId);
        mtAssembleProcessActual.setAssembleProcessActualId(assembleProcessActualId);
        return this.mtAssembleProcessActualMapper.selectOne(mtAssembleProcessActual);
    }

    @Override
    public List<MtAssembleProcessActual> assembleProcessActualPropertyBatchGet(Long tenantId,
                    List<String> assembleProcessActualIds) {
        if (CollectionUtils.isEmpty(assembleProcessActualIds)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleProcessActualId", "【API:assembleProcessActualPropertyBatchGet】"));
        }
        return this.mtAssembleProcessActualMapper.selectByIdsCustom(tenantId, assembleProcessActualIds);
    }

    @Override
    public List<String> propertyLimitAssembleProcessActualQuery(Long tenantId, MtAssembleProcessActualVO dto) {
        List<MtAssembleProcessActual> list =
                        this.mtAssembleProcessActualMapper.selectAssembleProcessActual(tenantId, dto);
        return list.stream().map(MtAssembleProcessActual::getAssembleProcessActualId).collect(Collectors.toList());
    }

    @Override
    public List<MtAssembleProcessActualVO3> componentLimitAssembleActualQuery(Long tenantId,
                    MtAssembleProcessActualVO2 dto) {
        List<MtAssembleProcessActualVO3> list =
                        this.mtAssembleProcessActualMapper.selectComponentAssembleProcessActual(tenantId, dto);
        return list.stream().sorted(Comparator.comparing(MtAssembleProcessActualVO3::getComponentType)
                        .thenComparing(Comparator.comparingDouble((MtAssembleProcessActualVO3 c) -> Double.valueOf(
                                        StringUtils.isEmpty(c.getBomComponentId()) ? "0" : c.getBomComponentId())))
                        .thenComparing(Comparator.comparingDouble((MtAssembleProcessActualVO3 c) -> Double
                                        .valueOf(StringUtils.isEmpty(c.getMaterialId()) ? "0" : c.getMaterialId())))
                        .thenComparing(Comparator.comparingDouble((MtAssembleProcessActualVO3 c) -> Double.valueOf(
                                        StringUtils.isEmpty(c.getRouterStepId()) ? "0" : c.getRouterStepId()))))
                        .collect(Collectors.toList());
    }

    @Override
    public List<MtAssembleProcessActual> eventLimitComponentAssembleActualQuery(Long tenantId,
                    MtAssembleProcessActualVO4 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eventId", "【API:eventLimitComponentAssembleActualQuery】"));
        }

        MtAssembleProcessActualVO vo = new MtAssembleProcessActualVO();
        BeanUtils.copyProperties(dto, vo);

        return this.mtAssembleProcessActualMapper.selectAssembleProcessActual(tenantId, vo);
    }

    /**
     * assembleProcessActualCreate-新增装配过程实绩
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/25
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtAssembleProcessActualVO6 assembleProcessActualCreate(Long tenantId, MtAssembleProcessActualVO1 dto,
                    String fullUpdate) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getAssembleConfirmActualId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleConfirmActualId", "【API:assembleProcessActualCreate】"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eventId", "【API:assembleProcessActualCreate】"));
        }
        if (dto.getAssembleQty() == null && dto.getScrappedQty() == null) {
            throw new MtException("MT_ASSEMBLE_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002", "ASSEMBLE",
                                            "assembleQty、scrappedQty", "【API:assembleProcessActualCreate】"));
        }

        MtAssembleProcessActualVO6 mtAssembleProcessActualVO6 = new MtAssembleProcessActualVO6();
        mtAssembleProcessActualVO6.setAssembleConfirmActualId(dto.getAssembleConfirmActualId());

        // 获取装配确认实绩属性
        MtAssembleConfirmActual mtAssembleConfirmActual = mtAssembleConfirmActualRepository
                        .assembleConfirmActualPropertyGet(tenantId, dto.getAssembleConfirmActualId());
        if (mtAssembleConfirmActual == null
                        || StringUtils.isEmpty(mtAssembleConfirmActual.getAssembleConfirmActualId())) {
            throw new MtException("MT_ASSEMBLE_0036",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0036", "ASSEMBLE",
                                            "assembleQty、scrappedQty", "【API:assembleProcessActualCreate】"));
        }

        // 获取routerId
        String routerId = dto.getRouterId();
        if (StringUtils.isEmpty(dto.getRouterId())) {
            String routerStepId = "";
            if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
                routerStepId = dto.getRouterStepId();
            } else {
                routerStepId = mtAssembleConfirmActual.getRouterStepId();
            }

            if (StringUtils.isNotEmpty(routerStepId)) {
                MtRouterStep mtRouterStep = mtRouterStepRepository.routerStepGet(tenantId, routerStepId);
                if (mtRouterStep != null) {
                    routerId = mtRouterStep.getRouterId();
                }
            }
        }

        // 获取装配组
        String assembleGroupId = dto.getAssembleGroupId();
        if (StringUtils.isEmpty(dto.getAssembleGroupId()) && StringUtils.isNotEmpty(dto.getAssemblePointId())) {
            MtAssemblePoint mtAssemblePoint =
                            mtAssemblePointRepository.assemblePointPropertyGet(tenantId, dto.getAssemblePointId());
            if (mtAssemblePoint != null) {
                assembleGroupId = mtAssemblePoint.getAssembleGroupId();
            }
        }

        // 获取装配时消耗库位
        String locatorId = dto.getLocatorId();
        if (StringUtils.isEmpty(dto.getLocatorId())) {
            MtEoComponentActualVO mtEoComponentVO = new MtEoComponentActualVO();
            mtEoComponentVO.setComponentType(mtAssembleConfirmActual.getComponentType());
            mtEoComponentVO.setEoId(mtAssembleConfirmActual.getEoId());
            mtEoComponentVO.setMaterialId(mtAssembleConfirmActual.getMaterialId());
            mtEoComponentVO.setOperationId(mtAssembleConfirmActual.getOperationId());
            locatorId = mtEoComponentActualRepository.eoComponentAssembleLocatorGet(tenantId, mtEoComponentVO);
        }

        // 获取装配方式
        String assembleMethod = dto.getAssembleMethod();
        if (StringUtils.isEmpty(dto.getAssembleMethod())) {
            if (StringUtils.isNotEmpty(dto.getAssemblePointId()) || StringUtils.isNotEmpty(dto.getAssembleGroupId())) {
                MtAssemblePointActualVO1 mtAssemblePointActualVO1 = new MtAssemblePointActualVO1();
                mtAssemblePointActualVO1.setAssembleGroupId(dto.getAssembleGroupId());
                mtAssemblePointActualVO1.setAssemblePointId(dto.getAssemblePointId());
                mtAssemblePointActualVO1.setMaterialId(mtAssembleConfirmActual.getMaterialId());
                List<String> tempResultList = mtAssemblePointActualRepository
                                .propertyLimitAssemblePointActualQuery(tenantId, mtAssemblePointActualVO1);
                if (CollectionUtils.isEmpty(tempResultList) || tempResultList.size() == 1) {
                    assembleMethod = "S_FEEDING";
                } else if (tempResultList.size() > 1) {
                    assembleMethod = "M_FEEDING";
                }
            }
        }

        // 获取物料批最新历史ID
        String materialLotHisId = "";
        if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {
            MtMaterialLot mtMaterialLot =
                            mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotId());
            if (mtMaterialLot != null) {
                materialLotHisId = mtMaterialLot.getLatestHisId();
            }
        }

        // 2. 创建装配过程实绩
        MtAssembleProcessActual processActual = new MtAssembleProcessActual();
        processActual.setTenantId(tenantId);
        processActual.setAssembleConfirmActualId(dto.getAssembleConfirmActualId());
        processActual.setAssembleQty(dto.getAssembleQty() == null ? Double.valueOf(0.0D) : dto.getAssembleQty());
        processActual.setScrapQty(dto.getScrappedQty() == null ? Double.valueOf(0.0D) : dto.getScrappedQty());
        processActual.setRouterId(routerId);
        processActual.setSubstepId(dto.getSubstepId());
        processActual.setWorkcellId(dto.getWorkcellId());
        processActual.setAssembleGroupId(assembleGroupId);
        processActual.setAssemblePointId(dto.getAssemblePointId());
        processActual.setReferenceArea(dto.getReferenceArea());
        processActual.setReferencePoint(dto.getReferencePoint());
        processActual.setLocatorId(locatorId);
        processActual.setAssembleMethod(assembleMethod);
        processActual.setMaterialLotId(dto.getMaterialLotId());
        processActual.setOperateBy(
                        dto.getOperateBy() == null ? DetailsHelper.getUserDetails().getUserId() : dto.getOperateBy());
        processActual.setEventId(dto.getEventId());
        processActual.setEventTime(new Date());
        processActual.setEventBy(DetailsHelper.getUserDetails().getUserId());
        processActual.setMaterialLotHisId(materialLotHisId);
        self().insertSelective(processActual);

        mtAssembleProcessActualVO6.setAssembleProcessActualId(processActual.getAssembleProcessActualId());
        return mtAssembleProcessActualVO6;
    }

    /**
     * eoComponentActualAssembleProcess-执行作业组件装配实绩处理
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/25
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtAssembleProcessActualVO6 eoComponentActualAssembleProcess(Long tenantId, MtAssembleProcessActualVO5 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoComponentActualAssembleProcess】"));
        }
        if (dto.getTrxAssembleQty() == null) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "trxAssembleQty", "【API:eoComponentActualAssembleProcess】"));
        }
        if (new BigDecimal(dto.getTrxAssembleQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ASSEMBLE_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0007", "ASSEMBLE",
                                            "trxAssembleQty", "【API:eoComponentActualAssembleProcess】"));
        }

        // 2. 判断执行作业是否需要按工序装配
        String isOperationFlag = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());

        String eventTypeCode = null;
        String assembleExcessFlag = null;
        String bomComponentId = null;
        String operationId = null;
        String routerStepId = null;

        if ((StringUtils.isEmpty(isOperationFlag) || "N".equals(isOperationFlag))
                        && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 2.1.
            // 若获取结果为空或为N，且输入参数assembleExcessFlag未输入或输入N，表示非强制装配模式下不按照工序进行装配
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ASSEMBLE_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0044", "ASSEMBLE", "【API:eoComponentActualAssembleProcess】"));
            }

            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null || !"ASSEMBLING".equals(bomComponent.getBomComponentType())) {
                throw new MtException("MT_ASSEMBLE_0045",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0045",
                                                "ASSEMBLE", "ASSEMBLING", "【API:eoComponentActualAssembleProcess】"));
            }

            eventTypeCode = "EO_PROCESS_ACTUAL_ASSEMBLE";
            assembleExcessFlag = "N";
            bomComponentId = dto.getBomComponentId();
            operationId = "";
            routerStepId = "";
        } else if ((StringUtils.isEmpty(isOperationFlag) || "N".equals(isOperationFlag))
                        && "Y".equals(dto.getAssembleExcessFlag())) {
            // 2.2. 若获取结果为空或为N，且输入参数assembleExcessFlag为Y，表示强制装配模式下不按照工序进行装配
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0046", "ASSEMBLE", "【API:eoComponentActualAssembleProcess】"));
            }

            eventTypeCode = "EO_PROCESS_ACTUAL_EXCESS_ASSEMBLE";
            assembleExcessFlag = "Y";
            bomComponentId = "";
            operationId = "";
            routerStepId = "";
        } else if ("Y".equals(isOperationFlag) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 2.3. 若获取结果为Y，且输入参数assembleExcessFlag未输入或输入N，表示非强制装配模式下按照工序进行装配
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ASSEMBLE_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0044", "ASSEMBLE", "【API:eoComponentActualAssembleProcess】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ASSEMBLE_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0047", "ASSEMBLE", "【API:eoComponentActualAssembleProcess】"));
            }

            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null || !"ASSEMBLING".equals(bomComponent.getBomComponentType())) {
                throw new MtException("MT_ASSEMBLE_0045",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0045",
                                                "ASSEMBLE", "ASSEMBLING", "【API:eoComponentActualAssembleProcess】"));
            }

            eventTypeCode = "EO_PROCESS_ACTUAL_OPERATION_ASSEMBLE";
            assembleExcessFlag = "N";
            bomComponentId = dto.getBomComponentId();
            operationId = null;
            routerStepId = dto.getRouterStepId();
        } else if ("Y".equals(isOperationFlag) && "Y".equals(dto.getAssembleExcessFlag())) {
            // 2.4. 若获取结果为Y，且输入参数assembleExcessFlag输入为Y，表示强制装配模式下按照工序进行装配
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0046", "ASSEMBLE", "【API:eoComponentActualAssembleProcess】"));
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("MT_ASSEMBLE_0048", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0048", "ASSEMBLE", "【API:eoComponentActualAssembleProcess】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ASSEMBLE_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0047", "ASSEMBLE", "【API:eoComponentActualAssembleProcess】"));
            }
            eventTypeCode = "EO_PROCESS_ACTUAL_EXCESS_OPERATION_ASSEMBLE";
            assembleExcessFlag = "Y";
            bomComponentId = "";
            operationId = dto.getOperationId();
            routerStepId = dto.getRouterStepId();
        }

        // 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(eventTypeCode);
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 更新执行作业装配确认实绩并记录历史
        MtAssembleConfirmActualHis mtAssembleConfirmActualHis = new MtAssembleConfirmActualHis();
        mtAssembleConfirmActualHis.setEoId(dto.getEoId());
        mtAssembleConfirmActualHis.setMaterialId(dto.getMaterialId());
        mtAssembleConfirmActualHis.setBomComponentId(bomComponentId);
        mtAssembleConfirmActualHis.setOperationId(operationId);
        mtAssembleConfirmActualHis.setRouterStepId(routerStepId);
        mtAssembleConfirmActualHis.setComponentType("ASSEMBLING");
        mtAssembleConfirmActualHis.setAssembleExcessFlag(assembleExcessFlag);
        mtAssembleConfirmActualHis.setEventId(eventId);
        mtAssembleConfirmActualHis.setAssembleRouterType(dto.getAssembleRouterType());
        MtAssembleConfirmActualVO14 mtAssembleConfirmActualVO14 = mtAssembleConfirmActualRepository
                        .assembleConfirmActualUpdate(tenantId, mtAssembleConfirmActualHis);
        String assembleConfirmActualId = null;
        if (null != mtAssembleConfirmActualVO14) {
            assembleConfirmActualId = mtAssembleConfirmActualVO14.getAssembleConfirmActualId();
        }

        MtAssembleProcessActualVO1 assembleProcessVO1 = new MtAssembleProcessActualVO1();
        assembleProcessVO1.setAssembleConfirmActualId(assembleConfirmActualId);
        assembleProcessVO1.setAssembleQty(dto.getTrxAssembleQty());
        assembleProcessVO1.setRouterId(dto.getRouterId());
        assembleProcessVO1.setSubstepId(dto.getSubstepId());
        assembleProcessVO1.setWorkcellId(dto.getWorkcellId());
        assembleProcessVO1.setAssemblePointId(dto.getAssemblePointId());
        assembleProcessVO1.setReferenceArea(dto.getReferenceArea());
        assembleProcessVO1.setReferencePoint(dto.getReferencePoint());
        assembleProcessVO1.setLocatorId(dto.getLocatorId());
        assembleProcessVO1.setAssembleMethod(dto.getAssembleMethod());
        assembleProcessVO1.setOperateBy(dto.getOperationBy());
        assembleProcessVO1.setEventId(eventId);
        assembleProcessVO1.setRouterStepId(dto.getRouterStepId());
        assembleProcessVO1.setMaterialLotId(dto.getMaterialLotId());

        return assembleProcessActualCreate(tenantId, assembleProcessVO1, "N");
    }

    /**
     * eoComponentActualAssembleCancelProcess-执行作业组件装配撤销处理
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/25
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoComponentActualAssembleCancelProcess(Long tenantId, MtAssembleProcessActualVO5 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoComponentActualAssembleCancelProcess】"));
        }
        if (dto.getTrxAssembleQty() == null) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "trxAssembleQty", "【API:eoComponentActualAssembleCancelProcess】"));
        }
        if (new BigDecimal(dto.getTrxAssembleQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ASSEMBLE_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0007", "ASSEMBLE",
                                            "trxAssembleQty", "【API:eoComponentActualAssembleCancelProcess】"));
        }

        // 2. 获取装配清单ID
        String bomId = dto.getBomId();
        if (StringUtils.isEmpty(dto.getBomId())) {
            bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
        }

        // 3. 判断执行作业是否需要按工序装配
        String isOperationFlag = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());
        String eventTypeCode = null;
        String assembleExcessFlag = null;
        String bomComponentId = null;
        String operationId = null;
        String routerStepId = null;
        String materialId = null;
        if ((StringUtils.isEmpty(isOperationFlag) || "N".equals(isOperationFlag))
                        && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 2.1.
            // 若获取结果为空或为N，且输入参数assembleExcessFlag未输入或输入N，表示针对非强制装配模式下不按照工序的装配结果进行装配取消
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ASSEMBLE_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0053", "ASSEMBLE", "【API:eoComponentActualAssembleCancelProcess】"));
            }

            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null || !"ASSEMBLING".equals(bomComponent.getBomComponentType())) {
                throw new MtException("MT_ASSEMBLE_0045",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0045",
                                                "ASSEMBLE", "ASSEMBLING",
                                                "【API:eoComponentActualAssembleCancelProcess】"));
            }
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                materialId = dto.getMaterialId();
            } else {
                if (StringUtils.isNotEmpty(bomComponent.getMaterialId())) {
                    materialId = bomComponent.getMaterialId();
                } else {
                    throw new MtException("MT_ASSEMBLE_0071",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0071",
                                                    "ASSEMBLE", bomComponent.getBomComponentId(),
                                                    "【API:eoComponentActualAssembleCancelProcess】"));

                }
            }
            eventTypeCode = "EO_PROCESS_ACTUAL_ASSEMBLE_CANCEL";
            assembleExcessFlag = "N";
            bomComponentId = dto.getBomComponentId();
            operationId = "";
            routerStepId = "";
        } else if ((StringUtils.isEmpty(isOperationFlag) || "N".equals(isOperationFlag))
                        && "Y".equals(dto.getAssembleExcessFlag())) {
            // 2.2.
            // 若获取结果为空或为N，且输入参数assembleExcessFlag为Y，表示针对强制装配模式下不按照工序进行装配结果进行装配取消
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0055", "ASSEMBLE", "【API:eoComponentActualAssembleCancelProcess】"));
            }

            eventTypeCode = "EO_PROCESS_ACTUAL_EXCESS_ASSEMBLE_CANCEL";
            assembleExcessFlag = "Y";
            bomComponentId = "";
            operationId = "";
            routerStepId = "";
            materialId = dto.getMaterialId();
        } else if ("Y".equals(isOperationFlag) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 2.3.
            // 若获取结果为Y，且输入参数assembleExcessFlag未输入或输入N，表示针对非强制装配模式下按照工序进行装配的装配结果进行取消
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ASSEMBLE_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0053", "ASSEMBLE", "【API:eoComponentActualAssembleCancelProcess】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ASSEMBLE_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0056", "ASSEMBLE", "【API:eoComponentActualAssembleCancelProcess】"));
            }

            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null || !"ASSEMBLING".equals(bomComponent.getBomComponentType())) {
                throw new MtException("MT_ASSEMBLE_0045",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0045",
                                                "ASSEMBLE", "ASSEMBLING",
                                                "【API:eoComponentActualAssembleCancelProcess】"));
            }
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                materialId = dto.getMaterialId();
            } else {
                if (StringUtils.isNotEmpty(bomComponent.getMaterialId())) {
                    materialId = bomComponent.getMaterialId();
                } else {
                    throw new MtException("MT_ASSEMBLE_0071",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0071",
                                                    "ASSEMBLE", bomComponent.getBomComponentId(),
                                                    "【API:eoComponentActualAssembleCancelProcess】"));

                }
            }
            if (StringUtils.isNotEmpty(dto.getOperationId())) {
                operationId = dto.getOperationId();
            } else {
                MtRouterOperation mtRouterOperation = null;
                try {
                    mtRouterOperation = mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
                } catch (MtException e) {
                    e.printStackTrace();
                }

                if (mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                    throw new MtException("MT_ASSEMBLE_0072",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0072",
                                                    "ASSEMBLE", "【API:eoComponentActualAssembleCancelProcess】"));
                }
                operationId = mtRouterOperation.getOperationId();
            }

            eventTypeCode = "EO_PROCESS_ACTUAL_OPERATION_ASSEMBLE_CANCEL";
            assembleExcessFlag = "N";
            bomComponentId = dto.getBomComponentId();
            routerStepId = dto.getRouterStepId();
        } else if ("Y".equals(isOperationFlag) && "Y".equals(dto.getAssembleExcessFlag())) {
            // 2.4.
            // 若获取结果为Y，且输入参数assembleExcessFlag输入为Y，表示针对强制装配模式下按照工序进行装配的装配实绩进行取消
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0055", "ASSEMBLE", "【API:eoComponentActualAssembleCancelProcess】"));
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("MT_ASSEMBLE_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0057", "ASSEMBLE", "【API:eoComponentActualAssembleCancelProcess】"));
            }

            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ASSEMBLE_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0056", "ASSEMBLE", "【API:eoComponentActualAssembleCancelProcess】"));
            }
            eventTypeCode = "EO_PROCESS_ACTUAL_EXCESS_OPERATION_ASSEMBLE_CANCEL";
            assembleExcessFlag = "Y";
            bomComponentId = "";
            operationId = dto.getOperationId();
            routerStepId = dto.getRouterStepId();
            materialId = dto.getMaterialId();
        }

        // 获取执行作业组件装配实绩
        MtAssembleConfirmActual mtAssembleConfirmActual = new MtAssembleConfirmActual();
        mtAssembleConfirmActual.setEoId(dto.getEoId());
        mtAssembleConfirmActual.setMaterialId(materialId);
        mtAssembleConfirmActual.setBomComponentId(bomComponentId);
        mtAssembleConfirmActual.setOperationId(operationId);
        mtAssembleConfirmActual.setRouterStepId(routerStepId);
        mtAssembleConfirmActual.setComponentType("ASSEMBLING");
        mtAssembleConfirmActual.setAssembleExcessFlag(assembleExcessFlag);
        mtAssembleConfirmActual.setBomId(bomId);
        List<String> assembleConfirmActualIds = mtAssembleConfirmActualRepository
                        .propertyLimitAssembleConfirmActualQuery(tenantId, mtAssembleConfirmActual);
        if (CollectionUtils.isEmpty(assembleConfirmActualIds)) {
            throw new MtException("MT_ASSEMBLE_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0054", "ASSEMBLE", "【API:eoComponentActualAssembleCancelProcess】"));
        }

        // 根据条件查询，可以确定唯一一条数据
        String assembleConfirmActualId = assembleConfirmActualIds.get(0);

        // 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(eventTypeCode);
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 更新执行作业装配确认实绩并记录历史
        MtAssembleConfirmActualHis mtAssembleConfirmActualHis = new MtAssembleConfirmActualHis();
        mtAssembleConfirmActualHis.setAssembleConfirmActualId(assembleConfirmActualId);
        mtAssembleConfirmActualHis.setEventId(eventId);
        mtAssembleConfirmActualRepository.assembleConfirmActualUpdate(tenantId, mtAssembleConfirmActualHis);

        MtAssembleProcessActualVO1 assembleProcessVO1 = new MtAssembleProcessActualVO1();
        assembleProcessVO1.setAssembleConfirmActualId(assembleConfirmActualId);
        assembleProcessVO1.setAssembleQty(-dto.getTrxAssembleQty());
        assembleProcessVO1.setRouterId(dto.getRouterId());
        assembleProcessVO1.setSubstepId(dto.getSubstepId());
        assembleProcessVO1.setWorkcellId(dto.getWorkcellId());
        assembleProcessVO1.setAssemblePointId(dto.getAssemblePointId());
        assembleProcessVO1.setReferenceArea(dto.getReferenceArea());
        assembleProcessVO1.setReferencePoint(dto.getReferencePoint());
        assembleProcessVO1.setLocatorId(dto.getLocatorId());
        assembleProcessVO1.setAssembleMethod(dto.getAssembleMethod());
        assembleProcessVO1.setOperateBy(dto.getOperationBy());
        assembleProcessVO1.setEventId(eventId);
        assembleProcessVO1.setRouterStepId(dto.getRouterStepId());
        assembleProcessVO1.setMaterialLotId(dto.getMaterialLotId());
        assembleProcessActualCreate(tenantId, assembleProcessVO1, "N");
    }

    /**
     * componentAssembleProcess-组件装配处理
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/25
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtAssembleProcessActualVO6 componentAssembleProcess(Long tenantId, MtAssembleProcessActualVO5 dto) {
        // 1. 获取routerType
        String routerType = null;
        if (StringUtils.isNotEmpty(dto.getRouterId())) {
            MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, dto.getRouterId());
            if (mtRouter != null) {
                routerType = mtRouter.getRouterType();
            }
        }

        // 2. 根据输入全部参数, 处理执行作业装配过程实绩和执行作业装配确认实绩
        MtAssembleProcessActualVO5 mtAssembleProcessActualVO5 = dto;
        mtAssembleProcessActualVO5.setAssembleRouterType(routerType);
        MtAssembleProcessActualVO6 mtAssembleProcessActual = eoComponentActualAssembleProcess(tenantId, dto);

        // 3. 处理执行作业组件实绩
        MtEoComponentActualVO5 eoComponentVO5 = new MtEoComponentActualVO5();
        eoComponentVO5.setEoId(dto.getEoId());
        eoComponentVO5.setMaterialId(dto.getMaterialId());
        eoComponentVO5.setBomComponentId(dto.getBomComponentId());
        eoComponentVO5.setOperationId(dto.getOperationId());
        eoComponentVO5.setRouterStepId(dto.getRouterStepId());
        eoComponentVO5.setTrxAssembleQty(dto.getTrxAssembleQty());
        eoComponentVO5.setAssembleExcessFlag(dto.getAssembleExcessFlag());
        eoComponentVO5.setAssembleRouterType(dto.getAssembleRouterType());
        eoComponentVO5.setWorkcellId(dto.getWorkcellId());
        eoComponentVO5.setLocatorId(dto.getLocatorId());
        eoComponentVO5.setParentEventId(dto.getParentEventId());
        eoComponentVO5.setEventRequestId(dto.getEventRequestId());
        eoComponentVO5.setShiftDate(dto.getShiftDate());
        eoComponentVO5.setShiftCode(dto.getShiftCode());
        eoComponentVO5.setAssembleRouterType(routerType);
        mtEoComponentActualRepository.eoComponentAssemble(tenantId, eoComponentVO5);

        // 4. 获取workOrderId
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        String workOrderId = mtEo == null ? null : mtEo.getWorkOrderId();

        // 5. 处理生产指令组件实绩
        String bomComponentType = "ASSEMBLING";
        String materialId = dto.getMaterialId();
        if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
            // 5.a
            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null) {
                throw new MtException("MT_ASSEMBLE_0071",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0071",
                                                "ASSEMBLE", dto.getBomComponentId(), "【API:componentAssembleProcess】"));
            }

            bomComponentType = bomComponent.getBomComponentType();
            materialId = bomComponent.getMaterialId();
        }

        routerType = null;
        String routerStepId = null;
        String operationId = dto.getOperationId();

        // 5.b 获取工艺路线工序
        if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
            MtRouterOperation mtRouterOperation =
                            mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
            if (mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                throw new MtException("MT_ASSEMBLE_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0072", "ASSEMBLE", "【API:componentAssembleProcess】"));
            }

            operationId = mtRouterOperation.getOperationId();
        }

        // 5.c 获取 routerType、routerStepId
        if (StringUtils.isNotEmpty(dto.getRouterStepId()) || StringUtils.isNotEmpty(dto.getOperationId())) {
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, workOrderId);
            if (mtWorkOrder != null) {
                MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, mtWorkOrder.getRouterId());
                if (mtRouter != null) {
                    routerType = mtRouter.getRouterType();

                    List<String> routerStepIdList = mtRouterStepRepository.operationStepQuery(tenantId, operationId,
                                    mtRouter.getRouterId());
                    if (CollectionUtils.isNotEmpty(routerStepIdList)) {
                        if (routerStepIdList.size() > 1) {
                            List<MtRouterStep> mtRouterStepList =
                                            mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIdList);
                            if (CollectionUtils.isNotEmpty(mtRouterStepList)) {
                                mtRouterStepList = mtRouterStepList.stream()
                                                .sorted(Comparator.comparing(MtRouterStep::getSequence))
                                                .collect(Collectors.toList());
                                routerStepId = mtRouterStepList.get(0).getRouterStepId();
                            }
                        } else {
                            routerStepId = routerStepIdList.get(0);
                        }
                    }
                }
            }
        }

        // 5.d 根据生产指令和物料获取生产指令组件行
        MtWorkOrderVO27 mtWorkOrderVO27 = new MtWorkOrderVO27();
        mtWorkOrderVO27.setWorkOrderId(workOrderId);
        mtWorkOrderVO27.setComponentType(bomComponentType);
        mtWorkOrderVO27.setMaterialId(materialId);
        mtWorkOrderVO27.setOperationId(operationId);
        List<String> woBomComponentIdList =
                        mtWorkOrderRepository.woMaterialLimitComponentQuery(tenantId, mtWorkOrderVO27);

        String assembleExcessFlag = "Y";
        String bomComponentId = null;
        if (CollectionUtils.isNotEmpty(woBomComponentIdList)) {
            assembleExcessFlag = "N";
            if (woBomComponentIdList.size() > 1) {
                List<MtBomComponentVO13> mtBomComponentVO13List =
                                mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, woBomComponentIdList);
                if (CollectionUtils.isNotEmpty(mtBomComponentVO13List)) {
                    mtBomComponentVO13List = mtBomComponentVO13List.stream()
                                    .sorted(Comparator.comparing(MtBomComponent::getLineNumber).reversed())
                                    .collect(Collectors.toList());
                    bomComponentId = mtBomComponentVO13List.get(0).getBomComponentId();
                }
            } else {
                bomComponentId = woBomComponentIdList.get(0);
            }
        }

        // 执行处理生产指令组件实绩
        MtWoComponentActualVO1 mtWoComponentActualVO1 = new MtWoComponentActualVO1();
        mtWoComponentActualVO1.setTrxAssembleQty(dto.getTrxAssembleQty());
        mtWoComponentActualVO1.setWorkcellId(dto.getWorkcellId());
        mtWoComponentActualVO1.setLocatorId(dto.getLocatorId());
        mtWoComponentActualVO1.setParentEventId(dto.getParentEventId());
        mtWoComponentActualVO1.setEventRequestId(dto.getEventRequestId());
        mtWoComponentActualVO1.setShiftDate(dto.getShiftDate());
        mtWoComponentActualVO1.setShiftCode(dto.getShiftCode());
        mtWoComponentActualVO1.setWorkOrderId(workOrderId);
        mtWoComponentActualVO1.setMaterialId(materialId);
        mtWoComponentActualVO1.setOperationId(operationId);
        mtWoComponentActualVO1.setBomComponentId(bomComponentId);
        mtWoComponentActualVO1.setRouterStepId(routerStepId);
        mtWoComponentActualVO1.setAssembleExcessFlag(assembleExcessFlag);
        mtWoComponentActualVO1.setAssembleRouterType(routerType);
        mtWorkOrderComponentActualRepository.woComponentAssemble(tenantId, mtWoComponentActualVO1);
        return mtAssembleProcessActual;
    }

    /**
     * componentAssembleCancelProcess-组件装配撤销处理
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/25
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void componentAssembleCancelProcess(Long tenantId, MtAssembleProcessActualVO5 dto) {
        // 1. 根据输入全部参数, 处理执行作业装配过程实绩和执行作业装配确认实绩
        eoComponentActualAssembleCancelProcess(tenantId, dto);

        // 3. 处理执行作业组件实绩
        MtEoComponentActualVO11 eoCompentVO11 = new MtEoComponentActualVO11();
        eoCompentVO11.setEoId(dto.getEoId());
        eoCompentVO11.setMaterialId(dto.getMaterialId());
        eoCompentVO11.setBomComponentId(dto.getBomComponentId());
        eoCompentVO11.setOperationId(dto.getOperationId());
        eoCompentVO11.setRouterStepId(dto.getRouterStepId());
        eoCompentVO11.setTrxAssembleQty(dto.getTrxAssembleQty());
        eoCompentVO11.setAssembleExcessFlag(dto.getAssembleExcessFlag());
        eoCompentVO11.setBomId(dto.getBomId());
        eoCompentVO11.setWorkcellId(dto.getWorkcellId());
        eoCompentVO11.setLocatorId(dto.getLocatorId());
        eoCompentVO11.setParentEventId(dto.getParentEventId());
        eoCompentVO11.setEventRequestId(dto.getEventRequestId());
        eoCompentVO11.setShiftDate(dto.getShiftDate());
        eoCompentVO11.setShiftCode(dto.getShiftCode());
        mtEoComponentActualRepository.eoComponentAssembleCancel(tenantId, eoCompentVO11);

        // 4. 获取workOrderId
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        String workOrderId = mtEo == null ? null : mtEo.getWorkOrderId();
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：componentScrapProcess】"));
        }

        if (StringUtils.isEmpty(mtWorkOrder.getBomId())) {
            throw new MtException("MT_ORDER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0005", "ORDER", "【API：componentScrapProcess】"));
        }

        // 5. 处理生产指令组件实绩
        String bomComponentType = "ASSEMBLING";
        String materialId = dto.getMaterialId();

        // 5.a
        if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null) {
                throw new MtException("MT_ASSEMBLE_0071",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0071",
                                                "ASSEMBLE", dto.getBomComponentId(), "【API:componentAssembleProcess】"));
            }

            bomComponentType = bomComponent.getBomComponentType();
            materialId = bomComponent.getMaterialId();
        }

        String routerType = null;
        String routerStepId = null;
        String operationId = dto.getOperationId();

        // 5.b 获取工艺路线工序
        if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
            MtRouterOperation mtRouterOperation =
                            mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
            if (mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                throw new MtException("MT_ASSEMBLE_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0072", "ASSEMBLE", "【API:componentAssembleProcess】"));
            }

            operationId = mtRouterOperation.getOperationId();
        }

        // 5.c 获取 routerType、routerStepId
        if (StringUtils.isNotEmpty(dto.getRouterStepId()) || StringUtils.isNotEmpty(dto.getOperationId())) {
            MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, mtWorkOrder.getRouterId());
            if (mtRouter != null) {
                routerType = mtRouter.getRouterType();

                List<String> routerStepIdList = mtRouterStepRepository.operationStepQuery(tenantId, operationId,
                                mtRouter.getRouterId());
                if (CollectionUtils.isNotEmpty(routerStepIdList)) {
                    if (routerStepIdList.size() > 1) {
                        List<MtRouterStep> mtRouterStepList =
                                        mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIdList);
                        if (CollectionUtils.isNotEmpty(mtRouterStepList)) {
                            mtRouterStepList = mtRouterStepList.stream()
                                            .sorted(Comparator.comparing(MtRouterStep::getSequence))
                                            .collect(Collectors.toList());
                            routerStepId = mtRouterStepList.get(0).getRouterStepId();
                        }
                    } else {
                        routerStepId = routerStepIdList.get(0);
                    }
                }
            }
        }

        // 5.d 根据生产指令和物料获取生产指令组件行
        MtWorkOrderVO27 mtWorkOrderVO27 = new MtWorkOrderVO27();
        mtWorkOrderVO27.setWorkOrderId(workOrderId);
        mtWorkOrderVO27.setComponentType(bomComponentType);
        mtWorkOrderVO27.setMaterialId(materialId);
        mtWorkOrderVO27.setOperationId(operationId);
        List<String> woBomComponentIdList =
                        mtWorkOrderRepository.woMaterialLimitComponentQuery(tenantId, mtWorkOrderVO27);

        String assembleExcessFlag = "Y";
        String bomComponentId = null;
        if (CollectionUtils.isNotEmpty(woBomComponentIdList)) {
            assembleExcessFlag = "N";
            if (woBomComponentIdList.size() > 1) {
                List<MtBomComponentVO13> mtBomComponentVO13List =
                                mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, woBomComponentIdList);
                if (CollectionUtils.isNotEmpty(mtBomComponentVO13List)) {
                    mtBomComponentVO13List = mtBomComponentVO13List.stream()
                                    .sorted(Comparator.comparing(MtBomComponent::getLineNumber).reversed())
                                    .collect(Collectors.toList());
                    bomComponentId = mtBomComponentVO13List.get(0).getBomComponentId();
                }
            } else {
                bomComponentId = woBomComponentIdList.get(0);
            }
        }

        // 5. 处理生产指令组件实绩
        MtWoComponentActualVO12 componentV12 = new MtWoComponentActualVO12();
        componentV12.setTrxAssembleQty(dto.getTrxAssembleQty());
        componentV12.setAssembleExcessFlag(assembleExcessFlag);
        componentV12.setWorkcellId(dto.getWorkcellId());
        componentV12.setLocatorId(dto.getLocatorId());
        componentV12.setParentEventId(dto.getParentEventId());
        componentV12.setEventRequestId(dto.getEventRequestId());
        componentV12.setShiftDate(dto.getShiftDate());
        componentV12.setShiftCode(dto.getShiftCode());

        componentV12.setBomId(mtWorkOrder.getBomId());
        componentV12.setWorkOrderId(workOrderId);
        componentV12.setMaterialId(materialId);
        componentV12.setBomComponentId(bomComponentId);
        componentV12.setOperationId(operationId);
        componentV12.setRouterStepId(routerStepId);
        componentV12.setAssembleRouterType(routerType);
        mtWorkOrderComponentActualRepository.woComponentAssembleCancel(tenantId, componentV12);
    }

    /**
     * eoComponentActualScrapProcess-执行作业组件装配报废实绩处理
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/25
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoComponentActualScrapProcess(Long tenantId, MtAssembleProcessActualVO7 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoComponentActualScrapProcess】"));
        }
        if (dto.getTrxScrappedQty() == null) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "trxScrappedQty", "【API:eoComponentActualScrapProcess】"));
        }
        if (new BigDecimal(dto.getTrxScrappedQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ASSEMBLE_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0007", "ASSEMBLE", "trxAssembleQty", "【API:eoComponentActualScrapProcess】"));
        }

        // 2. 获取装配清单ID
        String bomId = dto.getBomId();
        if (StringUtils.isEmpty(dto.getBomId())) {
            bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
        }

        // 3. 判断执行作业是否需要按工序装配
        String isOperationFlag = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());

        String eventTypeCode = null;
        String assembleExcessFlag = null;
        String bomComponentId = null;
        String operationId = null;
        String routerStepId = null;
        String materialId = null;
        if ((StringUtils.isEmpty(isOperationFlag) || "N".equals(isOperationFlag))
                        && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 2.1.
            // 若获取结果为空或为N，且输入参数assembleExcessFlag未输入或输入N，表示针对非强制装配模式下不按照工序的装配结果进行装配报废
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ASSEMBLE_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0058", "ASSEMBLE", "【API:eoComponentActualScrapProcess】"));
            }

            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null || !"ASSEMBLING".equals(bomComponent.getBomComponentType())) {
                throw new MtException("MT_ASSEMBLE_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0045", "ASSEMBLE", "ASSEMBLING", "【API:eoComponentActualScrapProcess】"));
            }
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                materialId = dto.getMaterialId();
            } else {
                if (StringUtils.isNotEmpty(bomComponent.getMaterialId())) {
                    materialId = bomComponent.getMaterialId();
                } else {
                    throw new MtException("MT_ASSEMBLE_0071",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0071",
                                                    "ASSEMBLE", bomComponent.getBomComponentId(),
                                                    "【API:eoComponentActualScrapProcess】"));

                }
            }

            eventTypeCode = "EO_PROCESS_ACTUAL_SCRAP";
            assembleExcessFlag = "N";
            bomComponentId = dto.getBomComponentId();
            operationId = "";
            routerStepId = "";
        } else if ((StringUtils.isEmpty(isOperationFlag) || "N".equals(isOperationFlag))
                        && "Y".equals(dto.getAssembleExcessFlag())) {
            // 2.2.
            // 若获取结果为空或为N，且输入参数assembleExcessFlag为Y，表示针对强制装配模式下不按照工序进行装配结果进行装配报废
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0059", "ASSEMBLE", "【API:eoComponentActualScrapProcess】"));
            }

            eventTypeCode = "EO_PROCESS_ACTUAL_EXCESS_SCRAP";
            assembleExcessFlag = "Y";
            bomComponentId = "";
            operationId = "";
            routerStepId = "";
            materialId = dto.getMaterialId();
        } else if ("Y".equals(isOperationFlag) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 2.3.
            // 若获取结果为Y，且输入参数assembleExcessFlag未输入或输入N，表示针对非强制装配模式下按照工序进行装配的装配结果进行报废
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ASSEMBLE_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0058", "ASSEMBLE", "【API:eoComponentActualScrapProcess】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ASSEMBLE_0060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0060", "ASSEMBLE", "【API:eoComponentActualScrapProcess】"));
            }

            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null || !"ASSEMBLING".equals(bomComponent.getBomComponentType())) {
                throw new MtException("MT_ASSEMBLE_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0045", "ASSEMBLE", "ASSEMBLING", "【API:eoComponentActualScrapProcess】"));
            }
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                materialId = dto.getMaterialId();
            } else {
                if (StringUtils.isNotEmpty(bomComponent.getMaterialId())) {
                    materialId = bomComponent.getMaterialId();
                } else {
                    throw new MtException("MT_ASSEMBLE_0071",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0071",
                                                    "ASSEMBLE", bomComponent.getBomComponentId(),
                                                    "【API:eoComponentActualScrapProcess】"));

                }
            }
            if (StringUtils.isNotEmpty(dto.getOperationId())) {
                operationId = dto.getOperationId();
            } else {
                MtRouterOperation mtRouterOperation = null;
                try {
                    mtRouterOperation = mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
                } catch (MtException e) {
                    e.printStackTrace();
                }
                if (mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                    throw new MtException("MT_ASSEMBLE_0072", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0072", "ASSEMBLE", "【API:eoComponentActualScrapProcess】"));
                }
                operationId = mtRouterOperation.getOperationId();
            }
            eventTypeCode = "EO_PROCESS_ACTUAL_OPERATION_SCRAP";
            assembleExcessFlag = "N";
            bomComponentId = dto.getBomComponentId();
            routerStepId = dto.getRouterStepId();
        } else if ("Y".equals(isOperationFlag) && "Y".equals(dto.getAssembleExcessFlag())) {
            // 2.4.
            // 若获取结果为Y，且输入参数assembleExcessFlag输入为Y，表示针对强制装配模式下按照工序进行装配的装配实绩进行报废
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0059", "ASSEMBLE", "【API:eoComponentActualScrapProcess】"));
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("MT_ASSEMBLE_0061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0061", "ASSEMBLE", "【API:eoComponentActualScrapProcess】"));
            }
            // update by peng.yuan 2020/2/24
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ASSEMBLE_0060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0060", "ASSEMBLE", "【API:eoComponentActualScrapProcess】"));
            }

            eventTypeCode = "EO_PROCESS_ACTUAL_EXCESS_OPERATION_SCRAP";
            assembleExcessFlag = "Y";
            bomComponentId = "";
            operationId = dto.getOperationId();
            routerStepId = dto.getRouterStepId();
            materialId = dto.getMaterialId();
        }

        // 获取执行作业组件装配实绩
        MtAssembleConfirmActual mtAssembleConfirmActual = new MtAssembleConfirmActual();
        mtAssembleConfirmActual.setEoId(dto.getEoId());
        mtAssembleConfirmActual.setMaterialId(materialId);
        mtAssembleConfirmActual.setBomComponentId(bomComponentId);
        mtAssembleConfirmActual.setOperationId(operationId);
        mtAssembleConfirmActual.setRouterStepId(routerStepId);
        mtAssembleConfirmActual.setComponentType("ASSEMBLING");
        mtAssembleConfirmActual.setAssembleExcessFlag(assembleExcessFlag);
        mtAssembleConfirmActual.setBomId(bomId);
        List<String> assembleConfirmActualIds = mtAssembleConfirmActualRepository
                        .propertyLimitAssembleConfirmActualQuery(tenantId, mtAssembleConfirmActual);
        if (CollectionUtils.isEmpty(assembleConfirmActualIds)) {
            throw new MtException("MT_ASSEMBLE_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0054", "ASSEMBLE", "【API:eoComponentActualScrapProcess】"));
        }

        // 根据条件查询，可以确定唯一一条数据
        String assembleConfirmActualId = assembleConfirmActualIds.get(0);

        // 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(eventTypeCode);
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 更新执行作业装配确认实绩并记录历史
        MtAssembleConfirmActualHis mtAssembleConfirmActualHis = new MtAssembleConfirmActualHis();
        mtAssembleConfirmActualHis.setAssembleConfirmActualId(assembleConfirmActualId);
        mtAssembleConfirmActualHis.setEventId(eventId);
        mtAssembleConfirmActualRepository.assembleConfirmActualUpdate(tenantId, mtAssembleConfirmActualHis);

        MtAssembleProcessActualVO1 assembleProcessVO1 = new MtAssembleProcessActualVO1();
        assembleProcessVO1.setAssembleConfirmActualId(assembleConfirmActualId);
        assembleProcessVO1.setScrappedQty(dto.getTrxScrappedQty());
        assembleProcessVO1.setRouterId(dto.getRouterId());
        assembleProcessVO1.setSubstepId(dto.getSubstepId());
        assembleProcessVO1.setWorkcellId(dto.getWorkcellId());
        assembleProcessVO1.setAssemblePointId(dto.getAssemblePointId());
        assembleProcessVO1.setReferenceArea(dto.getReferenceArea());
        assembleProcessVO1.setReferencePoint(dto.getReferencePoint());
        assembleProcessVO1.setLocatorId(dto.getLocatorId());
        assembleProcessVO1.setAssembleMethod(dto.getAssembleMethod());
        assembleProcessVO1.setOperateBy(dto.getOperationBy());
        assembleProcessVO1.setEventId(eventId);
        assembleProcessVO1.setRouterStepId(dto.getRouterStepId());
        assembleProcessVO1.setMaterialLotId(dto.getMaterialLotId());
        assembleProcessActualCreate(tenantId, assembleProcessVO1, "N");
    }

    /**
     * eoComponentActualScrapCancelProcess-执行作业组件装配报废取消实绩处理
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/26
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoComponentActualScrapCancelProcess(Long tenantId, MtAssembleProcessActualVO7 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoComponentActualScrapCancelProcess】"));
        }
        if (dto.getTrxScrappedQty() == null) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "trxScrappedQty", "【API:eoComponentActualScrapCancelProcess】"));
        }
        if (new BigDecimal(dto.getTrxScrappedQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ASSEMBLE_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0007", "ASSEMBLE",
                                            "trxAssembleQty", "【API:eoComponentActualScrapCancelProcess】"));
        }

        // 2. 获取装配清单ID
        String bomId = dto.getBomId();
        if (StringUtils.isEmpty(dto.getBomId())) {
            bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
        }

        // 3. 判断执行作业是否需要按工序装配
        String isOperationFlag = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());

        String eventTypeCode = null;
        String assembleExcessFlag = null;
        String bomComponentId = null;
        String operationId = null;
        String routerStepId = null;

        if ((StringUtils.isEmpty(isOperationFlag) || "N".equals(isOperationFlag))
                        && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 2.1.
            // 若获取结果为空或为N，且输入参数assembleExcessFlag未输入或输入N，表示针对非强制装配模式下不按照工序的装配报废结果进行装配报废取消
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ASSEMBLE_0062", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0062", "ASSEMBLE", "【API:eoComponentActualScrapCancelProcess】"));
            }

            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null || !"ASSEMBLING".equals(bomComponent.getBomComponentType())) {
                throw new MtException("MT_ASSEMBLE_0045",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0045",
                                                "ASSEMBLE", "ASSEMBLING", "【API:eoComponentActualScrapCancelProcess】"));
            }

            eventTypeCode = "EO_PROCESS_ACTUAL_SCRAP_CANCEL";
            assembleExcessFlag = "N";
            bomComponentId = dto.getBomComponentId();
            operationId = "";
            routerStepId = "";
        } else if ((StringUtils.isEmpty(isOperationFlag) || "N".equals(isOperationFlag))
                        && "Y".equals(dto.getAssembleExcessFlag())) {
            // 2.2.
            // 若获取结果为空或为N，且输入参数assembleExcessFlag为Y，表示针对强制装配模式下不按照工序进行装配报废结果进行装配报废取消
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0063", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0063", "ASSEMBLE", "【API:eoComponentActualScrapCancelProcess】"));
            }

            eventTypeCode = "EO_PROCESS_ACTUAL_EXCESS_SCRAP_CANCEL";
            assembleExcessFlag = "Y";
            bomComponentId = "";
            operationId = "";
            routerStepId = "";
        } else if ("Y".equals(isOperationFlag) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 2.3.
            // 若获取结果为Y，且输入参数assembleExcessFlag未输入或输入N，表示针对非强制装配模式下按照工序进行装配的装配报废结果进行报废取消
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ASSEMBLE_0062", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0062", "ASSEMBLE", "【API:eoComponentActualScrapCancelProcess】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ASSEMBLE_0064", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0064", "ASSEMBLE", "【API:eoComponentActualScrapCancelProcess】"));
            }

            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null || !"ASSEMBLING".equals(bomComponent.getBomComponentType())) {
                throw new MtException("MT_ASSEMBLE_0045",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0045",
                                                "ASSEMBLE", "ASSEMBLING", "【API:eoComponentActualScrapCancelProcess】"));
            }

            operationId = dto.getOperationId();
            if (StringUtils.isEmpty(operationId)) {
                MtRouterOperation mtRouterOperation = null;
                try {
                    mtRouterOperation = mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
                } catch (MtException e) {
                    e.printStackTrace();
                }
                if (mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                    throw new MtException("MT_ASSEMBLE_0072",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0072",
                                                    "ASSEMBLE", "【API:eoComponentActualScrapCancelProcess】"));
                }
                operationId = mtRouterOperation.getOperationId();
            }

            eventTypeCode = "EO_PROCESS_ACTUAL_OPERATION_SCRAP_CANCEL";
            assembleExcessFlag = "N";
            bomComponentId = dto.getBomComponentId();
            routerStepId = dto.getRouterStepId();
        } else if ("Y".equals(isOperationFlag) && "Y".equals(dto.getAssembleExcessFlag())) {
            // 2.4.
            // 若获取结果为Y，且输入参数assembleExcessFlag输入为Y，表示针对强制装配模式下按照工序进行装配的装配报废实绩进行报废取消
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0063", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0063", "ASSEMBLE", "【API:eoComponentActualScrapCancelProcess】"));
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("MT_ASSEMBLE_0065", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0065", "ASSEMBLE", "【API:eoComponentActualScrapCancelProcess】"));
            }


            // update by peng.yuan 2020/2/24
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ASSEMBLE_0064", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0064", "ASSEMBLE", "【API:eoComponentActualScrapCancelProcess】"));
            }

            eventTypeCode = "EO_PROCESS_ACTUAL_EXCESS_OPERATION_SCRAP_CANCEL";
            assembleExcessFlag = "Y";
            bomComponentId = "";
            operationId = dto.getOperationId();
            routerStepId = dto.getRouterStepId();
        }

        String materialId = dto.getMaterialId();
        if (StringUtils.isEmpty(materialId) && StringUtils.isNotEmpty(bomComponentId)) {
            MtBomComponent mtBomComponent = mtBomComponentRepository.bomComponentBasicGet(tenantId, bomComponentId);
            if (mtBomComponent == null || StringUtils.isEmpty(mtBomComponent.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0071",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0071",
                                                "ASSEMBLE", bomComponentId,
                                                "【API:eoComponentActualScrapCancelProcess】"));
            }

            materialId = mtBomComponent.getMaterialId();
        }

        // 获取执行作业组件装配实绩
        MtAssembleConfirmActual mtAssembleConfirmActual = new MtAssembleConfirmActual();
        mtAssembleConfirmActual.setEoId(dto.getEoId());
        mtAssembleConfirmActual.setMaterialId(materialId);
        mtAssembleConfirmActual.setBomComponentId(bomComponentId);
        mtAssembleConfirmActual.setOperationId(operationId);
        mtAssembleConfirmActual.setRouterStepId(routerStepId);
        mtAssembleConfirmActual.setComponentType("ASSEMBLING");
        mtAssembleConfirmActual.setAssembleExcessFlag(assembleExcessFlag);
        mtAssembleConfirmActual.setBomId(bomId);
        List<String> assembleConfirmActualIds = mtAssembleConfirmActualRepository
                        .propertyLimitAssembleConfirmActualQuery(tenantId, mtAssembleConfirmActual);
        if (CollectionUtils.isEmpty(assembleConfirmActualIds)) {
            throw new MtException("MT_ASSEMBLE_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0054", "ASSEMBLE", "【API:eoComponentActualScrapCancelProcess】"));
        }

        // 根据条件查询，可以确定唯一一条数据
        String assembleConfirmActualId = assembleConfirmActualIds.get(0);

        // 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(eventTypeCode);
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 更新执行作业装配确认实绩并记录历史
        MtAssembleConfirmActualHis mtAssembleConfirmActualHis = new MtAssembleConfirmActualHis();
        mtAssembleConfirmActualHis.setAssembleConfirmActualId(assembleConfirmActualId);
        mtAssembleConfirmActualHis.setEventId(eventId);
        mtAssembleConfirmActualRepository.assembleConfirmActualUpdate(tenantId, mtAssembleConfirmActualHis);

        MtAssembleProcessActualVO1 assembleProcessVO1 = new MtAssembleProcessActualVO1();
        assembleProcessVO1.setAssembleConfirmActualId(assembleConfirmActualId);
        assembleProcessVO1.setScrappedQty(-dto.getTrxScrappedQty());
        assembleProcessVO1.setRouterId(dto.getRouterId());
        assembleProcessVO1.setSubstepId(dto.getSubstepId());
        assembleProcessVO1.setWorkcellId(dto.getWorkcellId());
        assembleProcessVO1.setAssemblePointId(dto.getAssemblePointId());
        assembleProcessVO1.setReferenceArea(dto.getReferenceArea());
        assembleProcessVO1.setReferencePoint(dto.getReferencePoint());
        assembleProcessVO1.setLocatorId(dto.getLocatorId());
        assembleProcessVO1.setAssembleMethod(dto.getAssembleMethod());
        assembleProcessVO1.setOperateBy(dto.getOperationBy());
        assembleProcessVO1.setEventId(eventId);
        assembleProcessVO1.setRouterStepId(dto.getRouterStepId());
        assembleProcessVO1.setMaterialLotId(dto.getMaterialLotId());
        assembleProcessActualCreate(tenantId, assembleProcessVO1, "N");
    }

    /**
     * componentScrapProcess-组件报废处理
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/26
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void componentScrapProcess(Long tenantId, MtAssembleProcessActualVO7 dto) {
        // 1. 根据输入全部参数, 处理执行作业装配过程实绩和执行作业装配确认实绩
        eoComponentActualScrapProcess(tenantId, dto);

        // 3. 处理执行作业组件实绩
        MtEoComponentActualVO4 eoCompentVO4 = new MtEoComponentActualVO4();
        eoCompentVO4.setEoId(dto.getEoId());
        eoCompentVO4.setMaterialId(dto.getMaterialId());
        eoCompentVO4.setBomComponentId(dto.getBomComponentId());
        eoCompentVO4.setOperationId(dto.getOperationId());
        eoCompentVO4.setRouterStepId(dto.getRouterStepId());
        eoCompentVO4.setTrxScrappedQty(dto.getTrxScrappedQty());
        eoCompentVO4.setAssembleExcessFlag(dto.getAssembleExcessFlag());
        eoCompentVO4.setBomId(dto.getBomId());
        eoCompentVO4.setWorkcellId(dto.getWorkcellId());
        eoCompentVO4.setLocatorId(dto.getLocatorId());
        eoCompentVO4.setParentEventId(dto.getParentEventId());
        eoCompentVO4.setEventRequestId(dto.getEventRequestId());
        eoCompentVO4.setShiftDate(dto.getShiftDate());
        eoCompentVO4.setShiftCode(dto.getShiftCode());
        mtEoComponentActualRepository.eoComponentScrap(tenantId, eoCompentVO4);

        // 4. 获取workOrderId
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        String workOrderId = mtEo == null ? null : mtEo.getWorkOrderId();
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：componentScrapProcess】"));
        }

        if (StringUtils.isEmpty(mtWorkOrder.getBomId())) {
            throw new MtException("MT_ORDER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0005", "ORDER", "【API：componentScrapProcess】"));
        }

        // 5. 处理生产指令组件实绩
        String bomComponentType = "ASSEMBLING";
        String materialId = dto.getMaterialId();
        if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
            // 5.a
            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null) {
                throw new MtException("MT_ASSEMBLE_0071",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0071",
                                                "ASSEMBLE", dto.getBomComponentId(), "【API:componentAssembleProcess】"));
            }

            bomComponentType = bomComponent.getBomComponentType();
            materialId = bomComponent.getMaterialId();
        }

        String routerType = null;
        String routerStepId = null;
        String operationId = dto.getOperationId();

        // 5.b 获取工艺路线工序
        if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
            MtRouterOperation mtRouterOperation =
                            mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
            if (mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                throw new MtException("MT_ASSEMBLE_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0072", "ASSEMBLE", "【API:componentAssembleProcess】"));
            }

            operationId = mtRouterOperation.getOperationId();
        }

        // 5.c 获取 routerType、routerStepId
        if (StringUtils.isNotEmpty(dto.getRouterStepId()) || StringUtils.isNotEmpty(dto.getOperationId())) {
            MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, mtWorkOrder.getRouterId());
            if (mtRouter != null) {
                routerType = mtRouter.getRouterType();

                List<String> routerStepIdList = mtRouterStepRepository.operationStepQuery(tenantId, operationId,
                                mtRouter.getRouterId());
                if (CollectionUtils.isNotEmpty(routerStepIdList)) {
                    if (routerStepIdList.size() > 1) {
                        List<MtRouterStep> mtRouterStepList =
                                        mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIdList);
                        if (CollectionUtils.isNotEmpty(mtRouterStepList)) {
                            mtRouterStepList = mtRouterStepList.stream()
                                            .sorted(Comparator.comparing(MtRouterStep::getSequence))
                                            .collect(Collectors.toList());
                            routerStepId = mtRouterStepList.get(0).getRouterStepId();
                        }
                    } else {
                        routerStepId = routerStepIdList.get(0);
                    }
                }
            }
        }

        // 5.d 根据生产指令和物料获取生产指令组件行
        MtWorkOrderVO27 mtWorkOrderVO27 = new MtWorkOrderVO27();
        mtWorkOrderVO27.setWorkOrderId(workOrderId);
        mtWorkOrderVO27.setComponentType(bomComponentType);
        mtWorkOrderVO27.setMaterialId(materialId);
        mtWorkOrderVO27.setOperationId(operationId);
        List<String> woBomComponentIdList =
                        mtWorkOrderRepository.woMaterialLimitComponentQuery(tenantId, mtWorkOrderVO27);

        String assembleExcessFlag = "Y";
        String bomComponentId = null;
        if (CollectionUtils.isNotEmpty(woBomComponentIdList)) {
            assembleExcessFlag = "N";
            if (woBomComponentIdList.size() > 1) {
                List<MtBomComponentVO13> mtBomComponentVO13List =
                                mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, woBomComponentIdList);
                if (CollectionUtils.isNotEmpty(mtBomComponentVO13List)) {
                    mtBomComponentVO13List = mtBomComponentVO13List.stream()
                                    .sorted(Comparator.comparing(MtBomComponent::getLineNumber).reversed())
                                    .collect(Collectors.toList());
                    bomComponentId = mtBomComponentVO13List.get(0).getBomComponentId();
                }
            } else {
                bomComponentId = woBomComponentIdList.get(0);
            }
        }

        // 5. 处理生产指令组件实绩
        MtWoComponentActualVO8 componentV8 = new MtWoComponentActualVO8();
        componentV8.setTrxScrappedQty(dto.getTrxScrappedQty());
        componentV8.setWorkcellId(dto.getWorkcellId());
        componentV8.setLocatorId(dto.getLocatorId());
        componentV8.setParentEventId(dto.getParentEventId());
        componentV8.setEventRequestId(dto.getEventRequestId());
        componentV8.setShiftDate(dto.getShiftDate());
        componentV8.setShiftCode(dto.getShiftCode());

        componentV8.setBomId(mtWorkOrder.getBomId());
        componentV8.setWorkOrderId(workOrderId);
        componentV8.setMaterialId(materialId);
        componentV8.setOperationId(operationId);
        componentV8.setBomComponentId(bomComponentId);
        componentV8.setRouterStepId(routerStepId);
        componentV8.setAssembleExcessFlag(assembleExcessFlag);
        componentV8.setAssembleRouterType(routerType);
        mtWorkOrderComponentActualRepository.woComponentScrap(tenantId, componentV8);
    }

    /**
     * componentScrapCancelProcess-组件报废取消处理
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/26
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void componentScrapCancelProcess(Long tenantId, MtAssembleProcessActualVO7 dto) {
        // 1. 根据输入全部参数, 处理执行作业装配过程实绩和执行作业装配确认实绩
        eoComponentActualScrapCancelProcess(tenantId, dto);

        // 3. 处理执行作业组件实绩
        MtEoComponentActualVO4 eoCompentVO4 = new MtEoComponentActualVO4();
        eoCompentVO4.setEoId(dto.getEoId());
        eoCompentVO4.setMaterialId(dto.getMaterialId());
        eoCompentVO4.setBomComponentId(dto.getBomComponentId());
        eoCompentVO4.setOperationId(dto.getOperationId());
        eoCompentVO4.setRouterStepId(dto.getRouterStepId());
        eoCompentVO4.setTrxScrappedQty(dto.getTrxScrappedQty());
        eoCompentVO4.setAssembleExcessFlag(dto.getAssembleExcessFlag());
        eoCompentVO4.setBomId(dto.getBomId());
        eoCompentVO4.setWorkcellId(dto.getWorkcellId());
        eoCompentVO4.setLocatorId(dto.getLocatorId());
        eoCompentVO4.setParentEventId(dto.getParentEventId());
        eoCompentVO4.setEventRequestId(dto.getEventRequestId());
        eoCompentVO4.setShiftDate(dto.getShiftDate());
        eoCompentVO4.setShiftCode(dto.getShiftCode());
        mtEoComponentActualRepository.eoComponentScrapCancel(tenantId, eoCompentVO4);

        // 4. 获取workOrderId
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        String workOrderId = mtEo == null ? null : mtEo.getWorkOrderId();
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：componentScrapProcess】"));
        }

        if (StringUtils.isEmpty(mtWorkOrder.getBomId())) {
            throw new MtException("MT_ORDER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0005", "ORDER", "【API：componentScrapProcess】"));
        }

        // 5. 处理生产指令组件实绩
        String bomComponentType = "ASSEMBLING";
        String materialId = dto.getMaterialId();
        if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
            // 5.a
            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null) {
                throw new MtException("MT_ASSEMBLE_0071",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0071",
                                                "ASSEMBLE", dto.getBomComponentId(), "【API:componentAssembleProcess】"));
            }

            bomComponentType = bomComponent.getBomComponentType();
            materialId = bomComponent.getMaterialId();
        }

        String routerType = null;
        String routerStepId = null;
        String operationId = dto.getOperationId();

        // 5.b 获取工艺路线工序
        if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
            MtRouterOperation mtRouterOperation =
                            mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
            if (mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                throw new MtException("MT_ASSEMBLE_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0072", "ASSEMBLE", "【API:componentAssembleProcess】"));
            }

            operationId = mtRouterOperation.getOperationId();
        }

        // 5.c 获取 routerType、routerStepId
        if (StringUtils.isNotEmpty(dto.getRouterStepId()) || StringUtils.isNotEmpty(dto.getOperationId())) {
            MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, mtWorkOrder.getRouterId());
            if (mtRouter != null) {
                routerType = mtRouter.getRouterType();

                List<String> routerStepIdList = mtRouterStepRepository.operationStepQuery(tenantId, operationId,
                                mtRouter.getRouterId());
                if (CollectionUtils.isNotEmpty(routerStepIdList)) {
                    if (routerStepIdList.size() > 1) {
                        List<MtRouterStep> mtRouterStepList =
                                        mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIdList);
                        if (CollectionUtils.isNotEmpty(mtRouterStepList)) {
                            mtRouterStepList = mtRouterStepList.stream()
                                            .sorted(Comparator.comparing(MtRouterStep::getSequence))
                                            .collect(Collectors.toList());
                            routerStepId = mtRouterStepList.get(0).getRouterStepId();
                        }
                    } else {
                        routerStepId = routerStepIdList.get(0);
                    }
                }
            }
        }

        // 5.d 根据生产指令和物料获取生产指令组件行
        MtWorkOrderVO27 mtWorkOrderVO27 = new MtWorkOrderVO27();
        mtWorkOrderVO27.setWorkOrderId(workOrderId);
        mtWorkOrderVO27.setComponentType(bomComponentType);
        mtWorkOrderVO27.setMaterialId(materialId);
        mtWorkOrderVO27.setOperationId(operationId);
        List<String> woBomComponentIdList =
                        mtWorkOrderRepository.woMaterialLimitComponentQuery(tenantId, mtWorkOrderVO27);

        String assembleExcessFlag = "Y";
        String bomComponentId = null;
        if (CollectionUtils.isNotEmpty(woBomComponentIdList)) {
            assembleExcessFlag = "N";
            if (woBomComponentIdList.size() > 1) {
                List<MtBomComponentVO13> mtBomComponentVO13List =
                                mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, woBomComponentIdList);
                if (CollectionUtils.isNotEmpty(mtBomComponentVO13List)) {
                    mtBomComponentVO13List = mtBomComponentVO13List.stream()
                                    .sorted(Comparator.comparing(MtBomComponent::getLineNumber).reversed())
                                    .collect(Collectors.toList());
                    bomComponentId = mtBomComponentVO13List.get(0).getBomComponentId();
                }
            } else {
                bomComponentId = woBomComponentIdList.get(0);
            }
        }

        // 5. 处理生产指令组件实绩
        MtWoComponentActualVO8 componentV8 = new MtWoComponentActualVO8();
        componentV8.setTrxScrappedQty(dto.getTrxScrappedQty());
        componentV8.setWorkcellId(dto.getWorkcellId());
        componentV8.setLocatorId(dto.getLocatorId());
        componentV8.setParentEventId(dto.getParentEventId());
        componentV8.setEventRequestId(dto.getEventRequestId());
        componentV8.setShiftDate(dto.getShiftDate());
        componentV8.setShiftCode(dto.getShiftCode());

        componentV8.setBomId(mtWorkOrder.getBomId());
        componentV8.setWorkOrderId(workOrderId);
        componentV8.setMaterialId(materialId);
        componentV8.setOperationId(operationId);
        componentV8.setBomComponentId(bomComponentId);
        componentV8.setRouterStepId(routerStepId);
        componentV8.setAssembleExcessFlag(assembleExcessFlag);
        componentV8.setAssembleRouterType(routerType);
        mtWorkOrderComponentActualRepository.woComponentScrapCancel(tenantId, componentV8);
    }

    /**
     * eoComponentActualRemoveProcess-执行作业组件装配移除处理
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/26
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoComponentActualRemoveProcess(Long tenantId, MtAssembleProcessActualVO5 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoComponentActualRemoveProcess】"));
        }
        if (dto.getTrxAssembleQty() == null) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "trxAssembleQty", "【API:eoComponentActualRemoveProcess】"));
        }
        if (new BigDecimal(dto.getTrxAssembleQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ASSEMBLE_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0007", "ASSEMBLE", "trxAssembleQty", "【API:eoComponentActualRemoveProcess】"));
        }

        // 2. 判断执行作业是否需要按工序装配
        String isOperationFlag = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());

        String eventTypeCode = null;
        String assembleExcessFlag = null;
        String bomComponentId = null;
        String operationId = null;
        String routerStepId = null;

        if ((StringUtils.isEmpty(isOperationFlag) || "N".equals(isOperationFlag))
                        && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 2.1.
            // 若获取结果为空或为N，且输入参数assembleExcessFlag未输入或输入N，表示非强制装配移除模式下不按照工序进行装配移除
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ASSEMBLE_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0049", "ASSEMBLE", "【API:eoComponentActualRemoveProcess】"));
            }

            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null || !"REMOVE".equals(bomComponent.getBomComponentType())) {
                throw new MtException("MT_ASSEMBLE_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0045", "ASSEMBLE", "REMOVE", "【API:eoComponentActualRemoveProcess】"));
            }

            eventTypeCode = "EO_PROCESS_ACTUAL_REMOVE";
            assembleExcessFlag = "N";
            bomComponentId = dto.getBomComponentId();
            operationId = "";
            routerStepId = "";
        } else if ((StringUtils.isEmpty(isOperationFlag) || "N".equals(isOperationFlag))
                        && "Y".equals(dto.getAssembleExcessFlag())) {
            // 2.2. 若获取结果为空或为N，且输入参数assembleExcessFlag为Y，表示强制装配移除模式下不按照工序进行装配移除
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0050", "ASSEMBLE", "【API:eoComponentActualRemoveProcess】"));
            }

            eventTypeCode = "EO_PROCESS_ACTUAL_EXCESS_REMOVE";
            assembleExcessFlag = "Y";
            bomComponentId = "";
            operationId = "";
            routerStepId = "";
        } else if ("Y".equals(isOperationFlag) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 2.3.
            // 若获取结果为Y，且输入参数assembleExcessFlag未输入或输入N，表示非强制装配移除模式下按照工序进行装配移除
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ASSEMBLE_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0049", "ASSEMBLE", "【API:eoComponentActualRemoveProcess】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ASSEMBLE_0051", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0051", "ASSEMBLE", "【API:eoComponentActualRemoveProcess】"));
            }

            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null || !"REMOVE".equals(bomComponent.getBomComponentType())) {
                throw new MtException("MT_ASSEMBLE_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0045", "ASSEMBLE", "REMOVE", "【API:eoComponentActualRemoveProcess】"));
            }

            eventTypeCode = "EO_PROCESS_ACTUAL_OPERATION_REMOVE";
            assembleExcessFlag = "N";
            bomComponentId = dto.getBomComponentId();
            operationId = null;
            routerStepId = dto.getRouterStepId();
        } else if ("Y".equals(isOperationFlag) && "Y".equals(dto.getAssembleExcessFlag())) {
            // 2.4. 若获取结果为Y，且输入参数assembleExcessFlag输入为Y，表示强制装配移除模式下按照工序进行装配移除
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0050", "ASSEMBLE", "【API:eoComponentActualRemoveProcess】"));
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("MT_ASSEMBLE_0052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0052", "ASSEMBLE", "【API:eoComponentActualRemoveProcess】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ASSEMBLE_0051", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0051", "ASSEMBLE", "【API:eoComponentActualRemoveProcess】"));
            }
            eventTypeCode = "EO_PROCESS_ACTUAL_EXCESS_OPERATION_REMOVE";
            assembleExcessFlag = "Y";
            bomComponentId = "";
            operationId = dto.getOperationId();
            routerStepId = dto.getRouterStepId();
        }

        // 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(eventTypeCode);
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 更新执行作业装配确认实绩并记录历史
        MtAssembleConfirmActualHis mtAssembleConfirmActualHis = new MtAssembleConfirmActualHis();
        mtAssembleConfirmActualHis.setEoId(dto.getEoId());
        mtAssembleConfirmActualHis.setMaterialId(dto.getMaterialId());
        mtAssembleConfirmActualHis.setBomComponentId(bomComponentId);
        mtAssembleConfirmActualHis.setOperationId(operationId);
        mtAssembleConfirmActualHis.setRouterStepId(routerStepId);
        mtAssembleConfirmActualHis.setComponentType("REMOVE");
        mtAssembleConfirmActualHis.setAssembleExcessFlag(assembleExcessFlag);
        mtAssembleConfirmActualHis.setEventId(eventId);
        mtAssembleConfirmActualHis.setAssembleRouterType(dto.getAssembleRouterType());
        MtAssembleConfirmActualVO14 mtAssembleConfirmActualVO14 = mtAssembleConfirmActualRepository
                        .assembleConfirmActualUpdate(tenantId, mtAssembleConfirmActualHis);
        String assembleConfirmActualId = null;
        if (null != mtAssembleConfirmActualVO14) {
            assembleConfirmActualId = mtAssembleConfirmActualVO14.getAssembleConfirmActualId();
        }

        MtAssembleProcessActualVO1 assembleProcessVO1 = new MtAssembleProcessActualVO1();
        assembleProcessVO1.setAssembleConfirmActualId(assembleConfirmActualId);
        assembleProcessVO1.setAssembleQty(dto.getTrxAssembleQty());
        assembleProcessVO1.setRouterId(dto.getRouterId());
        assembleProcessVO1.setSubstepId(dto.getSubstepId());
        assembleProcessVO1.setWorkcellId(dto.getWorkcellId());
        assembleProcessVO1.setAssemblePointId(dto.getAssemblePointId());
        assembleProcessVO1.setReferenceArea(dto.getReferenceArea());
        assembleProcessVO1.setReferencePoint(dto.getReferencePoint());
        assembleProcessVO1.setLocatorId(dto.getLocatorId());
        assembleProcessVO1.setAssembleMethod(dto.getAssembleMethod());
        assembleProcessVO1.setOperateBy(dto.getOperationBy());
        assembleProcessVO1.setEventId(eventId);
        assembleProcessVO1.setRouterStepId(dto.getRouterStepId());
        assembleProcessVO1.setMaterialLotId(dto.getMaterialLotId());
        assembleProcessActualCreate(tenantId, assembleProcessVO1, "N");
    }

    /**
     * componentRemoveProcess-组件装配移除处理
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/26
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void componentRemoveProcess(Long tenantId, MtAssembleProcessActualVO5 dto) {
        // 1. 获取routerType
        String routerType = null;
        if (StringUtils.isNotEmpty(dto.getRouterId())) {
            MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, dto.getRouterId());
            if (mtRouter != null) {
                routerType = mtRouter.getRouterType();
            }
        }

        // 2. 根据输入全部参数, 处理执行作业装配过程实绩和执行作业装配确认实绩
        MtAssembleProcessActualVO5 assembleProcessVO5 = new MtAssembleProcessActualVO5();
        assembleProcessVO5.setEoId(dto.getEoId());
        assembleProcessVO5.setMaterialId(dto.getMaterialId());
        assembleProcessVO5.setBomComponentId(dto.getBomComponentId());
        assembleProcessVO5.setOperationId(dto.getOperationId());
        assembleProcessVO5.setRouterStepId(dto.getRouterStepId());
        assembleProcessVO5.setTrxAssembleQty(dto.getTrxAssembleQty());
        assembleProcessVO5.setAssembleExcessFlag(dto.getAssembleExcessFlag());
        assembleProcessVO5.setBomId(dto.getBomId());
        assembleProcessVO5.setLocatorId(dto.getLocatorId());
        assembleProcessVO5.setAssembleMethod(dto.getAssembleMethod());
        assembleProcessVO5.setAssemblePointId(dto.getAssemblePointId());
        assembleProcessVO5.setAssembleGroupId(dto.getAssembleGroupId());
        assembleProcessVO5.setReferenceArea(dto.getReferenceArea());
        assembleProcessVO5.setReferencePoint(dto.getReferencePoint());
        assembleProcessVO5.setRouterId(dto.getRouterId());
        assembleProcessVO5.setSubstepId(dto.getSubstepId());
        assembleProcessVO5.setOperationBy(dto.getOperationBy());
        assembleProcessVO5.setMaterialLotId(dto.getMaterialLotId());
        assembleProcessVO5.setWorkcellId(dto.getWorkcellId());
        assembleProcessVO5.setParentEventId(dto.getParentEventId());
        assembleProcessVO5.setEventRequestId(dto.getEventRequestId());
        assembleProcessVO5.setShiftDate(dto.getShiftDate());
        assembleProcessVO5.setShiftCode(dto.getShiftCode());
        assembleProcessVO5.setAssembleRouterType(routerType);
        eoComponentActualRemoveProcess(tenantId, assembleProcessVO5);

        // 3. 处理执行作业组件实绩
        MtEoComponentActualVO5 eoComponentVO5 = new MtEoComponentActualVO5();
        eoComponentVO5.setEoId(dto.getEoId());
        eoComponentVO5.setMaterialId(dto.getMaterialId());
        eoComponentVO5.setBomComponentId(dto.getBomComponentId());
        eoComponentVO5.setOperationId(dto.getOperationId());
        eoComponentVO5.setRouterStepId(dto.getRouterStepId());
        eoComponentVO5.setTrxAssembleQty(dto.getTrxAssembleQty());
        eoComponentVO5.setAssembleExcessFlag(dto.getAssembleExcessFlag());
        eoComponentVO5.setAssembleRouterType(dto.getAssembleRouterType());
        eoComponentVO5.setWorkcellId(dto.getWorkcellId());
        eoComponentVO5.setLocatorId(dto.getLocatorId());
        eoComponentVO5.setParentEventId(dto.getParentEventId());
        eoComponentVO5.setEventRequestId(dto.getEventRequestId());
        eoComponentVO5.setShiftDate(dto.getShiftDate());
        eoComponentVO5.setShiftCode(dto.getShiftCode());
        eoComponentVO5.setAssembleRouterType(routerType);
        mtEoComponentActualRepository.eoComponentRemove(tenantId, eoComponentVO5);

        // 4. 获取workOrderId
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        String workOrderId = mtEo == null ? null : mtEo.getWorkOrderId();

        // 5. 处理生产指令组件实绩
        String bomComponentType = "REMOVE";
        String materialId = dto.getMaterialId();
        if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
            // 5.a
            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null) {
                throw new MtException("MT_ASSEMBLE_0071",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0071",
                                                "ASSEMBLE", dto.getBomComponentId(), "【API:componentAssembleProcess】"));
            }

            bomComponentType = bomComponent.getBomComponentType();
            materialId = bomComponent.getMaterialId();
        }

        routerType = null;
        String routerStepId = null;
        String operationId = dto.getOperationId();

        // 5.b 获取工艺路线工序
        if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
            MtRouterOperation mtRouterOperation =
                            mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
            if (mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                throw new MtException("MT_ASSEMBLE_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0072", "ASSEMBLE", "【API:componentAssembleProcess】"));
            }

            operationId = mtRouterOperation.getOperationId();
        }

        // 5.c 获取 routerType、routerStepId
        if (StringUtils.isNotEmpty(dto.getRouterStepId()) || StringUtils.isNotEmpty(dto.getOperationId())) {
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, workOrderId);
            if (mtWorkOrder != null) {
                MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, mtWorkOrder.getRouterId());
                if (mtRouter != null) {
                    routerType = mtRouter.getRouterType();

                    List<String> routerStepIdList = mtRouterStepRepository.operationStepQuery(tenantId, operationId,
                                    mtRouter.getRouterId());
                    if (CollectionUtils.isNotEmpty(routerStepIdList)) {
                        if (routerStepIdList.size() > 1) {
                            List<MtRouterStep> mtRouterStepList =
                                            mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIdList);
                            if (CollectionUtils.isNotEmpty(mtRouterStepList)) {
                                mtRouterStepList = mtRouterStepList.stream()
                                                .sorted(Comparator.comparing(MtRouterStep::getSequence))
                                                .collect(Collectors.toList());
                                routerStepId = mtRouterStepList.get(0).getRouterStepId();
                            }
                        } else {
                            routerStepId = routerStepIdList.get(0);
                        }
                    }
                }
            }
        }

        // 5.d 根据生产指令和物料获取生产指令组件行
        MtWorkOrderVO27 mtWorkOrderVO27 = new MtWorkOrderVO27();
        mtWorkOrderVO27.setWorkOrderId(workOrderId);
        mtWorkOrderVO27.setComponentType(bomComponentType);
        mtWorkOrderVO27.setMaterialId(materialId);
        mtWorkOrderVO27.setOperationId(operationId);
        List<String> woBomComponentIdList =
                        mtWorkOrderRepository.woMaterialLimitComponentQuery(tenantId, mtWorkOrderVO27);

        String assembleExcessFlag = "Y";
        String bomComponentId = null;
        if (CollectionUtils.isNotEmpty(woBomComponentIdList)) {
            assembleExcessFlag = "N";
            if (woBomComponentIdList.size() > 1) {
                List<MtBomComponentVO13> mtBomComponentVO13List =
                                mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, woBomComponentIdList);
                if (CollectionUtils.isNotEmpty(mtBomComponentVO13List)) {
                    mtBomComponentVO13List = mtBomComponentVO13List.stream()
                                    .sorted(Comparator.comparing(MtBomComponent::getLineNumber).reversed())
                                    .collect(Collectors.toList());
                    bomComponentId = mtBomComponentVO13List.get(0).getBomComponentId();
                }
            } else {
                bomComponentId = woBomComponentIdList.get(0);
            }
        }

        // 5. 处理生产指令组件实绩
        MtWoComponentActualVO1 woComponentActualVO1 = new MtWoComponentActualVO1();
        woComponentActualVO1.setTrxAssembleQty(dto.getTrxAssembleQty());
        woComponentActualVO1.setWorkcellId(dto.getWorkcellId());
        woComponentActualVO1.setLocatorId(dto.getLocatorId());
        woComponentActualVO1.setParentEventId(dto.getParentEventId());
        woComponentActualVO1.setEventRequestId(dto.getEventRequestId());
        woComponentActualVO1.setShiftDate(dto.getShiftDate());
        woComponentActualVO1.setShiftCode(dto.getShiftCode());
        woComponentActualVO1.setWorkOrderId(workOrderId);
        woComponentActualVO1.setMaterialId(materialId);
        woComponentActualVO1.setBomComponentId(bomComponentId);
        woComponentActualVO1.setOperationId(operationId);
        woComponentActualVO1.setRouterStepId(routerStepId);
        woComponentActualVO1.setAssembleExcessFlag(assembleExcessFlag);
        woComponentActualVO1.setAssembleRouterType(routerType);
        mtWorkOrderComponentActualRepository.woComponentRemove(tenantId, woComponentActualVO1);
    }

    /**
     * eoAssemblePointComponentAssembleProcess-执行作业装配点组件装配处理
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/26
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoAssemblePointComponentAssembleProcess(Long tenantId, MtAssembleProcessActualVO5 dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoAssemblePointComponentAssembleProcess】"));
        }
        if (dto.getTrxAssembleQty() == null) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "trxAssembleQty", "【API:eoAssemblePointComponentAssembleProcess】"));
        }

        // 1. 获取参数
        // 1.1. 获取装配点 assemblePointId
        String assemblePointId = dto.getAssemblePointId();
        if (StringUtils.isEmpty(dto.getAssemblePointId()) && StringUtils.isNotEmpty(dto.getMaterialLotId())) {
            MtAssemblePointActualVO1 mtAssemblePointActualVO1 = new MtAssemblePointActualVO1();
            mtAssemblePointActualVO1.setMaterialLotId(dto.getMaterialLotId());
            List<String> assemblePointActualIds = this.mtAssemblePointActualRepository
                            .propertyLimitAssemblePointActualQuery(tenantId, mtAssemblePointActualVO1);
            if (CollectionUtils.isNotEmpty(assemblePointActualIds)) {
                // 业务确认，根据物料批可以确定一条数据
                MtAssemblePointActualVO1 pointActualVO1 = mtAssemblePointActualRepository
                                .assemblePointActualPropertyGet(tenantId, assemblePointActualIds.get(0));
                if (pointActualVO1 != null) {
                    assemblePointId = pointActualVO1.getAssemblePointId();
                }
            }
        }
        if (StringUtils.isEmpty(assemblePointId)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assemblePointId", "【API:eoAssemblePointComponentAssembleProcess】"));
        }
        // 1.2 获取物料 materialId
        String materialId = dto.getMaterialId();
        if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getBomComponentId())) {
            MtBomComponent mtBomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (mtBomComponent != null) {
                materialId = mtBomComponent.getMaterialId();
            }
        } else if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getBomComponentId())
                        && StringUtils.isNotEmpty(dto.getMaterialLotId())) {
            MtMaterialLot mtMaterialLot =
                            mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotId());
            if (mtMaterialLot != null) {
                materialId = mtMaterialLot.getMaterialId();
            }
        }
        // 1.3. 获取货位 locatorId
        String locatorId = dto.getLocatorId();
        if (StringUtils.isEmpty(dto.getLocatorId()) && StringUtils.isNotEmpty(materialId)
                        && StringUtils.isNotEmpty(dto.getEoId())) {
            MtEoComponentActualVO mtEoComponentVO = new MtEoComponentActualVO();
            mtEoComponentVO.setMaterialId(materialId);
            mtEoComponentVO.setEoId(dto.getEoId());
            String issuedLocatorId =
                            mtEoComponentActualRepository.eoComponentAssembleLocatorGet(tenantId, mtEoComponentVO);
            if (issuedLocatorId == null) {
                locatorId = "";
            } else {
                locatorId = issuedLocatorId;
            }
        }

        // 获取 EO 数据
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());

        // 2. 进行装配点组件物料消耗
        MtAssemblePointVO4 mtAssemblePointVO4 = new MtAssemblePointVO4();
        mtAssemblePointVO4.setAssemblePointId(assemblePointId);
        if (StringUtils.isNotEmpty(materialId)) {
            mtAssemblePointVO4.setMaterialId(materialId);
        }
        mtAssemblePointVO4.setMaterialLotId(dto.getMaterialLotId());
        mtAssemblePointVO4.setQty(dto.getTrxAssembleQty());
        mtAssemblePointVO4.setLocatorId(locatorId);
        // 业务确认，eo获取空，则传入空
        mtAssemblePointVO4.setSiteId(mtEo == null ? "" : mtEo.getSiteId());
        mtAssemblePointVO4.setWorkcellId(dto.getWorkcellId());
        mtAssemblePointVO4.setParentEventId(dto.getParentEventId());
        mtAssemblePointVO4.setEventRequestId(dto.getEventRequestId());
        mtAssemblePointVO4.setShiftCode(dto.getShiftCode());
        mtAssemblePointVO4.setShiftDate(dto.getShiftDate());
        mtAssemblePointRepository.assemblePointMaterialConsume(tenantId, mtAssemblePointVO4);

        // 3. 记录装配实绩
        MtAssembleProcessActualVO5 assembleProcessVO6 = new MtAssembleProcessActualVO5();
        assembleProcessVO6.setEoId(dto.getEoId());
        assembleProcessVO6.setMaterialId(materialId);
        if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {
            // 业务确认，输入为空，则不传入
            assembleProcessVO6.setMaterialLotId(dto.getMaterialLotId());
        }

        String bomComponentId = null;
        if (StringUtils.isEmpty(dto.getBomComponentId()) && !"Y".equals(dto.getAssembleExcessFlag())) {
            // 获取 BomComponentId
            MtEoComponentActualVO6 mtEoComponentVO6 = new MtEoComponentActualVO6();
            mtEoComponentVO6.setMaterialId(materialId);
            mtEoComponentVO6.setEoId(dto.getEoId());
            List<String> bomComponentIds =
                            mtEoComponentActualRepository.eoMaterialLimitComponentQuery(tenantId, mtEoComponentVO6);
            if (CollectionUtils.isEmpty(bomComponentIds)) {
                // 业务确认，结果为空就是空字符串
                bomComponentId = "";
            } else {
                // 业务确认，只会返回一条数据
                bomComponentId = bomComponentIds.get(0);
            }
        } else if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
            bomComponentId = dto.getBomComponentId();
        }

        assembleProcessVO6.setBomComponentId(bomComponentId);
        assembleProcessVO6.setOperationId(dto.getOperationId());
        assembleProcessVO6.setRouterStepId(dto.getRouterStepId());
        assembleProcessVO6.setTrxAssembleQty(dto.getTrxAssembleQty());
        assembleProcessVO6.setAssembleExcessFlag(dto.getAssembleExcessFlag());
        assembleProcessVO6.setLocatorId(locatorId);
        assembleProcessVO6.setAssemblePointId(assemblePointId);
        assembleProcessVO6.setReferencePoint(dto.getReferencePoint());
        assembleProcessVO6.setRouterId(dto.getRouterId());
        assembleProcessVO6.setSubstepId(dto.getSubstepId());
        assembleProcessVO6.setOperationBy(dto.getOperationBy());
        assembleProcessVO6.setWorkcellId(dto.getWorkcellId());
        assembleProcessVO6.setParentEventId(dto.getParentEventId());
        assembleProcessVO6.setEventRequestId(dto.getEventRequestId());
        assembleProcessVO6.setShiftCode(dto.getShiftCode());
        assembleProcessVO6.setShiftDate(dto.getShiftDate());
        assembleProcessVO6.setReferenceArea(dto.getReferenceArea());
        assembleProcessVO6.setMaterialId(materialId);
        componentAssembleProcess(tenantId, assembleProcessVO6);
    }

    /**
     * eoAssembleGroupComponentAssembleProcess-执行作业装配组组件装配处理
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/26
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoAssembleGroupComponentAssembleProcess(Long tenantId, MtAssembleProcessActualVO5 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoAssembleGroupComponentAssembleProcess】"));
        }
        if (dto.getTrxAssembleQty() == null) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "trxAssembleQty", "【API:eoAssembleGroupComponentAssembleProcess】"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "workcellId", "【API:eoAssembleGroupComponentAssembleProcess】"));
        }

        // 2. 获取已安装的装配组
        List<MtAssembleGroupActualVO2> assembleGroupActuals = mtAssembleGroupActualRepository
                        .wkcLimitCurrentAssembleGroupQuery(tenantId, dto.getWorkcellId());

        // 汇总assembleGrouId
        List<String> assembleGrouIds = assembleGroupActuals.stream().map(MtAssembleGroupActualVO2::getAssembleGroupId)
                        .distinct().collect(Collectors.toList());

        if (CollectionUtils.isEmpty(assembleGroupActuals)) {
            throw new MtException("MT_ASSEMBLE_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0034", "ASSEMBLE", "【API:eoAssembleGroupComponentAssembleProcess】"));
        } else {
            if (StringUtils.isNotEmpty(dto.getAssembleGroupId())) {
                if (!assembleGrouIds.contains(dto.getAssembleGroupId())) {
                    throw new MtException("MT_ASSEMBLE_0023",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0023",
                                                    "ASSEMBLE", "assembleGroupId:" + dto.getAssembleGroupId(),
                                                    "workcellId:" + dto.getWorkcellId(),
                                                    "【API:eoAssembleGroupComponentAssembleProcess】"));
                }
                assembleGrouIds = assembleGrouIds.stream().filter(t -> t.equals(dto.getAssembleGroupId()))
                                .collect(Collectors.toList());
            }
        }

        // 3. 获取装配组的控制属性
        List<MtAssembleGroup> mtAssembleGroupList =
                        mtAssembleGroupRepository.assembleGroupPropertyBatchGet(tenantId, assembleGrouIds);

        if (CollectionUtils.isEmpty(mtAssembleGroupList)) {
            MtAssembleGroup temp;
            for (String t : assembleGrouIds) {
                temp = new MtAssembleGroup();
                temp.setAssembleGroupId(t);
                temp.setAssembleControlFlag("N");
                mtAssembleGroupList.add(temp);
            }

        }
        if (mtAssembleGroupList.size() != assembleGrouIds.size()) {
            MtAssembleGroup temp;
            for (String t : assembleGrouIds) {
                if (!mtAssembleGroupList.stream().anyMatch(tt -> tt.getAssembleGroupId().equals(t))) {
                    temp = new MtAssembleGroup();
                    temp.setAssembleGroupId(t);
                    temp.setAssembleControlFlag("N");
                    mtAssembleGroupList.add(temp);
                }
            }
        }
        // 4. 基于第三步步获取到的每一个装配组及其属性依次执行
        for (MtAssembleGroup mtAssembleGroup : mtAssembleGroupList) {
            if ("Y".equals(mtAssembleGroup.getAssembleControlFlag())) {
                // 若assembleControlFlag = Y，则按照装配控制进行装配组物料消耗
                MtAssembleGroupActualVO4 mtAssembleGroupActualVO4 = new MtAssembleGroupActualVO4();
                mtAssembleGroupActualVO4.setWorkcellId(dto.getWorkcellId());
                mtAssembleGroupActualVO4.setEoId(dto.getEoId());
                mtAssembleGroupActualVO4.setReferenceArea(dto.getReferenceArea());
                mtAssembleGroupActualVO4.setAssembleGroupId(mtAssembleGroup.getAssembleGroupId());
                mtAssembleGroupActualRepository.wkcAssembleGroupControlVerify(tenantId, mtAssembleGroupActualVO4);

                // 获取装配控制Id
                MtAssembleControlVO1 mtAssembleControlVO1 = new MtAssembleControlVO1();
                mtAssembleControlVO1.setObjectType("EO");
                mtAssembleControlVO1.setObjectId(dto.getEoId());
                mtAssembleControlVO1.setOrganizationType("WORKCELL");
                mtAssembleControlVO1.setOrganizationId(dto.getWorkcellId());
                mtAssembleControlVO1.setReferenceArea(dto.getReferenceArea());
                String assembleControlId =
                                mtAssembleControlRepository.assembleControlGet(tenantId, mtAssembleControlVO1);
                if (StringUtils.isEmpty(assembleControlId)) {
                    throw new MtException("MT_ASSEMBLE_0035",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0035",
                                                    "ASSEMBLE", "【API:eoAssembleGroupComponentAssembleProcess】"));
                }

                // 获取装配点、物料、数量
                MtAssemblePointControlVO5 mtAssemblePointControlVO5 = new MtAssemblePointControlVO5();
                mtAssemblePointControlVO5.setAssembleControlId(assembleControlId);
                mtAssemblePointControlVO5.setAssembleGroupId(mtAssembleGroup.getAssembleGroupId());
                if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                    mtAssemblePointControlVO5.setMaterialId(dto.getMaterialId());
                } else if (StringUtils.isEmpty(dto.getMaterialId())
                                && StringUtils.isNotEmpty(dto.getBomComponentId())) {
                    MtBomComponentVO8 bomComponentVO8 =
                                    mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
                    if (bomComponentVO8 != null) {
                        mtAssemblePointControlVO5.setMaterialId(bomComponentVO8.getMaterialId());
                    }
                }

                List<MtAssemblePointControlVO4> mtAssemblePointControlVO4List = mtAssemblePointControlRepository
                                .assembleGroupLimitAssembleControlPropertyQuery(tenantId, mtAssemblePointControlVO5);
                if (CollectionUtils.isEmpty(mtAssemblePointControlVO4List)) {
                    throw new MtException("MT_ASSEMBLE_0035",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0035",
                                                    "ASSEMBLE", "【API:eoAssembleGroupComponentAssembleProcess】"));
                }

                // 5. 依次循环调用 eoAssemblePointComponentAssembleProcess
                for (MtAssemblePointControlVO4 mtAssemblePointControl : mtAssemblePointControlVO4List) {
                    MtAssembleProcessActualVO5 assembleProcessVO5 = new MtAssembleProcessActualVO5();
                    assembleProcessVO5.setAssemblePointId(mtAssemblePointControl.getAssemblePointId());
                    assembleProcessVO5.setMaterialId(mtAssemblePointControl.getMaterialId());
                    assembleProcessVO5.setTrxAssembleQty(new BigDecimal(dto.getTrxAssembleQty().toString())
                                    .multiply(new BigDecimal(mtAssemblePointControl.getUnitQty().toString()))
                                    .doubleValue());
                    assembleProcessVO5.setEoId(dto.getEoId());
                    assembleProcessVO5.setBomComponentId(dto.getBomComponentId());
                    assembleProcessVO5.setOperationId(dto.getOperationId());
                    assembleProcessVO5.setRouterStepId(dto.getRouterStepId());
                    assembleProcessVO5.setAssembleExcessFlag(dto.getAssembleExcessFlag());
                    assembleProcessVO5.setLocatorId(dto.getLocatorId());
                    assembleProcessVO5.setRouterId(dto.getRouterId());
                    assembleProcessVO5.setSubstepId(dto.getSubstepId());
                    assembleProcessVO5.setOperationBy(dto.getOperationBy());
                    assembleProcessVO5.setWorkcellId(dto.getWorkcellId());
                    assembleProcessVO5.setEventRequestId(dto.getEventRequestId());
                    assembleProcessVO5.setParentEventId(dto.getParentEventId());
                    assembleProcessVO5.setShiftDate(dto.getShiftDate());
                    assembleProcessVO5.setShiftCode(dto.getShiftCode());
                    assembleProcessVO5.setReferenceArea(dto.getReferenceArea());
                    assembleProcessVO5.setReferencePoint(dto.getReferencePoint());
                    eoAssemblePointComponentAssembleProcess(tenantId, assembleProcessVO5);
                }
            } else {
                // 若assembleControlFlag ≠ Y，则按照顺序装配组模式进行装配组物料消耗
                BigDecimal sequenceNeedConsumeQty = new BigDecimal(dto.getTrxAssembleQty().toString());

                // 验证参数：
                if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getBomComponentId())) {
                    throw new MtException("MT_ASSEMBLE_0002",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002",
                                                    "ASSEMBLE", "materialId、bomComponentId",
                                                    "【API:eoAssembleGroupComponentAssembleProcess】"));
                }

                // 获取顺序装配组第一个可消耗的装配点 first_assemblePointId
                String materialId = null;

                if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                    materialId = dto.getMaterialId();
                } else {
                    MtBomComponentVO8 bomComponentVO8 =
                                    mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
                    if (bomComponentVO8 != null) {
                        materialId = bomComponentVO8.getMaterialId();
                    }
                }

                String firstAssemblePointId = mtAssemblePointRepository.firstAvailableLoadingAssemblePointGet(tenantId,
                                mtAssembleGroup.getAssembleGroupId(), materialId);

                // 获取装配点装配数量
                MtAssemblePointActualVO2 assemblePointActualVO2 = new MtAssemblePointActualVO2();
                assemblePointActualVO2.setAssemblePointId(firstAssemblePointId);
                assemblePointActualVO2.setMaterialId(materialId);
                Double loadingQty = mtAssemblePointActualRepository.assemblePointLoadingMaterialQtyGet(tenantId,
                                assemblePointActualVO2);

                BigDecimal loadingQtyBig = new BigDecimal(loadingQty.toString());

                if (loadingQtyBig.compareTo(sequenceNeedConsumeQty) >= 0) {
                    // 若loadingQty ≥ sequenceNeedConsumeQty，认为当前装配点满足消耗需求
                    MtAssembleProcessActualVO5 assembleProcessVO5 = new MtAssembleProcessActualVO5();
                    assembleProcessVO5.setAssemblePointId(firstAssemblePointId);
                    assembleProcessVO5.setMaterialId(materialId);
                    assembleProcessVO5.setTrxAssembleQty(dto.getTrxAssembleQty());
                    assembleProcessVO5.setEoId(dto.getEoId());
                    assembleProcessVO5.setBomComponentId(dto.getBomComponentId());
                    assembleProcessVO5.setOperationId(dto.getOperationId());
                    assembleProcessVO5.setRouterStepId(dto.getRouterStepId());
                    assembleProcessVO5.setAssembleExcessFlag(dto.getAssembleExcessFlag());
                    assembleProcessVO5.setLocatorId(dto.getLocatorId());
                    assembleProcessVO5.setRouterId(dto.getRouterId());
                    assembleProcessVO5.setSubstepId(dto.getSubstepId());
                    assembleProcessVO5.setOperationBy(dto.getOperationBy());
                    assembleProcessVO5.setWorkcellId(dto.getWorkcellId());
                    assembleProcessVO5.setEventRequestId(dto.getEventRequestId());
                    assembleProcessVO5.setParentEventId(dto.getParentEventId());
                    assembleProcessVO5.setShiftDate(dto.getShiftDate());
                    assembleProcessVO5.setShiftCode(dto.getShiftCode());
                    assembleProcessVO5.setReferenceArea(dto.getReferenceArea());
                    assembleProcessVO5.setReferencePoint(dto.getReferencePoint());
                    eoAssemblePointComponentAssembleProcess(tenantId, assembleProcessVO5);
                } else {
                    // 若loadingQty ＜ sequenceNeedConsumeQty(第四步)，认为当前装配点不满足消耗需求
                    // a. 首先将当前装配点数量卸载清空
                    MtAssembleProcessActualVO5 assembleProcessVO5 = new MtAssembleProcessActualVO5();
                    assembleProcessVO5.setAssemblePointId(firstAssemblePointId);
                    assembleProcessVO5.setMaterialId(materialId);
                    assembleProcessVO5.setTrxAssembleQty(loadingQty);
                    assembleProcessVO5.setEoId(dto.getEoId());
                    assembleProcessVO5.setBomComponentId(dto.getBomComponentId());
                    assembleProcessVO5.setOperationId(dto.getOperationId());
                    assembleProcessVO5.setRouterStepId(dto.getRouterStepId());
                    assembleProcessVO5.setAssembleExcessFlag(dto.getAssembleExcessFlag());
                    assembleProcessVO5.setLocatorId(dto.getLocatorId());
                    assembleProcessVO5.setRouterId(dto.getRouterId());
                    assembleProcessVO5.setSubstepId(dto.getSubstepId());
                    assembleProcessVO5.setOperationBy(dto.getOperationBy());
                    assembleProcessVO5.setWorkcellId(dto.getWorkcellId());
                    assembleProcessVO5.setEventRequestId(dto.getEventRequestId());
                    assembleProcessVO5.setParentEventId(dto.getParentEventId());
                    assembleProcessVO5.setShiftDate(dto.getShiftDate());
                    assembleProcessVO5.setShiftCode(dto.getShiftCode());
                    assembleProcessVO5.setReferenceArea(dto.getReferenceArea());
                    assembleProcessVO5.setReferencePoint(dto.getReferencePoint());
                    eoAssemblePointComponentAssembleProcess(tenantId, assembleProcessVO5);

                    // 然后减少需消耗数量再次调用该API
                    assembleProcessVO5 = new MtAssembleProcessActualVO5();
                    assembleProcessVO5.setAssembleGroupId(dto.getAssembleGroupId());
                    assembleProcessVO5.setMaterialId(dto.getMaterialId());
                    assembleProcessVO5.setTrxAssembleQty(sequenceNeedConsumeQty.subtract(loadingQtyBig).doubleValue());
                    assembleProcessVO5.setEoId(dto.getEoId());
                    assembleProcessVO5.setBomComponentId(dto.getBomComponentId());
                    assembleProcessVO5.setOperationId(dto.getOperationId());
                    assembleProcessVO5.setRouterStepId(dto.getRouterStepId());
                    assembleProcessVO5.setAssembleExcessFlag(dto.getAssembleExcessFlag());
                    assembleProcessVO5.setLocatorId(dto.getLocatorId());
                    assembleProcessVO5.setRouterId(dto.getRouterId());
                    assembleProcessVO5.setSubstepId(dto.getSubstepId());
                    assembleProcessVO5.setOperationBy(dto.getOperationBy());
                    assembleProcessVO5.setWorkcellId(dto.getWorkcellId());
                    assembleProcessVO5.setEventRequestId(dto.getEventRequestId());
                    assembleProcessVO5.setParentEventId(dto.getParentEventId());
                    assembleProcessVO5.setShiftDate(dto.getShiftDate());
                    assembleProcessVO5.setShiftCode(dto.getShiftCode());
                    assembleProcessVO5.setReferenceArea(dto.getReferenceArea());
                    assembleProcessVO5.setReferencePoint(dto.getReferencePoint());
                    eoAssembleGroupComponentAssembleProcess(tenantId, assembleProcessVO5);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assembleProActAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "keyId", "【API:assembleProActAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtAssembleProcessActual mtAssembleProcessActual = new MtAssembleProcessActual();
        mtAssembleProcessActual.setTenantId(tenantId);
        mtAssembleProcessActual.setAssembleProcessActualId(dto.getKeyId());
        mtAssembleProcessActual = mtAssembleProcessActualMapper.selectOne(mtAssembleProcessActual);
        if (mtAssembleProcessActual == null) {
            throw new MtException("MT_ASSEMBLE_0077",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0077", "ASSEMBLE",
                                            dto.getKeyId(), TABLE_NAME, "【API:assembleProActAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, ATTR_TABLE_NAME, dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }

    @Override
    public List<MtAssembleProcessActual> selectListByMaterialLotIds(Long tenantId, List<String> materialLotIdList) {
        return mtAssembleProcessActualMapper.selectListByMaterialLotIds(tenantId, materialLotIdList);
    }

    @Override
    public List<MtAssembleProcessActualVO3> componentLimitAssembleActualForEoIds(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            return Collections.emptyList();
        }
        return mtAssembleProcessActualMapper.componentLimitAssembleActualForEoIds(tenantId, eoId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtAssembleProcessActualVO13> assembleProcessActualBatchCreate(Long tenantId,
                    MtAssembleProcessActualVO12 dto) {
        String apiName = "【API:assembleProcessActualBatchCreate】";
        // 1. 验证参数有效性
        if (dto == null || CollectionUtils.isEmpty(dto.getAssembleInfoList()) || dto.getAssembleInfoList().stream()
                        .anyMatch(t -> MtIdHelper.isIdNull(t.getAssembleConfirmActualId()))) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "assembleConfirmActualId", apiName));
        }
        if (MtIdHelper.isIdNull(dto.getEventId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eventId", apiName));
        }
        if (dto.getAssembleInfoList().stream()
                        .anyMatch(t -> t.getAssembleQty() == null && t.getScrappedQty() == null)) {
            throw new MtException("MT_ASSEMBLE_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0002", "ASSEMBLE", "assembleQty、scrappedQty", apiName));

        }

        // 获取装配确认实绩属性
        List<String> assembleConfirmActualIds = dto.getAssembleInfoList().stream()
                        .map(MtAssembleProcessActualVO12.AssembleInfo::getAssembleConfirmActualId).distinct()
                        .collect(Collectors.toList());
        List<MtAssembleConfirmActual> mtAssembleConfirmActuals = mtAssembleConfirmActualRepository
                        .assembleConfirmActualPropertyBatchGet(tenantId, assembleConfirmActualIds);
        if (CollectionUtils.isEmpty(mtAssembleConfirmActuals)

                        || mtAssembleConfirmActuals.size() != assembleConfirmActualIds.size()) {
            throw new MtException("MT_ASSEMBLE_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0036", "ASSEMBLE", apiName));
        }
        Map<String, MtAssembleConfirmActual> mtAssembleConfirmActualMap = mtAssembleConfirmActuals.stream()
                        .collect(Collectors.toMap(MtAssembleConfirmActual::getAssembleConfirmActualId, c -> c));
        List<String> routerStepIds = mtAssembleConfirmActuals.stream().map(MtAssembleConfirmActual::getRouterStepId)
                        .distinct().filter(MtIdHelper::isIdNotNull).collect(Collectors.toList());

        // 获取routerId
        List<String> tempRouterStepIds =
                        dto.getAssembleInfoList().stream().filter(t -> MtIdHelper.isIdNull(t.getRouterId()))
                                        .map(MtAssembleProcessActualVO12.AssembleInfo::getRouterStepId).distinct()
                                        .filter(MtIdHelper::isIdNotNull).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(tempRouterStepIds)) {
            routerStepIds.addAll(tempRouterStepIds);
        }
        Map<String, String> mtRouterStepMap = new HashMap<>(routerStepIds.size());
        if (CollectionUtils.isNotEmpty(routerStepIds)) {
            List<MtRouterStep> mtRouterSteps = mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIds);
            if (CollectionUtils.isNotEmpty(mtRouterSteps)) {
                mtRouterStepMap = mtRouterSteps.stream()
                                .collect(Collectors.toMap(MtRouterStep::getRouterStepId, MtRouterStep::getRouterId));
            }
        }

        // 获取装配组
        List<String> assemblePointIds =
                        dto.getAssembleInfoList().stream().filter(t -> MtIdHelper.isIdNull(t.getAssembleGroupId()))
                                        .map(MtAssembleProcessActualVO12.AssembleInfo::getAssemblePointId).distinct()
                                        .filter(MtIdHelper::isIdNotNull).collect(Collectors.toList());
        Map<String, String> mtAssemblePointMap = new HashMap<>(assemblePointIds.size());
        if (CollectionUtils.isNotEmpty(assemblePointIds)) {
            List<MtAssemblePoint> mtAssemblePoints =
                            mtAssemblePointRepository.assemblePointPropertyBatchGet(tenantId, assemblePointIds);
            if (CollectionUtils.isNotEmpty(mtAssemblePoints)) {
                mtAssemblePointMap = mtAssemblePoints.stream().collect(Collectors
                                .toMap(MtAssemblePoint::getAssemblePointId, MtAssemblePoint::getAssembleGroupId));
            }
        }

        // 获取装配时消耗库位
        List<String> locatorConfirmIds =
                        dto.getAssembleInfoList().stream().filter(t -> MtIdHelper.isIdNull(t.getLocatorId()))
                                        .map(MtAssembleProcessActualVO12.AssembleInfo::getAssembleConfirmActualId)
                                        .distinct().filter(MtIdHelper::isIdNotNull).collect(Collectors.toList());
        Map<String, MtEo> mtEoMap = new HashMap<>();
        Map<MtPfepInventoryVO10, String> mtPfepInventoryMap = new HashMap<>();
        Map<String, String> mtModProdLineManufacturingMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(locatorConfirmIds)) {
            List<MtAssembleConfirmActual> confirmActuals = mtAssembleConfirmActualMap.entrySet().stream()
                            .filter(t -> locatorConfirmIds.contains(t.getKey())).map(Map.Entry::getValue)
                            .collect(Collectors.toList());
            List<String> eoIds = confirmActuals.stream().map(MtAssembleConfirmActual::getEoId).distinct()
                            .collect(Collectors.toList());
            List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
            List<String> prodLineIds =
                            mtEos.stream().map(MtEo::getProductionLineId).distinct().collect(Collectors.toList());
            mtEoMap = mtEos.stream().collect(Collectors.toMap(MtEo::getEoId, c -> c));

            // 根据pfep获取默认发料库位
            List<MtPfepInventoryVO> voList = new ArrayList<>(confirmActuals.size());
            for (MtAssembleConfirmActual confirmActual : confirmActuals) {
                MtPfepInventoryVO queryVO = new MtPfepInventoryVO();
                queryVO.setOrganizationType("PROD_LINE");
                MtEo mtEo = mtEoMap.get(confirmActual.getEoId());
                if (mtEo != null && MtIdHelper.isIdNotNull(confirmActual.getMaterialId())) {
                    queryVO.setOrganizationId(mtEo.getProductionLineId());
                    queryVO.setSiteId(mtEo.getSiteId());
                    queryVO.setMaterialId(confirmActual.getMaterialId());
                    voList.add(queryVO);
                }
            }
            List<MtPfepInventoryVO3> mtPfepInventoryVO3s = mtPfepInventoryRepository.pfepInventoryBatchGet(tenantId,
                            voList, Arrays.asList(MtPfepInventory.FIELD_ISSUED_LOCATOR_ID));

            // 获取不到，按空处理
            mtPfepInventoryMap = mtPfepInventoryVO3s.stream().collect(Collectors.toMap(
                            t -> new MtPfepInventoryVO10(t.getMaterialId(), t.getSiteId(), t.getOrganizationType(),
                                            t.getOrganizationId()),
                            c -> MtIdHelper.isIdNull(c.getIssuedLocatorId()) ? "" : c.getIssuedLocatorId(),
                            (o, n) -> n));

            // 根据产线获取
            if (CollectionUtils.isNotEmpty(prodLineIds)) {
                List<MtModProdLineManufacturing> mtModProdLineManufacturings = mtModProdLineManufacturingRepository
                                .prodLineManufacturingPropertyBatchGet(tenantId, prodLineIds);
                mtModProdLineManufacturingMap = mtModProdLineManufacturings.stream()
                                .collect(Collectors.toMap(MtModProdLineManufacturing::getProdLineId,
                                                MtModProdLineManufacturing::getIssuedLocatorId));
            }
        }

        // 获取装配方式
        List<String> assembleMethods = dto.getAssembleInfoList().stream()
                        .filter(t -> StringUtils.isEmpty(t.getAssembleMethod())
                                        && (MtIdHelper.isIdNotNull(t.getAssembleGroupId())
                                                        || MtIdHelper.isIdNotNull(t.getAssemblePointId())))
                        .map(MtAssembleProcessActualVO12.AssembleInfo::getAssembleConfirmActualId).distinct()
                        .filter(MtIdHelper::isIdNotNull).collect(Collectors.toList());
        Map<MtAssemblePointActualVO12, Long> assembleMethodMap = new HashMap<>(assembleMethods.size());
        if (CollectionUtils.isNotEmpty(assembleMethods)) {
            List<String> materialIds = mtAssembleConfirmActualMap.entrySet().stream()
                            .filter(t -> assembleMethods.contains(t.getKey())).map(Map.Entry::getValue)
                            .map(MtAssembleConfirmActual::getMaterialId).distinct().filter(MtIdHelper::isIdNotNull)
                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(materialIds)) {
                List<MtAssemblePointActualVO11> mtAssemblePointActualVO11s =
                                mtAssemblePointActualMapper.selectPropertyByConditions(tenantId, materialIds);
                assembleMethodMap = mtAssemblePointActualVO11s.stream().collect(Collectors.toMap(
                                t -> new MtAssemblePointActualVO12(
                                                MtIdHelper.isIdNull(t.getAssembleGroupId()) ? ""
                                                                : t.getAssembleGroupId(),
                                                MtIdHelper.isIdNull(t.getAssemblePointId()) ? ""
                                                                : t.getAssemblePointId(),
                                                t.getMaterialId()),
                                MtAssemblePointActualVO11::getCount));
            }
        }

        // 获取物料批最新历史ID
        List<String> materialLotIds = dto.getAssembleInfoList().stream()
                        .map(MtAssembleProcessActualVO12.AssembleInfo::getMaterialLotId).distinct()
                        .filter(MtIdHelper::isIdNotNull).collect(Collectors.toList());
        Map<String, String> materialLottMap = new HashMap<>(materialLotIds.size());
        if (CollectionUtils.isNotEmpty(materialLotIds)) {
            List<MtMaterialLot> materialLotList =
                            mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds);
            if (CollectionUtils.isNotEmpty(materialLotList)) {
                materialLottMap = materialLotList.stream().collect(
                                Collectors.toMap(MtMaterialLot::getMaterialLotId, MtMaterialLot::getLatestHisId));
            }
        }

        // 2. 创建装配过程实绩
        Date now = new Date(System.currentTimeMillis());
        Long userId = MtUserClient.getCurrentUserId();
        SequenceInfo mtSequenceInfo = MtSqlHelper.getSequenceInfo(MtAssembleProcessActual.class);
        List<String> Ids = this.mtCustomDbRepository.getNextKeys(mtSequenceInfo.getPrimarySequence(),
                        dto.getAssembleInfoList().size());
        List<String> cIds = this.mtCustomDbRepository.getNextKeys(mtSequenceInfo.getCidSequence(),
                        dto.getAssembleInfoList().size());
        int count = 0;
        List<MtAssembleProcessActual> insertList = new ArrayList<>(dto.getAssembleInfoList().size());
        List<MtAssembleProcessActualVO13> result = new ArrayList<>(dto.getAssembleInfoList().size());
        for (MtAssembleProcessActualVO12.AssembleInfo assembleInfo : dto.getAssembleInfoList()) {
            MtAssembleConfirmActual mtAssembleConfirmActual =
                            mtAssembleConfirmActualMap.get(assembleInfo.getAssembleConfirmActualId());
            MtAssembleProcessActual processActual = new MtAssembleProcessActual();
            processActual.setTenantId(tenantId);
            processActual.setAssembleConfirmActualId(assembleInfo.getAssembleConfirmActualId());
            processActual.setAssembleQty(assembleInfo.getAssembleQty() == null ? Double.valueOf(0.0D)
                            : assembleInfo.getAssembleQty());
            processActual.setScrapQty(assembleInfo.getScrappedQty() == null ? Double.valueOf(0.0D)
                            : assembleInfo.getScrappedQty());
            processActual.setSubstepId(assembleInfo.getSubstepId());
            processActual.setWorkcellId(assembleInfo.getWorkcellId());
            processActual.setAssemblePointId(assembleInfo.getAssemblePointId());
            processActual.setReferenceArea(assembleInfo.getReferenceArea());
            processActual.setReferencePoint(assembleInfo.getReferencePoint());
            processActual.setMaterialLotId(assembleInfo.getMaterialLotId());
            processActual.setOperateBy(dto.getOperateBy() == null ? MtUserClient.getCurrentUserId()
                            : Long.valueOf(dto.getOperateBy()));
            processActual.setEventId(dto.getEventId());
            processActual.setEventTime(now);
            processActual.setEventBy(userId);
            processActual.setCreatedBy(userId);
            processActual.setCreationDate(now);
            processActual.setLastUpdatedBy(userId);
            processActual.setLastUpdateDate(now);
            processActual.setObjectVersionNumber(1L);
            processActual.setAssembleProcessActualId(Ids.get(count));
            processActual.setCid(Long.valueOf(cIds.get(count)));

            processActual.setRouterId(assembleInfo.getRouterId());
            if (MtIdHelper.isIdNull(assembleInfo.getRouterId())) {
                if (MtIdHelper.isIdNotNull(assembleInfo.getRouterStepId())) {
                    processActual.setRouterId(mtRouterStepMap.get(assembleInfo.getRouterStepId()));
                }
                if (MtIdHelper.isIdNull(processActual.getRouterId())) {
                    processActual.setRouterId(mtRouterStepMap.get(mtAssembleConfirmActual.getRouterStepId()));
                }
            }
            processActual.setAssembleGroupId(assembleInfo.getAssembleGroupId());
            if (MtIdHelper.isIdNull(assembleInfo.getAssembleGroupId())
                            && MtIdHelper.isIdNotNull(assembleInfo.getAssemblePointId())) {
                processActual.setAssembleGroupId(mtAssemblePointMap.get(assembleInfo.getAssemblePointId()));
            }
            if (MtIdHelper.isIdNotNull(assembleInfo.getMaterialLotId())) {
                processActual.setMaterialLotHisId(materialLottMap.get(assembleInfo.getMaterialLotId()));
            }

            String assembleMethod = assembleInfo.getAssembleMethod();
            if (StringUtils.isEmpty(assembleMethod) && (MtIdHelper.isIdNotNull(assembleInfo.getAssembleGroupId())
                            || MtIdHelper.isIdNotNull(assembleInfo.getAssemblePointId()))) {

                MtAssemblePointActualVO12 mtAssemblePointActualVO12 = new MtAssemblePointActualVO12(
                                MtIdHelper.isIdNull(assembleInfo.getAssembleGroupId()) ? ""
                                                : assembleInfo.getAssembleGroupId(),
                                MtIdHelper.isIdNull(assembleInfo.getAssemblePointId()) ? ""
                                                : assembleInfo.getAssemblePointId(),
                                mtAssembleConfirmActual.getMaterialId());
                Long pointActualCount = assembleMethodMap.get(mtAssemblePointActualVO12);
                if (pointActualCount == null || pointActualCount == 1L) {
                    assembleMethod = "S_FEEDING";
                } else {
                    assembleMethod = "M_FEEDING";
                }
            }
            processActual.setAssembleMethod(assembleMethod);
            String locatorId = assembleInfo.getLocatorId();
            if (MtIdHelper.isIdNull(locatorId)) {
                MtEo mtEo = mtEoMap.get(mtAssembleConfirmActual.getEoId());
                if (mtEo != null) {
                    if (MtIdHelper.isIdNotNull(mtAssembleConfirmActual.getMaterialId())) {
                        MtPfepInventoryVO10 mtPfepInventoryVO10 =
                                        new MtPfepInventoryVO10(mtAssembleConfirmActual.getMaterialId(),
                                                        mtEo.getSiteId(), "PROD_LINE", mtEo.getProductionLineId());
                        locatorId = mtPfepInventoryMap.get(mtPfepInventoryVO10);
                    }
                    if (MtIdHelper.isIdNull(locatorId)) {
                        locatorId = mtModProdLineManufacturingMap.get(mtEo.getProductionLineId());
                    }
                }
            }

            processActual.setLocatorId(locatorId);
            insertList.add(processActual);

            // 返回值
            MtAssembleProcessActualVO13 res =
                            mtAssembleProcessActualTransMapper.mtAssProAcVO12TOMtAssProActVO13(assembleInfo);
            res.setAssembleProcessActualId(Ids.get(count));
            result.add(res);
            count++;
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            mtCustomDbRepository.batchInsertTarzan(insertList);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtAssembleProcessActualVO6> eoComponentActualAssembleBatchProcess(Long tenantId,
                    MtAssembleProcessActualVO9 dto) {
        String apiName = "【API:eoComponentActualAssembleBatchProcess】";
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(dto.getEoInfo())

                        || dto.getEoInfo().stream().anyMatch(t -> MtIdHelper.isIdNull(t.getEoId()))) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", apiName));
        }

        for (MtAssembleProcessActualVO10 ever : dto.getEoInfo()) {
            if (StringUtils.isEmpty(ever.getOperationAssembleFlag())) {

                throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0001", "ASSEMBLE", "operationAssembleFlag", apiName));
            }
            if (CollectionUtils.isEmpty(ever.getMaterialInfo())) {
                throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0001", "ASSEMBLE", "trxAssembleQty", apiName));
            }
            for (MtAssembleProcessActualVO11 material : ever.getMaterialInfo()) {
                if (material.getTrxAssembleQty() == null) {
                    throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE", "trxAssembleQty", apiName));
                }
                if (new BigDecimal(material.getTrxAssembleQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
                    throw new MtException("MT_ASSEMBLE_0007", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0007", "ASSEMBLE", "trxAssembleQty", apiName));
                }
            }
        }

        // 生成事件,如果传入了就不用管了
        String eventId = dto.getEventId();
        if (MtIdHelper.isIdNull(eventId)) {
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("COMPONENT_ASSEMBLE");
            eventCreateVO.setParentEventId(dto.getParentEventId());
            eventCreateVO.setEventRequestId(dto.getEventRequestId());
            eventCreateVO.setWorkcellId(dto.getWorkcellId());
            eventCreateVO.setShiftCode(dto.getShiftCode());
            eventCreateVO.setShiftDate(dto.getShiftDate() == null ? null
                            : Date.from(dto.getShiftDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        }

        // 记录需要验证的BOM
        List<String> bomComponentIds = new ArrayList<>();
        // 这里进行不同情况的判断
        List<MtAssembleConfirmActualVO25> assembleConfirmActualList = new ArrayList<>();
        List<MtAssembleProcessActualVO12.AssembleInfo> assembleProcessActualList = new ArrayList<>();
        for (MtAssembleProcessActualVO10 eo : dto.getEoInfo()) {
            for (MtAssembleProcessActualVO11 material : eo.getMaterialInfo()) {
                String assembleExcessFlag = null;
                String bomComponentId = null;
                String operationId = null;
                String routerStepId = null;
                if (!MtBaseConstants.YES.equals(eo.getOperationAssembleFlag())
                                && !MtBaseConstants.YES.equals(material.getAssembleExcessFlag())) {
                    // 2.1.
                    // 若获取结果为空或为N，且输入参数assembleExcessFlag未输入或输入N，表示非强制装配模式下不按照工序进行装配
                    if (MtIdHelper.isIdNull(material.getBomComponentId())) {

                        throw new MtException("MT_ASSEMBLE_0044", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0044", "ASSEMBLE", apiName));
                    }
                    // checkComponentTypeFlag输入不为N时，需要检验装配清单类型
                    if (!MtBaseConstants.NO.equals(dto.getCheckComponentTypeFlag())) {
                        bomComponentIds.add(material.getBomComponentId());
                    }

                    assembleExcessFlag = MtBaseConstants.NO;
                    bomComponentId = material.getBomComponentId();
                    operationId = MtBaseConstants.LONG_SPECIAL;
                    routerStepId = MtBaseConstants.LONG_SPECIAL;
                } else if (!MtBaseConstants.YES.equals(eo.getOperationAssembleFlag())
                                && MtBaseConstants.YES.equals(material.getAssembleExcessFlag())) {
                    // 2.2. 若获取结果为空或为N，且输入参数assembleExcessFlag为Y，表示强制装配模式下不按照工序进行装配
                    if (MtIdHelper.isIdNull(material.getMaterialId())) {

                        throw new MtException("MT_ASSEMBLE_0046", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0046", "ASSEMBLE", apiName));
                    }

                    assembleExcessFlag = MtBaseConstants.YES;
                    bomComponentId = MtBaseConstants.LONG_SPECIAL;
                    operationId = MtBaseConstants.LONG_SPECIAL;
                    routerStepId = MtBaseConstants.LONG_SPECIAL;
                } else if (MtBaseConstants.YES.equals(eo.getOperationAssembleFlag())
                                && !MtBaseConstants.YES.equals(material.getAssembleExcessFlag())) {
                    // 2.3. 若获取结果为Y，且输入参数assembleExcessFlag未输入或输入N，表示非强制装配模式下按照工序进行装配
                    if (MtIdHelper.isIdNull(material.getBomComponentId())) {

                        throw new MtException("MT_ASSEMBLE_0044", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0044", "ASSEMBLE", apiName));
                    }
                    if (MtIdHelper.isIdNull(eo.getRouterStepId())) {
                        throw new MtException("MT_ASSEMBLE_0047", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0047", "ASSEMBLE", apiName));
                    }

                    if (!MtBaseConstants.NO.equals(dto.getCheckComponentTypeFlag())) {
                        bomComponentIds.add(material.getBomComponentId());
                    }

                    assembleExcessFlag = MtBaseConstants.NO;
                    bomComponentId = material.getBomComponentId();
                    operationId = null;
                    routerStepId = eo.getRouterStepId();
                } else if (MtBaseConstants.YES.equals(eo.getOperationAssembleFlag())
                                && MtBaseConstants.YES.equals(material.getAssembleExcessFlag())) {
                    // 2.4. 若获取结果为Y，且输入参数assembleExcessFlag输入为Y，表示强制装配模式下按照工序进行装配
                    if (MtIdHelper.isIdNull(material.getMaterialId())) {

                        throw new MtException("MT_ASSEMBLE_0046", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0046", "ASSEMBLE", apiName));
                    }
                    if (MtIdHelper.isIdNull(dto.getOperationId())) {
                        throw new MtException("MT_ASSEMBLE_0048", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0048", "ASSEMBLE", apiName));
                    }
                    if (MtIdHelper.isIdNull(eo.getRouterStepId())) {
                        throw new MtException("MT_ASSEMBLE_0047", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0047", "ASSEMBLE", apiName));
                    }
                    assembleExcessFlag = MtBaseConstants.YES;
                    bomComponentId = MtBaseConstants.LONG_SPECIAL;
                    operationId = dto.getOperationId();
                    routerStepId = eo.getRouterStepId();
                }

                // 准备需要新增的数据
                MtAssembleConfirmActualVO25 assembleConfirmActualVO25 = new MtAssembleConfirmActualVO25();
                assembleConfirmActualVO25.setEoId(eo.getEoId());
                assembleConfirmActualVO25.setMaterialId(material.getMaterialId());
                assembleConfirmActualVO25.setBomId(eo.getBomId());
                assembleConfirmActualVO25.setBomComponentId(bomComponentId);
                assembleConfirmActualVO25.setOperationId(operationId);
                assembleConfirmActualVO25.setRouterStepId(routerStepId);
                assembleConfirmActualVO25.setComponentType("ASSEMBLING");
                assembleConfirmActualVO25.setAssembleExcessFlag(assembleExcessFlag);
                assembleConfirmActualVO25.setAssembleRouterType(eo.getAssembleRouterType());
                assembleConfirmActualVO25.setSubstituteFlag(material.getSubstituteFlag());
                assembleConfirmActualList.add(assembleConfirmActualVO25);

                MtAssembleProcessActualVO12.AssembleInfo assembleProcessVO1 =
                                new MtAssembleProcessActualVO12.AssembleInfo();
                assembleProcessVO1.setAssembleQty(material.getTrxAssembleQty());
                assembleProcessVO1.setRouterId(eo.getRouterId());
                assembleProcessVO1.setSubstepId(eo.getSubstepId());
                assembleProcessVO1.setWorkcellId(dto.getWorkcellId());
                assembleProcessVO1.setAssemblePointId(material.getAssemblePointId());
                assembleProcessVO1.setReferenceArea(material.getReferenceArea());
                assembleProcessVO1.setReferencePoint(material.getReferencePoint());
                assembleProcessVO1.setLocatorId(material.getLocatorId());
                assembleProcessVO1.setAssembleMethod(material.getAssembleMethod());
                assembleProcessVO1.setRouterStepId(eo.getRouterStepId());
                assembleProcessVO1.setMaterialLotId(material.getMaterialLotId());
                assembleProcessActualList.add(assembleProcessVO1);

            }
        }

        // 验证BOM
        if (CollectionUtils.isNotEmpty(bomComponentIds)) {
            List<String> distinctBomComponentIds = bomComponentIds.stream().distinct().collect(Collectors.toList());
            List<MtBomComponentVO13> bomComponents =
                            mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, distinctBomComponentIds);
            if (distinctBomComponentIds.size() != bomComponents.size()
                            || bomComponents.stream().anyMatch(t -> !"ASSEMBLING".equals(t.getBomComponentType()))) {
                throw new MtException("MT_ASSEMBLE_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,

                                "MT_ASSEMBLE_0045", "ASSEMBLE", "ASSEMBLING", apiName));
            }
        }

        // 更新执行作业装配确认实绩并记录历史
        MtAssembleConfirmActualVO24 confirmActual = new MtAssembleConfirmActualVO24();
        confirmActual.setEventId(eventId);
        confirmActual.setActualList(assembleConfirmActualList);
        List<MtAssembleConfirmActualVO26> confirmActualList =
                        mtAssembleConfirmActualRepository.assembleConfirmActualBatchUpdate(tenantId, confirmActual);

        Map<MtAssembleConfirmActualVO25, MtAssembleConfirmActualVO26> cofirmMap = confirmActualList.stream()
                        .collect(Collectors.toMap(t -> new MtAssembleConfirmActualVO25(t.getEoId(), t.getMaterialId(),
                                        t.getOperationId(), t.getComponentType(), t.getBomComponentId(), t.getBomId(),
                                        t.getRouterStepId()), t -> t));

        for (MtAssembleConfirmActualVO25 ever : assembleConfirmActualList) {
            MtAssembleConfirmActualVO26 s = cofirmMap.get(new MtAssembleConfirmActualVO25(ever.getEoId(),
                            ever.getMaterialId(), ever.getOperationId(), ever.getComponentType(),
                            ever.getBomComponentId(), ever.getBomId(), ever.getRouterStepId()));
            ever.setAssembleConfirmActualId(s.getAssembleConfirmActualId());
        }

        int index = 0;
        for (MtAssembleProcessActualVO12.AssembleInfo ever : assembleProcessActualList) {
            ever.setAssembleConfirmActualId(assembleConfirmActualList.get(index).getAssembleConfirmActualId());
            index++;
        }

        MtAssembleProcessActualVO12 processActual = new MtAssembleProcessActualVO12();
        processActual.setEventId(eventId);
        processActual.setOperateBy(dto.getOperationBy());
        processActual.setAssembleInfoList(assembleProcessActualList);
        List<MtAssembleProcessActualVO13> result = self().assembleProcessActualBatchCreate(tenantId, processActual);

        return result.stream().map(t -> {
            MtAssembleProcessActualVO6 one = new MtAssembleProcessActualVO6();
            one.setAssembleConfirmActualId(t.getAssembleConfirmActualId());
            one.setAssembleProcessActualId(t.getAssembleProcessActualId());
            return one;
        }).collect(Collectors.toList());
    }

    @Override
    public void eoAssembleLimitWoBatchAssemble(Long tenantId, MtAssembleProcessActualVO18 dto) {
        String apiName = "【API:eoAssembleLimitWoBatchAssemble】";

        // 参数校验
        List<MtAssembleProcessActualVO14> woInfo = dto.getWoInfo();
        if (woInfo.stream().anyMatch(t -> MtIdHelper.isIdNull(t.getWorkOrderId()))) {

            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "workOrderId", apiName));
        }
        if (woInfo.stream().anyMatch(t -> StringUtils.isEmpty(t.getOperationAssembleFlag()))) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "operationAssembleFlag", apiName));
        }

        List<MtAssembleProcessActualVO15> totalMaterialInfoList =
                        woInfo.stream().map(MtAssembleProcessActualVO14::getMaterialInfo).flatMap(Collection::stream)
                                        .collect(Collectors.toList());
        if (totalMaterialInfoList.stream().anyMatch(t -> t.getTrxAssembleQty() == null)) {

            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "trxAssembleQty", apiName));
        }
        if (totalMaterialInfoList.stream()
                        .anyMatch(t -> BigDecimal.valueOf(t.getTrxAssembleQty()).compareTo(BigDecimal.ZERO) <= 0)) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxAssembleQty", apiName));
        }

        // 批量查询生产指令数据
        List<MtWorkOrder> mtWorkOrderList = mtWorkOrderRepository.woPropertyBatchGet(tenantId, woInfo.stream()
                        .map(MtAssembleProcessActualVO14::getWorkOrderId).distinct().collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(mtWorkOrderList) || woInfo.size() != mtWorkOrderList.size()) {

            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", apiName));
        }

        // 记录woId对应工艺路线类型
        Map<String, String> woRouterTypeMap = new HashMap<>();

        // 记录按工序装配的woid对应的顺序最小步骤id
        Map<String, String> workOrderRouterStepMap = new HashMap<>();

        List<MtWorkOrder> routerWorkOrderList = mtWorkOrderList.stream()
                        .filter(t -> MtIdHelper.isIdNotNull(t.getRouterId())).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(routerWorkOrderList)) {
            List<MtRouter> mtRouterList = mtRouterRepository.routerBatchGet(tenantId, routerWorkOrderList.stream()
                            .map(MtWorkOrder::getRouterId).distinct().collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(mtRouterList)) {
                Map<String, MtRouter> mtRouterMap =
                                mtRouterList.stream().collect(Collectors.toMap(MtRouter::getRouterId, t -> t));
                for (MtWorkOrder mtWorkOrder : routerWorkOrderList) {
                    MtRouter mtRouter = mtRouterMap.get(mtWorkOrder.getRouterId());
                    if (mtRouter != null) {
                        woRouterTypeMap.put(mtWorkOrder.getWorkOrderId(), mtRouter.getRouterType());
                    }
                }
            }

            // 筛选按工序装配的wo
            List<String> operationAssembleWoIds = woInfo.stream()
                            .filter(t -> t.getOperationAssembleFlag().equalsIgnoreCase(MtBaseConstants.YES))
                            .map(MtAssembleProcessActualVO14::getWorkOrderId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(operationAssembleWoIds)) {
                List<MtWorkOrder> operationAssembleWorkOrderList = routerWorkOrderList.stream()
                                .filter(t -> operationAssembleWoIds.contains(t.getWorkOrderId()))
                                .collect(Collectors.toList());
                List<String> routerStepIds = mtRouterStepRepository.operationStepBatchQuery(tenantId,
                                Collections.singletonList(dto.getOperationId()), operationAssembleWorkOrderList.stream()
                                                .map(MtWorkOrder::getRouterId).distinct().collect(Collectors.toList()));
                if (CollectionUtils.isNotEmpty(routerStepIds)) {
                    Map<String, List<MtRouterStep>> routerStepMap =
                                    mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIds).stream()
                                                    .collect(Collectors.groupingBy(MtRouterStep::getRouterId));

                    for (MtWorkOrder mtWorkOrder : operationAssembleWorkOrderList) {
                        List<MtRouterStep> mtRouterStepList = routerStepMap.get(mtWorkOrder.getRouterId());
                        if (CollectionUtils.isNotEmpty(mtRouterStepList)) {
                            mtRouterStepList.sort(Comparator.comparing(MtRouterStep::getSequence).reversed());
                            workOrderRouterStepMap.put(mtWorkOrder.getWorkOrderId(),
                                            mtRouterStepList.get(0).getRouterStepId());
                        }
                    }
                }
            }
        }

        // 查询wo是否强制装配
        List<MtWorkOrderVO27> queryAssembleAsReqList = new ArrayList<>();
        for (MtAssembleProcessActualVO14 wo : woInfo) {
            for (MtAssembleProcessActualVO15 materialInfo : wo.getMaterialInfo()) {
                MtWorkOrderVO27 workOrderVO27 = new MtWorkOrderVO27();
                workOrderVO27.setWorkOrderId(wo.getWorkOrderId());
                workOrderVO27.setOperationId(dto.getOperationId());
                if (MtIdHelper.isIdNotNull(materialInfo.getBomComponentId())) {
                    workOrderVO27.setMaterialId(materialInfo.getMaterialId());
                    workOrderVO27.setComponentType(materialInfo.getBomComponentType());
                } else {
                    workOrderVO27.setMaterialId(materialInfo.getMaterialId());
                    workOrderVO27.setComponentType("ASSEMBLING");
                    // 设置传入的装配清单类型为查询一致，用于后面匹配查询结果
                    materialInfo.setBomComponentType("ASSEMBLING");
                }
                queryAssembleAsReqList.add(workOrderVO27);
            }
        }

        // 记录wo对应强制装配标识/装配清单
        Map<MtWorkOrderVO27, String> woAssembleExcessFlagMap = new HashMap<>();
        Map<MtWorkOrderVO27, String> woComponentIdMap = new HashMap<>();
        List<MtWorkOrderVO68> woBomComponentList =
                        mtWorkOrderRepository.woMaterialLimitComponentBatchQuery(tenantId, queryAssembleAsReqList);

        for (MtWorkOrderVO68 woBomComponent : woBomComponentList) {
            String assembleExcessFlag = MtBaseConstants.NO;
            String bomComponentId = null;
            if (CollectionUtils.isEmpty(woBomComponent.getBomComponentList())) {
                assembleExcessFlag = MtBaseConstants.YES;
            } else if (woBomComponent.getBomComponentList().size() == 1) {
                bomComponentId = woBomComponent.getBomComponentList().get(0).getBomComponentId();
            } else {
                woBomComponent.getBomComponentList().sort(Comparator.comparing(MtWorkOrderVO69::getLineNumber));
                bomComponentId = woBomComponent.getBomComponentList().get(0).getBomComponentId();
            }

            woAssembleExcessFlagMap.put(woBomComponent.getWoInfo(), assembleExcessFlag);
            if (bomComponentId != null) {
                woComponentIdMap.put(woBomComponent.getWoInfo(), bomComponentId);
            }
        }

        // 处理生产指令组件实绩
        MtWoComponentActualVO33 woComponentActual = new MtWoComponentActualVO33();
        List<MtWoComponentActualVO32> woComponentActualVO32s = new ArrayList<>();
        woComponentActual.setWorkcellId(dto.getWorkcellId());
        woComponentActual.setLocatorId(dto.getLocatorId());
        woComponentActual.setParentEventId(dto.getParentEventId());
        woComponentActual.setEventRequestId(dto.getEventRequestId());
        woComponentActual.setShiftCode(dto.getShiftCode());
        woComponentActual.setShiftDate(dto.getShiftDate());
        woComponentActual.setCheckComponentTypeFlag(dto.getCheckComponentTypeFlag());
        woComponentActual.setEventId(dto.getEventId());
        woComponentActual.setOperationId(dto.getOperationId());
        for (MtAssembleProcessActualVO14 wo : woInfo) {
            MtWoComponentActualVO32 woComponentActualVO32 = new MtWoComponentActualVO32();
            woComponentActualVO32.setAssembleRouterType(woRouterTypeMap.get(wo.getWorkOrderId()));
            woComponentActualVO32.setWorkOrderId(wo.getWorkOrderId());
            woComponentActualVO32.setRouterStepId(workOrderRouterStepMap.get(wo.getWorkOrderId()));
            woComponentActualVO32.setOperationAssembleFlag(wo.getOperationAssembleFlag());
            List<MtEoComponentActualVO29> woComponentActualVO29s = new ArrayList<>();
            for (MtAssembleProcessActualVO15 actualVO15 : wo.getMaterialInfo()) {
                MtWorkOrderVO27 workOrderVO27 = new MtWorkOrderVO27(wo.getWorkOrderId(), actualVO15.getMaterialId(),
                                actualVO15.getBomComponentType(), dto.getOperationId());

                MtEoComponentActualVO29 woComponentActualVO29 = new MtEoComponentActualVO29();
                woComponentActualVO29.setAssembleExcessFlag(woAssembleExcessFlagMap.get(workOrderVO27));
                woComponentActualVO29.setSubstituteFlag(actualVO15.getSubstituteFlag());
                woComponentActualVO29.setBomComponentId(woComponentIdMap.get(workOrderVO27));
                woComponentActualVO29.setMaterialId(actualVO15.getMaterialId());
                woComponentActualVO29.setTrxAssembleQty(actualVO15.getTrxAssembleQty());
                woComponentActualVO29s.add(woComponentActualVO29);
            }
            woComponentActualVO32.setMaterialInfo(woComponentActualVO29s);
            woComponentActualVO32s.add(woComponentActualVO32);
        }
        woComponentActual.setWoInfo(woComponentActualVO32s);

        mtWorkOrderComponentActualRepository.woComponentBatchAssemble(tenantId, woComponentActual);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void componentAssembleBatchProcess(Long tenantId, MtAssembleProcessActualVO16 dto) {
        String apiName = "【API:componentAssembleBatchProcess】";

        List<MtAssembleProcessActualVO17> eoInfoList = dto.getEoInfo();
        if (CollectionUtils.isEmpty(eoInfoList)) {

            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoInfo", apiName));
        }

        List<String> bomComponentIds = new ArrayList<>();
        Set<String> eoIdSet = new HashSet<>(eoInfoList.size());
        eoInfoList.forEach(t -> {
            // 判断eoId是否输入
            if (MtIdHelper.isIdNull(t.getEoId())) {

                throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", apiName));
            }
            // 判断eoId是否有重复
            if (!eoIdSet.add(t.getEoId())) {
                throw new MtException("MT_MOVING_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0072", "MOVING", "eoId", apiName));
            }

            // 判断组件信息是否输入
            List<MtAssembleProcessActualVO11> materialInfoList = t.getMaterialInfo();
            if (CollectionUtils.isEmpty(materialInfoList)) {
                throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,

                                "MT_ASSEMBLE_0001", "ASSEMBLE", "materialInfo", apiName));
            }
            materialInfoList.forEach(m -> {
                // 判断事务装配数量是否输入
                if (m.getTrxAssembleQty() == null) {

                    throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE", "trxAssembleQty", apiName));
                }
                // 判断数量是否小于0
                if (BigDecimal.valueOf(m.getTrxAssembleQty()).compareTo(BigDecimal.ZERO) <= 0) {
                    throw new MtException("MT_ASSEMBLE_0007", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0007", "ASSEMBLE", "trxAssembleQty", apiName));
                }
                if(MtIdHelper.isIdNotNull(m.getBomComponentId())) {
                    bomComponentIds.add(m.getBomComponentId());
                }
            });
        });
        // 获取装配组件信息
        Map<String, MtBomComponentVO13> bomComponentMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(bomComponentIds)) {
            List<String> distinctBomComponentIds = bomComponentIds.stream().distinct().collect(Collectors.toList());
            List<MtBomComponentVO13> bomComponentList =
                            mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, distinctBomComponentIds);
            if (distinctBomComponentIds.size() != bomComponentList.size()) {
                distinctBomComponentIds.removeAll(bomComponentList.stream().map(MtBomComponentVO13::getBomComponentId)
                                .collect(Collectors.toList()));
                throw new MtException("MT_ASSEMBLE_0071", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,

                                "MT_ASSEMBLE_0071", "ASSEMBLE", distinctBomComponentIds.toString(), apiName));
            }
            bomComponentMap = bomComponentList.stream()
                            .collect(Collectors.toMap(MtBomComponentVO13::getBomComponentId, t -> t));
        }

        List<String> eoIds = new ArrayList<>(eoIdSet);

        // 获取woId
        List<MtEo> eoList = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
        Map<String, String> woIdMap = eoList.stream().collect(Collectors.toMap(MtEo::getEoId, MtEo::getWorkOrderId));
        Map<String, List<String>> woEoIdMap = eoList.stream().collect(Collectors.groupingBy(MtEo::getWorkOrderId,
                        Collectors.mapping(MtEo::getEoId, Collectors.toList())));

        // 事务装配数量
        Map<MtAssembleProcessActualVO10.EoTuple, BigDecimal> sumTrxAssembleQtyMap = new HashMap<>();

        Map<String, MtBomComponentVO13> finalbomComponentMap = bomComponentMap;
        eoInfoList.forEach(t -> {
            String woId = woIdMap.get(t.getEoId());
            t.getMaterialInfo().forEach(m -> {

                MtBomComponentVO13 bomComponent = null;
                if (MtIdHelper.isIdNotNull(m.getBomComponentId())) {
                    bomComponent = finalbomComponentMap.get(m.getBomComponentId());
                    // 如果组件类型不为装配，直接报错
                    if (!"ASSEMBLING".equalsIgnoreCase(bomComponent.getBomComponentType())) {

                        throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0105", "ORDER", "ASSEMBLING", apiName));
                    }
                    if (MtIdHelper.isIdNull(m.getLocatorId())) {
                        m.setLocatorId(bomComponent.getIssuedLocatorId());
                    }
                }
                String bomComponentType = bomComponent == null ? "" : bomComponent.getBomComponentType();

                // 按照woId,materialId,bomComponentType,operationId分组汇总数量
                MtAssembleProcessActualVO10.EoTuple eoTuple = new MtAssembleProcessActualVO10.EoTuple(woId,
                                m.getMaterialId(), bomComponentType, dto.getOperationId());
                BigDecimal sumTrxAssembleQty = sumTrxAssembleQtyMap.get(eoTuple);
                if (sumTrxAssembleQty == null) {
                    sumTrxAssembleQty = BigDecimal.valueOf(m.getTrxAssembleQty());
                } else {
                    sumTrxAssembleQty = sumTrxAssembleQty.add(BigDecimal.valueOf(m.getTrxAssembleQty()));
                }
                sumTrxAssembleQtyMap.put(eoTuple, sumTrxAssembleQty);


                // 设置替代标识
                if (m.getSubstituteFlag() == null) {
                    // 如果为传入BomComponentId，设置为N
                    if (MtIdHelper.isIdNull(m.getBomComponentId())) {
                        m.setSubstituteFlag("");
                    } else {
                        // 如果传入BomComponentId且未传入materialId，设置为N
                        if (MtIdHelper.isIdNull(m.getMaterialId())) {
                            m.setSubstituteFlag(MtBaseConstants.NO);
                        } else {
                            // 如果传入的materialId与组件上的materialId一致，则为N，否则为Y
                            if (MtIdHelper.isNotSame(m.getMaterialId(), bomComponent.getMaterialId())) {
                                m.setSubstituteFlag(MtBaseConstants.YES);
                            } else {
                                m.setSubstituteFlag(MtBaseConstants.NO);
                            }
                        }

                    }
                }
            });
        });

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("COMPONENT_ASSEMBLE");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftDate(dto.getShiftDate() == null ? null
                        : Date.from(dto.getShiftDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        eventCreateVO.setShiftCode(dto.getShiftCode());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 获取EO按工序装配标识
//        Map<String, String> operationAssembleFlagMap = mtEoRepository.eoOperationAssembleFlagBatchGet(tenantId, eoIds)
//                        .stream().collect(Collectors.toMap(MtEoVO49::getEoId, MtEoVO49::getOperationAssembleFlag));
        //V20201211 modify by penglin.sui for hui.ma 锐科全部按工序装配
        Map<String, String> operationAssembleFlagMap = new HashMap<>(eoIds.size());
        eoIds.forEach(item ->{
            operationAssembleFlagMap.put(item,MtBaseConstants.YES);
        });
        MtAssembleProcessActualVO9 mtAssembleProcessActualVO9 =
                        mtAssembleProcessActualTransMapper.actualVO16ToActualVO9(dto);
        mtAssembleProcessActualVO9.setEventId(eventId);
        mtAssembleProcessActualVO9.setCheckComponentTypeFlag(MtBaseConstants.NO);
        mtAssembleProcessActualVO9.getEoInfo().forEach(t -> t.setOperationAssembleFlag(
                        MtFieldsHelper.getOrDefault(operationAssembleFlagMap.get(t.getEoId()), MtBaseConstants.NO)));

        // 处理执行作业装配过程实绩和执行作业装配确认实绩
        self().eoComponentActualAssembleBatchProcess(tenantId, mtAssembleProcessActualVO9);

        // 处理执行作业组件实绩
        MtEoComponentActualVO31 eoComponentActualVO31 =
                        mtAssembleProcessActualTransMapper.actualVO16ToEoComponentActualVO31(dto);
        eoComponentActualVO31.setEventId(eventId);
        eoComponentActualVO31.setCheckComponentTypeFlag(MtBaseConstants.NO);
        eoComponentActualVO31.getEoInfo().forEach(t -> t.setOperationAssembleFlag(
                        MtFieldsHelper.getOrDefault(operationAssembleFlagMap.get(t.getEoId()), MtBaseConstants.NO)));

        mtEoComponentActualRepository.eoComponentBatchAssemble(tenantId, eoComponentActualVO31);

        // 调用eoAssembleLimitWoBatchAssemble,并处理返回结果
        MtAssembleProcessActualVO18 mtAssembleProcessActualVO18 = new MtAssembleProcessActualVO18();
        mtAssembleProcessActualVO18.setCheckComponentTypeFlag(MtBaseConstants.NO);
        mtAssembleProcessActualVO18.setEventId(eventId);
        mtAssembleProcessActualVO18.setEventRequestId(dto.getEventRequestId());
        mtAssembleProcessActualVO18.setOperationBy(dto.getOperationBy());
        mtAssembleProcessActualVO18.setOperationId(dto.getOperationId());
        mtAssembleProcessActualVO18.setParentEventId(dto.getParentEventId());
        mtAssembleProcessActualVO18.setShiftCode(dto.getShiftCode());
        mtAssembleProcessActualVO18.setShiftDate(dto.getShiftDate());
        mtAssembleProcessActualVO18.setWorkcellId(dto.getWorkcellId());

        Map<String, MtAssembleProcessActualVO14> woInfoMap = new HashMap<>(finalbomComponentMap.size());
        for (Map.Entry<MtAssembleProcessActualVO10.EoTuple, BigDecimal> sumQtyEntry : sumTrxAssembleQtyMap.entrySet()) {
            MtAssembleProcessActualVO10.EoTuple eoTuple = sumQtyEntry.getKey();
            MtAssembleProcessActualVO14 woInfo = woInfoMap.get(eoTuple.getWorkOrderId());
            if (woInfo == null) {
                woInfo = new MtAssembleProcessActualVO14();
                woInfo.setWorkOrderId(eoTuple.getWorkOrderId());
                List<String> eoIdList = woEoIdMap.get(woInfo.getWorkOrderId());
                String operationAssembleFlag = null;
                for (String eoId : eoIdList) {
                    String tmpOperationAssembleFlag = operationAssembleFlagMap.get(eoId);
                    if (operationAssembleFlag == null) {
                        operationAssembleFlag = tmpOperationAssembleFlag;
                    } else {
                        // 同一WO下工序装配标识不一致
                        if (!operationAssembleFlag.equalsIgnoreCase(tmpOperationAssembleFlag)) {
                            throw new MtException("MT_ASSEMBLE_0084",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_ASSEMBLE_0084", "ASSEMBLE",
                                                            eoTuple.getWorkOrderId() + "", apiName));
                        }
                    }
                }
                woInfo.setOperationAssembleFlag(operationAssembleFlag);
                woInfo.setMaterialInfo(new ArrayList<>());
                woInfoMap.put(woInfo.getWorkOrderId(), woInfo);

            }
            List<MtAssembleProcessActualVO15> materialInfoList = woInfo.getMaterialInfo();
            MtAssembleProcessActualVO15 materialInfo = new MtAssembleProcessActualVO15();
            materialInfo.setMaterialId(eoTuple.getMaterialId());
            materialInfo.setTrxAssembleQty(sumQtyEntry.getValue().doubleValue());
            materialInfo.setBomComponentType(eoTuple.getComponentType());
            materialInfoList.add(materialInfo);
        }
        mtAssembleProcessActualVO18.setWoInfo(new ArrayList<>(woInfoMap.values()));

        self().eoAssembleLimitWoBatchAssemble(tenantId, mtAssembleProcessActualVO18);
    }
}
