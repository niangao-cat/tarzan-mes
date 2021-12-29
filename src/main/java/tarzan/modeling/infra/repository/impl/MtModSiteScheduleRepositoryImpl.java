package tarzan.modeling.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.NumberHelper;
import tarzan.modeling.domain.entity.MtModSiteSchedule;
import tarzan.modeling.domain.repository.MtModSiteScheduleRepository;
import tarzan.modeling.domain.vo.MtModSiteScheduleVO;
import tarzan.modeling.infra.mapper.MtModSiteScheduleMapper;

/**
 * 站点计划属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@Component
public class MtModSiteScheduleRepositoryImpl extends BaseRepositoryImpl<MtModSiteSchedule>
                implements MtModSiteScheduleRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModSiteScheduleMapper mtModSiteScheduleMapper;

    @Override
    public MtModSiteSchedule siteSchedulePropertyGet(Long tenantId, String siteId) {
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "siteId", "【API:siteSchedulePropertyGet】"));
        }

        MtModSiteSchedule mtModSiteSchedule = new MtModSiteSchedule();
        mtModSiteSchedule.setTenantId(tenantId);
        mtModSiteSchedule.setSiteId(siteId);
        return this.mtModSiteScheduleMapper.selectOne(mtModSiteSchedule);
    }

    @Override
    public List<MtModSiteSchedule> siteSchedulePropertyBatchGet(Long tenantId, List<String> siteIds) {
        if (CollectionUtils.isEmpty(siteIds)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "siteId", "【API:siteSchedulePropertyBatchGet】"));
        }
        return mtModSiteScheduleMapper.selectByIdsCustom(tenantId, siteIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void siteSchedulePropertyUpdate(Long tenantId, MtModSiteScheduleVO dto, String fullUpdate) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "siteId", "【API:siteSchedulePropertyUpdate】"));
        }
        if (dto.getDemandTimeFence() != null && !NumberHelper.isDouble(dto.getDemandTimeFence().toString())) {
            throw new MtException("MT_MODELING_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "demandTimeFence", "【API:siteSchedulePropertyUpdate】"));
        }
        if (dto.getFixTimeFence() != null && !NumberHelper.isDouble(dto.getFixTimeFence().toString())) {
            throw new MtException("MT_MODELING_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "fixTimeFence", "【API:siteSchedulePropertyUpdate】"));
        }
        if (dto.getFrozenTimeFence() != null && !NumberHelper.isDouble(dto.getFrozenTimeFence().toString())) {
            throw new MtException("MT_MODELING_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "frozenTimeFence", "【API:siteSchedulePropertyUpdate】"));
        }
        if (dto.getForwardPlanningTimeFence() != null
                        && !NumberHelper.isDouble(dto.getForwardPlanningTimeFence().toString())) {
            throw new MtException("MT_MODELING_0037",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0037", "MODELING",
                                            "forwardPlanningTimeFence", "【API:siteSchedulePropertyUpdate】"));
        }
        if (dto.getReleaseTimeFence() != null && !NumberHelper.isDouble(dto.getReleaseTimeFence().toString())) {
            throw new MtException("MT_MODELING_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "releaseTimeFence", "【API:siteSchedulePropertyUpdate】"));
        }
        if (dto.getOrderTimeFence() != null && !NumberHelper.isDouble(dto.getOrderTimeFence().toString())) {
            throw new MtException("MT_MODELING_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0037", "MODELING", "orderTimeFence", "【API:siteSchedulePropertyUpdate】"));
        }

        // 2. 数据处理
        MtModSiteSchedule oldData = new MtModSiteSchedule();
        oldData.setSiteId(dto.getSiteId());
        oldData.setTenantId(tenantId);
        oldData = mtModSiteScheduleMapper.selectOne(oldData);

        if (oldData == null) {
            // 新增
            MtModSiteSchedule newData = new MtModSiteSchedule();
            newData.setTenantId(tenantId);
            newData.setSiteId(dto.getSiteId());
            newData.setDemandTimeFence(dto.getDemandTimeFence());
            newData.setFixTimeFence(dto.getFixTimeFence());
            newData.setForwardPlanningTimeFence(dto.getForwardPlanningTimeFence());
            newData.setFrozenTimeFence(dto.getFrozenTimeFence());
            newData.setOrderTimeFence(dto.getOrderTimeFence());
            newData.setPlanStartTime(dto.getPlanStartTime());
            newData.setReleaseTimeFence(dto.getReleaseTimeFence());
            self().insertSelective(newData);
        } else {
            if ("Y".equalsIgnoreCase(fullUpdate)) {
                BeanUtils.copyProperties(dto, oldData);
                oldData.setTenantId(tenantId);
                self().updateByPrimaryKey(oldData);
            }
            else {
                // 更新
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
                if (dto.getPlanStartTime() != null) {
                    oldData.setPlanStartTime(dto.getPlanStartTime());
                }
                oldData.setTenantId(tenantId);
                self().updateByPrimaryKeySelective(oldData);
            }
        }
    }


}
