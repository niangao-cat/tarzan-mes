package tarzan.inventory.domain.trans.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import tarzan.inventory.domain.entity.MtInvJournal;
import tarzan.inventory.domain.entity.MtInvOnhandQuantity;
import tarzan.inventory.domain.trans.MtInvOnhandQuantityTransferMapper;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO13;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO17;

@Component
public class MtInvOnhandQuantityTransferMapperImpl implements MtInvOnhandQuantityTransferMapper {

    @Override
    public MtInvOnhandQuantityVO invOnhandQuantityVO13ToInvOnhandQuantityVO(
                    MtInvOnhandQuantityVO13 invOnhandQuantityVO13) {
        if (invOnhandQuantityVO13 == null) {
            return null;
        }

        MtInvOnhandQuantityVO mtInvOnhandQuantityVO = new MtInvOnhandQuantityVO();

        mtInvOnhandQuantityVO.setSiteId(invOnhandQuantityVO13.getSiteId());
        mtInvOnhandQuantityVO.setMaterialId(invOnhandQuantityVO13.getMaterialId());
        mtInvOnhandQuantityVO.setLocatorId(invOnhandQuantityVO13.getLocatorId());
        mtInvOnhandQuantityVO.setLotCode(invOnhandQuantityVO13.getLotCode());
        mtInvOnhandQuantityVO.setOwnerType(invOnhandQuantityVO13.getOwnerType());
        mtInvOnhandQuantityVO.setOwnerId(invOnhandQuantityVO13.getOwnerId());

        return mtInvOnhandQuantityVO;
    }

    @Override
    public MtInvOnhandQuantityVO invOnhandQuantityToInvOnhandQuantityVO(MtInvOnhandQuantity invOnhandQuantity) {
        if (invOnhandQuantity == null) {
            return null;
        }

        MtInvOnhandQuantityVO mtInvOnhandQuantityVO = new MtInvOnhandQuantityVO();

        mtInvOnhandQuantityVO.setSiteId(invOnhandQuantity.getSiteId());
        mtInvOnhandQuantityVO.setMaterialId(invOnhandQuantity.getMaterialId());
        mtInvOnhandQuantityVO.setLocatorId(invOnhandQuantity.getLocatorId());
        mtInvOnhandQuantityVO.setLotCode(invOnhandQuantity.getLotCode());
        mtInvOnhandQuantityVO.setOwnerType(invOnhandQuantity.getOwnerType());
        mtInvOnhandQuantityVO.setOwnerId(invOnhandQuantity.getOwnerId());

        return mtInvOnhandQuantityVO;
    }

    @Override
    public MtInvJournal invOnhandQuantityToInvJournal(MtInvOnhandQuantity invOnhandQuantity) {
        if (invOnhandQuantity == null) {
            return null;
        }

        MtInvJournal mtInvJournal = new MtInvJournal();

        mtInvJournal.setCreationDate(invOnhandQuantity.getCreationDate());
        mtInvJournal.setCreatedBy(invOnhandQuantity.getCreatedBy());
        mtInvJournal.setLastUpdateDate(invOnhandQuantity.getLastUpdateDate());
        mtInvJournal.setLastUpdatedBy(invOnhandQuantity.getLastUpdatedBy());
        mtInvJournal.setObjectVersionNumber(invOnhandQuantity.getObjectVersionNumber());
        mtInvJournal.setTableId(invOnhandQuantity.getTableId());
        mtInvJournal.set_token(invOnhandQuantity.get_token());
        Map<String, Object> map = invOnhandQuantity.getFlex();
        if (map != null) {
            mtInvJournal.setFlex(new HashMap<String, Object>(map));
        }
        mtInvJournal.setTenantId(invOnhandQuantity.getTenantId());
        mtInvJournal.setSiteId(invOnhandQuantity.getSiteId());
        mtInvJournal.setMaterialId(invOnhandQuantity.getMaterialId());
        mtInvJournal.setLocatorId(invOnhandQuantity.getLocatorId());
        mtInvJournal.setOnhandQuantity(invOnhandQuantity.getOnhandQuantity());
        mtInvJournal.setLotCode(invOnhandQuantity.getLotCode());
        mtInvJournal.setOwnerType(invOnhandQuantity.getOwnerType());
        mtInvJournal.setOwnerId(invOnhandQuantity.getOwnerId());
        mtInvJournal.setCid(invOnhandQuantity.getCid());
        if (mtInvJournal.get_innerMap() != null) {
            Map<String, Object> map1 = invOnhandQuantity.get_innerMap();
            if (map1 != null) {
                mtInvJournal.get_innerMap().putAll(map1);
            }
        }
        if (mtInvJournal.get_tls() != null) {
            Map<String, Map<String, String>> map2 = invOnhandQuantity.get_tls();
            if (map2 != null) {
                mtInvJournal.get_tls().putAll(map2);
            }
        }

        return mtInvJournal;
    }

