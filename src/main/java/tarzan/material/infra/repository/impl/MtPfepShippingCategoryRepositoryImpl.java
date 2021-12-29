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
import tarzan.material.domain.entity.MtPfepShippingCategory;
import tarzan.material.domain.repository.MtPfepShippingCategoryRepository;
import tarzan.material.infra.mapper.MtPfepShippingCategoryMapper;

/**
 * 物料类别发运属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Component
public class MtPfepShippingCategoryRepositoryImpl extends BaseRepositoryImpl<MtPfepShippingCategory>
                implements MtPfepShippingCategoryRepository {

    private static final String TABLE_NAME = "mt_pfep_shipping_category";
    private static final String ATTR_TABLE_NAME = "mt_pfep_shipping_category_attr";

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtPfepShippingCategoryMapper mtPfepShippingCategoryMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pfepShippingCategoryAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId", "【API:pfepShippingCategoryAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtPfepShippingCategory entity = new MtPfepShippingCategory();
        entity.setTenantId(tenantId);
        entity.setPfepShippingCategoryId(dto.getKeyId());
        entity = mtPfepShippingCategoryMapper.selectOne(entity);
        if (entity == null) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            dto.getKeyId(), TABLE_NAME,
                                            "【API:pfepShippingCategoryAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, ATTR_TABLE_NAME, dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }
}
