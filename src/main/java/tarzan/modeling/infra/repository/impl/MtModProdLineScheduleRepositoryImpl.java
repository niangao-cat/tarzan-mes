package tarzan.modeling.infra.repository.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.modeling.domain.entity.MtModProdLineSchedule;
import tarzan.modeling.domain.repository.MtModProdLineScheduleRepository;
import tarzan.modeling.domain.vo.MtModProdLineScheduleVO;
import tarzan.modeling.infra.mapper.MtModProdLineScheduleMapper;

/**
 * 生产线计划属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Component
public class MtModProdLineScheduleRepositoryImpl extends BaseRepositoryImpl<MtModProdLineSchedule>
                implements MtModProdLineScheduleRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModProdLineScheduleMapper mtModProdLineScheduleMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Override
    public MtModProdLineSchedule prodLineSchedulePropertyGet(Long tenantId, String prodLineId) {
        if (StringUtils.isEmpty(prodLineId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "prodLineId", "【API:prodLineSchedulePropertyGet】"));
        }

        MtModProdLineSchedule mtModProdLineSchedule = new MtModProdLineSchedule();
        mtModProdLineSchedule.setTenantId(tenantId);
        mtModProdLineSchedule.setProdLineId(prodLineId);
        return this.mtModProdLineScheduleMapper.selectOne(mtModProdLineSchedule);
    }

    @Override
    public List<MtModProdLineSchedule> prodLineSchedulePropertyBatchGet(Long tenantId, List<String> prodLineIds) {
        if (CollectionUtils.isEmpty(prodLineIds)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "prodLineId", "【API:prodLineSchedulePropertyBatchGet】"));
        }

        return this.mtModProdLineScheduleMapper.selectByIdsCustom(tenantId, prodLineIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void prodLineSchedulePropertyUpdate(Long tenantId, MtModProdLineScheduleVO dto, String fullUpdate) {

        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getProdLineId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "prodLineId", "【API:prodLineSchedulePropertyUpdate】"));
        }

        if (dto.getRate() != null && new BigDecimal(dto.getRate().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MODELING_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "rate", "【API:prodLineSchedulePropertyUpdate】"));
        }
        if (dto.getActivity() != null && new BigDecimal(dto.getActivity().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MODELING_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "activity", "【API:prodLineSchedulePropertyUpdate】"));
        }
        if (dto.getDemandTimeFence() != null
                        && new BigDecimal(dto.getDemandTimeFence().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MODELING_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "demandTimeFence", "【API:prodLineSchedulePropertyUpdate】"));
        }
        if (dto.getFixTimeFence() != null
                        && new BigDecimal(dto.getFixTimeFence().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MODELING_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "fixTimeFence", "【API:prodLineSchedulePropertyUpdate】"));
        }
        if (dto.getFrozenTimeFence() != null
                        && new BigDecimal(dto.getFrozenTimeFence().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MODELING_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "frozenTimeFence", "【API:prodLineSchedulePropertyUpdate】"));
        }
        if (dto.getForwardPlanningTimeFence() != null && new BigDecimal(dto.getForwardPlanningTimeFence().toString())
                        .compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MODELING_0037",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0037", "MODELING",
                                            "forwardPlanningTimeFence", "【API:prodLineSchedulePropertyUpdate】"));
        }
        if (dto.getReleaseTimeFence() != null
                        && new BigDecimal(dto.getReleaseTimeFence().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MODELING_0037",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0037", "MODELING",
                                            "releaseTimeFence", "【API:prodLineSchedulePropertyUpdate】"));
        }
        if (dto.getOrderTimeFence() != null
                        && new BigDecimal(dto.getOrderTimeFence().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MODELING_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "orderTimeFence", "【API:prodLineSchedulePropertyUpdate】"));
        }

        // 1.1 验证DispatchMethod有效性
        if (StringUtils.isNotEmpty(dto.getRateType())) {
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("MODELING");
            mtGenTypeVO2.setTypeGroup("RATE_TYPE");
            List<MtGenType> genTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);

            // 所有类型汇总
            List<String> types = genTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (!types.contains(dto.getRateType())) {
                throw new MtException("MT_MODELING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0002", "MODELING", "rateType", "【API:prodLineSchedulePropertyUpdate】"));
            }
        }

        // 2. 数据处理
        MtModProdLineSchedule oldData = new MtModProdLineSchedule();
        oldData.setTenantId(tenantId);
        oldData.setProdLineId(dto.getProdLineId());
        oldData = mtModProdLineScheduleMapper.selectOne(oldData);

        if (oldData == null) {
            // 新增
            // 2.1 校验Activity是否传入
            if (dto.getActivity() == null) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "activity", "【API:prodLineSchedulePropertyUpdate】"));
            }

            MtModProdLineSchedule newData = new MtModProdLineSchedule();
            newData.setTenantId(tenantId);
            newData.setProdLineId(dto.getProdLineId());
            newData.setActivity(dto.getActivity());
            newData.setDemandTimeFence(dto.getDemandTimeFence());
            newData.setFixTimeFence(dto.getFixTimeFence());
            newData.setForwardPlanningTimeFence(dto.getForwardPlanningTimeFence());
            newData.setFrozenTimeFence(dto.getFrozenTimeFence());
            newData.setOrderTimeFence(dto.getOrderTimeFence());
            newData.setRate(dto.getRate());
            newData.setRateType(dto.getRateType());
            newData.setReleaseTimeFence(dto.getReleaseTimeFence());
            self().insertSelective(newData);
        } else {
            if ("Y".equalsIgnoreCase(fullUpdate)) {
                BeanUtils.copyProperties(dto, oldData);
                oldData.setTenantId(tenantId);
                self().updateByPrimaryKey(oldData);
            } else {
                // 更新
                if (dto.getRate() != null) {
                    oldData.setRate(dto.getRate());
                }
                if (dto.getActivity() != null) {
                    oldData.setActivity(dto.getActivity());
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
                if (dto.getRateType() != null) {
                    oldData.setRateType(dto.getRateType());
                }
                oldData.setTenantId(tenantId);
                self().updateByPrimaryKeySelective(oldData);
            }
        }
    }

}
