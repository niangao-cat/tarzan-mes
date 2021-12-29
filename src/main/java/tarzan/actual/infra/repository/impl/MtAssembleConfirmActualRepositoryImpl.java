package tarzan.actual.infra.repository.impl;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.SequenceInfo;
import io.tarzan.common.domain.util.*;
import tarzan.actual.domain.entity.MtAssembleConfirmActual;
import tarzan.actual.domain.entity.MtAssembleConfirmActualHis;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.actual.domain.repository.*;
import tarzan.actual.domain.trans.MtAssembleConfirmActualTransMapper;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtAssembleConfirmActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.domain.vo.MtEventVO;
import tarzan.general.domain.vo.MtEventVO1;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.entity.MtMaterialLotHis;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotHisRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO13;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO16;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotHisVO2;
import tarzan.material.domain.entity.MtPfepManufacturing;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtPfepManufacturingRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterOperationComponent;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.*;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtEoBom;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoBomRepository;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtEoVO19;
import tarzan.order.domain.vo.MtEoVO20;
import tarzan.order.domain.vo.MtWorkOrderVO7;
import tarzan.order.domain.vo.MtWorkOrderVO8;

/**
 * 装配确认实绩，指示执行作业组件材料的装配和确认情况 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtAssembleConfirmActualRepositoryImpl extends BaseRepositoryImpl<MtAssembleConfirmActual>
                implements MtAssembleConfirmActualRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtAssembleConfirmActualMapper mtAssembleConfirmActualMapper;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtBomSubstituteRepository mtBomSubstituteRepository;

    @Autowired
    private MtEoBomRepository mtEoBomRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;

    @Autowired
    private MtAssembleGroupActualRepository mtAssembleGroupActualRepository;

    @Autowired
    private MtAssembleProcessActualRepository mtAssembleProcessActualRepository;

    @Autowired
    private MtAssemblePointActualRepository mtAssemblePointActualRepository;

    @Autowired
    private MtRouterOperationComponentRepository mtRouterOperationComponentRepository;

    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private MtMaterialLotHisRepository mtMaterialLotHisRepository;

    @Autowired
    private MtPfepManufacturingRepository mtPfepManufacturingRepository;

    @Autowired
    private MtAssembleConfirmActualHisRepository mtAssembleConfirmActualHisRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtMaterialRepository iMtMaterialService;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtAssembleConfirmActualTransMapper mtAssembleConfirmActualTransMapper;

    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;

    @Override
    public List<String> propertyLimitAssembleConfirmActualQuery(Long tenantId, MtAssembleConfirmActual dto) {
        if (StringUtils.isNotEmpty(dto.getBypassBy()) && !NumberHelper.isDouble(dto.getBypassBy())) {
            return Collections.emptyList();
        }

        if (StringUtils.isNotEmpty(dto.getConfirmedBy()) && !NumberHelper.isDouble(dto.getConfirmedBy())) {
            return Collections.emptyList();
        }

        List<MtAssembleConfirmActual> list =
                        this.mtAssembleConfirmActualMapper.selectAssesbleConfirmActual(tenantId, dto);
        return list.stream().map(MtAssembleConfirmActual::getAssembleConfirmActualId).collect(Collectors.toList());
    }

    /**
     * 批量获取装配确认实绩属性/sen.luo 2018-03-22
     *
     * @param tenantId
     * @param assembleConfirmActualIds
     * @return
     */
    @Override
    public List<MtAssembleConfirmActual> assembleConfirmActualPropertyBatchGet(Long tenantId,
                    List<String> assembleConfirmActualIds) {
        if (CollectionUtils.isEmpty(assembleConfirmActualIds)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleConfirmActualId", "【API:assembleConfirmActualPropertyBatchGet】"));
        }

        return this.mtAssembleConfirmActualMapper.assembleConfirmActualPropertyBatchGet(tenantId,
                        assembleConfirmActualIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtAssembleConfirmActualVO14 assembleConfirmActualUpdate(Long tenantId, MtAssembleConfirmActualHis dto) {
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eventId", "【API:assembleConfirmActualUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getAssembleConfirmActualId()) && StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002", "ASSEMBLE",
                                            "assembleConfirmActualId、eoId", "【API:assembleConfirmActualUpdate】"));
        }

        MtBomComponentVO8 bomComponentVO = null;
        if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
            bomComponentVO = mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (null == bomComponentVO) {
                throw new MtException("MT_ASSEMBLE_0071",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0071",
                                                "ASSEMBLE", dto.getBomComponentId(),
                                                "【API:assembleConfirmActualUpdate】"));
            }
        }

        // Step2判断componentConfirmActuaId是否有值输入
        // 2-a
        List<MtAssembleConfirmActual> list;

        if (StringUtils.isEmpty(dto.getAssembleConfirmActualId())) {
            MtAssembleConfirmActual actual = new MtAssembleConfirmActual();
            actual.setEoId(dto.getEoId());

            if (null == dto.getBomComponentId()) {
                actual.setBomComponentId("");
            } else {
                actual.setBomComponentId(dto.getBomComponentId());
            }

            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                actual.setMaterialId(dto.getMaterialId());
            } else {
                if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
                    actual.setMaterialId(bomComponentVO.getMaterialId());
                } else {
                    throw new MtException("MT_ORDER_0032",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032",
                                                    "ORDER", "bomComponentId、materialId",
                                                    "【API:assembleConfirmActualUpdate】"));
                }
            }

            if (StringUtils.isNotEmpty(dto.getComponentType())) {
                actual.setComponentType(dto.getComponentType());
            } else {
                if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
                    actual.setComponentType(bomComponentVO.getBomComponentType());
                } else {
                    actual.setComponentType("ASSEMBLING");
                }
            }

            if (null == dto.getBomId()) {
                String bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
                if (StringUtils.isEmpty(bomId)) {
                    actual.setBomId("");
                } else {
                    actual.setBomId(bomId);
                }
            } else {
                actual.setBomId(dto.getBomId());
            }

            if (null != dto.getRouterStepId()) {
                actual.setRouterStepId(dto.getRouterStepId());
            } else {
                actual.setRouterStepId("");
            }

            if (StringUtils.isNotEmpty(dto.getOperationId())) {
                actual.setOperationId(dto.getOperationId());
            } else {
                if (StringUtils.isEmpty(dto.getRouterStepId())) {
                    actual.setOperationId("");
                }
            }
            list = this.mtAssembleConfirmActualMapper.selectForEmptyString(tenantId, actual);
        } else {
            MtAssembleConfirmActual actual2 = new MtAssembleConfirmActual();
            actual2.setAssembleConfirmActualId(dto.getAssembleConfirmActualId());
            actual2.setEoId(dto.getEoId());
            actual2.setMaterialId(dto.getMaterialId());
            actual2.setComponentType(dto.getComponentType());
            actual2.setOperationId(dto.getOperationId());
            actual2.setRouterStepId(dto.getRouterStepId());
            actual2.setBomComponentId(dto.getBomComponentId());
            actual2.setBomId(dto.getBomId());
            list = this.mtAssembleConfirmActualMapper.selectForEmptyString(tenantId, actual2);

            if (CollectionUtils.isEmpty(list)) {
                throw new MtException("MT_ASSEMBLE_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0036", "ASSEMBLE", "【API:assembleConfirmActualUpdate】"));
            }
        }

        String assembleConfirmActualId = null;
        String assembleConfirmActualHisId = null;
        if (CollectionUtils.isNotEmpty(list)) {
            // Step3 更新
            MtAssembleConfirmActual t = list.get(0);
            MtAssembleConfirmActualHis his = new MtAssembleConfirmActualHis();

            if (dto.getAssembleExcessFlag() != null) {
                t.setAssembleExcessFlag(dto.getAssembleExcessFlag());
            }
            if (dto.getAssembleRouterType() != null) {
                t.setAssembleRouterType(dto.getAssembleRouterType());
            }
            if (dto.getSubstituteFlag() != null) {
                t.setSubstituteFlag(dto.getSubstituteFlag());
            }
            if (dto.getBypassFlag() != null) {
                t.setBypassFlag(dto.getBypassFlag());
            }
            if (dto.getBypassBy() != null) {
                t.setBypassBy(dto.getBypassBy());
            }
            if (dto.getConfirmedBy() != null) {
                t.setConfirmedBy(dto.getConfirmedBy());
            }
            if (dto.getConfirmFlag() != null) {
                t.setConfirmFlag(dto.getConfirmFlag());
            }
            t.setTenantId(tenantId);
            self().updateByPrimaryKeySelective(t);

            BeanUtils.copyProperties(t, his);
            his.setEventId(dto.getEventId());
            his.setTenantId(tenantId);
            mtAssembleConfirmActualHisRepository.insertSelective(his);

            assembleConfirmActualId = t.getAssembleConfirmActualId();
            assembleConfirmActualHisId = his.getAssembleConfirmActualHisId();
        } else {
            MtAssembleConfirmActual actual = new MtAssembleConfirmActual();
            actual.setEoId(dto.getEoId());

            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                actual.setMaterialId(dto.getMaterialId());
            } else {
                if (null == bomComponentVO || StringUtils.isEmpty(bomComponentVO.getMaterialId())) {
                    throw new MtException("MT_ASSEMBLE_0071",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0071",
                                                    "ASSEMBLE", dto.getBomComponentId(),
                                                    "【API:assembleConfirmActualUpdate】"));
                }
                actual.setMaterialId(bomComponentVO.getMaterialId());
            }

            if (StringUtils.isNotEmpty(dto.getOperationId())) {
                actual.setOperationId(dto.getOperationId());
            } else {
                if (StringUtils.isEmpty(dto.getRouterStepId())) {
                    actual.setOperationId("");
                } else {
                    MtRouterOperation mtRouterOperation =
                                    mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
                    if (null == mtRouterOperation || StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                        throw new MtException("MT_ASSEMBLE_0072", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ASSEMBLE_0072", "ASSEMBLE", "【API:assembleConfirmActualUpdate】"));
                    }
                    actual.setOperationId(mtRouterOperation.getOperationId());
                }
            }

            if (StringUtils.isNotEmpty(dto.getComponentType())) {
                actual.setComponentType(dto.getComponentType());
            } else {
                if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
                    actual.setComponentType(bomComponentVO.getBomComponentType());
                } else {
                    actual.setComponentType("ASSEMBLING");
                }
            }

            if (null != dto.getBomComponentId()) {
                actual.setBomComponentId(dto.getBomComponentId());
            } else {
                actual.setBomComponentId("");
            }

            if (null != dto.getBomId()) {
                actual.setBomId(dto.getBomId());
            } else {
                String bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
                if (StringUtils.isEmpty(bomId)) {
                    actual.setBomId("");
                } else {
                    actual.setBomId(bomId);
                }
            }

            actual.setRouterStepId(dto.getRouterStepId());

            if (null != dto.getAssembleExcessFlag()) {
                actual.setAssembleExcessFlag(dto.getAssembleExcessFlag());
            } else {
                if (null != dto.getBomComponentId()) {
                    actual.setAssembleExcessFlag("N");
                } else {
                    actual.setAssembleExcessFlag("Y");
                }
            }

            actual.setAssembleRouterType(dto.getAssembleRouterType());

            if (null != dto.getSubstituteFlag()) {
                actual.setSubstituteFlag(dto.getSubstituteFlag());
            } else {
                if (StringUtils.isEmpty(dto.getBomComponentId())) {
                    actual.setSubstituteFlag("N");
                } else if (null == dto.getMaterialId()) {
                    actual.setSubstituteFlag("N");
                } else {
                    if (bomComponentVO.getMaterialId().equals(dto.getMaterialId())) {
                        actual.setSubstituteFlag("N");
                    } else {
                        actual.setSubstituteFlag("Y");
                    }
                }
            }

            actual.setBypassFlag(dto.getBypassFlag());
            actual.setBypassBy(dto.getBypassBy());
            actual.setConfirmedBy(dto.getConfirmedBy());
            actual.setConfirmFlag(dto.getConfirmFlag());
            actual.setTenantId(tenantId);
            self().insertSelective(actual);

            MtAssembleConfirmActualHis his = new MtAssembleConfirmActualHis();
            BeanUtils.copyProperties(actual, his);
            his.setTenantId(tenantId);
            his.setEventId(dto.getEventId());
            mtAssembleConfirmActualHisRepository.insertSelective(his);

            assembleConfirmActualId = actual.getAssembleConfirmActualId();
            assembleConfirmActualHisId = his.getAssembleConfirmActualHisId();
        }

        MtAssembleConfirmActualVO14 result = new MtAssembleConfirmActualVO14();
        result.setAssembleConfirmActualHisId(assembleConfirmActualHisId);
        result.setAssembleConfirmActualId(assembleConfirmActualId);
        return result;
    }

    /**
     * materialLimitAssembleActualQuery-获取指定物料的装配实绩
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Override
    public List<MtAssembleConfirmActualVO3> materialLimitAssembleActualQuery(Long tenantId,
                    MtAssembleConfirmActualVO2 dto) {
        return mtAssembleConfirmActualMapper.materialLimitAssembleActualQuery(tenantId, dto);
    }

    @Override
    public MtAssembleConfirmActual assembleConfirmActualPropertyGet(Long tenantId, String assembleConfirmActualId) {
        // Step1
        if (StringUtils.isEmpty(assembleConfirmActualId)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleConfirmActualId", "【API:assembleConfirmActualPropertyGet】"));
        }

        MtAssembleConfirmActual mtAssembleConfirmActual = new MtAssembleConfirmActual();
        mtAssembleConfirmActual.setTenantId(tenantId);
        mtAssembleConfirmActual.setAssembleConfirmActualId(assembleConfirmActualId);
        return this.mtAssembleConfirmActualMapper.selectOne(mtAssembleConfirmActual);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoComponentAssembleConfirm(Long tenantId, MtAssembleConfirmActualVO dto) {
        // Step 1验证输入参数的合规性
        if (StringUtils.isEmpty(dto.getAssembleConfirmActualId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleConfirmActualId", "【API:eoComponentAssembleConfirm】"));
        }
        // Step 2获取事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        BeanUtils.copyProperties(dto, eventCreateVO);
        eventCreateVO.setEventTypeCode("ASSEMBLE_ACTUAL_CONFIRM");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // Step 3更新装配确认实绩assembleConfirmActualUpdate
        MtAssembleConfirmActualHis his = new MtAssembleConfirmActualHis();
        his.setAssembleConfirmActualId(dto.getAssembleConfirmActualId());
        his.setConfirmFlag("Y");
        if (null == dto.getConfirmBy()) {
            his.setConfirmedBy(DetailsHelper.getUserDetails().getUserId().toString());
        } else {
            his.setConfirmedBy(dto.getConfirmBy());
        }
        his.setEventId(eventId);
        assembleConfirmActualUpdate(tenantId, his);
    }

    /**
     * eoUnconfirmedComponentQuery-获取执行作业未确认的组件
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    @Override
    public List<MtAssembleConfirmActualVO4> eoUnconfirmedComponentQuery(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoUnconfirmedComponentQuery】"));
        }
        return mtAssembleConfirmActualMapper.eoUnconfirmedComponentQuery(tenantId, eoId);
    }

    /**
     * eoBypassedComponentQuery-获取执行作业被放行的组件
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    @Override
    public List<MtAssembleConfirmActualVO4> eoBypassedComponentQuery(Long tenantId, MtAssembleConfirmActualVO5 dto) {
        if (dto == null || StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoBypassedComponentQuery】"));
        }
        return mtAssembleConfirmActualMapper.eoBypassedComponentQuery(tenantId, dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoComponentAssembleConfirmCancel(Long tenantId, MtAssembleConfirmActualVO dto) {
        // Step 1验证输入参数的合规性
        if (StringUtils.isEmpty(dto.getAssembleConfirmActualId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleConfirmActualId", "【API:eoComponentAssembleConfirmCancel】"));
        }
        MtAssembleConfirmActual actual = assembleConfirmActualPropertyGet(tenantId, dto.getAssembleConfirmActualId());
        if (null == actual || !"Y".equals(actual.getConfirmFlag())) {
            throw new MtException("MT_ASSEMBLE_0040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0040", "ASSEMBLE", "【API:eoComponentAssembleConfirmCancel】"));

        }

        // Step 2获取事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        BeanUtils.copyProperties(dto, eventCreateVO);
        eventCreateVO.setEventTypeCode("ASSEMBLE_ACTUAL_CONFIRM_CANCEL");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // Step 3更新装配确认实绩assembleConfirmActualUpdate
        MtAssembleConfirmActualHis his = new MtAssembleConfirmActualHis();
        his.setAssembleConfirmActualId(dto.getAssembleConfirmActualId());
        his.setConfirmFlag("N");
        his.setConfirmedBy("");
        his.setEventId(eventId);
        assembleConfirmActualUpdate(tenantId, his);
    }

    @Override
    public void eoComponentIsConfirmedValidate(Long tenantId, MtAssembleConfirmActualVO6 dto) {
        // Step 1验证输入参数的合规性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoComponentIsConfirmedValidate】"));
        }
        if (StringUtils.isEmpty(dto.getBomComponentId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "bomComponentId", "【API:eoComponentIsConfirmedValidate】"));
        }
        // Step 2获取指定执行作业指定组件的装配实绩
        MtAssembleConfirmActual actual = new MtAssembleConfirmActual();
        actual.setEoId(dto.getEoId());
        actual.setBomComponentId(dto.getBomComponentId());
        actual.setRouterStepId(dto.getRouterStepId());
        List<String> assembleConfirmActualIds = propertyLimitAssembleConfirmActualQuery(tenantId, actual);
        if (CollectionUtils.isEmpty(assembleConfirmActualIds)) {
            throw new MtException("MT_ASSEMBLE_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0043", "ASSEMBLE", "【API:eoComponentIsConfirmedValidate】"));
        }

        List<MtAssembleConfirmActual> actualList =
                        assembleConfirmActualPropertyBatchGet(tenantId, assembleConfirmActualIds);
        if (actualList.stream().anyMatch(t -> !"Y".equals(t.getConfirmFlag()))) {
            throw new MtException("MT_ASSEMBLE_0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0042", "ASSEMBLE", "【API:eoComponentIsConfirmedValidate】"));
        }
    }

    @Override
    public void eoAssembleMaterialSubstituteValidate(Long tenantId, MtAssembleConfirmActualVO7 dto) {
        // Step 1验证输入参数的合规性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoAssembleMaterialSubstituteValidate】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "materialId", "【API:eoAssembleMaterialSubstituteValidate】"));
        }
        if (StringUtils.isEmpty(dto.getBomComponentId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "bomComponentId", "【API:eoAssembleMaterialSubstituteValidate】"));
        }
        // 1-b判断输入参数bomComponentId是否为输入参数eoId的组件
        String eoBomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
        MtBomComponentVO8 bomComponent =
                        mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());

        if (eoBomId == null || bomComponent == null || !eoBomId.equals(bomComponent.getBomId())) {
            throw new MtException("MT_ASSEMBLE_0023",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0023", "ASSEMBLE",
                                            "eoId", "bomComponentId", "【API:eoAssembleMaterialSubstituteValidate】"));
        }

        // Step 2验证输入物料是否为装配清单行的替代物料
        MtBomSubstituteVO5 bomSubstituteVO = new MtBomSubstituteVO5();
        bomSubstituteVO.setMaterialId(dto.getMaterialId());
        bomSubstituteVO.setBomComponentId(dto.getBomComponentId());
        mtBomSubstituteRepository.bomComponentSubstituteVerify(tenantId, bomSubstituteVO);
    }

    @Override
    public void eoAssembleMaterialExcessVerify(Long tenantId, MtAssembleConfirmActualVO9 dto) {
        // Step 1验证输入参数的合规性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoAssembleMaterialExcessVerify】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "materialId", "【API:eoAssembleMaterialExcessVerify】"));
        }

        // Step 2
        String operationAssembleFlag = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());
        if ("Y".equals(operationAssembleFlag) && StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_ASSEMBLE_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0047", "ASSEMBLE", "【API:eoAssembleMaterialExcessVerify】"));
        }

        // Step 3判断物料是否为执行作业装配清单需求或装配清单替代物料需求
        MtEoVO19 mtEoVO = new MtEoVO19();
        mtEoVO.setEoId(dto.getEoId());
        if ("Y".equals(operationAssembleFlag)) {
            mtEoVO.setRouterStepId(dto.getRouterStepId());
        }
        List<MtEoVO20> list = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        if (list.stream().anyMatch(t -> t.getMaterialId().equals(dto.getMaterialId()))) {
            throw new MtException("MT_ASSEMBLE_0069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0069", "ASSEMBLE", "【API:eoAssembleMaterialExcessVerify】"));
        } else {
            boolean result = false;
            for (MtEoVO20 vo : list) {
                MtBomSubstituteVO5 bomSubstitute = new MtBomSubstituteVO5();
                bomSubstitute.setMaterialId(dto.getMaterialId());
                bomSubstitute.setBomComponentId(vo.getBomComponentId());
                try {
                    mtBomSubstituteRepository.bomComponentSubstituteVerify(tenantId, bomSubstitute);
                    result = true;
                    break;
                } catch (MtException e) {
                    continue;
                }
            }

            if (result) {
                throw new MtException("MT_ASSEMBLE_0070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0070", "ASSEMBLE", "【API:eoAssembleMaterialExcessVerify】"));
            }
        }
    }

    /**
     * eoLimitAssembleActualTraceQuery-获取执行作业装配追溯实绩
     *
     * @author chuang.yang
     * @date 2019/7/1
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.assemble_confirm_actual.view.MtAssembleConfirmActualVO13>
     */
    @Override
    public List<MtAssembleConfirmActualVO13> eoLimitAssembleActualTraceQuery(Long tenantId,
                    MtAssembleConfirmActualVO12 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoLimitAssembleActualTraceQuery】"));
        }

        // 输入为空或不输入则不限制 处理参数格式
        if ("".equals(dto.getEoId())) {
            dto.setEoId(null);
        }
        if ("".equals(dto.getMaterialId())) {
            dto.setMaterialId(null);
        }

        // 2.
        // 根据输入参数从表MT_ASSEMBLE_CONFIRM_ACTUAL和表中MT_ASSEMBLE_PROCESS_ACTUAL获取数据
        List<MtAssembleConfirmActualVO13> traceQueryList =
                        mtAssembleConfirmActualMapper.eoLimitAssembleActualTraceQuery(tenantId, dto);
        if (CollectionUtils.isEmpty(traceQueryList)) {
            return Collections.emptyList();
        }

        // 3. 根据第二步获取的eventId列表, 获取值列表eventRequestId和eventTypeId
        List<String> eventIds = traceQueryList.stream().map(MtAssembleConfirmActualVO13::getEventId)
                        .collect(Collectors.toList());

        List<MtEventVO1> eventList = mtEventRepository.eventBatchGet(tenantId, eventIds);
        if (CollectionUtils.isEmpty(eventList)) {
            return traceQueryList;
        }

        // 根据event数据填充eventTypeId
        Map<String, MtEventVO1> eventMap = eventList.stream().collect(Collectors.toMap(t -> t.getEventId(), t -> t));
        traceQueryList.forEach(t -> {
            MtEventVO1 event = eventMap.get(t.getEventId());
            if (event != null) {
                t.setEventTypeId(event.getEventTypeId());
            }
        });

        // 4. 判断是否继续获取物料批装载EO
        if (!"Y".equals(dto.getIsAllLayer())) {
            // 则结束API输出结果
            return traceQueryList;
        }

        // 根据获取的每一条数据继续获取物料批装载的EO
        // 记录所有请求id影响的事件id
        List<String> requestEventIds = new ArrayList<>();

        for (MtEventVO1 event : eventList) {
            MtEventVO eventVO = new MtEventVO();
            eventVO.setEventRequestId(event.getEventRequestId());
            List<String> tempEventIds = mtEventRepository.propertyLimitEventQuery(tenantId, eventVO);
            if (CollectionUtils.isNotEmpty(tempEventIds)) {
                requestEventIds.addAll(tempEventIds);
            }
        }

        List<String> eoIds = new ArrayList<>();
        for (MtAssembleConfirmActualVO13 confirmActual : traceQueryList) {
            MtMaterialLotHisVO2 mtMaterialLotHisVO2 = new MtMaterialLotHisVO2();
            mtMaterialLotHisVO2.setMaterialLotId(confirmActual.getMaterialLotId());
            List<MtMaterialLotHis> materialLotHisList =
                            mtMaterialLotHisRepository.materialLotHisQuery(tenantId, mtMaterialLotHisVO2);

            if (CollectionUtils.isNotEmpty(materialLotHisList)) {
                for (MtMaterialLotHis his : materialLotHisList) {
                    if (requestEventIds.contains(his.getEventId())) {
                        eoIds.add(his.getEoId());
                    }
                }
            }
        }

        // 若获取eoId列表为空，返回结果
        if (CollectionUtils.isEmpty(eoIds)) {
            return traceQueryList;
        }

        // 去重
        eoIds = eoIds.stream().distinct().collect(Collectors.toList());

        for (String eoId : eoIds) {
            MtAssembleConfirmActualVO12 confirmActualVO12 = new MtAssembleConfirmActualVO12();
            confirmActualVO12.setEoId(eoId);
            confirmActualVO12.setIsAllLayer("Y");
            List<MtAssembleConfirmActualVO13> resultList =
                            this.eoLimitAssembleActualTraceQuery(tenantId, confirmActualVO12);
            if (CollectionUtils.isNotEmpty(resultList)) {
                traceQueryList.addAll(resultList);
            }
        }

        return traceQueryList;
    }

    /**
     * eoComponentAssembleConfirmValidate -执行作业组件装配确认验证
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    @Override
    public void eoComponentAssembleConfirmValidate(Long tenantId, String assembleConfirmActualId) {
        // 1.校验输入
        if (StringUtils.isEmpty(assembleConfirmActualId)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleConfirmActualId", "【API:eoComponentAssembleConfirmValidate】"));
        }
        // 2.根据输入执行作业获取执行作业需求组件信息：
        MtAssembleConfirmActual mtAssembleConfirmActual =
                        assembleConfirmActualPropertyGet(tenantId, assembleConfirmActualId);
        if (null == mtAssembleConfirmActual) {
            throw new MtException("MT_ASSEMBLE_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0036", "ASSEMBLE", "【API:eoComponentAssembleConfirmValidate】"));
        }

        if ("Y".equals(mtAssembleConfirmActual.getAssembleExcessFlag())) {
            // 2.1校验结束通过
            return;
        }
        if ("Y".equals(mtAssembleConfirmActual.getBypassFlag())) {
            // 2.2 装配确认实绩当前状态为放行，不需要进行确认验证
            throw new MtException("MT_ASSEMBLE_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0037", "ASSEMBLE", "【API:eoComponentAssembleConfirmValidate】"));
        }

        // 2.3判断装配确认实绩数量是否满足装配需求
        if (StringUtils.isEmpty(mtAssembleConfirmActual.getBomComponentId())) {
            throw new MtException("MT_ASSEMBLE_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0038", "ASSEMBLE", "【API:eoComponentAssembleConfirmValidate】"));
        }

        // 2.4 获取组件未完成装配的数量
        MtEoComponentActualVO12 mtEoComponentVO12 = new MtEoComponentActualVO12();
        mtEoComponentVO12.setEoId(mtAssembleConfirmActual.getEoId());
        mtEoComponentVO12.setRouterStepId(mtAssembleConfirmActual.getRouterStepId());
        List<MtEoComponentActualVO13> mtEoComponentVO13s =
                        mtEoComponentActualRepository.eoUnassembledComponentQuery(tenantId, mtEoComponentVO12);

        // 若获取结果不为空且获取结果unassembledQty ＞ 0，验证失败
        for (MtEoComponentActualVO13 vo : mtEoComponentVO13s) {
            if (mtAssembleConfirmActual.getBomComponentId().equals(vo.getBomComponentId())
                            && mtAssembleConfirmActual.getMaterialId().equals(vo.getMaterialId())) {

                if (null != vo.getUnassembledQty()
                                && BigDecimal.valueOf(vo.getUnassembledQty()).compareTo(BigDecimal.ZERO) == 1) {
                    throw new MtException("MT_ASSEMBLE_0039",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0039",
                                                    "ASSEMBLE", "【API:eoComponentAssembleConfirmValidate】"));
                }
            }
        }
    }

    /**
     * woAssembleQtyExcessVerify-验证生产指令组件装配数量是否超量
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    @Override
    public void woAssembleQtyExcessVerify(Long tenantId, MtAssembleConfirmActualVO8 dto) {
        // 1验证输入参数的合规性
        if (dto == null || StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "workOrderId", "【API:woAssembleQtyExcessVerify】"));
        }
        if (StringUtils.isEmpty(dto.getBomComponentId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "bomComponentId", "【API:woAssembleQtyExcessVerify】"));
        }
        if (null == dto.getQty()) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "qty", "【API:woAssembleQtyExcessVerify】"));
        }
        // 逻辑新增(获取物料)
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            MtBomComponentVO8 bomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponent == null || StringUtils.isEmpty(bomComponent.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0071",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0071",
                                                "ASSEMBLE", dto.getBomComponentId(),
                                                "【API:woAssembleQtyExcessVerify】"));
            }
            dto.setMaterialId(bomComponent.getMaterialId());
        }
        // 2.是否按工序装配标识
        String operationAssembleFlag = mtWorkOrderRepository.woOperationAssembleFlagGet(tenantId, dto.getWorkOrderId());

        if ("Y".equals(operationAssembleFlag) && StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_ASSEMBLE_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0047", "ASSEMBLE", "【API:woAssembleQtyExcessVerify】"));
        }

        // 第三步
        if ("N".equals(dto.getIssueControlFlag())) {
            // 标识不考虑投料限制
            MtWoComponentActualVO20 mtWoComponentVO20 = new MtWoComponentActualVO20();
            mtWoComponentVO20.setWorkOrderId(dto.getWorkOrderId());
            if ("Y".equals(operationAssembleFlag)) {
                mtWoComponentVO20.setRouterStepId(dto.getRouterStepId());
            }
            List<MtWoComponentActualVO6> mtWoComponentVO6s = mtWorkOrderComponentActualRepository
                            .woUnassembledComponentQuery(tenantId, mtWoComponentVO20);

            List<MtWoComponentActualVO6> collect = mtWoComponentVO6s.stream()
                            .filter(t -> t.getBomComponentId().equals(dto.getBomComponentId())
                                            && t.getMaterialId().equals(dto.getMaterialId()))
                            .collect(Collectors.toList());

            BigDecimal unassembledQty;
            if (CollectionUtils.isNotEmpty(collect)) {
                unassembledQty = collect.stream().collect(
                                CollectorsUtil.summingBigDecimal(c -> c.getUnassembledQty() == null ? BigDecimal.ZERO
                                                : BigDecimal.valueOf(c.getUnassembledQty())));
            } else {
                unassembledQty = BigDecimal.ZERO;
            }

            if (new BigDecimal(dto.getQty().toString()).compareTo(unassembledQty) == 1) {
                throw new MtException("MT_ASSEMBLE_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0068", "ASSEMBLE", "【API:woAssembleQtyExcessVerify】"));
            }
        } else {
            // 为Y或者为空执行路线
            MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
            mtWorkOrderVO7.setWorkOrderId(dto.getWorkOrderId());
            if ("Y".equals(operationAssembleFlag)) {
                mtWorkOrderVO7.setRouterStepId(dto.getRouterStepId());
            }
            // 2.2.1获取生产指令组件和组件需求用量列表
            List<MtWorkOrderVO8> mtWorkOrderVO8s = mtWorkOrderRepository.woComponentQtyQuery(tenantId, mtWorkOrderVO7);

            // 2.2.2获取生产指令或生产指令在指定工艺已装配的组件实绩列表
            MtWoComponentActualVO5 componentV5 = new MtWoComponentActualVO5();
            componentV5.setWorkOrderId(dto.getWorkOrderId());
            componentV5.setRouterStepId(dto.getRouterStepId());
            componentV5.setBomComponentId(dto.getBomComponentId());
            List<MtWoComponentActualVO4> mtWoComponentVO4s = mtWorkOrderComponentActualRepository
                            .componentLimitWoComponentAssembleActualQuery(tenantId, componentV5);

            // 按照bomComponentId、materialId进行汇总
            Map<String, Map<String, BigDecimal>> collect = mtWoComponentVO4s.stream().collect(Collectors.groupingBy(
                            MtWoComponentActualVO4::getBomComponentId,
                            Collectors.groupingBy(MtWoComponentActualVO4::getMaterialId,
                                            CollectorsUtil.summingBigDecimal(c -> BigDecimal
                                                            .valueOf(c.getAssembleQty() == null ? Double.valueOf(0.0D)
                                                                            : c.getAssembleQty())))));

            // 2.2.3获取生产指令物料的投料限制：
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
            if (null == mtWorkOrder) {
                throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0006", "ORDER", "【API:woAssembleQtyExcessVerify】"));
            }

            MtPfepInventoryVO queryVO = new MtPfepInventoryVO();
            queryVO.setMaterialId(dto.getMaterialId());
            queryVO.setOrganizationType("PRODUCTIONLINE");
            queryVO.setSiteId(mtWorkOrder.getSiteId());
            queryVO.setOrganizationId(mtWorkOrder.getProductionLineId());
            MtPfepManufacturing mtPfepManufacturing =
                            mtPfepManufacturingRepository.pfepManufacturingIssueControlGet(tenantId, queryVO);

            if (mtPfepManufacturing == null || StringUtils.isEmpty(mtPfepManufacturing.getIssueControlType())
                            || mtPfepManufacturing.getIssueControlQty() == null) {
                if (mtPfepManufacturing == null) {
                    mtPfepManufacturing = new MtPfepManufacturing();
                }
                mtPfepManufacturing.setIssueControlType("FIX");
                mtPfepManufacturing.setIssueControlQty(Double.valueOf(0.0D));
            }
            // 2.2.5判断数量是否超量
            mtWorkOrderVO8s =
                            mtWorkOrderVO8s.stream().filter(t -> dto.getBomComponentId().equals(t.getBomComponentId()))
                                            .collect(Collectors.toList());
            BigDecimal sumAssembleQty = new BigDecimal(dto.getQty().toString());
            Double componentQty = Double.valueOf(0.0D);
            BigDecimal issueComponentQty = BigDecimal.ZERO;
            for (MtWorkOrderVO8 order08 : mtWorkOrderVO8s) {
                // 需求数量
                if (dto.getMaterialId().equals(order08.getMaterialId())) {
                    componentQty = BigDecimal.valueOf(componentQty)
                                    .add(new BigDecimal(order08.getComponentQty().toString())).doubleValue();
                } else {
                    MtBomSubstituteVO6 bomSubstituteVO6 = new MtBomSubstituteVO6();
                    bomSubstituteVO6.setBomComponentId(order08.getBomComponentId());
                    bomSubstituteVO6.setQty(order08.getComponentQty());
                    bomSubstituteVO6.setMaterialId(dto.getMaterialId());
                    List<MtBomSubstituteVO3> bomSubstituteVO3s =
                                    mtBomSubstituteRepository.bomSubstituteQtyCalculate(tenantId, bomSubstituteVO6);
                    if (CollectionUtils.isNotEmpty(bomSubstituteVO3s)) {
                        // 确认过，通过这个条件只能取到一个数据
                        componentQty = BigDecimal.valueOf(componentQty)
                                        .add(new BigDecimal(bomSubstituteVO3s.get(0).getComponentQty().toString()))
                                        .doubleValue();
                    }
                }
            }
            // 根据输入的materialId、bomComponentId匹配第三步4-b-ii获取sum_AssembleQty，
            // 获取到数据为空，认为获取到数据为0
            Map<String, BigDecimal> tempMap = collect.get(dto.getBomComponentId());
            if (tempMap != null) {
                BigDecimal qty = tempMap.get(dto.getMaterialId());
                if (qty != null) {
                    sumAssembleQty = sumAssembleQty.add(qty);
                }
            }
            // 计算考虑投料限制后的数量
            if ("FIX".equals(mtPfepManufacturing.getIssueControlType())
                            && BigDecimal.valueOf(componentQty).compareTo(BigDecimal.ZERO) != 0) {
                issueComponentQty = BigDecimal.valueOf(componentQty)
                                .add(BigDecimal.valueOf(mtPfepManufacturing.getIssueControlQty()));
            }

            if ("PERCENT".equals(mtPfepManufacturing.getIssueControlType())
                            && BigDecimal.valueOf(componentQty).compareTo(BigDecimal.ZERO) != 0) {
                issueComponentQty = BigDecimal.valueOf(componentQty)
                                .add(BigDecimal.valueOf(componentQty)
                                                .multiply(BigDecimal.valueOf(mtPfepManufacturing.getIssueControlQty()))
                                                .divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_DOWN));
            }
            if (sumAssembleQty.compareTo(issueComponentQty) == 1) {
                throw new MtException("MT_ASSEMBLE_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0068", "ASSEMBLE", "【API:woAssembleQtyExcessVerify】"));
            }
        }
    }

    /**
     * eoAssembleQtyExcessVerify -验证执行作业组件装配数量是否超量
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    @Override
    public void eoAssembleQtyExcessVerify(Long tenantId, MtAssembleConfirmActualVO8 dto) {
        // 1验证输入参数的合规性
        if (dto == null || StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoAssembleQtyExcessVerify】"));
        }
        if (StringUtils.isEmpty(dto.getBomComponentId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "bomComponentId", "【API:eoAssembleQtyExcessVerify】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "qty", "【API:eoAssembleQtyExcessVerify】"));
        }

        // 获取物料
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            MtBomComponentVO8 bomComponentVO8 =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (bomComponentVO8 == null || StringUtils.isEmpty(bomComponentVO8.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0071",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0071",
                                                "ASSEMBLE", bomComponentVO8.getBomComponentId(),
                                                "【API:eoAssembleQtyExcessVerify】"));
            }
            dto.setMaterialId(bomComponentVO8.getMaterialId());
        }

        // 获取是否按工序装配标识
        String operationAssembleFlag = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());
        if ("Y".equals(operationAssembleFlag) && StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_ASSEMBLE_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0047", "ASSEMBLE", "【API:eoAssembleQtyExcessVerify】"));
        }
        // 第三步
        if ("N".equals(dto.getIssueControlFlag())) {
            // 若输入参数issueControlFlag为N，标识不考虑投料限制
            MtEoComponentActualVO12 mtEoComponentVO12 = new MtEoComponentActualVO12();
            mtEoComponentVO12.setEoId(dto.getEoId());
            if ("Y".equals(operationAssembleFlag)) {
                mtEoComponentVO12.setRouterStepId(dto.getRouterStepId());
            }

            List<MtEoComponentActualVO13> mtEoComponentVO13s =
                            mtEoComponentActualRepository.eoUnassembledComponentQuery(tenantId, mtEoComponentVO12);

            List<MtEoComponentActualVO13> fileredVo = mtEoComponentVO13s.stream()
                            .filter(t -> t.getBomComponentId().equals(dto.getBomComponentId())
                                            && t.getMaterialId().equals(dto.getMaterialId()))
                            .collect(Collectors.toList());

            BigDecimal unassembledQty = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(fileredVo)) {
                unassembledQty = fileredVo.stream().collect(
                                CollectorsUtil.summingBigDecimal(c -> c.getUnassembledQty() == null ? BigDecimal.ZERO
                                                : BigDecimal.valueOf(c.getUnassembledQty())));
            } else {
                unassembledQty = BigDecimal.ZERO;
            }

            if (new BigDecimal(dto.getQty().toString()).compareTo(unassembledQty) == 1) {
                throw new MtException("MT_ASSEMBLE_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0068", "ASSEMBLE", "【API:eoAssembleQtyExcessVerify】"));
            }
        } else {
            // 为Y或者为空执行路线 ,标识需考虑投料限制
            MtEoVO19 mtEoVO19 = new MtEoVO19();
            mtEoVO19.setEoId(dto.getEoId());
            if ("Y".equals(operationAssembleFlag)) {
                mtEoVO19.setRouterStepId(dto.getRouterStepId());

            }
            List<MtEoVO20> mtEoVO20List = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
            // 获取执行作业或执行作业在指定工艺已装配的组件实绩列表

            MtEoComponentActualVO10 vo10 = new MtEoComponentActualVO10();
            vo10.setEoId(dto.getEoId());
            vo10.setRouterStepId(dto.getRouterStepId());
            vo10.setBomComponentId(dto.getBomComponentId());
            List<MtEoComponentActual> actualList =
                            mtEoComponentActualRepository.componentLimitEoComponentAssembleActualQuery(tenantId, vo10);
            // 按照bomComponentId、materialId进行汇总
            Map<String, Map<String, BigDecimal>> collect = actualList.stream().collect(Collectors.groupingBy(
                            MtEoComponentActual::getBomComponentId,
                            Collectors.groupingBy(MtEoComponentActual::getMaterialId,
                                            CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(
                                                            c.getAssembleQty() == null ? 0.0D : c.getAssembleQty())))));

            // 获取执行作业物料的投料限制：
            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
            if (null == mtEo) {
                throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0020", "ORDER", "【API:eoAssembleQtyExcessVerify】"));
            }

            MtPfepInventoryVO queryVO = new MtPfepInventoryVO();
            queryVO.setMaterialId(dto.getMaterialId());
            queryVO.setOrganizationType("PRODUCTIONLINE");
            queryVO.setSiteId(mtEo.getSiteId());
            queryVO.setOrganizationId(mtEo.getProductionLineId());
            MtPfepManufacturing mtPfepManufacturing =
                            mtPfepManufacturingRepository.pfepManufacturingIssueControlGet(tenantId, queryVO);
            // 若获取结果存在一个字段为空值，
            // 即认为获取结果为issueControlType = FIX，issueControlQty = 0
            if (mtPfepManufacturing == null || StringUtils.isEmpty(mtPfepManufacturing.getIssueControlType())
                            || mtPfepManufacturing.getIssueControlQty() == null) {
                if (mtPfepManufacturing == null) {
                    mtPfepManufacturing = new MtPfepManufacturing();
                }
                mtPfepManufacturing.setIssueControlType("FIX");
                mtPfepManufacturing.setIssueControlQty(Double.valueOf(0.0D));
            }

            // 判断数量是否超量
            mtEoVO20List = mtEoVO20List.stream().filter(t -> dto.getBomComponentId().equals(t.getBomComponentId()))
                            .collect(Collectors.toList());
            Double componentQty = Double.valueOf(0.0D);
            for (MtEoVO20 mtEoVO20 : mtEoVO20List) {
                // 需求数量
                if (dto.getMaterialId().equals(mtEoVO20.getMaterialId())) {
                    componentQty = BigDecimal.valueOf(componentQty).add(new BigDecimal(mtEoVO20.getComponentQty()))
                                    .doubleValue();
                } else {
                    MtBomSubstituteVO6 bomSubstituteVO6 = new MtBomSubstituteVO6();
                    bomSubstituteVO6.setBomComponentId(mtEoVO20.getBomComponentId());
                    bomSubstituteVO6.setQty(mtEoVO20.getComponentQty());
                    bomSubstituteVO6.setMaterialId(dto.getMaterialId());
                    List<MtBomSubstituteVO3> bomSubstituteVO3s =
                                    mtBomSubstituteRepository.bomSubstituteQtyCalculate(tenantId, bomSubstituteVO6);
                    if (CollectionUtils.isNotEmpty(bomSubstituteVO3s)) {
                        // 确认过，通过这个条件只能取到一个数据
                        componentQty = BigDecimal.valueOf(componentQty)
                                        .add(new BigDecimal(bomSubstituteVO3s.get(0).getComponentQty())).doubleValue();
                    }
                }
            }
            // 根据获取到的componentQty计算考虑投料限制后的数量
            BigDecimal issueComponentQty = BigDecimal.ZERO;
            // 计算考虑投料限制后的数量
            if ("FIX".equals(mtPfepManufacturing.getIssueControlType())
                            && BigDecimal.valueOf(componentQty).compareTo(BigDecimal.ZERO) != 0) {
                issueComponentQty = BigDecimal.valueOf(componentQty)
                                .add(BigDecimal.valueOf(mtPfepManufacturing.getIssueControlQty()));
            }
            if ("PERCENT".equals(mtPfepManufacturing.getIssueControlType())
                            && BigDecimal.valueOf(componentQty).compareTo(BigDecimal.ZERO) != 0) {
                issueComponentQty = BigDecimal.valueOf(componentQty)
                                .add(BigDecimal.valueOf(componentQty)
                                                .multiply(BigDecimal.valueOf(mtPfepManufacturing.getIssueControlQty()))
                                                .divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_DOWN));
            }

            // 根据输入的materialId、bomComponentId匹配第三步4-b-ii获取sum_AssembleQty，
            // 获取到数据为空，认为获取到数据为0
            Map<String, BigDecimal> tempMap = collect.get(dto.getBomComponentId());
            BigDecimal sumAssembleQty = new BigDecimal(dto.getQty().toString());
            if (tempMap != null) {
                BigDecimal qty = tempMap.get(dto.getMaterialId());
                if (qty != null) {
                    sumAssembleQty = sumAssembleQty.add(qty);
                }
            }
            if (sumAssembleQty.compareTo(issueComponentQty) == 1) {
                throw new MtException("MT_ASSEMBLE_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0068", "ASSEMBLE", "【API:eoAssembleQtyExcessVerify】"));
            }
        }
    }

    /**
     * eoWkcBackflushProcess-执行作业工作单元组件反冲装配处理
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWkcBackflushProcess(Long tenantId, MtAssembleConfirmActualVO10 dto) {
        // 1.1判断输入参数是否合规
        if (dto == null || StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "workcellId", "【API:eoWkcBackflushProcess】"));
        }
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoWkcBackflushProcess】"));
        }
        if (null == dto.getQty()) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "qty", "【API:eoWkcBackflushProcess】"));
        }

        // 2.获取执行作业需反冲组件
        MtEoVO19 mtEo19 = new MtEoVO19();
        mtEo19.setEoId(dto.getEoId());
        if (null != dto.getRouterStepId()) {
            mtEo19.setRouterStepId(dto.getRouterStepId());
        }
        List<MtEoVO20> mtEoVO20s = mtEoRepository.eoComponentQtyQuery(tenantId, mtEo19);
        List<String> bomComponentIds = mtEoVO20s.stream().map(MtEoVO20::getBomComponentId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bomComponentIds)) {
            return;
        }

        // 2.1获取组件类型和组件装配方式
        List<MtBomComponentVO13> bomComponents =
                        mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIds);
        bomComponents = bomComponents.stream().filter(c -> "ASSEMBLING".equals(c.getBomComponentType()))
                        .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bomComponents)) {
            return;
        }

        // 3.获取工作单元上已装配的装配组
        List<MtAssembleGroupActualVO2> mtAssembleGroupActualVO2s = mtAssembleGroupActualRepository
                        .wkcLimitCurrentAssembleGroupQuery(tenantId, dto.getWorkcellId());
        if (CollectionUtils.isEmpty(mtAssembleGroupActualVO2s)) {
            // 3.1assembleMethod = BACKFLASH的数据更新装配实绩
            for (MtBomComponentVO13 bomComponent : bomComponents) {
                if ("BACKFLASH".equals(bomComponent.getAssembleMethod())) {
                    for (MtEoVO20 vo : mtEoVO20s) {
                        if (vo.getBomComponentId().equals(bomComponent.getBomComponentId())) {
                            MtAssembleProcessActualVO5 assembleProcessVO5 = new MtAssembleProcessActualVO5();
                            assembleProcessVO5.setEoId(dto.getEoId());// 执行作业ID
                            assembleProcessVO5.setMaterialId(vo.getMaterialId());
                            assembleProcessVO5.setBomComponentId(vo.getBomComponentId());
                            assembleProcessVO5.setRouterId("");
                            assembleProcessVO5.setRouterStepId(dto.getRouterStepId());

                            BigDecimal qty = BigDecimal.valueOf(dto.getQty()).multiply(BigDecimal
                                            .valueOf(vo.getPreQty() == null ? Double.valueOf(0.0D) : vo.getPreQty()));
                            assembleProcessVO5.setTrxAssembleQty(qty.doubleValue());

                            assembleProcessVO5.setAssembleExcessFlag("N");
                            assembleProcessVO5.setAssembleMethod("INV_BACKFLASH");
                            assembleProcessVO5.setOperationBy(dto.getOperationBy());
                            assembleProcessVO5.setWorkcellId(dto.getWorkcellId());

                            assembleProcessVO5.setParentEventId(dto.getParentEventId());
                            assembleProcessVO5.setEventRequestId(dto.getEventRequestId());
                            assembleProcessVO5.setShiftCode(dto.getShiftCode());
                            assembleProcessVO5.setShiftDate(dto.getShiftDate());
                            // 传入装配货位
                            if (dto.getLocatorId() != null) {
                                assembleProcessVO5.setLocatorId(dto.getLocatorId());
                            } else {
                                MtRouterOperation mtRouterOperation = null;
                                if (dto.getRouterStepId() != null) {
                                    mtRouterOperation = mtRouterOperationRepository.routerOperationGet(tenantId,
                                                    dto.getRouterStepId());
                                    if (mtRouterOperation != null
                                                    && StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                                        throw new MtException("MT_ASSEMBLE_0072",
                                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                        "MT_ASSEMBLE_0072", "ASSEMBLE",
                                                                        "【API:eoWkcBackflushProcess】"));

                                    }
                                }
                                MtEoComponentActualVO mtEoComponentVO = new MtEoComponentActualVO();
                                mtEoComponentVO.setEoId(dto.getEoId());
                                mtEoComponentVO.setMaterialId(vo.getMaterialId());
                                if (mtRouterOperation != null) {
                                    mtEoComponentVO.setOperationId(mtRouterOperation.getOperationId());
                                }
                                String locatorGet = mtEoComponentActualRepository
                                                .eoComponentAssembleLocatorGet(tenantId, mtEoComponentVO);
                                assembleProcessVO5.setLocatorId(locatorGet);
                            }
                            mtAssembleProcessActualRepository.componentAssembleProcess(tenantId, assembleProcessVO5);

                            // 新增逻辑更新现有量
                            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
                            if (mtEo == null) {
                                throw new MtException("MT_ASSEMBLE_0004",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "MT_ASSEMBLE_0004", "ASSEMBLE", "eoId",
                                                                "【API:eoWkcBackflushProcess】"));
                            }
                            // 创建事件ID
                            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                            eventCreateVO.setLocatorId(assembleProcessVO5.getLocatorId());
                            eventCreateVO.setEventRequestId(dto.getEventRequestId());
                            eventCreateVO.setParentEventId(dto.getParentEventId());
                            eventCreateVO.setShiftCode(dto.getShiftCode());
                            eventCreateVO.setShiftDate(dto.getShiftDate());
                            eventCreateVO.setWorkcellId(dto.getWorkcellId());
                            eventCreateVO.setEventTypeCode("EO_PROCESS_ACTUAL_INV_BACKFLASH");
                            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                            MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
                            updateOnhandVO.setSiteId(mtEo.getSiteId());
                            updateOnhandVO.setMaterialId(vo.getMaterialId());
                            updateOnhandVO.setLocatorId(assembleProcessVO5.getLocatorId());
                            updateOnhandVO.setLotCode("");
                            updateOnhandVO.setChangeQuantity(qty.doubleValue());
                            updateOnhandVO.setEventId(eventId);
                            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnhandVO);
                        }
                    }
                }
            }
        } else {
            for (MtBomComponentVO13 bomComponent : bomComponents) {
                for (MtEoVO20 vo : mtEoVO20s) {
                    if (vo.getBomComponentId().equals(bomComponent.getBomComponentId())) {
                        MtAssemblePointActualVO1 mtAssemblePointActualVO1 = new MtAssemblePointActualVO1();
                        mtAssemblePointActualVO1.setMaterialId(vo.getMaterialId());
                        mtAssemblePointActualVO1.setWorkcellId(dto.getWorkcellId());
                        List<String> propertys = mtAssemblePointActualRepository
                                        .propertyLimitAssemblePointActualQuery(tenantId, mtAssemblePointActualVO1);

                        if (CollectionUtils.isNotEmpty(propertys)) {
                            // 根据获取结果列表循环调用
                            MtAssembleProcessActualVO5 assembleProcessVO5 = new MtAssembleProcessActualVO5();
                            assembleProcessVO5.setEoId(dto.getEoId());
                            assembleProcessVO5.setWorkcellId(dto.getWorkcellId());
                            assembleProcessVO5.setMaterialId(vo.getMaterialId());
                            assembleProcessVO5.setBomComponentId(vo.getBomComponentId());
                            assembleProcessVO5.setRouterStepId(dto.getRouterStepId());
                            BigDecimal qty = BigDecimal.valueOf(dto.getQty()).multiply(BigDecimal
                                            .valueOf(vo.getPreQty() == null ? Double.valueOf(0.0D) : vo.getPreQty()));
                            assembleProcessVO5.setTrxAssembleQty(qty.doubleValue());
                            assembleProcessVO5.setAssembleExcessFlag("N");
                            assembleProcessVO5.setReferenceArea(dto.getReferenceArea());
                            assembleProcessVO5.setOperationBy(dto.getOperationBy());
                            assembleProcessVO5.setParentEventId(dto.getParentEventId());
                            assembleProcessVO5.setEventRequestId(dto.getEventRequestId());
                            assembleProcessVO5.setShiftDate(dto.getShiftDate());
                            assembleProcessVO5.setShiftCode(dto.getShiftCode());
                            // 传入装配货位
                            if (dto.getLocatorId() != null) {
                                assembleProcessVO5.setLocatorId(dto.getLocatorId());
                            } else {
                                MtRouterOperation mtRouterOperation = null;
                                if (dto.getRouterStepId() != null) {
                                    mtRouterOperation = mtRouterOperationRepository.routerOperationGet(tenantId,
                                                    dto.getRouterStepId());
                                    if (mtRouterOperation != null
                                                    && StringUtils.isEmpty(mtRouterOperation.getOperationId())) {
                                        throw new MtException("MT_ASSEMBLE_0072",
                                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                        "MT_ASSEMBLE_0072", "ASSEMBLE",
                                                                        "【API:eoWkcBackflushProcess】"));

                                    }
                                }
                                MtEoComponentActualVO mtEoComponentVO = new MtEoComponentActualVO();
                                mtEoComponentVO.setEoId(dto.getEoId());
                                mtEoComponentVO.setMaterialId(vo.getMaterialId());
                                if (mtRouterOperation != null) {
                                    mtEoComponentVO.setOperationId(mtRouterOperation.getOperationId());
                                }
                                String locatorGet = mtEoComponentActualRepository
                                                .eoComponentAssembleLocatorGet(tenantId, mtEoComponentVO);
                                assembleProcessVO5.setLocatorId(locatorGet);
                            }
                            mtAssembleProcessActualRepository.eoAssembleGroupComponentAssembleProcess(tenantId,
                                            assembleProcessVO5);
                        }
                    }
                }
            }
        }
    }

    /**
     * eoBomToAssembleConfirmActualCopy-将执行作业装配清单复制到组件装配确认实绩
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoBomToAssembleConfirmActualCopy(Long tenantId, MtAssembleConfirmActualVO11 dto) {
        // 1.验证输入参数的合规性
        if (dto == null || StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoBomToAssembleConfirmActualCopy】"));
        }
        // 2.判断执行作业是否需启用工序装配

        String operationAssembleFlag = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());
        if ("Y".equals(operationAssembleFlag)) {
            // 执行作业需启用工序装配
            String routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
            if (StringUtils.isEmpty(routerId)) {
                return;
            }

            List<MtRouterStepVO5> routerStepOpVOS = mtRouterStepRepository.routerStepListQuery(tenantId, routerId);
            if (CollectionUtils.isEmpty(routerStepOpVOS)) {
                return;
            }

            List<String> routerStepIds =
                            routerStepOpVOS.stream().map(MtRouterStepVO5::getRouterStepId).collect(Collectors.toList());

            // 传入获取到的步骤清单routerStepId列表，依次调用
            Map<String, List<MtRouterOperationComponent>> routerStepIdMaps = new HashMap<>();
            for (String routerStepId : routerStepIds) {
                List<MtRouterOperationComponent> mtRouterOperationComponents = mtRouterOperationComponentRepository
                                .routerOperationComponentQuery(tenantId, routerStepId);

                if (CollectionUtils.isNotEmpty(mtRouterOperationComponents)) {
                    routerStepIdMaps.put(routerStepId, mtRouterOperationComponents);
                }
            }

            if (routerStepIdMaps.size() == 0) {
                return;
            }

            // 调用API{eventCreate}获取事件（仅生成一次事件）
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            BeanUtils.copyProperties(dto, eventCreateVO);
            eventCreateVO.setEventTypeCode("EO_ASSEMBLE_CONFIRM_ACTUAL_COPY_CREATE");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            for (Entry<String, List<MtRouterOperationComponent>> entry : routerStepIdMaps.entrySet()) {
                List<MtRouterOperationComponent> mtRouterOperationComponents = entry.getValue();

                for (MtRouterOperationComponent mtRouterOperationComponent : mtRouterOperationComponents) {
                    MtAssembleConfirmActualHis mtAssembleConfirmActualHis = new MtAssembleConfirmActualHis();
                    mtAssembleConfirmActualHis.setEoId(dto.getEoId());
                    mtAssembleConfirmActualHis.setBomComponentId(mtRouterOperationComponent.getBomComponentId());
                    mtAssembleConfirmActualHis.setRouterStepId(entry.getKey());
                    mtAssembleConfirmActualHis.setAssembleExcessFlag("N");
                    mtAssembleConfirmActualHis.setEventId(eventId);
                    this.assembleConfirmActualUpdate(tenantId, mtAssembleConfirmActualHis);
                }
            }
        } else {
            // 5.执行作业不启用工序装配
            MtEoVO19 mtEoVO = new MtEoVO19();
            mtEoVO.setEoId(dto.getEoId());
            List<MtEoVO20> mtEoVO20s = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO);
            if (CollectionUtils.isEmpty(mtEoVO20s)) {
                return;
            }

            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            BeanUtils.copyProperties(dto, eventCreateVO);
            eventCreateVO.setEventTypeCode("EO_ASSEMBLE_CONFIRM_ACTUAL_COPY_CREATE");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            // 根据获取到的结果依次生成执行作业装配清
            for (MtEoVO20 mtEoVO20 : mtEoVO20s) {
                MtAssembleConfirmActualHis mtAssembleConfirmActualHis = new MtAssembleConfirmActualHis();
                mtAssembleConfirmActualHis.setEoId(dto.getEoId());
                mtAssembleConfirmActualHis.setBomComponentId(mtEoVO20.getBomComponentId());
                mtAssembleConfirmActualHis.setAssembleExcessFlag("N");
                mtAssembleConfirmActualHis.setEventId(eventId);
                this.assembleConfirmActualUpdate(tenantId, mtAssembleConfirmActualHis);
            }
        }
    }

    @Override
    public List<MtAssembleConfirmActualVO16> eoLimitMaterialLotAssembleActualQuery(Long tenantId,
                    MtAssembleConfirmActualVO15 dto) {
        // 【第一步】校验参数的合规性,必输校验
        if (null == dto || StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:eoLimitMaterialLotAssembleActualQuery】"));
        }
        // 【第二步】根据以下条件获取参数
        List<MtAssembleConfirmActualVO16> confirmProcessActuals =
                        mtAssembleConfirmActualMapper.eoLimitMaterialLotAssembleActualQuery(tenantId, dto);
        if (CollectionUtils.isNotEmpty(confirmProcessActuals)) {
            List<String> materialIds = confirmProcessActuals.stream().map(MtAssembleConfirmActualVO16::getMaterialId)
                            .collect(Collectors.toList());
            List<MtMaterialVO> materialVOS = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);
            Map<String, String> materialCodes = new HashMap<>();
            if (CollectionUtils.isNotEmpty(materialVOS)) {
                materialCodes = materialVOS.stream()
                                .collect(Collectors.toMap(MtMaterialVO::getMaterialId, MtMaterialVO::getMaterialCode));
            }
            Map<String, String> materialLotCodes = new HashMap<>();
            List<String> materialLotIds = confirmProcessActuals.stream()
                            .map(MtAssembleConfirmActualVO16::getMaterialLotId).collect(Collectors.toList());
            List<MtMaterialLot> mtMaterialLots =
                            mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds);
            if (CollectionUtils.isNotEmpty(mtMaterialLots)) {
                materialLotCodes = mtMaterialLots.stream().collect(
                                Collectors.toMap(MtMaterialLot::getMaterialLotId, MtMaterialLot::getMaterialLotCode));
            }

            for (MtAssembleConfirmActualVO16 vo16 : confirmProcessActuals) {
                vo16.setMaterialLotCode(materialLotCodes.get(vo16.getMaterialLotId()));
                vo16.setMaterialCode(materialCodes.get(vo16.getMaterialId()));
            }
            return confirmProcessActuals.stream()
                            .filter(t -> new BigDecimal(t.getAssembleQty().toString()).compareTo(BigDecimal.ZERO) > 0)
                            .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<MtAssembleConfirmActualVO18> propertyLimitAssembleConfirmActualPropertyQuery(Long tenantId,
                    MtAssembleConfirmActualVO17 dto) {
        List<MtAssembleConfirmActualVO18> confirmActualVO18List =
                        mtAssembleConfirmActualMapper.selectCondition(tenantId, dto);
        if (CollectionUtils.isEmpty(confirmActualVO18List)) {
            return null;
        }
        // 获取到的物料 materialId列表
        List<String> materialIds = confirmActualVO18List.stream().map(MtAssembleConfirmActualVO18::getMaterialId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, MtMaterialVO> mtMaterialVOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(materialIds)) {
            List<MtMaterialVO> mtMaterialVOS = iMtMaterialService.materialPropertyBatchGet(tenantId, materialIds);
            // 获取物料编码和物料描述
            if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
                mtMaterialVOMap = mtMaterialVOS.stream().collect(Collectors.toMap(t -> t.getMaterialId(), t -> t));
            }
        }

        // 获取到的装配清单 bomId列表
        List<String> bomIds = confirmActualVO18List.stream().map(MtAssembleConfirmActualVO18::getBomId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, MtBomVO7> bomVO7Map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(bomIds)) {
            List<MtBomVO7> mtBomVO7s = mtBomRepository.bomBasicBatchGet(tenantId, bomIds);
            // 获取装配清单描述 bomName
            if (CollectionUtils.isNotEmpty(mtBomVO7s)) {
                bomVO7Map = mtBomVO7s.stream().collect(Collectors.toMap(t -> t.getBomId(), t -> t));
            }
        }


        // 获取到的执行作业 eoId列表
        List<String> eoIds = confirmActualVO18List.stream().map(MtAssembleConfirmActualVO18::getEoId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, MtEo> eoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eoIds)) {
            List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
            // 获取执行作业编号
            if (CollectionUtils.isNotEmpty(mtEos)) {
                eoMap = mtEos.stream().collect(Collectors.toMap(t -> t.getEoId(), t -> t));
            }
        }


        for (MtAssembleConfirmActualVO18 actualVO18 : confirmActualVO18List) {
            actualVO18.setMaterialCode(null == mtMaterialVOMap.get(actualVO18.getMaterialId()) ? null
                            : mtMaterialVOMap.get(actualVO18.getMaterialId()).getMaterialCode());
            actualVO18.setMaterialName(null == mtMaterialVOMap.get(actualVO18.getMaterialId()) ? null
                            : mtMaterialVOMap.get(actualVO18.getMaterialId()).getMaterialName());
            actualVO18.setBomName(null == bomVO7Map.get(actualVO18.getBomId()) ? null
                            : bomVO7Map.get(actualVO18.getBomId()).getBomName());
            actualVO18.setEoNum(null == eoMap.get(actualVO18.getEoId()) ? null
                            : eoMap.get(actualVO18.getEoId()).getEoNum());
        }
        return confirmActualVO18List;
    }

    @Override
    public List<MtAssembleConfirmActualVO20> eoLimitAssembleActualBatchQuery(Long tenantId,
                    MtAssembleConfirmActualVO19 dto) {
        if (CollectionUtils.isEmpty(dto.getEoIdList())) {
            return Collections.emptyList();
        }

        return mtAssembleConfirmActualMapper.eoLimitAssembleActualBatchQuery(tenantId, dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtAssembleConfirmActualVO26> assembleConfirmActualBatchUpdate(Long tenantId,
                    MtAssembleConfirmActualVO24 dto) {
        String apiName = "【API:assembleConfirmActualBatchUpdate】";
        if (MtIdHelper.isIdNull(dto.getEventId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eventId", apiName));
        }
        if (CollectionUtils.isEmpty(dto.getActualList())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "actualList", apiName));
        }
        List<MtAssembleConfirmActualVO25> inputActualList =
                        dto.getActualList().stream().distinct().collect(Collectors.toList());
        if (inputActualList.stream().anyMatch(
                        t -> MtIdHelper.isIdNull(t.getAssembleConfirmActualId()) && MtIdHelper.isIdNull(t.getEoId()))) {
            throw new MtException("MT_ASSEMBLE_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0002", "ASSEMBLE", "assembleConfirmActualId、eoId", apiName));
        }

        // 判断传入了的BomComponentId的是否在表里存在
        List<String> tempBomComponentIds = inputActualList.stream().map(MtAssembleConfirmActualVO25::getBomComponentId)
                        .filter(MtIdHelper::isIdNotNull).distinct().collect(Collectors.toList());
        Map<String, MtBomComponentVO13> bomComponentMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(tempBomComponentIds)) {
            List<MtBomComponentVO13> bomComponentList =
                            mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, tempBomComponentIds);
            if (bomComponentList.size() != tempBomComponentIds.size()) {
                tempBomComponentIds.removeAll(bomComponentList.stream().map(MtBomComponentVO13::getBomComponentId)
                                .collect(Collectors.toList()));
                throw new MtException("MT_ASSEMBLE_0071", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0071", "ASSEMBLE", tempBomComponentIds.toString(), apiName));
            }
            bomComponentMap = bomComponentList.stream()
                            .collect(Collectors.toMap(MtBomComponentVO13::getBomComponentId, t -> t));
        }

        // Step2判断componentConfirmActuaId是否有值输入，并且是否有重复
        List<String> inputActualIds =
                        inputActualList.stream().map(MtAssembleConfirmActualVO25::getAssembleConfirmActualId)
                                        .filter(MtIdHelper::isIdNotNull).collect(Collectors.toList());
        List<String> distinctActualIds = inputActualIds.stream().distinct().collect(Collectors.toList());
        // 如果存在重复的主键
        if (distinctActualIds.size() != inputActualIds.size()) {
            throw new MtException("MT_ASSEMBLE_0082", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0082", "ASSEMBLE", "assembleConfirmActualId", apiName));
        }

        // 获取未输入bomId的eoId，通过eoId去eoBom表找到对应的bomId
        List<String> noBomEoIds = inputActualList.stream()
                        .filter(t -> MtIdHelper.isIdNull(t.getAssembleConfirmActualId())
                                        && MtIdHelper.isIdNull(t.getBomId()))
                        .map(MtAssembleConfirmActualVO25::getEoId).distinct().collect(Collectors.toList());
        Map<String, String> eoBomMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(noBomEoIds)) {
            List<MtEoBom> eoBomList = mtEoBomRepository.eoBomBatchGet(tenantId, noBomEoIds);
            if (CollectionUtils.isNotEmpty(eoBomList)) {
                eoBomMap = eoBomList.stream().collect(Collectors.toMap(MtEoBom::getEoId, MtEoBom::getBomId));
            }
        }

        // 获取未传入工艺Id的工艺路线步骤对应工序
        Map<String, String> routerOperationMap = new HashMap<>();
        List<String> noOperationRouterStepIds = inputActualList.stream()
                        .filter(t -> MtIdHelper.isIdNull(t.getOperationId())
                                        && MtIdHelper.isIdNotNull(t.getRouterStepId()))
                        .map(MtAssembleConfirmActualVO25::getRouterStepId).distinct().filter(MtIdHelper::isIdNotNull)
                        .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(noOperationRouterStepIds)) {
            List<MtRouterOperation> routerOperationsList =
                            mtRouterOperationRepository.routerOperationBatchGet(tenantId, noOperationRouterStepIds);
            if (routerOperationsList.size() != noOperationRouterStepIds.size()) {
                throw new MtException("MT_ASSEMBLE_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0072", "ASSEMBLE", apiName));
            }
            if (CollectionUtils.isNotEmpty(routerOperationsList)) {
                routerOperationMap = routerOperationsList.stream().collect(Collectors
                                .toMap(MtRouterOperation::getRouterStepId, MtRouterOperation::getOperationId));
            }
        }

        // 入參個數
        int inputDataSize = inputActualList.size();

        // 查詢的条件列表
        List<String> eoIds = new ArrayList<>(inputDataSize);
        List<String> bomComponentIds = new ArrayList<>(inputDataSize);
        List<String> materialIds = new ArrayList<>(inputDataSize);
        List<String> componentTypes = new ArrayList<>(inputDataSize);
        List<String> bomIds = new ArrayList<>(inputDataSize);
        List<String> routerStepIds = new ArrayList<>(inputDataSize);
        List<String> operationIds = new ArrayList<>(inputDataSize);

        // 已存在数据的集合
        List<MtAssembleConfirmActual> existActualList;

        // 保存原生传入的数据
        List<MtAssembleConfirmActualVO25> originalInputActualList = new ArrayList<>(inputDataSize);
        for (MtAssembleConfirmActualVO25 inputActual : inputActualList) {
            // 保存传入的数据
            MtAssembleConfirmActualVO25 oldInputActual =
                            mtAssembleConfirmActualTransMapper.cloneActualVO25(inputActual);
            originalInputActualList.add(oldInputActual);

            MtBomComponentVO13 mtBomComponentVO13 = bomComponentMap.get(oldInputActual.getBomComponentId());
            // 新增时候构造匹配条件
            // 执行作业ID
            eoIds.add(oldInputActual.getEoId());

            // 装配清单组件ID
            String bomComponentId = MtFieldsHelper.getOrDefault(oldInputActual.getBomComponentId(), "");
            bomComponentIds.add(bomComponentId);
            oldInputActual.setBomComponentId(bomComponentId);

            // 物料ID
            if (MtIdHelper.isIdNotNull(oldInputActual.getMaterialId())) {
                materialIds.add(oldInputActual.getMaterialId());
            } else {
                if (MtIdHelper.isIdNull(oldInputActual.getBomComponentId())) {
                    throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0032", "bomComponentId、materialId", apiName));
                }
                materialIds.add(mtBomComponentVO13.getMaterialId());
                oldInputActual.setMaterialId(mtBomComponentVO13.getMaterialId());
            }

            // 组件类型
            if (StringUtils.isNotEmpty(oldInputActual.getComponentType())) {
                componentTypes.add(oldInputActual.getComponentType());
            } else {
                String bomComponentType = MtIdHelper.isIdNull(oldInputActual.getBomComponentId()) ? "ASSEMBLING"
                                : mtBomComponentVO13.getBomComponentType();
                componentTypes.add(bomComponentType);
                oldInputActual.setComponentType(bomComponentType);
            }

            // 装配清单ID
            if (MtIdHelper.isIdNull(oldInputActual.getBomId())) {
                String bomId = MtFieldsHelper.getOrDefault(eoBomMap.get(oldInputActual.getEoId()), "");
                bomIds.add(bomId);
                oldInputActual.setBomId(bomId);
            } else {
                oldInputActual.setBomId(oldInputActual.getBomId());
            }

            // 工艺路线步骤ID
            String routerStepId = MtFieldsHelper.getOrDefault(oldInputActual.getRouterStepId(), "");
            routerStepIds.add(routerStepId);
            oldInputActual.setRouterStepId(routerStepId);

            // 工艺Id
            if (MtIdHelper.isIdNotNull(oldInputActual.getOperationId())) {
                operationIds.add(oldInputActual.getOperationId());
            } else {
                if (MtIdHelper.isIdNull(oldInputActual.getRouterStepId())) {
                    operationIds.add("");
                    oldInputActual.setOperationId("");
                } else {
                    String operationId = MtFieldsHelper
                                    .getOrDefault(routerOperationMap.get(oldInputActual.getRouterStepId()), "");
                    operationIds.add(operationId);
                    oldInputActual.setOperationId(operationId);
                }
            }
        }


        existActualList = self().selectByCondition(Condition.builder(MtAssembleConfirmActual.class)
                        .andWhere(Sqls.custom().andEqualTo(MtAssembleConfirmActual.FIELD_TENANT_ID, tenantId)
                                        .andIn(MtAssembleConfirmActual.FIELD_EO_ID, eoIds, true))
                        .andWhere(Sqls.custom().andIn(MtAssembleConfirmActual.FIELD_BOM_COMPONENT_ID, bomComponentIds,
                                        true))
                        .andWhere(Sqls.custom().andIn(MtAssembleConfirmActual.FIELD_MATERIAL_ID, materialIds, true))
                        .andWhere(Sqls.custom().andIn(MtAssembleConfirmActual.FIELD_COMPONENT_TYPE, componentTypes,
                                        true))
                        .andWhere(Sqls.custom().andIn(MtAssembleConfirmActual.FIELD_BOM_ID, bomIds, true))
                        .andWhere(Sqls.custom().andIn(MtAssembleConfirmActual.FIELD_ROUTER_STEP_ID, routerStepIds,
                                        true))
                        .andWhere(Sqls.custom().andIn(MtAssembleConfirmActual.FIELD_OPERATION_ID, operationIds, true))
                        .build());

        if (CollectionUtils.isNotEmpty(distinctActualIds)) {
            List<MtAssembleConfirmActual> actualList = self().selectByCondition(Condition
                            .builder(MtAssembleConfirmActual.class)
                            .andWhere(Sqls.custom().andEqualTo(MtAssembleConfirmActual.FIELD_TENANT_ID, tenantId).andIn(
                                            MtAssembleConfirmActual.FIELD_ASSEMBLE_CONFIRM_ACTUAL_ID,
                                            distinctActualIds))
                            .build());
            if (actualList.size() != distinctActualIds.size()) {
                throw new MtException("MT_ASSEMBLE_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0036", apiName));
            }
            existActualList.addAll(actualList);
        }


        // 根据唯一性索引建立的实体映射
        Map<MtAssembleConfirmActualTupleVO, MtAssembleConfirmActual> existActualTupleMap = existActualList.stream()
                        .collect(Collectors.toMap(t -> mtAssembleConfirmActualTransMapper.actualToActualTupleVO(t),
                                        t -> t, (t1, t2) -> t1));

        // 根据主键建立的映射
        Map<String, MtAssembleConfirmActual> existActualIdMap = existActualList.stream().collect(
                        Collectors.toMap(MtAssembleConfirmActual::getAssembleConfirmActualId, t -> t, (t1, t2) -> t1));

        // 新增或更新的数据集
        List<MtAssembleConfirmActual> fullUpdateList = new ArrayList<>(inputDataSize);
        List<MtAssembleConfirmActual> insertList = new ArrayList<>(inputDataSize);
        List<MtAssembleConfirmActualHis> hisInsertList = new ArrayList<>(inputDataSize);

        // 计算需要新增的数据个数，并提前查询数据库已存在的数据
        int insertDataNum = 0;
        Map<Integer, MtAssembleConfirmActual> needHandleActualIndexMap = new HashMap<>(inputDataSize);
        Set<MtAssembleConfirmActualTupleVO> insertDataSet = new HashSet<>(inputDataSize);
        for (int i = 0; i < originalInputActualList.size(); i++) {
            MtAssembleConfirmActualVO25 actualVO25 = originalInputActualList.get(i);
            // 未传入主键的主句
            if (MtIdHelper.isIdNull(actualVO25.getAssembleConfirmActualId())) {
                MtAssembleConfirmActualTupleVO tupleVO =
                                mtAssembleConfirmActualTransMapper.actualVO25ToTupleVO(actualVO25);
                // 存在处理相同的数据，直接报错
                if (!insertDataSet.add(tupleVO)) {
                    throw new MtException("MT_ASSEMBLE_0082", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0082", tupleVO.toString(), apiName));
                }

                // 将已经获取到的需要处理的已存在的数据缓存起来，下面直接通过下标获取，避免创建对象再来对比
                MtAssembleConfirmActual needHandleActual = existActualTupleMap.get(tupleVO);
                if (needHandleActual != null) {
                    needHandleActualIndexMap.put(i, needHandleActual);
                } else {
                    insertDataNum++;
                }
            } else {
                // 传入了主键的数据
                MtAssembleConfirmActual needHandleActual =
                                existActualIdMap.get(actualVO25.getAssembleConfirmActualId());
                MtAssembleConfirmActualTupleVO tupleVO =
                                mtAssembleConfirmActualTransMapper.actualToActualTupleVO(needHandleActual);
                // 存在处理相同的数据，直接报错
                if (!insertDataSet.add(tupleVO)) {
                    throw new MtException("MT_ASSEMBLE_0082", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0082", tupleVO.toString(), apiName));
                }
                needHandleActualIndexMap.put(i, needHandleActual);
            }
        }

        // 获取新增的主键
        List<String> insertIds = new ArrayList<>();
        if (insertDataNum > 0) {
            SequenceInfo sequenceInfo = MtSqlHelper.getSequenceInfo(MtAssembleConfirmActual.class);
            insertIds = mtCustomDbRepository.getNextKeys(sequenceInfo.getPrimarySequence(), insertDataNum);
        }

        // 获取历史表主键
        SequenceInfo hisSequenceInfo = MtSqlHelper.getSequenceInfo(MtAssembleConfirmActualHis.class);
        List<String> insertHisIds =
                        mtCustomDbRepository.getNextKeys(hisSequenceInfo.getPrimarySequence(), inputDataSize);

        // 主键获取下标
        int insertIdIndex = 0;
        // 循环处理下标
        int handleIndex = 0;

        List<MtAssembleConfirmActualVO26> resultList = new ArrayList<>(inputDataSize);

        for (MtAssembleConfirmActualVO25 originalInputActual : originalInputActualList) {
            // 由于上一步已经获取到了已存在的数据，所以直接通过下标在需要处理的实绩集合里面获取
            MtAssembleConfirmActual existActual = null;
            if (needHandleActualIndexMap.size() > 0) {
                existActual = needHandleActualIndexMap.get(handleIndex);
            }

            // 获取原传入的入参
            MtAssembleConfirmActualVO25 inputActual = inputActualList.get(handleIndex);
            if (existActual == null) {
                MtAssembleConfirmActual insertActual =
                                mtAssembleConfirmActualTransMapper.actualVO25ToActual(originalInputActual);

                if (null != inputActual.getAssembleExcessFlag()) {
                    insertActual.setAssembleExcessFlag(inputActual.getAssembleExcessFlag());
                } else if (MtIdHelper.isIdNotNull(inputActual.getBomComponentId())) {
                    insertActual.setAssembleExcessFlag(MtBaseConstants.NO);
                } else {
                    insertActual.setAssembleExcessFlag(MtBaseConstants.YES);
                }

                if (null != inputActual.getSubstituteFlag()) {
                    insertActual.setSubstituteFlag(inputActual.getSubstituteFlag());
                } else if (MtIdHelper.isIdNull(inputActual.getBomComponentId())
                                || MtIdHelper.isIdNull(inputActual.getMaterialId())) {
                    insertActual.setSubstituteFlag(MtBaseConstants.NO);
                } else {
                    MtBomComponentVO13 bomComponentVO = bomComponentMap.get(inputActual.getBomComponentId());
                    insertActual.setSubstituteFlag(bomComponentVO.getMaterialId().equals(inputActual.getMaterialId())
                                    ? MtBaseConstants.NO
                                    : MtBaseConstants.YES);
                }

                insertActual.setBypassFlag(dto.getBypassFlag());
                insertActual.setBypassBy(dto.getBypassBy());
                insertActual.setConfirmedBy(dto.getConfirmedBy());
                insertActual.setConfirmFlag(dto.getConfirmFlag());
                insertActual.setTenantId(tenantId);
                insertActual.setAssembleConfirmActualId(insertIds.get(insertIdIndex++));
                insertList.add(insertActual);

                MtAssembleConfirmActualHis his = new MtAssembleConfirmActualHis();
                BeanUtils.copyProperties(insertActual, his);
                his.setTenantId(tenantId);
                his.setEventId(dto.getEventId());
                his.setAssembleConfirmActualHisId(insertHisIds.get(handleIndex));
                hisInsertList.add(his);

                // 组装需要返回的数据
                MtAssembleConfirmActualVO26 actualVO26 =
                                mtAssembleConfirmActualTransMapper.actualVO25ToActualVO26(inputActual);
                actualVO26.setAssembleConfirmActualId(insertActual.getAssembleConfirmActualId());
                actualVO26.setAssembleConfirmActualHisId(his.getAssembleConfirmActualHisId());
                resultList.add(actualVO26);

            } else {
                // 更新已有数据
                MtAssembleConfirmActualHis his = new MtAssembleConfirmActualHis();

                if (inputActual.getAssembleExcessFlag() != null) {
                    existActual.setAssembleExcessFlag(inputActual.getAssembleExcessFlag());
                }
                if (inputActual.getAssembleRouterType() != null) {
                    existActual.setAssembleRouterType(inputActual.getAssembleRouterType());
                }
                if (inputActual.getSubstituteFlag() != null) {
                    existActual.setSubstituteFlag(inputActual.getSubstituteFlag());
                }
                if (dto.getBypassFlag() != null) {
                    existActual.setBypassFlag(dto.getBypassFlag());
                }
                if (dto.getBypassBy() != null) {
                    existActual.setBypassBy(dto.getBypassBy());
                }
                if (dto.getConfirmedBy() != null) {
                    existActual.setConfirmedBy(dto.getConfirmedBy());
                }
                if (dto.getConfirmFlag() != null) {
                    existActual.setConfirmFlag(dto.getConfirmFlag());
                }
                existActual.setTenantId(tenantId);
                fullUpdateList.add(existActual);

                BeanUtils.copyProperties(existActual, his);
                his.setEventId(dto.getEventId());
                his.setTenantId(tenantId);
                his.setAssembleConfirmActualHisId(insertHisIds.get(handleIndex));
                hisInsertList.add(his);

                // 组装需要返回的数据
                MtAssembleConfirmActualVO26 actualVO26 =
                                mtAssembleConfirmActualTransMapper.actualVO25ToActualVO26(inputActual);
                actualVO26.setAssembleConfirmActualId(existActual.getAssembleConfirmActualId());
                actualVO26.setAssembleConfirmActualHisId(his.getAssembleConfirmActualHisId());
                resultList.add(actualVO26);
            }
            handleIndex++;
        }

        mtCustomDbRepository.batchUpdateTarzan(fullUpdateList);
        mtCustomDbRepository.batchInsertTarzan(insertList);
        mtCustomDbRepository.batchInsertTarzan(hisInsertList);

        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoWkcBackflushBatchProcess(Long tenantId, MtAssembleConfirmActualVO27 dto) {
        String apiName = "【API:eoWkcBackflushBatchProcess】";
        long startTime = System.currentTimeMillis();
        // 1.1判断输入参数是否合规
        if (dto == null || MtIdHelper.isIdNull(dto.getWorkcellId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "workcellId", apiName));
        }
        List<MtAssembleConfirmActualVO28> eoInfoList = dto.getEoInfo();
        if (CollectionUtils.isEmpty(eoInfoList)) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", apiName));
        }
        if (eoInfoList.stream().anyMatch(t -> MtIdHelper.isIdNull(t.getEoId()))) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", apiName));
        }
        if (eoInfoList.stream().anyMatch(t -> t.getQty() == null)) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "qty", apiName));
        }

        // 判断eoId是否重复
        List<String> eoIds = eoInfoList.stream().map(MtAssembleConfirmActualVO28::getEoId).collect(Collectors.toList());
        List<String> distinctEoIds = eoIds.stream().distinct().collect(Collectors.toList());
        if (eoIds.size() != distinctEoIds.size()) {
            throw new MtException("MT_ASSEMBLE_0082", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0082", "ASSEMBLE", "eoId", apiName));
        }

        List<MtEo> eoList = mtEoRepository.eoPropertyBatchGet(tenantId, distinctEoIds);
        if (CollectionUtils.isEmpty(eoList)) {
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0004", "ASSEMBLE", "eoId", apiName));
        }
        Map<String, MtEo> eoMap = eoList.stream().collect(Collectors.toMap(MtEo::getEoId, t -> t));

        // 判断eoId对应的执行作业是否存在
        List<MtEoBom> eoBomList = mtEoBomRepository.eoBomBatchGet(tenantId, distinctEoIds);
        if (eoBomList.size() != distinctEoIds.size()) {
            distinctEoIds.removeAll(eoBomList.stream().map(MtEoBom::getEoId).collect(Collectors.toList()));
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0004", "ASSEMBLE", "eoId -> " + distinctEoIds.toString(), apiName));
        }

        Map<String, MtAssembleConfirmActualVO28> eoInfoMap =
                        eoInfoList.stream().collect(Collectors.toMap(MtAssembleConfirmActualVO28::getEoId, t -> t));

        // 建立bom和eo的映射关系
        Map<String, List<String>> bomEoMap = eoBomList.stream().collect(Collectors.groupingBy(MtEoBom::getBomId,
                        Collectors.mapping(MtEoBom::getEoId, Collectors.toList())));
        Map<String, String> eoBomMap =
                        eoBomList.stream().collect(Collectors.toMap(MtEoBom::getEoId, MtEoBom::getBomId));

        // 传入了routerStepId
        List<String> routerStepIds = eoInfoList.stream().map(MtAssembleConfirmActualVO28::getRouterStepId)
                        .filter(MtIdHelper::isIdNotNull).distinct().collect(Collectors.toList());

        List<String> bomComponentIds = new ArrayList<>();

        Map<String, MtRouterOpComponentVO> routerOpComponentMap = new HashMap<>(routerStepIds.size());
        Map<String, String> operationIdMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(routerStepIds)) {
            List<MtRouterOpComponentVO1> mtRouterOpComponentVO1s = new ArrayList<>(routerStepIds.size());
            routerStepIds.forEach(t -> {
                mtRouterOpComponentVO1s.add(new MtRouterOpComponentVO1(t, null));
            });

            // 批量获取步骤的组件单位用量
            List<MtRouterOpComponentVO> routerOpComponentList = mtRouterOperationComponentRepository
                            .routerOperationComponentPerQtyBatchQuery(tenantId, mtRouterOpComponentVO1s);
            routerOpComponentMap = routerOpComponentList.stream()
                            .collect(Collectors.toMap(MtRouterOpComponentVO::getRouterStepId, t -> t));
            bomComponentIds = routerOpComponentList.stream().map(MtRouterOpComponentVO::getBomComponentId)
                            .collect(Collectors.toList());

            operationIdMap = mtRouterOperationRepository.routerOperationBatchGet(tenantId, routerStepIds).stream()
                            .collect(Collectors.toMap(MtRouterOperation::getRouterStepId,
                                            MtRouterOperation::getOperationId));
            if (operationIdMap.values().stream().distinct().collect(Collectors.toList()).size() > 1) {
                throw new MtException("MT_ASSEMBLE_0085", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0085", "ASSEMBLE", apiName));
            }

        }

        long before = System.currentTimeMillis();
        // 批量计算装配清单组件物料用量
        List<String> routerStepEmptyEoIdList = eoInfoList.stream().filter(t -> MtIdHelper.isIdNull(t.getRouterStepId()))
                        .map(MtAssembleConfirmActualVO28::getEoId).collect(Collectors.toList());
        Map<String, Double> bomComponentQtyMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(routerStepEmptyEoIdList)) {
            Set<String> routerStepEmptyEoBomIdSet = new HashSet<>();
            routerStepEmptyEoIdList.forEach(t -> routerStepEmptyEoBomIdSet.add(eoBomMap.get(t)));
            List<MtBomComponentVO21> bomMessages = routerStepEmptyEoBomIdSet.stream()
                            .map(t -> new MtBomComponentVO21(t, null, 1.0D)).collect(Collectors.toList());

            before = System.currentTimeMillis();
            MtBomComponentVO22 bomComponentCalculate = new MtBomComponentVO22();
            bomComponentCalculate.setBomMessages(bomMessages);
            bomComponentCalculate.setIsPhantomUnfold(MtBaseConstants.NO);
            bomComponentCalculate.setAttritionFlag(MtBaseConstants.YES);
            List<MtBomComponentVO25> bomComponentQtyList =
                            mtBomComponentRepository.bomComponentQtyBatchCalculate(tenantId, bomComponentCalculate);
            System.out.println("eoWkcBackflushBatchProcess -> bomComponentQtyBatchCalculate 执行时间(总): "
                            + (System.currentTimeMillis() - before));

            // 获取bomComponentId
            bomComponentIds.addAll(bomComponentQtyList.stream().map(MtBomComponentVO25::getBomComponentList)
                            .flatMap(Collection::stream).map(MtBomComponentVO24::getBomComponentId)
                            .collect(Collectors.toList()));
            bomComponentQtyMap = bomComponentQtyList.stream().map(MtBomComponentVO25::getBomComponentList)
                            .flatMap(Collection::stream).collect(Collectors.toMap(MtBomComponentVO24::getBomComponentId,
                                            t -> t.getMaterialMessageList().get(0).getQty()));
        }

        // 获取装配清单组件
        before = System.currentTimeMillis();
        List<MtBomComponentVO13> bomComponentList = CollectionUtils.isEmpty(bomComponentIds) ? Collections.emptyList()
                        : mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIds);
        System.out.println("eoWkcBackflushBatchProcess -> bomComponentBasicBatchGet 执行时间(总): "
                        + (System.currentTimeMillis() - before));

        // 建立bomComponent和bomId之间的映射
        Map<String, String> bomComponentBomMap = bomComponentList.stream()
                        .collect(Collectors.toMap(MtBomComponentVO13::getBomComponentId, MtBomComponentVO13::getBomId));

        // 按照组件行类型分组
        Map<String, List<MtBomComponentVO13>> bomComponentTypeMap = bomComponentList.stream()
                        .collect(Collectors.groupingBy(MtBomComponentVO13::getBomComponentType));

        // 获取装配类型的组件
        List<MtBomComponentVO13> assemblingComponentList =
                        bomComponentTypeMap.get(MtBaseConstants.BOM_COMPONENT_TYPE.ASSEMBLING);
        if (CollectionUtils.isNotEmpty(assemblingComponentList)) {
            Map<String, List<MtBomComponentVO13>> assembleMethodMap = assemblingComponentList.stream()
                            .collect(Collectors.groupingBy(MtBomComponentVO13::getAssembleMethod));
            List<MtBomComponentVO13> backFLashList = assembleMethodMap.get(MtBaseConstants.ASSEMBLE_METHOD.BACKFLASH);
            List<MtBomComponentVO13> feedingList = assembleMethodMap.get(MtBaseConstants.ASSEMBLE_METHOD.FEEDING);

            // 组装查询库位的参数
            List<MtEoComponentActualVO> eoComponentActualVOList = new ArrayList<>();
            List<String> assembleMethodList = Arrays.asList(MtBaseConstants.ASSEMBLE_METHOD.BACKFLASH,
                            MtBaseConstants.ASSEMBLE_METHOD.FEEDING);
            for (String assembleMethod : assembleMethodList) {
                List<MtBomComponentVO13> tmpBomComponentList = assembleMethodMap.get(assembleMethod);
                if (CollectionUtils.isNotEmpty(tmpBomComponentList)) {
                    for (MtBomComponentVO13 bomComponent : tmpBomComponentList) {
                        String bomId = bomComponentBomMap.get(bomComponent.getBomComponentId());
                        List<String> bomEoIds = bomEoMap.get(bomId);
                        if (CollectionUtils.isNotEmpty(bomEoIds)) {
                            for (String eoId : bomEoIds) {
                                MtAssembleConfirmActualVO28 inputEoInfo = eoInfoMap.get(eoId);
                                MtEoComponentActualVO mtEoComponentVO = new MtEoComponentActualVO();
                                mtEoComponentVO.setEoId(eoId);
                                mtEoComponentVO.setMaterialId(bomComponent.getMaterialId());
                                mtEoComponentVO.setOperationId(operationIdMap.get(inputEoInfo.getRouterStepId()));
                                eoComponentActualVOList.add(mtEoComponentVO);
                            }
                        }
                    }
                }
            }

            // 对于入参去重
            eoComponentActualVOList = eoComponentActualVOList.stream().distinct().collect(Collectors.toList());
            // 通过eoId，materialId,componentType,operationId建立locatorId的关系
            before = System.currentTimeMillis();
            Map<MtEoComponentActualVO, String> locatorIdMap = mtEoComponentActualRepository
                            .eoComponentAssembleLocatorBatchGet(tenantId, eoComponentActualVOList).stream()
                            .collect(Collectors.toMap(
                                            t -> new MtEoComponentActualVO(t.getEoId(), t.getMaterialId(),
                                                            t.getComponentType(), t.getOperationId()),
                                            t -> t.getIssuedLocatorId(), (t1, t2) -> t1));
            System.out.println("eoWkcBackflushBatchProcess -> eoComponentAssembleLocatorBatchGet 执行时间(总): "
                            + (System.currentTimeMillis() - before));

            Date shiftDate = null;
            if (dto.getShiftDate() != null) {
                ZonedDateTime zonedDateTime = dto.getShiftDate().atStartOfDay(ZoneId.systemDefault());
                shiftDate = Date.from(zonedDateTime.toInstant());
            }
            if (CollectionUtils.isNotEmpty(backFLashList)) {
                // 创建事件ID
                MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                eventCreateVO.setEventRequestId(dto.getEventRequestId());
                eventCreateVO.setParentEventId(dto.getParentEventId());
                eventCreateVO.setShiftCode(dto.getShiftCode());
                eventCreateVO.setShiftDate(shiftDate);
                eventCreateVO.setWorkcellId(dto.getWorkcellId());
                eventCreateVO.setEventTypeCode("COMPONET_BACKFLUSH");
                String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                MtInvOnhandQuantityVO16 onhandProcessOnhand = new MtInvOnhandQuantityVO16();
                onhandProcessOnhand.setEventId(eventId);
                List<MtInvOnhandQuantityVO13> onhandList = new ArrayList<>();
                onhandProcessOnhand.setOnhandList(onhandList);

                Map<String, MtAssembleProcessActualVO17> eoInfoDataMap = new HashMap<>();
                for (MtBomComponentVO13 bomComponent : backFLashList) {
                    String bomId = bomComponentBomMap.get(bomComponent.getBomComponentId());
                    List<String> bomEoIds = bomEoMap.get(bomId);
                    if (CollectionUtils.isNotEmpty(bomEoIds)) {
                        for (String eoId : bomEoIds) {
                            MtAssembleConfirmActualVO28 inputEoInfo = eoInfoMap.get(eoId);

                            if (null == eoMap.get(inputEoInfo.getEoId())) {
                                throw new MtException("MT_ASSEMBLE_0004",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "MT_ASSEMBLE_0004", "ASSEMBLE", "eoId", apiName));
                            }

                            if (null == eoInfoDataMap.get(eoId)) {
                                MtAssembleProcessActualVO17 eoInfoData = new MtAssembleProcessActualVO17();
                                eoInfoData.setEoId(eoId);
                                eoInfoData.setRouterId(MtBaseConstants.LONG_SPECIAL);
                                eoInfoData.setRouterStepId(inputEoInfo.getRouterStepId());
                                eoInfoData.setBomId(bomId);
                                eoInfoData.setMaterialInfo(new ArrayList<>());
                                eoInfoDataMap.put(eoId, eoInfoData);
                            }

                            Double perQty = MtIdHelper.isIdNotNull(inputEoInfo.getRouterStepId())
                                            ? routerOpComponentMap.get(inputEoInfo.getRouterStepId()).getPerQty()
                                            : bomComponentQtyMap.get(bomComponent.getBomComponentId());
                            BigDecimal qty = BigDecimal.valueOf(inputEoInfo.getQty()).multiply(
                                            BigDecimal.valueOf(perQty == null ? Double.valueOf(0.0D) : perQty));
                            String locatorId = locatorIdMap
                                            .get(new MtEoComponentActualVO(eoId, bomComponent.getMaterialId(), null,
                                                            operationIdMap.get(inputEoInfo.getRouterStepId())));

                            MtAssembleProcessActualVO11 materialInfoData = new MtAssembleProcessActualVO11();
                            materialInfoData.setMaterialId(bomComponent.getMaterialId());
                            materialInfoData.setBomComponentId(bomComponent.getBomComponentId());
                            materialInfoData.setTrxAssembleQty(qty.doubleValue());
                            materialInfoData.setAssembleExcessFlag(MtBaseConstants.NO);
                            materialInfoData.setLocatorId(locatorId);
                            materialInfoData.setAssembleMethod("INV_BACKFLASH");
                            eoInfoDataMap.get(eoId).getMaterialInfo().add(materialInfoData);

                            MtInvOnhandQuantityVO13 updateOnhandVO = new MtInvOnhandQuantityVO13();
                            updateOnhandVO.setSiteId(eoMap.get(inputEoInfo.getEoId()).getSiteId());
                            updateOnhandVO.setMaterialId(bomComponent.getMaterialId());
                            updateOnhandVO.setLocatorId(locatorId);
                            updateOnhandVO.setLotCode("");
                            updateOnhandVO.setChangeQuantity(qty.doubleValue());
                            onhandList.add(updateOnhandVO);
                        }
                    }
                }

                MtAssembleProcessActualVO16 assembleProcessActualData = new MtAssembleProcessActualVO16();
                assembleProcessActualData.setWorkcellId(dto.getWorkcellId());
                assembleProcessActualData.setParentEventId(dto.getParentEventId());
                assembleProcessActualData.setEventRequestId(dto.getEventRequestId());
                assembleProcessActualData.setShiftCode(dto.getShiftCode());
                assembleProcessActualData.setShiftDate(dto.getShiftDate());
                assembleProcessActualData.setEoInfo(Lists.newArrayList(eoInfoDataMap.values().iterator()));
                before = System.currentTimeMillis();
                mtAssembleProcessActualRepository.componentAssembleBatchProcess(tenantId, assembleProcessActualData);
                System.out.println("eoWkcBackflushBatchProcess -> componentAssembleBatchProcess 执行时间(总): "
                                + (System.currentTimeMillis() - before));

                before = System.currentTimeMillis();
                mtInvOnhandQuantityRepository.onhandQtyUpdateBatchProcess(tenantId, onhandProcessOnhand);
                System.out.println("eoWkcBackflushBatchProcess -> onhandQtyUpdateBatchProcess 执行时间(总): "
                                + (System.currentTimeMillis() - before));
            }

            if (CollectionUtils.isNotEmpty(feedingList)) {
                for (MtBomComponentVO13 bomComponent : feedingList) {
                    String bomId = bomComponentBomMap.get(bomComponent.getBomComponentId());
                    List<String> bomEoIds = bomEoMap.get(bomId);
                    if (CollectionUtils.isNotEmpty(bomEoIds)) {
                        for (String eoId : bomEoIds) {
                            MtAssembleConfirmActualVO28 inputEoInfo = eoInfoMap.get(eoId);
                            Double perQty = null;
                            if (MtIdHelper.isIdNotNull(inputEoInfo.getRouterStepId())) {
                                perQty = routerOpComponentMap.get(inputEoInfo.getRouterStepId()).getPerQty();
                            } else {
                                perQty = bomComponentQtyMap.get(bomComponent.getBomComponentId());
                            }
                            // 根据获取结果列表循环调用
                            MtAssembleProcessActualVO5 assembleProcessVO5 = new MtAssembleProcessActualVO5();
                            assembleProcessVO5.setEoId(inputEoInfo.getEoId());
                            assembleProcessVO5.setWorkcellId(dto.getWorkcellId());
                            assembleProcessVO5.setAgWorkcellId(dto.getAgWorkcellId());
                            assembleProcessVO5.setMaterialId(bomComponent.getMaterialId());
                            assembleProcessVO5.setBomComponentId(bomComponent.getBomComponentId());
                            assembleProcessVO5.setRouterStepId(inputEoInfo.getRouterStepId());
                            BigDecimal qty = BigDecimal.valueOf(inputEoInfo.getQty()).multiply(
                                            BigDecimal.valueOf(perQty == null ? Double.valueOf(0.0D) : perQty));
                            assembleProcessVO5.setTrxAssembleQty(qty.doubleValue());
                            assembleProcessVO5.setAssembleExcessFlag(MtBaseConstants.NO);
                            assembleProcessVO5.setReferenceArea(dto.getReferenceArea());
                            assembleProcessVO5.setOperationBy(dto.getOperationBy());
                            assembleProcessVO5.setParentEventId(dto.getParentEventId());
                            assembleProcessVO5.setEventRequestId(dto.getEventRequestId());
                            assembleProcessVO5.setShiftDate(shiftDate);
                            assembleProcessVO5.setShiftCode(dto.getShiftCode());

                            // 获取装配货位
                            String locatorId = locatorIdMap.get(new MtEoComponentActualVO(eoId,
                                            bomComponent.getMaterialId(), bomComponent.getBomComponentType(),
                                            operationIdMap.get(inputEoInfo.getRouterStepId())));
                            assembleProcessVO5.setLocatorId(locatorId);
                            assembleProcessVO5.setLocatorId(locatorId);
                            mtAssembleProcessActualRepository.eoAssembleGroupComponentAssembleProcess(tenantId,
                                            assembleProcessVO5);
                        }

                    }


                }

            }
        }
        System.out.println("eoWkcBackflushBatchProcess 执行时间(总): " + (System.currentTimeMillis() - startTime));
    }

}
