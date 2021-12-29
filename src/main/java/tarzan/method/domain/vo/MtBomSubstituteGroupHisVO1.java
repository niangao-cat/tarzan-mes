package tarzan.method.domain.vo;

import java.util.List;

import tarzan.method.domain.entity.MtBomSubstituteGroupHis;
import tarzan.method.domain.entity.MtBomSubstituteHis;

public class MtBomSubstituteGroupHisVO1 extends MtBomSubstituteGroupHis {

    private List<MtBomSubstituteHis> bomSubstituteHisList;

    public List<MtBomSubstituteHis> getBomSubstituteHisList() {
        return bomSubstituteHisList;
    }

    public void setBomSubstituteHisList(List<MtBomSubstituteHis> bomSubstituteHisList) {
        this.bomSubstituteHisList = bomSubstituteHisList;
    }
}
