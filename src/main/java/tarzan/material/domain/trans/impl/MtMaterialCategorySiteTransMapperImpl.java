package tarzan.material.domain.trans.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import tarzan.material.domain.entity.MtMaterialCategorySite;
import tarzan.material.domain.trans.MtMaterialCategorySiteTransMapper;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO7;

@Component
public class MtMaterialCategorySiteTransMapperImpl implements MtMaterialCategorySiteTransMapper {

    @Override
    public List<MtMaterialCategorySiteVO7> materialCategorySiteToMaterialCategorySiteVO4List(
                    List<MtMaterialCategorySite> vo) {
        if (vo == null) {
            return null;
        }

        List<MtMaterialCategorySiteVO7> list = new ArrayList<MtMaterialCategorySiteVO7>(vo.size());
        for (MtMaterialCategorySite mtMaterialCategorySite : vo) {
            list.add(mtMaterialCategorySiteToMtMaterialCategorySiteVO7(mtMaterialCategorySite));
        }

        return list;
    }

    protected MtMaterialCategorySiteVO7 mtMaterialCategorySiteToMtMaterialCategorySiteVO7(
                    MtMaterialCategorySite mtMaterialCategorySite) {
        if (mtMaterialCategorySite == null) {
            return null;
        }

        MtMaterialCategorySiteVO7 mtMaterialCategorySiteVO7 = new MtMaterialCategorySiteVO7();

        mtMaterialCategorySiteVO7.setMaterialCategorySiteId(mtMaterialCategorySite.getMaterialCategorySiteId());
        mtMaterialCategorySiteVO7.setMaterialCategoryId(mtMaterialCategorySite.getMaterialCategoryId());
        mtMaterialCategorySiteVO7.setSiteId(mtMaterialCategorySite.getSiteId());

        return mtMaterialCategorySiteVO7;
    }
}
