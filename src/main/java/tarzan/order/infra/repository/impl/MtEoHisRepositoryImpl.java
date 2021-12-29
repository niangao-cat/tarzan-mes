package tarzan.order.infra.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventVO1;
import tarzan.order.domain.entity.MtEoHis;
import tarzan.order.domain.repository.MtEoHisRepository;
import tarzan.order.domain.vo.MtEoHisVO;
import tarzan.order.domain.vo.MtEoHisVO1;
import tarzan.order.domain.vo.MtEoHisVO2;
import tarzan.order.infra.mapper.MtEoHisMapper;

/**
 * 执行作业历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@Component
public class MtEoHisRepositoryImpl extends BaseRepositoryImpl<MtEoHis> implements MtEoHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoHisMapper mtEoHisMapper;

    @Autowired
    private MtEventRepository mtEventRepository;


    @Override
    public List<MtEoHis> eoHisPropertyQuery(Long tenantId, MtEoHisVO condition) {
        if (StringUtils.isEmpty(condition.getEoId()) && StringUtils.isEmpty(condition.getEventId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0032", "ORDER", "【eoId、eventId】", "【API:eoHisPropertyQuery】"));
        }

        return this.mtEoHisMapper.selectByConditionCustom(tenantId, condition);
    }

    @Override
    public List<MtEoHisVO1> eoLimitEoHisQuery(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoLimitEoHisQuery】"));
        }

        MtEoHisVO condition = new MtEoHisVO();
        condition.setEoId(eoId);
        List<MtEoHis> mtEoHiz = eoHisPropertyQuery(tenantId, condition);
        if (CollectionUtils.isEmpty(mtEoHiz)) {
            return Collections.emptyList();
        }

        final List<MtEoHisVO1> result = new ArrayList<MtEoHisVO1>();
        mtEoHiz.stream().forEach(c -> {
            MtEoHisVO1 vo = new MtEoHisVO1();
            BeanUtils.copyProperties(c, vo);
            result.add(vo);
        });

        List<String> eventIds = mtEoHiz.stream().map(MtEoHis::getEventId).collect(Collectors.toList());
        List<MtEventVO1> mtEvents = this.mtEventRepository.eventBatchGet(tenantId, eventIds);
        if (CollectionUtils.isNotEmpty(mtEvents)) {
            result.stream().forEach(c -> {
                Optional<MtEventVO1> vo =
                        mtEvents.stream().filter(t -> t.getEventId().equals(c.getEventId())).findFirst();
                if (vo.isPresent()) {
                    c.setEventType(vo.get().getEventTypeCode());
                }
            });
        }
        return result;
    }

    @Override
    public List<MtEoHis> eventLimitEoHisBatchQuery(Long tenantId, List<String> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eventId", "【API:eventLimitEoHisBatchQuery】"));
        }
        return this.mtEoHisMapper.selectByEventIds(tenantId, eventIds);
    }

    @Override
    public List<MtEoHis> eventLimitEoHisQuery(Long tenantId, String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eventId", "【API:eventLimitEoHisQuery】"));
        }

        MtEoHis mtEoHis = new MtEoHis();
        mtEoHis.setTenantId(tenantId);
        mtEoHis.setEventId(eventId);
        return this.mtEoHisMapper.select(mtEoHis);
    }

    @Override
    public MtEoHisVO2 eoLatestHisGet(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoLatestHisGet】"));
        }
        return mtEoHisMapper.selectRecent(tenantId, eoId);
    }
}
