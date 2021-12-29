package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月16日 下午2:45:33
 *
 */
public class MtWorkOrderVO44 implements Serializable {
    
    private static final long serialVersionUID = 4150227972565705601L;
    
    @ApiModelProperty(value = "替代组ID")
    private String bomSubstituteGroupId;
    @ApiModelProperty(value = "替代组")
    private String substituteGroup;
    @ApiModelProperty(value = "替代策略")
    private String substitutePolicy;
    @ApiModelProperty(value = "替代策略描述")
    private String substitutePolicyDesc;
    @ApiModelProperty(value = "装配清单组件行替代项属性ID")
    private String bomSubstituteId;
    @ApiModelProperty(value = "替代物料ID")
    private String materialId;
    @ApiModelProperty(value = "替代物料编码")
    private String materialCode;
    @ApiModelProperty(value = "替代物料名称")
    private String materialName;
    @ApiModelProperty(value = "替代值")
    private Double substituteValue;
    @ApiModelProperty(value = "替代用量")
    private Double substituteUsage;
    @ApiModelProperty(value = "耗用数量")
    private Double assembleQty;
    
    public String getBomSubstituteGroupId() {
        return bomSubstituteGroupId;
    }
    public void setBomSubstituteGroupId(String bomSubstituteGroupId) {
        this.bomSubstituteGroupId = bomSubstituteGroupId;
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
    public String getSubstitutePolicyDesc() {
        return substitutePolicyDesc;
    }
    public void setSubstitutePolicyDesc(String substitutePolicyDesc) {
        this.substitutePolicyDesc = substitutePolicyDesc;
    }

}
