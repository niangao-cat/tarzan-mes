package tarzan.actual.infra.repository.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtNcIncident;
import tarzan.actual.domain.entity.MtNcIncidentHis;
import tarzan.actual.domain.repository.MtNcIncidentHisRepository;
import tarzan.actual.domain.repository.MtNcIncidentRepository;
import tarzan.actual.domain.vo.MtNcIncidentVO1;
import tarzan.actual.domain.vo.MtNcIncidentVO2;
import tarzan.actual.domain.vo.MtNcIncidentVO3;
import tarzan.actual.infra.mapper.MtNcIncidentHisMapper;
import tarzan.actual.infra.mapper.MtNcIncidentMapper;

/**
 * 不良事故 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:48
 */
@Component
public class MtNcIncidentRepositoryImpl extends BaseRepositoryImpl<MtNcIncident> implements MtNcIncidentRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtNcIncidentHisRepository mtNcIncidentHisRepository;

    @Autowired
    private MtNcIncidentMapper mtNcIncidentMapper;

    @Autowired
    private MtNcIncidentHisMapper mtNcIncidentHisMapper;

    @Override
    public MtNcIncident ncIncidentNumLimitNcIncidentGet(Long tenantId, String siteId, String incidentNumber) {
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "siteId", "【API:ncIncidentNumLimitNcIncidentGet】"));
        }
        if (StringUtils.isEmpty(incidentNumber)) {
            throw new MtException("MT_NC_RECORD_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_NC_RECORD_0001",
                                            "NC_RECORD", "incidentNumber", "【API:ncIncidentNumLimitNcIncidentGet】"));
        }
        MtNcIncident dto = new MtNcIncident();
        dto.setTenantId(tenantId);
        dto.setSiteId(siteId);
        dto.setIncidentNumber(incidentNumber);
        dto = mtNcIncidentMapper.selectOne(dto);
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtNcIncidentVO3 ncIncidentAndHisCreate(Long tenantId, MtNcIncidentVO1 dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "siteId", "【API:ncIncidentAndHisCreate】"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "eventId", "【API:ncIncidentAndHisCreate】"));
        }

        String incidentNumber = null;
        if (StringUtils.isEmpty(dto.getIncidentNumber())) {
            incidentNumber = mtNcIncidentMapper.generateIncidentNumber(tenantId);
        } else {
            incidentNumber = dto.getIncidentNumber();

            // udpate by chuang.yang
            // 添加唯一性校验
            MtNcIncident verify = new MtNcIncident();
            verify.setSiteId(dto.getSiteId());
            verify.setIncidentNumber(incidentNumber);
            List<MtNcIncident> mtNcIncidentList = mtNcIncidentMapper.select(verify);
            if (CollectionUtils.isNotEmpty(mtNcIncidentList)) {
                throw new MtException("MT_NC_RECORD_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_NC_RECORD_0006", "NC_RECORD", "【API:ncIncidentAndHisCreate】"));
            }
        }

        MtNcIncident mtNcIncident = new MtNcIncident();
        mtNcIncident.setTenantId(tenantId);
        mtNcIncident.setSiteId(dto.getSiteId());
        mtNcIncident.setIncidentNumber(incidentNumber);
        mtNcIncident.setNcIncidentStatus("NEW");
        mtNcIncident.setIncidentDateTime(new Date());
        self().insertSelective(mtNcIncident);

        MtNcIncidentHis mtNcIncidentHis = new MtNcIncidentHis();
        BeanUtils.copyProperties(mtNcIncident, mtNcIncidentHis);

        mtNcIncidentHis.setTenantId(tenantId);
        mtNcIncidentHis.setEventId(dto.getEventId());
        mtNcIncidentHisRepository.insertSelective(mtNcIncidentHis);

        MtNcIncidentVO3 result = new MtNcIncidentVO3();
        result.setNcIncidentId(mtNcIncident.getNcIncidentId());
        result.setNcIncidentHisId(mtNcIncidentHis.getNcIncidentHisId());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtNcIncidentVO3 ncIncidentAndHisUpdate(Long tenantId, MtNcIncidentVO2 dto) {
        if (StringUtils.isEmpty(dto.getNcIncidentId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncIncidentId", "【API:ncIncidentAndHisUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getStatus())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "status", "【API:ncIncidentAndHisUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "eventId", "【API:ncIncidentAndHisUpdate】"));
        }

        MtNcIncidentHis tmp = new MtNcIncidentHis();
        tmp.setTenantId(tenantId);
        tmp.setNcIncidentId(dto.getNcIncidentId());
        tmp.setEventId(dto.getEventId());
        tmp = this.mtNcIncidentHisMapper.selectOne(tmp);
        if (null != tmp) {
            throw new MtException("MT_NC_RECORD_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0006", "NC_RECORD", "【API:ncIncidentAndHisUpdate】"));
        }

        MtNcIncident mtNcIncident = new MtNcIncident();
        mtNcIncident.setTenantId(tenantId);
        mtNcIncident.setNcIncidentId(dto.getNcIncidentId());
        mtNcIncident = mtNcIncidentMapper.selectOne(mtNcIncident);

        mtNcIncident.setNcIncidentStatus(dto.getStatus());
        mtNcIncident.setIncidentDateTime(new Date());
        mtNcIncident.setTenantId(tenantId);
        self().updateByPrimaryKey(mtNcIncident);

        MtNcIncidentHis mtNcIncidentHis = new MtNcIncidentHis();
        BeanUtils.copyProperties(mtNcIncident, mtNcIncidentHis);

        mtNcIncidentHis.setTenantId(tenantId);
        mtNcIncidentHis.setEventId(dto.getEventId());

        mtNcIncidentHisRepository.insertSelective(mtNcIncidentHis);

        MtNcIncidentVO3 result = new MtNcIncidentVO3();
        result.setNcIncidentId(mtNcIncident.getNcIncidentId());
        result.setNcIncidentHisId(mtNcIncidentHis.getNcIncidentHisId());
        return result;
    }

    @Override
    public List<MtNcIncident> ncIncidentBatchGet(Long tenantId, List<String> ncIncidents) {
        if (CollectionUtils.isEmpty(ncIncidents)) {
            return Collections.emptyList();
        }
        return mtNcIncidentMapper
                        .selectByCondition(Condition.builder(MtNcIncident.class)
                                        .andWhere(Sqls.custom().andEqualTo(MtNcIncident.FIELD_TENANT_ID, tenantId)
                                                        .andIn(MtNcIncident.FIELD_NC_INCIDENT_ID, ncIncidents))
                                        .build());
    }
}
