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
import tarzan.material.domain.entity.MtPfepShipping;
import tarzan.material.domain.repository.MtPfepShippingRepository;
import tarzan.material.infra.mapper.MtPfepShippingMapper;

/**
 * 物料发运属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Component
public class MtPfepShippingRepositoryImpl extends BaseRepositoryImpl<MtPfepShipping>
                implements MtPfepShippingRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtPfepShippingMapper mtPfepShippingMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pfepShippingAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        if (StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId", "【API:pfepShippingAttrPropertyUpdate】"));
        }
        // 获取主表数据
        MtPfepShipping mtPfepShipping = new MtPfepShipping();
        mtPfepShipping.setTenantId(tenantId);
        mtPfepShipping.setPfepShippingId(dto.getKeyId());
        mtPfepShipping = mtPfepShippingMapper.selectByPrimaryKey(mtPfepShipping);
        if (mtPfepShipping == null || StringUtils.isEmpty(mtPfepShipping.getPfepShippingId())) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            "keyId:" + dto.getKeyId(), "mt_pfep_shipping",
                                            "【API:pfepShippingAttrPropertyUpdate】"));
        }

        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_pfep_shipping_attr", dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }
}
