package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/4/3 9:45 上午
 */
public class MtBomComponentVO23 implements Serializable {

    private static final long serialVersionUID = 7362138350150430051L;
    @ApiModelProperty("组件行对应物料Id")
    private String materialId;

    @ApiModelProperty("虚拟件对应的组件的需求数量")
    private Double qty;

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

}
