package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtNcIncident;
import tarzan.actual.domain.vo.MtNcIncidentVO1;
import tarzan.actual.domain.vo.MtNcIncidentVO2;
import tarzan.actual.domain.vo.MtNcIncidentVO3;

/**
 * 不良事故资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:48
 */
public interface MtNcIncidentRepository extends BaseRepository<MtNcIncident>, AopProxy<MtNcIncidentRepository> {

    /**
     * ncIncidentNumLimitNcIncidentGet-根据事故编码查询不良事故
     *
     * @author sen.luo
     * @date 2019/4/1
     * @param tenantId
     * @param siteId
     * @return
     */
    MtNcIncident ncIncidentNumLimitNcIncidentGet(Long tenantId, String siteId, String incidentNumber);

    /**
     * ncIncidentAndHisCreate-不良事故及历史创建
     *
     * @author sen.luo
     * @date 2019/4/1
     * @param tenantId
     * @param dto
     * @return
     */
    MtNcIncidentVO3 ncIncidentAndHisCreate(Long tenantId, MtNcIncidentVO1 dto);

    /**
     * ncIncidentAndHisUpdate-不良事故及历史更新
     *
     * @author sen.luo
     * @date 2019/4/1
     * @param tenantId
     * @param dto
     * @return
     */
    MtNcIncidentVO3 ncIncidentAndHisUpdate(Long tenantId, MtNcIncidentVO2 dto);

    /**
     * ncIncidentBatchGet-批量查询不良事故
     */
    List<MtNcIncident> ncIncidentBatchGet(Long tenantId, List<String> ncIncidents);
}
