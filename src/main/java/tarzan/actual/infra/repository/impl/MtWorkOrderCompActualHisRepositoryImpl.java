package tarzan.actual.infra.repository.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtWorkOrderCompActualHis;
import tarzan.actual.domain.repository.MtWorkOrderCompActualHisRepository;
import tarzan.actual.domain.vo.MtWoComponentActualVO7;
import tarzan.actual.infra.mapper.MtWorkOrderCompActualHisMapper;
import tarzan.general.domain.entity.MtEvent;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventVO1;

/**
 * 生产订单组件装配实绩历史，记录生产订单物料和组件实际装配情况变更历史 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtWorkOrderCompActualHisRepositoryImpl extends BaseRepositoryImpl<MtWorkOrderCompActualHis>
                implements MtWorkOrderCompActualHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtWorkOrderCompActualHisMapper mtWorkOrderComponentActualHisMapper;

    @Override
    public List<MtWoComponentActualVO7> woComponentActualLimitHisQuery(Long tenantId,
                    String workOrderComponentActualId) {
        if (StringUtils.isEmpty(workOrderComponentActualId)) {
            throw new MtException("MT_ORDER_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ORDER_0001", "ORDER",
                                            "workOrderComponentActualId", "【API:woComponentActualLimitHisQuery】"));
        }

        // 获取历史数据
        List<MtWoComponentActualVO7> woComponentActualHisList =
                        mtWorkOrderComponentActualHisMapper.selectByActualId(tenantId, workOrderComponentActualId);
        if (CollectionUtils.isEmpty(woComponentActualHisList)) {
            return Collections.emptyList();
        }

        // eventId 集合
        List<String> eventIds = woComponentActualHisList.stream().map(MtWoComponentActualVO7::getEventId).distinct()
                        .collect(Collectors.toList());

        // 获取event属性
        List<MtEventVO1> eventVO1List = mtEventRepository.eventBatchGet(tenantId, eventIds);
        if (CollectionUtils.isNotEmpty(eventVO1List)) {
            // 将event属性按 eventId 转为 map
            Map<String, MtEventVO1> eventVO1Map =
                            eventVO1List.stream().collect(Collectors.toMap(MtEvent::getEventId, t -> t));
            woComponentActualHisList.forEach(t -> {
                MtEventVO1 eventVO1 = eventVO1Map.get(t.getEventId());
                if (eventVO1 != null) {
                    t.setEventTypeCode(eventVO1.getEventTypeCode());
                }
            });
        }

        return woComponentActualHisList;
    }

    @Override
    public List<MtWorkOrderCompActualHis> eventLimitWoComponentActualHisBatchQuery(Long tenantId,
                    List<String> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventIds", "【API:eventLimitWoComponentActualHisBatchQuery】"));
        }

        return mtWorkOrderComponentActualHisMapper.selectByEventIds(tenantId, eventIds);
    }

    @Override
    public List<MtWorkOrderCompActualHis> eventLimitWoComponentActualHisQuery(Long tenantId, String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API:eventLimitWoComponentActualHisQuery】"));
        }

        return mtWorkOrderComponentActualHisMapper.selectByEventId(tenantId, eventId);
    }
}
