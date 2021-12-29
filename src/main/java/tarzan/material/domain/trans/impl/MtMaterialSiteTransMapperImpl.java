package tarzan.material.domain.trans.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.trans.MtMaterialSiteTransMapper;
import tarzan.material.domain.vo.MtMaterialSiteVO4;

@Component
public class MtMaterialSiteTransMapperImpl implements MtMaterialSiteTransMapper {

    @Override
    public List<MtMaterialSiteVO4> materialSiteToMaterialSiteVO4List(List<MtMaterialSite> vo) {
        if (vo == null) {
            return null;
        }

        List<MtMaterialSiteVO4> list = new ArrayList<MtMaterialSiteVO4>(vo.size());
        for (MtMaterialSite mtMaterialSite : vo) {
            list.add(mtMaterialSiteToMtMaterialSiteVO4(mtMaterialSite));
        }

        return list;
    }

    protected MtMaterialSiteVO4 mtMaterialSiteToMtMaterialSiteVO4(MtMaterialSite mtMaterialSite) {
        if (mtMaterialSite == null) {
            return null;
        }

        MtMaterialSiteVO4 mtMaterialSiteVO4 = new MtMaterialSiteVO4();

        mtMaterialSiteVO4.setMaterialSiteId(mtMaterialSite.getMaterialSiteId());
        mtMaterialSiteVO4.setMaterialId(mtMaterialSite.getMaterialId());
        mtMaterialSiteVO4.setSiteId(mtMaterialSite.getSiteId());
        mtMaterialSiteVO4.setSourceIdentificationId(mtMaterialSite.getSourceIdentificationId());
        mtMaterialSiteVO4.setEnableFlag(mtMaterialSite.getEnableFlag());

        return mtMaterialSiteVO4;
    }
}
