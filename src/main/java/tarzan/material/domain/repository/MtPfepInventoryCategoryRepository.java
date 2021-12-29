package tarzan.material.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtPfepInventoryCategory;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO;

/**
 * 物料类别存储属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepInventoryCategoryRepository
                extends BaseRepository<MtPfepInventoryCategory>, AopProxy<MtPfepInventoryCategoryRepository> {

    /**
     * 物料类别存储属性新增&更新
     * 
     * @Author peng.yuan
     * @Date 2019/9/19 15:27
     * @param tenantId : 用户组id
     * @param vo :
     * @param fullUpdate : 全量更新字段
     * @return java.lang.String pfepInventoryCategoryId
     */
    String materialCategoryPfepInventoryUpdate(Long tenantId, MtPfepInventoryCategoryVO vo, String fullUpdate);

    /**
     * pfepInventoryCatgAttrPropertyUpdate-物料类别库存PFEP属性新增&更新扩展表属性
     *
     * @Author Xie.yiyang
     * @Date 2019/11/19 14:29
     * @param tenantId
     * @param dto
     * @return void
     */
    void pfepInventoryCatgAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);
}
