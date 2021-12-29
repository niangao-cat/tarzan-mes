package tarzan.actual.infra.repository.impl;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtWorkOrderActualHis;
import tarzan.actual.domain.repository.MtWorkOrderActualHisRepository;
import tarzan.actual.domain.vo.MtWorkOrderActualHisVO;
import tarzan.actual.domain.vo.MtWorkOrderActualHisVO2;
import tarzan.actual.infra.mapper.MtWorkOrderActualHisMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventVO1;

/**
 * 生产指令实绩历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtWorkOrderActualHisRepositoryImpl extends BaseRepositoryImpl<MtWorkOrderActualHis>
                implements MtWorkOrderActualHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtWorkOrderActualHisMapper mtWorkOrderActualHisMapper;

    @Override
    public List<MtWorkOrderActualHis> woActualHisPropertyQuery(Long tenantId, MtWorkOrderActualHisVO dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId()) && StringUtils.isEmpty(dto.getWorkOrderActualId())
                        && StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032", "ORDER",
                                            "{workOrderId、workOrderActualId、eventId}",
                                            "【API:woActualHisPropertyQuery】"));
        }
        return mtWorkOrderActualHisMapper.queryHis(tenantId, dto);
    }

    @Override
    public List<MtWorkOrderActualHis> eventLimitWoActualHisQuery(Long tenantId, String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API:eventLimitWoActualHisQuery】"));

        }
        MtWorkOrderActualHis tmp = new MtWorkOrderActualHis();
        tmp.setTenantId(tenantId);
        tmp.setEventId(eventId);
        return mtWorkOrderActualHisMapper.select(tmp);
    }

    @Override
    public List<MtWorkOrderActualHis> eventLimitWoActualHisBatchQuery(Long tenantId, List<String> eventId) {
        if (eventId.size() == 0) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API:eventLimitWoActualHisBatchQuery】"));
        }
        return mtWorkOrderActualHisMapper.selectByEventIds(tenantId, eventId);
    }

    @Override
    public List<MtWorkOrderActualHisVO2> woLimitWoActualHisQuery(Long tenantId, MtWorkOrderActualHisVO vo) {
        if (StringUtils.isEmpty(vo.getWorkOrderId()) && StringUtils.isEmpty(vo.getWorkOrderActualId())) {
            throw new MtException("MT_ORDER_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032", "ORDER",
                                            "{workOrderId、workOrderActualId}", "【API:woLimitWoActualHisQuery】"));
        }

        MtWorkOrderActualHisVO tmp = new MtWorkOrderActualHisVO();
        tmp.setWorkOrderActualId(vo.getWorkOrderActualId());
        tmp.setWorkOrderId(vo.getWorkOrderId());

        List<MtWorkOrderActualHis> mtWorkOrderActualHis = woActualHisPropertyQuery(tenantId, tmp);
        if (CollectionUtils.isEmpty(mtWorkOrderActualHis)) {
            return Collections.emptyList();
        }

        final List<MtWorkOrderActualHisVO2> result = new ArrayList<MtWorkOrderActualHisVO2>();
        mtWorkOrderActualHis.forEach(c -> {
            MtWorkOrderActualHisVO2 v1 = new MtWorkOrderActualHisVO2();
            BeanUtils.copyProperties(c, v1);
            result.add(v1);
        });
        List<String> eventIds = mtWorkOrderActualHis.stream().map(MtWorkOrderActualHis::getEventId).collect(toList());
        List<MtEventVO1> mtEvents = this.mtEventRepository.eventBatchGet(tenantId, eventIds);
        if (CollectionUtils.isNotEmpty(mtEvents)) {
            result.stream().forEach(c -> {
                Optional<MtEventVO1> v3 =
                                mtEvents.stream().filter(t -> t.getEventId().equals(c.getEventId())).findFirst();
                if (v3.isPresent()) {
                    c.setEventType(v3.get().getEventTypeCode());
                }
            });
        }
        return result;
    }
}
