package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtContainerVO10 implements Serializable {

    private static final long serialVersionUID = 6466441227652313536L;

    private String containerId;
    private String reservedObjectType;
    private String reservedObjectId;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getReservedObjectType() {
        return reservedObjectType;
    }

    public void setReservedObjectType(String reservedObjectType) {
        this.reservedObjectType = reservedObjectType;
    }

    public String getReservedObjectId() {
        return reservedObjectId;
    }

    public void setReservedObjectId(String reservedObjectId) {
        this.reservedObjectId = reservedObjectId;
    }
}
