package tarzan.actual.infra.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
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
import tarzan.actual.domain.entity.MtAssemblePointActualHis;
import tarzan.actual.domain.repository.MtAssembleGroupActualHisRepository;
import tarzan.actual.domain.repository.MtAssemblePointActualHisRepository;
import tarzan.actual.domain.vo.MtAssembleGroupActualHisVO;
import tarzan.actual.domain.vo.MtAssembleGroupActualHisVO2;
import tarzan.actual.domain.vo.MtAssemblePointActualHisVO;
import tarzan.actual.domain.vo.MtAssemblePointActualHisVO1;
import tarzan.actual.infra.mapper.MtAssemblePointActualHisMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventVO1;

/**
 * 装配点实绩历史，记录装配组下装配点实际装配物料和数量变更记录 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtAssemblePointActualHisRepositoryImpl extends BaseRepositoryImpl<MtAssemblePointActualHis>
                implements MtAssemblePointActualHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtAssemblePointActualHisMapper assemblePointActualHisMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtAssembleGroupActualHisRepository mtAssembleGroupActualHisRepository;

    @Override
    public List<MtAssemblePointActualHisVO1> assemblePointActualHisQuery(Long tenantId,
                                                                         MtAssemblePointActualHisVO condition) {
        if (StringUtils.isEmpty(condition.getAssemblePointActualId()) && StringUtils.isEmpty(condition.getEventId())
                        && StringUtils.isEmpty(condition.getAssembleGroupActualId())
                        && StringUtils.isEmpty(condition.getAssemblePointId())
                        && StringUtils.isEmpty(condition.getMaterialId())
                        && StringUtils.isEmpty(condition.getMaterialLotId())) {
            throw new MtException("MT_ASSEMBLE_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0002", "ASSEMBLE",
                            "{assemblePointActualId、eventId、materialId、assembleGroupActualId、assemblePointId、materialLotId}",
                            "【API:assemblePointActualHisQuery】"));
        }

        MtAssemblePointActualHis assemblePointActualHis = new MtAssemblePointActualHis();
        assemblePointActualHis.setTenantId(tenantId);
        if (null != condition.getAssemblePointActualId()) {
            assemblePointActualHis.setAssemblePointActualId(condition.getAssemblePointActualId());
        }
        if (null != condition.getEventId()) {
            assemblePointActualHis.setEventId(condition.getEventId());
        }
        if (null != condition.getAssembleGroupActualId()) {
            assemblePointActualHis.setAssembleGroupActualId(condition.getAssembleGroupActualId());
        }
        if (null != condition.getAssemblePointId()) {
            assemblePointActualHis.setAssemblePointId(condition.getAssemblePointId());
        }
        if (null != condition.getMaterialId()) {
            assemblePointActualHis.setMaterialId(condition.getMaterialId());
        }
        if (null != condition.getMaterialLotId()) {
            assemblePointActualHis.setMaterialLotId(condition.getMaterialLotId());
        }

        List<MtAssemblePointActualHis> assemblePointActualHiz =
                        this.assemblePointActualHisMapper.select(assemblePointActualHis);
        if (CollectionUtils.isEmpty(assemblePointActualHiz)) {
            return Collections.emptyList();
        }

        List<String> eventIds = assemblePointActualHiz.stream().map(MtAssemblePointActualHis::getEventId)
                        .collect(Collectors.toList());
        List<MtEventVO1> eventList = this.mtEventRepository.eventBatchGet(tenantId, eventIds);

        final List<MtAssembleGroupActualHisVO2> assembleGroupActualHiz = new ArrayList<MtAssembleGroupActualHisVO2>();
        for (MtAssemblePointActualHis mtAssemblePointActualHis : assemblePointActualHiz) {
            MtAssembleGroupActualHisVO assembleGroupActualHisCondition = new MtAssembleGroupActualHisVO();
            assembleGroupActualHisCondition.setEventId(mtAssemblePointActualHis.getEventId());
            assembleGroupActualHisCondition
                            .setAssembleGroupActualId(mtAssemblePointActualHis.getAssembleGroupActualId());
            assembleGroupActualHiz.addAll(this.mtAssembleGroupActualHisRepository.assembleGroupActualHisQuery(tenantId,
                            assembleGroupActualHisCondition));
        }

        final List<MtAssemblePointActualHisVO1> result = new ArrayList<MtAssemblePointActualHisVO1>();
        assemblePointActualHiz.stream().forEach(c -> {
            MtAssemblePointActualHisVO1 vo = new MtAssemblePointActualHisVO1();
            vo.setAssmeblePointActualHisId(c.getAssemblePointActualHisId());
            vo.setAssmeblePointActualId(c.getAssemblePointActualId());
            vo.setEventId(c.getEventId());

            Optional<MtEventVO1> eventOptional =
                            eventList.stream().filter(e -> e.getEventId().equals(c.getEventId())).findFirst();
            if (eventOptional.isPresent()) {
                vo.setEventTypeCode(eventOptional.get().getEventTypeCode());
            }

            vo.setAssembleGroupActualId(c.getAssembleGroupActualId());
            vo.setAssemblePointId(c.getAssemblePointId());
            vo.setMaterialId(c.getMaterialId());
            vo.setMaterialLotId(c.getMaterialLotId());
            vo.setFeedingSequence(c.getFeedingSequence());
            vo.setFeedingMaterialLotSequence(c.getFeedingMaterialLotSequence());
            vo.setQty(c.getQty());
            vo.setTrxQty(c.getTrxQty());
            vo.setFeedingQty(c.getFeedingQty());
            vo.setTrxFeedingQty(c.getTrxFeedingQty());

            Optional<MtAssembleGroupActualHisVO2> assembleGroupOptional = assembleGroupActualHiz.stream()
                            .filter(e -> e.getEventId().equals(c.getEventId())
                                            && e.getAssembleGroupActualId().equals(c.getAssembleGroupActualId()))
                            .findFirst();
            if (assembleGroupOptional.isPresent()) {
                vo.setAssembleGroupId(assembleGroupOptional.get().getAssembleGroupId());
                vo.setWorkcellId(assembleGroupOptional.get().getWorkcellId());
            }
            result.add(vo);
        });
        return result;
    }
}
