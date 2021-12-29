package tarzan.method.domain.vo;

import java.util.List;

import tarzan.method.domain.entity.MtBomHis;

public class MtBomHisVO2 extends MtBomHis {

    private List<MtBomComponentHisVO1> bomComponentHisList;

    public List<MtBomComponentHisVO1> getBomComponentHisList() {
        return bomComponentHisList;
    }

    public void setBomComponentHisList(List<MtBomComponentHisVO1> bomComponentHisList) {
        this.bomComponentHisList = bomComponentHisList;
    }
}
