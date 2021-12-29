package tarzan.method.domain.vo;

import java.util.List;

import tarzan.method.domain.entity.MtBomHis;

public class MtBomHisVO3 {

    /**
     * 
     */
    private MtBomHis bomHis;
    private List<MtBomComponentHisVO1> bomComponentHis;

    public MtBomHis getBomHis() {
        return bomHis;
    }

    public void setBomHis(MtBomHis bomHis) {
        this.bomHis = bomHis;
    }

    public List<MtBomComponentHisVO1> getBomComponentHis() {
        return bomComponentHis;
    }

    public void setBomComponentHis(List<MtBomComponentHisVO1> bomComponentHis) {
        this.bomComponentHis = bomComponentHis;
    }

}
