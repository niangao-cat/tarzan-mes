package tarzan.material.infra.repository.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtPfepPurchaseSupplier;
import tarzan.material.domain.repository.MtPfepPurchaseSupplierRepository;
import tarzan.material.infra.mapper.MtPfepPurchaseSupplierMapper;

/**
 * 物料供应商采购属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Component
public class MtPfepPurchaseSupplierRepositoryImpl extends BaseRepositoryImpl<MtPfepPurchaseSupplier>
                implements MtPfepPurchaseSupplierRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtPfepPurchaseSupplierMapper mtPfepPurchaseSupplierMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    /**
     * pfepPurchaseSupplierAttrPropertyUpdate-物料供应商采购PFEP属性新增&更新扩展表属性
     *
     * @author chuang.yang
     * @date 2019/11/20
     * @param tenantId
     * @param dto
     * @return void
     */
    @Override
    public void pfepPurchaseSupplierAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId", "【API:pfepPurchaseSupplierAttrPropertyUpdate】"));
        }

        // 获取主表数据
        MtPfepPurchaseSupplier mtPfepPurchaseSupplier = mtPfepPurchaseSupplierMapper.selectByPrimaryKey(dto.getKeyId());
        if (mtPfepPurchaseSupplier == null || StringUtils.isEmpty(mtPfepPurchaseSupplier.getPfepPurchaseSupplierId())) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            "keyId:" + dto.getKeyId(), "mt_pfep_purchase_supplier",
                                            "【API:pfepPurchaseSupplierAttrPropertyUpdate】"));
        }

        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_pfep_purchase_supplier_attr", dto.getKeyId(),
                        dto.getEventId(), dto.getAttrs());
    }

    @Override
    public List<MtPfepPurchaseSupplier> queryPfepPurchaseSupplierBySupplierId(Long tenantId, List<String> supplierIds) {
        if (CollectionUtils.isEmpty(supplierIds)) {
            return Collections.emptyList();
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("t.SUPPLIER_ID", supplierIds, 1000);
        return mtPfepPurchaseSupplierMapper.queryByIds(tenantId, whereInValuesSql);
    }

}
