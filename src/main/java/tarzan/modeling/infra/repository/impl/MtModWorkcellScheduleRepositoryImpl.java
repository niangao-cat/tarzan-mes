package tarzan.modeling.infra.repository.impl;

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
import tarzan.modeling.domain.entity.MtModWorkcellSchedule;
import tarzan.modeling.domain.repository.MtModWorkcellScheduleRepository;
import tarzan.modeling.infra.mapper.MtModWorkcellScheduleMapper;

/**
 * 工作单元计划属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@Component
public class MtModWorkcellScheduleRepositoryImpl extends BaseRepositoryImpl<MtModWorkcellSchedule>
                implements MtModWorkcellScheduleRepository {


    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModWorkcellScheduleMapper mtModWorkcellScheduleMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Override
    public MtModWorkcellSchedule workcellSchedulePropertyGet(Long tenantId, String workcellId) {
        if (StringUtils.isEmpty(workcellId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "workcellId", "【API:workcellSchedulePropertyGet】"));
        }

        MtModWorkcellSchedule mtModWorkcellSchedule = new MtModWorkcellSchedule();
        mtModWorkcellSchedule.setTenantId(tenantId);
        mtModWorkcellSchedule.setWorkcellId(workcellId);
        return this.mtModWorkcellScheduleMapper.selectOne(mtModWorkcellSchedule);
    }

    @Override
    public List<MtModWorkcellSchedule> workcellSchedulePropertyBatchGet(Long tenantId, List<String> workcellIds) {
        if (CollectionUtils.isEmpty(workcellIds)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "workcellId", "【API:workcellSchedulePropertyBatchGet】"));
        }

        return this.mtModWorkcellScheduleMapper.selectByIdsCustom(tenantId, workcellIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void workcellSchedulePropertyUpdate(Long tenantId, MtModWorkcellSchedule dto, String fullUpdate) {
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "workcellId", "【API:workcellSchedulePropertyUpdate】"));
        }

        if (StringUtils.isNotEmpty(dto.getRateType())) {
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("MODELING");
            mtGenTypeVO2.setTypeGroup("RATE_TYPE");
            List<MtGenType> rateTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
            List<String> rateTypeCodes =
                            rateTypes.stream().map(MtGenType::getTypeCode).distinct().collect(Collectors.toList());
            if (!rateTypeCodes.contains(dto.getRateType())) {
                throw new MtException("MT_MODELING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0002", "MODELING", "rateType", "【API:workcellSchedulePropertyUpdate】"));
            }
        }

        MtModWorkcellSchedule mtModWorkcellSchedule = new MtModWorkcellSchedule();
        mtModWorkcellSchedule.setTenantId(tenantId);
        mtModWorkcellSchedule.setWorkcellId(dto.getWorkcellId());
        mtModWorkcellSchedule = this.mtModWorkcellScheduleMapper.selectOne(mtModWorkcellSchedule);
        if (null == mtModWorkcellSchedule) {
            if (null == dto.getActivity()) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "activity", "【API:workcellSchedulePropertyUpdate】"));
            }
            dto.setTenantId(tenantId);
            self().insertSelective(dto);
        } else {
            if ("Y".equalsIgnoreCase(fullUpdate)) {
                if (null == dto.getActivity()) {
                    throw new MtException("MT_MODELING_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001",
                                                    "MODELING", "activity", "【API:workcellSchedulePropertyUpdate】"));
                }
                mtModWorkcellSchedule.setRateType(dto.getRateType());
                mtModWorkcellSchedule.setRate(dto.getRate());
                mtModWorkcellSchedule.setActivity(dto.getActivity());
                mtModWorkcellSchedule.setTenantId(tenantId);
                self().updateByPrimaryKey(mtModWorkcellSchedule);
            } else {
                if (null != dto.getRateType()) {
                    mtModWorkcellSchedule.setRateType(dto.getRateType());
                }
                if (null != dto.getRate()) {
                    mtModWorkcellSchedule.setRate(dto.getRate());
                }
                if (null != dto.getActivity()) {
                    mtModWorkcellSchedule.setActivity(dto.getActivity());
                }
                mtModWorkcellSchedule.setTenantId(tenantId);
                self().updateByPrimaryKeySelective(mtModWorkcellSchedule);
            }
        }
    }


}
