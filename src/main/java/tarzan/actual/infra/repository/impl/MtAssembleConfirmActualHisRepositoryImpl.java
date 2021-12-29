package tarzan.actual.infra.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtAssembleConfirmActualHis;
import tarzan.actual.domain.repository.MtAssembleConfirmActualHisRepository;
import tarzan.actual.domain.vo.MtAssembleConfirmActualHisVO;
import tarzan.actual.domain.vo.MtAssembleConfirmActualHisVO1;
import tarzan.actual.infra.mapper.MtAssembleConfirmActualHisMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventVO1;

/**
 * 装配确认实绩历史，指示执行作业组件材料的装配和确认历史记录情况 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtAssembleConfirmActualHisRepositoryImpl extends BaseRepositoryImpl<MtAssembleConfirmActualHis>
                implements MtAssembleConfirmActualHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtAssembleConfirmActualHisMapper mtAssembleConfirmActualHisMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Override
    public List<MtAssembleConfirmActualHisVO1> assembleConfirmActualLimitHisQuery(Long tenantId,
                                                                                  MtAssembleConfirmActualHisVO dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getAssembleConfirmActualId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleConfirmActualId", "【API:assembleConfirmActualLimitHisQuery】"));
        }

        // Step 2
        MtAssembleConfirmActualHis his = new MtAssembleConfirmActualHis();
        his.setEventId(dto.getEventId());
        his.setAssembleConfirmActualId(dto.getAssembleConfirmActualId());
        List<MtAssembleConfirmActualHis> list = mtAssembleConfirmActualHisMapper.mySelect(tenantId, his);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        final List<MtAssembleConfirmActualHisVO1> result = new ArrayList<MtAssembleConfirmActualHisVO1>();
        list.stream().forEach(c -> {
            MtAssembleConfirmActualHisVO1 assembleConfirmHisVO = new MtAssembleConfirmActualHisVO1();
            assembleConfirmHisVO.setAssembleConfirmActualId(c.getAssembleConfirmActualId());
            assembleConfirmHisVO.setEventId(c.getEventId());
            assembleConfirmHisVO.setEventTypeCode(null);
            assembleConfirmHisVO.setEoId(c.getEoId());
            assembleConfirmHisVO.setMaterialId(c.getMaterialId());
            assembleConfirmHisVO.setOperationId(c.getOperationId());
            assembleConfirmHisVO.setComponentType(c.getComponentType());
            assembleConfirmHisVO.setBomComponentId(c.getBomComponentId());
            assembleConfirmHisVO.setBomId(c.getBomId());
            assembleConfirmHisVO.setRouterStepId(c.getRouterStepId());
            assembleConfirmHisVO.setAssembleExcessFlag(c.getAssembleExcessFlag());
            assembleConfirmHisVO.setAssembleRouterType(c.getAssembleRouterType());
            assembleConfirmHisVO.setSubstituteFlag(c.getSubstituteFlag());
            assembleConfirmHisVO.setBypassFlag(c.getBypassFlag());
            assembleConfirmHisVO.setBypassBy(c.getBypassBy());
            assembleConfirmHisVO.setConfirmFlag(c.getConfirmFlag());
            assembleConfirmHisVO.setConfirmedBy(c.getConfirmedBy());
            result.add(assembleConfirmHisVO);
        });

        // Step 3
        List<String> eventIds =
                        result.stream().map(MtAssembleConfirmActualHisVO1::getEventId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(eventIds)) {
            return result;
        }

        List<MtEventVO1> eventVO1List = mtEventRepository.eventBatchGet(tenantId, eventIds);
        if (CollectionUtils.isEmpty(eventVO1List)) {
            return result;
        }

        for (MtAssembleConfirmActualHisVO1 vo : result) {
            Optional<MtEventVO1> optional =
                            eventVO1List.stream().filter(c -> vo.getEventId().equals(c.getEventId())).findFirst();
            if (optional.isPresent()) {
                vo.setEventTypeCode(optional.get().getEventTypeCode());
            }
        }

        List<MtAssembleConfirmActualHisVO1> list1 = result.stream().filter(c -> null != c.getEventId())
                        .sorted(Comparator.comparingDouble(
                                        (MtAssembleConfirmActualHisVO1 t) -> Double.valueOf(t.getEventId())))
                        .collect(Collectors.toList());
        List<MtAssembleConfirmActualHisVO1> list2 =
                        result.stream().filter(c -> null == c.getEventId()).collect(Collectors.toList());
        list1.addAll(list2);

        return list1;
    }

    /**
     * 批量获取指定事件的装配确认实绩/sen.luo 2018-03-22
     *
     * @param tenantId
     * @param eventIds
     * @return
     */
    @Override
    public List<MtAssembleConfirmActualHis> eventLimitAssembleConfirmActualHisBatchQuery(Long tenantId,
                                                                                         List<String> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "eventdIds", "【API:eventLimitAssembleConfirmActualHisBatchQuery】"));
        }
        return mtAssembleConfirmActualHisMapper.selectByEventIds(tenantId, eventIds);
    }

    /**
     * 获取指定事件的装配确认实绩/sen.luo 2018-03-22
     *
     * @param tenantId
     * @param eventId
     * @return
     */
    @Override
    public List<MtAssembleConfirmActualHis> eventLimitAssembleConfirmActualHisQuery(Long tenantId, String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "eventId", "【API:eventLimitAssembleConfirmActualHisQuery】"));
        }

        MtAssembleConfirmActualHis his = new MtAssembleConfirmActualHis();
        his.setTenantId(tenantId);
        his.setEventId(eventId);
        return mtAssembleConfirmActualHisMapper.select(his);
    }

}
