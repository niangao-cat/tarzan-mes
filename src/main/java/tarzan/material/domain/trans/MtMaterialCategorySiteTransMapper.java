package tarzan.material.domain.trans;

import java.util.List;

import tarzan.material.domain.entity.MtMaterialCategorySite;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO7;

public interface MtMaterialCategorySiteTransMapper {

    List<MtMaterialCategorySiteVO7> materialCategorySiteToMaterialCategorySiteVO4List(List<MtMaterialCategorySite> vo);

}
