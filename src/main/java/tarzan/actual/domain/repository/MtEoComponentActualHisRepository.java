package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtEoComponentActualHis;
import tarzan.actual.domain.vo.MtEoComponentActualVO3;

/**
 * 执行作业组件装配实绩历史，记录执行作业物料和组件实际装配情况变更历史资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoComponentActualHisRepository
                extends BaseRepository<MtEoComponentActualHis>, AopProxy<MtEoComponentActualHisRepository> {

    List<MtEoComponentActualHis> queryEoComponentHis(Long tenantId, MtEoComponentActualHis dto);

    /**
     * eventLimitEoComponentActualHisQuery-获取指定事件的执行作业组件实绩历史记录
     *
     * @param tenantId
     * @param eventId
     * @author guichuan.li
     * @date 2019/3/12
     */
    List<MtEoComponentActualHis> eventLimitEoComponentActualHisQuery(Long tenantId, String eventId);

    /**
     * eoComponentActualLimitHisQuery-获取指定执行作业组件实绩的所有历史记录
     *
     * @param tenantId
     * @author guichuan.li
     * @date 2019/3/12
     */
    List<MtEoComponentActualVO3> eoComponentActualLimitHisQuery(Long tenantId, String eoComponentActualId);

    /**
     * eventLimitEoComponentActualHisBatchQuery-批量获取指定事件的执行作业组件实绩历史记录
     *
     * @author guichuan.li
     * @date 2019/3/13
     * @param tenantId
     */
    List<MtEoComponentActualHis> eventLimitEoComponentActualHisBatchQuery(Long tenantId, List<String> eventIds);

}
