package tarzan.modeling.infra.repository.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModAreaSchedule;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.repository.MtModAreaScheduleRepository;
import tarzan.modeling.domain.vo.MtModAreaScheduleVO;
import tarzan.modeling.infra.mapper.MtModAreaScheduleMapper;

/**
 * 区域计划属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Component
public class MtModAreaScheduleRepositoryImpl extends BaseRepositoryImpl<MtModAreaSchedule>
                implements MtModAreaScheduleRepository {


    @Autowired
    private MtErrorMessageRepository MtErrorMessageRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private MtModAreaScheduleMapper mtModAreaScheduleMapper;

    @Override
    public MtModAreaSchedule areaSchedulePropertyGet(Long tenantId, String areaId) {
        if (StringUtils.isEmpty(areaId)) {
            throw new MtException("MT_MODELING_0001", MtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "areaId", "【API:areaSchedulePropertyGet】"));
        }

        MtModAreaSchedule area = new MtModAreaSchedule();
        area.setTenantId(tenantId);
        area.setAreaId(areaId);
        return this.mtModAreaScheduleMapper.selectOne(area);
    }

    @Override
    public List<MtModAreaSchedule> areaSchedulePropertyBatchGet(Long tenantId, List<String> areaIds) {
        if (CollectionUtils.isEmpty(areaIds)) {
            throw new MtException("MT_MODELING_0001", MtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "areaId", "【API:areaSchedulePropertyBatchGet】"));
        }
        return this.mtModAreaScheduleMapper.selectByIdsCustom(tenantId, areaIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void areaSchedulePropertyUpdate(Long tenantId, MtModAreaScheduleVO dto, String fullUpdate) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getAreaId())) {
            throw new MtException("MT_MODELING_0001", MtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "areaId", "【API:areaSchedulePropertyUpdate】"));
        }
        if (dto.getDemandTimeFence() != null
                        && new BigDecimal(dto.getDemandTimeFence().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MODELING_0037", MtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "demandTimeFence", "【API:areaSchedulePropertyUpdate】"));
        }
        if (dto.getFixTimeFence() != null
                        && new BigDecimal(dto.getFixTimeFence().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MODELING_0037", MtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "fixTimeFence", "【API:areaSchedulePropertyUpdate】"));
        }
        if (dto.getFrozenTimeFence() != null
                        && new BigDecimal(dto.getFrozenTimeFence().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MODELING_0037", MtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "frozenTimeFence", "【API:areaSchedulePropertyUpdate】"));
        }
        if (dto.getForwardPlanningTimeFence() != null && new BigDecimal(dto.getForwardPlanningTimeFence().toString())
                        .compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MODELING_0037",
                            MtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0037", "MODELING",
                                            "forwardPlanningTimeFence", "【API:areaSchedulePropertyUpdate】"));
        }
        if (dto.getReleaseTimeFence() != null
                        && new BigDecimal(dto.getReleaseTimeFence().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MODELING_0037", MtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "releaseTimeFence", "【API:areaSchedulePropertyUpdate】"));
        }
        if (dto.getOrderTimeFence() != null
                        && new BigDecimal(dto.getOrderTimeFence().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MODELING_0037", MtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "orderTimeFence", "【API:areaSchedulePropertyUpdate】"));
        }

        if (dto.getDelayTimeFence() != null
                        && new BigDecimal(dto.getDelayTimeFence().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MODELING_0037", MtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "delayTimeFence", "【API:areaSchedulePropertyUpdate】"));
        }

        // 1.1 验证 basicAlgorithm 有效性
        if (StringUtils.isNotEmpty(dto.getBasicAlgorithm())) {
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("MODELING");
            mtGenTypeVO2.setTypeGroup("BASIC_ALGORITHM");
            List<MtGenType> genTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);

            // 所有类型汇总
            List<String> types = genTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (!types.contains(dto.getBasicAlgorithm())) {
                throw new MtException("MT_MODELING_0002", MtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0002", "MODELING", "basicAlgorithm", "【API:areaSchedulePropertyUpdate】"));
            }
        }
        // 1.2 验证 prodLineRule 有效性
        if (StringUtils.isNotEmpty(dto.getProdLineRule())) {
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("MODELING");
            mtGenTypeVO2.setTypeGroup("PROD_LINE_RULE");
            List<MtGenType> genTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);

            // 所有类型汇总
            List<String> types = genTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (!types.contains(dto.getProdLineRule())) {
                throw new MtException("MT_MODELING_0002", MtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0002", "MODELING", "prodLineRule", "【API:areaSchedulePropertyUpdate】"));
            }
        }
        // 1.3 验证 phaseType 有效性
        if (StringUtils.isNotEmpty(dto.getPhaseType())) {
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("MODELING");
            mtGenTypeVO2.setTypeGroup("PLANNING_PHASE_TIME");
            List<MtGenType> genTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);

            // 所有类型汇总
            List<String> types = genTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (!types.contains(dto.getPhaseType())) {
                throw new MtException("MT_MODELING_0002", MtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0002", "MODELING", "phaseType", "【API:areaSchedulePropertyUpdate】"));
            }
        }
        // 1.4 验证 planningBase 有效性
        if (StringUtils.isNotEmpty(dto.getPlanningBase())) {
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("MODELING");
            mtGenTypeVO2.setTypeGroup("PLANNING_BASE");
            List<MtGenType> genTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);

            // 所有类型汇总
            List<String> types = genTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (!types.contains(dto.getPlanningBase())) {
                throw new MtException("MT_MODELING_0002", MtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0002", "MODELING", "planningBase", "【API:areaSchedulePropertyUpdate】"));
            }
        }
        // 1.5 验证 releaseConcurrentRule 有效性
        if (StringUtils.isNotEmpty(dto.getReleaseConcurrentRule())) {
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("MODELING");
            mtGenTypeVO2.setTypeGroup("RELEASE_CONCURRENT_RULE");
            List<MtGenType> genTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);

            // 所有类型汇总
            List<String> types = genTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (!types.contains(dto.getReleaseConcurrentRule())) {
                throw new MtException("MT_MODELING_0002",
                                MtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0002",
                                                "MODELING", "releaseConcurrentRule",
                                                "【API:areaSchedulePropertyUpdate】"));
            }
        }
        // 1.6 验证 followAreaId
        if (StringUtils.isNotEmpty(dto.getFollowAreaId())) {
            MtModArea mtModArea = mtModAreaRepository.areaBasicPropertyGet(tenantId, dto.getFollowAreaId());
            if (mtModArea == null) {
                throw new MtException("MT_MODELING_0005", MtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0005", "MODELING", "followAreaId", "【API:areaSchedulePropertyUpdate】"));
            }
        }

        // 2 数据处理
        MtModAreaSchedule oldData = new MtModAreaSchedule();
        oldData.setTenantId(tenantId);
        oldData.setAreaId(dto.getAreaId());
        oldData = mtModAreaScheduleMapper.selectOne(oldData);

        if (oldData == null) {
            // 新增
            MtModAreaSchedule newData = new MtModAreaSchedule();
            newData.setTenantId(tenantId);
            newData.setAreaId(dto.getAreaId());
            newData.setBasicAlgorithm(dto.getBasicAlgorithm());
            newData.setDelayTimeFence(dto.getDelayTimeFence());
            newData.setDemandTimeFence(dto.getDemandTimeFence());
            newData.setFixTimeFence(dto.getFixTimeFence());
            newData.setFollowAreaId(dto.getFollowAreaId());
            newData.setForwardPlanningTimeFence(dto.getForwardPlanningTimeFence());
            newData.setFrozenTimeFence(dto.getFrozenTimeFence());
            newData.setOrderTimeFence(dto.getOrderTimeFence());
            newData.setPhaseType(dto.getPhaseType());
            newData.setPlanningBase(dto.getPlanningBase());
            newData.setPlanStartTime(dto.getPlanStartTime());
            newData.setProdLineRule(dto.getProdLineRule());
            newData.setReleaseConcurrentRule(dto.getReleaseConcurrentRule());
            newData.setReleaseTimeFence(dto.getReleaseTimeFence());
            self().insertSelective(newData);
        } else {
            if ("Y".equalsIgnoreCase(fullUpdate)) {
                oldData.setPlanStartTime(dto.getPlanStartTime());
                oldData.setDemandTimeFence(dto.getDemandTimeFence());
                oldData.setFixTimeFence(dto.getFixTimeFence());
                oldData.setFrozenTimeFence(dto.getFrozenTimeFence());
                oldData.setForwardPlanningTimeFence(dto.getForwardPlanningTimeFence());
                oldData.setReleaseTimeFence(dto.getReleaseTimeFence());
                oldData.setOrderTimeFence(dto.getOrderTimeFence());
                oldData.setDelayTimeFence(dto.getDelayTimeFence());
                oldData.setBasicAlgorithm(dto.getBasicAlgorithm());
                oldData.setProdLineRule(dto.getProdLineRule());
                oldData.setPhaseType(dto.getPhaseType());
                oldData.setPlanningBase(dto.getPlanningBase());
                oldData.setReleaseConcurrentRule(dto.getReleaseConcurrentRule());
                oldData.setFollowAreaId(dto.getFollowAreaId());
                self().updateByPrimaryKey(oldData);
            } else {
                // 更新
                if (dto.getPlanStartTime() != null) {
                    oldData.setPlanStartTime(dto.getPlanStartTime());
                }
                if (dto.getDemandTimeFence() != null) {
                    oldData.setDemandTimeFence(dto.getDemandTimeFence());
                }
                if (dto.getFixTimeFence() != null) {
                    oldData.setFixTimeFence(dto.getFixTimeFence());
                }
                if (dto.getFrozenTimeFence() != null) {
                    oldData.setFrozenTimeFence(dto.getFrozenTimeFence());
                }
                if (dto.getForwardPlanningTimeFence() != null) {
                    oldData.setForwardPlanningTimeFence(dto.getForwardPlanningTimeFence());
                }
                if (dto.getReleaseTimeFence() != null) {
                    oldData.setReleaseTimeFence(dto.getReleaseTimeFence());
                }
                if (dto.getOrderTimeFence() != null) {
                    oldData.setOrderTimeFence(dto.getOrderTimeFence());
                }
                if (dto.getDelayTimeFence() != null) {
                    oldData.setDelayTimeFence(dto.getDelayTimeFence());
                }
                if (dto.getBasicAlgorithm() != null) {
                    oldData.setBasicAlgorithm(dto.getBasicAlgorithm());
                }
                if (dto.getProdLineRule() != null) {
                    oldData.setProdLineRule(dto.getProdLineRule());
                }
                if (dto.getPhaseType() != null) {
                    oldData.setPhaseType(dto.getPhaseType());
                }
                if (dto.getPlanningBase() != null) {
                    oldData.setPlanningBase(dto.getPlanningBase());
                }
                if (dto.getReleaseConcurrentRule() != null) {
                    oldData.setReleaseConcurrentRule(dto.getReleaseConcurrentRule());
                }
                if (dto.getFollowAreaId() != null) {
                    oldData.setFollowAreaId(dto.getFollowAreaId());
                }
                self().updateByPrimaryKeySelective(oldData);
            }
        }
    }


}
