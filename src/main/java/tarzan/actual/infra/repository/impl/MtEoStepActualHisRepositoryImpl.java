package tarzan.actual.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtEoStepActualHis;
import tarzan.actual.domain.repository.MtEoStepActualHisRepository;
import tarzan.actual.infra.mapper.MtEoStepActualHisMapper;

/**
 * 执行作业-工艺路线步骤执行实绩 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtEoStepActualHisRepositoryImpl extends BaseRepositoryImpl<MtEoStepActualHis>
                implements MtEoStepActualHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoStepActualHisMapper mtEoStepActualHisMapper;

    @Override
    public List<MtEoStepActualHis> eoStepActualHisQuery(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "【API:eoStepActualHisQuery】"));
        }

        List<MtEoStepActualHis> mtEoStepActualHiz = this.mtEoStepActualHisMapper.eoStepActualHisQuery(tenantId, eoId);
        if (CollectionUtils.isEmpty(mtEoStepActualHiz)) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eoId", "【API:eoStepActualHisQuery】"));
        }
        return mtEoStepActualHiz;
    }

    @Override
    public List<MtEoStepActualHis> eoStepActualHisByEventQuery(Long tenantId, String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "【API:eoStepActualHisByEventQuery】"));
        }

        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setTenantId(tenantId);
        mtEoStepActualHis.setEventId(eventId);
        List<MtEoStepActualHis> mtEoStepActualHiz = this.mtEoStepActualHisMapper.select(mtEoStepActualHis);
        if (CollectionUtils.isEmpty(mtEoStepActualHiz)) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eventId", "【API:eoStepActualHisByEventQuery】"));
        }
        return mtEoStepActualHiz;
    }

}
