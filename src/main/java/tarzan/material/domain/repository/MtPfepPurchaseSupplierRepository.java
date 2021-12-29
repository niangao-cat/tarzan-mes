package tarzan.material.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtPfepPurchaseSupplier;

/**
 * 物料供应商采购属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepPurchaseSupplierRepository
                extends BaseRepository<MtPfepPurchaseSupplier>, AopProxy<MtPfepPurchaseSupplierRepository> {

    /**
     * pfepPurchaseSupplierAttrPropertyUpdate-物料供应商采购PFEP属性新增&更新扩展表属性
     *
     * @author chuang.yang
     * @date 2019/11/20
     * @param tenantId
     * @param dto
     * @return void
     */
    void pfepPurchaseSupplierAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);

    /**
     * 根据供应商ID获取供应商采购PFEP属性
     * 
     * @author guichuanli
     * @date 2019/12/04
     * @param tenantId
     * @param supplierIds
     * @return
     */
    List<MtPfepPurchaseSupplier> queryPfepPurchaseSupplierBySupplierId(Long tenantId, List<String> supplierIds);
}
