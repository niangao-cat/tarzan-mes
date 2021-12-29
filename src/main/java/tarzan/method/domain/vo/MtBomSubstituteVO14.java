package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/4/7 3:35 下午
 */
public class MtBomSubstituteVO14 implements Serializable {
    private static final long serialVersionUID = -6566581877609727095L;
    @ApiModelProperty("装配清单组件行ID")
    private String bomComponentId;
    @ApiModelProperty("组件行物料ID")
    private String bomComponentMaterialId;
    @ApiModelProperty("组件行需求数量")
    private Double qty;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomComponentMaterialId() {
        return bomComponentMaterialId;
    }

    public void setBomComponentMaterialId(String bomComponentMaterialId) {
        this.bomComponentMaterialId = bomComponentMaterialId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

}
