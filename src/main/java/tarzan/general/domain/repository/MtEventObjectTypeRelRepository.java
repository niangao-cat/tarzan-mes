package tarzan.general.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.general.domain.entity.MtEventObjectTypeRel;
import tarzan.general.domain.vo.*;

/**
 * 事件类型与对象类型关系定义资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventObjectTypeRelRepository
                extends BaseRepository<MtEventObjectTypeRel>, AopProxy<MtEventObjectTypeRelRepository> {

    /**
     * eventTypeLimitObjectTypeQuery-根据事件类型获取关联的对象类型
     *
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @return java.util.List<hmes.event_object_type_rel.view.MtEventObjectTypeRelVO2>
     * @author chuang.yang
     * @date 2019/3/5
     */
    List<MtEventObjectTypeRelVO2> eventTypeLimitObjectTypeQuery(Long tenantId, MtEventObjectTypeRelVO1 dto);

    /**
     * objectTypeLimitEventTypeQuery-根据对象类型获取关联的事件类型
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.event_object_type_rel.view.MtEventObjectTypeRelVO4>
     * @author chuang.yang
     * @date 2019/3/5
     */
    List<MtEventObjectTypeRelVO4> objectTypeLimitEventTypeQuery(Long tenantId, MtEventObjectTypeRelVO3 dto);

    /**
     * eventLimitEventObjectInfoQuery-根据事件获取事件影响对象信息
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.event_object_type_rel.view.MtEventObjectTypeRelVO6>
     * @author chuang.yang
     * @date 2019/3/5
     */
    List<MtEventObjectTypeRelVO6> eventLimitEventObjectInfoQuery(Long tenantId, MtEventObjectTypeRelVO5 dto);

    /**
     * requestLimitEventObjectInfoQuery-根据事件请求获取影响对象信息
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.event_object_type_rel.view.MtEventObjectTypeRelVO8>
     * @author chuang.yang
     * @date 2019/3/6
     */
    List<MtEventObjectTypeRelVO8> requestLimitEventObjectInfoQuery(Long tenantId, MtEventObjectTypeRelVO7 dto);
}
