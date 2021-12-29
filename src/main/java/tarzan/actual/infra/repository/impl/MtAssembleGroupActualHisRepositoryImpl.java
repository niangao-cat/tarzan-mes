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
import tarzan.actual.domain.entity.MtAssembleGroupActualHis;
import tarzan.actual.domain.repository.MtAssembleGroupActualHisRepository;
import tarzan.actual.domain.vo.MtAssembleGroupActualHisVO;
import tarzan.actual.domain.vo.MtAssembleGroupActualHisVO2;
import tarzan.actual.infra.mapper.MtAssembleGroupActualHisMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventVO1;

/**
 * 装配组实绩历史,记录装配组所有安装位置历史记录 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtAssembleGroupActualHisRepositoryImpl extends BaseRepositoryImpl<MtAssembleGroupActualHis>
                implements MtAssembleGroupActualHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtAssembleGroupActualHisMapper assembleGroupActualHisMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Override
    public List<MtAssembleGroupActualHisVO2> assembleGroupActualHisQuery(Long tenantId,
                                                                         MtAssembleGroupActualHisVO condition) {
        if (StringUtils.isEmpty(condition.getAssembleGroupActualId()) && StringUtils.isEmpty(condition.getEventId())
                        && StringUtils.isEmpty(condition.getAssembleGroupId())
                        && StringUtils.isEmpty(condition.getWorkcellId())) {
            throw new MtException("MT_ASSEMBLE_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002", "ASSEMBLE",
                                            "{assembleGroupActualId、eventId、assembleGroupId、workcellId}",
                                            "【API:assembleGroupActualHisQuery】"));
        }

        MtAssembleGroupActualHis assembleGroupActualHis = new MtAssembleGroupActualHis();
        assembleGroupActualHis.setTenantId(tenantId);
        if (null != condition.getAssembleGroupActualId()) {
            assembleGroupActualHis.setAssembleGroupActualId(condition.getAssembleGroupActualId());
        }
        if (null != condition.getEventId()) {
            assembleGroupActualHis.setEventId(condition.getEventId());
        }
        if (null != condition.getAssembleGroupId()) {
            assembleGroupActualHis.setAssembleGroupId(condition.getAssembleGroupId());
        }
        if (null != condition.getWorkcellId()) {
            assembleGroupActualHis.setWorkcellId(condition.getWorkcellId());
        }

        List<MtAssembleGroupActualHis> assembleGroupActualHiz =
                        this.assembleGroupActualHisMapper.select(assembleGroupActualHis);
        if (CollectionUtils.isEmpty(assembleGroupActualHiz)) {
            return Collections.emptyList();
        }

        List<String> eventIds = assembleGroupActualHiz.stream().map(MtAssembleGroupActualHis::getEventId)
                        .collect(Collectors.toList());
        List<MtEventVO1> eventList = this.mtEventRepository.eventBatchGet(tenantId, eventIds);

        final List<MtAssembleGroupActualHisVO2> result = new ArrayList<MtAssembleGroupActualHisVO2>();
        assembleGroupActualHiz.stream().forEach(c -> {
            MtAssembleGroupActualHisVO2 vo = new MtAssembleGroupActualHisVO2();
            vo.setAssembleGroupActualId(c.getAssembleGroupActualId());
            vo.setAssembleGroupId(c.getAssembleGroupId());
            vo.setEventId(c.getEventId());
            Optional<MtEventVO1> optional =
                            eventList.stream().filter(e -> e.getEventId().equals(c.getEventId())).findFirst();
            if (optional.isPresent()) {
                vo.setEventTypeCode(optional.get().getEventTypeCode());
            }
            vo.setWorkcellId(c.getWorkcellId());
            result.add(vo);
        });
        return result;
    }

}
