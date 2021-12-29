package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomSubstituteVO6 implements Serializable {

    private static final long serialVersionUID = 3637272742535095428L;
    private String bomComponentId;
    private Double qty;
    private String substituteFlag;
    private String materialId;

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

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

}
