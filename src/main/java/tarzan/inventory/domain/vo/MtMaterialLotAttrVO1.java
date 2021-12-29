package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.tarzan.common.domain.vo.MtExtendAttrVO;

public class MtMaterialLotAttrVO1 implements Serializable {

    private static final long serialVersionUID = 3585845946011438606L;
    private String materialLotId;
    private List<MtExtendAttrVO> attr;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public List<MtExtendAttrVO> getAttr() {
        return attr;
    }

    public void setAttr(List<MtExtendAttrVO> attr) {
        this.attr = attr;
    }
}
