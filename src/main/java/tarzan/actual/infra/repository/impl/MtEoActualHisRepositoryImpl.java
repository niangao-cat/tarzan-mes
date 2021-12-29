package tarzan.actual.infra.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtEoActualHis;
import tarzan.actual.domain.repository.MtEoActualHisRepository;
import tarzan.actual.domain.vo.MtEoActualHisVO1;
import tarzan.actual.domain.vo.MtEoActualHisVO2;
import tarzan.actual.domain.vo.MtEoActualHisVO3;
import tarzan.actual.infra.mapper.MtEoActualHisMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventVO1;

/**
 * 执行作业实绩历史 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtEoActualHisRepositoryImpl extends BaseRepositoryImpl<MtEoActualHis> implements MtEoActualHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoActualHisMapper mtEoActualHisMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    /**
     * eventLimitEoActualHisQuery-获取指定事件的执行作业实绩历史记录
     *
     * @param tenantId
     * @param eventId
     * @return
     */
    @Override
    public List<MtEoActualHis> eventLimitEoActualHisQuery(Long tenantId, String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API：eventLimitEoActualHisQuery】"));
        }

        MtEoActualHis his = new MtEoActualHis();
        his.setTenantId(tenantId);
        his.setEventId(eventId);
        return mtEoActualHisMapper.select(his);
    }

    /**
     * eventLimitEoActualHisBatchQuery-获取一批事件的执行作业实绩历史记录
     *
     * @param tenantId
     * @param eventIds
     * @return
     */
    @Override
    public List<MtEoActualHis> eventLimitEoActualHisBatchQuery(Long tenantId, List<String> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventIds", "【API：eventLimitEoActualHisBatchQuery】"));
        }
        return mtEoActualHisMapper.eventLimitEoActualHisBatchQuery(tenantId, eventIds);
    }

    /**
     * eoActualHisPropertyQuery-获取执行作业实绩变更历史
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Override
    public List<MtEoActualHis> eoActualHisPropertyQuery(Long tenantId, MtEoActualHisVO1 dto) {
        if (StringUtils.isEmpty(dto.getEoActualId()) && StringUtils.isEmpty(dto.getEoId())
                        && StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0032", "ORDER", "【eoId、eoActualId、eventId】", "【API：eoActualHisPropertyQuery】"));
        }

        return mtEoActualHisMapper.eoActualHisPropertyQuery(tenantId, dto);
    }

    /**
     * eoLimitEoActualHisQuery-获取指定执行作业或执行作业实绩的所有历史记录
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Override
    public List<MtEoActualHisVO2> eoLimitEoActualHisQuery(Long tenantId, MtEoActualHisVO3 dto) {
        if (StringUtils.isEmpty(dto.getEoId()) && StringUtils.isEmpty(dto.getEoActualId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0032", "ORDER", "【eoId、eoActualId】", "【API：eoLimitEoActualHisQuery】"));
        }

        MtEoActualHis his = new MtEoActualHis();
        his.setTenantId(tenantId);
        if (StringUtils.isNotEmpty(dto.getEoActualId())) {
            his.setEoActualId(dto.getEoActualId());
        }

        if (StringUtils.isNotEmpty(dto.getEoId())) {
            his.setEoId(dto.getEoId());
        }

        List<MtEoActualHis> mtEoActualHisList = mtEoActualHisMapper.select(his);

        List<MtEoActualHisVO2> resultList = new ArrayList<>();

        for (MtEoActualHis eoActualHis : mtEoActualHisList) {

            MtEoActualHisVO2 vo2 = new MtEoActualHisVO2();
            BeanUtils.copyProperties(eoActualHis, vo2);

            MtEventVO1 mtEventVO1 = mtEventRepository.eventGet(tenantId, eoActualHis.getEventId());
            vo2.setEventType(mtEventVO1 == null ? null : mtEventVO1.getEventTypeCode());

            resultList.add(vo2);
        }

        return resultList;
    }

}
