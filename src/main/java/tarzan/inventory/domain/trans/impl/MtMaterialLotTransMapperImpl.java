package tarzan.inventory.domain.trans.impl;

import org.springframework.stereotype.Component;
import tarzan.inventory.domain.trans.MtMaterialLotTransMapper;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.inventory.domain.vo.MtMaterialLotVO41;

/**
 * @Author: chuang.yang
 * @Date: 2021/7/8 11:12
 * @Description:
 */
@Component
public class MtMaterialLotTransMapperImpl implements MtMaterialLotTransMapper {
    @Override
    public MtMaterialLotVO20 mtMaterialLotVO41ToMtMaterialLotVO20(MtMaterialLotVO41 source) {
        if (source == null) {
            return null;
        }

        MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
        mtMaterialLotVO20.setTrxPrimaryUomQty(source.getTrxPrimaryUomQty());
        mtMaterialLotVO20.setTrxSecondaryUomQty(source.getTrxSecondaryUomQty());
        mtMaterialLotVO20.setMaterialLotId(source.getMaterialLotId());
        mtMaterialLotVO20.setMaterialLotCode(source.getMaterialLotCode());
        mtMaterialLotVO20.setSiteId(source.getSiteId());
        mtMaterialLotVO20.setEnableFlag(source.getEnableFlag());
        mtMaterialLotVO20.setQualityStatus(source.getQualityStatus());
        mtMaterialLotVO20.setMaterialId(source.getMaterialId());
        mtMaterialLotVO20.setPrimaryUomId(source.getPrimaryUomId());
        mtMaterialLotVO20.setSecondaryUomId(source.getSecondaryUomId());
        mtMaterialLotVO20.setLocatorId(source.getLocatorId());
        mtMaterialLotVO20.setAssemblePointId(source.getAssemblePointId());
        mtMaterialLotVO20.setLoadTime(source.getLoadTime());
        mtMaterialLotVO20.setUnloadTime(source.getUnloadTime());
        mtMaterialLotVO20.setOwnerType(source.getOwnerType());
        mtMaterialLotVO20.setOwnerId(source.getOwnerId());
        mtMaterialLotVO20.setLot(source.getLot());
        mtMaterialLotVO20.setOvenNumber(source.getOvenNumber());
        mtMaterialLotVO20.setSupplierId(source.getSupplierId());
        mtMaterialLotVO20.setSupplierSiteId(source.getSupplierSiteId());
        mtMaterialLotVO20.setCustomerId(source.getCustomerId());
        mtMaterialLotVO20.setCustomerSiteId(source.getCustomerSiteId());
        mtMaterialLotVO20.setCreateReason(source.getCreateReason());
        mtMaterialLotVO20.setIdentification(source.getIdentification());
        mtMaterialLotVO20.setEoId(source.getEoId());
        mtMaterialLotVO20.setInLocatorTime(source.getInLocatorTime());
        mtMaterialLotVO20.setFreezeFlag(source.getFreezeFlag());
        mtMaterialLotVO20.setInSiteTime(source.getInSiteTime());
        return mtMaterialLotVO20;
    }
}
