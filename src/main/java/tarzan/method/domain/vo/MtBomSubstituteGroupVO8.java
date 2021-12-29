package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/4/7 4:15 下午
 */
public class MtBomSubstituteGroupVO8 implements Serializable {
    private static final long serialVersionUID = -2095737710369873879L;
    @ApiModelProperty(value = "替代项ID")
    private String bomSubstituteId;
    @ApiModelProperty(value = "替代物料ID")
    private String materialId;
    @ApiModelProperty(value = "替代值")
    private Double substituteValue;
    @ApiModelProperty(value = "替代单位用量")
    private Double substituteUsage;

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

    public Double getSubstituteUsage() {
        return substituteUsage;
    }

    public void setSubstituteUsage(Double substituteUsage) {
        this.substituteUsage = substituteUsage;
    }
}
