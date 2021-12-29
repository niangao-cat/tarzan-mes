package tarzan.general.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.general.domain.entity.MtEvent;
import tarzan.general.domain.vo.*;

/**
 * 事件记录资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventRepository extends BaseRepository<MtEvent>, AopProxy<MtEventRepository> {

    /**
     * eventGet-获取事件记录
     *
     * @param tenantId
     * @param eventId
     * @return
     */
    MtEventVO1 eventGet(Long tenantId, String eventId);

    /**
     * eventBatchGet-批量获取事件记录
     *
     * @param tenantId
     * @param eventIds
     * @return
     */
    List<MtEventVO1> eventBatchGet(Long tenantId, List<String> eventIds);

    /**
     * propertyLimitEventQuery-根据属性获取事件记录
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitEventQuery(Long tenantId, MtEventVO dto);

    /**
     * groupLimitEventQuery-根据事件组获取该事件组下事件
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> requestLimitEventQuery(Long tenantId, MtEventVO2 dto);

    /**
     * parentEventQuery-获取父子事件
     *
     * @author chuang.yang
     * @date 2019/3/5
     * @param tenantId
     * @param eventIds
     * @return java.util.List<hmes.event.view.EventVO3>
     */
    List<MtEventVO3> parentEventQuery(Long tenantId, List<String> eventIds);

    /**
     * propertyLimitRequestAndEventQuery-根据属性获取请求和事件
     *
     * @Author lxs
     * @Date 2019/4/23
     * @Params
     * @Return
     */
    List<MtEventVO6> propertyLimitRequestAndEventQuery(Long tenantId, MtEventVO4 dto);

    /**
     * eventCreate-新增事件记录
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String eventCreate(Long tenantId, MtEventCreateVO dto);

}
