package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtEoRouterActualHis;
import tarzan.actual.domain.vo.MtEoRouterActualHisVO;

/**
 * EO工艺路线实绩历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoRouterActualHisRepository
                extends BaseRepository<MtEoRouterActualHis>, AopProxy<MtEoRouterActualHisRepository> {

    /**
     * 获取特定事件执行作业工艺路线实绩历史
     * 
     * @param tenantId
     * @param eventId
     * @return
     */
    List<MtEoRouterActualHis> eoRouterActualHisByEventQuery(Long tenantId, String eventId);

    /**
     * 获取特定执行作业工艺路线实绩历史
     * 
     * @param tenantId
     * @param eoId
     * @return
     */
    List<MtEoRouterActualHis> eoRouterActualHisQuery(Long tenantId, String eoId);

    /**
     * 获取特定事件及特定执行作业工艺路线实绩历史
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtEoRouterActualHis> eventAndEoRouterLimitEoRouterActualQuery(Long tenantId, MtEoRouterActualHisVO condition);

}
