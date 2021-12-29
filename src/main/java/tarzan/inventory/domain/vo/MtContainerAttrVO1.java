package tarzan.inventory.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/4/17 13:57
 * @Description:
 */
public class MtContainerAttrVO1 implements Serializable {
    private static final long serialVersionUID = -3766444684722346096L;

    private String containerId;
    private String attrName;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }
}
