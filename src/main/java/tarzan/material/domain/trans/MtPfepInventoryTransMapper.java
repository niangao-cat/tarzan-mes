package tarzan.material.domain.trans;

import java.util.List;

import tarzan.material.domain.entity.MtPfepInventory;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.material.domain.vo.MtPfepInventoryVO11;
import tarzan.material.domain.vo.MtPfepInventoryVO3;

public interface MtPfepInventoryTransMapper {

    List<MtPfepInventoryVO3> inventoryVOToInventoryVO3List(List<MtPfepInventoryVO> voList);

    MtPfepInventoryVO11 inventoryToInventoryVO11(MtPfepInventory vo);

    MtPfepInventoryVO11 inventoryVO3ToInventoryVO11(MtPfepInventoryVO3 vo);

}
