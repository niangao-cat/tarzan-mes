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
import tarzan.order.domain.entity.MtWorkOrderHis;
import tarzan.order.domain.repository.MtWorkOrderHisRepository;
import tarzan.order.domain.vo.MtWorkOrderHisVO;
import tarzan.order.domain.vo.MtWorkOrderHisVO1;
import tarzan.order.domain.vo.MtWorkOrderHisVO2;
import tarzan.order.infra.mapper.MtWorkOrderHisMapper;

/**
 * 生产指令历史 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
@Component
public class MtWorkOrderHisRepositoryImpl extends BaseRepositoryImpl<MtWorkOrderHis>
                implements MtWorkOrderHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtWorkOrderHisMapper mtWorkOrderHisMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Override
    public List<MtWorkOrderHis> woHisPropertyQuery(Long tenantId, MtWorkOrderHisVO condition) {
        if (StringUtils.isEmpty(condition.getWorkOrderId()) && StringUtils.isEmpty(condition.getEventId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0032", "ORDER", "【workOrderId、eventId】", "【API:woHisPropertyQuery】"));
        }
        return this.mtWorkOrderHisMapper.selectByConditionCustom(tenantId, condition);
    }

    @Override
    public List<MtWorkOrderHisVO1> woLimitWoHisQuery(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woLimitWoHisQuery】"));
        }

        MtWorkOrderHisVO condition = new MtWorkOrderHisVO();
        condition.setWorkOrderId(workOrderId);
        List<MtWorkOrderHis> mtWorkOrderHiz = woHisPropertyQuery(tenantId, condition);
        if (CollectionUtils.isEmpty(mtWorkOrderHiz)) {
            return Collections.emptyList();
        }

        final List<MtWorkOrderHisVO1> result = new ArrayList<MtWorkOrderHisVO1>();
        mtWorkOrderHiz.stream().forEach(c -> {
            MtWorkOrderHisVO1 vo = new MtWorkOrderHisVO1();
            BeanUtils.copyProperties(c, vo);
            result.add(vo);
        });

        List<String> eventIds = mtWorkOrderHiz.stream().map(MtWorkOrderHis::getEventId).collect(Collectors.toList());
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
    public List<MtWorkOrderHis> eventLimitWoHisQuery(Long tenantId, String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API:eventLimitWoHisQuery】"));
        }

        MtWorkOrderHis mtWorkOrderHis = new MtWorkOrderHis();
        mtWorkOrderHis.setTenantId(tenantId);
        mtWorkOrderHis.setEventId(eventId);
        return this.mtWorkOrderHisMapper.select(mtWorkOrderHis);
    }

    @Override
    public List<MtWorkOrderHis> eventLimitWoHisBatchQuery(Long tenantId, List<String> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API:eventLimitWoHisBatchQuery】"));
        }
        return this.mtWorkOrderHisMapper.selectByEventIds(tenantId, eventIds);
    }

    /**
     * woLatestHisGet-获取生产指令最新历史
     *
     * @author chuang.yang
     * @date 2019/9/27
     * @param tenantId
     * @param workOrderId
     * @return tarzan.order.domain.vo.MtWorkOrderHisVO2
     */
    @Override
    public MtWorkOrderHisVO2 woLatestHisGet(Long tenantId, String workOrderId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001", "ORDER",
                            "workOrderId", "【API：woLatestHisGet】"));
        }

        return mtWorkOrderHisMapper.selectLatestHis(tenantId, workOrderId);
    }

}
