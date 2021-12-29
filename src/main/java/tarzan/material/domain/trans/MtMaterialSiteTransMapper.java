package tarzan.material.domain.trans;

import java.util.List;

import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.vo.MtMaterialSiteVO4;

public interface MtMaterialSiteTransMapper {

    List<MtMaterialSiteVO4> materialSiteToMaterialSiteVO4List(List<MtMaterialSite> vo);
}
