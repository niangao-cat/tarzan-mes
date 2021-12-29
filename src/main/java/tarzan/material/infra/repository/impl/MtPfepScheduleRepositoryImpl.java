package tarzan.material.infra.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtPfepSchedule;
import tarzan.material.domain.repository.MtPfepScheduleRepository;
import tarzan.material.infra.mapper.MtPfepScheduleMapper;

/**
 * 物料计划属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Component
public class MtPfepScheduleRepositoryImpl extends BaseRepositoryImpl<MtPfepSchedule>
                implements MtPfepScheduleRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtPfepScheduleMapper mtPfepScheduleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pfepScheduleAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId", "【API:pfepScheduleAttrPropertyUpdate】"));
        }
        // 获取主表数据
        MtPfepSchedule mtPfepSchedule = new MtPfepSchedule();
        mtPfepSchedule.setTenantId(tenantId);
        mtPfepSchedule.setPfepScheduleId(dto.getKeyId());
        mtPfepSchedule = mtPfepScheduleMapper.selectOne(mtPfepSchedule);
        if (mtPfepSchedule == null || StringUtils.isEmpty(mtPfepSchedule.getPfepScheduleId())) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            "keyId:" + dto.getKeyId(), "mt_pfep_schedule",
                                            "【API:pfepScheduleAttrPropertyUpdate】"));
        }

        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_pfep_schedule_attr", dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }
}
