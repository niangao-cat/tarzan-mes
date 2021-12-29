package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomSubstituteGroupVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4802847688468662245L;
    private String bomId;
    private String bomComponentId;
    private String substituteGroup;
    private String substitutePolicy;
    private String enableFlag;
    private String onlyAvailableFlag;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getSubstituteGroup() {
        return substituteGroup;
    }

    public void setSubstituteGroup(String substituteGroup) {
        this.substituteGroup = substituteGroup;
    }

    public String getSubstitutePolicy() {
        return substitutePolicy;
    }

    public void setSubstitutePolicy(String substitutePolicy) {
        this.substitutePolicy = substitutePolicy;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getOnlyAvailableFlag() {
        return onlyAvailableFlag;
    }

    public void setOnlyAvailableFlag(String onlyAvailableFlag) {
        this.onlyAvailableFlag = onlyAvailableFlag;
    }

}