    @Override
    public MtInvOnhandQuantity invOnhandQuantityVOToInvOnhandQuantity(MtInvOnhandQuantityVO invOnhandQuantityVO) {
        if (invOnhandQuantityVO == null) {
            return null;
        }

        MtInvOnhandQuantity mtInvOnhandQuantity = new MtInvOnhandQuantity();

        mtInvOnhandQuantity.setSiteId(invOnhandQuantityVO.getSiteId());
        mtInvOnhandQuantity.setMaterialId(invOnhandQuantityVO.getMaterialId());
        mtInvOnhandQuantity.setLocatorId(invOnhandQuantityVO.getLocatorId());
        mtInvOnhandQuantity.setLotCode(invOnhandQuantityVO.getLotCode());
        mtInvOnhandQuantity.setOwnerType(invOnhandQuantityVO.getOwnerType());
        mtInvOnhandQuantity.setOwnerId(invOnhandQuantityVO.getOwnerId());

        return mtInvOnhandQuantity;
    }

    @Override
    public MtInvOnhandQuantityVO17 invOnhandQuantityVO13ToInvOnhandQuantityVO17(
                    MtInvOnhandQuantityVO13 invOnhandQuantityVO13) {
        if (invOnhandQuantityVO13 == null) {
            return null;
        }

        MtInvOnhandQuantityVO17 mtInvOnhandQuantityVO17 = new MtInvOnhandQuantityVO17();

        mtInvOnhandQuantityVO17.setActualLocatorId(invOnhandQuantityVO13.getLocatorId());
        mtInvOnhandQuantityVO17.setActualChangeQuantity(invOnhandQuantityVO13.getChangeQuantity());
        mtInvOnhandQuantityVO17.setSiteId(invOnhandQuantityVO13.getSiteId());
        mtInvOnhandQuantityVO17.setMaterialId(invOnhandQuantityVO13.getMaterialId());
        mtInvOnhandQuantityVO17.setLotCode(invOnhandQuantityVO13.getLotCode());
        mtInvOnhandQuantityVO17.setOwnerType(invOnhandQuantityVO13.getOwnerType());
        mtInvOnhandQuantityVO17.setOwnerId(invOnhandQuantityVO13.getOwnerId());

        return mtInvOnhandQuantityVO17;
    }

    @Override
    public MtInvOnhandQuantityVO13 invOnhandQuantityVO17ToInvOnhandQuantityVO13(
                    MtInvOnhandQuantityVO17 invOnhandQuantityVO17) {
        if (invOnhandQuantityVO17 == null) {
            return null;
        }

        MtInvOnhandQuantityVO13 mtInvOnhandQuantityVO13 = new MtInvOnhandQuantityVO13();

        mtInvOnhandQuantityVO13.setChangeQuantity(invOnhandQuantityVO17.getActualChangeQuantity());
        mtInvOnhandQuantityVO13.setLocatorId(invOnhandQuantityVO17.getActualLocatorId());
        mtInvOnhandQuantityVO13.setSiteId(invOnhandQuantityVO17.getSiteId());
        mtInvOnhandQuantityVO13.setMaterialId(invOnhandQuantityVO17.getMaterialId());
        mtInvOnhandQuantityVO13.setLotCode(invOnhandQuantityVO17.getLotCode());
        mtInvOnhandQuantityVO13.setOwnerType(invOnhandQuantityVO17.getOwnerType());
        mtInvOnhandQuantityVO13.setOwnerId(invOnhandQuantityVO17.getOwnerId());

        return mtInvOnhandQuantityVO13;
    }
}
