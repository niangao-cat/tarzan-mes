package tarzan.general.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.general.domain.entity.MtEventRequestType;
import tarzan.general.domain.vo.MtEventRequestTypeVO;

/**
 * 事件组类型定义资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventRequestTypeRepository
                extends BaseRepository<MtEventRequestType>, AopProxy<MtEventRequestTypeRepository> {

    /**
     * propertyLimitEventGroupTypeQuery-根据属性获取事件组类型
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitEventGroupTypeQuery(Long tenantId, MtEventRequestTypeVO dto);

    /**
     * eventGroupTypeGet-获取事件组类型属性
     *
     * @param tenantId
     * @param requestTypeId
     * @return
     */
    MtEventRequestType eventGroupTypeGet(Long tenantId, String requestTypeId);


    String eventRequestTypeBasicPropertyUpdate(Long tenantId, MtEventRequestType dto);
}
