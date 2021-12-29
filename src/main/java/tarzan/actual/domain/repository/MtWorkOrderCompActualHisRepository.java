package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtWorkOrderCompActualHis;
import tarzan.actual.domain.vo.MtWoComponentActualVO7;

/**
 * 生产订单组件装配实绩历史，记录生产订单物料和组件实际装配情况变更历史资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtWorkOrderCompActualHisRepository
                extends BaseRepository<MtWorkOrderCompActualHis>, AopProxy<MtWorkOrderCompActualHisRepository> {

    /**
     * woComponentActualLimitHisQuery-获取指定生产指令组件实绩的所有历史记录/sen.luo 2018-03-13
     *
     * @param tenantId
     * @param workOrderComponentActualId
     * @return
     */
    List<MtWoComponentActualVO7> woComponentActualLimitHisQuery(Long tenantId, String workOrderComponentActualId);

    /**
     * eventLimitWoComponentActualHisBatchQuery-批量获取指定事件的生产指令组件实绩历史记录/sen.luo 2018-03-13
     *
     * @param tenantId
     * @param eventIds
     * @return
     */
    List<MtWorkOrderCompActualHis> eventLimitWoComponentActualHisBatchQuery(Long tenantId, List<String> eventIds);

    /**
     * eventLimitWoComponentActualHisQuery-获取指定事件的生产指令组件实绩历史记录/sen.luo 2018-03-13
     *
     * @param tenantId
     * @param eventId
     * @return
     */
    List<MtWorkOrderCompActualHis> eventLimitWoComponentActualHisQuery(Long tenantId, String eventId);
}
