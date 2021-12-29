package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtContainerTypeAttrVO1 implements Serializable {

    private static final long serialVersionUID = -8041335058534630976L;
    private String containerTypeId;
    private String attrName;

    public String getContainerTypeId() {
        return containerTypeId;
    }

    public void setContainerTypeId(String containerTypeId) {
        this.containerTypeId = containerTypeId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }
}
