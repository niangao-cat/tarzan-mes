package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtContLoadDtlVO10 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1791253871689432590L;
    private String containerId; // 容器
    private String allLevelFlag; // 是否获取所有层级

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

}
