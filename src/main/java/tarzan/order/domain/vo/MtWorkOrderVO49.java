package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月16日 下午2:45:33
 *
 */
public class MtWorkOrderVO49 implements Serializable {

    private static final long serialVersionUID = 3030427426231137663L;

    @ApiModelProperty(value = "替代组件ID")
    private String substituteMaterialId;
    @ApiModelProperty(value = "替代组件编码")
    private String substituteMaterialCode;
    @ApiModelProperty(value = "替代组件描述")
    private String substituteMaterialName;
    @ApiModelProperty(value = "装配数量")
    private Double substituteMaterialAssembleQty;
    @ApiModelProperty(value = "需求数量")
    private Double substituteMaterialComponentQty;

    public String getSubstituteMaterialId() {
        return substituteMaterialId;
    }

    public void setSubstituteMaterialId(String substituteMaterialId) {
        this.substituteMaterialId = substituteMaterialId;
    }

    public String getSubstituteMaterialCode() {
        return substituteMaterialCode;
    }

    public void setSubstituteMaterialCode(String substituteMaterialCode) {
        this.substituteMaterialCode = substituteMaterialCode;
    }

    public String getSubstituteMaterialName() {
        return substituteMaterialName;
    }

    public void setSubstituteMaterialName(String substituteMaterialName) {
        this.substituteMaterialName = substituteMaterialName;
    }

    public Double getSubstituteMaterialAssembleQty() {
        return substituteMaterialAssembleQty;
    }

    public void setSubstituteMaterialAssembleQty(Double substituteMaterialAssembleQty) {
        this.substituteMaterialAssembleQty = substituteMaterialAssembleQty;
    }

    public Double getSubstituteMaterialComponentQty() {
        return substituteMaterialComponentQty;
    }

    public void setSubstituteMaterialComponentQty(Double substituteMaterialComponentQty) {
        this.substituteMaterialComponentQty = substituteMaterialComponentQty;
    }

}
