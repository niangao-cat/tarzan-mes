package tarzan.inventory.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/4/3 12:23
 */
public class MtContainerVO3 implements Serializable {

    private static final long serialVersionUID = -1865961869703155655L;
    private String containerId;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getLoadObjectType() {
        return loadObjectType;
    }

    public void setLoadObjectType(String loadObjectType) {
        this.loadObjectType = loadObjectType;
    }

    private String loadObjectType;
}
