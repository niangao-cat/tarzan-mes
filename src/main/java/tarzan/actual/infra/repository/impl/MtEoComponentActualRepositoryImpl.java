package tarzan.actual.infra.repository.impl;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import io.tarzan.common.domain.util.MtFieldsHelper;
import io.tarzan.common.domain.util.MtIdHelper;
import tarzan.actual.domain.entity.MtAssembleConfirmActual;
import tarzan.actual.domain.entity.MtAssembleConfirmActualHis;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.actual.domain.entity.MtEoComponentActualHis;
import tarzan.actual.domain.repository.MtAssembleConfirmActualRepository;
import tarzan.actual.domain.repository.MtEoComponentActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtEoComponentActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.material.domain.entity.MtPfepInventory;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtPfepInventoryRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.material.domain.vo.MtPfepInventoryVO3;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.*;
import tarzan.modeling.domain.entity.MtModProdLineManufacturing;
import tarzan.modeling.domain.repository.MtModProdLineManufacturingRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtEoBom;
import tarzan.order.domain.entity.MtEoRouter;
import tarzan.order.domain.repository.MtEoBomRepository;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.vo.MtEoBomVO;
import tarzan.order.domain.vo.MtEoVO19;
import tarzan.order.domain.vo.MtEoVO20;

