package tarzan.inventory.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/4/3 11:16
 */
public class MtContainerVO2 implements Serializable {
    private static final long serialVersionUID = -7243162469676996072L;

    private String containerId;
    private String loadObjectType;
    private String loadObjectId;
    private String allLevelFlag;
    private Double loadQty;

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

    public String getAllLevelFlag() {
        return allLevelFlag;
    }

    public void setAllLevelFlag(String allLevelFlag) {
        this.allLevelFlag = allLevelFlag;
    }

    public Double getLoadQty() {
        return loadQty;
    }

    public void setLoadQty(Double loadQty) {
        this.loadQty = loadQty;
    }
}
