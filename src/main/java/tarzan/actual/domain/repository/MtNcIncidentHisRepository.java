package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtNcIncidentHis;

/**
 * 不良事故历史资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:48
 */
public interface MtNcIncidentHisRepository
                extends BaseRepository<MtNcIncidentHis>, AopProxy<MtNcIncidentHisRepository> {

    /**
     * ncIncidentNumLimitNcIncidentHisQuery-根据事故编码查询事故历史清单
     *
     * @param tenantId
     * @param siteId
     * @return
     * @author sen.luo
     * @date 2019/4/1
     */
    List<MtNcIncidentHis> ncIncidentNumLimitNcIncidentHisQuery(Long tenantId, String siteId, String incidentNumber);

    /**
     * eventLimitNcIncidentHisQuery-根据事件查询事故清单
     *
     * @param tenantId
     * @param eventId
     * @return
     * @author sen.luo
     * @date 2019/4/1
     */
    List<MtNcIncidentHis> eventLimitNcIncidentHisQuery(Long tenantId, String eventId);
}
