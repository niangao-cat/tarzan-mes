package tarzan.inventory.infra.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import tarzan.general.domain.vo.MtEventVO2;
import tarzan.inventory.domain.entity.MtContainerHis;
import tarzan.inventory.domain.repository.MtContainerHisRepository;
import tarzan.inventory.domain.vo.MtContainerHisVO;
import tarzan.inventory.domain.vo.MtContainerHisVO1;
import tarzan.inventory.domain.vo.MtContainerHisVO2;
import tarzan.inventory.domain.vo.MtContainerHisVO3;
import tarzan.inventory.infra.mapper.MtContainerHisMapper;

/**
 * 容器历史 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
@Component
public class MtContainerHisRepositoryImpl extends BaseRepositoryImpl<MtContainerHis>
                implements MtContainerHisRepository {

    @Autowired
    private MtContainerHisMapper mtContainerHisMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Override
    public List<MtContainerHis> eventLimitContainerHisBatchQuery(Long tenantId, List<String> eventIds) {
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "eventId", "【API：eventLimitContainerHisBatchQuery】"));

        }

        return mtContainerHisMapper.selectMtContainerHisByEventIds(tenantId, eventIds);
    }

    @Override
    public List<MtContainerHis> requestLimitContainerHisQuery(Long tenantId, MtContainerHisVO dto) {

        if (dto == null || StringUtils.isEmpty(dto.getEventRequestId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "eventRequestId", "【API：requestLimitContainerHisQuery】"));
        }
        MtEventVO2 eventVO2 = new MtEventVO2();
        eventVO2.setEventTypeCode(dto.getEventTypeCode());
        eventVO2.setEventRequestId(dto.getEventRequestId());
        List<String> eventIds = mtEventRepository.requestLimitEventQuery(tenantId, eventVO2);
        if (CollectionUtils.isEmpty(eventIds)) {
            return Collections.emptyList();
        }

        return mtContainerHisMapper.selectMtContainerHisByEventIds(tenantId, eventIds);
    }

    @Override
    public List<MtContainerHis> eventLimitContainerHisQuery(Long tenantId, String eventId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eventId", "【API：eventLimitContainerHisQuery】"));

        }
        MtContainerHis mtContainerHis = new MtContainerHis();
        mtContainerHis.setEventId(eventId);
        mtContainerHis.setTenantId(tenantId);
        return mtContainerHisMapper.select(mtContainerHis);
    }

    @Override
    public List<MtContainerHisVO2> containerHisQuery(Long tenantId, MtContainerHisVO1 dto) {

        // Step1验证参数有效性
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API：containerHisQuery】"));

        }

        // Step2根据输入参数限定获取容器历史表MT_CONTAINER_HIS中数据
        List<MtContainerHis> hisList = mtContainerHisMapper.selectMtContainerHisByEventTime(tenantId, dto);

        List<MtContainerHisVO2> list = new ArrayList<MtContainerHisVO2>();
        List<String> eventIds = hisList.stream().map(MtContainerHis::getEventId).collect(Collectors.toList());
        List<MtEventVO1> eventVO1List = mtEventRepository.eventBatchGet(tenantId, eventIds);
        hisList.stream().forEach(t -> {
            MtContainerHisVO2 hisVO2 = new MtContainerHisVO2();
            BeanUtils.copyProperties(t, hisVO2);
            eventVO1List.stream().forEach(tt -> {
                if (t.getEventId().equals(tt.getEventId())) {
                    hisVO2.setEventBy(tt.getEventBy());
                    hisVO2.setEventTime(tt.getEventTime());
                    hisVO2.setEventTypeCode(tt.getEventTypeCode());
                    hisVO2.setRequestTypeCode(tt.getRequestTypeCode());
                }
            });

            list.add(hisVO2);
        });
        return list;
    }

    @Override
    public MtContainerHisVO3 containerLatestHisGet(Long tenantId, String containerId) {
        // 1.必输校验
        if (StringUtils.isEmpty(containerId)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API：containerLatestHisGet】"));
        }
        // 2.根据输入参数containerId依据最大的逻辑在表MT_CONTAINER_HIS获取最新的containerHisId和eventId进行输出
        return mtContainerHisMapper.containerLatestHisGet(tenantId, containerId);
    }

}
