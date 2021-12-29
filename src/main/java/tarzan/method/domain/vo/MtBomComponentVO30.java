package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtBomComponentVO30 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5322707168645676864L;
    private String attritionFlag;
    private String bomId;
    private Double primaryQty;
    private Double qty;
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

    public String getAttritionFlag() {
        return attritionFlag;
    }

    public void setAttritionFlag(String attritionFlag) {
        this.attritionFlag = attritionFlag;
    }

}
