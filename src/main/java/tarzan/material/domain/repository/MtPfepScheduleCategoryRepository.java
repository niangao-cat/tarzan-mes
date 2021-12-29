package tarzan.material.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtPfepScheduleCategory;

/**
 * 物料类别计划属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepScheduleCategoryRepository
                extends BaseRepository<MtPfepScheduleCategory>, AopProxy<MtPfepScheduleCategoryRepository> {

    /**
     * pfepScheduleCategoryAttrPropertyUpdate-物料类别计划PFEP属性新增&更新扩展表属性
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/20
     */
    void pfepScheduleCategoryAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);
}
