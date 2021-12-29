package tarzan.inventory.api.dto;

import java.io.Serializable;

public class MtContainerDTO implements Serializable {
    private static final long serialVersionUID = 5408665587710111829L;

    private String containerId;

    private String targetStatus;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(String targetStatus) {
        this.targetStatus = targetStatus;
    }

    @Override
    public String toString() {
        return "MtContainerDTO{" +
                "containerId='" + containerId + '\'' +
                ", targetStatus='" + targetStatus + '\'' +
                '}';
    }
}
