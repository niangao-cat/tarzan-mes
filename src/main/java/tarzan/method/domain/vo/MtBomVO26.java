package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class MtBomVO26 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8339828495764093465L;
    @ApiModelProperty(value = "装配清单头ID")
    private String bomId;
    @ApiModelProperty(value = "装配清单基本数量")
    private Double primaryQty;
    @ApiModelProperty(value = "产品需求数量")
    private Double qty;
    @ApiModelProperty(value = "装配清单组件行信息清单")
    private List<MtBomComponentVO27> bomComponentList;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public Double getPrimaryQty() {
        return primaryQty;
    }

    public void setPrimaryQty(Double primaryQty) {
        this.primaryQty = primaryQty;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public List<MtBomComponentVO27> getBomComponentList() {
        return bomComponentList;
    }

    public void setBomComponentList(List<MtBomComponentVO27> bomComponentList) {
        this.bomComponentList = bomComponentList;
    }

}
