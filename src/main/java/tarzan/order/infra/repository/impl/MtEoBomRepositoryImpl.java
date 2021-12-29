package tarzan.order.infra.repository.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.actual.domain.repository.MtEoComponentActualRepository;
import tarzan.actual.domain.vo.MtEoComponentActualVO10;
import tarzan.material.domain.entity.MtPfepManufacturing;
import tarzan.material.domain.repository.MtPfepManufacturingRepository;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.method.domain.entity.MtRouterLink;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.*;
import tarzan.modeling.domain.entity.MtModSiteManufacturing;
import tarzan.modeling.domain.repository.MtModSiteManufacturingRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtEoBom;
import tarzan.order.domain.entity.MtEoBomHis;
import tarzan.order.domain.entity.MtEoHis;
import tarzan.order.domain.repository.MtEoBomHisRepository;
import tarzan.order.domain.repository.MtEoBomRepository;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.vo.*;
import tarzan.order.infra.mapper.MtEoBomMapper;
import tarzan.order.infra.mapper.MtEoMapper;

/**
 * EO装配清单 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@Component
public class MtEoBomRepositoryImpl extends BaseRepositoryImpl<MtEoBom> implements MtEoBomRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoBomMapper mtEoBomMapper;

    @Autowired
    private MtEoMapper mtEoMapper;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtModSiteManufacturingRepository mtModSiteManufacturingRepository;

    @Autowired
    private MtPfepManufacturingRepository mtPfepManufacturingRepository;

    @Autowired
    private MtEoBomHisRepository mtEoBomHisRepository;

    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;

    @Autowired
    private MtBomSiteAssignRepository mtBomSiteAssignRepository;

    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtRouterOperationComponentRepository mtRouterOperationComponentRepository;

    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtRouterLinkRepository mtRouterLinkRepository;


    @Override
    public String eoBomGet(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoBomGet】"));

        }
        MtEoBom tmp = new MtEoBom();
        tmp.setTenantId(tenantId);
        tmp.setEoId(eoId);
        tmp = mtEoBomMapper.selectOne(tmp);
        if (tmp == null) {
            return "";
        } else {
            return tmp.getBomId();
        }
    }

    @Override
    public List<MtEoBom> eoBomBatchGet(Long tenantId, List<String> eoIds) {
        if (CollectionUtils.isEmpty(eoIds)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoIds", "【API:eoBomBatchGet】"));
        }
        return mtEoBomMapper.eoBomBatchGet(tenantId, eoIds);
    }

    @Override
    public List<MtBomComponentVO19> attritionLimitEoComponentQtyQuery(Long tenantId, MtBomComponentVO20 dto) {
        // 返回结果
        List<MtBomComponentVO19> returnList = new ArrayList<>();
        // 1. 检验参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:attritionLimitEoComponentQtyQuery】"));
        }

        // 2. 获取执行作业信息
        MtEo mtEo = this.mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0020", "ORDER", "【API:attritionLimitEoComponentQtyQuery】"));
        }

        // 如果dividedByStep有输入值，判断dividedByStep=Y或者N，否则报错
        if (StringUtils.isNotEmpty(dto.getDividedByStep())
                && !MtBaseConstants.YES_NO.contains(dto.getDividedByStep())) {
            throw new MtException("MT_ORDER_0154", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0154", "ORDER", "dividedByStep", "【API：attritionLimitEoComponentQtyQuery】"));
        }

        // 若输入参数qty为空，获取需求数量qty为当前获取到执行作业数量
        if (dto.getQty() == null) {
            dto.setQty(mtEo.getQty());
        }

        // 获取的交集
        List<String> routerStepIds = new ArrayList<>();

        // 获取站点生产属性
        MtModSiteManufacturing mtModSiteManufacturing =
                mtModSiteManufacturingRepository.siteManufacturingPropertyGet(tenantId, mtEo.getSiteId());

        // 3. 获取bomId
        String bomId = eoBomGet(tenantId, dto.getEoId());

        // 当前时间
        long currentTimes = System.currentTimeMillis();

        // 情况1：operationId、stepName、routerStepId均未输入或输入值均为空
        if (StringUtils.isEmpty(dto.getOperationId()) && StringUtils.isEmpty(dto.getStepName())
                && StringUtils.isEmpty(dto.getRouterStepId())) {
            if (StringUtils.isEmpty(bomId) || dto.getQty() == null) {
                // 若获取结果为空，返回空
                return Collections.emptyList();
            }

            // 获取 routerId
            String routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
            if (StringUtils.isEmpty(routerId)) {
                return returnList;
            }

            if (MtBaseConstants.YES.equals(dto.getDividedByStep())) {
                // 按步骤获取每个步骤的组件信息和需求用量
                routerStepIds.addAll(this.getRouterStepOpList(tenantId, routerId));
                if (CollectionUtils.isEmpty(routerStepIds)) {
                    return Collections.emptyList();
                }
            }

            else if (StringUtils.isEmpty(dto.getDividedByStep()) || MtBaseConstants.NO.equals(dto.getDividedByStep())) {
                // 若获取结果不为空，同时dividedByStep为空或者为N或者为null
                // 表示获取执行作业的组件用量传入bomId和第二步获取到的需求数量qty、和输入参数
                // 4.调用API{ bomComponentQtyCalculate }获取bomComponentId、materialId、componentQty
                MtBomComponentVO5 bomComponentVO5 = new MtBomComponentVO5();
                bomComponentVO5.setBomId(bomId);
                bomComponentVO5.setQty(dto.getQty());
                bomComponentVO5.setAttritionFlag("Y");
                List<MtBomComponentVO2> resultList =
                        mtBomComponentRepository.bomComponentQtyCalculate(tenantId, bomComponentVO5);

                if (CollectionUtils.isEmpty(resultList)) {
                    return Collections.emptyList();
                }

                List<String> bomComponentIds = resultList.stream().map(MtBomComponentVO2::getBomComponentId)
                        .collect(Collectors.toList());

                // 4. 根据获取到的bomComponentId调用API{ bomComponentBasicBatchGet }，获取组件行号lineNumber和数量qty
                List<MtBomComponentVO13> bomComponentVO13s =
                        mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIds);

                Map<String, MtBomComponentVO13> mtBomComponentMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(bomComponentVO13s)) {
                    List<MtBomComponentVO13> bomComponents = bomComponentVO13s.stream()
                            .filter(t -> t.getDateFrom().getTime() <= currentTimes && t.getDateTo() == null
                                    || t.getDateTo().getTime() > currentTimes)
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(bomComponents)) {
                        mtBomComponentMap = bomComponents.stream()
                                .collect(Collectors.toMap(MtBomComponentVO13::getBomComponentId, t -> t));
                    }

                }

                // 5.bomId调用API{ bomBasicGet}获取装配清单基本数量primaryQty
                MtBomVO7 mtBomVO7 = mtBomRepository.bomBasicGet(tenantId, bomId);
                if (mtBomVO7 == null) {
                    throw new MtException("MT_ORDER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0005", "ORDER", "bomId", "【API：attritionLimitEoComponentQtyQuery】"));
                }
                Double primaryQty = mtBomVO7.getPrimaryQty();


                // 若获取到值不为‘COMPONENT_ATTRITION’
                if (mtModSiteManufacturing == null || !"COMPONENT_ATTRITION"
                        .equals(mtModSiteManufacturing.getAttritionCalculateStrategy())) {
                    Map<String, MtBomComponentVO13> finalMtBomComponentMap = mtBomComponentMap;

                    // 返回结果
                    return resultList.stream().map(t -> {
                        MtBomComponentVO19 vo19 = new MtBomComponentVO19();
                        vo19.setBomComponentId(t.getBomComponentId());
                        vo19.setMaterialId(t.getMaterialId());
                        vo19.setComponentQty(t.getComponentQty());

                        vo19.setBomId(mtBomVO7.getBomId());

                        MtBomComponentVO13 componentVO13 = finalMtBomComponentMap.get(t.getBomComponentId());
                        if (componentVO13 != null) {
                            vo19.setSequence(componentVO13.getLineNumber());
                            vo19.setPerQty(BigDecimal.valueOf(componentVO13.getQty())
                                    .divide(BigDecimal.valueOf(primaryQty), 10, BigDecimal.ROUND_HALF_DOWN)
                                    .doubleValue());
                        }
                        return vo19;
                    }).collect(Collectors.toList());

                } else {
                    // 5. 表示计算组件损耗时还需要考虑订单物料的损耗率
                    // 5.1. 获取损耗策略
                    MtPfepInventoryVO queryVO = new MtPfepInventoryVO();
                    queryVO.setSiteId(mtEo.getSiteId());
                    queryVO.setMaterialId(mtEo.getMaterialId());
                    queryVO.setOrganizationType("PRODUCTIONLINE");
                    queryVO.setOrganizationId(mtEo.getProductionLineId());
                    MtPfepManufacturing mtPfepManufacturing = mtPfepManufacturingRepository
                            .pfepManufacturingAttritionControlGet(tenantId, queryVO);

                    String attritionControlType = "FIX";
                    BigDecimal attritionControlQty = BigDecimal.ZERO;

                    if (StringUtils.isNotEmpty(mtPfepManufacturing.getAttritionControlType())
                            && mtPfepManufacturing.getAttritionControlQty() != null) {
                        attritionControlQty = new BigDecimal(mtPfepManufacturing.getAttritionControlQty().toString());
                        attritionControlType = mtPfepManufacturing.getAttritionControlType();
                    }

                    // 5.2. 计算组件用量
                    for (MtBomComponentVO2 tempVo : resultList) {
                        MtBomComponentVO19 vo19 = new MtBomComponentVO19();
                        vo19.setBomComponentId(tempVo.getBomComponentId());
                        vo19.setMaterialId(tempVo.getMaterialId());

                        vo19.setBomId(mtBomVO7.getBomId());

                        if ("FIX".equals(attritionControlType)) {
                            // 求和
                            vo19.setComponentQty(new BigDecimal(tempVo.getComponentQty().toString())
                                    .add(attritionControlQty).doubleValue());
                        }
                        if ("PERCENT".equals(attritionControlType)) {
                            // 1 + attritionControlQty/100
                            BigDecimal percent = new BigDecimal(1 + attritionControlQty
                                    .divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_DOWN)
                                    .doubleValue());
                            vo19.setComponentQty(new BigDecimal(tempVo.getComponentQty().toString()).multiply(percent)
                                    .doubleValue());
                        }

                        MtBomComponentVO13 componentVO13 = mtBomComponentMap.get(tempVo.getBomComponentId());
                        if (componentVO13 != null) {
                            vo19.setSequence(componentVO13.getLineNumber());
                            vo19.setPerQty(BigDecimal.valueOf(componentVO13.getQty())
                                    .divide(BigDecimal.valueOf(primaryQty), 10, BigDecimal.ROUND_HALF_DOWN)
                                    .doubleValue());
                        }
                        returnList.add(vo19);
                    }
                    return returnList;
                }

            }
            // else if (MtBaseConstants.YES.equals(dto.getDividedByStep())) {
            // // 首先调用API{ eoRouterGet }获取执行作业的工艺路线routerId，如果为空，则返回空，否则继续执行
            // routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
            // if (StringUtils.isEmpty(routerId)) {
            // return Collections.emptyList();
            // }
            // // 调用API{ routerStepListQuery }routeStepId相当于第七步的in_routerStepId
            // List<MtRouterStepVO5> routerStepOpVOS =
            // this.mtRouterStepRepository.routerStepListQuery(tenantId, routerId);
            // if (CollectionUtils.isNotEmpty(routerStepOpVOS)) {
            // routerStepIds.addAll(routerStepOpVOS.stream().map(MtRouterStepVO5::getRouterStepId)
            // .collect(Collectors.toList()));
            // }
            // }
        } else {
            // 首先调用API{ eoRouterGet }获取执行作业的工艺路线routerId，如果为空，则返回空，否则继续执行
            String routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
            // 若输入参数operationId、stepName、routerStepId不全为空，表示获取生产指令的组件用量需求
            // 第六步，通过输入参数获取步骤ID
            if (StringUtils.isEmpty(routerId)) {
                return Collections.emptyList();
            }

            // 若operationId不为空，调用API{ operationStepQuery }

            List<String> operationRouterStepIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(dto.getOperationId())) {
                operationRouterStepIds =
                        mtRouterStepRepository.operationStepQuery(tenantId, dto.getOperationId(), routerId);
            }

            String stepNameRouterStepId = null;
            if (StringUtils.isNotEmpty(dto.getStepName())) {
                stepNameRouterStepId = getRouterStepOp(tenantId, routerId, dto.getStepName());
            }

            if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
                if (StringUtils.isEmpty(dto.getStepName()) && StringUtils.isEmpty(dto.getOperationId())) {
                    // stepName 和 OperationId 都没有输入，则取输入routerStepId
                    routerStepIds.add(dto.getRouterStepId());
                } else if (StringUtils.isNotEmpty(dto.getStepName()) && StringUtils.isNotEmpty(dto.getOperationId())) {
                    // stepName 和 operationId 都有输入，则取交集，任一查询为空，交集为空
                    if (stepNameRouterStepId != null && CollectionUtils.isNotEmpty(operationRouterStepIds)) {
                        if (stepNameRouterStepId.equals(dto.getRouterStepId())
                                && operationRouterStepIds.contains(dto.getRouterStepId())) {
                            routerStepIds.add(dto.getRouterStepId());
                        }
                    }
                } else if (StringUtils.isNotEmpty(dto.getOperationId())
                        && CollectionUtils.isNotEmpty(operationRouterStepIds)
                        && operationRouterStepIds.contains(dto.getRouterStepId())) {
                    // operationId 有输入，则取交集，查询为空，交集为空
                    routerStepIds.add(dto.getRouterStepId());
                } else if (StringUtils.isNotEmpty(dto.getStepName())
                        && dto.getRouterStepId().equals(stepNameRouterStepId)) {
                    // stepName 有输入，则取交集，查询为空，交集为空
                    routerStepIds.add(dto.getRouterStepId());
                }
            } else {
                if (StringUtils.isNotEmpty(dto.getStepName()) && StringUtils.isNotEmpty(dto.getOperationId())) {
                    // stepName 和 operationId 都有输入，则取交集，任一查询为空，交集为空
                    if (stepNameRouterStepId != null && CollectionUtils.isNotEmpty(operationRouterStepIds)) {
                        if (operationRouterStepIds.contains(stepNameRouterStepId)) {
                            routerStepIds.add(stepNameRouterStepId);
                        }
                    }
                } else if (StringUtils.isNotEmpty(dto.getOperationId())
                        && CollectionUtils.isNotEmpty(operationRouterStepIds)) {
                    // operationId 有输入，则取查询结果
                    routerStepIds.addAll(operationRouterStepIds);
                } else if (StringUtils.isNotEmpty(dto.getStepName()) && stepNameRouterStepId != null) {
                    // stepName 有输入，则取查询结果
                    routerStepIds.add(stepNameRouterStepId);
                }
            }
        }

        // 第七步,根据获取到的交集in_routerStepId、输入参数bomComponentId，调用API{ routerOperationComponentPerQtyQuery }
        for (String routerStepId : routerStepIds) {
            // 7. 获取步骤组件用量
            MtRouterOpComponentVO1 vo1 = new MtRouterOpComponentVO1();
            vo1.setRouterStepId(routerStepId);
            vo1.setBomComponentId(dto.getBomComponentId());
            List<MtRouterOpComponentVO> routerOperationComponentList =
                    mtRouterOperationComponentRepository.routerOperationComponentPerQtyQuery(tenantId, vo1);

            if (CollectionUtils.isNotEmpty(routerOperationComponentList)) {
                // 新增输出参数：
                MtRouterOperation mtRouterOperation =
                        mtRouterOperationRepository.routerOperationGet(tenantId, routerStepId);

                // 批量获取组件信息
                List<String> bomComponentIdList = routerOperationComponentList.stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getBomComponentId()))
                        .map(MtRouterOpComponentVO::getBomComponentId).distinct().collect(Collectors.toList());

                // 转为Map数据
                Map<String, MtBomComponentVO13> bomComponentVO13Map = new HashMap<>();
                if (CollectionUtils.isNotEmpty(bomComponentIdList)) {
                    List<MtBomComponentVO13> bomComponentVO13s =
                            mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIdList);
                    if (CollectionUtils.isNotEmpty(bomComponentVO13s)) {
                        bomComponentVO13Map = bomComponentVO13s.stream()
                                .collect(Collectors.toMap(MtBomComponentVO13::getBomComponentId, t -> t));
                    }
                }

                routerOperationComponentList.sort(Comparator.comparingDouble(
                        (MtRouterOpComponentVO c) -> c.getSequence() == null ? 0.0D : c.getSequence()));
                // 若获取到值不为‘COMPONENT_ATTRITION’
                if (mtModSiteManufacturing == null || !"COMPONENT_ATTRITION"
                        .equals(mtModSiteManufacturing.getAttritionCalculateStrategy())) {
                    for (MtRouterOpComponentVO t : routerOperationComponentList) {
                        // 9.获取bom组件信息
                        if (MapUtils.isNotEmpty(bomComponentVO13Map)) {
                            MtBomComponentVO13 componentVO13 = bomComponentVO13Map.get(t.getBomComponentId());
                            if (componentVO13 == null) {
                                continue;
                            }


                            // 筛选有效数据：并筛选获取有效数据，筛选逻辑：DATE_FROM小于等于当前日期、且DATE_TO为空或大于当前日期
                            if (componentVO13.getDateFrom().getTime() > currentTimes) {
                                continue;
                            }

                            MtRouterStep step = new MtRouterStep();
                            step.setTenantId(tenantId);
                            step.setRouterStepId(routerStepId);
                            MtRouterStep routerStep = mtRouterStepRepository.selectOne(step);

                            if (componentVO13.getDateTo() == null
                                    || componentVO13.getDateTo().getTime() > currentTimes) {
                                MtBomComponentVO19 result = new MtBomComponentVO19();
                                result.setRouterOperationComponentId(t.getRouterOperationComponentId());
                                result.setRouterOperationId(t.getRouterOperationId());
                                result.setBomComponentId(t.getBomComponentId());
                                result.setSequence(t.getSequence());
                                result.setPerQty(t.getPerQty());
                                result.setRouterStepId(routerStepId);
                                result.setMaterialId(componentVO13.getMaterialId());

                                // 增加返回参数
                                result.setRouterId(routerStep.getRouterId());
                                result.setBomId(componentVO13.getBomId());

                                if (mtRouterOperation != null) {
                                    result.setOperationId(mtRouterOperation.getOperationId());
                                }

                                // 8. 计算数量
                                if (dto.getQty() != null && t.getPerQty() != null) {
                                    BigDecimal calculateQty = BigDecimal.valueOf(dto.getQty())
                                            .multiply(BigDecimal.valueOf(t.getPerQty()));
                                    result.setComponentQty(calculateQty.doubleValue());
                                }

                                returnList.add(result);
                            }
                        }
                    }
                } else {
                    // 5.1. 获取损耗策略
                    MtPfepInventoryVO queryVO = new MtPfepInventoryVO();
                    queryVO.setSiteId(mtEo.getSiteId());
                    queryVO.setMaterialId(mtEo.getMaterialId());
                    queryVO.setOrganizationType("PRODUCTIONLINE");
                    queryVO.setOrganizationId(mtEo.getProductionLineId());
                    MtPfepManufacturing mtPfepManufacturing = mtPfepManufacturingRepository
                            .pfepManufacturingAttritionControlGet(tenantId, queryVO);

                    String attritionControlType = "FIX";
                    BigDecimal attritionControlQty = BigDecimal.ZERO;

                    if (StringUtils.isNotEmpty(mtPfepManufacturing.getAttritionControlType())
                            && mtPfepManufacturing.getAttritionControlQty() != null) {
                        attritionControlQty = new BigDecimal(mtPfepManufacturing.getAttritionControlQty().toString());
                        attritionControlType = mtPfepManufacturing.getAttritionControlType();
                    }
                    for (MtRouterOpComponentVO t : routerOperationComponentList) {


                        // 9.获取bom组件信息
                        if (MapUtils.isNotEmpty(bomComponentVO13Map)) {
                            MtBomComponentVO13 componentVO13 = bomComponentVO13Map.get(t.getBomComponentId());
                            if (componentVO13 == null) {
                                continue;
                            }

                            // 筛选有效数据：并筛选获取有效数据，筛选逻辑：DATE_FROM小于等于当前日期、且DATE_TO为空或大于当前日期
                            if (componentVO13.getDateFrom().getTime() > currentTimes) {
                                continue;
                            }

                            MtRouterStep step = new MtRouterStep();
                            step.setTenantId(tenantId);
                            step.setRouterStepId(routerStepId);
                            MtRouterStep routerStep = mtRouterStepRepository.selectOne(step);

                            if (componentVO13.getDateTo() == null
                                    || componentVO13.getDateTo().getTime() > currentTimes) {
                                MtBomComponentVO19 result = new MtBomComponentVO19();
                                result.setRouterOperationComponentId(t.getRouterOperationComponentId());
                                result.setRouterOperationId(t.getRouterOperationId());
                                result.setBomComponentId(t.getBomComponentId());
                                result.setSequence(t.getSequence());
                                result.setPerQty(t.getPerQty());
                                result.setRouterStepId(routerStepId);
                                result.setMaterialId(componentVO13.getMaterialId());

                                // 增加返回参数
                                result.setRouterId(routerStep.getRouterId());
                                result.setBomId(componentVO13.getBomId());

                                if (mtRouterOperation != null) {
                                    result.setOperationId(mtRouterOperation.getOperationId());
                                }

                                BigDecimal calculateQty = BigDecimal.ZERO;
                                if (dto.getQty() != null && t.getPerQty() != null) {
                                    calculateQty = BigDecimal.valueOf(dto.getQty())
                                            .multiply(BigDecimal.valueOf(t.getPerQty()));
                                }
                                // 8. 计算数量
                                if ("FIX".equals(attritionControlType)) {
                                    // 求和
                                    result.setComponentQty(new BigDecimal(calculateQty.doubleValue())
                                            .add(attritionControlQty).doubleValue());
                                }
                                if ("PERCENT".equals(attritionControlType)) {
                                    // 1 + attritionControlQty/100
                                    BigDecimal percent = new BigDecimal(1 + attritionControlQty
                                            .divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_DOWN)
                                            .doubleValue());
                                    result.setComponentQty(new BigDecimal(calculateQty.doubleValue()).multiply(percent)
                                            .doubleValue());
                                }
                                returnList.add(result);
                            }
                        }
                    }
                }
            }
        }
        return returnList;
    }

    @Override
    public void eoBomValidate(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoBomValidate】"));
        }

        MtEo mtEo = new MtEo();
        mtEo.setTenantId(tenantId);
        mtEo.setEoId(eoId);
        mtEo = mtEoMapper.selectOne(mtEo);
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0020", "ORDER", "【API:eoBomValidate】"));
        }

        String bomId = eoBomGet(tenantId, eoId);
        if (StringUtils.isNotEmpty(bomId)) {
            MtBomVO3 bvtmp = new MtBomVO3();
            bvtmp.setBomId(bomId);
            mtBomRepository.bomAvailableVerify(tenantId, bvtmp);

            List<String> siteIds = this.mtBomSiteAssignRepository.bomLimitEnableSiteQuery(tenantId, bomId);
            if (!siteIds.contains(mtEo.getSiteId())) {
                throw new MtException("MT_ORDER_0152", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0152", "ORDER", "eoId", "eoId", "BOM", "【API:eoBomValidate】"));
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoBomVO2 eoBomUpdate(Long tenantId, MtEoBomVO dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoBomUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getBomId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "bomId", "【API:eoBomUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eventId", "【API:eoBomUpdate】"));
        }

        MtEo mtEo = this.mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (null == mtEo) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0020", "ORDER", "【API:eoBomUpdate】"));
        }

        MtEoBom mtEoBom = new MtEoBom();
        mtEoBom.setTenantId(tenantId);
        mtEoBom.setEoId(dto.getEoId());
        mtEoBom = this.mtEoBomMapper.selectOne(mtEoBom);
        String bomId = (null == mtEoBom) ? "" : mtEoBom.getBomId();

        if (StringUtils.isNotEmpty(bomId) && null != mtEoBom) {
            mtEoBom.setTenantId(tenantId);
            mtEoBom.setBomId(dto.getBomId());
            self().updateByPrimaryKeySelective(mtEoBom);
        } else {
            mtEoBom = new MtEoBom();
            mtEoBom.setTenantId(tenantId);
            mtEoBom.setEoId(dto.getEoId());
            mtEoBom.setBomId(dto.getBomId());
            self().insertSelective(mtEoBom);
        }

        MtEoBomHis mtEoBomHis = new MtEoBomHis();
        mtEoBomHis.setTenantId(tenantId);
        mtEoBomHis.setEoBomId(mtEoBom.getEoBomId());
        mtEoBomHis.setEoId(mtEoBom.getEoId());
        mtEoBomHis.setBomId(mtEoBom.getBomId());
        mtEoBomHis.setEventId(dto.getEventId());
        this.mtEoBomHisRepository.insertSelective(mtEoBomHis);

        MtEoVO vo = new MtEoVO();
        vo.setEventId(dto.getEventId());
        vo.setEoId(mtEo.getEoId());
        vo.setEoNum(mtEo.getEoNum());
        vo.setSiteId(mtEo.getSiteId());
        vo.setWorkOrderId(mtEo.getWorkOrderId());
        vo.setStatus(mtEo.getStatus());
        vo.setLastEoStatus(mtEo.getLastEoStatus());
        vo.setProductionLineId(mtEo.getProductionLineId());
        vo.setWorkcellId(mtEo.getWorkcellId());
        vo.setPlanStartTime(mtEo.getPlanStartTime());
        vo.setPlanEndTime(mtEo.getPlanEndTime());
        vo.setQty(mtEo.getQty());
        vo.setUomId(mtEo.getUomId());
        vo.setEoType(mtEo.getEoType());
        vo.setValidateFlag("N");
        vo.setIdentification(mtEo.getIdentification());
        vo.setMaterialId(mtEo.getMaterialId());
        this.mtEoRepository.eoUpdate(tenantId, vo, "N");

        MtEoBomVO2 result = new MtEoBomVO2();
        result.setEoBomId(mtEoBom.getEoBomId());
        result.setEoBomHisId(mtEoBomHis.getEoBomHisId());

        return result;
    }

    @Override
    public void eoBomUpdateValidate(Long tenantId, MtEoBomVO dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoBomUpdateValidate】"));
        }
        if (StringUtils.isEmpty(dto.getBomId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "bomId", "【API:eoBomUpdateValidate】"));
        }

        MtEo mtEo = this.mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (null == mtEo) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0020", "ORDER", "【API:eoBomUpdateValidate】"));
        }
        String bomId = eoBomGet(tenantId, dto.getEoId());
        String status = mtEo.getStatus();

        MtBomVO3 bvtmp = new MtBomVO3();
        bvtmp.setBomId(dto.getBomId());
        try {
            mtBomRepository.bomAvailableVerify(tenantId, bvtmp);
        } catch (MtException e) {
            throw new MtException("MT_ORDER_0131", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0131", "ORDER", "【API:eoBomUpdateValidate】"));
        }
        MtBomVO7 mtBom = mtBomRepository.bomBasicGet(tenantId, dto.getBomId());
        if (mtBom == null || !"EO".equals(mtBom.getBomType())) {
            throw new MtException("MT_ORDER_0132", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0132", "ORDER", "【EO】", "【API:eoBomUpdateValidate】"));
        }

        // Step 2
        if (!"NEW".equals(status) && !"HOLD".equals(status)) {
            throw new MtException("MT_ORDER_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0044", "ORDER", "【‘NEW’，‘HOLD’】", "【API:eoBomUpdateValidate】"));
        }

        // Step 3
        MtEoComponentActualVO10 vo10 = new MtEoComponentActualVO10();
        vo10.setBomId(bomId);
        vo10.setEoId(dto.getEoId());
        List<MtEoComponentActual> actualList =
                mtEoComponentActualRepository.componentLimitEoComponentAssembleActualQuery(tenantId, vo10);
        for (MtEoComponentActual t : actualList) {
            if ((t.getAssembleQty() != null
                    && (new BigDecimal(t.getAssembleQty().toString())).compareTo(BigDecimal.ZERO) != 0)
                    || (t.getScrappedQty() != null && new BigDecimal(t.getScrappedQty().toString())
                    .compareTo(BigDecimal.ZERO) != 0)) {
                throw new MtException("MT_ORDER_0138", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0138", "ORDER", "【API:eoBomUpdateValidate】"));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoBomBatchUpdate(Long tenantId, MtEoBomVO3 dto) {
        // 第一步，判断输入参数eoId、bomId、eventId是否有值，否则返回错误消息
        List<String> eoIds = new ArrayList<>();

        Map<String, MtEo> eoMap = new HashMap<>();
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eventId", "【API:eoBomBatchUpdate】"));
        }

        if (CollectionUtils.isEmpty(dto.getEoBomList())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "[eoId,BomId]列表", "【API:eoBomBatchUpdate】"));
        }

        if (CollectionUtils.isNotEmpty(dto.getEoBomList())) {
            if (dto.getEoBomList().stream().anyMatch(t -> StringUtils.isEmpty(t.getEoId()))) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0001", "ORDER", "eoId", "【API:eoBomBatchUpdate】"));
            }

            if (dto.getEoBomList().stream().anyMatch(t -> StringUtils.isEmpty(t.getBomId()))) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0001", "ORDER", "bomId", "【API:eoBomBatchUpdate】"));
            }

            // 第二步，根据输入参数列表判断eoId，判断执行作业是否存在，若不存在报错
            eoIds = dto.getEoBomList().stream().map(MtEoBomVO4::getEoId).collect(Collectors.toList());
            List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
            if (CollectionUtils.isEmpty(mtEos) || mtEos.size() != dto.getEoBomList().size()) {
                throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0020", "ORDER", "【API:eoBomBatchUpdate】"));
            } else {
                eoMap = mtEos.stream().collect(Collectors.toMap(MtEo::getEoId, t -> t));
            }
        }

        // 第三步，确认执行作业存在后
        // 根据输入参数eoId依次调用API{eoBomGet}判断更新或新增
        List<String> sqlList = new ArrayList<>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date date = new Date();

        List<String> mtEoBomIds = this.customDbRepository.getNextKeys("mt_eo_bom_s", dto.getEoBomList().size());
        List<String> mtEoBomCids = this.customDbRepository.getNextKeys("mt_eo_bom_cid_s", dto.getEoBomList().size());
        List<String> mtEoBomHisIds = this.customDbRepository.getNextKeys("mt_eo_bom_his_s", dto.getEoBomList().size());
        List<String> mtEoBomHisCidIds =
                this.customDbRepository.getNextKeys("mt_eo_bom_his_cid_s", dto.getEoBomList().size());
        List<String> mtEoCids = this.customDbRepository.getNextKeys("mt_eo_cid_s", dto.getEoBomList().size());
        List<String> mtEoHisIds = this.customDbRepository.getNextKeys("mt_eo_his_s", dto.getEoBomList().size());
        List<String> mtEoHisCids = this.customDbRepository.getNextKeys("mt_eo_his_cid_s", dto.getEoBomList().size());

        // 批量获取数据
        Map<String, MtEoBom> eoBomMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eoIds)) {
            List<MtEoBom> eoBoms = mtEoBomMapper.eoBomBatchGet(tenantId, eoIds);
            if (CollectionUtils.isNotEmpty(eoBoms)) {
                eoBomMap = eoBoms.stream().collect(Collectors.toMap(MtEoBom::getEoId, t -> t));
            }
        }
        int index = 0;
        for (MtEoBomVO4 vo4 : dto.getEoBomList()) {
            MtEoBom mtEoBom = eoBomMap.get(vo4.getEoId());
            if (null != mtEoBom) {
                // 更新逻辑
                mtEoBom.setBomId(vo4.getBomId());
                mtEoBom.setTenantId(tenantId);
                mtEoBom.setCid(Long.valueOf(mtEoBomCids.get(index)));
                mtEoBom.setLastUpdatedBy(userId);
                mtEoBom.setLastUpdateDate(date);
                sqlList.addAll(customDbRepository.getUpdateSql(mtEoBom));
            } else {
                // 新增逻辑
                mtEoBom = new MtEoBom();
                mtEoBom.setEoId(vo4.getEoId());
                mtEoBom.setBomId(vo4.getBomId());
                mtEoBom.setTenantId(tenantId);
                mtEoBom.setEoBomId(mtEoBomIds.get(index));
                mtEoBom.setCid(Long.valueOf(mtEoBomCids.get(index)));
                mtEoBom.setCreatedBy(userId);
                mtEoBom.setCreationDate(date);
                mtEoBom.setLastUpdatedBy(userId);
                mtEoBom.setLastUpdateDate(date);
                sqlList.addAll(customDbRepository.getInsertSql(mtEoBom));
            }
            // 第四步，更新或新增成功后记录MT_EO_BOM历史，在MT_EO_BOM_HIS中新增数据
            MtEoBomHis mtEoBomHis = new MtEoBomHis();
            mtEoBomHis.setEoBomId(mtEoBom.getEoBomId());
            mtEoBomHis.setEoId(mtEoBom.getEoId());
            mtEoBomHis.setBomId(mtEoBom.getBomId());
            mtEoBomHis.setEventId(dto.getEventId());
            mtEoBomHis.setTenantId(tenantId);
            mtEoBomHis.setEoBomHisId(mtEoBomHisIds.get(index));
            mtEoBomHis.setCid(Long.valueOf(mtEoBomHisCidIds.get(index)));
            mtEoBomHis.setCreatedBy(userId);
            mtEoBomHis.setLastUpdatedBy(userId);
            mtEoBomHis.setCreationDate(date);
            mtEoBomHis.setLastUpdateDate(date);
            sqlList.addAll(customDbRepository.getInsertSql(mtEoBomHis));

            // 第五步，第四步执行成功后，传入eoId和eventId依次调用API{eoUpdate}更新执行作业验证标识validateFlag = N
            MtEo mtEo = eoMap.get(vo4.getEoId());
            if (mtEo != null) {
                mtEo.setTenantId(tenantId);
                mtEo.setValidateFlag("N");
                mtEo.setLatestHisId(mtEoHisIds.get(index));
                mtEo.setCid(Long.valueOf(mtEoCids.get(index)));
                mtEo.setLastUpdatedBy(userId);
                mtEo.setLastUpdateDate(date);
                sqlList.addAll(customDbRepository.getUpdateSql(mtEo));
                // 记录历史
                MtEoHis mtEoHis = new MtEoHis();
                mtEoHis.setTenantId(tenantId);
                mtEoHis.setEoId(mtEo.getEoId());
                mtEoHis.setEoNum(mtEo.getEoNum());
                mtEoHis.setSiteId(mtEo.getSiteId());
                mtEoHis.setWorkOrderId(mtEo.getWorkOrderId());
                mtEoHis.setStatus(mtEo.getStatus());
                mtEoHis.setLastEoStatus(mtEo.getLastEoStatus());
                mtEoHis.setProductionLineId(mtEo.getProductionLineId());
                mtEoHis.setWorkcellId(mtEo.getWorkcellId());
                mtEoHis.setPlanStartTime(mtEo.getPlanStartTime());
                mtEoHis.setPlanEndTime(mtEo.getPlanEndTime());
                mtEoHis.setQty(mtEo.getQty());
                mtEoHis.setUomId(mtEo.getUomId());
                mtEoHis.setEoType(mtEo.getEoType());
                mtEoHis.setValidateFlag(mtEo.getValidateFlag());
                mtEoHis.setIdentification(mtEo.getIdentification());
                mtEoHis.setMaterialId(mtEo.getMaterialId());
                mtEoHis.setEventId(dto.getEventId());
                mtEoHis.setTrxQty(mtEo.getQty());
                mtEoHis.setEoHisId(mtEoHisIds.get(index));
                mtEoHis.setCid(Long.valueOf(mtEoHisCids.get(index)));
                mtEoHis.setCreationDate(date);
                mtEoHis.setCreatedBy(userId);
                mtEoHis.setLastUpdateDate(date);
                mtEoHis.setLastUpdatedBy(userId);
                sqlList.addAll(customDbRepository.getInsertSql(mtEoHis));
            }
            index++;
        }
        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
    }

    /**
     * 服务于API：woComponentQtyQuery 获取嵌套工艺路线下的步骤
     *
     * @author chuang.yang
     * @date 2020/1/16
     * @param tenantId
     * @param routerId
     * @return java.util.List<hmes.router_step.view.RouterStepOpVO>
     */
    private List<String> getRouterStepOpList(Long tenantId, String routerId) {
        List<String> resultList = new ArrayList<>();

        // 查询当前工艺路线的步骤
        List<MtRouterStepVO5> routerStepOpVOList = mtRouterStepRepository.routerStepListQuery(tenantId, routerId);

        if (CollectionUtils.isNotEmpty(routerStepOpVOList)) {
            List<String> routerStepList = routerStepOpVOList.stream().map(MtRouterStepVO5::getRouterStepId)
                    .collect(Collectors.toList());

            // 筛选为嵌套工艺路线的步骤
            List<MtRouterStepVO5> routerTypeRouterStepList = routerStepOpVOList.stream()
                    .filter(t -> "ROUTER".equals(t.getRouterStepType())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(routerTypeRouterStepList)) {
                List<String> routerStepIds = routerTypeRouterStepList.stream().map(MtRouterStepVO5::getRouterStepId)
                        .collect(Collectors.toList());

                // 结果不需要ROUTER类型
                routerStepList.removeAll(routerStepIds);

                List<MtRouterLink> mtRouterLinks = mtRouterLinkRepository.routerLinkBatchGet(tenantId, routerStepIds);
                List<String> routerIds = mtRouterLinks.stream().map(MtRouterLink::getRouterId).distinct()
                        .collect(Collectors.toList());

                for (String router : routerIds) {
                    // 获取嵌套工艺路线下的步骤
                    List<String> tempList = getRouterStepOpList(tenantId, router);
                    if (CollectionUtils.isNotEmpty(tempList)) {
                        resultList.addAll(tempList);
                    }
                }
            }

            resultList.addAll(routerStepList);
        }

        return resultList;
    }

    public List<MtBomComponentVO19> setResultList(Long tenantId, String bomComponentId, List<String> routerStepIds,
                                                  Double eoQty) {
        List<MtBomComponentVO19> resultList = new ArrayList<>();
        for (String routerStepId : routerStepIds) {
            // 7. 获取步骤组件用量
            MtRouterOpComponentVO1 vo1 = new MtRouterOpComponentVO1();
            vo1.setRouterStepId(routerStepId);
            vo1.setBomComponentId(bomComponentId);
            List<MtRouterOpComponentVO> routerOperationComponentList =
                    mtRouterOperationComponentRepository.routerOperationComponentPerQtyQuery(tenantId, vo1);

            if (CollectionUtils.isNotEmpty(routerOperationComponentList)) {
                // 新增输出参数：
                MtRouterOperation mtRouterOperation =
                        mtRouterOperationRepository.routerOperationGet(tenantId, routerStepId);

                // 批量获取组件信息
                List<String> bomComponentIdList = routerOperationComponentList.stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getBomComponentId()))
                        .map(MtRouterOpComponentVO::getBomComponentId).distinct().collect(Collectors.toList());

                // 转为Map数据
                Map<String, MtBomComponentVO13> bomComponentVO13Map = null;
                if (CollectionUtils.isNotEmpty(bomComponentIdList)) {
                    List<MtBomComponentVO13> bomComponentVO13s =
                            mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIdList);
                    if (CollectionUtils.isNotEmpty(bomComponentVO13s)) {
                        bomComponentVO13Map = bomComponentVO13s.stream()
                                .collect(Collectors.toMap(t -> t.getBomComponentId(), t -> t));
                    }
                }

                for (MtRouterOpComponentVO t : routerOperationComponentList) {
                    // 9.获取bom组件信息
                    if (MapUtils.isNotEmpty(bomComponentVO13Map)) {
                        MtBomComponentVO13 componentVO13 = bomComponentVO13Map.get(t.getBomComponentId());
                        if (componentVO13 == null) {
                            continue;
                        }

                        // 当前时间
                        long currentTimes = System.currentTimeMillis();

                        // 筛选有效数据：并筛选获取有效数据，筛选逻辑：DATE_FROM小于等于当前日期、且DATE_TO为空或大于当前日期
                        if (componentVO13.getDateFrom().getTime() > currentTimes) {
                            continue;
                        }

                        if (componentVO13.getDateTo() == null || componentVO13.getDateTo().getTime() > currentTimes) {
                            MtBomComponentVO19 result = new MtBomComponentVO19();
                            result.setRouterOperationComponentId(t.getRouterOperationComponentId());
                            result.setRouterOperationId(t.getRouterOperationId());
                            result.setBomComponentId(t.getBomComponentId());
                            result.setSequence(t.getSequence());
                            result.setPerQty(t.getPerQty());
                            result.setRouterStepId(routerStepId);
                            result.setMaterialId(componentVO13.getMaterialId());

                            if (mtRouterOperation != null) {
                                result.setOperationId(mtRouterOperation.getOperationId());
                            }

                            // 8. 计算数量
                            if (eoQty != null && t.getPerQty() != null) {
                                BigDecimal calculateQty =
                                        BigDecimal.valueOf(eoQty).multiply(BigDecimal.valueOf(t.getPerQty()));
                                result.setComponentQty(calculateQty.doubleValue());
                            }

                            resultList.add(result);
                        }
                    }
                }
            }
        }

        return resultList;
    }

    /**
     * 递归，根据stepname查找routerStepId
     *
     * @Author peng.yuan
     * @Date 2020/1/20 15:09
     * @param tenantId :
     * @param routerId :
     * @return java.lang.String
     */
    private String getRouterStepOp(Long tenantId, String routerId, String stepName) {
        String result;

        // 如果第一层查找到直接返回
        result = mtRouterStepRepository.stepNameLimitRouterStepGet(tenantId, routerId, stepName);

        if (StringUtils.isNotEmpty(result)) {
            return result;
        }

        // 查询当前工艺路线的步骤
        List<MtRouterStepVO5> routerStepOpVOList = mtRouterStepRepository.routerStepListQuery(tenantId, routerId);

        if (CollectionUtils.isNotEmpty(routerStepOpVOList)) {
            List<String> routerStepList = routerStepOpVOList.stream().map(MtRouterStepVO5::getRouterStepId)
                    .collect(Collectors.toList());

            // 筛选为嵌套工艺路线的步骤
            List<MtRouterStepVO5> routerTypeRouterStepList = routerStepOpVOList.stream()
                    .filter(t -> "ROUTER".equals(t.getRouterStepType())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(routerTypeRouterStepList)) {
                List<String> routerStepIds = routerTypeRouterStepList.stream().map(MtRouterStepVO5::getRouterStepId)
                        .collect(Collectors.toList());

                // 结果不需要ROUTER类型
                routerStepList.removeAll(routerStepIds);

                List<MtRouterLink> mtRouterLinks = mtRouterLinkRepository.routerLinkBatchGet(tenantId, routerStepIds);
                List<String> routerIds = mtRouterLinks.stream().map(MtRouterLink::getRouterId).distinct()
                        .collect(Collectors.toList());

                for (String router : routerIds) {

                    // 获取嵌套工艺路线下的步骤
                    result = getRouterStepOp(tenantId, router, stepName);
                    if (StringUtils.isNotEmpty(result)) {
                        return result;
                    }

                }
            }
        }

        return result;
    }
}
