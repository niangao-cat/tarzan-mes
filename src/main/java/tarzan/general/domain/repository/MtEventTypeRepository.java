package tarzan.general.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.general.domain.entity.MtEventType;
import tarzan.general.domain.vo.MtEventTypeVO;

/**
 * 事件类型定义资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventTypeRepository extends BaseRepository<MtEventType>, AopProxy<MtEventTypeRepository> {

    /**
     * propertyLimitEventTypeQuery-根据属性获取事件
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitEventTypeQuery(Long tenantId, MtEventTypeVO dto);

    /**
     * eventTypeGet-获取事件类型属性
     *
     * @param tenantId
     * @param eventTypeId
     * @return
     */
    MtEventType eventTypeGet(Long tenantId, String eventTypeId);

    /**
     * eventTypeBatchGet-批量获取事件类型属性
     * @author xiao.tang02@hand-china.com 2019年12月5日下午2:28:30
     * @param tenantId
     * @param eventTypeIds
     * @return
     * @return List<MtEventType>
     */
    List<MtEventType> eventTypeBatchGet(Long tenantId, List<String> eventTypeIds);
}
