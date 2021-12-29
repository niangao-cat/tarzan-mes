package tarzan.inventory.domain.vo;


import java.io.Serializable;
import java.util.List;

import io.tarzan.common.domain.vo.MtExtendVO5;

public class MtContainerTypeAttrVO3 implements Serializable {
    private static final long serialVersionUID = 4716079205193166757L;

    private String containerTypeId;
    private List<MtExtendVO5> attr;

    public String getContainerTypeId() {
        return containerTypeId;
    }

    public void setContainerTypeId(String containerTypeId) {
        this.containerTypeId = containerTypeId;
    }

    public List<MtExtendVO5> getAttr() {
        return attr;
    }

    public void setAttr(List<MtExtendVO5> attr) {
        this.attr = attr;
    }
}
