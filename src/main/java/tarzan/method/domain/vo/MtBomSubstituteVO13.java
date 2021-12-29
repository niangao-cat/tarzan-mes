package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2020/1/8 19:02
 * @Author: ${yiyang.xie}
 */
public class MtBomSubstituteVO13 implements Serializable {
    private static final long serialVersionUID = 2586002963908791556L;
    @ApiModelProperty("替代料的物料ID")
    private String substituteMaterialId;
    @ApiModelProperty("对应主料用量")
    private Double componentQty;
    @ApiModelProperty("替代物料对应替代策略")
    private String substitutePolicy;

    public String getSubstituteMaterialId() {
        return substituteMaterialId;
    }

    public void setSubstituteMaterialId(String substituteMaterialId) {
        this.substituteMaterialId = substituteMaterialId;
    }

    public Double getComponentQty() {
        return componentQty;
    }

    public void setComponentQty(Double componentQty) {
        this.componentQty = componentQty;
    }

    public String getSubstitutePolicy() {
        return substitutePolicy;
    }

    public void setSubstitutePolicy(String substitutePolicy) {
        this.substitutePolicy = substitutePolicy;
    }
}