package tarzan.material.domain.trans.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import tarzan.material.domain.entity.MtPfepInventory;
import tarzan.material.domain.trans.MtPfepInventoryTransMapper;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.material.domain.vo.MtPfepInventoryVO11;
import tarzan.material.domain.vo.MtPfepInventoryVO3;

@Component
public class MtPfepInventoryTransMapperImpl implements MtPfepInventoryTransMapper {

    @Override
    public List<MtPfepInventoryVO3> inventoryVOToInventoryVO3List(List<MtPfepInventoryVO> voList) {
        if (voList == null) {
            return null;
        }

        List<MtPfepInventoryVO3> list = new ArrayList<MtPfepInventoryVO3>(voList.size());
        for (MtPfepInventoryVO mtPfepInventoryVO : voList) {
            list.add(mtPfepInventoryVOToMtPfepInventoryVO3(mtPfepInventoryVO));
        }

        return list;
    }

    @Override
    public MtPfepInventoryVO11 inventoryToInventoryVO11(MtPfepInventory vo) {
        if (vo == null) {
            return null;
        }

        MtPfepInventoryVO11 mtPfepInventoryVO2 = new MtPfepInventoryVO11();

        mtPfepInventoryVO2.setIdentifyType(vo.getIdentifyType());
        mtPfepInventoryVO2.setIdentifyId(vo.getIdentifyId());
        mtPfepInventoryVO2.setStockLocatorId(vo.getStockLocatorId());
        mtPfepInventoryVO2.setPackageLength(vo.getPackageLength());
        mtPfepInventoryVO2.setPackageWidth(vo.getPackageWidth());
        mtPfepInventoryVO2.setPackageHeight(vo.getPackageHeight());
        mtPfepInventoryVO2.setPackageSizeUomId(vo.getPackageSizeUomId());
        mtPfepInventoryVO2.setPackageWeight(vo.getPackageWeight());
        mtPfepInventoryVO2.setWeightUomId(vo.getWeightUomId());
        mtPfepInventoryVO2.setMaxStockQty(vo.getMaxStockQty());
        mtPfepInventoryVO2.setMinStockQty(vo.getMinStockQty());
        mtPfepInventoryVO2.setIssuedLocatorId(vo.getIssuedLocatorId());
        mtPfepInventoryVO2.setCompletionLocatorId(vo.getCompletionLocatorId());

        return mtPfepInventoryVO2;
    }

    @Override
    public MtPfepInventoryVO11 inventoryVO3ToInventoryVO11(MtPfepInventoryVO3 vo) {
        if (vo == null) {
            return null;
        }

        MtPfepInventoryVO11 mtPfepInventoryVO2 = new MtPfepInventoryVO11();

        mtPfepInventoryVO2.setIdentifyType(vo.getIdentifyType());
        mtPfepInventoryVO2.setIdentifyId(vo.getIdentifyId());
        mtPfepInventoryVO2.setStockLocatorId(vo.getStockLocatorId());
        mtPfepInventoryVO2.setPackageLength(vo.getPackageLength());
        mtPfepInventoryVO2.setPackageWidth(vo.getPackageWidth());
        mtPfepInventoryVO2.setPackageHeight(vo.getPackageHeight());
        mtPfepInventoryVO2.setPackageSizeUomId(vo.getPackageSizeUomId());
        mtPfepInventoryVO2.setPackageWeight(vo.getPackageWeight());
        mtPfepInventoryVO2.setWeightUomId(vo.getWeightUomId());
        mtPfepInventoryVO2.setMaxStockQty(vo.getMaxStockQty());
        mtPfepInventoryVO2.setMinStockQty(vo.getMinStockQty());
        mtPfepInventoryVO2.setIssuedLocatorId(vo.getIssuedLocatorId());
        mtPfepInventoryVO2.setCompletionLocatorId(vo.getCompletionLocatorId());

        return mtPfepInventoryVO2;
    }

    protected MtPfepInventoryVO3 mtPfepInventoryVOToMtPfepInventoryVO3(MtPfepInventoryVO mtPfepInventoryVO) {
        if (mtPfepInventoryVO == null) {
            return null;
        }

        MtPfepInventoryVO3 mtPfepInventoryVO3 = new MtPfepInventoryVO3();

        mtPfepInventoryVO3.setMaterialId(mtPfepInventoryVO.getMaterialId());
        mtPfepInventoryVO3.setSiteId(mtPfepInventoryVO.getSiteId());
        mtPfepInventoryVO3.setOrganizationType(mtPfepInventoryVO.getOrganizationType());
        mtPfepInventoryVO3.setOrganizationId(mtPfepInventoryVO.getOrganizationId());

        return mtPfepInventoryVO3;
    }
}
