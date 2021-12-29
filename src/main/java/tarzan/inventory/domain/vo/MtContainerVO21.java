package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtContainerVO21 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3185858702510916351L;
    private String containerId;
    private String loadObjectType;
    private String loadObjectId;
    
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
    public String getLoadObjectId() {
        return loadObjectId;
    }
    public void setLoadObjectId(String loadObjectId) {
        this.loadObjectId = loadObjectId;
    }

    
}
