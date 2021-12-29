package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtContLoadDtlVO2 implements Serializable {
    private static final long serialVersionUID = 1760254603517518819L;

    private String containerId; // 容器
    private String allLevelFlag; // 是否获取所有层级
    private String materialId; // 物料id

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getAllLevelFlag() {
        return allLevelFlag;
    }

    public void setAllLevelFlag(String allLevelFlag) {
        this.allLevelFlag = allLevelFlag;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }
}
