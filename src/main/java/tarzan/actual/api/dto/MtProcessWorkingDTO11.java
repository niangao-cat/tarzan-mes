package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/13 3:13 下午
 */
public class MtProcessWorkingDTO11 implements Serializable {
    private static final long serialVersionUID = 415681053297842220L;
    @ApiModelProperty("替代组")
    private String substituteGroup;

    @ApiModelProperty("替代策略")
    private String substitutePolicy;

    @ApiModelProperty("替代策略描述")
    private String substitutePolicyDesc;

    @ApiModelProperty("替代物料ID")
    private String materialId;
    @ApiModelProperty("替代物料编码")
    private String materialCode;
    @ApiModelProperty("替代物料名")
    private String materialName;

    @ApiModelProperty("替代值")
    private Double substituteValue;
    @ApiModelProperty("替代用量")
    private Double substituteUsage;

    @ApiModelProperty("替代料装配数量")
    private Double assembleQty;
    @ApiModelProperty("替代料报废数量")
    private Double scrappedQty;

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

    public String getSubstitutePolicyDesc() {
        return substitutePolicyDesc;
    }

    public void setSubstitutePolicyDesc(String substitutePolicyDesc) {
        this.substitutePolicyDesc = substitutePolicyDesc;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
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

    public Double getAssembleQty() {
        return assembleQty;
    }

    public void setAssembleQty(Double assembleQty) {
        this.assembleQty = assembleQty;
    }

    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }
}
