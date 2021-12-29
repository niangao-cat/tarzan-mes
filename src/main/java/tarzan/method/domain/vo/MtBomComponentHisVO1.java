package tarzan.method.domain.vo;

import java.util.List;

import tarzan.method.domain.entity.MtBomComponentHis;
import tarzan.method.domain.entity.MtBomReferencePointHis;

public class MtBomComponentHisVO1 extends MtBomComponentHis {

    private List<MtBomReferencePointHis> bomReferencePointHisList;

    private List<MtBomSubstituteGroupHisVO1> bomSubstituteGroupHisList;

    public List<MtBomReferencePointHis> getBomReferencePointHisList() {
        return bomReferencePointHisList;
    }

    public void setBomReferencePointHisList(List<MtBomReferencePointHis> bomReferencePointHisList) {
        this.bomReferencePointHisList = bomReferencePointHisList;
    }

    public List<MtBomSubstituteGroupHisVO1> getBomSubstituteGroupHisList() {
        return bomSubstituteGroupHisList;
    }

    public void setBomSubstituteGroupHisList(List<MtBomSubstituteGroupHisVO1> bomSubstituteGroupHisList) {
        this.bomSubstituteGroupHisList = bomSubstituteGroupHisList;
    }
}
