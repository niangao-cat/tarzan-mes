package tarzan.method.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtBomComponentHis;
import tarzan.method.domain.repository.MtBomComponentHisRepository;
import tarzan.method.infra.mapper.MtBomComponentHisMapper;

/**
 * 装配清单行历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Component
public class MtBomComponentHisRepositoryImpl extends BaseRepositoryImpl<MtBomComponentHis>
                implements MtBomComponentHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtBomComponentHisMapper mtBomComponentHisMapper;

    /**
     * bomComponentHisQuery-获取装配清单组件行历史
     *
     * @param tenantId
     * @param bomComponentId
     * @return
     */
    @Override
    public List<MtBomComponentHis> bomComponentHisQuery(Long tenantId, String bomComponentId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(bomComponentId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomComponentId", "【API:bomComponentHisQuery】"));
        }
        return mtBomComponentHisMapper.selectBybomComponentId(tenantId, bomComponentId);
    }

    @Override
    public List<MtBomComponentHis> eventLimitBomComponentHisBatchQuery(Long tenantId, List<String> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "eventId", "【API:eventLimitBomComponentHisBatchQuery】"));
        }
        return this.mtBomComponentHisMapper.selectByEventIds(tenantId, eventIds);
    }
}
