package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.repository.HmeMaterialTransferRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 物料转移资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-05-09 11:08
 */
@Component
public class HmeMaterialTransferRepositoryImpl implements HmeMaterialTransferRepository {
    private static final String MT_MATERIAL_LOT_ATTR = "mt_material_lot_attr";

    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Override
    public MtMaterialLot materialLotPropertyGet(Long tenantId, String materialLotCode) {
        if (StringUtils.isEmpty(materialLotCode)) {
            throw new MtException("MT_MATERIAL_TFR_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0001", HmeConstants.ConstantValue.HME, "materialLotCode"));
        }
        MtMaterialLot materialLot = new MtMaterialLot();
        materialLot.setTenantId(tenantId);
        materialLot.setMaterialLotCode(materialLotCode);
        return mtMaterialLotMapper.selectOne(materialLot);
    }

    @Override
    public void operationLock(Long tenantId, String materialLotCode, String lockFlag) {
        MtMaterialLot materialLot = materialLotPropertyGet(tenantId, materialLotCode);
        List<MtExtendAttrDTO3> materialLotAttrList = new ArrayList<>();
        MtExtendAttrDTO3 materialLotAttr = new MtExtendAttrDTO3();
        materialLotAttr.setAttrName("LOCK_FLAG");
        materialLotAttr.setAttrValue(lockFlag);
        materialLotAttrList.add(materialLotAttr);
        mtExtendSettingsService.attrSave(tenantId, MT_MATERIAL_LOT_ATTR, materialLot.getMaterialLotId(),
                null, materialLotAttrList);
    }

}
