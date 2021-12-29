package tarzan.material.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtPfepPurchase;

/**
 * 物料采购属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepPurchaseRepository extends BaseRepository<MtPfepPurchase>, AopProxy<MtPfepPurchaseRepository> {

    /**
     * pfepPurchaseAttrPropertyUpdate-物料采购PFEP属性新增&更新扩展表属性
     *
     * @Author Xie.yiyang
     * @Date 2019/11/19 11:16
     * @param tenantId
     * @param dto
     * @return void
     */
    void pfepPurchaseAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);
}
