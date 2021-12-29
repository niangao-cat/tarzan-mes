package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtEoActualHis;
import tarzan.actual.domain.vo.MtEoActualHisVO1;
import tarzan.actual.domain.vo.MtEoActualHisVO2;
import tarzan.actual.domain.vo.MtEoActualHisVO3;

/**
 * 执行作业实绩历史资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoActualHisRepository extends BaseRepository<MtEoActualHis>, AopProxy<MtEoActualHisRepository> {

    /**
     * eventLimitEoActualHisQuery-获取指定事件的执行作业实绩历史记录
     *
     * @param tenantId
     * @param eventId
     * @return
     */
    List<MtEoActualHis> eventLimitEoActualHisQuery(Long tenantId, String eventId);

    /**
     * eventLimitEoActualHisBatchQuery-获取一批事件的执行作业实绩历史记录
     *
     * @param tenantId
     * @param eventIds
     * @return
     */
    List<MtEoActualHis> eventLimitEoActualHisBatchQuery(Long tenantId, List<String> eventIds);

    /**
     * eoActualHisPropertyQuery-获取执行作业实绩变更历史
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoActualHis> eoActualHisPropertyQuery(Long tenantId, MtEoActualHisVO1 dto);

    /**
     * eoLimitEoActualHisQuery-获取指定执行作业或执行作业实绩的所有历史记录
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoActualHisVO2> eoLimitEoActualHisQuery(Long tenantId, MtEoActualHisVO3 dto);

}
