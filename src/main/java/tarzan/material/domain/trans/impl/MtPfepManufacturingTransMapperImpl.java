package tarzan.material.domain.trans.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import tarzan.material.domain.entity.MtPfepManufacturing;
import tarzan.material.domain.trans.MtPfepManufacturingTransMapper;
import tarzan.material.domain.vo.MtPfepManufacturingVO;
import tarzan.material.domain.vo.MtPfepManufacturingVO1;
import tarzan.material.domain.vo.MtPfepManufacturingVO11;

@Component
public class MtPfepManufacturingTransMapperImpl implements MtPfepManufacturingTransMapper {

    @Override
    public MtPfepManufacturingVO manufacturingToManufacturingVO(MtPfepManufacturing entity) {
        if (entity == null) {
            return null;
        }

        MtPfepManufacturingVO mtPfepManufacturingVO = new MtPfepManufacturingVO();

        mtPfepManufacturingVO.setDefaultBomId(entity.getDefaultBomId());
        mtPfepManufacturingVO.setDefaultRoutingId(entity.getDefaultRoutingId());
        mtPfepManufacturingVO.setIssueControlType(entity.getIssueControlType());
        mtPfepManufacturingVO.setIssueControlQty(entity.getIssueControlQty());
        mtPfepManufacturingVO.setCompleteControlType(entity.getCompleteControlType());
        mtPfepManufacturingVO.setCompleteControlQty(entity.getCompleteControlQty());
        mtPfepManufacturingVO.setAttritionControlType(entity.getAttritionControlType());
        mtPfepManufacturingVO.setAttritionControlQty(entity.getAttritionControlQty());
        mtPfepManufacturingVO.setOperationAssembleFlag(entity.getOperationAssembleFlag());

        return mtPfepManufacturingVO;
    }

    @Override
    public MtPfepManufacturingVO manufacturingVO11ToManufacturingVO(MtPfepManufacturingVO11 vo) {
        if (vo == null) {
            return null;
        }

        MtPfepManufacturingVO mtPfepManufacturingVO = new MtPfepManufacturingVO();

        mtPfepManufacturingVO.setDefaultBomId(vo.getDefaultBomId());
        mtPfepManufacturingVO.setDefaultRoutingId(vo.getDefaultRoutingId());
        mtPfepManufacturingVO.setIssueControlType(vo.getIssueControlType());
        mtPfepManufacturingVO.setIssueControlQty(vo.getIssueControlQty());
        mtPfepManufacturingVO.setCompleteControlType(vo.getCompleteControlType());
        mtPfepManufacturingVO.setCompleteControlQty(vo.getCompleteControlQty());
        mtPfepManufacturingVO.setAttritionControlType(vo.getAttritionControlType());
        mtPfepManufacturingVO.setAttritionControlQty(vo.getAttritionControlQty());
        mtPfepManufacturingVO.setOperationAssembleFlag(vo.getOperationAssembleFlag());

        return mtPfepManufacturingVO;
    }

    @Override
    public List<MtPfepManufacturingVO11> manufacturingVO1ToManufacturingVO11List(List<MtPfepManufacturingVO1> vo) {
        if (vo == null) {
            return null;
        }

        List<MtPfepManufacturingVO11> list = new ArrayList<MtPfepManufacturingVO11>(vo.size());
        for (MtPfepManufacturingVO1 mtPfepManufacturingVO1 : vo) {
            list.add(mtPfepManufacturingVO1ToMtPfepManufacturingVO11(mtPfepManufacturingVO1));
        }

        return list;
    }

    protected MtPfepManufacturingVO11 mtPfepManufacturingVO1ToMtPfepManufacturingVO11(
                    MtPfepManufacturingVO1 mtPfepManufacturingVO1) {
        if (mtPfepManufacturingVO1 == null) {
            return null;
        }

        MtPfepManufacturingVO11 mtPfepManufacturingVO11 = new MtPfepManufacturingVO11();

        mtPfepManufacturingVO11.setMaterialId(mtPfepManufacturingVO1.getMaterialId());
        mtPfepManufacturingVO11.setSiteId(mtPfepManufacturingVO1.getSiteId());
        mtPfepManufacturingVO11.setOrganizationType(mtPfepManufacturingVO1.getOrganizationType());
        mtPfepManufacturingVO11.setOrganizationId(mtPfepManufacturingVO1.getOrganizationId());

        return mtPfepManufacturingVO11;
    }
}
