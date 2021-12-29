package tarzan.actual.infra.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtNcIncidentHis;
import tarzan.actual.domain.repository.MtNcIncidentHisRepository;
import tarzan.actual.infra.mapper.MtNcIncidentHisMapper;

/**
 * 不良事故历史 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:48
 */
@Component
public class MtNcIncidentHisRepositoryImpl extends BaseRepositoryImpl<MtNcIncidentHis>
                implements MtNcIncidentHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtNcIncidentHisMapper mtNcIncidentHisMapper;

    @Override
    public List<MtNcIncidentHis> ncIncidentNumLimitNcIncidentHisQuery(Long tenantId, String siteId,
                                                                      String incidentNumber) {
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_NC_RECORD_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_NC_RECORD_0001",
                                            "NC_RECORD", "siteId",
                                            "【API:ncIncidentNumLimitNcIncidentHisQueryincidentNumber】"));
        }
        if (StringUtils.isEmpty(incidentNumber)) {
            throw new MtException("MT_NC_RECORD_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_NC_RECORD_0001",
                                            "NC_RECORD", "incidentNumber",
                                            "【API:ncIncidentNumLimitNcIncidentHisQueryincidentNumber】"));
        }

        MtNcIncidentHis mtNcIncidentHis = new MtNcIncidentHis();
        mtNcIncidentHis.setTenantId(tenantId);
        mtNcIncidentHis.setSiteId(siteId);
        mtNcIncidentHis.setIncidentNumber(incidentNumber);
        return mtNcIncidentHisMapper.select(mtNcIncidentHis);
    }

    @Override
    public List<MtNcIncidentHis> eventLimitNcIncidentHisQuery(Long tenantId, String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "eventId", "【API:eventLimitNcIncidentHisQuery】"));
        }

        MtNcIncidentHis mtNcIncidentHis = new MtNcIncidentHis();
        mtNcIncidentHis.setTenantId(tenantId);
        mtNcIncidentHis.setEventId(eventId);
        return mtNcIncidentHisMapper.select(mtNcIncidentHis);
    }

}
