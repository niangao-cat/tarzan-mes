package tarzan.material.infra.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtPfepPurchaseSupplierCatg;
import tarzan.material.domain.repository.MtPfepPurchaseSupplierCatgRepository;
import tarzan.material.infra.mapper.MtPfepPurchaseSupplierCatgMapper;

/**
 * 物料类别供应商采购属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Component
public class MtPfepPurchaseSupplierCatgRepositoryImpl extends BaseRepositoryImpl<MtPfepPurchaseSupplierCatg>
                implements MtPfepPurchaseSupplierCatgRepository {

    private static final String TABLE_NAME = "mt_pfep_purchase_supplier_catg";
    private static final String ATTR_TABLE_NAME = "mt_pfep_pur_supplier_catg_attr";

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtPfepPurchaseSupplierCatgMapper mtPfepPurchaseSupplierCatgMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pfepPurSupplierCatgAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId", "【API:pfepPurSupplierCatgAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtPfepPurchaseSupplierCatg mtPfepPurchaseSupplierCatg = new MtPfepPurchaseSupplierCatg();
        mtPfepPurchaseSupplierCatg.setTenantId(tenantId);
        mtPfepPurchaseSupplierCatg.setPfepPurchaseSupplierCatgId(dto.getKeyId());
        mtPfepPurchaseSupplierCatg = mtPfepPurchaseSupplierCatgMapper.selectOne(mtPfepPurchaseSupplierCatg);
        if (mtPfepPurchaseSupplierCatg == null) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            dto.getKeyId(), TABLE_NAME, "【API:pfepPurSupplierCatgAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, ATTR_TABLE_NAME, dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }
}
