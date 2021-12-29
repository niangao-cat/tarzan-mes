package tarzan.method.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtBomReferencePointHis;
import tarzan.method.domain.repository.MtBomReferencePointHisRepository;
import tarzan.method.infra.mapper.MtBomReferencePointHisMapper;

/**
 * 装配清单行参考点关系历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Component
public class MtBomReferencePointHisRepositoryImpl extends BaseRepositoryImpl<MtBomReferencePointHis>
                implements MtBomReferencePointHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtBomReferencePointHisMapper mtBomReferencePointHisMapper;

    /**
     * bomReferencePointHisQuery-获取装配清单组件行参考点历史
     *
     * @param tenantId
     * @param bomReferencePointId
     * @return
     */
    @Override
    public List<MtBomReferencePointHis> bomReferencePointHisQuery(Long tenantId, String bomReferencePointId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(bomReferencePointId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomReferencePointId", "【API:bomReferencePointHisQuery】"));
        }
        return mtBomReferencePointHisMapper.selectByBomReferencePointId(tenantId, bomReferencePointId);
    }

    @Override
    public List<MtBomReferencePointHis> eventLimitBomReferencePointHisBatchQuery(Long tenantId, List<String> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "eventId", "【API:eventLimitBomReferencePointHisBatchQuery】"));
        }
        return this.mtBomReferencePointHisMapper.selectBomReferencePointHisByEventIds(tenantId, eventIds);
    }

}
