package tarzan.method.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtBomSubstituteHis;
import tarzan.method.domain.repository.MtBomSubstituteHisRepository;
import tarzan.method.infra.mapper.MtBomSubstituteHisMapper;

/**
 * 装配清单行替代项历史 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Component
public class MtBomSubstituteHisRepositoryImpl extends BaseRepositoryImpl<MtBomSubstituteHis>
                implements MtBomSubstituteHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtBomSubstituteHisMapper mtBomSubstituteHisMapper;

    /**
     * bomReferencePointHisQuery-获取装配清单组件行参考点历史
     *
     * @param tenantId
     * @param bomSubstituteId
     * @return
     */
    @Override
    public List<MtBomSubstituteHis> bomSubstituteHisQuery(Long tenantId, String bomSubstituteId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(bomSubstituteId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomSubstituteGroupId", "【API:bomSubstituteHisQuery】"));
        }

        return mtBomSubstituteHisMapper.selectByBomSubstituteId(tenantId, bomSubstituteId);
    }

    @Override
    public List<MtBomSubstituteHis> eventLimitBomSubstituteHisBatchQuery(Long tenantId, List<String> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "eventId", "【API:eventLimitBomSubstituteHisBatchQuery】"));
        }
        return this.mtBomSubstituteHisMapper.selectByEventIds(tenantId, eventIds);
    }
}
