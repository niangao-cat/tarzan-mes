package tarzan.actual.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtEoRouterActualHis;
import tarzan.actual.domain.repository.MtEoRouterActualHisRepository;
import tarzan.actual.domain.vo.MtEoRouterActualHisVO;
import tarzan.actual.infra.mapper.MtEoRouterActualHisMapper;

/**
 * EO工艺路线实绩历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtEoRouterActualHisRepositoryImpl extends BaseRepositoryImpl<MtEoRouterActualHis>
                implements MtEoRouterActualHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoRouterActualHisMapper mtEoRouterActualHisMapper;

    @Override
    public List<MtEoRouterActualHis> eoRouterActualHisByEventQuery(Long tenantId, String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "【API:eoRouterActualHisByEventQuery】"));
        }

        MtEoRouterActualHis mtEoRouterActualHis = new MtEoRouterActualHis();
        mtEoRouterActualHis.setTenantId(tenantId);
        mtEoRouterActualHis.setEventId(eventId);
        List<MtEoRouterActualHis> mtEoRouterActualHiz = this.mtEoRouterActualHisMapper.select(mtEoRouterActualHis);
        if (CollectionUtils.isEmpty(mtEoRouterActualHiz)) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eventId", "【API:eoRouterActualHisByEventQuery】"));
        }
        return mtEoRouterActualHiz;
    }

    @Override
    public List<MtEoRouterActualHis> eoRouterActualHisQuery(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "【API:eoRouterActualHisQuery】"));
        }

        MtEoRouterActualHis mtEoRouterActualHis = new MtEoRouterActualHis();
        mtEoRouterActualHis.setTenantId(tenantId);
        mtEoRouterActualHis.setEoId(eoId);
        List<MtEoRouterActualHis> mtEoRouterActualHiz = this.mtEoRouterActualHisMapper.select(mtEoRouterActualHis);
        if (CollectionUtils.isEmpty(mtEoRouterActualHiz)) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eoId", "【API:eoRouterActualHisQuery】"));
        }
        return mtEoRouterActualHiz;
    }

    @Override
    public List<MtEoRouterActualHis> eventAndEoRouterLimitEoRouterActualQuery(Long tenantId,
                    MtEoRouterActualHisVO condition) {
        if (StringUtils.isEmpty(condition.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "【API:eventAndEoRouterLimitEoRouterActualQuery】"));
        }

        MtEoRouterActualHis mtEoRouterActualHis = new MtEoRouterActualHis();
        mtEoRouterActualHis.setTenantId(tenantId);
        mtEoRouterActualHis.setEventId(condition.getEventId());
        List<MtEoRouterActualHis> mtEoRouterActualHiz = this.mtEoRouterActualHisMapper.select(mtEoRouterActualHis);
        if (CollectionUtils.isEmpty(mtEoRouterActualHiz)) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eventId", "【API:eventAndEoRouterLimitEoRouterActualQuery】"));
        }

        if (1 == mtEoRouterActualHiz.size()) {
            return mtEoRouterActualHiz;
        } else {
            if (StringUtils.isEmpty(condition.getEoId()) || StringUtils.isEmpty(condition.getRouterId())) {
                throw new MtException("MT_MOVING_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0037", "MOVING", "【API:eventAndEoRouterLimitEoRouterActualQuery】"));
            }

            mtEoRouterActualHis.setEventId(condition.getEventId());
            mtEoRouterActualHis.setEoId(condition.getEoId());
            mtEoRouterActualHis.setRouterId(condition.getRouterId());
            mtEoRouterActualHis.setTenantId(tenantId);
            mtEoRouterActualHiz = this.mtEoRouterActualHisMapper.select(mtEoRouterActualHis);
            if (CollectionUtils.isEmpty(mtEoRouterActualHiz)) {
                throw new MtException("MT_MOVING_0015",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                                "{eventId,eoId,routerId}",
                                                "【API:eventAndEoRouterLimitEoRouterActualQuery】"));
            }
            return mtEoRouterActualHiz;
        }
    }

}
