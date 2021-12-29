package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomComponentVO11 implements Serializable {

    private static final long serialVersionUID = -6422648554939327684L;

    private String bomComponentId;

    private Double qty;

    private String substituteFlag;

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

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

}
