package com.ruike.wms.app.service.impl;

import com.ruike.wms.app.service.WmsEventService;
import com.ruike.wms.domain.vo.WmsEventVO;
import org.springframework.stereotype.Service;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;

/**
 * 事件服务实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/9 11:49
 */
@Service
public class WmsEventServiceImpl implements WmsEventService {
    private final MtEventRequestRepository mtEventRequestRepository;
    private final MtEventRepository mtEventRepository;

    public WmsEventServiceImpl(MtEventRequestRepository mtEventRequestRepository, MtEventRepository mtEventRepository) {
        this.mtEventRequestRepository = mtEventRequestRepository;
        this.mtEventRepository = mtEventRepository;
    }

    @Override
    public WmsEventVO createEventOnly(Long tenantId, String eventType) {
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventTypeCode(eventType);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
        return new WmsEventVO.Builder().requestTypeCode(eventType).eventId(eventId).build();
    }

    @Override
    public WmsEventVO createEventRequest(Long tenantId, String eventType) {
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, eventType);
        return new WmsEventVO.Builder().requestTypeCode(eventType).eventRequestId(eventRequestId).build();
    }

    @Override
    public WmsEventVO createEventWithRequest(Long tenantId, String eventType) {
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, eventType);
        return createEventByRequestId(tenantId, eventType, eventRequestId);
    }

    @Override
    public WmsEventVO createEventByRequestId(Long tenantId, String eventType, String eventRequestId) {
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventTypeCode(eventType);
        eventCreate.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
        return new WmsEventVO.Builder().requestTypeCode(eventType).eventRequestId(eventRequestId).eventId(eventId).build();
    }

    @Override
    public WmsEventVO createEventRequestWithParent(Long tenantId, String parentEventId, String eventType, String eventRequestId) {
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventTypeCode(eventType);
        eventCreate.setEventRequestId(eventRequestId);
        eventCreate.setParentEventId(parentEventId);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
        return new WmsEventVO.Builder().requestTypeCode(eventType).eventRequestId(eventRequestId).eventId(eventId).build();
    }
}
