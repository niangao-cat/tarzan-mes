package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;


public class MtBomComponentVO27 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8696618341191019066L;
    @ApiModelProperty("组件行ID")
    private String bomComponentId;
    @ApiModelProperty("组件行用量")
    private Double bomComponentQty;
    @ApiModelProperty("损耗策略")
    private String attritionPolicy;
    @ApiModelProperty("损耗百分比")
    private Double attritionChance;
    @ApiModelProperty("损耗固定值")
    private Double attritionQty;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Double getBomComponentQty() {
        return bomComponentQty;
    }

    public void setBomComponentQty(Double bomComponentQty) {
        this.bomComponentQty = bomComponentQty;
    }

    public String getAttritionPolicy() {
        return attritionPolicy;
    }

    public void setAttritionPolicy(String attritionPolicy) {
        this.attritionPolicy = attritionPolicy;
    }

    public Double getAttritionChance() {
        return attritionChance;
    }

    public void setAttritionChance(Double attritionChance) {
        this.attritionChance = attritionChance;
    }

    public Double getAttritionQty() {
        return attritionQty;
    }

    public void setAttritionQty(Double attritionQty) {
        this.attritionQty = attritionQty;
    }

}
