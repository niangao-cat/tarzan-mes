package tarzan.actual.api.dto;

import java.io.Serializable;

public class MtInstructionActualDetailDTO2 implements Serializable {
    private static final long serialVersionUID = -8799281998385021972L;
    private String actualDetailId;
    private String actualId;
    private String materialLotId;
    private String uomId;
    private String containerId;

    public String getActualDetailId() {
        return actualDetailId;
    }

    public void setActualDetailId(String actualDetailId) {
        this.actualDetailId = actualDetailId;
    }

    public String getActualId() {
        return actualId;
    }

    public void setActualId(String actualId) {
        this.actualId = actualId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }
}
