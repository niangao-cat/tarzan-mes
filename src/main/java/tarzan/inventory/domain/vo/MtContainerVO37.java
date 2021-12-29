package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author : MrZ
 * @date : 2020-02-10 10:41
 **/
public class MtContainerVO37 implements Serializable {
    private static final long serialVersionUID = -4559725138356049053L;
    private String containerId;
    private String loadObjectType;
    private String loadObjectId;
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

    public Double getLoadQty() {
        return loadQty;
    }

    public void setLoadQty(Double loadQty) {
        this.loadQty = loadQty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtContainerVO37 vo12 = (MtContainerVO37) o;
        return Objects.equals(containerId, vo12.containerId) && Objects.equals(loadObjectType, vo12.loadObjectType)
                        && Objects.equals(loadObjectId, vo12.loadObjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerId, loadObjectType, loadObjectId);
    }
}
