package tarzan.general.infra.repository.impl;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.entity.MtEvent;
import tarzan.general.domain.entity.MtEventRequest;
import tarzan.general.domain.entity.MtEventRequestType;
import tarzan.general.domain.entity.MtEventType;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.*;
import tarzan.general.infra.mapper.MtEventMapper;
import tarzan.general.infra.mapper.MtEventRequestMapper;
import tarzan.general.infra.mapper.MtEventRequestTypeMapper;
import tarzan.general.infra.mapper.MtEventTypeMapper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 事件记录 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@Component
public class MtEventRepositoryImpl extends BaseRepositoryImpl<MtEvent> implements MtEventRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtEventMapper mtEventMapper;
    @Autowired
    private MtEventTypeMapper mtEventTypeMapper;
    @Autowired
    private MtEventRequestMapper mtEventRequestMapper;
    @Autowired
    private MtEventRequestTypeMapper mtEventRequestTypeMapper;

    @Override
    public MtEventVO1 eventGet(Long tenantId, String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0001", "EVENT", "eventId", "【API:eventGet】"));
        }

        MtEvent mtEvent = new MtEvent();
        mtEvent.setTenantId(tenantId);
        mtEvent.setEventId(eventId);
        mtEvent = mtEventMapper.selectOne(mtEvent);
        if (mtEvent == null) {
            return null;
        }

        MtEventVO1 vo = new MtEventVO1();
        BeanUtils.copyProperties(mtEvent, vo);

        if (StringUtils.isNotEmpty(mtEvent.getEventTypeId())) {
            MtEventType mtEventType = new MtEventType();
            mtEventType.setTenantId(tenantId);
            mtEventType.setEventTypeId(mtEvent.getEventTypeId());
            mtEventType = this.mtEventTypeMapper.selectOne(mtEventType);
            if (mtEventType != null) {
                vo.setEventTypeCode(mtEventType.getEventTypeCode());
            }
        }

        if (StringUtils.isNotEmpty(mtEvent.getEventRequestId())) {
            MtEventRequest mtEventRequest = new MtEventRequest();
            mtEventRequest.setTenantId(tenantId);
            mtEventRequest.setEventRequestId(mtEvent.getEventRequestId());
            mtEventRequest = mtEventRequestMapper.selectOne(mtEventRequest);
            if (mtEventRequest != null) {
                MtEventRequestType mtEventRequestType = new MtEventRequestType();
                mtEventRequestType.setTenantId(tenantId);
                mtEventRequestType.setRequestTypeId(mtEventRequest.getRequestTypeId());
                mtEventRequestType = mtEventRequestTypeMapper.selectOne(mtEventRequestType);
                vo.setRequestTypeCode(mtEventRequestType.getRequestTypeCode());
                vo.setRequestTypeId(mtEventRequest.getRequestTypeId());
            }
        }
        return vo;
    }

    @Override
    public List<MtEventVO1> eventBatchGet(Long tenantId, List<String> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0001", "EVENT", "eventId", "【API:eventBatchGet】"));
        }

        List<MtEvent> mtEvents = mtEventMapper.selectEventByIds(tenantId, eventIds);
        if (CollectionUtils.isEmpty(mtEvents)) {
            return Collections.emptyList();
        }

        final List<MtEventVO1> result = new ArrayList<MtEventVO1>();
        final List<String> eventTypeIds = new ArrayList<String>();
        final List<String> eventRequestIds = new ArrayList<>();
        mtEvents.forEach(c -> {
            MtEventVO1 vo = new MtEventVO1();
            BeanUtils.copyProperties(c, vo);
            result.add(vo);
            eventTypeIds.add(c.getEventTypeId());
            eventRequestIds.add(c.getEventRequestId());
        });

        List<MtEventType> mtEventTypes = this.mtEventTypeMapper.selectByIdsCustom(tenantId, eventTypeIds);
        if (CollectionUtils.isNotEmpty(mtEventTypes)) {
            result.forEach(c -> {
                Optional<MtEventType> mtEventType = mtEventTypes.stream()
                                .filter(t -> t.getEventTypeId().equals(c.getEventTypeId())).findFirst();
                mtEventType.ifPresent(eventType -> c.setEventTypeCode(eventType.getEventTypeCode()));
            });
        }

        List<String> requestTypeIds;
        List<MtEventRequest> mtEventRequests = mtEventRequestMapper.selectByIdsCustom(tenantId, eventRequestIds);
        if (CollectionUtils.isNotEmpty(mtEventRequests)) {
            requestTypeIds = mtEventRequests.stream().map(MtEventRequest::getRequestTypeId).distinct()
                            .collect(Collectors.toList());

            List<MtEventRequestType> mtEventRequestTypes =
                            this.mtEventRequestTypeMapper.selectByIdsCustom(tenantId, requestTypeIds);
            if (CollectionUtils.isNotEmpty(mtEventRequestTypes)) {
                result.forEach(c -> {
                    Optional<MtEventRequest> mtEventRequest = mtEventRequests.stream()
                                    .filter(t -> t.getEventRequestId().equals(c.getEventRequestId())).findFirst();

                    if (mtEventRequest.isPresent()) {
                        Optional<MtEventRequestType> mtEventRequestType = mtEventRequestTypes.stream().filter(
                                        t -> t.getRequestTypeId().equals(mtEventRequest.get().getRequestTypeId()))
                                        .findFirst();
                        if (mtEventRequestType.isPresent()) {
                            c.setRequestTypeCode(mtEventRequestType.get().getRequestTypeCode());
                            c.setRequestTypeId(mtEventRequestType.get().getRequestTypeId());
                        }
                    }
                });
            }
        }

        return result;
    }

    @Override
    public List<String> propertyLimitEventQuery(Long tenantId, MtEventVO dto) {
        if (ObjectFieldsHelper.isAllFieldNull(dto)) {
            throw new MtException("MT_EVENT_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0002", "EVENT", "【API:propertyLimitEventQuery】"));
        }

        return mtEventMapper.selectByConditionCustom(tenantId, dto);
    }

    @Override
    public List<String> requestLimitEventQuery(Long tenantId, MtEventVO2 dto) {
        if (StringUtils.isEmpty(dto.getEventRequestId())) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0001", "EVENT", "eventRequestId", "【API:requestLimitEventQuery】"));
        }
        return mtEventMapper.requestLimitEventQuery(tenantId, dto.getEventRequestId(), dto.getEventTypeCode());
    }

    @Override
    public List<MtEventVO3> parentEventQuery(Long tenantId, List<String> eventIds) {
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0001", "EVENT", "eventIds", "【API:parentEventQuery】"));
        }

        return mtEventMapper.selectParentEventId(tenantId, eventIds);
    }

    @Override
    public List<MtEventVO6> propertyLimitRequestAndEventQuery(Long tenantId, MtEventVO4 dto) {
        // 1.校验
        if (dto == null || dto.getEndTime() == null) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0001", "EVENT", "endTime", "【API:propertyLimitRequestAndEventQuery】"));
        }
        if (dto.getStartTime() == null) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0001", "EVENT", "startTime", "【API:propertyLimitRequestAndEventQuery】"));
        }

        if (StringUtils.isNotEmpty(dto.getRequestTypeCode()) && StringUtils.isNotEmpty(dto.getEventTypeCode())) {
            throw new MtException("MT_EVENT_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0018", "EVENT", "【API:propertyLimitRequestAndEventQuery】"));
        }

        // 2.通过事件带出事件请求属性和事件类型属性
        List<MtEventVO5> list = mtEventMapper.propertyLimitRequestAndEventQuery(tenantId, dto);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        List<MtEventVO6> result = new ArrayList<>();

        // 3.组装数据
        for (MtEventVO5 vo : list) {
            // 事件类型数据。
            MtEventVO6 eventType = new MtEventVO6();

            if (StringUtils.isNotEmpty(vo.getLocatorId())) {
                MtModLocator mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(tenantId, vo.getLocatorId());
                if (mtModLocator != null) {
                    eventType.setLocatorCode(mtModLocator.getLocatorCode());
                }
            }
            if (StringUtils.isNotEmpty(vo.getWorkcellId())) {
                MtModWorkcell mtModWorkcell =
                                mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, vo.getWorkcellId());
                if (mtModWorkcell != null) {
                    eventType.setWorkcellCode(mtModWorkcell.getWorkcellCode());
                }
            }

            eventType.setLineType("event");
            eventType.setLineKid(vo.getEventId());
            eventType.setTypeCode(vo.getEventTypeCode());
            eventType.setTypeDescription(vo.getEventTypeDescription());
            eventType.setEventTime(vo.getEventTime());
            eventType.setEventBy(vo.getEventTypeUserName());
            eventType.setParentEventId(vo.getParentEventId());

            eventType.setEventRequestId(vo.getEventRequestId());
            eventType.setShiftCode(vo.getShiftCode());
            eventType.setShiftDate(vo.getShiftDate());

            result.add(eventType);

            if (StringUtils.isNotEmpty(vo.getEventRequestId())) {
                // 存在事件请求
                List<MtEventVO6> filterList =
                                result.stream().filter(t -> t.getEventRequestId().equals(vo.getEventRequestId()))
                                                .collect(Collectors.toList());
                if (filterList.size() == 1) {
                    MtEventVO6 eventRequest = new MtEventVO6();
                    eventRequest.setLineType("request");
                    eventRequest.setEventRequestId(vo.getEventRequestId());
                    eventRequest.setTypeCode(vo.getRequestTypeCode());
                    eventRequest.setTypeDescription(vo.getRequestTypeDescription());
                    eventRequest.setEventTime(vo.getRequestTime());
                    eventRequest.setEventBy(vo.getRequestTypeUserName());
                    result.add(eventRequest);
                }
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String eventCreate(Long tenantId, MtEventCreateVO dto) {
        if (StringUtils.isEmpty(dto.getEventTypeCode())) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0001", "EVENT", "eventTypeCode", "【API:eventCreate】"));
        }

        // 根据 eventRequestId 判断对应数据是否存在
        if (StringUtils.isNotEmpty(dto.getEventRequestId())) {
            MtEventRequest mtEventRequest = new MtEventRequest();
            mtEventRequest.setTenantId(tenantId);
            mtEventRequest.setEventRequestId(dto.getEventRequestId());
            mtEventRequest = mtEventRequestMapper.selectOne(mtEventRequest);
            if (mtEventRequest == null || StringUtils.isEmpty(mtEventRequest.getEventRequestId())) {
                throw new MtException("MT_EVENT_0008",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_EVENT_0008", "EVENT",
                                                "eventRequestId:" + dto.getEventRequestId(), "【API:eventCreate】"));
            }
        }

        // 根据EventTypeCode参数获取EventType信息
        MtEventType mtEventType = new MtEventType();
        mtEventType.setTenantId(tenantId);
        mtEventType.setEventTypeCode(dto.getEventTypeCode());
        mtEventType = mtEventTypeMapper.selectOne(mtEventType);
        if (mtEventType == null || StringUtils.isEmpty(mtEventType.getEventTypeId())
                        || !"Y".equals(mtEventType.getEnableFlag())) {
            throw new MtException("MT_EVENT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0004", "EVENT", "eventTypeCode:" + dto.getEventTypeCode(), "【API:eventCreate】"));
        }

        if (StringUtils.isNotEmpty(dto.getWorkcellId())) {
            MtModWorkcell mtModWorkcell =
                            mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
            if (mtModWorkcell == null || StringUtils.isEmpty(mtModWorkcell.getWorkcellId())
                            || !"Y".equals(mtModWorkcell.getEnableFlag())) {
                throw new MtException("MT_EVENT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_EVENT_0004", "EVENT", "workcellId:" + dto.getWorkcellId(), "【API:eventCreate】"));
            }
        }

        if (StringUtils.isNotEmpty(dto.getLocatorId())) {
            MtModLocator mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(tenantId, dto.getLocatorId());
            if (mtModLocator == null || StringUtils.isEmpty(mtModLocator.getLocatorId())
                            || !"Y".equals(mtModLocator.getEnableFlag())) {
                throw new MtException("MT_EVENT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_EVENT_0004", "EVENT", "locatorId:" + dto.getLocatorId(), "【API:eventCreate】"));
            }
        }

        if (StringUtils.isNotEmpty(dto.getParentEventId())) {
            MtEvent mtEvent = new MtEvent();
            mtEvent.setEventId(dto.getParentEventId());
            mtEvent.setTenantId(tenantId);
            mtEvent = mtEventMapper.selectOne(mtEvent);
            if (mtEvent == null || StringUtils.isEmpty(mtEvent.getEventId())) {
                throw new MtException("MT_EVENT_0008",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_EVENT_0008", "EVENT",
                                                "parentEventId:" + dto.getParentEventId(), "【API:eventCreate】"));
            }
        }

        MtEvent mtEvent = new MtEvent();
        mtEvent.setEventBy(DetailsHelper.getUserDetails() == null?-1L:DetailsHelper.getUserDetails().getUserId());
        mtEvent.setEventTime(new Date());
        mtEvent.setEventTypeId(mtEventType.getEventTypeId());
        mtEvent.setWorkcellId(dto.getWorkcellId());
        mtEvent.setLocatorId(dto.getLocatorId());
        mtEvent.setParentEventId(dto.getParentEventId());
        mtEvent.setEventRequestId(dto.getEventRequestId());
        mtEvent.setShiftCode(dto.getShiftCode());
        mtEvent.setShiftDate(dto.getShiftDate());
        mtEvent.setTenantId(tenantId);

        self().insertSelective(mtEvent);

        return mtEvent.getEventId();
    }

}
