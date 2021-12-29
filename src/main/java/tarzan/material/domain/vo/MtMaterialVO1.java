package tarzan.material.domain.vo;

import java.io.Serializable;

public class MtMaterialVO1 implements Serializable {

    private static final long serialVersionUID = -6404875357278349591L;
    private String materialId;
    private String primaryUomId; // 基本计量单位
    private String primaryUomCode;
    private String primaryUomName;
    private String secondaryUomId; // 辅助单位
    private String secondaryUomCode;
    private String secondaryUomName;
    private Double conversionRate; // 主辅单位转换比例：基本计量单位/辅助单位

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

    public String getPrimaryUomCode() {
        return primaryUomCode;
    }

    public void setPrimaryUomCode(String primaryUomCode) {
        this.primaryUomCode = primaryUomCode;
    }

    public String getPrimaryUomName() {
        return primaryUomName;
    }

    public void setPrimaryUomName(String primaryUomName) {
        this.primaryUomName = primaryUomName;
    }

    public String getSecondaryUomId() {
        return secondaryUomId;
    }

    public void setSecondaryUomId(String secondaryUomId) {
        this.secondaryUomId = secondaryUomId;
    }

    public String getSecondaryUomCode() {
        return secondaryUomCode;
    }

    public void setSecondaryUomCode(String secondaryUomCode) {
        this.secondaryUomCode = secondaryUomCode;
    }

    public String getSecondaryUomName() {
        return secondaryUomName;
    }

    public void setSecondaryUomName(String secondaryUomName) {
        this.secondaryUomName = secondaryUomName;
    }

    public Double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }
}
