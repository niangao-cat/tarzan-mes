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
import tarzan.material.domain.entity.MtPfepPurchaseCategory;
import tarzan.material.domain.repository.MtPfepPurchaseCategoryRepository;
import tarzan.material.infra.mapper.MtPfepPurchaseCategoryMapper;

/**
 * 物料类别采购属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Component
public class MtPfepPurchaseCategoryRepositoryImpl extends BaseRepositoryImpl<MtPfepPurchaseCategory>
                implements MtPfepPurchaseCategoryRepository {

    private static final String TABLE_NAME = "mt_pfep_purchase_category";
    private static final String ATTR_TABLE_NAME = "mt_pfep_purchase_category_attr";

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepsitory;

    @Autowired
    private MtPfepPurchaseCategoryMapper mtPfepPurchaseCategoryMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pfepPurchaseCategoryAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId", "【API:pfepPurchaseCategoryAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtPfepPurchaseCategory mtPfepPurchaseCategory = new MtPfepPurchaseCategory();
        mtPfepPurchaseCategory.setTenantId(tenantId);
        mtPfepPurchaseCategory.setPfepPurchaseCategoryId(dto.getKeyId());
        mtPfepPurchaseCategory = mtPfepPurchaseCategoryMapper.selectOne(mtPfepPurchaseCategory);
        if (mtPfepPurchaseCategory == null) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            dto.getKeyId(), TABLE_NAME,
                                            "【API:pfepPurchaseCategoryAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepsitory.attrPropertyUpdate(tenantId, ATTR_TABLE_NAME, dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }
}
