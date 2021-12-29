package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtMaterialLotAttrVO2 implements Serializable {
    private static final long serialVersionUID = 8445950546671158354L;
    private String materialLotId;
    private String attrName;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

}
