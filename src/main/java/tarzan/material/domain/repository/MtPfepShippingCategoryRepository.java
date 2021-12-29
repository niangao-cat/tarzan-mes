package tarzan.material.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtPfepShippingCategory;

/**
 * 物料类别发运属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepShippingCategoryRepository
                extends BaseRepository<MtPfepShippingCategory>, AopProxy<MtPfepShippingCategoryRepository> {
    /**
     * pfepShippingCategoryAttrPropertyUpdate-物料类别发运PFEP属性新增&更新扩展表属性
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/20
     */
    void pfepShippingCategoryAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);
}
