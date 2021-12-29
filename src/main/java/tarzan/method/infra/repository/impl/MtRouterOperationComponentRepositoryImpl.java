package tarzan.method.infra.repository.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtIdHelper;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.method.domain.entity.MtBom;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterOperationComponent;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.*;
import tarzan.method.infra.mapper.MtRouterOperationComponentMapper;

/**
 * 工艺路线步骤对应工序组件 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@Component
public class MtRouterOperationComponentRepositoryImpl extends BaseRepositoryImpl<MtRouterOperationComponent>
                implements MtRouterOperationComponentRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtRouterOperationComponentMapper mtRouterOperationComponentMapper;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;


    @Override
    public List<MtRouterOperationComponent> routerOperationComponentQuery(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerOperationComponentQuery】"));
        }

        return this.mtRouterOperationComponentMapper.selectRouterOperationComponent(tenantId, routerStepId);
    }

    @Override
    public List<MtRouterOpComponentVO> routerOperationComponentPerQtyQuery(Long tenantId, MtRouterOpComponentVO1 dto) {
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerOperationComponentPerQtyQuery】"));
        }
        List<MtRouterOperationComponent> mtRouterOperationComponents =
                        this.mtRouterOperationComponentMapper.selectRouterOperationComponent2(tenantId, dto);
        if (CollectionUtils.isEmpty(mtRouterOperationComponents)) {
            return Collections.emptyList();
        }

        List<MtRouterOpComponentVO> list = new ArrayList<MtRouterOpComponentVO>();
        for (MtRouterOperationComponent mtRouterOperationComponent : mtRouterOperationComponents) {
            MtRouterOpComponentVO vo = new MtRouterOpComponentVO();
            vo.setRouterOperationComponentId(mtRouterOperationComponent.getRouterOperationComponentId());
            vo.setRouterOperationId(mtRouterOperationComponent.getRouterOperationId());
            vo.setBomComponentId(mtRouterOperationComponent.getBomComponentId());
            vo.setSequence(mtRouterOperationComponent.getSequence());
            vo.setPerQty(0.0D);
            vo.setQty(0.0D);

            MtBomComponent mtBomComponent = this.mtBomComponentRepository.bomComponentBasicGet(tenantId,
                            mtRouterOperationComponent.getBomComponentId());
            if (mtBomComponent != null) {
                BigDecimal qty = new BigDecimal(
                                mtBomComponent.getQty() == null ? "0" : mtBomComponent.getQty().toString());
                vo.setQty(qty.doubleValue());

                MtBomVO7 mtBom = this.mtBomRepository.bomBasicGet(tenantId, mtBomComponent.getBomId());
                if (null != mtBom && null != mtBom.getPrimaryQty()
                                && BigDecimal.ZERO.compareTo(BigDecimal.valueOf(mtBom.getPrimaryQty())) != 1) {
                    vo.setPerQty(qty.divide(BigDecimal.valueOf(mtBom.getPrimaryQty()), 10, BigDecimal.ROUND_HALF_DOWN)
                                    .doubleValue());
                }
            }

            list.add(vo);
        }
        return list;
    }

    @Override
    public String routerOperationComponentVerify(Long tenantId, MtRouterOpComponentVO2 dto) {
        // 参数有效性验证
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "materialId", "【API:routerOperationComponentVerify】"));
        }
        if (StringUtils.isEmpty(dto.getRouterId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerOperationComponentVerify】"));
        }

        // 1. 获取所有步骤组件
        List<MtRouterOperationComponent> routerOperationComponentList =
                        mtRouterOperationComponentMapper.selectByRouterId(tenantId, dto.getRouterId());
        if (CollectionUtils.isEmpty(routerOperationComponentList)) {
            return "N";
        }

        // 获取结果中的 bomComponentId
        List<String> bomComponentIdList = routerOperationComponentList.stream()
                        .map(MtRouterOperationComponent::getBomComponentId).distinct().collect(Collectors.toList());

        // 2. 根据 bomComponentId 获取物料id
        List<MtBomComponentVO13> mtBomComponentList =
                        mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIdList);

        // 获取结果中的 MaterialId
        List<String> materialIdList =
                        mtBomComponentList.stream().map(MtBomComponent::getMaterialId).collect(Collectors.toList());

        if (materialIdList.contains(dto.getMaterialId())) {
            return "Y";
        } else {
            return "N";
        }
    }

    @Override
    public List<String> operationOrMaterialLimitBomComponentQuery(Long tenantId, MtRouterOpComponentVO3 condition) {
        if (StringUtils.isEmpty(condition.getBomId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "bomId", "【API:operationOrMaterialLimitBomComponentQuery】"));
        }
        if (StringUtils.isEmpty(condition.getMaterialId())) {
            throw new MtException("MT_ROUTER_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001", "ROUTER",
                                            "materialId", "【API:operationOrMaterialLimitBomComponentQuery】"));
        }
        if (StringUtils.isEmpty(condition.getComponentType())) {
            throw new MtException("MT_ROUTER_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001", "ROUTER",
                                            "componentType", "【API:operationOrMaterialLimitBomComponentQuery】"));
        }

        List<String> result = new ArrayList<String>();
        if (StringUtils.isNotEmpty(condition.getRouterId()) && StringUtils.isNotEmpty(condition.getOperationId())) {
            List<MtRouterStepVO5> routerStepOpVos =
                            mtRouterStepRepository.routerStepListQuery(tenantId, condition.getRouterId());

            if (CollectionUtils.isNotEmpty(routerStepOpVos)) {
                List<MtRouterStepVO5> filterRouterStepOpVos = routerStepOpVos.stream()
                                .filter(t -> condition.getOperationId()
                                                .equals(null == t.getOperationId() ? "" : t.getOperationId()))
                                .collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(filterRouterStepOpVos)) {
                    final List<String> bomComponentIds = new ArrayList<String>();

                    for (MtRouterStepVO5 routerStepOpVO : filterRouterStepOpVos) {
                        List<MtRouterOperationComponent> mtRouterOperationComponents =
                                        routerOperationComponentQuery(tenantId, routerStepOpVO.getRouterStepId());

                        if (CollectionUtils.isNotEmpty(mtRouterOperationComponents)) {
                            mtRouterOperationComponents.stream()
                                            .forEach(t -> bomComponentIds.add(t.getBomComponentId()));
                        }
                    }

                    if (CollectionUtils.isNotEmpty(bomComponentIds)) {
                        List<MtBomComponentVO13> bomComponents = this.mtBomComponentRepository
                                        .bomComponentBasicBatchGet(tenantId, bomComponentIds);
                        Date now = new Date();

                        for (MtBomComponentVO13 bomComponent : bomComponents) {
                            if (bomComponent.getMaterialId().equals(condition.getMaterialId())
                                            && bomComponent.getBomComponentType().equals(condition.getComponentType())
                                            && bomComponent.getDateFrom().getTime() <= now.getTime()
                                            && (null == bomComponent.getDateTo()
                                                            || bomComponent.getDateTo().getTime() > now.getTime())) {
                                result.add(bomComponent.getBomComponentId());
                            }
                        }
                    }
                }
            }
        }

        if (StringUtils.isEmpty(condition.getRouterId()) || StringUtils.isEmpty(condition.getOperationId())
                        || CollectionUtils.isEmpty(result)) {
            MtBomComponentVO bomComponentVO = new MtBomComponentVO();
            bomComponentVO.setBomId(condition.getBomId());
            bomComponentVO.setMaterialId(condition.getMaterialId());
            bomComponentVO.setBomComponentType(condition.getComponentType());
            bomComponentVO.setOnlyAvailableFlag("Y");
            List<MtBomComponentVO16> bomComponentVo16List =
                            mtBomComponentRepository.propertyLimitBomComponentQuery(tenantId, bomComponentVO);
            if (CollectionUtils.isNotEmpty(bomComponentVo16List)) {
                result = bomComponentVo16List.get(0).getBomComponentId();
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void routerOperationCompAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVo10) {
        if (StringUtils.isEmpty(mtExtendVo10.getKeyId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "bomId", "【API:routerOperationCompAttrPropertyUpdate】"));
        }
        MtRouterOperationComponent mtRouterOperationComponent = new MtRouterOperationComponent();
        mtRouterOperationComponent.setTenantId(tenantId);
        mtRouterOperationComponent.setRouterOperationComponentId(mtExtendVo10.getKeyId());
        MtRouterOperationComponent routerOperationComponent =
                        mtRouterOperationComponentMapper.selectOne(mtRouterOperationComponent);
        if (null == routerOperationComponent) {
            throw new MtException("MT_ROUTER_0071",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0071", "ROUTER",
                                            mtExtendVo10.getKeyId(), "mt_router_operation_comp",
                                            "【API:routerOperationCompAttrPropertyUpdate】"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_router_operation_c_attr", mtExtendVo10.getKeyId(),
                        mtExtendVo10.getEventId(), mtExtendVo10.getAttrs());

    }

    @Override
    public List<MtRouterOpComponentVO4> selectRouterOpComponentByRouterIds(Long tenantId, List<String> routerIds) {
        if (CollectionUtils.isEmpty(routerIds)) {
            return Collections.emptyList();
        }
        return mtRouterOperationComponentMapper.selectByRouterIds(tenantId, routerIds);
    }

    @Override
    public List<MtRouterOperationComponent> selectRouterOpComponentByBomComponentId(Long tenantId,
                    List<String> bomComponentIds) {
        return mtRouterOperationComponentMapper.selectByCondition(Condition.builder(MtRouterOperationComponent.class)
                        .andWhere(Sqls.custom().andIn(MtRouterOperationComponent.FIELD_BOM_COMPONENT_ID,
                                        bomComponentIds, true))
                        .build());
    }

    @Override
    public List<MtRouterOperationComponent> selectByRouterOperationIdsAndComponentIds(Long tenantId,
                    List<String> routerOperationIds, List<String> bomComponentIds) {
        return mtRouterOperationComponentMapper.selectByCondition(Condition.builder(MtRouterOperationComponent.class)
                        .andWhere(Sqls.custom()
                                        .andIn(MtRouterOperationComponent.FIELD_BOM_COMPONENT_ID, bomComponentIds, true)
                                        .andIn(MtRouterOperationComponent.FIELD_ROUTER_OPERATION_ID, routerOperationIds,
                                                        true))
                        .build());
    }

    @Override
    public List<MtRouterOpComponentVO> routerOperationComponentPerQtyBatchQuery(Long tenantId,
                    List<MtRouterOpComponentVO1> dtoList) {
        final String apiName = "【API:routerOperationComponentPerQtyBatchQuery】";
        if (CollectionUtils.isEmpty(dtoList)
                        || dtoList.stream().anyMatch(t -> MtIdHelper.isIdNull(t.getRouterStepId()))) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", apiName));
        }

        List<String> routerStepIdList =
                        dtoList.stream().map(MtRouterOpComponentVO1::getRouterStepId).collect(Collectors.toList());

        // get router operation by router step
        List<MtRouterOperation> routerOperationList =
                        mtRouterOperationRepository.routerOperationBatchGet(tenantId, routerStepIdList);
        if (CollectionUtils.isEmpty(routerOperationList)) {
            return Collections.emptyList();
        }

        // router operation - router step
        Map<String, String> routerOperationMap = routerOperationList.stream().collect(
                        Collectors.toMap(MtRouterOperation::getRouterOperationId, MtRouterOperation::getRouterStepId));

        // get router operation component by router operation
        List<MtRouterOperationComponent> routerOperationComponentList = self().selectByCondition(Condition
                        .builder(MtRouterOperationComponent.class)
                        .andWhere(Sqls.custom().andEqualTo(MtRouterOperationComponent.FIELD_TENANT_ID, tenantId))
                        .andWhere(Sqls.custom().andIn(MtRouterOperationComponent.FIELD_ROUTER_OPERATION_ID,
                                        Lists.newArrayList(routerOperationMap.keySet().iterator())))
                        .build());
        if (CollectionUtils.isEmpty(routerOperationComponentList)) {
            return Collections.emptyList();
        }

        // filter param which enter bom component
        Map<String, List<String>> routerOperationWithComponentMap =
                        dtoList.stream().filter(t -> MtIdHelper.isIdNotNull(t.getBomComponentId()))
                                        .collect(Collectors.groupingBy(MtRouterOpComponentVO1::getRouterStepId,
                                                        Collectors.mapping(MtRouterOpComponentVO1::getBomComponentId,
                                                                        Collectors.toList())));
        if (MapUtils.isNotEmpty(routerOperationWithComponentMap)) {
            // remove element which router operation has been limited
            // router operation -> param router step -> param bom component
            routerOperationComponentList.removeIf(t -> CollectionUtils.isNotEmpty(
                            routerOperationWithComponentMap.get(routerOperationMap.get(t.getRouterOperationId())))
                            && !routerOperationWithComponentMap.get(routerOperationMap.get(t.getRouterOperationId()))
                                            .contains(t.getBomComponentId()));
        }

        // get primary qty by bom component
        List<String> bomComponentIdList = routerOperationComponentList.stream()
                        .filter(t -> MtIdHelper.isIdNotNull(t.getBomComponentId()))
                        .map(MtRouterOperationComponent::getBomComponentId).collect(Collectors.toList());
        Map<String, Double> bomComponentQtyMap = new HashMap<>();
        Map<String, Double> bomComponentPrimaryQtyMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(bomComponentIdList)) {
            // get bom component
            List<MtBomComponentVO13> bomComponentList =
                            mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIdList);

            if (CollectionUtils.isNotEmpty(bomComponentList)) {
                bomComponentQtyMap = bomComponentList.stream().collect(
                                Collectors.toMap(MtBomComponentVO13::getBomComponentId, MtBomComponentVO13::getQty));

                // get bom by bom component
                List<String> bomIdList = bomComponentList.stream().map(MtBomComponentVO13::getBomId)
                                .collect(Collectors.toList());
                List<MtBomVO7> bomList = mtBomRepository.bomBasicBatchGet(tenantId, bomIdList);
                if (CollectionUtils.isNotEmpty(bomList)) {
                    Map<String, Double> bomPrimaryQtyMap =
                                    bomList.stream().collect(Collectors.toMap(MtBom::getBomId, MtBom::getPrimaryQty));

                    bomComponentPrimaryQtyMap = bomComponentList.stream().collect(Collectors.toMap(
                                    MtBomComponentVO13::getBomComponentId, t -> bomPrimaryQtyMap.get(t.getBomId())));
                }
            }
        }

        // construct return values
        List<MtRouterOpComponentVO> resultList = new ArrayList<>();
        MtRouterOpComponentVO result;
        for (MtRouterOperationComponent roc : routerOperationComponentList) {
            result = new MtRouterOpComponentVO();
            result.setRouterStepId(routerOperationMap.get(roc.getRouterOperationId()));
            result.setRouterOperationComponentId(roc.getRouterOperationComponentId());
            result.setRouterOperationId(roc.getRouterOperationId());
            result.setBomComponentId(roc.getBomComponentId());
            result.setSequence(roc.getSequence());
            result.setQty(null == bomComponentQtyMap.get(roc.getBomComponentId()) ? 0.0D
                            : bomComponentQtyMap.get(roc.getBomComponentId()));

            if (null == bomComponentPrimaryQtyMap.get(roc.getBomComponentId())) {
                result.setPerQty(0.0D);
            } else {
                BigDecimal qty = BigDecimal.valueOf(result.getQty());
                BigDecimal primaryQty = BigDecimal.valueOf(bomComponentPrimaryQtyMap.get(roc.getBomComponentId()));
                result.setPerQty(qty.divide(primaryQty, 10, BigDecimal.ROUND_HALF_DOWN).doubleValue());
            }

            resultList.add(result);
        }

        return resultList;
    }

    @Override
    public List<MtRouterOpComponentVO6> operationOrMaterialLimitBomComponentBatchQuery(Long tenantId,
                    List<MtRouterOpComponentVO3> condition) {
        final String apiName = "【API:operationOrMaterialLimitBomComponentBatchQuery】";
        if (condition.stream().anyMatch(t -> MtIdHelper.isIdNull(t.getBomId()))) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "bomId", apiName));
        }
        if (condition.stream().anyMatch(t -> MtIdHelper.isIdNull(t.getMaterialId()))) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "materialId", apiName));
        }
        if (condition.stream().anyMatch(t -> StringUtils.isEmpty(t.getComponentType()))) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "componentType", apiName));
        }

        List<MtRouterOpComponentVO6> resultList = new ArrayList<>();

        // 筛选 同时传入routerId和operationId的数据
        List<MtRouterOpComponentVO3> routerOperationConditionList = condition.stream().filter(
                        t -> MtIdHelper.isIdNotNull(t.getRouterId()) && MtIdHelper.isIdNotNull(t.getOperationId()))
                        .collect(Collectors.toList());

        List<MtRouterOpComponentVO3> unRouterOperationConditionList = condition.stream().filter(
                        t -> !(MtIdHelper.isIdNotNull(t.getRouterId()) && MtIdHelper.isIdNotNull(t.getOperationId())))
                        .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(routerOperationConditionList)) {
            List<MtRouterOpComponentVO6> routerOperationConditionResultList =
                            this.dealRouterOperationConditionList(tenantId, routerOperationConditionList);
            if (CollectionUtils.isNotEmpty(routerOperationConditionResultList)) {
                resultList.addAll(routerOperationConditionResultList);
            }
        }

        if (CollectionUtils.isNotEmpty(unRouterOperationConditionList)) {
            List<MtRouterOpComponentVO6> unRouterOperationConditionResultList =
                            this.dealUnRouterOperationConditionList(tenantId, unRouterOperationConditionList);
            if (CollectionUtils.isNotEmpty(unRouterOperationConditionResultList)) {
                resultList.addAll(unRouterOperationConditionResultList);
            }
        }

        return resultList;
    }

    private List<MtRouterOpComponentVO6> dealRouterOperationConditionList(Long tenantId,
                    List<MtRouterOpComponentVO3> routerOperationConditionList) {
        // 输入参数根据routerId分组，有可能传入参数routerId相同
        Map<String, List<MtRouterOpComponentVO3>> routerOperationConditionMap = routerOperationConditionList.stream()
                        .collect(Collectors.groupingBy(MtRouterOpComponentVO3::getRouterId));

        // 根据工艺路线id集合，批量获取工艺步骤信息
        List<MtRouterStepVO15> routerStepList = mtRouterStepRepository.routerStepListBatchQuery(tenantId,
                        routerOperationConditionList.stream().map(MtRouterOpComponentVO3::getRouterId).distinct()
                                        .collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(routerStepList)) {
            return null;
        }

        // 记录所有工艺步骤ID结合
        List<String> totalRouterStepIds = new ArrayList<>();

        // 记录 routerId+operationId 对应 routerStepIds 的集合
        Map<MtRouterOperationVO1, List<String>> routerOperationRouterStepIdsMap = new HashMap<>();

        for (MtRouterStepVO15 routerStep : routerStepList) {
            // 筛选 routerId对应传入 operationIds 集合的数据
            if (CollectionUtils.isEmpty(routerStep.getRouterStepList())) {
                continue;
            }

            List<MtRouterStepVO5> curRouterStepList = routerStep.getRouterStepList().stream()
                            .filter(t -> routerOperationConditionMap.get(routerStep.getRouterId()).stream()
                                            .map(MtRouterOpComponentVO3::getOperationId).collect(Collectors.toList())
                                            .contains(t.getOperationId()))
                            .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(curRouterStepList)) {
                continue;
            }

            totalRouterStepIds.addAll(curRouterStepList.stream().map(MtRouterStepVO5::getRouterStepId)
                            .collect(Collectors.toList()));

            for (MtRouterStepVO5 curRouterStep : curRouterStepList) {
                MtRouterOperationVO1 routerOperation = new MtRouterOperationVO1();
                routerOperation.setRouterId(routerStep.getRouterId());
                routerOperation.setOperationId(curRouterStep.getOperationId());

                // 记录关系
                if (routerOperationRouterStepIdsMap.get(routerOperation) != null) {
                    routerOperationRouterStepIdsMap.get(routerOperation).add(curRouterStep.getRouterStepId());
                } else {
                    routerOperationRouterStepIdsMap.put(routerOperation,
                                    Collections.singletonList(curRouterStep.getRouterStepId()));
                }
            }
        }

        if (CollectionUtils.isEmpty(totalRouterStepIds)) {
            return dealUnRouterOperationConditionList(tenantId, routerOperationConditionList);
        }

        List<MtRouterOperation> mtRouterOperationList =
                        mtRouterOperationRepository.routerOperationBatchGet(tenantId, totalRouterStepIds);
        if (CollectionUtils.isEmpty(mtRouterOperationList)) {
            return null;
        }

        SecurityTokenHelper.close();
        List<MtRouterOperationComponent> routerOpnCompList = this.mtRouterOperationComponentMapper
                        .selectByCondition(Condition.builder(MtRouterOperationComponent.class)
                                        .andWhere(Sqls.custom().andEqualTo(MtRouterOperationComponent.FIELD_TENANT_ID,
                                                        tenantId))
                                        .andWhere(Sqls.custom().andIn(
                                                        MtRouterOperationComponent.FIELD_ROUTER_OPERATION_ID,
                                                        mtRouterOperationList.stream()
                                                                        .map(MtRouterOperation::getRouterOperationId)
                                                                        .collect(Collectors.toList())))
                                        .build());
        if (CollectionUtils.isEmpty(routerOpnCompList)) {
            return null;
        }

        // 查询装配清单信息
        List<MtBomComponent> bomComponentList = mtBomComponentRepository.selectBomComponentByBomComponentIds(tenantId,
                        routerOpnCompList.stream().map(MtRouterOperationComponent::getBomComponentId).distinct()
                                        .collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(bomComponentList)) {
            return null;
        }
        // 筛选有效数据
        Date now = new Date();
        bomComponentList = bomComponentList.stream()
                        .filter(bomComponent -> bomComponent.getDateFrom().getTime() <= now.getTime()
                                        && (null == bomComponent.getDateTo()
                                                        || bomComponent.getDateTo().getTime() > now.getTime()))
                        .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bomComponentList)) {
            return null;
        }

        Map<MtRouterOperationVO1, MtRouterOpComponentVO3> routerOpComponentMap = routerOperationConditionList.stream()
                        .collect(Collectors.toMap(t -> new MtRouterOperationVO1(t.getRouterId(), t.getOperationId()),
                                        t -> t, (t1, t2) -> t1));

        // 整理结果
        List<MtRouterOpComponentVO6> resultList = new ArrayList<>();
        for (Map.Entry<MtRouterOperationVO1, MtRouterOpComponentVO3> routerOpComponentEntry : routerOpComponentMap
                        .entrySet()) {
            MtRouterOpComponentVO3 condition = routerOpComponentEntry.getValue();
            MtRouterOperationVO1 routerOperationVO1 = routerOpComponentEntry.getKey();

            List<String> routerStepIds = routerOperationRouterStepIdsMap.get(routerOperationVO1);
            if (CollectionUtils.isEmpty(routerStepIds)) {
                continue;
            }
            List<String> routerOperationIds =
                            mtRouterOperationList.stream().filter(t -> routerStepIds.contains(t.getRouterStepId()))
                                            .map(MtRouterOperation::getRouterOperationId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(routerOperationIds)) {
                continue;
            }

            List<String> bomComponentIds = routerOpnCompList.stream()
                            .filter(t -> routerOperationIds.contains(t.getRouterOperationId()))
                            .map(MtRouterOperationComponent::getBomComponentId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(bomComponentIds)) {
                continue;
            }

            List<MtBomComponent> curBomComponentList = bomComponentList.stream()
                            .filter(t -> bomComponentIds.contains(t.getBomComponentId())
                                            && condition.getBomId().equals(t.getBomId())
                                            && condition.getMaterialId().equals(t.getMaterialId())
                                            && condition.getComponentType().equals(t.getBomComponentType()))
                            .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(curBomComponentList)) {
                MtRouterOpComponentVO6 result = new MtRouterOpComponentVO6();
                result.setRouterId(condition.getRouterId());
                result.setOperationId(condition.getOperationId());
                result.setBomComponentIds(curBomComponentList.stream().map(MtBomComponent::getBomComponentId)
                                .collect(Collectors.toList()));
                result.setBomId(condition.getBomId());
                result.setMaterialId(condition.getMaterialId());
                result.setComponentType(condition.getComponentType());
                resultList.add(result);
            }
        }

        return resultList;
    }

    private List<MtRouterOpComponentVO6> dealUnRouterOperationConditionList(Long tenantId,
                    List<MtRouterOpComponentVO3> unRouterOperationConditionList) {
        List<String> bomIds = unRouterOperationConditionList.stream().map(MtRouterOpComponentVO3::getBomId).distinct()
                        .collect(Collectors.toList());
        List<MtBomComponent> mtBomComponentList = mtBomComponentRepository.selectBomComponentByBomIds(tenantId, bomIds);
        if (CollectionUtils.isEmpty(mtBomComponentList)) {
            return null;
        }

        Map<MtRouterOpComponentVO7, List<MtBomComponent>> bomComponentMap = mtBomComponentList.stream()
                        .collect(Collectors.groupingBy(t -> new MtRouterOpComponentVO7(t.getBomId(), t.getMaterialId(),
                                        t.getBomComponentType())));

        // 整理结果
        List<MtRouterOpComponentVO6> resultList = new ArrayList<>();
        for (MtRouterOpComponentVO3 routerOpComponent : unRouterOperationConditionList) {
            List<MtBomComponent> bomComponentList =
                            bomComponentMap.get(new MtRouterOpComponentVO7(routerOpComponent.getBomId(),
                                            routerOpComponent.getMaterialId(), routerOpComponent.getComponentType()));
            if (CollectionUtils.isEmpty(bomComponentList)) {
                continue;
            }

            MtRouterOpComponentVO6 result = new MtRouterOpComponentVO6();
            result.setRouterId(routerOpComponent.getRouterId());
            result.setOperationId(routerOpComponent.getOperationId());
            result.setBomComponentIds(bomComponentList.stream().map(MtBomComponent::getBomComponentId)
                            .collect(Collectors.toList()));
            result.setBomId(routerOpComponent.getBomId());
            result.setMaterialId(routerOpComponent.getMaterialId());
            result.setComponentType(routerOpComponent.getComponentType());
            resultList.add(result);
        }
        return resultList;
    }
}
