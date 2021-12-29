package tarzan.material.domain.trans;

import java.util.List;

import tarzan.material.domain.entity.MtPfepManufacturing;
import tarzan.material.domain.vo.MtPfepManufacturingVO;
import tarzan.material.domain.vo.MtPfepManufacturingVO1;
import tarzan.material.domain.vo.MtPfepManufacturingVO11;

public interface MtPfepManufacturingTransMapper {

    MtPfepManufacturingVO manufacturingToManufacturingVO(MtPfepManufacturing entity);

    MtPfepManufacturingVO manufacturingVO11ToManufacturingVO(MtPfepManufacturingVO11 vo);

    List<MtPfepManufacturingVO11> manufacturingVO1ToManufacturingVO11List(List<MtPfepManufacturingVO1> vo);

}
