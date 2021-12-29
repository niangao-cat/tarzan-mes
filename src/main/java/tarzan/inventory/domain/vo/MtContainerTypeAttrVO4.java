package tarzan.inventory.domain.vo;

import tarzan.inventory.domain.entity.MtContainerType;

/**
 * @author Leeloing
 * @date 2019-10-17 19:50
 */
public class MtContainerTypeAttrVO4 extends MtContainerType {
    private static final long serialVersionUID = -442382600299401596L;
    private String containerId;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }
}
