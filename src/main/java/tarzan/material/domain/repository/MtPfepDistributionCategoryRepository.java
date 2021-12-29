package tarzan.material.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtPfepDistributionCategory;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO2;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO3;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO4;

/**
 * 物料类别配送属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepDistributionCategoryRepository
                extends BaseRepository<MtPfepDistributionCategory>, AopProxy<MtPfepDistributionCategoryRepository> {

    /**
     * pfepDistributionCatgAttrPropertyUpdate-物料类别配送PFEP属性新增&更新扩展表属性
     *
     * @Author Xie.yiyang
     * @Date 2019/11/19 14:17
     * @param tenantId
     * @param dto
     * @return void
     */
    void pfepDistributionCatgAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);


    /**
     * 物料类别存储属性新增&更新
     * 
     * @Author peng.yuan
     * @Date 2020/2/4 16:28
     * @param tenantId :
     * @param vo :
     * @return
     */
    String materialCategoryPfepDistributionUpdate(Long tenantId, MtPfepInventoryCategoryVO2 vo);

    /**
     * materialCategoryDistrubutionPfepQuery-获取物料类别配送属性
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtPfepInventoryCategoryVO4> materialCategoryDistributionPfepQuery(Long tenantId,
                                                                           MtPfepInventoryCategoryVO3 dto);
}
