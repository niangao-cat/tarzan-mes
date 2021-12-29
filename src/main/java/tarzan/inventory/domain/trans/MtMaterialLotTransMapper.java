package tarzan.inventory.domain.trans;

import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.inventory.domain.vo.MtMaterialLotVO41;

/**
 * @Author: chuang.yang
 * @Date: 2021/7/8 11:11
 * @Description:
 */
public interface MtMaterialLotTransMapper {

    MtMaterialLotVO20 mtMaterialLotVO41ToMtMaterialLotVO20(MtMaterialLotVO41 source);
}
