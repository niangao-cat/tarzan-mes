package tarzan.general.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.general.domain.entity.MtEventObjectType;
import tarzan.general.domain.vo.MtEventObjectTypeVO;

/**
 * 对象类型定义资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventObjectTypeRepository
                extends BaseRepository<MtEventObjectType>, AopProxy<MtEventObjectTypeRepository> {

    /**
     * objectTypeGet-获取对象类型属性
     *
     * @param tenantId
     * @param objectTypeId
     * @return
     */
    MtEventObjectType objectTypeGet(Long tenantId, String objectTypeId);

    /**
     * propertyLimitObjectTypeQuery-根据属性获取对象类型
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitObjectTypeQuery(Long tenantId, MtEventObjectTypeVO dto);


    String eventObjectTypeBasicPropertyUpdate(Long tenantId, MtEventObjectType dto);
}
