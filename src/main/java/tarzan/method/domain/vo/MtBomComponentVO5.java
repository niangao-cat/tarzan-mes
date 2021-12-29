package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomComponentVO5 implements Serializable {

    private static final long serialVersionUID = 8728015244609173489L;
    private String bomId;
    private Double qty;
    private String substituteFlag;
    private String attritionFlag;
    private String bomComponentId;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    public String getAttritionFlag() {
        return attritionFlag;
    }

    public void setAttritionFlag(String attritionFlag) {
        this.attritionFlag = attritionFlag;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }
}
