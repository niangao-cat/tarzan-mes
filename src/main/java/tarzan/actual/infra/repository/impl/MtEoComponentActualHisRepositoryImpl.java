package tarzan.actual.infra.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtEoComponentActualHis;
import tarzan.actual.domain.repository.MtEoComponentActualHisRepository;
import tarzan.actual.domain.vo.MtEoComponentActualVO3;
import tarzan.actual.infra.mapper.MtEoComponentActualHisMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventVO1;

/**
 * 执行作业组件装配实绩历史，记录执行作业物料和组件实际装配情况变更历史 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtEoComponentActualHisRepositoryImpl extends BaseRepositoryImpl<MtEoComponentActualHis>
                implements MtEoComponentActualHisRepository {

    @Autowired
    private MtEoComponentActualHisMapper mtEoComponentActualHisMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Override
    public List<MtEoComponentActualHis> queryEoComponentHis(Long tenantId, MtEoComponentActualHis dto) {
        return mtEoComponentActualHisMapper.queryEoComponentHis(tenantId, dto);
    }

    @Override
    public List<MtEoComponentActualHis> eventLimitEoComponentActualHisQuery(Long tenantId, String eventId) {
        // Step 1
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API:eventLimitEoComponentActualHisQuery】"));
        }

        MtEoComponentActualHis actualHis = new MtEoComponentActualHis();
        actualHis.setEventId(eventId);
        return self().queryEoComponentHis(tenantId, actualHis);
    }

    @Override
    public List<MtEoComponentActualVO3> eoComponentActualLimitHisQuery(Long tenantId, String eoComponentActualId) {
        // Step 1
        if (StringUtils.isEmpty(eoComponentActualId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoComponentActualId", "【API:eoComponentActualLimitHisQuery】"));
        }

        List<MtEoComponentActualVO3> vo3List = new ArrayList<MtEoComponentActualVO3>();
        List<String> eventList = new ArrayList<String>();

        MtEoComponentActualHis actualHis = new MtEoComponentActualHis();
        actualHis.setEoComponentActualId(eoComponentActualId);
        List<MtEoComponentActualHis> hisList = self().queryEoComponentHis(tenantId, actualHis);
        for (MtEoComponentActualHis his : hisList) {
            MtEoComponentActualVO3 vo3 = new MtEoComponentActualVO3();
            eventList.add(his.getEventId());
            vo3.setEoComponentActualId(his.getEoComponentActualId());
            vo3.setEventId(his.getEventId());
            vo3.setTrxAssembleQty(his.getTrxAssembleQty());
            vo3.setTrxScrappedQty(his.getTrxScrappedQty());
            vo3.setEoId(his.getEoId());
            vo3.setMaterialId(his.getMaterialId());
            vo3.setOperationId(his.getOperationId());
            vo3.setAssembleQty(his.getTrxAssembleQty());
            vo3.setScrappedQty(his.getScrappedQty());
            vo3.setComponentType(his.getComponentType());
            vo3.setBomComponentId(his.getBomComponentId());
            vo3.setBomId(his.getBomId());
            vo3.setRouterStepId(his.getRouterStepId());
            vo3.setAssembleRouterType(his.getAssembleRouterType());
            vo3.setAssembleExcessFlag(his.getAssembleExcessFlag());
            vo3.setSubstituteFlag(his.getSubstituteFlag());
            vo3.setActualLastTime(his.getActualLastTime());
            vo3.setActualFirstTime(his.getActualFirstTime());
            vo3List.add(vo3);
        }
        List<MtEventVO1> eventVO1List = mtEventRepository.eventBatchGet(tenantId, eventList);
        for (MtEoComponentActualVO3 vo3 : vo3List) {
            for (MtEventVO1 vo1 : eventVO1List) {
                if (vo1.getEventId().equals(vo3.getEventId())) {
                    vo3.setEventTypeCode(vo1.getEventTypeCode());
                }
            }
        }
        return vo3List;
    }

    @Override
    public List<MtEoComponentActualHis> eventLimitEoComponentActualHisBatchQuery(Long tenantId, List<String> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventIds", "【API：eventLimitEoComponentActualHisBatchQuery】"));
        }
        return this.mtEoComponentActualHisMapper.selectByEventIds(tenantId, eventIds);
    }

}