/**
 * 执行作业组件装配实绩，记录执行作业物料和组件实际装配情况 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtEoComponentActualRepositoryImpl extends BaseRepositoryImpl<MtEoComponentActual>
                implements MtEoComponentActualRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(MtEoComponentActualRepositoryImpl.class);
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoBomRepository mtEoBomRepository;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtBomReferencePointRepository mtBomReferencePointRepository;

    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtPfepInventoryRepository mtPfepInventoryRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtModProdLineManufacturingRepository mtModProdLineManufacturingRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtEoComponentActualMapper mtEoComponentActualMapper;

    @Autowired
    private MtBomSubstituteGroupRepository mtBomSubstituteGroupRepository;

    @Autowired
    private MtBomSubstituteRepository mtBomSubstituteRepository;

    @Autowired
    private MtRouterOperationComponentRepository mtRouterOperationComponentRepository;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtAssembleConfirmActualRepository mtAssembleConfirmActualRepository;

    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Override
    public List<MtEoComponentActualVO1> eoComponentReferencePointQuery(Long tenantId, MtEoComponentActualVO dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "eoId", "【API:eoComponentReferencePointQuery】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "materialId", "【API:eoComponentReferencePointQuery】"));
        }

        // Step2获取执行作业的装配清单
        String bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
        if (StringUtils.isEmpty(bomId)) {
            return Collections.emptyList();
        }

        String routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());

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
        Map<String, List<MtEoComponentActualVO1>> resultMap = new HashMap<String, List<MtEoComponentActualVO1>>();

        MtBomReferencePointVO3 bomReferencePointVO3 = new MtBomReferencePointVO3();
        for (String bomComponentId : bomComponentIds) {
            bomReferencePointVO3.setBomComponentId(bomComponentId);
            List<MtBomReferencePoint> pointList = mtBomReferencePointRepository
                            .bomComponentReferencePointQuery(tenantId, bomReferencePointVO3);

            final List<MtEoComponentActualVO1> result = new ArrayList<MtEoComponentActualVO1>();
            pointList.stream().forEach(t -> {
                MtEoComponentActualVO1 mtEoComponentVO1 = new MtEoComponentActualVO1();
                mtEoComponentVO1.setLineNumber(t.getLineNumber());
                mtEoComponentVO1.setReferencePoint(t.getReferencePoint());
                mtEoComponentVO1.setQty(t.getQty());
                result.add(mtEoComponentVO1);
            });

            resultMap.put(bomComponentId, result);
        }

        if (StringUtils.isNotEmpty(dto.getOperationId())) {
            Iterator<String> keys = resultMap.keySet().iterator();
            while (keys.hasNext()) {
                List<MtEoComponentActualVO1> list = resultMap.get(keys.next());
                list.stream().forEach(t -> t.setOperationId(dto.getOperationId()));
            }
        } else {
            if (StringUtils.isEmpty(routerId)) {
                Iterator<String> keys = resultMap.keySet().iterator();
                while (keys.hasNext()) {
                    List<MtEoComponentActualVO1> list = resultMap.get(keys.next());
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
                        List<MtEoComponentActualVO1> list = resultMap.get(bomComponentId);
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

        final List<MtEoComponentActualVO1> totalList = new ArrayList<MtEoComponentActualVO1>();
        Iterator<String> keys = resultMap.keySet().iterator();
        while (keys.hasNext()) {
            totalList.addAll(resultMap.get(keys.next()));
        }
        return totalList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoComponentActualUpdate(Long tenantId, MtEoComponentActualHis dto) {
        // Step1判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API:eoComponentActualUpdate】"));
        }
        if (dto.getAssembleQty() != null && dto.getTrxAssembleQty() != null) {
            throw new MtException("MT_ORDER_0102", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0102", "ORDER", "assembleQty、trxScrappedQty", "【API:eoComponentActualUpdate】"));
        }
        if (dto.getScrappedQty() != null && dto.getTrxScrappedQty() != null) {
            throw new MtException("MT_ORDER_0102", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0102", "ORDER", "scrappedQty、trxScrappedQty", "【API:eoComponentActualUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEoComponentActualId()) && StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0032", "ORDER", "eoComponentActualId、eoId", "【API:eoComponentActualUpdate】"));
        }

        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date(System.currentTimeMillis());
        List<MtEoComponentActual> list;
        List<String> sqlList = new ArrayList<String>();

        MtBomComponentVO8 vo8 = null;
        if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
            vo8 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
        }

        // Step2判断eoComponentActualId是否有值输入
        if (StringUtils.isEmpty(dto.getEoComponentActualId())) {
            MtEoComponentActualVO9 mtEoComponentVO9 = new MtEoComponentActualVO9();
            mtEoComponentVO9.setEoId(dto.getEoId());
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                mtEoComponentVO9.setBomComponentId("");
            } else {
                mtEoComponentVO9.setBomComponentId(dto.getBomComponentId());
            }
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                mtEoComponentVO9.setMaterialId(dto.getMaterialId());
            } else {
                if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
                    if (vo8 != null) {
                        mtEoComponentVO9.setMaterialId(vo8.getMaterialId());
                    }
                } else {
                    throw new MtException("MT_ORDER_0032",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032",
                                                    "ORDER", "bomComponentId、materialId",
                                                    "【API:eoComponentActualUpdate】"));
                }
            }
            if (StringUtils.isNotEmpty(dto.getComponentType())) {
                mtEoComponentVO9.setComponentType(dto.getComponentType());
            } else {
                if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
                    if (vo8 != null) {
                        mtEoComponentVO9.setComponentType(vo8.getBomComponentType());
                    }
                } else {
                    mtEoComponentVO9.setComponentType("ASSEMBLING");
                }
            }
            if (dto.getBomId() == null) {
                String bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
                if (StringUtils.isEmpty(bomId)) {
                    mtEoComponentVO9.setBomId("");
                } else {
                    mtEoComponentVO9.setBomId(bomId);
                }
            } else {
                mtEoComponentVO9.setBomId(dto.getBomId());
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                mtEoComponentVO9.setRouterStepId("");
            } else {
                mtEoComponentVO9.setRouterStepId(dto.getRouterStepId());
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                if (StringUtils.isEmpty(dto.getRouterStepId())) {
                    mtEoComponentVO9.setOperationId("");
                } else {
                    // 如果有输入，根据routerStep找routerOperation作为限定，找不到限定为空
                    MtRouterOperation mro = null;
                    try {
                        mro = mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
                    } catch (MtException e) {
                        LOGGER.debug(e.getMessage());
                    }
                    if (mro == null) {
                        mtEoComponentVO9.setOperationId("");
                    } else {
                        mtEoComponentVO9.setOperationId(mro.getOperationId());
                    }
                }
            } else {
                mtEoComponentVO9.setOperationId(dto.getOperationId());
            }
            list = this.mtEoComponentActualMapper.limitQueryComponentActual(tenantId, mtEoComponentVO9);
        } else {
            MtEoComponentActualVO9 mtEoComponentVO9 = new MtEoComponentActualVO9();
            mtEoComponentVO9.setEoComponentActualId(dto.getEoComponentActualId());
            mtEoComponentVO9.setEoId(dto.getEoId());
            mtEoComponentVO9.setMaterialId(dto.getMaterialId());
            mtEoComponentVO9.setOperationId(dto.getOperationId());
            mtEoComponentVO9.setRouterStepId(dto.getRouterStepId());
            mtEoComponentVO9.setBomId(dto.getBomId());
            mtEoComponentVO9.setComponentType(dto.getComponentType());
            mtEoComponentVO9.setBomComponentId(dto.getBomComponentId());
            // 仅获取一条数据
            list = this.mtEoComponentActualMapper.limitQueryComponentActual(tenantId, mtEoComponentVO9);
            if (CollectionUtils.isEmpty(list)) {
                throw new MtException("MT_ORDER_0101", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0101", "ORDER", "【API:eoComponentActualUpdate】"));
            }
        }

        // 第四步(list有数据)
        List<MtEoComponentActualHis> hisList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (MtEoComponentActual actual : list) {
                MtEoComponentActualHis actualHis = new MtEoComponentActualHis();
                BeanUtils.copyProperties(actual, actualHis);
                if (dto.getActualFirstTime() != null) {
                    actualHis.setActualFirstTime(dto.getActualFirstTime());
                } else {
                    if ((actual.getAssembleQty() == null || new BigDecimal(actual.getAssembleQty().toString())
                                    .compareTo(BigDecimal.ZERO) == 0)
                                    && (dto.getAssembleQty() != null
                                                    && new BigDecimal(dto.getAssembleQty().toString())
                                                                    .compareTo(BigDecimal.ZERO) != 0
                                                    || dto.getTrxAssembleQty() != null && new BigDecimal(
                                                                    dto.getTrxAssembleQty().toString())
                                                                                    .compareTo(BigDecimal.ZERO) != 0)
                                    && actual.getActualFirstTime() == null) {
                        actualHis.setActualFirstTime(new Date(System.currentTimeMillis()));
                    }
                }

                if (dto.getActualLastTime() != null) {
                    actualHis.setActualLastTime(dto.getActualLastTime());
                } else {
                    if (dto.getAssembleQty() != null
                                    && new BigDecimal(dto.getAssembleQty().toString()).compareTo(BigDecimal.ZERO) != 0
                                    || dto.getTrxAssembleQty() != null
                                                    && new BigDecimal(dto.getTrxAssembleQty().toString())
                                                                    .compareTo(BigDecimal.ZERO) != 0) {
                        actualHis.setActualLastTime(new Date(System.currentTimeMillis()));
                    }
                }

                if (dto.getAssembleQty() != null) {
                    actualHis.setAssembleQty(dto.getAssembleQty());
                } else if (dto.getTrxAssembleQty() != null) {
                    actualHis.setAssembleQty(new BigDecimal(actual.getAssembleQty().toString())
                                    .add(new BigDecimal(dto.getTrxAssembleQty().toString())).doubleValue());
                } else if (dto.getTrxAssembleQty() == null && dto.getAssembleQty() == null) {
                    actualHis.setAssembleQty(actual.getAssembleQty());
                }

                if (dto.getScrappedQty() != null) {
                    actualHis.setScrappedQty(dto.getScrappedQty());
                } else if (dto.getTrxScrappedQty() != null) {
                    actualHis.setScrappedQty(new BigDecimal(actual.getScrappedQty().toString())
                                    .add(new BigDecimal(dto.getTrxScrappedQty().toString())).doubleValue());

                } else if (dto.getScrappedQty() == null && dto.getTrxScrappedQty() == null) {
                    actualHis.setScrappedQty(actual.getScrappedQty());
                }

                if (dto.getAssembleExcessFlag() != null) {
                    actualHis.setAssembleExcessFlag(dto.getAssembleExcessFlag());
                }
                if (dto.getAssembleRouterType() != null) {
                    actualHis.setAssembleRouterType(dto.getAssembleRouterType());
                }
                if (dto.getSubstituteFlag() != null) {
                    actualHis.setSubstituteFlag(dto.getSubstituteFlag());
                }

                actualHis.setTrxAssembleQty(new BigDecimal(actualHis.getAssembleQty().toString())
                                .subtract(new BigDecimal(actual.getAssembleQty().toString())).doubleValue());
                actualHis.setTrxScrappedQty(new BigDecimal(actualHis.getScrappedQty().toString())
                                .subtract(new BigDecimal(actual.getScrappedQty().toString())).doubleValue());
                hisList.add(actualHis);
            }
        }

        // Step 5 list无数据
        else {
            MtEoComponentActualHis actualhis = new MtEoComponentActualHis();
            actualhis.setEoId(dto.getEoId());
            actualhis.setBomComponentId(dto.getBomComponentId());
            actualhis.setRouterStepId(dto.getRouterStepId());
            actualhis.setAssembleRouterType(dto.getAssembleRouterType());
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                if (vo8 == null || StringUtils.isEmpty(vo8.getMaterialId())) {
                    throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0148", "ORDER", "【API:eoComponentActualUpdate】"));
                }

                actualhis.setMaterialId(vo8.getMaterialId());
            } else {
                actualhis.setMaterialId(dto.getMaterialId());
            }

            if (StringUtils.isEmpty(dto.getOperationId())) {
                if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
                    MtRouterOperation mtRouterOperation = null;
                    try {
                        mtRouterOperation =
                                        mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
                    } catch (MtException e) {
                        LOGGER.debug(e.getMessage());
                    }
                    if (mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                        throw new MtException("MT_ORDER_0149", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0149", "ORDER", "【API:eoComponentActualUpdate】"));
                    }

                    actualhis.setOperationId(mtRouterOperation.getOperationId());
                }
            } else {
                actualhis.setOperationId(dto.getOperationId());
            }

            if (dto.getAssembleQty() != null) {
                actualhis.setAssembleQty(dto.getAssembleQty());
            } else if (dto.getTrxAssembleQty() != null) {
                actualhis.setAssembleQty(dto.getTrxAssembleQty());
            } else if (dto.getTrxAssembleQty() == null && dto.getAssembleQty() == null) {
                actualhis.setAssembleQty(0.0D);
            }

            if (dto.getScrappedQty() != null) {
                actualhis.setScrappedQty(dto.getScrappedQty());
            } else if (dto.getTrxScrappedQty() != null) {
                actualhis.setScrappedQty(dto.getTrxScrappedQty());
            } else if (dto.getScrappedQty() == null && dto.getTrxScrappedQty() == null) {
                actualhis.setScrappedQty(0.0D);
            }

            if (StringUtils.isEmpty(dto.getComponentType())) {
                if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
                    if (vo8 == null || StringUtils.isEmpty(vo8.getBomComponentType())) {
                        throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0148", "ORDER", "【API:eoComponentActualUpdate】"));
                    }

                    actualhis.setComponentType(vo8.getBomComponentType());
                } else {
                    actualhis.setComponentType("ASSEMBLING");
                }
            } else {
                actualhis.setComponentType(dto.getComponentType());
            }

            if (StringUtils.isEmpty(dto.getBomId())) {
                actualhis.setBomId(mtEoBomRepository.eoBomGet(tenantId, dto.getEoId()));
            } else {
                actualhis.setBomId(dto.getBomId());
            }
            if (StringUtils.isEmpty(dto.getAssembleExcessFlag())) {
                if (StringUtils.isEmpty(dto.getBomComponentId())) {
                    actualhis.setAssembleExcessFlag("Y");
                } else {
                    actualhis.setAssembleExcessFlag("N");
                }
            } else {
                actualhis.setAssembleExcessFlag(dto.getAssembleExcessFlag());
            }
            if (StringUtils.isNotEmpty(dto.getSubstituteFlag())) {
                actualhis.setSubstituteFlag(dto.getSubstituteFlag());
            } else {
                if (StringUtils.isEmpty(dto.getBomComponentId()) || StringUtils.isNotEmpty(dto.getBomComponentId())
                                && StringUtils.isEmpty(dto.getMaterialId())) {
                    actualhis.setSubstituteFlag("N");
                } else if (StringUtils.isNotEmpty(dto.getBomComponentId())
                                && StringUtils.isNotEmpty(dto.getMaterialId())) {
                    if (vo8 == null) {
                        throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0148", "ORDER", "【API:eoComponentActualUpdate】"));
                    }

                    if (dto.getMaterialId().equals(vo8.getMaterialId())) {
                        actualhis.setSubstituteFlag("N");
                    } else {
                        actualhis.setSubstituteFlag("Y");
                    }
                }
            }

            if (dto.getActualFirstTime() != null) {
                actualhis.setActualFirstTime(dto.getActualFirstTime());
            } else {
                if (dto.getAssembleQty() != null
                                && new BigDecimal(dto.getAssembleQty().toString()).compareTo(BigDecimal.ZERO) != 0
                                || dto.getTrxAssembleQty() != null && new BigDecimal(dto.getTrxAssembleQty().toString())
                                                .compareTo(BigDecimal.ZERO) != 0) {
                    actualhis.setActualFirstTime(new Date(System.currentTimeMillis()));
                }
            }

            if (dto.getActualLastTime() != null) {
                actualhis.setActualLastTime(dto.getActualLastTime());
            } else {
                if (dto.getAssembleQty() != null
                                && new BigDecimal(dto.getAssembleQty().toString()).compareTo(BigDecimal.ZERO) != 0
                                || dto.getTrxAssembleQty() != null && new BigDecimal(dto.getTrxAssembleQty().toString())
                                                .compareTo(BigDecimal.ZERO) != 0) {
                    actualhis.setActualLastTime(new Date(System.currentTimeMillis()));
                }
            }
            actualhis.setTrxAssembleQty(actualhis.getAssembleQty());
            actualhis.setTrxScrappedQty(actualhis.getScrappedQty());
            hisList.add(actualhis);
        }
        // 数据更新或新增
        if (CollectionUtils.isNotEmpty(list)) {
            for (MtEoComponentActualHis his : hisList) {
                // cid
                MtEoComponentActual mtEoComponentActual = new MtEoComponentActual();
                BeanUtils.copyProperties(his, mtEoComponentActual);
                mtEoComponentActual.setTenantId(tenantId);
                mtEoComponentActual.setCid(
                                Long.valueOf(this.customDbRepository.getNextKey("mt_eo_component_actual_cid_s")));
                mtEoComponentActual.setLastUpdateDate(now);
                mtEoComponentActual.setLastUpdatedBy(userId);
                sqlList.addAll(customDbRepository.getUpdateSql(mtEoComponentActual));
            }
        } else {
            for (MtEoComponentActualHis his : hisList) {
                // 主键
                MtEoComponentActual mtEoComponentActual = new MtEoComponentActual();
                BeanUtils.copyProperties(his, mtEoComponentActual);
                mtEoComponentActual.setTenantId(tenantId);
                mtEoComponentActual
                                .setEoComponentActualId(this.customDbRepository.getNextKey("mt_eo_component_actual_s"));
                // cid
                mtEoComponentActual.setCid(
                                Long.valueOf(this.customDbRepository.getNextKey("mt_eo_component_actual_cid_s")));
                mtEoComponentActual.setCreatedBy(userId);
                mtEoComponentActual.setCreationDate(now);
                mtEoComponentActual.setLastUpdateDate(now);
                mtEoComponentActual.setLastUpdatedBy(userId);
                mtEoComponentActual.setObjectVersionNumber(1L);

                his.setEoComponentActualId(mtEoComponentActual.getEoComponentActualId());
                sqlList.addAll(customDbRepository.getInsertSql(mtEoComponentActual));
            }
        }
        // 记录历史

        for (MtEoComponentActualHis his : hisList) {
            his.setTenantId(tenantId);
            his.setEventId(dto.getEventId());
            // 主键
            his.setEoComponentActualHisId(this.customDbRepository.getNextKey("mt_eo_component_actual_his_s"));
            // cid
            his.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_eo_component_actual_his_cid_s")));
            his.setCreatedBy(userId);
            his.setCreationDate(now);
            his.setLastUpdateDate(now);
            his.setLastUpdatedBy(userId);
            his.setObjectVersionNumber(1L);
            sqlList.addAll(customDbRepository.getInsertSql(his));
        }
        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
    }

    @Override
    public String eoComponentAssembleLocatorGet(Long tenantId, MtEoComponentActualVO dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoComponentAssembleLocatorGet】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "materialId", "【API:eoComponentAssembleLocatorGet】"));
        }

        // Step 2获取执行作业的装配清单
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoComponentAssembleLocatorGet】"));
        }

        String bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
        String routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
        String siteId = mtEo.getSiteId();
        String productionLineId = mtEo.getProductionLineId();
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

        // Step 6根据生产指令对应生产线和物料获取物料PFEP属性中组件发料库位
        MtPfepInventoryVO queryVO = new MtPfepInventoryVO();
        queryVO.setOrganizationType("PRODUCTIONLINE");
        queryVO.setOrganizationId(productionLineId);
        queryVO.setSiteId(siteId);
        queryVO.setMaterialId(dto.getMaterialId());
        MtPfepInventory mtPfepInventory =
                        mtPfepInventoryRepository.pfepDefaultManufacturingLocationGet(tenantId, queryVO);
        if (mtPfepInventory != null && StringUtils.isNotEmpty(mtPfepInventory.getIssuedLocatorId())) {
            return mtPfepInventory.getIssuedLocatorId();
        }

        // Step 7
        MtModProdLineManufacturing mtModProdLineManufacturing = mtModProdLineManufacturingRepository
                        .prodLineManufacturingPropertyGet(tenantId, mtEo.getProductionLineId());
        if (mtModProdLineManufacturing == null) {
            return null;
        }
        return mtModProdLineManufacturing.getIssuedLocatorId();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoComponentScrap(Long tenantId, MtEoComponentActualVO4 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoComponentScrap】"));
        }
        if (dto.getTrxScrappedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxScrappedQty", "【API:eoComponentScrap】"));
        }
        if (new BigDecimal(dto.getTrxScrappedQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxScrappedQty", "【API:eoComponentScrap】"));
        }

        MtEoComponentActualVO9 vo9 = new MtEoComponentActualVO9();
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        MtEoComponentActualHis actualHis = new MtEoComponentActualHis();
        List<String> stringList;

        // Step 2
        String bomId = null;
        if (null == dto.getBomId()) {
            bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
        } else {
            // a)获取bomId为输入参数，输入为空字符串获取结果也为空字符串
            bomId = dto.getBomId();
        }
        // Step 3
        String flagGet = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());
        if ((StringUtils.isEmpty(flagGet) || "N".equals(flagGet)) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 3-a
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ORDER_0119", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0119", "ORDER", "【API:eoComponentScrap】"));
            }
            MtBomComponentVO8 bomComponentVO8 =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponentVO8 == null || !"ASSEMBLING".equals(bomComponentVO8.getBomComponentType())) {
                throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:eoComponentScrap】"));
            }
            // 3-a-ii获取执行作业组件装配实绩
            vo9.setBomComponentId(dto.getBomComponentId());
            vo9.setOperationId("");
            vo9.setRouterStepId("");
            vo9.setAssembleExcessFlag("N");
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                vo9.setMaterialId(dto.getMaterialId());
            } else {
                if (StringUtils.isNotEmpty(bomComponentVO8.getMaterialId())) {
                    vo9.setMaterialId(bomComponentVO8.getMaterialId());
                } else {
                    throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0148", "ORDER", "【API:eoComponentScrap】"));

                }
            }
            // 获取事件ID(新增事件)
            eventCreateVO.setEventTypeCode("EO_COMPONENT_SCRAP");

        } else if ((StringUtils.isEmpty(flagGet) || "N".equals(flagGet)) && "Y".equals(dto.getAssembleExcessFlag())) {
            // 3-b
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ORDER_0120", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0120", "ORDER", "【API:eoComponentScrap】"));
            }
            // 3-b-ii获取执行作业组件装配实绩
            vo9.setAssembleExcessFlag("Y");
            vo9.setMaterialId(dto.getMaterialId());
            vo9.setBomComponentId("");
            vo9.setOperationId("");
            vo9.setRouterStepId("");
            // 获取事件ID(新增事件)
            eventCreateVO.setEventTypeCode("EO_COMPONENT_EXCESS_SCRAP");

        } else if ("Y".equals(flagGet) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 3-c
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ORDER_0119", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0119", "ORDER", "【API:eoComponentScrap】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ORDER_0121", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0121", "ORDER", "【API:eoComponentScrap】"));
            }
            MtBomComponentVO8 bomComponentVO8 =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponentVO8 == null || !"ASSEMBLING".equals(bomComponentVO8.getBomComponentType())) {
                throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:eoComponentScrap】"));
            }
            // 获取执行作业组件装配实绩
            vo9.setBomComponentId(dto.getBomComponentId());
            vo9.setRouterStepId(dto.getRouterStepId());
            vo9.setAssembleExcessFlag("N");
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                vo9.setMaterialId(dto.getMaterialId());
            } else {
                if (StringUtils.isNotEmpty(bomComponentVO8.getMaterialId())) {
                    vo9.setMaterialId(bomComponentVO8.getMaterialId());
                } else {
                    throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0148", "ORDER", "【API:eoComponentScrap】"));
                }
            }
            if (StringUtils.isNotEmpty(dto.getOperationId())) {
                vo9.setOperationId(dto.getOperationId());
            } else {
                MtRouterOperation mtRouterOperation = null;
                try {
                    mtRouterOperation = mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
                } catch (MtException e) {
                    LOGGER.debug(e.getMessage());
                }
                if (mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                    throw new MtException("MT_ORDER_0149", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0149", "ORDER", "【API:eoComponentScrap】"));
                }
                vo9.setOperationId(mtRouterOperation.getOperationId());
            }

            // 获取事件ID(新增事件)
            eventCreateVO.setEventTypeCode("EO_COMPONENT_OPERATION_SCRAP");
        } else if ("Y".equals(flagGet) && "Y".equals(dto.getAssembleExcessFlag())) {
            // 3-d
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ORDER_0120", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0120", "ORDER", "【API:eoComponentScrap】"));
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("MT_ORDER_0122", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0122", "ORDER", "【API:eoComponentScrap】"));
            }
            // update by peng.yuan 2020/2/25
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ORDER_0121", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0121", "ORDER", "【API:eoComponentScrap】"));
            }

            vo9.setOperationId(dto.getOperationId());
            vo9.setAssembleExcessFlag("Y");
            vo9.setMaterialId(dto.getMaterialId());
            vo9.setBomComponentId("");
            vo9.setRouterStepId(dto.getRouterStepId());
            eventCreateVO.setEventTypeCode("EO_COMPONENT_EXCESS_OPERATION_SCRAP");
        }
        // 获取执行作业组件装配实绩
        vo9.setEoId(dto.getEoId());
        vo9.setComponentType("ASSEMBLING");
        vo9.setBomId(bomId);
        // 根据限定条件仅查询到一条数据
        stringList = propertyLimitEoComponentAssembleActualQuery(tenantId, vo9);
        if (CollectionUtils.isEmpty(stringList)) {
            throw new MtException("MT_ORDER_0114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0114", "ORDER", "【API:eoComponentScrap】"));
        }
        // 获取事件ID(新增事件)

        for (String t : stringList) {
            eventCreateVO.setWorkcellId(dto.getWorkcellId());
            eventCreateVO.setLocatorId(dto.getLocatorId());
            eventCreateVO.setParentEventId(dto.getParentEventId());
            eventCreateVO.setEventRequestId(dto.getEventRequestId());
            eventCreateVO.setShiftDate(dto.getShiftDate());
            eventCreateVO.setShiftCode(dto.getShiftCode());
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            // 更新执行作业装配实绩并记录历史
            actualHis.setEoComponentActualId(t);
            actualHis.setTrxScrappedQty(dto.getTrxScrappedQty());
            actualHis.setEventId(eventId);
            eoComponentActualUpdate(tenantId, actualHis);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoComponentScrapCancel(Long tenantId, MtEoComponentActualVO4 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoComponentScrapCancel】"));
        }
        if (dto.getTrxScrappedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxScrappedQty", "【API:eoComponentScrapCancel】"));
        }
        if (new BigDecimal(dto.getTrxScrappedQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxScrappedQty", "【API:eoComponentScrapCancel】"));
        }

        MtEoComponentActualVO9 vo9 = new MtEoComponentActualVO9();
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        MtEoComponentActualHis actualHis = new MtEoComponentActualHis();
        List<String> stringList;

        // Step 2
        String bomId;
        if (null == dto.getBomId()) {
            bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
        } else {
            bomId = dto.getBomId();
        }
        // Step 3
        String flagGet = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());
        if ((StringUtils.isEmpty(flagGet) || "N".equals(flagGet)) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 3-a
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ORDER_0123", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0123", "ORDER", "【API:eoComponentScrapCancel】"));
            }
            MtBomComponentVO8 bomComponentVO8 =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponentVO8 == null || !"ASSEMBLING".equals(bomComponentVO8.getBomComponentType())) {
                throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:eoComponentScrapCancel】"));
            }
            // 3-a-ii获取执行作业组件装配实绩
            vo9.setBomComponentId(dto.getBomComponentId());
            vo9.setAssembleExcessFlag("N");
            vo9.setOperationId("");
            vo9.setRouterStepId("");
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                vo9.setMaterialId(dto.getMaterialId());
            } else {
                if (StringUtils.isNotEmpty(bomComponentVO8.getMaterialId())) {
                    vo9.setMaterialId(bomComponentVO8.getMaterialId());
                } else {
                    throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0148", "ORDER", "【API:eoComponentScrapCancel】"));

                }
            }
            // 获取事件ID(新增事件)
            eventCreateVO.setEventTypeCode("EO_COMPONENT_SCRAP_CANCEL");
        } else if ((StringUtils.isEmpty(flagGet) || "N".equals(flagGet)) && "Y".equals(dto.getAssembleExcessFlag())) {
            // 3-b
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ORDER_0124", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0124", "ORDER", "【API:eoComponentScrapCancel】"));
            }
            // 3-b-ii获取执行作业组件装配实绩
            vo9.setAssembleExcessFlag("Y");
            vo9.setMaterialId(dto.getMaterialId());
            vo9.setBomComponentId("");
            vo9.setOperationId("");
            vo9.setRouterStepId("");
            // 获取事件ID(新增事件)
            eventCreateVO.setEventTypeCode("EO_COMPONENT_EXCESS_SCRAP_CANCEL");

        } else if ("Y".equals(flagGet) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 3-c
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ORDER_0123", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0123", "ORDER", "【API:eoComponentScrapCancel】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ORDER_0125", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0125", "ORDER", "【API:eoComponentScrapCancel】"));
            }
            MtBomComponentVO8 bomComponentVO8 =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponentVO8 == null || !"ASSEMBLING".equals(bomComponentVO8.getBomComponentType())) {
                throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:eoComponentScrapCancel】"));
            }
            // 获取执行作业组件装配实绩
            vo9.setBomComponentId(dto.getBomComponentId());
            vo9.setRouterStepId(dto.getRouterStepId());
            vo9.setAssembleExcessFlag("N");
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                vo9.setMaterialId(dto.getMaterialId());
            } else {
                if (StringUtils.isNotEmpty(bomComponentVO8.getMaterialId())) {
                    vo9.setMaterialId(bomComponentVO8.getMaterialId());
                } else {
                    throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0148", "ORDER", "【API:eoComponentScrapCancel】"));
                }
            }
            if (StringUtils.isNotEmpty(dto.getOperationId())) {
                vo9.setOperationId(dto.getOperationId());
            } else {
                MtRouterOperation mtRouterOperation = null;
                try {
                    mtRouterOperation = mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
                } catch (MtException e) {
                    LOGGER.debug(e.getMessage());
                }
                if (mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                    throw new MtException("MT_ORDER_0149", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0149", "ORDER", "【API:eoComponentScrapCancel】"));
                }
                vo9.setOperationId(mtRouterOperation.getOperationId());
            }

            // 获取事件ID(新增事件)
            eventCreateVO.setEventTypeCode("EO_COMPONENT_OPERATION_SCRAP_CANCEL");
        } else if ("Y".equals(flagGet) && "Y".equals(dto.getAssembleExcessFlag())) {
            // 3-d
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ORDER_0124", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0124", "ORDER", "【API:eoComponentScrapCancel】"));
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("MT_ORDER_0126", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0126", "ORDER", "【API:eoComponentScrapCancel】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ORDER_0125", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0125", "ORDER", "【API:eoComponentScrapCancel】"));
            }
            vo9.setOperationId(dto.getOperationId());
            vo9.setAssembleExcessFlag("Y");
            vo9.setMaterialId(dto.getMaterialId());
            vo9.setBomComponentId("");
            vo9.setRouterStepId(dto.getRouterStepId());
            eventCreateVO.setEventTypeCode("EO_COMPONENT_EXCESS_OPERATION_SCRAP_CANCEL");
        }
        // 获取执行作业组件装配实绩
        vo9.setEoId(dto.getEoId());
        vo9.setComponentType("ASSEMBLING");
        vo9.setBomId(bomId);

        // 根据限定条件仅查询到一条数据
        stringList = propertyLimitEoComponentAssembleActualQuery(tenantId, vo9);
        if (CollectionUtils.isEmpty(stringList)) {
            throw new MtException("MT_ORDER_0114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0114", "ORDER", "【API:eoComponentScrapCancel】"));
        }
        // 获取事件ID(新增事件),只会找到一条数据。所以一个历史一个更新
        for (String t : stringList) {
            eventCreateVO.setWorkcellId(dto.getWorkcellId());
            eventCreateVO.setLocatorId(dto.getLocatorId());
            eventCreateVO.setParentEventId(dto.getParentEventId());
            eventCreateVO.setEventRequestId(dto.getEventRequestId());
            eventCreateVO.setShiftDate(dto.getShiftDate());
            eventCreateVO.setShiftCode(dto.getShiftCode());
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            // 更新执行作业装配实绩并记录历史
            actualHis.setEoComponentActualId(t);
            actualHis.setTrxScrappedQty(-dto.getTrxScrappedQty());
            actualHis.setEventId(eventId);
            eoComponentActualUpdate(tenantId, actualHis);
            // Step 4
            MtEoComponentActual actual = new MtEoComponentActual();
            actual.setTenantId(tenantId);
            actual.setEoComponentActualId(t);
            actual = this.mtEoComponentActualMapper.selectOne(actual);
            if (new BigDecimal(actual.getScrappedQty().toString()).compareTo(BigDecimal.ZERO) < 0) {
                throw new MtException("MT_ORDER_0127", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0127", "ORDER", "【API:eoComponentScrapCancel】"));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoComponentRemove(Long tenantId, MtEoComponentActualVO5 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoComponentRemove】"));
        }
        if (dto.getTrxAssembleQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxAssembleQty", "【API:eoComponentRemove】"));
        }
        if (new BigDecimal(dto.getTrxAssembleQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxAssembleQty", "【API:eoComponentRemove】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        MtEoComponentActualHis actualHis = new MtEoComponentActualHis();

        // Step 2
        String flagGet = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());
        if ((StringUtils.isEmpty(flagGet) || "N".equals(flagGet)) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 2-a
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ORDER_0109", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0109", "ORDER", "【API:eoComponentRemove】"));
            }
            MtBomComponentVO8 bomComponentVO8 =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponentVO8 == null || !"REMOVE".equals(bomComponentVO8.getBomComponentType())) {
                throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105", "ORDER", "REMOVE", "【API:eoComponentRemove】"));
            }
            eventCreateVO.setEventTypeCode("EO_COMPONENT_REMOVE");
            actualHis.setBomComponentId(dto.getBomComponentId());
            actualHis.setTrxAssembleQty(dto.getTrxAssembleQty());
            actualHis.setOperationId("");
            actualHis.setRouterStepId("");
            actualHis.setAssembleExcessFlag("N");
        } else if ((StringUtils.isEmpty(flagGet) || "N".equals(flagGet)) && "Y".equals(dto.getAssembleExcessFlag())) {
            // 2-b
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ORDER_0110", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0110", "ORDER", "【API:eoComponentRemove】"));
            }
            eventCreateVO.setEventTypeCode("EO_COMPONENT_EXCESS_REMOVE");
            actualHis.setBomComponentId("");
            actualHis.setOperationId("");
            actualHis.setRouterStepId("");
            actualHis.setTrxAssembleQty(dto.getTrxAssembleQty());
            actualHis.setAssembleExcessFlag("Y");
        } else if ("Y".equals(flagGet) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 2-c
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ORDER_0109", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0109", "ORDER", "【API:eoComponentRemove】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ORDER_0111", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0111", "ORDER", "【API:eoComponentRemove】"));
            }
            MtBomComponentVO8 bomComponentVO8 =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponentVO8 == null || !"REMOVE".equals(bomComponentVO8.getBomComponentType())) {
                throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105", "ORDER", "REMOVE", "【API:eoComponentRemove】"));
            }

            eventCreateVO.setEventTypeCode("EO_COMPONENT_OPERATION_REMOVE");
            actualHis.setBomComponentId(dto.getBomComponentId());
            actualHis.setRouterStepId(dto.getRouterStepId());
            actualHis.setTrxAssembleQty(dto.getTrxAssembleQty());
            actualHis.setAssembleExcessFlag("N");

        } else if ("Y".equals(flagGet) && "Y".equals(dto.getAssembleExcessFlag())) {
            // 2-d
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ORDER_0110", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0110", "ORDER", "【API:eoComponentRemove】"));
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("MT_ORDER_0112", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0112", "ORDER", "【API:eoComponentRemove】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ORDER_0111", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0111", "ORDER", "【API:eoComponentRemove】"));
            }
            eventCreateVO.setEventTypeCode("EO_COMPONENT_EXCESS_OPERATION_REMOVE");
            actualHis.setOperationId(dto.getOperationId());
            actualHis.setTrxAssembleQty(dto.getTrxAssembleQty());
            actualHis.setAssembleExcessFlag("Y");
            actualHis.setBomComponentId("");
            actualHis.setRouterStepId(dto.getRouterStepId());
        }

        // 获取事件ID(新增事件)
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 更新执行作业装配移除实绩并记录历史
        actualHis.setEoId(dto.getEoId());
        actualHis.setMaterialId(dto.getMaterialId());
        actualHis.setComponentType("REMOVE");
        actualHis.setAssembleRouterType(dto.getAssembleRouterType());
        actualHis.setEventId(eventId);
        eoComponentActualUpdate(tenantId, actualHis);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoComponentAssemble(Long tenantId, MtEoComponentActualVO5 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoComponentAssemble】"));
        }
        if (dto.getTrxAssembleQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxAssembleQty", "【API:eoComponentAssemble】"));
        }
        if (new BigDecimal(dto.getTrxAssembleQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxAssembleQty", "【API:eoComponentAssemble】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        MtEoComponentActualHis actualHis = new MtEoComponentActualHis();

        // Step 2
        String flagGet = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());
        if ((StringUtils.isEmpty(flagGet) || "N".equals(flagGet)) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ORDER_0104", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0104", "ORDER", "【API:eoComponentAssemble】"));
            }
            MtBomComponentVO8 bomComponentVO8 =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponentVO8 == null || !"ASSEMBLING".equals(bomComponentVO8.getBomComponentType())) {
                throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:eoComponentAssemble】"));
            }
            eventCreateVO.setEventTypeCode("EO_COMPONENT_ASSEMBLE");
            actualHis.setBomComponentId(dto.getBomComponentId());
            actualHis.setTrxAssembleQty(dto.getTrxAssembleQty());
            actualHis.setAssembleExcessFlag("N");
            actualHis.setOperationId("");
            actualHis.setRouterStepId("");
        } else if ((StringUtils.isEmpty(flagGet) || "N".equals(flagGet)) && "Y".equals(dto.getAssembleExcessFlag())) {
            // 2-b
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ORDER_0106", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0106", "ORDER", "【API:eoComponentAssemble】"));
            }
            eventCreateVO.setEventTypeCode("EO_COMPONENT_EXCESS_ASSEMBLE");
            actualHis.setTrxAssembleQty(dto.getTrxAssembleQty());
            actualHis.setAssembleExcessFlag("Y");
            actualHis.setBomComponentId("");
            actualHis.setOperationId("");
            actualHis.setRouterStepId("");
        } else if ("Y".equals(flagGet) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 2-c
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ORDER_0104", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0104", "ORDER", "【API:eoComponentAssemble】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ORDER_0107", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0107", "ORDER", "【API:eoComponentAssemble】"));
            }
            MtBomComponentVO8 bomComponentVO8 =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponentVO8 == null || !"ASSEMBLING".equals(bomComponentVO8.getBomComponentType())) {
                throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:eoComponentAssemble】"));
            }

            eventCreateVO.setEventTypeCode("EO_COMPONENT_OPERATION_ASSEMBLE");
            actualHis.setBomComponentId(dto.getBomComponentId());
            actualHis.setRouterStepId(dto.getRouterStepId());
            actualHis.setTrxAssembleQty(dto.getTrxAssembleQty());
            actualHis.setAssembleExcessFlag("N");

        } else if ("Y".equals(flagGet) && "Y".equals(dto.getAssembleExcessFlag())) {
            // 2-d
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ORDER_0106", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0106", "ORDER", "【API:eoComponentAssemble】"));
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("MT_ORDER_0108", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0108", "ORDER", "【API:eoComponentAssemble】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ORDER_0107", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0107", "ORDER", "【API:eoComponentAssemble】"));
            }
            eventCreateVO.setEventTypeCode("EO_COMPONENT_EXCESS_OPERATION_ASSEMBLE");
            actualHis.setOperationId(dto.getOperationId());
            actualHis.setTrxAssembleQty(dto.getTrxAssembleQty());
            actualHis.setAssembleExcessFlag("Y");
            actualHis.setBomComponentId("");
            actualHis.setRouterStepId(dto.getRouterStepId());
        }

        // 获取事件ID(新增事件)
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        // 更新执行作业装配移除实绩并记录历史
        actualHis.setEoId(dto.getEoId());
        actualHis.setMaterialId(dto.getMaterialId());
        actualHis.setComponentType("ASSEMBLING");
        actualHis.setAssembleRouterType(dto.getAssembleRouterType());
        actualHis.setEventId(eventId);
        eoComponentActualUpdate(tenantId, actualHis);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoComponentAssembleCancel(Long tenantId, MtEoComponentActualVO11 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoComponentAssembleCancel】"));
        }
        if (dto.getTrxAssembleQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxAssembleQty", "【API:eoComponentAssembleCancel】"));
        }
        if (new BigDecimal(dto.getTrxAssembleQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxAssembleQty", "【API:eoComponentAssembleCancel】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        MtEoComponentActualHis actualHis = new MtEoComponentActualHis();
        MtEoComponentActualVO9 vo9 = new MtEoComponentActualVO9();
        List<String> stringList;

        // Step 2
        String bomId;
        if (dto.getBomId() == null) {
            bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
        } else {
            bomId = dto.getBomId();
        }
        // Step 3
        String flagGet = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());
        if ((StringUtils.isEmpty(flagGet) || "N".equals(flagGet)) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 3-a
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ORDER_0113", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0113", "ORDER", "【API:eoComponentAssembleCancel】"));
            }
            MtBomComponentVO8 vo8 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (vo8 == null || !"ASSEMBLING".equals(vo8.getBomComponentType())) {
                throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:eoComponentAssembleCancel】"));
            }
            // 3-a-ii
            vo9.setBomComponentId(dto.getBomComponentId());
            vo9.setAssembleExcessFlag("N");
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                vo9.setMaterialId(dto.getMaterialId());
            } else {
                if (StringUtils.isNotEmpty(vo8.getMaterialId())) {
                    vo9.setMaterialId(vo8.getMaterialId());
                } else {
                    throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0148", "ORDER", "【API:eoComponentAssembleCancel】"));

                }
            }
            // 3-a-iii
            eventCreateVO.setEventTypeCode("EO_COMPONENT_ASSEMBLE_CANCEL");
            vo9.setOperationId("");
            vo9.setRouterStepId("");
        } else if ((StringUtils.isEmpty(flagGet) || "N".equals(flagGet)) && "Y".equals(dto.getAssembleExcessFlag())) {
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ORDER_0115", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0115", "ORDER", "【API:eoComponentAssembleCancel】"));
            }
            // 3-b-ii
            vo9.setAssembleExcessFlag("Y");
            vo9.setMaterialId(dto.getMaterialId());
            // 3-b-iii
            eventCreateVO.setEventTypeCode("EO_COMPONENT_EXCESS_ASSEMBLE_CANCEL");
            vo9.setBomComponentId("");
            vo9.setOperationId("");
            vo9.setRouterStepId("");
        } else if ("Y".equals(flagGet) && (StringUtils.isEmpty(dto.getAssembleExcessFlag())
                        || "N".equals(dto.getAssembleExcessFlag()))) {
            // 3-c
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_ORDER_0113", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0113", "ORDER", "【API:eoComponentAssembleCancel】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ORDER_0116", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0116", "ORDER", "【API:eoComponentAssembleCancel】"));
            }
            MtBomComponentVO8 vo8 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (vo8 == null || !"ASSEMBLING".equals(vo8.getBomComponentType())) {
                throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105", "ORDER", "ASSEMBLING", "【API:eoComponentAssembleCancel】"));
            }
            // 3-c-ii
            vo9.setBomComponentId(dto.getBomComponentId());
            vo9.setRouterStepId(dto.getRouterStepId());
            vo9.setAssembleExcessFlag("N");
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                vo9.setMaterialId(dto.getMaterialId());
            } else {
                if (StringUtils.isNotEmpty(vo8.getMaterialId())) {
                    vo9.setMaterialId(vo8.getMaterialId());
                } else {
                    throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0148", "ORDER", "【API:eoComponentAssembleCancel】"));

                }
            }
            if (StringUtils.isNotEmpty(dto.getOperationId())) {
                vo9.setOperationId(dto.getOperationId());
            } else {
                MtRouterOperation mtRouterOperation = null;
                try {
                    mtRouterOperation = mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
                } catch (MtException e) {
                    LOGGER.debug(e.getMessage());
                }
                if (mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                    throw new MtException("MT_ORDER_0149", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0149", "ORDER", "【API:eoComponentAssembleCancel】"));
                }
                vo9.setOperationId(mtRouterOperation.getOperationId());
            }
            // 3-c-iii
            eventCreateVO.setEventTypeCode("EO_COMPONENT_OPERATION_ASSEMBLE_CANCEL");
        } else if ("Y".equals(flagGet) && "Y".equals(dto.getAssembleExcessFlag())) {
            // 3-d
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ORDER_0115", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0115", "ORDER", "【API:eoComponentAssembleCancel】"));
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("MT_ORDER_0117", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0117", "ORDER", "【API:eoComponentAssembleCancel】"));
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ORDER_0116", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0116", "ORDER", "【API:eoComponentAssembleCancel】"));
            }
            // 3-d-ii
            vo9.setOperationId(dto.getOperationId());
            vo9.setAssembleExcessFlag("Y");
            vo9.setMaterialId(dto.getMaterialId());
            vo9.setBomComponentId("");
            vo9.setRouterStepId(dto.getRouterStepId());
            // 3-d-iii
            eventCreateVO.setEventTypeCode("EO_COMPONENT_EXCESS_OPERATION_ASSEMBLE_CANCEL");
        }
        // 获取执行作业组件装配实绩
        vo9.setEoId(dto.getEoId());
        vo9.setComponentType("ASSEMBLING");
        vo9.setBomId(bomId);

        // 根据限定条件仅查询到一条数据
        stringList = propertyLimitEoComponentAssembleActualQuery(tenantId, vo9);
        if (CollectionUtils.isEmpty(stringList)) {
            throw new MtException("MT_ORDER_0114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0114", "ORDER", "【API:eoComponentAssembleCancel】"));
        }
        // 获取事件ID(新增事件)
        for (String t : stringList) {
            eventCreateVO.setWorkcellId(dto.getWorkcellId());
            eventCreateVO.setLocatorId(dto.getLocatorId());
            eventCreateVO.setParentEventId(dto.getParentEventId());
            eventCreateVO.setEventRequestId(dto.getEventRequestId());
            eventCreateVO.setShiftDate(dto.getShiftDate());
            eventCreateVO.setShiftCode(dto.getShiftCode());
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            // 更新执行作业装配移除实绩并记录历史
            actualHis.setEoComponentActualId(t);
            actualHis.setTrxAssembleQty(-dto.getTrxAssembleQty());
            actualHis.setEventId(eventId);
            eoComponentActualUpdate(tenantId, actualHis);

            // Step 4
            MtEoComponentActual actual = new MtEoComponentActual();
            actual.setTenantId(tenantId);
            actual.setEoComponentActualId(t);
            actual = this.mtEoComponentActualMapper.selectOne(actual);
            if (new BigDecimal(actual.getAssembleQty().toString()).compareTo(BigDecimal.ZERO) == -1) {
                throw new MtException("MT_ORDER_0118", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0118", "ORDER", "【API:eoComponentAssembleCancel】"));
            }
        }
    }

    @Override
    public void eoComponentRemoveVerify(Long tenantId, MtEoComponentActualVO7 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoComponentRemoveVerify】"));
        }
        if (dto.getTrxAssembleQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxAssembleQty", "【API:eoComponentRemoveVerify】"));
        }
        if (new BigDecimal(dto.getTrxAssembleQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxAssembleQty", "【API:eoComponentRemoveVerify】"));
        }
        if (StringUtils.isEmpty(dto.getBomComponentId()) && StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0032", "ORDER", "materialId、bomComponentId", "【API:eoComponentRemoveVerify】"));
        }
        if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
            MtBomComponentVO8 vo8 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (vo8 == null || !"REMOVE".equals(vo8.getBomComponentType())) {
                throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0105", "ORDER", "REMOVE", "【API:eoComponentRemoveVerify】"));
            }
        }

        // Step 2获取物料
        MtEoComponentActualVO9 mtEoComponentVO9 = new MtEoComponentActualVO9();
        String materialId = null;
        if (StringUtils.isNotEmpty(dto.getMaterialId())) {
            materialId = dto.getMaterialId();
        } else {
            MtBomComponentVO8 vo8 = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (vo8 != null) {
                materialId = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId())
                                .getMaterialId();
            }
        }

        // Step 3 获取对应物料净装配数量：装配数量 – 移除数量
        Double assembleQty = 0.0D;
        mtEoComponentVO9.setMaterialId(materialId);
        mtEoComponentVO9.setEoId(dto.getEoId());
        List<MtEoComponentActual> actualList = materialLimitEoComponentAssembleActualQuery(tenantId, mtEoComponentVO9);
        if (CollectionUtils.isNotEmpty(actualList)) {
            for (MtEoComponentActual t : actualList) {
                if ("ASSEMBLING".equals(t.getComponentType())) {
                    assembleQty = new BigDecimal(assembleQty.toString())
                                    .add(new BigDecimal(t.getAssembleQty().toString())).doubleValue();
                } else if ("REMOVE".equals(t.getComponentType())) {

                    assembleQty = new BigDecimal(assembleQty.toString())
                                    .subtract(new BigDecimal(t.getAssembleQty().toString())).doubleValue();
                }
            }
        }
        // Step 4
        if (new BigDecimal(dto.getTrxAssembleQty().toString()).compareTo(new BigDecimal(assembleQty.toString())) == 1) {
            throw new MtException("MT_ORDER_0128", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0128", "ORDER", "【API:eoComponentRemoveVerify】"));
        }

        // Step 5判断是否为强制装配移除
        if ("Y".equals(dto.getAssembleExcessFlag())) {
            return;
        }

        // Step 6
        if (StringUtils.isEmpty(dto.getBomComponentId())) {
            throw new MtException("MT_ORDER_0109", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0109", "ORDER", "bomComponentId", "【API:eoComponentRemoveVerify】"));
        }
        MtEoComponentActualVO12 mtEoComponentVO12 = new MtEoComponentActualVO12();
        mtEoComponentVO12.setEoId(dto.getEoId());
        mtEoComponentVO12.setRouterStepId(dto.getRouterStepId());
        List<MtEoComponentActualVO13> vo13List = eoUnassembledComponentQuery(tenantId, mtEoComponentVO12);
        Double sumUnAssembleQty = 0.0D;
        for (MtEoComponentActualVO13 t : vo13List) {
            if (dto.getBomComponentId().equals(t.getBomComponentId()) && materialId.equals(t.getMaterialId())) {
                sumUnAssembleQty = new BigDecimal(sumUnAssembleQty.toString())
                                .add(new BigDecimal(t.getUnassembledQty().toString())).doubleValue();
            }
        }
        // 验证
        if (new BigDecimal(dto.getTrxAssembleQty().toString())
                        .compareTo(new BigDecimal(sumUnAssembleQty.toString())) == 1) {
            throw new MtException("MT_ORDER_0129", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0129", "ORDER", "【API:eoComponentRemoveVerify】"));
        }

    }

    @Override
    public void eoComponentIsAssembledValidate(Long tenantId, MtEoComponentActualVO6 dto) {
        // Step 1判断输入参数合规
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoComponentIsAssembledValidate】"));
        }
        if (StringUtils.isEmpty(dto.getBomComponentId()) && StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ORDER_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032", "ORDER",
                                            "bomComponentId、materialId", "【API:eoComponentIsAssembledValidate】"));
        }
        if (StringUtils.isEmpty(dto.getBomComponentId()) && StringUtils.isEmpty(dto.getComponentType())) {
            throw new MtException("MT_ORDER_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032", "ORDER",
                                            "bomComponentId、componentType", "【API:eoComponentIsAssembledValidate】"));
        }

        MtEoComponentActual actual = new MtEoComponentActual();
        actual.setTenantId(tenantId);
        // Step 2获取装配清单id
        if (StringUtils.isNotEmpty(dto.getBomId())) {
            actual.setBomId(dto.getBomId());
        } else {
            String bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
            actual.setBomId(bomId);
        }
        actual.setEoId(dto.getEoId());
        actual.setBomComponentId(dto.getBomComponentId());
        actual.setRouterStepId(dto.getRouterStepId());
        actual.setMaterialId(dto.getMaterialId());
        actual.setComponentType(dto.getComponentType());
        actual.setOperationId(dto.getOperationId());
        List<MtEoComponentActual> list = mtEoComponentActualMapper.select(actual);
        if (CollectionUtils.isEmpty(list)) {
            throw new MtException("MT_ORDER_0103", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0103", "ORDER", "【API:eoComponentIsAssembledValidate】"));
        }
        StringBuilder str = new StringBuilder();
        if (list.size() > 1) {
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                str.append("routerStepId、");
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                str.append("operationId、");
            }
            if (StringUtils.isNotEmpty(str)) {
                int a = str.lastIndexOf("、");
                str.deleteCharAt(a);
            }
            if (StringUtils.isEmpty(dto.getRouterStepId()) || StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0032", "ORDER", str.toString(), "【API:eoComponentIsAssembledValidate】"));
            }
        } else {
            MtEoComponentActual actual1 = list.get(0);
            if (StringUtils.isNotEmpty(actual1.getRouterStepId()) && StringUtils.isEmpty(dto.getRouterStepId())) {
                throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0032", "ORDER", "routerStepId", "【API:eoComponentIsAssembledValidate】"));
            }
            if (StringUtils.isNotEmpty(actual1.getOperationId()) && StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0032", "ORDER", "operationId", "【API:eoComponentIsAssembledValidate】"));
            }
        }
    }

    @Override
    public List<MtEoComponentActualVO8> eoComponentSubstituteQuery(Long tenantId, MtEoComponentActualVO2 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoComponentSubstituteQuery】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "materialId", "【API:eoComponentSubstituteQuery】"));
        }

        // Step 2
        String bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
        if (StringUtils.isEmpty(bomId)) {
            return Collections.emptyList();
        }

        // Step 3
        MtBomComponentVO bomComponentVO = new MtBomComponentVO();
        bomComponentVO.setBomId(bomId);
        bomComponentVO.setMaterialId(dto.getMaterialId());
        if (StringUtils.isEmpty(dto.getComponentType())) {
            bomComponentVO.setBomComponentType("ASSEMBLING");
        } else {
            bomComponentVO.setBomComponentType(dto.getComponentType());
        }
        bomComponentVO.setOnlyAvailableFlag("Y");
        List<MtBomComponentVO16> listMap =
                        mtBomComponentRepository.propertyLimitBomComponentQuery(tenantId, bomComponentVO);
        if (CollectionUtils.isEmpty(listMap)) {
            return Collections.emptyList();
        }

        // Step 4
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

        // Step 5
        List<MtEoComponentActualVO8> vo8List = new ArrayList<MtEoComponentActualVO8>();
        for (Entry<String, List<MtBomSubstituteGroupVO3>> entry : bomSubstituteGroupMap.entrySet()) {
            String bomComponentId = entry.getKey();
            List<MtBomSubstituteGroupVO3> bomSubstituteGroupList = entry.getValue();

            MtEoComponentActualVO8 vo8 = null;
            for (MtBomSubstituteGroupVO3 vo3 : bomSubstituteGroupList) {
                vo8 = new MtEoComponentActualVO8();
                MtBomSubstitute mtBomSubstitute =
                                mtBomSubstituteRepository.bomSubstituteBasicGet(tenantId, vo3.getBomSubstituteId());
                MtBomSubstituteGroup mtBomSubstituteGroup = mtBomSubstituteGroupRepository
                                .bomSubstituteGroupBasicGet(tenantId, vo3.getBomSubstituteGroupId());

                vo8.setBomComponentId(bomComponentId);
                vo8.setBomSubstituteGroupId(vo3.getBomSubstituteGroupId());
                vo8.setBomSubstituteId(vo3.getBomSubstituteId());
                if (mtBomSubstitute != null) {
                    vo8.setMaterialId(mtBomSubstitute.getMaterialId());
                    vo8.setSubstituteValue(mtBomSubstitute.getSubstituteValue());
                    vo8.setSubstituteUsage(mtBomSubstitute.getSubstituteUsage());
                }
                if (mtBomSubstituteGroup != null) {
                    vo8.setSubstitutePolicy(mtBomSubstituteGroup.getSubstitutePolicy());
                }
                vo8List.add(vo8);
            }
        }

        return vo8List.stream()
                        .sorted(Comparator.comparing((MtEoComponentActualVO8 c) -> Double.valueOf(
                                        StringUtils.isEmpty(c.getBomComponentId()) ? "0" : c.getBomComponentId()))
                                        .thenComparing((MtEoComponentActualVO8 c) -> Double
                                                        .valueOf(StringUtils.isEmpty(c.getBomSubstituteGroupId()) ? "0"
                                                                        : c.getBomSubstituteGroupId()))
                                        .thenComparing((MtEoComponentActualVO8 c) -> Double
                                                        .valueOf(StringUtils.isEmpty(c.getBomSubstituteId()) ? "0"
                                                                        : c.getBomSubstituteId())))
                        .collect(Collectors.toList());
    }

    @Override
    public List<String> propertyLimitEoComponentAssembleActualQuery(Long tenantId, MtEoComponentActualVO9 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getEoComponentActualId()) && StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032", "ORDER",
                                            "eoComponentActualId、eoId",
                                            "【API:propertyLimitEoComponentAssembleActualQuery】"));
        }
        List<MtEoComponentActual> list = this.mtEoComponentActualMapper.limitQueryComponentActual(tenantId, dto);
        return list.stream().map(MtEoComponentActual::getEoComponentActualId).collect(Collectors.toList());
    }

    @Override
    public MtEoComponentActual eoComponentAssembleActualPropertyGet(Long tenantId, String eoComponentActualId) {
        if (StringUtils.isEmpty(eoComponentActualId)) {
            throw new MtException("MT_ORDER_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001", "ORDER",
                                            "eoComponentActualId", "【API:eoComponentAssembleActualPropertyGet】"));
        }

        MtEoComponentActual mtEoComponentActual = new MtEoComponentActual();
        mtEoComponentActual.setTenantId(tenantId);
        mtEoComponentActual.setEoComponentActualId(eoComponentActualId);
        return this.mtEoComponentActualMapper.selectOne(mtEoComponentActual);
    }

    @Override
    public List<MtEoComponentActual> componentLimitEoComponentScrapActualQuery(Long tenantId,
                    MtEoComponentActualVO10 dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:componentLimitEoComponentScrapActualQuery】"));
        }
        return this.mtEoComponentActualMapper.limitScrapQueryComponentActual(tenantId, dto);
    }

    @Override
    public List<MtEoComponentActual> materialLimitEoComponentAssembleActualQuery(Long tenantId,
                    MtEoComponentActualVO9 dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:materialLimitEoComponentAssembleActualQuery】"));
        }
        return this.mtEoComponentActualMapper.limitQueryComponentActual(tenantId, dto);
    }

    @Override
    public List<MtEoComponentActual> materialLimitEoComponentScrapActualQuery(Long tenantId,
                    MtEoComponentActualVO9 dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:materialLimitEoComponentScrapActualQuery】"));
        }
        return this.mtEoComponentActualMapper.materialLimitScrapActualQuery(tenantId, dto);
    }

    @Override
    public List<MtEoComponentActual> componentLimitEoComponentAssembleActualQuery(Long tenantId,
                    MtEoComponentActualVO10 dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:componentLimitEoComponentAssembleActualQuery】"));
        }
        return this.mtEoComponentActualMapper.limitAssableQueryCompentActul(tenantId, dto);
    }

    @Override
    public List<MtEoComponentActualVO13> eoUnassembledComponentQuery(Long tenantId, MtEoComponentActualVO12 dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoUnassembledComponentQuery】"));

        }
        // Step 2获取执行作业组件和组件用量列表
        MtEoVO19 mtEoVO19 = new MtEoVO19();
        mtEoVO19.setEoId(dto.getEoId());
        mtEoVO19.setOperationId(dto.getOperationId());
        mtEoVO19.setRouterStepId(dto.getRouterStepId());
        List<MtEoVO20> mtEoVO20List = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);

        // Step 3获取执行作业或执行作业在指定工艺已装配的组件实绩列表
        MtEoComponentActualVO10 mtEoComponentVO10 = new MtEoComponentActualVO10();
        mtEoComponentVO10.setEoId(dto.getEoId());
        mtEoComponentVO10.setOperationId(dto.getOperationId());
        mtEoComponentVO10.setRouterStepId(dto.getRouterStepId());
        List<MtEoComponentActual> actualList =
                        componentLimitEoComponentAssembleActualQuery(tenantId, mtEoComponentVO10);

        Map<String, Map<String, BigDecimal>> resultMap = actualList.stream().collect(Collectors.groupingBy(
                        MtEoComponentActual::getBomComponentId,
                        Collectors.groupingBy(MtEoComponentActual::getMaterialId,
                                        CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(
                                                        c.getAssembleQty() == null ? 0.0D : c.getAssembleQty())))));

        // 根据bomComponentId筛选存在与第二步结果中但是不存在于第三步获取结果中的数据列表
        List<String> bomComponentIds = new ArrayList<String>();
        for (MtEoVO20 mtEoVO20 : mtEoVO20List) {
            if (!resultMap.containsKey(mtEoVO20.getBomComponentId())) {
                bomComponentIds.add(mtEoVO20.getBomComponentId());
            }
        }

        final List<MtEoComponentActualVO13> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(bomComponentIds)) {
            List<MtBomComponentVO13> bomComponents =
                            mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIds);
            if (CollectionUtils.isNotEmpty(bomComponents)) {
                for (MtBomComponent mtBomComponent : bomComponents) {
                    List<MtEoVO20> tempEoVO20List = mtEoVO20List.stream()
                                    .filter(c -> c.getBomComponentId().equals(mtBomComponent.getBomComponentId()))
                                    .collect(Collectors.toList());

                    tempEoVO20List.stream().forEach(c -> {
                        MtEoComponentActualVO13 mtEoComponentVO13 = new MtEoComponentActualVO13();
                        mtEoComponentVO13.setBomComponentId(c.getBomComponentId());
                        mtEoComponentVO13.setMaterialId(c.getMaterialId());
                        mtEoComponentVO13.setUnassembledQty(c.getComponentQty());
                        mtEoComponentVO13.setComponentType(mtBomComponent.getBomComponentType());
                        list.add(mtEoComponentVO13);
                    });
                }
            }
        }

        // Step 5根据输入值unstartFlag对第二步第三步获取结果进行比较
        if ("Y".equals(dto.getUnstartFlag())) {
            return list;
        } else {
            // 根据bomComponentId筛选既存在与第二步结果也存在与第三步结果但第二步中的componentQty与第三步中的sum_assembleQty不一致的数据列表
            bomComponentIds.clear();
            for (MtEoVO20 mtEoVO20 : mtEoVO20List) {
                if (resultMap.containsKey(mtEoVO20.getBomComponentId())) {
                    bomComponentIds.add(mtEoVO20.getBomComponentId());
                }
            }

            final List<MtEoComponentActualVO13> list2 = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(bomComponentIds)) {
                List<MtBomComponentVO13> bomComponents =
                                mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIds);

                if (CollectionUtils.isNotEmpty(bomComponents)) {
                    for (MtBomComponent mtBomComponent : bomComponents) {
                        List<MtEoVO20> tempEoVO20List = mtEoVO20List.stream()
                                        .filter(c -> c.getBomComponentId().equals(mtBomComponent.getBomComponentId()))
                                        .collect(Collectors.toList());
                        Map<String, BigDecimal> map = resultMap.get(mtBomComponent.getBomComponentId());

                        for (MtEoVO20 vo : tempEoVO20List) {
                            for (Entry<String, BigDecimal> entry : map.entrySet()) {
                                if (vo.getMaterialId().equals(entry.getKey())) {
                                    MtEoComponentActualVO13 mtEoComponentVO13 = new MtEoComponentActualVO13();
                                    mtEoComponentVO13.setBomComponentId(vo.getBomComponentId());
                                    mtEoComponentVO13.setMaterialId(vo.getMaterialId());

                                    BigDecimal componentQty = BigDecimal.valueOf(
                                                    vo.getComponentQty() == null ? 0.0D : vo.getComponentQty());
                                    BigDecimal sumAssembleQty = entry.getValue();
                                    mtEoComponentVO13.setUnassembledQty(
                                                    componentQty.subtract(sumAssembleQty).doubleValue());
                                    mtEoComponentVO13.setComponentType(mtBomComponent.getBomComponentType());
                                    list2.add(mtEoComponentVO13);
                                } else {
                                    MtEoComponentActualVO13 mtEoComponentVO13 = new MtEoComponentActualVO13();
                                    mtEoComponentVO13.setBomComponentId(vo.getBomComponentId());
                                    mtEoComponentVO13.setMaterialId(entry.getKey());

                                    MtBomSubstituteVO6 bomSubstituteVO6 = new MtBomSubstituteVO6();
                                    bomSubstituteVO6.setBomComponentId(vo.getBomComponentId());
                                    bomSubstituteVO6.setQty(vo.getComponentQty());
                                    // 新增传入第3步获取的materialId不再进行筛选(仅获取到一条数据)
                                    bomSubstituteVO6.setMaterialId(entry.getKey());
                                    List<MtBomSubstituteVO3> bomSubstituteList = mtBomSubstituteRepository
                                                    .bomSubstituteQtyCalculate(tenantId, bomSubstituteVO6);
                                    BigDecimal componentQty = BigDecimal.ZERO;
                                    if (CollectionUtils.isNotEmpty(bomSubstituteList)) {
                                        componentQty = BigDecimal.valueOf(
                                                        bomSubstituteList.get(0).getComponentQty() == null ? 0.0D
                                                                        : bomSubstituteList.get(0).getComponentQty());
                                    }

                                    BigDecimal sumAssembleQty = entry.getValue();
                                    mtEoComponentVO13.setUnassembledQty(
                                                    componentQty.subtract(sumAssembleQty).doubleValue());
                                    mtEoComponentVO13.setComponentType(mtBomComponent.getBomComponentType());
                                    list2.add(mtEoComponentVO13);

                                }
                            }
                        }
                    }
                }
            }
            list.addAll(list2);
            // 新增逻辑(最终返回unassembledQty不为0或不为null的数据)
            return list.stream().filter(t -> t.getUnassembledQty() != null
                            && new BigDecimal(t.getUnassembledQty().toString()).compareTo(BigDecimal.ZERO) != 0)
                            .collect(Collectors.toList());
        }
    }

    @Override
    public MtEoComponentActualVO15 eoComponentAssemblePeriodGet(Long tenantId, MtEoComponentActualVO14 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getEoId()) && StringUtils.isEmpty(dto.getEoComponentActualId())) {
            throw new MtException("MT_ORDER_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032", "ORDER",
                                            "eoId、eoComponentActualId", "【API:eoComponentAssemblePeriodGet】"));
        }
        if (StringUtils.isNotEmpty(dto.getPeriodUom()) && !"DAY".equals(dto.getPeriodUom())
                        && !"HOUR".equals(dto.getPeriodUom()) && !"MIN".equals(dto.getPeriodUom())) {
            throw new MtException("MT_ORDER_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0035", "ORDER", "[DAY、HOUR、MIN]", "【API:eoComponentAssemblePeriodGet】"));
        }
        // Step 2
        MtEoComponentActual actual = new MtEoComponentActual();
        actual.setTenantId(tenantId);
        actual.setEoComponentActualId(dto.getEoComponentActualId());
        actual.setEoId(dto.getEoId());
        actual.setMaterialId(dto.getMaterialId());
        actual.setOperationId(dto.getOperationId());
        List<MtEoComponentActual> list = this.mtEoComponentActualMapper.select(actual);
        MtEoComponentActualVO15 vo15 = new MtEoComponentActualVO15();
        Date minActualFirstTime = null;
        Date maxActualLastTime = null;
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        if (list.size() == 1) {
            minActualFirstTime = list.get(0).getActualFirstTime();
            maxActualLastTime = list.get(0).getActualLastTime();
        } else if (list.size() > 1) {
            List<Date> minList = list.stream().filter(t -> t.getActualFirstTime() != null)
                            .sorted(Comparator.comparing(MtEoComponentActual::getActualFirstTime))
                            .map(MtEoComponentActual::getActualFirstTime).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(minList)) {
                minActualFirstTime = minList.get(0);
            }
            if (list.stream().allMatch(t -> t.getActualLastTime() != null)) {
                maxActualLastTime = list.stream()
                                .sorted(Comparator.comparing(MtEoComponentActual::getActualLastTime).reversed())
                                .map(MtEoComponentActual::getActualLastTime).findFirst().get();

            }
        }
        // Step 3根据第二步获取结果和输入单位计算时长
        vo15.setActualFirstTime(minActualFirstTime);
        vo15.setActualLastTime(maxActualLastTime);

        // 当开始时间与结束时间均不为空时返回计算结果
        if (StringUtils.isEmpty(dto.getPeriodUom())) {
            vo15.setPeriodUom("HOUR");
        } else {
            vo15.setPeriodUom(dto.getPeriodUom());
        }
        if (minActualFirstTime != null && maxActualLastTime != null) {
            BigDecimal tempTime =
                            new BigDecimal(String.valueOf(maxActualLastTime.getTime() - minActualFirstTime.getTime()));
            if (StringUtils.isEmpty(dto.getPeriodUom()) || "HOUR".equals(dto.getPeriodUom())) {
                vo15.setPeriodTime(tempTime.divide(new BigDecimal(1000 * 60 * 60 + ""), 6, BigDecimal.ROUND_HALF_DOWN)
                                .doubleValue());
            }
            if ("DAY".equals(dto.getPeriodUom())) {
                vo15.setPeriodTime(
                                tempTime.divide(new BigDecimal(1000 * 60 * 60 * 24 + ""), 6, BigDecimal.ROUND_HALF_DOWN)
                                                .doubleValue());

            }

            if ("MIN".equals(dto.getPeriodUom())) {
                vo15.setPeriodTime(tempTime.divide(new BigDecimal(1000 * 60 + ""), 6, BigDecimal.ROUND_HALF_DOWN)
                                .doubleValue());

            }
        }
        return vo15;
    }

    @Override
    public List<MtEoComponentActualVO16> eoAssembledSubstituteMaterialQuery(Long tenantId, MtEoComponentActualVO6 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoAssembledSubstituteMaterialQuery】"));
        }

        // Step 2获取执行作业数量
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null || mtEo.getQty() == null) {
            return Collections.emptyList();
        }

        List<MtEoComponentActualVO16> resultList = new ArrayList<MtEoComponentActualVO16>();

        // Step 3获取执行作业组件装配实绩
        MtEoComponentActual actual = new MtEoComponentActual();
        actual.setEoId(dto.getEoId());
        actual.setBomComponentId(dto.getBomComponentId());
        actual.setRouterStepId(dto.getRouterStepId());
        actual.setSubstituteFlag("Y");
        actual.setBomId(dto.getBomId());
        List<MtEoComponentActual> list =
                        this.mtEoComponentActualMapper.queryAssembledSubstituteMaterial(tenantId, actual);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<String> stringList =
                        list.stream().map(MtEoComponentActual::getBomComponentId).collect(Collectors.toList());
        List<MtBomComponentVO13> mtBomComponentList =
                        mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, stringList);

        // Step 4根据第三步结果列表依次获取组件行原始物料和需求数量
        Double preQty = 0.0D;
        Double componentRequirementQty = 0.0D;
        List<MtRouterOpComponentVO> voList;
        for (MtEoComponentActual t : list) {
            MtEoComponentActualVO16 mtEoComponentVO16 = new MtEoComponentActualVO16();
            BeanUtils.copyProperties(t, mtEoComponentVO16);
            if (StringUtils.isNotEmpty(t.getRouterStepId())) {
                MtRouterOpComponentVO1 vo1 = new MtRouterOpComponentVO1();
                vo1.setRouterStepId(t.getRouterStepId());
                voList = mtRouterOperationComponentRepository.routerOperationComponentPerQtyQuery(tenantId, vo1);
                for (MtRouterOpComponentVO tt : voList) {
                    if (t.getBomComponentId().equals(tt.getBomComponentId())) {
                        preQty = (tt.getPerQty() == null) ? Double.valueOf(0.0D) : tt.getPerQty();
                        break;
                    }
                }
                componentRequirementQty = new BigDecimal(preQty.toString())
                                .multiply(new BigDecimal(mtEo.getQty().toString())).doubleValue();
            } else {
                List<String> bomIds = new ArrayList<>();
                bomIds.add(t.getBomId());
                List<MtBomVO7> mtBomList = mtBomRepository.bomBasicBatchGet(tenantId, bomIds);
                for (MtBomComponent mtBomComponent : mtBomComponentList) {
                    if (t.getBomComponentId().equals(mtBomComponent.getBomComponentId())) {
                        componentRequirementQty = BigDecimal.valueOf(mtEo.getQty())
                                        .multiply(BigDecimal.valueOf(mtBomComponent.getQty()).divide(
                                                        BigDecimal.valueOf(mtBomList.get(0).getPrimaryQty()), 10,
                                                        BigDecimal.ROUND_HALF_DOWN))
                                        .doubleValue();
                        break;
                    }
                }

            }
            mtBomComponentList.stream()
                            .filter(mtBomComponent -> mtBomComponent.getBomComponentId().equals(t.getBomComponentId()))
                            .forEach(mtBomComponent -> {
                                mtEoComponentVO16.setComponentMaterialId(mtBomComponent.getMaterialId());
                            });
            mtEoComponentVO16.setComponentRequirementQty(componentRequirementQty);

            // Step 5根据第三步获取的每一条数据和第四步结果获取替代物料使用数量
            MtBomSubstituteVO6 bomSubstituteVO6 = new MtBomSubstituteVO6();
            bomSubstituteVO6.setBomComponentId(mtEoComponentVO16.getBomComponentId());
            bomSubstituteVO6.setQty(componentRequirementQty);
            bomSubstituteVO6.setMaterialId(t.getMaterialId());

            // 仅返回一条数据
            List<MtBomSubstituteVO3> vo3List =
                            mtBomSubstituteRepository.bomSubstituteQtyCalculate(tenantId, bomSubstituteVO6);
            if (CollectionUtils.isNotEmpty(vo3List)) {
                mtEoComponentVO16.setRequirementQty(vo3List.get(0).getComponentQty());
            }
            resultList.add(mtEoComponentVO16);
        }
        return resultList;
    }

    @Override
    public List<MtEoComponentActualVO17> eoAssembledExcessMaterialQuery(Long tenantId, MtEoComponentActualVO6 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoAssembledExcessMaterialQuery】"));
        }
        // Step 2
        MtEoComponentActual actual = new MtEoComponentActual();
        actual.setTenantId(tenantId);
        actual.setEoId(dto.getEoId());
        actual.setMaterialId(dto.getMaterialId());
        actual.setOperationId(dto.getOperationId());
        actual.setAssembleExcessFlag("Y");
        actual.setBomId(dto.getBomId());
        List<MtEoComponentActual> actualList = this.mtEoComponentActualMapper.select(actual);
        List<MtEoComponentActualVO17> list = new ArrayList<>();
        actualList.stream().forEach(t -> {
            MtEoComponentActualVO17 vo17 = new MtEoComponentActualVO17();
            BeanUtils.copyProperties(t, vo17);
            list.add(vo17);
        });

        return list;
    }

    @Override
    public List<String> eoMaterialLimitComponentQuery(Long tenantId, MtEoComponentActualVO6 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoMaterialLimitComponentGet】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "materialId", "【API:eoMaterialLimitComponentGet】"));
        }

        // Step 2
        String bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
        if (StringUtils.isEmpty(bomId)) {
            return Collections.emptyList();
        }

        // Step 3
        MtBomComponentVO bomComponentVO = new MtBomComponentVO();
        bomComponentVO.setBomId(bomId);
        bomComponentVO.setMaterialId(dto.getMaterialId());
        if (StringUtils.isEmpty(dto.getComponentType())) {
            bomComponentVO.setBomComponentType("ASSEMBLING");
        } else {
            bomComponentVO.setBomComponentType(dto.getComponentType());
        }
        bomComponentVO.setOnlyAvailableFlag("Y");

        List<MtBomComponentVO16> listMap =
                        mtBomComponentRepository.propertyLimitBomComponentQuery(tenantId, bomComponentVO);
        if (CollectionUtils.isEmpty(listMap)) {
            return Collections.emptyList();
        }

        List<String> stringList = new ArrayList<>();
        if (StringUtils.isEmpty(dto.getOperationId())) {
            stringList.addAll(listMap.get(0).getBomComponentId());
        } else {
            String routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
            if (StringUtils.isEmpty(routerId)) {
                return Collections.emptyList();
            }

            List<MtRouterStepVO5> routerStepOpVOS = this.mtRouterStepRepository.routerStepListQuery(tenantId, routerId);
            List<String> routerStepIds =
                            routerStepOpVOS.stream().filter(t -> t.getOperationId().equals(dto.getOperationId()))
                                            .map(MtRouterStepVO5::getRouterStepId).collect(Collectors.toList());

            List<String> bomComponentIds = new ArrayList<String>();
            for (String routerStepId : routerStepIds) {
                List<MtRouterOperationComponent> mtRouterOperationComponents = mtRouterOperationComponentRepository
                                .routerOperationComponentQuery(tenantId, routerStepId);
                bomComponentIds.addAll(mtRouterOperationComponents.stream()
                                .map(MtRouterOperationComponent::getBomComponentId).collect(Collectors.toList()));
            }

            if (CollectionUtils.isEmpty(bomComponentIds)) {
                return Collections.emptyList();
            }

            List<String> disBomComponentIds = bomComponentIds.stream().distinct().collect(Collectors.toList());
            stringList.addAll(listMap.get(0).getBomComponentId().stream().filter(t -> disBomComponentIds.contains(t))
                            .collect(Collectors.toList()));
        }

        return stringList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoComponentMerge(Long tenantId, MtEoComponentActualVO18 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getPrimaryEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "primaryEoId", "【API:eoComponentMerge】"));
        }
        if (CollectionUtils.isEmpty(dto.getSecondaryEoIds())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "secondaryEoIds", "【API:eoComponentMerge】"));
        }
        if (StringUtils.isEmpty(dto.getTargetEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "targetEoId", "【API:eoComponentMerge】"));
        }
        List<String> eoIds = new ArrayList<>();
        eoIds.add(dto.getPrimaryEoId());
        eoIds.addAll(dto.getSecondaryEoIds());
        List<MtEo> mtEoList = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
        if (CollectionUtils.isEmpty(mtEoList)) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoComponentMerge】"));
        }

        // Step 2 获取主来源执行作业装配清单
        String bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getPrimaryEoId());
        if (StringUtils.isEmpty(bomId)) {
            return;
        }

        // Step 3获取事件ID(新增事件)
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setEventTypeCode("EO_COMPONENT_ACTUAL_MERGE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // Step 4根据来源执行作业装配清单更新目标执行作业装配清单
        MtEoBomVO mtEoBomVO = new MtEoBomVO();
        mtEoBomVO.setEoId(dto.getTargetEoId());
        mtEoBomVO.setBomId(bomId);
        mtEoBomVO.setEventId(eventId);
        mtEoBomRepository.eoBomUpdate(tenantId, mtEoBomVO);

        // Step 5 获取主、副来源执行作业组件实绩
        // 5-a根据输入的主、副来源执行作业依次调用
        MtEoComponentActualVO9 vo9 = new MtEoComponentActualVO9();
        vo9.setEoId(dto.getPrimaryEoId());
        List<MtEoComponentActual> actualList = materialLimitEoComponentAssembleActualQuery(tenantId, vo9);

        for (String t : dto.getSecondaryEoIds()) {
            vo9 = new MtEoComponentActualVO9();
            vo9.setEoId(t);
            actualList.addAll(materialLimitEoComponentAssembleActualQuery(tenantId, vo9));
        }

        if (CollectionUtils.isEmpty(actualList)) {
            return;
        }

        List<MtEoComponentActual> actualList1 = actualList;
        Map<String, BigDecimal> result1 = actualList.stream().collect(Collectors.groupingBy(
                        c -> c.getMaterialId() + "@" + c.getOperationId() + "@" + c.getComponentType() + "@"
                                        + c.getBomComponentId() + "@" + c.getRouterStepId() + "@" + c.getBomId(),
                        CollectorsUtil.summingBigDecimal(c -> BigDecimal
                                        .valueOf(c.getAssembleQty() == null ? 0.0D : c.getAssembleQty()))));

        Map<String, BigDecimal> result2 = actualList.stream().collect(Collectors.groupingBy(
                        c -> c.getMaterialId() + "@" + c.getOperationId() + "@" + c.getComponentType() + "@"
                                        + c.getBomComponentId() + "@" + c.getRouterStepId() + "@" + c.getBomId(),
                        CollectorsUtil.summingBigDecimal(c -> BigDecimal
                                        .valueOf(c.getScrappedQty() == null ? 0.0D : c.getScrappedQty()))));

        List<MtEoComponentActual> actualList2 = new ArrayList<>();
        result1.entrySet().forEach(t -> {
            result2.entrySet().forEach(tt -> {
                if (t.getKey().equals(tt.getKey())) {
                    MtEoComponentActual actual = new MtEoComponentActual();
                    String[] s = t.getKey().split("@", -1);
                    actual.setMaterialId(s[0]);
                    actual.setOperationId(s[1]);
                    actual.setComponentType(s[2]);
                    actual.setBomComponentId(s[3]);
                    actual.setRouterStepId(s[4]);
                    actual.setBomId(s[5]);
                    actual.setAssembleQty(t.getValue().doubleValue());
                    actual.setScrappedQty(tt.getValue().doubleValue());
                    actualList2.add(actual);
                }
            });
        });

        // Step 6
        for (MtEoComponentActual t : actualList1) {
            MtEoComponentActualHis actualHis = new MtEoComponentActualHis();
            actualHis.setEoComponentActualId(t.getEoComponentActualId());
            actualHis.setAssembleQty(0.0D);
            actualHis.setScrappedQty(0.0D);
            actualHis.setEventId(eventId);
            eoComponentActualUpdate(tenantId, actualHis);
        }

        // Step 7针对第五步6-a-iii获取到的每行数据的汇总结果更新目标执行作业组件装配实绩
        for (MtEoComponentActual t : actualList2) {
            MtEoComponentActualHis actualHis = new MtEoComponentActualHis();
            actualHis.setEoId(dto.getTargetEoId());
            actualHis.setMaterialId(t.getMaterialId());
            actualHis.setOperationId(t.getOperationId());
            actualHis.setTrxAssembleQty(t.getAssembleQty());
            actualHis.setTrxScrappedQty(t.getScrappedQty());
            actualHis.setComponentType(t.getComponentType());
            actualHis.setBomComponentId(t.getBomComponentId());
            actualHis.setBomId(t.getBomId());
            actualHis.setRouterStepId(t.getRouterStepId());
            actualHis.setEventId(eventId);
            eoComponentActualUpdate(tenantId, actualHis);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoComponentSplit(Long tenantId, MtEoComponentActualVO19 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getSourceEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "sourceEoId", "【API:eoComponentSplit】"));
        }
        if (StringUtils.isEmpty(dto.getTargetEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "targetEoId", "【API:eoComponentSplit】"));
        }
        if (dto.getSplitQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "splitQty", "【API:eoComponentSplit】"));
        }

        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getSourceEoId());
        if (null == mtEo) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoComponentSplit】"));
        }

        if (new BigDecimal(dto.getSplitQty().toString()).compareTo(new BigDecimal(mtEo.getQty().toString())) >= 0) {
            throw new MtException("MT_ORDER_0140", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0140", "ORDER", "【API:eoComponentSplit】"));
        }

        if (new BigDecimal(dto.getSplitQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "splitQty", "【API:eoComponentSplit】"));
        }

        // Step 2 获取来源执行作业装配清单
        String bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getSourceEoId());
        if (StringUtils.isEmpty(bomId)) {
            return;
        }

        // Step 3获取事件ID(新增事件)
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        BeanUtils.copyProperties(dto, eventCreateVO);
        eventCreateVO.setEventTypeCode("EO_COMPONENT_ACTUAL_SPLIT");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // Step 4根据来源执行作业装配清单更新目标执行作业装配清单
        MtEoBomVO mtEoBomVO = new MtEoBomVO();
        mtEoBomVO.setEoId(dto.getTargetEoId());
        mtEoBomVO.setBomId(bomId);
        mtEoBomVO.setEventId(eventId);
        mtEoBomRepository.eoBomUpdate(tenantId, mtEoBomVO);

        // Step 5获取来源执行作业组件实绩
        MtEoComponentActualVO10 vo10 = new MtEoComponentActualVO10();
        vo10.setEoId(dto.getSourceEoId());
        vo10.setBomId(bomId);
        List<MtEoComponentActual> actualList = componentLimitEoComponentAssembleActualQuery(tenantId, vo10);
        if (CollectionUtils.isEmpty(actualList)) {
            return;
        }

        // Step
        // 6根据第五步获取到的结果列表依次获取来源执行作业组件实绩装配组件单位用量perQty(若perQty获取结果为空，认为perQty =
        // 0)
        List<MtRouterOpComponentVO> voList;
        for (MtEoComponentActual t : actualList) {
            // 6-a
            BigDecimal perQty = BigDecimal.ZERO;
            if (!"Y".equals(t.getSubstituteFlag())) {
                if (StringUtils.isNotEmpty(t.getRouterStepId())) {
                    MtRouterOpComponentVO1 vo1 = new MtRouterOpComponentVO1();
                    vo1.setRouterStepId(t.getRouterStepId());
                    vo1.setBomComponentId(t.getBomComponentId());
                    voList = mtRouterOperationComponentRepository.routerOperationComponentPerQtyQuery(tenantId, vo1);
                    if (CollectionUtils.isNotEmpty(voList)) {
                        // 依据条件获取到一条数据
                        perQty = voList.stream().collect(
                                        CollectorsUtil.summingBigDecimal(c -> c.getPerQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(c.getPerQty())));
                    }
                } else {
                    MtBomComponentVO8 bomComponentVO8 =
                                    mtBomComponentRepository.bomComponentBasicGet(tenantId, t.getBomComponentId());
                    if (bomComponentVO8 != null) {
                        perQty = BigDecimal.valueOf(bomComponentVO8.getPreQty());
                    }
                }
            } else {
                if (StringUtils.isNotEmpty(t.getRouterStepId())) {
                    MtRouterOpComponentVO1 vo1 = new MtRouterOpComponentVO1();
                    vo1.setRouterStepId(t.getRouterStepId());
                    vo1.setBomComponentId(t.getBomComponentId());
                    // 仅会获取到一条数据
                    voList = mtRouterOperationComponentRepository.routerOperationComponentPerQtyQuery(tenantId, vo1);
                    if (CollectionUtils.isNotEmpty(voList)) {
                        for (MtRouterOpComponentVO tt : voList) {
                            MtBomSubstituteVO6 bomSubstituteVO6 = new MtBomSubstituteVO6();
                            bomSubstituteVO6.setMaterialId(t.getMaterialId());
                            bomSubstituteVO6.setBomComponentId(t.getBomComponentId());
                            bomSubstituteVO6.setQty(tt.getPerQty());
                            List<MtBomSubstituteVO3> vo3List = mtBomSubstituteRepository
                                            .bomSubstituteQtyCalculate(tenantId, bomSubstituteVO6);
                            if (CollectionUtils.isNotEmpty(vo3List)) {
                                perQty = perQty.add(vo3List.stream().collect(CollectorsUtil
                                                .summingBigDecimal(c -> c.getComponentQty() == null ? BigDecimal.ZERO
                                                                : BigDecimal.valueOf(c.getComponentQty()))));
                            }
                        }
                    }
                } else {
                    MtBomComponentVO8 bomComponentVO8 =
                                    mtBomComponentRepository.bomComponentBasicGet(tenantId, t.getBomComponentId());
                    if (bomComponentVO8 != null) {
                        MtBomSubstituteVO6 bomSubstituteVO6 = new MtBomSubstituteVO6();
                        bomSubstituteVO6.setMaterialId(t.getMaterialId());
                        bomSubstituteVO6.setBomComponentId(t.getBomComponentId());
                        bomSubstituteVO6.setQty(bomComponentVO8.getPreQty());
                        List<MtBomSubstituteVO3> vo3List =
                                        mtBomSubstituteRepository.bomSubstituteQtyCalculate(tenantId, bomSubstituteVO6);
                        if (CollectionUtils.isNotEmpty(vo3List)) {
                            perQty = perQty.add(vo3List.stream()
                                            .collect(CollectorsUtil.summingBigDecimal(c -> c.getComponentQty() == null
                                                            ? BigDecimal.ZERO
                                                            : BigDecimal.valueOf(c.getComponentQty()))));
                        }
                    }
                }
            }

            // Step7减少来源执行作业组件实绩装配数量并新增目标执行作业组件装配实绩
            MtEoComponentActualHis actualHis = new MtEoComponentActualHis();
            actualHis.setEoComponentActualId(t.getEoComponentActualId());
            Double assembleAty = 0.0D;
            if (perQty.compareTo(BigDecimal.ZERO) == 0) {
                assembleAty = t.getAssembleQty();
            } else {
                Double temp = BigDecimal.valueOf(mtEo.getQty())
                                .subtract(BigDecimal.valueOf(dto.getSplitQty()).multiply(perQty)).doubleValue();
                assembleAty = Math.min(t.getAssembleQty(), temp);
            }
            actualHis.setEventId(eventId);
            actualHis.setAssembleQty(assembleAty);
            eoComponentActualUpdate(tenantId, actualHis);

            // Step 7-b若perQty≠0，则继续调用
            if (new BigDecimal(perQty.toString()).compareTo(BigDecimal.ZERO) != 0) {
                MtEoComponentActualHis actualHis1 = new MtEoComponentActualHis();
                BeanUtils.copyProperties(t, actualHis1);
                actualHis1.setEventId(eventId);
                actualHis1.setEoId(dto.getTargetEoId());
                actualHis1.setAssembleQty(BigDecimal.valueOf(t.getAssembleQty())
                                .subtract(BigDecimal.valueOf(actualHis.getAssembleQty())).doubleValue());
                actualHis1.setTrxScrappedQty(null);
                actualHis1.setEoComponentActualId(null);
                eoComponentActualUpdate(tenantId, actualHis1);
            }
        }
    }

    @Override
    public void eoComponentUpdateVerify(Long tenantId, MtEoComponentActualVO20 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoComponentUpdateVerify】"));
        }
        if (StringUtils.isEmpty(dto.getBomComponentId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "bomComponentId", "【API:eoComponentUpdateVerify】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "materialId", "【API:eoComponentUpdateVerify】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "qty", "【API:eoComponentUpdateVerify】"));
        }

        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (null == mtEo) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoComponentUpdateVerify】"));
        }

        String status = mtEo.getStatus();
        String bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
        if (StringUtils.isEmpty(bomId)) {
            throw new MtException("MT_ORDER_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0024", "ORDER", "【API:eoComponentUpdateVerify】"));
        }

        String flag = mtBomRepository.bomAutoRevisionGet(tenantId, bomId);
        if ("Y".equals(flag)) {
            MtEoBomVO mtEoBomVO = new MtEoBomVO();
            mtEoBomVO.setBomId(bomId);
            mtEoBomVO.setEoId(dto.getEoId());

            // 验证执行作业装配清单变更，并返回该API的验证结果，结束API
            mtEoBomRepository.eoBomUpdateValidate(tenantId, mtEoBomVO);
            return;
        }

        // Step 2验证执行作业状态是否满足变更要求
        if (!"NEW".equals(status) && !"HOLD".equals(status)) {
            throw new MtException("MT_ORDER_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0044", "ORDER", "【‘NEW’，‘HOLD’】", "【API:eoComponentUpdateVerify】"));
        }

        // Step 3验证执行作业是否已存在组件装配实绩
        MtEoComponentActualVO10 vo10 = new MtEoComponentActualVO10();
        vo10.setEoId(dto.getEoId());
        vo10.setBomId(bomId);
        vo10.setBomComponentId(dto.getBomComponentId());
        List<MtEoComponentActual> actualList = componentLimitEoComponentAssembleActualQuery(tenantId, vo10);
        if (CollectionUtils.isNotEmpty(actualList)) {
            Boolean hasNotNullFlag = false;
            for (MtEoComponentActual t : actualList) {
                if (t.getAssembleQty() != null
                                && new BigDecimal(t.getAssembleQty().toString()).compareTo(BigDecimal.ZERO) != 0
                                || t.getScrappedQty() != null && new BigDecimal(t.getScrappedQty().toString())
                                                .compareTo(BigDecimal.ZERO) != 0) {
                    hasNotNullFlag = true;
                    if (!dto.getMaterialId().equals(t.getMaterialId())) {
                        throw new MtException("MT_ORDER_0133", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0133", "ORDER", "【API:eoComponentUpdateVerify】"));
                    }
                }
            }

            if (hasNotNullFlag) {
                BigDecimal sumAssembleQty = actualList.stream().collect(
                                CollectorsUtil.summingBigDecimal(c -> c.getAssembleQty() == null ? BigDecimal.ZERO
                                                : BigDecimal.valueOf(c.getAssembleQty())));

                if (new BigDecimal(dto.getQty().toString()).compareTo(sumAssembleQty) == -1) {
                    throw new MtException("MT_ORDER_0134", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0134", "ORDER", "【API:eoComponentUpdateVerify】"));
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtEoComponentActualVO24 eoComponentUpdate(Long tenantId, MtEoComponentActualVO21 dto, String fullUpdate) {
        // Step 1数据校验
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoComponentUpdate】"));
        }

        for (MtBomComponentVO9 t : dto.getBomComponentVO9s()) {
            if (t.getQty() == null) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "qty", "【API:eoComponentUpdate】"));
            }
            if (t.getLineNumber() == null) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "lineNumber", "【API:eoComponentUpdate】"));
            }
            if (StringUtils.isEmpty(t.getMaterialId())) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "materialId", "【API:eoComponentUpdate】"));
            }
            if (StringUtils.isEmpty(t.getBomComponentType())) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "bomComponentType", "【API:eoComponentUpdate】"));
            }
            if (t.getDateFrom() == null) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "dateFrom", "【API:eoComponentUpdate】"));
            }
        }

        // Step 2
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (null == mtEo) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoComponentUpdate】"));
        }
        String bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
        if (StringUtils.isEmpty(bomId)) {
            throw new MtException("MT_ORDER_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0024", "ORDER", "【API:eoComponentUpdate】"));
        }
        // Step 3 更新装配清单组件
        MtBomComponentVO10 vo10 = new MtBomComponentVO10();
        vo10.setBomComponents(dto.getBomComponentVO9s());
        vo10.setBomId(bomId);
        MtBomComponentVO15 mtBomComponentVO15 = mtBomComponentRepository.bomComponentUpdate(tenantId, vo10, fullUpdate);
        String dealBomId = null;
        MtEoComponentActualVO24 result = new MtEoComponentActualVO24();
        if (null != mtBomComponentVO15) {
            dealBomId = mtBomComponentVO15.getBomId();
            result.setBomComponentId(mtBomComponentVO15.getBomComponentId());
            result.setBomComponentHisId(mtBomComponentVO15.getBomComponentHisId());
        }

        // Step 4获取事件ID(新增事件)
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setEventTypeCode("EO_COMPONENT_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // Step 5更新执行作业装配清单
        MtEoBomVO mtEoBomVO = new MtEoBomVO();
        mtEoBomVO.setBomId(dealBomId);
        mtEoBomVO.setEoId(dto.getEoId());
        mtEoBomVO.setEventId(eventId);
        mtEoBomRepository.eoBomUpdate(tenantId, mtEoBomVO);

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoComponentAssembleBypass(Long tenantId, MtEoComponentActualVO23 dto) {
        // step1
        if (StringUtils.isEmpty(dto.getAssembleConfirmActualId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleConfirmActualId ", "【API:eoComponentAssembleBypass】"));
        }

        // step2
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setEventTypeCode("ASSEMBLE_ACTUAL_BYPASS");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtAssembleConfirmActualHis mtAssembleConfirmActual = new MtAssembleConfirmActualHis();
        mtAssembleConfirmActual.setAssembleConfirmActualId(dto.getAssembleConfirmActualId());
        mtAssembleConfirmActual.setBypassFlag("Y");
        if (dto.getBypassBy() == null) {
            mtAssembleConfirmActual.setBypassBy(DetailsHelper.getUserDetails().getUserId().toString());
        } else {
            mtAssembleConfirmActual.setBypassBy(dto.getBypassBy());
        }
        mtAssembleConfirmActual.setEventId(eventId);
        this.mtAssembleConfirmActualRepository.assembleConfirmActualUpdate(tenantId, mtAssembleConfirmActual);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoComponentAssembleBypassCancel(Long tenantId, MtEoComponentActualVO22 dto) {
        // step1
        if (StringUtils.isEmpty(dto.getAssembleConfirmActualId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleConfirmActualId ", "【API:eoComponentAssembleBypassCancel】"));
        }

        MtAssembleConfirmActual mtAssembleConfirmActual = this.mtAssembleConfirmActualRepository
                        .assembleConfirmActualPropertyGet(tenantId, dto.getAssembleConfirmActualId());
        if (mtAssembleConfirmActual == null || !"Y".equals(mtAssembleConfirmActual.getBypassFlag())) {
            throw new MtException("MT_ASSEMBLE_0074", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0074", "ASSEMBLE", "【API:eoComponentAssembleBypassCancel】"));
        }

        // step2
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setEventTypeCode("ASSEMBLE_ACTUAL_BYPASS_CANCEL");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtAssembleConfirmActualHis mtAssembleConfirmActualHis = new MtAssembleConfirmActualHis();
        mtAssembleConfirmActualHis.setAssembleConfirmActualId(dto.getAssembleConfirmActualId());
        mtAssembleConfirmActualHis.setBypassFlag("N");
        mtAssembleConfirmActualHis.setBypassBy("");
        mtAssembleConfirmActualHis.setEventId(eventId);
        this.mtAssembleConfirmActualRepository.assembleConfirmActualUpdate(tenantId, mtAssembleConfirmActualHis);
    }

    @Override
    public void eoAllComponentIsConfirmedValidate(Long tenantId, String eoId) {
        // step1
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId ", "【API:eoAllComponentIsConfirmedValidate】"));
        }
        MtEoComponentActualVO12 dto = new MtEoComponentActualVO12();
        dto.setEoId(eoId);
        dto.setUnstartFlag("Y");
        List<MtEoComponentActualVO13> mtEoComponentVO13s = this.eoUnassembledComponentQuery(tenantId, dto);
        if (CollectionUtils.isNotEmpty(mtEoComponentVO13s)) {
            throw new MtException("MT_ASSEMBLE_0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0041", "ASSEMBLE", "【API:eoAllComponentIsConfirmedValidate】"));
        }

        // 第三步，验证执行作业下所有已装配组件是否已经确认
        List<MtAssembleConfirmActualVO4> mtAssembleConfirmActualVO4s =
                        this.mtAssembleConfirmActualRepository.eoUnconfirmedComponentQuery(tenantId, eoId);
        if (CollectionUtils.isNotEmpty(mtAssembleConfirmActualVO4s)) {
            throw new MtException("MT_ASSEMBLE_0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0042", "ASSEMBLE", "【API:eoAllComponentIsConfirmedValidate】"));
        }
    }

    @Override
    public List<MtEoComponentActualVO25> propertyLimitEoComponentActualPropertyQuery(Long tenantId,
                    MtEoComponentActualVO9 dto) {
        List<MtEoComponentActualVO25> result = new ArrayList<>();
        // 第一步查询数据
        List<MtEoComponentActual> actuals =
                        this.mtEoComponentActualMapper.propertyLimitEoComponentActualPropertyQuery(tenantId, dto);

        if (CollectionUtils.isNotEmpty(actuals)) {
            // 第二步
            Map<String, String> operationNames = new HashMap<>();
            // 根据第一步获取到的工艺 operationId列表，调用API{operationBatchGet}获取站点编码和站点描述

            List<String> operationIds = actuals.stream().map(MtEoComponentActual::getOperationId)
                            .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(operationIds)) {
                List<MtOperation> mtOperations = mtOperationRepository.operationBatchGet(tenantId, operationIds);
                if (CollectionUtils.isNotEmpty(mtOperations)) {
                    operationNames = mtOperations.stream().collect(
                                    Collectors.toMap(MtOperation::getOperationId, MtOperation::getOperationName));
                }
            }

            // 根据第一步获取到的物料 materialId列表，调用API{materialPropertyBatchGet }获取物料编码和物料描述
            Map<String, MtMaterialVO> materialMap = new HashMap<>();
            List<String> materialIds = actuals.stream().map(MtEoComponentActual::getMaterialId)
                            .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(materialIds)) {
                List<MtMaterialVO> mtMaterialVOS =
                                this.mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);
                if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
                    materialMap = mtMaterialVOS.stream().collect(Collectors.toMap(MtMaterialVO::getMaterialId, t -> t));
                }
            }



            Map<String, String> bomNames = new HashMap<>();
            // 根据第一步获取到的装配清单 bomId列表，调用API{bomBasicBatchGet }获取装配清单描述
            List<String> bomIds = actuals.stream().map(MtEoComponentActual::getBomId).filter(StringUtils::isNotEmpty)
                            .distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(bomIds)) {
                List<MtBomVO7> bomVO7s = this.mtBomRepository.bomBasicBatchGet(tenantId, bomIds);
                if (CollectionUtils.isNotEmpty(bomVO7s)) {
                    bomNames = bomVO7s.stream().collect(Collectors.toMap(MtBomVO7::getBomId, MtBomVO7::getBomName));
                }
            }

            Map<String, String> routerStepNames = new HashMap<>();
            // 根据第一步获取到的工艺路线步骤 routerStepId列表，调用API{ routerStepBatchGet }获取步骤识别码
            List<String> routerStepIds = actuals.stream().map(MtEoComponentActual::getRouterStepId)
                            .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(routerStepIds)) {
                List<MtRouterStep> routerSteps = mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIds);
                if (CollectionUtils.isNotEmpty(routerSteps)) {
                    routerStepNames = routerSteps.stream().collect(
                                    Collectors.toMap(MtRouterStep::getRouterStepId, MtRouterStep::getStepName));
                }
            }

            Map<String, String> eoNums = new HashMap<>();
            // 根据第一步获取到的执行作业 eoId列表，调用API{ eoPropertyBatchGet }获取执行作业编号
            List<String> eoIds = actuals.stream().map(MtEoComponentActual::getEoId).filter(StringUtils::isNotEmpty)
                            .distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(eoIds)) {
                List<MtEo> mtEos = this.mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
                if (CollectionUtils.isNotEmpty(mtEos)) {
                    eoNums = mtEos.stream().collect(Collectors.toMap(MtEo::getEoId, MtEo::getEoNum));
                }
            }

            for (MtEoComponentActual actual : actuals) {
                MtEoComponentActualVO25 vo25 = new MtEoComponentActualVO25();
                vo25.setEoComponentActualId(actual.getEoComponentActualId());
                vo25.setEoId(actual.getEoId());
                vo25.setMaterialId(actual.getMaterialId());
                vo25.setOperationId(actual.getOperationId());
                vo25.setAssembleQty(actual.getAssembleQty());
                vo25.setScrappedQty(actual.getScrappedQty());
                vo25.setComponentType(actual.getComponentType());
                vo25.setBomComponentId(actual.getBomComponentId());
                vo25.setBomId(actual.getBomId());
                vo25.setRouterStepId(actual.getRouterStepId());
                vo25.setAssembleExcessFlag(actual.getAssembleExcessFlag());
                vo25.setAssembleRouterType(actual.getAssembleRouterType());
                vo25.setSubstituteFlag(actual.getSubstituteFlag());
                vo25.setActualFirstTime(actual.getActualFirstTime());
                vo25.setActualLastTime(actual.getActualLastTime());
                vo25.setOperationName(operationNames.get(actual.getOperationId()));

                MtMaterialVO materialVO = materialMap.get(actual.getMaterialId());
                vo25.setMaterialCode(null != materialVO ? materialVO.getMaterialCode() : null);
                vo25.setMaterialName(null != materialVO ? materialVO.getMaterialName() : null);
                vo25.setBomName(bomNames.get(actual.getBomId()));
                vo25.setRouterStepName(routerStepNames.get(actual.getRouterStepId()));
                vo25.setEoNum(eoNums.get(actual.getEoId()));
                result.add(vo25);
            }
            return result.stream().sorted(Comparator
                            .comparingDouble((MtEoComponentActualVO25 c) -> Double
                                            .valueOf(StringUtils.isEmpty(c.getEoId()) ? "0" : c.getEoId()))
                            .thenComparingDouble(c -> Double
                                            .valueOf(StringUtils.isEmpty(c.getMaterialId()) ? "0" : c.getMaterialId()))
                            .thenComparingDouble(c -> Double.valueOf(
                                            StringUtils.isEmpty(c.getOperationId()) ? "0" : c.getOperationId()))
                            .thenComparingDouble(
                                            c -> Double.valueOf(StringUtils.isEmpty(c.getBomId()) ? "0" : c.getBomId()))
                            .thenComparingDouble(c -> Double.valueOf(
                                            StringUtils.isEmpty(c.getRouterStepId()) ? "0" : c.getRouterStepId())))
                            .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoComponentActualBatchUpdate(Long tenantId, MtEoComponentActualVO26 dto) {
        // 第一步，判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API:eoComponentActualBatchUpdate】"));
        }

        if (CollectionUtils.isEmpty(dto.getEoComponentActualList())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoComponentActualList", "【API:eoComponentActualBatchUpdate】"));
        }

        // 判断每一列表中若assembleQty和trxAssembleQty同时有值输入
        if (dto.getEoComponentActualList().stream()
                        .anyMatch(t -> null != t.getAssembleQty() && null != t.getTrxAssembleQty())) {
            throw new MtException("MT_ORDER_0102",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0102", "ORDER",
                                            "assembleQty、trxAssembleQty", "【API:eoComponentActualBatchUpdate】"));
        }

        // 判断每一列表中若scrappedQty和trxScrappedQty同时有值输入，返回错误消息
        if (dto.getEoComponentActualList().stream()
                        .anyMatch(t -> null != t.getScrappedQty() && null != t.getTrxScrappedQty())) {
            throw new MtException("MT_ORDER_0102",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0102", "ORDER",
                                            "crappedQty、trxScrappedQty", "【API:eoComponentActualBatchUpdate】"));
        }

        // 判断每一列表中输入参数eoComponentActualId、eoId是否均有输入且不为空
        if (dto.getEoComponentActualList().stream().anyMatch(
                        t -> StringUtils.isEmpty(t.getEoComponentActualId()) && StringUtils.isEmpty(t.getEoId()))) {
            throw new MtException("MT_ORDER_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032", "ORDER",
                                            "eoComponentActualId、eoId", "【API:eoComponentActualBatchUpdate】"));
        }

        // 第三步，判断eoComponentActualId是否有值输入
        List<String> sqlList = new ArrayList<>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date date = new Date();

        List<String> mtEoComponentActualIds = this.customDbRepository.getNextKeys("mt_eo_component_actual_s",
                        dto.getEoComponentActualList().size());
        List<String> mtEoComponentActualCids = this.customDbRepository.getNextKeys("mt_eo_component_actual_cid_s",
                        dto.getEoComponentActualList().size());
        List<String> mtEoComponentActualHisIds = this.customDbRepository.getNextKeys("mt_eo_component_actual_his_s",
                        dto.getEoComponentActualList().size());
        List<String> mtEoComponentActualHisCids = this.customDbRepository
                        .getNextKeys("mt_eo_component_actual_his_cid_s", dto.getEoComponentActualList().size());
        List<String> bomComponentIds =
                        dto.getEoComponentActualList().stream().map(MtEoComponentActualVO27::getBomComponentId)
                                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

        List<String> eoIds = dto.getEoComponentActualList().stream().map(MtEoComponentActualVO27::getEoId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

        List<String> routerStepIds =
                        dto.getEoComponentActualList().stream().map(MtEoComponentActualVO27::getRouterStepId)
                                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

        Map<String, MtBomComponentVO13> bomComponentMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(bomComponentIds)) {
            List<MtBomComponentVO13> bomComponentVO13s =
                            mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIds);
            if (CollectionUtils.isNotEmpty(bomComponentIds)) {
                bomComponentMap = bomComponentVO13s.stream()
                                .collect(Collectors.toMap(MtBomComponentVO13::getBomComponentId, t -> t));
            }
        }

        Map<String, MtEoBom> eoBomMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eoIds)) {
            List<MtEoBom> eoBoms = mtEoBomRepository.eoBomBatchGet(tenantId, eoIds);
            if (CollectionUtils.isNotEmpty(eoBoms)) {
                eoBomMap = eoBoms.stream().collect(Collectors.toMap(MtEoBom::getEoId, t -> t));
            }
        }

        Map<String, MtRouterOperation> operationMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(routerStepIds)) {
            List<MtRouterOperation> operations =
                            mtRouterOperationRepository.routerOperationBatchGet(tenantId, routerStepIds);
            if (CollectionUtils.isNotEmpty(operations)) {
                operationMap = operations.stream()
                                .collect(Collectors.toMap(MtRouterOperation::getRouterStepId, t -> t));
            }
        }

        int index = 0;
        for (MtEoComponentActualVO27 vo27 : dto.getEoComponentActualList()) {
            MtEoComponentActual actual = new MtEoComponentActual();
            MtEoComponentActualHis actualHis = new MtEoComponentActualHis();
            if (StringUtils.isEmpty(vo27.getEoComponentActualId())) {
                // eoId
                actual.setEoId(vo27.getEoId());
                // bpmComponentId
                actual.setBomComponentId(
                                StringUtils.isNoneEmpty(vo27.getBomComponentId()) ? vo27.getBomComponentId() : "");

                // materialId
                if (StringUtils.isNotEmpty(vo27.getMaterialId())) {
                    actual.setMaterialId(vo27.getMaterialId());
                } else {
                    if (StringUtils.isNotEmpty(vo27.getBomComponentId())) {
                        MtBomComponentVO13 componentVO13 = bomComponentMap.get(vo27.getBomComponentId());
                        if (null != componentVO13) {
                            actual.setMaterialId(componentVO13.getMaterialId());
                        }
                    } else {
                        throw new MtException("MT_ORDER_0032",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032",
                                                        "ORDER", "bomComponentId、materialId",
                                                        "【API:eoComponentActualBatchUpdate】"));
                    }
                }

                // componentType
                if (StringUtils.isNotEmpty(vo27.getComponentType())) {
                    actual.setComponentType(vo27.getComponentType());
                } else {
                    if (StringUtils.isNotEmpty(vo27.getBomComponentId())) {
                        MtBomComponentVO13 componentVO13 = bomComponentMap.get(vo27.getBomComponentId());
                        if (null != componentVO13) {
                            actual.setComponentType(componentVO13.getBomComponentType());
                        }
                    } else {
                        actual.setComponentType("ASSEMBLING");
                    }
                }

                // bomId
                actual.setBomId(vo27.getBomId());
                if (null == vo27.getBomId()) {
                    MtEoBom mtEoBom = eoBomMap.get(vo27.getEoId());
                    if (null != mtEoBom) {
                        actual.setBomId(mtEoBom.getBomId());
                    } else {
                        actual.setBomId("");
                    }
                }

                // routerStepId
                actual.setRouterStepId(StringUtils.isNotEmpty(vo27.getRouterStepId()) ? vo27.getRouterStepId() : "");

                // operationId
                if (StringUtils.isNotEmpty(vo27.getOperationId())) {
                    actual.setOperationId(vo27.getOperationId());
                } else {
                    if (StringUtils.isNotEmpty(vo27.getRouterStepId())) {
                        MtRouterOperation operation = operationMap.get(vo27.getRouterStepId());
                        if (null != operation) {
                            actual.setOperationId(operation.getOperationId());
                        }
                    } else {
                        actual.setOperationId("");
                    }
                }
                actual.setTenantId(tenantId);
                actual = mtEoComponentActualMapper.selectOne(actual);
            } else {
                actual.setEoComponentActualId(vo27.getEoComponentActualId());
                actual.setEoId(vo27.getEoId());
                actual.setMaterialId(vo27.getMaterialId());
                actual.setBomComponentId(vo27.getBomComponentId());
                actual.setComponentType(vo27.getComponentType());
                actual.setOperationId(vo27.getOperationId());
                actual.setRouterStepId(vo27.getRouterStepId());
                actual.setBomId(vo27.getBomId());
                actual.setTenantId(tenantId);
                actual = mtEoComponentActualMapper.selectOne(actual);
                if (null == actual) {
                    throw new MtException("MT_ORDER_0101", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0101", "ORDER", "【API:eoComponentActualBatchUpdate】"));
                }
            }
            // 新增或者更新
            if (null != actual) {
                // 更新
                Double oldAssembleQty = actual.getAssembleQty();
                // assembleQty
                if (null != vo27.getAssembleQty()) {
                    actual.setAssembleQty(vo27.getAssembleQty());
                }
                if (null != vo27.getTrxAssembleQty()) {
                    actual.setAssembleQty(BigDecimal.valueOf(actual.getAssembleQty())
                                    .add(BigDecimal.valueOf(vo27.getTrxAssembleQty())).doubleValue());
                }


                Double oldScrappedQty = actual.getScrappedQty();
                // scrappedQty
                if (null != vo27.getScrappedQty()) {
                    actual.setScrappedQty(vo27.getScrappedQty());
                }
                if (null != vo27.getTrxScrappedQty()) {
                    actual.setScrappedQty(BigDecimal.valueOf(actual.getScrappedQty())
                                    .add(BigDecimal.valueOf(vo27.getTrxScrappedQty())).doubleValue());
                }

                actual.setAssembleExcessFlag(vo27.getAssembleExcessFlag());
                actual.setAssembleRouterType(vo27.getAssembleRouterType());
                actual.setSubstituteFlag(vo27.getSubstituteFlag());

                if (null != vo27.getActualFirstTime()) {
                    actual.setActualFirstTime(vo27.getActualFirstTime());
                } else {
                    if (BigDecimal.valueOf(oldAssembleQty).compareTo(BigDecimal.ZERO) == 0
                                    && null != vo27.getAssembleQty()
                                    && BigDecimal.valueOf(vo27.getAssembleQty()).compareTo(BigDecimal.ZERO) != 0
                                    || null != vo27.getTrxAssembleQty()
                                                    && BigDecimal.valueOf(vo27.getTrxAssembleQty())
                                                                    .compareTo(BigDecimal.ZERO) != 0
                                                    && null == actual.getActualFirstTime()) {
                        actual.setActualFirstTime(date);
                    }
                    if (BigDecimal.valueOf(oldAssembleQty).compareTo(BigDecimal.ZERO) == 0
                                    && null != vo27.getAssembleQty()
                                    && BigDecimal.valueOf(vo27.getAssembleQty()).compareTo(BigDecimal.ZERO) != 0
                                    || null != vo27.getTrxAssembleQty()
                                                    && BigDecimal.valueOf(vo27.getTrxAssembleQty())
                                                                    .compareTo(BigDecimal.ZERO) != 0
                                                    && null != actual.getActualFirstTime()) {
                        actual.setActualFirstTime(actual.getActualFirstTime());
                    }

                    if (BigDecimal.valueOf(oldAssembleQty).compareTo(BigDecimal.ZERO) != 0
                                    || null == vo27.getAssembleQty() && null == vo27.getTrxAssembleQty()
                                    || null != vo27.getAssembleQty() && BigDecimal.valueOf(vo27.getAssembleQty())
                                                    .compareTo(BigDecimal.ZERO) == 0
                                    || null != vo27.getTrxAssembleQty() && BigDecimal.valueOf(vo27.getTrxAssembleQty())
                                                    .compareTo(BigDecimal.ZERO) == 0) {
                        actual.setActualFirstTime(actual.getActualFirstTime());
                    }

                }

                // actualLastTime
                if (null != vo27.getActualLastTime()) {
                    actual.setActualLastTime(vo27.getActualLastTime());
                } else {
                    if (null != vo27.getAssembleQty()
                                    && BigDecimal.valueOf(vo27.getAssembleQty()).compareTo(BigDecimal.ZERO) != 0
                                    || null != vo27.getTrxAssembleQty() && BigDecimal.valueOf(vo27.getTrxAssembleQty())
                                                    .compareTo(BigDecimal.ZERO) != 0) {
                        actual.setActualLastTime(date);
                    }
                    if (null == vo27.getAssembleQty() && null == vo27.getTrxAssembleQty()
                                    || null != vo27.getAssembleQty() && BigDecimal.valueOf(vo27.getAssembleQty())
                                                    .compareTo(BigDecimal.ZERO) == 0
                                    || null != vo27.getTrxAssembleQty() && BigDecimal.valueOf(vo27.getTrxAssembleQty())
                                                    .compareTo(BigDecimal.ZERO) == 0) {
                        actual.setActualLastTime(actual.getActualLastTime());
                    }
                }
                actual.setTenantId(tenantId);
                actual.setCid(Long.valueOf(mtEoComponentActualCids.get(index)));
                actual.setLastUpdatedBy(userId);
                actual.setLastUpdateDate(date);
                sqlList.addAll(customDbRepository.getUpdateSql(actual));

                actualHis.setTrxAssembleQty(BigDecimal.valueOf(actual.getAssembleQty())
                                .subtract(BigDecimal.valueOf(oldAssembleQty)).doubleValue());
                actualHis.setTrxScrappedQty(BigDecimal.valueOf(actual.getScrappedQty())
                                .subtract(BigDecimal.valueOf(oldScrappedQty)).doubleValue());

            } else {
                // 新增
                actual = new MtEoComponentActual();
                actual.setEoId(vo27.getEoId());

                // MATERIAL_ID
                if (StringUtils.isNotEmpty(vo27.getMaterialId())) {
                    actual.setMaterialId(vo27.getMaterialId());
                } else {
                    MtBomComponentVO13 componentVO13 = bomComponentMap.get(vo27.getBomComponentId());
                    if (null == componentVO13 || StringUtils.isEmpty(componentVO13.getMaterialId())) {
                        throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0148", "ORDER", "【API:eoComponentActualBatchUpdate】"));
                    }
                    actual.setMaterialId(componentVO13.getMaterialId());
                }

                // OPERATION_ID
                if (StringUtils.isNotEmpty(vo27.getOperationId())) {
                    actual.setOperationId(vo27.getOperationId());
                } else {
                    if (StringUtils.isNotEmpty(vo27.getRouterStepId())) {
                        MtRouterOperation operation = operationMap.get(vo27.getRouterStepId());
                        if (null == operation || StringUtils.isEmpty(operation.getOperationId())) {
                            throw new MtException("MT_ORDER_0149", mtErrorMessageRepository.getErrorMessageWithModule(
                                            tenantId, "MT_ORDER_0149", "ORDER", "【API:eoComponentActualBatchUpdate】"));
                        }
                        actual.setOperationId(operation.getOperationId());
                    } else {
                        actual.setOperationId("");
                    }
                }

                // ASSEMBLE_QTY
                if (null != vo27.getAssembleQty()) {
                    actual.setAssembleQty(vo27.getAssembleQty());
                } else if (null != vo27.getTrxAssembleQty()) {
                    actual.setAssembleQty(vo27.getTrxAssembleQty());
                } else {
                    actual.setAssembleQty(0.0D);
                }

                // SCRAPPED_QTY
                if (null != vo27.getScrappedQty()) {
                    actual.setScrappedQty(vo27.getScrappedQty());
                } else if (null != vo27.getTrxScrappedQty()) {
                    actual.setScrappedQty(vo27.getTrxScrappedQty());
                } else {
                    actual.setScrappedQty(0.0D);
                }

                // COMPONENT_TYPE
                if (StringUtils.isNotEmpty(vo27.getComponentType())) {
                    actual.setComponentType(vo27.getComponentType());
                } else {
                    if (StringUtils.isNotEmpty(vo27.getBomComponentId())) {
                        MtBomComponentVO13 componentVO13 = bomComponentMap.get(vo27.getBomComponentId());
                        if (null == componentVO13 || StringUtils.isEmpty(componentVO13.getBomComponentType())) {
                            throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(
                                            tenantId, "MT_ORDER_0148", "ORDER", "【API:eoComponentActualBatchUpdate】"));
                        }
                        actual.setComponentType(componentVO13.getBomComponentType());
                    } else {
                        actual.setComponentType("ASSEMBLING");
                    }
                }

                // BOM_COMPONENT_ID
                actual.setBomComponentId(vo27.getBomComponentId());
                actual.setBomId(vo27.getBomId());
                if (null == vo27.getBomId()) {
                    MtEoBom mtEoBom = eoBomMap.get(actual.getEoId());
                    if (null != mtEoBom) {
                        actual.setBomId(mtEoBom.getBomId());
                    }
                }

                // ROUTER_STEP_ID
                actual.setRouterStepId(vo27.getRouterStepId());

                // ASSEMBLE_EXCESS_FLAG
                actual.setAssembleExcessFlag(vo27.getAssembleExcessFlag());
                if (null == vo27.getAssembleExcessFlag()) {
                    if (null != vo27.getBomComponentId()) {
                        actual.setAssembleExcessFlag("N");
                    } else {
                        actual.setAssembleExcessFlag("Y");
                    }
                }
                // ASSEMBLE_ROUTER_TYPE
                actual.setAssembleRouterType(vo27.getAssembleRouterType());

                // SUBSTITUTE_FLAG
                actual.setSubstituteFlag(vo27.getSubstituteFlag());
                if (null == vo27.getSubstituteFlag()) {
                    if (StringUtils.isEmpty(vo27.getBomComponentId())
                                    || StringUtils.isNotEmpty(vo27.getBomComponentId())
                                                    && StringUtils.isEmpty(vo27.getMaterialId())) {
                        actual.setSubstituteFlag("N");
                    }
                    if (StringUtils.isNotEmpty(vo27.getBomComponentId())
                                    && StringUtils.isNotEmpty(vo27.getMaterialId())) {
                        MtBomComponentVO13 vo13 = bomComponentMap.get(vo27.getBomComponentId());
                        if (null == vo13 || StringUtils.isEmpty(vo13.getMaterialId())) {
                            throw new MtException("MT_ORDER_0148", mtErrorMessageRepository.getErrorMessageWithModule(
                                            tenantId, "MT_ORDER_0148", "ORDER", "【API:eoComponentActualBatchUpdate】"));
                        }
                        if (vo13.getMaterialId().equals(vo27.getMaterialId())) {
                            actual.setSubstituteFlag("N");
                        } else {
                            actual.setSubstituteFlag("Y");
                        }
                    }
                }
                // ACTUAL_FIRST_TIME
                if (null != vo27.getActualFirstTime()) {
                    actual.setActualFirstTime(vo27.getActualFirstTime());
                } else {
                    if (null != vo27.getAssembleQty()
                                    && BigDecimal.valueOf(vo27.getAssembleQty()).compareTo(BigDecimal.ZERO) != 0
                                    || null != vo27.getTrxAssembleQty() && BigDecimal.valueOf(vo27.getTrxAssembleQty())
                                                    .compareTo(BigDecimal.ZERO) != 0) {
                        actual.setActualFirstTime(date);
                    } else {
                        actual.setActualFirstTime(null);
                    }
                }

                // ACTUAL_LAST_TIME
                if (null != vo27.getActualLastTime()) {
                    actual.setActualLastTime(vo27.getActualLastTime());
                } else {
                    if (null != vo27.getAssembleQty()
                                    && BigDecimal.valueOf(vo27.getAssembleQty()).compareTo(BigDecimal.ZERO) != 0
                                    || null != vo27.getTrxAssembleQty() && BigDecimal.valueOf(vo27.getTrxAssembleQty())
                                                    .compareTo(BigDecimal.ZERO) != 0) {
                        actual.setActualLastTime(date);
                    } else {
                        actual.setActualLastTime(null);
                    }
                }

                actual.setTenantId(tenantId);
                actual.setEoComponentActualId(mtEoComponentActualIds.get(index));
                actual.setCid(Long.valueOf(mtEoComponentActualCids.get(index)));
                actual.setCreatedBy(userId);
                actual.setLastUpdatedBy(userId);
                actual.setCreationDate(date);
                actual.setLastUpdateDate(date);
                sqlList.addAll(customDbRepository.getInsertSql(actual));

                actualHis.setTrxAssembleQty(actual.getAssembleQty());
                actualHis.setTrxScrappedQty(actual.getScrappedQty());
            }

            // 记录历史
            actualHis.setEoComponentActualId(actual.getEoComponentActualId());
            actualHis.setEoId(actual.getEoId());
            actualHis.setMaterialId(actual.getMaterialId());
            actualHis.setOperationId(actual.getOperationId());
            actualHis.setAssembleQty(actual.getAssembleQty());
            actualHis.setScrappedQty(actual.getScrappedQty());
            actualHis.setComponentType(actual.getComponentType());
            actualHis.setBomComponentId(actual.getBomComponentId());
            actualHis.setBomId(actual.getBomId());
            actualHis.setRouterStepId(actual.getRouterStepId());
            actualHis.setAssembleExcessFlag(actual.getAssembleExcessFlag());
            actualHis.setAssembleRouterType(actual.getAssembleRouterType());
            actualHis.setSubstituteFlag(actual.getSubstituteFlag());
            actualHis.setActualFirstTime(actual.getActualFirstTime());
            actualHis.setActualLastTime(actual.getActualLastTime());
            actualHis.setEventId(dto.getEventId());
            actualHis.setTenantId(tenantId);
            actualHis.setEoComponentActualHisId(mtEoComponentActualHisIds.get(index));
            actualHis.setCid(Long.valueOf(mtEoComponentActualHisCids.get(index)));
            actualHis.setCreationDate(date);
            actualHis.setCreatedBy(userId);
            actualHis.setLastUpdateDate(date);
            actualHis.setLastUpdatedBy(userId);
            sqlList.addAll(customDbRepository.getInsertSql(actualHis));
            index++;
        }
        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoComponentBatchAssemble(Long tenantId, MtEoComponentActualVO31 dto) {
        String apiName = "【API:eoComponentBatchAssemble】";
        Map<String, String> mtBomComponentMap = new HashMap<>();

        // checkComponentTypeFlag输入不为N时，需要检验装配清单类型
        boolean needCheckComponentTypeFlag = !MtBaseConstants.NO.equalsIgnoreCase(dto.getCheckComponentTypeFlag());
        if (needCheckComponentTypeFlag) {
            List<String> bomComponentIds = dto.getEoInfo().stream().map(MtEoComponentActualVO30::getMaterialInfo)
                            .flatMap(Collection::stream).map(MtEoComponentActualVO29::getBomComponentId).distinct()
                            .filter(MtIdHelper::isIdNotNull).collect(Collectors.toList());
            List<MtBomComponent> mtBomComponents =
                            mtBomComponentRepository.selectBomComponentByBomComponentIds(tenantId, bomComponentIds);
            mtBomComponentMap = mtBomComponents.stream().collect(
                            Collectors.toMap(MtBomComponent::getBomComponentId, MtBomComponent::getBomComponentType));

        }
        List<MtEoComponentActualVO27> eoComponentActualList = new ArrayList<>();
        for (MtEoComponentActualVO30 enInfo : dto.getEoInfo()) {

            if (MtIdHelper.isIdNull(enInfo.getEoId())) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "eoId", apiName));
            }
            if (StringUtils.isEmpty(enInfo.getOperationAssembleFlag())) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "operationAssembleFlag", apiName));
            }
            boolean operationAssembleFlag = enInfo.getOperationAssembleFlag().equalsIgnoreCase(MtBaseConstants.YES);
            for (MtEoComponentActualVO29 materialInfo : enInfo.getMaterialInfo()) {
                if (materialInfo.getTrxAssembleQty() == null) {
                    throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0001", "ORDER", "trxAssembleQty", apiName));
                }
                if (BigDecimal.valueOf(materialInfo.getTrxAssembleQty()).compareTo(BigDecimal.ZERO) <= 0) {
                    throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0059", "ORDER", "trxAssembleQty", apiName));
                }
                boolean assembleExcessFlag = MtBaseConstants.YES.equalsIgnoreCase(materialInfo.getAssembleExcessFlag());

                MtEoComponentActualVO27 mtEoComponentActualVO27 = new MtEoComponentActualVO27();
                if (operationAssembleFlag && assembleExcessFlag) {
                    if (MtIdHelper.isIdNull(materialInfo.getMaterialId())) {
                        throw new MtException("MT_ORDER_0106", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "MT_ORDER_0106", "ORDER", apiName));
                    }
                    if (MtIdHelper.isIdNull(dto.getOperationId())) {
                        throw new MtException("MT_ORDER_0108", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "MT_ORDER_0108", "ORDER", apiName));
                    }
                    if (MtIdHelper.isIdNull(enInfo.getRouterStepId())) {

                        throw new MtException("MT_ORDER_0181", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0181", "ORDER", enInfo.getEoId().toString(), apiName));
                    }
                    mtEoComponentActualVO27.setTrxAssembleQty(materialInfo.getTrxAssembleQty());
                    mtEoComponentActualVO27.setAssembleExcessFlag(MtBaseConstants.YES);
                    mtEoComponentActualVO27.setBomComponentId(MtBaseConstants.LONG_SPECIAL);
                    mtEoComponentActualVO27.setRouterStepId(enInfo.getRouterStepId());
                } else if (operationAssembleFlag) {
                    if (MtIdHelper.isIdNull(materialInfo.getBomComponentId())) {
                        throw new MtException("MT_ORDER_0180", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0180", "ORDER", enInfo.getEoId().toString(), apiName));
                    }
                    if (MtIdHelper.isIdNull(enInfo.getRouterStepId())) {
                        throw new MtException("MT_ORDER_0181", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0181", "ORDER", enInfo.getEoId().toString(), apiName));
                    }
                    if (needCheckComponentTypeFlag && !"ASSEMBLING"

                                    .equalsIgnoreCase(mtBomComponentMap.get(materialInfo.getBomComponentId()))) {
                        throw new MtException("MT_ORDER_0105", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0105", "ORDER", "ASSEMBLING", apiName));


                    }
                    mtEoComponentActualVO27.setBomComponentId(materialInfo.getBomComponentId());
                    mtEoComponentActualVO27.setRouterStepId(enInfo.getRouterStepId());
                    mtEoComponentActualVO27.setTrxAssembleQty(materialInfo.getTrxAssembleQty());
                    mtEoComponentActualVO27.setAssembleExcessFlag(MtBaseConstants.NO);
                } else if (!assembleExcessFlag) {
                    if (MtIdHelper.isIdNull(materialInfo.getBomComponentId())) {
                        throw new MtException("MT_ORDER_0180", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0180", "ORDER", enInfo.getEoId().toString(), apiName));
                    }
                    if (needCheckComponentTypeFlag && !"ASSEMBLING"

                                    .equalsIgnoreCase(mtBomComponentMap.get(materialInfo.getBomComponentId()))) {
                        throw new MtException("MT_ORDER_0105", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "MT_ORDER_0105", "ORDER", apiName));

                    }
                    mtEoComponentActualVO27.setBomComponentId(materialInfo.getBomComponentId());
                    mtEoComponentActualVO27.setTrxAssembleQty(materialInfo.getTrxAssembleQty());
                    mtEoComponentActualVO27.setAssembleExcessFlag(MtBaseConstants.NO);
                    mtEoComponentActualVO27.setRouterStepId(MtBaseConstants.LONG_SPECIAL);

                } else if (assembleExcessFlag) {
                    if (MtIdHelper.isIdNull(materialInfo.getMaterialId())) {
                        throw new MtException("MT_ORDER_0106", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "MT_ORDER_0106", "ORDER", apiName));
                    }
                    mtEoComponentActualVO27.setTrxAssembleQty(materialInfo.getTrxAssembleQty());
                    mtEoComponentActualVO27.setAssembleExcessFlag(MtBaseConstants.YES);
                    mtEoComponentActualVO27.setBomComponentId(MtBaseConstants.LONG_SPECIAL);
                    mtEoComponentActualVO27.setRouterStepId(MtBaseConstants.LONG_SPECIAL);
                }
                mtEoComponentActualVO27.setSubstituteFlag(materialInfo.getSubstituteFlag());
                mtEoComponentActualVO27.setBomId(enInfo.getBomId());
                mtEoComponentActualVO27.setEoId(enInfo.getEoId());
                mtEoComponentActualVO27.setMaterialId(materialInfo.getMaterialId());
                mtEoComponentActualVO27.setComponentType("ASSEMBLING");
                mtEoComponentActualVO27.setAssembleRouterType(enInfo.getAssembleRouterType());
                eoComponentActualList.add(mtEoComponentActualVO27);
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
            eventCreateVO.setShiftDate(dto.getShiftDate() == null ? null
                            : Date.from(dto.getShiftDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            eventCreateVO.setShiftCode(dto.getShiftCode());
            dto.setEventId(mtEventRepository.eventCreate(tenantId, eventCreateVO));
        }

        if (CollectionUtils.isNotEmpty(eoComponentActualList)) {
            // 调用之前对参数按唯一性去重，同时汇总数量
            Map<MtEoComponentActualVO27, List<MtEoComponentActualVO27>> eoComponentActualMap =
                            eoComponentActualList.stream()
                                            .collect(Collectors.groupingBy(t -> new MtEoComponentActualVO27(t.getEoId(),
                                                            t.getMaterialId(), t.getComponentType(),
                                                            t.getBomComponentId(), t.getBomId(), t.getRouterStepId())));

            List<MtEoComponentActualVO27> finalDataList = new ArrayList<>(eoComponentActualMap.size());
            for (Entry<MtEoComponentActualVO27, List<MtEoComponentActualVO27>> eoComponentActualEntry : eoComponentActualMap
                            .entrySet()) {
                List<MtEoComponentActualVO27> eoComponentActualEntryValue = eoComponentActualEntry.getValue();
                BigDecimal trxAssembleQty = eoComponentActualEntryValue.stream().collect(
                                CollectorsUtil.summingBigDecimal(t -> BigDecimal.valueOf(t.getTrxAssembleQty())));

                MtEoComponentActualVO27 finalData = new MtEoComponentActualVO27();
                finalData.setTrxAssembleQty(trxAssembleQty.doubleValue());
                finalData.setAssembleExcessFlag(eoComponentActualEntryValue.get(0).getAssembleExcessFlag());
                finalData.setBomComponentId(eoComponentActualEntryValue.get(0).getBomComponentId());
                finalData.setRouterStepId(eoComponentActualEntryValue.get(0).getRouterStepId());
                finalData.setSubstituteFlag(eoComponentActualEntryValue.get(0).getSubstituteFlag());
                finalData.setBomId(eoComponentActualEntryValue.get(0).getBomId());
                finalData.setEoId(eoComponentActualEntryValue.get(0).getEoId());
                finalData.setMaterialId(eoComponentActualEntryValue.get(0).getMaterialId());
                finalData.setComponentType(eoComponentActualEntryValue.get(0).getComponentType());
                finalData.setAssembleRouterType(eoComponentActualEntryValue.get(0).getAssembleRouterType());
                finalDataList.add(finalData);
            }

            MtEoComponentActualVO26 mtEoComponentActualVO26 = new MtEoComponentActualVO26();
            mtEoComponentActualVO26.setEoComponentActualList(finalDataList);
            mtEoComponentActualVO26.setEventId(dto.getEventId());
            mtEoComponentActualVO26.setOperationId(dto.getOperationId());

            // 更新执行作业装配移除实绩并记录历史
            self().eoComponentActualBatchUpdate(tenantId, mtEoComponentActualVO26);
        }
    }

    @Override
    public List<MtEoComponentActualVO33> eoComponentAssembleLocatorBatchGet(Long tenantId,
                    List<MtEoComponentActualVO> inputList) {
        String apiName = "【API:eoComponentAssembleLocatorBatchGet】";
        if (CollectionUtils.isEmpty(inputList)) {
            return Collections.emptyList();
        }

        // 入參校驗
        List<String> eoIds = inputList.stream().map(MtEoComponentActualVO::getEoId).filter(MtIdHelper::isIdNotNull)
                        .collect(toList());
        if (eoIds.size() != inputList.size()) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", apiName));
        }

        List<String> materialIds = inputList.stream().map(MtEoComponentActualVO::getMaterialId)
                        .filter(MtIdHelper::isIdNotNull).collect(toList());
        if (materialIds.size() != inputList.size()) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "materialId", apiName));
        }

        eoIds = eoIds.stream().distinct().collect(toList());
        List<MtEo> eoList = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
        if (eoList.size() != eoIds.size()) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", apiName));
        }

        List<MtEoComponentActualVO33> resutList = new ArrayList<>(inputList.size());

        Map<String, MtEo> eoMap = eoList.stream().collect(toMap(MtEo::getEoId, t -> t));

        // 获取roRouter
        List<MtEoRouter> eoRouterList = mtEoRouterRepository.eoRouterBatchGet(tenantId, eoIds);
        Map<String, String> eoRouterIdMap =
                        eoRouterList.stream().collect(toMap(MtEoRouter::getEoId, MtEoRouter::getRouterId));

        // 获取eoBom
        List<MtEoBom> eoBomList = mtEoBomRepository.eoBomBatchGet(tenantId, eoIds);
        Map<String, String> eoBomIdMap = eoBomList.stream().collect(toMap(MtEoBom::getEoId, MtEoBom::getBomId));

        List<MtRouterOpComponentVO3> routerOpConditionList = new ArrayList<>(inputList.size());

        // 通过入参下标与MtRouterOpComponentVO3建立映射关系
        Map<Integer, MtRouterOpComponentVO3> indexRouterOpMap = new HashMap<>(inputList.size());


        for (int i = 0; i < inputList.size(); i++) {
            MtEoComponentActualVO item = inputList.get(i);
            String bomId = eoBomIdMap.get(item.getEoId());
            String routerId = eoRouterIdMap.get(item.getEoId());
            if (MtIdHelper.isIdNotNull(bomId) && MtIdHelper.isIdNotNull(routerId)) {
                MtRouterOpComponentVO3 condition = new MtRouterOpComponentVO3();
                condition.setBomId(bomId);
                condition.setMaterialId(item.getMaterialId());
                if (StringUtils.isEmpty(item.getComponentType())) {
                    condition.setComponentType(MtBaseConstants.BOM_COMPONENT_TYPE.ASSEMBLING);
                } else {
                    condition.setComponentType(item.getComponentType());
                }
                condition.setRouterId(routerId);
                condition.setOperationId(item.getOperationId());
                routerOpConditionList.add(condition);
                indexRouterOpMap.put(i, condition);
            }

        }

        Set<Integer> processedIndexSet = new HashSet<>(inputList.size());
        if (routerOpConditionList.size() > 0) {
            // 去重，避免拿到返回结果toMap报错
            List<MtRouterOpComponentVO6> routerOpComponentList = this.mtRouterOperationComponentRepository
                            .operationOrMaterialLimitBomComponentBatchQuery(tenantId, routerOpConditionList);
            if (CollectionUtils.isNotEmpty(routerOpComponentList)) {
                Map<MtRouterOpComponentVO3, MtRouterOpComponentVO6> bomComponentIdMap = routerOpComponentList.stream()
                                .collect(toMap(t -> new MtRouterOpComponentVO3(t.getBomId(), t.getMaterialId(),
                                                t.getComponentType(), t.getRouterId(), t.getOperationId()), t -> t,
                                                (o, n) -> n));

                // 获取bomComponentId
                List<String> bomComponentIds =
                                routerOpComponentList.stream().map(MtRouterOpComponentVO6::getBomComponentIds)
                                                .flatMap(Collection::stream).collect(toList());

                // 根据bomComponentId获取组件信息
                List<MtBomComponentVO13> bomComponentList =
                                mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIds);
                Map<String, MtBomComponentVO13> bomComponentMap =
                                bomComponentList.stream().collect(toMap(MtBomComponentVO13::getBomComponentId, t -> t));
                for (Entry<Integer, MtRouterOpComponentVO3> inputParamEntry : indexRouterOpMap.entrySet()) {
                    MtRouterOpComponentVO3 condition = inputParamEntry.getValue();
                    MtRouterOpComponentVO6 routerOpComponentVO6 = bomComponentIdMap.get(condition);
                    if (routerOpComponentVO6 != null) {
                        // 根据装配清单组件行ID获取发料库位
                        List<String> componentIds = routerOpComponentVO6.getBomComponentIds();
                        List<String> issuedLocatorIds = new ArrayList<>(componentIds.size());
                        componentIds.forEach(i -> {
                            MtBomComponentVO13 bomComponentVO13 = bomComponentMap.get(i);
                            if (bomComponentVO13 != null) {
                                issuedLocatorIds.add(MtFieldsHelper.getOrDefault(bomComponentVO13.getIssuedLocatorId(),
                                                MtBaseConstants.LONG_SPECIAL));
                            }
                        });
                        if (CollectionUtils.isNotEmpty(issuedLocatorIds) && issuedLocatorIds.size() == 1) {
                            Integer inputParamIndex = inputParamEntry.getKey();
                            MtEoComponentActualVO inputParam = inputList.get(inputParamIndex);
                            // 构造返回数据
                            resutList.add(new MtEoComponentActualVO33(inputParam.getEoId(), inputParam.getMaterialId(),
                                            inputParam.getComponentType(), inputParam.getOperationId(),
                                            issuedLocatorIds.get(0)));
                            processedIndexSet.add(inputParamIndex);
                        }

                    }
                }
            }
        }

        if (processedIndexSet.size() == inputList.size()) {
            return resutList;
        }

        // 通过入参下标与MtRouterOpComponentVO3建立映射关系
        Map<Integer, MtPfepInventoryVO> indexPfepInvMap = new HashMap<>(inputList.size());
        List<MtPfepInventoryVO> pfepInventoryVOList = new ArrayList<>(inputList.size() - processedIndexSet.size());
        for (int i = 0; i < inputList.size(); i++) {
            if (!processedIndexSet.contains(i)) {
                MtEoComponentActualVO inputParam = inputList.get(i);
                MtEo eo = eoMap.get(inputParam.getEoId());
                MtPfepInventoryVO queryVO = new MtPfepInventoryVO();
                queryVO.setOrganizationType("PRODUCTIONLINE");
                queryVO.setOrganizationId(eo.getProductionLineId());
                queryVO.setSiteId(eo.getSiteId());
                queryVO.setMaterialId(inputParam.getMaterialId());
                pfepInventoryVOList.add(queryVO);
                indexPfepInvMap.put(i, queryVO);
            }
        }

        // 根据生产指令对应生产线和物料获取物料PFEP属性中组件发料库位
        List<MtPfepInventoryVO3> mtPfepInventoryList = mtPfepInventoryRepository.pfepInventoryBatchGet(tenantId,
                        pfepInventoryVOList, Arrays.asList(MtPfepInventory.FIELD_ISSUED_LOCATOR_ID));
        if (CollectionUtils.isNotEmpty(mtPfepInventoryList)) {
            Map<MtPfepInventoryVO, MtPfepInventoryVO3> pfepInvMap =
                            mtPfepInventoryList.stream()
                                            .collect(toMap(t -> new MtPfepInventoryVO(t.getMaterialId(), t.getSiteId(),
                                                            t.getOrganizationType(), t.getOrganizationId()), t -> t,
                                                            (o, n) -> n));
            for (Entry<Integer, MtPfepInventoryVO> inputParamEntry : indexPfepInvMap.entrySet()) {
                MtPfepInventoryVO3 pfepInv = pfepInvMap.get(inputParamEntry.getValue());
                if (pfepInv != null && MtIdHelper.isIdNotNull(pfepInv.getIssuedLocatorId())) {
                    Integer inputParamIndex = inputParamEntry.getKey();
                    MtEoComponentActualVO inputParam = inputList.get(inputParamIndex);
                    // 构造返回数据
                    resutList.add(new MtEoComponentActualVO33(inputParam.getEoId(), inputParam.getMaterialId(),
                                    inputParam.getComponentType(), inputParam.getOperationId(),
                                    pfepInv.getIssuedLocatorId()));
                    processedIndexSet.add(inputParamIndex);
                }
            }
        }

        if (processedIndexSet.size() == inputList.size()) {
            return resutList;
        }

        // 获取生产线生产属性
        // 通过入参下标与MtRouterOpComponentVO3建立映射关系
        Map<Integer, String> indexProdIdMap = new HashMap<>(inputList.size());
        List<String> prodIds = new ArrayList<>(inputList.size() - processedIndexSet.size());
        for (int i = 0; i < inputList.size(); i++) {
            if (!processedIndexSet.contains(i)) {
                MtEo mtEo = eoMap.get(inputList.get(i).getEoId());
                prodIds.add(mtEo.getProductionLineId());
                indexProdIdMap.put(i, mtEo.getProductionLineId());
            }
        }
        List<MtModProdLineManufacturing> prodLineManufacturingList =
                        mtModProdLineManufacturingRepository.prodLineManufacturingPropertyBatchGet(tenantId, prodIds);
        if (CollectionUtils.isNotEmpty(prodLineManufacturingList)) {
            Map<String, String> prodLineManufacturingMap = prodLineManufacturingList.stream()
                            .collect(toMap(t -> t.getProdLineId(), t -> t.getIssuedLocatorId()));
            for (Entry<Integer, String> inputParamEntry : indexProdIdMap.entrySet()) {
                String issuedLocatorId = prodLineManufacturingMap.get(inputParamEntry.getValue());
                if (MtIdHelper.isIdNotNull(issuedLocatorId)) {
                    Integer inputParamIndex = inputParamEntry.getKey();
                    MtEoComponentActualVO inputParam = inputList.get(inputParamIndex);
                    // 构造返回数据
                    resutList.add(new MtEoComponentActualVO33(inputParam.getEoId(), inputParam.getMaterialId(),
                                    inputParam.getComponentType(), inputParam.getOperationId(), issuedLocatorId));
                    processedIndexSet.add(inputParamIndex);
                }
            }
        }

        if (processedIndexSet.size() == inputList.size()) {
            return resutList;
        }

        for (int i = 0; i < inputList.size(); i++) {
            if (!processedIndexSet.contains(i)) {
                MtEoComponentActualVO inputParam = inputList.get(i);
                // 构造返回数据
                resutList.add(new MtEoComponentActualVO33(inputParam.getEoId(), inputParam.getMaterialId(),
                                inputParam.getComponentType(), inputParam.getOperationId(), null));
            }
        }
        return resutList;
    }
}
