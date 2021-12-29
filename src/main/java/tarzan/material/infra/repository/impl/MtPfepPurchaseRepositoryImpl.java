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
import tarzan.material.domain.entity.MtPfepPurchase;
import tarzan.material.domain.repository.MtPfepPurchaseRepository;
import tarzan.material.infra.mapper.MtPfepPurchaseMapper;

/**
 * 物料采购属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Component
public class MtPfepPurchaseRepositoryImpl extends BaseRepositoryImpl<MtPfepPurchase>
                implements MtPfepPurchaseRepository {


    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtPfepPurchaseMapper mtPfepPurchaseMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pfepPurchaseAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId", "【API:pfepPurchaseAttrPropertyUpdate】"));
        }
        // 获取主表数据
        MtPfepPurchase mtPfepPurchase = new MtPfepPurchase();
        mtPfepPurchase.setTenantId(tenantId);
        mtPfepPurchase.setPfepPurchaseId(dto.getKeyId());
        mtPfepPurchase = mtPfepPurchaseMapper.selectOne(mtPfepPurchase);
        if (mtPfepPurchase == null || StringUtils.isEmpty(mtPfepPurchase.getPfepPurchaseId())) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            "keyId:" + dto.getKeyId(), "mt_pfep_purchase",
                                            "【API:pfepPurchaseAttrPropertyUpdate】"));
        }

        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_pfep_purchase_attr", dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }
}
