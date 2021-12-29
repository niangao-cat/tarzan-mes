package tarzan.material.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtPfepPurchaseSupplierCatg;

/**
 * 物料类别供应商采购属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepPurchaseSupplierCatgRepository
                extends BaseRepository<MtPfepPurchaseSupplierCatg>, AopProxy<MtPfepPurchaseSupplierCatgRepository> {

    /**
     * pfepPurSupplierCatgAttrPropertyUpdate-物料类别供应商采购PFEP属性新增&更新扩展表属性
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/19
     */
    void pfepPurSupplierCatgAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);

}
