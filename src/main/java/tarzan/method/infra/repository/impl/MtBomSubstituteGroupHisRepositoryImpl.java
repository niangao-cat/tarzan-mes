package tarzan.method.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtBomSubstituteGroupHis;
import tarzan.method.domain.repository.MtBomSubstituteGroupHisRepository;
import tarzan.method.infra.mapper.MtBomSubstituteGroupHisMapper;

/**
 * 装配清单行替代组历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Component
public class MtBomSubstituteGroupHisRepositoryImpl extends BaseRepositoryImpl<MtBomSubstituteGroupHis>
                implements MtBomSubstituteGroupHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtBomSubstituteGroupHisMapper mtBomSubstituteGroupHisMapper;

    /**
     * bomSubstituteGroupHisQuery-获取装配清单组件行替代组历史
     *
     * @param tenantId
     * @param bomSubstituteGroupId
     * @return
     */
    @Override
    public List<MtBomSubstituteGroupHis> bomSubstituteGroupHisQuery(Long tenantId, String bomSubstituteGroupId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(bomSubstituteGroupId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomSubstituteGroupId", "【API:bomSubstituteGroupHisQuery】"));
        }

        return mtBomSubstituteGroupHisMapper.selectByBomSubstituteGroupId(tenantId, bomSubstituteGroupId);
    }

    @Override
    public List<MtBomSubstituteGroupHis> eventLimitBomSubstituteGroupHisBatchQuery(Long tenantId,
                    List<String> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "eventId", "【API:eventLimitBomSubstituteGroupHisBatchQuery】"));
        }
        return this.mtBomSubstituteGroupHisMapper.selectBomSubstituteGroupByEventIds(tenantId, eventIds);
    }

}
