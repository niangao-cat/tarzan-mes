package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/3/13 16:41
 */
public class MtEoComponentActualVO8 implements Serializable {
    private static final long serialVersionUID = 6954900644723535193L;
    private String bomSubstituteGroupId;
    private String bomSubstituteId;
    private String materialId;
    private Double substituteValue;
    private String substitutePolicy;
    private Double substituteUsage;
    private String bomComponentId;

    public String getBomSubstituteGroupId() {
        return bomSubstituteGroupId;
    }

    public void setBomSubstituteGroupId(String bomSubstituteGroupId) {
        this.bomSubstituteGroupId = bomSubstituteGroupId;
    }

    public String getBomSubstituteId() {
        return bomSubstituteId;
    }

    public void setBomSubstituteId(String bomSubstituteId) {
        this.bomSubstituteId = bomSubstituteId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public Double getSubstituteValue() {
        return substituteValue;
    }

    public void setSubstituteValue(Double substituteValue) {
        this.substituteValue = substituteValue;
    }

    public String getSubstitutePolicy() {
        return substitutePolicy;
    }

    public void setSubstitutePolicy(String substitutePolicy) {
        this.substitutePolicy = substitutePolicy;
    }

    public Double getSubstituteUsage() {
        return substituteUsage;
    }

    public void setSubstituteUsage(Double substituteUsage) {
        this.substituteUsage = substituteUsage;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

}
