package tarzan.actual.infra.repository.impl;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.sys.SequenceInfo;
import io.tarzan.common.domain.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.domian.SecurityToken;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.opensaml.xml.security.SecurityHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtWorkOrderCompActualHis;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.repository.MtWorkOrderCompActualHisRepository;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.trans.MtWoComponentActualTransMapper;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtWorkOrderComponentActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.material.domain.entity.MtPfepInventory;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtPfepInventoryRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.*;
import tarzan.modeling.domain.entity.MtModProdLineManufacturing;
import tarzan.modeling.domain.repository.MtModProdLineManufacturingRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtWorkOrderVO7;
import tarzan.order.domain.vo.MtWorkOrderVO8;

import static io.tarzan.common.domain.util.MtBaseConstants.STRING_SPECIAL;

/**
 * 生产订单组件装配实绩，记录生产订单物料和组件实际装配情况 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
@Slf4j
public class MtWorkOrderComponentActualRepositoryImpl extends BaseRepositoryImpl<MtWorkOrderComponentActual>
                implements MtWorkOrderComponentActualRepository {


    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtPfepInventoryRepository mtPfepInventoryRepository;

    @Autowired
    private MtBomSubstituteGroupRepository mtBomSubstituteGroupRepository;

    @Autowired
    private MtBomSubstituteRepository mtBomSubstituteRepository;

    @Autowired
    private MtWorkOrderComponentActualMapper mtWorkOrderComponentActualMapper;

    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;

    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtWorkOrderCompActualHisRepository mtWorkOrderCompActualHisRepository;

    @Autowired
    private MtModProdLineManufacturingRepository mtModProdLineManufacturingRepository;

    @Autowired
    private MtBomReferencePointRepository mtBomReferencePointRepository;

    @Autowired
    private MtRouterOperationComponentRepository mtRouterOperationComponentRepository;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtWoComponentActualTransMapper workOrderComponentActualTranMapper;

    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;

    @Override
    public String woComponentAssembleLocatorGet(Long tenantId, MtWoComponentActualVO18 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woComponentAssembleLocatorGet】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "materialId", "【API:woComponentAssembleLocatorGet】"));
        }

        MtWorkOrder mtWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woComponentAssembleLocatorGet】"));
        }

        String bomId = mtWorkOrder.getBomId();
        String siteId = mtWorkOrder.getSiteId();
        String productionLineId = mtWorkOrder.getProductionLineId();
        String routerId = mtWorkOrder.getRouterId();
        String issuedLocatorId = null;

        if (StringUtils.isNotEmpty(bomId) && StringUtils.isNotEmpty(routerId)) {
            MtRouterOpComponentVO3 condition = new MtRouterOpComponentVO3();
            condition.setBomId(bomId);
            condition.setMaterialId(dto.getMaterialId());
            if (StringUtils.isEmpty(dto.getComponentType())) {
                condition.setComponentType("ASSEMBLING");
            } else {
                condition.setComponentType(dto.getComponentType());
            }
            condition.setRouterId(routerId);
            condition.setOperationId(dto.getOperationId());

            List<String> bomComponentIds = this.mtRouterOperationComponentRepository
                            .operationOrMaterialLimitBomComponentQuery(tenantId, condition);
            if (CollectionUtils.isNotEmpty(bomComponentIds)) {
                // 第四步，根据装配清单组件行ID获取发料库位
                List<MtBomComponentVO13> bomComponents =
                                this.mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIds);
                if (CollectionUtils.isNotEmpty(bomComponents)) {

                    List<String> issuedLocatorIds =
                                    bomComponents.stream().filter(t -> !"".equals(t.getIssuedLocatorId()))
                                                    .map(MtBomComponentVO13::getIssuedLocatorId).distinct()
                                                    .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(issuedLocatorIds) && issuedLocatorIds.size() == 1) {
                        issuedLocatorId = bomComponents.get(0).getIssuedLocatorId();
                    }
                }
            }
        }

        if (StringUtils.isNotEmpty(issuedLocatorId)) {
            return issuedLocatorId;
        }

        // 7. 第六步，根据生产指令对应生产线和物料获取物料PFEP属性中组件发料库位
        MtPfepInventoryVO q = new MtPfepInventoryVO();
        q.setOrganizationType("PRODUCTIONLINE");
        q.setOrganizationId(productionLineId);
        q.setSiteId(siteId);
        q.setMaterialId(dto.getMaterialId());
        MtPfepInventory mtPfepInventory = mtPfepInventoryRepository.pfepDefaultManufacturingLocationGet(tenantId, q);
        if (mtPfepInventory != null && StringUtils.isNotEmpty(mtPfepInventory.getIssuedLocatorId())) {
            return mtPfepInventory.getIssuedLocatorId();
        }

        MtModProdLineManufacturing mtModProdLineManufacturing = mtModProdLineManufacturingRepository
                        .prodLineManufacturingPropertyGet(tenantId, mtWorkOrder.getProductionLineId());
        if (null != mtModProdLineManufacturing) {
            issuedLocatorId = mtModProdLineManufacturing.getIssuedLocatorId();
        }
        return issuedLocatorId;
    }

    @Override
    public List<MtBomSubstituteVO7> woComponentSubstituteQuery(Long tenantId, MtWoComponentActualVO17 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woComponentSubstituteQuery】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "materialId", "【API:woComponentSubstituteQuery】"));
        }

        MtWorkOrder mtWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (null == mtWorkOrder || StringUtils.isEmpty(mtWorkOrder.getBomId())) {
            return Collections.emptyList();
        }

        MtBomComponentVO bomComponentVO = new MtBomComponentVO();
        bomComponentVO.setBomId(mtWorkOrder.getBomId());
        bomComponentVO.setMaterialId(dto.getMaterialId());
        if (StringUtils.isNotEmpty(dto.getComponentType())) {
            bomComponentVO.setBomComponentType(dto.getComponentType());
        } else {
            bomComponentVO.setBomComponentType("ASSEMBLING");
        }
        bomComponentVO.setOnlyAvailableFlag("Y");
        List<MtBomComponentVO16> listMap =
                        this.mtBomComponentRepository.propertyLimitBomComponentQuery(tenantId, bomComponentVO);
        if (CollectionUtils.isEmpty(listMap)) {
            return Collections.emptyList();
        }

        List<String> bomComponentIds = listMap.get(0).getBomComponentId();
        bomComponentIds = bomComponentIds.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bomComponentIds)) {
            return Collections.emptyList();
        }

        Map<String, List<MtBomSubstituteGroupVO3>> bomSubstituteGroupMap =
                        new HashMap<String, List<MtBomSubstituteGroupVO3>>();
        for (String bomComponentId : bomComponentIds) {
            List<MtBomSubstituteGroupVO3> list =
                            mtBomSubstituteGroupRepository.bomSubstituteQuery(tenantId, bomComponentId);
            bomSubstituteGroupMap.put(bomComponentId, list);
        }
        if (MapUtils.isEmpty(bomSubstituteGroupMap)) {
            return Collections.emptyList();
        }

        List<MtBomSubstituteVO7> result = new ArrayList<MtBomSubstituteVO7>();
        for (Entry<String, List<MtBomSubstituteGroupVO3>> entry : bomSubstituteGroupMap.entrySet()) {
            String bomComponentId = entry.getKey();
            List<MtBomSubstituteGroupVO3> bomSubstituteGroupList = entry.getValue();

            MtBomSubstituteVO7 bomSubstituteVO7 = null;
            for (MtBomSubstituteGroupVO3 vo3 : bomSubstituteGroupList) {
                bomSubstituteVO7 = new MtBomSubstituteVO7();
                MtBomSubstitute mtBomSubstitute =
                                mtBomSubstituteRepository.bomSubstituteBasicGet(tenantId, vo3.getBomSubstituteId());
                MtBomSubstituteGroup mtBomSubstituteGroup = mtBomSubstituteGroupRepository
                                .bomSubstituteGroupBasicGet(tenantId, vo3.getBomSubstituteGroupId());

                bomSubstituteVO7.setBomComponentId(bomComponentId);
                bomSubstituteVO7.setBomSubstituteGroupId(vo3.getBomSubstituteGroupId());
                bomSubstituteVO7.setBomSubstituteId(vo3.getBomSubstituteId());
                if (mtBomSubstitute != null) {
                    bomSubstituteVO7.setMaterialId(mtBomSubstitute.getMaterialId());
                    bomSubstituteVO7.setSubstituteValue(mtBomSubstitute.getSubstituteValue());
                    bomSubstituteVO7.setSubstituteUsage(mtBomSubstitute.getSubstituteUsage());
                }
                if (mtBomSubstituteGroup != null) {
                    bomSubstituteVO7.setSubstitutePolicy(mtBomSubstituteGroup.getSubstitutePolicy());
                }
                result.add(bomSubstituteVO7);
            }
        }

        return result.stream()
                        .sorted(Comparator.comparing((MtBomSubstituteVO7 c) -> Double.valueOf(
                                        StringUtils.isEmpty(c.getBomComponentId()) ? "0" : c.getBomComponentId()))
                                        .thenComparing((MtBomSubstituteVO7 c) -> Double
                                                        .valueOf(StringUtils.isEmpty(c.getBomSubstituteGroupId()) ? "0"
                                                                        : c.getBomSubstituteGroupId()))
                                        .thenComparing((MtBomSubstituteVO7 c) -> Double
                                                        .valueOf(StringUtils.isEmpty(c.getBomSubstituteId()) ? "0"
                                                                        : c.getBomSubstituteId())))
                        .collect(Collectors.toList());
    }

    @Override
    public void woComponentIsAssembledVerify(Long tenantId, MtWoComponentActualVO27 dto) {

        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woComponentIsAssembledVerify】"));
        }

        if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getBomComponentId())) {
            throw new MtException("MT_ORDER_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032", "ORDER",
                                            "bomComponentId、materialId", "【API:woComponentIsAssembledVerify】"));
        }

        if (StringUtils.isEmpty(dto.getBomComponentId()) && StringUtils.isEmpty(dto.getComponentType())) {
            throw new MtException("MT_ORDER_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032", "ORDER",
                                            "bomComponentId、componentType", "【API:woComponentIsAssembledVerify】"));
        }

        MtWorkOrder mtWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woComponentIsAssembledVerify】"));
        }

        String bomId = dto.getBomId();
        String dtoBomId = mtWorkOrder.getBomId();
        if (StringUtils.isEmpty(bomId)) {
            dto.setBomId(dtoBomId);
        }

        List<MtWorkOrderComponentActual> wcas = mtWorkOrderComponentActualMapper.queryWoComponentActual(tenantId, dto);
        String msg = null;
        if (CollectionUtils.isEmpty(wcas)) {
            msg = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0103", "ORDER",
                            "【API:woComponentIsAssembledVerify】");
            throw new MtException("MT_ORDER_0103", msg);
        } else {
            if (wcas.size() > 1) {
                msg = "";
                if (StringUtils.isEmpty(dto.getRouterStepId())) {
                    msg = "routerStepId";
                }
                if (StringUtils.isEmpty(dto.getOperationId())) {
                    if (StringUtils.isNotEmpty(msg)) {
                        msg += "、operationId";
                    } else {
                        msg = "operationId";
                    }
                }
                if (StringUtils.isEmpty(dto.getRouterStepId()) || StringUtils.isEmpty(dto.getOperationId())) {
                    throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0032", "ORDER", msg, "【API:woComponentIsAssembledVerify】"));
                }
            } else {
                MtWorkOrderComponentActual wca = wcas.get(0);
                if (StringUtils.isNotEmpty(wca.getRouterStepId()) && StringUtils.isEmpty(dto.getRouterStepId())) {
                    throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0032", "ORDER", "routerStepId", "【API:woComponentIsAssembledVerify】"));
                }
                if (StringUtils.isNotEmpty(wca.getOperationId()) && StringUtils.isEmpty(dto.getOperationId())) {
                    throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0032", "ORDER", "operationId", "【API:woComponentIsAssembledVerify】"));
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woComponentAssemble(Long tenantId, MtWoComponentActualVO1 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woComponentAssemble】"));
        }
        if (dto.getTrxAssembleQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxAssembleQty", "【API:woComponentAssemble】"));
        }
        if (BigDecimal.valueOf(dto.getTrxAssembleQty()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxAssembleQty", "【API:woComponentAssemble】"));
        }

        String operationAssembleFlag = mtWorkOrderRepository.woOperationAssembleFlagGet(tenantId, dto.getWorkOrderId());

        MtWoComponentActualVO2 cv2 = new MtWoComponentActualVO2();
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        String eventId = null;
        String eventTypeCode = null;
        MtBomComponentVO8 ecv08 = null;

        if (StringUtils.isEmpty(operationAssembleFlag) || "N".equals(operationAssembleFlag)) {
            if (StringUtils.isEmpty(dto.getAssembleExcessFlag()) || "N".equals(dto.getAssembleExcessFlag())) {
                // 非强制装配模式下不按照工序进行装配
                if (StringUtils.isEmpty(dto.getBomComponentId())) {
                    throw new MtException("MT_ORDER_0104", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0104", "ORDER", "【API:woComponentAssemble】"));
                }

                ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
                if (ecv08 == null || !"ASSEMBLING".equals(ecv08.getBomComponentType())) {
                    throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:woComponentAssemble】"));
                }

                // 3. 生成事件 并 记录事件与对象关系
                eventTypeCode = "WO_COMPONENT_ASSEMBLE";
                eventCreateVO.setEventTypeCode(eventTypeCode);
                eventCreateVO.setParentEventId(dto.getParentEventId());
                eventCreateVO.setEventRequestId(dto.getEventRequestId());
                eventCreateVO.setWorkcellId(dto.getWorkcellId());
                eventCreateVO.setLocatorId(dto.getLocatorId());
                eventCreateVO.setShiftCode(dto.getShiftCode());
                eventCreateVO.setShiftDate(dto.getShiftDate());

                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                cv2.setWorkOrderId(dto.getWorkOrderId());
                cv2.setMaterialId(dto.getMaterialId());
                cv2.setBomComponentId(dto.getBomComponentId());
                cv2.setOperationId("");
                cv2.setRouterStepId("");
                cv2.setTrxAssembleQty(dto.getTrxAssembleQty());
                cv2.setComponentType("ASSEMBLING");
                cv2.setAssembleExcessFlag("N");
                cv2.setAssembleRouterType(dto.getAssembleRouterType());
                cv2.setEventId(eventId);
                this.woComponentActualUpdate(tenantId, cv2);
            } else if ("Y".equals(dto.getAssembleExcessFlag())) {
                // 若获取结果为空或为N，且输入参数assembleExcessFlag为Y，表示强制装配模式下不按照工序进行装配
                if (StringUtils.isEmpty(dto.getMaterialId())) {
                    throw new MtException("MT_ORDER_0106", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0106", "ORDER", "【API:woComponentAssemble】"));
                }

                // 3. 生成事件 并 记录事件与对象关系
                eventTypeCode = "WO_COMPONENT_EXCESS_ASSEMBLE";
                eventCreateVO.setEventTypeCode(eventTypeCode);
                eventCreateVO.setParentEventId(dto.getParentEventId());
                eventCreateVO.setEventRequestId(dto.getEventRequestId());
                eventCreateVO.setWorkcellId(dto.getWorkcellId());
                eventCreateVO.setLocatorId(dto.getLocatorId());
                eventCreateVO.setShiftCode(dto.getShiftCode());
                eventCreateVO.setShiftDate(dto.getShiftDate());
                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                cv2.setWorkOrderId(dto.getWorkOrderId());
                cv2.setMaterialId(dto.getMaterialId());
                cv2.setBomComponentId("");
                cv2.setOperationId("");
                cv2.setRouterStepId("");
                cv2.setTrxAssembleQty(dto.getTrxAssembleQty());
                cv2.setComponentType("ASSEMBLING");
                cv2.setAssembleExcessFlag("Y");
                cv2.setAssembleRouterType(dto.getAssembleRouterType());
                cv2.setEventId(eventId);
                this.woComponentActualUpdate(tenantId, cv2);
            }
        } else if ("Y".equals(operationAssembleFlag)) {
            if (StringUtils.isEmpty(dto.getAssembleExcessFlag()) || "N".equals(dto.getAssembleExcessFlag())) {
                // 表示非强制装配模式下按照工序进行装配
                if (StringUtils.isEmpty(dto.getBomComponentId())) {
                    throw new MtException("MT_ORDER_0104", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0104", "ORDER", "【API:woComponentAssemble】"));
                }
                if (StringUtils.isEmpty(dto.getRouterStepId())) {
                    throw new MtException("MT_ORDER_0107", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0107", "ORDER", "【API:woComponentAssemble】"));
                }
                ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
                if (ecv08 == null || !"ASSEMBLING".equals(ecv08.getBomComponentType())) {
                    throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:woComponentAssemble】"));
                }

                eventTypeCode = "WO_COMPONENT_OPERATION_ASSEMBLE";
                eventCreateVO.setEventTypeCode(eventTypeCode);
                eventCreateVO.setParentEventId(dto.getParentEventId());
                eventCreateVO.setEventRequestId(dto.getEventRequestId());
                eventCreateVO.setWorkcellId(dto.getWorkcellId());
                eventCreateVO.setLocatorId(dto.getLocatorId());
                eventCreateVO.setShiftCode(dto.getShiftCode());
                eventCreateVO.setShiftDate(dto.getShiftDate());
                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                cv2.setWorkOrderId(dto.getWorkOrderId());
                cv2.setMaterialId(dto.getMaterialId());
                cv2.setBomComponentId(dto.getBomComponentId());
                cv2.setRouterStepId(dto.getRouterStepId());
                cv2.setTrxAssembleQty(dto.getTrxAssembleQty());
                cv2.setComponentType("ASSEMBLING");
                cv2.setAssembleExcessFlag("N");
                cv2.setAssembleRouterType(dto.getAssembleRouterType());
                cv2.setEventId(eventId);
                this.woComponentActualUpdate(tenantId, cv2);
            } else if ("Y".equals(dto.getAssembleExcessFlag())) {
                // 若获取结果为Y，且输入参数assembleExcessFlag输入为Y，表示强制装配模式下按照工序进行装配
                if (StringUtils.isEmpty(dto.getMaterialId())) {
                    throw new MtException("MT_ORDER_0106", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0106", "ORDER", "【API:woComponentAssemble】"));
                }
                if (StringUtils.isEmpty(dto.getOperationId())) {
                    throw new MtException("MT_ORDER_0108", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0108", "ORDER", "【API:woComponentAssemble】"));
                }
                eventTypeCode = "WO_COMPONENT_EXCESS_OPERATION_ASSEMBLE";
                eventCreateVO.setEventTypeCode(eventTypeCode);
                eventCreateVO.setParentEventId(dto.getParentEventId());
                eventCreateVO.setEventRequestId(dto.getEventRequestId());
                eventCreateVO.setWorkcellId(dto.getWorkcellId());
                eventCreateVO.setLocatorId(dto.getLocatorId());
                eventCreateVO.setShiftCode(dto.getShiftCode());
                eventCreateVO.setShiftDate(dto.getShiftDate());
                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                cv2.setWorkOrderId(dto.getWorkOrderId());
                cv2.setMaterialId(dto.getMaterialId());
                cv2.setBomComponentId("");
                cv2.setOperationId(dto.getOperationId());
                // 强制装配 使用传入的routerStepId add by sanfeng.zhang 2020-12-25
                cv2.setRouterStepId(dto.getRouterStepId());
                cv2.setTrxAssembleQty(dto.getTrxAssembleQty());
                cv2.setComponentType("ASSEMBLING");
                cv2.setAssembleExcessFlag("Y");
                cv2.setAssembleRouterType(dto.getAssembleRouterType());
                cv2.setEventId(eventId);
                this.woComponentActualUpdate(tenantId, cv2);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woComponentActualUpdate(Long tenantId, MtWoComponentActualVO2 dto) {
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API:woComponentActualUpdate】"));
        }
        if (dto.getTrxAssembleQty() != null && dto.getAssembleQty() != null) {
            throw new MtException("MT_ORDER_0102", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0102", "ORDER", "assembleQty、trxAssembleQty", "【API:woComponentActualUpdate】"));
        }

        if (dto.getScrappedQty() != null && dto.getTrxScrappedQty() != null) {
            throw new MtException("MT_ORDER_0102", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0102", "ORDER", "scrappedQty、trxScrappedQty", "【API:woComponentActualUpdate】"));
        }

        if (StringUtils.isEmpty(dto.getWorkOrderId()) && StringUtils.isEmpty(dto.getWorkOrderComponentActualId())) {
            throw new MtException("MT_ORDER_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032", "ORDER",
                                            "workOrderComponentActualId、workOrderId", "【API:woComponentActualUpdate】"));
        }
        MtWorkOrderComponentActual mtWorkOrderComponentActual = new MtWorkOrderComponentActual();
        MtWorkOrder mtWorkOrder = null;
        Double trxAssembleQty = 0.0D;

        MtBomComponentVO8 ecv08 = null;
        // 根据输入参数 BomComponentId 获取BomComponent基础信息
        if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
            ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
        }

        Double trxScrappedQty = 0.0D;
        if (StringUtils.isEmpty(dto.getWorkOrderComponentActualId())) {
            mtWorkOrderComponentActual.setWorkOrderId(dto.getWorkOrderId());
            if (dto.getBomComponentId() != null) {
                mtWorkOrderComponentActual.setBomComponentId(dto.getBomComponentId());
            } else {
                mtWorkOrderComponentActual.setBomComponentId("");
            }
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                mtWorkOrderComponentActual.setMaterialId(dto.getMaterialId());
            } else {
                if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
                    if (ecv08 != null) {
                        mtWorkOrderComponentActual.setMaterialId(ecv08.getMaterialId());
                    } else {
                        mtWorkOrderComponentActual.setMaterialId("");
                    }
                } else {
                    throw new MtException("MT_ORDER_0032",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032",
                                                    "ORDER", "bomComponentId、materialId",
                                                    "【API:woComponentActualUpdate】"));
                }
            }

            if (StringUtils.isNotEmpty(dto.getComponentType())) {
                mtWorkOrderComponentActual.setComponentType(dto.getComponentType());
            } else {
                if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
                    if (ecv08 != null) {
                        mtWorkOrderComponentActual.setComponentType(ecv08.getBomComponentType());
                    } else {
                        mtWorkOrderComponentActual.setComponentType("");
                    }
                } else {
                    mtWorkOrderComponentActual.setComponentType("ASSEMBLING");
                }
            }

            if (dto.getBomId() != null) {
                mtWorkOrderComponentActual.setBomId(dto.getBomId());
            } else {
                mtWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
                if (mtWorkOrder == null) {
                    throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0006", "ORDER", "【API:woComponentActualUpdate】"));
                }
                mtWorkOrderComponentActual.setBomId(mtWorkOrder.getBomId());
            }

            if (dto.getRouterStepId() != null) {
                mtWorkOrderComponentActual.setRouterStepId(dto.getRouterStepId());
            } else {
                mtWorkOrderComponentActual.setRouterStepId("");
            }

            if (StringUtils.isNotEmpty(dto.getOperationId())) {
                mtWorkOrderComponentActual.setOperationId(dto.getOperationId());
            } else {
                if (StringUtils.isEmpty(dto.getRouterStepId())) {
                    mtWorkOrderComponentActual.setOperationId("");
                } else {
                    // 如果有输入，根据routerStep找routerOperation作为限定，找不到限定为空
                    MtRouterOperation mro =
                                    mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
                    if (mro == null) {
                        mtWorkOrderComponentActual.setOperationId("");
                    } else {
                        mtWorkOrderComponentActual.setOperationId(mro.getOperationId());
                    }
                }
            }

            Optional<MtWorkOrderComponentActual> first = this.mtWorkOrderComponentActualMapper
                            .selectForEmptyString(tenantId, mtWorkOrderComponentActual).stream().findFirst();
            if (first.isPresent()) {
                mtWorkOrderComponentActual = first.get();
            } else {
                mtWorkOrderComponentActual = null;
            }
        } else {
            mtWorkOrderComponentActual.setWorkOrderComponentActualId(dto.getWorkOrderComponentActualId());
            mtWorkOrderComponentActual.setWorkOrderId(dto.getWorkOrderId());
            mtWorkOrderComponentActual.setBomComponentId(dto.getBomComponentId());
            mtWorkOrderComponentActual.setMaterialId(dto.getMaterialId());
            mtWorkOrderComponentActual.setComponentType(dto.getComponentType());
            mtWorkOrderComponentActual.setOperationId(dto.getOperationId());
            mtWorkOrderComponentActual.setRouterStepId(dto.getRouterStepId());
            mtWorkOrderComponentActual.setBomId(dto.getBomId());
            Optional<MtWorkOrderComponentActual> first = this.mtWorkOrderComponentActualMapper
                            .selectForEmptyString(tenantId, mtWorkOrderComponentActual).stream().findFirst();
            if (!first.isPresent()) {
                throw new MtException("MT_ORDER_00101", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_00101", "ORDER", "【API:woComponentActualUpdate】"));
            }
            mtWorkOrderComponentActual = first.get();
        }

        // 更新
        if (mtWorkOrderComponentActual != null) {
            // 记录更新前装配数量
            Double oldAssembleQty = mtWorkOrderComponentActual.getAssembleQty();
            if (dto.getAssembleQty() != null) {
                mtWorkOrderComponentActual.setAssembleQty(dto.getAssembleQty());

                // 更新后-更新前
                trxAssembleQty = BigDecimal.valueOf(dto.getAssembleQty()).subtract(BigDecimal.valueOf(oldAssembleQty))
                                .doubleValue();
            }
            if (dto.getTrxAssembleQty() != null) {
                trxAssembleQty = dto.getTrxAssembleQty();
                mtWorkOrderComponentActual.setAssembleQty(BigDecimal.valueOf(oldAssembleQty)
                                .add(BigDecimal.valueOf(trxAssembleQty)).doubleValue());
            }

            // 记录更新前报废数量
            Double oldScrappedQty = mtWorkOrderComponentActual.getScrappedQty();
            if (dto.getScrappedQty() != null) {
                mtWorkOrderComponentActual.setScrappedQty(dto.getScrappedQty());

                trxScrappedQty = BigDecimal.valueOf(dto.getScrappedQty()).subtract(BigDecimal.valueOf(oldScrappedQty))
                                .doubleValue();
            }
            if (dto.getTrxScrappedQty() != null) {
                trxScrappedQty = dto.getTrxScrappedQty();
                mtWorkOrderComponentActual.setScrappedQty(BigDecimal.valueOf(oldScrappedQty)
                                .add(BigDecimal.valueOf(dto.getTrxScrappedQty())).doubleValue());
            }

            mtWorkOrderComponentActual.setAssembleExcessFlag(dto.getAssembleExcessFlag());
            mtWorkOrderComponentActual.setAssembleRouterType(dto.getAssembleRouterType());
            mtWorkOrderComponentActual.setSubstituteFlag(dto.getSubstituteFlag());

            if (dto.getActualFirstTime() != null) {
                mtWorkOrderComponentActual.setActualFirstTime(dto.getActualFirstTime());
            } else {
                if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(oldAssembleQty)) == 0 && ((dto.getAssembleQty() != null
                                && BigDecimal.ZERO.compareTo(new BigDecimal(dto.getAssembleQty().toString())) != 0)
                                || (dto.getTrxAssembleQty() != null && BigDecimal.ZERO
                                                .compareTo(new BigDecimal(dto.getTrxAssembleQty().toString())) != 0))
                                && mtWorkOrderComponentActual.getActualFirstTime() == null) {
                    mtWorkOrderComponentActual.setActualFirstTime(new Date());
                }
            }
            if (dto.getActualLastTime() != null) {
                mtWorkOrderComponentActual.setActualLastTime(dto.getActualLastTime());
            } else {
                if ((dto.getAssembleQty() != null
                                && BigDecimal.ZERO.compareTo(new BigDecimal(dto.getAssembleQty().toString())) != 0)
                                || (dto.getTrxAssembleQty() != null && BigDecimal.ZERO
                                                .compareTo(new BigDecimal(dto.getTrxAssembleQty().toString())) != 0)) {
                    mtWorkOrderComponentActual.setActualLastTime(new Date());
                }
            }

            mtWorkOrderComponentActual.setTenantId(tenantId);
            self().updateByPrimaryKeySelective(mtWorkOrderComponentActual);
        } else {
            // 新增
            mtWorkOrderComponentActual = new MtWorkOrderComponentActual();
            mtWorkOrderComponentActual.setWorkOrderId(dto.getWorkOrderId());
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                mtWorkOrderComponentActual.setMaterialId(dto.getMaterialId());
            } else {
                if (ecv08 == null || StringUtils.isEmpty(ecv08.getMaterialId())) {
                    throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0148", "ORDER", "【API:woComponentActualUpdate】"));
                }

                mtWorkOrderComponentActual.setMaterialId(ecv08.getMaterialId());
            }

            if (StringUtils.isNotEmpty(dto.getOperationId())) {
                mtWorkOrderComponentActual.setOperationId(dto.getOperationId());
            } else {
                if (StringUtils.isEmpty(dto.getRouterStepId())) {
                    mtWorkOrderComponentActual.setOperationId("");
                } else {
                    MtRouterOperation mro =
                                    mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
                    if (mro == null || StringUtils.isEmpty(mro.getOperationId())) {
                        throw new MtException("MT_ORDER_0149", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0149", "ORDER", "【API:woComponentActualUpdate】"));
                    }

                    mtWorkOrderComponentActual.setOperationId(mro.getOperationId());
                }
            }

            if (dto.getRouterStepId() != null) {
                mtWorkOrderComponentActual.setRouterStepId(dto.getRouterStepId());
            }


            if (dto.getAssembleQty() != null) {
                trxAssembleQty = BigDecimal.valueOf(dto.getAssembleQty())
                                .subtract(BigDecimal.valueOf(mtWorkOrderComponentActual.getAssembleQty() == null ? 0.0D
                                                : mtWorkOrderComponentActual.getAssembleQty()))
                                .doubleValue();
                mtWorkOrderComponentActual.setAssembleQty(dto.getAssembleQty());
            } else if (dto.getTrxAssembleQty() != null) {
                trxAssembleQty = dto.getTrxAssembleQty();
                mtWorkOrderComponentActual.setAssembleQty(dto.getTrxAssembleQty());
            } else {
                mtWorkOrderComponentActual.setAssembleQty(0.0D);
            }


            if (dto.getScrappedQty() != null) {
                trxScrappedQty = BigDecimal.valueOf(dto.getScrappedQty())
                                .subtract(BigDecimal.valueOf(mtWorkOrderComponentActual.getScrappedQty() == null ? 0.0D
                                                : mtWorkOrderComponentActual.getScrappedQty()))
                                .doubleValue();
                mtWorkOrderComponentActual.setScrappedQty(dto.getScrappedQty());
            } else if (dto.getTrxScrappedQty() != null) {
                trxScrappedQty = dto.getTrxScrappedQty();
                mtWorkOrderComponentActual.setScrappedQty(dto.getTrxScrappedQty());
            } else {
                mtWorkOrderComponentActual.setScrappedQty(0.0D);
            }


            if (StringUtils.isNotEmpty(dto.getComponentType())) {
                mtWorkOrderComponentActual.setComponentType(dto.getComponentType());
            } else {
                if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
                    if (ecv08 == null || StringUtils.isEmpty(ecv08.getBomComponentType())) {
                        throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0148", "ORDER", "【API:woComponentActualUpdate】"));
                    }

                    mtWorkOrderComponentActual.setComponentType(ecv08.getBomComponentType());
                } else {
                    mtWorkOrderComponentActual.setComponentType("ASSEMBLING");
                }
            }


            if (dto.getBomComponentId() != null) {
                mtWorkOrderComponentActual.setBomComponentId(dto.getBomComponentId());
            } else {
                mtWorkOrderComponentActual.setBomComponentId("");
            }


            if (dto.getBomId() != null) {
                mtWorkOrderComponentActual.setBomId(dto.getBomId());
            } else {
                mtWorkOrderComponentActual.setBomId(mtWorkOrder.getBomId());
            }

            if (dto.getAssembleExcessFlag() != null) {
                mtWorkOrderComponentActual.setAssembleExcessFlag(dto.getAssembleExcessFlag());
            } else {
                if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
                    mtWorkOrderComponentActual.setAssembleExcessFlag("N");
                } else {
                    mtWorkOrderComponentActual.setAssembleExcessFlag("Y");
                }
            }

            if (dto.getAssembleRouterType() != null) {
                mtWorkOrderComponentActual.setAssembleRouterType(dto.getAssembleRouterType());
            } else {
                mtWorkOrderComponentActual.setAssembleRouterType("");
            }

            if (dto.getSubstituteFlag() != null) {
                mtWorkOrderComponentActual.setSubstituteFlag(dto.getSubstituteFlag());
            } else {
                if (StringUtils.isEmpty(dto.getBomComponentId())) {
                    mtWorkOrderComponentActual.setSubstituteFlag("N");
                } else {
                    if (StringUtils.isEmpty(dto.getMaterialId())) {
                        mtWorkOrderComponentActual.setSubstituteFlag("N");
                    } else {
                        if (ecv08 == null) {
                            throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(
                                            tenantId, "MT_ORDER_0148", "ORDER", "【API:woComponentActualUpdate】"));
                        }

                        if (dto.getMaterialId().equals(ecv08.getMaterialId())) {
                            // 若获取结果bom_materialId与输入material一致，存入N
                            mtWorkOrderComponentActual.setSubstituteFlag("N");
                        } else {
                            // 若若获取结果bom_materialId与输入material不一致，存入Y
                            mtWorkOrderComponentActual.setSubstituteFlag("Y");
                        }
                    }
                }
            }

            if (dto.getActualFirstTime() != null) {
                mtWorkOrderComponentActual.setActualFirstTime(dto.getActualFirstTime());
            } else {
                // 若输入值assembleQty、trxAssembleQty不均为空或0，更新为当前系统时间
                // 若输入值assembleQty、trxAssembleQty均为空或0，不更新

                if ((dto.getAssembleQty() != null
                                && BigDecimal.ZERO.compareTo(new BigDecimal(dto.getAssembleQty().toString())) != 0)
                                || (dto.getTrxAssembleQty() != null && BigDecimal.ZERO
                                                .compareTo(new BigDecimal(dto.getTrxAssembleQty().toString())) != 0)) {
                    mtWorkOrderComponentActual.setActualFirstTime(new Date());
                }
            }
            if (dto.getActualLastTime() != null) {
                mtWorkOrderComponentActual.setActualLastTime(dto.getActualLastTime());
            } else {
                if ((dto.getAssembleQty() != null
                                && BigDecimal.ZERO.compareTo(new BigDecimal(dto.getAssembleQty().toString())) != 0)
                                || (dto.getTrxAssembleQty() != null && BigDecimal.ZERO
                                                .compareTo(new BigDecimal(dto.getTrxAssembleQty().toString())) != 0)) {
                    mtWorkOrderComponentActual.setActualLastTime(new Date());
                }
            }
            // 新增
            mtWorkOrderComponentActual.setTenantId(tenantId);
            self().insertSelective(mtWorkOrderComponentActual);
        }

        MtWorkOrderCompActualHis mtWorkOrderComponentActualHis = new MtWorkOrderCompActualHis();
        mtWorkOrderComponentActualHis.setEventId(dto.getEventId());

        MtWorkOrderComponentActual mtWorkOrderComponentActualTemp = new MtWorkOrderComponentActual();
        mtWorkOrderComponentActualTemp.setTenantId(tenantId);
        mtWorkOrderComponentActualTemp
                        .setWorkOrderComponentActualId(mtWorkOrderComponentActual.getWorkOrderComponentActualId());

        mtWorkOrderComponentActual = this.mtWorkOrderComponentActualMapper.selectOne(mtWorkOrderComponentActualTemp);

        mtWorkOrderComponentActualHis.setActualFirstTime(mtWorkOrderComponentActual.getActualFirstTime());
        mtWorkOrderComponentActualHis.setActualLastTime(mtWorkOrderComponentActual.getActualLastTime());
        mtWorkOrderComponentActualHis.setOperationId(mtWorkOrderComponentActual.getOperationId());
        mtWorkOrderComponentActualHis.setAssembleExcessFlag(mtWorkOrderComponentActual.getAssembleExcessFlag());
        mtWorkOrderComponentActualHis.setBomComponentId(mtWorkOrderComponentActual.getBomComponentId());
        mtWorkOrderComponentActualHis.setAssembleQty(mtWorkOrderComponentActual.getAssembleQty());
        mtWorkOrderComponentActualHis.setAssembleRouterType(mtWorkOrderComponentActual.getAssembleRouterType());
        mtWorkOrderComponentActualHis.setBomId(mtWorkOrderComponentActual.getBomId());
        mtWorkOrderComponentActualHis.setComponentType(mtWorkOrderComponentActual.getComponentType());
        mtWorkOrderComponentActualHis.setMaterialId(mtWorkOrderComponentActual.getMaterialId());
        mtWorkOrderComponentActualHis.setRouterStepId(mtWorkOrderComponentActual.getRouterStepId());
        mtWorkOrderComponentActualHis.setScrappedQty(mtWorkOrderComponentActual.getScrappedQty());
        mtWorkOrderComponentActualHis.setSubstituteFlag(mtWorkOrderComponentActual.getSubstituteFlag());
        mtWorkOrderComponentActualHis.setTrxAssembleQty(trxAssembleQty);
        mtWorkOrderComponentActualHis.setTrxScrappedQty(trxScrappedQty);
        mtWorkOrderComponentActualHis
                        .setWorkOrderComponentActualId(mtWorkOrderComponentActual.getWorkOrderComponentActualId());
        mtWorkOrderComponentActualHis.setWorkOrderId(mtWorkOrderComponentActual.getWorkOrderId());
        mtWorkOrderComponentActualHis.setTenantId(tenantId);
        // 记录历史
        mtWorkOrderCompActualHisRepository.insertSelective(mtWorkOrderComponentActualHis);
    }

    @Override
    public void woComponentRemoveVerify(Long tenantId, MtWoComponentActualVO3 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woComponentRemoveVerify】"));
        }
        if (dto.getTrxAssembleQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxAssembleQty", "【API:woComponentRemoveVerify】"));
        }
        if (new BigDecimal(dto.getTrxAssembleQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxAssembleQty", "【API:woComponentRemoveVerify】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getBomComponentId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0032", "ORDER", "materialId、bomComponentId", "【API:woComponentRemoveVerify】"));
        }
        MtBomComponentVO8 ecv08 = null;
        if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
            ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (ecv08 == null || !"REMOVE".equals(ecv08.getBomComponentType())) {
                throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105", "ORDER", "REMOVE", "【API:woComponentRemoveVerify】"));
            }
        }

        if (StringUtils.isEmpty(dto.getMaterialId())) {
            dto.setMaterialId(ecv08.getMaterialId());
        }
        MtWoComponentActualVO19 q2 = new MtWoComponentActualVO19();
        q2.setMaterialId(dto.getMaterialId());
        q2.setWorkOrderId(dto.getWorkOrderId());
        List<MtWoComponentActualVO4> v4s = materialLimitWoComponentAssembleActualQuery(tenantId, q2);
        BigDecimal sumAssembleQty = BigDecimal.ZERO;
        for (MtWoComponentActualVO4 v4 : v4s) {
            if ("ASSEMBLING".equals(v4.getComponentType()) && v4.getAssembleQty() != null) {
                sumAssembleQty = sumAssembleQty.add(BigDecimal.valueOf(v4.getAssembleQty()));
            } else if ("REMOVE".equals(v4.getComponentType()) && v4.getAssembleQty() != null) {
                sumAssembleQty = sumAssembleQty.subtract(BigDecimal.valueOf(v4.getAssembleQty()));
            }
        }
        if (sumAssembleQty.compareTo(BigDecimal.valueOf(dto.getTrxAssembleQty())) < 0) {
            throw new MtException("MT_ORDER_0128", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0128", "ORDER", "【API:woComponentRemoveVerify】"));
        }
        if (!"Y".equals(dto.getAssembleExcessFlag())) {
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ORDER_0109", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0109", "ORDER", "【API:woComponentRemoveVerify】"));
            } else {
                MtWoComponentActualVO20 q3 = new MtWoComponentActualVO20();
                q3.setWorkOrderId(dto.getWorkOrderId());
                q3.setRouterStepId(dto.getRouterStepId());
                List<MtWoComponentActualVO6> v6s = self().woUnassembledComponentQuery(tenantId, q3);
                BigDecimal sumUnassembleQty = BigDecimal.ZERO;
                if (v6s != null) {
                    for (MtWoComponentActualVO6 v6 : v6s) {
                        if (dto.getBomComponentId().equals(v6.getBomComponentId())
                                        && dto.getMaterialId().equals(v6.getMaterialId())
                                        && v6.getUnassembledQty() != null) {
                            sumUnassembleQty = sumUnassembleQty.add(BigDecimal.valueOf(v6.getUnassembledQty()));
                        }
                    }
                }
                if (sumUnassembleQty.compareTo(BigDecimal.valueOf(dto.getTrxAssembleQty())) < 0) {
                    throw new MtException("MT_ORDER_0129", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0129", "ORDER", "【API:woComponentRemoveVerify】"));
                }
            }
        }
    }

    @Override
    public List<MtWoComponentActualVO4> materialLimitWoComponentAssembleActualQuery(Long tenantId,
                    MtWoComponentActualVO19 dto) {
        List<MtWoComponentActualVO4> v4s = new ArrayList<MtWoComponentActualVO4>();
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001", "ORDER",
                                            "workOrderId", "【API:materialLimitWoComponentAssembleActualQuery】"));
        }

        MtWorkOrderComponentActual mtWorkOrderComponentActual = new MtWorkOrderComponentActual();
        mtWorkOrderComponentActual.setWorkOrderId(dto.getWorkOrderId());
        mtWorkOrderComponentActual.setMaterialId(dto.getMaterialId());
        mtWorkOrderComponentActual.setBomId(dto.getBomId());
        mtWorkOrderComponentActual.setOperationId(dto.getOperationId());
        mtWorkOrderComponentActual.setComponentType(dto.getComponentType());
        mtWorkOrderComponentActual.setTenantId(tenantId);
        List<MtWorkOrderComponentActual> list =
                        this.mtWorkOrderComponentActualMapper.select(mtWorkOrderComponentActual);
        process(v4s, list);
        return v4s;
    }

    @Override
    public List<MtWoComponentActualVO4> componentLimitWoComponentAssembleActualQuery(Long tenantId,
                    MtWoComponentActualVO5 dto) {
        List<MtWoComponentActualVO4> v4s = new ArrayList<MtWoComponentActualVO4>();
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001", "ORDER",
                                            "workOrderId", "【API:componentLimitWoComponentAssembleActualQuery】"));
        }
        List<MtWorkOrderComponentActual> wocas = this.mtWorkOrderComponentActualMapper
                        .componentLimitWoComponentAssembleActualQuery(tenantId, dto);
        process(v4s, wocas);
        return v4s;
    }

    @Override
    public List<MtWoComponentActualVO6> woUnassembledComponentQuery(Long tenantId, MtWoComponentActualVO20 dto) {
        List<MtWoComponentActualVO6> v6s = new ArrayList<MtWoComponentActualVO6>();
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woUnassembledComponentQuery】"));
        }
        MtWoComponentActualVO6 v6 = null;
        MtWorkOrderVO7 wo07 = new MtWorkOrderVO7();
        wo07.setWorkOrderId(dto.getWorkOrderId());
        wo07.setOperationId(dto.getOperationId());
        wo07.setRouterStepId(dto.getRouterStepId());

        // 第二步，获取生产指令组件和组件用量列表
        List<MtWorkOrderVO8> wo08s = this.mtWorkOrderRepository.woComponentQtyQuery(tenantId, wo07);

        // 如果第二步数据为空没必要进行后续对比，直接返回空即可
        if (CollectionUtils.isNotEmpty(wo08s)) {

            // 第三步，获取生产指令或生产指令在指定工艺已装配的组件实绩列表
            MtWoComponentActualVO5 v5 = new MtWoComponentActualVO5();
            v5.setWorkOrderId(dto.getWorkOrderId());
            v5.setOperationId(dto.getOperationId());
            v5.setRouterStepId(dto.getRouterStepId());
            List<MtWoComponentActualVO4> v4s = componentLimitWoComponentAssembleActualQuery(tenantId, v5);

            // 第三步结果汇总，获取结果按照bomComponentId、materialId进行分组,汇总得到sum_assembleQty
            Map<String, Map<String, BigDecimal>> assembleMap = v4s.stream().collect(Collectors.groupingBy(
                            MtWoComponentActualVO4::getBomComponentId,
                            Collectors.groupingBy(MtWoComponentActualVO4::getMaterialId,
                                            CollectorsUtil.summingBigDecimal(
                                                            (t -> BigDecimal.valueOf(t.getAssembleQty() == null ? 0.0D
                                                                            : t.getAssembleQty()))))));

            // 筛选存在与第二步结果中但是不存在于第三步结果的数据
            MtBomComponentVO8 ecv08 = null;
            for (MtWorkOrderVO8 wo08 : wo08s) {
                if (!assembleMap.containsKey(wo08.getBomComponentId())) {
                    ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId, wo08.getBomComponentId());
                    v6 = new MtWoComponentActualVO6();
                    v6.setMaterialId(wo08.getMaterialId());
                    v6.setBomComponentId(wo08.getBomComponentId());
                    v6.setUnassembledQty(wo08.getComponentQty());
                    if (ecv08 == null || StringUtils.isEmpty(ecv08.getBomComponentType())) {
                        v6.setComponentType("");
                    } else {
                        v6.setComponentType(ecv08.getBomComponentType());
                    }
                    v6s.add(v6);
                }
            }

            // 第五步，输入值unstartFlag为Y,直接结束API
            if (!"Y".equals(dto.getUnstartFlag())) {
                // 获取已开始装配但未完成装配的组件
                for (MtWorkOrderVO8 wo08 : wo08s) {
                    // 筛选既存在与第二步结果也存在与第三步结果(筛选数量不相等的数据这块逻辑变了，取消这个判断)
                    if (assembleMap.containsKey(wo08.getBomComponentId())) {
                        // 获取装配清单行对应的组件类型
                        ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId, wo08.getBomComponentId());
                        // 获得第三步结果的数据
                        Map<String, BigDecimal> componentMap = assembleMap.get(wo08.getBomComponentId());
                        // 第三步获取到的可能存在多个物料，循环每个进行判断(燊华修改逻辑)
                        for (Map.Entry<String, BigDecimal> entry : componentMap.entrySet()) {
                            BigDecimal sumAssembleQty = entry.getValue();
                            // 第二步对应的物料与第三步对应的物料一致
                            if (entry.getKey().equals(wo08.getMaterialId())) {
                                v6 = new MtWoComponentActualVO6();
                                v6.setMaterialId(wo08.getMaterialId());
                                v6.setBomComponentId(wo08.getBomComponentId());
                                v6.setUnassembledQty(BigDecimal.valueOf(wo08.getComponentQty()).subtract(sumAssembleQty)
                                                .doubleValue());
                                if (ecv08 == null || StringUtils.isEmpty(ecv08.getBomComponentType())) {
                                    v6.setComponentType("");
                                } else {
                                    v6.setComponentType(ecv08.getBomComponentType());
                                }
                                v6s.add(v6);
                            }
                            // 第二步对应的物料与第三步对应的物料不一致
                            else {
                                v6 = new MtWoComponentActualVO6();
                                v6.setMaterialId(entry.getKey());
                                v6.setBomComponentId(wo08.getBomComponentId());
                                if (ecv08 == null || StringUtils.isEmpty(ecv08.getBomComponentType())) {
                                    v6.setComponentType("");
                                } else {
                                    v6.setComponentType(ecv08.getBomComponentType());
                                }
                                // 获取替代件数量，使用替代件数量计算
                                MtBomSubstituteVO6 bomSubstituteVO6 = new MtBomSubstituteVO6();
                                bomSubstituteVO6.setBomComponentId(wo08.getBomComponentId());
                                bomSubstituteVO6.setQty(wo08.getComponentQty());
                                // 新增传入第3步获取的materialId(仅获取到一条数据)
                                bomSubstituteVO6.setMaterialId(entry.getKey());
                                List<MtBomSubstituteVO3> bomSubstitutes = this.mtBomSubstituteRepository
                                                .bomSubstituteQtyCalculate(tenantId, bomSubstituteVO6);
                                if (CollectionUtils.isNotEmpty(bomSubstitutes)) {
                                    v6.setUnassembledQty(BigDecimal
                                                    .valueOf(bomSubstitutes.get(0).getComponentQty() == null ? 0.0D
                                                                    : bomSubstitutes.get(0).getComponentQty())
                                                    .subtract(sumAssembleQty).doubleValue());
                                } else {
                                    v6.setUnassembledQty(-sumAssembleQty.doubleValue());
                                }
                                v6s.add(v6);
                            }
                        }
                    }
                }
            }
        }
        // 新增逻辑(最终返回unassembledQty不为0或不为null的数据)
        return v6s.stream().filter(t -> t.getUnassembledQty() != null
                        && new BigDecimal(t.getUnassembledQty().toString()).compareTo(BigDecimal.ZERO) != 0)
                        .collect(Collectors.toList());
    }

    @Override
    public List<MtWoComponentActualVO21> woComponentReferencePointQuery(Long tenantId, MtWoComponentActualVO18 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woComponentReferencePointQuery】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "materialId", "【API:woComponentReferencePointQuery】"));
        }

        MtWorkOrder mtWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woComponentReferencePointQuery】"));
        }

        String bomId = mtWorkOrder.getBomId();
        String routerId = mtWorkOrder.getRouterId();
        if (StringUtils.isEmpty(bomId)) {
            return Collections.emptyList();
        }

        MtRouterOpComponentVO3 condition = new MtRouterOpComponentVO3();
        condition.setBomId(bomId);
        condition.setMaterialId(dto.getMaterialId());
        if (StringUtils.isEmpty(dto.getComponentType())) {
            condition.setComponentType("ASSEMBLING");
        } else {
            condition.setComponentType(dto.getComponentType());
        }
        condition.setRouterId(routerId);
        condition.setOperationId(dto.getOperationId());

        List<String> bomComponentIds = this.mtRouterOperationComponentRepository
                        .operationOrMaterialLimitBomComponentQuery(tenantId, condition);
        if (CollectionUtils.isEmpty(bomComponentIds)) {
            return Collections.emptyList();
        }

        bomComponentIds = bomComponentIds.stream().distinct().collect(Collectors.toList());
        Map<String, List<MtWoComponentActualVO21>> resultMap = new HashMap<String, List<MtWoComponentActualVO21>>();

        MtBomReferencePointVO3 bomReferencePointVO3 = new MtBomReferencePointVO3();
        for (String bomComponentId : bomComponentIds) {
            bomReferencePointVO3.setBomComponentId(bomComponentId);
            List<MtBomReferencePoint> mtBomReferencePoints = mtBomReferencePointRepository
                            .bomComponentReferencePointQuery(tenantId, bomReferencePointVO3);

            final List<MtWoComponentActualVO21> result = new ArrayList<MtWoComponentActualVO21>();
            mtBomReferencePoints.stream().forEach(t -> {
                MtWoComponentActualVO21 mtWoComponentVO21 = new MtWoComponentActualVO21();
                mtWoComponentVO21.setLineNumber(t.getLineNumber());
                mtWoComponentVO21.setReferencePoint(t.getReferencePoint());
                mtWoComponentVO21.setQty(t.getQty());
                mtWoComponentVO21.setOperationId(dto.getOperationId());
                result.add(mtWoComponentVO21);
            });

            resultMap.put(bomComponentId, result);
        }

        if (StringUtils.isNotEmpty(dto.getOperationId())) {
            Iterator<String> keys = resultMap.keySet().iterator();
            while (keys.hasNext()) {
                List<MtWoComponentActualVO21> list = resultMap.get(keys.next());
                list.stream().forEach(t -> t.setOperationId(dto.getOperationId()));
            }
        } else {
            if (StringUtils.isEmpty(routerId)) {
                Iterator<String> keys = resultMap.keySet().iterator();
                while (keys.hasNext()) {
                    List<MtWoComponentActualVO21> list = resultMap.get(keys.next());
                    list.stream().forEach(t -> t.setOperationId(""));
                }
            } else {
                Map<String, List<String>> map = new HashMap<String, List<String>>();
                List<MtRouterStepVO5> routerStepOps =
                                this.mtRouterStepRepository.routerStepListQuery(tenantId, routerId);

                for (MtRouterStepVO5 routerStepOp : routerStepOps) {
                    List<MtRouterOperationComponent> mtRouterOperationComponents = mtRouterOperationComponentRepository
                                    .routerOperationComponentQuery(tenantId, routerStepOp.getRouterStepId());

                    for (MtRouterOperationComponent routerOperationComponent : mtRouterOperationComponents) {
                        if (map.containsKey(routerOperationComponent.getBomComponentId())) {
                            List<String> operationIds = map.get(routerOperationComponent.getBomComponentId());
                            if (!operationIds.contains(routerStepOp.getOperationId())) {
                                operationIds.add(routerStepOp.getOperationId());
                            }
                        } else {
                            List<String> operationIds = new ArrayList<String>();
                            operationIds.add(routerStepOp.getOperationId());
                            map.put(routerOperationComponent.getBomComponentId(), operationIds);
                        }
                    }
                }

                for (String bomComponentId : bomComponentIds) {
                    if (map.containsKey(bomComponentId)) {
                        List<MtWoComponentActualVO21> list = resultMap.get(bomComponentId);
                        List<String> operationIds = map.get(bomComponentId);
                        if (operationIds.size() == 1) {
                            list.stream().forEach(t -> t.setOperationId(operationIds.get(0)));
                        } else {
                            list.stream().forEach(t -> t.setOperationId(""));
                        }
                    }
                }
            }
        }

        final List<MtWoComponentActualVO21> totalList = new ArrayList<MtWoComponentActualVO21>();
        Iterator<String> keys = resultMap.keySet().iterator();
        while (keys.hasNext()) {
            totalList.addAll(resultMap.get(keys.next()));
        }
        return totalList;

    }

    @Override
    public MtWoComponentActualVO4 woComponentAssembleActualPropertyGet(Long tenantId,
                    String workOrderComponentActualId) {
        MtWoComponentActualVO4 v4 = null;
        if (StringUtils.isEmpty(workOrderComponentActualId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woComponentAssembleActualPropertyGet】"));
        }

        MtWorkOrderComponentActual mtWorkOrderComponentActual = new MtWorkOrderComponentActual();
        mtWorkOrderComponentActual.setTenantId(tenantId);
        mtWorkOrderComponentActual.setWorkOrderComponentActualId(workOrderComponentActualId);

        mtWorkOrderComponentActual = this.mtWorkOrderComponentActualMapper.selectOne(mtWorkOrderComponentActual);
        if (mtWorkOrderComponentActual != null) {
            v4 = new MtWoComponentActualVO4();
            BeanUtils.copyProperties(mtWorkOrderComponentActual, v4);
        }
        return v4;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woComponentRemove(Long tenantId, MtWoComponentActualVO1 dto) {
        // 第一步，判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woComponentRemove】"));
        }
        if (dto.getTrxAssembleQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxAssembleQty", "【API:woComponentRemove】"));
        }

        if (new BigDecimal(dto.getTrxAssembleQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxAssembleQty", "【API:woComponentRemove】"));
        }

        // 第二步，判断生产指令是否需要按工序装配移除：传入生产指令
        String operationAssembleFlag = mtWorkOrderRepository.woOperationAssembleFlagGet(tenantId, dto.getWorkOrderId());

        MtWoComponentActualVO2 cv2 = new MtWoComponentActualVO2();
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        String eventId = null;
        String eventTypeCode = null;
        MtBomComponentVO8 ecv08 = null;

        if (StringUtils.isEmpty(operationAssembleFlag) || "N".equals(operationAssembleFlag)) {
            if (StringUtils.isEmpty(dto.getAssembleExcessFlag()) || "N".equals(dto.getAssembleExcessFlag())) {
                // 2-a若获取结果为空或为N，且输入参数assembleExcessFlag未输入或输入N，表示非强制装配移除模式下不按照工序进行装配移除
                if (StringUtils.isEmpty(dto.getBomComponentId())) {
                    throw new MtException("MT_ORDER_0109", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0109", "ORDER", "【API:woComponentRemove】"));
                }
                ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
                if (ecv08 == null || !"REMOVE".equals(ecv08.getBomComponentType())) {
                    throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0105", "ORDER", "REMOVE", "【API:woComponentRemove】"));
                }

                // 3. 生成事件 并 记录事件与对象关系
                eventTypeCode = "WO_COMPONENT_REMOVE";
                eventCreateVO.setEventTypeCode(eventTypeCode);
                eventCreateVO.setParentEventId(dto.getParentEventId());
                eventCreateVO.setEventRequestId(dto.getEventRequestId());
                eventCreateVO.setWorkcellId(dto.getWorkcellId());
                eventCreateVO.setLocatorId(dto.getLocatorId());
                eventCreateVO.setShiftCode(dto.getShiftCode());
                eventCreateVO.setShiftDate(dto.getShiftDate());
                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                cv2.setWorkOrderId(dto.getWorkOrderId());
                cv2.setMaterialId(dto.getMaterialId());
                cv2.setBomComponentId(dto.getBomComponentId());
                cv2.setOperationId("");
                cv2.setRouterStepId("");
                cv2.setTrxAssembleQty(dto.getTrxAssembleQty());
                cv2.setComponentType("REMOVE");
                cv2.setAssembleExcessFlag("N");
                cv2.setAssembleRouterType(dto.getAssembleRouterType());
                cv2.setEventId(eventId);
                this.woComponentActualUpdate(tenantId, cv2);
            } else if ((StringUtils.isEmpty(operationAssembleFlag) || "N".equals(operationAssembleFlag))
                            && "Y".equals(dto.getAssembleExcessFlag())) {
                // 2-b若获取结果为空或为N，且输入参数assembleExcessFlag为Y，表示强制装配移除模式下不按照工序进行装配移除
                if (StringUtils.isEmpty(dto.getMaterialId())) {
                    throw new MtException("MT_ORDER_0106", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0106", "ORDER", "【API:woComponentRemove】"));
                }

                eventTypeCode = "WO_COMPONENT_EXCESS_REMOVE";
                eventCreateVO.setEventTypeCode(eventTypeCode);
                eventCreateVO.setParentEventId(dto.getParentEventId());
                eventCreateVO.setEventRequestId(dto.getEventRequestId());
                eventCreateVO.setWorkcellId(dto.getWorkcellId());
                eventCreateVO.setLocatorId(dto.getLocatorId());
                eventCreateVO.setShiftCode(dto.getShiftCode());
                eventCreateVO.setShiftDate(dto.getShiftDate());
                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                cv2.setWorkOrderId(dto.getWorkOrderId());
                cv2.setMaterialId(dto.getMaterialId());
                cv2.setBomComponentId("");
                cv2.setOperationId("");
                cv2.setRouterStepId("");
                cv2.setTrxAssembleQty(dto.getTrxAssembleQty());
                cv2.setComponentType("REMOVE");
                cv2.setAssembleExcessFlag("Y");
                cv2.setAssembleRouterType(dto.getAssembleRouterType());
                cv2.setEventId(eventId);
                this.woComponentActualUpdate(tenantId, cv2);
            }
        } else if ("Y".equals(operationAssembleFlag)) {
            if (StringUtils.isEmpty(dto.getAssembleExcessFlag()) || "N".equals(dto.getAssembleExcessFlag())) {
                // 2-c若获取结果为Y，且输入参数assembleExcessFlag未输入或输入N，表示非强制装配移除模式下按照工序进行装配移除
                if (StringUtils.isEmpty(dto.getBomComponentId())) {
                    throw new MtException("MT_ORDER_0109", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0109", "ORDER", "【API:woComponentRemove】"));
                }
                if (StringUtils.isEmpty(dto.getRouterStepId())) {
                    throw new MtException("MT_ORDER_0111", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0111", "ORDER", "【API:woComponentRemove】"));
                }
                ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
                if (ecv08 == null || !"REMOVE".equals(ecv08.getBomComponentType())) {
                    throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0105", "ORDER", "REMOVE", "【API:woComponentRemove】"));
                }

                eventTypeCode = "WO_COMPONENT_OPERATION_REMOVE";
                eventCreateVO.setEventTypeCode(eventTypeCode);
                eventCreateVO.setParentEventId(dto.getParentEventId());
                eventCreateVO.setEventRequestId(dto.getEventRequestId());
                eventCreateVO.setWorkcellId(dto.getWorkcellId());
                eventCreateVO.setLocatorId(dto.getLocatorId());
                eventCreateVO.setShiftCode(dto.getShiftCode());
                eventCreateVO.setShiftDate(dto.getShiftDate());
                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                cv2.setWorkOrderId(dto.getWorkOrderId());
                cv2.setMaterialId(dto.getMaterialId());
                cv2.setBomComponentId(dto.getBomComponentId());
                cv2.setRouterStepId(dto.getRouterStepId());
                cv2.setTrxAssembleQty(dto.getTrxAssembleQty());
                cv2.setComponentType("REMOVE");
                cv2.setAssembleExcessFlag("N");
                cv2.setAssembleRouterType(dto.getAssembleRouterType());
                cv2.setEventId(eventId);
                this.woComponentActualUpdate(tenantId, cv2);
            } else if ("Y".equals(dto.getAssembleExcessFlag()) && "Y".equals(operationAssembleFlag)) {
                // 若获取结果为Y，且输入参数assembleExcessFlag输入为Y，表示强制装配模式下按照工序进行装配
                if (StringUtils.isEmpty(dto.getMaterialId())) {
                    throw new MtException("MT_ORDER_0110", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0110", "ORDER", "【API:woComponentRemove】"));
                }
                if (StringUtils.isEmpty(dto.getOperationId())) {
                    throw new MtException("MT_ORDER_0112", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0112", "ORDER", "【API:woComponentRemove】"));
                }
                eventTypeCode = "WO_COMPONENT_EXCESS_OPERATION_REMOVE";
                eventCreateVO.setEventTypeCode(eventTypeCode);
                eventCreateVO.setParentEventId(dto.getParentEventId());
                eventCreateVO.setEventRequestId(dto.getEventRequestId());
                eventCreateVO.setWorkcellId(dto.getWorkcellId());
                eventCreateVO.setLocatorId(dto.getLocatorId());
                eventCreateVO.setShiftCode(dto.getShiftCode());
                eventCreateVO.setShiftDate(dto.getShiftDate());
                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                cv2.setWorkOrderId(dto.getWorkOrderId());
                cv2.setMaterialId(dto.getMaterialId());
                cv2.setBomComponentId("");
                cv2.setOperationId(dto.getOperationId());
                cv2.setRouterStepId("");
                cv2.setTrxAssembleQty(dto.getTrxAssembleQty());
                cv2.setComponentType("REMOVE");
                cv2.setAssembleExcessFlag("Y");
                cv2.setAssembleRouterType(dto.getAssembleRouterType());
                cv2.setEventId(eventId);
                this.woComponentActualUpdate(tenantId, cv2);
            }
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woComponentScrap(Long tenantId, MtWoComponentActualVO8 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woComponentScrap】"));
        }
        if (dto.getTrxScrappedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxScrappedQty ", "【API:woComponentScrap】"));
        }

        if (BigDecimal.valueOf(dto.getTrxScrappedQty()).compareTo(BigDecimal.valueOf(0.0)) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxAssembleQty", "【API:woComponentScrap】"));
        }

        MtWorkOrder mtWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woComponentScrap】"));
        }

        String bomId = null;
        if (dto.getBomId() != null) {
            bomId = dto.getBomId();
        } else {
            bomId = mtWorkOrder.getBomId();
        }
        String operationAssembleFlag = mtWorkOrderRepository.woOperationAssembleFlagGet(tenantId, dto.getWorkOrderId());
        MtWoComponentActualVO2 cv2 = new MtWoComponentActualVO2();
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        String eventId = null;
        String eventTypeCode = null;
        MtBomComponentVO8 ecv08 = null;

        MtWoComponentActualVO9 v9 = new MtWoComponentActualVO9();
        String workOrderComponentActualId = null;
        List<String> workOrderComponentActualIds = null;
        if (StringUtils.isEmpty(operationAssembleFlag) || "N".equals(operationAssembleFlag)) {
            if (StringUtils.isEmpty(dto.getAssembleExcessFlag()) || "N".equals(dto.getAssembleExcessFlag())) {
                // 若获取结果为空或为N，且输入参数assembleExcessFlag未输入或输入N，表示非强制装配移除模式下不按照工序进行装配移除
                if (StringUtils.isEmpty(dto.getBomComponentId())) {
                    throw new MtException("MT_ORDER_0119", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0119", "ORDER", "【API:woComponentScrap】"));
                }
                ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
                if (ecv08 == null || !"ASSEMBLING".equals(ecv08.getBomComponentType())) {
                    throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:woComponentScrap】"));
                }

                v9.setWorkOrderId(dto.getWorkOrderId());
                if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                    v9.setMaterialId(dto.getMaterialId());
                } else {
                    if (StringUtils.isNotEmpty(ecv08.getMaterialId())) {
                        v9.setMaterialId(ecv08.getMaterialId());
                    } else {
                        throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0148", "ORDER", "【API:woComponentScrap】"));

                    }
                }

                v9.setBomComponentId(dto.getBomComponentId());
                v9.setOperationId("");
                v9.setRouterStepId("");
                v9.setComponentType("ASSEMBLING");
                v9.setAssembleExcessFlag("N");
                v9.setBomId(bomId);
                workOrderComponentActualIds = propertyLimitWoComponentAssembleActualQuery(tenantId, v9);
                if (CollectionUtils.isEmpty(workOrderComponentActualIds)) {
                    throw new MtException("MT_ORDER_0114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0114", "ORDER", "【API:woComponentScrap】"));
                } else if (workOrderComponentActualIds.size() == 1) {
                    workOrderComponentActualId = workOrderComponentActualIds.get(0);
                    eventTypeCode = "WO_COMPONENT_SCRAP";
                    eventCreateVO.setEventTypeCode(eventTypeCode);
                    eventCreateVO.setParentEventId(dto.getParentEventId());
                    eventCreateVO.setEventRequestId(dto.getEventRequestId());
                    eventCreateVO.setWorkcellId(dto.getWorkcellId());
                    eventCreateVO.setLocatorId(dto.getLocatorId());
                    eventCreateVO.setShiftCode(dto.getShiftCode());
                    eventCreateVO.setShiftDate(dto.getShiftDate());
                    eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                    cv2.setWorkOrderComponentActualId(workOrderComponentActualId);
                    cv2.setTrxScrappedQty(dto.getTrxScrappedQty());
                    cv2.setEventId(eventId);
                    this.woComponentActualUpdate(tenantId, cv2);
                }

            } else if ("Y".equals(dto.getAssembleExcessFlag())) {
                // 若获取结果为空或为N，且输入参数assembleExcessFlag为Y，表示针对强制装配模式下不按照工序进行装配结果进行装配报废
                if (StringUtils.isEmpty(dto.getMaterialId())) {
                    throw new MtException("MT_ORDER_0120", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0120", "ORDER", "【API:woComponentScrap】"));
                }
                v9.setWorkOrderId(dto.getWorkOrderId());
                v9.setMaterialId(dto.getMaterialId());
                v9.setBomComponentId("");
                v9.setOperationId("");
                v9.setRouterStepId("");
                v9.setComponentType("ASSEMBLING");
                v9.setAssembleExcessFlag("Y");
                v9.setBomId(bomId);
                workOrderComponentActualIds = propertyLimitWoComponentAssembleActualQuery(tenantId, v9);
                if (CollectionUtils.isEmpty(workOrderComponentActualIds)) {
                    throw new MtException("MT_ORDER_0114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0114", "ORDER", "【API:woComponentScrap】"));
                } else if (workOrderComponentActualIds.size() == 1) {
                    workOrderComponentActualId = workOrderComponentActualIds.get(0);
                    eventTypeCode = "WO_COMPONENT_EXCESS_SCRAP";
                    eventCreateVO.setEventTypeCode(eventTypeCode);
                    eventCreateVO.setParentEventId(dto.getParentEventId());
                    eventCreateVO.setEventRequestId(dto.getEventRequestId());
                    eventCreateVO.setWorkcellId(dto.getWorkcellId());
                    eventCreateVO.setLocatorId(dto.getLocatorId());
                    eventCreateVO.setShiftCode(dto.getShiftCode());
                    eventCreateVO.setShiftDate(dto.getShiftDate());
                    eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                    cv2.setWorkOrderComponentActualId(workOrderComponentActualId);
                    cv2.setTrxScrappedQty(dto.getTrxScrappedQty());
                    cv2.setEventId(eventId);
                    this.woComponentActualUpdate(tenantId, cv2);
                }
            }
        } else if ("Y".equals(operationAssembleFlag)) {
            if (StringUtils.isEmpty(dto.getAssembleExcessFlag()) || "N".equals(dto.getAssembleExcessFlag())) {
                // 若获取结果为Y，且输入参数assembleExcessFlag未输入或输入N，表示针对非强制装配模式下按照工序进行装配的装配结果进行报废
                if (StringUtils.isEmpty(dto.getBomComponentId())) {
                    throw new MtException("MT_ORDER_0119", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0119", "ORDER", "【API:woComponentScrap】"));
                }
                if (StringUtils.isEmpty(dto.getRouterStepId())) {
                    throw new MtException("MT_ORDER_0121", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0121", "ORDER", "【API:woComponentScrap】"));
                }
                ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
                if (ecv08 == null || !"ASSEMBLING".equals(ecv08.getBomComponentType())) {
                    throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:woComponentScrap】"));
                }
                if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                    v9.setMaterialId(dto.getMaterialId());
                } else {
                    if (StringUtils.isNotEmpty(ecv08.getMaterialId())) {
                        v9.setMaterialId(ecv08.getMaterialId());
                    } else {
                        throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0148", "ORDER", "【API:woComponentScrap】"));

                    }
                }
                if (StringUtils.isNotEmpty(dto.getOperationId())) {
                    v9.setOperationId(dto.getOperationId());
                } else {
                    MtRouterOperation mtRouterOperation =
                                    mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
                    if (mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                        throw new MtException("MT_ORDER_0149", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0149", "ORDER", "【API:woComponentScrap】"));
                    }
                    v9.setOperationId(mtRouterOperation.getOperationId());
                }
                v9.setWorkOrderId(dto.getWorkOrderId());
                v9.setBomComponentId(dto.getBomComponentId());

                v9.setRouterStepId(dto.getRouterStepId());
                v9.setComponentType("ASSEMBLING");
                v9.setAssembleExcessFlag("N");
                v9.setBomId(bomId);
                workOrderComponentActualIds = propertyLimitWoComponentAssembleActualQuery(tenantId, v9);
                if (CollectionUtils.isEmpty(workOrderComponentActualIds)) {
                    throw new MtException("MT_ORDER_0114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0114", "ORDER", "【API:woComponentScrap】"));
                } else if (workOrderComponentActualIds.size() == 1) {
                    workOrderComponentActualId = workOrderComponentActualIds.get(0);
                    eventTypeCode = "WO_COMPONENT_OPERATION_SCRAP";
                    eventCreateVO.setEventTypeCode(eventTypeCode);
                    eventCreateVO.setParentEventId(dto.getParentEventId());
                    eventCreateVO.setEventRequestId(dto.getEventRequestId());
                    eventCreateVO.setWorkcellId(dto.getWorkcellId());
                    eventCreateVO.setLocatorId(dto.getLocatorId());
                    eventCreateVO.setShiftCode(dto.getShiftCode());
                    eventCreateVO.setShiftDate(dto.getShiftDate());
                    eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                    cv2.setWorkOrderComponentActualId(workOrderComponentActualId);
                    cv2.setTrxScrappedQty(dto.getTrxScrappedQty());
                    cv2.setEventId(eventId);
                    this.woComponentActualUpdate(tenantId, cv2);
                }

            } else if ("Y".equals(dto.getAssembleExcessFlag())) {
                // 若获取结果为Y，且输入参数assembleExcessFlag输入为Y，表示强制装配模式下按照工序进行装配
                if (StringUtils.isEmpty(dto.getMaterialId())) {
                    throw new MtException("MT_ORDER_0120", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0120", "ORDER", "【API:woComponentScrap】"));
                }
                if (StringUtils.isEmpty(dto.getOperationId())) {
                    throw new MtException("MT_ORDER_0122", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0122", "ORDER", "【API:woComponentScrap】"));
                }

                v9.setWorkOrderId(dto.getWorkOrderId());
                v9.setMaterialId(dto.getMaterialId());
                v9.setBomComponentId("");
                v9.setOperationId(dto.getOperationId());
                // 2020-12-25 强制装配也传routerStepId by sanfeng.zhang
                v9.setRouterStepId(dto.getRouterStepId());
                v9.setComponentType("ASSEMBLING");
                v9.setAssembleExcessFlag("Y");
                v9.setBomId(bomId);
                workOrderComponentActualIds = propertyLimitWoComponentAssembleActualQuery(tenantId, v9);
                if (CollectionUtils.isEmpty(workOrderComponentActualIds)) {
                    throw new MtException("MT_ORDER_0114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0114", "ORDER", "【API:woComponentScrap】"));
                } else if (workOrderComponentActualIds.size() == 1) {
                    workOrderComponentActualId = workOrderComponentActualIds.get(0);
                    eventTypeCode = "WO_COMPONENT_EXCESS_OPERATION_SCRAP";
                    eventCreateVO.setEventTypeCode(eventTypeCode);
                    eventCreateVO.setParentEventId(dto.getParentEventId());
                    eventCreateVO.setEventRequestId(dto.getEventRequestId());
                    eventCreateVO.setWorkcellId(dto.getWorkcellId());
                    eventCreateVO.setLocatorId(dto.getLocatorId());
                    eventCreateVO.setShiftCode(dto.getShiftCode());
                    eventCreateVO.setShiftDate(dto.getShiftDate());
                    eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                    cv2.setWorkOrderComponentActualId(workOrderComponentActualId);
                    cv2.setTrxScrappedQty(dto.getTrxScrappedQty());
                    cv2.setEventId(eventId);
                    this.woComponentActualUpdate(tenantId, cv2);
                }
            }
        }
    }

    @Override
    public List<String> propertyLimitWoComponentAssembleActualQuery(Long tenantId, MtWoComponentActualVO9 dto) {
        List<String> workOrderComponentActualIds = null;
        if (StringUtils.isEmpty(dto.getWorkOrderId()) && StringUtils.isEmpty(dto.getWorkOrderComponentActualId())) {
            throw new MtException("MT_ORDER_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032", "ORDER",
                                            "workOrderComponentActualId、workOrderId",
                                            "【API:propertyLimitWoComponentAssembleActualQuery】"));
        }

        workOrderComponentActualIds = this.mtWorkOrderComponentActualMapper
                        .propertyLimitWoComponentAssembleActualQuery(tenantId, dto);
        return workOrderComponentActualIds;
    }

    @Override
    public List<MtWoComponentActualVO10> woAssembledExcessMaterialQuery(Long tenantId, MtWoComponentActualVO22 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woAssembledExcessMaterialQuery】"));
        }

        return this.mtWorkOrderComponentActualMapper.woAssembledExcessMaterialQuery(tenantId, dto);
    }


    @Override
    public List<MtWoComponentActualVO11> woAssembledSubstituteMaterialQuery(Long tenantId,
                    MtWoComponentActualVO23 dto) {
        // 第一步，获取输入参数，判workOrderId是否有输入
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woAssembledSubstituteMaterialQuery】"));
        }
        // 第二步，获取生产指令数量：
        MtWorkOrder mtWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (null == mtWorkOrder || null == mtWorkOrder.getQty()) {
            return Collections.emptyList();
        }
        // 第三步获取生产指令组件装配实绩3.1
        List<MtWorkOrderComponentActual> mtWorkOrderComponentActuals =
                        this.mtWorkOrderComponentActualMapper.woAssembledSubstituteMaterialQuery(tenantId, dto);
        if (CollectionUtils.isEmpty(mtWorkOrderComponentActuals)) {
            return Collections.emptyList();
        }

        List<String> bomComponentIds = mtWorkOrderComponentActuals.stream()
                        .map(MtWorkOrderComponentActual::getBomComponentId).collect(Collectors.toList());
        List<String> bomIds = mtWorkOrderComponentActuals.stream().map(MtWorkOrderComponentActual::getBomId)
                        .collect(Collectors.toList());
        // 3.2首先根据第三步取到的bomComponentId调用API{bomComponentBasicBatchGet}获取相应组件对应的物料component_materialId、组件行数量component_qty
        List<MtBomComponentVO13> mtBomComponents =
                        this.mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIds);
        List<MtBomVO7> mtBoms = this.mtBomRepository.bomBasicBatchGet(tenantId, bomIds);


        List<MtWoComponentActualVO11> result = new ArrayList<MtWoComponentActualVO11>();
        MtWoComponentActualVO11 mtWoComponentVO11 = null;
        String componentMaterialId = null;
        Double preQty = 0.0D;
        Double primaryQty = 0.0D;
        Double componentQty = 0.0D;
        Double componentRequirementQty = 0.0D;
        Double requirementQty = 0.0D;

        for (MtWorkOrderComponentActual mtWorkOrderComponentActual : mtWorkOrderComponentActuals) {
            mtWoComponentVO11 = new MtWoComponentActualVO11();
            mtWoComponentVO11.setWorkOrderComponentActualId(mtWorkOrderComponentActual.getWorkOrderComponentActualId());
            mtWoComponentVO11.setWorkOrderId(mtWorkOrderComponentActual.getWorkOrderId());
            mtWoComponentVO11.setMaterialId(mtWorkOrderComponentActual.getMaterialId());
            mtWoComponentVO11.setOperationId(mtWorkOrderComponentActual.getOperationId());
            mtWoComponentVO11.setAssembleQty(mtWorkOrderComponentActual.getAssembleQty());
            mtWoComponentVO11.setScrappedQty(mtWorkOrderComponentActual.getScrappedQty());
            mtWoComponentVO11.setBomComponentId(mtWorkOrderComponentActual.getBomComponentId());
            mtWoComponentVO11.setBomId(mtWorkOrderComponentActual.getBomId());
            mtWoComponentVO11.setRouterStepId(mtWorkOrderComponentActual.getRouterStepId());
            mtWoComponentVO11.setActualFirstTime(mtWorkOrderComponentActual.getActualFirstTime());
            mtWoComponentVO11.setActualLastTime(mtWorkOrderComponentActual.getActualLastTime());

            if (StringUtils.isNotEmpty(mtWorkOrderComponentActual.getRouterStepId())) {
                MtRouterOpComponentVO1 mtRouterOpComponentVO1 = new MtRouterOpComponentVO1();
                mtRouterOpComponentVO1.setRouterStepId(mtWorkOrderComponentActual.getRouterStepId());
                List<MtRouterOpComponentVO> routerOperationComponentList = mtRouterOperationComponentRepository
                                .routerOperationComponentPerQtyQuery(tenantId, mtRouterOpComponentVO1);

                Optional<MtRouterOpComponentVO> optional = routerOperationComponentList.stream().filter(
                                c -> mtWorkOrderComponentActual.getBomComponentId().equals(c.getBomComponentId()))
                                .findFirst();

                if (optional.isPresent()) {
                    preQty = optional.get().getPerQty() == null ? Double.valueOf(0.0D) : optional.get().getPerQty();
                } else {
                    preQty = Double.valueOf(0.0D);
                }
                componentRequirementQty =
                                (BigDecimal.valueOf(preQty).multiply(BigDecimal.valueOf(mtWorkOrder.getQty())))
                                                .doubleValue();
            } else {
                Optional<MtBomVO7> mtBom = mtBoms.stream()
                                .filter(c -> mtWorkOrderComponentActual.getBomId().equals(c.getBomId())).findFirst();
                if (mtBom.isPresent()) {
                    primaryQty = mtBom.get().getPrimaryQty() == null ? Double.valueOf(0.0D)
                                    : mtBom.get().getPrimaryQty();
                } else {
                    primaryQty = Double.valueOf(0.0D);
                }

                Optional<MtBomComponentVO13> mtBomComponent = mtBomComponents.stream().filter(
                                c -> mtWorkOrderComponentActual.getBomComponentId().equals(c.getBomComponentId()))
                                .findFirst();
                if (mtBomComponent.isPresent()) {
                    componentQty = mtBomComponent.get().getQty() == null ? Double.valueOf(0.0D)
                                    : mtBomComponent.get().getQty();
                } else {
                    componentQty = Double.valueOf(0.0D);
                }

                if (BigDecimal.valueOf(primaryQty).compareTo(BigDecimal.ZERO) == 0) {
                    componentRequirementQty = Double.valueOf(0.0D);
                } else {
                    componentRequirementQty = BigDecimal.valueOf(mtWorkOrder.getQty())
                                    .multiply(BigDecimal.valueOf(componentQty))
                                    .divide(BigDecimal.valueOf(primaryQty), 10, BigDecimal.ROUND_HALF_DOWN)
                                    .doubleValue();
                }
            }

            MtBomSubstituteVO6 bomSubstituteVO6 = new MtBomSubstituteVO6();
            bomSubstituteVO6.setBomComponentId(mtWorkOrderComponentActual.getBomComponentId());
            bomSubstituteVO6.setQty(componentRequirementQty);
            // 确认过输入条数跟输出条数一样，所以能取到数据
            bomSubstituteVO6.setMaterialId(mtWorkOrderComponentActual.getMaterialId());
            List<MtBomSubstituteVO3> bomSubstitutes =
                            this.mtBomSubstituteRepository.bomSubstituteQtyCalculate(tenantId, bomSubstituteVO6);
            if (CollectionUtils.isNotEmpty(bomSubstitutes)) {
                requirementQty = bomSubstitutes.get(0).getComponentQty();
            }
            mtWoComponentVO11.setRequirementQty(requirementQty);
            Optional<MtBomComponentVO13> mtBomComponent = mtBomComponents.stream()
                            .filter(c -> mtWorkOrderComponentActual.getBomComponentId().equals(c.getBomComponentId()))
                            .findFirst();
            if (mtBomComponent.isPresent()) {
                componentMaterialId = mtBomComponent.get().getMaterialId();
            } else {
                componentMaterialId = null;
            }
            mtWoComponentVO11.setComponentMaterialId(componentMaterialId);
            mtWoComponentVO11.setComponentRequirementQty(componentRequirementQty);
            result.add(mtWoComponentVO11);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woComponentScrapCancel(Long tenantId, MtWoComponentActualVO8 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woComponentScrapCancel】"));
        }
        if (dto.getTrxScrappedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxScrappedQty ", "【API:woComponentScrapCancel】"));
        }

        if (BigDecimal.valueOf(dto.getTrxScrappedQty()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxAssembleQty", "【API:woComponentScrapCancel】"));
        }


        MtWorkOrder mtWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woComponentScrapCancel】"));
        }

        String bomId = null;
        if (dto.getBomId() != null) {
            bomId = dto.getBomId();
        } else {
            bomId = mtWorkOrder.getBomId();
        }

        // 第三步
        String operationAssembleFlag = mtWorkOrderRepository.woOperationAssembleFlagGet(tenantId, dto.getWorkOrderId());
        MtWoComponentActualVO2 cv2 = new MtWoComponentActualVO2();
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        String eventId = null;
        String eventTypeCode = null;
        MtBomComponentVO8 ecv08 = null;
        MtWoComponentActualVO9 v9 = new MtWoComponentActualVO9();
        String workOrderComponentActualId = null;
        List<String> workOrderComponentActualIds = null;
        MtWorkOrderComponentActual mtWorkOrderComponentActual = null;

        if ((StringUtils.isEmpty(operationAssembleFlag) || "N".equals(operationAssembleFlag))
                        && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 若获取结果为空或为N，且输入参数assembleExcessFlag未输入或输入N，表示针对非强制装配模式下不按照工序的装配报废结果进行装配报废取消
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ORDER_0123", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0123", "ORDER", "【API:woComponentScrapCancel】"));
            }
            ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (ecv08 == null || !"ASSEMBLING".equals(ecv08.getBomComponentType())) {
                throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:woComponentScrapCancel】"));
            }

            v9.setWorkOrderId(dto.getWorkOrderId());
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                v9.setMaterialId(dto.getMaterialId());
            } else {
                if (StringUtils.isNotEmpty(ecv08.getMaterialId())) {
                    v9.setMaterialId(ecv08.getMaterialId());
                } else {
                    throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0148", "ORDER", "【API:woComponentScrapCancel】"));

                }
            }

            v9.setBomComponentId(dto.getBomComponentId());
            v9.setOperationId("");
            v9.setRouterStepId("");
            v9.setComponentType("ASSEMBLING");
            v9.setAssembleExcessFlag("N");
            v9.setBomId(bomId);
            workOrderComponentActualIds = propertyLimitWoComponentAssembleActualQuery(tenantId, v9);
            if (CollectionUtils.isEmpty(workOrderComponentActualIds)) {
                throw new MtException("MT_ORDER_0114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0114", "ORDER", "【API:woComponentScrapCancel】"));
            } else if (workOrderComponentActualIds.size() == 1) {
                workOrderComponentActualId = workOrderComponentActualIds.get(0);
                eventTypeCode = "WO_COMPONENT_SCRAP_CANCEL";
                eventCreateVO.setEventTypeCode(eventTypeCode);
                eventCreateVO.setParentEventId(dto.getParentEventId());
                eventCreateVO.setEventRequestId(dto.getEventRequestId());
                eventCreateVO.setWorkcellId(dto.getWorkcellId());
                eventCreateVO.setLocatorId(dto.getLocatorId());
                eventCreateVO.setShiftCode(dto.getShiftCode());
                eventCreateVO.setShiftDate(dto.getShiftDate());
                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                cv2.setWorkOrderComponentActualId(workOrderComponentActualId);
                cv2.setTrxScrappedQty(-dto.getTrxScrappedQty());
                cv2.setEventId(eventId);
                this.woComponentActualUpdate(tenantId, cv2);
            }
        } else if ((StringUtils.isEmpty(operationAssembleFlag) || "N".equals(operationAssembleFlag))
                        && "Y".equals(dto.getAssembleExcessFlag())) {
            // 若获取结果为空或为N，且输入参数assembleExcessFlag为Y，表示针对强制装配模式下不按照工序进行装配结果进行装配报废
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ORDER_0124", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0124", "ORDER", "【API:woComponentScrapCancel】"));
            }
            v9.setWorkOrderId(dto.getWorkOrderId());
            v9.setMaterialId(dto.getMaterialId());
            v9.setBomComponentId("");
            v9.setOperationId("");
            v9.setRouterStepId("");
            v9.setComponentType("ASSEMBLING");
            v9.setAssembleExcessFlag("Y");
            v9.setBomId(bomId);
            workOrderComponentActualIds = propertyLimitWoComponentAssembleActualQuery(tenantId, v9);
            if (CollectionUtils.isEmpty(workOrderComponentActualIds)) {
                throw new MtException("MT_ORDER_0114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0114", "ORDER", "【API:woComponentScrapCancel】"));
            } else if (workOrderComponentActualIds.size() == 1) {
                workOrderComponentActualId = workOrderComponentActualIds.get(0);
                eventTypeCode = "WO_COMPONENT_EXCESS_SCRAP_CANCEL";
                eventCreateVO.setEventTypeCode(eventTypeCode);
                eventCreateVO.setParentEventId(dto.getParentEventId());
                eventCreateVO.setEventRequestId(dto.getEventRequestId());
                eventCreateVO.setWorkcellId(dto.getWorkcellId());
                eventCreateVO.setLocatorId(dto.getLocatorId());
                eventCreateVO.setShiftCode(dto.getShiftCode());
                eventCreateVO.setShiftDate(dto.getShiftDate());
                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                cv2.setWorkOrderComponentActualId(workOrderComponentActualId);
                cv2.setTrxScrappedQty(-dto.getTrxScrappedQty());
                cv2.setEventId(eventId);
                this.woComponentActualUpdate(tenantId, cv2);
            }
        } else if ("Y".equals(operationAssembleFlag) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {

            // 若获取结果为Y，且输入参数assembleExcessFlag未输入或输入N，表示针对非强制装配模式下按照工序进行装配的装配报废结果进行报废取消
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ORDER_0123", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0123", "ORDER", "【API:woComponentScrapCancel】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ORDER_0125", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0125", "ORDER", "【API:woComponentScrapCancel】"));
            }
            ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (ecv08 == null || !"ASSEMBLING".equals(ecv08.getBomComponentType())) {
                throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:woComponentScrapCancel】"));
            }
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                v9.setMaterialId(dto.getMaterialId());
            } else {
                if (StringUtils.isNotEmpty(ecv08.getMaterialId())) {
                    v9.setMaterialId(ecv08.getMaterialId());
                } else {
                    throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0148", "ORDER", "【API:woComponentScrapCancel】"));

                }
            }
            if (StringUtils.isNotEmpty(dto.getOperationId())) {
                v9.setOperationId(dto.getOperationId());
            } else {
                MtRouterOperation mtRouterOperation =
                                mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
                if (mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                    throw new MtException("MT_ORDER_0149", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0149", "ORDER", "【API:woComponentScrapCancel】"));
                }
                v9.setOperationId(mtRouterOperation.getOperationId());
            }
            v9.setWorkOrderId(dto.getWorkOrderId());
            v9.setBomComponentId(dto.getBomComponentId());

            v9.setRouterStepId(dto.getRouterStepId());
            v9.setComponentType("ASSEMBLING");
            v9.setAssembleExcessFlag("N");
            v9.setBomId(bomId);
            workOrderComponentActualIds = propertyLimitWoComponentAssembleActualQuery(tenantId, v9);
            if (CollectionUtils.isEmpty(workOrderComponentActualIds)) {
                throw new MtException("MT_ORDER_0114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0114", "ORDER", "【API:woComponentScrapCancel】"));
            } else if (workOrderComponentActualIds.size() == 1) {
                workOrderComponentActualId = workOrderComponentActualIds.get(0);
                eventTypeCode = "WO_COMPONENT_OPERATION_SCRAP_CANCEL";
                eventCreateVO.setEventTypeCode(eventTypeCode);
                eventCreateVO.setParentEventId(dto.getParentEventId());
                eventCreateVO.setEventRequestId(dto.getEventRequestId());
                eventCreateVO.setWorkcellId(dto.getWorkcellId());
                eventCreateVO.setLocatorId(dto.getLocatorId());
                eventCreateVO.setShiftCode(dto.getShiftCode());
                eventCreateVO.setShiftDate(dto.getShiftDate());

                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                cv2.setWorkOrderComponentActualId(workOrderComponentActualId);
                cv2.setTrxScrappedQty(-dto.getTrxScrappedQty());
                cv2.setEventId(eventId);
                this.woComponentActualUpdate(tenantId, cv2);

            }
        } else if ("Y".equals(dto.getAssembleExcessFlag()) && "Y".equals(operationAssembleFlag)) {
            // 若获取结果为Y，且输入参数assembleExcessFlag输入为Y，表示针对强制装配模式下按照工序进行装配的装配报废实绩进行报废取消
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ORDER_0124", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0124", "ORDER", "【API:woComponentScrapCancel】"));
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("MT_ORDER_0126", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0126", "ORDER", "【API:woComponentScrapCancel】"));
            }

            v9.setWorkOrderId(dto.getWorkOrderId());
            v9.setMaterialId(dto.getMaterialId());
            v9.setBomComponentId("");
            v9.setOperationId(dto.getOperationId());
            v9.setRouterStepId("");
            v9.setComponentType("ASSEMBLING");
            v9.setAssembleExcessFlag("Y");
            v9.setBomId(bomId);
            workOrderComponentActualIds = propertyLimitWoComponentAssembleActualQuery(tenantId, v9);
            if (CollectionUtils.isEmpty(workOrderComponentActualIds)) {
                throw new MtException("MT_ORDER_0114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0114", "ORDER", "【API:woComponentScrapCancel】"));
            } else if (workOrderComponentActualIds.size() == 1) {
                workOrderComponentActualId = workOrderComponentActualIds.get(0);
                eventTypeCode = "WO_COMPONENT_EXCESS_OPERATION_SCRAP_CANCEL";
                eventCreateVO.setEventTypeCode(eventTypeCode);
                eventCreateVO.setParentEventId(dto.getParentEventId());
                eventCreateVO.setEventRequestId(dto.getEventRequestId());
                eventCreateVO.setWorkcellId(dto.getWorkcellId());
                eventCreateVO.setLocatorId(dto.getLocatorId());
                eventCreateVO.setShiftCode(dto.getShiftCode());
                eventCreateVO.setShiftDate(dto.getShiftDate());

                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                cv2.setWorkOrderComponentActualId(workOrderComponentActualId);
                cv2.setTrxScrappedQty(-dto.getTrxScrappedQty());
                cv2.setEventId(eventId);
                this.woComponentActualUpdate(tenantId, cv2);
            }
        }
        mtWorkOrderComponentActual = new MtWorkOrderComponentActual();
        mtWorkOrderComponentActual.setTenantId(tenantId);
        mtWorkOrderComponentActual.setWorkOrderComponentActualId(workOrderComponentActualId);

        mtWorkOrderComponentActual = this.mtWorkOrderComponentActualMapper.selectOne(mtWorkOrderComponentActual);
        if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(mtWorkOrderComponentActual.getScrappedQty())) > 0) {
            throw new MtException("MT_ORDER_0127", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0127", "ORDER", "【API:woComponentScrapCancel】"));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woComponentAssembleCancel(Long tenantId, MtWoComponentActualVO12 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woComponentAssembleCancel】"));
        }
        if (dto.getTrxAssembleQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxAssembleQty", "【API:woComponentAssembleCancel】"));
        }
        if (BigDecimal.valueOf(dto.getTrxAssembleQty()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxAssembleQty", "【API:woComponentAssembleCancel】"));
        }

        MtWorkOrder mtWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woComponentAssembleCancel】"));
        }

        String bomId = null;
        if (dto.getBomId() != null) {
            bomId = dto.getBomId();
        } else {
            bomId = mtWorkOrder.getBomId();
        }

        String operationAssembleFlag = mtWorkOrderRepository.woOperationAssembleFlagGet(tenantId, dto.getWorkOrderId());

        MtWoComponentActualVO2 cv2 = new MtWoComponentActualVO2();
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        String eventId = null;
        String eventTypeCode = null;
        MtBomComponentVO8 ecv08 = null;
        MtWoComponentActualVO9 v9 = new MtWoComponentActualVO9();
        String workOrderComponentActualId = null;
        List<String> workOrderComponentActualIds = null;
        MtWorkOrderComponentActual mtWorkOrderComponentActual = null;

        if (StringUtils.isEmpty(operationAssembleFlag) || "N".equals(operationAssembleFlag)) {
            if (StringUtils.isEmpty(dto.getAssembleExcessFlag()) || "N".equals(dto.getAssembleExcessFlag())) {
                // 若获取结果为空或为N，且输入参数assembleExcessFlag未输入或输入N，表示针对非强制装配模式下不按照工序的装配结果进行装配取消
                if (StringUtils.isEmpty(dto.getBomComponentId())) {
                    throw new MtException("MT_ORDER_0113", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0113", "ORDER", "【API:woComponentAssembleCancel】"));
                }
                ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
                if (ecv08 == null || !"ASSEMBLING".equals(ecv08.getBomComponentType())) {
                    throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:woComponentAssembleCancel】"));
                }

                v9.setWorkOrderId(dto.getWorkOrderId());
                if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                    v9.setMaterialId(dto.getMaterialId());
                } else {
                    if (StringUtils.isNotEmpty(ecv08.getMaterialId())) {
                        v9.setMaterialId(ecv08.getMaterialId());
                    } else {
                        throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0148", "ORDER", "【API:woComponentAssembleCancel】"));

                    }
                }

                v9.setBomComponentId(dto.getBomComponentId());
                v9.setOperationId("");
                v9.setRouterStepId("");
                v9.setComponentType("ASSEMBLING");
                v9.setAssembleExcessFlag("N");
                v9.setBomId(bomId);
                workOrderComponentActualIds = propertyLimitWoComponentAssembleActualQuery(tenantId, v9);
                if (CollectionUtils.isEmpty(workOrderComponentActualIds)) {
                    throw new MtException("MT_ORDER_0114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0114", "ORDER", "【API:woComponentAssembleCancel】"));
                } else if (workOrderComponentActualIds.size() == 1) {
                    workOrderComponentActualId = workOrderComponentActualIds.get(0);
                    eventTypeCode = "WO_COMPONENT_ASSEMBLE_CANCEL";
                    eventCreateVO.setEventTypeCode(eventTypeCode);
                    eventCreateVO.setParentEventId(dto.getParentEventId());
                    eventCreateVO.setEventRequestId(dto.getEventRequestId());
                    eventCreateVO.setWorkcellId(dto.getWorkcellId());
                    eventCreateVO.setLocatorId(dto.getLocatorId());
                    eventCreateVO.setShiftCode(dto.getShiftCode());
                    eventCreateVO.setShiftDate(dto.getShiftDate());
                    eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                    cv2.setWorkOrderComponentActualId(workOrderComponentActualId);
                    cv2.setTrxAssembleQty(-dto.getTrxAssembleQty());
                    cv2.setEventId(eventId);
                    this.woComponentActualUpdate(tenantId, cv2);
                }

            } else if ("Y".equals(dto.getAssembleExcessFlag())) {
                // 若获取结果为空或为N，且输入参数assembleExcessFlag为Y，表示针对强制装配模式下不按照工序进行装配结果进行装配取消
                if (StringUtils.isEmpty(dto.getMaterialId())) {
                    throw new MtException("MT_ORDER_0115", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0115", "ORDER", "【API:woComponentAssembleCancel】"));
                }
                v9.setWorkOrderId(dto.getWorkOrderId());
                v9.setMaterialId(dto.getMaterialId());
                v9.setBomComponentId("");
                v9.setOperationId("");
                v9.setRouterStepId("");
                v9.setComponentType("ASSEMBLING");
                v9.setAssembleExcessFlag("Y");
                v9.setBomId(bomId);
                workOrderComponentActualIds = propertyLimitWoComponentAssembleActualQuery(tenantId, v9);
                if (CollectionUtils.isEmpty(workOrderComponentActualIds)) {
                    throw new MtException("MT_ORDER_0114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0114", "ORDER", "【API:woComponentAssembleCancel】"));
                } else if (workOrderComponentActualIds.size() == 1) {
                    workOrderComponentActualId = workOrderComponentActualIds.get(0);
                    eventTypeCode = "WO_COMPONENT_EXCESS_ASSEMBLE_CANCEL";
                    eventCreateVO.setEventTypeCode(eventTypeCode);
                    eventCreateVO.setParentEventId(dto.getParentEventId());
                    eventCreateVO.setEventRequestId(dto.getEventRequestId());
                    eventCreateVO.setWorkcellId(dto.getWorkcellId());
                    eventCreateVO.setLocatorId(dto.getLocatorId());
                    eventCreateVO.setShiftCode(dto.getShiftCode());
                    eventCreateVO.setShiftDate(dto.getShiftDate());

                    eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                    cv2.setWorkOrderComponentActualId(workOrderComponentActualId);
                    cv2.setTrxAssembleQty(-dto.getTrxAssembleQty());
                    cv2.setEventId(eventId);
                    this.woComponentActualUpdate(tenantId, cv2);
                }
            }
        } else if ("Y".equals(operationAssembleFlag)) {
            if (StringUtils.isEmpty(dto.getAssembleExcessFlag()) || "N".equals(dto.getAssembleExcessFlag())) {
                // 若获取结果为Y，且输入参数assembleExcessFlag未输入或输入N，表示针对非强制装配模式下按照工序进行装配的装配结果进行取消
                if (StringUtils.isEmpty(dto.getBomComponentId())) {
                    throw new MtException("MT_ORDER_0113", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0113", "ORDER", "【API:woComponentAssembleCancel】"));
                }
                if (StringUtils.isEmpty(dto.getRouterStepId())) {
                    throw new MtException("MT_ORDER_0116", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0116", "ORDER", "【API:woComponentAssembleCancel】"));
                }
                ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
                if (ecv08 == null || !"ASSEMBLING".equals(ecv08.getBomComponentType())) {
                    throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:woComponentAssembleCancel】"));
                }
                if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                    v9.setMaterialId(dto.getMaterialId());
                } else {
                    if (StringUtils.isNotEmpty(ecv08.getMaterialId())) {
                        v9.setMaterialId(ecv08.getMaterialId());
                    } else {
                        throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0148", "ORDER", "【API:woComponentAssembleCancel】"));

                    }
                }
                if (StringUtils.isNotEmpty(dto.getOperationId())) {
                    v9.setOperationId(dto.getOperationId());
                } else {
                    MtRouterOperation mtRouterOperation =
                                    mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
                    if (mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                        throw new MtException("MT_ORDER_0149", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0149", "ORDER", "【API:woComponentAssembleCancel】"));
                    }
                    v9.setOperationId(mtRouterOperation.getOperationId());
                }

                v9.setWorkOrderId(dto.getWorkOrderId());
                v9.setBomComponentId(dto.getBomComponentId());
                v9.setRouterStepId(dto.getRouterStepId());
                v9.setComponentType("ASSEMBLING");
                v9.setAssembleExcessFlag("N");
                v9.setBomId(bomId);
                workOrderComponentActualIds = propertyLimitWoComponentAssembleActualQuery(tenantId, v9);
                if (CollectionUtils.isEmpty(workOrderComponentActualIds)) {
                    throw new MtException("MT_ORDER_0114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0114", "ORDER", "【API:woComponentAssembleCancel】"));
                } else if (workOrderComponentActualIds.size() == 1) {
                    workOrderComponentActualId = workOrderComponentActualIds.get(0);
                    eventTypeCode = "WO_COMPONENT_OPERATION_ASSEMBLE_CANCEL";
                    eventCreateVO.setEventTypeCode(eventTypeCode);
                    eventCreateVO.setParentEventId(dto.getParentEventId());
                    eventCreateVO.setEventRequestId(dto.getEventRequestId());
                    eventCreateVO.setWorkcellId(dto.getWorkcellId());
                    eventCreateVO.setLocatorId(dto.getLocatorId());
                    eventCreateVO.setShiftCode(dto.getShiftCode());
                    eventCreateVO.setShiftDate(dto.getShiftDate());
                    eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                    cv2.setWorkOrderComponentActualId(workOrderComponentActualId);
                    cv2.setTrxAssembleQty(-dto.getTrxAssembleQty());
                    cv2.setEventId(eventId);
                    this.woComponentActualUpdate(tenantId, cv2);
                }
            } else if ("Y".equals(dto.getAssembleExcessFlag())) {
                // 若获取结果为Y，且输入参数assembleExcessFlag输入为Y，表示针对强制装配模式下按照工序进行装配的装配实绩进行取消
                if (StringUtils.isEmpty(dto.getMaterialId())) {
                    throw new MtException("MT_ORDER_0115", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0115", "ORDER", "【API:woComponentAssembleCancel】"));
                }
                if (StringUtils.isEmpty(dto.getOperationId())) {
                    throw new MtException("MT_ORDER_0117", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0117", "ORDER", "【API:woComponentAssembleCancel】"));
                }

                v9.setWorkOrderId(dto.getWorkOrderId());
                v9.setMaterialId(dto.getMaterialId());
                v9.setBomComponentId("");
                v9.setOperationId(dto.getOperationId());
                //V20201215 modify by penglin.sui for hui.ma 与HCM客服确认，传入工艺步骤ID
                v9.setRouterStepId(dto.getRouterStepId());
                v9.setComponentType("ASSEMBLING");
                v9.setAssembleExcessFlag("Y");
                v9.setBomId(bomId);
                workOrderComponentActualIds = propertyLimitWoComponentAssembleActualQuery(tenantId, v9);
                if (CollectionUtils.isEmpty(workOrderComponentActualIds)) {
                    throw new MtException("MT_ORDER_0114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0114", "ORDER", "【API:woComponentAssembleCancel】"));
                } else if (workOrderComponentActualIds.size() == 1) {
                    workOrderComponentActualId = workOrderComponentActualIds.get(0);
                    eventTypeCode = "WO_COMPONENT_EXCESS_OPERATION_ASSEMBLE_CANCEL";
                    eventCreateVO.setEventTypeCode(eventTypeCode);
                    eventCreateVO.setParentEventId(dto.getParentEventId());
                    eventCreateVO.setEventRequestId(dto.getEventRequestId());
                    eventCreateVO.setWorkcellId(dto.getWorkcellId());
                    eventCreateVO.setLocatorId(dto.getLocatorId());
                    eventCreateVO.setShiftCode(dto.getShiftCode());
                    eventCreateVO.setShiftDate(dto.getShiftDate());
                    eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                    cv2.setWorkOrderComponentActualId(workOrderComponentActualId);
                    cv2.setTrxAssembleQty(-dto.getTrxAssembleQty());
                    cv2.setEventId(eventId);
                    this.woComponentActualUpdate(tenantId, cv2);
                }
            }
        }

        mtWorkOrderComponentActual = new MtWorkOrderComponentActual();
        mtWorkOrderComponentActual.setTenantId(tenantId);
        mtWorkOrderComponentActual.setWorkOrderComponentActualId(workOrderComponentActualId);
        mtWorkOrderComponentActual = this.mtWorkOrderComponentActualMapper.selectOne(mtWorkOrderComponentActual);
        if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(mtWorkOrderComponentActual.getAssembleQty())) > 0) {
            throw new MtException("MT_ORDER_0118", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0118", "ORDER", "【API:woComponentAssembleCancel】"));
        }

    }

    @Override
    public List<MtWoComponentActualVO4> componentLimitWoComponentScrapActualQuery(Long tenantId,
                    MtWoComponentActualVO5 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001", "ORDER",
                                            "workOrderId", "【API:componentLimitWoComponentScrapActualQuery】"));
        }

        return this.mtWorkOrderComponentActualMapper.componentLimitWoComponentScrapActualQuery(tenantId, dto);
    }

    @Override
    public List<MtWoComponentActualVO4> materialLimitWoComponentScrapActualQuery(Long tenantId,
                    MtWoComponentActualVO19 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:materialLimitWoComponentScrapActualQuery】"));
        }

        return this.mtWorkOrderComponentActualMapper.materialLimitWoComponentScrapActualQuery(tenantId, dto);
    }

    private static List<String> periodUoms = new ArrayList<String>(Arrays.asList("DAY", "HOUR", "MIN"));

    @Override
    public MtWoComponentActualVO13 woComponentAssemblePeriodGet(Long tenantId, MtWoComponentActualVO24 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId()) && StringUtils.isEmpty(dto.getWorkOrderComponentActualId())) {
            throw new MtException("MT_ORDER_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032", "ORDER",
                                            "workOrderId、workOrderComponentActualId",
                                            "【API:woComponentAssemblePeriodGet】"));
        }

        if (StringUtils.isNotEmpty(dto.getPeriodUom()) && !periodUoms.contains(dto.getPeriodUom())) {
            throw new MtException("MT_ORDER_0035",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0035", "ORDER",
                                            "periodUom", "[ DAY、HOUR、MIN]", "【API:woComponentAssemblePeriodGet】"));
        }

        // 默认为 HOUR
        if (StringUtils.isEmpty(dto.getPeriodUom())) {
            dto.setPeriodUom("HOUR");
        }

        MtWorkOrderComponentActual mtWorkOrderComponentActual = new MtWorkOrderComponentActual();
        mtWorkOrderComponentActual.setWorkOrderComponentActualId(dto.getWorkOrderComponentActualId());
        mtWorkOrderComponentActual.setWorkOrderId(dto.getWorkOrderId());
        mtWorkOrderComponentActual.setMaterialId(dto.getMaterialId());
        mtWorkOrderComponentActual.setOperationId(dto.getOperationId());
        mtWorkOrderComponentActual.setTenantId(tenantId);
        List<MtWorkOrderComponentActual> workOrderComponentActuals =
                        mtWorkOrderComponentActualMapper.select(mtWorkOrderComponentActual);
        if (CollectionUtils.isEmpty(workOrderComponentActuals)) {
            return null;
        }
        Date minActualFirstTime = null;
        Date maxActualLastTime = null;

        // 获取结束时间最大值
        if (workOrderComponentActuals.stream().allMatch(t -> t.getActualLastTime() != null)) {
            Optional<MtWorkOrderComponentActual> maxLastTime = workOrderComponentActuals.stream()
                            .max(Comparator.comparing(MtWorkOrderComponentActual::getActualLastTime));
            if (maxLastTime.isPresent()) {
                maxActualLastTime = maxLastTime.get().getActualLastTime();
            }
        }

        // 获取开始时间的最小值
        Optional<MtWorkOrderComponentActual> minFirstTime =
                        workOrderComponentActuals.stream().filter(t -> t.getActualFirstTime() != null)
                                        .min(Comparator.comparing(MtWorkOrderComponentActual::getActualFirstTime));


        if (minFirstTime.isPresent()) {
            minActualFirstTime = minFirstTime.get().getActualFirstTime();
        }

        Double periodTime = null;
        BigDecimal day = null;
        if (minActualFirstTime != null && maxActualLastTime != null) {
            day = new BigDecimal(String.valueOf(maxActualLastTime.getTime()))
                            .subtract(new BigDecimal(String.valueOf(minActualFirstTime.getTime())));
        }


        if (day != null) {
            if ("HOUR".equals(dto.getPeriodUom())) {
                periodTime = day.divide(new BigDecimal(1000 * 60 * 60 + ""), 6, BigDecimal.ROUND_HALF_DOWN)
                                .doubleValue();
            } else if ("DAY".equals(dto.getPeriodUom())) {
                periodTime = day.divide(new BigDecimal(1000 * 60 * 60 * 24 + ""), 6, BigDecimal.ROUND_HALF_DOWN)
                                .doubleValue();
            } else if ("MIN".equals(dto.getPeriodUom())) {
                periodTime = day.divide(new BigDecimal(1000 * 60 + ""), 6, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            }
        }
        MtWoComponentActualVO13 result = new MtWoComponentActualVO13();
        result.setActualFirstTime(minActualFirstTime);
        result.setActualLastTime(maxActualLastTime);
        result.setPeriodUom(dto.getPeriodUom());
        result.setPeriodTime(periodTime);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woComponentSplit(Long tenantId, MtWoComponentActualVO14 dto) {
        if (StringUtils.isEmpty(dto.getSourceWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "sourceWorkOrderId", "【API:woComponentSplit】"));
        }
        if (StringUtils.isEmpty(dto.getTargetWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "targetWorkOrderId", "【API:woComponentSplit】"));
        }

        if (dto.getSplitQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "splitQty", "【API:woComponentSplit】"));
        }

        MtWorkOrder sourceWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, dto.getSourceWorkOrderId());
        if (sourceWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woComponentSplit】"));
        }

        if (BigDecimal.valueOf(dto.getSplitQty()).compareTo(BigDecimal.valueOf(sourceWorkOrder.getQty())) >= 0) {
            throw new MtException("MT_ORDER_0140", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0140", "ORDER", "【API:woComponentSplit】"));
        }


        if (StringUtils.isEmpty(sourceWorkOrder.getBomId())) {
            return;
        }

        if (BigDecimal.valueOf(dto.getSplitQty()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "splitQty", "【API:woComponentSplit】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        String eventId = null;
        String eventTypeCode = null;

        eventTypeCode = "WO_COMPONENT_ACTUAL_SPLIT";
        eventCreateVO.setEventTypeCode(eventTypeCode);
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        this.mtWorkOrderRepository.woBomUpdate(tenantId, dto.getTargetWorkOrderId(), sourceWorkOrder.getBomId(),
                        eventId);

        // 4. 获取来源生产指令组件实绩
        MtWoComponentActualVO5 v5 = new MtWoComponentActualVO5();
        v5.setWorkOrderId(dto.getSourceWorkOrderId());
        v5.setBomId(sourceWorkOrder.getBomId());
        List<MtWoComponentActualVO4> woComponentVO4s = this.componentLimitWoComponentAssembleActualQuery(tenantId, v5);
        if (CollectionUtils.isNotEmpty(woComponentVO4s)) {
            List<MtRouterOpComponentVO> routerOperationComponentList = null;
            MtBomComponentVO8 ecv08;
            List<MtBomSubstituteVO3> bomSubstitutes;
            MtBomSubstituteVO6 bomSubstituteVO6 = new MtBomSubstituteVO6();
            for (MtWoComponentActualVO4 mtWoComponentVO4 : woComponentVO4s) {
                Double perQty = 0.0D;
                Double assembleQty = 0.0D;
                if (!"Y".equals(mtWoComponentVO4.getSubstituteFlag())) {
                    if (StringUtils.isNotEmpty(mtWoComponentVO4.getRouterStepId())) {
                        MtRouterOpComponentVO1 vo1 = new MtRouterOpComponentVO1();
                        vo1.setRouterStepId(mtWoComponentVO4.getRouterStepId());
                        vo1.setBomComponentId(mtWoComponentVO4.getBomComponentId());
                        routerOperationComponentList = mtRouterOperationComponentRepository
                                        .routerOperationComponentPerQtyQuery(tenantId, vo1);

                        // 业务确认：如果有返回值，结果唯一, 没有返回值，perQty为0
                        if (CollectionUtils.isNotEmpty(routerOperationComponentList)) {
                            perQty = routerOperationComponentList.get(0).getPerQty();
                        }

                    } else {
                        ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId,
                                        mtWoComponentVO4.getBomComponentId());
                        if (ecv08 != null && ecv08.getPreQty() != null) {
                            perQty = ecv08.getPreQty();
                        }
                    }
                } else {
                    if (StringUtils.isNotEmpty(mtWoComponentVO4.getRouterStepId())) {
                        MtRouterOpComponentVO1 vo1 = new MtRouterOpComponentVO1();
                        vo1.setRouterStepId(mtWoComponentVO4.getRouterStepId());
                        vo1.setBomComponentId(mtWoComponentVO4.getBomComponentId());
                        routerOperationComponentList = mtRouterOperationComponentRepository
                                        .routerOperationComponentPerQtyQuery(tenantId, vo1);

                        // 业务确认：如果有返回值，结果唯一, 没有返回值，perQty为0
                        if (CollectionUtils.isNotEmpty(routerOperationComponentList)) {
                            bomSubstituteVO6.setBomComponentId(routerOperationComponentList.get(0).getBomComponentId());
                            bomSubstituteVO6.setQty(routerOperationComponentList.get(0).getPerQty());
                            bomSubstituteVO6.setMaterialId(mtWoComponentVO4.getMaterialId());
                            bomSubstitutes = this.mtBomSubstituteRepository.bomSubstituteQtyCalculate(tenantId,
                                            bomSubstituteVO6);

                            if (CollectionUtils.isNotEmpty(bomSubstitutes)) {
                                perQty = bomSubstitutes.get(0).getComponentQty();
                            } else {
                                perQty = 0.0D;
                            }
                        }
                    } else {
                        ecv08 = mtBomComponentRepository.bomComponentBasicGet(tenantId,
                                        mtWoComponentVO4.getBomComponentId());
                        if (ecv08 != null && ecv08.getPreQty() != null) {
                            bomSubstituteVO6.setBomComponentId(ecv08.getBomComponentId());
                            bomSubstituteVO6.setQty(ecv08.getPreQty());
                            bomSubstituteVO6.setMaterialId(ecv08.getMaterialId());
                            bomSubstitutes = this.mtBomSubstituteRepository.bomSubstituteQtyCalculate(tenantId,
                                            bomSubstituteVO6);

                            if (CollectionUtils.isNotEmpty(bomSubstitutes)) {
                                perQty = bomSubstitutes.get(0).getComponentQty();
                            } else {
                                perQty = 0.0D;
                            }
                        }
                    }
                }

                // 以上逻辑计算perQty
                MtWoComponentActualVO2 cv2 = new MtWoComponentActualVO2();
                if (BigDecimal.valueOf(perQty).compareTo(BigDecimal.ZERO) == 0) {
                    cv2.setWorkOrderComponentActualId(mtWoComponentVO4.getWorkOrderComponentActualId());
                    cv2.setAssembleQty(mtWoComponentVO4.getAssembleQty());
                    cv2.setEventId(eventId);
                    this.woComponentActualUpdate(tenantId, cv2);
                } else {
                    assembleQty = Math.min(mtWoComponentVO4.getAssembleQty(),
                                    BigDecimal.valueOf(sourceWorkOrder.getQty())
                                                    .subtract(BigDecimal.valueOf(dto.getSplitQty()))
                                                    .multiply(BigDecimal.valueOf(perQty)).doubleValue());
                    cv2.setWorkOrderComponentActualId(mtWoComponentVO4.getWorkOrderComponentActualId());
                    cv2.setAssembleQty(assembleQty);
                    cv2.setEventId(eventId);
                    this.woComponentActualUpdate(tenantId, cv2);

                    // 更新目标工单
                    cv2 = new MtWoComponentActualVO2();
                    cv2.setEventId(eventId);
                    cv2.setWorkOrderId(dto.getTargetWorkOrderId());
                    cv2.setMaterialId(mtWoComponentVO4.getMaterialId());
                    cv2.setOperationId(mtWoComponentVO4.getOperationId());
                    cv2.setAssembleQty(BigDecimal.valueOf(mtWoComponentVO4.getAssembleQty())
                                    .subtract(BigDecimal.valueOf(assembleQty)).doubleValue());

                    cv2.setComponentType(mtWoComponentVO4.getComponentType());
                    cv2.setBomComponentId(mtWoComponentVO4.getBomComponentId());
                    cv2.setBomId(mtWoComponentVO4.getBomId());
                    cv2.setRouterStepId(mtWoComponentVO4.getRouterStepId());
                    cv2.setAssembleExcessFlag(mtWoComponentVO4.getAssembleExcessFlag());
                    cv2.setAssembleRouterType(mtWoComponentVO4.getAssembleRouterType());
                    cv2.setSubstituteFlag(mtWoComponentVO4.getSubstituteFlag());
                    cv2.setActualFirstTime(mtWoComponentVO4.getActualFirstTime());
                    cv2.setActualLastTime(mtWoComponentVO4.getActualLastTime());

                    this.woComponentActualUpdate(tenantId, cv2);
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woComponentMerge(Long tenantId, MtWoComponentActualVO15 dto) {
        if (StringUtils.isEmpty(dto.getPrimaryWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "primaryWorkOrderId", "【API:woComponentMerge】"));
        }
        if (CollectionUtils.isEmpty(dto.getSecondaryWorkOrderIds())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "secondaryWorkOrderIds", "【API:woComponentMerge】"));
        }
        if (StringUtils.isEmpty(dto.getTargetWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "targetWorkOrderId", "【API:woComponentMerge】"));
        }
        List<String> woIds = new ArrayList<String>();
        woIds.add(dto.getPrimaryWorkOrderId());
        woIds.addAll(dto.getSecondaryWorkOrderIds());
        List<MtWorkOrder> workOrders = this.mtWorkOrderRepository.woPropertyBatchGet(tenantId, woIds);
        if (CollectionUtils.isEmpty(workOrders) || workOrders.size() != woIds.size()) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woComponentMerge】"));
        }

        Optional<MtWorkOrder> primaryWorkOrderOp = workOrders.stream()
                        .filter(t -> dto.getPrimaryWorkOrderId().equals(t.getWorkOrderId())).findFirst();
        if (!primaryWorkOrderOp.isPresent()) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woComponentMerge】"));
        }
        MtWorkOrder primaryWorkOrder = primaryWorkOrderOp.get();

        // 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        String eventId = null;
        String eventTypeCode = null;
        eventTypeCode = "WO_COMPONENT_ACTUAL_MERGE";
        eventCreateVO.setEventTypeCode(eventTypeCode);
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        // primarySource_bomId不为空，根据来源生产指令装配清单更新目标生产指令装配清单
        if (StringUtils.isNotEmpty(primaryWorkOrder.getBomId())) {
            this.mtWorkOrderRepository.woBomUpdate(tenantId, dto.getTargetWorkOrderId(), primaryWorkOrder.getBomId(),
                            eventId);
        }

        MtWoComponentActualVO19 mtWoComponentVO19 = new MtWoComponentActualVO19();

        // 获取所有需要汇总的结果
        List<MtWoComponentActualVO4> allWoComponentVO4 = new ArrayList<MtWoComponentActualVO4>();
        MtWoComponentActualVO2 v2 = null;
        for (MtWorkOrder w : workOrders) {
            mtWoComponentVO19.setWorkOrderId(w.getWorkOrderId());
            List<MtWoComponentActualVO4> v4s =
                            this.materialLimitWoComponentAssembleActualQuery(tenantId, mtWoComponentVO19);

            if (CollectionUtils.isEmpty(v4s)) {
                continue;
            }
            // 记录需要汇总的结果
            allWoComponentVO4.addAll(v4s);
            // 第五步-a
            for (MtWoComponentActualVO4 v4 : v4s) {
                v2 = new MtWoComponentActualVO2();
                v2.setWorkOrderComponentActualId(v4.getWorkOrderComponentActualId());
                v2.setAssembleQty(0.0D);
                v2.setScrappedQty(0.0D);
                v2.setEventId(eventId);
                this.woComponentActualUpdate(tenantId, v2);
            }
        }

        // 分组获取
        Map<String, List<MtWoComponentActualVO4>> groupResult =
                        allWoComponentVO4.stream().collect(Collectors.groupingBy((t -> {
                            return t.getMaterialId() + "," + t.getOperationId() + "," + t.getComponentType() + ","
                                            + t.getBomComponentId() + "," + t.getRouterStepId() + "," + t.getBomId();
                        })));
        // 每一组结果进行新增目标生产指令组件实绩装配
        for (Map.Entry<String, List<MtWoComponentActualVO4>> entry : groupResult.entrySet()) {
            List<MtWoComponentActualVO4> ever = entry.getValue();
            BigDecimal assembleQty = ever.stream().filter(c -> null != c.getAssembleQty())
                            .collect(CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(c.getAssembleQty())));
            BigDecimal scrappedQty = ever.stream().filter(c -> null != c.getScrappedQty())
                            .collect(CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(c.getScrappedQty())));

            String[] ks = entry.getKey().split(",", -1);
            v2 = new MtWoComponentActualVO2();
            v2.setWorkOrderId(dto.getTargetWorkOrderId());
            v2.setEventId(eventId);
            v2.setMaterialId(parse(ks[0]));
            v2.setOperationId(parse(ks[1]));
            v2.setTrxAssembleQty(assembleQty.doubleValue());
            v2.setTrxScrappedQty(scrappedQty.doubleValue());
            v2.setComponentType(parse(ks[2]));
            v2.setBomComponentId(parse(ks[3]));
            v2.setRouterStepId(parse(ks[4]));
            v2.setBomId(parse(ks[5]));
            self().woComponentActualUpdate(tenantId, v2);
        }
    }

    @Override
    public void woComponentUpdateVerify(Long tenantId, MtWoComponentActualVO25 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woComponentUpdateVerify】"));
        }
        if (StringUtils.isEmpty(dto.getBomComponentId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "bomComponentId", "【API:woComponentUpdateVerify】"));
        }

        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "materialId", "【API:woComponentUpdateVerify】"));
        }

        if (dto.getQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "qty", "【API:woComponentUpdateVerify】"));
        }

        MtWorkOrder mtWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woComponentUpdateVerify】"));
        }

        if (StringUtils.isEmpty(mtWorkOrder.getBomId())) {
            throw new MtException("MT_ORDER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0005", "ORDER", "【API：woComponentUpdateVerify】"));
        }

        String bomAutoRevisionFlag = this.mtBomRepository.bomAutoRevisionGet(tenantId, mtWorkOrder.getBomId());

        if ("Y".equals(bomAutoRevisionFlag)) {
            this.mtWorkOrderRepository.wobomUpdateValidate(tenantId, mtWorkOrder.getWorkOrderId(),
                            mtWorkOrder.getBomId());
            return;
        } else {
            List<String> statusComponentUpdateVerify = Arrays.asList("NEW", "HOLD");
            if (mtWorkOrder.getStatus() == null || !statusComponentUpdateVerify.contains(mtWorkOrder.getStatus())) {
                throw new MtException("MT_ORDER_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0044", "ORDER", "[‘NEW’，‘HOLD’]", "【API：woComponentUpdateVerify】"));
            }

            MtWoComponentActualVO5 v5 = new MtWoComponentActualVO5();
            v5.setWorkOrderId(dto.getWorkOrderId());
            v5.setBomId(mtWorkOrder.getBomId());
            v5.setBomComponentId(dto.getBomComponentId());
            List<MtWoComponentActualVO4> v4s = this.componentLimitWoComponentAssembleActualQuery(tenantId, v5);
            if (CollectionUtils.isNotEmpty(v4s)) {
                BigDecimal sumAssembleQty = BigDecimal.ZERO;
                for (MtWoComponentActualVO4 v4 : v4s) {
                    if (v4.getAssembleQty() != null) {
                        sumAssembleQty = sumAssembleQty.add(BigDecimal.valueOf(v4.getAssembleQty()));
                    }
                }
                for (MtWoComponentActualVO4 v4 : v4s) {
                    if ((v4.getAssembleQty() != null
                                    && BigDecimal.valueOf(v4.getAssembleQty()).compareTo(BigDecimal.ZERO) != 0)
                                    || (v4.getScrappedQty() != null && BigDecimal.valueOf(v4.getScrappedQty())
                                                    .compareTo(BigDecimal.ZERO) != 0)) {
                        if (!dto.getMaterialId().equals(v4.getMaterialId())) {
                            throw new MtException("MT_ORDER_0133", mtErrorMessageRepository.getErrorMessageWithModule(
                                            tenantId, "MT_ORDER_0133", "ORDER", "【API：woComponentUpdateVerify】"));
                        } else {
                            if (sumAssembleQty.compareTo(BigDecimal.valueOf(dto.getQty())) > 0) {
                                throw new MtException("MT_ORDER_0134",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "MT_ORDER_0134", "ORDER",
                                                                "【API：woComponentUpdateVerify】"));
                            }
                        }

                    }
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woComponentUpdate(Long tenantId, MtWoComponentActualVO16 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woComponentUpdate】"));
        }
        if (CollectionUtils.isEmpty(dto.getLines())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "lineNumber", "【API:woComponentUpdate】"));
        }
        for (MtBomComponentVO9 v16 : dto.getLines()) {
            if (v16.getLineNumber() == null) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "lineNumber", "【API:woComponentUpdate】"));
            }
            if (v16.getQty() == null) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "qty", "【API:woComponentUpdate】"));
            }
            if (StringUtils.isEmpty(v16.getMaterialId())) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "materialId", "【API:woComponentUpdate】"));
            }
            if (StringUtils.isEmpty(v16.getBomComponentType())) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "bomComponentType", "【API:woComponentUpdate】"));
            }
            if (v16.getDateFrom() == null) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "dateFrom", "【API:woComponentUpdate】"));
            }
        }

        MtWorkOrder mtWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woComponentUpdate】"));
        }

        if (StringUtils.isEmpty(mtWorkOrder.getBomId())) {
            throw new MtException("MT_ORDER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0005", "ORDER", "【API：woComponentUpdate】"));
        }
        MtBomComponentVO10 v10 = new MtBomComponentVO10();
        v10.setBomId(mtWorkOrder.getBomId());
        v10.setBomComponents(dto.getLines());
        MtBomComponentVO15 mtBomComponentVO15 = this.mtBomComponentRepository.bomComponentUpdate(tenantId, v10,"N");
        String bomId = null;
        if (null != mtBomComponentVO15) {
            bomId = mtBomComponentVO15.getBomId();
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        String eventId = null;
        String eventTypeCode = null;

        eventTypeCode = "WO_COMPONENT_UPDATE";
        eventCreateVO.setEventTypeCode(eventTypeCode);
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        this.mtWorkOrderRepository.woBomUpdate(tenantId, dto.getWorkOrderId(), bomId, eventId);
    }

    @Override
    public List<MtWoComponentActualVO28> propertyLimitWoComponentActualPropertyQuery(Long tenantId,
                                                                                     MtWoComponentActualVO9 dto) {
        // 条件查询数据
        List<MtWorkOrderComponentActual> actuals = this.mtWorkOrderComponentActualMapper
                .propertyLimitWoComponentActualPropertyQuery(tenantId, dto);

        if (CollectionUtils.isNotEmpty(actuals)) {
            List<MtWoComponentActualVO28> result = new ArrayList<>();
            // 第二步
            Map<String, String> operationNames = new HashMap<>(0);
            // 根据第一步获取到的工艺 operationId列表，调用API{operationBatchGet}获取站点编码和站点描述
            List<String> operationIds = actuals.stream().map(MtWorkOrderComponentActual::getOperationId)
                    .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(operationIds)) {
                List<MtOperation> mtOperations = mtOperationRepository.operationBatchGet(tenantId, operationIds);
                if (CollectionUtils.isNotEmpty(mtOperations)) {
                    operationNames = mtOperations.stream().collect(
                            Collectors.toMap(MtOperation::getOperationId, MtOperation::getOperationName));
                }
            }

            // 根据第一步获取到的物料 materialId列表，调用API{materialPropertyBatchGet }获取物料编码和物料描述
            Map<String, MtMaterialVO> materialMap = new HashMap<>(0);
            List<String> materialIds = actuals.stream().map(MtWorkOrderComponentActual::getMaterialId)
                    .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(materialIds)) {
                List<MtMaterialVO> mtMaterialVOS =
                        this.mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);
                if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
                    materialMap = mtMaterialVOS.stream().collect(Collectors.toMap(MtMaterialVO::getMaterialId, t -> t));
                }
            }



            Map<String, String> bomNames = new HashMap<>(0);
            // 根据第一步获取到的装配清单 bomId列表，调用API{bomBasicBatchGet }获取装配清单描述
            List<String> bomIds = actuals.stream().map(MtWorkOrderComponentActual::getBomId)
                    .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(bomIds)) {
                List<MtBomVO7> bomVO7s = this.mtBomRepository.bomBasicBatchGet(tenantId, bomIds);
                if (CollectionUtils.isNotEmpty(bomVO7s)) {
                    bomNames = bomVO7s.stream().collect(Collectors.toMap(MtBomVO7::getBomId, MtBomVO7::getBomName));
                }
            }


            Map<String, String> routerStepNames = new HashMap<>(0);
            // 根据第一步获取到的工艺路线步骤 routerStepId列表，调用API{ routerStepBatchGet }获取步骤识别码
            List<String> routerStepIds = actuals.stream().map(MtWorkOrderComponentActual::getRouterStepId)
                    .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(routerStepIds)) {
                List<MtRouterStep> routerSteps = mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIds);
                if (CollectionUtils.isNotEmpty(routerSteps)) {
                    routerStepNames = routerSteps.stream().collect(
                            Collectors.toMap(MtRouterStep::getRouterStepId, MtRouterStep::getStepName));
                }
            }

            Map<String, String> workOrderNums = new HashMap<>(0);
            // 根据第一步获取到的生产指令 woId列表，调用API{ woPropertyBatchGet }获取生产指令编号
            List<String> woIds = actuals.stream().map(MtWorkOrderComponentActual::getWorkOrderId)
                    .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(woIds)) {
                List<MtWorkOrder> mtWorkOrders = this.mtWorkOrderRepository.woPropertyBatchGet(tenantId, woIds);
                if (CollectionUtils.isNotEmpty(mtWorkOrders)) {
                    workOrderNums = mtWorkOrders.stream().collect(
                            Collectors.toMap(MtWorkOrder::getWorkOrderId, MtWorkOrder::getWorkOrderNum));
                }
            }

            for (MtWorkOrderComponentActual actual : actuals) {
                MtWoComponentActualVO28 vo28 = new MtWoComponentActualVO28();
                vo28.setWorkOrderComponentActualId(actual.getWorkOrderComponentActualId());
                vo28.setWorkOrderId(actual.getWorkOrderId());
                vo28.setMaterialId(actual.getMaterialId());
                vo28.setOperationId(actual.getOperationId());
                vo28.setAssembleQty(actual.getAssembleQty());
                vo28.setScrappedQty(actual.getScrappedQty());
                vo28.setComponentType(actual.getComponentType());
                vo28.setBomComponentId(actual.getBomComponentId());
                vo28.setBomId(actual.getBomId());
                vo28.setRouterStepId(actual.getRouterStepId());
                vo28.setAssembleExcessFlag(actual.getAssembleExcessFlag());
                vo28.setAssembleRouterType(actual.getAssembleRouterType());
                vo28.setSubstituteFlag(actual.getSubstituteFlag());
                vo28.setActualFirstTime(actual.getActualFirstTime());
                vo28.setActualLastTime(actual.getActualLastTime());
                vo28.setOperationName(operationNames.get(actual.getOperationId()));

                MtMaterialVO materialVO = materialMap.get(actual.getMaterialId());
                vo28.setMaterialCode(null != materialVO ? materialVO.getMaterialCode() : null);
                vo28.setMaterialName(null != materialVO ? materialVO.getMaterialName() : null);
                vo28.setBomName(bomNames.get(actual.getBomId()));
                vo28.setRouterStepName(routerStepNames.get(actual.getRouterStepId()));
                vo28.setWoNum(workOrderNums.get(actual.getWorkOrderId()));
                result.add(vo28);
            }
            return result.stream().sorted(Comparator
                    .comparingDouble((MtWoComponentActualVO28 c) -> Double.valueOf(
                            StringUtils.isEmpty(c.getWorkOrderId()) ? "0" : c.getWorkOrderId()))
                    .thenComparingDouble(c -> Double
                            .valueOf(StringUtils.isEmpty(c.getMaterialId()) ? "0" : c.getMaterialId()))
                    .thenComparingDouble(
                            c -> Double.valueOf(StringUtils.isEmpty(c.getBomId()) ? "0" : c.getBomId()))
                    .thenComparingDouble(c -> Double.valueOf(
                            StringUtils.isEmpty(c.getRouterStepId()) ? "0" : c.getRouterStepId()))
                    .thenComparingDouble(c -> Double.valueOf(
                            StringUtils.isEmpty(c.getOperationId()) ? "0" : c.getOperationId())))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public static String toString(Object o) {
        return o == null ? "null" : o.toString();
    }

    public static String parse(String s) {
        return "null".equals(s) ? null : s;
    }

    /***
     * 将wocas转换到v4s中
     *
     * @param v4s
     * @param wocas
     */
    public static void process(List<MtWoComponentActualVO4> v4s, List<MtWorkOrderComponentActual> wocas) {
        MtWoComponentActualVO4 v4;
        if (CollectionUtils.isNotEmpty(wocas)) {
            for (MtWorkOrderComponentActual e : wocas) {
                v4 = new MtWoComponentActualVO4();
                v4.setComponentType(e.getComponentType());
                v4.setWorkOrderId(e.getWorkOrderId());
                v4.setOperationId(e.getOperationId());
                v4.setBomId(e.getBomId());
                v4.setMaterialId(e.getMaterialId());
                v4.setScrappedQty(e.getScrappedQty());
                v4.setWorkOrderComponentActualId(e.getWorkOrderComponentActualId());
                v4.setSubstituteFlag(e.getSubstituteFlag());
                v4.setRouterStepId(e.getRouterStepId());
                v4.setActualFirstTime(e.getActualFirstTime());
                v4.setActualLastTime(e.getActualLastTime());
                v4.setAssembleExcessFlag(e.getAssembleExcessFlag());
                v4.setAssembleQty(e.getAssembleQty());
                v4.setAssembleRouterType(e.getAssembleRouterType());
                v4.setBomComponentId(e.getBomComponentId());
                v4s.add(v4);
            }

            // 排序
            v4s.sort(new Comparator<MtWoComponentActualVO4>() {
                @Override
                public int compare(MtWoComponentActualVO4 o1, MtWoComponentActualVO4 o2) {
                    int r = o1.getBomComponentId().compareTo(o2.getBomComponentId());
                    if (r != 0) {
                        return r;
                    } else {
                        r = o1.getOperationId().compareTo(o2.getOperationId());
                        if (r != 0) {
                            return r;
                        } else {
                            return o1.getMaterialId().compareTo(o2.getMaterialId());
                        }
                    }
                }
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woComponentActualBatchUpdate(Long tenantId, MtWoComponentActualVO29 dto) {
        String apiName = "【API:woComponentActualBatchUpdate】";
        if (MtIdHelper.isIdNull(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001","ORDER",
                    "eventId", apiName));
        }
        if (dto.getWoInfoList().stream().anyMatch(t -> t.getTrxAssembleQty() != null)
                && dto.getWoInfoList().stream().anyMatch(t -> t.getAssembleQty() != null)) {
            throw new MtException("MT_ORDER_0102", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0102","ORDER",
                    "assembleQty、trxAssembleQty", apiName));
        }

        if (dto.getWoInfoList().stream().anyMatch(t -> t.getScrappedQty() != null)
                && dto.getWoInfoList().stream().anyMatch(t -> t.getTrxScrappedQty() != null)) {
            throw new MtException("MT_ORDER_0102", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0102","ORDER",
                    "scrappedQty、trxScrappedQty", apiName));
        }

        if (dto.getWoInfoList().stream().anyMatch(t -> MtIdHelper.isIdNull(t.getWorkOrderComponentActualId()))
                && dto.getWoInfoList().stream().anyMatch(t -> MtIdHelper.isIdNull(t.getWorkOrderId()))) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032","ORDER",
                    "workOrderComponentActualId、workOrderId", apiName));
        }

        List<MtWoComponentActualVO30> inputActualList = dto.getWoInfoList();
        // 入參個數
        int inputDataSize = inputActualList.size();

        List<MtWorkOrder> mtWorkOrderList;
        List<String> mtWorkOrderIdList;
        List<MtBomComponentVO13> bomComponentList;
        List<String> bomComponentIdList;
        List<MtRouterOperation> routerOperationList;
        List<String> routerOperationIdList = new ArrayList<>();
        List<String> materialIdList = new ArrayList<>();
        List<String> bomIdList = new ArrayList<>();
        List<String> routerStepIdList;
        List<String> componentTypeList = new ArrayList<>();
        mtWorkOrderIdList = dto.getWoInfoList().stream().map(MtWoComponentActualVO30::getWorkOrderId).distinct()
                .collect(Collectors.toList());
        bomComponentIdList = dto.getWoInfoList().stream().map(MtWoComponentActualVO30::getBomComponentId).distinct()
                .filter(MtIdHelper::isIdNotNull).collect(Collectors.toList());
        routerStepIdList = dto.getWoInfoList().stream().map(MtWoComponentActualVO30::getRouterStepId).distinct()
                .filter(MtIdHelper::isIdNotNull).collect(Collectors.toList());
        // 获取生产指令属性
        Map<String, MtWorkOrder> mtWorkOrderMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(mtWorkOrderIdList)) {
            mtWorkOrderList = this.mtWorkOrderRepository.woPropertyBatchGet(tenantId, mtWorkOrderIdList);
            if (CollectionUtils.isEmpty(mtWorkOrderList)) {
                throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0006","ORDER",
                        "【API:woComponentActualBatchUpdate】"));
            }
            mtWorkOrderMap = mtWorkOrderList.stream().collect(Collectors.toMap(MtWorkOrder::getWorkOrderId, c -> c));
        }

        // 根据输入参数 BomComponentId 获取BomComponent基础信息
        Map<String, MtBomComponentVO13> bomComponentMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(bomComponentIdList)) {
            bomComponentList = mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIdList);
            if (CollectionUtils.isEmpty(bomComponentList)) {
                throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0148","ORDER",
                        "【API:woComponentActualBatchUpdate】"));
            }
            bomComponentMap = bomComponentList.stream()
                    .collect(Collectors.toMap(MtBomComponentVO13::getBomComponentId, c -> c));
        }

        // 根据工艺步骤，查询工艺信息
        Map<String, String> routerOperationMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(routerStepIdList)) {
            routerOperationList = mtRouterOperationRepository.routerOperationBatchGet(tenantId, routerStepIdList);
            routerOperationMap = routerOperationList.stream().collect(
                    Collectors.toMap(MtRouterOperation::getRouterStepId, MtRouterOperation::getOperationId));
        }

        List<MtWoComponentActualVO30> noPrimayKeyDataList = new ArrayList<>();
        List<String> workOrderComponentActualIdList = new ArrayList<>();

        // int noPrimayKeyNum = 0;
        for (MtWoComponentActualVO30 mtWoComponentActualVO30 : dto.getWoInfoList()) {
            if (MtIdHelper.isIdNull(mtWoComponentActualVO30.getWorkOrderComponentActualId())) {
                MtBomComponentVO13 bomComponent = null;
                if (MtIdHelper.isIdNotNull(mtWoComponentActualVO30.getBomComponentId())) {
                    bomComponent = bomComponentMap.get(mtWoComponentActualVO30.getBomComponentId());
                }

                // 给bomComponentId设置默认值
                mtWoComponentActualVO30.setBomComponentId(MtFieldsHelper.getOrDefault(
                        mtWoComponentActualVO30.getBomComponentId(), ""));

                // 获取物料Id
                if (MtIdHelper.isIdNull(mtWoComponentActualVO30.getMaterialId())) {
                    if (bomComponent == null) {
                        throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0148","ORDER", "【API:woComponentActualBatchUpdate】"));
                    }
                    mtWoComponentActualVO30.setMaterialId(bomComponent.getMaterialId());

                }
                if (MtIdHelper.isIdNull(mtWoComponentActualVO30.getBomId())) {
                    MtWorkOrder mtWorkOrder = mtWorkOrderMap.get(mtWoComponentActualVO30.getWorkOrderId());
                    if (mtWorkOrder != null) {
                        mtWoComponentActualVO30.setBomId(mtWorkOrder.getBomId());
                    } else {
                        mtWoComponentActualVO30.setBomId("");
                    }
                }
                if (MtIdHelper.isIdNull(dto.getOperationId())) {
                    if (MtIdHelper.isIdNotNull(mtWoComponentActualVO30.getRouterStepId())) {
                        String operationId = routerOperationMap.get(mtWoComponentActualVO30.getRouterStepId());
                        dto.setOperationId(MtFieldsHelper.getOrDefault(operationId, ""));
                    } else {
                        dto.setOperationId("");
                    }
                }

                if (StringUtils.isEmpty(mtWoComponentActualVO30.getComponentType())) {
                    if (bomComponent != null) {
                        mtWoComponentActualVO30.setComponentType(bomComponent.getBomComponentType());
                    } else {
                        mtWoComponentActualVO30.setComponentType("ASSEMBLING");
                    }

                }

                // materialIdList.add(mtWoComponentActualVO30.getMaterialId());
                // bomIdList.add(mtWoComponentActualVO30.getBomId());
                // routerOperationIdList.add(dto.getOperationId());
                // componentTypeList.add(mtWoComponentActualVO30.getComponentType());
                // mtWorkOrderIdList.add(mtWoComponentActualVO30.getWorkOrderId());
                // routerStepIdList.add(mtWoComponentActualVO30.getRouterStepId());
                // bomComponentIdList.add(mtWoComponentActualVO30.getBomComponentId());
                // noPrimayKeyNum++;
                noPrimayKeyDataList.add(mtWoComponentActualVO30);
            } else {
                workOrderComponentActualIdList.add(mtWoComponentActualVO30.getWorkOrderComponentActualId());
            }
        }

        List<MtWorkOrderComponentActual> mtWorkOrderComponentActualList = new ArrayList<>(dto.getWoInfoList().size());
        // if (noPrimayKeyNum > 0) {
        if (CollectionUtils.isNotEmpty(noPrimayKeyDataList)) {
            /*SecurityTokenHelper.close();
            mtWorkOrderComponentActualList = self().selectByCondition(Condition
                    .builder(MtWorkOrderComponentActual.class)
                    .andWhere(Sqls
                            .custom().andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                            .andIn(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrderIdList))
                    .orWhere(Sqls.custom()
                            .andIn(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID,
                                    bomComponentIdList, true))
                    .orWhere(Sqls.custom()
                            .andIn(MtWorkOrderComponentActual.FIELD_MATERIAL_ID, materialIdList, true))
                    .orWhere(Sqls.custom()
                            .andIn(MtWorkOrderComponentActual.FIELD_COMPONENT_TYPE, componentTypeList,
                                    true))
                    .orWhere(Sqls.custom().andIn(MtWorkOrderComponentActual.FIELD_BOM_ID, bomIdList, true))
                    .orWhere(Sqls.custom()
                            .andIn(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routerStepIdList,
                                    true))
                    .orWhere(Sqls.custom().andIn(MtWorkOrderComponentActual.FIELD_OPERATION_ID,
                            routerOperationIdList, true))
                    .build());*/

            Condition.Builder builder = Condition.builder(MtWorkOrderComponentActual.class);
            for (MtWoComponentActualVO30 noPrimayKeyData : noPrimayKeyDataList) {
                Sqls sql = Sqls.custom().andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtWorkOrderComponentActual.FIELD_OPERATION_ID,
                                                MtFieldsHelper.getOrDefault(dto.getOperationId(), STRING_SPECIAL))
                                .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID,
                                                MtFieldsHelper.getOrDefault(noPrimayKeyData.getWorkOrderId(),
                                                                STRING_SPECIAL))
                                .andEqualTo(MtWorkOrderComponentActual.FIELD_MATERIAL_ID,
                                                MtFieldsHelper.getOrDefault(noPrimayKeyData.getMaterialId(),
                                                                STRING_SPECIAL))
                                .andEqualTo(MtWorkOrderComponentActual.FIELD_COMPONENT_TYPE,
                                                MtFieldsHelper.getOrDefault(noPrimayKeyData.getComponentType(),
                                                                STRING_SPECIAL))
                                .andEqualTo(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID,
                                                MtFieldsHelper.getOrDefault(noPrimayKeyData.getBomComponentId(),
                                                                STRING_SPECIAL))
                                .andEqualTo(MtWorkOrderComponentActual.FIELD_BOM_ID,
                                                MtFieldsHelper.getOrDefault(noPrimayKeyData.getBomId(), STRING_SPECIAL))
                                .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, MtFieldsHelper
                                                .getOrDefault(noPrimayKeyData.getRouterStepId(), STRING_SPECIAL));
                builder.orWhere(sql);
            }

            SecurityTokenHelper.close();
            mtWorkOrderComponentActualList = self().selectByCondition(builder.build());
        }

        if (CollectionUtils.isNotEmpty(workOrderComponentActualIdList)) {
            workOrderComponentActualIdList =
                    workOrderComponentActualIdList.stream().distinct().collect(Collectors.toList());
            SecurityTokenHelper.close();
            List<MtWorkOrderComponentActual> actualList = self().selectByCondition(Condition
                    .builder(MtWorkOrderComponentActual.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                            .andIn(MtWorkOrderComponentActual.FIELD_WORK_ORDER_COMPONENT_ACTUAL_ID,
                                    workOrderComponentActualIdList))
                    .build());

            if (actualList.size() != workOrderComponentActualIdList.size()) {
                throw new MtException("MT_ORDER_0101",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0101","ORDER", apiName));
            }
            mtWorkOrderComponentActualList.addAll(actualList);
        }
        // 根据唯一索引建立的实体映射
        Map<MtWoComponentActualTupleVO, MtWorkOrderComponentActual> actualMap = mtWorkOrderComponentActualList.stream()
                .collect(Collectors.toMap(t -> new MtWoComponentActualTupleVO(t.getWorkOrderId(),
                        t.getMaterialId(), t.getOperationId(), t.getComponentType(),
                        t.getBomComponentId(), t.getBomId(), t.getRouterStepId()), c -> c));
        // 根据主键建立的映射
        Map<String, MtWorkOrderComponentActual> existActualIdMap = mtWorkOrderComponentActualList.stream()
                .collect(Collectors.toMap(MtWorkOrderComponentActual::getWorkOrderComponentActualId, t -> t,
                        (t1, t2) -> t1));

        // 需要新增的个数
        int insertDataNum = 0;
        // 计算需要新增的数据个数，并提前查询数据库已存在的数据
        Set<MtWoComponentActualTupleVO> insertDataSet = new HashSet<>(inputDataSize);
        for (int i = 0; i < inputActualList.size(); i++) {
            MtWoComponentActualVO30 mtWoComponentActualVO30 = inputActualList.get(i);
            if (MtIdHelper.isIdNull(mtWoComponentActualVO30.getWorkOrderComponentActualId())) {
                MtWoComponentActualTupleVO workComponentActualVO31 = workOrderComponentActualTranMapper
                        .woComponentActualVO30ToActualTupleVO(mtWoComponentActualVO30);
                workComponentActualVO31.setOperationId(dto.getOperationId());
                // 存在处理相同的数据，直接报错
                if (!insertDataSet.add(workComponentActualVO31)) {
                    throw new MtException("MT_ASSEMBLE_0082",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0082","ASSEMBLE",
                                    workComponentActualVO31.toString(),
                                    apiName));
                }
                if (!actualMap.containsKey(workComponentActualVO31)) {
                    insertDataNum++;
                }
            } else {
                // 传入了主键的数据
                MtWoComponentActualTupleVO workComponentActualVO31 = workOrderComponentActualTranMapper
                        .woComponentActualVO30ToActualTupleVO(mtWoComponentActualVO30);
                workComponentActualVO31.setOperationId(dto.getOperationId());
                // 存在处理相同的数据，直接报错
                if (!insertDataSet.add(workComponentActualVO31)) {
                    throw new MtException("MT_ASSEMBLE_0082", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0082","ASSEMBLE", workComponentActualVO31.toString(), apiName));
                }
            }
        }

        // 新增或更新的数据集
        List<MtWorkOrderCompActualHis> insertHisList = new ArrayList<>(inputActualList.size());
        List<MtWorkOrderComponentActual> insertList = new ArrayList<>(inputActualList.size());
        List<MtWorkOrderComponentActual> updateList = new ArrayList<>(inputActualList.size());

        // 获取主键序列
        List<String> ids = new ArrayList<>();
        if (insertDataNum > 0) {
            SequenceInfo woCompActualSeq = MtSqlHelper.getSequenceInfo(MtWorkOrderComponentActual.class);
            ids = mtCustomDbRepository.getNextKeys(woCompActualSeq.getPrimarySequence(), insertDataNum);
        }

        SequenceInfo woCompActualHisSeq = MtSqlHelper.getSequenceInfo(MtWorkOrderCompActualHis.class);
        List<String> hisIds = mtCustomDbRepository.getNextKeys(woCompActualHisSeq.getPrimarySequence(),
                inputActualList.size());

        // 更新或新增数据
        int inserDataIndex = 0;
        int handleIndex = 0;
        for (MtWoComponentActualVO30 v : inputActualList) {
            Double trxScrappedQty = 0.0D;
            Double trxAssembleQty = 0.0D;
            MtWoComponentActualTupleVO workComponentActualVO31 = new MtWoComponentActualTupleVO(v.getWorkOrderId(),
                    v.getMaterialId(), dto.getOperationId(), v.getComponentType(), v.getBomComponentId(),
                    v.getBomId(), v.getRouterStepId());
            MtWorkOrderComponentActual mtWorkOrderComponentActual = new MtWorkOrderComponentActual();

            MtWorkOrderComponentActual existActual = null;
            // 判断是新增还是更新
            if (MtIdHelper.isIdNotNull(v.getWorkOrderComponentActualId())) {
                // 更新
                existActual = existActualIdMap.get(v.getWorkOrderComponentActualId());

            } else if (actualMap.containsKey(workComponentActualVO31)) {
                existActual = actualMap.get(workComponentActualVO31);
            }

            // 更新逻辑
            if (existActual != null) {

                // 记录更新前装配数量
                Double oldAssembleQty = existActual.getAssembleQty();
                if (v.getAssembleQty() != null) {
                    existActual.setAssembleQty(v.getAssembleQty());

                    // 更新后-更新前
                    trxAssembleQty = BigDecimal.valueOf(v.getAssembleQty()).subtract(BigDecimal.valueOf(oldAssembleQty))
                            .doubleValue();
                }
                if (v.getTrxAssembleQty() != null) {
                    trxAssembleQty = v.getTrxAssembleQty();
                    existActual.setAssembleQty(BigDecimal.valueOf(oldAssembleQty)
                            .add(BigDecimal.valueOf(trxAssembleQty)).doubleValue());
                }

                // 记录更新前报废数量
                Double oldScrappedQty = existActual.getScrappedQty();
                if (v.getScrappedQty() != null) {
                    existActual.setScrappedQty(v.getScrappedQty());

                    trxScrappedQty = BigDecimal.valueOf(v.getScrappedQty()).subtract(BigDecimal.valueOf(oldScrappedQty))
                            .doubleValue();
                }
                if (v.getTrxScrappedQty() != null) {
                    trxScrappedQty = v.getTrxScrappedQty();
                    existActual.setScrappedQty(BigDecimal.valueOf(oldScrappedQty)
                            .add(BigDecimal.valueOf(v.getTrxScrappedQty())).doubleValue());
                }

                existActual.setAssembleExcessFlag(v.getAssembleExcessFlag());
                existActual.setAssembleRouterType(v.getAssembleRouterType());
                existActual.setSubstituteFlag(v.getSubstituteFlag());

                if (v.getActualFirstTime() != null) {
                    existActual.setActualFirstTime(v.getActualFirstTime());
                } else {
                    if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(oldAssembleQty)) == 0 && ((v
                            .getAssembleQty() != null
                            && BigDecimal.ZERO.compareTo(new BigDecimal(v.getAssembleQty().toString())) != 0)
                            || (v.getTrxAssembleQty() != null && BigDecimal.ZERO
                            .compareTo(new BigDecimal(v.getTrxAssembleQty().toString())) != 0))
                            && v.getActualFirstTime() == null) {
                        existActual.setActualFirstTime(new Date());
                    }
                }
                if (v.getActualLastTime() != null) {
                    existActual.setActualLastTime(v.getActualLastTime());
                } else {
                    if ((v.getAssembleQty() != null
                            && BigDecimal.ZERO.compareTo(new BigDecimal(v.getAssembleQty().toString())) != 0)
                            || (v.getTrxAssembleQty() != null && BigDecimal.ZERO.compareTo(
                            new BigDecimal(v.getTrxAssembleQty().toString())) != 0)) {
                        existActual.setActualLastTime(new Date());
                    }
                }
                existActual.setTenantId(tenantId);
                updateList.add(existActual);
                MtWorkOrderCompActualHis mtWorkOrderCompActualHis =
                        workOrderComponentActualTranMapper.woComponentActualToCompActualHis(existActual);
                mtWorkOrderCompActualHis.setTrxAssembleQty(trxAssembleQty);
                mtWorkOrderCompActualHis.setTrxScrappedQty(trxScrappedQty);
                mtWorkOrderCompActualHis.setEventId(dto.getEventId());
                mtWorkOrderCompActualHis.setWorkOrderCompActualHisId(hisIds.get(handleIndex));
                insertHisList.add(mtWorkOrderCompActualHis);
            } else {
                // 新增
                mtWorkOrderComponentActual = new MtWorkOrderComponentActual();
                mtWorkOrderComponentActual.setWorkOrderId(v.getWorkOrderId());

                mtWorkOrderComponentActual.setMaterialId(v.getMaterialId());
                MtBomComponentVO13 mtBomComponentVO13 = bomComponentMap.get(v.getBomComponentId());
                if (MtIdHelper.isIdNull(v.getMaterialId())) {
                    if (mtBomComponentVO13 == null) {
                        throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0032","ORDER", "materialId、bomComponentId", apiName));
                    }
                    mtWorkOrderComponentActual.setMaterialId(mtBomComponentVO13.getMaterialId());
                }

                mtWorkOrderComponentActual.setOperationId(dto.getOperationId());

                if (v.getRouterStepId() != null) {
                    mtWorkOrderComponentActual.setRouterStepId(v.getRouterStepId());
                }

                if (v.getAssembleQty() != null) {
                    trxAssembleQty = BigDecimal.valueOf(v.getAssembleQty())
                            .subtract(BigDecimal
                                    .valueOf(mtWorkOrderComponentActual.getAssembleQty() == null ? 0.0D
                                            : mtWorkOrderComponentActual.getAssembleQty()))
                            .doubleValue();
                    mtWorkOrderComponentActual.setAssembleQty(v.getAssembleQty());
                } else if (v.getTrxAssembleQty() != null) {
                    trxAssembleQty = v.getTrxAssembleQty();
                    mtWorkOrderComponentActual.setAssembleQty(v.getTrxAssembleQty());
                } else {
                    mtWorkOrderComponentActual.setAssembleQty(0.0D);
                }

                if (v.getScrappedQty() != null) {
                    trxScrappedQty = BigDecimal.valueOf(v.getScrappedQty())
                            .subtract(BigDecimal
                                    .valueOf(mtWorkOrderComponentActual.getScrappedQty() == null ? 0.0D
                                            : mtWorkOrderComponentActual.getScrappedQty()))
                            .doubleValue();
                    mtWorkOrderComponentActual.setScrappedQty(v.getScrappedQty());
                } else if (v.getTrxScrappedQty() != null) {
                    trxScrappedQty = v.getTrxScrappedQty();
                    mtWorkOrderComponentActual.setScrappedQty(v.getTrxScrappedQty());
                } else {
                    mtWorkOrderComponentActual.setScrappedQty(0.0D);
                }

                mtWorkOrderComponentActual.setComponentType(v.getComponentType());


                mtWorkOrderComponentActual.setBomComponentId(
                        MtIdHelper.isIdNull(v.getBomComponentId()) ? null : v.getBomComponentId());

                mtWorkOrderComponentActual.setBomId(v.getBomId());


                if (v.getAssembleExcessFlag() != null) {
                    mtWorkOrderComponentActual.setAssembleExcessFlag(v.getAssembleExcessFlag());
                } else {
                    mtWorkOrderComponentActual.setAssembleExcessFlag(
                            MtIdHelper.isIdNotNull(v.getBomComponentId()) ? MtBaseConstants.NO
                                    : MtBaseConstants.YES);
                }

                if (v.getAssembleRouterType() != null) {
                    mtWorkOrderComponentActual.setAssembleRouterType(v.getAssembleRouterType());
                } else {
                    mtWorkOrderComponentActual.setAssembleRouterType("");
                }

                if (v.getSubstituteFlag() != null) {
                    mtWorkOrderComponentActual.setSubstituteFlag(v.getSubstituteFlag());
                } else {
                    MtBomComponentVO13 bomComponent = bomComponentMap.get(v.getBomComponentId());
                    if (bomComponent == null) {
                        mtWorkOrderComponentActual.setSubstituteFlag(MtBaseConstants.NO);
                    } else {
                        if (MtIdHelper.isIdNull(v.getMaterialId())) {
                            mtWorkOrderComponentActual.setSubstituteFlag(MtBaseConstants.NO);
                        } else {
                            if (v.getMaterialId().equals(bomComponent.getMaterialId())) {
                                // 若获取结果bom_materialId与输入material一致，存入N
                                mtWorkOrderComponentActual.setSubstituteFlag(MtBaseConstants.NO);
                            } else {
                                // 若若获取结果bom_materialId与输入material不一致，存入Y
                                mtWorkOrderComponentActual.setSubstituteFlag(MtBaseConstants.YES);
                            }
                        }
                    }
                }

                if (v.getActualFirstTime() != null) {
                    mtWorkOrderComponentActual.setActualFirstTime(v.getActualFirstTime());
                } else {
                    // 若输入值assembleQty、trxAssembleQty不均为空或0，更新为当前系统时间
                    // 若输入值assembleQty、trxAssembleQty均为空或0，不更新
                    if ((v.getAssembleQty() != null
                            && BigDecimal.ZERO.compareTo(new BigDecimal(v.getAssembleQty().toString())) != 0)
                            || (v.getTrxAssembleQty() != null && BigDecimal.ZERO.compareTo(
                            new BigDecimal(v.getTrxAssembleQty().toString())) != 0)) {
                        mtWorkOrderComponentActual.setActualFirstTime(new Date());
                    }
                }
                if (v.getActualLastTime() != null) {
                    mtWorkOrderComponentActual.setActualLastTime(v.getActualLastTime());
                } else {
                    if ((v.getAssembleQty() != null
                            && BigDecimal.ZERO.compareTo(new BigDecimal(v.getAssembleQty().toString())) == 0)
                            || (v.getTrxAssembleQty() != null && BigDecimal.ZERO.compareTo(
                            new BigDecimal(v.getTrxAssembleQty().toString())) == 0)) {
                        mtWorkOrderComponentActual.setActualLastTime(new Date());
                    }
                }
                // 新增
                mtWorkOrderComponentActual.setTenantId(tenantId);
                mtWorkOrderComponentActual.setWorkOrderComponentActualId(ids.get(inserDataIndex++));
                insertList.add(mtWorkOrderComponentActual);

                // 生成历史
                MtWorkOrderCompActualHis mtWorkOrderCompActualHis = workOrderComponentActualTranMapper
                        .woComponentActualToCompActualHis(mtWorkOrderComponentActual);
                mtWorkOrderCompActualHis.setTrxAssembleQty(trxAssembleQty);
                mtWorkOrderCompActualHis.setTrxScrappedQty(trxScrappedQty);
                mtWorkOrderCompActualHis.setEventId(dto.getEventId());
                mtWorkOrderCompActualHis.setWorkOrderCompActualHisId(hisIds.get(handleIndex));
                insertHisList.add(mtWorkOrderCompActualHis);
            }
            handleIndex++;
        }

        mtCustomDbRepository.batchInsertTarzan(insertList);
        mtCustomDbRepository.batchInsertTarzan(insertHisList);
        mtCustomDbRepository.batchUpdateTarzan(updateList);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woComponentBatchAssemble(Long tenantId, MtWoComponentActualVO33 dto) {
        String apiName = "【API:woComponentBatchAssemble】";
        Map<String, String> mtBomComponentMap = new HashMap<>();

        // checkComponentTypeFlag输入不为N时，需要检验装配清单类型
        boolean needCheckComponentTypeFlag = !MtBaseConstants.NO.equalsIgnoreCase(dto.getCheckComponentTypeFlag());
        if (needCheckComponentTypeFlag) {
            List<String> bomComponentIds = dto.getWoInfo().stream().map(MtWoComponentActualVO32::getMaterialInfo)
                    .flatMap(Collection::stream).map(MtEoComponentActualVO29::getBomComponentId).distinct()
                    .filter(MtIdHelper::isIdNotNull).collect(Collectors.toList());
            List<MtBomComponent> mtBomComponents =
                    mtBomComponentRepository.selectBomComponentByBomComponentIds(tenantId, bomComponentIds);
            mtBomComponentMap = mtBomComponents.stream().collect(
                    Collectors.toMap(MtBomComponent::getBomComponentId, MtBomComponent::getBomComponentType));

        }
        Map<MtWoComponentActualTupleVO, MtWoComponentActualVO30> woInfoParamMap = new HashMap<>(dto.getWoInfo().size());
        List<MtWoComponentActualVO30> woComponentActualVO30s = new ArrayList<>();
        for (MtWoComponentActualVO32 enInfo : dto.getWoInfo()) {
            if (MtIdHelper.isIdNull(enInfo.getWorkOrderId())) {
                throw new MtException("MT_ORDER_0001",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001", "ORDER","workOrderId", apiName));
            }
            if (StringUtils.isEmpty(enInfo.getOperationAssembleFlag())) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001","ORDER",
                        "operationAssembleFlag", apiName));
            }
            boolean operationAssembleFlag = enInfo.getOperationAssembleFlag().equalsIgnoreCase(MtBaseConstants.YES);
            for (MtEoComponentActualVO29 materialInfo : enInfo.getMaterialInfo()) {
                if (materialInfo.getTrxAssembleQty() == null) {
                    throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001","ORDER",
                            "trxAssembleQty", apiName));
                }
                if (BigDecimal.valueOf(materialInfo.getTrxAssembleQty()).compareTo(BigDecimal.ZERO) <= 0) {
                    throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0059","ORDER",
                            "trxAssembleQty", apiName));
                }
                boolean assembleExcessFlag = MtBaseConstants.YES.equalsIgnoreCase(materialInfo.getAssembleExcessFlag());

                MtWoComponentActualVO30 actualUpdateVO = new MtWoComponentActualVO30();
                if (operationAssembleFlag && assembleExcessFlag) {
                    if (MtIdHelper.isIdNull(materialInfo.getMaterialId())) {
                        throw new MtException("MT_ORDER_0106",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0106","ORDER", apiName));
                    }
                    if (MtIdHelper.isIdNull(dto.getOperationId())) {
                        throw new MtException("MT_ORDER_0108",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0108","ORDER", apiName));
                    }
                    if (MtIdHelper.isIdNull(enInfo.getRouterStepId())) {
                        throw new MtException("MT_ORDER_0107",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0107","ORDER", apiName));
                    }
                    actualUpdateVO.setTrxAssembleQty(materialInfo.getTrxAssembleQty());
                    actualUpdateVO.setAssembleExcessFlag(MtBaseConstants.YES);
                    actualUpdateVO.setBomComponentId(MtBaseConstants.LONG_SPECIAL);
                    actualUpdateVO.setRouterStepId(enInfo.getRouterStepId());
                } else if (operationAssembleFlag) {
                    if (MtIdHelper.isIdNull(materialInfo.getBomComponentId())) {
                        throw new MtException("MT_ORDER_0104",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0104","ORDER", apiName));
                    }
                    if (MtIdHelper.isIdNull(enInfo.getRouterStepId())) {
                        throw new MtException("MT_ORDER_0107",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0107","ORDER", apiName));
                    }
                    if (needCheckComponentTypeFlag && !"ASSEMBLING"
                            .equalsIgnoreCase(mtBomComponentMap.get(materialInfo.getBomComponentId()))) {
                        throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105", "ORDER", apiName));

                    }
                    actualUpdateVO.setBomComponentId(materialInfo.getBomComponentId());
                    actualUpdateVO.setRouterStepId(enInfo.getRouterStepId());
                    actualUpdateVO.setTrxAssembleQty(materialInfo.getTrxAssembleQty());
                    actualUpdateVO.setAssembleExcessFlag(MtBaseConstants.NO);
                } else if (!assembleExcessFlag) {
                    if (MtIdHelper.isIdNull(materialInfo.getBomComponentId())) {
                        throw new MtException("MT_ORDER_0104",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0104","ORDER", apiName));
                    }
                    if (needCheckComponentTypeFlag && !"ASSEMBLING"
                            .equalsIgnoreCase(mtBomComponentMap.get(materialInfo.getBomComponentId()))) {
                        throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105","ORDER", apiName));

                    }
                    actualUpdateVO.setBomComponentId(materialInfo.getBomComponentId());
                    actualUpdateVO.setTrxAssembleQty(materialInfo.getTrxAssembleQty());
                    actualUpdateVO.setAssembleExcessFlag(MtBaseConstants.NO);
                    actualUpdateVO.setRouterStepId(MtBaseConstants.LONG_SPECIAL);

                } else if (assembleExcessFlag) {
                    if (MtIdHelper.isIdNull(materialInfo.getMaterialId())) {
                        throw new MtException("MT_ORDER_0106",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0106","ORDER", apiName));
                    }
                    actualUpdateVO.setTrxAssembleQty(materialInfo.getTrxAssembleQty());
                    actualUpdateVO.setAssembleExcessFlag(MtBaseConstants.YES);
                    actualUpdateVO.setBomComponentId(MtBaseConstants.LONG_SPECIAL);
                    actualUpdateVO.setRouterStepId(MtBaseConstants.LONG_SPECIAL);
                }
                actualUpdateVO.setSubstituteFlag(materialInfo.getSubstituteFlag());
                actualUpdateVO.setBomId(enInfo.getBomId());
                actualUpdateVO.setWorkOrderId(enInfo.getWorkOrderId());
                actualUpdateVO.setMaterialId(materialInfo.getMaterialId());
                actualUpdateVO.setComponentType("ASSEMBLING");
                actualUpdateVO.setAssembleRouterType(enInfo.getAssembleRouterType());

                //对需要传入woComponentActualBatchUpdate的数据根据唯一性索引进行去重，并将数量汇总
                MtWoComponentActualTupleVO tupleVO = workOrderComponentActualTranMapper
                        .woComponentActualVO30ToActualTupleVO(actualUpdateVO);
                if (woInfoParamMap.containsKey(tupleVO)) {
                    MtWoComponentActualVO30 woComponentActual = woInfoParamMap.get(tupleVO);
                    woComponentActual.setTrxAssembleQty(BigDecimal.valueOf(woComponentActual.getTrxAssembleQty())
                            .add(BigDecimal.valueOf(actualUpdateVO.getTrxAssembleQty()))
                            .doubleValue());
                } else {
                    woInfoParamMap.put(tupleVO, actualUpdateVO);
                    woComponentActualVO30s.add(actualUpdateVO);
                }
            }
        }

        // 获取事件ID(新增事件)
        if (MtIdHelper.isIdNull(dto.getEventId())) {
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("COMPONENT_ASSEMBLE");
            eventCreateVO.setWorkcellId(dto.getWorkcellId());
            eventCreateVO.setLocatorId(dto.getLocatorId());
            eventCreateVO.setParentEventId(dto.getParentEventId());
            eventCreateVO.setEventRequestId(dto.getEventRequestId());
            eventCreateVO.setShiftDate(dto.getShiftDate() == null ? null : Date.from(dto.getShiftDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            eventCreateVO.setShiftCode(dto.getShiftCode());
            dto.setEventId(mtEventRepository.eventCreate(tenantId, eventCreateVO));
        }

        if (CollectionUtils.isNotEmpty(woComponentActualVO30s)) {
            MtWoComponentActualVO29 mtWoComponentActualVO29 = new MtWoComponentActualVO29();
            mtWoComponentActualVO29.setEventId(dto.getEventId());
            mtWoComponentActualVO29.setOperationId(dto.getOperationId());
            mtWoComponentActualVO29.setWoInfoList(woComponentActualVO30s);

            // 更新生产装配移除实绩并记录历史
            self().woComponentActualBatchUpdate(tenantId, mtWoComponentActualVO29);
        }
    }
}
