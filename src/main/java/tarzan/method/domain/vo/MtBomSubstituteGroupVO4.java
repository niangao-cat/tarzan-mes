package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtBomSubstituteGroupVO4 implements Serializable {

    private static final long serialVersionUID = -296479957392711121L;
    private String bomSubstituteGroupId;
    private String bomComponentId;
    private String substituteGroup;
    private String substitutePolicy;
    private String enableFlag;
    private String copiedFromGroupId;
    private List<MtBomSubstituteVO8> mtBomSubstituteList;

    public String getBomSubstituteGroupId() {
        return bomSubstituteGroupId;
    }

    public void setBomSubstituteGroupId(String bomSubstituteGroupId) {
        this.bomSubstituteGroupId = bomSubstituteGroupId;
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

    public String getCopiedFromGroupId() {
        return copiedFromGroupId;
    }

    public void setCopiedFromGroupId(String copiedFromGroupId) {
        this.copiedFromGroupId = copiedFromGroupId;
    }

    public List<MtBomSubstituteVO8> getMtBomSubstituteList() {
        return mtBomSubstituteList;
    }

    public void setMtBomSubstituteList(List<MtBomSubstituteVO8> mtBomSubstituteList) {
        this.mtBomSubstituteList = mtBomSubstituteList;
    }
}
