package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtContLoadDtlVO7 implements Serializable {

    private static final long serialVersionUID = -8631831707762895160L;
    private String materialId; // 物料
    private String primaryUomId; // 主单位
    private Double primaryUomQty; // 单位数量
    private String secondaryUomId; // 辅助单位
    private Double secondaryUomQty; // 辅助单位数量

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getPrimaryUomId() {
        return primaryUomId;
    }

    public void setPrimaryUomId(String primaryUomId) {
        this.primaryUomId = primaryUomId;
    }

    public Double getPrimaryUomQty() {
        return primaryUomQty;
    }

    public void setPrimaryUomQty(Double primaryUomQty) {
        this.primaryUomQty = primaryUomQty;
    }

    public String getSecondaryUomId() {
        return secondaryUomId;
    }

    public void setSecondaryUomId(String secondaryUomId) {
        this.secondaryUomId = secondaryUomId;
    }

    public Double getSecondaryUomQty() {
        return secondaryUomQty;
    }

    public void setSecondaryUomQty(Double secondaryUomQty) {
        this.secondaryUomQty = secondaryUomQty;
    }
}
