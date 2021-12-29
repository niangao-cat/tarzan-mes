package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2020/1/6 17:40
 * @Author: ${yiyang.xie}
 */
public class MtBomSubstituteVO11 implements Serializable {
    private static final long serialVersionUID = -1081155696121885541L;
    @ApiModelProperty("主料所在的组件行ID")
    private String bomComponentId;
    @ApiModelProperty("替代料的数量")
    private Double qty;
    @ApiModelProperty("替代料的物料ID")
    private String substituteMaterialId;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getSubstituteMaterialId() {
        return substituteMaterialId;
    }

    public void setSubstituteMaterialId(String substituteMaterialId) {
        this.substituteMaterialId = substituteMaterialId;
    }
}
