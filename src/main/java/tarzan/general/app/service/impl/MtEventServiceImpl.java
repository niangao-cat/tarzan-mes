package tarzan.general.app.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtUserVO3;
import io.tarzan.common.infra.feign.MtUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tarzan.general.api.dto.MtEventDTO;
import tarzan.general.api.dto.MtEventDTO2;
import tarzan.general.api.dto.MtEventDTO3;
import tarzan.general.app.service.MtEventService;
import tarzan.general.domain.vo.MtEventVO5;
import tarzan.general.infra.mapper.MtEventMapper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 事件记录应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@Service
public class MtEventServiceImpl implements MtEventService {
    private static final String YES = "Y";
    private static final String NO = "N";

    @Autowired
    private MtUserService mtUserService;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtEventMapper mtEventMapper;

    @Override
    public Page<MtEventDTO2> eventUnionRequestGroupQueryForUi(Long tenantId, MtEventDTO dto, PageRequest pageRequest) {
        // 判断必输字段
        if (dto == null || dto.getEndTime() == null) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_EVENT_0001", "EVENT", "endTime", "【API:eventUnionRequestGroupQueryForUi】"));
        }
        if (dto.getStartTime() == null) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_EVENT_0001", "EVENT", "startTime", "【API:eventUnionRequestGroupQueryForUi】"));
        }
        // 获取所有的事件ID
        List<String> eventIdList =
                StringUtils.isNotEmpty(dto.getEventIdList()) ? Arrays.asList(dto.getEventIdList().split(","))
                        : null;
        // 获取所有的事件请求ID
        List<String> requestIdList = StringUtils.isNotEmpty(dto.getRequestIdList())
                ? Arrays.asList(dto.getRequestIdList().split(","))
                : null;
        // 因为查询事件请求时需要判断，如果传入参数中包含事件中包含但是事件请求中不包含的属性时，需要联立mt_event表查询
        String eventJudgeFlag;
        if (StringUtils.isNotEmpty(dto.getLocatorId()) || StringUtils.isNotEmpty(dto.getWorkcellId())
                || StringUtils.isNotEmpty(dto.getShiftCode()) || StringUtils.isNotEmpty(dto.getEventIdList())
                || StringUtils.isNotEmpty(dto.getEventTypeCode()) || dto.getShiftDate() != null) {
            eventJudgeFlag = YES;
        } else {
            eventJudgeFlag = NO;
        }
        // 首先获取当前页面需要展示的第一级表格数据(里面包含eventRequest和event)
        Page<MtEventDTO2> page = PageHelper.doPage(pageRequest, () -> mtEventMapper
                .eventUnionRequestGroupQueryForUi(tenantId, dto, eventIdList, requestIdList, eventJudgeFlag));

        // 如果一级表格没有值，直接返回
        if (CollectionUtils.isEmpty(page)) {
            return page;
        }

        // 记录其中事件请求数据ID，获取所有相关的事件信息
        List<String> requestIdParentList =
                page.stream().filter(t -> !t.getEventFlag()).map(MtEventDTO2::getKid).collect(toList());
        List<MtEventDTO3> childrenEventList =
                mtEventMapper.requestBatchLimitQuery(tenantId, dto, eventIdList, requestIdParentList);

        // 调用服务批量获取用户信息
        ResponseEntity<Page<MtUserVO3>> res =
                mtUserService.userByOrganization(tenantId, tenantId, MtBaseConstants.MAX_USER_SIZE);
        List<MtUserVO3> userVO3List = res != null && res.getBody() != null ? res.getBody() : new ArrayList<MtUserVO3>();

        // 记录所有用户信息
        Map<Long, String> userMap = null;
        if (null != userVO3List) {
            userMap = userVO3List.stream().collect(toMap(MtUserVO3::getId, MtUserVO3::getLoginName));
        }

        // 获取所有的库存信息
        List<String> locatorId = new ArrayList<String>();
        locatorId.addAll(page.getContent().stream().map(MtEventDTO2::getLocatorId).collect(toList()));
        locatorId.addAll(childrenEventList.stream().map(MtEventDTO3::getLocatorId).collect(toList()));
        List<MtModLocator> locatorList = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, locatorId);
        Map<String, String> locatorMap = null;
        if (CollectionUtils.isNotEmpty(locatorList)) {
            locatorMap = locatorList.stream().collect(toMap(MtModLocator::getLocatorId, MtModLocator::getLocatorCode));
        }

        // 获取所有的WKC信息
        List<String> wkcId = new ArrayList<String>();
        wkcId.addAll(page.getContent().stream().map(MtEventDTO2::getWorkcellId).collect(toList()));
        wkcId.addAll(childrenEventList.stream().map(MtEventDTO3::getWorkcellId).collect(toList()));
        List<MtModWorkcell> wkcList = mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, wkcId);
        Map<String, String> wkcMap = null;
        if (CollectionUtils.isNotEmpty(wkcList)) {
            wkcMap = wkcList.stream().collect(toMap(MtModWorkcell::getWorkcellId, MtModWorkcell::getWorkcellCode));
        }

        // 准备二级表格页结果数据
        for (MtEventDTO2 ever : page.getContent()) {
            // 拼接用户信息
            if (userMap != null) {
                ever.setOperationUserName(userMap.get(ever.getOperationBy()));
            }
            // 拼接库位信息
            if (locatorMap != null) {
                ever.setLocatorCode(locatorMap.get(ever.getLocatorId()));
            }
            // 拼接WKC信息
            if (wkcMap != null) {
                ever.setWorkcellCode(wkcMap.get(ever.getWorkcellId()));
            }
            // 如果事件类型数据，直接忽略，如果事件请求型数据，获取其二级表格
            if (!ever.getEventFlag()) {
                // 根据请求ID获取其子节点
                List<MtEventDTO3> children = childrenEventList.stream()
                        .filter(t -> ever.getKid().equals(t.getEventRequestId())).collect(toList());
                if (CollectionUtils.isNotEmpty(children)) {
                    for (MtEventDTO3 childrenEver : children) {
                        // 拼接用户信息
                        if (userMap != null) {
                            childrenEver.setOperationUserName(userMap.get(childrenEver.getOperationBy()));
                        }
                        // 拼接库位信息
                        if (locatorMap != null) {
                            childrenEver.setLocatorCode(locatorMap.get(childrenEver.getLocatorId()));
                        }
                        // 拼接WKC信息
                        if (wkcMap != null) {
                            childrenEver.setWorkcellCode(wkcMap.get(childrenEver.getWorkcellId()));
                        }
                    }
                    // 整理好数据后塞入其父节点
                    ever.setChildren(children);
                }
            }
        }

        return page;
    }

    @Override
    public List<MtEventVO5> parentEventQueryForUi(Long tenantId, String parentEventId) {
        if (StringUtils.isEmpty(parentEventId)) {
            return Collections.emptyList();
        }

        List<MtEventVO5> list = mtEventMapper.parentEventQueryForUi(tenantId, parentEventId);
        List<Long> userIdList = new ArrayList<>();
        userIdList.addAll(list.stream().map(MtEventVO5::getEventBy).collect(toList()));
        userIdList.addAll(list.stream().map(MtEventVO5::getRequestBy).collect(toList()));

        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        // locator & workcell info
        List<MtModLocator> locatorList = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId,
                list.stream().map(MtEventVO5::getLocatorId).collect(toList()));

        List<MtModWorkcell> workcellList = mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId,
                list.stream().map(MtEventVO5::getWorkcellId).collect(toList()));

        // user info
        Map<Long, List<MtUserVO3>> userMap = null;
        ResponseEntity<Page<MtUserVO3>> res =
                mtUserService.userByOrganization(tenantId, tenantId, MtBaseConstants.MAX_USER_SIZE);
        List<MtUserVO3> userVO3List = res != null && res.getBody() != null ? res.getBody() : new ArrayList<MtUserVO3>();
        if (null != userVO3List) {
            userMap = userVO3List.stream().collect(Collectors.groupingBy(MtUserVO3::getId));
        }
        for (MtEventVO5 eventVO : list) {
            if (StringUtils.isNotEmpty(eventVO.getLocatorId())) {
                MtModLocator mtModLocator = locatorList.stream()
                        .filter(l -> eventVO.getLocatorId().equals(l.getLocatorId())).findAny().orElse(null);
                eventVO.setLocatorCode(mtModLocator == null ? null : mtModLocator.getLocatorCode());
            }

            if (StringUtils.isNotEmpty(eventVO.getWorkcellId())) {
                MtModWorkcell mtModWorkcell = workcellList.stream()
                        .filter(w -> eventVO.getWorkcellId().equals(w.getWorkcellId())).findAny().orElse(null);
                eventVO.setWorkcellCode(mtModWorkcell == null ? null : mtModWorkcell.getWorkcellCode());
            }

            if (userMap != null) {
                if (CollectionUtils.isNotEmpty(userMap.get(eventVO.getEventBy()))) {
                    eventVO.setEventTypeUserName(userMap.get(eventVO.getEventBy()).get(0).getLoginName());
                }
                if (CollectionUtils.isNotEmpty(userMap.get(eventVO.getRequestBy()))) {
                    eventVO.setRequestTypeUserName(userMap.get(eventVO.getRequestBy()).get(0).getLoginName());
                }
            }

            eventVO.setParentEventFlag(parentEventId.equals(eventVO.getEventId()) ? Boolean.TRUE : Boolean.FALSE);
        }

        return list;
    }
}
