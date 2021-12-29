package tarzan.material.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtPfepManufacturingCategory;
import tarzan.material.domain.vo.MtPfepManufactureCateVO1;

/**
 * 物料类别生产属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepManufacturingCategoryRepository
                extends BaseRepository<MtPfepManufacturingCategory>, AopProxy<MtPfepManufacturingCategoryRepository> {

    /**
     * materialCategoryPfepManufacturingUpdate-物料类别生产属性新增&更新
     *
     * @author chuang.yang
     * @date 2019/9/18
     * @param tenantId
     * @param dto
     * @param fullUpdate
     * @return java.lang.String
     */
    String materialCategoryPfepManufacturingUpdate(Long tenantId, MtPfepManufactureCateVO1 dto, String fullUpdate);

    /**
     * pfepMfgCatgAttrPropertyUpdate-物料类别制造PFEP属性新增&更新扩展表属性
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/19
     */
    void pfepMfgCatgAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);
}
