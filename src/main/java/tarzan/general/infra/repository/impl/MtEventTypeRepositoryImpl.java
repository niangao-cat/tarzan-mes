package tarzan.general.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.domain.entity.MtEventType;
import tarzan.general.domain.repository.MtEventTypeRepository;
import tarzan.general.domain.vo.MtEventTypeVO;
import tarzan.general.infra.mapper.MtEventTypeMapper;

/**
 * 事件类型定义 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@Component
public class MtEventTypeRepositoryImpl extends BaseRepositoryImpl<MtEventType> implements MtEventTypeRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEventTypeMapper mtEventTypeMapper;

    @Override
    public List<String> propertyLimitEventTypeQuery(Long tenantId, MtEventTypeVO dto) {
        if (StringUtils.isEmpty(dto.getDefaultEventTypeFlag()) && StringUtils.isEmpty(dto.getDescription())
                        && StringUtils.isEmpty(dto.getEnableFlag()) && StringUtils.isEmpty(dto.getEventTypeCode())
                        && StringUtils.isEmpty(dto.getOnhandChangeFlag())
                        && StringUtils.isEmpty(dto.getOnhandChangeType())) {
            throw new MtException("MT_EVENT_0002", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0002", "EVENT", "【API：propertyLimitEventTypeQuery】"));
        }
        return mtEventTypeMapper.propertyLimitEventTypeQuery(tenantId, dto);
    }

    @Override
    public MtEventType eventTypeGet(Long tenantId, String eventTypeId) {
        if (StringUtils.isEmpty(eventTypeId)) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0001", "EVENT", "eventTypeId", "【API：eventTypeGet】"));
        }
        MtEventType mtEventType = new MtEventType();
        mtEventType.setTenantId(tenantId);
        mtEventType.setEventTypeId(eventTypeId);
        return mtEventTypeMapper.selectOne(mtEventType);
    }

    @Override
    public List<MtEventType> eventTypeBatchGet(Long tenantId, List<String> eventTypeIds) {
        if (CollectionUtils.isEmpty(eventTypeIds)) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_EVENT_0001", "EVENT", "eventTypeIdList", "【API：eventTypeBatchGet】"));
        }
        return mtEventTypeMapper.selectByCondition(Condition.builder(MtEventType.class)
                .andWhere(Sqls.custom().andEqualTo(MtEventType.FIELD_TENANT_ID, tenantId)
                        .andIn(MtEventType.FIELD_EVENT_TYPE_ID, eventTypeIds)).build());
    }
}
