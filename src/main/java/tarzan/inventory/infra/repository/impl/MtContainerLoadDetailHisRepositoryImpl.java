package tarzan.inventory.infra.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventVO1;
import tarzan.general.domain.vo.MtEventVO2;
import tarzan.inventory.domain.entity.MtContainerLoadDetailHis;
import tarzan.inventory.domain.repository.MtContainerLoadDetailHisRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlHisVO;
import tarzan.inventory.domain.vo.MtContLoadDtlHisVO2;
import tarzan.inventory.domain.vo.MtContLoadDtlHisVO3;
import tarzan.inventory.infra.mapper.MtContainerLoadDetailHisMapper;

/**
 * 容器装载明细历史 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
@Component
public class MtContainerLoadDetailHisRepositoryImpl extends BaseRepositoryImpl<MtContainerLoadDetailHis>
                implements MtContainerLoadDetailHisRepository {

    @Autowired
    private MtContainerLoadDetailHisMapper mtContainerLoadDetailHisMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Override
    public List<MtContainerLoadDetailHis> eventLimitContainerLoadDetailBatchQuery(Long tenantId,
                                                                                  List<String> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "eventId",
                                            "【API：eventLimitContainerLoadDetailBatchQuery】"));
        }
        return mtContainerLoadDetailHisMapper.containerLoadDetailBatchQueryByEventId(tenantId, eventIds);
    }

    @Override
    public List<MtContainerLoadDetailHis> requestLimitContainerLoadDetailHisQuery(Long tenantId,
                                                                                  MtContLoadDtlHisVO dto) {
        if (StringUtils.isEmpty(dto.getEventRequestId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "eventRequestId",
                                            "【API：requestLimitContainerLoadDetailHisQuery】"));
        }
        MtEventVO2 eventVO2 = new MtEventVO2();
        eventVO2.setEventRequestId(dto.getEventRequestId());
        eventVO2.setEventTypeCode(dto.getEventTypeCode());

        List<String> eventIds = mtEventRepository.requestLimitEventQuery(tenantId, eventVO2);
        if (CollectionUtils.isEmpty(eventIds)) {
            return Collections.emptyList();
        }

        return mtContainerLoadDetailHisMapper.containerLoadDetailBatchQueryByEventId(tenantId, eventIds);
    }

    @Override
    public List<MtContainerLoadDetailHis> eventLimitContainerLoadDetailQuery(Long tenantId, String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "eventId", "【API：eventLimitContainerLoadDetailQuery】"));
        }
        MtContainerLoadDetailHis mtContainerHis = new MtContainerLoadDetailHis();
        mtContainerHis.setEventId(eventId);
        mtContainerHis.setTenantId(tenantId);
        return mtContainerLoadDetailHisMapper.select(mtContainerHis);
    }

    @Override
    public List<MtContLoadDtlHisVO3> containerLoadDetailHisQuery(Long tenantId, MtContLoadDtlHisVO2 dto) {
        // 第一步校验
        if (StringUtils.isEmpty(dto.getContainerLoadDetailId()) && StringUtils.isEmpty(dto.getContainerId())
                        && StringUtils.isEmpty(dto.getLoadObjectType()) && StringUtils.isEmpty(dto.getLoadObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                            "MATERIAL_LOT",
                                            " containerLoadDetailId、containerId、loadObjectType、loadObjectId",
                                            "【API：containerLoadDetailHisQuery】"));
        }

        // 第二步，根据输入参数限定获取容器历史表
        List<MtContLoadDtlHisVO3> mtContLoadDtlHisVO3s =
                        mtContainerLoadDetailHisMapper.containerLoadDetailHisQuery(tenantId, dto);
        if (CollectionUtils.isEmpty(mtContLoadDtlHisVO3s)) {
            return Collections.emptyList();
        }

        List<String> eventIds = new ArrayList<String>();
        mtContLoadDtlHisVO3s.forEach(t -> {
            eventIds.add(t.getEventId());
        });

        // 3.第三步，根据第二步获取到的所有结果中eventId，调用API{eventBatchGet}获取事件类型eventTypeCode
        // 和请求编码requestTypeCode，结合第二步获取结果进行返回
        List<MtEventVO1> eventVO1s = mtEventRepository.eventBatchGet(tenantId, eventIds);

        for (MtContLoadDtlHisVO3 his : mtContLoadDtlHisVO3s) {
            for (MtEventVO1 eventVO1 : eventVO1s) {
                if (his.getEventId().equals(eventVO1.getEventId())) {
                    his.setEventTypeCode(eventVO1.getEventTypeCode());
                    his.setRequestTypeCode(eventVO1.getRequestTypeCode());
                    break;
                }
            }
        }
        return mtContLoadDtlHisVO3s;
    }



}
