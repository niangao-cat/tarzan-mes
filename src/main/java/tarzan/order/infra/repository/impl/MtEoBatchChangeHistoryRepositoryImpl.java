package tarzan.order.infra.repository.impl;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.general.domain.entity.MtEvent;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventVO1;
import tarzan.order.domain.entity.MtEoBatchChangeHistory;
import tarzan.order.domain.repository.MtEoBatchChangeHistoryRepository;
import tarzan.order.domain.vo.*;
import tarzan.order.infra.mapper.MtEoBatchChangeHistoryMapper;

/**
 * 执行作业变更记录 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@Component
public class MtEoBatchChangeHistoryRepositoryImpl extends BaseRepositoryImpl<MtEoBatchChangeHistory>
        implements MtEoBatchChangeHistoryRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoBatchChangeHistoryMapper mtEoBatchChangeHistoryMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Override
    public List<MtEoBatchChangeHistoryVO> relTargetEoQuery(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:relTargetEoQuery】"));
        }

        MtEoBatchChangeHistory mtEoBatchChangeHistory = new MtEoBatchChangeHistory();
        mtEoBatchChangeHistory.setTenantId(tenantId);
        mtEoBatchChangeHistory.setSourceEoId(eoId);
        List<MtEoBatchChangeHistory> mtEoBatchChangeHistorys =
                this.mtEoBatchChangeHistoryMapper.select(mtEoBatchChangeHistory);
        if (CollectionUtils.isEmpty(mtEoBatchChangeHistorys)) {
            return Collections.emptyList();
        }

        // 获取事件
        List<String> eventIds = mtEoBatchChangeHistorys.stream().map(MtEoBatchChangeHistory::getEventId).distinct()
                .collect(Collectors.toList());
        List<MtEventVO1> events = mtEventRepository.eventBatchGet(tenantId, eventIds);

        // 转为map数据
        Map<String, MtEventVO1> mtEventMap = null;
        if (CollectionUtils.isNotEmpty(events)) {
            mtEventMap = events.stream().collect(Collectors.toMap(t -> t.getEventId(), t -> t));
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final List<MtEoBatchChangeHistoryVO> resultList = Collections
                .synchronizedList(new ArrayList<MtEoBatchChangeHistoryVO>(mtEoBatchChangeHistorys.size()));

        Map<String, MtEventVO1> finalMtEventMap = mtEventMap;
        mtEoBatchChangeHistorys.stream().forEach(c -> {
            MtEoBatchChangeHistoryVO result = new MtEoBatchChangeHistoryVO();
            result.setTargetEoId(c.getEoId());
            result.setReason(c.getReason());
            result.setCreationDate(format.format(c.getCreationDate()));
            result.setEventId(c.getEventId());
            result.setSequence(c.getSequence());
            result.setTrxQty(c.getSourceTrxQty());
            result.setTargetTrxQty(c.getTrxQty());
            result.setSourceEoStepActualId(c.getSourceEoStepActualId());
            if (MapUtils.isNotEmpty(finalMtEventMap)) {
                MtEventVO1 mtEvent = finalMtEventMap.get(c.getEventId());
                if (mtEvent != null) {
                    result.setEventBy(mtEvent.getEventBy());
                    result.setEventTime(mtEvent.getEventTime());
                }
            }
            resultList.add(result);
        });
        return resultList;
    }

    @Override
    public List<MtEoBatchChangeHistoryVO2> relSourceEoQuery(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:relSourceEoQuery】"));
        }

        MtEoBatchChangeHistory mtEoBatchChangeHistory = new MtEoBatchChangeHistory();
        mtEoBatchChangeHistory.setTenantId(tenantId);
        mtEoBatchChangeHistory.setEoId(eoId);
        List<MtEoBatchChangeHistory> mtEoBatchChangeHistorys =
                this.mtEoBatchChangeHistoryMapper.select(mtEoBatchChangeHistory);
        if (CollectionUtils.isEmpty(mtEoBatchChangeHistorys)) {
            return Collections.emptyList();
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final List<MtEoBatchChangeHistoryVO2> mtEoBatchChangeHistoryVOs = new ArrayList<MtEoBatchChangeHistoryVO2>();
        mtEoBatchChangeHistorys.stream().forEach(c -> {
            MtEoBatchChangeHistoryVO2 vo = new MtEoBatchChangeHistoryVO2();
            vo.setSourceEoId(c.getSourceEoId());
            vo.setReason(c.getReason());
            vo.setCreationDate(format.format(c.getCreationDate()));
            vo.setEventId(c.getEventId());
            vo.setSequence(c.getSequence());
            vo.setSourceTrxQty(c.getSourceTrxQty());
            vo.setTrxQty(c.getTrxQty());
            mtEoBatchChangeHistoryVOs.add(vo);
        });

        List<String> eventIds = mtEoBatchChangeHistoryVOs.stream().filter(t -> StringUtils.isNotEmpty(t.getEventId()))
                .map(MtEoBatchChangeHistoryVO2::getEventId).distinct().collect(Collectors.toList());

        List<MtEventVO1> eventVO1List = null;
        if (CollectionUtils.isNotEmpty(eventIds)) {
            eventVO1List = mtEventRepository.eventBatchGet(tenantId, eventIds);
        }

        // 调用API{ eventBatchGet}，传入eventId，获取eventTime和eventBy作为输出参数输出
        Map<String, MtEventVO1> eventVO1Map = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(eventVO1List)) {
            eventVO1Map = eventVO1List.stream().collect(Collectors.toMap(MtEvent::getEventId, t -> t));
        }

        // 组装数据
        for (MtEoBatchChangeHistoryVO2 historyVO : mtEoBatchChangeHistoryVOs) {
            MtEventVO1 mtEventVO1 = eventVO1Map.get(historyVO.getEventId());
            if (null != mtEventVO1) {
                historyVO.setEventTime(mtEventVO1.getEventTime());
                historyVO.setEventBy(mtEventVO1.getEventBy());
            }
        }

        return mtEoBatchChangeHistoryVOs;
    }

    @Override
    public List<MtEoBatchChangeHistoryVO3> eoRelQuery(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoRelQuery】"));
        }

        List<MtEoBatchChangeHistoryVO3> list = new ArrayList<MtEoBatchChangeHistoryVO3>();
        list.addAll(recEoBatchChangeSourceHis(tenantId, eoId));
        list.addAll(recEoBatchChangeTarHis(tenantId, eoId));
        List<String> eventIds = list.stream().filter(t -> StringUtils.isNotEmpty(t.getEventId()))
                .map(MtEoBatchChangeHistoryVO3::getEventId).distinct().collect(Collectors.toList());
        List<MtEventVO1> events = mtEventRepository.eventBatchGet(tenantId, eventIds);

        // add by peng.yuan 2019-12-01
        Map<String, MtEventVO1> eventVO1Map = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(events)) {
            eventVO1Map = events.stream().collect(Collectors.toMap(MtEvent::getEventId, t -> t));
        }

        // 组装数据
        for (MtEoBatchChangeHistoryVO3 historyVO3 : list) {
            MtEventVO1 mtEventVO1 = eventVO1Map.get(historyVO3.getEventId());
            if (null != mtEventVO1) {
                historyVO3.setEventTime(mtEventVO1.getEventTime());
                historyVO3.setEventBy(mtEventVO1.getEventBy());
            }
        }
        return list;
    }

    /**
     * eoRelUpdate-更新&新增执行作业关系
     *
     * @author chuang.yang
     * @date 2019/12/2
     * @param tenantId
     * @param dto
     * @param fullUpdate
     * @return java.lang.String
     */
    @Override
    public String eoRelUpdate(Long tenantId, MtEoBatchChangeHistoryVO4 dto, String fullUpdate) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eventId", "【API:eoRelUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getReason())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "reason", "【API:eoRelUpdate】"));
        }
        if (!Arrays.asList(MtBaseConstants.REASON.R, MtBaseConstants.REASON.A, MtBaseConstants.REASON.M,
                MtBaseConstants.REASON.P, MtBaseConstants.REASON.S).contains(dto.getReason())) {
            throw new MtException("MT_ORDER_0159", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0159", "ORDER", "reason", "【API:eoRelUpdate】"));
        }

        String eoBatchChangeHistoryId = null;
        if (StringUtils.isNotEmpty(dto.getEoBatchChangeHistoryId())) {
            // 更新模式
            MtEoBatchChangeHistory mtEoBatchChangeHistory =
                    mtEoBatchChangeHistoryMapper.selectByPrimaryKey(dto.getEoBatchChangeHistoryId());
            if (mtEoBatchChangeHistory == null) {
                throw new MtException("MT_ORDER_0161",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0161", "ORDER",
                                "eoBatchChangeHistoryId:" + dto.getEoBatchChangeHistoryId(),
                                "【API:eoRelUpdate】"));
            }

            mtEoBatchChangeHistory.setEoId(dto.getEoId());
            mtEoBatchChangeHistory.setSourceEoId(dto.getSourceEoId());
            mtEoBatchChangeHistory.setReason(dto.getReason());
            mtEoBatchChangeHistory.setSequence(dto.getSequence());
            mtEoBatchChangeHistory.setEventId(dto.getEventId());
            mtEoBatchChangeHistory.setSourceTrxQty(dto.getSourceTrxQty());
            mtEoBatchChangeHistory.setTrxQty(dto.getTrxQty());

            if (MtBaseConstants.YES.equals(fullUpdate)) {
                // 字符串 null -> ""
                mtEoBatchChangeHistory = (MtEoBatchChangeHistory) ObjectFieldsHelper
                        .setStringFieldsEmpty(mtEoBatchChangeHistory);
                self().updateByPrimaryKey(mtEoBatchChangeHistory);
            } else {
                self().updateByPrimaryKeySelective(mtEoBatchChangeHistory);
            }

            eoBatchChangeHistoryId = mtEoBatchChangeHistory.getEoBatchChangeHistoryId();
        } else {
            // 新增模式
            // 验证唯一性约束
            MtEoBatchChangeHistory mtEoBatchChangeHistory = new MtEoBatchChangeHistory();
            mtEoBatchChangeHistory.setTenantId(tenantId);
            mtEoBatchChangeHistory.setSequence(dto.getSequence());
            mtEoBatchChangeHistory.setEventId(dto.getEventId());
            List<MtEoBatchChangeHistory> mtEoBatchChangeHistoryList =
                    mtEoBatchChangeHistoryMapper.select(mtEoBatchChangeHistory);
            if (CollectionUtils.isNotEmpty(mtEoBatchChangeHistoryList)) {
                throw new MtException("MT_ORDER_0151",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0151", "ORDER",
                                "MT_ EO_BATCH_CHANGE_HISTORY", "SEQUENCE,EVENT_ID",
                                "【API:eoRelUpdate】"));
            }

            mtEoBatchChangeHistory.setTenantId(tenantId);
            mtEoBatchChangeHistory.setEoId(dto.getEoId());
            mtEoBatchChangeHistory.setSourceEoId(dto.getSourceEoId());
            mtEoBatchChangeHistory.setReason(dto.getReason());
            mtEoBatchChangeHistory.setSequence(dto.getSequence());
            mtEoBatchChangeHistory.setEventId(dto.getEventId());
            mtEoBatchChangeHistory.setSourceTrxQty(dto.getSourceTrxQty());
            mtEoBatchChangeHistory.setTrxQty(dto.getTrxQty());
            mtEoBatchChangeHistory.setSourceEoStepActualId(dto.getSourceEoStepActualId());

            self().insertSelective(mtEoBatchChangeHistory);
            eoBatchChangeHistoryId = mtEoBatchChangeHistory.getEoBatchChangeHistoryId();
        }

        return eoBatchChangeHistoryId;
    }

    private List<MtEoBatchChangeHistoryVO3> recEoBatchChangeSourceHis(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            return Collections.emptyList();
        }

        MtEoBatchChangeHistory record = new MtEoBatchChangeHistory();
        record.setTenantId(tenantId);
        record.setEoId(eoId);
        List<MtEoBatchChangeHistory> mtEoBatchChangeHistorys = this.mtEoBatchChangeHistoryMapper.select(record);
        if (CollectionUtils.isEmpty(mtEoBatchChangeHistorys)) {
            return Collections.emptyList();
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final List<MtEoBatchChangeHistoryVO3> result = new ArrayList<MtEoBatchChangeHistoryVO3>();
        for (MtEoBatchChangeHistory mtEoBatchChangeHistory : mtEoBatchChangeHistorys) {
            MtEoBatchChangeHistoryVO3 vo = new MtEoBatchChangeHistoryVO3();
            vo.setSourceEoId(mtEoBatchChangeHistory.getSourceEoId());
            vo.setTargetEoId(mtEoBatchChangeHistory.getEoId());
            vo.setReason(mtEoBatchChangeHistory.getReason());
            vo.setCreationDate(format.format(mtEoBatchChangeHistory.getCreationDate()));
            vo.setEventId(mtEoBatchChangeHistory.getEventId());
            vo.setSequence(mtEoBatchChangeHistory.getSequence());
            vo.setSourceEoStepActualId(mtEoBatchChangeHistory.getSourceEoStepActualId());
            result.add(vo);
            result.addAll(recEoBatchChangeSourceHis(tenantId, vo.getSourceEoId()));
        }
        return result;
    }

    private List<MtEoBatchChangeHistoryVO3> recEoBatchChangeTarHis(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            return Collections.emptyList();
        }

        MtEoBatchChangeHistory record = new MtEoBatchChangeHistory();
        record.setTenantId(tenantId);
        record.setSourceEoId(eoId);
        List<MtEoBatchChangeHistory> mtEoBatchChangeHistorys = this.mtEoBatchChangeHistoryMapper.select(record);
        if (CollectionUtils.isEmpty(mtEoBatchChangeHistorys)) {
            return Collections.emptyList();
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final List<MtEoBatchChangeHistoryVO3> result = new ArrayList<MtEoBatchChangeHistoryVO3>();
        for (MtEoBatchChangeHistory mtEoBatchChangeHistory : mtEoBatchChangeHistorys) {
            MtEoBatchChangeHistoryVO3 vo = new MtEoBatchChangeHistoryVO3();
            vo.setSourceEoId(mtEoBatchChangeHistory.getSourceEoId());
            vo.setTargetEoId(mtEoBatchChangeHistory.getEoId());
            vo.setReason(mtEoBatchChangeHistory.getReason());
            vo.setCreationDate(format.format(mtEoBatchChangeHistory.getCreationDate()));
            vo.setEventId(mtEoBatchChangeHistory.getEventId());
            vo.setSequence(mtEoBatchChangeHistory.getSequence());
            vo.setSourceEoStepActualId(mtEoBatchChangeHistory.getSourceEoStepActualId());
            result.add(vo);
            result.addAll(recEoBatchChangeTarHis(tenantId, vo.getTargetEoId()));
        }
        return result;
    }

    @Override
    public List<MtEoBatchChangeHistoryVO6> relSourceEoTreeQuery(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:relSourceEoTreeQuery】"));
        }
        List<MtEoBatchChangeHistoryVO6> mtEoBatchChangeHistorys = relSourceEoTreeGet(tenantId, eoId);
        if (CollectionUtils.isNotEmpty(mtEoBatchChangeHistorys)) {
            List<String> eventIds = mtEoBatchChangeHistorys.stream().filter(t -> StringUtils.isNotEmpty(t.getEventId()))
                    .map(MtEoBatchChangeHistoryVO6::getEventId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(eventIds)) {
                List<MtEventVO1> eventList = mtEventRepository.eventBatchGet(tenantId, eventIds);
                for (MtEoBatchChangeHistoryVO6 vo : mtEoBatchChangeHistorys) {
                    Optional<MtEventVO1> eventOp =
                            eventList.stream().filter(t -> t.getEventId().equals(vo.getEventId())).findFirst();
                    if (eventOp.isPresent()) {
                        vo.setEventBy(eventOp.get().getEventBy());
                        vo.setEventTime(eventOp.get().getEventTime());
                    }
                }
            }
        }
        return mtEoBatchChangeHistorys;
    }

    private List<MtEoBatchChangeHistoryVO6> relSourceEoTreeGet(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            return Collections.emptyList();
        }

        MtEoBatchChangeHistory record = new MtEoBatchChangeHistory();
        record.setTenantId(tenantId);
        record.setEoId(eoId);
        List<MtEoBatchChangeHistory> mtEoBatchChangeHistorys = this.mtEoBatchChangeHistoryMapper.select(record);
        if (CollectionUtils.isEmpty(mtEoBatchChangeHistorys)) {
            return Collections.emptyList();
        }

        final List<MtEoBatchChangeHistoryVO6> result = new ArrayList<MtEoBatchChangeHistoryVO6>();
        for (MtEoBatchChangeHistory mtEoBatchChangeHistory : mtEoBatchChangeHistorys) {
            MtEoBatchChangeHistoryVO6 vo = new MtEoBatchChangeHistoryVO6();
            vo.setEoId(mtEoBatchChangeHistory.getEoId());
            vo.setSourceEoId(mtEoBatchChangeHistory.getSourceEoId());
            vo.setReason(mtEoBatchChangeHistory.getReason());
            vo.setCreationDate(mtEoBatchChangeHistory.getCreationDate());
            vo.setEventId(mtEoBatchChangeHistory.getEventId());
            vo.setSequence(mtEoBatchChangeHistory.getSequence());
            vo.setSourceTrxQty(mtEoBatchChangeHistory.getSourceTrxQty());
            vo.setTrxQty(mtEoBatchChangeHistory.getTrxQty());
            vo.setSourceEoStepActualId(mtEoBatchChangeHistory.getSourceEoStepActualId());
            result.add(vo);
            result.addAll(relSourceEoTreeGet(tenantId, vo.getSourceEoId()));
        }
        return result;
    }

    @Override
    public List<MtEoBatchChangeHistoryVO5> relTargetEoTreeQuery(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:relTargetTreeEoQuery】"));
        }
        List<MtEoBatchChangeHistoryVO5> mtEoBatchChangeHistorys = relTargetEoTreeGet(tenantId, eoId);
        if (CollectionUtils.isNotEmpty(mtEoBatchChangeHistorys)) {
            List<String> eventIds = mtEoBatchChangeHistorys.stream().filter(t -> StringUtils.isNotEmpty(t.getEventId()))
                    .map(MtEoBatchChangeHistoryVO5::getEventId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(eventIds)) {
                List<MtEventVO1> eventList = mtEventRepository.eventBatchGet(tenantId, eventIds);
                for (MtEoBatchChangeHistoryVO5 vo : mtEoBatchChangeHistorys) {
                    Optional<MtEventVO1> eventOp =
                            eventList.stream().filter(t -> t.getEventId().equals(vo.getEventId())).findFirst();
                    if (eventOp.isPresent()) {
                        vo.setEventBy(eventOp.get().getEventBy());
                        vo.setEventTime(eventOp.get().getEventTime());
                    }
                }
            }
        }
        return mtEoBatchChangeHistorys;
    }

    private List<MtEoBatchChangeHistoryVO5> relTargetEoTreeGet(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            return Collections.emptyList();
        }

        MtEoBatchChangeHistory record = new MtEoBatchChangeHistory();
        record.setTenantId(tenantId);
        record.setSourceEoId(eoId);
        List<MtEoBatchChangeHistory> mtEoBatchChangeHistorys = this.mtEoBatchChangeHistoryMapper.select(record);
        if (CollectionUtils.isEmpty(mtEoBatchChangeHistorys)) {
            return Collections.emptyList();
        }

        final List<MtEoBatchChangeHistoryVO5> result = new ArrayList<MtEoBatchChangeHistoryVO5>();
        for (MtEoBatchChangeHistory mtEoBatchChangeHistory : mtEoBatchChangeHistorys) {
            MtEoBatchChangeHistoryVO5 vo = new MtEoBatchChangeHistoryVO5();
            vo.setTargetEoId(mtEoBatchChangeHistory.getEoId());
            vo.setSourceEoId(mtEoBatchChangeHistory.getSourceEoId());
            vo.setReason(mtEoBatchChangeHistory.getReason());
            vo.setCreationDate(mtEoBatchChangeHistory.getCreationDate());
            vo.setEventId(mtEoBatchChangeHistory.getEventId());
            vo.setSequence(mtEoBatchChangeHistory.getSequence());
            vo.setTargetTrxQty(mtEoBatchChangeHistory.getTrxQty());
            vo.setTrxQty(mtEoBatchChangeHistory.getSourceTrxQty());
            vo.setSourceEoStepActualId(mtEoBatchChangeHistory.getSourceEoStepActualId());
            result.add(vo);
            result.addAll(relTargetEoTreeGet(tenantId, vo.getTargetEoId()));
        }
        return result;
    }
}
