package tarzan.method.api.dto;

import java.io.Serializable;

public class MtBomSubstituteGroupDTO implements Serializable {


    private static final long serialVersionUID = -569009510787581293L;
    private String bomSubstituteGroupId;// 替代组属性ID
    private String bomComponentId;// BOM组件行ID
    private String substituteGroup;// 替代组
    private String substitutePolicy;// 替代策略
    private String enableFlag;// 是否有效

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtBomSubstituteDTO2 [bomSubstituteGroupId=");
        builder.append(bomSubstituteGroupId);
        builder.append(", bomComponentId=");
        builder.append(bomComponentId);
        builder.append(", substituteGroup=");
        builder.append(substituteGroup);
        builder.append(", substitutePolicy=");
        builder.append(substitutePolicy);
        builder.append(", enableFlag=");
        builder.append(enableFlag);
        builder.append("]");
        return builder.toString();
    }

}
